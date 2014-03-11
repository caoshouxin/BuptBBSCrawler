package com.bupt.google.crawler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class ParserPage_Firefox {
	public static String ENCODING="utf-8";

	public static String parserPage_Firefox(String targetUrl) {
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
			page = webClient.getPage(new URL(targetUrl));
			webResponse = page.getWebResponse();
			xml = page.asXml();
			ENCODING = webResponse.getContentCharset();
			// 保存到文本文件
			// WriteToFile.writeHtmlToFile(xml);
		} catch (FailingHttpStatusCodeException e) {
			// TODO Auto-generated catch block
			System.err.println(targetUrl + ": FailingHttpStatusCodeException");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			System.err.println(targetUrl + "MalformedURLException");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println(targetUrl + "IOException");
		}
		return xml;
	}
}
