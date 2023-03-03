package cn.apotato.common.kafka.config;

import cn.apotato.common.kafka.properties.KafkaExtendProperties;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@EnableConfigurationProperties({KafkaExtendProperties.class, KafkaProperties.class})
@Import({KafkaConsumerConfig.class, KafkaTopicConfig.class})
@Configuration
public class KafkaConfig {

    private final KafkaProperties kafkaProperties;
    private final KafkaExtendProperties properties;

    public KafkaConfig(KafkaProperties kafkaProperties, KafkaExtendProperties properties) {
        this.kafkaProperties = kafkaProperties;
        this.properties = properties;
    }

    /**
     * 创建一个kafka管理类，相当于rabbitMQ的管理类rabbitAdmin,没有此bean无法自定义的使用adminClient创建topic
     *
     * @return {@link KafkaAdmin}
     */
    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> props = new HashMap<>();
        //配置Kafka实例的连接地址
        // kafka的地址，不是zookeeper
        StringBuilder bootstrapServers = new StringBuilder();
        for (String server : properties.getBootstrapServers()) {
            bootstrapServers.append(server).append(",");
        }
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers.substring(0, bootstrapServers.lastIndexOf(",")));
        return new KafkaAdmin(props);
    }

    /**
     * kafka客户端，在spring中创建这个bean之后可以注入并且创建topic
     *
     * @return {@link AdminClient}
     */
    @Bean
    public AdminClient adminClient() {
        return AdminClient.create(kafkaAdmin().getConfigurationProperties());
    }

    /**
     * 根据生产者工厂构建kafkaTemplate
     *
     * @param producerFactory 生产工厂
     * @return {@link KafkaTemplate}<{@link String}, {@link String}>
     */
    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    /**
     * 生产工厂
     *
     * @return {@link ProducerFactory}<{@link String}, {@link String}>
     */
    @Bean
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(kafkaProperties.buildProducerProperties());
    }

}
