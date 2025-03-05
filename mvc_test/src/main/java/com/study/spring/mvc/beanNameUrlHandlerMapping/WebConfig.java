package com.study.spring.mvc.beanNameUrlHandlerMapping;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter;

import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class WebConfig {
    @Bean
    public TomcatServletWebServerFactory tomcatServletWebServerFactory() {
        return new TomcatServletWebServerFactory();
    }

    @Bean
    public DispatcherServlet dispatcherServlet() {
        return new DispatcherServlet();
    }

    @Bean
    public DispatcherServletRegistrationBean dispatcherServletRegistrationBean(DispatcherServlet dispatcherServlet) {
        return new DispatcherServletRegistrationBean(dispatcherServlet, "/");
    }

//    //路径映射，将根据请求路径需要对应的处理类，以/开头
//    @Bean
//    public BeanNameUrlHandlerMapping beanNameUrlHandlerMapping() {
//        return new BeanNameUrlHandlerMapping();
//    }
//
//    //控制器方法调用，调用handleRequest方法
//    @Bean
//    public SimpleControllerHandlerAdapter simpleControllerHandlerAdapter() {
//        return new SimpleControllerHandlerAdapter();
//    }

    //自定义HandlerMapping
    @Component
    static class MyHandlerMapping implements HandlerMapping {
        @Override
        public HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
            String key = request.getRequestURI();
            Controller controller = collect.get(key);
            if(null == controller){
                return null;
            }
            return new HandlerExecutionChain(controller);
        }

        @Autowired
        private ApplicationContext context;
        private Map<String,Controller> collect;

        @PostConstruct
        public void init() {
            collect = context.getBeansOfType(Controller.class)
                    .entrySet().stream()
                    .filter(e -> e.getKey().startsWith("/"))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            System.out.println(collect);
        }
    }

    static class MyHandlerAdapter implements HandlerAdapter{

        //只处理Controller的实现
        @Override
        public boolean supports(Object handler) {
            return handler instanceof Controller;
        }

        //处理器方法调用
        @Override
        public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            if(handler instanceof Controller controller){
                controller.handleRequest(request,response);
            }
            return null;
        }

        @Override
        public long getLastModified(HttpServletRequest request, Object handler) {
            return 0;
        }
    }

    @Component("/c1")
    public static class Controller1 implements Controller {

        @Override
        public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
            response.getWriter().print("this is c1");
            return null;
        }
    }

    @Component("c2")
    public static class Controller2 implements Controller {

        @Override
        public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
            response.getWriter().print("this is c2");
            return null;
        }
    }

    @Bean("/c3")
    public Controller Controller3() {
        return (request, response) -> {
            response.getWriter().print("this is c4");
            return null;
        };
    }

}
