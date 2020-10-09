# <font color=red>Java集合类 List</font>

概要图

![Java集合类.jpg](https://img-blog.csdn.net/20180411230300733)

### <font color=red>Java中常用集合类</font>

常用集合类主要分为Collection和Map两大分支。橙色表示接口，蓝色表示实现类。

![常用集合.PNG](http://img.blog.csdn.net/20180226114215542)

### <font color=red>线性结构</font>

Java中使用List接口描述线性结构，其实现类使用最频繁的有ArrayList（数组线性表）和LinkedList（双向链表）。

List里存放的对象时有序的，同时也是可以重复的，并提供了按索引访问的方式。

List接口定义的常用方法如下：

- size()：获取List的长度（即List包含数据元素的总数）

- add(Object obj)：向List的尾部添加一个元素obj

- add(int index, Object obj)：向List中索引index的位置添加元素obj

- set(int i, Object obj)：使用元素obj替换索引i 位置的元素，并返回被替换的元素。

- remove(int i)：删除索引i 指定位置的元素，并返回被删元素

- get(int i)：返回索引i 指定位置的元素。


### <font color=red>数组线性表 （ArrayList）</font>

ArrayList使用**<font color=blue>连续</font>**的内存单元存储数据元素，当添加或删除数据元素时（除数组线性表的最后位置外），ArrayList需要移动其被添加（或删除）元素后面的全部元素。在遍历所有元素方面，ArrayList具有很好的效率，因为其数据元素的连续性，所有只需要获取到第一个元素的地址，再使用地址指针下移的方式即可获取全部元素。

ArrayList适合存储经常用于显示的数据，并且不对查询结果进行修改，而仅仅是用于客户显示。

如果我们只是在ArrayList的末尾进行添加（或删除）操作，可以使用ArrayList。

### <font color=red>泛型（简介）</font>

泛型的本质是参数化类型，也就是说所操作的数据类型被指定为一个参数。这种参数类型可以用在类、接口和方法的创建中，分别称为泛型类、泛型接口和泛型方法。 Java语言引入泛型的好处是安全简单。

对于数据结构来说，泛型用于指定当前数据结构存储的元素类型，例如：ArrayList<String>表示当前的数组线性表只能存储String类型的数据。当然也可以在List中存储自定义类型，例如：LinkedList<Student>表示当前的双向链表中只能存储Student的对象。

### <font color=red>数组线性表与数组的区别</font>

- 数组是定长有序的线型结合

- 数组线性表是任意长度的线型集合。

- 两者本质的区别在与长度是否可变。

- 两者获取元素的方式不同

	- 数组：使用下标：array [index]

	- 数组线性表：使用get方法：list.get(index)


- 获取长度的方式不同

	- 数组：length属性

	- 数组线性表：size()方法

### <font color=red>LinkedList（双向链表）</font>

LinkedList是使用指针关联的双向链表，其获取下一个元素的方式是通过指向下一个元素的地址对象（通常叫做指针）获取的，由于它的链式存储结构，LinkedList在任意位置的元素插入（或删除）效率都比较快，因为元素插入（或删除）不需要移动LinkedList中的其它元素，当需要一个频繁插入（或删除）的线性结构时，首选的List实现类应该是LinkedList。

- LinkedList对于ArrayList来说，其遍历速度较慢，因为他获取下一个元素的时间为寻址时间。

- LinkedList适合存储数据元素变动较大的线性集合，可以更快速的对指定位置的元素进行增加、删除、修改和查询功能。

### <font color=red>LindedList与ArrayList的区别</font>

ArrayList和LinkedList在用法上没有区别，但是在功能上还是有区别的。LinkedList经常用在增删操作较多而查询操作较少的情况下，ArrayList则相反。

- ArrayList的存储空间是连续的，故对所有元素的遍历速度较快。

- LinkedList的存储方式是链式的，并非连续的，所以每个元素都记录了前元素和后元素，故插入元素和删除的速度较快。

### <font color=red>Vector（介绍）</font>

- Vector是一个Object类型的可变长的数组，其元素类型可以是任意的数据类型（Object的子类），我们使用Vector通常是存储元素类型不同，但描述对象又统一的集合。

- Vector与ArrayList的区别是Vector是线程安全的，故Vector相对于ArrayList的速度稍慢一些。

### <font color=red>使用ArrayList实例化List接口</font>

Example:

```java
package com.li.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class ArrayListLearning {
	
	//添加
	public static void addTest(List<String> list) {
		list.add("第一个元素");
		list.add("第二个元素");
		list.add("第三个元素");
	}

	 //向集合里面添加一个集合
	public static void addAllTest(List<String> list) {
		List list01 = new ArrayList();
		list01.add("第四个元素");
		list01.add("第五个元素");
		list01.add("第六个元素");
		list01.add("第七个元素");

		list.addAll(list01);
	}

	/*
	 * 遍历：迭代器 hasNext():表示是否有下个元素，如果有返回true，否则false
	 * next():返回当前光标的元素，同时向下移一位(注意：只要使用一次便向下移动一位)
	 */
	public static void iteraterTest(List<String> list) {
		Iterator iterator = list.iterator();

		System.out.println("迭代器遍历");
		while (iterator.hasNext()) {
			System.out.println(iterator.next());
		}
		System.out.println("Capacity："+list.size());
		
		//移除操作
		list.remove(0);
		//替换操作
		list.set(0,"已被替换");
		
		System.out.println("\nforeach遍历");
		for(String s : list) {
			 System.out.println(s);
		}
		
	}

	public static void main(String[] args) {
		
		//采用实现类来实例化接口，也采用泛型机制
		//List list = new ArrayList();
		List<String> list=new ArrayList<String>();
		
		
		//添加
		addTest(list);
		addAllTest(list);
		
		//遍历
		iteraterTest(list);
		
		//查找
		System.out.println("集合中是否包含“第三个元素”："+list.contains("第三个元素"));
		System.out.println("集合中是否包含“第三个元素”："+list.contains(new String("第三个元素")));
		
		//清除
		list.clear();
		System.out.println("集合中是否包含“第三个元素”："+list.contains("第三个元素"));
	}


}

```

### <font color="red">注意</font>

对于引用数据类型（例如类），线性表或图存储的是对象的引用。将它们添加到线性表或者图中时，不要在添加的过程中修改同一个对象的内容，可以每一次添加时创建一个新的对象，否则添加进去的都是同一个引用。

封装类

```
package com.java12;

public class Room {
	String id;
	int price;
	
	public Room(){
		
	}
	
	public Room(String id, int price){
		this.id=id;
		this.price=price;
	}

	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "房号："+id+", 房价："+price;
	}
	
	
}

```

线性表

```
package com.java12;

import java.util.ArrayList;

public class RoomList {

	public static void main(String[] args) {
		Room[] rArr=new Room[3];
		rArr[0]=new Room("101", 100);
		rArr[1]=new Room("102", 200);
		rArr[2]=new Room("103", 300);
		
		ArrayList<Room> list=new ArrayList<Room>();
		
		Room r=new Room();
		for(int i=0; i<3; i++){
			
			//这种方式将导致存入内容的全是和rArr[2]一样的，因为始终是一个对象的引用
			//r.id=rArr[i].getId();
			//r.price=rArr[i].getPrice();
			
			//这种方式不会存入导致相同，因为r每次的引用不同
			//r=rArr[i];
			
			//或者可以将r直接声明在循环内
			
			list.add(r);
		}
		for(int i=0; i<3; i++){
			System.out.println(list.get(i));
		}
	}

}

```