package com.dodlu.core.app.configuration;

import static java.util.concurrent.Executors.newScheduledThreadPool;

import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;

@Configuration
@EnableAsync
@ConfigurationProperties(prefix = "async")
public class AsyncConfiguration implements AsyncConfigurer {
  private final Integer poolSize = 100;
  private final Integer poolMaxSize = 100;
  private final Integer queueCapacity = 200;
  private final Integer poolScheduleSize = 50;

  @Bean("asyncExecutor")
  @Override
  public Executor getAsyncExecutor() {
    ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
    threadPoolTaskExecutor.setCorePoolSize(poolSize);
    threadPoolTaskExecutor.setMaxPoolSize(poolMaxSize);
    threadPoolTaskExecutor.setQueueCapacity(queueCapacity);
    threadPoolTaskExecutor.initialize();

    return new DelegatingSecurityContextAsyncTaskExecutor(threadPoolTaskExecutor);
  }

  @Bean
  public ScheduledExecutorService getExecutorService() {
    return newScheduledThreadPool(poolScheduleSize);
  }

  @Bean
  public MethodInvokingFactoryBean methodInvokingFactoryBean() {
    MethodInvokingFactoryBean methodInvokingFactoryBean = new MethodInvokingFactoryBean();
    methodInvokingFactoryBean.setTargetClass(SecurityContextHolder.class);
    methodInvokingFactoryBean.setTargetMethod("setStrategyName");
    methodInvokingFactoryBean.setArguments(
        new String[] {SecurityContextHolder.MODE_INHERITABLETHREADLOCAL});
    return methodInvokingFactoryBean;
  }
}
