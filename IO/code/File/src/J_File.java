import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class J_File {

	/*基本方法的测试*/
	public static void test1() {
		/* 桌面的myFile文件夹 */
		File f = new File("D:\\桌面", "myFile");
		
		if (!f.exists()) {
			/*创建路径，并不能创建文件*/
			f.mkdir();
			System.out.println("创建成功！\n");
		}
		System.out.println("对刚刚创建成功的路径的测试：");
		System.out.println("是否为文件："+f.isFile());
		System.out.println("是否为路径："+f.isDirectory());
		System.out.println("是否可读："+f.canRead());
		System.out.println("是否可写："+f.canWrite());
		System.out.println("是否具有隐藏属性："+f.isHidden());
		System.out.println("是否采用绝对路径的形式："+f.isAbsolute());
		System.out.println("绝对路径："+f.getAbsolutePath());
		System.out.println("文件夹名称："+f.getName());
		System.out.println("带路径名称："+f.getPath());
		System.out.println("父路径名："+f.getParent());
		System.out.println("最后的修改时间（毫秒数）："+f.lastModified());
		
		/*在f路径下创建一个文件，或者使用f的获取路径的方法*/
		File file=new File(f, "test.txt");
		PrintWriter f_out=null;
		try {
			f_out=new PrintWriter(file);
			f_out.println("一个测试文件");
			f_out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("新创建的文件是否已经存在："+file.exists());
	}
	
	/*相对路径的测试*/
	public static void test2() {
		File f1 = new File(".", "TestFile");
		if(!f1.exists()) {
			f1.mkdir();
			System.out.println("创建成功！");
		}
		//该工程所在的路径，在该工程的路径下创建一个文件夹
		System.out.println(f1.getAbsolutePath());
		
		
		File f2=new File("\\");
		/*该结果可能依各系统不同而不同，我的是D盘*/
		System.out.println(f2.getAbsolutePath());
	}
	
	public static void main(String[] args) {
		//J_File.test1();
		//J_File.test2();
		
	}

}
