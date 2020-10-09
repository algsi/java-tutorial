package com.printstream;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/*数据的输入输出流*/

public class J_Data {

	public static void main(String[] args) {
		try {
			FileOutputStream fout=new FileOutputStream("D:\\桌面\\out.txt");
			DataOutputStream dfout=new DataOutputStream(fout);
			
			int i;
			for (i=0; i<4; i++){
				dfout.writeInt('0'+i); //字符的ASCII码
			}
			dfout.close();
			
			FileInputStream fin=new FileInputStream("D:\\桌面\\out.txt");
			DataInputStream dfin=new DataInputStream(fin);
			for(i=0; i<4; i++){
				System.out.print(dfin.readInt()+", ");
			}
			dfin.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
