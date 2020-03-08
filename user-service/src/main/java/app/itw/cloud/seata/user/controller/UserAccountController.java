package app.itw.cloud.seata.user.controller;


import app.itw.cloud.seata.tdo.DefaultResult;
import app.itw.cloud.seata.user.entity.UserAccount;
import app.itw.cloud.seata.user.service.IUserAccountService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
@RequestMapping("/api/v1/user/account")
//nacos动态刷新
@RefreshScope
public class UserAccountController {

  @Autowired
  private IUserAccountService userAccountService;

  @Value("${msg}")
  private String msg;

  /**
   * 订单下单后账户扣款
   */
  @PostMapping("/deduction")
  public DefaultResult deduction(String userId, Integer money) {
    System.out.println(userId + " 下单扣款：" + money + "元，开始！");
    QueryWrapper<UserAccount> wrapper = new QueryWrapper<>();
    wrapper.eq("user_id", userId);
    UserAccount one = userAccountService.getOne(wrapper);
    //用户是否存在
    if (one == null) {
      return DefaultResult.Error("用户不存在！");
    }
    //余额是否足够
    if ((one.getMoney() - money) < 0) {
      return DefaultResult.Error("用户余额不足！");
    }
    //扣除金额
    one.setMoney(one.getMoney() - money);
    userAccountService.updateById(one);
    System.out.println(userId + " 下单扣款：" + money + "元，成功！");
    return DefaultResult.Ok();
  }

  @GetMapping("test")
  public DefaultResult hello() {
    return DefaultResult.Ok(msg);
  }
}
