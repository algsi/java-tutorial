# <font color=red>Java中的final关键字</font>

### <font color=red>final关键字的用法</font>

在Java中，final关键字可以用来修饰类、方法和变量（包括成员变量和局部变量）。

- 修饰类

当用final修饰一个类时，表明这个类不能被继承。也就是说，如果一个类你永远不会让他被继承，就可以用final进行修饰。final类中的成员变量可以根据需要设为final，但是要注意final类中的所有成员方法都会被隐式地指定为final方法。

- 修饰方法

下面这段话摘自《Java编程思想》第四版第143页：

> 使用final方法的原因有两个。第一个原因是把方法锁定，以防任何继承类修改它的含义；第二个原因是效率。在早期的Java实现版本中，会将final方法转为内嵌调用。但是如果方法过于庞大，可能看不到内嵌调用带来的任何性能提升。在最近的Java版本中，不需要使用final方法进行这些优化了。

因此，如果只有在想明确禁止该方法在子类中被覆盖的情况下才将方法设置为final的。

<font color="blue">注：类的private方法会隐式地被指定为final方法。为什么呢？因为private方法不能被覆盖，所有再添加final关键字就显得冗余。</font>

- 修饰变量

修饰变量是final用得最多的地方，也是接下来要重点阐述的内容。首先了解一下final变量的基本语法：

<font color=blue>对于一个final变量，如果是基本数据类型的变量，则其数值一旦在初始化之后便不能更改；如果是引用类型的变量，则在对其初始化之后便不能再让其指向另一个对象。</font>

那么final变量和普通变量到底有何区别呢？下面请看一个例子：

```java
public class Test {

	public static void main(String[] args) {
				String a = "Hello world";
        final String b = "Hello ";
        String c = "Hello ";
        
        String d = b + "world";
        String e = c + "world";
        String f="Hello "+"world";
        
        System.out.println((a == d));
        System.out.println((a == e));
        System.out.println((a == f));
	}
}

```

运行结果：
```
true
false
true
```

这个程序的运行结果为什么第一个和第三个比较结果为true，而第二个比较结果为fasle。这里面就是final变量和普通变量的区别了，当final变量是基本数据类型以及String类型时，如果在编译期间能知道它的确切值，则编译器会把它当做编译期常量使用。也就是说在用到该final变量的地方，相当于直接访问的这个常量，不需要在运行时确定，即字符串d和f的构造方式是一模一样的。这种和C语言中的宏替换有点像。因此在上面的一段代码中，由于变量b被final修饰，因此会被当做编译器常量，所以在使用到b的地方会直接将变量b替换为它的 值。而对于变量c的访问却需要在运行时去寻找进行，想必其中的区别大家应该明白了。<font color=blue>不过要注意，只有在编译期间能确切知道final变量值的情况下，编译器才会进行这样的优化</font>。比如下面的这段代码就不会进行优化：

```java
public class Test {
    public static void main(String[] args)  {
        String a = "Hello ";
        final String b = getHello();
        String c = b + "world";
        System.out.println((a == c));

    }

    public static String getHello() {
        return "Hello ";
    }
}
```

这段代码的输出结果为false。

在上面提到被final修饰的引用变量一旦初始化赋值之后就不能再指向其他的对象，那么该引用变量指向的对象的内容可变吗？看下面这个例子：

```java
public class Test {

	public static void main(String[] args)  {
		final MyClass myclass=new MyClass();
		myclass.i+=1;
		System.out.println(myclass.i);
    }
}

class MyClass {
    public int i = 0;
}
```

这段代码可以顺利编译通过并且有输出结果，输出结果为1。这说明引用变量被final修饰之后，虽然不能再指向其他对象，但是它指向的对象的内容是可变的


