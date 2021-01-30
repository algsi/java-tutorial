package com.li.regex.matcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*�˳��򽫶�Matcher����ĸ���ƥ���Ӵ��滻��ָ���ַ����ķ������в���*/

public class ReplaceTest {

	/* ��replaceAll�������в��� */
	/*��Ŀ���ַ����������ģʽ��ƥ����Ӵ�ȫ���滻Ϊָ�����ַ����������ط����µ������ַ���*/
	public static void replaceAllTest() {
		Pattern pattern = Pattern.compile("cat");
		Matcher matcher = pattern.matcher("cat cat cat cat");
		boolean flag=matcher.find();
		if(flag) {
			System.out.println(matcher.replaceAll("hat"));
		}
	}
	
	/* ��replaceFirst�������в��� */
	/*��Ŀ���ַ������һ�������ģʽ��ƥ����Ӵ��滻Ϊָ�����ַ�����*/
	public static void replaceFirstTest() {
		Pattern pattern = Pattern.compile("cat");
		Matcher matcher = pattern.matcher("cat cat cat cat");
		boolean flag=matcher.find();
		if(flag) {
			System.out.println(matcher.replaceFirst("hat"));
		}
	}
	
	/* ��appendReplacement(StringBuffer sb, String replacement)��appendTail(StringBuffer sb)�������в��� */
	/*appendReplacement����ǰƥ���Ӵ��滻Ϊָ���ַ��������ҽ��滻����Ӵ��Լ���֮ǰ���ϴ�ƥ���Ӵ�֮����ַ�������ӵ�һ��StringBuffer�����*/
	/*appendTail�����һ��ƥ�乤����ʣ����ַ�����ӵ�һ��StringBuffer�����*/
	public static void appendTest() {
		Pattern pattern = Pattern.compile("Kelvin");
		Matcher matcher = pattern.matcher("Kelvin Li and Kelvin Chan are both working in Kelvin Chen's KelvinSoftShop company");
		StringBuffer sb = new StringBuffer();
		int i=0;
        //ʹ��find()�������ҵ�һ��ƥ��Ķ���
        boolean result = matcher.find();
        //ʹ��ѭ�������������е�kelvin�ҳ����滻�ٽ����ݼӵ�sb��
        while(result) {
            i++;
            matcher.appendReplacement(sb, "Sakila");
            System.out.println("��"+i+"��ƥ���sb�������ǣ�"+sb.toString());
            result = matcher.find();
        }
        //������appendTail()���������һ��ƥ����ʣ���ַ����ӵ�sb�
        matcher.appendTail(sb);
        System.out.println("����m.appendTail(sb)��sb������������:"+ sb.toString());
	}
	
	public static void main(String[] args) {
		//ReplaceTest.replaceAllTest();
		//ReplaceTest.replaceFirstTest();
		ReplaceTest.appendTest();
	}

}
