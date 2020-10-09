# 可重入读写锁 ReentrantReadWriteLock

`ReentrantReadWriteLock` 也是基于 `AbstractQueuedSynchronizer` 实现的，它实现了 `ReadWriteLock` 接口，并且具有 `ReentrantLock` 语义，有下面这些属性（来自Java doc文档）：

- 获取顺序：此类不会将读取优先或写入优先强加给锁访问的排序。
  - 非公平模式（默认）：连续竞争的非公平锁可能无限期地推迟一个或多个reader或writer线程，但吞吐量通常要高于公平锁。
  - 公平模式：线程利用一个近似到达顺序的策略来争夺进入。当释放当前保持的锁时，可以为等待时间最长的单个writer线程分配写入锁，如果有一组等待时间大于所有正在等待的writer线程的reader，将为该组分配读者锁。
  - 试图获得公平写入锁的非重入的线程将会阻塞，除非读取锁和写入锁都自由（这意味着没有等待线程）。
- 重入：此锁允许reader和writer按照 `ReentrantLock` 的样式重新获取读取锁或写入锁。在写入线程保持的所有写入锁都已经释放后，才允许重入reader使用读取锁。writer可以获取读取锁，但reader不能获取写入锁。
- 锁降级：重入还允许从写入锁降级为读取锁，实现方式是：先获取写入锁，然后获取读取锁，最后释放写入锁。但是，**从读取锁升级到写入锁是不可能的。**
- 锁获取的中断：读取锁和写入锁都支持锁获取期间响应中断。
- Condition 支持：写入锁提供了一个 `Condition` 实现，对于写入锁来说，该实现的行为与 `ReentrantLock.newCondition()` 提供的 `Condition` 实现对 `ReentrantLock` 所做的行为相同。当然，此 `Condition` 只能用于写入锁。
  读取锁不支持 `Condition`，`readLock().newCondition()` 会抛出 `UnsupportedOperationException`。
- 监测：此类支持一些确定是读取锁还是写入锁的方法。这些方法设计用于监视系统状态，而不是同步控制。

## 1. AQS回顾

在之前的文章已经提到，AQS以单个 `int` 类型的原子变量来表示其状态，定义了4个抽象方法（ `tryAcquire(int)、tryRelease(int)、tryAcquireShared(int)、tryReleaseShared(int)`，前两个方法用于独占/排他模式，后两个用于共享模式 ）留给子类实现，用于自定义同步器的行为以实现特定的功能。（当然还有另外两个方法：`tryAcquireNanos(int, long)、tryAcquireSharedNanos(int, long)`）。

对于 `ReentrantLock`，它是可重入的独占锁，内部的 `Sync` 类实现了 `tryAcquire(int)、tryRelease(int)` 方法，并用状态的值来表示重入次数，加锁或重入锁时状态加 1，释放锁时状态减 1，状态值等于 0 表示锁空闲。

对于 `CountDownLatch`，它是一个关卡，在条件满足前阻塞所有等待线程，条件满足后允许所有线程通过。内部类 `Sync` 把状态初始化为大于 0 的某个值，当状态大于 0 时所有wait线程阻塞，每调用一次 countDown 方法就把状态值减 1，减为 0 时允许所有线程通过。利用了AQS的共享模式。

现在，要用AQS来实现 `ReentrantReadWriteLock`。

### 1.1. 一点思考问题

- AQS 只有一个状态，那么如何表示 **多个读锁** 和 **单个写锁** 呢？
- `ReentrantLock` 里，状态值表示重入计数，现在如何在 AQS 里表示每个读锁、写锁的重入次数呢？
- 如何实现读锁、写锁的公平性呢？

### 1.2. 一点提示

- 一个状态是没法既表示读锁，又表示写锁的，不够用啊，那就辦成两份用了，客家话说一个饭粒咬成两半吃，状态的高位部分表示读锁，低位表示写锁，由于写锁只有一个，所以写锁的重入计数也解决了，这也会导致写锁可重入的次数减小。
- 由于读锁可以同时有多个，肯定不能再用辦成两份用的方法来处理了，但我们有 ThreadLocal，可以把线程重入读锁的次数作为值存在 ThreadLocal 里。
- 对于公平性的实现，可以通过AQS的等待队列和它的抽象方法来控制，在状态值的另一半里存储当前持有读锁的线程数。如果读线程申请读锁，当前写锁重入次数不为 0 时，则等待，否则可以马上分配；如果是写线程申请写锁，当前状态为 0 则可以马上分配，否则等待。

## 2. 内部结构

## 3. 源码分析