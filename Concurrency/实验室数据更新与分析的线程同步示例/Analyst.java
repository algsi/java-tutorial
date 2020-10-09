package com.vin.thread;

/**
 * created by Vintage
 * 实验室分析人员：分析实验数据
 */

public class Analyst extends Thread {

    Experiment data;

    public Analyst(Experiment data) {
        this.data = data;
    }

    // 分析实验数据的方法，三次分析实验数据
    public void run() {
        System.out.println("分析员线程开始工作");
        for (int k = 0; k < 3; k++)
            data.analyze();
        System.out.println("分析员线程结束工作");
    }
}
