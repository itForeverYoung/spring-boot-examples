package com.it.forever.young.threadlocal;

import java.util.Random;

/**
 * @author zhangjikai
 * @date 2020/5/13 0:10
 **/
public class ThreadLocalTest {


    public static void main(String[] args) {
        ThreadLocal<Integer> threadLocal = new ThreadLocal<>();
        new Thread(() -> {
            threadLocal.set(new Random().nextInt(100));
            System.out.printf("%s中的值是%s %n", Thread.currentThread().getName(), threadLocal.get());
        }, "线程1").start();

        new Thread(() -> {
            threadLocal.set(new Random().nextInt(100));
            System.out.printf("%s中的值是%s %n", Thread.currentThread().getName(), threadLocal.get());
        }, "线程2").start();

        ThreadLocal<Integer> inheritableThreadLocal = new InheritableThreadLocal<>();
        inheritableThreadLocal.set(123);

        new Thread(() -> {
            System.out.printf("%s中的值是%s %n", Thread.currentThread().getName(), inheritableThreadLocal.get());
        }, "线程3").start();
    }

}
