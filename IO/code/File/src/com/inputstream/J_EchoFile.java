package com.inputstream;

import java.io.FileInputStream;
import java.io.IOException;

public class J_EchoFile {

	public static void main(String[] args) {
		try {
			// ʹ�ù��췽��ʵ����һ��FileInputStream����
			//����ʹ��б�������ֱ�ʾ�ļ��ľ���·���������ָ��·������Ĭ�ϵ�ǰ·����
			FileInputStream f = new FileInputStream("D:\\����\\test.txt"); 
			int i; // ��¼�ֽڸ���
			int b = f.read();
			for (i = 0; b != -1; i++) {
				System.out.print((char) b);
				b = f.read();
			}
			System.out.println();
			System.out.println("�ļ�\"test.txt\"�ֽ���Ϊ��" + i); // ��Ҫ��˫����ת��
			f.close();
		} catch (IOException e) {
			System.err.println("�����쳣��" + e);
			e.printStackTrace();
		}
	}

}
