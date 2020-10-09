package com.li.collection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ArrayListLearning {
	
	//���
	public static void addTest(List<String> list) {
		list.add("��һ��Ԫ��");
		list.add("�ڶ���Ԫ��");
		list.add("������Ԫ��");
	}

	 //�򼯺��������һ������
	public static void addAllTest(List<String> list) {
		List list01 = new ArrayList();
		list01.add("���ĸ�Ԫ��");
		list01.add("�����Ԫ��");
		list01.add("������Ԫ��");
		list01.add("���߸�Ԫ��");

		list.addAll(list01);
	}

	/*
	 * ������������ hasNext():��ʾ�Ƿ����¸�Ԫ�أ�����з���true������false
	 * next():���ص�ǰ����Ԫ�أ�ͬʱ������һλ(ע�⣺ֻҪʹ��һ�α������ƶ�һλ)
	 */
	public static void iteraterTest(List<String> list) {
		Iterator iterator = list.iterator();

		System.out.println("����������");
		while (iterator.hasNext()) {
			System.out.println(iterator.next());
		}
		System.out.println("Capacity��"+list.size());
		
		//�Ƴ�����
		list.remove(0);
		//�滻����
		list.set(0,"�ѱ��滻");
		
		System.out.println("\nforeach����");
		for(String s : list) {
			 System.out.println(s);
		}
		
	}

	public static void main(String[] args) {
		
		//����ʵ������ʵ�����ӿڣ�Ҳ���÷��ͻ���
		//List list = new ArrayList();
		List<String> list=new ArrayList<String>();
		
		
		//���
		addTest(list);
		addAllTest(list);
		
		//����
		iteraterTest(list);
		
		//����
		System.out.println("�������Ƿ������������Ԫ�ء���"+list.contains("������Ԫ��"));
		System.out.println("�������Ƿ������������Ԫ�ء���"+list.contains(new String("������Ԫ��")));
		
		//���
		list.clear();
		System.out.println("�������Ƿ������������Ԫ�ء���"+list.contains("������Ԫ��"));
	}


}
