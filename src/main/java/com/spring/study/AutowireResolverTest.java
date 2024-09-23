package com.spring.study;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.InjectionMetadata;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AutowireResolverTest {


    public static void main(String[] args) throws Throwable {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.registerSingleton("bean1",new Bean1());
        beanFactory.registerSingleton("bean2",new Bean2());
        beanFactory.registerSingleton("bean3",new Bean3());

        beanFactory.setAutowireCandidateResolver(new ContextAnnotationAutowireCandidateResolver());//解析@Value

        AutowiredAnnotationBeanPostProcessor processor = new AutowiredAnnotationBeanPostProcessor();
        processor.setBeanFactory(beanFactory);


        Bean1 bean1 = new Bean1();
//        System.out.println(bean1);
//        processor.postProcessProperties(null,bean1,"bean1");
//        System.out.println(bean1);

        Method method = AutowiredAnnotationBeanPostProcessor.class.getDeclaredMethod("findAutowiringMetadata",String.class,Class.class, PropertyValues.class);
        method.setAccessible(true);
        InjectionMetadata metadata = (InjectionMetadata) method.invoke(processor,"bean1",Bean1.class,null);
        System.out.println(metadata);

        metadata.inject(bean1,"bean1",null);
        System.out.println(bean1);

        Field bean3 = Bean1.class.getDeclaredField("bean3");
        DependencyDescriptor ddl = new DependencyDescriptor(bean3,false);
//        beanFactory.resolveDependency(ddl,null);
        Object o = beanFactory.doResolveDependency(ddl,null,null,null);
        System.out.println(o);
    }



    static class Bean1 {
        private static Logger log = LoggerFactory.getLogger(BeanFactory02.Bean1.class);

        @Autowired
        private Bean2 bean2;

        @Autowired
        private Bean3 bean3;

        public Bean2 getBean2() {
            return bean2;
        }

        public Bean1(){
            log.info("create Bean1");
        }

        @Override
        public String toString() {
            return "Bean1{" +
                    "bean2=" + bean2 +
                    ", bean3=" + bean3 +
                    '}';
        }
    }

    static class Bean2 {
        private static Logger log = LoggerFactory.getLogger(BeanFactory02.Bean2.class);

        public Bean2(){
            log.info("create Bean2");
        }
    }

    @Slf4j
    static class Bean3{
//
//        @Autowired
//        private Bean1 bean1;
//
//        @Autowired
//        private Bean2 bean2;

        public Bean3(){
            log.info("create Bean3");
        }
    }
}
