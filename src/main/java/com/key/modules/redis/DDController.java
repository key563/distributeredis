package com.key.modules.redis;

import com.key.common.redis.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class DDController {

    private final Logger logger = LoggerFactory.getLogger(DDController.class);
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

    @RequestMapping(value = "/getDemo/{id}")
    public String getDemo(String key, @PathVariable(name = "id") String id) {
        redisTemplate.opsForValue().set("test", "test");
        String result = "";
        Boolean flag = redisUtils.setNx("setNxTest", id);
        System.out.println(flag);
        result = flag.toString();
        return result;
    }

    @RequestMapping(value = "/log")
    public void logTest() {
        String name = "阿萨德科技和";
        int age = 32;
        logger.trace(name);
        logger.debug(name);
        logger.info(name);
        logger.warn(name);
        logger.error(name);

        logger.info("name:" + name + " , age:" + age);
        logger.info("name:{} , age:{}", name, age);
    }

}
