# <font color=red>Tomcat服务器</font>

### <font color=red>Web容器简介</font>

- 要想运行一个Java Web程序，则必须要有相应的Web容器支持，所有的程序代码要在容器中执行，并将最后结果交付给用户使用

- Web容器是一种服务程序，就是为应用服务器组（如JSP，Servlet等）提供一个运行环境，使JSP、servlet直接跟容器中的环境变量接口交互，不必关注其他系统问题，主要有web服务器来实现。

- 常见Web容器
	- 大型（付费）
		- weblogic
		- websphere

	- 中小型
		- Jboss
		- Tomcat

### <font color=red>Tomcat简介</font>

Tomcat是Apache软件基金会（Apache Software Foundation 可以直接登录www.apache.ort 进行访问）的Jakarta项目中的一个核心项目，由Apache、SUN和其他的一些公司及个人共同开发而成。

由于有了SUN的支持，最新的Servlet和JSP规范总能在Tomcat中得到体现

Tomcat技术先进，性能稳定，而且免费，深受Java爱好者的喜爱，并且得到部分软件开发商的认可，现在已经成为目前比较流行的web应用服务器

Tomcat安装后，默认端口号为8080，如果想修改端口号，可以打开Tomcat目录下的con/server.xml文件找下面内容修改端口号即可（port）

![Tomcat端口修改.PNG](http://img.blog.csdn.net/20180310093102678)

此文件的末尾配置的相关工程的工程名以及工程的加载路径（path）。



### <font color=red>安装配置Tomcat</font>

1. 进入apache官网下载tomcat 8.在左手边的菜单区，选择download下的tomcat8 版本。根据你操作系统选择不同的下载文件，建议下载.zip格式的软件包，这样免于安装直接用。如下图：
![这里写图片描述](http://img.blog.csdn.net/20180205231904177)

2. 将安装文件下载到本地硬盘

3. 下载完成后进入到文件目录，解压缩刚刚下载的软件包

4. 打开软件，可以看到软件包的目录，bin  -- 文件夹主要是存放Tomcat服务启动相关的执行档及相关设定。 conf -- 文件夹主要是存放Tomcat服务器相关的设定。 webapps -- 文件夹主要是存放Tomcat管理网站及日后开发好的JSP网站布署的地方。 logs -- 文件夹主要是存放Tomcat日志文件存放的地方
![这里写图片描述](http://img.blog.csdn.net/20180205231918880)

5. 当然这样下载之后是不能直接运行的，还需要下载jdk(可以在sun官网下载).还需要配置环境变量。进入“控制面板”，鼠标单击单击”系统与安全“。进入高级系统设置配置环境变量
![这里写图片描述](http://img.blog.csdn.net/20180205231930182)

6. 选择“新增”，针对某个用户就选择上面的新增。针对全部用户就选择系统下的新增。建议选择系统下面的新增。变量值为Tomcat目录下bin的文件路径，如下图：
![这里写图片描述](http://img.blog.csdn.net/20180205231937655)

7. 新增或者编辑“变量名:CLASSPATH”，变量值就是%TOMCAT_HOME%\BIN，实际上就是指定tomcat的路径。
!![这里写图片描述](http://img.blog.csdn.net/20180205231946566)

8. tomcat环境变量配置完成。下面可以启动tomcat了。运行tomcat目录/bin/startup
![这里写图片描述](http://img.blog.csdn.net/20180205231953174)

9. tomcat 开始运行，弹出运行对话框。如下图：
![这里写图片描述](http://img.blog.csdn.net/20180205232008827)

10. 此时打开浏览器输入http://localhost:8080将会出现如下图
![这里写图片描述](http://img.blog.csdn.net/20180205232016385)

### <font color=red>将Tomcat集成到eclipse中</font>

1. 打开Eclipse，单击“Window”菜单，选择下方的“Preferences”。
![这里写图片描述](http://img.blog.csdn.net/20160705110138349)

2. 单击“Server”选项，选择下方的“Runtime Environments”。
![这里写图片描述](http://img.blog.csdn.net/20160705110536855)

3. 点击“Add”添加Tomcat，依据你下载的Tomcat版本选择Tomcat
 ![这里写图片描述](http://img.blog.csdn.net/20160705111130113)

4. 点击“Next”，选中自己安装的Tomcat路径。
![这里写图片描述](http://img.blog.csdn.net/20160705112803636)

5. 点击“Finish”完成。
 ![这里写图片描述](http://img.blog.csdn.net/20160705112952920)

6. Servers标签下方区域多了一个服务器，双击红色部分

![1.jpg](http://img.blog.csdn.net/20180310093152101)

7. 选择Use Tomcat installation，并保（此页面为Tomcat的配置文件，可以在此修改端口号）。

![2.jpg](http://img.blog.csdn.net/20180310093300819)















### <font color=red>HTTP状态码</font>

- 200：成功

- 3XX：被请求的资源有一系列可供选择的回馈信息，每个都有自己特定的地址和浏览器驱动的商议信息。

- 4XX：请求错误，类的状态码代表了客户端看起来可能发生了错误，妨碍了服务器的处理。
	- 403：禁止-即使有权限也不需要访问
	- 404：服务器找不到指定的资源，文档不存在

- 5XX：服务器错误。这类状态码代表服务器在处理请求的过程中有错误或者异常状态发生，也有可能是服务器意识到当前的软硬件资源无法完成对请求的处理
	- 500：服务器内部错误，因为意外情况，服务器不能完成请求。

### <font color=red>配置首页</font>

为web项目配置首页，可以新建一个名为index.jsp的文件，如果想修改首页的名称，可以找到web.xml文件中的相关配置

```
<welcome-file-list>
    <welcome-file>index.html</welcome-file>
    <welcome-file>index2.html</welcome-file>
</welcome-file-list>
```

### <font color=red>JSP</font>

JSP（Java server page），Java服务页，使用Java完成动态的web开发，代码风格也ASP相类似，都属于在HTML页面中嵌入Java代码实现功能，由于Java语言的跨平台性，JSP不会受到操作系统或者开发平台的限制，有多种服务器支持，如Tomcat, Weblogic, Websphere, JBoss等

JSP的前身是Servlet（服务器端小程序），适用于中大型项目开发

- JSP的优点：
	- 平台适应性广，几乎所有平台都支持JSP
	- 编译后运行，运行效率高
	- 统一的数据库接口标准JDBC

- JSP的缺点：
	- 开发运行环境相对ASP来说，要复杂一些
	- 相对于ASP的VBScript脚本语言来说，Java语言学习起来要困难一些