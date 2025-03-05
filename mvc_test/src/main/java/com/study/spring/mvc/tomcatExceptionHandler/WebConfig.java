package com.study.spring.mvc.tomcatExceptionHandler;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistrarBeanPostProcessor;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.view.BeanNameViewResolver;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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

    @Bean
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        return new RequestMappingHandlerMapping();
    }

    @Bean
    public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
        RequestMappingHandlerAdapter adapter = new RequestMappingHandlerAdapter();
        adapter.setMessageConverters(List.of(new MappingJackson2HttpMessageConverter()));
        return adapter;
    }

    //修改了Tomcat 默认错误地址
    //出现错误，会使用请求转发 forward 跳转到 error 地址
    @Bean
    public ErrorPageRegistrar errorPageRegistrar() throws Exception {
        return registry -> registry.addErrorPages(new ErrorPage("/error"));
    }

    @Bean
    public ErrorPageRegistrarBeanPostProcessor errRegistrationBeanPostProcessor() {
        return new ErrorPageRegistrarBeanPostProcessor();
    }

    @Bean
    public BasicErrorController basicErrorController() {
        ErrorProperties properties =  new ErrorProperties();
        //返回信息包含异常信息
        properties.setIncludeException(true);
        return new BasicErrorController(new DefaultErrorAttributes(),properties);
    }

    @Bean
    public View error() {
        return new View() {
            @Override
            public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws IOException {
                System.out.println("error");
                response.setContentType("text/html;charset=utf-8");
                response.getWriter().println("""
                        <h3>错误信息</h3>
                        """);
            }
        };
    }

    @Bean
    public ViewResolver viewResolver(){
        return new BeanNameViewResolver();
    }

    @Controller
    public static class MyController {

        @RequestMapping("/test")
        public ModelAndView test() {
            int i = 1 / 0;
            return null;
        }

//        @RequestMapping("/error")
//        @ResponseBody
//        public String error(HttpServletRequest request){
//            Throwable throwable = (Throwable) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
//            return throwable.getMessage();
//        }

    }
}
