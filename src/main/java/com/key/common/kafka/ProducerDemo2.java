package com.key.common.kafka;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.log4j.Logger;

import java.util.Properties;

public class ProducerDemo2 {
    static Logger log = Logger.getLogger(ProducerDemo2.class);

    private static final String BROKER_LIST = "192.168.204.130:9092";
    private static KafkaProducer<String, String> producer = null;

    /**
     * 初始化生产者
     */
    static {
        Properties configs = initConfig();
        producer = new KafkaProducer<String, String>(configs);
    }

    /**
     * 初始化配置
     */
    private static Properties initConfig() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKER_LIST);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return properties;
    }

    public static void main(String[] args) throws InterruptedException {
        //消息实体
        ProducerRecord<String, String> record = null;
        for (int i = 0; i < 1000; i++) {
            record = new ProducerRecord<String, String>(KafkaConstants.TOPIC_TEST, "value" + (int) (10 * (Math.random())));
            //发送消息 ，并接收回调函数
            producer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    if (null != e) {
                        log.info("send error" + e.getMessage());
                    } else {
                        System.out.println(String.format("offset:%s,partition:%s", recordMetadata.offset(), recordMetadata.partition()));
                    }
                }
            });
        }
        producer.close();
    }
}
