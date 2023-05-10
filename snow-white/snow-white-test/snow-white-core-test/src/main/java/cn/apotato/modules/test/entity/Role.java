package cn.apotato.modules.test.entity;

import cn.apotato.common.model.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.ArrayTypeHandler;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "role",autoResultMap = true)
public class Role extends BaseModel {

    /**
     * 角色名字
     */
    private String name;
    /**
     * 类型：0系统预设、1自定义创建
     */
    private Integer type;

    /**
     * org id
     */
    private Long orgId;

    /**
     * 菜单
     */
    @TableField(typeHandler = ArrayTypeHandler.class)
    private String[] menuId;
}
