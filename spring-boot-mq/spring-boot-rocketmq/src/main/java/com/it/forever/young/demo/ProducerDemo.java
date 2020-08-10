package com.it.forever.young.demo;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;

/**
 * @author zhanj566
 * @date 2020/7/27 1:54 PM
 **/
public class ProducerDemo {

    private static final String NAME_SRV = "localhost:9876;localhost:9877";
    private static final String TOPIC_DEMO = "topic-demo";

    public static void main(String[] args) throws MQClientException, UnsupportedEncodingException, RemotingException, InterruptedException, MQBrokerException {
        // 初始化一个生产者，需要指定一个生产组
        DefaultMQProducer producer = new DefaultMQProducer("producer_group_1");
        // 设置连接的namesrv
        producer.setNamesrvAddr(NAME_SRV);
        // 设置生产者名称
        producer.setInstanceName("producer_demo");
        // 设置重发3次
        producer.setRetryTimesWhenSendFailed(3);
        producer.start();
        int val = 100;
        for (int i = 0; i < val; i++) {
            // 初始化一条消息，参数从前往后分别是：1. 发往的主题；2. tag；3. 消息主题
            Message message = new Message(TOPIC_DEMO, "tagA", ("hello rocket, " + i ).getBytes(RemotingHelper.DEFAULT_CHARSET));
            // rocketmq支持延迟消息，可以通过设置消息的delayTimeLevel属性，但是目前并不支持任意时间，只支持预设的一些等级，比如说，设置值为3，实际上是代表延迟10s
            // 1s/5s/10s/30s/1m/2m/3m/4m/5m/6m/7m/8m/9m/10m/20m/30m/1h/2h
            message.setDelayTimeLevel(3);
            /**
             * 如果没有指定发往哪个message queues，producer会轮流向每个message queue发送消息，而consumer会根据负载均衡策略，分别从不同的queue中消费消息
             */
            /**
             * 消息发送的结果会有四种状态
             * 1. SEND_OK, 发送成功，这个成功实际上表示，没有发生下面3种情况
             * 2. FLUSH_DISK_TIMEOUT, 刷盘超时，需要将broker的刷盘策略设置为SYNC_FLUSH才会出现
             * 3. FLUSH_SLAVE_TIMEOUT, 同步到slave超时，需要在主备状态下，将broker的同步策略设置为SYNC_MASTER才会出现
             * 4. SLAVE_NOT_AVAILABLE, 从节点不可用，需要在主备状态下，将broker的同步策略设置为SYNC_MASTER，并且没有找到slave节点时，才会出现
             */
            // 只发送一次
            producer.sendOneway(message);
            // 同步发送
            SendResult result = producer.send(message);
            // 异步发送
            producer.send(message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    // 发送成功了回调此方法
                    System.out.println(sendResult.getSendStatus());
                }

                @Override
                public void onException(Throwable e) {
                    // 发送失败了回调此方法
                    e.printStackTrace();
                }
            });
            System.out.println(result);
        }
        producer.shutdown();
    }

}
