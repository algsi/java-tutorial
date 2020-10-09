# Java 8 新特性：Lambda表达式

[TOC]

Lambda 表达式，也可称为闭包，它是推动 Java 8 发布的最重要新特性。

行为参数化。简单的说就是将方法的逻辑以参数的形式传递到方法中，方法主体仅包含模板类通用代码，而一些会随着业务场景而变化的逻辑则以参数的形式传递到方法之中，采用行为参数化可以让程序更加的通用，以应对频繁变更的需求。

使用 Lambda 表达式可以使代码变的更加简洁紧凑。 

## 语法

lambda 表达式的语法格式如下：

```
(parameters) -> expression
或
(parameters) -> {statements;}
```

左侧：指定了Lambda表达式所需要的所有参数
右侧：指定了Lambda体，即Lambda表达式所要执行的功能。

以下是lambda表达式的重要特征:

-   **可选类型声明：**不需要声明参数类型，编译器会根据参数、返回类型、异常类型（如果存在）等因素做正确的判定。
-   **可选的参数圆括号：**一个参数无需定义圆括号，但多个参数需要定义圆括号。
-   **可选的大括号：**如果主体包含了一个语句，就不需要使用大括号。
-   **可选的返回关键字：**如果主体只有一个表达式返回值则编译器会自动返回值，大括号需要指定明表达式返回了一个数值。

上面要注意expression和statement的区别，如果这点不注意的话，编译就会报语法错误。

下面是一个使用expression的示例（因为`a + b`是一个表达式，所以不能使用大括号）：

```java
(int a, int b) -> a + b;
```

下面是一个使用statement的示例（return语句是一个语句，必须使用）

```java
(int a, int b) -> {return a + b;};
```

如果圆括号中什么都没有，则表示该Lambda不接受任何参数：

```java
() -> {return 1 + 1;}
```

## Lambda表达式示例

Lambda 表达式的简单例子:

```java
// 1. 不需要参数,返回值为 5  
() -> 5  
    
// 2. 接收一个参数(数字类型),返回其2倍的值  
x -> 2 * x  
  
// 3. 接受2个参数(数字),并返回他们的差值  
(x, y) -> x – y  
  
// 4. 接收2个int型整数,返回他们的和  
(int x, int y) -> x + y  
  
// 5. 接受一个 string 对象,并在控制台打印,不返回任何值(看起来像是返回void)  
(String s) -> System.out.print(s)
```

在 Java8Tester.java 文件输入以下代码：

```java
public class Java8Test {

    interface MathOperation {
        int operation(int a, int b);
    }

    private int operate(int a, int b, MathOperation mathOperation) {
        return mathOperation.operation(a, b);
    }

    public static void main(String[] args) {

        Java8Test tester = new Java8Test();

        // with type declaration
        MathOperation addition = (int a, int b) -> a + b;
        // without type declaration
        MathOperation subtraction = (a, b) -> a - b;
        // brace with return statement
        MathOperation multiplication = (int a, int b) -> {return a * b;};

        MathOperation division = new MathOperation() {
            @Override
            public int operation(int a, int b) {
                return a / b;
            }
        };

        System.out.println("10 + 5 = " + tester.operate(10, 5, addition));
        System.out.println("10 - 5 = " + tester.operate(10, 5, subtraction));
        System.out.println("10 x 5 = " + tester.operate(10, 5, multiplication));
        System.out.println("10 / 5 = " + tester.operate(10, 5, division));

    }

}

```

在上面的示例中，我们最后的division对象的创建实际上使用了匿名方法来生成接口对象，而其他的对象的创建使用的是Lambda表达式，从这可以窥探出Lambda表达式实际就是定义了一个行内执行的方法类型接口。

使用 Lambda 表达式需要注意以下两点：

-   Lambda 表达式主要用来定义行内执行的方法类型接口，例如，一个简单方法接口。在上面例子中，我们使用各种类型的Lambda表达式来定义MathOperation接口的方法。
-   Lambda 表达式免去了使用匿名方法的麻烦，并且给予Java简单但是强大的函数化的编程能力。

## 变量作用域

lambda 表达式只能引用标记了 final 的外层局部变量，这就是说不能在 lambda 内部修改定义在域外的局部变量，否则会编译错误。

在 Java8Tester.java 文件输入以下代码：

```java
package com.vintage;

public class Java8Test {

    private static final String salutation = "Hello!";

    interface GreetingService {
        void sayMessage(String message);
    }

    public static void main(String[] args) {
        GreetingService service = message -> System.out.println(salutation + message);
        service.sayMessage(" World");
    }

}

```

我们也可以直接在 lambda 表达式中访问外层的局部变量：

```java
public class Java8Tester {
    public static void main(String args[]) {
        final int num = 1;
        Converter<Integer, String> s = (param) -> System.out.println(String.valueOf(param + num));
        s.convert(2);  // 输出结果为 3
        
    }
 
    public interface Converter<T1, T2> {
        void convert(int i);
    }
}
```

lambda 表达式的局部变量可以不用声明为 final，但是必须不可被后面的代码修改（即隐性的具有 final 的语义）

```java
int num = 1;  
Converter<Integer, String> s = (param) -> System.out.println(String.valueOf(param + num));
s.convert(2);
num = 5;  
//报错信息：Local variable num defined in an enclosing scope must be final or effectively 
 final
```

为什么要限制我们直接使用外部的局部变量呢？主要原因在于内存模型，我们都知道实例变量在堆上分配的，而局部变量在栈上进行分配，lambda 表达式运行在一个独立的线程中，了解 JVM 的同学应该都知道栈内存是线程私有的，所以局部变量也属于线程私有，如果肆意的允许 lambda 表达式引用局部变量，可能会存在局部变量以及所属的线程被回收，而 lambda 表达式所在的线程却不知情，这个时候去访问就会出现错误，之所以允许引用事实上的 final（没有被声明为 final，但是实际中不存在更改变量值的逻辑），是因为对于该变量操作的是变量副本，因为约定了变量值不会被更改，所以这份副本始终有效。这一限制可能会让刚刚开始接触函数式编程的同学不太适应，需要慢慢的转变思维方式。

实际上在 java 8th 之前，我们在方法中使用内部类时就已经遇到了这样的限制，因为生命周期的限制 JVM 采用复制的策略将局部变量复制一份到内部类中，但是这样会带来多个线程中数据不一致的问题，于是衍生了禁止修改内部类引用的外部局部变量这一简单、粗暴的策略，只不过在 8th 之前必须要求这部分变量采用 final 修饰，但是 8th 开始放宽了这一限制，只要求所引用变量是 “事实上” 的 final 类型即可。

所以说，Lambda表达式也需要看清楚场景再用，不可在Lambda表达式中修改外部的局部变量。

在 Lambda 表达式当中不允许声明一个与局部变量同名的参数或者局部变量。

```java
String first = "";  
Comparator<String> comparator = (first, second) -> Integer.compare(first.length(), second.length());  //编译会出错 
```

## Lambda表达式实战

### 实战1：线程

```java
public class Test {
 
    public static void main(String[] args) {
 
        // Java8之前：
        new Thread(new Runnable() {
            public void run() {
                System.out.println("hello world");
            }
        }).start();
 
        // Java8方式：
        new Thread(() -> System.out.println("hello world")).start();
    }
}
```

### 实战2：遍历集合元素

```java
public class Test2 {
 
    public static void main(String[] args) {
        // Java8之前：
        List<String> list1 = Arrays.asList("a", "b", "c", "d");
        for (String str : list1) {
            System.out.println(str);
        }
 
        // Java 8之后：
        List list2 = Arrays.asList("a", "b", "c", "d");
        list2.forEach(n -> System.out.println(n));
 
        // 使用Java 8的方法引用更方便，方法引用由::双冒号操作符标示，
        list2.forEach(System.out::println);
 
    }
}
```

### 实战3：map函数

map函数可以说是函数式编程里最重要的一个方法了。map的作用是将一个对象变换为另外一个，也就是一对一的输出。

```java
import java.util.Arrays;
import java.util.List;
 
public class Test3 {
 
    public static void main(String[] args) {
        map();
    }
 
    public static void map() {
        List<Double> cost = Arrays.asList(10.0, 20.0, 30.0);
        cost.stream().map(x -> x + x * 0.05).forEach(x -> System.out.println(x));
    }
}
```

### 实战4：reduce函数

map的作用是将一个对象变为另外一个，而reduce实现的则是将所有值合并为一个，简而言之是多对一的输出。

```java
import java.util.Arrays;
import java.util.List;
 
public class Test4 {
 
    public static void main(String[] args) {
        mapReduce();
    }
 
    public static void mapReduce() {
        List<Double> cost = Arrays.asList(10.0, 20.0, 30.0);
        double allCost = cost.stream().map(x -> x + x * 0.05).reduce((sum, x) -> sum + x).get();
        System.out.println(allCost);
    }
}
```

### 实战5：过滤

```java
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
 
public class Test5 {
 
    public static void main(String[] args) {
        filter();
    }
    public static void filter() {
        List<Double> cost = Arrays.asList(10.0, 20.0, 30.0, 40.0);
        List<Double> filteredCost = cost.stream().filter(x -> x > 25.0).collect(Collectors.toList());
        filteredCost.forEach(x -> System.out.println(x));
    }
}
```

### 实战6：predicate过滤

```java
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
 
public class Test6 {
 
    public static void filter(List<String> languages, Predicate<String> condition) {
        languages.stream().filter(x -> condition.test(x)).forEach(x -> System.out.println(x + " "));
    }
 
    public static void main(String[] args) {
        List<String> languages = Arrays.asList("Java", "Python", "scala", "Shell", "R");
        System.out.println("Language starts with J: ");
        filter(languages, x -> x.startsWith("J"));
        System.out.println("\nLanguage ends with a: ");
        filter(languages, x -> x.endsWith("a"));
        System.out.println("\nAll languages: ");
        filter(languages, x -> true);
        System.out.println("\nNo languages: ");
        filter(languages, x -> false);
        System.out.println("\nLanguage length bigger three: ");
        filter(languages, x -> x.length() > 4);
    }
 
}
```

