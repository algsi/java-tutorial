import java.util.Scanner;

public class ScannerTest {

	public static void main(String[] args) {
		Scanner input=new Scanner(System.in);
		System.out.println("������һ���ַ���");
		String s=input.next();
		System.out.println("������һ������");
		int i=input.nextInt();
		System.out.println("�ַ�����"+s+"�� ������"+i);
		
	}

}
