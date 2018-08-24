package com.key.distributeredis.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@PropertySource("classpath:application.properties")
public class RedisConfig {

    /**
     * 注入 RedisConnectionFactory
     */
    @Autowired
    RedisConnectionFactory redisConnectionFactory;

    /**
     * 实例化 RedisTemplate 对象
     *  默认依赖对象（ConditionalOnMissingBean注解）
     * @return
     */
    @Bean(name = "redisTemplate")
    @ConditionalOnMissingBean(name = "redisTemplate")
    public RedisTemplate<String, Object> functionDomainRedisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        initDomainRedisTemplate(redisTemplate, redisConnectionFactory,false);
        return redisTemplate;
    }

    /**
     * 实例化 RedisTemplate 对象（激活redis事务）
     *  配置第二个RedisTemplate
     * @return
     */
    @Bean(name = "transactionalRedisTemplate")
    public RedisTemplate<String, Object> transactionalRedisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        initDomainRedisTemplate(redisTemplate, redisConnectionFactory,true);
        return redisTemplate;
    }

    /**
     * 设置数据存入 redis 的序列化方式
     *
     * @param redisTemplate
     * @param factory
     * @param enableTransactionSupport
     */
    private void initDomainRedisTemplate(RedisTemplate<String, Object> redisTemplate, RedisConnectionFactory factory, Boolean enableTransactionSupport) {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setConnectionFactory(factory);
        //设置 是否支持 事务
        redisTemplate.setEnableTransactionSupport(enableTransactionSupport);
    }

    /**
     * 实例化 HashOperations 对象,可以使用 Hash 类型操作
     *  @Qualifier("redisTemplate") 因为存在两个redisTemplate,这里指定自动注入的redisTemplate参数的bean
     * @param redisTemplate
     * @return
     */
    @Bean
    public HashOperations<String, String, Object> hashOperations(@Qualifier("redisTemplate") RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForHash();
    }

    /**
     * 实例化 ValueOperations 对象,可以使用 String 操作
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public ValueOperations<String, Object> valueOperations(@Qualifier("redisTemplate") RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForValue();
    }

    /**
     * 实例化 ListOperations 对象,可以使用 List 操作
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public ListOperations<String, Object> listOperations(@Qualifier("redisTemplate") RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForList();
    }

    /**
     * 实例化 SetOperations 对象,可以使用 Set 操作
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public SetOperations<String, Object> setOperations(@Qualifier("redisTemplate") RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForSet();
    }

    /**
     * 实例化 ZSetOperations 对象,可以使用 ZSet 操作
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public ZSetOperations<String, Object> zSetOperations(@Qualifier("redisTemplate") RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForZSet();
    }

//    /**
//     * 注入封装RedisTemplate
//     *
//     */
//    @Bean(name = "redisUtils")
//    public RedisUtils redisUtil(RedisTemplate<String, Object> redisTemplate) {
//        RedisUtils redisUtil = new RedisUtils();
////        redisUtil.setRedisTemplate(redisTemplate);
//        return redisUtil;
//    }
}