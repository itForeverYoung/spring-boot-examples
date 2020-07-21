package com.it.forever.young.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangjikai
 * @date 2020/3/30 21:25
 **/
@RestController
public class TestController {

    public static void main(String[] args) throws Exception {
        ExecutorService service = new ThreadPoolExecutor(2,2,1, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), new CustomizableThreadFactory("test-"));

        service.execute(new R1("111"));
        service.execute(new R2("222"));
        service.execute(new R3("333"));

        Thread.sleep(3000);

        service.execute(new R1("444"));
        service.execute(new R2("555"));
        service.execute(new R3("666"));

        service.shutdown();

    }

    @Data
    @AllArgsConstructor
    static class R1 implements Runnable {

        private String value;

        @Override
        public void run() {
            System.out.println(Thread.currentThread() + " _ " + value);
        }
    }

    @Data
    @AllArgsConstructor
    static class R2 implements Runnable {

        private String value;

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + "_" + value);
        }
    }

    @Data
    @AllArgsConstructor
    static class R3 implements Runnable {

        private String value;

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + "_" + value);
        }
    }

}
