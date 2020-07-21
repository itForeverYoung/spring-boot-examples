package com.it.forever.young.demo;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * @author zhanj566
 * @date 2020/7/17 2:39 PM
 **/
public class ProducerDemo {

    private static final String BROKER_LIST = "127.0.0.1:9092";
    private static final String TOPIC = "topic-demo";

    public static Properties initConfig() {
        // ProducerConfig类中定义了一些静态常量，是kafka提供的一些配置项的key
        Properties properties = new Properties();
        // bootstrap.servers，指定生产者客户端需要连接的kafka集群地址，可以设置多个，以逗号隔开即可
        properties.put("bootstrap.servers", BROKER_LIST);
        // client.id，用来设定producer生产者对应的客户端id，默认为""，如果客户端没有设定，则KafkaProducer会自动生成一个非空字符串，类似于"Producer-1"这中
        properties.put("client.id", "producer.client.id.demo");
        // 发送到broker的消息必须是byte[]数组，所以在发往broker之前，需要对key，value进行序列化，这里就是指定序列化器，
        // 注意，put的value必须是类全限定名
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        // retries，配置发送重试次数，当kafka发送消息抛出可重试异常时，如果配置了重试次数，则会默认再重发
        properties.put(ProducerConfig.RETRIES_CONFIG, 10);
        // retry.backoff.ms，配置重试间隔，默认为100ms
        properties.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 100);
        /**
         * acks是一个很重要的配置参数，即当目标分区的几个副本成功希尔该消息，生产者才认为该消息发送成功，这就涉及到了吞吐量和可靠性的权衡
         * acks有三种配置：
         * 1. acks=1，即只要分区的leader副本成功写入该消息之后，生产者就认为发送成功；如果当leader写入后，还没有同步到follower副本，则该消息还是丢失的；此时是吞吐量和可靠性的折中选择；
         * 2. acks=0，即只要生产者把消息发送出去，就认为是发送成功；无论消息写入到kafka出现了什么异常，生产者都不会知道，消息丢失；此时吞吐量最高，可靠性最低；
         * 3. acks=-1或acks=all，即生产者发送消息后，必须等待ISR中的所以副本写入成功，才会认为消息发送成功；但是如果ISR中只有一个leader副本，就退化成了acks=1的配置；此时可靠性最高，但不是绝对的。
         * acks对应的值应该是字符串，默认配置为"1"
         */
        properties.put(ProducerConfig.ACKS_CONFIG, "1");
        // 生产者可发送的消息的最大值，默认为1MB
        // properties.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, 1048576);
        // compression.type，配置消息的压缩方式，默认是none，即不压缩，支持gzip，snappy，lz4等
        properties.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "none");
        // connections.max.idle.ms，多久之后关闭限制的连接，默认是540000ms，即9分钟
        properties.put(ProducerConfig.CONNECTIONS_MAX_IDLE_MS_CONFIG, 540000);
        // send.buffer.bytes，设置socket发送缓冲区的大小，默认是131072，即128kb，如果设置为-1，代表使用操作系统的默认值
        properties.put(ProducerConfig.SEND_BUFFER_CONFIG, 131072);
        // receive.buffer.bytes，设置socket接收缓冲区的大小，默认是32768，即32kb，如果设置为-1，代表使用操作系统的默认值
        properties.put(ProducerConfig.RECEIVE_BUFFER_CONFIG, 32768);
        // request.timeout.ms，设置生产者等待请求响应的超时时间，默认是30000ms
        properties.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 30000);
        return properties;
    }

    public static void main(String[] args) {
        Properties properties = initConfig();
        // KafkaProducer是线程安全的，可以在多个线程中共享一个KafkaProducer实例，也可以池化KafkaProducer实例供多个线程使用
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
        ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, "hello world");
        try {
            /**
             * 消息在被发往broker之前，会经过拦截器（Interceptor，可以没有），序列化器（Serializer，必须有），分区器（Partitioner）
             * 1. 生产者拦截器的接口是org.apache.kafka.clients.producer.ProducerInterceptor，没有默认实现
             *     - 生产者拦截器的方法调用是按照这样的顺序，1. 发送之前，调用onSend方法；2. 在发送成功或者异常返回后，调用onAcknowledgement方法，onAcknowledgement方法的调用早于用户定义的callback。
             * 2. 序列化器的接口是org.apache.kafka.common.serialization.Serializer，kafka默认提供了多种实现，例如StringSerializer，DoubleSerializer，LongSerializer，IntegerSerializer等
             * 3. 分区器的接口是org.apache.kafka.clients.producer.Partitioner，kafka默认提供了一种实现，DefaultPartitioner
             *     - 默认的DefaultPartitioner实现逻辑是这样的，1. 如果定义了key，那就对key做hash，然后根据hash值从所有分区中选择一个；2. 如果key==null，那就以轮询方式，从所有可用的分区中选择一个分区
             *
             * KafkaProducer -> ProducerInterceptor -> Serializer -> Partitioner -> RecordAccumulator -> 最后，sender线程从RecordAccumulator中获取消息，发送给broker；
             * RecordAccumulator是一个消息缓冲器，producer会把消息缓存到RecordAccumulator中，这样sender线程就可以批量发送消息，来提升性能，RecordAccumulator的缓存大小可以通过buffer.memory属性进行设置；
             * RecordAccumulator中维护了一个concurrentHashMap对象，不同的partition分区各自持有一个ProducerBatch的双端队列Deque，消息在尾部插入到队列；sender从头部读取队列消息；
             * ProducerBatch是一组ProducerRecord消息记录。
             */
            // 发送消息有三种模式，发后即忘(fire-and-forget)，同步(sync)，异步(async)
            // 下面这种就是发后即忘模式，即消息发过去就结束了，不会判断消息是否发送成功，效率最高，可靠性最差。
            producer.send(record);
            // 下面这种就是同步模式，因为KafkaProducer.send方法是有返回值的，返回值是Future类型，那么，我们就可以通过调用Future.get方法同步等待发送结果
            producer.send(record).get();
            // 下面这种就是异步模式，即通过一个callback异步接收返回结果
            producer.send(record, (metadata, exception) -> {
                // metadata和exception是互斥的，肯定有一个为空，即消息要么发送成功，要么发送失败
                // 这里，metadata就是异步返回的发送结果，如果消息发送出现了异常，就会通过exception传递
                if (exception != null) {
                    exception.printStackTrace();
                } else {
                    System.out.println(metadata);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        // 关闭producer，阻塞等待所有消息发送结束后关闭
        producer.close();
    }

}
