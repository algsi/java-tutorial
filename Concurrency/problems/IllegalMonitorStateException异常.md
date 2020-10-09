# IllegalMonitorStateException异常

Java中 `wait()`，`notify()` 和 `notifyAll()` 这三个方法需要在同步方法或者同步语句块中调用，如果在非同步方法或者非同步语句块中调用了这些方法，程序可以通过编译，但在运行的时候将会得到 `IllegalMonitorStateException` 异常，并伴随着一些含糊的消息，比如"当前线程不是拥有者"。异常消息的意思是，调用 `wait()`，`notify()` 和 `notifyAll()` 这些方法的线程在调用这些方法前必须拥有对象的锁。

