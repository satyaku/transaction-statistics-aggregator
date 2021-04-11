package com.n26.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ThreadConfig {
	
	@Value("${config.executor.corePoolSize:10}")
	private int corePoolSize;
	
	@Value("${config.executor.maxPoolSize:10}")
	private int maxPoolSize;

	
	/**
	 * This Bean is declared to provide the system a pool support
	 * for async processing. Core and max pool size can be changed as per requirement
	 * from the application.yml file
	 * 
	 * @return TaskExecutor
	 */
	@Bean
    public TaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setThreadNamePrefix("task_executor");
        executor.initialize();
        return executor;
    }
}
