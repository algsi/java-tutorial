# cookie的作用域和路径

## 问题

以前没想过cookie还有作用域和限制路径这个事情，但是今天在写代码中遇到了这个问题。

项目名为ServletTest，有一个登录页面（login.html），位于项目的根路径下面，登录信息提交给一个Servlet（SetData.java）,配置的urlpattern为（/do/login，相对于根路径），在此Servlet中将登录信息保存为两个cookie存于浏览器中。下面为Servlet部分代码

```java
@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		
		Cookie firstName = new Cookie("first_name", req.getParameter("first_name"));
		Cookie lastName = new Cookie("last_name", req.getParameter("last_name"));

		
		// 为两个 Cookies 设置过期日期为 24 小时后，秒为单位
        firstName.setMaxAge(60*60*24); 
        lastName.setMaxAge(60*60*24);
       
        resp.addCookie(firstName);
        resp.addCookie(lastName);
       
       resp.sendRedirect("../index.jsp");
       //req.getRequestDispatcher("../index.jsp").forward(req, resp);
	}
```

在index.jsp页面获取cookie信息并显示（位于根路径下的index.jsp）。

问题就是我使用重定向方式（sendRedirect）到index.jsp页面获取不到cookie，而使用请求转发方式（getRequestDispatcher）能获取到cookie信息。这是为什么呢？这两种方式是有区别的：sendRedirect()是重新定向，前后页面不是一个同一个request，在浏览器端运行，地址栏信息发生改变，可以跳转到任意网页；而getRequestDispatcher()是请求转发，前后页面共享一个request，并且这个是在服务端运行的，对浏览器来说是透明的，地址栏信息不会发生变化，只能跳转到web应用内的网页。
具体区别请看我的另一篇文章：[请求转发方式与重定向方式的区别](https://blog.csdn.net/sinat_37976731/article/details/80303880)

因为sendRedirect之后，前后页面不是一个同一个request，地址栏信息也发生了变化，使用sendRedirect后地址栏的地址信息为：http://localhost:8080/ServletTest/index.jsp
而使用getRequestDispatcher后地址栏的地址信息为（实际上虽然请求转发，但是地址信息没变）：http://localhost:8080/ServletTest/do/login

我使用浏览器查看cookie，发现cookie是有域名和路径的，如下图：

![cookie1.PNG](https://img-blog.csdn.net/20180513235112339)

![cookie2.PNG](https://img-blog.csdn.net/20180513235123927)

![cookie3.PNG](https://img-blog.csdn.net/20180513235135921)

然而我的index.jsp文件位于 "/ServletTest" 路径下（一级目录），不是同一个路径下，取不到 "/ServletTest/do" 路径下（二级目录）的cookie。

因此使用Cookie的setPath()方法设置了路径（cookie的路径变为"/ServletTest"），使用sendRedirect方法也可以取到cookie。

```java
/*获取的为根路径名"/ServletTest"，设置cookie路径*/
firstName.setPath(req.getContextPath());
lastName.setPath(req.getContextPath());
```

正常的cookie只能在一个应用中（简单理解，就是一个目录）共享，即一个cookie只能由创建它的应用获得，比如在/test1/*创建自己应用的cookie，/test2/*下是拿不到。而且很容易被忽视的一点：path默认是产生cookie的应用的路径。

如果两者需要共享的话，指定Path为"/"或根路径即可。


我们还可以通过Cookie的setDomain()方法来设置cookie的域名。如果我们需要在这个项目中设置一个cookie而另一个项目来获取，可以做此操作。Domain为设置Cookie的有效域，Path限制有效路径。

