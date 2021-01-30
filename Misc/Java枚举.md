# Java枚举

JDK版本：1.8

## 定义枚举类型

创建枚举类型的主要目的就是为了定义一些枚举常量。枚举的基本定义格式为：

```language
[枚举类型修饰词列表] enum 枚举类型标识符{
	enum01, enum02, ... , enumn
}
```

使用了关键字 enum。其中，`[]` 表示枚举类型修饰词列表是可选项；枚举类型修饰词列表是用来说明所定义的枚举类型的属性，可以包含0个、1个或多个枚举类型修饰词，如果有多个，之间用空格隔开。枚举类型修饰词可以是public、protected（当定义在类的内部时才可以使用）、static（当定义在类的内部时才可以使用，并且这时static是冗余的）、private（当定义在类的内部时才可以使用）等，但不能是abstract。如果枚举类型修饰词含有 public并且定义在类的外部或独立成一个Java文件，则要求该枚举定义所在的文件名与枚举类型标识符指定的名称相同，并且以 ".java" 作为后缀。而且在该文件中不能含有其他属性为 public 的类、接口或者枚举。每个Java源程序文件可以含有多个类、接口或枚举，但其中属性为public的只能有0个或1个（定义在内部的除外）。如果枚举类型独立成一个Java文件并且类型修饰词中不包含public，则表明该枚举类型的封装性为默认方式（注意不是default），只能在同一个包内部使用。

综述：

- 独立的枚举类型的修饰词：public 或者 没有（没有不同于default）

- 嵌套的枚举类型修饰词：public、protected、private、static（默认含有的）

## 枚举常量

```java
package com.common;

/**
 * created by Vintage
 */

public enum Season {

    春季, 夏季, 秋季, 冬季
}

```

如上，定义了枚举类型Season，它包含了4个**枚举常量**：春季、夏季、秋季和冬季。

## 枚举类型变量

枚举类型变量简称为枚举变量，其定义格式有两种，分别是：

```language
枚举类型标识符 枚举变量;
枚举类型标识符 枚举变量1, 枚举变量2, 枚举变量3;
```

在上面的定义给事中，第一种格式每次只定义一个枚举变量；第二种格式同时定义多个枚举变量，变量之间通过逗号分隔开。另外，还可以定义**枚举数组变量**，其定义格式与其他类型变量定义格式相同。例如：

```java
Season[] s;
```

示例：

```java
package com.common;

/**
 * created by Vintage
 */

public enum Season {

    // 枚举常量
    春季, 夏季, 秋季, 冬季;

    // 变量
    Season 夏季变量;

    // 数组变量
    Season[] 数组;
}

```

## 访问

对于枚举类型，不能通过new运算符来创建实例对象。可以直接通过枚举类型标识符访问枚举常量。例如：

```java
Season s =Season.夏季;
```

定义了Season枚举类型变量s，它的值为Season.夏季。对于枚举常量，它有些类似于类的静态成员域。即可以直接通过枚举类型标识符访问枚举常量，而且通过枚举变量访问枚举常量与直接通过枚举类型标识符访问枚举常量效果基本上是一样的。例如：设上面的语句已经定义了枚举变量s，则下面表达式：

```java
Season s =Season.夏季;
if (s.夏季 == Season.夏季){
    System.out.println("相等");
}
```

的值为true。这个例子同时也说明可以通过 "==" 运算符判断两个枚举常量是否相等。这里需要注意的是，上面的表达式并不改变枚举变量 s 的值。再如：

```language
s == Season.夏季
```

的值就是 false。另外，虽然枚举变量 s 的值为 Season.夏季，但是允许使用表达式 "s.春季"。

## name() 和 toString()

通过枚举常量，可以调用成员方法：

- public String name()

- public String toString()

这两个成员方法的工功能是相同的，都是返回枚举常量所对应的字符串。例如：

```language
s.name();
Season.夏季.toString();
```

均返回字符串 "夏季"。

## values()

通过枚举类型，可以通过调用成员方法 values 获得该枚举类型的所有枚举变量，其调用格式是：

```language
枚举类型标识符.values;
```

其中，枚举类型标识符指定具体的枚举类型。该成员方法返回由该枚举类型所有枚举常量组成的枚举数组。

```java
Season[] sa = Season.values();
for (Season s : sa){
    System.out.println(s.name());
}

```

输出：

```language
春季
夏季
秋季
冬季
```

## 枚举类型在 switch 语句中

```java
Season[] sa = Season.values();
for (Season s : sa) {
    switch (s) {
        case 春季:
            System.out.println("这是春季！");
            break;

        case 夏季:
            System.out.println("这是夏季！");
            break;
    }
}
```

在switch语句中，如果switch表达式的类型是枚举类型，则在作为switch语句各个分支的枚举常量前面不能加运算符 "." 以及枚举变量或枚举类型标识符，而应当直接使用枚举常量标识符。

## 枚举类型的构造方法

枚举类型其实和类还是很相似的，它也可以有自己的构造方法，但是构造方法只能是 private 权限，即便不写权限修饰符也是 private：

```java
package com.common;

/**
 * created by Vintage
 */

public enum Season {

    // 枚举常量
    Spring(1), Summer(2), Autum(3), Winter(4);

    int index;

    Season (int index){
        this.index = index;
    }

    public int getIndex(){
        return index;
    }

    Season getSeasonByIndex(int index){
        for (Season s : Season.values()){
            if (s.index == index){
                return s;
            }
        }
        return null;
    }
}
```

如上所示，我们给了枚举常量一个标志索引，我们可以通过枚举常量来获取索引值，也可以通过索引值来获取枚举常量。也就是说，在声明枚举常量时我们就调用了构造方法并初始化成员变量了。

我们还可以通过 valueOf() 方法来获取枚举常量:

```java
Season s = Season.valueOf("Spring");
System.out.println(s.name());
```

