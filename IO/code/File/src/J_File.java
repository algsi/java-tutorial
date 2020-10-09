import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class J_File {

	/*���������Ĳ���*/
	public static void test1() {
		/* �����myFile�ļ��� */
		File f = new File("D:\\����", "myFile");
		
		if (!f.exists()) {
			/*����·���������ܴ����ļ�*/
			f.mkdir();
			System.out.println("�����ɹ���\n");
		}
		System.out.println("�Ըոմ����ɹ���·���Ĳ��ԣ�");
		System.out.println("�Ƿ�Ϊ�ļ���"+f.isFile());
		System.out.println("�Ƿ�Ϊ·����"+f.isDirectory());
		System.out.println("�Ƿ�ɶ���"+f.canRead());
		System.out.println("�Ƿ��д��"+f.canWrite());
		System.out.println("�Ƿ�����������ԣ�"+f.isHidden());
		System.out.println("�Ƿ���þ���·������ʽ��"+f.isAbsolute());
		System.out.println("����·����"+f.getAbsolutePath());
		System.out.println("�ļ������ƣ�"+f.getName());
		System.out.println("��·�����ƣ�"+f.getPath());
		System.out.println("��·������"+f.getParent());
		System.out.println("�����޸�ʱ�䣨����������"+f.lastModified());
		
		/*��f·���´���һ���ļ�������ʹ��f�Ļ�ȡ·���ķ���*/
		File file=new File(f, "test.txt");
		PrintWriter f_out=null;
		try {
			f_out=new PrintWriter(file);
			f_out.println("һ�������ļ�");
			f_out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("�´������ļ��Ƿ��Ѿ����ڣ�"+file.exists());
	}
	
	/*���·���Ĳ���*/
	public static void test2() {
		File f1 = new File(".", "TestFile");
		if(!f1.exists()) {
			f1.mkdir();
			System.out.println("�����ɹ���");
		}
		//�ù������ڵ�·�����ڸù��̵�·���´���һ���ļ���
		System.out.println(f1.getAbsolutePath());
		
		
		File f2=new File("\\");
		/*�ý����������ϵͳ��ͬ����ͬ���ҵ���D��*/
		System.out.println(f2.getAbsolutePath());
	}
	
	public static void main(String[] args) {
		//J_File.test1();
		//J_File.test2();
		
	}

}
