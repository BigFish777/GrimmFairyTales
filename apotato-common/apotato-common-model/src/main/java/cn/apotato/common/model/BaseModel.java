package cn.apotato.common.model;

import cn.apotato.common.core.group.ValidationGroups;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 基本模型
 *
 * @author 胡晓鹏
 * @date 2023/04/21
 */
@Data
@ColumnWidth(15)
@ContentRowHeight(23)
@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER)
public class BaseModel implements Serializable {

    @ExcelIgnore
    @TableId(type = IdType.AUTO)
    @NotNull(message = "id不能为空", groups = {ValidationGroups.Update.class})
    private Long id;

    /**
     * 创建时间
     */
    @JsonIgnore
    @ExcelIgnore
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 修改时间
     */
    @JsonIgnore
    @ExcelIgnore
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;


    /**
     * 开始时间
     */
    @JsonIgnore
    @ExcelIgnore
    @TableField(exist = false)
    private LocalDateTime startedAt;

    /**
     * 结束时间
     */
    @JsonIgnore
    @ExcelIgnore
    @TableField(exist = false)
    private LocalDateTime endedAt;

    /**
     * 排序方式：倒叙=desc、正序=asc
     */
    @JsonIgnore
    @ExcelIgnore
    @TableField(exist = false)
    private String orderType;

    /**
     * 排序字段
     */
    @JsonIgnore
    @ExcelIgnore
    @TableField(exist = false)
    private List<String> orderFields;
}
