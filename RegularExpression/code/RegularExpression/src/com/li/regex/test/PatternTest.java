package com.li.regex.test;

public class PatternTest {

	public static void main(String[] args) {
		String regex="[0-9]{3}\\-?[0-9]{2}\\-?[0-9]{4}";
		String str="123121234";
		if(str.matches(regex)) {
			System.out.println("OK!");
		}
	}

}
