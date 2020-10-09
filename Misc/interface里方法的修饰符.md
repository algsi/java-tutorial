# interface里方法的修饰符

## JDK版本：1.8

#### 默认的修饰符（public & abstract）

接口中默认的方法修饰符为：public 和 abstract，即公开的抽象的方法，如果你在接口的方法中加上这两个修饰符，在IDEA中就会提示你修饰符是redundant（冗余的、累赘的）。

这种默认修饰符的方法，在接口的实现类中必须要实现这种方法。

#### default

1.8版本的JDK中为接口中的方法新增了default修饰符，使用了default修饰的方法不是抽象的也不能是静态的（所以不能使用abstract和static修饰），需要给出方法的方法体。

由于使用default修饰的方法不是抽象方法，所以接口实现类中可以不实现该方法，但是也可以实现该方法，实现该方法时该方法会变成公开的，即被 public 修饰。


#### static

使用static修饰的方法，表示该方法是属于接口的公开静态方法（public static），从而也不能是抽象方法，要给出方法体，而且，由于该方法被static修饰，该方法也不能被实现类去实现。

#### 关于接口变量

接口中的变量只能是公开的静态常量（public static final），即便是不加上这三个修饰符，也是公开的静态常量。

#### 示例

接口

```java
package com.common;

/**
 * created by Vintage
 * JDK version: 1.8
 */
public interface InterfaceModifier {
    
    public static final String str = "Hello World!";

    // public and abstract
    void m1();

    // public, not abstract, needed a body
    static void m2(){
        System.out.println("m2 method");
    }

    // not abstract, needed a body
    default void m3(){
        System.out.println("m3 method");
    }

}

```

实现类

```java
package com.common;

/**
 * created by Vintage
 */
public class InterfaceSubclass implements InterfaceModifier{


    @Override
    public void m1() {
		System.out.println(str);
    }

    // Not necessary
    @Override
    public void m3() {
    }
}
```