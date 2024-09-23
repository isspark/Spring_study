package com.spring.study.beanLifeCycleTest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@SpringBootApplication(scanBasePackages = {"com.spring.study.beanLifeCycleTest"})
public class BeanCycleTest {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(BeanCycleTest.class,args);

        context.close();
    }
}
