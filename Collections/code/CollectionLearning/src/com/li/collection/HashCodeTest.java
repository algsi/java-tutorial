package com.li.collection;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
 
/*һ������ֻ����һ����public���ε���*/ 
class People{
	
    private String name;
    private int age;
     
    public People(String name,int age) {
        this.name = name;
        this.age = age;
    }  
     
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

	@Override
    public int hashCode(){
    	return this.name.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj==null)
        	return false;
        if(this==obj)
        	return true;
        if(this.getClass()!=obj.getClass())  //��������Ƿ�һ�£���ʹ��instanceof�ؼ���
        	return false;
        return this.name.equals(((People)obj).name) && this.age== ((People)obj).age;
    }
    
    @Override
    public String toString(){
    	return "name��"+name+"  age��"+age;
    }
}
 
public class HashCodeTest {
 
    public static void main(String[] args) {
         
        People p1 = new People("Jack", 12);
        System.out.println(p1.hashCode());
             
        HashMap<People, Integer> hashMap = new HashMap<People, Integer>();
        hashMap.put(p1, 1);
         
        /*
         * ��Ϊ��д��hasocode���������Դ˴����Ϊ1��
         * ����дhashcode���������null
         */
        System.out.println(hashMap.get(new People("Jack", 12)));
    }
}