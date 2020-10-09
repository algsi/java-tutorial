import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;

public class J_BufferedReaderWriter {

	public static void main(String[] args) {
		try {
			BufferedWriter bw=new BufferedWriter(new FileWriter("D:\\����\\test.txt"));
			bw.write("��־�ߣ��¾���");
			bw.newLine();
			bw.write("�����ˣ��첻��");
			bw.newLine();
			bw.close();
			
			LineNumberReader br=new LineNumberReader(new FileReader("D:\\����\\test.txt"));
			String s;
			for (s=br.readLine(); s!=null; s=br.readLine()) {
				System.out.println(br.getLineNumber()+" : "+s);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
