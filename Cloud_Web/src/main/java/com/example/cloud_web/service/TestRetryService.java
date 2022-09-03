package com.example.cloud_web.service;

import org.springframework.remoting.RemoteAccessException;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * @author xiaohuichao
 * @createdDate 2022/8/12 16:38
 */
@Service
public class TestRetryService {

    @Retryable
    public String service(Integer seconds) throws InterruptedException {
        // ... do something
        System.out.println("service");
        Thread.sleep(Duration.ofSeconds(seconds).toMillis());
        return "service method";
    }
//    @Recover
    public void recover(RemoteAccessException e) {
        // ... panic
        System.out.println("recover");
    }

}
