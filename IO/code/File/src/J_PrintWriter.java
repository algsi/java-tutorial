import java.io.PrintWriter;

import javax.swing.plaf.synth.SynthSpinnerUI;

/*
 * PrintWriter����
 */

public class J_PrintWriter {

	public static void main(String[] args) {
		try {
			PrintWriter f=new PrintWriter("D:\\����\\out.txt");
			f.println("��Ҫ��˳���߽��Ǹ���ҹDo not go gentle into that good night");
			f.close();
		} catch (Exception e) {
			System.err.println("�����쳣��"+e);
			e.printStackTrace();
		}
	}

}
