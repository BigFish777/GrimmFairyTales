# 公共模块Kafka
## 📚使用手册
### 依赖引用
```xml
    <parent>
        <groupId>cn.apotato</groupId>
        <artifactId>apotato-common-kafka</artifactId>
        <version>1.0-SNAPSHOT</version>
    </parent>
```

### 🎁kafka配置文件
```properties
# kafka集群地址
spring.kafka.bootstrap-servers=112.126.74.249:9092,112.126.74.249:9093
# 初始化生产者配置
# 重试次数
spring.kafka.producer.retries=0
# 应答级别:多少个分区副本备份完成时向生产者发送ack确认(可选0、1、all/-1)
spring.kafka.producer.acks=1
# 批量大小
spring.kafka.producer.batch-size=16384
# 提交延时
spring.kafka.producer.properties.linger.ms=0
# 当生产端积累的消息达到batch-size或接收到消息linger.ms后,生产者就会将消息提交给kafka
# linger.ms为0表示每接收到一条消息就提交给kafka,这时候batch-size其实就没用了
# 生产端缓冲区大小
spring.kafka.producer.buffer-memory = 33554432
# Kafka提供的序列化和反序列化类
spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.apache.kafka.common.serialization.StringSerializer
# 自定义分区器
# spring.kafka.producer.properties.partitioner.class=com.felix.kafka.producer.CustomizePartitioner
# 初始化消费者配置
# 默认的消费组ID
spring.kafka.consumer.properties.group.id=defaultConsumerGroup
# 是否自动提交offset
spring.kafka.consumer.enable-auto-commit=true
# 提交offset延时(接收到消息后多久提交offset)
spring.kafka.consumer.auto.commit.interval.ms=1000
# 当kafka中没有初始offset或offset超出范围时将自动重置offset
# earliest:重置为分区中最小的offset;
# latest:重置为分区中最新的offset(消费分区中新产生的数据);
# none:只要有一个分区不存在已提交的offset,就抛出异常;
spring.kafka.consumer.auto-offset-reset=latest
# 消费会话超时时间(超过这个时间consumer没有发送心跳,就会触发rebalance操作)
spring.kafka.consumer.properties.session.timeout.ms=120000
# 消费请求超时时间
spring.kafka.consumer.properties.request.timeout.ms=180000
# Kafka提供的序列化和反序列化类
spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.value-deserializer=org.apache.kafka.common.serialization.StringDeserializer
# 消费端监听的topic不存在时，项目启动会报错(关掉)
spring.kafka.listener.missing-topics-fatal=false
# 设置批量消费
# spring.kafka.listener.type=batch
# 批量消费每次最多消费多少条消息
# spring.kafka.consumer.max-poll-records=50

# 自定义的扩展
# 自动创建topic所需要的topic名称集合
spring.kafka.topics=test1,test2
# 创建topic的分区数
spring.kafka.numPartitions=10
# 创建topic的副本数
spring.kafka.replicationFactor=2
```
### 🍺Hello Kafka
**简单的生产者**
```java
@RestController
public class KafkaProducer {
    @Autowired
    private KafkaProducer kafkaProducer;
    // 发送消息
    @GetMapping("/kafka/normal/{message}")
    public void sendMessage1(@PathVariable("message") String normalMessage) {
        kafkaProducer.sendMessage("topic1", normalMessage);
    }
}
```
**简单消费**
```java
@Component
public class KafkaConsumer {
    // 消费监听
    @KafkaListener(topics = {"topic1"})
    public void onMessage1(ConsumerRecord<?, ?> record){
        // 消费的哪个topic、partition的消息,打印出消息内容
        System.out.println("简单消费："+record.topic()+"-"+record.partition()+"-"+record.value());
    }
}
```

### 🛠 生产者
#### 带回调的生产者
```java
@RestController
public class KafkaProducer {
    @Autowired
    private KafkaProducer kafkaProducer;
    // 发送消息
    @GetMapping("/kafka/callbackOne/{message}}")
    public void sendMessage2(@PathVariable("message") String normalMessage) {
        kafkaProducer.sendMessage("topic1", normalMessage).addCallback(success -> {
            // 消息发送到的topic
            String topic = success.getRecordMetadata().topic();
            // 消息发送到的分区
            int partition = success.getRecordMetadata().partition();
            // 消息在分区内的offset
            long offset = success.getRecordMetadata().offset();
            System.out.println("发送消息成功:" + topic + "-" + partition + "-" + offset);
        }, failure -> {
            System.out.println("发送消息失败:" + failure.getMessage());
        });
    }
}
```
#### 事务提交
```java
@RestController
public class KafkaProducer {
    @Autowired
    private KafkaProducer kafkaProducer;
    // 发送消息
    @GetMapping("/kafka/callbackOne/{message}}")
    public void sendMessage2(@PathVariable("message") String normalMessage) {
        kafkaProducer.sendMessageInTransaction("topic1", normalMessage).addCallback(success -> {
            // 消息发送到的topic
            String topic = success.getRecordMetadata().topic();
            // 消息发送到的分区
            int partition = success.getRecordMetadata().partition();
            // 消息在分区内的offset
            long offset = success.getRecordMetadata().offset();
            System.out.println("发送消息成功:" + topic + "-" + partition + "-" + offset);
        }, failure -> {
            System.out.println("发送消息失败:" + failure.getMessage());
        });
        throw new RuntimeException("出现一个异常！");
    }
}
```

#### 自定义分区器

我们知道，kafka中每个topic被划分为多个分区，那么生产者将消息发送到topic时，具体追加到哪个分区呢？这就是所谓的分区策略，Kafka 为我们提供了默认的分区策略，同时它也支持自定义分区策略。其路由机制为：

① 若发送消息时指定了分区（即自定义分区策略），则直接将消息append到指定分区；

② 若发送消息时未指定 patition，但指定了 key（kafka允许为每条消息设置一个key），则对key值进行hash计算，根据计算结果路由到指定分区，这种情况下可以保证同一个 Key 的所有消息都进入到相同的分区；

③  patition 和 key 都未指定，则使用kafka默认的分区策略，轮询选出一个 patition；

※ 我们来自定义一个分区策略，将消息发送到我们指定的partition，首先新建一个分区器类实现Partitioner接口，重写方法，其中partition方法的返回值就表示将消息发送到几号分区，

```java
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

```
在application.propertise中配置自定义分区器，配置的值就是分区器类的全路径名
```properties
# 自定义分区器
spring.kafka.producer.properties.partitioner.class=cn.apotato.common.kafka.producer.CustomizePartitioner
```

### 🏗 消费者
#### 指定topic、partition、offset消费
前面我们在监听消费topic1的时候，监听的是topic1上所有的消息，如果我们想指定topic、指定partition、指定offset来消费呢？也很简单，@KafkaListener注解已全部为我们提供，
```java
/**
 * 指定topic、partition、offset消费
 * 同时监听topic1和topic2，监听topic1的0号分区、topic2的 "0号和1号" 分区，指向1号分区的offset初始值为8
 * 注意：topics和topicPartitions不能同时使用
 **/
@KafkaListener(id = "consumer1",groupId = "felix-group",topicPartitions = {
        @TopicPartition(topic = "topic1", partitions = { "0" }),
        @TopicPartition(topic = "topic2", partitions = "0", partitionOffsets = @PartitionOffset(partition = "1", initialOffset = "8"))
})
public void onMessage2(ConsumerRecord<?, ?> record) {
    System.out.println("topic:"+record.topic()+"|partition:"+record.partition()+"|offset:"+record.offset()+"|value:"+record.value());
}
```
**属性解释：**
- id：消费者ID；
- groupId：消费组ID；
- topics：监听的topic，可监听多个；
- topicPartitions：可配置更加详细的监听信息，可指定topic、parition、offset监听。


#### 批量消费
```properties
# 设置批量消费
spring.kafka.listener.type=batch
# 批量消费每次最多消费多少条消息
spring.kafka.consumer.max-poll-records=50
```

接收消息时用List来接收，监听代码如下👇:
```java
@KafkaListener(id = "consumer2",groupId = "felix-group", topics = "topic1")
public void onMessage3(List<ConsumerRecord<?, ?>> records) {
    System.out.println(">>>批量消费一次，records.size()="+records.size());
    for (ConsumerRecord<?, ?> record : records) {
        System.out.println(record.value());
    }
}
```

#### ConsumerAwareListenerErrorHandler 异常处理器
通过异常处理器，我们可以处理consumer在消费时发生的异常。

新建一个 ConsumerAwareListenerErrorHandler 类型的异常处理方法，用@Bean注入，BeanName默认就是方法名，然后我们将这个异常处理器的BeanName放到@KafkaListener注解的errorHandler属性里面，当监听抛出异常的时候，则会自动调用异常处理器，

```java
// 新建一个异常处理器，用@Bean注入
@Bean
public ConsumerAwareListenerErrorHandler consumerAwareErrorHandler() {
    return (message, exception, consumer) -> {
        System.out.println("消费异常："+message.getPayload());
        return null;
    };
}

// 将这个异常处理器的BeanName放到@KafkaListener注解的errorHandler属性里面
@KafkaListener(topics = {"topic1"},errorHandler = "consumerAwareErrorHandler")
public void onMessage4(ConsumerRecord<?, ?> record) throws Exception {
    throw new Exception("简单消费-模拟异常");
}

// 批量消费也一样，异常处理器的message.getPayload()也可以拿到各条消息的信息
@KafkaListener(topics = "topic1",errorHandler="consumerAwareErrorHandler")
public void onMessage5(List<ConsumerRecord<?, ?>> records) throws Exception {
    System.out.println("批量消费一次...");
    throw new Exception("批量消费-模拟异常");
}
```


#### KafkaConsumerMessageFilter 消息过滤器 
消息过滤器可以在消息抵达consumer之前被拦截，在实际应用中，我们可以根据自己的业务逻辑，筛选出需要的信息再交由KafkaListener处理，不需要的消息则过滤掉。
配置消息过滤只需要 配置一个或多个 实现[KafkaConsumerMessageFilter.java](src%2Fmain%2Fjava%2Fcn%2Fapotato%2Fcommon%2Fkafka%2Ffilter%2FKafkaConsumerMessageFilter.java)（消息过滤策器）接口的Bean，返回true的时候消息将会被抛弃，返回false时，消息能正常抵达监听容器

```java
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
    public boolean handle(ConsumerRecord<String, Object> consumerRecord) {
        if (Integer.parseInt(consumerRecord.value().toString()) % 2 == 0) {
            return false;
        }
        //返回true消息则被过滤
        return true;
    }
}

```

**消息过滤监听**
```java
@KafkaListener(topics = {"topic1"},containerFactory = "filterContainerFactory")
public void onMessage6(ConsumerRecord<?, ?> record) {
    System.out.println(record.value());
}
```

#### @SendTo 消息转发
在实际开发中，我们可能有这样的需求，应用A从TopicA获取到消息，经过处理后转发到TopicB，再由应用B监听处理消息，即一个应用处理完成后将该消息转发至其他应用，完成消息的转发。
在SpringBoot集成Kafka实现消息的转发也很简单，只需要通过一个@SendTo注解，被注解方法的return值即转发的消息内容，如下
```java
/**
 * 消息转发
 * 从topic1接收到的消息经过处理后转发到topic2
 * @return void
 **/
@KafkaListener(topics = {"topic1"})
@SendTo("topic2")
public String onMessage7(ConsumerRecord<?, ?> record) {
    return record.value()+"-forward message";
}
```

#### 定时启动、停止监听器
默认情况下，当消费者项目启动的时候，监听器就开始工作，监听消费发送到指定topic的消息，那如果我们不想让监听器立即工作，想让它在我们指定的时间点开始工作，或者在我们指定的时间点停止工作，该怎么处理呢——使用KafkaListenerEndpointRegistry，下面我们就来实现：
1. 禁止监听器自启动；
2. 创建两个定时任务，一个用来在指定时间点启动定时器，另一个在指定时间点停止定时器；

新建一个定时任务类，用注解@EnableScheduling声明，KafkaListenerEndpointRegistry 在SpringIO中已经被注册为Bean，直接注入，设置禁止KafkaListener自启动，

```java
@EnableScheduling
@Component
public class CronTimer {
    /**
     * @KafkaListener注解所标注的方法并不会在IOC容器中被注册为Bean，
     * 而是会被注册在KafkaListenerEndpointRegistry中，
     * 而KafkaListenerEndpointRegistry在SpringIOC中已经被注册为Bean
     **/
    @Autowired
    private KafkaListenerEndpointRegistry registry;

    @Autowired
    private ConsumerFactory consumerFactory;

    // 监听器容器工厂(设置禁止KafkaListener自启动)
    @Bean
    public ConcurrentKafkaListenerContainerFactory delayContainerFactory() {
        ConcurrentKafkaListenerContainerFactory container = new ConcurrentKafkaListenerContainerFactory();
        container.setConsumerFactory(consumerFactory);
        //禁止KafkaListener自启动
        container.setAutoStartup(false);
        return container;
    }

    // 监听器
    @KafkaListener(id="timingConsumer",topics = "topic1",containerFactory = "delayContainerFactory")
    public void onMessage1(ConsumerRecord<?, ?> record){
        System.out.println("消费成功："+record.topic()+"-"+record.partition()+"-"+record.value());
    }

    // 定时启动监听器
    @Scheduled(cron = "0 42 11 * * ? ")
    public void startListener() {
        System.out.println("启动监听器...");
        // "timingConsumer"是@KafkaListener注解后面设置的监听器ID,标识这个监听器
        if (!registry.getListenerContainer("timingConsumer").isRunning()) {
            registry.getListenerContainer("timingConsumer").start();
        }
        //registry.getListenerContainer("timingConsumer").resume();
    }

    // 定时停止监听器
    @Scheduled(cron = "0 45 11 * * ? ")
    public void shutDownListener() {
        System.out.println("关闭监听器...");
        registry.getListenerContainer("timingConsumer").pause();
    }
}
```