package com.example.cloud_web;

import com.example.cloud_web.service.TestRetryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.*;


@SpringBootApplication
@EnableFeignClients
@RestController
//@EnableRetry(proxyTargetClass = true)
public class CloudWebApplication {

    private Logger LOG = LoggerFactory.getLogger(CloudWebApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(CloudWebApplication.class, args);
    }

    @Autowired
    HelloClient client;

    @RequestMapping("/")
    public String hello() {
        return client.hello();
    }

    @FeignClient(value = "helloClient", url = "http://localhost:8900/")
    interface HelloClient {

        @RequestMapping(value = "/", method = RequestMethod.GET)
        String hello();

        @Retryable
        @RequestMapping(value = "/cb/hello", method = RequestMethod.GET)
        String cbHello(@RequestParam(value = "s") Integer second);
    }


    @Autowired
    private CircuitBreakerFactory circuitBreakerFactory;

    @GetMapping("/delay/{seconds}")
    public String delay(@PathVariable Integer seconds) {
        return circuitBreakerFactory.create("slow").run( () -> client.cbHello(seconds), t -> {
            LOG.warn("delay call failed error", t);
            return "fallback:" + "world";
        });
    }

    @GetMapping("/delay/default/{seconds}")
    public String delayDefault(@PathVariable Integer seconds) {
        return circuitBreakerFactory.create("testSlow").run( () -> client.cbHello(seconds), t -> {
            LOG.warn("delay call failed error", t);
            return "fallback:" + "world";
        });
    }

    @Autowired
    private TestRetryService testRetryService;

    @GetMapping("/test/{seconds}")
    public String retryTest01(@PathVariable Integer seconds) throws InterruptedException {
        return testRetryService.service(seconds);
    }

    @GetMapping("/test2/{seconds}")
    public String retryTest02(@PathVariable Integer seconds) throws InterruptedException {
        return circuitBreakerFactory.create("slow").run( () -> {
            try {
                return testRetryService.service(seconds);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return "";
            }
        }, t -> {
            LOG.warn("delay call failed error", t);
            return "fallback:" + "world";
        });
    }


}
