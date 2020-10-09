package com.vin.thread;

/**
 * created by Vintage
 * 多线程编程实例：实验数据的分析与更新
 * 实验程序：可以进行数据更新以及数据的分析
 */
public class Experiment {

    // 温度与气压
    private int temperature, pressure;

    // 为true则表示数据已经更新完毕并已经准备好被分析，为false则表示原来的数据已经被分析完毕，可以更新了
    private boolean isReady = false;

    // 实验数据的更新
    public synchronized void update(int t, int p) {
        System.out.println("进入更新方法内部");

        // 如果是true，则说明数据更新
        if (isReady) {
            System.out.println("    等待数据分析完毕。。。");
            try {
                wait();    // 将线程置为等待态，等待数据分析完毕
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 等待态结束后执行的第一条语句
            System.out.println("    继续更新数据。。。");
        }
        temperature = t;
        pressure = p;
        System.out.println("数据更新完成：温度值为 " + temperature + ", 气压值为 " + pressure);
        isReady = true;
        // 激活正在等待数据更新完成的线程
        notify();
    }

    // 数据分析
    public synchronized void analyze() {
        System.out.println("进入数据分析方法内部");

        // 数据是否可以被分析，如果还不能，则等待
        if (!isReady) {
            System.out.println("    等待数据更新完成。。。");
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("    继续分析数据");
        }
        int t = temperature;
        int p = pressure;
        System.out.println("分析完成: 温度值为 " + t + ", 气压值为 " + p);
        isReady = false;
        // 激活正在等待数据分析完成的线程
        notify();
    }
}
