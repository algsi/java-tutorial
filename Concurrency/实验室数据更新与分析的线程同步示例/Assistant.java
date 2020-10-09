package com.vin.thread;

/**
 * created by Vintage
 * 实验助理：负责更新数据
 */

public class Assistant extends Thread {

    private Experiment data;

    public Assistant(Experiment data) {
        this.data = data;
    }

    // 更新数据的方法，三次更新实验数据
    @Override
    public void run() {
        System.out.println("助理线程开始工作");
        int i, j, k;
        for (k = 0; k < 3; k++) {
            i = (int)(Math.random()*1000);
            j = (int)(Math.random()*1000);
            data.update(i, j);
        }
        System.out.println("助理线程结束工作");
    }
}
