package com.study.spring.mvc.converter;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;

import java.util.Date;

public class TestServletRequestDataBinder {

    public static void main(String[] args) {
        Mybean4 mybean4 = new Mybean4();
        ServletRequestDataBinder dataBinder = new ServletRequestDataBinder(mybean4);

        dataBinder.initDirectFieldAccess();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("name", "zhang");
        request.setParameter("age", "18");
        request.setParameter("birth", "2018/08/08");

        dataBinder.bind(new ServletRequestParameterPropertyValues(request));

        System.out.println(mybean4);
    }

    static class Mybean4{
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
