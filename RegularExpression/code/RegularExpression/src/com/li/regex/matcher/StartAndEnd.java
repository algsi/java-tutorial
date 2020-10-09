package com.li.regex.matcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*本程序演示Matcher类里面的start和end方法，还演示matches()和lookingAt()正两个方法的区别*/

public class StartAndEnd {

	public static void main(String[] args) {

		String[] input = new String[] { "Java has regular expressions in 1.4",
										"regular expressions now expressing in Java", 
										"Java represses oracular expressions" };
		
		Pattern p1 = Pattern.compile("re\\w*");
		Pattern p2 = Pattern.compile("Java.*");
		
		for (int i = 0; i < input.length; i++) {
			System.out.println("input[" + i + "]: " + input[i]);

			Matcher m1 = p1.matcher(input[i]);
			Matcher m2 = p2.matcher(input[i]);

			while (m1.find())
				System.out.println("m1.find() '" + m1.group() + "' start = " + m1.start() + " end = " + m1.end());

			while (m2.find())
				System.out.println("m2.find() '" + m2.group() + "' start = " + m2.start() + " end = " + m2.end());

			if (m1.lookingAt())
				System.out.println("m1.lookingAt() start = " + m1.start() + " end = " + m1.end());
			if (m2.lookingAt())
				System.out.println("m2.lookingAt() start = " + m2.start() + " end = " + m2.end());

			if (m1.matches())
				System.out.println("m1.matches() start = " + m1.start() + " end = " + m1.end());
			if (m2.matches())
				System.out.println("m2.matches() start = " + m2.start() + " end = " + m2.end());

			System.out.println();
		}
	}

}
