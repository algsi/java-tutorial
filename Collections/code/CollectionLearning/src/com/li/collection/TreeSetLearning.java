package com.li.collection;

import java.util.TreeSet;

public class TreeSetLearning {

	public static void main(String[] args) {
		People p1 = new People("Jack", 18);
		People p2 = new People("Jack", 18);
		People p3 = new People("Rose", 18);
		People p4 = new People("Jack", 20);
		
		//指定TreeSet集合里边的对象按照这个外比较器的规则进行排序
		TreeSet<People> hashset = new TreeSet<People>(new PeopleComparator());
		
		hashset.add(p1);
		hashset.add(p2);
		hashset.add(p3);
		hashset.add(p4);
		
		System.out.println(hashset);
		System.out.println("capacity："+hashset.size());

	}

}
