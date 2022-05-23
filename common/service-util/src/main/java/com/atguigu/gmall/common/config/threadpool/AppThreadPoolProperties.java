package com.atguigu.gmall.common.config.threadpool;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/23 21:11
 */
@Data
@ConfigurationProperties(prefix = "app.threadpool")
public class AppThreadPoolProperties {

    private  Integer corePoolSize;//核心线程数
    private  Integer maximumPoolSize;//最大线程数
    private  Integer keepAliveTime;//以分钟为单位
    private TimeUnit unit = TimeUnit.MINUTES;
    private  Integer queueSize = 1000;//队列大小

    //拒绝策略 //如果没有异步能力，慢一点就慢一点，同步把他运行完
    private RejectedExecutionHandler rejectHandler = new ThreadPoolExecutor.CallerRunsPolicy();

}
