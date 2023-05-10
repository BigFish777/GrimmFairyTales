package cn.apotato.modules.kafka.config;

import cn.apotato.common.core.utils.SpringUtils;
import cn.apotato.modules.kafka.filter.KafkaConsumerMessageFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;

import javax.annotation.Resource;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Resource
    private ConsumerFactory consumerFactory;


    /**
     * 过滤容器工厂
     *
     * @return {@link ConcurrentKafkaListenerContainerFactory}
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory filterContainerFactory() {
        ConcurrentKafkaListenerContainerFactory factory = new ConcurrentKafkaListenerContainerFactory();
        factory.setConsumerFactory(consumerFactory);
        // 被过滤的消息将被丢弃
        factory.setAckDiscarded(true);
        // 获取Kafka消费者消息过滤器对象
        Map<String, KafkaConsumerMessageFilter> filterMap = SpringUtils.getBean(KafkaConsumerMessageFilter.class);
        // 如果过滤器为空则直接跳过不设置消息过滤策略
        if (filterMap.isEmpty()) {
            return factory;
        }
        // 消息过滤策略 返回true的时候消息将会被抛弃，返回false时，消息能正常抵达监听容器
        factory.setRecordFilterStrategy(consumerRecord -> {
            for (KafkaConsumerMessageFilter filter : filterMap.values()) {
                // 在过滤器链中只要有一个不符合条件的就过滤掉
                if (filter.handle(consumerRecord)) {
                    return true;
                }
            }
            //返回true消息则被过滤
            return false;
        });
        return factory;
    }

}
