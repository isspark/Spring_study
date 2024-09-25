package com.study.spring.mvc.converter;

import org.springframework.core.GenericTypeResolver;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class TestGenericType {

    public static void main(String[] args) {
        //获取泛型类型的方式
        //1. JDK api
        Type type = Student.class.getGenericSuperclass();
        System.out.println(type);
        Type type2 = Teacher.class.getGenericSuperclass();
        System.out.println(type2);
        if(type instanceof ParameterizedType parameterizedType){
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            for (Type actualTypeArgument : actualTypeArguments) {
                System.out.println(actualTypeArgument);
            }
        }
        //2. spring api
        Class<?> t = GenericTypeResolver.resolveTypeArgument(Student.class, BaseDto.class);
        System.out.println();
    }
}
