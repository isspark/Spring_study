package com.xstar.life.springboot.boot_test.autoconfig;

import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.type.AnnotationMetadata;

public class AopAutoConfigurationTest {

    public static void main(String[] args) {
        GenericApplicationContext context = new AnnotationConfigApplicationContext();

        StandardEnvironment env = new StandardEnvironment();
//        env.getPropertySources().addLast(new SimpleCommandLinePropertySource("--spring.aop.auto=false"));

        AnnotationConfigUtils.registerAnnotationConfigProcessors(context.getDefaultListableBeanFactory());
        context.registerBean(MyConfiguration.class);
        context.refresh();

        for (String name : context.getBeanDefinitionNames()) {
            System.out.println(name);
        }

        AnnotationAwareAspectJAutoProxyCreator creator = context.getBean("org.springframework.aop.config.internalAutoProxyCreator", AnnotationAwareAspectJAutoProxyCreator.class);
        System.out.println(creator.isProxyTargetClass());
    }

    @Configuration
    //1. 直接通过Import导入
//    @Import({AutoConfigration1.class,AutoConfigration2.class})
    //2. 通过ImportSelector导入
    @Import(MyImportSelector.class)
    static class MyConfiguration {
    }

    static class MyImportSelector implements ImportSelector {

        @Override
        public String[] selectImports(AnnotationMetadata importingClassMetadata) {
            return new String[]{AopAutoConfiguration.class.getName()};
        }
    }

    @Configuration
    static class AutoConfigration1 {

        @Bean
        public Bean01 bean01() {
            return new Bean01("第三方");
        }
    }

    @Configuration
    static class AutoConfigration2 {

        @Bean
        public Bean02 bean02() {
            return new Bean02();
        }
    }
}
