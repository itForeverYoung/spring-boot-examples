package com.it.forever.young.web;

import com.it.forever.young.response.R;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jikzhang
 * @version 1.0
 * @date 2019/8/5 15:33
 */
@RestController
//@RequestMapping("/weather")
public class ESController {


    @GetMapping("weather")
    public R list() {


        return R.ok();
    }

    @PostMapping("weather")
    public R create() {


        return R.ok();
    }

    @PutMapping("weather")
    public R update() {


        return R.ok();
    }

    @Delete("weather")
    public R delete() {


        return R.ok();
    }
}
