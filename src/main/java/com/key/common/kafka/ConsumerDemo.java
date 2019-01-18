package com.key.common.kafka;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;

import java.util.*;

/**
 * 消费者测试DEMO
 */
public class ConsumerDemo {

    private Properties properties = new Properties();

    private void configure() {
        // 配置kafka服务broker list
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.204.130:9092");
        // 指定consumer group
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "test");
        // 将下一次消费的位置offset自动commit到服务器
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        // 指定key和value反序列化方式
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
    }

    /**
     * 手动提交offset，并指定的partition的最新offset值
     * 需要将enable.auto.commit 设置为 false
     *
     * @param consumer
     */
    public void manualCommitControlOffSet(KafkaConsumer<String, String> consumer) {
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Long.MAX_VALUE);
                for (TopicPartition partition : records.partitions()) {
                    List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);
                    for (ConsumerRecord<String, String> record : partitionRecords) {
                        System.out.println(record.offset() + ": " + record.value());
                    }
                    long lastOffset = partitionRecords.get(partitionRecords.size() - 1).offset();
                    consumer.commitSync(Collections.singletonMap(partition, new OffsetAndMetadata(lastOffset + 1)));
                }
            }
        } finally {
            consumer.close();
        }
    }

    /**
     * 手动提交offset
     * 需要将enable.auto.commit 设置为 false
     *
     * @param consumer
     */
    public void manualCommit(KafkaConsumer<String, String> consumer) {
        List<ConsumerRecord<String, String>> buffer = new ArrayList<>();
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(1000);
                for (ConsumerRecord<String, String> record : records) {
                    buffer.add(record);
                }
                System.out.println("消费数据大小:" + buffer.size());
                if (buffer.size() >= KafkaConstants.MIN_BATCH_SIZE) {
                    // 进行持久化到DB操作
                    // insertIntoDb(buffer);
                    //手动提交offset
                    consumer.commitSync();
                    buffer.clear();
                }
            }
        } catch (Exception e) {
            consumer.close();
        }
    }

    /**
     * 自动提交offset
     *
     * @param consumer
     */
    public void autoCommit(KafkaConsumer<String, String> consumer) {
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(KafkaConstants.CONSUMER_TIMEOUT);
                for (ConsumerRecord<String, String> record : records)
                    System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
            }
        } catch (Exception e) {
            consumer.close();
        }

    }


    public static void main(String[] args) {
        ConsumerDemo consumerDemo = new ConsumerDemo();
        consumerDemo.configure();
        // 新增consumer对象，并设置参数
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerDemo.properties);
        // 订阅topic列表
        consumer.subscribe(Arrays.asList(KafkaConstants.TOPIC_TEST));
        // 消费topic队列数据
        consumerDemo.autoCommit(consumer);
//        consumerDemo.manualCommit(consumer);
//        consumerDemo.manualCommitControlOffSet(consumer);
    }
}
