package com.spring.study.beanLifeCycleTest;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SpringBeanCycle {

    public SpringBeanCycle() {
        System.out.println("SpringBeanCycle constructor");
    }

    @Autowired
    public void autowire(@Value("${JAVA_HOME}") String home){
        System.out.println("home: " + home);
    }

    @PostConstruct
    public void init() {
        System.out.println("SpringBeanCycle init");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("SpringBeanCycle destroy");
    }
}
