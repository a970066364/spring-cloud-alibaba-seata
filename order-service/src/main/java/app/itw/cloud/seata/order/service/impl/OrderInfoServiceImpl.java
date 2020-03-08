package app.itw.cloud.seata.order.service.impl;

import app.itw.cloud.seata.order.client.StorageServiceClient;
import app.itw.cloud.seata.order.client.UserServiceClient;
import app.itw.cloud.seata.order.entity.OrderInfo;
import app.itw.cloud.seata.order.mapper.OrderInfoMapper;
import app.itw.cloud.seata.order.service.IOrderInfoService;
import app.itw.cloud.seata.tdo.DefaultResult;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Tison
 * @since 2020-03-07
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements
    IOrderInfoService {

  @Autowired
  private StorageServiceClient storageServiceClient;
  @Autowired
  private UserServiceClient userServiceClient;

  /**
   * GlobalTransactional 分布式事务注解
   */
  @Override
  @GlobalTransactional
  public void generateOrder(String userId, String commodityCode, Integer count){

    //假设商品默认价格为：100元
    int unitPrice = 100;
    int money = count * 100;
    //扣除用户的余额
    DefaultResult defaultResult = userServiceClient.deduction(userId,money);
    //如果状态不为200直接返回
    if (defaultResult.getStatus() != 200) {
      //回滚事务
      throw new RuntimeException(defaultResult.getMsg());
    }
    //如果用户扣除成功，生成订单
    OrderInfo orderInfo = new OrderInfo();
    orderInfo.setCommodityCode(commodityCode);
    orderInfo.setMoney(money);
    orderInfo.setCount(count);
    orderInfo.setUserId(userId);
    save(orderInfo);
    //扣除库存
    defaultResult = storageServiceClient.deduction(commodityCode,count);
    if (defaultResult.getStatus() != 200) {
      //抛出异常，回滚事务
      throw new RuntimeException(defaultResult.getMsg());
    }
    //模拟异常
    if (userId.equals("1002")&&count==10){
          int a=1/0;
    }
  }
}
