package cn.apotato.test.factory;

import cn.apotato.modules.kafka.filter.KafkaConsumerMessageFilter;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;


/**
 * 卡夫卡消费者消息过滤器演示
 *
 * @author 胡晓鹏
 * @date 2023/02/24
 */
@Component
public class KafkaConsumerMessageFilterDemo implements KafkaConsumerMessageFilter {
    /**
     * 处理消息记录
     *
     * @param consumerRecord 消费记录
     * @return boolean
     */
    @Override
    public boolean handle(ConsumerRecord consumerRecord) {
        if (Integer.parseInt(consumerRecord.value().toString()) % 2 == 0) {
            return false;
        }
        //返回true消息则被过滤
        return true;
    }
}
