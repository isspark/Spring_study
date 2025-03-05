package com.xstar.life.springboot.boot_test.springApplicationRunTest;

import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.DefaultBootstrapContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.context.config.ConfigDataEnvironmentPostProcessor;
import org.springframework.boot.logging.DeferredLogs;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.reactive.context.AnnotationConfigReactiveWebServerApplicationContext;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.ClassPathResource;

import java.lang.reflect.InvocationTargetException;

public class SpringApplicationRunTest {

    public static void main(String[] args) throws Exception {
        //添加app监听器
        SpringApplication app = new SpringApplication();
        app.addListeners(e -> System.out.println("app监听器" + e.getClass()));
        app.addInitializers(c -> System.out.println("初始化器" + c.getClass()));

        System.out.println("2.封装启动 args");
        ApplicationArguments arguments =  new DefaultApplicationArguments(args);

        System.out.println("3. 系统环境变量：yaml、properties");
//        ApplicationEnvironment
        ConfigDataEnvironmentPostProcessor postProcessor = new ConfigDataEnvironmentPostProcessor(new DeferredLogs(),new DefaultBootstrapContext());
        postProcessor.postProcessEnvironment(new StandardEnvironment(),app);

        System.out.println("8.创建容器");
        GenericApplicationContext context = createContext(WebApplicationType.SERVLET);
        System.out.println("9.准备容器");
        for (ApplicationContextInitializer initializer : app.getInitializers()) {
            initializer.initialize(context);
        }
        System.out.println("10. 加载Bean定义");
        AnnotatedBeanDefinitionReader reader = new AnnotatedBeanDefinitionReader(context.getDefaultListableBeanFactory());
        reader.register(Config1.class);
        XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(context.getDefaultListableBeanFactory());
        xmlReader.loadBeanDefinitions(new ClassPathResource("b03.xml"));
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(context.getDefaultListableBeanFactory());
        scanner.scan("com.xstar.life.springboot.boot_test.springApplicationRunTest.test");
        System.out.println("11. refresh容器");
        context.refresh();
        for (String name : context.getBeanDefinitionNames()) {
            System.out.println("name: " + name + " 来源：" + context.getBeanFactory().getBeanDefinition(name).getResourceDescription());
        }
        System.out.println("12. 执行runner:可以通过这种方式预加载一些数据");
        for (CommandLineRunner runner : context.getBeansOfType(CommandLineRunner.class).values()){
            runner.run(args);
        }

        for (ApplicationRunner value : context.getBeansOfType(ApplicationRunner.class).values()) {
            value.run(arguments);
        }
//        //获取事件发送器实现类名
//        List<String> names = SpringFactoriesLoader.loadFactoryNames(SpringApplicationRunListener.class, SpringApplicationRunTest.class.getClassLoader());
//        for (String name : names) {
//            System.out.println(name);
//            Class<?> clazz = Class.forName(name);
//            Constructor<?> constructor = clazz.getDeclaredConstructor(SpringApplication.class, String[].class);
//            constructor.setAccessible(true);
//            SpringApplicationRunListener publisher = (SpringApplicationRunListener) constructor.newInstance(app,args);
//
//            //发布事件
//            DefaultBootstrapContext bootstrapContext = new DefaultBootstrapContext();
//            publisher.starting(bootstrapContext);//spring boot 开始启动
//            publisher.environmentPrepared(bootstrapContext,new StandardEnvironment()); //环境信息配置完毕
//            GenericApplicationContext context = new GenericApplicationContext();
//            publisher.contextPrepared(context); //上下文配置完毕，并调用初始化器之后
//            publisher.contextLoaded(context);//bean definition 加载完毕
//            context.refresh();
//            publisher.started(context,null); //spring boot 容器初始化完成（refresh 方法执行完毕）
//            publisher.ready(context,null); //spring boot 容器准备就绪
//            publisher.failed(context,new Exception("error")); //spring boot 启动失败
//        }
    }

    static GenericApplicationContext createContext(WebApplicationType type){
        GenericApplicationContext context = null;
        switch (type){
            case SERVLET -> context = new AnnotationConfigServletWebServerApplicationContext();
            case REACTIVE -> context = new AnnotationConfigReactiveWebServerApplicationContext();
            case NONE -> context= new AnnotationConfigApplicationContext();
        }
        return context;
    }

    static class Bean1 {
    }

    static class Bean2 {
    }

    static class Bean3 {

    }

    @Configuration
    static class Config1{

        @Bean
        public Bean1 bean1(){
            return new Bean1();
        }

        @Bean
        public Bean2 bean2(){
            return new Bean2();
        }


        @Bean
        public ServletWebServerFactory servletWebServerFactory(){
            return new TomcatServletWebServerFactory();
        }

        @Bean
        public CommandLineRunner commandLineRunner(){
            return args -> System.out.println("commandLineRunner:" + args);
        }

        @Bean
        public ApplicationRunner applicationRunner(){
            return args -> System.out.println("applicationRunner:" + args);
        }
    }

}
