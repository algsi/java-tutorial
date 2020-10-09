# 正确重新hashCode方法

## 示例

```java
public class Specification implements Comparable<Specification>, Serializable {

    /**
     * 规格编码
     */
    private String code;

    private String name;
    /**
     * CPU内核数
     */
    private int cpuCores;

    /**
     * 内存
     */
    private int memorySize;

    ...

    @Override
    public int hashCode() {
        int result = 1
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
```

重写了hashcode方法，我们知道如果重写hashcode方法是用到了对象类型，那么该对象类型也必须要重写hashCode方法，否则每次得到的hashcode值不一定一致，那么重写hashcode方法的意义就不大了。

重写 hashCode方法并不一定要用到所有属性，如果你认为只用一部分属性就足够确定两个对象是否相等，那就可以使用一部分属性。

计算hashCode方法的注意事项：

- 不能包含equals方法中没有用到的属性，否则会导致相同的对象具有不同的hash值，此处列出hashCode方法和equals方法之间应该具备的联系：

  - hashCode相等，两个对象不一定相等，此时需要根据equals方法来判断对象的对等性。

  - equals为true，两个对象一定相等，hashCode值也一定相等。

- 有些对象，比如String对象和Bigdecimal对象已经重写了hashcode方法，这些类型的值可以直接用于重写hashcode方法。

- 为要用31来计算，而且很多人都是这么写的，这是因为31是个神奇的数字，任何数 $n*31$ 都可以被jvm优化为 $(n<<5)-n$，移位和减法的操作效率比乘法的操作效率高很多！

- Google首席Java架构师Joshua Bloch在他的著作《Effective Java》中提出了一种简单通用的hashCode算法：

    1. 初始化一个整形变量，为此变量赋予一个非零的常数值，比如`int result = 17`;

    2. 如果是对象应用（例如有String类型的字段），如果equals方法中采取递归调用的比较方式，那么hashCode中同样采取递归调用hashCode的方式。否则需要为这个域计算一个范式，比如当这个域的值为null的时候（即 String a = null 时），那么hashCode值为0。

## java.util.Objects

java.util.Objects 工具类中为我们提供了一个静态的方法来计算 hashCode：

```java
public static int hash(Object... values) {
    return Arrays.hashCode(values);
}
```

只需要将你计算 hashCode 值时用到的属性作为参数传给该方法即可，能否发现，这个方法里面其实调用了另一个静态方法 `Arrays.hashCode(Object a[])`：

```java
public static int hashCode(Object a[]) {
    if (a == null)
        return 0;

    int result = 1;

    for (Object element : a)
        result = 31 * result + (element == null ? 0 : element.hashCode());

    return result;
}
```

可以借鉴这里面实现 hashCode 的方法，在这里，初始 result 为 1。
