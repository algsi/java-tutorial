import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/*
 * �ӿ���̨�������ݵ�����
 */

public class J_ReadData {

	public static void main(String[] args) {
		
		int i=0;
		BufferedReader f=new BufferedReader(new InputStreamReader(System.in));
		System.out.println("������һ��������");
		try {
			String s=f.readLine();
			i=Integer.parseInt(s);
		} catch (IOException e) {
			System.err.println("�����쳣��"+e);
			e.printStackTrace();
		}
		System.out.println(i);
	}

}
