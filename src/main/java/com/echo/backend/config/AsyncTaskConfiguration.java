package com.echo.backend.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableScheduling   // enable spring schedule tasks execution support
@EnableAsync        // enabling spring async method execution support
public class AsyncTaskConfiguration implements AsyncConfigurer {
    private final Logger logger = LoggerFactory.getLogger(AsyncTaskConfiguration.class);
    @Override
    @Bean(name = "taskExecutor")
    public Executor getAsyncExecutor() {
        logger.debug("Creating Async Task Executor");

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);

        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(25);
        executor.setThreadNamePrefix("ExecutorThreadPool-");

        return executor;
    }

    /**When an exception is thrown from a method annotated with @Async, and that exception isn't caught within the method itself,
     it's considered an uncaught exception. The AsyncUncaughtExceptionHandler interface provides a way to handle such exceptions globally.*/
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
	/*This handler is used by default if no custom AsyncUncaughtExceptionHandler implementation is specified.
	It's a straightforward implementation that logs the uncaught exceptions to the console.*/
        return new SimpleAsyncUncaughtExceptionHandler();
    }

}
