package com.xstar.life.springboot.boot_test.autoconfig;

public class Bean01 {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bean01() {
    }

    public Bean01(String name) {
        System.out.println(name);
        this.name = name;
    }
}
