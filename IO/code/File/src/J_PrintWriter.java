import java.io.PrintWriter;

import javax.swing.plaf.synth.SynthSpinnerUI;

/*
 * PrintWriter例程
 */

public class J_PrintWriter {

	public static void main(String[] args) {
		try {
			PrintWriter f=new PrintWriter("D:\\桌面\\out.txt");
			f.println("不要温顺地走进那个良夜Do not go gentle into that good night");
			f.close();
		} catch (Exception e) {
			System.err.println("发生异常："+e);
			e.printStackTrace();
		}
	}

}
