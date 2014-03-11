package com.bupt.google.crawler;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MysqlConnection {

	public static String drv = "com.mysql.jdbc.Driver";
	public static String url = "jdbc:mysql://localhost:3306/crawlcontent";
	public static String usr = "root";
	public static String pwd = "123";

	private Connection conn = null;
	private ResultSet rs = null;
	private PreparedStatement pstm = null;

	public PreparedStatement getPstm() {
		return pstm;
	}

	public void setPstm(PreparedStatement pstm) {
		this.pstm = pstm;
	}

	public void setPCstmParameter(int id, String value) {
		try {
			if (pstm != null) {
				pstm.setString(id, value);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setPCstmParameter(int id, int value) {
		try {
			if (pstm != null) {
				pstm.setInt(id, value);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setPCstmParameter(int id, FileInputStream files) {
		try {
			if (pstm != null) {
				pstm.setBinaryStream(id, files, files.available());
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public boolean createConn() {
		boolean b = false;
		try {
			Class.forName(drv).newInstance();
			conn = DriverManager.getConnection(url, usr, pwd);
			b = true;
		} catch (SQLException e) {
		} catch (ClassNotFoundException e) {
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		}
		return b;
	}

	public boolean update() {
		boolean b = false;
		try {
			if (pstm != null) {
				pstm.execute();
			}
			b = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	public void setPrepareStatement(String sql) {
		try {
			pstm = conn.prepareStatement(sql);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void query() {
		try {
			if (pstm != null) {
				rs = pstm.executeQuery();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean next() {
		boolean b = false;
		try {
			if (rs.next())
				b = true;
		} catch (Exception e) {
		}
		return b;
	}

	public String getValue(String field) {
		String value = "";
		try {
			if (rs != null)
				value = rs.getString(field);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (value == null)
			value = "";
		return value;
	}

	public InputStream getImage(String image) {
		InputStream value = null;
		try {
			if (rs != null)
				value = rs.getBinaryStream(image);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	public void closeConn() {
		try {
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
		}
	}

	public void closePstm() {
		try {
			if (pstm != null)
				pstm.close();
		} catch (SQLException e) {
		}
	}

	public void closeRs() {
		try {
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
		}
	}

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	public ResultSet getRs() {
		return rs;
	}

	public void setRs(ResultSet rs) {
		this.rs = rs;
	}

	/**
	 * 本方法用于将爬取的网页保存到crawlcontent.craltest数据库中
	 * 
	 * @param targetUrl
	 *            ：目标url
	 * @param text
	 *            ：要保存的文本数据
	 * @param xml
	 *            ：要保存的含html代码的网页内容
	 * @param unicode
	 *            ：编码方式;utf-8:标准
	 * @throws UnsupportedEncodingException
	 */
	public void saveMysql(String sql, String targetUrl, String text,
			String xml, String unicode) throws UnsupportedEncodingException {
		Date nowTime = new Date();
		SimpleDateFormat simpleFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");

		if (this.createConn()) {
			// String sql =
			// "insert into crawltest(linkName,linkValue,linkCode,crawlTime) values(?,?,?,?)";
			this.setPrepareStatement(sql);
			this.setPCstmParameter(1, targetUrl);
			if (unicode.equalsIgnoreCase("utf8")
					|| unicode.equalsIgnoreCase("utf-8")) {
				this.setPCstmParameter(2, text);
				this.setPCstmParameter(3, xml);
			} else {
				this.setPCstmParameter(2, new String(text.getBytes(), "utf-8"));
				this.setPCstmParameter(3, new String(xml.getBytes(), "utf-8"));
			}

			this.setPCstmParameter(4, simpleFormat.format(nowTime));
			this.update();
			this.closePstm();
			this.closeConn();
		}
		System.out.println("-----------------------------------------------");
	}

	public void saveToCrawlBupt(String unicode, String crawlLink,
			String crawlTime, String jobTitle, String jobContent,
			String jobTime, String jobPoster) {
		try {
			if (this.createConn()) {
				String sql = "insert into crawlBuptJob(crawlink,crawltime,jobTitle,jobcontent,jobtime,jobposter) values(?,?,?,?,?,?)";
				this.setPrepareStatement(sql);
				this.setPCstmParameter(1, crawlLink);
				this.setPCstmParameter(2, crawlTime);
				if (unicode.equalsIgnoreCase("utf8")
						|| unicode.equalsIgnoreCase("utf-8")) {
					this.setPCstmParameter(3, jobTitle);
					this.setPCstmParameter(4, jobContent);
					this.setPCstmParameter(5, jobTime);
					this.setPCstmParameter(6, jobPoster);
				} else {
					this.setPCstmParameter(3, new String(jobTitle.getBytes(),
							"utf-8"));
					this.setPCstmParameter(4, new String(jobContent.getBytes(),
							"utf-8"));
					this.setPCstmParameter(5, new String(jobTime.getBytes()));
					this.setPCstmParameter(6, new String(jobPoster.getBytes()));

				}
				this.update();
				this.closePstm();
				this.closeConn();
			}
			System.out
					.println("-----------------------------------------------");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}
}
