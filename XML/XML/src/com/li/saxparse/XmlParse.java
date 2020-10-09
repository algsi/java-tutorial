package com.li.saxparse;

import java.util.List;

import com.li.entity.Book;

public interface XmlParse {
	public List<Book> parseXml(String fileName);
}
