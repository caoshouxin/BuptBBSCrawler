package com.bupt.google.crawler;

import java.io.BufferedInputStream;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
public class HttpClientTest {

	private static String url="http://bbs.byr.cn/#!article/WorkLife/891195";
	
	public static void main(String[] args) throws ClientProtocolException, IOException {
		HttpClient httpClient=HttpClients.createDefault();
		HttpGet htttpGet=new HttpGet(url);
		HttpResponse httpResponse=httpClient.execute(htttpGet);
		HttpEntity httpEntity=httpResponse.getEntity();
		//System.out.println(httpEntity.getContentLength());
		BufferedInputStream buf=new BufferedInputStream(httpEntity.getContent());
		byte[] b=new byte[1024];
		while (buf.read(b)!=-1) {
			System.out.println(new String(b,"GBK"));
		}
		buf.close();
	}
}
