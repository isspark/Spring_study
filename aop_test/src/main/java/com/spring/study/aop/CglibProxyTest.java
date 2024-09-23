package com.spring.study.aop;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

public class CglibProxyTest {

    static class Target {
        public void foo() {
            System.out.println("foo");
        }

        public final void foo2() {
            System.out.println("foo2");
        }
    }

    public static void main(String[] args) {
        Target target = new Target();
        Target proxy = (Target) Enhancer.create(Target.class, (MethodInterceptor) (obj, method, args1, methodProxy) -> {
            System.out.println("before");
            //方法反射调用
            Object result = method.invoke(target, args1);
            //methodProxy.invoke可以避免反射进行调用.内部不是用的反射，需要目标类
            //Object result = methodProxy.invoke(target,args1);
            //methodProxy.invokeSuper可以避免反射进行调用.内部不是用的反射，需要代理类自己
            //Object result = methodProxy.invokeSuper(obj,args1);
            System.out.println("after");
            return result;
        });
        proxy.foo();
        proxy.foo2();
    }
}
