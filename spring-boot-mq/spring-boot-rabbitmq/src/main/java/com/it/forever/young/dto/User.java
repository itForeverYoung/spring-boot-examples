package com.it.forever.young.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhangjikai
 * @date 2020/3/26 21:41
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String name;
    private String address;
    private int age;

}
