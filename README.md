<h3>如果能帮到你，帮我点个start吧，谢谢</h3>



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
github地址:https://github.com/a970066364/spring-cloud-alibaba-seata （配置文件、SQL文件已经在各工程下）		</br>


![图片](https://github.com/a970066364/spring-cloud-alibaba-seata/blob/master/md-img/9.png)		</br>

启动工程后的注册信息		</br>


![图片](https://github.com/a970066364/spring-cloud-alibaba-seata/blob/master/md-img/10.png)		</br>


关于流程项目执行流程以及数据变化	</br>

发起请求，模拟用户下单：POST 127.0.0.1:15020/api/v1/order/generate	 </br>

![输入图片说明](https://images.gitee.com/uploads/images/2020/0308/162747_9b35f32e_2166801.png "11.png")

业务入口：OrderInfoController#generate  </br>

1、请求用户服务去扣除余额  </br>
![输入图片说明](https://images.gitee.com/uploads/images/2020/0308/162837_10923e39_2166801.png "12.png")  </br>
![输入图片说明](https://images.gitee.com/uploads/images/2020/0308/162858_9e3e372a_2166801.png "13.png") </br>

seata对user库的user_account表行数据备份，如果发生异常该数据用于回滚补偿，如果本次事务成功提交或回滚成功都会删除该库下的undo_log相关行记录，如果回滚失败则会保留需要人工干预（由于非连续操作事务ID与下面不一致）</br>
![输入图片说明](https://images.gitee.com/uploads/images/2020/0308/162943_49ba04e3_2166801.png "14.png")  </br>

2.订单生成保存入库，</br>
![输入图片说明](https://images.gitee.com/uploads/images/2020/0308/163002_b5510613_2166801.png "15.png")

生成了订单数据  </br></br></br>
![输入图片说明](https://images.gitee.com/uploads/images/2020/0308/163015_692b88e1_2166801.png "16.png")  </br>

seata对user库的order_info表行数据备份，如果发生异常该数据用于回滚补偿，如果本次事务成功提交或回滚成功都会删除该库下的undo_log相关行记录，如果回滚失败则会保留需要人工干预（由于非连续操作事务ID与下面不一致）  </br>

![输入图片说明](https://images.gitee.com/uploads/images/2020/0308/163027_1ae27aea_2166801.png "17.png")  </br>

3、扣除库存 </br>
![输入图片说明](https://images.gitee.com/uploads/images/2020/0308/163102_fda4ec7a_2166801.png "18.png") </br>

![输入图片说明](https://images.gitee.com/uploads/images/2020/0308/163111_599ba229_2166801.png "19.png") </br>


seata对user库的storage_info表行数据备份，如果发生异常该数据用于回滚补偿，如果本次事务成功提交或回滚成功都会删除该库下的undo_log相关行记录，如果回滚失败则会保留需要人工干预（由于非连续操作事务ID与下面不一致） </br>

![输入图片说明](https://images.gitee.com/uploads/images/2020/0308/163137_09bac1d0_2166801.png "20.png")</br>

1、当订单服务发起了全局事务，@GlobalTransactional的方法。会写入一条发起者的服务信息以及全局事务ID  </br>
![输入图片说明](https://images.gitee.com/uploads/images/2020/0308/163200_2c9745fb_2166801.png "21.png")</br>
2、执行用户余额扣除则会插入一条，接着订单生成、库存扣除都会插入，执行的链路都会记录下来  </br>
![输入图片说明](https://images.gitee.com/uploads/images/2020/0308/163223_d21cad1a_2166801.png "22.png")
3、每次执行的行数据都会被记录下来，用于回滚处理时找到对应的数据源库。</br>
![输入图片说明](https://images.gitee.com/uploads/images/2020/0308/163238_b2456816_2166801.png "23.png") </br>
4、以上的步骤成功提交或者成功回滚都会将seata的数据清除。 </br> </br> </br>

 **

### 关于seata分布式事务的流程原理(等我有时间研究整理好源码在更)

** 

分析undo_log表的结构，该表中 </br>
 </br>
rollback_info	#字段存储变更前的数据和变更后的数据， </br>
context		#保存数据的格式 默认为json </br>

![输入图片说明](https://images.gitee.com/uploads/images/2020/0308/163329_27452683_2166801.png "24.png") </br>

 </br>
这是我拷贝库存表其中一次事务的数据，该数据中记录了 库存变更前是985个，变更后的库存数量为980个，因此一旦事务中途发生了异常需要全局回滚时，则找到该条数据回滚到初始的值即完成了事务回滚，但也会发生事务回滚失败的时候，可能在你准备回滚时其他的业务事务操作正好把 985的数据修改了，发生了脏数据，此时原始数据已经不存在或是被修改seata就无法回滚了，此时必须人工处理。 </br>
{
	"@class": "io.seata.rm.datasource.undo.BranchUndoLog",
	"xid": "192.168.10.1:8091:2037379766",
	"branchId": 2037379772,
	"sqlUndoLogs": ["java.util.ArrayList", [{
		"@class": "io.seata.rm.datasource.undo.SQLUndoLog",
		"sqlType": "UPDATE",
		"tableName": "storage_info",
		"beforeImage": {
			"@class": "io.seata.rm.datasource.sql.struct.TableRecords",
			"tableName": "storage_info",
			"rows": ["java.util.ArrayList", [{
				"@class": "io.seata.rm.datasource.sql.struct.Row",
				"fields": ["java.util.ArrayList", [{
					"@class": "io.seata.rm.datasource.sql.struct.Field",
					"name": "id",
					"keyType": "PRIMARY_KEY",
					"type": 4,
					"value": 1
				}, {
					"@class": "io.seata.rm.datasource.sql.struct.Field",
					"name": "commodity_code",
					"keyType": "NULL",
					"type": 12,
					"value": "iphone"
				}, {
					"@class": "io.seata.rm.datasource.sql.struct.Field",
					"name": "count",
					"keyType": "NULL",
					"type": 4,
					"value": 985
				}]]
			}]]
		},
		"afterImage": {
			"@class": "io.seata.rm.datasource.sql.struct.TableRecords",
			"tableName": "storage_info",
			"rows": ["java.util.ArrayList", [{
				"@class": "io.seata.rm.datasource.sql.struct.Row",
				"fields": ["java.util.ArrayList", [{
					"@class": "io.seata.rm.datasource.sql.struct.Field",
					"name": "id",
					"keyType": "PRIMARY_KEY",
					"type": 4,
					"value": 1
				}, {
					"@class": "io.seata.rm.datasource.sql.struct.Field",
					"name": "commodity_code",
					"keyType": "NULL",
					"type": 12,
					"value": "iphone"
				}, {
					"@class": "io.seata.rm.datasource.sql.struct.Field",
					"name": "count",
					"keyType": "NULL",
					"type": 4,
					"value": 980
				}]]
			}]]
		}
	}]]
}
