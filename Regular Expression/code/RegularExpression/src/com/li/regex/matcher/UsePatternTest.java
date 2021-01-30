package com.li.regex.matcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*此程序对Matcher类中的usePattern方法进行测试*/

public class UsePatternTest {

	public static void main(String[] args) {
		Pattern pattern = Pattern.compile("Java");
		Pattern newPattern=Pattern.compile("C\\+\\+");
		Matcher matcher = pattern.matcher("Java C++");
		while(matcher.find()) {
			System.out.println(matcher.group());
		}
		
		matcher.usePattern(newPattern);
		while(matcher.find()) {
			System.out.println(matcher.group());
		}
	}

}
