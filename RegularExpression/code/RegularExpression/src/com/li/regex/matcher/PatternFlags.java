package com.li.regex.matcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*此程序对匹配模式进行测试*/

public class PatternFlags {

	public static void main(String[] args) {

		//同时启用多个匹配模式：对大小写不敏感以及倒三角号表示一行的开始
		Pattern p = Pattern.compile("^java", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
		Matcher m = p.matcher("java has regex\nJava has regex\n" + "JAVA has pretty good regular expressions\n"
				+ "Regular expressions are in Java");
		while (m.find())
			System.out.println(m.group());
	}

}
