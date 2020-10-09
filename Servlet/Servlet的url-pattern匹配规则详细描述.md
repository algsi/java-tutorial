# Servlet的url-pattern匹配规则详细描述

## 概述

在利用servlet或Filter进行url请求的匹配时，很关键的一点就是匹配规则，但servlet容器中的匹配规则既不是简单的通配，也不是正则表达式，而是由自己的规则，比较容易混淆。本文来详细举例介绍下。下面的说明都是在tomcat服务器中得到验证的。

先介绍一下匹配的概念，上例子代码。在一个app（如名字为myapp）的web.xml文件中，有如下信息：

```xml
<servlet>
	<servlet-name>MyServlet</servlet-name>
	<servlet-class>com.nau.MyServlet</servlet-class>
</servlet>

<servlet-mapping>
	<servlet-name>MyServlet</servlet-name>
	<url-pattern>xxxxxx</url-pattern>
	<url-pattern>yyyyyyy</url-pattern>
</servlet-mapping>
```

上面的配置信息，其中 &lt;servlet&gt; 标签首先配置声明一个servlet，包括servlet的名字和对应的java类名。其中 &lt;servlet-mapping&gt; 标签声明了与该servlet相应的匹配规则，每个 &lt;url-pattern&gt; 标签代表一个匹配规则。

当浏览器发起一个url请求后，该请求发送到servlet容器的时候，容器先会将请求的url减去当前应用上下文（application context）的路径作为servlet的映射url，比如url是 `http://10.43.11.143/myapp/kata/detail.html`，其应用上下文（context）是 `myapp`，容器会将 `http://10.43.11.143/myapp` 去掉，剩下的 `/kata/detail.html` 部分拿来做servlet的映射匹配（注意前面有斜杠，所以现在；应该明白了为什么 url-pattern 配置要有斜杠前缀）。这个映射匹配过程是有优先顺序的(具体的优先顺序规则后面介绍)，而且当有一个servlet匹配成功以后，就不会去理会剩下的servlet了。

注意Filter的匹配规则与servlet一样，但对于filter，不会像servlet那样只匹配一个servlet，因为filter的集合是一个链，所以只会有处理的顺序不同，而不会出现只选择一个filter。Filter的处理顺序和filter-mapping在web.xml中定义的顺序相同。

下面我们详细介绍各种匹配规则

## 一、精确匹配

&lt;url-pattern&gt; 中配置的项必须与url完全精确匹配。

```xml
<servlet-mapping>
    <servlet-name>MyServlet</servlet-name>
    <url-pattern>/kata/detail.html</url-pattern>
    <url-pattern>/demo.html</url-pattern>
    <url-pattern>/table</url-pattern>
</servlet-mapping>
```

当在浏览器中输入如下几种url时，都会被匹配到该servlet

http://10.43.11.143/myapp/kata/detail.html
http://10.43.11.143/myapp/demo.html
http://10.43.11.143/myapp/table

注意：

http://10.43.11.143/myapp/table/ 是非法的url，不会被当作http://10.43.11.143/myapp/table 识别

另外上述url后面可以跟任意的查询条件，即url传参，都会被匹配，如

http://10.43.11.143/myapp/table?hello 这个请求就会被匹配到MyServlet。

## 二、扩展名匹配

```xml
<servlet-mapping>
    <servlet-name>MyServlet</servlet-name>
    <url-pattern>*.jsp</url-pattern>
</servlet-mapping>
```

则任何扩展名为jsp（文件名和路径任意）的url请求都会匹配，比如下面的url都会被匹配

http://10.43.11.143/myapp/demo.jsp
http://10.43.11.143/myapp/test.jsp

## 三、路径匹配

```xml
<servlet-mapping>
    <servlet-name>MyServlet</servlet-name>
    <url-pattern>/kata/*</url-pattern>
</servlet-mapping>
```

则请求的ulr只要前面（myapp之后）的路径是/kata，而后面的路径可以任意。比如下面的url都会被匹配。

http://10.43.11.143/myapp/kata/demo.html
http://10.43.11.143/myapp/kata/test.jsp
http://10.43.11.143/myapp/kata/test/detail.html
http://10.43.11.143/myapp/kata/action
http://10.43.11.143/myapp/kata/action/

注意：路径和扩展名匹配无法同时设置，比如下面的三个<url-pattern>都是非法的，如果设置，启动tomcat服务器会报错。

```xml
<url-pattern>/kata/*.jsp</url-pattern>

<url-pattern>/*.jsp</url-pattern>

<url-pattern>he*.jsp</url-pattern>
```

另外注意：

```xml
<url-pattern>/aa/*/bb</url-pattern>
```

这个是精确匹配，url必须是 `/aa/*/bb`，这里的 `*` 不是通配的含义。

## 四、匹配任意的url

```xml
<url-pattern>/</url-pattern>

<url-pattern>/*</url-pattern>
```

则所有的url都可以被匹配上。其中 `/*` 是路径匹配，只是路径就是 `/`。

## 五、优先顺序

当一个url与多个servlet的匹配规则可以匹配时，则按照 精确路径 > 最长路径 > 扩展名 这样的优先级匹配到对应的servlet。举例如下：

例1：比如servletA 的url-pattern为 `/test`，servletB的url-pattern为 `/*` ，这个时候，如果我访问的url为 `http://localhost/test` ，这个时候容器就会先进行精确路径匹配，发现 `/test` 正好被servletA精确匹配，那么就去调用servletA，不会去管servletB。

例2：比如servletA的url-pattern为 `/test/*` ，而servletB的url-pattern为 `/test/a/*`，此时访问 `http://localhost/test/a` 时，容器会选择路径最长的servlet来匹配，也就是这里的servletB。

例3: 比如servletA的url-pattern：`*.action`，servletB的url-pattern为 `/*`，这个时候，如果我访问的url为 `http://localhost/test.action`，这个时候容器就会优先进行路径匹配，而不是去匹配扩展名，这样就去调用servletB。