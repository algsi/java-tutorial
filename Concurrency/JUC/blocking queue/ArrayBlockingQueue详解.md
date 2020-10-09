# ArrayBlockingQueue 详解

java.util.concurrent.ArrayBlockingQueue 是 JDK 中提供的一个基于数组实现的有界阻塞队列。

在解析 ArrayBlockingQueue 的源码之前，我们需要熟悉 java.util.concurrent.locks.ReentrantLock 和 java.util.concurrent.locks.Condition 这两个类的使用方法以及内部机制。

## BoundedBuffer 与 two condition instances

BoundedBuffer 是 java.util.concurrent.locks.Condition 类注释中提供的一个关于有界缓存的示例。实际上生产者-消费者（producer-consumer）问题，也叫做有界缓冲区（bounded-buffer）问题。

BoundedBuffer 就是一个简易版本的 ArrayBlockingQueue，我们可以通过 BoundedBuffer 来快速了解 ArrayBlockingQueue 的实现机制。

### BoundedBuffer

BoundedBuffer 代码如下所示：

```java
public class BoundedBuffer {

    final Lock lock = new ReentrantLock();
    final Condition notFull = lock.newCondition();   // 写线程条件
    final Condition notEmpty = lock.newCondition();  // 读线程条件

    final Object[] items = new Object[100];
    int putper, takeptr, count;

    /**
     * Producer
     */
    public void put(Object x) throws InterruptedException {
        lock.lock();
        try {
            while (count == items.length) {
                notFull.wait();
            }
            items[putper] = x;
            if (++putper == items.length) {
                putper = 0;
            }
            ++count;
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Consumer
     */
    public Object take() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0) {
                notEmpty.await();
            }
            Object x = items[takeptr];
            if (++takeptr == items.length) {
                takeptr = 0;
            }
            --count;
            notFull.signal();
            return x;
        } finally {
            lock.unlock();
        }
    }
}
```

BoundedBuffer 提供了 take 和 put 两个核心方法。如果一个线程尝试调用 take 方法从一个空的 buffer 中获取元素，那么这个线程会阻塞直到 buffer 中有可用的元素；如果一个线程尝试调用 put 方法往一个满的 buffer 中添加元素，那么这个线程会阻塞直到 buffer 有可用的空间。

### two condition instances

在 BoundedBuffer 的实现中，使用了两个 Condition 实例。为什么要使用两个 Condition，使用一个 Condition 不可以吗？

从上面的代码中，从一个 ReentrantLock 创建出两个 Condition 对象，这两个 Condition 对象共用一个同步队列（sync queue），但是拥有各自单独的条件等待队列（condition queue）。使用两个 Condition 对象，可以分别将写线程和读线程放入不同的 condition 队列。

多个 condition 的强大之处。假设缓存已满，调用 `notFull.wait()`，阻塞的肯定是写线程，等待 notFull 条件，调用 `notEmpty.signal()` 唤醒的是读线程；相反地，假设缓存已空，调用 `notEmpty.await()` 阻塞的肯定是读线程，等待 notEmpty 条件，调用 `notFull.signal()` 唤醒的肯定是写线程。

但如果我们只有一个 condition 实例呢？假设缓存队列已满，调用 condition 的 signal 方法去唤醒线程，但是这个 Lock 不知道唤醒的是读线程还是写线程，因为读线程和写线程都阻塞在同一个 condition 队列中，如果唤醒的是读线程，那么皆大欢喜，如果唤醒的是写线程，那么线程刚被唤醒又要阻塞了，因为条件不满足（队列是满的），这时又要去重新唤醒其他线程，这样就浪费了很多时间，而即便是调用 signalAll 唤醒所有线程也不一定就是读线程会抢到锁。所以，只使用一个 condition 实例的实现方式，锁的效率是十分低下的。

多个 condition 会有自己单独的等待队列，调用 await 方法，会将线程放到对应的等待队列中。当调用某个 condition 的 signalAll/signal 方法，则只会唤醒对应的等待队列中的线程。唤醒的粒度变小了，且更具针对性，因此效率也就更高。

