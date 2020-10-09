# Java 异常

- 异常：在Java语言中，将程序执行中发生的不正常情况称为“异常”。(开发过程中的语法错误和逻辑错误不是异常)

- Java程序在执行过程中所发生的异常事件可分为两类：

	- Error:  Java虚拟机无法解决的严重问题。如：JVM系统内部错误、资源耗尽等严重情况。一般不编写针对性的代码进行处理
	- Exception: 其它因编程错误或偶然的外在因素导致的一般性问题，可以使用针对性的代码进行处理

- 异常分类

	
![异常.jpg](D:\桌面\TempPhoto\异常.jpg)


## 处理异常的机制

- Java采用异常处理机制，将异常处理的程序代码集中在一起，与正常的程序代码分开，使得程序简洁，并易于维护。

- Java提供的是异常处理的抓抛模型。

- Java程序的执行过程中如出现异常，会生成一个异常类对象，该异常对象将被提交给Java运行时系统，这个过程称为抛出(throw)异常。

- 异常对象的生成

	- 由虚拟机自动生成：程序运行过程中，虚拟机检测到程序发生了问题，如果在当前代码中没有找到相应的处理程序，就会在后台自动创建一个对应异常类的实例对象并抛出——自动抛出

	- 由开发人员手动创建：Exception exception = new ClassCastException();

        创建好的异常对象不抛出对程序没有任何影响，和创建一个普通对象一样。

- 如果一个方法内抛出异常，该异常对象会被抛给调用者方法中处理。如果异常没有在调用者方法中处理，它继续被抛给这个调用方法的上层方法。这个过程将一直继续下去，直到异常被处理。这一过程称为捕获(catch)异常。

- 如果一个异常回到main()方法，并且main()也不处理，则程序运行终止。

- 程序员通常只能处理Exception，而对Error无能为力。

## 异常关键字

| 关键字 | 作用 |
|--------|--------|
| try | 用于封装可能出现异常的代码段 |
| catch | 捕获异常区域，如果被封装到try中的代码真的出现异常，程序将跳转到catch区域，并且在catch区域中可以得到异常的信息 |
| throw | 抛出异常到其调用者 |
| throws | 用于声明当前方法可能会出现异常，但是方法体内并没有处理异常的机制，如果要使用该方法，需要调用者处理可能发生的异常信息 |
| finally | 即使程序出现异常，仍然要执行的代码段 |

## 常见异常

| 异常 | 描述 |
|--------|--------|
| RuntimeException | java.lang包中多数异常的基类 |
| ArithmeticException | 算术错误，如分母为 0 |
| IllegalArgumentException | 方法收到非法参数 |
| ArrayIndexOutOfBoundsException | 数组下标出界 |
| NullPointerException | 空指针异常，试图访问 null 对象引用 |
| SecurityException | 试图违反安全性 |
| ClassNotFoundException | 不能加载请求的类 |
| AWTException | AWT 中的异常 |
| IOException | I/O 异常的根类 |
| FileNotFoundException | 不能找到文件 |
| EOFException | 文件结束 |
| IllegalAccessException | 对类的访问被拒绝 |
| NoSuchMethodException | 请求的方法不存在 |
| InterruptedException | 线程中断 |

## Example

```java
public class ExceptionDemo {

	public static void main(String[] args) {

		String[] test=new String[9];
		
		//将可能会出现异常的代码使用try catch包围起来
		try {
			String stuNo=test[9];                       //此处stuNo将变成局部变量，出了代码块将无效
			int a=19;
			System.out.println("====>"+a/0);
		} catch (ArrayIndexOutOfBoundsException e) {    //捕获异常
			System.out.println("数组下标越界!");
			e.printStackTrace();
		} catch (ArithmeticException e){
			System.out.println("算术异常！");
			e.printStackTrace();
		}finally {
			System.out.println("仍然要执行的部分");
		}
		
		try {
			getArrayIndexVale(test, 10);
		} catch (ArrayIndexOutOfBoundsException e) {    //捕获异常
			e.printStackTrace();
		}
		System.out.println(">==简单的输出==<");   //此处还能继续执行
		
	}
	
	//可以抛出多个异常
	public static String getArrayIndexVale(String[] array, int index) throws ArrayIndexOutOfBoundsException, NullPointerException{
		try {
			return array[index];
		} catch (ArrayIndexOutOfBoundsException e) {
			 //抛出异常
			throw new ArrayIndexOutOfBoundsException("下标越界："+index);          
		}
	}

}

```

## 自定义异常

自定义一个余额不足异常并继承RuntimeException异常

```java
package com.Exception;


/**
 * 自定义：余额不足异常
 * 形成来自父类的构造方法：右键->source->Generate Constructors from Superclass
 * @author Vinsmoke
 *
 */
public class InsufficiFundException extends RuntimeException{

	public InsufficiFundException() {
		super();
	}

	public InsufficiFundException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public InsufficiFundException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public InsufficiFundException(String arg0) {
		super(arg0);
	}

	public InsufficiFundException(Throwable arg0) {
		super(arg0);
	}
	

}

```

抛出异常

```java
package com.Exception;

public class StuSystemManager {

	public static void main(String[] args) {

		payMoney();

	}

	/* 用于声明当前方法可能会出现异常 */
	public static void payMoney() throws InsufficiFundException {
		double money = 25.34;
		if (money < 240) {
			// 提示用户余额不足
			throw new InsufficiFundException("余额不足，请充值！");
		}
	}

}

```

## 常见的异常抛出与处理

#### 自己捕获异常并处理异常

```java
package com.general;

public class Test {
	
	public static void exception(){
		try {
			int a=1/0;
			System.out.println(a);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
        	System.out.println("出现异常也要执行的代码");
        }
		System.out.println("后面执行的代码");
		
	}
	
	
	public static void main(String[] args) {
		exception();
	}
}
```

运行结果：

```language
java.lang.ArithmeticException: / by zero
	at com.general.Test.exception(Test.java:7)
	at com.general.Test.main(Test.java:20)
出现异常也要执行的代码
后面执行的代码

```

我们可以看到，在方法中自己捕获异常并输出异常，而调用者不需要来处理，且在方法中，异常之后的代码仍然可以执行到。

#### 声明可能出现异常，但不处理

我们在方法中声明可能出现的异常，但是方法体内并没有处理异常的机制，如果要使用该方法，需要调用者处理可能发生的异常信息。

```java
package com.general;

public class Test {
	
	public static void exception() throws Exception{
		int a=1/0;
		System.out.println(a);
		System.out.println("后面执行的代码");
	}
	
	
	public static void main(String[] args) {
		try {
			exception();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

```

运行结果：

```
java.lang.ArithmeticException: / by zero
	at com.general.Test.exception(Test.java:6)
	at com.general.Test.main(Test.java:14)

```

我们可以看到，出现异常，后面的代码并不会被执行，并且还需要调用者来捕获异常。

#### 抛出异常

将方法可能出现的异常给抛出，一旦抛出，程序不再运行。

```java
package com.general;

public class Test {
	
	public static void exception(){
		try {
			int a = 1 / 0;
			System.out.println(a);
		} catch (Exception e) {
			throw new ArithmeticException();
		}
		System.out.println("后面执行的代码");
	}
	
	
	public static void main(String[] args) {
		exception();
		System.out.println("主方法");
	}
}

```

运行结果：

```language
Exception in thread "main" java.lang.ArithmeticException
	at com.general.Test.exception(Test.java:10)
	at com.general.Test.main(Test.java:17)

```