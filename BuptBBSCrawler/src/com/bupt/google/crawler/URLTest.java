package com.bupt.google.crawler;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class URLTest {
	private final MysqlConnection mysql = new MysqlConnection();
	private String ENCODING;
	public void testHomePage(String targetUrl) throws MalformedURLException {
		URL url = new URL(targetUrl);
		URLConnection conn = null;
		try {
			conn = url.openConnection();
			InputStream in = conn.getInputStream();
			BufferedInputStream buff = new BufferedInputStream(in);
			byte[] b = new byte[1024];
			File file = new File("out.txt");
			FileOutputStream fout = new FileOutputStream(file, true);
			BufferedOutputStream buffout = new BufferedOutputStream(fout);
			while ((buff.read(b)) != -1) {
				buffout.write(b);
			}
			buffout.flush();
			buffout.close();
			buff.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void testHtmlUnitPage(String urlString){
		WebClient webClient = new WebClient(BrowserVersion.FIREFOX_17);
		// 设置webClient的参数
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setActiveXNative(true);
		webClient.getOptions().setCssEnabled(true);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		webClient.setAjaxController(new NicelyResynchronizingAjaxController());
		// 模拟浏览器打开一个目标网址
		String xml = null;
		HtmlPage page = null;
		WebResponse webResponse = null;
		try {
			page=webClient.getPage(new URL(urlString));
			webResponse = page.getWebResponse();
			xml = page.asXml();
			ENCODING = webResponse.getContentCharset();
			// 保存到文本文件
			WriteToFile.writeHtmlToFile(xml);
		} catch (FailingHttpStatusCodeException e) {
			// TODO Auto-generated catch block
			System.err.println(urlString+": FailingHttpStatusCodeException");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			System.err.println(urlString+"MalformedURLException");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println(urlString+"IOException");
		}
		
		/*// 保存到数据库
		try {
			if (page!=null) {
				mysql.saveMysql(urlString, page.asText(), xml, ENCODING);
			}else{
				System.out.println(urlString+"无法访问");
			}
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
		}*/
		//return xml;
	}
	public void testURL(String urlString) throws IOException {
		URL url = new URL(
				"http://www.cnblogs.com/gpcuster/archive/2010/06/04/1751538.html");
		// URLConnection conn=url.openConnection();
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		System.out.println(conn.getResponseCode());
		System.out.println(" url的授权：" + url.getAuthority());
		System.out.println("URL的的主机名：" + url.getHost());
		System.out.println("URL的锚点：" + url.getRef());
		System.out.println("URL的查询部分：" + url.getQuery());
		System.out.println("URL的路径：" + url.getPath());
		System.out.println("URL的u用户信息：" + url.getUserInfo());
	}

	public static void main(String[] args) throws IOException {
		String urlString=" <a href=\"/article/JobInfo/127445\">北京首都国际机场股份有限公司2014校园招聘火热启动</a>";
		//http://bbs.byr.cn/elite/path?v=%2Fgroups%2Fsystem.faq%2FJobInfo
		//String urlString="http://www.google.co.jp/#newwindow=1&q=ubuntu mysql-workbench使用教程";
		/*URL url = new URL(urlString);
		HttpURLConnection conn=(HttpURLConnection) url.openConnection();
		if(conn.getResponseCode()==200){
			URLDecoder decoder=new URLDecoder();
			System.out.println(decoder.decode(urlString,"utf-8"));
		}*/
		Pattern p = Pattern.compile("(\\d+)([￥$])");  
	     String str = "8899￥";  
	     Matcher m = p.matcher(str);  
	     if(m.matches()){  
	      System.out.println("货币金额: " + m.group(2));  
	      System.out.println("货币种类: " + m.group(1));  
	     }  
	}
}
