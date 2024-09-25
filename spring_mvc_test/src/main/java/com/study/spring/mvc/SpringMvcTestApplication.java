package com.study.spring.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Map;

//@SpringBootApplication
public class SpringMvcTestApplication {

    public static void main(String[] args) throws Exception {
//        SpringApplication.run(SpringMvcTestApplication.class, args);
        AnnotationConfigServletWebServerApplicationContext context = new AnnotationConfigServletWebServerApplicationContext(WebConfig.class);

        RequestMappingHandlerMapping handlerMapping = context.getBean(RequestMappingHandlerMapping.class);

        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();
        handlerMethods.forEach((requestMappingInfo, handlerMethod) -> System.out.println(requestMappingInfo + ":" + handlerMethod));

        HandlerExecutionChain chain = handlerMapping.getHandler(new MockHttpServletRequest("GET","/test"));

        System.out.println(">>>>>>>>>>>>>>>>");
        MockHttpServletRequest request = new MockHttpServletRequest("GET","/test");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MyRequestMappingHandlerAdapter handlerAdapter = context.getBean(MyRequestMappingHandlerAdapter.class);
        handlerAdapter.invokeHandlerMethod(request, response, (HandlerMethod) chain.getHandler());
        System.out.println(">>>>>>>>>>>>>>>>>>arg");
        handlerAdapter.getArgumentResolvers().forEach(System.out::println);
        System.out.println(">>>>>>>>>>>>>>>>>>return");
        handlerAdapter.getReturnValueHandlers().forEach(System.out::println);

        MockHttpServletRequest request2 = new MockHttpServletRequest("GET","/test2");
        request2.addHeader("token","1111");
        HandlerExecutionChain chain2 = handlerMapping.getHandler(request2);
        MockHttpServletResponse response2 = new MockHttpServletResponse();
        handlerAdapter.invokeHandlerMethod(request2, response2, (HandlerMethod) chain2.getHandler());

        //aggregation
    }

}
