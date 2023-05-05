package cn.apotato.modules.test.controller;

import cn.apotato.common.kafka.producer.KafkaProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * kafka生产者和消费的使用
 *
 * @author 胡晓鹏
 * @date 2023/05/04
 */
@Slf4j
@RequestMapping("kafka")
@RestController
public class KafkaController {

    @Resource
    private KafkaProducer kafkaProducer;

    @GetMapping("send")
    public String sendMessage(String msg) {
        log.info("收到消息：{}", msg);
        kafkaProducer.sendMessage("test1", msg).addCallback(
                success -> {
                    // 消息发送到的topic
                    String topic = success.getRecordMetadata().topic();
                    // 消息发送到的分区
                    int partition = success.getRecordMetadata().partition();
                    // 消息在分区内的offset
                    long offset = success.getRecordMetadata().offset();
                    log.info("发送消息成功: topic={}, partition={}， offset={}",topic ,partition ,offset);
                },
                error -> {}
        );
        return msg;
    }

    /**
     * 消费消息
     *
     * @param record 记录
     */
    @KafkaListener(topics = {"test1"})
    public void onMessage1(ConsumerRecord<?, ?> record){
        // 消费的哪个topic、partition的消息,打印出消息内容
        log.info("简单消费：topic={}, partition={}, value={}", record.topic(), record.partition(), record.value());
    }


}
