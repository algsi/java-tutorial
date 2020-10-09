package com.printstream;

import java.io.FileNotFoundException;
import java.io.PrintStream;

public class J_PrintStream {

	public static void main(String[] args) {
		try {
			PrintStream f=new PrintStream("D:\\桌面\\out.txt");
			f.printf("%1$d+%2$d=%3$d", 1, 2, (1+2));
			f.close();
		} catch (FileNotFoundException e) {
			System.err.println("发生异常："+e);
			e.printStackTrace();
		}
		
	}

}
