package com.li.regex.matcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*该程序演示Matcher类里group的概念*/

public class GroupTest {

	public static final String poem =
			"Twas brillig, and the slithy toves/n" +
			"Did gyre and gimble in the wabe./n" +
			"All mimsy were the borogoves,/n" +
			"And the mome raths outgrabe./n/n" +
			"Beware the Jabberwock, my son,/n" +
			"The jaws that bite, the claws that catch./n" +
			"Beware the Jubjub bird, and shun/n" +
			"The frumious Bandersnatch.";
	
	public static void main(String[] args) {
		/* 注意：正则表达式中包含的括号以及括号的嵌套*/
		Matcher m = Pattern.compile("(\\S+)\\s+((\\S+)\\s+(\\S+))").matcher(poem);
		while (m.find()) {
			for (int j = 0; j <= m.groupCount(); j++) {
				System.out.println("group "+j+" [" + m.group(j) + "]");
				
				/*测试有参数的start和end方法，找出某个group的起始终结位置*/
				if(j==1) {
					System.out.println("group1' start: "+m.start(1)+" , group1' end: "+m.end(1));
				}
			}
			System.out.println();
		}
	}
}
