package com.bupt.google.crawler;

import java.io.IOException;

public class StringTest {

	public static void main(String[] args) throws IOException {
		String str="../user/index.jsp";
		System.out.println(str.lastIndexOf("/"));
		String[] regs=str.split("/");
		for (String string : regs) {
			System.out.println(string);
		}
	}
}
