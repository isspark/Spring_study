package com.study.spring.mvc.controllerTest;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.annotation.ExpressionValueMethodArgumentResolver;
import org.springframework.web.method.annotation.RequestHeaderMethodArgumentResolver;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolverComposite;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.*;

import java.util.List;

public class ControllerTest {

    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(WebConfig.class);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("name","zhangsan");

        ServletInvocableHandlerMethod handlerMethod = new ServletInvocableHandlerMethod(new WebConfig.Controller1(),
                WebConfig.Controller1.class.getMethod("foo", WebConfig.User.class));

        ServletRequestDataBinderFactory factory = new ServletRequestDataBinderFactory(null,null);

        handlerMethod.setDataBinderFactory(factory);
        handlerMethod.setParameterNameDiscoverer(new DefaultParameterNameDiscoverer());
        handlerMethod.setHandlerMethodArgumentResolvers(getArgumentResolverComposite(context));

        ModelAndViewContainer container = new ModelAndViewContainer();
        handlerMethod.invokeAndHandle(new ServletWebRequest(request),container);

        System.out.println(container.getModel());
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
}
