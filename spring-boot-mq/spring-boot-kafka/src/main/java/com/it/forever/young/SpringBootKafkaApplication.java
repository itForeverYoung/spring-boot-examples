package com.it.forever.young;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author balala
 */
@SpringBootApplication
public class SpringBootKafkaApplication {

    /**
     * 1. kafka可以保证发送到同一个分区的消息是有序的
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(SpringBootKafkaApplication.class, args);
    }

}
