package com.example.mysqltest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.Executor;
@Configuration
//@EnableAsync
public class AsyncConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncConfiguration.class);
    @Bean (name = "taskExecutor")
    public Executor taskExecutor() {
        LOGGER.debug("Creating Async Task Executor");
//        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
////        // Adjust the core and max pool size based on the number of available CPU cores
////        int cores = Runtime.getRuntime().availableProcessors();
////        executor.setCorePoolSize(cores);
////        executor.setMaxPoolSize(2 * cores);
////
////        // Use an unbounded queue or set a larger queue capacity based on workload
////        executor.setQueueCapacity(Integer.MAX_VALUE);
//        executor.setCorePoolSize(Integer.MAX_VALUE); // Set a very large number
//        executor.setMaxPoolSize(Integer.MAX_VALUE);
//        executor.setQueueCapacity(Integer.MAX_VALUE);
//        executor.setThreadNamePrefix("Thread-");
//        executor.initialize();
        SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor();
        executor.setThreadNamePrefix("Thread-");
        return executor;
    }
}