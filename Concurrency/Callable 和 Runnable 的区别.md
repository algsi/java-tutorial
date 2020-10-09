# Callable 和 Runnable 的区别

Callable 接口：

```java
package java.util.concurrent;

@FunctionalInterface
public interface Callable<V> {
    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    V call() throws Exception;
}
```

Runnable 接口：

```java
@FunctionalInterface
public interface Runnable {
    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see     java.lang.Thread#run()
     */
    public abstract void run();
}

```

下面是关于Callable源码中的注释部分

> A task that returns a result and may throw an exception.
> The interface {@code Callable} interface is similar to {@link java.lang.Runnable}, in that both are designed for classes whose instances are potentially by another thread. A {@Runnable}, however, does not return a result and can not throw a check exception.

注释中就直接说明了 Runnable 和 Callable 接口设计的意愿是让类的实例被其他的线程执行，而不同的是实现Callable接口的任务线程能返回执行结果，而实现Runnable接口的任务线程不能返回结果；还有Callable接口的call()方法允许抛出异常；而Runnable接口的run()方法的异常只能在内部处理，不能继续上抛。