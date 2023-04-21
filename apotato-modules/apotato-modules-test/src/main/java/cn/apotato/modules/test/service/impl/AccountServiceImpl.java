package cn.apotato.modules.test.service.impl;

import cn.apotato.modules.test.entity.Account;
import cn.apotato.modules.test.mapper.AccountMapper;
import cn.apotato.modules.test.service.AccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * impl账户服务
 *
 * @author 胡晓鹏
 * @date 2023/04/21
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {
}
