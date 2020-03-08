package app.itw.cloud.seata.tdo;

public class DefaultResult {


  private int status = 200;//状态： DefaultResultCodeEnum.success.getCode() 正常，500服务错误，DefaultResultCodeEnum.failure.getCode()错误有误；
  //可用枚举类，但没必要
  final static int status500 = 500; //500服务错误
  final static int status200 = 200; //正常
  final static int status404 = 404; //错误

  private Object data;//数据

  private String code;// 业务代码，0正常

  private String msg;// 消息

  public DefaultResult(int status, String code, String msg, Object data) {
    this.status = status;
    this.code = code;
    this.msg = msg;
    this.data = data;
  }

  /**
   * 服务器异常
   *
   * @param msg 错误提示
   */
  public static DefaultResult ExceptionError(String msg) {
    return new DefaultResult(status500, "-1", msg, null);
  }

  /**
   * 操作有误，
   *
   * @param msg 代码
   * @return 错误提示
   */
  public static DefaultResult Error(String code, String msg) {
    return new DefaultResult(status404, code, msg, null);
  }

  /**
   * 操作有误，
   *
   * @return 错误提示
   */
  public static DefaultResult Error(String msg) {
    return new DefaultResult(status404, "-1", msg, null);
  }

  /**
   * 操作有误，
   *
   * @return 错误提示
   */
  public static DefaultResult Error() {
    return new DefaultResult(status404, "-1", "操作失败", null);
  }

  /**
   * 操作成功
   */
  public static DefaultResult Ok() {
    return new DefaultResult(status200, "0", "操作成功", null);
  }

  /**
   * 操作成功
   *
   * @param msg 成功提示
   */
  public static DefaultResult Ok(String msg) {
    return new DefaultResult(status200, "0", msg, null);
  }

  /**
   * 操作成功
   *
   * @param code 代码
   * @param msg 成功提示
   */
  public static DefaultResult Ok(String code, String msg) {
    return new DefaultResult(status200, code, msg, null);
  }

  /**
   * 操作成功
   *
   * @param code 代码
   * @param msg 成功提示
   * @param data 数据
   */
  public static DefaultResult Ok(String code, String msg, Object data) {
    return new DefaultResult(status200, code, msg, data);
  }

  /**
   * 操作成功
   *
   * @param msg 成功提示
   * @param data 数据
   */
  public static DefaultResult Ok(String msg, Object data) {
    return new DefaultResult(status200, "0", msg, data);
  }

  /**
   * 操作成功
   *
   * @param data 数据
   */
  public static DefaultResult Ok(Object data) {
    return new DefaultResult(status200, "0", "操作成功", data);
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public Object getData() {
    return data;
  }

  public void setData(Object data) {
    this.data = data;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }


}
