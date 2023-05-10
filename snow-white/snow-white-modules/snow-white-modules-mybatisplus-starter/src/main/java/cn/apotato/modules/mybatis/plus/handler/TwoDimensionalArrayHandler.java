package cn.apotato.modules.mybatis.plus.handler;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.*;

/**
 * 二维数组处理程序
 *
 * @author 胡晓鹏
 * &#064;date  2023/04/17
 */
public abstract class TwoDimensionalArrayHandler<T> implements TypeHandler<T[][]> {

    /**
     * 得到类型
     *
     * @param type 类型
     * @return {@link Class}<{@link T}>
     */
    abstract protected Class<T> getType();

    /**
     * 设置参数
     *
     * @param preparedStatement 事先准备好声明中
     * @param i                 我
     * @param parameter         参数
     * @param jdbcType          jdbc类型
     * @throws SQLException sqlexception异常
     */
    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, T[][] parameter, JdbcType jdbcType) throws SQLException {
        try (Connection connection = preparedStatement.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            String databaseProductName = metaData.getDatabaseProductName();
            if (StringUtils.isNotBlank(databaseProductName)) {
                Array array = connection.createArrayOf("float8", parameter);
                switch (databaseProductName) {
                    case "mysql":
                        array = connection.createArrayOf("double", parameter);
                        break;
                    case "postgresql":
                        array = connection.createArrayOf("float8", parameter);
                        break;
                    default: break;
                }
                preparedStatement.setArray(i, array);
            }
        }
    }

    @Override
    public T[][] getResult(ResultSet resultSet, String columnName) throws SQLException {
        return getArray(resultSet.getArray(columnName));
    }

    @Override
    public T[][] getResult(ResultSet resultSet, int i) throws SQLException {
        return getArray(resultSet.getArray(i));
    }

    @Override
    public T[][] getResult(CallableStatement callableStatement, int i) throws SQLException {
        return getArray(callableStatement.getArray(i));
    }

    private T[][] getArray(Array array) throws SQLException {
        if (array == null) { return null;}
        Object[] rows = (Object[]) array.getArray();
        int rowCount  = rows.length;
        int columnCount = ((Object[]) rows[0]).length;
        // 创建二维数组并转换类型
        T[][] result = (T[][]) java.lang.reflect.Array.newInstance(getType(), rowCount, columnCount);
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                Object o = ((Object[]) rows[i])[j];
                result[i][j] = (T) o;
            }
        }
        return (T[][]) result;
    }
}
