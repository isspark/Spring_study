package com.study.spring.mvc.routerFunctionHandlerMapping;

import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;

public class RouterFunctionHandlerMappingTest {

    public static void main(String[] args) {
        AnnotationConfigServletWebServerApplicationContext context = new AnnotationConfigServletWebServerApplicationContext(WebConfig.class);
    }
}
