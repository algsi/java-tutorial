package com.li.regex.matcher;

import java.util.Arrays;
import java.util.regex.Pattern;

/*���������Matcher��split����*/

public class SplitTest {

	public static void main(String[] args) {
		String input = "This!!unusual use!!of exclamation!!points";
		
		/*����Arrays��ľ�̬���������ָ�����ݸ�ʽ���*/
		System.out.println(Arrays.asList(Pattern.compile("!!").split(input)));
			
		/*give this function a parameter*/
		System.out.println(Arrays.asList(Pattern.compile("!!").split(input, 3)));
		
		/*String's function : split*/
		System.out.println(Arrays.asList("Aha! String has a split() built in!".split(" ")));
	}

}
