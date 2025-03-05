package com.study.spring.mvc.converter;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.InvocableHandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.ServletRequestDataBinderFactory;

import java.util.Date;
import java.util.List;

public class TestServletReqestDataBinderFactory {
    public static void main(String[] args) throws Exception {
       User user = new User();


        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("name", "zhang");
        request.setParameter("age", "18");
        request.setParameter("birth", "2018|08|08");
        request.setParameter("address.name", "测试");
        //1. 用工厂，无转换功能
//        ServletRequestDataBinderFactory factory = new ServletRequestDataBinderFactory(null,null);
//        WebDataBinder dataBinder =  factory.createBinder(new ServletWebRequest(request),user,"user");

        //2. 用@InitBinder转换         用propertyEditorRegistry 的 PropertyEditor
//        InvocableHandlerMethod initBinderMethod = new InvocableHandlerMethod(new MyController(),MyController.class.getDeclaredMethod("test",WebDataBinder.class));
//        ServletRequestDataBinderFactory factory = new ServletRequestDataBinderFactory(List.of(initBinderMethod),null);
//        WebDataBinder dataBinder =  factory.createBinder(new ServletWebRequest(request),user,"user");

        //3. 用ConversionService转换   ConversionService的Formatter转换器
//        FormattingConversionService conversionService = new FormattingConversionService();
//        conversionService.addFormatter(new MyDateFormatter("user conversion service"));
//        ConfigurableWebBindingInitializer initializer = new ConfigurableWebBindingInitializer();
//        initializer.setConversionService(conversionService);
//        ServletRequestDataBinderFactory factory = new ServletRequestDataBinderFactory(null,initializer);
//        WebDataBinder dataBinder =  factory.createBinder(new ServletWebRequest(request),user,"user");

        //4. 同时用@initbinder和ConversionService转换
//        FormattingConversionService conversionService = new FormattingConversionService();
//        conversionService.addFormatter(new MyDateFormatter("user conversion service"));
//        ConfigurableWebBindingInitializer initializer = new ConfigurableWebBindingInitializer();
//        initializer.setConversionService(conversionService);
//        InvocableHandlerMethod initBinderMethod = new InvocableHandlerMethod(new MyController(),MyController.class.getDeclaredMethod("test",WebDataBinder.class));
//        ServletRequestDataBinderFactory factory = new ServletRequestDataBinderFactory(List.of(initBinderMethod),initializer);
//        WebDataBinder dataBinder =  factory.createBinder(new ServletWebRequest(request),user,"user");

        //5. 使用默认ConversionService转换
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
        ConfigurableWebBindingInitializer initializer = new ConfigurableWebBindingInitializer();
        initializer.setConversionService(conversionService);
        ServletRequestDataBinderFactory factory = new ServletRequestDataBinderFactory(null,initializer);
        WebDataBinder dataBinder =  factory.createBinder(new ServletWebRequest(request),user,"user");

        dataBinder.bind(new ServletRequestParameterPropertyValues(request));

        System.out.println(user);
    }

    static class MyController{

        @InitBinder
        public void test(WebDataBinder dataBinder){
            //扩展DataBinder的转化器
            dataBinder.addCustomFormatter(new MyDateFormatter("use init binder impl"));
        }
    }

    static class User{
        private String name;
        private int age;

        @DateTimeFormat(pattern = "yyyy|MM|dd")
        private Date birth;

        private Address address;

        @Override
        public String toString() {
            return "Mybean{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    ", birth=" + birth +
                    '}';
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

        public Date getBirth() {
            return birth;
        }

        public void setBirth(Date birth) {
            this.birth = birth;
        }

        public Address getAddress() {
            return address;
        }

        public void setAddress(Address address) {
            this.address = address;
        }
    }

    static class Address{
        private String name;

        @Override
        public String toString() {
            return "Address{" +
                    "name='" + name + '\'' +
                    '}';
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
