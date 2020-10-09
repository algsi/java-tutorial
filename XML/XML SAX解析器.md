# XML SAX解析器

SAX（Simple API For XML）是一个公共的基于事件的XML文档解析标准，能够通过一个简单的、快速的方法来对XML文档进行处理，和DOM相比它所占用的系统资源更少。

SAX既是一个接口，也是一个软件包。作为接口，SAX是事件驱动型XML解析的一个标准接口，对文档进行顺序扫描，当扫描到文档（document）开始、元素（element）开始与结束、文档（document）结束等地方时通知事件处理函数，由事件处理函数做相应动作，然后继续同样的扫描，直至文档结束。

- SAX的优点：

	- 解析速度快
	- 占用内存少

- SAX的缺点：

	- 无法知道当前解析标签（节点）的上层标签，及其嵌套结构，仅仅知道当前解析的标签的名字和属性，要知道其他信息需要程序猿自己编码
	- 只能读取XML，无法修改XML
	- 无法随机访问某个标签（节点）

- SAX解析适用场合
	- 对于CPU资源宝贵的设备，如Android等移动设备
	- 对于只需从xml读取信息而无需修改xml

## 事件类型

大多数SAX会产生以下类型的事件

- 在文档的开始和结束时触发文档处理事件

- 在文档内每一XML元素接受解析的前后触发元素事件

- 任何元数据通常由单独的事件处理

## 解析步骤

- 创建事件处理程序（即编写ContentHandler的实现类，一般继承自DefaultHandler类，采用adapter模式）

- 创建SAX解析器

- 将事件处理程序分配到解析器

- 对文档进行解析，将每个事件发送给事件处理程序

## 常用接口：ContentHandler接口

ContentHandler是Java类包中一个特殊的SAX接口，该接口封装了一些对事件处理的方法，当XML解析器开始解析XML输入文档时，它会遇到某些特殊的事件，比如文档的开头和结束、元素的开头和结束、以及元素中的某些字符数据等事件。当遇到这些事件时，XML解析器会调用ContentHandler接口中的相应方法来响应该事件。

ContentHandler接口常用方法：

- void startDocument()：文档解析开始的处理

- void endDocument()：文档解析结束的处理

- void startElement(String uri, String localName, String qName, Attributes attr)：ElementNode开始的处理

- void endtElement(String uri, String localName, String qName)：ElementNode结束的处理

- void characters(char[] ch, int start, int length)：具体在某一节点中的处理

## 示例

XML文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<books>
	<book bookno="001">
		<title>Java编程语言</title>
		<author>atuer</author>
		<price>80</price>
	</book>
	<book bookno="002">
		<title>算法</title>
		<author>高纳德</author>
		<price>100</price>
	</book>
</books>
```

对应类（Book.java）

```java
package com.li.entity;

public class Book {
	private String bookNo;
	private String title;
	private String author;
	private Double price;
	public String getBookNo() {
		return bookNo;
	}
	public void setBookNo(String bookNo) {
		this.bookNo = bookNo;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Book() {
	}
	public Book(String bookNo, String title, String author, Double price) {
		super();
		this.bookNo = bookNo;
		this.title = title;
		this.author = author;
		this.price = price;
	}
	@Override
	public String toString() {
		return "Book [bookNo=" + bookNo + ", title=" + title + ", author=" + author + ", price=" + price + "]";
	}
}

```

解析接口（XmlParse.java）

```java
package com.li.parse;

import java.util.List;

import com.li.entity.Book;

public interface XmlParse {
	public List<Book> parseXml(String fileName);
}

```

解析器（MySaxHandler.java）

```java
package com.li.parse;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.li.entity.Book;

public class MySaxHandler extends DefaultHandler {

	/*元素标签的名字*/
	private String tag;
	
	private Book book;
	private List<Book> list;

	public List<Book> getList(){
		return list;
	}
	
	@Override
	public void characters(char[] ch, int start, int end) throws SAXException {
		if(tag!=null) {
			/*还需去掉两端无用的空格*/
			String s=new String(ch, start, end).trim();
			if(tag.equals("title")) {
				book.setTitle(s);
			}
			if(tag.equals("author")) {
				book.setAuthor(s);
			}
			if(tag.equals("price")) {
				book.setPrice(Double.parseDouble(s));
			}
		}
	}

	@Override
	public void startDocument() throws SAXException {
		list = new ArrayList<Book>();
	}

	@Override
	public void endDocument() throws SAXException {
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attr) throws SAXException {
		if (qName.equals("book")) {
			book = new Book();
			/* Attribute：处理属性 */
			book.setBookNo(attr.getValue("bookno"));
		}
		tag = qName;
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (qName.equals("book")) {
			list.add(book);
			/*将实例添加到线性表中，并将实例引用置空*/
			book = null;
		}
		tag=null;
	}
}

```

解析实例（SaxParser.java）

```java
package com.li.parse;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.li.entity.Book;

public class SaxParser implements XmlParse {

	@Override
	public List<Book> parseXml(String fileName) {
		List<Book> list=new ArrayList<>();
		SAXParserFactory factory=SAXParserFactory.newInstance();
		MySaxHandler handler=null;
		try {
			SAXParser parser = factory.newSAXParser();
			InputStream is = new FileInputStream(fileName);
			handler = new MySaxHandler();
			parser.parse(is, handler);
		} catch (Exception e) {
			e.printStackTrace();
		}
		list=handler.getList();
		return list;
	}
	
	public static void main(String[] args) {
		XmlParse parser=new SaxParser();
		List<Book> list=parser.parseXml("books.xml");
		System.out.println("SAX解析结果：");
		for(Book book : list) {
			System.out.println(book);
		}
	}

}

```