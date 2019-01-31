package com.key.modules.redis;

import com.key.common.redis.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/demo")
public class DDController {

    private static Logger logger = LoggerFactory.getLogger(DDController.class);
    private static Logger logger_info = LoggerFactory.getLogger("custom_test");

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
        logger.info(name);
        logger.debug(name);
        logger.error(name);
        logger_info.error("自定义日志输出：异常-" + name);
        logger_info.info("自定义日志输出：正常--" + name);
        return "";
    }

}
