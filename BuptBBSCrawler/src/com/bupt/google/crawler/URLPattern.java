package com.bupt.google.crawler;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLPattern {
	private static final String linkContent = "<td[\\s]*class=\"a-content\"[\\s]*>(.*?)[\\s]*--[\\s]*<br/>";
	private static void parserURL()
			throws UnsupportedEncodingException, IOException {
		// String str = "<a href=\"www.baidu.com\">ni</a>";
		/*
		 * URL url = new URL("http://localhost:8080/testNutch/index.jsp");
		 * URLConnection conn = url.openConnection(); BufferedInputStream buff =
		 * new BufferedInputStream( conn.getInputStream());
		 */
		FileInputStream fin = new FileInputStream("out.txt");

		byte[] b = new byte[fin.available()];
		System.out
				.println("开始---------------------------------------------------");
		String urlcontent = new String("");
		while ((fin.read(b)) != -1) {
			urlcontent += new String(b, "Utf-8");
			// String regex =
			// "<[aA][^>]*href=(\"([^\"]*)\"|\'([^\']*)\'|([^\\s>]*))[^>]*>(.*?)</[aA]>";
		}
		Pattern pattern = Pattern.compile(linkContent, Pattern.DOTALL);
		Matcher mat = pattern.matcher(urlcontent);
		/*
		 * while (mat.find()) { System.out.println(mat.group() + " "); }
		 */
		System.out.println("meiyoume ");
		while (mat.find()) {
		/*	System.out.println(mat.group(1).trim() + "  2"
					+ mat.group(2).trim() + "  3" + mat.group(3).trim());*/
			System.out.println(mat.group(1)+"  ");
		}
	}

	private static void testJavaRegex(String regex) {
		/*
		 * String urlString = "<a href=\"http://www.baidu.com\">nihao</a>  " +
		 * "<a href=\"/article/JobInfo/127445\">北京首都国际机场股份有限公司2014校园招聘火热启动</a>";
		 */
		String urlString = "<a href=\"/article/JobInfo/127805\">2013年动感求职万企校园行大型招聘会 "
				+ "北京站（送mini ipad）</a> </td><td class=\"title_10\">2013-12-03</td>";
		// String regex = "<[aA][^>]*href=\"([^\"]*)\"[^>]*>(.*?)</[aA]>";
		Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
		Matcher mat = pattern.matcher(urlString);
		/*
		 * while (mat.find()) { System.out.println(mat.group()); }
		 */
		// System.out.println(mat.group());
		// System.out.println(mat.group());
		while (mat.find()) {
			System.out.println(mat.group(1).trim());
			System.out.println(mat.group(2).trim());
		}
	}

	public static void main(String[] args) throws IOException {
		System.out.println("2013年动感求职万企");
		// String urlString =
		//用于分析页面中的内容，标题，时间发帖者
		//String regex = "<[aA][\\s]*href=\"(/article/JobInfo/[\\d]*)\">(.*?)</[aA]>[\\s]*</td>[\\s]*<td[^>]*>(.*?)</td>[\\s]*<td[^>]*>[^<]*<[aA][^>]*href=\"/user/query/(.*?)\"[^>]*>";
		// <td[^>]*>.*?<[aA][^>]*href=\"/user/query/[\\S]*\"[^>]*>(.*>)</[aA]>"
		//-----------------------------------------------------------
		//String regex="<a[\\s]*href=\"(/board/JobInfo[\\S]+)\"[^>]*>";
		
		parserURL();
		// testJavaRegex(regex);
		// String regex="(<a[^<]*[^t]{1}[^i]{1}[^t]{1}[^>]*>)";
		//String url = " <a s><a href=\"www.baiud.com\"><a target=\"_blank\" href=\"/article/JobInfo/25455\" title=\"在新窗口打开此主题\"> <samp class=\"tag ico-pos-article-top\"/> </a>";
		// Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
		// Matcher mat = pattern.matcher(url);
		/*
		 * while (mat.find()) { System.out.println(mat.group()); }
		 */
		// System.out.println(mat.group());
		// System.out.println(mat.group());
	}
}