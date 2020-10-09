package com.li.collection;

import java.util.Comparator;

/*一个用于People类的外比较器*/

public class PeopleComparator implements Comparator<People> {

	@Override
	public int compare(People o1, People o2) {
		//先按年龄比较，如果年龄相等，则按姓名
		if(o1.getAge()==o2.getAge())
			return o1.getName().compareTo(o2.getName());
		else
			return o1.getAge()-o2.getAge();
	}

}
