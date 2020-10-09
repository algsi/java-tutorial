# 利用反射机制将Servlet集成

## 问题引出

在进行 JavaWeb 开发时，有时我们需要写很多个Servlet来对请求进行处理或转发，整个过程繁琐而且还有很多重复的部分。此时我们可以使用反射将一类操作集成到一个Servlet中。

## 案例设计

例如，对用户信息的增删改查操作，这一相似而已一类的操作我们都要写好几个Servlet呢？这样未免太占用资源，我们现在设想，编写一个基础类（BasicServlet），此基础类继承HTTPServlet，在这里我们重写其 doPost 和 doGet 方法。我们再编写一个继承类（StudentManage）来继承 BasicServlet，在这里，我们编写各种方法去处理各种操作（如增删改查），集成到了一个 Servlet 中，我们调用该StudentManage 服务，在 BasicServlet 接收操作参数（操作标识符），再利用反射调用 StudentManage 中的处理方法。

## 程序

#### 基础Servlet

```java
package com.li.common.servlet;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BasicServlet extends HttpServlet {

	private static final long serialVersionUID = 8780356163185352965L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		/* 获取传入进来要调用的方法名称，StudentManage继承了该类，所以在这里接收方法 */
		String handler = req.getParameter("handler");

		try {
			/* 通过此对象获取类，再获取类中指定名称并传入参数类型的对应Class对象 */
			Method method = this.getClass().getDeclaredMethod(handler, HttpServletRequest.class, HttpServletResponse.class);
			/* 执行方法，this实际上是StudentManage对象 */
			method.invoke(this, req, resp);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}

```

#### 集成Servlet

```java
package com.li.controller;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.li.common.servlet.BasicServlet;

@WebServlet("/do/stumanage")
public class StudentManage extends BasicServlet {

	private static final long serialVersionUID = 6523652706243471892L;

	public void addStu(HttpServletRequest req, HttpServletResponse resp) {
		System.out.println("添加信息");
	}

	public void deleteStu(HttpServletRequest req, HttpServletResponse resp) {
		System.out.println("删除信息");
	}

	public void updateStu(HttpServletRequest req, HttpServletResponse resp) {
		System.out.println("更新信息");
	}

	public void queryStu(HttpServletRequest req, HttpServletResponse resp) {
		System.out.println("检索信息");
	}
}

```

执行流程：

1. 在页面请求要处理的服务并传入参数到StudentManage控制器中，此参数标识了你要请求的具体服务，例如，若要请求添加信息服务，可以传入参数 handler=addStu。

2. 因为 StudentManage 继承了 BasicServlet，所以参数传入到 BasicServlet 的doPost或doGet方法中。在此方法中我们接收了参数，并利用反射来该对象（this，实际上是StudentManage对象）的指定名称的方法。

3. 我们从页面调用 StudentManage 并传入参数 handler=updateStu，输出结果为：更新信息。