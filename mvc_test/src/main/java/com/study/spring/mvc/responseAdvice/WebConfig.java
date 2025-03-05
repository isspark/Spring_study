package com.study.spring.mvc.responseAdvice;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@Configuration
public class WebConfig {


    @ControllerAdvice
    static class MyControllerAdvice implements ResponseBodyAdvice<Object> {

        /*
        满足条件才转换
         */
        @Override
        public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
            return true;
        }

        @Override
        public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
            if (body instanceof Result) {
                return body;
            }
            // 查看方法上的@ResponseBody 注解
//            if(returnType.getMethodAnnotation(ResponseBody.class) != null){
//                return Result.ok(body);
//            }
            //查看类中是否存在@ResponseBody 注解
//            if(returnType.getContainingClass().isAnnotationPresent(ResponseBody.class)){
//                return Result.ok(body);
//            }
            //可以查找注解中是否包含@ResponseBody 注解
            if (AnnotationUtils.findAnnotation(returnType.getContainingClass(), ResponseBody.class) != null) {
                return Result.ok(body);
            }
            return body;
        }
    }

//    @Controller
    @RestController
    public static class MyController {
//        @ResponseBody
        public User user() {
            return new User("xstar", 22);
        }
    }

}
