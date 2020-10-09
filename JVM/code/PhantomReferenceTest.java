package com.test;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 虚引用的缓存区
 *
 * @author Vintage
 * 2018/11/2
 */

public class PhantomReferenceTest {

    /** 引用队列 */
    private static ReferenceQueue<MyObject> phantomQueue = new ReferenceQueue<MyObject>();
    private static Map<Reference<MyObject>, String> map = new HashMap<Reference<MyObject>, String>();

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
                obj = (Reference<MyObject>) phantomQueue.remove();
                Object value = map.get(obj);
                System.out.println("clean resource: "+value);
                map.remove(obj);
                System.out.println("删除的虚引用为："+obj+"，获取虚引用的对象obj.get() = "+obj.get());
                System.exit(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        MyObject object = new MyObject();
        Reference<MyObject> phanRef = new PhantomReference<>(object, phantomQueue);
        System.out.println("创建的虚引用为："+phanRef);

        map.put(phanRef, "Some Resources");

        // 启动线程
        new Thread(new CheckRefQueue()).start();

        // 删除强引用，只留下虚引用
        object = null;

        // 休眠，等待GC将虚引用回收
        TimeUnit.SECONDS.sleep(1);

        int i =1;
        while(true) {
            System.out.println("第"+ (i++) +"次gc");
            System.gc();
            TimeUnit.SECONDS.sleep(1);
        }
    }

}
