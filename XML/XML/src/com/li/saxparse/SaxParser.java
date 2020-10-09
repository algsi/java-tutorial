package com.li.saxparse;

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
		System.out.println("SAX½âÎö½á¹û£º");
		for(Book book : list) {
			System.out.println(book);
		}
	}

}
