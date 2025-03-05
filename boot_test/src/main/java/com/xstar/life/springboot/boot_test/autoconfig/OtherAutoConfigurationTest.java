package com.xstar.life.springboot.boot_test.autoconfig;

import com.zaxxer.hikari.HikariDataSource;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.type.AnnotationMetadata;

public class OtherAutoConfigurationTest {

    public static void main(String[] args) {
        GenericApplicationContext context = new GenericApplicationContext();

        StandardEnvironment env = new StandardEnvironment();
//        env.getPropertySources().addLast(new SimpleCommandLinePropertySource("--spring.aop.auto=false"));

        AnnotationConfigUtils.registerAnnotationConfigProcessors(context.getDefaultListableBeanFactory());
        context.registerBean(MyConfiguration.class);
        context.refresh();

        for (String name : context.getBeanDefinitionNames()) {
            System.out.println(name);
        }
    }

    @Configuration
    @Import(MyImportSelector.class)
    static class MyConfiguration {

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

    static class MyImportSelector implements DeferredImportSelector {

        @Override
        public String[] selectImports(AnnotationMetadata importingClassMetadata) {
            return new String[]{AopAutoConfiguration.class.getName(),
                    DataSourceAutoConfiguration.class.getName(),
                    MybatisAutoConfiguration.class.getName(),
                    DataSourceTransactionManagerAutoConfiguration.class.getName(),
                    TransactionAutoConfiguration.class.getName()};
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
