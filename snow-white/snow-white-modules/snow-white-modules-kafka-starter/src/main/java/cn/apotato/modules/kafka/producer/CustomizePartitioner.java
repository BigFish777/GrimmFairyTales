package cn.apotato.modules.kafka.producer;

import cn.hutool.core.util.StrUtil;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;
import java.util.Objects;

/**
 * 自定义分区
 *
 * @author 胡晓鹏
 * @date 2023/02/24
 */
public class CustomizePartitioner implements Partitioner {
    @Override
    public int partition(String topic, Object key, byte[] bytes, Object o1, byte[] bytes1, Cluster cluster) {
        // 分区自定义规则， 例如我们的topic命名规则为以下格式：模块名_业务名。那我们可以共同的业务名称和key的放在同一个分区里面
        // 例如：
        if (StrUtil.isBlank(topic) || Objects.isNull(key)) {
            return 0;
        }
        String[] strings = topic.split("_");
        return (strings[0] + key).hashCode();
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
