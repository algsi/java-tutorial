package com.li.regex.matcher;

import java.util.Arrays;
import java.util.regex.Pattern;

/*本程序测试Matcher的split方法*/

public class SplitTest {

	public static void main(String[] args) {
		String input = "This!!unusual use!!of exclamation!!points";
		
		/*调用Arrays类的静态方法，将分割的内容格式输出*/
		System.out.println(Arrays.asList(Pattern.compile("!!").split(input)));
			
		/*give this function a parameter*/
		System.out.println(Arrays.asList(Pattern.compile("!!").split(input, 3)));
		
		/*String's function : split*/
		System.out.println(Arrays.asList("Aha! String has a split() built in!".split(" ")));
	}

}
