package app.itw.cloud.seata.order.service;

import app.itw.cloud.seata.order.entity.OrderInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Tison
 * @since 2020-03-07
 */
public interface IOrderInfoService extends IService<OrderInfo> {

  /**
   * 生成用户订单
   * @param userId
   * @param commodityCode
   * @param count
   */
  void generateOrder(String userId, String commodityCode, Integer count);
}
