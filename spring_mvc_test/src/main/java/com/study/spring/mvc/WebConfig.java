package com.study.spring.mvc;

import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Collections;

@Configuration
@ComponentScan
public class WebConfig {

    public static void main(String[] args) {
//        SpringApplication.run(SpringMvcTestApplication.class, args);
//        AnnotationConfigServletWebApplicationContext context = new AnnotationConfigServletWebApplicationContext(WebConfig.class);
        AnnotationConfigServletWebServerApplicationContext context = new AnnotationConfigServletWebServerApplicationContext(WebConfig.class);
    }

    //1. 内嵌WEB容器工厂
    @Bean
    public ServletWebServerFactory tomcatServletWebServerFactory() {
        return new TomcatServletWebServerFactory();
    }

    //2.创建DispatcherServlet
    @Bean
    public DispatcherServlet dispatcherServlet() {
        return new DispatcherServlet();
    }

    //3.注册DispatcherServlet
    @Bean
    public DispatcherServletRegistrationBean dispatcherServletRegistrationBean(DispatcherServlet dispatcherServlet) {
        return new DispatcherServletRegistrationBean(dispatcherServlet, "/");
    }

    @Bean
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        return new RequestMappingHandlerMapping();
    }

    @Bean
    public MyRequestMappingHandlerAdapter requestMappingHandlerAdapter(){
        MyRequestMappingHandlerAdapter adapter = new MyRequestMappingHandlerAdapter();
        adapter.setArgumentResolvers(Collections.singletonList(new TokenArgumentResolver()));
        return adapter;
    }
//    @Bean("/hello")
//    public Controller controller1(){
//        return (request, response) -> {
//            response.getWriter().print("hello");
//            return null;
//        };
//    }
}
