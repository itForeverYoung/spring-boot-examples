package com.it.forever.young.demo;

import org.apache.rocketmq.client.consumer.DefaultLitePullConsumer;
import org.apache.rocketmq.client.consumer.store.ReadOffsetType;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhanj566
 * @date 2020/7/27 1:54 PM
 **/
public class PullConsumerDemo {

    private static final String NAME_SRV = "localhost:9876;localhost:9877";
    private static final String TOPIC_DEMO = "topic-demo";
    // pull 模式的话，需要客户端自己保存当前的消费位移
    private static Map<MessageQueue, Long> OFFSET_TAB = new HashMap<>();

    public static void main(String[] args) throws MQClientException {
        // 创建消费者时，指定一个消费组（和kafka中的消费组概念类似），消费组的意思是，在当前组下的所有消费者，共同消费一个topic下的消息，这样可以提高消费能力
        DefaultLitePullConsumer pullConsumer = new DefaultLitePullConsumer("consumer_group_2");
        // groupName 需要配合messageModel使用
        // 设置消费者连接的namesrv，如果有多个，用分号隔开
        pullConsumer.setNamesrvAddr(NAME_SRV);
        /**
         * 消息模式有两种
         * 1. CLUSTERING，此模式下，消费组内的每个消费者，只消费一部分消息；所有消费者消费的内容加起来才是整个topic的全部消息，以此可达到负载均衡的效果
         * 2. BROADCASTING，此模式下，消费组内的每个消费者，都会消费全部的消息，也就是说，一个消息会被多个消费者消费，有点类似于rabbit的主题订阅模式
         */
        // pullConsumer.setMessageModel(MessageModel.BROADCASTING);
        // topic用来指定当前消费者要消费哪个主题下的消息，如果不需要消费该主题下全部消息，可以通过tag来区分，tag是在发送消息时加上的。
        // 将tag设置为null或者*，代表要消费该主题下的全部消息
        /**
         * 假设说，有一个下单->支付->物流更新业务，理论上这是一个业务流程，那么，我们可以使用一个topic，然后使用不同的tag来区分当前的执行状态（tag标注是下单、支付、物流）
         */
        pullConsumer.start();
        Collection<MessageQueue> messageQueues = pullConsumer.fetchMessageQueues(TOPIC_DEMO);
        for (MessageQueue messageQueue : messageQueues) {
            long offset = pullConsumer.getOffsetStore().readOffset(messageQueue, ReadOffsetType.MEMORY_FIRST_THEN_STORE);
        }

    }

}
