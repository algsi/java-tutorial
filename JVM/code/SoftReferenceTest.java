package com.test;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;

/**
 * @author Vintage
 * 2018/11/2
 */
public class SoftReferenceTest {

    private static ReferenceQueue<MyObject> softQueue = new ReferenceQueue<MyObject>();

    /**
     * Class definition
     */
    public static class MyObject{

        @Override
        protected void finalize() throws Throwable {
            super.finalize();
            System.out.println("MyObject' s finalize() called");
        }

        @Override
        public String toString() {
            return "This is MyObject";
        }
    }

    public static class CheckRefQueue implements Runnable {

        // 引用类对象
        Reference<MyObject> obj = null;

        @Override
        public void run() {
            try {
                System.out.println("调用前");
                // 删除队列中的下一个引用，该方法会一直阻塞直到有一个引用可用
                obj = (Reference<MyObject>)softQueue.remove();
                System.out.println("调用后");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            /*
            当软引用被回收了，softQueue.remove()中就加入了一个引用而脱离阻塞状态，obj此时不会为null。
             */
            if(obj != null) {
                System.out.println("Object for SoftReference is "+obj.get());
            }
        }
    }

    public static void main(String[] args) {
        MyObject object = new MyObject();

        // 使用软引用队列实现软引用
        SoftReference<MyObject> softRef = new SoftReference<>(object, softQueue);

        // 线程开启
        new Thread(new CheckRefQueue()).start();

        // 删除强引用
        object = null;
        // 显示调用垃圾回收器：Runs the garbage collector.
        System.gc();

        // 调用软引用的get()方法，取得MyObject对象的引用
        System.out.println("After GC: Soft Get = "+softRef.get());

        System.out.println("此时，请求大块的内存，依据实际的程序调整：");
        byte[] b = new byte[5*1024*692];

        // 再调用软引用的get()方法，取得MyObject对象的引用
        System.out.println("After new byte[]:Soft Get = "+softRef.get());
        System.gc();
    }

}
