package app.itw.cloud.seata.order.controller;


import app.itw.cloud.seata.order.service.IOrderInfoService;
import app.itw.cloud.seata.tdo.DefaultResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Tison
 * @since 2020-03-07
 */
@RestController
@RequestMapping("/api/v1/order")
//nacos动态刷新
@RefreshScope
public class OrderInfoController {


  @Autowired
  private IOrderInfoService orderInfoService;

  @Value("${msg}")
  private String msg;

  /**
   * 订单下单后库存减去
   */
  @RequestMapping("/generate")
  public DefaultResult generate(String userId, String commodityCode, Integer count) {
    System.out.println("用户：" + userId + "开始下单，商品编号：" + commodityCode + "，数量：" + count + "个，开始！");
    try {
       orderInfoService.generateOrder(userId, commodityCode, count);
    }catch (RuntimeException e){
      return DefaultResult.Error(e.getMessage());
    }
    System.out.println("用户：" + userId + "开始下单，商品编号：" + commodityCode + "，数量：" + count + "个，成功！");
    return DefaultResult.Ok();
  }

  @GetMapping("test")
  public DefaultResult hello() {
    return DefaultResult.Ok(msg);
  }


}
