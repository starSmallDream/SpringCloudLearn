package cn.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @author xiaohuichao
 * @createdDate 2022/8/11 15:07
 */
@SpringBootApplication
@RestController
@RefreshScope
public class CloudServerApplication {

    private Logger LOG = LoggerFactory.getLogger(CloudServerApplication.class);

    @Autowired
//    DiscoveryClient client;


    @Value("${info.foo}")
    private String helloValue;

    @RequestMapping("/")
    public String hello() {
//        List<ServiceInstance> instances = client.getInstances("HelloServer");
//        ServiceInstance selectedInstance = instances
//                .get(new Random().nextInt(instances.size()));
//        return "Hello World: " + selectedInstance.getServiceId() + ":" + selectedInstance
//                .getHost() + ":" + selectedInstance.getPort();

        return "my Hello World" + new Random().nextInt(999) + " => " + helloValue;
    }

    /**
     * 断路器Hello
     * @return
     */
    @RequestMapping(value = "/cb/hello")
    public String cbHello(@RequestParam(value = "s") Integer second) throws InterruptedException {
        LOG.info("Request Time {}", System.currentTimeMillis());
        Thread.sleep(second * 1000);
        return "cb/hello:" + second;
    }

    public static void main(String[] args) {
        SpringApplication.run(CloudServerApplication.class, args);
    }


}
