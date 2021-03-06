package com.key.common.kafka;

/**
 * kafka 静态类
 */
public class KafkaConstants {

    // 测试topic
    public static final String TOPIC_TEST = "myTopicDemo";
    public static final String TOPIC_TEST2 = "testforflume";
    public static final String TOPIC_TEST3 = "testforflume2";

    // 消费者timeout
    public static final int CONSUMER_TIMEOUT = 1000;

    // 消费数量大小
    public static final int MIN_BATCH_SIZE = 10;

}
