# <font color="red">XML DOM解析器</font>

## DOM 解析器

DOM （Document Object Model，文档对象模型）是W3C制定的一套规范标准，规定了解析文档的接口，各种语言可以按照DOM规范去实现这些接口，给出文档的解析器。DOM规范中所指的文档相当广泛，包括XML文件和HTML文件。

接口只关心功能，不规定功能的具体实现，因此，在特定语言中使用DOM规范就需要定义DOM规范指定的接口，并给出实现这些接口的类的集合，这一过程称作语言绑定。

## DOM文档节点

DOM解析器调用parse方法返回一个叫做Document对象，它是由实现了Node接口的实例组成的树状结构数据，这些实例称作Document对象中的节点。实际上Document接口也是Node接口的子接口。

##### DOM文档节点常用方法

- short getNodeType()

	- 返回一个表示节点类型的常量（Node接口规定的常量值），例如对于Element节点getNodeType()方法返回的值为：Node.ELEMENT_NODE

- NodeList getChildNodes()

	- 返回由当前节点的所有子节点组成的NodeList对象

- Node getFirstChiild()

	- 返回当前节点的第一个子节点

- Node getLastChild()

	- 返回当前节点的最后一个子节点

- NodeList getTextContent()

	- 返回当前节点及所有子孙节点中的文本内容

##### 文档节点子孙关系

为了解析规范的XML文档，DOM规定了各种类型节点之间可以形成子孙关系

![DOM节点关系.PNG](https://img-blog.csdn.net/20180513115648415)

## 示例

XML文件（books.xml）

```java
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

XML对应实体类（Book.java）

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
package com.li.saxparse;

import java.util.List;

import com.li.entity.Book;

public interface XmlParse {
	public List<Book> parseXml(String fileName);
}

```

接口对应实现类（XMLDOMParser.java）

```java
package com.li.domparse;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.li.entity.Book;
import com.li.saxparse.XmlParse;

public class DOMXMLParser implements XmlParse {

	@Override
	public List<Book> parseXml(String fileName) {
		List<Book> list=new ArrayList<>();
		
		/*获取文档生成器工厂对象*/
		DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
		
		/*文档对象*/
		Document document=null;
		
		try {
			/*文档生成器*/
			DocumentBuilder builder=factory.newDocumentBuilder();
			
			document=builder.parse(new File(fileName));
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/*根节点*/
		Node rootNode = (Node) document.getDocumentElement();
		/*获取<book>元素列表*/
		NodeList bookElementList = rootNode.getChildNodes();
		for(int i=0; i<bookElementList.getLength(); i++) {
			/*获取单个<book>元素*/
			Node bookelement = (Node) bookElementList.item(i);
			/*取节点名称*/
			if(bookelement.getNodeName().equals("book")) {
				Book book=new Book();
				
				/*节点属性*/
				NamedNodeMap map=bookelement.getAttributes();
				Node booknoNode = (Node) map.getNamedItem("bookno");
				String bookNoString=booknoNode.getNodeValue();
				book.setBookNo(bookNoString.trim());
				
				/*获取子节点*/
				NodeList subBookElementList = bookelement.getChildNodes();
				for(int j=0; j<subBookElementList.getLength(); j++) {
					
					/*子节点*/
					Node subElemtNode = (Node) subBookElementList.item(j);
					
					/*子节点元素名称*/
					String subElementNameString=subElemtNode.getNodeName();
					
					/*分析子节点元素名称从而获取内容*/
					if(subElementNameString.equals("title")) {
						/*获取节点内容*/
						book.setTitle(subElemtNode.getTextContent().trim());
					}
					if(subElementNameString.equals("author")) {
						/*获取节点内容*/
						book.setAuthor(subElemtNode.getTextContent().trim());
					}
					if(subElementNameString.equals("price")) {
						/*获取节点内容*/
						book.setPrice(Double.parseDouble(subElemtNode.getTextContent().trim()));
					}
				}
				list.add(book);
			}
		}
		return list;
	}
	
	public static void main(String[] args) {
		XmlParse parser = new DOMXMLParser();
		List<Book> list = parser.parseXml("books.xml");
		System.out.println("DOM解析结果: ");
		for(Book book : list) {
			System.out.println(book);
		}
	}
}

```
