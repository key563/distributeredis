package com.key.distributeredis.modules.redis;

import com.key.distributeredis.common.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class DDController {

    @Resource(name = "transactionalRedisTemplate")
    private RedisTemplate transactionalRedisTemplate;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private HashOperations<String, String, Object> hashOperations;

    @Autowired
    private ValueOperations<String, Object> valueOperations;

    @Autowired
    private RedisUtils redisUtils;

    @RequestMapping(value = "/getDemo/{id}", method = RequestMethod.POST)
    public String getDemo(String key, @PathVariable(name = "id") String id) {
        redisTemplate.opsForValue().set("test", "test");
        return "Yes";
    }
}
