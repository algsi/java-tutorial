# AQS相关流程

void acquire(int arg)
void acquireInterruptibly(int arg)
boolean acquireQueued(final Node node, int arg)
void acquireShared(int arg)
void acquireSharedInterruptibly(int arg)

private void cancelAcquire(Node node)
final boolean acquireQueued(final Node node, int arg)
private void doAcquireInterruptibly(int arg) throws InterruptedException
private boolean doAcquireNanos(int arg, long nanosTimeout) throws InterruptedException
private void doAcquireShared(int arg)
private void doAcquireSharedInterruptibly(int arg) throws InterruptedException
private boolean doAcquireSharedNanos(int arg, long nanosTimeout) throws InterruptedException

private void doReleaseShared()
final int fullyRelease(Node node)

boolean release(int arg)
boolean releaseShared(int arg)

boolean tryAcquire(int arg)
boolean tryAcquireNanos(int arg, long nanosTimeout)
int tryAcquireShared(int arg)
boolean tryAcquireSharedNanos(int arg, long nanosTimeout)

boolean tryRelease(int arg)
boolean tryReleaseShared(int arg)

## ReentrantLock

## ReentrantReadWriteLock

### Sync

Sync 是 ReentrantReadWriteLock 中定义的一个抽象的静态内部类，它继承了 AbstractQueuedSynchronizer，是 ReentrantReadWriteLock 中的抽象同步器，子类有 FairSync 和 NonfairLock，分别实现公平和非公平策略。

#### readerShouldBlock & writerShouldBlock

readerShouldBlock 和 writerShouldBlock 是 Sync 类里面的抽象方法。从这两个方法的命名来看，它们返回 boolean 值来决定读线程或是写线程是应该被阻塞等待还是应该被允许获取锁。

readerShouldBlock:

> Returns true if current thread, when trying to acquire the read lock, and otherwise eligible to do so, should block because of policy for overtaking other waiting threads.

writerShouldBlock:

> Returns true if current thread, when trying to acquire the write lock, and otherwise eligible to do so, should block because of policy for overtaking other waiting threads.

在 FairSync 的实现：

```java
final boolean writerShouldBlock() {
    return hasQueuedPredecessors();
}
final boolean readerShouldBlock() {
    return hasQueuedPredecessors();
}
```

可以看到，在 FairSync 中，无论是读线程还是写线程，都调用了 hasQueuedPredecessors 方法，即判断是否有其他线程在当前线程之前等待，如果有，那么当前线程则应该阻塞，也就是实现了公平的策略。

在 NonFariSync 的实现：

```java
final boolean writerShouldBlock() {
    return false; // writers can always barge
}

final boolean readerShouldBlock() {
    return apparentlyFirstQueuedIsExclusive();
}
```

在不公平的同步器中，明确了写线程具有优先权，写线程始终可以尝试抢先读线程之前获取锁，在 readerShouldBlock 方法调用了 apparentlyFirstQueuedIsExclusive 方法，该方法的代码如下：

```java
/**
 * Returns {@code true} if the apparent first queued thread, if one exists, is waiting in exclusive mode.
 */
final boolean apparentlyFirstQueuedIsExclusive() {
    Node h, s;
    return (h = head) != null &&
        (s = h.next)  != null &&
        !s.isShared()         &&
        s.thread != null;
}

apparentlyFirstQueuedIsExclusive 方法判断队列头节点的后继节点的等待模式是否排他模式。因此在 NonFairSync 中，如果在线程获取读锁的时候，如果队列中等待最久的一个线程在等待写锁，那么当前要获取读锁的线程就必须要等待了。

### ReadLock相关流程

#### lock()

`public void lock()` 方法获取锁，如果获取到锁则立马返回，否则线程等待，该方法不会响应中断。

1. 调用 `sync.acquireShared(1)`，acquireShared 是 AQS 中的一个方法。

    ```java
    /**
    * 以共享模式获取锁，忽略中断
    */
    public final void acquireShared(int arg) {
        if (tryAcquireShared(arg) < 0)
            doAcquireShared(arg);
    }
    ```

    tryAcquireShared 方法在 AQS 中是一个抽象的方法，它返回一个 int 类型值，当返回负数时表示获取锁失败，该方法被 ReentrantReadWriteLock 的静态内部类 Sync 实现了。

    tryAcquireShared 利用了 CAS 操作，如果 CAS 失败线程不会阻塞，而是直接返回。

2. 如果 tryAcquireShared 方法获取锁失败，即返回值为负数，那么会调用 doAcquireShared 方法，`private void doAcquireShared(int arg)` 方法是 AQS 中的一个私有方法，该方法的核心是自旋加 CAS。

### WriteLock相关流程

等待

## 相关场景

场景一：写锁已经被写线程持有，此时有多个线程在等待获取读锁。

多个线程在等待着读锁的时候，等待队列中除了最后一个读节点，其他读节点的 waitStatus 都是 -1，也就是说每个节点获取锁之后都需要唤醒后继节点。当写线程释放写锁后，并唤醒下一个节点后，读线程从结束阻塞并返回到doAcquireShared方法中执行 tryAcquireShared 方法，此时如果获取锁成功，便调用 setHeadAndPropagate 方法将头节点设置为自己，然后再调用 doReleaseShared 去唤醒下一个 shared 模式到节点，然后下一个读线程结束阻塞并返回到doAcquireShared方法中，以此往复，就这样一个节点唤醒下一个节点，实现了所有读线程都可以被依次唤醒获取读锁。
