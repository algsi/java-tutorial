import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.Date;

//����������������

public class J_BufferedInputStream {
	//������Ҫ��ȡ���ļ���·��
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
			
			long t=d2.getTime()-d1.getTime(); //��λΪms
			
			System.out.printf("��ȡ�ļ�%1$s(��%2$d�ֽ�)%n", m_fileName, i);
			System.out.printf("��������ķ�����Ҫ%1$dms%n", t);
			
			i=0;
			d1=new Date();
			f=new FileInputStream(m_fileName);
			BufferedInputStream fb=new BufferedInputStream(f);
			
			//read entire file
			while((ch=fb.read())!=-1)
				i++;
			
			fb.close();
			d2=new Date();
			
			t=d2.getTime()-d1.getTime(); //��λΪms
			System.out.printf("������ķ�����Ҫ%1$dms%n", t);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
