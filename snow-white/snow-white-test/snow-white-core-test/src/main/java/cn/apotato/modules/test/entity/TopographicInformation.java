package cn.apotato.modules.test.entity;

import cn.apotato.common.model.BaseModel;
import cn.apotato.modules.mybatis.plus.handler.ArrayHandlerOfTwo;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 地形信息
 * </p>
 *
 * @author xphu
 * {@code {@code @date}} 2023-03-21
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName(value = "topographic_information", autoResultMap = true)
public class TopographicInformation extends BaseModel {

    /**
     * 编号 林班号+小班号
     */
    @ExcelProperty("编号")
    private String number;

    /**
     * 林班号
     */
    @ExcelProperty("林班号")
    private String linBanNo;

    /**
     * 小班号
     */
    @ExcelProperty("小班号")
    private String xiaoBanNo;

    /**
     * 村
     */
    @ExcelProperty("村")
    private String village;

    /**
     * 小班特征
     */
    @ExcelProperty("小班特征")
    private String characteristic;

    /**
     * 小班面积
     */
    @ExcelProperty("小班面积")
    private String areaCovered;


    /**
     * 土地权属
     */
    @ExcelProperty("土地权属")
    private String landOwnership;

    /**
     * 地貌
     */
    @ExcelProperty("地貌")
    private String landforms;

    /**
     * 坡向
     */
    @ExcelProperty("坡向")
    private String aspectOfSlope;

    /**
     * 坡位
     */
    @ExcelProperty("坡位")
    private String slopePosition;

    /**
     * 坡度
     */
    @ExcelProperty("坡度")
    private String slope;

    /**
     * 土壤厚度
     */
    @ExcelProperty("土壤厚度")
    private String soilThickness;

    /**
     * 土壤名称
     */
    @ExcelProperty("土壤名称")
    private String soilName;

    /**
     * 林地保护等级
     */
    @ExcelProperty("林地保护等级")
    private String protectionLevel;

    /**
     * 地类
     */
    @ExcelProperty("地类")
    private String landType;

    /**
     * 坐标
     */
    @ExcelIgnore
    @TableField(typeHandler = ArrayHandlerOfTwo.class)
    private Double[][] coordinate;


}