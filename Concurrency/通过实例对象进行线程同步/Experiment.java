package com.vin.thread;

/**
 * created by Vintage
 * 基于实例对象的同步语句块的测试
 */
public class Experiment {

    // 线程休眠公共方法
    public static void sleepMethod(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void method01(int id) {
        System.out.println("线程："+id+"进入方法1");
        sleepMethod(1000);
        System.out.println("线程："+id+"离开方法1");
        sleepMethod(1000);
    }

    public void method02(int id) {
        System.out.println("线程："+id+"进入方法2");
        sleepMethod(1000);
        System.out.println("线程："+id+"离开方法2");
        sleepMethod(1000);
    }
}
