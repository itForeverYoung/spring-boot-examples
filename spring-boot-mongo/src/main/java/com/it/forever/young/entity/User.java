package com.it.forever.young.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @ClassName: User
 * @Author: Damon
 * @Date: 2019/11/13 11:40 AM
 * @Description: for mongo test
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("test")
public class User {

    private String name;
    private int age;
    private String address;
}
