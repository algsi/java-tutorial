package com.printstream;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/*���ݵ����������*/

public class J_Data {

	public static void main(String[] args) {
		try {
			FileOutputStream fout=new FileOutputStream("D:\\����\\out.txt");
			DataOutputStream dfout=new DataOutputStream(fout);
			
			int i;
			for (i=0; i<4; i++){
				dfout.writeInt('0'+i); //�ַ���ASCII��
			}
			dfout.close();
			
			FileInputStream fin=new FileInputStream("D:\\����\\out.txt");
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
