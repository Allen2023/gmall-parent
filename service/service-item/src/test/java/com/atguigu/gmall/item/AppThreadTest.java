package com.atguigu.gmall.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 1、new Thread
 * <p>
 * 2、异步(CompletableFuture)+线程池(ThreadPoolExecutor)
 * CompletableFuture：
 *
 *
 * 使用线程池
 * 1、准备自定义一个线程池
 * 2、CompletableFuture 给线程池中提交任务
 * 3、对提交的任务进行编排、组合、容错处理
 */
@SpringBootTest //这是一个SpringBoot测试
public class AppThreadTest {
    @Qualifier("corePool")
    @Autowired()
    ThreadPoolExecutor poolExecutor;

    /**
     * 启动异步任务
     */
    //@Transactional
    @Test
    public void startAsyncTest() {
        /*System.out.println("线程池:"+ poolExecutor);
        int corePoolSize = poolExecutor.getCorePoolSize();
        System.out.println("corePoolSize = " + corePoolSize);*/
        //不带返回值
        /*CompletableFuture.runAsync(()->{
            System.out.println(Thread.currentThread().getName() + "1324");
        },poolExecutor);*/
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread() + "正在计算");
            //System.out.println(Thread.currentThread().getName() + "正在计算");
            Double random = Math.random() * 100;

            return random.intValue();
        }, poolExecutor);
        try {
            Integer result = future.get();
            System.out.println("结果:" + result);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * CompletableFuture future
     * 1、thenXXX： 前一个任务结束以后，继续做接下来的事情
     * 2、whenXxx: when的事件回调
     * whenComplete： 完成后干啥
     * 前一个任务.whenComplete((t,u)->{ 处理t[上一步结果],u[上一步异常] })
     * xxxxAsync： 带了Async代表这些方法运行需要开新线程
     * 指定线程池：  就在指定线程池中开新线程
     * 3、exceptionally： 前面异常以后干什么
     */
    @Test
    public void bianPaiTest() {
        /*CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            System.out.println("哈哈");
            int i = 10 / 0;
        }, poolExecutor);
//        future完成立即执行whenComplete
        future.whenComplete((t,u)->{
            System.out.println("t"+t);
            System.out.println("u"+u);
        });*/

        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread() + "正在计算");
            System.out.println("哈哈");
            return 10 / 0;
        }, poolExecutor);
//        future完成立即执行whenComplete
        /*future.whenCompleteAsync((t, u) -> {
            System.out.println(Thread.currentThread() + "正在计算");
            if (u != null) {
                System.out.println("u" + u);//future的异常
            } else {
                System.out.println("t" + t);//future的返回值
            }
        }, poolExecutor);*/
        //exceptionally前面异常以后干什么 R apply(T t)
        future.exceptionally((t) -> {
            System.out.println(" 上次的异常:" + t);
            return null;
        });
    }

    @Test
    public void exceptionTest() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread() + "正在计算");
            System.out.println("哈哈");
            return 10 / 0;
        }, poolExecutor);
        //异常结果才会运行exceptionally
        CompletableFuture<Integer> exceptionally = future.exceptionally((exception) -> {
            System.out.println("异常 = " + exception);
            return 1;
        });
        Integer integer = exceptionally.get();
        System.out.println("integer = " + integer);
    }

    //链式调用
    @Test
    public void lianshidiaoyong() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread() + "正在计算");
            System.out.println("哈哈");
            return 10 / 1;
        }, poolExecutor).exceptionally((t) -> {//产生异常执行
            System.out.println("异常" + t);
            return 1;
        });
        Integer integer = future.get();//运行正常执行
        System.out.println("integer = " + integer);
    }

    /**
     * then系列进行任务编排
     * 1、thenRun：  传入 Runnable 启动一个无返回值的异步任务，
     * thenRun
     * thenRunAsync
     * thenRunAsync(带线程池)
     * 2、thenAccept:   传入 Consumer  void accept(T t); 接参数，但是也无返回值
     * thenAccept
     * thenAcceptAsync
     * thenAcceptAsync(带线程池)
     * 3、thenApply: 传入  Function:  R apply(T t);  而且有返回值
     */
    //异步编排
    @Test
    public void thenTest() throws ExecutionException, InterruptedException {
        //1.计算
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread() + "正在计算");
            int i = 1 + 1;
            return i;
        }, poolExecutor).thenApplyAsync((result) -> {
            System.out.println(Thread.currentThread() + "正在转换");
            result = result + 10;
            return result;
        }, poolExecutor).thenApplyAsync((result) -> {
            System.out.println(Thread.currentThread() + "变成字母");
            return result + "A";
        }, poolExecutor);
        String s = future.get();//运行正常执行
        System.out.println("s = " + s);
    }

    @Test
    public void zuheTest() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            System.out.println("打印A");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("A");
        });
        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
            System.out.println("打印B");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("B");
        });
        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> {
            System.out.println("打印C");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("C");
        });

        CompletableFuture.allOf(future, future1, future2)
                .whenComplete((a, b) -> {
                    System.out.println("结果" + a);
                    System.out.println("异常" + b);
        });
        CompletableFuture.anyOf(future, future1, future2)
                .whenComplete((a, b) -> {
                    System.out.println("结果" + a);
                    System.out.println("异常" + b);
                });
        //1.这三个任务全部完成以后 打印D
        /*long start = System.currentTimeMillis();
        System.out.println("start..");
        future.get();
        future1.get();
        future2.get();
        long end = System.currentTimeMillis();
        System.out.println("D----" + (end-start)/1000);*/
    }
}
