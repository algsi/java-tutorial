package com.li.regex.matcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*�˳����Matcher���е�usePattern�������в���*/

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
