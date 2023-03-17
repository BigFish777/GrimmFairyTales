# 公共模块Redis
## 📚使用手册
### 依赖引用
```xml
    <parent>
        <groupId>cn.apotato</groupId>
        <artifactId>apotato-common-redis</artifactId>
        <version>1.0-SNAPSHOT</version>
    </parent>
```

### 🎁Redis配置文件

```yaml
spring:
  redis:
    # Redis数据库索引（默认为0）
    database: 0
    # Redis服务器地址
    host: 127.0.0.1
    # Redis服务器连接端口
    port: 6379
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

### 🍺Hello Redis

**搭建Redis环境**

使用docker搭建一个简答的单机环境

```
version: '3.3'
services:
  redis:
    image: redis
    restart: always
    hostname: redis
    container_name: redis
    privileged: true
    ports:
      - 6379:6379
    environment:
      TZ: Asia/Shanghai
```


**Redis的使用**

```java
@RestController
public class RedisTestController {
    @Autowired
    private RedisService redisService;
    
    // 模拟请求，如果缓存中有数据则使用redis缓存中的数据，如果没有则查询并放入缓存
    @GetMapping
    public Object getData(String id) {
        Object obj;
        // redis中存在则返回数据
        if (redisService.hasKey(id)) {
            return redisService.getValue(id);
        }
        // 模拟获取业务中返回的数据
        Object data = service.getDataById(id);
        redisService.setValue(id, data, 3600L, TimeUnit.SECONDS);
        return data;
    }
}
```

**当然也可以是使用RedisTemplate**
```java
@RestController
public class RedisTestController {
    @Autowired
    private RedisTemplate redisTemplate;
    
    // 模拟请求，如果缓存中有数据则使用redis缓存中的数据，如果没有则查询并放入缓存
    @GetMapping
    public Object getData(String id) {
        Object obj;
        // redis中存在则返回数据
        if (redisTemplate.hasKey(id)) {
            return redisTemplate.opsForValue().get(id);
        }
        // 模拟获取业务中返回的数据
        Object data = service.getDataById(id);
        redisTemplate.opsForValue().set(id, data, 3600L, TimeUnit.SECONDS);
        return data;
    }
}
```