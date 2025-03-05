package com.xstar.life.springboot.boot_test.tomcat;

import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.coyote.http11.Http11Nio2Protocol;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Set;

public class TomcatTest {

    public static void main(String[] args) throws IOException, LifecycleException {
        //1. 创建 Tomcat 对象
        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir("tomcat");

        //2. 创建项目文件夹，即docBase文件夹
        File docBase = Files.createTempDirectory("boot.").toFile();
        docBase.deleteOnExit();

        //3. 创建 Tomcat 项目，在 Tomcat 中成为 Context
        Context context = tomcat.addContext("", docBase.getAbsolutePath());

        //4. 编程添加 Servlet
        //整合Spring
        WebApplicationContext springContext = getWebApplicationContext();
        context.addServletContainerInitializer(new ServletContainerInitializer() {
            @Override
            public void onStartup(Set<Class<?>> set, ServletContext servletContext) throws ServletException {
                HelloServlet helloServlet = new HelloServlet();
                servletContext.addServlet("aaa",helloServlet).addMapping("/hello");
                //整合Spring DispatcherServlet
//                DispatcherServlet dispatcherServlet = springContext.getBean(DispatcherServlet.class);
//                servletContext.addServlet("dispatcherServlet", dispatcherServlet).addMapping("/");
                for (ServletRegistrationBean value : springContext.getBeansOfType(ServletRegistrationBean.class).values()) {
                    value.onStartup(servletContext);
                }
            }
        }, Collections.emptySet());

        //5. 启动Tomcat
        tomcat.start();
        //6. 创建连接器，设置监听端口
        Connector connector = new Connector(new Http11Nio2Protocol());
        connector.setPort(8080);
        tomcat.setConnector(connector);
    }

    public static WebApplicationContext getWebApplicationContext(){

//        AnnotationConfigServletWebServerApplicationContext
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(WebConfig.class);
        context.refresh();
        return context;
    }
}
