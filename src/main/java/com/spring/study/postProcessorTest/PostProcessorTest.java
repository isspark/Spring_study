package com.spring.study.postProcessorTest;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.support.GenericApplicationContext;

public class PostProcessorTest {

    public static void main(String[] args) {
        GenericApplicationContext context = new GenericApplicationContext();
        context.registerBean("config",Config.class);
        context.registerBean(ConfigurationClassPostProcessor.class);
//        context.registerBean(MapperScannerConfigurer.class, bd -> {
//            bd.getPropertyValues().add("basePackage","com.spring.study.postProcessorTest");
//        });
        context.refresh();
        context.close();
    }
}
