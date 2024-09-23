package com.spring.study.aop;

import java.io.IOException;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JdkProxyTest  {

    interface Foo{
        void foo();
        void foo2();
    }

    static class FooImpl implements Foo{
        @Override
        public void foo() {
            System.out.println("foo");
        }

        @Override
        public void foo2() {
            System.out.println("foo2");
        }
    }

    public static void main(String[] args) throws IOException {
        ClassLoader loader = JdkProxyTest.class.getClassLoader();
        FooImpl target = new FooImpl();
        Foo proxy = (Foo) Proxy.newProxyInstance(loader, new Class[]{Foo.class}, (p, method, args1) -> {
            System.out.println("before invoke foo ");
            //目标.方法
            //方法.invoke(目标,参数)
            Object result = method.invoke(target,args);
            System.out.println("after invoke foo ");
            return result;
        });
        proxy.foo();
        proxy.foo2();

        System.out.println(proxy.getClass());
        System.in.read();
    }
}
