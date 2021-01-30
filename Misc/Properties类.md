# Properties

java.util.Properties 是存储键值对的工具类，可以使用它来对存储键值对的文件进行操作。

存储键值对的文件很简单，后缀为 .properties，例如下面是一个 info.properties 文件：

```
user=root
password=123456
```

格式也很简单，键和值，中间用等于号连接，无需其他符号。

那么，怎么读取这样的一个文件并解析呢？

我们的 info.properties 文件位于 com.general 包下，再在此包下建立一个Test类：

```java
package com.general;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.li.iterator.IteratorTest;

public class Test {

	public static void main(String[] args) {
		Properties props = new Properties();
		InputStream is=null;
		try {
			is=Test.class.getResourceAsStream("info.properties");
			props.load(is);
			System.out.println(props.getProperty("user"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

```