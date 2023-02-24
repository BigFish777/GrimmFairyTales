package cn.apotato.common.kafka.filter;

import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * Kafka消费者消息过滤器
 *
 * @author 胡晓鹏
 * @date 2023/02/24
 */
@FunctionalInterface
public interface KafkaConsumerMessageFilter {

    /**
     * 返回true的时候消息将会被抛弃，返回false时，消息能正常抵达监听容器
     * @param consumerRecord 消费记录
     * @return boolean
     */
    boolean handle(ConsumerRecord consumerRecord);
}
