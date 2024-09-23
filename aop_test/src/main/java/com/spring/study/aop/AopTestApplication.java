package com.spring.study.aop;

import com.spring.study.aop.service.MyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
//@EnableAspectJAutoProxy
public class AopTestApplication {

    private static final Logger log = LoggerFactory.getLogger(AopTestApplication.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(AopTestApplication.class, args);

        MyService myService = applicationContext.getBean(MyService.class);
        log.info("myService: {}", myService.getClass());
        myService.test();

        applicationContext.close();
    }

}
