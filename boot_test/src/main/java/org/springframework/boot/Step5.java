package org.springframework.boot;

import org.springframework.boot.context.config.ConfigDataEnvironmentPostProcessor;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.EnvironmentPostProcessorApplicationListener;
import org.springframework.boot.env.RandomValuePropertySourceEnvironmentPostProcessor;
import org.springframework.boot.logging.DeferredLogs;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.io.IOException;
import java.util.List;

public class Step5 {
    public static void main(String[] args) throws IOException {
        SpringApplication app = new SpringApplication();
        ApplicationEnvironment env = new ApplicationEnvironment();//系统环境变量、properties、yaml
        // 系统环境变量
        env.getPropertySources().addLast(new ResourcePropertySource(new ClassPathResource("step4.properties")));
        // 命令行参数
        env.getPropertySources().addFirst(new SimpleCommandLinePropertySource(args));
        app.addListeners(new EnvironmentPostProcessorApplicationListener());

//        List<String> names = SpringFactoriesLoader.loadFactoryNames(EnvironmentPostProcessor.class,Step5.class.getClassLoader());
//        for (String name : names) {
//            System.out.println(name);
//        }



        System.out.println("增强前>>>>>");
        for (PropertySource<?> ps : env.getPropertySources()) {
            System.out.println(ps);
        }
        //增强功能
        ConfigDataEnvironmentPostProcessor postProcessor = new ConfigDataEnvironmentPostProcessor(new DeferredLogs(),new DefaultBootstrapContext());
        postProcessor.postProcessEnvironment(env, app);

        System.out.println("增强后>>>>>");
        for (PropertySource<?> ps : env.getPropertySources()) {
            System.out.println(ps);
        }

        RandomValuePropertySourceEnvironmentPostProcessor postProcessor1 = new RandomValuePropertySourceEnvironmentPostProcessor(new DeferredLogs());
        postProcessor1.postProcessEnvironment(env, app);
        System.out.println("增强后>>>>>");
        for (PropertySource<?> ps : env.getPropertySources()) {
            System.out.println(ps);
        }

        System.out.println(env.getProperty("random.int"));

        //增强功能，能够统一格式解析
        ConfigurationPropertySources.attach(env);
        for (PropertySource<?> ps : env.getPropertySources()) {
            System.out.println(ps);
        }
        System.out.println(env.getProperty("user.first-name"));
        System.out.println(env.getProperty("user.middle-name"));
        System.out.println(env.getProperty("user.last-name"));
        //6 数据绑定，讲配置和对象属性绑定
        User user = Binder.get(env).bind("user", User.class).get();
        System.out.println(user);
        //7 banner
        SpringApplicationBannerPrinter printer = new SpringApplicationBannerPrinter(new DefaultResourceLoader(),new SpringBootBanner());
        printer.print(env,Step5.class,System.out);
        System.out.println(SpringBootVersion.getVersion());
    }

    static class User {
        private String lastName;


        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        @Override
        public String toString() {
            return "User{" +
                    "firstName='" + lastName + '\'' +
                    '}';
        }
    }
}
