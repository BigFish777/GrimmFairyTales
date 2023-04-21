package cn.apotato.common.core.base;

import cn.apotato.common.core.utils.UnderlineToCamelUtils;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static cn.apotato.common.core.base.BaseConstant.*;
import static cn.apotato.common.core.base.BaseConstant.ORDER_FIELDS;

/**
 * 基础查询包装
 *
 * @author 胡晓鹏
 * @date 2023/04/21
 */
public class BaseQueryWrapper <T> {

    /**
     * 创建λ查询包装
     *
     * @param entity    实体
     * @param isOrderBy 是否排序
     * @return {@link QueryWrapper}<{@link T}>
     * @throws IllegalAccessException 非法访问异常
     */
    public QueryWrapper<T> createLambdaQueryWrapper(T entity, boolean isOrderBy) throws IllegalAccessException {
        QueryWrapper<T> queryWrapper = new QueryWrapper<T>();
        Class<?> entityClass = entity.getClass();
        Field[] fields = entityClass.getDeclaredFields();
        // 没有字段直接返回
        if (fields.length == 0) {
            return queryWrapper;
        }
        // 处理特殊字段
        fields = HandlingOfSpecialFields(queryWrapper,fields, entity, isOrderBy);
        // 处理普通字段
        for (Field field : fields) {
            field.setAccessible(true);
            // 字段名、值、字段类型
            String fieldName = field.getName();
            Object value = field.get(entity);
            Class<?> fieldType = field.getType();
            // 如果字段上存在TableField注解，表明该字段被mybatis-plus管理并且是特殊的字段
            TableField annotation = field.getAnnotation(TableField.class);
            if (annotation != null) {
                // exist = false  是否为数据库表字段 默认 true 存在，false 不存在
                if (!annotation.exist()) {
                    if (!Objects.equals(STARTED_AT, fieldName) || !Objects.equals(ENDED_AT, fieldName)) {
                        continue;
                    }
                }
                // 获取数据库字段名称，默认使用字段名称（驼峰）转为下划线
                // 例：userName -> user_name
                String tableFieldValue = UnderlineToCamelUtils.camelToUnderline(fieldName);
                if (StringUtils.isNotBlank(tableFieldValue)) {
                    tableFieldValue = annotation.value();
                }
                setFieldQueryCriteria(queryWrapper, fieldType, tableFieldValue, value);
            }
        }
        return queryWrapper;
    }


    /**
     * 处理特殊字段
     * 处理：排序，时间区间
     */
    public Field[] HandlingOfSpecialFields(QueryWrapper<T> queryWrapper, Field[] fields, T entity, boolean isOrderBy) throws IllegalAccessException {
        Map<String, Field> fieldMap = Arrays.stream(fields).collect(Collectors.toMap(Field::getName, field -> field));
        //时间区间处理
        Field createdAtField = fieldMap.get(STARTED_AT);
        if (createdAtField != null) {
            createdAtField.setAccessible(true);
            Object value = createdAtField.get(entity);
            if (value != null) {
                queryWrapper.ge(UnderlineToCamelUtils.camelToUnderline(CREATED_AT), value);
            }
        }
        Field endAtField = fieldMap.get(ENDED_AT);
        if (endAtField != null) {
            endAtField.setAccessible(true);
            Object value = endAtField.get(entity);
            if (value != null) {
                queryWrapper.le(UnderlineToCamelUtils.camelToUnderline(CREATED_AT), value);
            }
        }

        //排序
        //排序方式 默认倒叙
        if (isOrderBy) {
            String orderType = ORDER_TYPE_DESC;
            Field orderTypeField = fieldMap.get(ORDER_TYPE);
            if (orderTypeField != null) {
                orderTypeField.setAccessible(true);
                Object o = orderTypeField.get(entity);
                if (o != null) {
                    orderType = (String) o;
                }
                orderTypeField.setAccessible(false);
            }
            // 排序字段
            List<String> orderFieldList = Collections.singletonList(CREATED_AT);
            Field orderFields = fieldMap.get(ORDER_FIELDS);
            if (orderFields != null) {
                orderFields.setAccessible(true);
                Object o = orderFields.get(entity);
                if (o != null) {
                    orderFieldList = JSONUtil.parseArray(JSONUtil.toJsonStr(o)).toList(String.class);
                }
            }
            orderFieldList = orderFieldList.stream().map(UnderlineToCamelUtils::camelToUnderline).collect(Collectors.toList());
            // 按照创建时间倒叙
            queryWrapper.orderBy(CollUtil.isNotEmpty(orderFieldList), Objects.equals(orderType, ORDER_TYPE_DSC), orderFieldList);
        }

        // 删除已处理的字段
        fieldMap.remove(STARTED_AT);
        fieldMap.remove(ENDED_AT);
        fieldMap.remove(ORDER_TYPE);
        fieldMap.remove(ORDER_FIELDS);
        return fieldMap.values().toArray(new Field[0]);
    }

    /**
     * 设置字段查询条件
     *
     * @param queryWrapper     λ查询
     * @param fieldType       字段类型
     * @param tableFieldValue 表字段值
     * @param value           价值
     */
    public void setFieldQueryCriteria(QueryWrapper<T> queryWrapper, Class<?> fieldType, String tableFieldValue, Object value) {
        // 如果字段为字符串，则使用like
        if (fieldType == String.class && value instanceof String) {
            queryWrapper.like(StringUtils.isNotBlank(value.toString()), tableFieldValue, value);
        }else {
            queryWrapper.eq(Objects.nonNull(value),tableFieldValue, value);
        }
    }
}
