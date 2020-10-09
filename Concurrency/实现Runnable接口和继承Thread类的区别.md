# 实现Runnable接口和继承Thread类的区别

Java中构建线程类的方式主要有两种：

1. 实现Runnable接口，实现其中的 run() 方法，将线程的运行逻辑放在其中，并最终通过该类的实例（实现了Runnable）来实例化Thread类。

2. 继承Thread类，重写Thread的 run() 方法，将线程的运行逻辑放在其中。

因为Thread类也是实现了Runnable类，所以以上两种方法本质上都是构造实现接口 java.lang.Runnable 的类。

## 示例

通过基础java.lang.Thread类

```java
package com.vin.common;

/**
 * created by Vintage
 */

public class CommonThread extends Thread {

    // The thread identifier.
    private int id;

    public CommonThread(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        for (int i = 0; i < 2; i++) {
            System.out.println("运行线程：" + id);
            try {
                // Thread sleeping.
                Thread.sleep((int) (Math.random() * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {

        // Only when you call "start" function, the JVM will startup thread.
        new CommonThread(1).start();
        new CommonThread(2).start();
    }
}

```

通过实现 java.lang.Runnable 接口

```java
package com.vin.common;

/**
 * created by Vintage
 */

public class CommonThread implements Runnable {

    // The thread identifier.
    private int id;

    public CommonThread(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        for (int i = 0; i < 2; i++) {
            System.out.println("运行线程：" + id);
            try {
                // Thread sleeping.
                Thread.sleep((int) (Math.random() * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {

        // Only when you call "start" function, the JVM will startup thread.
        Thread t1 = new Thread(new CommonThread(1));
        t1.start();
        Thread t2 = new Thread(new CommonThread(2));
        t2.start();
    }
}

```

## 区别

从上面两种方式的区别来看：

如果一个类继承Thread，则不适合资源共享，为什么呢？因为每一个实例都有自己的锁，就不方便实现资源控制（除非静态资源）。

但是如果实现了Runable接口的话，则很容易的实现资源共享，因为我们在构造多个线程类（Thread）时，可以传入同一个实例，这个实例实现了 java.lang.Runnable 接口。

用类继承Thread的方式来实现多线程也是可以资源共享的，例如，我们将一个在继承Thread的类中声明一个变量，然后在实例化多个对象之后，为这些对象都设置同一个实例变量，这样就实现了资源共享。

