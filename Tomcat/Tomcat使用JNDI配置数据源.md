# Tomcat使用JNDI配置数据源

要在Tomcat中使用JNDI来配置数据源，我们可以去参考Tomcat官网的关于某一版本的Document中的JNDI Resource的介绍，如：[JNDI Resources HOW-TO](https://tomcat.apache.org/tomcat-8.5-doc/jndi-resources-howto.html)

## 使用JNDI配置数据源

到服务器上对 DataSource 进行配置。

- 服务器： Tomcat 8.5版本

- 数据库：MySQL

1. <font color="red">第一步将Mysql驱动中得jar文件拷贝到Tomcat安装目录下的lib文件夹下，这一步很重要，一般容易忘记。</font>

2. 使用eclipse建立一个Dynamic Web Project，在 META-INF/ 下建立context.xml文件，

	```xml
    <?xml version="1.0" encoding="UTF-8"?>

    <Context>
    <Resource 
        auth="Container" 
		driverClassName="com.mysql.jdbc.Driver" 
		maxIdle="30" 
		maxTotal="50" 
		maxWaitMillis="-1" 
		name="jdbc/MVNT1" 
		username="root"
		password="123456" 
		type="javax.sql.DataSource" 
		url="jdbc:mysql://localhost:3306/learning?useSSL=true"/>
    </Context>
	```
	
    当然其实我们也可以找到Tomcat的server.xml文件夹，将此Resource标签配置到对应工程的Context标签中。这其中的属性的部分解释如下：
    
   ```xml
    <Context>

        <!-- maxTotal: Maximum number of database connections in pool. Make sure you
             configure your mysqld max_connections large enough to handle
             all of your db connections. Set to -1 for no limit.
             -->

        <!-- maxIdle: Maximum number of idle database connections to retain in pool.
             Set to -1 for no limit.  See also the DBCP documentation on this
             and the minEvictableIdleTimeMillis configuration parameter.
             -->

        <!-- maxWaitMillis: Maximum time to wait for a database connection to become available
             in ms, in this example 10 seconds. An Exception is thrown if
             this timeout is exceeded.  Set to -1 to wait indefinitely.
             -->

        <!-- username and password: MySQL username and password for database connections  -->

        <!-- driverClassName: Class name for the old mm.mysql JDBC driver is
             org.gjt.mm.mysql.Driver - we recommend using Connector/J though.
             Class name for the official MySQL Connector/J driver is com.mysql.jdbc.Driver.
             -->

        <!-- url: The JDBC connection url for connecting to your MySQL database.
             -->

      <Resource name="jdbc/TestDB" auth="Container" type="javax.sql.DataSource"
                   maxTotal="100" maxIdle="30" maxWaitMillis="10000"
                   username="javauser" password="javadude" driverClassName="com.mysql.jdbc.Driver"
                   url="jdbc:mysql://localhost:3306/javatest"/>

    </Context>
    ```
    
    其中数据源的名字配置在name属性里。

3. 在 WEB-INF/ 下创建 web.xml 注意，这里是项目下的 web.xml。添加以下内容：

	当然，如果我们一开始将数据源（Resource标签）配置Tomcat的server.xml文件中的话，那么这一步我们是可以不要的。
    
    ```xml
    <?xml version="1.0" encoding="UTF-8"?>
     <!DOCTYPE web-app PUBLIC
      '-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN'
      'http://java.sun.com/j2ee/dtds/web-app_2_3.dtd'>
    <web-app> 
        <resource-ref>
              <description>DB Connection</description>
              <!-- 这个名字必须和数据源名字一致 -->
              <res-ref-name>jdbc/MVNT1</res-ref-name>
              <res-type>javax.sql.DataSource</res-type>
              <res-auth>Container</res-auth>
        </resource-ref>
    </web-app>
	```
    
    至此，数据源的配置我们就完成了，下面是数据源的获取部分。

## 获取数据源

数据源是获取和数据库的连接，所以一般我们可以创建 ServletContext监听器（implements ServletContextListener）在 ServletContext 创建的时候就去获取数据源。如下：

```java
package com.li.bookstore.web.listener;

import javax.annotation.Resource;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;

import com.li.bookstore.common.ConnectionManager;

@WebListener
public class WebContextListener implements ServletContextListener {
	/*
	 * 使用Resource注解将server.xml中配置的数据源注入进来
	 * lookup：指定目录处的名称，此属性是固定的
	 * name：指定数据源的名称，即数据源处配置的name属性
	 */
	@Resource(lookup="java:/comp/env", name="jdbc/MVNT1")

	/*将找到的数据源保存在此变量中，javax.sql.DataSource*/
	private DataSource dataSource;

	@Override
	public void contextDestroyed(ServletContextEvent event) {

	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		/*测试数据源*/
		//System.out.println(dataSource);

		/*将数据源注入连接管理*/
		ConnectionManager.setDadaSource(dataSource);
	}
}

```

然后再通过数据源获取连接

```java
package com.li.bookstore.common;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

/*连接对象的管理者*/

public final class ConnectionManager {
	/*确保在每一个连接里是同一个连接对象，方便以后做事务的管理，针对每个线程创建一个独立的容器*/
	/*使用泛型标准*/
	private final static ThreadLocal<Connection> LOCAL=new ThreadLocal();
	private static DataSource dataSource;
	
	public static void setDadaSource(DataSource dataSource) {
		/*不能使用this*/
		ConnectionManager.dataSource=dataSource;
	}
	
	/*返回连接对象*/
	public static Connection getConnection() throws SQLException {
		/*获取连接对象*/
		Connection conn=LOCAL.get();
		if(null != conn) {
			return conn;
		}
		
		/*通过数据源得到连接，并放入线程中管理，再返回连接对象*/
		conn=dataSource.getConnection();
		LOCAL.set(conn);
		return conn;
	}
	
	/*释放连接对象*/
	public static void release() {
		Connection conn=LOCAL.get();
		if(null != conn) {
			DBUtil.release(conn);
			LOCAL.remove();
		}
	}
}

```

也可以直接使用Context来获取数据源（注意抛出异常）

```java
Context cxt=new InitialContext();

//获取与逻辑名相关联的数据源对象
DataSource ds=(DataSource)cxt.lookup("java:comp/env/jdbc/MVNT1");

Connection conn=ds.getConnection();
```