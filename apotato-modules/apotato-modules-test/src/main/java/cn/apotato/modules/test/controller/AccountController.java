package cn.apotato.modules.test.controller;

import cn.apotato.common.core.base.BaseController;
import cn.apotato.modules.test.entity.Account;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

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

    // todo 三种不通颗粒度的查询过滤的钩子函数
    /**
     * 查询结果过滤
     *
     * @param page 页面
     * @return {@link IPage}<{@link Account}>
     */
    @Override
    public IPage<Account> queryResultFilterHook(IPage<Account> page) {
        return super.queryResultFilterHook(page);
    }

    /**
     * 查询结果过滤
     *
     * @param records 记录
     * @return {@link List}<{@link Account}>
     */
    @Override
    public List<Account> queryResultFilterHook(List<Account> records) {
        return records.stream()
                .peek(System.out::println)
                .filter(account -> account.getAccount() != null)
                .collect(Collectors.toList());
    }

    /**
     * 查询结果过滤
     *
     * @param account 对象
     * @return {@link Account}
     */
    @Override
    public Account queryResultFilterHook(Account account) {
        account.setAccount("张三");
        return account;
    }




    // todo: 查询条件设置

    /**
     * 设置查询参数钩
     * 设置查询参数钩子函数
     * 设置请求的参数 {@link Account}
     *
     * @param account 查询参数
     * @return {@link Account}
     */
    @Override
    public Account setQueryParamHook(Account account) {
        // 例如：设置查询条件 orgId = 1
        account.setOrgId(0L);

        // 例如：将用户名称都加上统一字符“xxx-”
        String str = "xxx-";
        if (StringUtils.isNotBlank(account.getNickname())) {
            account.setNickname(str + account.getNickname());
        }
        return account;
    }

    /**
     * 设置查询参数钩子函数
     *
     * @param lambdaQueryWrapper 查询包装
     * @return {@link QueryWrapper}<{@link Account}>
     */
    @Override
    public LambdaQueryWrapper<Account> setQueryCriteriaHook(LambdaQueryWrapper<Account> lambdaQueryWrapper) {
        return lambdaQueryWrapper.isNotNull(Account::getAccount);
    }

}
