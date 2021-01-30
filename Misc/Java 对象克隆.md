# Java 对象克隆

在 Java 中，什么是对象克隆呢？简单地来说，就是将已有的一个对象，再复制出一个一模一样的出来。但是，需要注意的是，对象克隆有别于 Java 中的赋值。我们先来了解一下 Java 赋值，比如说，对于原始数据类型的赋值如下：

```java
int i = 5;
int j = i;
```

这很简单，i 和 j 的值都将为 5，不仅仅是int类型，其它七种原始数据类型(boolean，char，byte，short，float，double，long)同样适用于该类情况。

那么我们现在来看看引用数据类型，先给出一个用户类（User）：

```java
package com.xavier.clone;

/**
 * @author Xavier
 */
public class User implements Cloneable{

    private String name;

    private int age;

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public User() {}
}
```

再执行一段对象赋值程序：

```java
User user1 = new User("Li", 20);
User user2 = user1;
System.out.println(user1 == user2);
```

上面先创建了一个 User 对象 user1，再将其赋值给 user2，最后的输出结果也是不言而喻的（输出 true），这也告诉我们一个信息，user1 和 user2 这两个引用实际上指向内存堆中同一个对象，如果我们改变 user1 中的属性，user2 中的属性信息也会随着改变。

这是我们心中所想的克隆吗？当然不是，我们理解的克隆是一个变两个，两个变多个！

## 1. Object 中的 clone() 方法

在所有类的父类 Object 中，有这么一个方法是需要我们去了解的：clone()

我们可以看看源码中它的方法声明：

```java
protected native Object clone() throws CloneNotSupportedException;
```

它的访问限定符为 protected，并且还是 native 方法，大家都知道 native 方法是非 Java 语言实现的代码，供 Java 程序调用的，因为 Java 程序是运行在 JVM 虚拟机上面的，要想访问到比较底层的与操作系统相关的就没办法了，只能由靠近操作系统的语言来实现。

该方法的注释很多，有兴趣的可以自己去查看，我这里列出关键的几点：

> Creates and returns a copy of this object.
> x.clone() != x will be true
> x.clone().getClass() == x.getClass() will be true, but these are not absolute requirements.
> x.clone().equals(x) will be true, this is not an absolute requirement.

1. 创建并返回一个克隆的对象
2. 原始的对象和克隆的对象它们引用的对象是不同的，有单独的内存地址分配
3. 原始对象和克隆对象应该具有相同的类类型，但这不是强制的
4. 原始对象和克隆对象调用 equals() 方法应该返回 true，但这不是强制的

## 2. 为什么要克隆对象

为什么需要克隆对象？直接 new 一个对象不行吗？克隆的对象可能包含一些已经存在过的属性信息，而 new 出来的对象的属性都还是初始化时候的值，所以当需要一个新的对象来保存当前对象的“状态”就靠 clone() 方法了。那么我把这个对象的临时属性一个一个的赋值给我新 new 的对象不也行嘛？可以是可以，但是一来麻烦不说，二来，大家通过上面的源码都发现了 clone() 是一个 native 方法，就是快啊，在底层实现的。而且我们常见的对象赋值复制的是引用，即对象在内存中的地址，两个引用仍然指向了同一个对象，而通过 clone 方法赋值的对象跟原来的对象时同时独立存在的。

## 3. 如何实现克隆

先介绍两种克隆的方式：浅克隆（shallow clone）和深克隆（deep clone）。

在 Java 中，数据类型分为两种：基本数据类型和引用数据类型，基本的数据类型包括我们所知道简单的数据类型，引用数据类型包括类、接口等复杂的数据类型。因此，浅克隆和深克隆的区别就在于是否支持引用数据类型的成员变量的复制。

实现克隆的一般步骤是：

1. 被复制的类需要实现 java.lang.Cloneable 接口（不实现的话在调用 clone 方法时会抛出CloneNotSupportedException异常），该接口为标记接口，不包含任何方法。

2. 覆盖 clone() 方法，访问修饰符设置为 public（clone方法原本的修饰符为 protected，不修改访问权限的话别的类可能访问不到），在方法中调用 super.clone()方法得到需要复制的对象。（native 为本地方法）

### 浅克隆

下面是对上面的 User 类进行改造，实现了 java.lang.Cloneable 接口，并重写了 clone 方法，clone 方法会 抛出 CloneNotSupportException 异常，我们可以在 clone 方法中处理掉该异常或者把该异常往上抛出。

```java
package com.xavier.clone;

/**
 * @author Xavier
 */
public class User implements Cloneable{

    private String name;

    private int age;

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public User() { }

    /**
     * override clone(), and assign stronger access privileges ("public").
     *
     * @return
     * @throws CloneNotSupportedException
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        User user = null;
        user = (User) super.clone();
        return user;
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

我们可以看到，在 clone 方法中，我们先创建了一个空的 User 对象引用，我们可以把它看做克隆中的空壳，随后调用 `super.clone()` 方法将对象的信息赋值到空壳中，由于 clone 的返回值类型为 Object，所以需要进行强转，最后返回新创建的对象的引用。

示例程序：

```java
public static void main(String[] args) {
    User userSource = new User("Li", 20);
    try {
        User user = (User) userSource.clone();
        System.out.println(user);
        System.out.println("引用地址是否相同：" + (userSource == user));
    } catch (CloneNotSupportedException e) {
        e.printStackTrace();
    }
}
```

输出为：

```language
User{name='Li', age=20}
引用地址是否相同：false
```

可以看到，复制成功。这只是浅克隆，还有复杂一点的深克隆。

### 深克隆

我们先给出一个 Address 类：

```java
package com.xavier.clone;

/**
 * @author Xavier
 */
public class Address {

    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Address(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Address{" +
                "address='" + address + '\'' +
                '}';
    }
}
```

再在 User 类中添加一个 Address 类型的成员变量：

```java
package com.vin.common;

/**
 * @author Xavier
 */
public class User implements Cloneable{

    private String name;

    private int age;

    private Address address;

    public User(String name, int age, Address address) {
        this.name = name;
        this.age = age;
        this.address = address;
    }

    public User() { }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * override clone(), and assign stronger access privileges ("public").
     *
     * @return
     * @throws CloneNotSupportedException
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        User user = null;
        user = (User) super.clone();
        return user;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", address=" + address +
                '}';
    }
}

```

测试示例程序：

```java
public static void main(String[] args) {
    Address address = new Address("北京");
    User userSource = new User("Li", 20, address);
    try {
    User user = (User) userSource.clone();
        System.out.println(user);
        System.out.println("引用地址是否相同：" + (userSource == user));
        System.out.println("address 成员的引用地址是否相同：" + (userSource.getAddress() == user.getAddress()));
    } catch (CloneNotSupportedException e) {
    e.printStackTrace();
    }
}
```

输出结果：

```language
User{name='Li', age=20, address=Address{address='北京'}}
引用地址是否相同：false
address 成员的引用地址是否相同：true
```

上面的程序好像是复制成功了，address 成员变量的内容也是一样的（"北京"），但是最后在比较两个 User 对象的 address 成员变量的引用地址时，结果是 true，这就表明，两个 User 对象的 address 成员变量引用的还是同一处内存地址，原因是对于引用类型变量来说，浅克隆只是复制变量的引用，并没有真正地开辟另一块内存空间。

所以，为了达到真正的复制对象，即深克隆，而不是纯粹引用复制。我们需要将 Address 类可复制化，并且覆盖 clone 方法，完整代码如下：

Address 类：

```java
package com.xavier.clone;

/**
 * @author Xavier
 */
public class Address implements Cloneable{

    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Address(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Address{" +
                "address='" + address + '\'' +
                '}';
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Address address = (Address) super.clone();
        return address;
    }
}
```

注意，User 类中的 clone 方法也要实现深克隆：

```java
/**
 * override clone(), and assign stronger access privileges ("public").
 *
 * @return
 * @throws CloneNotSupportedException
 */
@Override
public Object clone() throws CloneNotSupportedException {
    User user = null;
    // 浅克隆
    user = (User) super.clone();
    // 深克隆
    user.address = (Address) user.address.clone();
    return user;
}
```

测试示例程序：

```java
public static void main(String[] args) {
    Address address = new Address("北京");
    User userSource = new User("Li", 20, address);
    try {
        User user = (User) userSource.clone();
        System.out.println(user);
        System.out.println("引用地址是否相同：" + (userSource == user));
        System.out.println("address 成员的引用地址是否相同：" + (userSource.getAddress() == user.getAddress()));
    } catch (CloneNotSupportedException e) {
        e.printStackTrace();
    }
}
```

运行结果：

```language
User{name='Li', age=20, address=Address{address='北京'}}
引用地址是否相同：false
address 成员的引用地址是否相同：false
```

这样我们就完成了对象的深克隆。

我们还可以看看 JDK 8 中 java.util.Date 类中实现的 clone 方法：

java.util.Date:

```java
/**
 * Return a copy of this object.
 */
public Object clone() {
    Date d = null;
    try {
        d = (Date)super.clone();
        if (cdate != null) {
            d.cdate = (BaseCalendar.Date) cdate.clone();
        }
    } catch (CloneNotSupportedException e) {} // Won't happen
    return d;
}
```

这里面直接在方法内部处理异常（当然这个异常一般不会发生）。

## 4. 深克隆与浅克隆的区别

在浅克隆中，如果原型对象的成员变量是值类型，将复制一份给克隆对象；如果原型对象的成员变量是引用类型，那么将成员变量的引用地址复制一份给克隆对象，也就是说原型对象和克隆对象的成员变量指向相同的内存地址。

简单来说，在浅克隆中，当对象被复制时只复制它本身和其中包含的值类型的成员变量，而引用类型的成员对象并没有复制。

在深克隆中，无论原型对象的成员变量是值类型还是引用类型，都将复制一份给克隆对象，深克隆将原型对象的所有引用对象也复制一份给克隆对象。

到这里其实我们也能看出一个问题，那就是如果引用类型里面还包含很多引用类型，或者内层引用类型的类里面又包含引用类型，使用 clone 方法就会很麻烦。这时我们可以用序列化的方式来实现对象的深克隆。

序列化就是将对象写到流的过程，写到流中的对象是原有对象的一个拷贝，而原对象仍然存在于内存中。通过序列化实现的拷贝不仅可以复制对象本身，而且可以复制其引用的成员对象，因此通过序列化将对象写到一个流中，再从流里将其读出来，可以实现深克隆。需要注意的是能够实现序列化的对象其类必须实现 java.io.Serializable 接口，否则无法实现序列化操作。

扩展：Java语言提供的 Cloneable 接口和 Serializable 接口的代码非常简单，它们都是空接口，这种空接口也称为标识接口，标识接口中没有任何方法的定义，其作用是告诉 JRE 这些接口的实现类是否具有某个功能，如是否支持克隆、是否支持序列化等。

## 5. 使用序列化和反序列化实现深克隆

如果引用类型里面还包含很多引用类型，或者内层引用类型的类里面又包含引用类型，使用 clone 方法就会很麻烦。这时我们可以用序列化的方式来实现对象的深克隆。

需要拷贝的对象都需要实现 java.io.Serializable 序列化接口。

User 类：

```java
package com.xavier.clone;

import java.io.*;

/**
 * @author Xavier
 */
public class User implements Serializable {

    private static final long serialVersionUID = 872390113109L;

    private String name;

    private int age;

    private Address address;

    public User(String name, int age, Address address) {
        this.name = name;
        this.age = age;
        this.address = address;
    }

    public User() { }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * 深度复制方法,需要对象及对象所有的引用属性都实现序列化
     *
     * @return 复制成功的对象的引用
     */
    public User myClone() {
        User user = null;

        try {
            // 写入字节流
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            oos.close();

            // 分配内存，写入原始对象，生成新对象
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);

            // 返回生成的新对象
            user = (User) ois.readObject();
            ois.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", address=" + address +
                '}';
    }
}

```

Address 类也需要实现 Serializable 接口：

```java
package com.xavier.clone;

import java.io.Serializable;

/**
 * @author Xavier
 */
public class Address implements Serializable {

    private static final long serialVersionUID = 872390113109L;

    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Address(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Address{" +
                "address='" + address + '\'' +
                '}';
    }

}
```

测试一下：

```java
package com.xavier.clone;

public class Test {

    public static void main(String[] args) {
        Address address = new Address("北京");
        User userSource = new User("Li", 20, address);

        User user = userSource.myClone();
        System.out.println(user);
        System.out.println("对象引地址：" + (user == userSource));
        System.out.println("成员对象引用地址：" + (user.getAddress() == userSource.getAddress()));
    }

}

```

输出结果：

```language
User{name='Li', age=20, address=Address{address='北京'}}
对象引地址：false
成员对象引用地址：false
```

基于序列化和反序列化实现的克隆不仅仅是深度克隆，更重要的是通过泛型限定，可以检查出要克隆的对象是否支持序列化，这项检查是编译器完成的，不是在运行时抛出异常，这种是方案明显优于使用 Object 类的 clone 方法克隆对象。让问题在编译的时候暴露出来总是优于把问题留到运行时。

对此，我们可以使用泛型限定，编写一个基于序列化的克隆工具类：

```java
package com.xavier.clone;

import java.io.*;

/**
 * 基于序列化的克隆工具类
 *
 * @author Xavier
 */
public class CloneUtils {

    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T clone(T obj){
        T cloneObj = null;
        try {
            //写入字节流
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream obs = new ObjectOutputStream(out);
            obs.writeObject(obj);
            obs.close();

            //分配内存，写入原始对象，生成新对象
            ByteArrayInputStream ios = new ByteArrayInputStream(out.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(ios);

            //返回生成的新对象
            cloneObj = (T) ois.readObject();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cloneObj;
    }
}
```

如下使用：

```java
User user = CloneUtils.clone(userSource);
```
