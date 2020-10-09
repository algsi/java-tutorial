# Java中创建对象的四种方式

Java是一种面向对象的语言，我们通常将事务抽象封装成一个类之后需要用到该类，使用类一般来说就会创建该类的对象，即将类实例化。

一想到创建对象，很多人都熟悉使用 new 关键字来创建对象，其实创建对象的方式有四种：

| 方式 | 说明 |
|--------|--------|
| new |  需要调用构造函数  |
| 反射 | 需要调用构造函数，免疫一切访问权限的限制(public,private等) |
| clone | 需要实现Cloneable接口,又分深复制,浅复制 |
| 序列化 | 1. 将对象保存在硬盘中 <br/> 2. 通过网络传输对象，需要实现Serializable接口 |

## 准备的测试类

在准备的测试类中，我给出了一个有参的构造方法和一个无参的构造方法：

```java
package com.vin.common;

/**
 * 用户测试类
 *
 * @author Vintage
 * 2018/11/7
 */
public class User {

    /** 姓名 */
    private String name;
    /** 年龄 */
    private int age;

    /**
     * 有参的构造方法
     *
     * @param name 姓名
     * @param age 年龄
     */
    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    /**
     * 无参的构造方法
     */
    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}

```

## 1. 使用 new 关键字

这是最常见也是最简单的创建对象的方式了。通过这种方式，我们可以调用对应的构造函数来创建对象。

```java
User user = new User("Li", 20);
```

## 2. 反射

Java中具体的反射机制以及其细节我就不在这篇文章中过多描述，可以自行了解一下。

通过反射来创建对象又分为以下两种方法。

##### （1）使用 Class 类的 newInstance() 方法

我们可以先获取类（User类）对应的 Class 类对象，然后在调用Class类的 newInstance() 方法调用目标类的 **无参的构造方法** 创建对象。需要注意的是，如果我们在 User 类中提供了有参的构造方法而不提供无参的构造方法，调用 newInstance() 方法会报 `java.lang.NoSuchMethodException` 异常。

示例程序：

```java
public class Test {

    public static void main(String[] args) {

        try {

            User user = User.class.newInstance();
            System.out.println(user);

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
```

输出结果：

```language
User{name='null', age=0}
```

##### （2）使用 Constructor 类的 newInstance() 方法

和 Class 类的 newInstance 方法很像，java.lang.reflect.Constructor 类里也有一个 newInstance() 方法可以创建对象。我们可以通过这个 newInstance() 方法调用目标类的有参和无参的构造方法，包括私有的构造方法。

我们先给 User 类增加一个私有的构造方法：

```java
/**
 * 私有的构造方法
 *
 * @param name 姓名
 */
private User(String name) {
	this.name = name;
}
```

示例程序：

```java
package com.vin.common;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Test {

    public static void main(String[] args) {

        try {
            // 有参的
            Constructor<User> constructor1 = User.class.getConstructor(String.class, int.class);
            User user1 = constructor1.newInstance("Li", 20);
            System.out.println(user1);

            // 无参的
            Constructor<User> constructor2 = User.class.getConstructor();
            User user2 = constructor2.newInstance();
            System.out.println(user2);

            // 要获取私有的构造方法，需要使用 getDeclaredConstructor 方法
            Class classType = User.class;
            Constructor<User> constructor3 = classType.getDeclaredConstructor(String.class);
            constructor3.setAccessible(true);
            User user3 = constructor3.newInstance("Li");
            System.out.println(user3);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

    }

}

```

输出结果：

```language
User{name='Li', age=20}
User{name='null', age=0}
User{name='Li', age=0}
```

## 3. 调用对象的 clone 方法

这种创建对象的方式其实就是通过一个已有的对象，克隆出一个新的对象。要调用对象的 clone 方法，需要先让对象实现 Cloneable 接口，而且，克隆又分为浅克隆和深克隆，具体请了解 Java 对象克隆。

```java
User user = userSource.clone();
```

用 clone 方法创建对象并不会调用任何构造函数。

## 4. 序列化

实际上这种方式也是一种对象克隆的方式，即使用序列化来完成深克隆，

当我们序列化和反序列化一个对象，JVM 会给我们创建一个单独的对象。在反序列化时，JVM 创建对象并不会调用任何构造函数。

为了反序列化一个对象，我们需要让我们的类实现Serializable接口。

详情请见使用序列化复制对象。示例如下：

```java
package com.vin.common;

import java.io.*;

/**
 * @author Vintage
 * 2018/11/8
 */
public class CloneUtils {

    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T clone(T obj){
        T cloneObj = null;
        try {
            //写入字节流
            ObjectOutputStream obs = new ObjectOutputStream(new FileOutputStream("data.obj"));
            obs.writeObject(obj);
            obs.close();

            //分配内存，写入原始对象，生成新对象
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("data.obj"));

            //返回生成的新对象
            cloneObj = (T) ois.readObject();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cloneObj;
    }

    public static void main(String[] args) {
        // 其中User类实现了Serializable接口
        User userSource = new User("Li");
        User user = CloneUtils.clone(userSource);
        System.out.println(user);
    }
}
```