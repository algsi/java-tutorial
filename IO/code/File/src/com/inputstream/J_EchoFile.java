package com.inputstream;

import java.io.FileInputStream;
import java.io.IOException;

public class J_EchoFile {

	public static void main(String[] args) {
		try {
			// 使用构造方法实例化一个FileInputStream对象
			//并且使用斜杠来划分表示文件的绝对路径，如果不指定路径，则默认当前路径下
			FileInputStream f = new FileInputStream("D:\\桌面\\test.txt"); 
			int i; // 记录字节个数
			int b = f.read();
			for (i = 0; b != -1; i++) {
				System.out.print((char) b);
				b = f.read();
			}
			System.out.println();
			System.out.println("文件\"test.txt\"字节数为：" + i); // 需要对双引号转义
			f.close();
		} catch (IOException e) {
			System.err.println("发生异常：" + e);
			e.printStackTrace();
		}
	}

}
