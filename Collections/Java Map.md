# <font color=red>Java集合——Map</font>

概要图

![Java集合类.jpg](https://img-blog.csdn.net/20180411230300733)

## <font color=red>映射集（Map）</font>

Java中使用Map接口描述映射结构，映射Map是一个独立的接口，描述的是键key-值value的对应关系，Map不允许键重复，并且每个键只能对应一个值，键不能重复，值可以重复，键和值都不能为空。

Map接口的常用方法（这些都是源码的一些摘录，由此可见学好英语是多么重要，不然注释都可能看不懂！）：

- **int size()**：return the number of key-value mappings in this map

- **boolean isEmpty()**：return if this map contains no key-value mappings

- **V put(K key, V value)**：Associates the specified value with the specified key in this map. If the map previously contained a mapping for the key, the old value is replaced by the specified value.

- **V remove(Object key)**：Removes the mapping for a key from this map if it is present. More formally, if this map contains a mapping from key k to value v such that
		(key==null ?  k==null : key.equals(k))
   that mapping is removed.  (The map can contain at most one such mapping.)

- **V get(Object key)**：Returns the value to which the specified key is mapped, or {@code null} if this map contains no mapping for the key.

- **boolean containsKey(Object key)**：Returns true if this map contains a mapping for the specified key. More formally, returns true if and only if this map contains a mapping for a key k such that

     	(key==null ? k==null : key.equals(k))

    (There can be at most one such mapping.)

- **boolean containsValue(Object value)**：Returns true if this map maps one or more keys to the specified value.  More formally, returns true if and only if this map contains at least one mapping to a value v such that

     	(value==null ? v==null : value.equals(v))
        
- **Set keySet()**：return a set view of the keys contained in this map，The set is backed by the map, so changes to the map are reflected in the set, and vice-versa.  If the map is modified while an iteration over the set is in progress (except through the iterator's own <tt>remove</tt> operation), the results of the iteration are undefined.

	返回键所组成的Set集合。

        
- interface Entry：

	这是Map接口中包含的一个内部接口，接口的两个泛型参数分别为Map的键和值，这个接口中包含了一些方法：例如获取键、获取值、设置值。

    ```
    interface Entry<K,V> {
            ......
            K getKey();
            V getValue();
            V setValue(V value);
            ......
        }
    ```

- **Set entrySet()**：Returns a {@link Set} view of the mappings contained in this map. The set is backed by the map, so changes to the map are reflected in the set, and vice-versa.  If the map is modified while an iteration over the set is in progress (except through the iterator's own <tt>remove</tt> operation, or through the <tt>setValue</tt> operation on a map entry returned by the iterator) the results of the iteration are undefined.

	该方法的声明如下：

		Set<Map.Entry<K, V>> entrySet();

	将Map中的键-值对存入集合，该集合的数据元素是Entry接口的实例对象，实例对象具有两个成员变量key和value来描述原Map中的键和值，并能通过方法获取键和值。
    并且我们通过注释可以发现，如果我们在对集合进行迭代的时候修改了Map元素的内容，那么迭代的结果将是未定义的。

- **Collection values()**：Returns a {@link Collection} view of the values contained in this map. The collection is backed by the map, so changes to the map are reflected in the collection, and vice-versa.  If the map is modified while an iteration over the collection is in progress (except through the iterator's own <tt>remove</tt> operation), the results of the iteration are undefined.

	返回该Map中所有值所组成的Collection集合。

- **V remove(Object key)：**Removes the mapping for a key from this map if it is present (optional operation).

	移除操作，并返回值。
    
- **void clear()：**Removes all of the mappings from this map (optional operation). The map will be empty after this call returns.

## <font color=red>HashMap（散列图）</font>

- HashMap通过hash算法排布存储Map中的键（key），HashMap也是最常用的图状数据结构，其存储的数据元素是成对出现的，也就是说每一个键（key）对应一个值（value）。

- HashMap中的数据元素不是按照我们添加的顺序排布的，并且其内存模式也不是连续的，但是其key值的排布是根据Hash算法获得的，所以在数据元素的检索方面速度还是较快的。

- HashMap不能直接装入迭代器，必须将HashMap的所有键key装入迭代器，再进行遍历，或者是使用Entry类，将所有数据元素转化为Entry的集合进行处理。

- HashMap不允许出现重复的键（key），并且每个键（key）只能对应一个值（value）。

## <font color=red>TreeMap（树状映射集）</font>

- TreeMap是一种**有序**的映射关系，即每对键key-值value在TreeMap中是有序排列的，并且这个序列遵循自然序列，当我们向TreeMap插入新的数据元素时，TreeMap可能会重新排序，所以TreeMap中的任何元素在整个映射组中是不固定的。

- 当我们的TreeMap键（key）是自定义类时，需要在自定义类中重写compareTo方法以提供比对形式，这使用的是内比较器，或者也使用外部比较器来初始化TreeMap，例：
	
    	TreeMap<Student> treemap=new TreeMap<Student>(new StudentComParator());

    否则在TreeMap不能对用户自定义的类型的键（key）进行正确的树状排序，也就不能对整个键值对起到有效的排序效果。

### TreeMap的特殊方法

TreeMap是Map的实现类，除实现Map接口的所有方法外，增加了一些与序列有关的方法。

- firstKey()：获取TreeMap第一个Key值

- lastKey()：获取TreeMap最后一个key值

- headMap(end)：获取TreeMap中从第一个开始到end-1位置的所有元素，组成一个排序映射（SortedMap）

- tailMap(start)：获取TreeMap中从start开始到最后一个元素之间的所有元素，组成一个排序映射（SortedMap）

## <font color=red>Map的基本操作</font>

### 添加与取值

```java
// 泛型标准
Map<String, String> map = new HashMap<String, String>();
/**
 * 添加数据的方式：map.put(key, value) 
 * 获取数据的方式：map.get(key)（通过key获取value）
 * 默认返回值类型为Object类型
 */
map.put("2017120901", "张三");
map.put("2017120902", "小强");
map.put("2017120903", "李四");

String stuName = map.get("2017120902") + ""; 
// 默认返回值类型是object，转变为String类型
// String stuName1=map.get("2017120909").toString();
//不能使用toString，因为需要避免空指针异常
// String stuName2=(String)map.get("2017120909"); //返回null，但是也有可能出现空指针异常

System.out.println(stuName);
```

```java
Map map = new HashMap();
map.put("123", "123");
map.put("12378", null);   //允许将value置为null

// int num=Integer.parseInt(map.get("1233")+""); //对于不存在的数据，还是会出现空指针异常
// 增加程序的健壮性
//也可先使用containsKey判断键值是否存在，避免空指针异常
Object o = map.get("1234");
int num = (o == null ? 0 : (int) o);

//int num1=map.containsKey("123")?0:map.get("123");

System.out.println(num);
```

## <font color=red>Map的遍历方式</font>

### 第一种：将key装入set

通过Map接口的keySet()方法将所有的KEY取出来放到Set中（返回值就是一个Set集合），通过get(key)获取value。

```java
Set<String> keySet = map.keySet();
for (String key : keySet) {
    System.out.println(key + "  key--get--value  " + map.get(key));
}
System.out.println();
```

### 第二种：直接获取value值

直接获取value值（返回值就是Collection），但是不能获取key。

```java
Collection<String> cols = map.values();
for(String value : cols){
    System.out.println("direct--get-->value："+value);
}
System.out.println("修改了map的内容，但是不改变Collection接口！（map的改变会映射）");
//此时修改map的内容
map.put("2017120908", "王五");
System.out.println("再输出");
//再输出Collection的内容，也会产生变化
for(String value : cols){
    System.out.println("direct--get-->value："+value);
}
System.out.println();
```

### 第三种：使用内部类Entry

```java
Set<Map.Entry<String, String>> entrySet=map.entrySet();
		for(Map.Entry<String, String> entry : entrySet){
			System.out.println(entry.getKey()+"--Map.Entry--"+entry.getValue());
		}
```

