import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/*
 * 从控制台读入数据的例程
 */

public class J_ReadData {

	public static void main(String[] args) {
		
		int i=0;
		BufferedReader f=new BufferedReader(new InputStreamReader(System.in));
		System.out.println("请输入一个整数：");
		try {
			String s=f.readLine();
			i=Integer.parseInt(s);
		} catch (IOException e) {
			System.err.println("发生异常："+e);
			e.printStackTrace();
		}
		System.out.println(i);
	}

}
