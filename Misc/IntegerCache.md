# IntegerCache

IntegerCache 是 java.lang.Integer 类中提供的一个缓存类。

先来看一段代码：

```java
Integer t1 = 100;
Integer t2 = Integer.valueOf(100);
System.out.println(t1 == t2);

Integer t3 = 200;
Integer t4 = Integer.valueOf(200);
Integer t5 = Integer.valueOf(200);
System.out.println(t3 == t4);
System.out.println(t4 == t5);
```

Output

```language
true
false
false
```

为什么会是这种输出结果呢？这里我们就必须要知道 Integer 类中的缓存类了。

Java 中基本类型的自动装箱会调用包装类的 valueOf 方法，那么我们先来看 Integer 中的 valueOf 方法。

```java
public static Integer valueOf(int i) {
    if (i >= IntegerCache.low && i <= IntegerCache.high)
        return IntegerCache.cache[i + (-IntegerCache.low)];
    return new Integer(i);
}
```

在 valueOf 方法中，首先判断了参数 i 是否在 $[IntegerCache.low, IntegerCache.high]$ 区间中，如果在这个区间中，则直接从 IntegerCache.cache 数组中返回对应的 Integer 对象，否则，就另外创建 Integer 对象。 

一般情况下，IntegerCache.low 的值为 -128，而 IntegerCache.high 的值为 127。这也解释了上面的结果，因为 100 在区间内，得到的始终是同一个对象，而 200 不在区间内，每一次都是新创建 Integer 对象。

## AutoBoxCacheMax

我们且看 IntegerCache 的源码。

```java
/**
 * Cache to support the object identity semantics of autoboxing for values between
 * -128 and 127 (inclusive) as required by JLS.
 *
 * The cache is initialized on first usage.  The size of the cache
 * may be controlled by the {@code -XX:AutoBoxCacheMax=<size>} option.
 * During VM initialization, java.lang.Integer.IntegerCache.high property
 * may be set and saved in the private system properties in the
 * sun.misc.VM class.
 */
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

该类的注释指示，IntegerCache 在第一次使用时被初始化。该类中有三个属性，low 和 high 为缓存的边界，而 cache 为缓存数组。缓存数组的大小还可以被 VM 参数 AutoBoxCacheMax 来控制。

static 语句块是我们需要着重关注的。static 语句块中首先默认 h 为 127，然后读取 VM 配置的 AutoBoxCacheMax 参数，并将该参数的值和 127 比较取较大者，并再保证数组的最大长度为 Integer.MAX_VALUE，最后再初始化 cache 数组。

我们可以配置 VM 参数 AutoBoxCacheMax 尝试一下，首先配置参数：`-XX:AutoBoxCacheMax=999`。

然后运行下面代码：

```java
Integer t3 = 999;
Integer t4 = Integer.valueOf(999);
Integer t5 = Integer.valueOf(999);
System.out.println(t3 == t4);
System.out.println(t4 == t5);
```

Output

```language
true
true
```

可以看到，配置项起了作用。