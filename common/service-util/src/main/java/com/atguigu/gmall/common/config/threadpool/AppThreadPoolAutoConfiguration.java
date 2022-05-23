package com.atguigu.gmall.common.config.threadpool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/23 21:05
 * 线程池的自动配置类
 */
@Slf4j
@EnableConfigurationProperties(AppThreadPoolProperties.class)
@Configuration
public class AppThreadPoolAutoConfiguration {
    //@Qualifier("corePool")
//    @Autowired
//    ThreadPoolExecutor threadPoolExecutor;

    @Primary//主要注入的 不说明注入哪个就注入Primary
    @Bean
    public ThreadPoolExecutor corePool(AppThreadPoolProperties threadPoolProperties,
                                       @Value("${spring.application.name:defaultApp}")String appName) {
        /**
         int corePoolSize,
         int maximumPoolSize,
         long keepAliveTime,
         TimeUnit unit,
         BlockingQueue<Runnable> workQueue,
         ThreadFactory threadFactory,
         RejectedExecutionHandler handler
         */
        return new ThreadPoolExecutor(
            threadPoolProperties.getCorePoolSize(),//核心线程数
                threadPoolProperties.getMaximumPoolSize(),//最大线程数
                threadPoolProperties.getKeepAliveTime(),//以分钟为单位
                threadPoolProperties.getUnit(),
                new LinkedBlockingQueue<>(threadPoolProperties.getQueueSize()),//队列大小
                new AppThreadFactory(appName+"-core"),//线程工厂
                threadPoolProperties.getRejectHandler()//拒绝策略 //如果没有异步能力，慢一点就慢一点，同步把他运行完
        );
    }

    @Bean
    public ThreadPoolExecutor otherPoolpoll(AppThreadPoolProperties threadPoolProperties,
                                            @Value("${spring.application.name:defaultApp}")String appName) {
        return new ThreadPoolExecutor(
                threadPoolProperties.getCorePoolSize()/2,//核心线程数
                threadPoolProperties.getMaximumPoolSize()/2,//最大线程数
                threadPoolProperties.getKeepAliveTime(),//以分钟为单位
                threadPoolProperties.getUnit(),
                new LinkedBlockingQueue<>(threadPoolProperties.getQueueSize()/2),//队列大小
                new AppThreadFactory(appName+"-other"),//线程工厂
                threadPoolProperties.getRejectHandler()//拒绝策略 //如果没有异步能力，慢一点就慢一点，同步把他运行完
        );
    }


    class AppThreadFactory implements ThreadFactory {
        private  String appName;
       private AtomicInteger count = new AtomicInteger(1);

        public AppThreadFactory(String appName) {
            this.appName = appName;
        }
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName("["+appName+"]"+count.getAndIncrement());
//            thread.setPriority(10);
            return thread;
        }
    }
}
