package com.it.forever.young.config;

import com.it.forever.young.constant.RabbitConstant;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.concurrent.Executors;

/**
 * @author zhangjikai
 * @date 2020/3/26 21:39
 **/
@Configuration
@EnableConfigurationProperties(RabbitProperties.class)
public class RabbitMqConfiguration {


    /**
     * ConnectionFactory是用于管理与Rabbit代理的连接的中央组件
     * @param rabbitProperties
     * @return
     */
    @Bean
    public ConnectionFactory connectionFactory(RabbitProperties rabbitProperties, ConnectionNameStrategy connectionNameStrategy) {
        // CachingConnectionFactory是目前唯一的官方实现
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        // 可以通过设置address来配置集群
        // connectionFactory.setAddresses("127.0.0.1:5672, 127.0.0.1:5673");
        connectionFactory.setHost(rabbitProperties.getHost());
        connectionFactory.setPort(rabbitProperties.getPort());
        connectionFactory.setUsername(rabbitProperties.getUsername());
        connectionFactory.setPassword(rabbitProperties.getPassword());
        // 开启发布者确认模式
        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
        connectionFactory.setPublisherConfirms(rabbitProperties.isPublisherConfirms());
        // 开启支持已返回的消息
        connectionFactory.setPublisherReturns(rabbitProperties.isPublisherReturns());
        // 设置缓存模式为连接
        // connectionFactory.setCacheMode(CachingConnectionFactory.CacheMode.CONNECTION);
        // 自定义线程工厂
        connectionFactory.setConnectionThreadFactory(new CustomizableThreadFactory("rabbitmq-"));
        // 设置连接标识
        connectionFactory.setConnectionNameStrategy(connectionNameStrategy);
        // 设置线程池
        connectionFactory.setExecutor(Executors.newFixedThreadPool(10, new CustomizableThreadFactory("rabbit-")));
        return connectionFactory;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory = new SimpleRabbitListenerContainerFactory();
        rabbitListenerContainerFactory.setConnectionFactory(connectionFactory);
        rabbitListenerContainerFactory.setMessageConverter(new Jackson2JsonMessageConverter());
        // 设置批处理，一次接收一批数据， @RabbitListener注解的方法可以通过list进行接收
        // 开启批处理的同时，会将deBatchingEnabled属性设置为false，此时解批处理的操作，也从监听器容器移到了监听器适配器中，适配器负责将list传递个监听器
        // 注：使用批处理的容器工厂，不能和多方法监听器共存
        // 开启批处理后，最后一条消息的header中会有一个属性设置为true，可以将@Header(AmqpHeaders.LAST_IN_BATCH) boolean last，添加到方法参数中，来判断是否为最后一条消息（通过MessageProperties.isLastInBatch()也可获取）
        // 此外，AmqpHeaders.BATCH_SIZE中填充了每一条消息的批处理大小
        // rabbitListenerContainerFactory.setBatchListener(true);
        // 当consumerBatchEnabled为true的时候，deBatchingEnabled必须为true
        // rabbitListenerContainerFactory.setConsumerBatchEnabled(true);
        return rabbitListenerContainerFactory;
    }

    @Bean
    public ConnectionNameStrategy connectionNameStrategy() {
        return new SimplePropertyValueConnectionNameStrategy("rabbit");
    }

    /**
     * 启用RabbitConnectionFactoryBean，配置SSL
     * @return
     */
    @Bean
    public RabbitConnectionFactoryBean rabbitConnectionFactoryBean() {
        RabbitConnectionFactoryBean rabbitConnectionFactoryBean = new RabbitConnectionFactoryBean();
        return rabbitConnectionFactoryBean;
    }

    /**
     * 封装了一些声明exchange、queue，binding到broker的方法，当前只有一个默认实现，
     * @see org.springframework.amqp.rabbit.core.RabbitAdmin
     * @param connectionFactory
     * @return
     */
    @Bean
    public AmqpAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        // 默认值5000 ms，作用于sendAndReceive方法（包括其重载方法），当超过replyTimeout时间后，若还没有返回，则直接返回null
        rabbitTemplate.setReplyTimeout(10000);
        // 默认false，当mandatory设置为true时，如果消息无法传递到一个queue中，则抛出AmqpMessageReturnedException
        rabbitTemplate.setMandatory(true);
        // 添加自定义的message后置处理器，发送消息前触发
        rabbitTemplate.setBeforePublishPostProcessors();
        // 添加自定义的message后置处理器，收到消息后触发
        rabbitTemplate.setAfterReceivePostProcessors();
        return rabbitTemplate;
    }

    @Bean
    public Queue firstQueue() {
        return new Queue(RabbitConstant.FIRST_QUEUE);
    }

}
