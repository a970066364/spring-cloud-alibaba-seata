package app.itw.cloud.seata.storage.service.impl;

import app.itw.cloud.seata.storage.entity.StorageInfo;
import app.itw.cloud.seata.storage.mapper.StorageInfoMapper;
import app.itw.cloud.seata.storage.service.IStorageInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class StorageInfoServiceImpl extends ServiceImpl<StorageInfoMapper, StorageInfo> implements
    IStorageInfoService {

}
