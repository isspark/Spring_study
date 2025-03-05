package com.xstar.life.springboot.boot_test.abstractTest;

public class SonServiceImpl extends AbstractFatherService{

    public void refresh(){
        super.refresh();
    }

    protected String getName(){
        return "son";
    }

    public static void main(String[] args) {
        AbstractFatherService fatherService = new SonServiceImpl();
        fatherService.refresh();
    }
}
