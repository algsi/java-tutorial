# AQS and ReentrantLock

## ReentrantLock相关接口

### `boolean tryLock()`

> Acquires the lock only if it is not held by another thread at the time of invocation.

ReentrantLock 中的 `public boolean tryLock()` 方法尝试获取锁，只有在该方法执行的时候锁是空闲的并且被该线程获取到才返回 true。

> Even when this lock has been set to use a fair ordering policy, a call to tryLock() will immediately acquire the lock if it is available, whether or not other threads are currently waiting for the lock.

上面的这段引用来自该方法的文档注视，它告诉我们该方法的执行策略始终是不公平的，即便锁被设置了公平等待策略。下面是该方法的源码：

```java
public boolean tryLock() {
  return sync.nonfairTryAcquire(1);
}
```

可以看到，该方法直接调用了同步器的 nonfairTryAcquire 方法，这就是不公平的获取锁的方式。Sync 内部类提供的 nonfairTryAcquire 方法如下：

```java
/**
 * Performs non-fair tryLock.  tryAcquire is implemented in
 * subclasses, but both need nonfair try for trylock method.
 */
final boolean nonfairTryAcquire(int acquires) {
    final Thread current = Thread.currentThread();
    int c = getState();
    if (c == 0) {
        if (compareAndSetState(0, acquires)) {
            setExclusiveOwnerThread(current);
            return true;
        }
    }
    else if (current == getExclusiveOwnerThread()) {
        int nextc = c + acquires;
        if (nextc < 0) // overflow
            throw new Error("Maximum lock count exceeded");
        setState(nextc);
        return true;
    }
    return false;
}
```

该方法首先获取锁的状态，如果锁的状态为 0，则说明锁是空闲的，那么就直接通过 CAS 方式改变锁的状态从而获取锁，注意这里没有判断在该线程之前是否还有其他线程在队列中等待，这里就体现该方法的不公平性。

### `boolean tryLock(long timeout, TimeUnit unit)`

```java
public boolean tryLock(long timeout, TimeUnit unit)
	  throws InterruptedException {
    return sync.tryAcquireNanos(1, unit.toNanos(timeout));
}
```

