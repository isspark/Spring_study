package com.spring.study.aop.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MyService {

    private static final Logger log = LoggerFactory.getLogger(MyService.class);

    public void test(){
        log.info("test()");
        test2();
    }

    public void test2(){
        log.info("test2()");
    }
}
