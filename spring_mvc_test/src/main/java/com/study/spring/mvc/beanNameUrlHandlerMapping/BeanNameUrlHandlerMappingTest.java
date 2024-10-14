package com.study.spring.mvc.beanNameUrlHandlerMapping;

import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

public class BeanNameUrlHandlerMappingTest {

    public static void main(String[] args) {
        AnnotationConfigServletWebServerApplicationContext context = new AnnotationConfigServletWebServerApplicationContext(WebConfig.class);
//        RequestMappingHandlerMapping handlerMapping = context.getBean(RequestMappingHandlerMapping.class);
//        handlerMapping.getHandlerMethods().forEach((k, v) -> System.out.println("路径："+k + " \t 方法信息：" + v));
    }
}
