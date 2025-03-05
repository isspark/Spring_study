package com.study.spring.mvc.converter;

import org.springframework.beans.BeanWrapperImpl;

import java.util.Date;

public class TestBeanWrapperImpl {

    public static void main(String[] args) {

        Mybean target = new Mybean();
        BeanWrapperImpl beanWrapper = new BeanWrapperImpl(target);
        beanWrapper.setPropertyValue("name", "zhangsan");
        beanWrapper.setPropertyValue("age", 18);
        beanWrapper.setPropertyValue("birth", "2018/08/08");
        System.out.println(target);
    }

    static class Mybean{
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public Date getBirth() {
            return birth;
        }

        public void setBirth(Date birth) {
            this.birth = birth;
        }
    }
}
