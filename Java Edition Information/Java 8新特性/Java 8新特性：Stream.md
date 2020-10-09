# Java 8新特性：Stream

Java 8 API添加了一个新的抽象称为流Stream，可以让你以一种声明的方式处理数据。

Stream 使用一种类似用 SQL 语句从数据库查询数据的直观方式来提供一种对 Java 集合运算和表达的高阶抽象。

Stream API可以极大提高Java程序员的生产力，让程序员写出高效率、干净、简洁的代码。

这种风格将要处理的元素集合看作一种流， 流在管道中传输， 并且可以在管道的节点上进行处理， 比如筛选， 排序，聚合等。

元素流在管道中经过中间操作（intermediate operation）的处理，最后由最终操作(terminal operation)得到前面处理的结果。

```
+--------------------+       +------+   +------+   +---+   +-------+
| stream of elements +-----> |filter+-> |sorted+-> |map+-> |collect|
+--------------------+       +------+   +------+   +---+   +-------+
```

以上的流程转换为 Java 代码为：

```java
List<Integer> transactionsIds = 
widgets.stream()
             .filter(b -> b.getColor() == RED)
             .sorted((x,y) -> x.getWeight() - y.getWeight())
             .mapToInt(Widget::getWeight)
             .sum();
```

## 什么是Stream

Stream（流）是一个来自数据源的元素队列并支持聚合操作

-   **无存储**元素是特定类型的对象，形成一个队列。 Java中的Stream并不会存储元素，而是按需计算。
-   **数据源** 流的来源。 可以是集合，数组，I/O channel， 产生器generator 等。
-   **聚合操作** 类似SQL语句一样的操作， 比如filter, map, reduce, find, match, sorted等。
-   **为函数式编程而生** 对Stream的任何修改都不会修改背后的数据源，比如对Stream执行过滤操作并不会删除被过滤的元素，而是会产生一个不包含过滤元素的新的Stream。
-   **惰性执行** Stream上的操作并不会立即执行，只有等到用户真正需要结果的时候才会执行。
-   **可消费型** Stream只能被“消费“一次，一旦遍历过就会石板，就像容器的迭代器那样，想要再次遍历必须重新生成。

和以前的Collection操作不同， Stream操作还有两个基础的特征：

-   **Pipelining**: 中间操作都会返回流对象本身。 这样多个操作可以串联成一个管道， 如同流式风格（fluent style）。 这样做可以对操作进行优化， 比如延迟执行(laziness)和短路( short-circuiting)。
-   **内部迭代**： 以前对集合遍历都是通过Iterator或者For-Each的方式, 显式的在集合外部进行迭代， 这叫做外部迭代。 Stream提供了内部迭代的方式， 通过访问者模式(Visitor)实现。

对于流的处理，主要有三种关键性操作，分别是：

-   流的创建
-   中间操作（intermediate operation）
-   最终操作（terminal operation）

## 生成流

在 Java 8 中, 可以有多重方法来创建流。

第一种是通过已有的集合来生成流。在Java 8中，除了增加了很多Stream相关的类之外，还对集合类自身做了增强，在其中增加了stream方法，可以将一个集合类转换成流：

-   **stream()** − 为集合创建串行流。
-   **parallelStream()** − 为集合创建并行流。

如下：

```java
List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd","", "jkl");
List<String> filtered = strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.toList());

Stream<String> stream = strings.stream();
```

第二种是通过Stream创建流。可以使用Stream类提供的方法，直接方法一个由指定元素组成的流。如下：

```java
Stream<String> stream = Stream.of("Hellos", "World", "Hi");
```

## Stream中间操作

Stream有很多中间操作，多个中间操作连接起来形成一个流水线，每一个中间操作就像流水线上的一个工人，每个工人都可以对流水线加工，加工后得到的结果还是一个流。

常用的中间操作列表有：

-   filter
-   map
-   limit
-   sorted
-   distinct

## map

map 方法用于映射每个元素到对应的结果（输入与输出是一对一的关系），以下代码片段使用 map 输出了元素对应的平方数：

```
List<Integer> numbers = Arrays.asList(3, 2, 2, 3, 7, 3, 5);
// 获取对应的平方数并且去重
List<Integer> squaresList = numbers.stream().map( i -> i*i).distinct().collect(Collectors.toList());
```

### filter

filter 方法用于通过设置的条件过滤出元素。以下代码片段使用 filter 方法过滤出空字符串：

```java
List<String>strings = Arrays.asList("abc", "", "bc", "efg", "abcd","", "jkl"); 
// 获取空字符串的数量 
int count = strings.stream().filter(string -> string.isEmpty()).count();
```

当filter方法中的方法类型接口返回值为true时，就不会过滤该元素。

### limit/skip

limit 方法用于获取指定数量的流；skip则是扔掉前n个元素。 以下代码片段使用 limit 方法打印出 10 条数据：

```java
Random random = new Random(); 
random.ints().limit(10).forEach(System.out::println);
```

### sorted

sorted 方法用于对流进行排序。以下代码片段使用 sorted 方法对输出的 10 个随机数进行排序：

```java
Random random = new Random(); 
random.ints().limit(10).sorted().forEach(System.out::println);
```

### distinct

distinct方法主要用来去重，以下代码片使用地上停车对元素进行去重：

```java
List<Integer> numbers = Arrays.asList(3, 2, 2, 3, 7, 3, 5);
// 获取对应的平方数并且去重
List<Integer> squaresList = numbers.stream().map( i -> i*i).distinct().collect(Collectors.toList());
```

## Stream最终操作

Stream的中间操作得到的结果还是一个Stream，那么如何将一个Stream转换成我们需要的类型呢？比如计算出流中的元素个数、将流装成集合等。这就需要最终操作（terminal operation）。

最终操作会消耗流，产生一个最终结果。也就是说，在最终结果之后，不能再次使用流，也不能再使用任何中间操作，否则将会抛出异常：

```java
java.lang.IllegalStateException: stream has already been operated upon or closed
```

俗话说，“你永远也不会两次踏入同一条河”也正是这个意思。

常用的最终操作如下：

-   forEach
-   count
-   collect

### forEach

Stream 提供了新的方法 'forEach' 来迭代流中的每个数据。以下代码片段使用 forEach 输出了10个随机数：

```java
Random random = new Random();
random.ints().limit(10).forEach(System.out::println);
```

或者加入一些简单的逻辑：

```java
 random.ints().limit(10).forEach(x -> System.out.println(x > 0 ? x : 0));
```

### count

count用来统计流中的元素个数。

```java
List<String>strings = Arrays.asList("abc", "", "bc", "efg", "abcd","", "jkl"); 
// 获取空字符串的数量 
int count = strings.stream().filter(string -> string.isEmpty()).count();
```

### collect

collect是一个规约操作，可以接受各种做饭作为参数，将流中的元素累积成一个汇总结果。

```java
List<String> strings = Arrays.asList("Hollis", "HollisChuang", "hollis");
strings = strings.stream.filter(string -> string.startsWith("Hollis")).collect(Collectors.toList());
```

## 并行（parallel）程序

```java
List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd","", "jkl");
        // 获取空字符串的数量
long count = strings.parallelStream().filter(String::isEmpty).count();
System.out.println(count);
```

上面使用了方法引用（method reference）替代了Lambda表达式。

我们可以很容易的在顺序运行和并行直接切换。

## 统计

一些产生统计结果的收集器也非常有用。它们主要用于int、double、long等基本类型上，它们可以用来产生类似如下的统计结果。

```java
List<Integer> numbers = Arrays.asList(3, 2, 2, 3, 7, 3, 5);
IntSummaryStatistics stats = numbers.stream().mapToInt(x -> x).summaryStatistics();

System.out.println("列表中最大的数 : " + stats.getMax());
System.out.println("列表中最小的数 : " + stats.getMin());
System.out.println("所有数之和 : " + stats.getSum());
System.out.println("平均数 : " + stats.getAverage());
```

