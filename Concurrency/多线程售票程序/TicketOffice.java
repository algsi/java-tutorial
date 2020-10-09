package com.vin.ticket;

/**
 * created by Vintage
 * 售票处：
 */

public class TicketOffice implements Runnable {

    private int ticketCount = 10;         // 票的总数，这是共享资源，多个线程会访问
    private Object mutex = new Object();  // 自定义的锁，或者干脆使用实例的锁

    public void sellTicket() {
//        synchronized (mutex) {
            if (ticketCount > 0) {
                System.out.println(Thread.currentThread().getName() + "正在卖票，售出的票号是："+ticketCount+"，还剩" + --ticketCount + "张票");
            } else {
                System.out.println("票已经卖完了！");
                return;
            }
//        }
    }

    @Override
    public void run() {
        // 利用循环不停地售票
        while (ticketCount > 0){
            sellTicket();
            /**
             * 在同步代码块里面sleep,和不睡眠效果是一样 的,作用只是自已不执行,也不让线程执行。sleep不释放锁，而是抱着sleep。其他线程拿不到锁，也不能执行同步代码。wait()可以释放锁
             * 所以把sleep放到同步代码块的外面,这样卖完一张票就睡一会,让其他线程再卖,这样所有的线程都可以卖票
             */
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
