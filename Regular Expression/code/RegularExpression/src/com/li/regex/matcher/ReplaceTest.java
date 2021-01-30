package com.li.regex.matcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*此程序将对Matcher类的四个将匹配子串替换成指定字符串的方法进行测试*/

public class ReplaceTest {

	/* 对replaceAll方法进行测试 */
	/*将目标字符串里与既有模式相匹配的子串全部替换为指定的字符串，并返回返回新的整个字符串*/
	public static void replaceAllTest() {
		Pattern pattern = Pattern.compile("cat");
		Matcher matcher = pattern.matcher("cat cat cat cat");
		boolean flag=matcher.find();
		if(flag) {
			System.out.println(matcher.replaceAll("hat"));
		}
	}
	
	/* 对replaceFirst方法进行测试 */
	/*将目标字符串里第一个与既有模式相匹配的子串替换为指定的字符串。*/
	public static void replaceFirstTest() {
		Pattern pattern = Pattern.compile("cat");
		Matcher matcher = pattern.matcher("cat cat cat cat");
		boolean flag=matcher.find();
		if(flag) {
			System.out.println(matcher.replaceFirst("hat"));
		}
	}
	
	/* 对appendReplacement(StringBuffer sb, String replacement)和appendTail(StringBuffer sb)方法进行测试 */
	/*appendReplacement将当前匹配子串替换为指定字符串，并且将替换后的子串以及其之前到上次匹配子串之后的字符串段添加到一个StringBuffer对象里。*/
	/*appendTail将最后一次匹配工作后剩余的字符串添加到一个StringBuffer对象里。*/
	public static void appendTest() {
		Pattern pattern = Pattern.compile("Kelvin");
		Matcher matcher = pattern.matcher("Kelvin Li and Kelvin Chan are both working in Kelvin Chen's KelvinSoftShop company");
		StringBuffer sb = new StringBuffer();
		int i=0;
        //使用find()方法查找第一个匹配的对象
        boolean result = matcher.find();
        //使用循环将句子里所有的kelvin找出并替换再将内容加到sb里
        while(result) {
            i++;
            matcher.appendReplacement(sb, "Sakila");
            System.out.println("第"+i+"次匹配后sb的内容是："+sb.toString());
            result = matcher.find();
        }
        //最后调用appendTail()方法将最后一次匹配后的剩余字符串加到sb里；
        matcher.appendTail(sb);
        System.out.println("调用m.appendTail(sb)后sb的最终内容是:"+ sb.toString());
	}
	
	public static void main(String[] args) {
		//ReplaceTest.replaceAllTest();
		//ReplaceTest.replaceFirstTest();
		ReplaceTest.appendTest();
	}

}
