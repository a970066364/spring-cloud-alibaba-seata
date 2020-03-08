package app.itw.cloud.seata.user.service.impl;

import app.itw.cloud.seata.user.entity.UserAccount;
import app.itw.cloud.seata.user.mapper.UserAccountMapper;
import app.itw.cloud.seata.user.service.IUserAccountService;
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
public class UserAccountServiceImpl extends ServiceImpl<UserAccountMapper, UserAccount> implements
    IUserAccountService {

}
