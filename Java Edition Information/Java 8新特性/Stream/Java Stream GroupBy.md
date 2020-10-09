# Java Stream GroupBy

## 1. group by, count, sort

### 1.1 group by and count

我们可以利用Java Stream来对集合中的元素进行分组聚合，并统计每个分组中的元素数量。

```java
public static void main(String[] args) {
    List<String> items = Arrays.asList("apple", "apple", "banana", "apple", "orange", "banana", "papaya");
    Map<String, Long> result = items.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    System.out.println(result);
}
```

output

```
{
    papaya=1, orange=1, banana=2, apple=3
}
```

注释：

- 利用 `Collectors.groupingBy()` 对集合中的元素进行聚合，`Function.identity()` 便指定了按照元素自身来集合，该方法输入是什么，输出就是什么，等价于 `input -> input`。

- Collectors.count() 得到的计数始终是 Long 型数据。
- 上面 `Collectors.groupingBy()` 第二个参数 downStream，也是一个Collectors，起到一个reduce的作用。

我们还可以选择按照字符串的长度来作为聚合的依据：

```java
List<String> items = Arrays.asList("apple", "apple", "banana", "apple", "orange", "banana", "papaya");
Map<Integer, Long> result = items.stream().collect(Collectors.groupingBy(String::length, Collectors.counting()));
```

### 1.2 group by and sort

group by之后，我们可能还想对结果进行排序，这个时候，我们只需要再指定一个比较器即可。

```java
public static void main(String[] args) {
    List<String> items = Arrays.asList("apple", "apple", "banana", "apple", "orange", "banana", "papaya");
    Map<String, Long> result = items.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    Map<String, Long> finalMap = new LinkedHashMap<>();
    // sort a map and add to finalMap
    result
        .entrySet()
        .stream()
        // 传入一个比较器 Comparator
        .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
        .forEachOrdered(e -> finalMap.put(e.getKey(), e.getValue()));

    System.out.println(finalMap);
}
```

output

```
{
    apple=3, banana=2, papaya=1, orange=1
}
```

## 2. User Defined Object

### 2.1 groupBying and counting and summing

Java Stream针对用户自定义的对象会有更多的操作。

首先给出一个 Java  POJO：

```java
public class Item {

    private String name;
    private int quantity;
    private BigDecimal price;
    
    // constructors, getter/setters
}
```

示例：

```java
public static void main(String[] args) {
    // 3 apple, 2 banana, others 1
    List<Item> items = Arrays.asList(
        new Item("apple", 10, new BigDecimal("9.99")),
        new Item("banana", 20, new BigDecimal("19.99")),
        new Item("orang", 10, new BigDecimal("29.99")),
        new Item("watermelon", 10, new BigDecimal("29.99")),
        new Item("papaya", 20, new BigDecimal("9.99")),
        new Item("apple", 10, new BigDecimal("9.99")),
        new Item("banana", 10, new BigDecimal("19.99")),
        new Item("apple", 20, new BigDecimal("9.99"))
    );

    // 按名称分组，并统计每个分组中元素的个数
    Map<String, Long> counting = items
        .stream()
        .collect(Collectors.groupingBy(Item::getName, Collectors.counting()));
    System.out.println(counting);

    // 按名称分组，并计算每个分组下所有元素的 quantity 值的和
    Map<String, Integer> sum = items
        .stream()
        .collect(Collectors.groupingBy(Item::getName, Collectors.summingInt(Item::getQuantity)));
    System.out.println(sum);
}
```

output：

```
{papaya=1, banana=2, apple=3, orang=1, watermelon=1}
{papaya=20, banana=30, apple=40, orang=10, watermelon=10}
```

如上，我们将集合中的元素分组，既可以统计每个组下的元素个数，也可以针对对象的某个属性对每个组下的所有元素进行 sum 操作。

### 2.2 groupBying and mapping

groupBying一个最常用的场景就是，针对元素的某一属性，将所有元素进行分组，每个组别下都对应一个元素集合。

也可能，我们还想对分组下的所有元素都再进行一次操作，比如，我们要提取分组下元素的某个属性转换成集合或者列表

```java
public static void main(String[] args) {
    // 3 apple, 2 banana, others 1
    List<Item> items = Arrays.asList(
        new Item("apple", 10, new BigDecimal("9.99")),
        new Item("banana", 20, new BigDecimal("19.99")),
        new Item("orang", 10, new BigDecimal("29.99")),
        new Item("watermelon", 10, new BigDecimal("29.99")),
        new Item("papaya", 20, new BigDecimal("9.99")),
        new Item("apple", 10, new BigDecimal("9.99")),
        new Item("banana", 10, new BigDecimal("19.99")),
        new Item("apple", 20, new BigDecimal("9.99"))
    );

    // 按 price 分组，每个 price 都对应一个对象集合
    Map<BigDecimal, List<Item>> groupByPrice = items
        .stream()
        .collect(Collectors.groupingBy(Item::getPrice));
    System.out.println(groupByPrice);

    // group by price, uses 'mapping' to convert List<Item> to Set<String>
    Map<BigDecimal, Set<String>> result = items
        .stream()
        .collect(Collectors
                 .groupingBy(
                     Item::getPrice,
                     Collectors.mapping(Item::getName, Collectors.toSet())
                 )
         );

    System.out.println(result);
}
```

output：

```
{
19.99=[
    Item[name='banana', quantity=20, price=19.99],
    Item[name='banana', quantity=10, price=19.99]
    ], 
29.99=[
    Item[name='orang', quantity=10, price=29.99], 
    Item[name='watermelon', quantity=10, price=29.99]
    ], 
9.99=[
    Item[name='apple', quantity=10, price=9.99],
    Item[name='papaya', quantity=20, price=9.99],
    Item[name='apple', quantity=10, price=9.99],
    Item[name='apple', quantity=20, price=9.99]
    ]
}

// groupBying + mapping to set
{19.99=[banana], 29.99=[orang, watermelon], 9.99=[papaya, apple]}
```

