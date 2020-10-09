# <font color="red">Java中Comparable和Comparator两种比较器</font>

Comparable 和 Comparator 都是用来实现集合中元素的比较、排序的两种比较器接口，这两种比较器很相似，只是 Comparable 是在集合内部定义的方法实现的排序，可称之为内比较器，而Comparator 是在集合外部实现的排序，可称之为外比较器。所以，如果想实现排序，就需要在集合外定义 Comparator 接口的方法或在集合内实现 Comparable 接口的方法。

## Comparable

Comparable接口位于 java.lang 包下。

实现了Comparable接口的类有一个特点，就是这些类可以和自己比较。Comparable 是一个对象本身就已经支持自比较所需要实现的接口（如 String、Integer 自己就可以完成比较大小操作，已经实现了Comparable接口），至于是以什么来作为比较的准则，则依赖该接口下的CompareTo方法的实现，CompareTo方法也叫自然比较方法。

自定义的类要在加入list容器中后能够排序，可以实现Comparable接口，在用Collections类的sort方法排序时，如果没有指定Comparator，那么就以自然顺序排序，如API所说：

> Sorts the specified list into ascending order, according to the natural ordering of its elements. All elements in the list must implement the Comparable interface.

这里的自然顺序就是实现Comparable接口设定的排序方式。

Comparable接口中只定义了一个方法：

	public int compareTo(T o);

这个方法的参数采用的是泛型参数。返回值的类型为int类型。对于返回值，源码里有这样的注释：
> function, which is defined to return one of <tt>-1</tt>, <tt>0</tt>, or <tt>1</tt> according to whether the value of <i>expression</i> is negative, zero or positive.

即返回值有三种情况：

1. 比较者大于被比较者（即CompareTo方法里的参数对象），那么返回1（positive）

2. 比较者等于被比较者，返回0（zero）

3. 比较者小于被比较者，返回-1（negative）

下面给出一个很简单的程序来说明这个情况

实现Comparable接口的一个类

```java
package com.li.compare;

public class Fruit implements Comparable<Fruit> {

	String name;
	int price;
	
	public Fruit(String name, int price) {
		this.name=name;
		this.price=price;
	}
	
	@Override
	public int compareTo(Fruit o) {
		//Integer类也实现了Comparable这个接口
		return ((Integer)price).compareTo(o.price);
	}
}

```

数据测试

```java
package com.li.compare;

public class Test {
	
	public static void main(String[] args) {
		Fruit f1=new Fruit("apple", 5);
		Fruit f2=new Fruit("banana", 7);
		Fruit f3=new Fruit("pear", 3);
		Fruit f4=new Fruit("watermelon", 5);

		System.out.println(f1.compareTo(f2));
		System.out.println(f1.compareTo(f3));
		System.out.println(f1.compareTo(f4));
	}
}

```

运行结果为：

```
-1
1
0
```

注意一下，前面说实现Comparable接口的类是可以支持和自己比较的，但是其实代码里面Comparable的泛型未必就一定要是Domain，将泛型指定为String或者指定为其他任何任何类型都可以----只要开发者指定了具体的比较算法就行。

## Comparator

Comparator位于 java.util 包下。

Comparator 是一个专用的比较器，当这个对象不支持自比较（没有实现Comparable这个接口）或者自比较函数不能满足你的要求时（不是你所想要的比较方式），这时可以写一个Comparator比较器来完成两个对象之间大小的比较。

两个比较器可以说一个是自已完成比较，一个是外部程序实现比较的差别而已。

用 Comparator 是策略模式（strategy design pattern），就是不改变对象自身（实际上有时候不能改变，例如，你要求对Integer类用绝对值来进行排序，但是我们并不能去改变Integer类，就需要采用这种比较器），而用一个策略对象（strategy object）来改变它的行为。

Comparator接口包含两个方法：

	public interface Comparator<T>{
    	......
        int compare(T o1, T o2);
        boolean euqals(Object obj);
    	......
    }

由于所有的类都继承了 java.lang.Object 类，在Object.java类中已经实现了 equals方法，所以，实现 Comparator 的类都需要实现 compare 方法。

对于 compare(T o1, T o2) 方法，是对两个参数对象进行比较，比较的原则，将由我们自己制定。其返回值也有三种情况：

- o1大于o2，返回正数

- o1等于o2，返回零

- o1小于o2，返回负数

一个简单的例子，上面的 Fruit 类的代码不变，我们写一个类继承Comparator。

```java
package com.li.compare;

import java.util.Comparator;

public class FruitComparator implements Comparator<Fruit> {

	@Override
	public int compare(Fruit o1, Fruit o2) {
		if (o1.price > o2.price)
			return 1;
		else if (o1.price == o2.price)
			return 0;
		else
			return -1;
	}

}

```

测试数据

```java
package com.li.compare;

public class Test {
	
	public static void main(String[] args) {
		Fruit f1=new Fruit("apple", 5);
		Fruit f2=new Fruit("banana", 7);
		Fruit f3=new Fruit("pear", 3);
		Fruit f4=new Fruit("watermelon", 5);
		
		FruitComparator fc=new FruitComparator();
		
		System.out.println(fc.compare(f1, f2));
		System.out.println(fc.compare(f1, f3));
		System.out.println(fc.compare(f1, f4));
	}
}
```

运行结果：

```
-1
1
0
```

当然因为泛型指定死了，所以实现Comparator接口的实现类只能是两个相同的类进行比较了，因此实现Comparator接口的实现类一般都会以"待比较的实体类+Comparator"来命名。