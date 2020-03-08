package app.itw.cloud.seata.storage.controller;


import app.itw.cloud.seata.storage.entity.StorageInfo;
import app.itw.cloud.seata.storage.service.IStorageInfoService;
import app.itw.cloud.seata.tdo.DefaultResult;
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
@RequestMapping("/api/v1/storage")
//nacos动态刷新
@RefreshScope
public class StorageInfoController {

  @Autowired
  private IStorageInfoService storageInfoService;

  @Value("${msg}")
  private String msg;
  /**
   * 订单下单后库存减去
   */
  @PostMapping("/deduction")
  public DefaultResult deduction(String commodityCode, Integer count) {
    System.out.println(commodityCode + " 库存扣除：" + count + "个，开始！");
    QueryWrapper<StorageInfo> wrapper = new QueryWrapper<>();
    wrapper.eq("commodity_code", commodityCode);
    StorageInfo one = storageInfoService.getOne(wrapper);
    if (one == null) {
      return DefaultResult.Error("该商品编码不存在！");
    }
    if ((one.getCount() - count) < 0) {
      return DefaultResult.Error("该商品库存不足！");
    }
    one.setCount(one.getCount() - count);
    storageInfoService.updateById(one);
    System.out.println(commodityCode + " 库存扣除：" + count + "个，成功！");
    return DefaultResult.Ok();
  }


  @GetMapping("test")
  public DefaultResult hello() {
    return DefaultResult.Ok(msg);
  }


}
