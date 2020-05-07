package com.it.forever.young;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author zhanj566
 */
@SpringBootApplication
public class SpringBootHelloWorldApplication {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.setAllowCircularReferences(false);
        SpringApplication.run(SpringBootHelloWorldApplication.class, args);
    }

}
