import java.io.FileInputStream;

import com.inputstream.J_Echo;

//标准输入输出流的重定向

public class J_SetIn {

	public static void main(String[] args) {
		try {
			//重定向
			System.setIn(new FileInputStream("D:\\桌面\\数据库查询.txt"));
			
			//调用
			J_Echo.mb_echo(System.in);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
