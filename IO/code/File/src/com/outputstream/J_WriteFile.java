package com.outputstream;

import java.io.FileOutputStream;
import java.io.IOException;

public class J_WriteFile {

	public static void main(String[] args) {
		String s = "�ļ����������";
		byte[] b = s.getBytes();
		try {
			// ��������ڴ��ļ������½�һ���ļ���
			FileOutputStream f = new FileOutputStream("D:\\����\\out.txt", false);
			f.write(b);
			f.flush();
			f.close();
		} catch (IOException e) {
			System.err.println("�׳��쳣��" + e);
			e.printStackTrace();
		}
	}

}
