# Java日期类：Date以及Calendar

对于时间类，这篇主要说明各种现实情况下如何取值，怎么定向取值，得到自己想要的时间参数。在Java中时间类主要有Date、Calendar，暂时只介绍 java.util.*下的时间类（因为java.sql.*下的日期类），这篇主要内容有以下几个。

1、如何获取当前时间参数

2、如何过去某一个月第一天和最后一天 

3、如何获取当日日期，当月月份，当年年份

4、如何加减日期

5、获取两个时间之间的时间差

　　首先，我们需要了解API下对其的介绍，让在使用的时候明白为什么这么写？而不是简单的复制粘贴应付差事。

## Calendar

首先，Calendar是一个抽象方法，从而没有公开的构造方法，它给我们提供了一个获取对象实例的静态的类方法：getInstance()。

#### 方法

##### 1. getInstance

使用该方法获得的Calendar对象就代表当前的系统时间

```java
Calendar c = Calendar.getInstance();
System.out.println(c);
```

Calendar的toString方法中输出了很多有关当前时间的信息。

##### 2. set

该方法时用来设置Calendar类代表指定的时间，它有很多重载的形式。

```java
// 指定字段
public void set(int field, int value)

// 指定年月日
public final void set(int year, int month, int date)


// 指定年月日时分
public final void set(int year, int month, int date, int hourOfDay, int minute)

// 指定年月日时分秒
public final void set(int year, int month, int date, int hourOfDay, int minute, int second)
```

诚如上面所见，设置指定的时间时，我们可以传入指定的参数，从而设置某一日历属性的值。

```java
Calendar c = Calendar.getInstance();
c.set(2018, 8 - 1, 29);
```

以上示例代码设置的时间为2018年8月29日，其参数的结构和Date类不一样。Calendar类中年份的数值直接书写，月份的值为实际的月份值减1（因为月份是从零开始的），日期的值就是实际的日期值。

如果只设定某个字段某个字段，例如日期（date字段）的值，则可以使用指定字段的set方法。在指定设置字段的方法中，参数field代表要设置的字段的类型，常见类型如下表，这些都是类常量字段，用以表示不同的意义，JDK中很多类都采用了这种做法：

| 常量 | 描述 |
|--------|--------|
| Calendar.YEAR | 年份 |
| Calendar.MONTH | 月份（从0开始） |
| Calendar.DATE | 日期 |
| Calendar.DAY_OF_MONTH | 日期，和Calendar.DATE字段完全相同 |
| Calendar.HOUR | 12小时制的小时数 |
| Calendar.HOUR_OF_DAY | 24小时制的小时数 |
| Calendar.MINUTE | 分钟 |
| Calendar.SECOND | 秒 |
| Calendar.DAY_OF_WEEK | 星期几 |

后续的参数value代表设置成的值。例如：c.set(Calendar.DATE,10); 该代码的作用是将c1对象代表的时间中日期设置为10号，其它所有的数值会被重新计算，例如星期几以及对应的相对时间数值等。


##### 3. get

get() 方法就是用来获取Calendar类中的信息的。

```java
// get() method
public void getMethod() {
    Calendar c = Calendar.getInstance();
    int year = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONDAY)+1;
    int date = c.get(Calendar.DATE);
    int hour = c.get(Calendar.HOUR_OF_DAY);
    int minute = c.get(Calendar.MINUTE);
    int second = c.get(Calendar.SECOND);
    int day_week = c.get(Calendar.DAY_OF_WEEK);

    System.out.println("年："+year);
    System.out.println("月："+month);
    System.out.println("日："+date);
    System.out.println("时："+hour);
    System.out.println("分："+minute);
    System.out.println("秒："+second);
    System.out.println("星期："+day_week);
}
```

在Calendar类中，周日的代表数字是1，周一的代表数字是2，周二的代表数字是3，依次类推，所以，如果你现在是星期三的话，那么 day_week 的值是4。特别要注意月份，是从0开始的。按照我们的习惯，需要加一或减一。

##### 4. add

```java
abstract public void add(int field, int amount)
```

该方法的作用是在Calendar对象中的某个字段上增加或减少一定的数值，增加是amount的值为正，减少是amount的值为负。

例如计算当前时间100天以后的日期，代码如下：

```java
@Test
public void add() {
    Calendar c = Calendar.getInstance();
    c.add(Calendar.DATE, 100);
    int year = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH)+1;
    int date = c.get(Calendar.DATE);
    System.out.println(year + "年" + month + "月" + date + "日");
}
```

这里 add 方法是指在c对象的Calendar.DATE类型上，也就是日期字段（date）上增加100，类内部会**重新计算该日期对象中其它各字段的值**，从而获得100天以后的日期，例如程序的输出结果可能为：2018年11月8日。

##### 5. before & after

```java
public boolean before(Object when)
	return when instanceof Calendar
        && compareTo((Calendar)when) < 0;
}

public boolean after(Object when) {
    return when instanceof Calendar
        && compareTo((Calendar)when) > 0;
}
```

可以看出，这两个方法的作用相似，就是是判断当前日期对象是否在when对象的前面/后面，如果在when对象的前面/后面则返回true，否则返回false。例如：

```java
@Test
public void afterAndAfter() {
    Calendar c1 = Calendar.getInstance();
    c1.set(2018, 8 - 1, 28);

    Calendar c2 = Calendar.getInstance();
    c2.set(2018, 10 - 1, 1);

    boolean b = c2.after(c1);
    System.out.println(b);
}
```

##### 6. setTimeInMillis & getTimeInMillis

```language
public void setTimeInMillis(long millis)
```

该方法通过毫秒数来设置Calendar类，参数是长整型类型。

```java
public void setTimeInMillis() {
    Calendar calendar1 = Calendar.getInstance();
    long t = 1252785271098L;

    long t1 = calendar1.getTimeInMillis();

    Calendar calendar2 = Calendar.getInstance();
    calendar2.setTimeInMillis(t1);
}
```

在转换时，使用Calendar类中的getTimeInMillis方法可以将Calendar对象转换为相对时间。在将相对时间转换为Calendar对象时，首先创建一个Calendar对象，然后再使用Calendar类的setTimeInMillis方法设置时间即可。

##### 7. getActualMaximum & getActualMaximum

```java
public int getActualMinimum(int field)

public int getActualMaximum(int field)
```

这两个方法也是一套的，是做什么的呢？比如getActualMaximum，它的参数传入某一属性（field），如果是日期（即上文中的Date），那么就是求当前日历时间所在月份的最大Date。

```java
// getActualMaximum: get maximum value in a field
// getActualMaximum: get minimum value in a field
public void getActualMaximum(){
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.YEAR, 2018);
    calendar.set(Calendar.MONTH, 9-1);

    // minimum month in 2018
    int minMonth = calendar.getActualMinimum(Calendar.MONTH);
    // maximum day in September
    int maxDate = calendar.getActualMaximum(Calendar.DATE);

    System.out.println(minMonth);
}
```

那么同理，对于其他属性以及getActualMinimum也是一样的。

##### 8. getTime

```java
public final Date getTime() {
    return new Date(getTimeInMillis());
}
```

顾名思义，就是获取时间，上面的源码也很简单：通过毫秒数来获取一个 java.util.Date 的实例。

#### 常用场景

##### 1. 获取今天或者之后多少天的日期

文章上面有介绍，即使用 add() 方法。

##### 2. 计算两个日期之间相差的天数

例如计算2018年8月30号和2018年9月2号之间相差的天数。该程序实现的原理为：首先代表两个特定的时间点，这里使用Calendar的对象进行代表，然后将两个时间点转换为对应的相对时间（长整型），求两个时间点相对时间的差值，然后除以一天的毫秒数(24小时*60分钟*60秒*1000毫秒)即可获得对应的天数。

```java
@Test
public void dayGap() {
    Calendar c1 = Calendar.getInstance();
    c1.set(2018, 8 - 1, 30);
    Calendar c2 = Calendar.getInstance();
    c2.set(2018, 9 - 1, 2);

    long days = (c2.getTimeInMillis() - c1.getTimeInMillis()) / (24*60*60*1000);
    System.out.println(days);
}
```

## Date



## 区别

