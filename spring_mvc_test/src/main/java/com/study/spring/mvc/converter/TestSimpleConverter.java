package com.study.spring.mvc.converter;

import org.springframework.beans.SimpleTypeConverter;

import java.util.Date;

public class TestSimpleConverter {

    public static void main(String[] args) {
        SimpleTypeConverter simpleTypeConverter = new SimpleTypeConverter();
        Integer number = simpleTypeConverter.convertIfNecessary("123", Integer.class);
        System.out.println(number);

        Date date = simpleTypeConverter.convertIfNecessary("2018/08/08", Date.class);
        System.out.println(date);
    }
}
