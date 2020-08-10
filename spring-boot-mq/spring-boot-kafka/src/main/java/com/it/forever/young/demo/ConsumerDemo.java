package com.it.forever.young.demo;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Properties;

/**
 * @author zhanj566
 * @date 2020/7/17 2:39 PM
 **/
public class ConsumerDemo {

    private static final String BROKER_LIST = "127.0.0.1:9092";
    private static final String TOPIC = "topic-demo";

    public static Properties initConfig() {
        // KafkaProducer有ProducerConfig，对应的，KafkaConsumer也有ConsumerConfig
        Properties properties = new Properties();
        // 配置消费者需要连接的kafka集群地址，如果有多个，逗号隔开
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKER_LIST);
        // 配置消费者对应的client.id
        properties.put("client.id", "consumer.client.id.demo");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "consumer.group.id.demo");
        // 生产者需要序列化消息，那么消费者就需要反序列化消息，所以这里需要指定key，value的反序列化器
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        return properties;
    }

    public static void main(String[] args) {
        Properties properties = initConfig();
        KafkaConsumer<String, String> consumer= new KafkaConsumer<>(properties);
        // subscribe有三个重载方法，1. 传入一个topic的集合；2. 传入一个正则表达式；3. 通过assign方法订阅指定分区
        // 1. 订阅一组topic的集合，AUTO_TOPIC
        consumer.subscribe(new ArrayList<String>(){{add(TOPIC);}});
        // 2. 订阅一个正则表达式，AUTO_PATTERN
        // consumer.subscribe(Pattern.compile("topic-*"));
        // 3. 订阅指定topic的指定分区，USER_ASSIGNED
        // consumer.assign(new ArrayList<TopicPartition>(){{add(new TopicPartition(TOPIC, 1));}});
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(10000));
                for (ConsumerRecord record : records) {
                    System.out.printf("topic is %s, %n, partition is %s, %n, offset is %s, %n, key is %s, %n, value is %s, %n.",
                            record.topic(), record.partition(), record.offset(), record.key(), record.value());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 同步位移提交
        consumer.commitSync();
        // 异步位移提交
        consumer.commitAsync();
        // 取消订阅的时候，1. 可以直接通过unsubscribe方法取消；2. 也可以调用subscribe方法，传入一个空的集合取消
        // consumer.unsubscribe();
        // consumer.subscribe(new ArrayList<>());
        // 关闭
        consumer.close();

    }

}
