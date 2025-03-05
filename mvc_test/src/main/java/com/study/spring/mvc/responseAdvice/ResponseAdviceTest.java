package com.study.spring.mvc.responseAdvice;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.annotation.ExpressionValueMethodArgumentResolver;
import org.springframework.web.method.annotation.RequestHeaderMethodArgumentResolver;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolverComposite;
import org.springframework.web.method.support.HandlerMethodReturnValueHandlerComposite;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

public class ResponseAdviceTest {

    public static void main(String[] args) throws Exception {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(WebConfig.class);

        ServletInvocableHandlerMethod handlerMethod = new ServletInvocableHandlerMethod(
                context.getBean(WebConfig.MyController.class),
                WebConfig.MyController.class.getMethod("user")
        );

        handlerMethod.setDataBinderFactory(new ServletRequestDataBinderFactory(Collections.emptyList(),null));
        handlerMethod.setParameterNameDiscoverer(new DefaultParameterNameDiscoverer());
        handlerMethod.setHandlerMethodArgumentResolvers(getArgumentResolverComposite(context));
        handlerMethod.setHandlerMethodReturnValueHandlers(getReturnValueHandlerComposite());

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        handlerMethod.invokeAndHandle(new ServletWebRequest(request,response),new ModelAndViewContainer());

        System.out.println(new String(response.getContentAsByteArray(), StandardCharsets.UTF_8));
        context.close();

    }

    public static HandlerMethodArgumentResolverComposite getArgumentResolverComposite(AnnotationConfigApplicationContext context){
        HandlerMethodArgumentResolverComposite composite = new HandlerMethodArgumentResolverComposite();
        composite.addResolver(new RequestParamMethodArgumentResolver(context.getBeanFactory(), false));
        composite.addResolver(new RequestHeaderMethodArgumentResolver(context.getBeanFactory()));
        composite.addResolver(new ServletCookieValueMethodArgumentResolver(context.getBeanFactory()));
        composite.addResolver(new ExpressionValueMethodArgumentResolver(context.getBeanFactory()));
        composite.addResolver(new PathVariableMethodArgumentResolver());
        composite.addResolver(new ServletRequestMethodArgumentResolver());
        composite.addResolver(new ServletModelAttributeMethodProcessor(false));
        composite.addResolver(new RequestResponseBodyMethodProcessor(List.of(new MappingJackson2HttpMessageConverter())));
        composite.addResolver(new ServletModelAttributeMethodProcessor(true));
        composite.addResolver(new RequestParamMethodArgumentResolver(context.getBeanFactory(), true));
        return composite;
    }

    private static HandlerMethodReturnValueHandlerComposite getReturnValueHandlerComposite() {
        HandlerMethodReturnValueHandlerComposite composite = new HandlerMethodReturnValueHandlerComposite();
        composite.addHandler(new ModelAndViewMethodReturnValueHandler());
        composite.addHandler(new ViewNameMethodReturnValueHandler());
        composite.addHandler(new ServletModelAttributeMethodProcessor(false));
        composite.addHandler(new HttpEntityMethodProcessor(List.of(new MappingJackson2HttpMessageConverter())));
        composite.addHandler(new HttpHeadersReturnValueHandler());
        composite.addHandler(new RequestResponseBodyMethodProcessor(List.of(new MappingJackson2HttpMessageConverter()),List.of(new WebConfig.MyControllerAdvice())));
        composite.addHandler(new ServletModelAttributeMethodProcessor(true));
        return composite;
    }

}

class User{
    private String name;

    private int age;

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}

class Result{
    private int code;
    private String message;
    private Object data;

    public Result(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static Result ok(Object data){
        return new Result(200,"success",data);
    }
}