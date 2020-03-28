package com.it.forever.young;

import com.it.forever.young.constant.RabbitConstant;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringBootRabbitmqApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootRabbitmqApplication.class, args);
    }

    @Bean
    ApplicationRunner runner(RabbitTemplate rabbitTemplate) {
        return args -> rabbitTemplate.convertAndSend(RabbitConstant.FIRST_QUEUE, "hello world");
    }

    @RabbitListener(queues = RabbitConstant.FIRST_QUEUE)
    public void listen(String in) {
        System.out.println(in);
    }

}
