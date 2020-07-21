package com.it.forever.young.programing;

import com.it.forever.young.constant.RabbitConstant;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerEndpoint;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author zhangjikai
 * @date 2020/3/31 22:45
 * 使用编程式注册消息监听器
 **/
@Configuration
@EnableRabbit
public class FirstQueueConfig implements RabbitListenerConfigurer {

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
        SimpleRabbitListenerEndpoint endpoint = new SimpleRabbitListenerEndpoint();
        endpoint.setQueueNames(RabbitConstant.FIRST_QUEUE);
        endpoint.setMessageListener(message -> System.out.println(new String(message.getBody())));
        // endpoint.setAckMode(AcknowledgeMode.MANUAL);
        endpoint.setId("programing-first-queue");
        registrar.registerEndpoint(endpoint);
    }

}
