package com.study.spring.mvc;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.cbor.MappingJackson2CborHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockPart;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.support.DefaultDataBinderFactory;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExpressionValueMethodArgumentResolver;
import org.springframework.web.method.annotation.InitBinderDataBinderFactory;
import org.springframework.web.method.annotation.ModelAttributeMethodProcessor;
import org.springframework.web.method.annotation.RequestHeaderMethodArgumentResolver;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolverComposite;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.PathVariableMapMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.PathVariableMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestPartMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;
import org.springframework.web.servlet.mvc.method.annotation.ServletCookieValueMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;
import org.springframework.web.servlet.mvc.method.annotation.ServletRequestDataBinderFactory;
import org.springframework.web.servlet.mvc.method.annotation.ServletRequestMethodArgumentResolver;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//@SpringBootApplication
public class ParamResolverTestApplication {

    public static void main(String[] args) throws Exception {
//        SpringApplication.run(SpringMvcTestApplication.class, args);
        AnnotationConfigServletWebServerApplicationContext context = new AnnotationConfigServletWebServerApplicationContext(EmptyWebConfig.class);

        HttpServletRequest request = mockRequest();

        //1. 控制器方法被封装为HandlerMethod对象
        HandlerMethod handlerMethod = new HandlerMethod(new Controller(), Controller.class.getDeclaredMethod("test", String.class, String.class, int.class, String.class, MultipartFile.class, Integer.class, String.class, String.class, String.class, HttpServletRequest.class, User.class, User.class, User.class));
        //2. 准备对象绑定和类型转化
//        DefaultDataBinderFactory dataBinderFactory = new DefaultDataBinderFactory(null);
        ServletRequestDataBinderFactory dataBinderFactory = new ServletRequestDataBinderFactory(null, null);
        //3. 准备 ModelAndViewContainer对象,用来存储中间Model结果
        ModelAndViewContainer container = new ModelAndViewContainer();
        //4. 解析每个参数
        for (MethodParameter methodParameter : handlerMethod.getMethodParameters()) {
            methodParameter.initParameterNameDiscovery(new DefaultParameterNameDiscoverer());
            List<String> annotationNames = Arrays.stream(methodParameter.getParameterAnnotations()).map(annotation -> annotation.annotationType().getSimpleName()).collect(Collectors.toList());

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
            if (composite.supportsParameter(methodParameter)) {
                Object value = composite.resolveArgument(methodParameter, container, new ServletWebRequest(request), dataBinderFactory);
                System.out.println("[" + methodParameter.getParameterIndex() + "] :" + methodParameter.getParameterType()
                        + "\t name: " + methodParameter.getParameterName()
                        + "\t annotationName: " + annotationNames
                        + "\t value: " + value);
            } else {
                System.out.println("[" + methodParameter.getParameterIndex() + "] :" + methodParameter.getParameterType()
                        + "\t name: " + methodParameter.getParameterName()
                        + "\t annotationName: " + annotationNames);
            }
        }
//        context.close();
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
        public void test(
                @RequestParam("name1") String name1,                                      // name1=张三
                String name2,                                                             // name2=李四
                @RequestParam("age") int age,                                             // age=18
                @RequestParam(name = "home", defaultValue = "${JAVA_HOME}") String home1,    // spring 获取数据
                @RequestParam("file") MultipartFile file,                                  //上传文件
                @PathVariable("id") Integer id,                                                // /test/124 /test/{id}
                @RequestHeader("Content-Type") String header,
                @CookieValue("token") String token,
                @Value("${JAVA_HOME}") String home2,                                        //spring 获牧数据
                HttpServletRequest reguest,                                                //request,response,session
                @ModelAttribute User user1,                                                //name=zhang&age=18
                User user2,                                                                //name=zhang&age=18
                @RequestBody User user3) {
        }
    }

    static class User {
        private String name;

        private int age;

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
