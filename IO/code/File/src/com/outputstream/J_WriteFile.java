package com.outputstream;

import java.io.FileOutputStream;
import java.io.IOException;

public class J_WriteFile {

	public static void main(String[] args) {
		String s = "文件输出流例程";
		byte[] b = s.getBytes();
		try {
			// 如果不存在此文件将会新建一个文件夹
			FileOutputStream f = new FileOutputStream("D:\\桌面\\out.txt", false);
			f.write(b);
			f.flush();
			f.close();
		} catch (IOException e) {
			System.err.println("抛出异常：" + e);
			e.printStackTrace();
		}
	}

}
