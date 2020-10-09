package com.li.collection;

import java.util.ArrayList;
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
