package com.li.collection;

import java.util.HashSet;

public class HashSetLearning {

	public static void main(String[] args) {
		People p1 = new People("Jack", 18);
		People p2 = new People("Jack", 18);
		People p3 = new People("Rose", 18);
		People p4 = new People("Jack", 20);
		HashSet<People> hashset = new HashSet<People>();
		
		hashset.add(p1);
		hashset.add(p2);
		hashset.add(p3);
		hashset.add(p4);
		
		System.out.println(hashset);
		System.out.println(hashset.size());
	}

}
