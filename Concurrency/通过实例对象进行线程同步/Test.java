package com.vin.thread;


/**
 * created by Vintage
 */
public class Test extends Thread {

    private int thread_id;
    private Experiment data;

    public Test(int thread_id) {
        this.thread_id = thread_id;
    }

    @Override
    public void run() {
        System.out.println("运行线程：" + thread_id);

        // 同步语句块与成员变量data所指向的实例对象相关联。查看data所对应的实例对象锁
        synchronized (data) {
            System.out.println("进入同步语句块的线程是：" + thread_id);
            data.method01(thread_id);
            data.method02(thread_id);
            System.out.println("离开同步语句块的线程是：" + thread_id);
        }
        System.out.println("结束线程" + thread_id);
    }

    public static void main(String[] args) {
        int n = 2;
        Test[] test = new Test[n];
        Experiment exp = new Experiment();
        for (int i = 0; i < n; i++) {
            test[i] = new Test(i);
            test[i].data = exp;
            test[i].start();
        }
    }
}
