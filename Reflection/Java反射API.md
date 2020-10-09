# Java反射API

## <font color="red">java.lang.reflect 库</font>

Class类和java.lang.reflect类库一起对反射的概念进行支持。

- java.lang包下：

	Class&lt;T&gt;：表示一个正在运行的Java应用程序中的类和接口，是Reflection的起源。

- java.lang.reflect包下：

	- Field 类：代表类的成员变量（成员变量也称为类的属性）
	- Method 类：代表类的方法
	- Constructor 类：代表类的构造方法
	- Array 类：提供了动态创建数组，以及访问数组元素的静态方法

## <font color="red">通过反射实例化对象</font>

平常情况我们通过 new Object 来生成一个类的实例，但有时候我们没法直接new，只能通过反射动态生成。比如，我们知道描述一个类的完整字符串路径，这时候我们无法通过new去生成类对象，只能通过反射来实例化。

#### 实例化无参构造函数的的对象

两种方式

- classType.newInstance();

- classType.getConstructor(new Class[]{}).newInstannce(new Object[]{})

    new Class[]{}表示参数类型对应的Class对象数组，因为无参，所有花括号中为空的，new Object[]{}表示参数数组，也为空。

#### 实例化带参构造函数的对象

- class.getConstructor(Class<?>...parameterTypes).newInstance(Object...initargs)

    Class<?>...parameterTypes：参数对应的Class类型数组
    Object...initargs：参数数组

	<font color="red">如果我们不传入参数，不能只传入null，应该换成new Class[]{}或者new Object[]{}，数组为空</font>

#### 获得当前类以及超类的公共（public）Method

- Method arrMethods = classType.getMethods();

#### 获得当前类所有声明的 Method

获得包括私有方法

- Method arrMethods = classType.getDeclaredMethods();

#### 获得当前类以及超类指定的public Method

- Method method = classType.getMethod(String name, Class<?>...parameterTypes);

	name：指定的方法名

	Class<?>...parameterTypes：方法里参数类型对应的Class类型对象的数组

#### 获得当前类声明指定的 Method

- Method method = classType.getDeclaredMethod(String name, Class<?>...parameterTypes);

	name：指定的方法名

	Class<?>...parameterTypes：方法里参数类型对应的Class类型对象的数组

#### 通过反射动态运行指定的Method

- Object return = method.invoke(Object obj, Object...args);

	return：返回的内容

	obj：对象名，调用的方法是obj对象的方法

	Object...args：参数数组，应使用new Object[]{}方式传入

#### 获得当前类以及超类的 public Filed

- Field[] arrFields = classType.getFields();

#### 获得当前类所有声明的 Filed

- Field[] arrFields = classType.getDeclaredFields();

#### 获得当前类以及超类指定的public Field

- Field field = classType.getField(String name);

	name：指定的属性

#### 获得当前类声明的指定的 Field

- Field field = classType.getDeclaredField(String name);

#### 通过反射动态设置Field的值

- field.set(Object obj, Object value);

	obj：该属性值所属的对象

	value：值

#### 通过反射动态获取Field的值

- Object return = field.get(Object obj)

	return：返回值

	obj：该属性值所属的对象

## <font color="red">使用示例</font>

```java
package com.li.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionAPIDemo {

	/*将异常往外抛*/
	public static void main(String[] args) throws Exception{
		
		/*获取类所关联的Class对象*/	
		Class<?> classType = Class.forName("com.li.reflection.Employee");
		
		/*通过反射来构造一个实例对象，返回的是一个Object类型，实际上是Employee类型，newInstance调用的是无参构造方法*/	
		Employee employee = (Employee) classType.newInstance();
		
		/*调用指定的构造方法来构造对象（无参构造方法），参数为空*/
		Constructor<?> constructor01 = classType.getConstructor(new Class[] {});
		Employee employee01 = (Employee)constructor01.newInstance(new Object[] {});
		System.out.println(employee01);
		
		/*调用指定的构造方法来构造对象（带参数的构造方法）*/
		Constructor<?> constructor02 = classType.getConstructor(new Class[] {String.class, int.class});
		Employee employee02 = (Employee)constructor02.newInstance(new Object[] {"太阳", 100});
		System.out.println(employee02);
		
		System.out.print("\n<--获取指定方法并调用-->");
		/*获取Class对象指定的方法，包括私有的*/
		Method method = classType.getDeclaredMethod("toString", new Class[] {});
		System.out.println();
		System.out.println(method.getName());
		/*方法的调用，需要指明对象，此方法无参，因此参数为空*/
		String desc =(String) method.invoke(employee02, new Object[] {});
		System.out.println(desc);
		
		System.out.println("\n<--获取所有方法，包含私有的-->");
		/*取Class对象所有声明的方法，包括私有的*/
		Method[] methods = classType.getDeclaredMethods();
		System.out.println();
		for(Method m : methods) {
			System.out.println("方法名称："+m.getName());
			System.out.println("方法访问修饰符："+m.getModifiers());
			System.out.println("方法返回类型："+m.getReturnType());
			System.out.println();
		}
		
		System.out.println("<--调用私有方法-->");
		/*访问调用私有方法*/
		Method pm = classType.getDeclaredMethod("work", new Class[] {});
		System.out.println(pm.getName());
		/*设置私有方法也有被访问的权限*/
		pm.setAccessible(true);
		pm.invoke(employee, new Object[] {});
		
		System.out.println("\n<--访问私有属性-->");
		/*获取Class指定的属性，包括私有的*/
		Field field = classType.getDeclaredField("name");
		/*设置私有属性也有被访问的权限*/
		field.setAccessible(true);
		field.set(employee, "Li");
		
		System.out.println(field.get(employee));
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
	public Employee() {
		System.out.println("调用无参构造方法");
	}
	public Employee(String name, int age) {
		super();
		this.name = name;
		this.age = age;
	}
	@Override
	public String toString() {
		return "Employee [name=" + name + ", age=" + age + "]";
	}
	private void work() {
		System.out.println("working...");
	}
	
}

```

运行结果

```
调用无参构造方法
调用无参构造方法
Employee [name=null, age=0]
Employee [name=太阳, age=100]

<--获取指定方法并调用-->
toString
Employee [name=太阳, age=100]

<--获取所有方法，包含私有的-->

方法名称：toString
方法访问修饰符：1
方法返回类型：class java.lang.String

方法名称：getName
方法访问修饰符：1
方法返回类型：class java.lang.String

方法名称：setName
方法访问修饰符：1
方法返回类型：void

方法名称：work
方法访问修饰符：2
方法返回类型：void

方法名称：getAge
方法访问修饰符：1
方法返回类型：int

方法名称：setAge
方法访问修饰符：1
方法返回类型：void

<--调用私有方法-->
work
working...

<--访问私有属性-->
Li

```

## <font color="red">反射创建数组的使用</font>

```java
package com.li.reflection;

import java.lang.reflect.Array;

public class ReflectionArrayDemo {

	public static void main(String[] args) throws Exception{

		/*创建一个一维数组*/
		Class<?> classType=Class.forName("java.lang.String");
		/*使用指定类型的Class对象和长度创建，返回的是Object类型数组*/
		Object array = Array.newInstance(classType, 5);
		/*赋值*/
		Array.set(array, 3, "abc");
		/*取值*/
		System.out.println(Array.get(array, 3));
		
		/*创建二维数组，3行3列*/
		/*先指定维度*/
		int[] dimens= {3, 3};
		Object arr = Array.newInstance(int.class, dimens);
		Object arrObj = Array.get(arr, 2);              //获取第三行，也就是一个一维数组
		Array.setInt(arrObj, 2, 10);                    //给指定位置赋值
		int [][] arrInt = (int [][])arr;
		System.out.println(arrInt[2][2]);
	}

}

```

## <font color="red">总结</font>

- 只要用到反射，先获得Class对象

- 没有方法能获得当前类的超类的private方法和属性，你必须通过getSuperclass()找到超类以后再去尝试获取。

- 通常情况下，即便是当前类，private方法或属性也是没有权限访问的，你需要设置压制权限setAccessible(true)来取得访问权限，但实际上，这已经破坏了规则，所以应该尽量少使用。