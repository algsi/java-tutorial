# <font color="red">Java内部类</font>

## 嵌套类

定义在一个类内部的类，叫作“嵌套类”。嵌套类分为两种：静态的的和非静态的。后者又有一个专门的名字，叫作“内部类”。如下示例：

```java
class OuterClass {
    ...
    static class StaticNestedClass {
        ...
    }
    class InnerClass {
        ...
    }
}
```

嵌套类同时也是外部类的成员。内部类可以访问所在类的所有成员，即使该成员是private的。而static嵌套类则不得访问所在类的静态成员。同时，嵌套类，static和非static的，都可以被声明为private、public、protected和default的。

#### static嵌套类

因为static嵌套类不能直接访问所在类的非static成员变量和方法，所以static嵌套类必须通过绑定所在类的实例来进行访问。而对于所在类的静态成员和方法包括private、protected和public修饰的，可以访问。因为它也有static修饰。

static嵌套类通过写出封装的类名来进行实例化和访问其内部成员：

```java
OuterClass.StaticNestedClass nestedObject =
2      new OuterClass.StaticNestedClass();
```

使用示例：

```java
public class Test {
	
	public static void main(String[] args) {
		OuterClass.StaticNestedClass nest=new OuterClass.StaticNestedClass();
		nest.print();
	}
}

class OuterClass{
	
	static private int num=10;
	private String str="abc";
	
	static class StaticNestedClass{
		public void print() {
			System.out.println(num);
			
			//can't access this variable
			//System.out.println(str);
		}
	}
}
```


## 内部类

### 1. 静态内部类（内部类中最简单的形式）

声明在类体部，方法体外，并且使用static修饰的内部类

访问特点可以类比静态变量和静态方法

脱离外部类的实例独立创建

- 在外部类的外部构建内部类的实例

	new Outer.Inner();

- 在外部类的内部构建内部类的实例

	new Inner();

静态内部类体部可以直接访问外部类中所有的静态成员，包含私有。

```java
package com.general;

public class Test {
	
	public static void main(String[] args) {
		 StaticOuter.StaticInner si = new StaticOuter.StaticInner();  
	        si.test2();  
	        System.out.println("si.b = "+si.b);  
	        System.out.println("si.a = "+si.a);  
	}
}

class StaticOuter {  
	
	private int a = 100;  
	private static int b = 150;  
	public static void test(){  
	    System.out.println("Outer static test ...");  
	}  
	public void test2(){  
	    System.out.println("Outer instabce test ...");  
	}     
  
	/*内部类*/
    static class StaticInner {  
        public  int a = 200;  
        static int b =300;  
        public static void test(){  
            System.out.println("Inner static test ...");  
        }  
        public  void test2(){  
            System.out.println("Inner instance test ...");  
            StaticOuter.test();  
            new StaticOuter().test2();  
            System.out.println("StaticOuter.b  = "+StaticOuter.b);  
        }     
    }  
} 
```

### 2. 成员内部类(非静态嵌套类)

- 没有使用static修饰的内部类。

- 在成员内部类中不允许出现静态变量和静态方法的声明。

- static只能用在静态常量（final）的声明上。

- 成员内部类中可以访问外部类中所有的成员(变量，方法)，包含私有成员，如果在内部类中定义有和外部类同名的实例变量，访问方式：OuterClass.this.outerMember;

- 构建内部类的实例，要求必须外部类的实例先存在

	外部类的外部/外部类的静态方法：new Outer().new Inner();

    在外部类里面的实例方法：
    - new Inner();
    - this.new Inner();

### 局部内部类:

- 定义在方法体，甚至比方法体更小的代码块中

- 类比局部变量。

- 局部内部类是所有内部类中最少使用的一种形式。

- 局部内部类可以访问的外部类的成员根据所在方法体不同。
    如果在静态方法中：可以访问外部类中所有静态成员，包含私有
    如果在实例方法中：可以访问外部类中所有的成员，包含私有。

- 局部内部类可以访问所在方法中定义的局部变量，但是要求局部变量必须使用final修饰。

### 匿名内部类

- 顾名思义，是没有名字的局部内部类，而那些有名字的内部类，也称有名内部类。匿名内部类由于没有类名而不能单独存在，定义匿名内部类的同时须直接实例化该类。

- 没有class,interface,implements,extends关键字

- 没有构造器。

- 一般用来隐式的继承某一个父类或者实现某一个接口

- 可以在匿名内部类中添加新的属性和方法，但是这些属性和方法不能被上转型对象所调用，只能被非上转型对象方式创建的匿名内部类对象所调用

- 在局部变量作用的范围内，如果定义的内部类需要使用该局部变量，则该变量必须有final修饰，但是从 Java 8开始，如果定义的内部类需要使用该局部变量，则该变量可以不使用final修饰。

```java
package com.general;

interface Pen {  
    public void write();  
}  

/*实现类*/
class  Pencil implements Pen {  
    @Override  
    public void write() {  
    }  
}

class Person {  
    public void user(Pen pen) {  
        pen.write();  
    }  
}  

public class Test {
	
	public static void main(String[] args) {
		Person li = new Person();  
        
        /*使用接口*/
        li.user(new Pen() {  
            @Override  
            public void write() {  
                System.out.println("写字");  
            }  
        }); 
        
        /*使用接口的实现类*/
        li.user(new Pencil() {
        	@Override
        	public void write() {
        		System.out.println("写字");
        	}
        });
	}
}

```

## 特点

内部类是一个独立的类：编译之后内部类会被编译成独立的.class文件，如果该内部类为有名内部类，则有名内部类字节码文件名为外部类的类名+$+内部类类名；如果为匿名内部类，则匿名内部类字节码文件名为外部类的类名+$+数字。
