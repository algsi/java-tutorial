# Arthas

## 执行结果存日志

默认情况下，该功能是关闭的，如果需要开启，请执行以下命令：

```bash
$ options save-result true
```

结果会异步保存在：{user.home}/logs/arthas-cache/result.log，请定期进行清理，以免占据磁盘空间

使用新版本Arthas的异步后台任务将结果存日志文件

trace Test t >>  &

此时命令会在后台异步执行，并将结果异步保存在文件（~/logs/arthas-cache/${PID}/${JobId}）中；

此时任务的执行不受session断开的影响；任务默认超时时间是1天，可以通过全局 options 命令修改默认超时时间；

此命令的结果将异步输出到文件中；此时不管 save-result 是否为true，都不会再往~/logs/arthas-cache/result.log 中异步写结果

## 排查函数调用异常案例

http://localhost/user/0

查看UserController的 参数/异常

watch com.example.demo.arthas.user.UserController * '{params, throwExp}'

第一个参数是类名，支持通配
第二个参数是函数名，支持通配

访问 `curl http://localhost/user/0` ,watch命令会打印调用的参数和异常

```language
$ watch com.example.demo.arthas.user.UserController * '{params, throwExp}'
Press Q or Ctrl+C to abort.
Affect(class-cnt:1 , method-cnt:2) cost in 53 ms.
ts=2019-02-15 01:35:25; [cost=0.996655ms] result=@ArrayList[
    @Object[][isEmpty=false;size=1],
    @IllegalArgumentException[java.lang.IllegalArgumentException: id < 1],
]
```

可以看到实际抛出的异常是 IllegalArgumentException。

可以输入 Q 或者 Ctrl+C 退出watch命令。如果想把获取到的结果展开，可以用 -x 参数：

```langauge
[arthas@32936]$ watch com.example.demo.arthas.user.UserController * '{params, throwExp}' -x 2
Press Q or Ctrl+C to abort.
Affect(class count: 1 , method count: 2) cost in 29 ms, listenerId: 2
method=com.example.demo.arthas.user.UserController.findUserById location=AtExceptionExit
ts=2021-02-16 21:48:18; [cost=0.517551ms] result=@ArrayList[
    @Object[][
        @Integer[0],
    ],
    java.lang.IllegalArgumentException: id < 1
	at com.example.demo.arthas.user.UserController.findUserById(UserController.java:19)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)

    ......
]
```

### 返回值表达式

在上面的例子里，第三个参数是返回值表达式，它实际上是一个ognl表达式，它支持一些内置对象：

loader
clazz
method
target
params
returnObj
throwExp
isBefore
isThrow
isReturn

你可以利用这些内置对象来组成不同的表达式。比如返回一个数组：

```language
watch com.example.demo.arthas.user.UserController * '{params[0], target, returnObj}'
```

更多参考： <https://arthas.aliyun.com/doc/advice-class.html>

### 条件表达式

watch命令支持在第4个参数里写条件表达式，比如：

watch com.example.demo.arthas.user.UserController * returnObj 'params[0] > 100'

当访问 `http:localhost/user/1` 时，watch命令没有输出

当访问 `http:localhost/user/101` 时，watch会打印出结果。

### 当异常时捕获

`watch` 命令支持 `-e` 选项，表示只捕获抛出异常时的请求：

```bash
watch com.example.demo.arthas.user.UserController * "{params[0],throwExp}" -e
```

### 按照耗时进行过滤

watch 命令支持按请求耗时进行过滤，比如：

```langauge
watch com.example.demo.arthas.user.UserController * '{params, returnObj}' '#cost>200'
```

## 查找Top N线程

### 查看所有线程信息

```bash
thread
```

### 查看具体线程的栈

查看线程ID 1 的栈：

```bash
$ thread 1
"main" Id=1 TIMED_WAITING
    at java.lang.Thread.sleep(Native Method)
    at java.lang.Thread.sleep(Thread.java:340)
    at java.util.concurrent.TimeUnit.sleep(TimeUnit.java:386)
    at demo.MathGame.main(MathGame.java:17)
```

### 查看CPU使用率top n线程的栈

参数n用来指定最忙的前N个线程并打印堆栈

thread -n 3

### 查找线程是否有阻塞

参数b用来指定找出当前阻塞其他线程的线程

thread -b
