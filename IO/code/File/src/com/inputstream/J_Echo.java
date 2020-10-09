package com.inputstream;

import java.io.IOException;
import java.io.InputStream;

/*
 * ��Ļ�����������
 */
public class J_Echo {

	public static void mb_echo(InputStream in) {
		try {
			while (true) { // �������벢����
				int i = in.read();
				if (i == -1)    //����������
					break;
				char c = (char) i; // ת��
				System.out.print(c);
			}
		} catch (IOException e) {
			System.err.println("�����쳣��"+e);
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		mb_echo(System.in);
	}

}
