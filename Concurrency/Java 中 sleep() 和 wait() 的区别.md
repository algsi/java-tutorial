# Java 中 sleep() 和 wait() 的区别

## 开门见山

sleep() 和 wait() 的区别就是 调用sleep方法的线程不会释放对象锁，而调用wait() 方法会释放对象锁