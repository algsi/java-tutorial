# Java Stream将List转Map

Java 8为我们提供了非常便利的Stream操作，我们可以用非常简单的方式来实现复杂的操作。本篇章介绍利用 Stream 将 List 转为 Map。

<https://blog.csdn.net/icannotdebug/article/details/78621705>

## 示例

```java
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Test;

public class DemoListToMap {
    List<Student> list = Arrays.asList(new Student(1, 18, "阿龙", GenderColumn.BOY.getCode()),
                                       new Student(2, 17, "小花", GenderColumn.GIRL.getCode()),
                                       new Student(3, 17, "阿浪", GenderColumn.LADYBOY.getCode()));

    @Test
    public void listToMapByObjectValue(){
        // Map 中 value 为对象，student -> student，也可以用java.util.function.Function.identity()来始终返回当前流中的对象。
        Map<Integer, Student> map = list.stream().collect(Collectors.toMap(Student::getId, student -> student));
        // 遍历打印结果
        map.forEach((key, value) -> {
            System.out.println("key: " + key + "    value: " + value);
        });
    }

    @Test
    public void listToMapByNameValue(){
        // Map 中 value 为对象中的属性
        Map<Integer, String> map = list.stream().collect(Collectors.toMap(Student::getId, Student::getName));
        map.forEach((key, value) -> {
            System.out.println("key: " + key + "    value: " + value);
        });
    }
}
```

执行第一个测试方法：value为对象

```
key: 1    value: Student [id=1, age=18, name=阿龙, gender=0]
key: 2    value: Student [id=2, age=17, name=小花, gender=1]
key: 3    value: Student [id=3, age=17, name=阿浪, gender=2]
```

可以看到，Student id为Key，而Student对象则为Value。

执行第二个测试方法：Value为对象中的属性

```
key: 1    value: 阿龙
key: 2    value: 小花
key: 3    value: 阿浪
```

注：java.util.function.Function.identity() 方法是 Function 接口中的静态方法，该方法的返回值正是 Function 本身，它的实现是：

```java
/**
 * Returns a function that always returns its input argument.
 *
 * @param <T> the type of the input and output objects to the function
 * @return a function that always returns its input argument
 */
static <T> Function<T, T> identity() {
    return t -> t;
}
```

也就是说，identity 给出了一种函数实现，入参是什么，出参就是什么，等价于 lambda 表达式中的 `input -> input`。

## Merge

我们知道，Map中的key必须是唯一的，所以在List转Map的过程中有可能出现不唯一的时候，就会报错。

测试以下方法：

```java
@Test
public void listToMapByAgeKey(){
    // value 为对象中的属性，以 age 为 key，以 name 为 value
    Map<Integer, String> map = list.stream().collect(Collectors.toMap(Student::getAge, Student::getName));
}
```

执行抛出异常：java.lang.IllegalStateException: Duplicate key 小花

因为age相同就会报错，此时 stream 包下 Collectors.toMap 方法有一个重载方法的参数，这个参数可以传一个合并的函数解决冲突。

```java
@Test
public void listToMapByAgeKey(){
    // value 为对象中的属性
    Map<Integer, String> map = list.stream().collect(
        Collectors.toMap(Student::getAge, Student::getName, (value1, value2) -> value1)
    );
    map.forEach((key, value) -> {
        System.out.println("key: " + key + "    value: " + value);
    });
}
```

`(value1, value2) -> value1` 这段代码就是合并函数的实现，它表示，在出现两个 key 冲突（相同）时，我们对两个 value 的处理方式，我们这里就直接选取第一个 value 作为最终的 value，那么这两个 value 究竟哪个是新来的，哪个是旧有的呢？我们知道流是有先后顺序的，第一个声明的 value1 就是 Map 中已经有的值了，而 value2 是后来的冲突的 value。

执行结果：

```
key: 17    value: 小花
key: 18    value: 阿龙
```

## 函数声明

要利用 Stream 将 List 转为 Map。就必须要用到 java.util.stream.Collectors.toMap 方法，该方法有三种声明形式，分别如下：

第一种：

```java
public static <T, K, U>
Collector<T, ?, Map<K,U>> toMap(Function<? super T, ? extends K> keyMapper,
                                Function<? super T, ? extends U> valueMapper) {
    return toMap(keyMapper, valueMapper, throwingMerger(), HashMap::new);
}
```

- @param keyMapper a mapping function to produce keys
- @param valueMapper a mapping function to produce values

该种方法，接受两个参数，分别是两个函数接口，keyMapper用来生成key，valueMap用来生成value。

第二种：

```java
public static <T, K, U>
Collector<T, ?, Map<K,U>> toMap(Function<? super T, ? extends K> keyMapper,
                                Function<? super T, ? extends U> valueMapper,
                                BinaryOperator<U> mergeFunction) {
    return toMap(keyMapper, valueMapper, mergeFunction, HashMap::new);
}
```

- @param keyMapper a mapping function to produce keys
- @param valueMapper a mapping function to produce values
- @param mergeFunction a merge function, used to resolve collisions between values associated with the same key, as supplied to {@link Map#merge(Object, Object, BiFunction)

这个方法多了个解决冲突的函数接口 BinaryOperation。该接口中有两个静态方法 minBy 和 maxBy，这两个静态方法都接收一个 Comparator 作为参数，这很明了了，它给我们提供了一种解决冲突的途径，即选较小者或较大者，但需要我们提供一个比较器。

minBy 示例：

```java
List<Student> list = Arrays.asList(
        new Student(3, 17, "a"),
        new Student(1, 18, "b"),
        new Student(2, 17, "c")
);

Map<Integer, String> map = list.stream().collect(Collectors.toMap(Student::getAge, Student::getName, BinaryOperator.minBy(String::compareTo)));
// 遍历打印结果
map.forEach((key, value) -> {
    System.out.println("key: " + key + "    value: " + value);
});
```

输出：
```
key: 17    value: a
key: 18    value: b
```

maxBy 示例：

```java
List<Student> list = Arrays.asList(
        new Student(3, 17, "a"),
        new Student(1, 18, "b"),
        new Student(2, 17, "c")
);

Map<Integer, String> map = list.stream().collect(Collectors.toMap(Student::getAge, Student::getName, BinaryOperator.maxBy(String::compareTo)));
// 遍历打印结果
map.forEach((key, value) -> {
    System.out.println("key: " + key + "    value: " + value);
});
```

输出：
```
key: 17    value: c
key: 18    value: b
```
