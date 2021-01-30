package com.li.regex.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*������ʽ��һ�����÷�ʾ��*/

public class EmailPattern {

	public static void main(String[] args) {
		// ����һ��E-mail��ַ��������ʽ������ת���ַ���ʹ��˫��б�ܣ�
		String regex = "^\\w+(-+.\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";

		String email = "1559541684@qq.com";

		if (email.matches(regex)) {
			System.out.println("��һ����ȷ�������ʽ");
		} else {
			System.out.println("������ȷ�������ʽ");
		}
		
		/*ʹ������������ʽ��ص���Ҫ��*/
		
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(email);
		while(matcher.find()) {
			for(int j = 0; j <= matcher.groupCount(); j++)
			      System.out.print("[" + matcher.group(j) + "]");
			System.out.println();
		}
	}
}
