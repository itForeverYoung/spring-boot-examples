package com.it.forever.young.cycle;

import com.it.forever.young.config.AppConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author zhangjikai
 * @date 2020/3/19 0:20
 **/
public class Test {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        B b = (B) applicationContext.getBean("b");
        b.testB();


    }

}
