# 方法引用（Method reference）

在学习Lambda表达式之后，我们通常使用Lambda表达式来创建匿名方法。然而，有时候我们仅仅是调用了一个已存在的方法。如下：

```java
Arrays.sort(stringsArray, (s1,s2)->s1.compareToIgnoreCase(s2));
```

 在Java8中，我们可以直接通过方法引用来**简写**Lambda表达式中已经存在的方法：

```java
Arrays.sort(stringsArray, String::compareToIgnoreCase);
```

这种特性就叫做方法引用(Method Reference)。

### 什么是方法引用

   **方法引用**是用来直接访问类或者实例的已经存在的方法或者构造方法。方法引用提供了一种引用而不执行方法的方式，它需要由兼容的函数式接口构成的目标类型上下文。计算时，方法引用会创建函数式接口的一个实例。

当Lambda表达式中只是执行一个方法调用时，不用Lambda表达式，直接通过方法引用的形式可读性更高一些。方法引用是一种更简洁易懂的Lambda表达式。

方法引用，就是一个Lambda表达式。在Java 8中，我们会使用Lambda表达式创建匿名方法，但是有时候，我们的Lambda表达式可能仅仅想调用一个已存在的方法，并不是想新创建一个匿名方法，对于这种情况，通过一个方法名字来引用这个已存在的方法会更加清晰，Java 8的方法引用允许我们这样做。方法引用是一个更加紧凑，易读的Lambda表达式，注意方法引用是一个Lambda表达式，其中方法引用的操作符是双冒号`::`。

方法引用的使用方法如下：

```java
public class Java8Test {

    interface MathOperation {
        int operate(int a, int b);
    }

    public static void main(String[] args) {

        // original
        MathOperation operation1 = new MathOperation() {
            @Override
            public int operate(int a, int b) {
                return Math.max(a, b);
            }
        };

        // lambda expression
        MathOperation operation2 = (x, y) -> {return Math.max(x, y);};

        // method reference
        MathOperation operation3 = Math::max;
        System.out.println(operation3.operate(3, 4));
    }

}
```

## 四种方法引用类型

方法引用的标准形式是：`类名::方法名`。（**注意：只需要写方法名，不需要写括号**）

有以下四种形式的方法引用：

| Type                             | Example                        |
| -------------------------------- | ------------------------------ |
| 引用静态方法                     | className::staticMethodName    |
| 引用某个对象的实例方法           | ObjectName::instanceMethodName |
| 引用某个类型的任意对象的实例方法 | ContainingType::methodName     |
| 引用构造方法                     | className::new                 |

### 静态方法引用

**组成语法格式：className::staticMethodName**

等同于把Lambda表达式的参数直接当成staticMethod的参数来调用。比如：

-   `System.out::println`等同于`x->System.out.println(x)`
-   ``Math::max`等同于`(x, y)->Math.max(x,y)`。
-   `String:valueOf`等价于`(s) -> {return String.valueOf(s)}`

### 实例对象的实例方法

这种语法与用于静态方法的语法类似，只不过这里使用对象引用而不是类名。实例方法引用又分为三种类型：

#### 1. 实例上的方法引用

**组成语法格式：objectName::instanceMethodName**

等同于把Lambda表达式的参数直接当成该对象的instanceMethod的参数来调用。比如：

```java
interface MathOperation {
        int operate(String b);
    }

public static void main(String[] args) {
    String a = "123";
    MathOperation operation = a::compareTo;
}
```

#### 2. 超类上的实例方法引用

**组成语法格式：super::instanceMethodName**

方法的名称由methodName指定，通过使用**super**，可以引用方法的超类版本。

还可以捕获this指针，`this :: equals` 等价于Lambda表达式`x -> this.equals(x);`

#### 3. 类型上的实例方法引用

**组成语法格式：className::instanceMethodName**

等同于把Lambda表达式的第一个参数当成instanceMethod的所属对象，其他剩余参数当成该方法的参数。比如`String::toLowerCase`等同于`x->x.toLowerCase()`，在这里，String就是`x`这个对象的类型。

可以这么理解，前两种是将传入对象当参数执行方法，后一种是调用传入对象的方法。

构造器引用语法如下：`ClassName::new`，把lambda表达式的参数当成ClassName构造器的参数 。例如`BigDecimal::new`等同于`x->new BigDecimal(x)`。

### 构造方法引用

**组成语法格式：Class::new**

构造函数本质上是静态方法，只是方法名字比较特殊，使用的是**new**关键字。

例如，`String::new`，等价于Lambda表达式`() -> new String();` 