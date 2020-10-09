# Java枚举类型（enum）

[TOC]

枚举类型是Java 5中新增特性的一部分，它是一种特殊的数据类型，之所以特殊是因为它既是一种类（class）类型却又比类多了一些特殊的约束。这些约束也造就了枚举类型的简洁性、安全性和便捷性。

枚举表示的类型其取值是必须有限的，也就是说每个值都是可以枚举出来的，比如一周共有七天，从周一到周日，这是可以数出来的。

参考资料：[https://www.cnblogs.com/alter888/p/9163612.html](https://www.cnblogs.com/alter888/p/9163612.html)

JDK中一个最好的枚举应用就是java.util.concurrent.TimeUnit，建议阅读该枚举类的源码，很简单明了。

## 1. 定义枚举类型

创建枚举类型的最主要目的就是为了定义一些枚举常量。枚举的基本定义格式为：

```java
[枚举类型修饰词列表] enum 枚举类型标识符{
    enum01, enum02, ... , enumn;
}
```

定义枚举类需要使用枚举关键字enum。

其中，`[]` 表示枚举类型修饰词列表是可选项，枚举类型修饰词列表是用来说明所定义的枚举类型的属性，可以包含0个、1个或多个枚举类型修饰词，如果有多个，之间用空格隔开。枚举类型修饰词可以是public、protected（当定义在类的内部时才可以使用）、static（当定义在类的内部时才可以使用，并且这时static是冗余的）、private（当定义在类的内部时才可以使用）等，但不能是abstract。

如果枚举类型修饰词含有public并且定义在类的外部或独立成一个Java文件，则要求该枚举定义所在的文件名与枚举类型标识符指定的名称相同，并且以 ".java" 作为后缀。而且在该文件中不能含有其他属性为 public 的类、接口或者枚举。每个Java源程序文件可以含有多个类、接口或枚举，但其中属性为public的只能有0个或1个（定义在内部的除外）。如果枚举类型独立成一个Java文件并且类型修饰词中不包含public，则表明该枚举类型的封装性为默认方式，只能在该枚举所在的包内部使用。

## 2. 枚举常量

如果没有枚举，恐怕下面这种形式是定义常量常见的方式了。

```java
public class Day {

    public static final int MONDAY =0;

    public static final int TUESDAY=1;

    public static final int WEDNESDAY=2;

    public static final int THURSDAY=3;

    public static final int FRIDAY=4;

    public static final int SATURDAY=5;

    public static final int SUNDAY=6;

}
```

上述的常量定义常量的方式称为int枚举模式，这样的定义方式并没有什么错，但它存在许多不足，如在类型安全和使用方便性上并没有多少好处，如果存在定义int值相同的变量，混淆的几率还是很大的，编译器也不会提出任何警告，因此这种方式在枚举出现后并不提倡，现在我们利用枚举类型来重新定义上述的常量，同时也感受一把枚举定义的方式，如下定义周一到周日的常量：

```java
public enum DAY {

    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY;
}
```

这种方式相当简洁，上面的代码定义了一个枚举类型DAY，它包含了七个枚举常量。

在定义枚举类型时我们使用的关键字是enum，与class关键字类似，只不过前者是定义枚举类型，后者是定义类类型。枚举类型Day中分别定义了从周一到周日的值，这里要注意，值一般是大写的字母，多个值之间以逗号分隔（最后一个使用分号结束）。同时我们应该知道的是枚举类型可以像类(class)类型一样，定义为一个单独的文件，当然也可以定义在其他类内部。

定义了枚举常量，我们该如何使用枚举常量呢？非常简单，直接引用即可，如下：

```java
public static void main(String[] args) {
        DAY day = DAY.FRIDAY;
    }
```

上述示例便是枚举类型的最简单的模型了。

## 3. 枚举变量

枚举类型变量简称为枚举变量，其定义格式有两种，分别是：

```java
枚举类型标识符 枚举变量;
枚举类型标识符 枚举变量1, 枚举变量2, 枚举变量3;
```

在上面的定义个数中，第一种格式每次只定义一个枚举变量；第二种格式同时定义多个枚举变量，变量之间通过逗号分隔开。另外，还可以定义**枚举数组变量**，其定义格式与其他类型变量定义格式相同。例如：

```java
DAY[] days;
```

## 4. 访问

对于枚举类型，不能通过new关键字来创建实例对象。可以直接通过枚举类型标识符访问枚举常量。例如：

```java
DAY day = DAY.MONDAY;
```

上面定义了DAY枚举类型变量day，它的值为DAY.MONDAY。对于枚举常量，它有些类似于类的静态成员域。即可以直接通过枚举类型标识符访问枚举常量，而且通过枚举变量访问枚举常量与直接通过枚举类型标识符访问枚举常量效果基本上是一样的。例如：设上面的语句已经定义了枚举变量day，则下面表达式：

```java
boolean result = (day.FRIDAY == DAY.FRIDAY);
```

上面的运算结果总是true。这个例子同时也说明可以通过 "==" 运算符判断两个枚举常量是否相等。这里需要注意的是，上面的表达式并不改变枚举变量day的值，枚举变量仍然表示DAY.MONDAY。

## 5. 枚举类实现原理

我们大概了解了枚举类型的定义与简单使用后，现在有必要来了解一下枚举类型的基本实现原理。实际上在使用关键字enum创建枚举类型并编译后，编译器会为我们生成一个相关的类，这个类继承了Java API中的java.lang.Enum类，也就是说通过关键字enum创建枚举类型在编译后事实上也是一个类类型而且该类继承自java.lang.Enum类。下面我们编译前面定义的DAY.java并查看生成的class文件来验证这个结论：

```language
javac DAY.java

Day.class
```

利用javac编译前面定义的DAY.java文件后生成了Day.class字节码文件，而Day.class就是枚举类型，这也就验证前面所说的使用关键字enum定义枚举类型并编译后，编译器会自动帮助我们生成一个与枚举相关的类。我们再来看看反编译Day.class文件：

```java
// 反编译Day.class
final class Day extends Enum
{
    //编 译器为我们添加的静态的values()方法
    public static Day[] values()
    {
        return (Day[])$VALUES.clone();
    }
    // 编译器为我们添加的静态的valueOf()方法，注意间接调用了Enum类的valueOf方法
    public static Day valueOf(String s)
    {
        return (Day)Enum.valueOf(com/vintage/Day, s);
    }
    // 私有构造函数
    private Day(String s, int i)
    {
        super(s, i);
    }
     // 前面定义的7种枚举实例
    public static final Day MONDAY;
    public static final Day TUESDAY;
    public static final Day WEDNESDAY;
    public static final Day THURSDAY;
    public static final Day FRIDAY;
    public static final Day SATURDAY;
    public static final Day SUNDAY;
    private static final Day $VALUES[];

    static
    {
        //实例化枚举实例
        MONDAY = new Day("MONDAY", 0);
        TUESDAY = new Day("TUESDAY", 1);
        WEDNESDAY = new Day("WEDNESDAY", 2);
        THURSDAY = new Day("THURSDAY", 3);
        FRIDAY = new Day("FRIDAY", 4);
        SATURDAY = new Day("SATURDAY", 5);
        SUNDAY = new Day("SUNDAY", 6);
        $VALUES = (new Day[] {
            MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
        });
    }
}
```

从反编译的代码可以看出编译器确实帮助我们生成了一个Day类(注意该类是final类型的，将无法被继承)，而且该类继承自java.lang.Enum类，该类是一个抽象类(稍后我们会分析该类中的主要方法)，除此之外，编译器还帮助我们生成了7个Day类型的实例对象分别对应枚举中定义的7个日期，这也充分说明了我们前面使用关键字enum定义的Day类型中的每种日期枚举常量也是实实在在的Day类型实例对象，只不过我们赋予它们的含义不一样。注意编译器还为我们生成了两个静态方法，分别是values()和 valueOf()，稍后会分析它们的用法。

到此我们也就明白了，使用关键字enum定义的枚举类型，在编译期后，也将转换成为一个实实在在的类，而在该类中，会存在每个在枚举类型中定义好变量的对应实例对象，如上述的MONDAY枚举类型对应`public static final Day MONDAY`;`，同时编译器会为该类创建两个方法，分别是values()和valueOf()。到此相信我们对枚举的实现原理也比较清晰，下面我们深入了解一下java.lang.Enum类以及其中一些方法的用途。

## 6. Enum的常见方法

Enum是所有 Java 语言枚举类型的公共基本类（注意Enum是抽象类），以下是它的一些方法：

### 6.1. compareTo

Enum抽象类实现了Comparable接口。

方法定义：`public final int compareTo(E o){...}`

方法说明：该方法比较同一种枚举类型对象之间的顺序关系。至于枚举对象之间的顺序关系由什么来决定的，且看下文。

示例：

```java
System.out.println(DAY.MONDAY.compareTo(DAY.TUESDAY));
```

事实上，上述语句的输出结果是-1。提前透露一下，比较的是枚举常量声明的先后顺序。

请注意一下，该方法被修饰为final类型，就说明该方法在子类当中不能被重写。

### 6.2. equals

方法定义：

```java
public final boolean equals(Object other) {
        return this==other;
}
```

是否好奇这和我们平常重写equals方法有些不太一样。这取决于枚举类型这一数据结构的特性。枚举类型的值是有限的，并且我们不能通过new关键字来实例化枚举变量，因此，枚举变量的比较其实就是枚举常量的比较，枚举常量是枚举类型几个不相同常量对象，所以，直接用`==`来判断是否相同即可。

### 6.3. getDeclaringClass

方法定义：`public final Class<E> getDeclaringClass() {}`

方法说明：返回与此枚举常量的枚举类型相对应的 Class 对象。

### 6.4. name

方法定义：

```java
public final String name() {
    return name;
}
```

该方法返回Enum类中的一个final属性name，调用此方法，实际上是返回枚举常量的名称。示例：

```java
System.out.println(DAY.MONDAY.name());   // 输出为：MONDAY
```

### 6.5. ordinal

方法定义：

```java
public final int ordinal() {
        return ordinal;
}
```

方法说明：返回枚举常量的序数（即它在枚举声明中的位置，第一个被声明的枚举常量序数为零，依次递增）。

示例

```java
public enum DAY {

    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY;

    public static void main(String[] args) {
        System.out.println(DAY.MONDAY.ordinal());
        System.out.println(DAY.TUESDAY.ordinal());
        System.out.println(DAY.WEDNESDAY.ordinal());
        System.out.println(DAY.THURSDAY.ordinal());
        System.out.println(DAY.FRIDAY.ordinal());
        System.out.println(DAY.SATURDAY.ordinal());
        System.out.println(DAY.SUNDAY.ordinal());
    }

}

```

输出

```language
0
1
2
3
4
5
6
```

上面说的compareTo方法，在内部就是通过ordinal方法的返回值来比较的。

### 6. 6. toString

方法定义：

```java
public String toString() {
        return name;
}
```

仍然是返回name属性，也就是说，如果我们的枚举类中，没有重写toString方法时，同一个枚举常量的toString方法和name方法的返回值是相同的。

### 6.7. valueOf

方法声明：

```java
public static <T extends Enum<T>> T valueOf(Class<T> enumType, String name) {}
```

方法说明：该方法时一个static方法，返回带指定名称的指定枚举类型的枚举常量。

示例

```java
DAY day = Enum.valueOf(DAY.class, "MONDAY");
System.out.println(day.name());            // output: MONDAY

// 编译器生成的valueOf方法
DAY day = DAY.valueOf("MONDAY");
System.out.println(day.name());
```

## 7. 编译器生成的values方法与valueOf方法

`values()`方法和`valueOf(String name)`方法是编译器生成的static方法。从前面的分析中，在Enum类中虽然没出现`values()`方法，但`valueOf()`方法还是有出现的，只不过编译器生成的`valueOf()`方法需传递一个name参数，而Enum自带的静态方法`valueOf()`则需要传递两个参数，从前面反编译后的代码可以看出，编译器生成的valueOf方法最终还是调用了Enum类的`valueOf`方法，`valueOf`方法参数上文。下面简单介绍`values`方法。

从我们定义的DAY这个枚举类中查看该方法的返回值：

```java
public static DAY[] values(){...}
```

这是一个静态方法，方法的作用就是获取枚举类中的所有变量，并作为数组返回

```java
for (DAY day : DAY.values()) {
    System.out.println(day.name());
}

// output followed
MONDAY
TUESDAY
WEDNESDAY
THURSDAY
FRIDAY
SATURDAY
SUNDAY
```

由于`values()`方法是由编译器插入到枚举类中的static方法，所以如果我们将枚举实例向上转型为Enum，那么`values()`方法将无法被调用，因为Enum类中并没有`values()`方法，`valueOf()`方法也是同样的道理，注意是一个参数的。

## 8. 枚举与Class对象

上述我们提到当枚举实例向上转型为Enum类型后，`values()`方法将会失效，也就无法一次性获取所有枚举实例变量，但是由于Class对象的存在，即使不使用`values()`方法，还是有可能一次获取到所有枚举实例变量的，在Class对象中存在如下方法：

| 返回类型  | 方法名称             | 方法说明                                                     |
| --------- | -------------------- | ------------------------------------------------------------ |
| `T[]`     | `getEnumConstants()` | 返回该枚举类型的所有元素，如果Class对象不是枚举类型，则返回null。 |
| `boolean` | `isEnum()`           | 当且仅当该类声明为源代码中的枚举时返回 true                  |

因此通过getEnumConstants()方法，同样可以轻而易举地获取所有枚举实例变量，下面通过代码来演示：

```java
Class<?> eClass = DAY.class;
if (eClass.isEnum()) {
    DAY[] days = (DAY[]) eClass.getEnumConstants();
    System.out.println(Arrays.toString(days));
}
```

输出结果

```
[MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY]
```

## 9. 枚举与switch

关于枚举与switch是个比较简单的话题，使用switch进行条件判断时，条件参数一般只能是整型，字符型。而枚举型确实也被switch所支持，在java 1.7后switch也对字符串进行了支持。这里我们简单看一下switch与枚举类型的使用：

```java
DAY day = DAY.MONDAY;
switch (day) {
    case MONDAY:
        break;
    case TUESDAY:
        break;
    case WEDNESDAY:
        break;
    case THURSDAY:
        break;
    case FRIDAY:
        break;
    case SATURDAY:
        break;
    case SUNDAY:
        break;
}
```

## 10. 向 enum 类添加方法与自定义构造函数

重新定义一个日期枚举类，带有desc成员变量描述该日期的对于中文描述，同时定义一个getDesc方法，返回中文描述内容，自定义**私有构造函数**，在声明枚举实例时传入对应的中文描述，代码如下：

```java
public enum Day2 {

    MONDAY("星期一"),
    TUESDAY("星期二"),
    WEDNESDAY("星期三"),
    THURSDAY("星期四"),
    FRIDAY("星期五"),
    SATURDAY("星期六"),
    SUNDAY("星期日");        //记住要用分号结束

    private String desc;    //中文描述

    /**
     * 私有构造,防止被外部调用
     */
    private Day2(String desc){
        this.desc=desc;
    }

    /**
     * 定义方法,返回描述,跟常规类的定义没区别
     */
    public String getDesc(){
        return desc;
    }

    public static void main(String[] args){
        for (Day2 day : Day2.values()) {
            System.out.println("name: " + day.name() + ", desc: " + day.getDesc());
        }
    }
}
```

其实，枚举类的构造方法是默认私有的，手动添加 private 修饰符是多次一举，因此，我们在定义枚举类的构造方法是可以不写 private。

可以借鉴 `java.util.concurrent.TImeUnit` 枚举类。

## 11. 枚举与单例模式

单例模式的实现方式有多种，其中，使用枚举类型实现单例模式便是其中一种

## 12. 枚举的常见用法

### 12.1. 枚举常量

这是最常见的用法

```java
public enum Color { RED, GREEN, BLANK, YELLOW }
```

### 12.2. switch

参考上面的标题

### 12.3. 实现接口

```java
public interface Behaviour {
    void print();

    String getInfo();
}

public enum Color implements Behaviour {

    RED("红色", 1),
    GREEN("绿色", 2),
    BLANK("白色", 3),
    YELLO("黄色", 4);

    // 成员变量
    private String name;
    private int index;

    // 构造方法
    Color(String name, int index) {
        this.name = name; this.index = index;
    }

    // 接口方法
    @Override
    public String getInfo() {
        return this.name;
    }
    // 接口方法
    @Override
    public void print() {
        System.out.println(this.index + ":" + this.name);
    }
}
```

### 12.4. 使用接口组织枚举

```java
public interface Food {

    enum Coffee implements Food {
        BLACK_COFFEE,
        DECAF_COFFEE,
        LATTE,
        CAPPUCCINO
    }

    enum Dessert implements Food {
        FRUIT,
        CAKE,
        GELATO
    }

    // 定义 Food 的其他行为
}
```

## 13. 优秀枚举示例

学习别人优秀的代码是一种很好的进步方式。

### 13.1. TimeUnit

`java.util.concurrent.TimeUnit` 是 JDK 提供的一个时间单位枚举类，使用该枚举类，我们可以将时间在不同的时间单位之间轻而易举地进行换算。

学习枚举类型，TimeUnit 是一个非常好的样例。

### 13.2. JdbcType

`org.apache.ibatis.type.JdbcType` 是 mybats JAR 包下的一个枚举类，它定义类 mybatis 中支持的 JdbcType。

源码如下。

```java
package org.apache.ibatis.type;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Clinton Begin
 */
public enum JdbcType {
  /*
   * This is added to enable basic support for the
   * ARRAY data type - but a custom type handler is still required
   */
  ARRAY(Types.ARRAY),
  BIT(Types.BIT),
  TINYINT(Types.TINYINT),
  SMALLINT(Types.SMALLINT),
  INTEGER(Types.INTEGER),
  BIGINT(Types.BIGINT),
  FLOAT(Types.FLOAT),
  REAL(Types.REAL),
  DOUBLE(Types.DOUBLE),
  NUMERIC(Types.NUMERIC),
  DECIMAL(Types.DECIMAL),
  CHAR(Types.CHAR),
  VARCHAR(Types.VARCHAR),
  LONGVARCHAR(Types.LONGVARCHAR),
  DATE(Types.DATE),
  TIME(Types.TIME),
  TIMESTAMP(Types.TIMESTAMP),
  BINARY(Types.BINARY),
  VARBINARY(Types.VARBINARY),
  LONGVARBINARY(Types.LONGVARBINARY),
  NULL(Types.NULL),
  OTHER(Types.OTHER),
  BLOB(Types.BLOB),
  CLOB(Types.CLOB),
  BOOLEAN(Types.BOOLEAN),
  CURSOR(-10), // Oracle
  UNDEFINED(Integer.MIN_VALUE + 1000),
  NVARCHAR(Types.NVARCHAR), // JDK6
  NCHAR(Types.NCHAR), // JDK6
  NCLOB(Types.NCLOB), // JDK6
  STRUCT(Types.STRUCT),
  JAVA_OBJECT(Types.JAVA_OBJECT),
  DISTINCT(Types.DISTINCT),
  REF(Types.REF),
  DATALINK(Types.DATALINK),
  ROWID(Types.ROWID), // JDK6
  LONGNVARCHAR(Types.LONGNVARCHAR), // JDK6
  SQLXML(Types.SQLXML), // JDK6
  DATETIMEOFFSET(-155); // SQL Server 2008

  public final int TYPE_CODE;
  private static Map<Integer,JdbcType> codeLookup = new HashMap<Integer,JdbcType>();

  static {
    for (JdbcType type : JdbcType.values()) {
      codeLookup.put(type.TYPE_CODE, type);
    }
  }

  JdbcType(int code) {
    this.TYPE_CODE = code;
  }

  public static JdbcType forCode(int code)  {
    return codeLookup.get(code);
  }

}
```

想上面的代码，有时候我们需要枚举实例定义属性并可以通过属性反向获取对应的枚举实例。在枚举实例很多的情况下，我们可以使用 HashMap，在枚举实例比较少的情况下，我们也可以使用 switch 来匹配。

枚举实例的属性应当用 `public` 和 `final` 这两个修饰符来修饰，一方面我们可以直接获取枚举对象的属性而不用通过 getter 方法，另一方面 `final` 保护了枚举实例的属性不被修改。
