# Java 8函数式接口

## 1. 什么是函数式接口

Java 8 引入了Lambda表达式，而Lambda表达式的本质就是一个简单接口的实现。所谓的函数式接口，当然首先是一个接口，然后就是在这个接口里面**只能有一个抽象方法**。

这种类型的接口也称为SAM接口，即Single Abstract Method interfaces。

## 2. 函数式接口的用途

它们主要用在Lambda表达式和方法引用（实际上也可认为是Lambda表达式）上。

如定义了一个函数式接口如下：

```java
@FunctionalInterface
interface GreetingService 
{
    void sayMessage(String message);
}
```

那么就可以使用Lambda表达式来表示该接口的一个实现

```java
GreetingService greetService1 = message -> System.out.println("Hello " + message);
```

在JAVA 8 之前，大多数可能就是用匿名类实现的。

## 3. 关于@FunctionalInterface注解

Java 8为函数式接口引入了一个新注解`@FunctionalInterface`，该注解位于`java.lang`包下面，主要用于**编译级错误检查**，加上该注解，当你写的接口不符合函数式接口定义的时候，编译器会报错。

正确例子，没有报错：

```java
@FunctionalInterface
public interface GreetingService {

    void sayMessage(String message);
}
```

错误例子，接口中包含了两个抽象方法，违反了函数式接口的定义，IDE报错提示其不是函数式接口：

```
Multiple non-overriding abstract methods found in interface net.jcip.examples.GreetingService
```

提醒：加不加`@FunctionalInterface`对于接口是不是函数式接口没有影响，该注解知识提醒编译器去检查该接口是否仅包含一个抽象方法

## 4. 函数式接口里允许定义默认方法

函数式接口里是可以包含默认方法，因为默认方法不是抽象方法，其有一个默认实现，所以是符合函数式接口的定义的；

如下代码不会报错：

```java
@FunctionalInterface
interface GreetingService
{
    void sayMessage(String message);

    default void doSomeMoreWork1()
    {
        // Method body
    }

    default void doSomeMoreWork2()
    {
        // Method body
    }
}
```

## 5. 函数式接口里允许定义静态方法

函数式接口里是可以包含静态方法，因为静态方法不能是抽象方法，是一个已经实现了的方法，所以是符合函数式接口的定义的；

如下代码不会报错：

```java
@FunctionalInterface
interface GreetingService 
{
    void sayMessage(String message);
    static void printHello(){
        System.out.println("Hello");
    }
}
```

## 6. 函数式接口里允许定义java.lang.Object里的public方法

函数式接口里是可以包含Object里的public方法，这些方法对于函数式接口来说，不被当成是抽象方法（虽然它们是抽象方法）；因为任何一个函数式接口的实现，默认都继承了Object类，包含了来自java.lang.Object里对这些抽象方法的实现；

如下代码不会报错：

```java
@FunctionalInterface
interface GreetingService  
{
    void sayMessage(String message);

    @Override
    boolean equals(Object obj);
}
```

## 7. JDK中的函数式接口举例

```
java.lang.Runnable,

java.awt.event.ActionListener, 

java.util.Comparator,

java.util.concurrent.Callable

java.util.function包下的接口，如Consumer、Predicate、Supplier等
```



