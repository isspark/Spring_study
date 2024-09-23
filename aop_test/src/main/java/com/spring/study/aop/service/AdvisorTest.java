package com.spring.study.aop.service;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;

public class AdvisorTest {

    public static void main(String[] args) throws NoSuchMethodException {

        //1. 备好切点
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* foo())");
//        pointcut.matches(MyServiceInterface.class.getMethod("foo"), com.spring.study.aop.service.MyService.class);
        //2. 备好通知
        MethodInterceptor methodInterceptor = new MethodInterceptor() {
            @Override
            public Object invoke(MethodInvocation invocation) throws Throwable {
                System.out.println("before...");
                Object obj = invocation.proceed();
                System.out.println("after...");
                return obj;
            }
        };
        //3. 备好切面
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut,methodInterceptor);
        //4. 创建代理
        /**
         *  a. proxyTargetClass = false,
         *      如果目标实现了接口，使用JDK实现
         *      如果目标没有实现接口，使用CGLIB实现
         *  b. proxyTargetClass = true,强制使用CGLIB实现
         */
        MyServiceInterface target = new MyService();
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setTarget(target);
        proxyFactory.addAdvisor(advisor);
        proxyFactory.setInterfaces(MyServiceInterface.class);

        MyServiceInterface proxy = (MyServiceInterface) proxyFactory.getProxy();
        //class com.spring.study.aop.service.AdvisorTest$MyService$$SpringCGLIB$$0
        //class com.spring.study.aop.service.$Proxy0
        System.out.println(proxy.getClass());
        proxy.foo();
        proxy.bar();
    }


    static class MyService implements MyServiceInterface{

        public void foo(){
            System.out.println("foo");
        }

        public void bar(){
            System.out.println("bar");
        }
    }

    static class MyService2 implements MyServiceInterface{

        public void foo(){
            System.out.println("foo2");
        }

        public void bar(){
            System.out.println("bar2");
        }
    }

    interface MyServiceInterface{
        void foo();
        void bar();
    }
}
