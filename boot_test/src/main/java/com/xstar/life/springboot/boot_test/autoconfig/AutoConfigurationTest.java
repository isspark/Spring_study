package com.xstar.life.springboot.boot_test.autoconfig;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.core.type.AnnotationMetadata;

import java.util.List;

public class AutoConfigurationTest {

    public static void main(String[] args) {
        AnnotationConfigServletWebServerApplicationContext context = new AnnotationConfigServletWebServerApplicationContext();
        //设置如果重复定义bean，抛出异常
//        context.getDefaultListableBeanFactory().setAllowBeanDefinitionOverriding(false);
        context.registerBean("config", MyConfiguration.class);
//        context.registerBean(ConfigurationClassPostProcessor.class);
        context.refresh();

        for (String name : context.getBeanDefinitionNames()) {
            System.out.println(name);
        }
    }


    //1. 直接通过Import导入
//    @Import({AutoConfigration1.class,AutoConfigration2.class})
    //2. 通过ImportSelector导入
//    @Import(MyImportSelector.class)
    @EnableAutoConfiguration
    @Configuration
    static class MyConfiguration {
//        @Bean
//        @ConditionalOnMissingBean
//        public Bean01 bean01() {
//            return new Bean01("本地项目");
//        }

        @Bean
        public HikariDataSource dataSource() {
            HikariDataSource dataSource = new HikariDataSource();
            dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/your_database");
            dataSource.setUsername("your_username");
            dataSource.setPassword("your_password");
            dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
            return dataSource;
        }
    }

    static class MyImportSelector implements ImportSelector {

        @Override
        public String[] selectImports(AnnotationMetadata importingClassMetadata) {
            //写死导入
//            return new String[]{AutoConfigration1.class.getName(),AutoConfigration2.class.getName()};
            //通过配置
//           List<MyImportSelector> selectors = SpringFactoriesLoader.loadFactories(MyImportSelector.class,null);
//            List<String> names = selectors.stream().map(s -> s.getClass().getName()).collect(Collectors.toList());
            List<String> names = SpringFactoriesLoader.loadFactoryNames(MyImportSelector.class,null);
//            List<String> names = SpringFactoriesLoader.loadFactoryNames(AutoConfigurationImportSelector.class,null);
//            for (String name : names) {
//                System.out.println(">>>>>>>>>>");
//                System.out.println(name);
//                System.out.println(">>>>>>>>>>");
//            }
            return names.toArray(new String[0]);
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
