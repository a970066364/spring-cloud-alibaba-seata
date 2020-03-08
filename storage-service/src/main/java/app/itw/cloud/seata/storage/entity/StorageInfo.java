package app.itw.cloud.seata.storage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author Tison
 * @since 2020-03-07
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class StorageInfo implements Serializable {

  private static final long serialVersionUID = 1L;
  @TableId(value = "id", type = IdType.AUTO)
  private Integer id;


  private String commodityCode;


  private Integer count;


}
