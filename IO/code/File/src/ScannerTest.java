import java.util.Scanner;

public class ScannerTest {

	public static void main(String[] args) {
		Scanner input=new Scanner(System.in);
		System.out.println("请输入一个字符串");
		String s=input.next();
		System.out.println("请输入一个整数");
		int i=input.nextInt();
		System.out.println("字符串："+s+"， 整数："+i);
		
	}

}
