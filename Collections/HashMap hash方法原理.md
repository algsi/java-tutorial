# HashMap has方法原理

根据 key 计算元素在数组中对应的下标是HashMap一个比较核心的算法，在 JDK 8 中，由下面这个方法来计算指定 Node 的 hashCode：

```java
static final int hash(Object key) {
    int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
```

这段代码非常简单，能看懂其中的计算步骤，但是要理解 `(h = key.hashCode()) ^ (h >>> 16)` 这一步的原因却并不是那么容易。

首先这个方法的返回值还是一个哈希值。为什么不直接返回 `key.hashCode()`，还要与 `(h >>> 16)` 做异或运算呢？首先要了解以下知识点：

必备知识点：

- `^` 异或运算
- `>>>` 无符号右运算
- `&` 与运算

## h >>> 16 是什么

h 是 key 直接得出来的 hashCode。计算机用补码来表示整数，在 Java 中，int 类型数据占 4 个字节，也就是 32 个 bit 位，`h >>> 16` 是用来取出 h 的高 16 位，如下展示：

```langauge
0000 0100 1011 0011  1101 1111 1110 0001

>>> 16

0000 0000 0000 0000  0000 0100 1011 0011
```

## 为什么要做异或运算

讲到这里，我们还要知道 HashMap 是如何通过 hash 方法返回值来计算数组下表的。在 putVal 方法中，有这么一段代码：

```java
if ((p = tab[i = (n - 1) & hash]) == null)
    tab[i] = newNode(hash, key, value, null);
```

`(n - 1) & hash`，其中 n 是整个数组的长度，它始终是 2 的幂次方（为什么是这样，这不是这篇文章的核心，可以自行去了解），这个表达式的返回值就是数组的下标。

由于绝大部分情况下 length 都小于 $2^{16}$ 即 65536，所以 `(n - 1) & hash` 结果始终是 h 的低 16 位与（length-1）进行 & 运算。如下例子。

例如：为了方便验证，假设 length 为8。HashMap 的默认初始容量为16

length = 8;  （length-1） = 7；转换二进制为111；

假设一个 key 的 hashcode 为 78897121，转换为二进制是 100101100111101111111100001，与（length-1）做 & 运算如下：

```language
    0000 0100 1011 0011 1101 1111 1110 0001

&

    0000 0000 0000 0000 0000 0000 0000 0111

=   0000 0000 0000 0000 0000 0000 0000 0001 （就是十进制1，所以下标为1）
```

上述运算实质是：001 与 111 做 & 运算。也就是哈希值的低三位参与了运算。如果让哈希值的低三位更加随机，那么 & 结果就更加随机，如何让哈希值的低三位更加随机，那么就是让其与高位异或。

补充知识：

- 当 length=8 时，下标运算结果取决于哈希值的低三位
- 当 length=16 时，下标运算结果取决于哈希值的低四位
- 当 length=32 时，下标运算结果取决于哈希值的低五位
- 当 length=2 的 N 次方，下标运算结果取决于哈希值的低 N 位

## 原因总结

由于和（length-1）运算，length 绝大多数情况小于2的16次方。所以始终是hashcode的低16位（甚至更低）参与运算。要是高16位也参与运算，会让得到的下标更加散列，从而在一定程度傻姑娘减少了 key 的冲突。

所以这样高16位是用不到的，如何让高16也参与运算呢。所以才有 `hash(Object key)` 方法。让他的 `hashCode()` 和自己的高 16 位进行 ^ 运算。所以 `(h >>> 16)` 得到他的高 16 位与 `hashCode()` 进行 ^ 运算。

## 为什么用 ^ 而不用 & 和 |

因为 & 和 | 都会使得结果偏向 0 或者 1，并不是均匀的概念，所以用 ^。这就是为什么有 `hash(Object key)` 的原因。

