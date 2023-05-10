package cn.apotato.modules.mybatis.plus.handler;

/**
 * 阵列处理器两个
 *
 * @author 胡晓鹏
 * @date 2023/04/21
 */
public class ArrayHandlerOfTwo extends TwoDimensionalArrayHandler<Double>{
    /**
     * 得到类型
     *
     * @return {@link Class}<{@link T}>
     */
    @Override
    protected Class<Double> getType() {
        return Double.class;
    }
}
