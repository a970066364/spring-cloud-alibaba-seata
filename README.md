



 alibaba seata 官网 https://seata.io	</br>

目前使用的是 seata 1.1.0版本，官方最新版本。2020年02月20日发布的		</br>
版本列表：https://github.com/seata/seata/releases		</br>


![图片1](https://github.com/a970066364/spring-cloud-alibaba-seata/blob/master/md-img/1.png)		</br>

下载seata-server-1.1.0服务端 ：		</br>

下载地址：https://github.com/seata/seata/releases/download/v1.1.0/seata-server-1.1.0.zip		</br>

下载seata-server-0.0.9服务端 ：(1.1.0缺少一些配置需要从0.9版本里找)		</br>

下载地址：https://github.com/seata/seata/releases/download/v0.9.0/seata-server-0.9.0.zip



下载完后，配置seata服务端		</br>



1.1.0版本下的conf文件夹		</br>

![图片](https://github.com/a970066364/spring-cloud-alibaba-seata/blob/master/md-img/2.png)		</br>



0.0.9版本下的conf文件夹		</br>

![图片](https://github.com/a970066364/spring-cloud-alibaba-seata/blob/master/md-img/3.png)		</br>



你自己的解压路径\seata-server-1.1.0\seata\conf	</br>

registry.conf	# 用于注册配置的方式	</br>

file.conf 		#seata的配置，我采用nacos配置中心，所以该文件已经用不到了，但建议看看。	</br>

一、file.conf的seata服务端配置环境	</br>

	采用nacos配置中心虽然用不到file.conf文件，但我们需要将文件中配置导入到nacosfile.conf在1.1.0的版本中缺失了很多，我们可以用0.0.9版本的file.conf来参考。	

我们需要修改的地方：	</br>

service {		</br>
  ...省略		</br>
  vgroup_mapping.my_test_tx_group = "default"	</br>
  ...省略		</br>
}		</br>

#该节点是指事务组的名称my_test_tx_group ，名字更改但项目中也要指定相同的事务组名称。		</br>

vgroup_mapping.my_test_tx_group = "default"		</br>

store {		</br>
mode = "db"		</br>
  db {		</br>
    db-type = "mysql"		</br>
    driver-class-name = "com.mysql.jdbc.Driver"		</br>
    url = "jdbc:mysql://127.0.0.1:3306/seata"		</br>
    user = "mysql"		</br>
    password = "mysql"		</br>
    global.table = "global_table"		</br>
    branch.table = "branch_table"		</br>
    lock-table = "lock_table"		</br>
 		  ...		</br>
  }		</br>
  ...		</br>
｝		</br>



#我们修改数据存储的方式改为db		</br>
mode = "db" # 默认是file的方式		</br>

#然后我们需要配置DB连接池以及手动创建该库seata，自行修正相关连接池信息		</br>
url = "jdbc:mysql://127.0.0.1:3306/seata"		</br>

	#接着我们看到这里有指定的3张表 ，表sql在0.0.9版本\seata\conf\db_store.sql，我们需要在seata里执行该sql文件，seata依靠这3张表用于分布式事务的提交和反向补偿和数据行锁。		
global.table = "global_table"		</br>
branch.table = "branch_table"		</br>
lock-table = "lock_table"		</br>



我们简单的搭建就需要知道这几个配置即可，第三点会讲到如何将这些配置导入nacos。		</br>
更多配置详情可参考官方参数说明：https://seata.io/zh-cn/docs/user/configurations.html		</br>





二、编辑registry.conf文件		</br>
该文件是配置是指定注册中心和配置中心，默认是file的，改为nacos。		</br>

#nacos注册中心的地址		</br>
serverAddr = "192.168.1.151:8848"		</br>
#namespace的命名空间为空的话默认是public，我新建了一个命名空间。		</br>
namespace = "xxxxxxxxxx"		</br>
#分组也必须和你项目中的注册在nacos一样。 registry与config 建议不同组，好区分		</br>
group = "SEATA_A01_GROUP" 		</br>
#默认即可		</br>
cluster = "default"  		</br>
		</br>
![图片](https://github.com/a970066364/spring-cloud-alibaba-seata/blob/master/md-img/4.png)		</br>


		</br>
三、将配置导入nacos		</br>

方式1：		</br>
	在seata1.0的包里已经没有提供 nacos-config.txt 的配置文件及nacos-config.sh。只能用seata0.9版本，在seata0.9\conf\nacos-config.txt、nacos-config.sh。该文件可通过nacos-config.sh脚本导入（这个脚本我导入是报错，已编写了自己的脚本）		</br>
		</br>
执行脚本的命令：sh nacos-config.sh -h localhost -p 8848		</br>


		</br>
方式2：		</br>
	自己写个post请求脚本或使用postMan工具，将nacos-config.txt里的所有参数注册到nacos。例如下面		</br>

请求方式：POST		</br>
请求地址：http://192.168.1.151:8848/nacos/v1/cs/configs? 		</br>
封装的参数：		</br>
dataId： store.db.driverClassName		</br>
namespace：public		</br>
group：SEATA_CONFIG_GROUP		</br>
content：com.mysql.jdbc.Driver		</br>

	关于nacos-config.txt 的坑，官方更新1.0之后对配置的key做了一些升级。统一格式命名为驼峰命名，否则在启动seata服务的时候会报错，报错也会打印找不到指定的key。		</br>
例如：store.db.driver-class-name  改成 store.db.driverClassName=com.mysql.jdbc.Driver		</br>
官方说明：https://seata.io/zh-cn/docs/user/configurations.html		</br>

![图片](https://github.com/a970066364/spring-cloud-alibaba-seata/blob/master/md-img/5.png)		</br>

我已经处理了nacos-config.txt 的坑 且导入的脚本我也打包到源码工程里了，可自行取用。		</br>

	脚本地址：https://github.com/a970066364/spring-cloud-alibaba-seata/blob/master/core-service/src/main/java/app/itw/cloud/seata/util/HttpPostUtils.java

	nacos.txt地址：https://github.com/a970066364/spring-cloud-alibaba-seata/blob/master/core-service/src/main/resources/application.properties
	

	导入到nacos之后还需要手动去做一件事，导入nacos的时候，配置默认都在public下，你还需要选中所有克隆到你创建的命名空间里。（选中当前页下一页继续全选，之前全择的依然有效）		

导入完成后的nacos注册中心服务列表（前3个是我自己手动创建的，其它是导入的）		</br>

![图片](https://github.com/a970066364/spring-cloud-alibaba-seata/blob/master/md-img/6.png)		</br>

四、启动seata服务		</br>
启动路径在 seata\bin\seata-server.bat。启动成功的图，会打印如下。 		</br>

![图片](https://github.com/a970066364/spring-cloud-alibaba-seata/blob/master/md-img/7.png)		</br>

对应的nacos注册中心的服务列表对应的命名空间里就会有该服务。		</br>

![图片](https://github.com/a970066364/spring-cloud-alibaba-seata/blob/master/md-img/8.png)		</br>


四、编写简单的项目模拟seata分布式事务		</br>
分别为：订单服务、库存服务、用户服务，核心模块。		</br>
springcloud seata+nacos+feign+mybatis-plus 		</br>
github地址:https://github.com/a970066364/spring-cloud-alibaba-seata		</br>

![图片](https://github.com/a970066364/spring-cloud-alibaba-seata/blob/master/md-img/9.png)		</br>

启动工程后的注册信息		</br>


![图片](https://github.com/a970066364/spring-cloud-alibaba-seata/blob/master/md-img/10.png)		</br>
关于seata分布式事务的流程原理稍后补上		</br>
