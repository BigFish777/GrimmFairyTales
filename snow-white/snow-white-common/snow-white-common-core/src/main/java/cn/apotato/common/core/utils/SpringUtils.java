package cn.apotato.common.core.utils;


import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Map;


/**
 * spring工具类 方便在非spring管理环境中获取bean
 *
 * @author 胡晓鹏
 * @date 2023/02/23
 */
@Component
public class SpringUtils implements BeanFactoryPostProcessor {

    /** Spring Bean工厂 */
    private static ConfigurableListableBeanFactory beanFactory;


    /**
     * Modify the application context's internal bean factory after its standard
     * initialization. All bean definitions will have been loaded, but no beans
     * will have been instantiated yet. This allows for overriding or adding
     * properties even to eager-initializing beans.
     *
     * @param beanFactory the bean factory used by the application context
     * @throws BeansException in case of errors
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        SpringUtils.beanFactory = beanFactory;
    }

    /**
     * 根据bean名称获取bean对象
     *
     * @param beanName bean名字
     * @return {@link T}
     */
    public static <T>  T getBean(String beanName) {
        if (beanFactory.containsBean(beanName)) {
            return (T) beanFactory.getBean(beanName);
        }
        return null;
    }

    /**
     * 根据bean类型获取全部的Bean
     *
     * @param beanType bean类型
     * @return {@link Map}<{@link String}, {@link T}>
     */
    public static <T> Map<String, T> getBean(Class<T> beanType) {
        return beanFactory.getBeansOfType(beanType);
    }

    /**
     * 注册豆
     *
     * @param beanName bean名字
     * @param obj      bean对象
     */
    public static void registerBean(String beanName, Object obj) {
        if (!beanFactory.containsBean(beanName)) {
            beanFactory.registerSingleton(beanName, obj);
        }
    }

}
