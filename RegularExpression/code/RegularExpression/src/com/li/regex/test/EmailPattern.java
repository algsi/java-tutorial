package com.li.regex.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*正则表达式的一个简单用法示例*/

public class EmailPattern {

	public static void main(String[] args) {
		// 定义一个E-mail地址的正则表达式（加上转义字符，使用双反斜杠）
		String regex = "^\\w+(-+.\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";

		String email = "1559541684@qq.com";

		if (email.matches(regex)) {
			System.out.println("是一个正确的邮箱格式");
		} else {
			System.out.println("不是正确的邮箱格式");
		}
		
		/*使用两个正则表达式相关的重要类*/
		
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(email);
		while(matcher.find()) {
			for(int j = 0; j <= matcher.groupCount(); j++)
			      System.out.print("[" + matcher.group(j) + "]");
			System.out.println();
		}
	}
}
