import java.io.FileReader;
import java.io.FileWriter;

public class J_FileReaderWriter {

	public static void main(String[] args) {
		try {
			FileWriter f_out = new FileWriter("D:\\����\\test.txt");
			f_out.write("��־�ߣ��¾���");
			f_out.close();

			FileReader f_in = new FileReader("D:\\����\\test.txt");
			for (int c=f_in.read(); c!=-1; c=f_in.read())
				System.out.print((char)c);
			f_in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
