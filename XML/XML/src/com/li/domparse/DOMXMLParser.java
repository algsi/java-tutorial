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
