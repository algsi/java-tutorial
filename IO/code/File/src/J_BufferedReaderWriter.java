import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;

public class J_BufferedReaderWriter {

	public static void main(String[] args) {
		try {
			BufferedWriter bw=new BufferedWriter(new FileWriter("D:\\桌面\\test.txt"));
			bw.write("有志者，事竟成");
			bw.newLine();
			bw.write("苦心人，天不负");
			bw.newLine();
			bw.close();
			
			LineNumberReader br=new LineNumberReader(new FileReader("D:\\桌面\\test.txt"));
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
