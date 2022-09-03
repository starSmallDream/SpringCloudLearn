package com.example.cloud_web.conf;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.circuitbreaker.springretry.SpringRetryCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.springretry.SpringRetryConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.policy.CircuitBreakerRetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.policy.TimeoutRetryPolicy;

import java.time.Duration;
import java.time.temporal.TemporalUnit;

/**
 * 断路器配置
 * @author xiaohuichao
 * @createdDate 2022/8/11 15:46
 */
@Configuration(proxyBeanMethods = false)
public class MyCircuitBreakerConfig {

    /**
     * 全局的默认断路器
     * @return
     */
//    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> defaultCustomizer() {
        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(4)).build())
                .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
                .build());
    }

    /**
     * 特定的断路器
     * @return
     */
//    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> slowCustomizer() {
        return factory -> factory.configure(builder -> builder.circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
                .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(2)).build()), "slow");
    }


    @Bean
    public Customizer<SpringRetryCircuitBreakerFactory> defaultCustomizerRetry() {
        TimeoutRetryPolicy timeoutRetryPolicy = new TimeoutRetryPolicy();
        timeoutRetryPolicy.setTimeout(5000);
        return factory -> factory.configureDefault(id -> new SpringRetryConfigBuilder(id)
                .retryPolicy(timeoutRetryPolicy).build());
    }

    @Bean
    public Customizer<SpringRetryCircuitBreakerFactory> slowCustomizerRetry() {

        CircuitBreakerRetryPolicy circuitBreakerRetryPolicy = new CircuitBreakerRetryPolicy(new SimpleRetryPolicy(6));
        circuitBreakerRetryPolicy.setOpenTimeout(Duration.ofSeconds(4).toMillis());
        circuitBreakerRetryPolicy.setResetTimeout(Duration.ofSeconds(3).toMillis());

        return factory -> factory.configure(builder -> builder.retryPolicy(circuitBreakerRetryPolicy).build(), "slow");
    }

}
