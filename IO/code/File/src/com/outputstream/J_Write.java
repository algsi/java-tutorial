package com.outputstream;

import java.io.IOException;
import java.io.OutputStream;

public class J_Write {

	public static void mb_Write(OutputStream out) {
		String s = "0Aa�ַ���";
		byte[] b = s.getBytes(); // ���ַ������ֽڴ���byte�������飨ASCII��
		try {
			out.write(b);
			out.flush();
			System.out.println();
			for (byte a : b) {
				System.out.print(a + " ");
			}
			out.close();
		} catch (IOException e) {
			System.err.println("�����쳣��" + e);
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		mb_Write(System.out);
	}

}
