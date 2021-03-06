package com.key.common.redis;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class RedisUtils {

    @Resource(name = "redisTemplate")
    private RedisTemplate<String,Object> redisTemplate;

    @Resource(name = "transactionalRedisTemplate")
    private RedisTemplate<String, Object> transactionalRedisTemplate;

    /**
     *  redis事务处理测试
     *
     * @param key
     * @param value
     */
    @Transactional
    public void setObject(String key, Object value){
        // 对于正常的redisTemplate中的multi的事务是不起作用的，当遇到异常时对于异常之前执行的操作不会进行回滚
        // 需要设置redisTemplate.setEnableTransactionSupport(true); 表示开启事务
        // 在

//        transactionalRedisTemplate.multi();
//        ListOperations<String,Object> listOperations = transactionalRedisTemplate.opsForList();
//        listOperations.leftPush(key,"value1");
////        int m = 1/0;
//        listOperations.leftPush(key,"value2");
//        listOperations.leftPush(key,"value3");
//
//        // 否则会报异常： Error in execution; nested exception is io.lettuce.core.RedisCommandExecutionException: ERR EXEC without MULTI
//        transactionalRedisTemplate.exec();
//        System.out.println("sss");


        // 对于redisTemplate 来说其通过multi,exec 和discard 方法来支持事务，但其并不能保证在同一连接请求下执行多个操作，execute方法并不支持同一个连接请求下执行多个事务操作
        // 而spring-data-redis提供的SessionCallBack 接口可以在同一连接下执行多条命令，会进行事务，如果出现异常会进行事务回滚
        // 并且可以返回每一条命令的执行结果
        List<Object> txResults = transactionalRedisTemplate.execute(new SessionCallback<List<Object>>() {
            public List<Object> execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                operations.opsForSet().add("key", "value4");

                int i = 1/0;
                operations.opsForSet().add("key", "value5");
                operations.opsForSet().add("key", "value6");


                // This will contain the results of all operations in the transaction
                // 这里会返回每一条命令（operations）的执行结果
                return operations.exec();
            }
        });
        System.out.println(txResults);
    }


//    ==================================== Basic =========================================

    /**
     * 指定缓存失效时间
     *
     * @param key 键
     * @param time  时间(秒)
     * @return
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key  键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

// ============================ String =============================

    /**
     * 普通缓存获取
     *
     * @param key   键
     * @return 值
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 普通缓存放入，并返回原value
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public Object getSet(String key, Object value) {
        try {
            return redisTemplate.opsForValue().getAndSet(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 普通缓存放入判断key是否存在，如果key不存在则添加并返回true，如果key存在则直接返回false
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean setNx(String key, Object value) {
        try {
            return redisTemplate.opsForValue().setIfAbsent(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 递增
     *
     * @param key 键
     * @param delta 要增加几(大于0)
     * @return
     */
    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return
     */
    public long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }


    /**
     * 分布式应用：获取锁
     * @param key
     * @param value
     * @return  true：获取锁成功，false：获取锁失败（等待锁）
     */
    public Boolean lock(String key,String value){

        if(redisTemplate.opsForValue().setIfAbsent(key,value)){
            return true;
        }
        String redisValue = (String) redisTemplate.opsForValue().get(key);
        if(StringUtils.isNotBlank(redisValue) && Long.valueOf(redisValue) < System.currentTimeMillis()){
            //锁过期
            String oldValue = (String) redisTemplate.opsForValue().getAndSet(key, value);
            //防止多线程同时获取锁
            if(StringUtils.isNotBlank(oldValue) && oldValue.equals(redisValue)){
                return true;
            }
        }
        // 等待锁
        return false;
    }

// ================================ Map =================================

    /**
     * HashGet
     *
     * @param key   键 不能为null
     * @param item  项 不能为null
     * @return 值
     */
    public Object hget(String key, String item) {
        if(StringUtils.isEmpty(key)){
            return null;
        }
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key   键
     * @return 对应的多个键值
     */
    public Map<Object, Object> hmget(String key) {
        if(StringUtils.isEmpty(key)){
            return null;
        }
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * HashSet
     *
     * @param key   键
     * @param map   对应多个键值
     * @return true 成功 false 失败
     */
    public boolean hmset(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     *
     * @param key   键
     * @param map   对应多个键值
     * @param time  时间(秒)
     * @return true成功 false失败
     */
    public boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建，并设置过期时间
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key   键 不能为null
     * @param item  项 可以使多个 不能为null
     */
    public void hdel(String key, Object... item) {
        if(!StringUtils.isEmpty(key)){
            redisTemplate.opsForHash().delete(key, item);
        }
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key   键 不能为null
     * @param item  项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String item) {
        if(StringUtils.isEmpty(key)){
            return false;
        }
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key   键
     * @param item  项
     * @param by    要增加的值(大于0)
     * @return
     */
    public double hincr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     *
     * @param key   键
     * @param item  项
     * @param by 要减少的值(小于0)
     * @return
     */
    public double hdecr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

// ============================ Set =============================

    /**
     * 根据key获取Set中的所有值
     *
     * @param key   键
     * @return
     */
    public Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 判断一个value在set中是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key   键
     * @param values    值（可以是多个）
     * @return 成功个数
     */
    public long sSet(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 将set数据放入缓存
     *
     * @param key   键
     * @param time  时间(秒)
     * @param values    值（可以是多个）
     * @return 成功个数
     */
    public long sSetAndTime(String key, long time, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0)
                expire(key, time);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key   键
     * @return
     */
    public long sGetSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 移除set中的value值
     *
     * @param key   键
     * @param values    值 可以是多个
     * @return 移除的个数
     */
    public long setRemove(String key, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

// =============================== List =================================

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始位置
     * @param end   结束位置 （start 和 end 为 0 和-1 时 代表所有值）
     * @return
     */
    public List<Object> lGetList(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取list缓存的长度
     *
     * @param key   键
     * @return
     */
    public long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean lSet(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存，并设置过期时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public boolean lSet(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0)
                expire(key, time);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean lSet(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存，并设置过期时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public boolean lSet(String key, List<Object> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0)
                expire(key, time);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return
     */
    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count  移除数量（list中可以存在多个相同的value）
     * @param value 值
     * @return 移除的个数
     */
    public long lRemove(String key, long count, Object value) {
        try {
            Long remove = redisTemplate.opsForList().remove(key, count, value);
            return remove;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

// =============================== ZSet =================================
    /**
     * 根据key获取Set中的所有值
     *
     * @param key   键
     * @param start 起始位置
     * @param end   结束位置
     * @return
     */
    public Set<Object> zGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForZSet().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 一个value在zSet中的排名（索引位置）
     *
     * @param key   键
     * @param value 值
     * @return 索引位置，-1表示不存在
     */
    public Long zGetIndex(String key, Object value) {
        try {
            Long index = redisTemplate.opsForZSet().rank(key, value);
            if(index == null){
                return -1l;
            }else{
                return index;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return -1l;
        }
    }
    // TODO    关于ZSet的相关方法还未实现，后续补充



}
