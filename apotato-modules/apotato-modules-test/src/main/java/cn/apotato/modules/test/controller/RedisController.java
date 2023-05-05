package cn.apotato.modules.test.controller;

import cn.apotato.common.redis.service.RedisService;
import cn.hutool.core.lang.Dict;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * Redis测试
 *
 * @author 胡晓鹏
 * @date 2023/05/04
 */
@AllArgsConstructor
@RequestMapping("redis")
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
