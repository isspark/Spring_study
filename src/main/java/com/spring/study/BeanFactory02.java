package com.spring.study;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class BeanFactory02 {

    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        //Bean的定义：class、scope、初始化、销毁
        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(Config.class).setScope("singleton").getBeanDefinition();

        beanFactory.registerBeanDefinition("config", beanDefinition);

        //给BeanFactory添加常用的后置处理器
        AnnotationConfigUtils.registerAnnotationConfigProcessors(beanFactory);

        //执行后置处理器
        for (BeanFactoryPostProcessor beanFactoryPostProcessor : beanFactory.getBeansOfType(BeanFactoryPostProcessor.class).values()) {
            beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);
        }

        for (String beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
            System.out.println(beanDefinitionName);
        }

//        System.out.println(beanFactory.getBean(Bean1.class).getBean2());

        //Bean后置处理器，针对Bean生命周期各个阶段进行扩展，如果@Autowired @Resource
        for (BeanPostProcessor beanPostProcessor : beanFactory.getBeansOfType(BeanPostProcessor.class).values()) {
            beanFactory.addBeanPostProcessor(beanPostProcessor);
        }
        System.out.println("---------------");
        System.out.println(beanFactory.getBean(Bean1.class).getBean2());

    }

    @Configuration
    static class Config{

        @Bean
        public Bean1 bean1(){
            return new Bean1();
        }

        @Bean
        public Bean2 bean2(){
            return new Bean2();
        }
    }

    static class Bean1 {
        private static Logger log = LoggerFactory.getLogger(Bean1.class);

        @Autowired
        private Bean2 bean2;

        public Bean2 getBean2() {
            return bean2;
        }

        public Bean1(){
            log.info("create Bean1");
        }

    }

    static class Bean2 {
        private static Logger log = LoggerFactory.getLogger(Bean2.class);

        public Bean2(){
            log.info("create Bean2");
        }
    }
}
