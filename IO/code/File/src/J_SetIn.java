import java.io.FileInputStream;

import com.inputstream.J_Echo;

//��׼������������ض���

public class J_SetIn {

	public static void main(String[] args) {
		try {
			//�ض���
			System.setIn(new FileInputStream("D:\\����\\���ݿ��ѯ.txt"));
			
			//����
			J_Echo.mb_echo(System.in);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
