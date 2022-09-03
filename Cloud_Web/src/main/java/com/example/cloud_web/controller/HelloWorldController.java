package com.example.cloud_web.controller;

import com.example.cloud_web.model.Greeting;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author xiaohuichao
 * @createdDate 2022/7/18 16:00
 */
@RestController()
@RefreshScope
public class HelloWorldController {

    private static final String template = "Hello, %s! = %s";
    private final AtomicLong counter = new AtomicLong();

    @Value("${info.foo}")
    private String helloValue;

    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name,helloValue));
    }

}
