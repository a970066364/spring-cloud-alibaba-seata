package app.itw.cloud.seata.order.client;


import app.itw.cloud.seata.tdo.DefaultResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 描述: author: Tsion date: 2020-02-24
 */

@FeignClient(name = "user-service")
public interface UserServiceClient {

  /**
   * 扣除用户余额
   * @param userId
   * @param money
   * @return
   */
  @RequestMapping(value = "/api/v1/user/account/deduction", method = RequestMethod.POST)
  public DefaultResult deduction(@RequestParam String userId, @RequestParam Integer money);
}
