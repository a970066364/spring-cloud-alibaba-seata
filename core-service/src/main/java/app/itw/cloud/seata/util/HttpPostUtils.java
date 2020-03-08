package app.itw.cloud.seata.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class HttpPostUtils {

  public static void main(String[] args) throws IOException {
    String nacosUrl = "http://192.168.1.151:8848/nacos/v1/cs/configs?";
    String nacosConfigPath = "D:\\admin\\src\\demo2020\\demo\\http-utils\\src\\main\\resources\\application.properties";
    Properties properties = new Properties();
    // 使用properties对象加载输入流
    properties.load((new FileInputStream(nacosConfigPath)));
    //注册配置
    registerNacosConfig(properties, nacosUrl);
  }

  /**
   * 注册nacos配置
   */
  public static void registerNacosConfig(Properties properties, String nacosUrl) {
    properties.forEach((k, v) -> {
      final String kk = k.toString();
      final String vv = v.toString();
      try {
        TimeUnit.MILLISECONDS.sleep(300);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("dataId", kk);
        params.add("namespace", "public");
        params.add("group", "SEATA_CONFIG_GROUP");
        params.add("content", vv);
        //发送Post数据并返回数据
        String resultVo = sendPostRequest(nacosUrl, params);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    });
  }

  /**
   * 向目的URL发送post请求
   *
   * @param url 目的url
   * @param params 发送的参数
   * @return ResultVO
   */
  public static String sendPostRequest(String url, MultiValueMap<String, String> params) {
    RestTemplate client = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    HttpMethod method = HttpMethod.POST;
    // 以表单的方式提交
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    //将请求头部和参数合成一个请求
    HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
    //执行HTTP请求，将返回的结构使用ResultVO类格式化
    ResponseEntity<String> response = client.exchange(url, method, requestEntity, String.class);
    return response.getBody();
  }
}
