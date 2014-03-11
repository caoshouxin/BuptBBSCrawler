package com.bupt.google.crawler;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * htmlunit工具的简单使用测试
 * 
 * @author hadoop
 * 
 */
public class HtmlUnitCrawler {

	private final MysqlConnection mysql = new MysqlConnection();
	private String ENCODING;

	/**
	 * 可以爬取没有js的网页
	 * 
	 * @param targetUrl
	 * @throws MalformedURLException
	 */
	public void testHomePage(String targetUrl) throws MalformedURLException {
		URL url = new URL(targetUrl);
		URLConnection conn = null;
		try {
			conn = url.openConnection();
			InputStream in = conn.getInputStream();
			WriteToFile.writeHtmlToFile(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 可以解析获取含有JS的网页
	 * 
	 * @param targetUrl
	 * @throws FailingHttpStatusCodeException
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public String parserPage_Firefox(String targetUrl) {
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
			page=webClient.getPage(new URL(targetUrl));
			webResponse = page.getWebResponse();
			xml = page.asXml();
			ENCODING = webResponse.getContentCharset();
			// 保存到文本文件
			//WriteToFile.writeHtmlToFile(xml);
		} catch (FailingHttpStatusCodeException e) {
			// TODO Auto-generated catch block
			System.err.println(targetUrl+": FailingHttpStatusCodeException");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			System.err.println(targetUrl+"MalformedURLException");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println(targetUrl+"IOException");
		}
		
		// 保存到数据库
		/*try {
			if (page!=null) {
				String sql = "insert into crawltest(linkName,linkValue,linkCode,crawlTime) values(?,?,?,?)";
				mysql.saveMysql(sql,targetUrl, page.asText(), xml, ENCODING);
			}else{
				System.out.println(targetUrl+"无法访问");
			}
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
		}*/
		return xml;
	}

	public static void main(String[] args)
			throws FailingHttpStatusCodeException, MalformedURLException,
			IOException {
		HtmlUnitCrawler crawler = new HtmlUnitCrawler();
		// testConnection();测试数据库连接是否成功
		Scanner scanner = new Scanner(System.in);
		// http://localhost:8080/testNutch/index.jsp 2 10 false
		System.out.println("输入格式为：url depth linkNumb mode;(爬取深度为：depth，"
				+ "爬取每一层的连接数,是否爬取网站外部链接)");
		String url = scanner.next();
		int depth = scanner.nextInt();
		int linkNumb = scanner.nextInt();
		// true：只爬取网站内部链接，false：可以爬取外部链接
		boolean crawlermode = scanner.nextBoolean();
		System.out.println("开始爬取------depth: " + depth + "------limitLinks:"
				+ linkNumb + "------crawlerMode:" + crawlermode);
		crawler.crawlerSite(url, depth, linkNumb, crawlermode);

		System.out.println("网页爬取完成!");
	}

	/**
	 * 要爬取网页的方法
	 * 
	 * @param queueList
	 *            :待爬取的链接队列
	 * @param hasVisited
	 *            ：已访问的链接集合
	 * @param depth
	 *            ：要爬取的深度
	 * @param linkNumb
	 *            ：每一层要爬取的数量
	 * @throws FailingHttpStatusCodeException
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public void crawlerSite(String targetUrl, int depth, int linkNumb,
			boolean crawlerMode) throws FailingHttpStatusCodeException,
			MalformedURLException, IOException {
		// TODO Auto-generated method stub
		final List<String> queueList = new LinkedList<>();// 先来先服务的队列
		final Set<String> hasVisited = new HashSet<>();
		LinkExtract parserLinks = new LinkExtract(linkNumb, crawlerMode);
		String htmlPage = null;
		int nowDepth = 0;
		int i = 1;

		queueList.add(targetUrl);
		// 遍历队列
		while (queueList.size() > 0) {
			targetUrl = queueList.remove(0).trim();
			// 判断条件：如果深度满足depth，退出;如果链接已经访问过，跳过;如果当前链接为卫兵：则跳过
			if (targetUrl.equals("a")) {
				nowDepth++;
				continue;
			}
			if (hasVisited.contains(targetUrl)) {
				continue;
			}
			if (nowDepth >= depth) {
				break;
			}
			hasVisited.add(targetUrl);
			htmlPage = parserPage_Firefox(targetUrl);
			queueList.add("a");// 卫兵
			System.out.println(i + ": 当前链接：" + targetUrl);
			i++;
			// 对读列添加链接,核心方法之一
			parserLinks.addPageLinks(htmlPage, queueList, hasVisited,
					targetUrl, ENCODING);

			System.out
					.println("---------------------------------------------------");
		}
		System.out.println("共爬取了：" + (i - 1) + "个链接;");
	}
}
