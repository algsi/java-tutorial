import java.io.RandomAccessFile;

public class J_RandomAccessFile {

	public static void main(String[] args) {
		try {
			RandomAccessFile f=new RandomAccessFile("D:\\桌面\\test.txt", "rw");
			int i;
			double d;
			for(i=0; i<10; i++)
				f.writeDouble(Math.PI*i);
			f.seek(16);
			f.writeDouble(0);
			f.seek(0);
			for(i=0; i<10; i++){
				d=f.readDouble();
				System.out.println("["+i+"]:"+d);
			}
		} catch (Exception e) {
			System.err.println("发生异常："+e);
			e.printStackTrace();
		}
	}

}
