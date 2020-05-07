package com.it.forever.young.cycle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhanj566
 * @date 2020/4/13 5:48 PM
 **/
@Component
public class A {

    @Autowired
    private B b;

    A () {
        System.out.println("A constructor");
    }

}
