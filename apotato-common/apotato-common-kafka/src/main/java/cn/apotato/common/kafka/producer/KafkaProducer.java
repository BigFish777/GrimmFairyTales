package cn.apotato.common.kafka.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import javax.annotation.Resource;

@Component
public class KafkaProducer {

    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * 发送消息
     *
     * @param topic   主题
     * @param message 消息
     * @return
     */
    public ListenableFuture<SendResult<String, Object>> sendMessage(String topic, Object message) {
        return kafkaTemplate.send(topic, message);
    }

    /**
     * 发送消息事务
     * 使用事务发送消息，后面报错消息不会发出去；不声明事务：后面报错但前面消息已经发送成功了
     * @param topic   主题
     * @param message 消息
     * @return {@link ListenableFuture}<{@link SendResult}<{@link String}, {@link Object}>>
     */
    public ListenableFuture<SendResult<String, Object>> sendMessageInTransaction(String topic, Object message) {
        return kafkaTemplate.executeInTransaction(operations -> operations.send(topic, message));
    }
}
