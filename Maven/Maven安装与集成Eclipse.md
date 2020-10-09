# Maven安装与集成Eclipse

## 下载安装

1. 前往https://maven.apache.org/download.cgi下载最新版的Maven程序

2. 将文件解压到D:\Program Files\Apache\maven目录下（取决于自己）

3. 新建环境变量MAVEN_HOME，赋值D:\Program Files\Apache\maven

4. 编辑环境变量Path，追加 %MAVEN_HOME%\bin\;

5. 至此，maven已经完成了安装，我们可以通过DOS命令检查一下我们是否安装成功，在DOS命令中输入: mvn -v 即可。

#### 配置Maven本地仓库

1. 在D:\Program Files\Apache\目录下新建maven-repository文件夹，该目录用作maven的本地库。

2. 打开D:\Program Files\Apache\maven\conf\settings.xml文件，查找下面这行代码：

    ```language
    <localRepository>/path/to/local/repo</localRepository>
    ```

localRepository节点默认是被注释掉的，需要把它移到注释之外，然后将localRepository节点的值改为我们在3.1中创建的目录D:\Program Files\Apache\maven-repository。

3. localRepository节点用于配置本地仓库，本地仓库其实起到了一个缓存的作用，它的默认地址是 C:\Users\用户名.m2。

当我们从maven中获取jar包的时候，maven首先会在本地仓库中查找，如果本地仓库有则返回；如果没有则从远程仓库中获取包，并在本地库中保存。

此外，我们在maven项目中运行mvn install，项目将会自动打包并安装到本地仓库中。

## Eclipse集成Maven

Eclipse安装maven插件，在网上有各种各样的方法，但是留下的印象总是时好时不好，同样的方法也不确定那一次能够成功，而且速度极慢。其实失败的大多数原因是因为所安装的maven插件版本与eclipse的版本不一致造成，为了避免这种问题可以采用下面这种方式：

1. 打开eclipse，菜单 Help -> Install New Software

2. 在Work with 地址栏输入：http://download.eclipse.org/releases/<font color="red">juno</font>  （注意：红字部分是eclipse对应的版本名称），稍等片刻。

    ![MavenInstall01.png](https://github.com/GitVinsmoke/Problem-and-Solution/blob/master/images/maven/MavenInstall01.png?raw=true)

3. 在filter框中输入maven便能定位要安装的插件，如图：

	![MavenInstall02.png](https://github.com/GitVinsmoke/Problem-and-Solution/blob/master/images/maven/MavenInstall02.png?raw=true)


4. 选择 Collaboration 下的 m2e - Maven Integration for Eclipse，并点击next按钮进行安装步骤。图：

	![MavenInstall03.png](https://github.com/GitVinsmoke/Problem-and-Solution/blob/master/images/maven/MavenInstall03.png?raw=true)

5. 安装完成重启eclipse，菜单：Window --> preferences ，输入maven进行相关设置。

#### 配置Eclipse的Maven环境

打开Window->Preferences->Maven->Installations，右侧点击Add。

设置maven的安装目录，然后Finish。

选中刚刚添加的maven，并Apply。

打开Window->Preferences->Maven->User Settings，配置如下并Apply：

D:\Program Files\Apache\maven\config\setting.xml

实际上就是Maven安装目录里面的配置文件。

至此，Maven的安装和配置全部结束。