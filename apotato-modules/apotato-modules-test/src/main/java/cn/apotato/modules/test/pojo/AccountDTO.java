package cn.apotato.modules.test.pojo;

import cn.apotato.modules.test.entity.Account;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 账户dto
 *
 * @author 胡晓鹏
 * @date 2023/05/05
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AccountDTO extends Account {

    /**
     * 角色名字
     */
    private String orgName;
}
