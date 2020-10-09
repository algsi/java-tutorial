package com.inputstream;

import java.io.IOException;
import java.io.InputStream;

/*
 * 屏幕输入回显例程
 */
public class J_Echo {

	public static void mb_echo(InputStream in) {
		try {
			while (true) { // 接受输入并回显
				int i = in.read();
				if (i == -1)    //输入流结束
					break;
				char c = (char) i; // 转换
				System.out.print(c);
			}
		} catch (IOException e) {
			System.err.println("发生异常："+e);
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		mb_echo(System.in);
	}

}
