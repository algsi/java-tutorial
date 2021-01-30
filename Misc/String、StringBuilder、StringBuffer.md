# <font color="red">String、StringBuilder、StringBuffer</font>

三者执行速度：StringBuilder > StringBuffer > String

- 都是 final 类, 都不允许被继承;
- String 长度是不可变的, StringBuffer、StringBuilder 长度是可变的;
- StringBuffer 是线程安全的, StringBuilder 不是线程安全的，但它们两个中的所有方法都是相同的，StringBuffer 在 StringBuilder 的方法之上添加了synchronized修饰，保证线程安全。
- StringBuilder 因为不需要锁，所以比 StringBuffer 拥有更好的性能。
- 如果一个 String 类型的字符串，在编译时就可以确定是一个字符串常量，则编译完成之后，字符串会自动拼接成一个常量。此时String的速度比 StringBuffer 和 StringBuilder 的性能好的多。


程序测试

```

package com.general;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Test {

	private static final String base = "base String. ";
	private static final int count = 2000000;

	public static void stringTest() {
		long begin, end;
		begin = System.currentTimeMillis();
		String test = new String(base);

		for (int i = 0; i < count / 100; i++)
			test = test + "add";
		end = System.currentTimeMillis();
		System.out.println((end - begin) + "millis has elapsed when used String.");
	}

	public static void stringBufferTest() {
		long begin, end;
		begin = System.currentTimeMillis();
		StringBuffer test = new StringBuffer(base);

		for (int i = 0; i < count / 100; i++)
			test = test.append("add");
		end = System.currentTimeMillis();
		System.out.println((end - begin) + "millis has elapsed when used StringBuffer.");
	}

	public static void stringBuilderTest() {
		long begin, end;
		begin = System.currentTimeMillis();
		StringBuilder test = new StringBuilder(base);

		for (int i = 0; i < count / 100; i++)
			test = test.append("add");
		end = System.currentTimeMillis();
		System.out.println((end - begin) + "millis has elapsed when used StringBuilder.");
	}

	public static String appendItemsToStringBuilder(List<String> list) {
		StringBuilder b = new StringBuilder();
		for (Iterator i = list.iterator(); i.hasNext();) {
			b.append(i.next()).append(" "); // 连续调用
		}
		return b.toString();
	}

	public static void addToStringBuilder() {
		List<String> list = new ArrayList<String>();
		list.add("I");
		list.add("play");
		list.add("Boureois");
		list.add("guitars");
		list.add("and");
		list.add("Huber");
		list.add("banjos");
		System.out.println(Test.appendItemsToStringBuilder(list));
	}

	public static String appendItemsToStringBuffer(List<String> list) {
		StringBuffer b = new StringBuffer();
		for (Iterator i = list.iterator(); i.hasNext();) {
			b.append(i.next()).append(" "); // 连续调用
		}
		return b.toString();
	}
	
	public static void addToStringBuffer() {
		List<String> list = new ArrayList<String>();
		list.add("I");
		list.add("play");
		list.add("Boureois");
		list.add("guitars");
		list.add("and");
		list.add("Huber");
		list.add("banjos");
		System.out.println(Test.appendItemsToStringBuffer(list));
	}
	
	public static void main(String[] args) {
		stringTest();
		stringBufferTest();
		stringBuilderTest();
		addToStringBuffer();
		addToStringBuilder();
	}
}
```