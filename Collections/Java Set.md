# <font color="red">Java集合类-Set</font>

概要图

![Java集合类.jpg](https://img-blog.csdn.net/20180411230300733)

## 集合Set

Java中使用Set接口描述一个集合（集合不允许有“重复值”，注意重复的概念），集合Set是Collection的子接口，Set不允许其数据元素重复出现，也就是说在Set中每一个数据元素都是唯一的。Set接口定义的常用方法如下：

| 序号 | 方法 | 描述 |
|--------|--------|--------|
|    1    |   size()     |   获取Set尺寸（即Set包含数据元素的总数）     |
|    2   |    add(Object obj)    |    向Set中添加数据元素obj    |
|    3    |     remove(Object obj)   |    从Set中移除数据元素obj    |
|    4    |     contains(Object obj)   |    判断当前Set中是否包含数据元素obj，如果包含返回true，否则返回false    |
| 5 | iterator() | 将Set装入迭代器 |

Set接口的交集、并集、差集运算

| 序号 | 方法 | 描述 |
|--------|--------|-----|
| 1 | addAll(Collection c)    | 向Set中添加c包含的全部数据元素（并集）|
| 2 | removeAll(Collection c) | 从Set中移除c包含的全部数据元素（差集）|
| 3 | retainAll(Collection c) | 求Set和c的交集 |
| 4 | containsAll(Collection c) | 判断Set是否包含c中的全部数据元素，如果全部包含返回true，否则返回false |

## HashSet（散列集）

- HashSet通过Hash算法排布集合内的元素，所谓的Hash算法就是把任意长度的输入（又叫做预映射），通过散列算法，变换成固定长度的输出，该输出就是散列值。这种转换是一种压缩映射。对于不同类型的信息，其散列值公式亦不完全相同。

- 当我们使用HashSet存储自定义类时，需要在自定义类中重写equals和hashCode方法，主要原因是集合内不允许有重复的数据元素，在集合校验元素的有效性时（数据元素不可重复），需要调用equals和hashCode验证。

- HashSet在判断数据元素是否重复时，有两个步骤，注意先后顺序（此处可以看一看我的另一篇博客：[浅析Java中的hashcode方法](https://blog.csdn.net/sinat_37976731/article/details/78735332)）：

    1. 先检查hashCode值是否与集合中已有相同。
    2. 如果hashCode相同再调用equals方法进一步检查。（equals返回真表示重复）

## TreeSet（树集）

- TreeSet是一个**有序**集合，其元素按照升序排列，默认是按照自然顺序排列，也就是说TreeSet中的对象元素需要实现Comparable接口来实现自比较功能。TreeSet类中跟HashSet类一样也没有get()方法来获取指定位置的元素，所以也只能通过迭代器方法来获取。

- TreeSet虽然是有序的，但是并没有具体的索引，当插入一个新的数据元素的时候，TreeSet中原有的数据元素可能需要重新排序，所以TreeSet插入和删除数据元素的效率较低。

- 当我们使用TreeSet存储自定义类时，需要在自定义类实现Comparable接口并重写其compareTo方法，以提供比对形式，否在TreeSet不能对用户自定义的类型进行正确的树状排序。


*******

## Example

#### HashSet存储自定义类(这里假设为学生类)

- 重写hashCode()方法：

     返回需要比较属性的hashCode()方法值，如果需要根据学生学号来区分对象，则

```java
@Override
public int hashCode() {
return stuNo.hashCode();
}
```

- 重写equals方法：去重条件为学号相同

```java
public boolean equals(Object o){
		if(o == null){
			return false;
		}else{
			if(this == o){              //引用相同则必定相同
				return true;
			}else{
				//
				if(o instanceof Student){ //类型检测关键字
					Student stu = (Student) o;  //需要强制转换
					if(stu.getStuNo().equals(getStuNo())){
						return true;
					}
				}
				return false;
			}
		}
	}
```

### TreeSet存储自定义类

需要提供比较器 Comparable or Comparator。

- 在自定义类中重写compareTo()方法，以提供比对形式，否在TreeSet不能对用户自定义的类型进行正确的树状排序

- 重写compareTo()方法以此实现Compareble接口，在下面的比较方法中，先比较学生的成绩，如果成绩相同，再比较学生学号

```Java
public class Student extends PStu implements Comparable<Student>{
 	//采用泛型
	private String stuNo;
	private String stuName;
	private int score;
	@Override
	public int compareTo(Student stu) {
		if(stu != null){
			if(score == stu.score){    //比较成绩
				return stuNo.compareTo(stu.getStuNo());  //比较学号，调用String类的CompareTo方法（String类也实现了Comparable接口）
			}
			return score - stu.score;
		}
		return 0;
	}

}
```

上面使用的是内比较器，在往TreeSet集合中添加时将自比较进行排序，这种需要对我们使用的类做改变（实现Comparable接口重写compareTo方法），我们也可以使用外比较器来提供排序规则。例如：

这是我们的一个People类:

```java
public class People{
	
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
        if(this.getClass()!=obj.getClass())  //检查类型是否一致，或使用instanceof关键字
        	return false;
        return this.name.equals(((People)obj).name) && this.age== ((People)obj).age;
    }
    
    @Override
    public String toString(){
    	return "name："+name+"  age："+age;
    }
}
```

我们为People类写了一个外比较器：

```java
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

```

测试：

```java
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

```

