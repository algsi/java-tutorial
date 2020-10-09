package com.li.regex.matcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*�˳����Matcher���е�find�������в���*/

public class FindTest {

	public static void main(String[] args) {

		// ����ģ�壺��������������
		Pattern pattern = Pattern.compile("\\d{3}");

		String s = "123 456 789 4d56";

		Matcher matcher = pattern.matcher(s);
		int count = 0;

		while (matcher.find()) {
			count++;
			System.out.println("Match number: " + count);

			// �˴�ƥ�䵽���������ֵ
			System.out.print("start(): " + matcher.start());

			// �˴�ƥ�䵽���յ�����ֵ��ƥ�䵽��ĩβ�ַ��±��һ
			System.out.println(", end(): " + matcher.end());

			// ƥ�䵽�����ݣ��������ָ��int���͵Ĳ������漰��group�ĸ���
			System.out.println(matcher.group(0));

			System.out.println();
		}
	}

}
