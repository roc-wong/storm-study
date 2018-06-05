package org.roc.storm.jdbc.bolt.mapper;

public class Children extends Parent {

    public Children() {
        System.out.println("children constructor code");
        staticHH();
    }

    static {
        System.out.println("children static code");
    }

    //非静态代码块
    {
        System.out.println("children nonStatic code");
    }

    public static void staticHH() {
        System.out.println("children static hh");
    }

    @Override
    public void hh() {
        super.hh();
        System.out.println("children hh");
    }

}