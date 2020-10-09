# Lock接口详解

java.util.concurrent.locks.Lock 接口提供了与 synchronized 关键字类似的同步功能，但需要在使用时手动获取锁和释放锁，并且 Lock 接口还扩展了可中断获取以及支持等待时长等的功能。下面一一讲解这些内容。

## 1. Lock 接口的使用

Lock 接口的一般使用方式如下，我们需要显式地获取锁，然后在 try-finally 代码块中释放锁。

```java
Lock l = ...;
l.lock();
try {
  // access the resource protected by this lock
} finally {
  l.unlock();
}
```

## 2. 接口方法详解

### 2.1. void lock()

该接口用来获取锁，如果此时锁已经被占用，则当前线程阻塞知道获取到锁为止。该方法不会响应中断。

### 2.2. void lockInterruptibly() throws InterruptedException

在线程不中断的情况下获取锁。

### 2.3. boolean tryLock()

只有在该方法执行的时候锁是空闲的才获取锁。如果锁是空闲的，则立马返回 true，否则，立马返回 false。

A typical usage idiom for this method would like:

```java
Lock lock = ...;
if (lock.tryLock()) {
  try {
    // manipulate protected state
  } finally {
    lock.unlock();
  }
} else {
  // perform alternative actions
}
```

该方法确保在获取到锁到时候锁是空闲的，并且不会在锁未获取到到情况下去释放锁。

### 2.4. boolean tryLock(long time, TimeUnit unit) throws InterruptedException

当锁在线程等待期间空闲并且线程非中断的情况下获取锁。

### 2.5 void unlock()

释放锁。通常来说，只有持有锁的线程才能释放锁，如果线程违反这个限制去释放锁，则抛出非受检异常（unchecked exceptino）。

### 2.6. Condition newCondition()

返回一个与当前 Lock 实例绑定的 Condition 实例。

一个线程在等待一个 condition 前一定是持有该锁的。线程调用 Condition 的 await 方法等待前会释放锁，并且只有当重新获取到了锁才从等待中返回。
