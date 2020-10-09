import java.io.FileReader;
import java.io.FileWriter;

public class J_FileReaderWriter {

	public static void main(String[] args) {
		try {
			FileWriter f_out = new FileWriter("D:\\桌面\\test.txt");
			f_out.write("有志者，事竟成");
			f_out.close();

			FileReader f_in = new FileReader("D:\\桌面\\test.txt");
			for (int c=f_in.read(); c!=-1; c=f_in.read())
				System.out.print((char)c);
			f_in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
