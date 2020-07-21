package com.it.forever.young.damo;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * @author zhangjikai
 * @date 2020/3/19 0:20
 **/
public class Test {

    public static void main(String[] args) {
        System.getProperties().list(System.out);
        new ArrayList<String>().forEach(s -> System.out.println(s));
        new LinkedList<String>().forEach(s -> System.out.println(s));
    }

}
