package com.spring.study;

public class JavaToolsDemo {

    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            while (true){}
        },"thread1");

        Thread thread2 = new Thread(() -> {
            while (true){}
        },"thread2");

        Thread thread3 = new Thread(() -> {
            while (true){}
        },"thread3");

        thread1.start();
        thread2.start();
        thread3.start();
    }
}
