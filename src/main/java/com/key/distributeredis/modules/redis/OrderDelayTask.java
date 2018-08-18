package com.key.distributeredis.modules.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Tuple;

import java.util.Calendar;
import java.util.Set;

public class OrderDelayTask {
    private Jedis jedis ;

    public OrderDelayTask(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxWaitMillis(1000);
        jedisPoolConfig.setMaxIdle(6);
        jedisPoolConfig.setMaxTotal(6);
        JedisPool pool = new JedisPool(jedisPoolConfig, "127.0.0.1", 6379, 0, "123456",0);
        jedis = pool.getResource();
    }

    public void productionDelayMessage(){
        for(int i=0; i < 5; i++){
            //延迟3秒
            Calendar c1 = Calendar.getInstance();
            c1.add(Calendar.SECOND,3);

            int second1 = (int) (c1.getTimeInMillis()/1000);
            jedis.zadd("OrderId",second1,"OID0000001"+i);
            System.out.println(System.currentTimeMillis()+"ms:redis生成了一个订单任务：订单ID为"+"OID0000001"+i);

        }
    }


    public void consumerDelayMessage(){

        while (true){
            Set<Tuple> items = jedis.zrangeWithScores("OrderId",0,1);
            if(items == null || items.isEmpty()){
                System.out.println("当前没有等待任务");
                try {
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                continue;
            }

            int score = (int) ((Tuple)items.toArray()[0]).getScore();
            Calendar c2 = Calendar.getInstance();
            int nowSecond = (int) (c2.getTimeInMillis()/1000);
            if (nowSecond >= score){
                String orderId = ((Tuple)items.toArray()[0]).getElement();
                Long num = jedis.zrem("OrderId",orderId);
                System.out.println(num);
                //高并发下，防止一个订单被多次消费
                if( num != null && num>0){
                    System.out.println(System.currentTimeMillis()+"ms:redis消费了一个任务：消费的订单OrderId为"+orderId);
                }
            }
        }
    }

    public static  void main(String[] args){

        OrderDelayTask orderDelayTask = new OrderDelayTask();
        orderDelayTask.productionDelayMessage();
        orderDelayTask.consumerDelayMessage();
        for(int i = 0; i < 50; i++){
            new Thread(new ThreadMessage()).start();
        }
        //
    }

}
class ThreadMessage implements Runnable{
    @Override
    public synchronized void run() {
        try{
            wait();
            OrderDelayTask orderDelayTask = new OrderDelayTask();
            orderDelayTask.consumerDelayMessage();
            notify();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}

