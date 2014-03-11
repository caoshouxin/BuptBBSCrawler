package com.bupt.google.crawler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkExtract {

	// 网站爬取网页模式
	private final boolean inlineMODE;
	private final int limitLinkNumb;

	public LinkExtract() {
		// TODO Auto-generated constructor stub
		this.inlineMODE = false;
		this.limitLinkNumb = 10;
	}

	public LinkExtract(int limitLinkNumb, boolean inlineMode) {
		this.limitLinkNumb = limitLinkNumb;
		this.inlineMODE = inlineMode;
	}

	/**
	 * 将给定的链接（可能是绝对路径，可能是相对路径）转换为绝对路径，进行爬取
	 * 
	 * @param urlString
	 * @param targetUrl
	 * @return：返回绝对路径的网页地址
	 */
	private String getAbsoluteUrl(String urlString, String targetUrl) {
		if (!urlString.startsWith("http://")) {
			/*
			 * targetUrl是原始链接，用于当当前链接不是以http：开头的绝对路径而是相对路径时，
			 * 加上url的头部信息http://www.baidu.com/user/index.html;
			 */
			// String[] lists=urlString.split("/");
			if (urlString.startsWith("/")) {
				// 域名为：
				String yuming = targetUrl.substring(0,
						targetUrl.indexOf("/", 7));
				urlString = yuming + urlString;
			} else {
				int index = targetUrl.lastIndexOf("/");
				if (index > 6) {
					// 提取到http://www.baidu.com/user;上一级域名
					targetUrl = targetUrl.substring(0, index);
				}
				// =http://www.baidu.com+/+next.html
				urlString = targetUrl + "/" + urlString;
				// 注意区分链接信息，如果链接以/开头：此时的网址则以域名开头
			}

		} else {
			// http://www.baidu.com(/)index.html
			int urlIndex = urlString.indexOf("/", 7);
			int targetIndex = targetUrl.indexOf("/", 7);
			if (urlIndex == -1) {
				urlIndex = urlString.length();
			}
			if (targetIndex == -1) {
				targetIndex = targetUrl.length();
			}
			// 用于判断是否是一个网站内部网址链接，否则根据参数，
			if (!urlString.substring(0, urlIndex).equalsIgnoreCase(
					targetUrl.substring(0, targetIndex))) {
				if (inlineMODE)
					return "a";
				else
					return urlString;
			}
		}
		return urlString;
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
		final String regex = "<[aA][^>]*href=(\"([^\"]*)\"|\'([^\']*)\'|([^\\s>]*))[^>]*>(.*?)</[aA]>";
		final Pattern p = Pattern.compile(regex, Pattern.DOTALL);
		final Matcher m = p.matcher(htmlPage);
		//String crawlTitle=null;
		String link=null;
		int nowLinkNumb = 0;// 已经访问过的链接数
		while (m.find()) {
			// 对链接进行绝对路径处理，可以爬取网站外部链接
			//crawlTitle=m.group(2);
			link = getAbsoluteUrl(m.group(1), targetUrl);
			if (isValidUrl(link)) {
				if (!hasVisited.contains(link)) {
					//URLDecoder.decode(result, ENCODING);
					System.out.println("当前页面的子链接："+link);
					queueList.add(link);
				}

			}
			nowLinkNumb++;
			if (nowLinkNumb > limitLinkNumb) {
				break;
			}
		}
		return queueList;
	}
}
