package com.key.distributeredis.modules.redis;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

public class redisTest {
    public static void main(String[] args) throws URISyntaxException {
        Date date = new Date();

        ReentrantLock ss = new ReentrantLock();
        Config config = new Config();

        URI uri = new URI("");
        config.useSingleServer().setAddress("redis://127.0.0.1:6379").setPassword("123456").setDatabase(0);

        RedissonClient redissonClient = Redisson.create(config);

        RLock lock = redissonClient.getLock("test_lock1");

        try{
            boolean isLock = lock.tryLock();
            if(isLock){
                System.out.println("now is locked");
            }else{
                System.out.println("now is not lock");
            }
        }catch (Exception e){

        }finally {
            lock.unlock();
        }

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxWaitMillis(1000);
        jedisPoolConfig.setMaxIdle(6);
        jedisPoolConfig.setMaxTotal(6);
        JedisPool pool = new JedisPool(jedisPoolConfig, "192.168.83.128", 6379, 0, "123456",0);
        Jedis jedis = pool.getResource();
        RedisTemplate<String,Object> template = new RedisTemplate<>();
        template.opsForSet();



    }
}
