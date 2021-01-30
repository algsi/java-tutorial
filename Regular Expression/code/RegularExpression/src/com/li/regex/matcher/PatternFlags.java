package com.li.regex.matcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*�˳����ƥ��ģʽ���в���*/

public class PatternFlags {

	public static void main(String[] args) {

		//ͬʱ���ö��ƥ��ģʽ���Դ�Сд�������Լ������Ǻű�ʾһ�еĿ�ʼ
		Pattern p = Pattern.compile("^java", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
		Matcher m = p.matcher("java has regex\nJava has regex\n" + "JAVA has pretty good regular expressions\n"
				+ "Regular expressions are in Java");
		while (m.find())
			System.out.println(m.group());
	}

}
