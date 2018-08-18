package com.key.distributeredis.modules.redis;

import com.key.distributeredis.common.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class DDController {

    @Resource(name = "transactionalRedisTemplate")
    private RedisTemplate redisTemplate1;

    @Autowired
    private HashOperations<String, String, Object> hashOperations;

    @Autowired
    private ValueOperations<String,Object> valueOperations;

    @Autowired
    private RedisUtils redisUtils;

    @RequestMapping("/getDemo")
    public String getDemo(String key){
//        WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
//        RedisUtils redisUtils1 = context.getBean(RedisUtils.class);

        return "Yes";
    }
}
