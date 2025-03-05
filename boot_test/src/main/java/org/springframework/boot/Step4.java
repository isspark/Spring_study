package org.springframework.boot;

import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.ResourcePropertySource;

import java.io.IOException;

public class Step4 {
    public static void main(String[] args) throws IOException {
        ApplicationEnvironment env = new ApplicationEnvironment();//系统环境变量、properties、yaml
        // 系统环境变量
        env.getPropertySources().addLast(new ResourcePropertySource(new ClassPathResource("step4.properties")));
        // 命令行参数
        env.getPropertySources().addFirst(new SimpleCommandLinePropertySource(args));
        //增强功能，能够统一格式解析
        ConfigurationPropertySources.attach(env);
        for (PropertySource<?> ps : env.getPropertySources()) {
            System.out.println(ps);
        }

        System.out.println(env.getProperty("user.first-name"));
        System.out.println(env.getProperty("user.middle-name"));
        System.out.println(env.getProperty("user.last-name"));
    }
}
