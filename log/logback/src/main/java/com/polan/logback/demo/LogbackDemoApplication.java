package com.polan.logback.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>启动类 +controller</p>
 * @author panliyong  2019-07-25 15:18
 */
@SpringBootApplication
@RestController
public class LogbackDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(LogbackDemoApplication.class, args);
    }

}
