package app.itw.cloud.seata.order.client;


import app.itw.cloud.seata.tdo.DefaultResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 描述: author: Tsion date: 2020-02-24
 */

@FeignClient(name = "storage-service")
public interface StorageServiceClient {

  /**
   * 减去库存
   * @param commodityCode
   * @param count
   * @return
   */
  @RequestMapping(value = "/api/v1/storage/deduction", method = RequestMethod.POST)
  public DefaultResult deduction(@RequestParam String commodityCode, @RequestParam Integer count);


}
