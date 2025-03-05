package com.xstar.life.springboot.boot_test.tomcat;

import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.List;
import java.util.Map;

@Configuration
public class WebConfig {

    @Bean
    public DispatcherServletRegistrationBean registrationBean(DispatcherServlet servlet) {
        return new DispatcherServletRegistrationBean(servlet, "/");
    }

    @Bean DispatcherServlet dispatcherServlet(WebApplicationContext applicationContext) {
        return new DispatcherServlet(applicationContext);
    }

    @Bean
    public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
        RequestMappingHandlerAdapter adapter = new RequestMappingHandlerAdapter();
        adapter.setMessageConverters(List.of(new MappingJackson2HttpMessageConverter()));
        return adapter;
    }

    @RestController
    static class MyController{

        @RequestMapping("/hello2")
        public Map<String,Object> hello(){
            return Map.of("hello2","world");
        }

    }
}
