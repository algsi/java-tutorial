# Java泛型中的extends和super

From: <https://www.cnblogs.com/xiarongjin/p/8309755.html>

[TOC]

经常发现有`List<? super T>` 和 `Set<? extends T>` 的声明，这是什么意思呢？

- `<? super T>` 表示包括 `T` 在内的任何 `T` 的父类

- `<? extends T>` 表示包括 `T` 在内的任何 `T` 的子类

补充：<?> 是 <? extends Object> 的简写。

下面我们详细分析一下两种通配符具体的区别。

## extends

<? extends T> 是 Upper Bound（上限） 的通配符，用来限制元素的类型的上限，比如：

`List<? extends Number> foo` 的通配符声明，表示集合中的元素类型上限为 Number，即只能是 Number 或者 Number 的子类。意味着以下的赋值是合法的：

```java
// Number "extends" Number (in this context)
List<? extends Number> foo = new ArrayList<Number>();

// Integer extends Number
List<? extends Number> foo = new ArrayList<Integer>();

// Double extends Number
List<? extends Number> foo = new ArrayList<Double>();
```

如果集合中元素类型为 Number 的父类则会编译出错，比如：

```java
List<? extends Number> list = new ArrayList<Object>();
```

有了上面的基础，我们再来看看 <? extends E> 限定的集合的读写操作。

1. 读取操作。

    通过以上给定的赋值语句，你一定能从 foo 列表中读取到的元素的类型是什么呢？你可以读取到 Number 类型的元素，因为以上的列表要么包含 Number 元素，要么包含Number子类的元素。

    你不能保证读取到 Integer，因为 foo 可能指向的是 `List<Double>`。

    你不能保证读取到 Double，因为 foo 可能指向的是 `List<Integer>`。

2. 写入操作。

    通过以上给定的赋值语句，你能把一个什么类型的元素合法地插入到 foo 中呢？

    你不能插入一个 Integer 元素，因为 foo 可能指向 `List<Double>`。

    你不能插入一个 Double 元素，因为 foo 可能指向 `List<Integer>`。

    你不能插入一个 Number 元素，因为 foo 可能指向 `List<Integer>`。

综上，你不能往 `List<? extends T>` 中插入任何类型的对象，因为你不能保证列表实际指向的类型是什么，因而你并不能保证列表中实际存储什么类型的对象。唯一可以保证的是，你可以从中读取到 `T` 或者 `T` 的子类。因此，泛型通配符`<? extends T>` 此写法的泛型集合不能使用写操作。

## super

现在考虑一下 `List<? super T>`。<? super T> 是 Lower Bound（下限） 的通配符 ，用来限制元素的类型下限，比如：

`List<? super Integer> foo` 的通配符声明，即集合中的元素只能是 Integer 或者 Integer 的父类。意味着以下赋值是合法的：

```java
// Integer is a "superclass" of Integer (in this context)
List<? super Integer> foo = new ArrayList<Integer>();

// Number is a superclass of Integer
List<? super Integer> foo = new ArrayList<Number>();

// Object is a superclass of Integer
List<? super Integer> foo = new ArrayList<Object>();
```

1. 读取操作。

   通过以上给定的赋值语句，你一定能从 foo 列表中读取到的元素的类型是什么呢？

   你不能保证读取到 Integer，因为 foo 可能指向 `List<Number>` 或者 `List<Object>`。

   你不能保证读取到 Number，因为 foo 可能指向 `List<Object>`。

   唯一可以保证的是，你可以读取到 Object 或者 Object 子类的对象（但你并不知道具体的子类是什么）。

2. 写入操作。

   通过以上给定的赋值语句，你能把一个什么类型的元素合法地插入到 foo 中呢？

   你可以插入 Integer 对象，因为上述声明的列表都支持 Integer。

   你可以插入 Integer 的子类的对象，因为 Integer 的子类同时也是 Integer，原因同上。

因此，泛型通配符`<? super T>` 此写法的泛型集合不能使用读操作，作为接口调用赋值时易出错。

## PECS

请记住PECS（Producer Extends Consumer Super）原则：生产者（Producer）使用extends，消费者（Consumer）使用super。

- 生产者使用extends。如果你需要一个列表提供 `T` 类型的元素（即你想从列表中读取 `T` 类型的元素），你需要把这个列表声明成 `<? extends T>` ，比如List<? extends Integer>，因此你不能往该列表中添加任何元素。
- 消费者使用super。如果需要一个列表使用 `T` 类型的元素（即你想把 `T` 类型的元素加入到列表中），你需要把这个列表声明成 `<? super T>`，比如 `List<? super Integer>`，因此你不能保证从中读取到的元素的类型。
- 既是生产者，也是消费者。如果一个列表即要生产，又要消费，你不能使用泛型通配符声明列表，比如`List<Integer>`。

## Example

请参考 `java.util.Collections` 里的copy方法(JDK1.8)：

```java
/**
 * Copies all of the elements from one list into another.  After the
 * operation, the index of each copied element in the destination list
 * will be identical to its index in the source list.  The destination
 * list must be at least as long as the source list.  If it is longer, the
 * remaining elements in the destination list are unaffected. <p>
 *
 * This method runs in linear time.
 *
 * @param  <T> the class of the objects in the lists
 * @param  dest The destination list.
 * @param  src The source list.
 * @throws IndexOutOfBoundsException if the destination list is too small
 *         to contain the entire source List.
 * @throws UnsupportedOperationException if the destination list's
 *         list-iterator does not support the <tt>set</tt> operation.
 */
public static <T> void copy(List<? super T> dest, List<? extends T> src) {
    int srcSize = src.size();
    if (srcSize > dest.size())
        throw new IndexOutOfBoundsException("Source does not fit in dest");

    if (srcSize < COPY_THRESHOLD ||
        (src instanceof RandomAccess && dest instanceof RandomAccess)) {
        for (int i=0; i<srcSize; i++)
            dest.set(i, src.get(i));
    } else {
        ListIterator<? super T> di=dest.listIterator();
        ListIterator<? extends T> si=src.listIterator();
        for (int i=0; i<srcSize; i++) {
            di.next();
            di.set(si.next());
        }
    }
}
```

亦或者是其他的几个方法的声明：

```java
boolean addAll(Collection<? extends E> c);

default boolean removeIf(Predicate<? super E> filter) {
        Objects.requireNonNull(filter);
        boolean removed = false;
        final Iterator<E> each = iterator();
        while (each.hasNext()) {
            if (filter.test(each.next())) {
                each.remove();
                removed = true;
            }
        }
        return removed;
    }
```

当调用 addAll 方法时，我们希望参数 c 集合能提供 E 元素（生产者，读操作），因此使用 `<? extends E>`。

当调用 removeIf 方法时，我们希望 filter 能消费 E 元素（消费者，写操作），因此使用 `<? super E>`。

我们可以从Java开发团队的代码中获得到一些启发，copy方法中使用到了PECS原则，实现了对参数的保护。

`java.util.map#replaceAll(BiFunction<? super K, ? super V, ? extends V> function)` 方法中，function 有三个类型限定参数，第一个和第二个都是接口的入参类型，视为消费者，第三个是接口的返参类型。视为生产者。

## 总结

为什么要引入泛型通配符？一句话：为了保证类型安全。

具体请看 《effective java》里，Joshua Bloch提出的PECS原则：

1. **频繁往外读取内容的，适合用上界 extends。**
2. **经常往里插入的，适合用下界 super。**

要加深这种理解，可以多看看 JDK Collection 中关于这一原则的应用。
