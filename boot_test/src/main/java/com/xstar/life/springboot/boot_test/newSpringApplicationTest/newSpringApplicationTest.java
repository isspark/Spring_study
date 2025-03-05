package com.xstar.life.springboot.boot_test.newSpringApplicationTest;

import com.xstar.life.springboot.boot_test.BootTestApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.GenericApplicationContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

public class newSpringApplicationTest {

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
//        SpringApplication.run(BootTestApplication.class, args);
        System.out.println("1. 演示获取Bean Definition 源");
        SpringApplication spring = new SpringApplication(BootTestApplication.class);
        spring.setSources(Set.of("classpath:b01.xml"));

        System.out.println("2. 演示推断应用类型");
        Method deduceFromClasspath = WebApplicationType.class.getDeclaredMethod("deduceFromClasspath");
        deduceFromClasspath.setAccessible(true);
        System.out.println("推断应用类型：" + deduceFromClasspath.invoke(null));

        System.out.println("3. 演示ApplicationContext 初始化器");
        spring.addInitializers(context -> {
            if(context instanceof GenericApplicationContext){
                ((GenericApplicationContext) context).registerBean("bean3", Bean3.class);
            }
        });

        System.out.println("4. 演示监听器与事件");
        spring.addListeners(event -> System.out.println("监听到事件：" + event.getClass()));

        System.out.println("5. 演示主类推断");
        Method deduceMainApplicationClass = SpringApplication.class.getDeclaredMethod("deduceMainApplicationClass");
        deduceMainApplicationClass.setAccessible(true);
        System.out.println("主类推断：" + deduceMainApplicationClass.invoke(spring));
        //创建ApplicationContext
        //调用初始化器 对ApplicationContext 做扩展
        //刷新ApplicationContext.refresh()
        ConfigurableApplicationContext context = spring.run(args);
        for (String name : context.getBeanDefinitionNames()){
            System.out.println("name:" + name + " 来源：" + context.getBeanFactory().getBeanDefinition(name).getResourceDescription());
        }
        context.close();
    }

    static class Bean2{}

    static class Bean1{}

    static class Bean3{
    }

    @Bean
    public Bean2 bean2(){
        return new Bean2();
    }
}
