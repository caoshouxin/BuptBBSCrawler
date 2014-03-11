package com.bupt.google.crawler;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

public class WriteToFile {

	/**
	 * 将爬取网页的内容通过流写入文本文件用于保存数据
	 * 
	 * @throws IOException
	 */
	public static void writeHtmlToFile(InputStream in) throws IOException {
		BufferedInputStream buff = new BufferedInputStream(in);
		byte[] b = new byte[1024];
		File file = new File("downloadHtml.txt");
		FileOutputStream fout = new FileOutputStream(file, true);
		BufferedOutputStream buffout = new BufferedOutputStream(fout);
		while ((buff.read(b)) != -1) {
			buffout.write(b);
		}
		buffout.flush();
		buffout.close();
		buff.close();
	}

	/**
	 * 将爬取网页的内容字符串写入 文本文件用于保存数据
	 * 
	 * @throws IOException
	 */
	public static void writeHtmlToFile(String htmlContent) throws IOException {
		BufferedWriter buff = new BufferedWriter(
				new FileWriter("out.txt", true));
		buff.write(htmlContent);
		buff.flush();
		buff.close();
	}
}
