package com.study.spring.mvc.converter;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.validation.DataBinder;

import java.util.Date;

public class TestDataBinder {

    public static void main(String[] args) {
        Mybean3 mybean3 = new Mybean3();
        DataBinder dataBinder = new DataBinder(mybean3);

        dataBinder.initDirectFieldAccess();
        MutablePropertyValues mutablePropertyValues = new MutablePropertyValues();
        mutablePropertyValues.add("name", "张三");
        mutablePropertyValues.add("age", "18");
        mutablePropertyValues.add("birth", "2018/08/08");
        dataBinder.bind(mutablePropertyValues);

        System.out.println(mybean3);
    }


    static class Mybean3{
        private String name;
        private int age;
        private Date birth;


        @Override
        public String toString() {
            return "Mybean{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    ", birth=" + birth +
                    '}';
        }
    }
}
