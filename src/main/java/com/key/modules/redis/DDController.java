package com.key.modules.redis;

import com.key.common.redis.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/demo")
public class DDController {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
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

    @RequestMapping(value = "/annotationTest")
    public String annotationTest(String name) {
        System.out.println(name);
        return "";
    }

}
