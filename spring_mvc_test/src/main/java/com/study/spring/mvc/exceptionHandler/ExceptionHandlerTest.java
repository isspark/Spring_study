package com.study.spring.mvc.exceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class ExceptionHandlerTest {

    public static void main(String[] args) throws NoSuchMethodException {
        ExceptionHandlerExceptionResolver resolver = new ExceptionHandlerExceptionResolver();
        resolver.setMessageConverters(List.of(new MappingJackson2HttpMessageConverter()));
        resolver.afterPropertiesSet();

        //测试JSON
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerMethod handlerMethod = new HandlerMethod(new Controller1(), Controller1.class.getMethod("test"));
        Exception e = new ArithmeticException("by zero");
        resolver.resolveException(request, response, handlerMethod, e);
        System.out.println(new String(response.getContentAsByteArray(), StandardCharsets.UTF_8));
        //测试ModelAndView
        HandlerMethod handlerMethod2 = new HandlerMethod(new Controller2(), Controller2.class.getMethod("test"));
        Exception e2 = new ArithmeticException("by zero2");
        ModelAndView mv = resolver.resolveException(request, response, handlerMethod2, e2);
        System.out.println(mv.getModel());
        System.out.println(mv.getViewName());
        //测试异常嵌套情况
        MockHttpServletResponse response2 = new MockHttpServletResponse();
        Exception e3 = new Exception("e1", new RuntimeException("e2",new IOException("e3")));
        HandlerMethod handlerMethod3 = new HandlerMethod(new Controller3(), Controller3.class.getMethod("test"));
        resolver.resolveException(request, response2, handlerMethod3, e3);
        System.out.println(new String(response2.getContentAsByteArray(), StandardCharsets.UTF_8));
        //测试异常处理方法参数解析
        MockHttpServletResponse response3 = new MockHttpServletResponse();
        HandlerMethod handlerMethod4 = new HandlerMethod(new Controller4(), Controller4.class.getMethod("test"));
        Exception e4 = new ArithmeticException("by zero4");
        resolver.resolveException(request, response3, handlerMethod4, e4);
        System.out.println(new String(response3.getContentAsByteArray(), StandardCharsets.UTF_8));
        //没有设置异常处理方法，所以什么都不会输出
        MockHttpServletResponse response5 = new MockHttpServletResponse();
        HandlerMethod handlerMethod5 = new HandlerMethod(new Controller5(), Controller5.class.getMethod("test"));
        Exception e5 = new ArithmeticException("by zero5");
        resolver.resolveException(request, response5, handlerMethod5, e5);
        System.out.println(new String(response5.getContentAsByteArray(), StandardCharsets.UTF_8));
        //增加全局处理器后，可以处理异常了
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(WebConfig.class);
        ExceptionHandlerExceptionResolver resolver1 = context.getBean(ExceptionHandlerExceptionResolver.class);
        resolver1.resolveException(request, response5, handlerMethod5, e5);
        System.out.println(new String(response5.getContentAsByteArray(), StandardCharsets.UTF_8));
    }

    static class Controller1 {
        public void test() {

        }

        @ExceptionHandler
        @ResponseBody
        public Map<String, Object> handle(ArithmeticException e) {
            return Map.of("error", e.getMessage());
        }
    }

    static class Controller2 {
        public void test() {

        }

        @ExceptionHandler
        public ModelAndView handle(ArithmeticException e) {
            return new ModelAndView("viewName",Map.of("error", e.getMessage()));
        }
    }

    static class Controller3 {
        public void test() {

        }

        @ExceptionHandler
        @ResponseBody
        public Map<String, Object> handle(IOException e) {
            return Map.of("error", e.getMessage());
        }
    }

    static class Controller4 {
        public void test() {

        }

        @ExceptionHandler
        @ResponseBody
        public Map<String,Object> handler(Exception e , HttpServletRequest request){
            System.out.println(request);
            return Map.of("error", e.getMessage());
        }
    }

    static class Controller5 {
        public void test() {

        }
    }
}
