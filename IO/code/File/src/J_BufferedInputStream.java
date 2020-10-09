import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.Date;

//带缓存的输入输出流

public class J_BufferedInputStream {
	//保存需要读取的文件的路径
	private static String m_fileName="bin\\J_BufferedInputStream.class";
	
	public static void main(String args[]){
		try {
			int i, ch;
			i=0;
			Date d1=new Date();
			FileInputStream f=new FileInputStream(m_fileName);
			
			//read entire file
			while((ch=f.read())!=-1) 
				i++;
			
			Date d2=new Date();
			
			long t=d2.getTime()-d1.getTime(); //单位为ms
			
			System.out.printf("读取文件%1$s(共%2$d字节)%n", m_fileName, i);
			System.out.printf("不帶緩存的方法需要%1$dms%n", t);
			
			i=0;
			d1=new Date();
			f=new FileInputStream(m_fileName);
			BufferedInputStream fb=new BufferedInputStream(f);
			
			//read entire file
			while((ch=fb.read())!=-1)
				i++;
			
			fb.close();
			d2=new Date();
			
			t=d2.getTime()-d1.getTime(); //单位为ms
			System.out.printf("帶緩存的方法需要%1$dms%n", t);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
