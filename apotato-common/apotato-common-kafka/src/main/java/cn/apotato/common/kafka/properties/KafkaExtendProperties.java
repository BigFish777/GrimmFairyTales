package cn.apotato.common.kafka.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ConfigurationProperties(prefix = "spring.kafka")
public class KafkaExtendProperties {
    /**
     * 主题
     */
    private List<String> topics;

    /**
     * 分区数
     * 分区数和生产者消费者业务有关，需要自己理解并设置合理的值
     * 每个topic通过若干个partition来分治，核心目的除了分摊单个broker的压力(IO压力、存储压力、连接压力等)外，
     * 还有一个很重要的原因是给后续的计算引擎提供一个很好的[并行度]，让数据处理的效率更高。
     */
    private Integer numPartitions = 2;
    /**
     * 副本数
     */
    private short replicationFactor = 1;

    private List<String> bootstrapServers = new ArrayList<>(Collections.singletonList("localhost:9092"));

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    public Integer getNumPartitions() {
        return numPartitions;
    }

    public void setNumPartitions(Integer numPartitions) {
        this.numPartitions = numPartitions;
    }

    public short getReplicationFactor() {
        return replicationFactor;
    }

    public void setReplicationFactor(short replicationFactor) {
        this.replicationFactor = replicationFactor;
    }

    public List<String> getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(List<String> bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }
}
