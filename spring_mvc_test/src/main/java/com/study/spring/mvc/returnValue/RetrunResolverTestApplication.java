package com.study.spring.mvc.returnValue;

import freemarker.template.Configuration;
import freemarker.template.Template;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockPart;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.support.HandlerMethodReturnValueHandlerComposite;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.method.annotation.HttpEntityMethodProcessor;
import org.springframework.web.servlet.mvc.method.annotation.HttpHeadersReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.ModelAndViewMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;
import org.springframework.web.servlet.mvc.method.annotation.ViewNameMethodReturnValueHandler;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import java.io.StringWriter;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.Map;

//@SpringBootApplication
public class RetrunResolverTestApplication {

    public static void main(String[] args) throws Exception {
//        SpringApplication.run(SpringMvcTestApplication.class, args);
        AnnotationConfigServletWebServerApplicationContext context = new AnnotationConfigServletWebServerApplicationContext(EmptyWebConfig.class);

        HttpServletRequest request = mockRequest();

        //1. 控制器方法被封装为HandlerMethod对象
        Method method = Controller.class.getDeclaredMethod("test1");
        Controller controller = new Controller();
        Object returnValue = method.invoke(controller);

        HandlerMethodReturnValueHandlerComposite composite =  getReturnValueHandlerComposite();
        HandlerMethod handlerMethod = new HandlerMethod(controller, method);
        ModelAndViewContainer container = new ModelAndViewContainer();
        ServletWebRequest webRequest = new ServletWebRequest(request,new MockHttpServletResponse());
        if (composite.supportsReturnType(handlerMethod.getReturnType())) {
            composite.handleReturnValue(returnValue,handlerMethod.getReturnType(), container,webRequest);
            System.out.println(container.getModel());
            System.out.println(container.getViewName());
            renderView(context,container,webRequest);
        }

    }

    private static void renderView(AnnotationConfigServletWebServerApplicationContext context,ModelAndViewContainer container,ServletWebRequest webRequest) throws Exception {
        FreeMarkerViewResolver resolver = context.getBean(FreeMarkerViewResolver.class);
        MockHttpServletResponse response = new MockHttpServletResponse();
        String viewName = container.getViewName();
        System.out.println("试图解析的视图名称为：" + viewName);
        FreeMarkerView view = (FreeMarkerView) resolver.resolveViewName(viewName, Locale.CHINA);
        if (view == null) {
            throw new RuntimeException("无法解析视图名称：" + viewName);
        }
        view.render(container.getModel(), webRequest.getRequest(), response);
        String content = response.getContentAsString();
        System.out.println(content);
    }

    private static HandlerMethodReturnValueHandlerComposite getReturnValueHandlerComposite() {
        HandlerMethodReturnValueHandlerComposite composite = new HandlerMethodReturnValueHandlerComposite();
        composite.addHandler(new ModelAndViewMethodReturnValueHandler());
        composite.addHandler(new ViewNameMethodReturnValueHandler());
        composite.addHandler(new ServletModelAttributeMethodProcessor(false));
        composite.addHandler(new HttpEntityMethodProcessor(List.of(new MappingJackson2HttpMessageConverter())));
        composite.addHandler(new HttpHeadersReturnValueHandler());
        composite.addHandler(new RequestResponseBodyMethodProcessor(List.of(new MappingJackson2HttpMessageConverter())));
        composite.addHandler(new ServletModelAttributeMethodProcessor(true));
        return composite;
    }

    private static HttpServletRequest mockRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("name1", "张三");
        request.setParameter("age", "18");
        request.setParameter("name2", "李四");
        request.addPart(new MockPart("file", "abc", "hello".getBytes(StandardCharsets.UTF_8)));
        Map<String, String> uriTemplate = new AntPathMatcher()
                .extractUriTemplateVariables("/test/{id}", "/test/12");
        request.setAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, uriTemplate);
        request.setContentType("application/json");
        request.setCookies(new Cookie("token", "123456"));
        request.setParameter("name", "张三");
        request.setParameter("age", "18");
        request.setContent("""
        {
         "name": "lisi",
         "age": "18"
        }
        """.getBytes(StandardCharsets.UTF_8));
        return new StandardServletMultipartResolver().resolveMultipart(request);
    }

    static class Controller {
        public ModelAndView test1(){
            System.out.println("test1");
            ModelAndView mav = new ModelAndView("view1");
            mav.addObject("name","xstar");
            return mav;
        }

        public String test2(){
            System.out.println("test2");
            return "view2";
        }

        @ModelAttribute
        public User test3(){
            System.out.println("test3");
            return new User("xstar",30);
        }

        public User test4(){
            System.out.println("test4");
            return new User("xstar4",40);
        }

        public HttpEntity<User> test5(){
            System.out.println("test5");
            return new HttpEntity<>(new User("xstar5",50));
        }

        public HttpHeaders test6(){
            System.out.println("test6");
            HttpHeaders headers = new HttpHeaders();
            headers.add("token","123456");
            headers.add("Content-TYpe","application/json");
            return headers;
        }

        @ResponseBody
        public User test7(){
            System.out.println("test7");
            return new User("xstar7",60);
        }
    }

    static class User {
        private String name;

        private int age;

        public User(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }


}
