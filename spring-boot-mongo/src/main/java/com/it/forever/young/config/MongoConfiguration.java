package com.it.forever.young.config;

import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * @ClassName: MongoConfiguration
 * @Author: Damon
 * @Date: 2019/11/14 1:29 PM
 * @Description: TODO
 **/
//@Configuration
@EnableMongoRepositories(basePackages = "com.example.mongo.repository")
public class MongoConfiguration {
}
