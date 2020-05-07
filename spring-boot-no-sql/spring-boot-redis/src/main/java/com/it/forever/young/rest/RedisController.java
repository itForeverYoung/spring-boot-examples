package com.it.forever.young.rest;

import com.it.forever.young.dto.User;
import com.it.forever.young.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhangjikai
 * @date 2020/3/24 22:39
 **/
@RestController
public class RedisController {

    @Autowired
    private RedisService redisService;

    @PostMapping("set/{key}/{value}")
    public void set(@PathVariable("key") String key, @PathVariable("value") Object value) {
        redisService.set(key, value);
    }

    @PostMapping("setObject/{key}")
    public void setObject(@PathVariable("key") String key, @RequestBody User user) {
        redisService.set(key, user);
    }

    @GetMapping("get/{key}")
    public Object get(@PathVariable("key") String key) {
        return  redisService.get(key);
    }

    @GetMapping("getObject/{key}")
    public User getObject(@PathVariable("key") String key) {
        return (User) redisService.get(key);
    }

}
