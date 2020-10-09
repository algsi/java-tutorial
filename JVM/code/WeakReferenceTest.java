package com.test;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

/**
 * @author Vintage
 * 2018/11/2
 */

public class WeakReferenceTest {

    /** 引用队列 */
    private static ReferenceQueue<MyObject> weakQueue = new ReferenceQueue<MyObject>();

    public static class MyObject {

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

        /** 引用 */
        Reference<MyObject> obj = null;

        @Override
        public void run() {
            try {
                // 删除队列中的下一个引用，该方法会一直阻塞直到有一个引用可用
                obj = (Reference<MyObject>) weakQueue.remove();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (obj != null) {
                System.out.println("删除的弱引用为："+obj+"，获取弱引用的对象obj.get() = "+obj.get());
            }
        }
    }

    public static void main(String[] args) {
        MyObject object = new MyObject();
        WeakReference<MyObject> weakReference = new WeakReference<MyObject>(object, weakQueue);
        System.out.println("创建的弱引用为：" + weakReference);

        // 启动线程
        new Thread(new CheckRefQueue()).start();

        // 删除强引用，object对象就只有弱引用了
        object = null;
        System.out.println("Before GC: Weak Get= "+weakReference.get());
        System.gc();
        System.out.println("After GC: Weak Get= "+weakReference.get());
    }

}
