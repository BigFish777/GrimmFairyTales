package cn.apotato.modules.kafka.config;

import cn.apotato.modules.kafka.properties.KafkaExtendProperties;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.errors.TopicExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;


@Configuration
public class KafkaTopicConfig {

    private final Logger logger = LoggerFactory.getLogger(KafkaTopicConfig.class);

    private final KafkaExtendProperties properties;
    private final AdminClient adminClient;

    public KafkaTopicConfig(KafkaExtendProperties properties, AdminClient adminClient) {
        this.properties = properties;
        this.adminClient = adminClient;
    }

    @PostConstruct
    public void init() throws ExecutionException, InterruptedException {
        initKafkaTopic();
    }

    /**
     * 初始化Kafka主题
     *
     * @throws ExecutionException   执行异常
     * @throws InterruptedException 中断异常
     */
    public void initKafkaTopic() throws ExecutionException, InterruptedException {
        // 注册topic
        List<String> topics = properties.getTopics();
        if (topics != null) {

            // 查询已存在的Topic
            Map<String, Boolean> existsTopicMap = new HashMap<>();
            try {
                Set<String> existsTopicSet = adminClient.listTopics().names().get();
                existsTopicSet.forEach(s -> existsTopicMap.put(s, Boolean.TRUE));
            }catch (Exception e) {
                logger.warn(e.getMessage());
            }

            List<NewTopic> newTopics = topics.stream()
                    // 过滤掉已存在的topic
                    .filter(topic -> Objects.isNull(existsTopicMap.get(topic)))
                    .map(topic -> new NewTopic(topic, properties.getNumPartitions(), properties.getReplicationFactor()))
                    .collect(Collectors.toList());
            CreateTopicsResult topicsResult = adminClient.createTopics(newTopics);
            // 查询创建结果是否出现异常情况，这里是个坑如果不写一下的代码创建失败也不会抛异常信息
            try {
                topicsResult.all().get();
            } catch (TopicExistsException e) {
                logger.warn(e.getMessage());
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 查询结果
        ListTopicsResult result = adminClient.listTopics();
        logger.info("当前所有的topic: " + result.names().get());
    }
}
