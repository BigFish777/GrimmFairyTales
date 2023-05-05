package cn.apotato.modules.test.controller;

import cn.apotato.common.core.base.BaseController;
import cn.apotato.modules.test.entity.Account;
import cn.apotato.modules.test.entity.Organization;
import cn.apotato.modules.test.pojo.AccountDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.yulichang.base.MPJBaseMapper;
import com.github.yulichang.query.MPJQueryWrapper;
import com.github.yulichang.toolkit.JoinWrappers;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * BaseController和MPJBaseMapper测试
 *
 * @author 胡晓鹏
 * @date 2023/04/21
 */
@RequestMapping("account")
@RestController
public class AccountController extends BaseController<Account, Long> {

    @Resource
    private MPJBaseMapper<Account> mapper;

    public AccountController(IService<Account> service, MPJBaseMapper<Account> mapper) {
        super(service, mapper);
    }

    /**
     * 级联查询 MPJQueryWrapper
     * <a href="https://mybatisplusjoin.com/pages/core/lambda/select/select.html">MPJQueryWrapper文档</a>
     * ==> SELECT t.* o.NAME AS org_name FROM account t LEFT JOIN organization o ON o.id = t.org_id WHERE t.id = ?;
     * @param accountId 帐户id
     * @return {@link List}<{@link Map}<{@link String}, {@link Object}>>
     */
    @GetMapping("join")
    public List<Map<String, Object>> getAccountInfo(Long accountId) {
        return mapper.selectJoinMaps(new MPJQueryWrapper<Account>().selectAll(Account.class)
                .select("o.name as org_name")
                .leftJoin("forest.organization o on o.id = t.org_id")
                .eq(accountId != null,"t.id", accountId)
        );
    }

    /**
     * 级联查询 MPJLambdaWrapper的简单使用
     * <a href="https://mybatisplusjoin.com/pages/core/str/select.html">MPJLambdaWrapper文档</a>
     * ==> SELECT t.* o.NAME AS org_name FROM account t LEFT JOIN organization o ON o.id = t.org_id WHERE t.id = ?;
     * @param accountId 帐户id
     * @return {@link List}<{@link AccountDTO}>
     */
    @GetMapping("join-lamda")
    public List<AccountDTO> getAccountInfoLamda(Long accountId) {
        return JoinWrappers.lambda(Account.class)
                .selectAsClass(Account.class, AccountDTO.class)
                .selectAs(Organization::getName, AccountDTO::getOrgName)
                .leftJoin(Organization.class, Organization::getId, Account::getOrgId)
                .eq(accountId != null,"t.id", accountId)
                .list(AccountDTO.class);
    }



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
