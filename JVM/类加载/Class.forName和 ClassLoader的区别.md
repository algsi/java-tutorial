# Class.forName 和 ClassLoader 的区别

在java中 Class.forName() 和 ClassLoader 都可以对类进行加载。ClassLoader就是遵循双亲委派模型最终调用启动类加载器的类加载器，实现的功能是“通过一个类的全限定名来获取描述此类的二进制字节流”，获取到二进制流后放到JVM中。Class.forName()方法实际上也是调用的ClassLoader来实现的。

`Class.forName(String className)` 这个方法的源码是：

```java
@CallerSensitive
public static Class<?> forName(String className)
            throws ClassNotFoundException {
    Class<?> caller = Reflection.getCallerClass();
    return forName0(className, true, ClassLoader.getClassLoader(caller), caller);
}
```

最后调用的是 forName0() 这个方法，在这个forName0方法中的第二个参数被默认设置为了true，这个参数代表在加载类的时候是否对加载的类进行初始化。

也可以调用 `Class.forName(String name, boolean initialize, ClassLoader loader)` 方法来手动选择在加载类的时候是否要对类进行初始化。`Class.forName(String name, boolean initialize,ClassLoader loader)` 的源码如下：

```java
/*
* @param name       fully qualified name of the desired class
* @param initialize if {@code true} the class will be initialized.
*                   See Section 12.4 of <em>The Java Language Specification</em>.
* @param loader     class loader from which the class must be loaded
* @return           class object representing the desired class
*
* @exception LinkageError if the linkage fails
* @exception ExceptionInInitializerError if the initialization provoked
*            by this method fails
* @exception ClassNotFoundException if the class cannot be located by
*            the specified class loader
*
* @see       java.lang.Class#forName(String)
* @see       java.lang.ClassLoader
* @since     1.2
*/
@CallerSensitive
public static Class<?> forName(String name, boolean initialize,
                                ClassLoader loader)
    throws ClassNotFoundException
{
    Class<?> caller = null;
    SecurityManager sm = System.getSecurityManager();
    if (sm != null) {
        // Reflective call to get caller class is only needed if a security manager
        // is present.  Avoid the overhead of making this call otherwise.
        caller = Reflection.getCallerClass();
        if (sun.misc.VM.isSystemDomainLoader(loader)) {
            ClassLoader ccl = ClassLoader.getClassLoader(caller);
            if (!sun.misc.VM.isSystemDomainLoader(ccl)) {
                sm.checkPermission(
                    SecurityConstants.GET_CLASSLOADER_PERMISSION);
            }
        }
    }
    return forName0(name, initialize, loader, caller);
}
```

源码中的注释只摘取了一部分，其中对参数initialize的描述是：if {@code true} the class will be initialized。意思就是说：如果参数为true，则加载的类将会被初始化。而且可以看到，这重载的两个方法，最后调用的都是同一个方法。

## 示例

下面还是例子来测试它们之间的区别吧。

一个含有静态代码块、静态变量、赋值给静态变量的静态方法的类：

```java
public class ClassForName {

    // 静态代码块
    static {
        System.out.println("执行了静态代码块");
    }

    private static String staticField = staticMethod();

    private static String staticMethod() {
        System.out.println("执行了静态方法");
        return "给静态属性赋值";
    }
}
```

使用Class.forName()的测试方法：

```java
public static void main(String[] args) {

    try {
        Class.forName("com.xavier.jvm.ClassForName");
        System.out.println("class load end");
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
    }
}
```

运行结果：

```language
执行了静态代码块
执行了静态方法
class load end
```

使用ClassLoader的测试方法：

```java
public static void main(String[] args) {

    try {
        ClassLoader.getSystemClassLoader().loadClass("com.xavier.jvm.ClassForName");
        System.out.println("class load end");
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
    }
}
```

运行结果：

```language
class load end
```

**根据运行结果得出Class.forName加载类时将类进了初始化，而ClassLoader的loadClass并没有对类进行初始化，只是把类加载到了虚拟机中。**

## 应用场景

在我们熟悉的Spring框架中的IOC的实现就是使用的ClassLoader。

而在我们使用JDBC时通常是使用Class.forName()方法来加载数据库连接驱动。这是因为在JDBC规范中明确要求Driver(数据库驱动)类必须向DriverManager注册自己。

以MySQL的驱动为例解释：

```java
package com.mysql.jdbc;

import java.sql.SQLException;

public class Driver extends NonRegisteringDriver implements java.sql.Driver {  
    // ~ Static fields/initializers  
    // ---------------------------------------------  

    //  
    // Register ourselves with the DriverManager  
    //  
    static {  
        try {  
            java.sql.DriverManager.registerDriver(new Driver());  
        } catch (SQLException E) {  
            throw new RuntimeException("Can't register driver!");  
        }  
    }  

    // ~ Constructors  
    // -----------------------------------------------------------  

    /**
     * Construct a new driver and register it with DriverManager
     *  
     * @throws SQLException
     *             if a database error occurs.
     */  
    public Driver() throws SQLException {  
        // Required for Class.forName().newInstance()      }  

}
```

我们看到Driver注册到DriverManager中的操作写在了静态代码块中，这就是为什么在写JDBC时使用Class.forName()的原因了。

