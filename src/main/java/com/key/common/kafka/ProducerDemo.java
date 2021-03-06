package com.key.common.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.*;

public class ProducerDemo {
    private Properties properties = new Properties();
    private Producer<String, String> producer;

    /**
     * 初始化连接参数
     */
    private void configure() {
        if (properties == null) {
            properties = new Properties();
        }
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.204.130:9092");
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        properties.put(ProducerConfig.RETRIES_CONFIG, 0);
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        properties.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        // 设置key和value的序列化方式，可以使用类名称方式，也可以使用字符串指明类全路径
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());
//        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        // 新建生产者对象，并设置连接参数
        producer = new KafkaProducer<String, String>(properties);
    }

    /**
     * 关闭连接
     */
    private void close() {
        try {
            if (producer != null) {
                producer.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 序列化数据为byte[]
     *
     * @param data
     * @return
     */
    public byte[] serialize(Map<String, Object> data) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsBytes(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 序列化数据为byte[]
     *
     * @param data
     * @return
     */
    public String serializeToString(Map<String, Object> data) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 组装数据
     *
     * @return
     */
    public Map<String, Object> collectData() throws IOException {
        Map<String, Object> result = new HashMap<>();
        for (int i = 0; i < 100; i++) {
            ProducerData producerData = new ProducerData();
            producerData.setAge(new Random().nextInt(50));
            producerData.setId(UUID.randomUUID().toString());
            producerData.setAddress("data_" + i);
            ObjectMapper mapper = new ObjectMapper();
            result.put(producerData.getId(), mapper.writeValueAsString(producerData));
        }
        System.out.println(result);
        return result;
    }

    public static void main(String[] args) {
        ProducerDemo producerDemo = new ProducerDemo();
        producerDemo.configure();
        try {
            // 模拟生成数据，并序列化需要发送的数据，序列化的方式与参数指定的value.serializable对应
            // 这里因参数指定value.serializable为ByteArraySerializer方式，因此需要将数据序列化为byte[]数组
            // 如果value.serializable为StringSerializer，则序列化为String字符串即可
            Map<String, Object> mapData = producerDemo.collectData();
            byte[] sendData = producerDemo.serialize(mapData);
            // 发送数据，通过ProducerRecord封装数据
            // ProducerRecord的构造方法必要参数为topic,value，还可以指定partition,timestamp,headers等
            producerDemo.producer.send(new ProducerRecord(KafkaConstants.TOPIC_TEST, null, sendData));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            producerDemo.close();
        }

    }
}
