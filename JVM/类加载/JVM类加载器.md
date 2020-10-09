# JVM类加载器

- 全盘负责：当一个类加载器负责加载某个Class时，该Class所依赖的和引用的其他Class也将由该类加载器负责载入，除非显示使用另外一个类加载器来载入

- 父类委托：先让父类加载器试图加载该类，只有在父类加载器无法加载该类时才尝试从自己的类路径中加载该类

- 缓存机制：缓存机制将会保证所有加载过的Class都会被缓存，当程序中需要使用某个Class时，类加载器先从缓存区寻找该Class，只有缓存区不存在，系统才会读取该类对应的二进制数据，并将其转换成Class对象，存入缓存区。这就是为什么修改了Class后，必须重启JVM，程序的修改才会生效

#### 类的加载

类加载有三种方式：

- 命令行启动应用时候由JVM初始化加载

- 通过Class.forName()方法动态加载

- 通过ClassLoader.loadClass()方法动态加载

例子：

```java
package com.general;

public class LoaderTest {

	public static void main(String[] args) throws ClassNotFoundException
    {
		 ClassLoader loader = LoaderTest.class.getClassLoader(); 
         System.out.println(loader); 
         //使用ClassLoader.loadClass()来加载类，不会执行初始化块 
         loader.loadClass("com.general.Test"); 
         //使用Class.forName()来加载类，默认会执行初始化块 
         //Class.forName("com.general.Test"); 
         //使用Class.forName()来加载类，并指定ClassLoader，初始化时不执行静态块 
         //Class.forName("com.general.Test", false, loader); 

    }

}
```

Test类

```java
package com.general;

public class Test {
	static {
		System.out.println("Test类的静态代码块被执行了");
	}
}

```

分别切换不同的类加载方式，会有不同的输出结果。

Class.forName()和ClassLoader.loadClass()区别：

- `Class.forName()`：将类的.class文件加载到jvm之外，还会对类进行解释，执行类中的static块。

- `ClassLoader.loadClass()`：只干一件事情，就是将.class文件加载到jvm中，不会执行static中的内容,只有在newInstance（创建一个实例）才会去执行static块。

- `Class.forName(name, initialize, loader)`：带参函数也可控制是否加载static块。并且只有调用了newInstance()方法才会调用构造函数，创建类的对象 。

#### 双亲委派模型

双亲委派模型的工作流程是：如果一个类加载器收到了一个加载的请求，它首先不会自己去加载这个类，而是把请求委托给父类去处理，依次向上，因此，所有的类加载请求最终都应该被传递到顶层的启动类加载器中，只有当父加载器在它的搜索范围中没有找到所需的类时，即无法完成该加载，子加载器才会尝试自己去加载该类。

双亲委派机制：

- 当`AppClassLoader`加载一个class时，它首先不会自己去尝试加载这个类，而是把类加载请求委派给父类加载器`ExtClassLoader`去完成。

- 当`ExtClassLoader`加载一个class时，它首先也不会自己去尝试加载这个类，而是把类加载请求委派给B`ootStrapClassLoader`去完成。

- 如果`BootStrapClassLoader`加载失败（例如在$JAVA_HOME/jre/lib里未查找到该class），会使用`ExtClassLoader`来尝试加载；

- 若`ExtClassLoader`也加载失败，则会使用`AppClassLoader`来加载，如果`AppClassLoader`也加载失败，则会报出异常ClassNotFoundException。

ClassLoder部分源码分析：

loadClass方法：

```java
public Class<?> loadClass(String name)throws ClassNotFoundException {
        return loadClass(name, false);
}

protected Class<?> loadClass(String name, boolean resolve)
    throws ClassNotFoundException
{
    synchronized (getClassLoadingLock(name)) {
        // First, check if the class has already been loaded
        Class<?> c = findLoadedClass(name);
        if (c == null) {
            long t0 = System.nanoTime();
            try {
                if (parent != null) {
                    c = parent.loadClass(name, false);
                } else {
                    c = findBootstrapClassOrNull(name);
                }
            } catch (ClassNotFoundException e) {
                // ClassNotFoundException thrown if class not found
                // from the non-null parent class loader
            }

            if (c == null) {
                // If still not found, then invoke findClass in order
                // to find the class.
                long t1 = System.nanoTime();
                c = findClass(name);

                // this is the defining class loader; record the stats
                sun.misc.PerfCounter.getParentDelegationTime().addTime(t1 - t0);
                sun.misc.PerfCounter.getFindClassTime().addElapsedTimeFrom(t1);
                sun.misc.PerfCounter.getFindClasses().increment();
            }
        }
        if (resolve) {
            resolveClass(c);
        }
        return c;
    }
}
```

该方法中，首先判断该类是否已经被加载，如果没有被加载，那么如果存在父类加载器，就委派给父类加载器加载，如果不存在父类加载器，则检查该类是否是由启动类加载器加载的类，如果这样处理之后仍然为空（说明加载不成功），就调用自身的加载器。

双亲委派模型意义：

- 系统类防止内存中出现多份同样的字节码
- 保证Java程序安全稳定运行

#### 自定义类加载器

通常情况下，我们都是直接使用系统类加载器。但是，有的时候，我们也需要自定义类加载器。比如应用是通过网络来传输 Java 类的字节码，为保证安全性，这些字节码经过了加密处理，这时系统类加载器就无法对其进行加载，这样则需要自定义类加载器来实现。自定义类加载器一般都是继承自ClassLoader类，从上面对loadClass方法来分析来看，我们只需要重写 findClass 方法即可。下面我们通过一个示例来演示自定义类加载器的流程：

```java
package com.general;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MyClassLoader extends ClassLoader{
	
	/*类的字节码文件的根目录名*/
	private String root;

	/*重写findClass文件*/
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		byte[] classData = loadClassData(name);
		if(classData==null) {
			throw new ClassNotFoundException();
		}
		else {
			return defineClass(name, classData, 0, classData.length);
		}
	}

	private byte[] loadClassData(String className) {
		/*形成.class文件的路径*/
		String fileName=root+File.separator+className.replace(".", File.separator)+".class";
		try {
			InputStream is=new FileInputStream(fileName);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int bufferSize = 1024;
			 byte[] buffer = new byte[bufferSize];
			 int length=0;
			 while((length=is.read(buffer))!=-1) {
				 baos.write(buffer, 0, length);
			 }
			 return baos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }
    
    public static void main(String[] args) {
		MyClassLoader loader=new MyClassLoader();
		loader.setRoot("D:"+File.separator+"桌面");
		
		Class<?> testClass=null;
		
		try {
			/*调用的是父类中的方法*/
			testClass=loader.loadClass("com.general.DateTest");
			Object object=testClass.newInstance();
			System.out.println(object.getClass().getClassLoader());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
```

自定义类的核心所在就是字节码文件的获取，如果是加密的字节码文件则需要在类中对该文件进行解密。当然，这里只是演示自定义类加载器，并没有涉及到加密与解密。这里还需要注意几点：

- 这里传递的文件名需要是类的全限定性名称，即`com.general.DateTest`格式的，因为 defineClass 方法是按这种格式进行处理的。

- 最好不要重写父类loadClass方法，因为这样容易破坏双亲委托模式。

- 我们加载的类（DateTest）本身可以被 `AppClassLoader` 类加载，因此我们不能把 `com.general.DateTest` 放在类路径下(我把它放在桌面上)。否则，由于双亲委托机制的存在，会直接导致该类由AppClassLoader加载，而不会通过我们自定义类加载器来加载，此时就会输出 `sun.misc.Launcher$AppClassLoader@2a139a55` 而不会输出 `com.general.MyClassLoader@70dea4e`，并且，我们还需要删除bin目录下的DateTest.class文件，以此找不到字节码文件，而使用自定义类加载器加载。