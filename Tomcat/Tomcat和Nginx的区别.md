# Nginx 和 Tomcat

Nginx常用做静态内容服务和代理服务器，将外来请求转发给后面的应用服务器（Tomcat，Django，JBoss等），Tomcat更多用来做一个应用容器，让Java、Web、Application等应用运行在里面。

严格意义上来讲，Nginx应该叫做HTTP Server，而Tomcat是一个Application Server，Tomcat是一个Servlet/JSO应用的容器。

客户端通过HTTP Server访问服务器上存储的资源（HTML文件，图片文件等），HTTP Server是中只是把服务器上的文件如实通过HTTP协议传输给客户端。

应用服务器往往是运行在HTTP Server的背后，执行应用，将动态的内容转化为静态的内容之后，通过HTTP Server分发到客户端。

注意：Nginx只是把请求做了分发，不做处理！
