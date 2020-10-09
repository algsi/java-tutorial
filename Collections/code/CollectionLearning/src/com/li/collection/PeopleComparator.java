package com.li.collection;

import java.util.Comparator;

/*һ������People�����Ƚ���*/

public class PeopleComparator implements Comparator<People> {

	@Override
	public int compare(People o1, People o2) {
		//�Ȱ�����Ƚϣ����������ȣ�������
		if(o1.getAge()==o2.getAge())
			return o1.getName().compareTo(o2.getName());
		else
			return o1.getAge()-o2.getAge();
	}

}
