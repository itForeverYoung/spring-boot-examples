package com.it.forever.young.response;

import java.util.HashMap;

/**
 * @author jikzhang
 * @version 1.0
 * @className R
 * @date 2019/8/5 15:36
 * @description 统一返回的response，对象中默认有code，msg为可选
 */
public class R extends HashMap<String, Object> {

    public R() {
        put("code", 0);
    }

    public R(int code) {
        put("code", code);
    }

    public R(int code, String msg) {
        put("code", code);
        put("msg", msg);
    }

    /**
     * 默认ok方法，code：200，msg：OK
     * @return
     */
    public static R ok() {
        return ok(200, "OK");
    }

    /**
     * 默认ok方法，code：200
     * @return
     */
    public static R ok(String msg) {
        return ok(200, msg);
    }

    public static R ok(int code, String msg) {
        return new R(code, msg);
    }

    /**
     * 默认error方法，code：500，msg：ERROR
     * @return
     */
    public static R error() {
        return error(500, "ERROR");
    }

    /**
     * 默认error方法，code：500
     * @return
     */
    public static R error(String msg) {
        return error(500, msg);
    }

    public static R error(int code, String msg) {
        return new R(code, msg);
    }

    @Override
    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }

}
