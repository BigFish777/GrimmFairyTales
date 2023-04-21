package cn.apotato.modules.test.controller;

import cn.apotato.common.core.base.BaseController;
import cn.apotato.modules.test.entity.Account;
import cn.apotato.modules.test.service.AccountService;
import com.baomidou.mybatisplus.extension.service.IService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 帐户控制器
 *
 * @author 胡晓鹏
 * @date 2023/04/21
 */
@AllArgsConstructor
@RequestMapping("account")
@RestController
public class AccountController extends BaseController<Account, Long> {

    private final AccountService accountService;

    /**
     * 服务
     *
     * @return {@link IService}
     */
    @Override
    protected IService<Account> service() {
        return accountService;
    }
}
