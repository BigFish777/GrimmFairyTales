package cn.apotato.modules.test.entity;

import cn.apotato.common.model.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * <p>
 * 组织表
 * </p>
 *
 * @author xphu
 * {@code {@code @date}} 2022-11-28
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName(value = "organization", schema = "forest")
public class Organization extends BaseModel {

    /**
     * 名称
     */
    @NotEmpty(message = "组织名称必填")
    private String name;

    /**
     * 上级组织id
     */
    @NotNull(message = "上级组织必填")
    private Long pid;

    /**
     * 联系人
     */
    private String personLiable;

    /**
     * 联系方式
     */
    private String contactInformation;


    /**
     * 孩子们
     */
    @TableField(exist = false)
    private List<Organization> children;

    /**
     * 账号
     */
    @TableField(exist = false)
    private List<Account> accountList;


    @TableField(exist = false)
    private Organization parent;


}
