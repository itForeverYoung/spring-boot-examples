package com.it.forever.young.pool;

import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangjikai
 * @date 2020/5/12 18:07
 **/
public class AutoThreadPool {

    private static ThreadPoolExecutor threadPoolExecutor;

    public static void init(int maxThread) {
        BlockingQueue<Runnable> queue = new LinkedTransferQueue<Runnable>() {
            @Override
            public boolean offer(Runnable runnable) {
                return super.tryTransfer(runnable);
            }
        };
        threadPoolExecutor = new ThreadPoolExecutor(0, maxThread, 0, TimeUnit.MILLISECONDS, queue, new CustomizableThreadFactory(""));
        threadPoolExecutor.setRejectedExecutionHandler((r, executor) -> {
            try {
                executor.getQueue().put(r);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        init(5);
        for (int i=0; i< 10; i++) {
            final int index = i+1;
            threadPoolExecutor.execute(() -> System.out.printf("第%s个线程，执行第%s个任务 %n",Thread.currentThread().getName(), index));
        }
    }
}
