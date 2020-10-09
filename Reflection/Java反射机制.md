# Java反射机制

## 反射概述

反射的概念是由 Smith 在1982年首次提出的，主要是程序可以访问、检测和修改它本省状态或行为的一种能力。

Java反射机制是在<font color="red">运行状态</font>中，对于任意一个类，都能够知道这个类所有的属性和方法；对于任意一个对象，都能够调用它的任意一个方法；这种动态获取信息以及动态调用对象的方法的功能称为Java语言的反射机制。

简单的来说，反射机制是指程序在运行时能够获取自身的信息。在Java中，只要给定类的名字，那么就可以通过反射机制来获得类的所有信息。包括其反问修饰符、父类、实现的接口、属性和方法的所有信息，并可以在运行时创建对象、修改属性（包括私有的）、调用方法（包括私有的）。

##### 为什么要使用反射机制？

为什么要使用反射机制？直接创建对象不就可以了吗，这就涉及到动态与静态的概念。

- 静态编译：在编译时就确定类型，绑定对象，即通过

	```java
		Student stu=new Student();
    ```

- 动态编译：运行时确定类型，绑定对象。动态编译最大限度发挥Java的灵活性，体现了多态的应用，用以降低类之间的耦合性。

	```java
		Class.forName("com.mysql.jdbc.Driver.class").newInstance();
    ```
一句话，反射机制的优点就是可以实现动态创建对象和编译，体现出很大的灵活性，特别是在J2EE的开发中。

它的缺点是对性能的影响。使用反射基本上是一种解释操作，这类操作总是慢于直接执行的相同操作。

##### Java反射机制主要提供了以下功能

- 在运行时判断任意一个类对象所属的类

	instanceof关键字：二元操作符，判断左边对象是否为右边类的实例，返回Boolean值

	要得到一个对象的类，可以使用getClass()方法。我们也会使用 == 来判断Class类型的对象是否相等，因为一种类型的对象的getClass()方法一定能够返回相同的引用。

- 在运行时构造任意一个类的对象

- 在运行时判断任意一个类所具有的成员变量和方法

- 在运行时调用任意一个对象的方法

## Class对象

Class对象是Reflection的起源。要想操纵类中的属性和方法，都必须从获取Class对象开始。

类是程序的一部分，每个类都有一个Class对象。换言之，每当编译了一个新的类，就会产生与之对应的Class对象。

Class类没有公共的构造方法。Class对象是在加载类时，由Java虚拟机以及通过调用类加载器中的方法自动加构造的，因此不能显式的声明一个Class对象。

##### 获取Class对象的方法

- object.getClass()：获取指定实例的Class

	```java
	String str="abc";
    Class c1=str.getClass();
    ```

- class.getSuperclass：获取当前Class的父类的Class

	```java
	List list=new ArrayList();
    Class listClass=list.getClass();
    Class superClass=listClass.getSupserclass();
    ```

- .class语法：.class直接获取

	```java
    Class listClass=ArrayList.class;
    ```

- Class.forName(类名)：用Class的静态方法，传入类名即可

	```java
	List list=new ArrayList();
    Class listClass=list.getClass();
    Class superClass=listClass.getSupserclass();
    ```

- Primitive TYPE：基本数据类型的包装类获取Class方式

    ```java
    Class longClass=Long.TYPE;
    Class integerClass=Integer.TYPE;
    Class voidClass=Void.TYPE;
	```

## 示例

```java
package com.li.classtype;

public class ClassDemo {
	
	public static void main(String[] args) {
		
		/*第一种：getClass()*/
		Employee employee=new Employee("Li", 21);
		Class<?> classType=employee.getClass();
		/*获取全类名*/
		System.out.println(classType.getName());
		/*获取超类对应的Class类*/
		System.out.println(classType.getSuperclass().getName());
		
		/*第二种：.class*/
		Class<?> classType1=Employee.class;
		
		/*第三种：Class.forName()*/
		try {
			Class<?> classType2=Class.forName("com.li.classtype.Employee");
			System.out.println(classType2.getName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		/*获取基本数据类型的Class对象*/
		/*基本数据类型没有父类*/
		Class<?> classType3=int.class;
		System.out.println(classType3.getName());
		/*NullPointException*/
		System.out.println(classType3.getSuperclass().getName());
		
		/*获取基本数据类型的Class对象，通过包装类TYPE属性获取对应基本数据类型的Class对象*/
		Class<?> classType4=Double.TYPE;
		System.out.println(classType4.getName());
		/*NullPointException*/
		System.out.println(classType4.getSuperclass().getName());
		
		/*获取基本数据类型的包装类的Class对象，通过包装类TYPE属性获取对应基本数据类型的Class对象*/
		Class<?> classType5=Double.class;
		System.out.println(classType5.getName());
		/*java.lang.Number*/
		System.out.println(classType5.getSuperclass().getName());
	}
}


class Employee{
	private String name;
	private int age;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public Employee(String name, int age) {
		super();
		this.name = name;
		this.age = age;
	}
	
}
```