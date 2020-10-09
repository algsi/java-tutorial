# Lambda表达式的序列化与强制类型转换

## 引入问题

JDK 8 java.util.Map 接口中有这么一段代码：

```java
public interface Map<K,V> {
    // omit...
    interface Entry<K,V> {
        // omit...
        public static <K extends Comparable<? super K>, V> Comparator<Map.Entry<K,V>> comparingByKey() {
            return (Comparator<Map.Entry<K, V>> & Serializable)
                (c1, c2) -> c1.getKey().compareTo(c2.getKey());
            }
    }
}
```

我们着重关注 comparingByKey 这个方法。我们知道下面这句是lambda表达式，它实现的是 java.util.Comparator 接口：

```java
(c1, c2) -> c1.getKey().compareTo(c2.getKey())
```

那么这句呢？

```java
(Comparator<Map.Entry<K, V>> & Serializable)
```

我们知道 `&` 是按位与运算符，但这里是两个数据类型进行按位与操作，这还是第一次见。

实际上，这是强制类型转换，转换成可序列化的 `Comparator<Map.Entry<K, V>>` 对象。`&` 的意思是且的意思。

这种强制类型转换可以将一个对象直接转换为 Serializable 对象。

## 实验

我们可以自己来做个小实验。

```java
public class Test {

    public static String dataFile = "/Users/xxx/Desktop/object.out";

    interface Function {
        void func(String s);
    }

    public static Function getFunction() {
        return (Function & Serializable) (s -> System.out.println(s));
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Function func = getFunction();
        ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(dataFile)));
        out.writeObject(func);
        System.out.println("output object finished.");
        out.close();

        ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(dataFile)));
        Function funcObject = (Function) in.readObject();
        funcObject.func("Hello World!");
        in.close();
    }
}
```

## 分析

首先，这是 Java 语法的支持，是语法的问题，我们现在关注一下语法。

强制转换表达式在运行时将一个数值类型的值转换为另一个数值类型的类似值；或者在编译时确认表达式的类型是布尔型的；或者在运行时检查引用值引用的对象的类型与指定的引用类型或引用类型列表兼容。

Java语言对类型转换表达式是按照如下方式定义的：

```java
CastExpression:
    ( PrimitiveType ) UnaryExpression
    ( ReferenceType {AdditionalBound} ) UnaryExpressionNotPlusMinus
    ( ReferenceType {AdditionalBound} ) LambdaExpression
```

其中 AdditionalBound 的定义如下：

```java
AdditionalBound:
    & InterfaceType
```

综上，我们知道强制类型转换表达式支持 lambda 表达式，对 Lambda 表达式的强制类型转换需要符合如下的语法：`( ReferenceType & InterfaceType ) LambdaExpression`，也就是说 Lambda 表达式被转化为引用类型和接口类型两种类型。

如果强制类型表达式包含一个类型列表，即一个 ReferenceType 后跟一个或多个 AdditionalBound，则必须满足下面所有条件，否则将发生编译时错误：

- ReferenceType必须要是引用类型或者接口类型
- 所有的数据类型进行泛型类型消除后，必须两两不同
- 没有两个列出的类型可以是同一通用接口的不同参数化的子类型

下面我们再给出两个例子结尾：

```java
Runnable r = (Runnable & Serializable)() -> System.out.println("Serializable!");
```

## 参考资料

[how-to-serialize-a-lambda](https://stackoverflow.com/questions/22807912/how-to-serialize-a-lambda)

[jls-15.16 Cast Expressions](https://docs.oracle.com/javase/specs/jls/se8/html/jls-15.html#jls-15.16)