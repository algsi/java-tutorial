package com.li.regex.matcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*此程序对Matcher类中的find方法进行测试*/

public class FindTest {

	public static void main(String[] args) {

		// 数字模板：连续的三个数字
		Pattern pattern = Pattern.compile("\\d{3}");

		String s = "123 456 789 4d56";

		Matcher matcher = pattern.matcher(s);
		int count = 0;

		while (matcher.find()) {
			count++;
			System.out.println("Match number: " + count);

			// 此次匹配到的起点索引值
			System.out.print("start(): " + matcher.start());

			// 此次匹配到的终点索引值，匹配到的末尾字符下标加一
			System.out.println(", end(): " + matcher.end());

			// 匹配到的内容，里面可以指定int类型的参数，涉及到group的概念
			System.out.println(matcher.group(0));

			System.out.println();
		}
	}

}
