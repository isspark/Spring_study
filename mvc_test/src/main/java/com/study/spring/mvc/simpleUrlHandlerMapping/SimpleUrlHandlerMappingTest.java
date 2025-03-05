package com.study.spring.mvc.simpleUrlHandlerMapping;

import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;

public class SimpleUrlHandlerMappingTest {

    public static void main(String[] args) {
        AnnotationConfigServletWebServerApplicationContext context = new AnnotationConfigServletWebServerApplicationContext(WebConfig.class);
    }
}
