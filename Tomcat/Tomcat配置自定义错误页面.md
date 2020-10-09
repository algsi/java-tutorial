# Tomcat配置自定义错误页面

## 一、说明

web系统开发过程中经常遇见400/404/500类型的错误页面，对于开发人员而言见到这种页面多了可能觉得问题不大，但是实际运用当中是面向客户的，需要配置一个合理的自定义错误页面。

## 二、实现

/WEB-INF/web.xml 文件

```xml
<!-- 错误页配置开始 -->
<!-- 400错误 -->
<error-page>
	<error-code>400</error-code>
	<location>/error.jsp</location>
</error-page>
<!-- 404 页面不存在错误 -->
<error-page>
	<error-code>404</error-code>
	<location>/error.jsp</location>
</error-page>
<!-- 500 服务器内部错误 -->
<error-page>
	<error-code>500</error-code>
	<location>/error.jsp</location>
</error-page>
<!-- java.lang.Exception异常错误,依据这个标记可定义多个类似错误提示 -->
<error-page>
	<exception-type>java.lang.Exception</exception-type>
	<location>/error.jsp</location>
</error-page>
<!-- java.lang.NullPointerException异常错误,依据这个标记可定义多个类似错误提示 -->
<error-page>
	<exception-type>java.lang.NullPointerException</exception-type>
	<location>/error.jsp</location>
</error-page>

<error-page>
	<exception-type>javax.servlet.ServletException</exception-type>
	<location>/error.jsp</location>
</error-page>
<!-- 错误页配置结束 -->
```

而 error.jsp 直接放在webapp或WebContent根目录下就可以了。

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	尊敬的朋友，后台服务器有问题，工程师们正在努力抢修中，请稍后访问......
</body>
</html>
```

重启服务器生效！！！