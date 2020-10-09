# CountDownLatch详解

CountDownLatch 是 JUC 包下提供的一个线程并发控制工具，也叫做闭锁。

## 1. 使用示例

CountDownLatch 的使用示例非常简单，它可以用来让一批线程都完成各自的任务之后才开始做某件事，否则等待。

一个常见的场景是 Driver-Worker，Driver 负责分发和收集结果，Worker 负责执行任务，Driver 需要等待多个 Worker 都执行完成各自的任务之后才看展一些其他的事情，这个时候就可以用到 CountDownLatch。

CountDownLatch 的类注释里面给出了两个例子，它们都是经典的应用场景：

示例一：

```java
class Driver {
    void main(String[] args) throws InterruptedException {
        int n = 10;
        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(n);

        for (int i = 0; i < n; ++i) {
            // create and start threads
            new Thread(new Worker(startSignal, doneSignal)).start();
        }

        // doSomethingElse();            // don't let run yet
        startSignal.countDown();         // let all threads proceed
        // doSomethingElse();
        doneSignal.await();              // wait for all to finish
    }
}

class Worker implements Runnable {

    private final CountDownLatch startSignal;
    private final CountDownLatch doneSignal;

    Worker(CountDownLatch startSignal, CountDownLatch doneSignal) {
        this.startSignal = startSignal;
        this.doneSignal = doneSignal;
    }

    @Override
    public void run() {
        try {
            // 等待开始的信号
            startSignal.await();
            doWork();
        } catch (InterruptedException ex) {

        } finally {
            // 当前 worker 已经完成任务，无论是否抛出异常，需要在 finally 语句块中 count down
            // 否则发生死锁
            doneSignal.countDown();
        }
    }

    void doWork() {
        System.out.println("doing work...");
    }
}
```

示例二：

```java
class Driver {
    void main() throws InterruptedException {
        int n = 10;
        CountDownLatch doneSignal = new CountDownLatch(n);
        ExecutorService e = Executors.newSingleThreadExecutor();
        for (int i = 0; i < n; ++i) {
            // create and start threads
            e.submit(new Worker(doneSignal, i));
        }

        doneSignal.await();  // wait for all to finish
        // all finished, shutdown ExecutorService
        e.shutdown();

        System.out.println("the workers are all finished");
    }
}

class Worker implements Runnable {

    private final CountDownLatch doneSignal;
    private final int i;

    public Worker(CountDownLatch doneSignal, int i) {
        this.doneSignal = doneSignal;
        this.i = i;
    }

    @Override
    public void run() {
        try {
            doWork(i);
        } catch (Exception ex) {

        } finally {
            // the work has done
            doneSignal.countDown();
        }
    }

    void doWork(int i) {
        System.out.println("worker" + i + " doing work...");
    }
}
```

## 2. CountDownLatch 源码解析

CountDownLatch 的实现依赖了 AbstractQueuedSynchronizer，和其他工具使用 AQS 的方法一样，CountDownLatch 内部定义了一个内部类 Sync 继承了 AQS 并覆盖了相关方法。

```java
private static final class Sync extends AbstractQueuedSynchronizer {
    private static final long serialVersionUID = 4982264981922014374L;

    Sync(int count) {
        setState(count);
    }

    int getCount() {
        return getState();
    }

    protected int tryAcquireShared(int acquires) {
        return (getState() == 0) ? 1 : -1;
    }

    protected boolean tryReleaseShared(int releases) {
        // Decrement count; signal when transition to zero
        for (;;) {
            int c = getState();
            if (c == 0)
                return false;
            int nextc = c-1;
            if (compareAndSetState(c, nextc))
                return nextc == 0;
        }
    }
}

private final Sync sync;
```

CountDownLatch的构造方法需要一个 count 参数，它等于 countDown 的次数。count 参数实际传递给了 Sync，并直接设置为 AQS 的 state，在这里暂且先记住 AQS 的 state 等于 count。

### 2.1. void await()

```java
public void await() throws InterruptedException {
    sync.acquireSharedInterruptibly(1);
}
```

CountDownLatch 的该方法的原理就是获取 AQS 的读锁，而获取读锁一定会调用 tryAcquireShared 方法，Sync 内部类重写了该方法（如上），如果 AQS 的 state 为 0，则获取读锁成功，否则调用 await 的方法阻塞，等待被唤醒。

### 2.2. boolean await(long timeout, TimeUnit unit)

```java
public boolean await(long timeout, TimeUnit unit)
    throws InterruptedException {
    return sync.tryAcquireSharedNanos(1, unit.toNanos(timeout));
}
```

该重载的方法在 await() 的基础上增加了等待时间，原理类似，也需要调用 tryAcquireShared 方法。

### 2.3. void countDown()

```java
public void countDown() {
    sync.releaseShared(1);
}
```

countDown 方法直接调用了 AQS 的 releaseShared 方法，我们先来看 AQS 的 releaseShared 方法：

```java
public final boolean releaseShared(int arg) {
    if (tryReleaseShared(arg)) {
        doReleaseShared();
        return true;
    }
    return false;
}
```

在该方法中首先调用了 tryReleaseShared 方法，Sync 类重写了 tryReleaseShared 方法：

```java
protected boolean tryReleaseShared(int releases) {
    // Decrement count; signal when transition to zero
    for (;;) {
        int c = getState();
        if (c == 0)
            return false;
        int nextc = c-1;
        if (compareAndSetState(c, nextc))
            return nextc == 0;
    }
}
```

在重写的方法里面，每一次调用 tryReleaseShared 时都会用 CAS + 自旋的方式将 AQS 的 state 减一。

结合这几处代码，我们总结一下 countDown 方法的执行逻辑。当 countDown 执行后 state 大于 0 时，则调用 tryReleaseShared 后 nextc 大于 0，那么会返回 false，也就是 releaseShared 方法不会执行 doReleaseShared 而直接返回；当 countDown 执行后 state 等于 0 时，则 nextc 等于 0，那么 tryReleaseShared 会返回 true，进而 releaseShared 方法将调用 doReleasedShared 方法，在该方法中会调用 unparkSuccessor 方法将后继节点的线程唤醒去获取锁，这时调用了 await 方法的线程就能获取到锁并往后继续执行了。

由此可见，为什么要让 AQS 的 state 的值等于 count 参数，这样时为了调用了 count 次数的 countDown 方法后去唤醒调用了 await 方法的线程。
