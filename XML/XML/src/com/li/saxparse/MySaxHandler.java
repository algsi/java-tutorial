package com.li.saxparse;

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
