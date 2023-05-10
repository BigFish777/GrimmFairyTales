# 🥔一颗土豆极简开发框架（apotato-cloud）
## 😀项目介绍
本框架对常用的技术框架和业务进行了封装并提供了不错的自定义的钩子入口。可以实现搭积木一样的方式是开发属于你自己的程序。真的是轻松加愉快

## 🔥项目目录（持续更新中...）
**apotato-cloud**
- [**apotato-common**](./apotato-common)
  - [apotato-common-core](./apotato-common/apotato-common-core/README.md)
  - [apotato-common-kafka](./apotato-common/apotato-common-kafka/README.md)
  - [apotato-common-minio](./apotato-common/apotato-common-minio/README.md)
  - [apotato-common-model](./apotato-common/apotato-common-model/README.md)
  - [apotato-common-mybatisplus](./apotato-common/apotato-common-mybatisplus/README.md)
  - [apotato-common-redis](./apotato-common/apotato-common-redis/README.md)

- [**apotato-modules**](./apotato-modules)
  - [apotato-modules-test](./apotato-modules/apotato-modules-test/README.md)


## 📚使用案例
#### 安装和引用

在maven中引入一下的依赖

```xml
<properties>
    <apotato-common.version>1.0-SNAPSHOT</apotato-common.version>
</properties>

<dependencies>
    <!--  model  -->
    <dependency>
        <groupId>cn.apotato</groupId>
        <artifactId>apotato-common-model</artifactId>
        <version>${apotato-common.version}</version>
        <scope>compile</scope>
    </dependency>
    <!--  redis  -->
    <dependency>
        <groupId>cn.apotato</groupId>
        <artifactId>apotato-common-redis</artifactId>
        <version>${apotato-common.version}</version>
        <scope>compile</scope>
    </dependency>
    <!--  kafka  -->
    <dependency>
        <groupId>cn.apotato</groupId>
        <artifactId>apotato-common-kafka</artifactId>
        <version>${apotato-common.version}</version>
    </dependency>
    <!--  minio  -->
    <dependency>
        <groupId>cn.apotato</groupId>
        <artifactId>apotato-common-minio</artifactId>
        <version>${apotato-common.version}</version>
    </dependency>
    <!--  mybatisplus  -->
    <dependency>
        <groupId>cn.apotato</groupId>
        <artifactId>apotato-common-mybatisplus</artifactId>
        <version>${apotato-common.version}</version>
    </dependency>
    <!-- SpringBoot Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>cn.hutool</groupId>
        <artifactId>hutool-all</artifactId>
    </dependency>
    <!-- lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>
    <!--  postgresql  -->
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
    </dependency>

</dependencies>
```



#### 使用案例

##### BaseController的使用

**实体类**

```java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "account", autoResultMap = true)
public class Account extends BaseModel {

    /**
     * 账户
     */
    @NotBlank(message = "账号必填")
    private String account;

    /**
     * 密码
     */
    @NotBlank(message = "密码必填")
    private String password;

    /**
     * 电话
     */
    @NotEmpty(message = "电话必填")
    private String phone;

    /**
     * 用户名
     */
    private String username;

    /**
     * 邮件
     */
    private String mail;
    /**
     * 地址
     */
    private String address;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 微信openId
     */
    private String openId;
    /**
     * 小程序使用
     */
    private String sessionKey;

    /**
     * 微信公众号
     */
    private String unionId;

    /**
     * 微信公众号
     */
    private String publicOpenId;

    /**
     * 是否关注公众号
     */
    private Boolean subscribeFlag;


    /**
     * 组织id
     */
    @NotNull(message = "组织必填")
    private Long orgId;

    /**
     * 角色
     */
    @TableField(typeHandler = ArrayTypeHandler.class)
    private Long[] roleId;

    /**
     * 菜单
     */
    @TableField(typeHandler = ArrayTypeHandler.class)
    private String[] menuId;


    @TableField(exist = false)
    private Long rootOrgId;

}

```

```java
@EqualsAndHashCode(callSuper = true)
@Data
public class AccountDTO extends Account {

    /**
     * 角色名字
     */
    private String orgName;
}
```

```java
@Getter
@Setter
@Accessors(chain = true)
@TableName(value = "organization", schema = "forest")
public class Organization extends BaseModel {

    /**
     * 名称
     */
    @NotEmpty(message = "组织名称必填")
    private String name;

    /**
     * 上级组织id
     */
    @NotNull(message = "上级组织必填")
    private Long pid;

    /**
     * 联系人
     */
    private String personLiable;

    /**
     * 联系方式
     */
    private String contactInformation;

}

```

**mapper 需要实现MPJBaseMapper**
```java
@Mapper
public interface AccountMapper extends MPJBaseMapper<Account> {
}
```

```java
@Mapper
public interface OrganizationMapper extends MPJBaseMapper<Organization> {
}
```

```java

/**
 * BaseController和MPJBaseMapper测试
 *
 * @author 胡晓鹏
 * @date 2023/04/21
 */
@RequestMapping("account")
@RestController
public class AccountController extends BaseController<Account, Long> {

    @Resource
    private MPJBaseMapper<Account> mapper;

    public AccountController(IService<Account> service, MPJBaseMapper<Account> mapper) {
        super(service, mapper);
    }

    /**
     * 级联查询 MPJQueryWrapper
     * <a href="https://mybatisplusjoin.com/pages/core/lambda/select/select.html">MPJQueryWrapper文档</a>
     * ==> SELECT t.* o.NAME AS org_name FROM account t LEFT JOIN organization o ON o.id = t.org_id WHERE t.id = ?;
     * @param accountId 帐户id
     * @return {@link List}<{@link Map}<{@link String}, {@link Object}>>
     */
    @GetMapping("join")
    public List<Map<String, Object>> getAccountInfo(Long accountId) {
        return mapper.selectJoinMaps(new MPJQueryWrapper<Account>().selectAll(Account.class)
                .select("o.name as org_name")
                .leftJoin("forest.organization o on o.id = t.org_id")
                .eq(accountId != null,"t.id", accountId)
        );
    }

    /**
     * 级联查询 MPJLambdaWrapper的简单使用
     * <a href="https://mybatisplusjoin.com/pages/core/str/select.html">MPJLambdaWrapper文档</a>
     * ==> SELECT t.* o.NAME AS org_name FROM account t LEFT JOIN organization o ON o.id = t.org_id WHERE t.id = ?;
     * @param accountId 帐户id
     * @return {@link List}<{@link AccountDTO}>
     */
    @GetMapping("join-lamda")
    public List<AccountDTO> getAccountInfoLamda(Long accountId) {
        return JoinWrappers.lambda(Account.class)
                .selectAsClass(Account.class, AccountDTO.class)
                .selectAs(Organization::getName, AccountDTO::getOrgName)
                .leftJoin(Organization.class, Organization::getId, Account::getOrgId)
                .eq(accountId != null,"t.id", accountId)
                .list(AccountDTO.class);
    }



    // todo 三种不通颗粒度的查询过滤的钩子函数
    /**
     * 查询结果过滤
     *
     * @param page 页面
     * @return {@link IPage}<{@link Account}>
     */
    @Override
    public IPage<Account> queryResultFilterHook(IPage<Account> page) {
        return super.queryResultFilterHook(page);
    }

    /**
     * 查询结果过滤
     *
     * @param records 记录
     * @return {@link List}<{@link Account}>
     */
    @Override
    public List<Account> queryResultFilterHook(List<Account> records) {
        return records.stream()
                .peek(System.out::println)
                .filter(account -> account.getAccount() != null)
                .collect(Collectors.toList());
    }

    /**
     * 查询结果过滤
     *
     * @param account 对象
     * @return {@link Account}
     */
    @Override
    public Account queryResultFilterHook(Account account) {
        account.setAccount("张三");
        return account;
    }




    // todo: 查询条件设置

    /**
     * 设置查询参数钩
     * 设置查询参数钩子函数
     * 设置请求的参数 {@link Account}
     *
     * @param account 查询参数
     * @return {@link Account}
     */
    @Override
    public Account setQueryParamHook(Account account) {
        // 例如：设置查询条件 orgId = 1
        account.setOrgId(0L);

        // 例如：将用户名称都加上统一字符“xxx-”
        String str = "xxx-";
        if (StringUtils.isNotBlank(account.getNickname())) {
            account.setNickname(str + account.getNickname());
        }
        return account;
    }

    /**
     * 设置查询参数钩子函数
     *
     * @param lambdaQueryWrapper 查询包装
     * @return {@link QueryWrapper}<{@link Account}>
     */
    @Override
    public LambdaQueryWrapper<Account> setQueryCriteriaHook(LambdaQueryWrapper<Account> lambdaQueryWrapper) {
        return lambdaQueryWrapper.isNotNull(Account::getAccount);
    }

}

```



##### kafka的使用

###### 配置

```yaml
 spring:
    kafka:
        #kafka集群地址
        bootstrap-servers: apotato.cn:9093
        #kafka自定义扩展
        #创建topic的分区数
        numPartitions: 8
        #创建topic的副本数
        replicationFactor: 1
        #自动创建topic所需要的topic名称集合
        topics: test1,test2
        #初始化消费者配置
        consumer:
          #当kafka中没有初始offset或offset超出范围时将自动重置offset
          # earliest:重置为分区中最小的offset;
          # latest:重置为分区中最新的offset(消费分区中新产生的数据);
          # none:只要有一个分区不存在已提交的offset,就抛出异常;
          auto-offset-reset: latest
          #是否自动提交offset
          enable-auto-commit: true
          #ky序列化
          key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
          value-deserializer: org.apache.kafka.common.serialization.StringDeserializer
          properties:
            #默认的消费组ID
            group:
              id: testGroup
            #消费请求超时时间
            request:
              timeout:
                ms: 180000
            #消费会话超时时间(超过这个时间consumer没有发送心跳,就会触发rebalance操作)
            session:
              timeout:
                ms: 120000
          #提交offset延时(接收到消息后多久提交offset)
          auto:
            commit:
              interval:
                ms: 1000
          #批量消费每次最多消费多少条消息（需要配置批量消费开关）
          #max-poll-records: 50
        #初始化生产者配置
        producer:
          #应答级别:多少个分区副本备份完成时向生产者发送ack确认(可选0、1、all/-1)
          acks: 1
          #批量大小
          batch-size: 16384
          #生产端缓冲区大小
          buffer-memory: 33554432
          #重试次数
          retries: 0
          #提交延时
          # 当生产端积累的消息达到batch-size或接收到消息linger.ms后,生产者就会将消息提交给kafka
          # linger.ms为0表示每接收到一条消息就提交给kafka,这时候batch-size其实就没用了
          properties:
            linger:
              ms: 0
          #Kafka提供的序列化和反序列化类
          key-serializer: org.apache.kafka.common.serialization.StringSerializer
          value-serializer: org.apache.kafka.common.serialization.StringSerializer
        listener:
          #消费端监听的topic不存在时，项目启动会报错(关掉)
          missing-topics-fatal: false
          #设置批量消费
          #type: batch
```



###### 生产者和消费者

```java
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
```



##### Redis的使用

###### 配置

```yaml
spring:
  redis:
    # Redis数据库索引（默认为0）
    database: 0
    # Redis服务器地址
    host: 127.0.0.1
    # Redis服务器连接端口
    port: 63790
    # 密码
    password: 123456
    jedis:
      pool:
        # 连接池最大连接数（使用负值表示没有限制）
        max-active: 8
        # 连接池最大阻塞等待时间（使用负值表示没有限制）
        max-wait: 1
        # 连接池中的最大空闲连接
        max-idle: 8
        # 连接池中的最小空闲连接
        min-idle: 1
    # 连接超时时间（毫秒）
    timeout: 5000
```



###### 使用

````java
@AllArgsConstructor
@RestController
public class RedisController {
    private final RedisService redisService;

    /**
     *  模拟请求，如果缓存中有数据则使用redis缓存中的数据，如果没有则查询并放入缓存
     * 获取数据
     *
     * @param id id
     * @return {@link Object}
     */
    @GetMapping
    public Object getData(String id) {
        Object obj;
        // redis中存在则返回数据
        if (redisService.hasKey(id)) {
            return redisService.getValue(id);
        }
        // 模拟获取业务中返回的数据
        Dict data = Dict.create().set("id", "ID121212").set("name", "张三");
        redisService.setValue(id, data, 3600L, TimeUnit.SECONDS);
        return data;
    }
}
````





##### Minio的使用

###### 配置

```yaml
# minio
minio:
  #minio服务地址
  endpoint: ${MINIO_URL:http://10.4.5.161:9000}
  #账户
  accessKey: ${MINIO_ACCESS_KEY:AKIAIOSFODNN7EXAMPLE}
  #密码
  secretKey: ${MINIO_SECRET_KEY:wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY}
  #桶名称
  bucketName: ${MINIO_BUCKET_NAME:test}
```



###### 使用

````java
@AllArgsConstructor
@RequestMapping("minio")
@RestController
public class MinioController {

    private final MinioTemplate minioTemplate;


    /**
     * 上传文件
     *
     * @param file 文件
     * @return {@link String}
     */
    @PostMapping("upload")
    public String uploadFile(MultipartFile file){
        return minioTemplate.upload(file);
    }


    /**
     * 下载文件
     *
     * @param fileName 文件名称
     * @param response 响应
     */
    @GetMapping("download")
    public void downloadFile(String fileName, HttpServletResponse response){
        minioTemplate.download(fileName, response);
    }

    /**
     * 预览文件
     *
     * @param fileName 文件名称
     * @param response 响应
     */
    @GetMapping("preview")
    public void previewFile(String fileName, HttpServletResponse response){
        minioTemplate.download(fileName, MinioTemplate.PREVIEW,response);
    }

}
````

