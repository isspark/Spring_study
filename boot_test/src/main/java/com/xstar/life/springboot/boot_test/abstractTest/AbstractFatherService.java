package com.xstar.life.springboot.boot_test.abstractTest;

public abstract class AbstractFatherService {

    protected String getName(){
        return  "father";
    }

    public void refresh(){
        String name = getName();
        System.out.println(name);
    }
}
