package com.it.forever.young.web;

import com.it.forever.young.entity.User;
import com.it.forever.young.response.R;
import com.it.forever.young.service.UserService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author jikzhang
 * @version 1.0
 * @date 2019/8/5 15:33
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("user")
    public R list() {
        List<User> data = userService.findList();
        return R.ok().put("data", data);
    }

    @PostMapping("user")
    public R create() {


        return R.ok();
    }

    @PutMapping("user")
    public R update() {


        return R.ok();
    }

    @Delete("user")
    public R delete() {


        return R.ok();
    }
}
