# 深入剖析Java中的装箱和拆箱

**首先声明JDK的版本：1.8.0_171**

## 一、什么是装箱？什么是拆箱？

Java为每种基本数据类型都提供了对应的包装器类型，至于为什么会为每种基本数据类型提供包装器类型在此不进行阐述，有兴趣的朋友可以查阅相关资料。在Java SE5之前，如果要生成一个数值为10的Integer对象，必须这样进行：

```java
Integer i = new Integer(10);
```

而在从Java SE5开始就提供了自动装箱的特性，如果要生成一个数值为10的Integer对象，只需要这样就可以了：

```java
Integer i = 10;
```

 这个过程中会自动根据数值创建对应的 Integer对象，这就是装箱。那什么是拆箱呢？顾名思义，跟装箱对应，就是自动将包装器类型转换为基本数据类型：

```java
Integer i = 10;  //装箱
int n = i;   //拆箱
```

简单一点说，装箱就是  自动将基本数据类型转换为包装器类型；拆箱就是  自动将包装器类型转换为基本数据类型。

下表是基本数据类型对应的包装器类型：

| 基本数据类型 | 	封装的基本数据类型 |
|--------|--------|
| int（4字节） | 	Integer |
| byte（1字节） | Byte |
| short（2字节） | Short |
| long（8字节） | Long |
| float（4字节） | Float |
| double（8字节） | Double |
| char（2字节） | Character |
| boolean（未定） | Boolean |

## 二、装箱和拆箱是如何实现的

上面的一小节了解装箱的基本概念之后，这一小节来了解一下装箱和拆箱是如何实现的。我们就以Interger类为例，下面看一段代码：

```java
public class Main {
    public static void main(String[] args) {
        Integer i = 10;
        int n = i;
    }
}
```

我们可以使用反编译工具来反编译字节码文件，从反编译得到的字节码内容可以看出来，在装箱的时候自动调用的是 Integer 的 valueOf(int) 方法。而在拆箱的时候自动调用的是 Integer 的 intValue 方法。

其他的也类似，比如Double、Character，不相信的朋友可以自己手动尝试一下。

因此可以用一句话总结装箱和拆箱的实现过程：

**装箱过程是通过调用包装器的valueOf方法实现的，而拆箱过程是通过调用包装器的 xxxValue方法实现的。（xxx代表对应的基本数据类型）。**

## 三、面试中相关的问题

虽然大多数人对装箱和拆箱的概念都清楚，但是在面试和笔试中遇到了与装箱和拆箱的问题却不一定会答得上来。下面列举一些常见的与装箱/拆箱有关的面试题。

#### 1.下面这段代码的输出结果是什么？

```java
public class Main {
    public static void main(String[] args) {
        Integer i1 = 100;
        Integer i2 = 100;
        Integer i3 = 200;
        Integer i4 = 200;
        System.out.println(i1==i2);
        System.out.println(i3==i4);
    }
}
```

也许有些朋友会说都会输出false，或者也有朋友会说都会输出true。但是事实上输出结果是：

```
true
false
```

是不是有点惊奇，为什么会出现这样的结果？输出结果表明i1和i2指向的是同一个对象，而i3和i4指向的是不同的对象。此时只需一看源码便知究竟，下面这段代码是Integer的valueOf方法的具体实现（当然 valueOf 有个重载的方法，传入的是String参数，我们这里讨论的是形参为 int 的 valueOf）：

```java
/**
 * Returns an {@code Integer} instance representing the specified
 * {@code int} value.  If a new {@code Integer} instance is not
 * required, this method should generally be used in preference to
 * the constructor {@link #Integer(int)}, as this method is likely
 * to yield significantly better space and time performance by
 * caching frequently requested values.
 *
 * This method will always cache values in the range -128 to 127,
 * inclusive, and may cache other values outside of this range.
 *
 * @param  i an {@code int} value.
 * @return an {@code Integer} instance representing {@code i}.
 * @since  1.5
 */
public static Integer valueOf(int i) {
    if (i >= IntegerCache.low && i <= IntegerCache.high)
        return IntegerCache.cache[i + (-IntegerCache.low)];
    return new Integer(i);
}
```

注释也是方法很好的描述，从注释里面我们知道，返回一个 Integer 实例来替代传入的参数，如果我们不要求创建一个新的实例的话（Integer），我们最好使用这个方法来替代构造方法，因为这个的缓存机制在时间和空间上有更好的性能。

其次，注释告诉我们，此方法将始终缓存-128到127范围内的值，两侧都是闭区间，并可以缓存此范围之外的其他值。

再看看方法体，如果形参在一定范围内，该方法似乎会返回缓存中维护的实例对象，否则将新建一个实例对象。

再看看中IntegerCache类的实现，这是 Integer 类中的一个嵌套类：

```java
private static class IntegerCache {
    static final int low = -128;
    static final int high;
    static final Integer cache[];

    static {
        // high value may be configured by property
        int h = 127;
        String integerCacheHighPropValue =
            sun.misc.VM.getSavedProperty("java.lang.Integer.IntegerCache.high");
        if (integerCacheHighPropValue != null) {
            try {
                int i = parseInt(integerCacheHighPropValue);
                i = Math.max(i, 127);
                // Maximum array size is Integer.MAX_VALUE
                h = Math.min(i, Integer.MAX_VALUE - (-low) -1);
            } catch( NumberFormatException nfe) {
                // If the property cannot be parsed into an int, ignore it.
            }
        }
        high = h;

        cache = new Integer[(high - low) + 1];
        int j = low;
        for(int k = 0; k < cache.length; k++)
            cache[k] = new Integer(j++);

        // range [-128, 127] must be interned (JLS7 5.1.7)
        assert IntegerCache.high >= 127;
    }

    private IntegerCache() {}
}
```

> The cache is initialized on first usage

该类中维护着一个实例的缓存数组，实例数组中的内容是从 -128 到 127 的Integer的实例（如果我们没有自定义维护的上限的话）。因此，在通过valueOf方法创建Integer对象的时候，如果数值在[-128,127]之间，便返回指向IntegerCache.cache中已经存在的对象的引用；否则创建一个新的Integer对象。（**这也涉及到一些常量池的问题，有兴趣的话可以自己去查一查**）。

上面的代码中i1和i2的数值为100，因此会直接从cache中取已经存在的对象，所以i1和i2指向的是同一个对象，而i3和i4则是分别指向不同的对象。

#### 2.下面这段代码的输出结果是什么？

```java
public class Main {
    public static void main(String[] args) {    
        Double i1 = 100.0;
        Double i2 = 100.0;
        Double i3 = 200.0;
        Double i4 = 200.0; 
        System.out.println(i1==i2);
        System.out.println(i3==i4);
    }
}
```

也许有的朋友会认为跟上面一道题目的输出结果相同，但是事实上却不是。实际输出结果为：

```language
false
false
```

至于具体为什么，读者可以去查看Double类的valueOf的实现。如下：

```java
public static Double valueOf(double d) {
    return new Double(d);
}
```

在这里只解释一下为什么Double类的valueOf方法会采用与Integer类的valueOf方法不同的实现。很简单：在某个范围内的整型数值的个数是有限的，而浮点数却不是，所以，如果要在常量池中维护浮点数常量，我们无法维护某个范围的所有浮点数，哪怕是一个很小的范围。现在可以联想到了，Float也是一样的。

注意，Integer、Short、Byte、Character、Long这几个类的valueOf方法的实现是类似的。　Double、Float的valueOf方法的实现是类似的，也就是说 Double、Float 并没有使用常量池技术，而其他封装类使用到了。

#### 3.下面这段代码输出结果是什么：

```java
public class Main {
    public static void main(String[] args) {
        Boolean i1 = false;
        Boolean i2 = false;
        Boolean i3 = true;
        Boolean i4 = true;
        System.out.println(i1==i2);
        System.out.println(i3==i4);
    }
}
```

输出结果是：

```language
true
true
```

至于为什么是这个结果，同样地，看了Boolean类的源码也会一目了然。下面是Boolean的valueOf方法的具体实现：

```java
public static Boolean valueOf(boolean b) {
    return (b ? TRUE : FALSE);
}
```

而其中的 TRUE 和FALSE又是什么呢？在Boolean中定义了2个静态成员属性：

```language
public static final Boolean TRUE = new Boolean(true);
/** 
* The <code>Boolean</code> object corresponding to the primitive 
* value <code>false</code>. 
*/
public static final Boolean FALSE = new Boolean(false);
```

至此，大家应该明白了为何上面输出的结果都是true了。

#### 4.谈谈Integer i = new Integer(xxx)和Integer i = xxx;这两种方式的区别。

当然，这个题目属于比较宽泛类型的。但是要点一定要答上，我总结一下主要有以下这两点区别：

1）第一种方式不会触发自动装箱的过程；而第二种方式会触发；

2）在执行效率和资源占用上的区别。第二种方式的执行效率和资源占用在一般性情况下要优于第一种情况（注意这并不是绝对的）。

#### 5.下面程序的输出结果是什么？

```java
public class Main {
    public static void main(String[] args) { 
        Integer a = 1;
        Integer b = 2;
        Integer c = 3;
        Integer d = 3;
        Integer e = 321;
        Integer f = 321;
        Long g = 3L;
        Long h = 2L;
        System.out.println(c==d);
        System.out.println(e==f);
        System.out.println(c==(a+b));
        System.out.println(c.equals(a+b));
        System.out.println(g==(a+b));
        System.out.println(g.equals(a+b));
        System.out.println(g.equals(a+h));
    }
}
```

先别看输出结果，读者自己想一下这段代码的输出结果是什么。这里面需要注意的是：当 "=="运算符的两个操作数都是 包装器类型的引用，则是比较指向的是否是同一个对象，而如果其中有一个操作数是表达式（即包含算术运算）则比较的是数值（即会**触发自动拆箱的过程**）。另外，对于包装器类型，**equals方法并不会进行类型转换**。明白了这2点之后，上面的输出结果便一目了然：

```language
true
false
true
true
true
false
true
```

第一个和第二个输出结果没有什么疑问。第三句由于  a+b包含了算术运算，因此会触发自动拆箱过程（会调用intValue方法），因此它们比较的是**数值是否相等**。而对于c.equals(a+b)会先触发自动拆箱过程，再触发自动装箱过程，也就是说a+b，会先各自调用intValue方法，得到了加法运算后的数值之后，便调用Integer.valueOf方法，再进行equals比较。同理对于后面的也是这样，不过要注意倒数第二个和最后一个输出的结果（如果数值是int类型的，装箱过程调用的是Integer.valueOf；如果是long类型的，装箱调用的Long.valueOf方法），如果类型不一样，那么equals方法返回结果也肯定是false的。