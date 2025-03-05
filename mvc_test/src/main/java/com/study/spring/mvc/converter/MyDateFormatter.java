package com.study.spring.mvc.converter;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyDateFormatter implements Formatter<Date> {

    private String desc;

    public MyDateFormatter(String desc) {
        this.desc = desc;
    }

    @Override
    public Date parse(String text, Locale locale) throws ParseException {
        System.out.println(desc);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy|MM|dd");
        return simpleDateFormat.parse(text);
    }

    @Override
    public String print(Date object, Locale locale) {
        System.out.println(desc);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy|MM|dd");
        return simpleDateFormat.format(object);
    }
}
