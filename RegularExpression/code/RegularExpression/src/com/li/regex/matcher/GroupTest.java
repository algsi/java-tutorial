package com.li.regex.matcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*�ó�����ʾMatcher����group�ĸ���*/

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
		/* ע�⣺������ʽ�а����������Լ����ŵ�Ƕ��*/
		Matcher m = Pattern.compile("(\\S+)\\s+((\\S+)\\s+(\\S+))").matcher(poem);
		while (m.find()) {
			for (int j = 0; j <= m.groupCount(); j++) {
				System.out.println("group "+j+" [" + m.group(j) + "]");
				
				/*�����в�����start��end�������ҳ�ĳ��group����ʼ�ս�λ��*/
				if(j==1) {
					System.out.println("group1' start: "+m.start(1)+" , group1' end: "+m.end(1));
				}
			}
			System.out.println();
		}
	}
}
