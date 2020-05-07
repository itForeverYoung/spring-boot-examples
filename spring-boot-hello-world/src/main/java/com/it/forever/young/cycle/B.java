package com.it.forever.young.cycle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhanj566
 * @date 2020/4/13 5:48 PM
 **/
@Component
public class B {

    @Autowired
    private A a;

    B () {
        System.out.println("B constructor");
    }

    public void testB() {
        System.out.println("A ----- " + a);
    }

}
