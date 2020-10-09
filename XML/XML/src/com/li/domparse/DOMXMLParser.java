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
		
		/*��ȡ�ĵ���������������*/
		DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
		
		/*�ĵ�����*/
		Document document=null;
		
		try {
			/*�ĵ�������*/
			DocumentBuilder builder=factory.newDocumentBuilder();
			
			document=builder.parse(new File(fileName));
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/*���ڵ�*/
		Node rootNode = (Node) document.getDocumentElement();
		/*��ȡ<book>Ԫ���б�*/
		NodeList bookElementList = rootNode.getChildNodes();
		for(int i=0; i<bookElementList.getLength(); i++) {
			/*��ȡ����<book>Ԫ��*/
			Node bookelement = (Node) bookElementList.item(i);
			/*ȡ�ڵ�����*/
			if(bookelement.getNodeName().equals("book")) {
				Book book=new Book();
				
				/*�ڵ�����*/
				NamedNodeMap map=bookelement.getAttributes();
				Node booknoNode = (Node) map.getNamedItem("bookno");
				String bookNoString=booknoNode.getNodeValue();
				book.setBookNo(bookNoString.trim());
				
				/*��ȡ�ӽڵ�*/
				NodeList subBookElementList = bookelement.getChildNodes();
				for(int j=0; j<subBookElementList.getLength(); j++) {
					
					/*�ӽڵ�*/
					Node subElemtNode = (Node) subBookElementList.item(j);
					
					/*�ӽڵ�Ԫ������*/
					String subElementNameString=subElemtNode.getNodeName();
					
					/*�����ӽڵ�Ԫ�����ƴӶ���ȡ����*/
					if(subElementNameString.equals("title")) {
						/*��ȡ�ڵ�����*/
						book.setTitle(subElemtNode.getTextContent().trim());
					}
					if(subElementNameString.equals("author")) {
						/*��ȡ�ڵ�����*/
						book.setAuthor(subElemtNode.getTextContent().trim());
					}
					if(subElementNameString.equals("price")) {
						/*��ȡ�ڵ�����*/
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
		System.out.println("DOM�������: ");
		for(Book book : list) {
			System.out.println(book);
		}
	}

}
