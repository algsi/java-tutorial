# Mac 下 多版本 JDK 安装及管理

在 Java 项目中，经常对 JDK 版本有不同的要求，可是不可能为了某个项目的运行重新下载不同版本JDK进行安装，这样就涉及到对本地环境中多个JDK版本的管理。

macOS 的 JDK 都是安装到一个指定目录的：`/Library/Java/JavaVirtualMachines/`，因此可以在这个目录下查看自己安装的所有 JDK：

```bash
(base) ➜  ~ ll /Library/Java/JavaVirtualMachines/
total 0
drwxr-xr-x  3 root  wheel    96B Jan 16 10:39 jdk-11.0.9.jdk
drwxr-xr-x  3 root  wheel    96B Mar  3  2020 jdk1.8.0_241.jdk
```

也可以使用 `/usr/libexec/java_home -V` 命令查看本级安装的 JDK 版本及其目录信息：

```bash
(base) ➜  ~ /usr/libexec/java_home -V
Matching Java Virtual Machines (3):
    11.0.9 (x86_64) "Oracle Corporation" - "Java SE 11.0.9" /Library/Java/JavaVirtualMachines/jdk-11.0.9.jdk/Contents/Home
    1.8.241.07 (x86_64) "Oracle Corporation" - "Java" /Library/Internet Plug-Ins/JavaAppletPlugin.plugin/Contents/Home
    1.8.0_241 (x86_64) "Oracle Corporation" - "Java SE 8" /Library/Java/JavaVirtualMachines/jdk1.8.0_241.jdk/Contents/Home
/Library/Java/JavaVirtualMachines/jdk1.8.0_241.jdk/Contents/Home
(base) ➜  ~
```

接下来就是配置环境变量并进行JDK版本管理，首先执行命令：`vim ~/.bash_profile` 修改环境变量，MAC建议此处修改环境变量，而不是修改/etc/profile。几种配置文件区别如下：

- `/etc/profile`：应用于所有用户的全局配置脚本
- `~/.bash_profile`：用户私人的启动文件。可以用来扩展或重写配置脚本中的设置

然后进行环境变量配置，输入以下信息并保存：

```bash
# multiple java version
export JAVA_8_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0_241.jdk/Contents/Home
export JAVA_11_HOME=/Library/Java/JavaVirtualMachines/jdk-11.0.9.jdk/Contents/Home
# alias for changing java version
alias java8="export JAVA_HOME=$JAVA_8_HOME"
alias java11="export JAVA_HOME=$JAVA_11_HOME"
# default java8
export JAVA_HOME=$JAVA_8_HOME
```

然后执行命令：`source  ~/.bash_profile` 使配置立即生效，如果未生效，关掉当前Terminal并重新打开。
执行命令：`java -version` 查看当前version号，执行命令 java8、java11 测试JDK版本切换是否正常，如下：

```bash
(base) ➜  ~ java -version
java version "1.8.0_241"
Java(TM) SE Runtime Environment (build 1.8.0_241-b07)
Java HotSpot(TM) 64-Bit Server VM (build 25.241-b07, mixed mode)
(base) ➜  ~ java11
(base) ➜  ~ java -version
java version "11.0.9" 2020-10-20 LTS
Java(TM) SE Runtime Environment 18.9 (build 11.0.9+7-LTS)
Java HotSpot(TM) 64-Bit Server VM 18.9 (build 11.0.9+7-LTS, mixed mode)
(base) ➜  ~ java8
(base) ➜  ~ java -version
java version "1.8.0_241"
Java(TM) SE Runtime Environment (build 1.8.0_241-b07)
Java HotSpot(TM) 64-Bit Server VM (build 25.241-b07, mixed mode)
(base) ➜  ~
```
