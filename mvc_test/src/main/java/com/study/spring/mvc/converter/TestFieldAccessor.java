package com.study.spring.mvc.converter;

import org.springframework.beans.DirectFieldAccessor;

import java.util.Date;

public class TestFieldAccessor {

    public static void main(String[] args) {
        Mybean2 mybean2 = new Mybean2();
        DirectFieldAccessor directFieldAccessor = new DirectFieldAccessor(mybean2);
        directFieldAccessor.setPropertyValue("name", "张三");
        directFieldAccessor.setPropertyValue("age", 18);
        directFieldAccessor.setPropertyValue("birth", "2018/08/08");
        System.out.println(mybean2);
    }

    static class Mybean2{
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
