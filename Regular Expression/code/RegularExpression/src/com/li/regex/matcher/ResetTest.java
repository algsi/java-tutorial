package com.li.regex.matcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*�˳����Matcher���е�reset�������в���*/

public class ResetTest {
	
	public static void main(String[] args) {
		
		Pattern pattern = Pattern.compile("Java");
		Matcher matcher = pattern.matcher("Java C++");
		while(matcher.find()) {
			System.out.println(matcher.group());
		}
		
		matcher.reset("Java C++ Java");
		while(matcher.find()) {
			System.out.println(matcher.group());
		}
	}

}
