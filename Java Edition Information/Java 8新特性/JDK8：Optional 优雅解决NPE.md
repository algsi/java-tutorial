# JDK8：Optional 优雅解决NPE

防止 NPE，是程序员的基本修养，注意 NPE 产生的场景：

1） 返回类型为基本数据类型，return 包装数据类型的对象时，自动拆箱有可能产生 NPE。

2） 数据库的查询结果可能为 null。

3） 集合里的元素即使 isNotEmpty，取出的数据元素也可能为 null。

4） 远程调用返回对象时，一律要求进行空指针判断，防止 NPE。

5） 对于 Session 中获取的数据，建议进行 NPE 检查，避免空指针。

6） 级联调用 obj.getA().getB().getC()；一连串调用，易产生 NPE。

JDK8 提供了 Optional 类来防止 NPE 问题，但首选我们需要明确的是，它并不是对null关键字的一种替代，而是对于null判定提供了一种更加优雅的实现，从而避免NullPointException。

JDK8增加了许多有用的API，Optional类就是其中之一。如果不了解Optional类，只是简单的认为它可以解决NPE问题，于是就有了如下代码：

```java
Optional<User> user = xxx
if(user.isPresent){
    return user.getOrders();
}else {
    return Collections.emptyList();
}
```

上面采用Optional类的代码和我们正常下面写法并没有太大区别：

```java
User user = xxx
if(user!=null){
    return user.getOrders();
}else{
    return Collections.emptyList();
}
```

直白的讲，当我们还在以如下几种方式使用 Optional 时，就得开始检视自己了：

- 调用 `isPresent()` 方法时

- 调用 `get()` 方法时

- Optional 类型作为类或者实例属性时

- Optional 类型作为方法参数时

那么Optinal的正确使用姿势来了，在 Optional 中我们真正可依赖的应该是除了 isPresent() 和 get() 的其他方法：

- `public<U> Optional<U> map(Function<? super T, ? extends U> mapper)`

- `public T orElse(T other)`

- `public T orElseGet(Supplier<? extends T> other)`

- `public void ifPresent(Consumer<? super T> consumer)`

- `public Optional<T> filter(Predicate<? super T> predicate)`

- `public<U> Optional<U> flatMap(Function<? super T, Optional<U>> mapper)`

- `public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X`

## 类解析

java.util.Optional 类十分简单，你甚至可以直接从源码入手直接学习。

```java
public final class Optional<T> {

}
```

### 变量

该类中声明的变量仅有两个：

```java
/**
 * Common instance for {@code empty()}.
 */
private static final Optional<?> EMPTY = new Optional<>();

/**
 * If non-null, the value; if null, indicates no value is present
 */
private final T value;
```

第一个变量毫无疑问是一个公共的实例，它调用的是私有构造方法，第二个变量是 Optional 类中存储的一个对象，一般也就是我们需要进行 NPE 判断的对象，`EMPTY` 对象中的 `value` 对象是 null。

### 构造方法

Optional对外提供了三种获取 Optional 实例的方法。它实际的构造方法是私有的，所以你不能通过 new 来创建这个对象：

```java
private Optional() {
    this.value = null;
}

private Optional(T value) {
    this.value = Objects.requireNonNull(value);
}

public static<T> Optional<T> empty() {
    @SuppressWarnings("unchecked")
    Optional<T> t = (Optional<T>) EMPTY;
    return t;
}

public static <T> Optional<T> of(T value) {
    return new Optional<>(value);
}

public static <T> Optional<T> ofNullable(T value) {
    return value == null ? empty() : of(value);
}
```

从上面的源码来看：Optional类有两个不同的构造方法，它们的 `value` 的可空性的要求不同，我们可以通过三种方式来获取 Optional 实例：

- `Optional.empty()`：该方法始终返回一个 Optional 的公共静态实例 EMPTY（`value` 为 null）。使用：

    ```java
    Optional<String> optional = Optional.empty();
    Optional.<Integer>empty();
    ```

- Optional.of(obj)：要求传入的对象不能为null，否则还没开始初始化就抛出NPE。

- Optional.ofNullable(obj)：以一种智能、宽容的方式来构造一个Optional实例，来者不拒。传参为null时实际调用 `Optional.empty()`，非null时，调用 `Optional.of(obj)`。

### 方法

#### get()、isPresent()、ifPresent()

这三个方法非常简单：

```java
public T get() {
    if (value == null) {
        throw new NoSuchElementException("No value present");
    }
    return value;
}


public boolean isPresent() {
    return value != null;
}

public void ifPresent(Consumer<? super T> consumer) {
    if (value != null)
        consumer.accept(value);
}
```

- get()：用来或者 value，尽量不用

- isPresent(): 判断 value 是否为 null，不用！

- ifPresent()：value 不空时才做点什么

    ```java
    user.ifPresent(System.out::println);

    // 而不要下面那样
    if (user.isPresent()) {
    System.out.println(user.get());
    }
    ```

#### orElse()

存在则返回，不存在则提供默认值。

它的源码如下：

```java
public T orElse(T other) {
    return value != null ? value : other;
}
```

可以看出它的逻辑非常简单，使用起来也很简单：

```java
Optional<User> user = Optional.ofNullable(....);
return user.orElse(null);  // 而不是 return user.isPresent() ? user.get() : null;
return user.orElse(UNKNOWN_USER);
```

#### orElseGet()

存在即返回, 不存在则由函数来产生。

它的源码如下：

```java
public T orElseGet(Supplier<? extends T> other) {
    return value != null ? value : other.get();
}
```

它允许我们在不存在的情况下用一个函数来生成对象：

```java
// 不要 return user.isPresent() ? user: fetchAUserFromDatabase();
return user.orElseGet(() -> fetchAUserFromDatabase());
```

#### orElseThrow()

不存在则抛出异常。

它的源码如下：

```java
public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
    if (value != null) {
        return value;
    } else {
        throw exceptionSupplier.get();
    }
}
```

它允许我们在 value 不存在的时候抛出指定的异常，使用如下：

```java
user.orElseThrow(() -> new IllegalStateException("user can not be null"));
```

当然，如果指定的 Supplier 接口实例为 null 也会抛出 NPE。

#### map 函数隆重登场

或许我们有过级联调用的场景：

```java
User user = ......
user.getUserName().toUpperCase();
```

但是这种级联调用最容易出现NPE，之前是怎么解决的呢？看下：

```java
User user = .....
User user = null;
if (user != null) {
    String name = user.getName();
    if (name != null) {
        return name.toUpperCase();
    } else {
        return null;
    }
} else {
    return null;
}
```

现在 map 就专门是为了解决这种级联调用的场景，先来看看 map 函数源码：

```java
public<U> Optional<U> map(Function<? super T, ? extends U> mapper) {
    Objects.requireNonNull(mapper);
    if (!isPresent())
        return empty();
    else {
        return Optional.ofNullable(mapper.apply(value));
    }
}
```

该函数接受一个接口实例，该接口是函数式接口，其中的 apply 方法接受参数并返回结果，具体的逻辑需要我们提供。

可以看到如果某个环节出现了 value 为 null 的情况，也只是会返回 EMPTY，而不会报NPE。

具体使用：

```java
user.map(u -> u.getUsername())
    .map(name -> name.toUpperCase())
    .orElse(null);
```

这种方式是不是更加优雅呢？
