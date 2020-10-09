package com.data;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataTest {

	public static void main(String[] args) throws ParseException {

		long begin = System.currentTimeMillis(); // currenttimemillisecond从某个时间到现在所经历的毫秒数
		System.out.println("现在：" + begin+"\n");

		for (int i = 0; i < 100000000; i++) {

		}

		// Data类
		Date date = new Date();
		System.out.println("当前时间: " + date+"\n");
		// 将毫秒数转为现在时间：用毫秒差初始化Date对象
		Date thatDate = new Date(begin); //如果使用长整型数据，注意数据后面的L
		System.out.println("毫秒转日期类：" + thatDate+"\n");

		/**
		 * 格式转换器
		 * pattern：yyyy-MM-dd HH:mm:ss SSS  （表示年、月、日、时、分、秒、毫秒），注意月和时要大写，否则会出错
		 * DateFormate是抽象类不能实例化，需要通过继承类来间接实例化
		 */
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
		String strDate = df.format(thatDate);
		System.out.println("使用格式转换器：" + strDate+"\n");
		
		//解析（需要对应的模板）
		Date fDate = df.parse(strDate);
		System.out.println("解析：" + fDate.getTime()+"\n");
		
		System.out.println("程序运行时间：" + (System.currentTimeMillis() - begin));
	}

}
