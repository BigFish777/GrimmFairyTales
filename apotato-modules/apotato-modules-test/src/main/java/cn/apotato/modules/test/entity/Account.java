package cn.apotato.modules.test.entity;

import cn.apotato.common.model.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import org.apache.ibatis.type.ArrayTypeHandler;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


/**
 * 账户
 * 账户的角色和菜单是分离的设计，角色的分配又前段控制。
 * 账户本身就是一个角色，而角色作为业务需求使用
 *
 * @author xphu
 * @date 2022/11/25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "account", autoResultMap = true)
public class Account extends BaseModel {

    /**
     * 账户
     */
    @NotBlank(message = "账号必填")
    private String account;

    /**
     * 密码
     */
    @NotBlank(message = "密码必填")
    private String password;

    /**
     * 电话
     */
    @NotEmpty(message = "电话必填")
    private String phone;

    /**
     * 用户名
     */
    private String username;

    /**
     * 邮件
     */
    private String mail;
    /**
     * 地址
     */
    private String address;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 微信openId
     */
    private String openId;
    /**
     * 小程序使用
     */
    private String sessionKey;

    /**
     * 微信公众号
     */
    private String unionId;

    /**
     * 微信公众号
     */
    private String publicOpenId;

    /**
     * 是否关注公众号
     */
    private Boolean subscribeFlag;


    /**
     * 组织id
     */
    @NotNull(message = "组织必填")
    private Long orgId;

    /**
     * 角色
     */
    @TableField(typeHandler = ArrayTypeHandler.class)
    private Long[] roleId;

    /**
     * 菜单
     */
    @TableField(typeHandler = ArrayTypeHandler.class)
    private String[] menuId;


    @TableField(exist = false)
    private Long rootOrgId;

}
