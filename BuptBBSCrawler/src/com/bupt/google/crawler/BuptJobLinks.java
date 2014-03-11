package com.bupt.google.crawler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 注意本爬虫：用于bbs.byr.cn中招聘板块网页爬取
 * 
 * @author hadoop
 * 
 */
public class BuptJobLinks {

	private MysqlConnection mysql = new MysqlConnection();
	// 网站爬取网页模式
	private final String linkRegex = "<[aA][\\s]*href=\"(/article/JobInfo/[\\d]*)\">(.*?)</[aA]>[\\s]*</td>[\\s]*"
			+ "<td[^>]*>(.*?)</td>[\\s]*<td[^>]*>[^<]*<[aA][^>]*href=\"/user/query/(.*?)\"[^>]*>";
	private final String linkPage = "<a[\\s]*href=\"(/board/JobInfo[\\S]+)\"[^>]*>";
	private static final String linkContent = "<td[\\s]*class=\"a-content\"[\\s]*>(.*?)[\\s]*--[\\s]*<br/>";

	/**
	 * 将给定的链接（可能是绝对路径，可能是相对路径）转换为绝对路径，进行爬取
	 * 
	 * @param urlString
	 * @param targetUrl
	 * @return：返回绝对路径的网页地址
	 */
	private String getAbsoluteUrl(String urlString, String targetUrl) {
		return targetUrl.substring(0, targetUrl.indexOf("/", 7)) + urlString;
	}

	/**
	 * 判断给定的url是否合法
	 * 
	 * @param urlString
	 * @return
	 * @throws IOException
	 */
	private boolean isValidUrl(String urlString) {
		// TODO Auto-generated method stub
		if (urlString.equals("a"))
			return false;
		try {
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			int state = conn.getResponseCode();
			if (state == 200) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println(urlString + "  :url验证过程出现错误");
			return false;
		}
	}

	/**
	 * 分析网页源码获取网页中含有的超链接
	 * 
	 * @param htmlPage
	 * @param targetUrl
	 * @param limitLinkNumb
	 * @return:返回含有超链接的网页的队列
	 * @throws IOException
	 */
	public List<String> addPageLinks(String htmlPage,
			final List<String> queueList, final Set<String> hasVisited,
			String targetUrl, final String ENCODING) throws IOException {
		// 用于URL字符串匹配，提取超链接
		final Pattern p = Pattern.compile(linkPage, Pattern.DOTALL);
		final Matcher m = p.matcher(htmlPage);
		beforProcess(htmlPage, targetUrl, ENCODING);
		String link = null;// 当前的链接
		while(m.find()) {
			// 对链接进行绝对路径处理，可以爬取网站外部链接
			link = getAbsoluteUrl(m.group(1), targetUrl);
			if (isValidUrl(link)) {
				if (!hasVisited.contains(link)) {
					// URLDecoder.decode(result, ENCODING);
					System.out.println("当前页面的子链接：" + link);
					queueList.add(link);
				}

			}
		}
		return queueList;
	}

	private void beforProcess(String htmlPage, String targetUrl, String eNCODING)
			throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		final Pattern p = Pattern.compile(linkRegex, Pattern.DOTALL);
		final Matcher m = p.matcher(htmlPage);

		String crawlTitle = null;
		String crawlTime = null;
		String crawlPoster = null;
		String crawlContent = null;
		String crawlLink = null;
		Date nowTime = new Date();
		String encoding = null;
		SimpleDateFormat simpleFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");

		while(m.find()) {
			crawlLink = m.group(1);
			crawlTitle = m.group(2);
			crawlTime=m.group(3);
			crawlPoster = m.group(4);
			crawlLink = getAbsoluteUrl(crawlLink, targetUrl);
			if (isValidUrl(crawlLink)) {
				crawlContent = ParserPage_Firefox.parserPage_Firefox(crawlLink);
				encoding = ParserPage_Firefox.ENCODING;
				crawlContent = findJobContent(crawlContent);
				if (crawlContent == null) {
					System.out.println("findJobContent:程序运行错误!");
				} else {
					mysql.saveToCrawlBupt(encoding, crawlLink,
							simpleFormat.format(nowTime), crawlTitle,
							crawlContent, crawlTime, crawlPoster);
					//System.out.println(crawlLink+" "+crawlContent+"  "+crawlPoster+"  "+crawlTitle+"  "+crawlTime);
				}
			}
		}
	}

	private String findJobContent(String crawlContent) {
		// TODO Auto-generated method stub

		final Pattern p = Pattern.compile(linkContent, Pattern.DOTALL);
		final Matcher m = p.matcher(crawlContent);
		if (m.find()) {
			return m.group(1);
		} else {
			return null;
		}
	}
}
