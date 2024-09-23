package com.spring.study;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Map;

@SpringBootApplication
public class BeanFactoryTest {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        ConfigurableApplicationContext context = SpringApplication.run(BeanFactoryTest.class, args);

        //获取容器中的所有单例
        Field singletonObjects = DefaultSingletonBeanRegistry.class.getDeclaredField("singletonObjects");
        singletonObjects.setAccessible(true);

        ConfigurableListableBeanFactory factory = context.getBeanFactory();
        Map<String, Object> map = (Map<String, Object>) singletonObjects.get(factory);

        map.forEach((k, v) -> System.out.println(k + ":" + v));

        //国际化
        System.out.println(context.getMessage("hi", null, Locale.CHINA));
        System.out.println(context.getMessage("hi", null, Locale.JAPAN));
        System.out.println(context.getMessage("hi", null, Locale.ENGLISH));



    }
}
