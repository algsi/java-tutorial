package com.data;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataTest {

	public static void main(String[] args) throws ParseException {

		long begin = System.currentTimeMillis(); // currenttimemillisecond��ĳ��ʱ�䵽�����������ĺ�����
		System.out.println("���ڣ�" + begin+"\n");

		for (int i = 0; i < 100000000; i++) {

		}

		// Data��
		Date date = new Date();
		System.out.println("��ǰʱ��: " + date+"\n");
		// ��������תΪ����ʱ�䣺�ú�����ʼ��Date����
		Date thatDate = new Date(begin); //���ʹ�ó��������ݣ�ע�����ݺ����L
		System.out.println("����ת�����ࣺ" + thatDate+"\n");

		/**
		 * ��ʽת����
		 * pattern��yyyy-MM-dd HH:mm:ss SSS  ����ʾ�ꡢ�¡��ա�ʱ���֡��롢���룩��ע���º�ʱҪ��д����������
		 * DateFormate�ǳ����಻��ʵ��������Ҫͨ���̳��������ʵ����
		 */
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
		String strDate = df.format(thatDate);
		System.out.println("ʹ�ø�ʽת������" + strDate+"\n");
		
		//��������Ҫ��Ӧ��ģ�壩
		Date fDate = df.parse(strDate);
		System.out.println("������" + fDate.getTime()+"\n");
		
		System.out.println("��������ʱ�䣺" + (System.currentTimeMillis() - begin));
	}

}
