package com.key.distributeredis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DistributeredisApplicationTests {

    @Test
    public void contextLoads() {

    }

    public Jedis initRedis() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxWaitMillis(1000);
        jedisPoolConfig.setMaxIdle(6);
        jedisPoolConfig.setMaxTotal(6);
        JedisPool pool = new JedisPool(jedisPoolConfig, "127.0.0.1", 6379, 0, "123456", 0);
        return pool.getResource();
    }

    @Test
    public void pushTest() {

        Jedis jedis = initRedis();
        //循环往redis中插入数据
        for (int i = 0; i < 100; i++) {
            jedis.lpush("orderId_" + i, "测试redis持久化" + i);
            jedis.lpush("orderId_" + i, "测试redis持久化" + i + 1);
            jedis.lpush("orderId_" + i, "测试redis持久化" + i + 2);
            jedis.lpush("orderId_" + i, "测试redis持久化" + i + 3);
            jedis.lpush("orderId_" + i, "测试redis持久化" + i + 4);
        }
    }


    @Test
    public void redisconnectTest(){
//        for(int i = 0 ; i<50; i++){
//            new RedisUtils().setObject("test201"+i,"q12312sss32");
//        }
    }


}
