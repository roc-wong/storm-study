package org.roc.storm.jdbc.bolt.mapper;

public class Parent {
    public Parent() {
        System.out.println("parent constructor method");
        staticHH();
    }

    static {
        System.out.println("parent static code");
    }

    //非静态代码块
    {
        System.out.println("parent nonStatic code");
    }

    public static void staticHH() {
        System.out.println("parent static hh");
    }

    public void hh() {
        System.out.println("parent hh");
    }


}