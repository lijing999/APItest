package cn.testfan.httptest.httpclient;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class HttpClientTest {

	public static CloseableHttpClient httpClient = null;

	@BeforeClass
	public static void setUpBeforeClass() {
		httpClient = HttpClients.createDefault();
	}

	@AfterClass
	public static void tearDownAfterClass() throws IOException {
		httpClient.close();
	}

	/**
	 * 如何利用httpclient框架发送get请求
	 * 
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@Test
	public void getTest() throws ClientProtocolException, IOException {
		String username = "test&f=an";
		username = URLEncoder.encode(username, "utf-8");
		String url = "http://101.200.167.51:8080/http/method?username=" + username + "&pwd=testfan111&cars=volvo";
		System.out.println(url);
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpResponse res = httpClient.execute(httpGet);
		try {
			int statusCode = res.getStatusLine().getStatusCode();// 获得状态行的状态码的信息
			System.out.println(statusCode);
			String resBody = EntityUtils.toString(res.getEntity());// 获得响应的http
																	// body部分
			System.out.println(resBody);
		} finally {
			res.close();
		}
	}

	/**
	 * 如何利用httpclient框架发送post请求
	 * 
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@Test
	public void postTest() throws ClientProtocolException, IOException {
		HttpPost post = new HttpPost("http://101.200.167.51:8080/http/method");
		// UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters,
		// "utf-8");
		List<NameValuePair> paramList = new ArrayList<>();// 将请求数据加入到此list中
		paramList.add(new BasicNameValuePair("username", "test=f&an"));
		paramList.add(new BasicNameValuePair("pwd", "123456"));
		paramList.add(new BasicNameValuePair("love", "1"));
		paramList.add(new BasicNameValuePair("love", "2"));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, Charset.forName("utf-8"));// 根据数据构造请求body部分
		post.setEntity(entity);
		CloseableHttpResponse res = httpClient.execute(post);// 发送请求
		try {
			int statusCode = res.getStatusLine().getStatusCode();
			// 得到响应的状态码
			System.out.println(statusCode);
			// 得到响应的内容
			String resBody = EntityUtils.toString(res.getEntity());
			System.out.println(resBody);
		} finally {
			res.close();
		}
	}

	/**
	 * get与重定向
	 * 
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@Test
	public void getAndRedirectTest() throws ClientProtocolException, IOException {
		String username = "test&f=an";
		username = URLEncoder.encode(username, "utf-8");
		String url = "http://101.200.167.51:8080/http/method?redirect=true&username=" + username
				+ "&pwd=testfan111&cars=volvo";
		System.out.println(url);
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpResponse res = httpClient.execute(httpGet);
		try {
			int statusCode = res.getStatusLine().getStatusCode();// 获得状态行的状态码的信息
			System.out.println(statusCode);
			String resBody = EntityUtils.toString(res.getEntity());// 获得响应的http
																	// body部分
			System.out.println(resBody);
		} finally {
			res.close();
		}
	}

	/**
	 * 手动实现跟随重定向
	 * 
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@Test
	public void postAndRedirectTest() throws ClientProtocolException, IOException {
		httpClient = HttpClients.createDefault();// 通过这个方法创建的httpclient，只能手工模拟重定向过程：即先发起post请求，然后发起get请求
		HttpPost post = new HttpPost("http://101.200.167.51:8080/http/method?redirect=true");
		// UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters,
		// "utf-8");
		List<NameValuePair> paramList = new ArrayList<>();// 将请求数据加入到此list中
		paramList.add(new BasicNameValuePair("username", "ykp"));
		paramList.add(new BasicNameValuePair("pwd", "123456"));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, Charset.forName("utf-8"));// 根据数据构造请求body部分
		post.setEntity(entity);
		CloseableHttpResponse res = httpClient.execute(post);// 发送请求
		String location = null;
		try {
			int statusCode = res.getStatusLine().getStatusCode();
			System.out.println(statusCode);
			location = res.getFirstHeader("Location").getValue();
		} finally {
			res.close();
		}
		HttpGet httpGet = new HttpGet(location);
		CloseableHttpResponse redictRes = httpClient.execute(httpGet);
		try {
			int statusCode = redictRes.getStatusLine().getStatusCode();// 获得状态行的状态码的信息
			System.out.println(statusCode);
			String resBody = EntityUtils.toString(redictRes.getEntity());// 获得响应的http
			// body部分
			System.out.println(resBody);
		} finally {
			res.close();
		}
	}

	/**
	 * 跟随重定向
	 * 
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@Test
	public void postAndAutoRedirectTest() throws ClientProtocolException, IOException {
		httpClient = HttpClients.custom().setRedirectStrategy(new LaxRedirectStrategy()).build();// 通过这个方法创建的httpclient，设置自动跟随重定向
		HttpPost post = new HttpPost("http://101.200.167.51:8080/http/method?redirect=true");
		// UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters,
		// "utf-8");
		List<NameValuePair> paramList = new ArrayList<>();// 将请求数据加入到此list中
		paramList.add(new BasicNameValuePair("username", "ykp"));
		paramList.add(new BasicNameValuePair("pwd", "123456"));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, Charset.forName("utf-8"));// 根据数据构造请求body部分
		post.setEntity(entity);
		CloseableHttpResponse res = httpClient.execute(post);// 发送请求
		try {
			int statusCode = res.getStatusLine().getStatusCode();
			System.out.println(statusCode);
			String resBody = EntityUtils.toString(res.getEntity());// 获得响应的http
			// body部分
			System.out.println(resBody);
		} finally {
			res.close();
		}
	}

	/**
	 * 如何向服务器发送json格式的数据
	 * 
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@Test
	public void postJsonTest() throws ClientProtocolException, IOException {
		HttpPost post = new HttpPost("http://101.200.167.51:8080/http/method1");
		String requestContent = "{'id':1,'name':'Yan Kunpeng','email':'yankunpeng85@sina.com','birthday':{'birthday':'19850101'},'regDate':'2015-07-07 11:35:08'}";
		StringEntity entity = new StringEntity(requestContent, "utf-8");// 如果是xml或json请求数据，那么通过StringEntity构造请求数据，同时设置字符集
		entity.setContentType("application/json");// 告诉服务器请求内容的类型
		post.setEntity(entity);
		CloseableHttpResponse res = httpClient.execute(post);// 发送请求
		try {
			int statusCode = res.getStatusLine().getStatusCode();
			System.out.println(statusCode);
			String resBody = EntityUtils.toString(res.getEntity());
			System.out.println(resBody);
		} finally {
			res.close();
		}
	}

	/**
	 * 如何向服务器发送xml格式的数据
	 * 
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@Test
	public void postXmlTest() throws ClientProtocolException, IOException {
		HttpPost post = new HttpPost("http://101.200.167.51:8080/http/method1");
		String requestContent = "<?xml version='1.0' encoding='ISO-8859-1'?><note><to>George</to><from>John</from><heading>Reminder</heading><body>Don't forget the meeting!</body></note>";
		StringEntity entity = new StringEntity(requestContent, "utf-8");// 如果是xml或json请求数据，那么通过StringEntity构造请求数据，同时设置字符集
		entity.setContentType("application/xml");// 告诉服务器请求内容的类型
		post.setEntity(entity);
		CloseableHttpResponse res = httpClient.execute(post);// 发送请求
		try {
			int statusCode = res.getStatusLine().getStatusCode();
			System.out.println(statusCode);
			String resBody = EntityUtils.toString(res.getEntity());
			System.out.println(resBody);
		} finally {
			res.close();
		}
	}

	/**
	 * 带有token验证信息的表单:如何提取表单中token，获得token后才能在提交表单时带上token
	 * 
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@Test
	public void postFormWithTokenTest() throws ClientProtocolException, IOException {
		HttpClientContext context = HttpClientContext.create();
		CookieStore cookieStore = new BasicCookieStore();// 创建cookie存储器，存储cookie信息
		context.setCookieStore(cookieStore);// context目的是保存各种配置信息，包括cookiestore
		HttpGet httpGet = new HttpGet("http://101.200.167.51:8080/http/tokenForm");
		CloseableHttpResponse res = httpClient.execute(httpGet, context);// 执行请求的时候，会通过context中的cookiestore进行cookie保存。
		String token = null;
		try {
			String resBody = EntityUtils.toString(res.getEntity());// 获得响应的http
																	// body部分
			System.out.println(resBody);
			// <input type="hidden" name="_token"
			// value="6c45hpvfbpt2clcdfrw6j5okv4ocearo">
			int t1 = resBody.indexOf("_token") + 15;// 找到value="的双引号的位置
			int t2 = resBody.indexOf("\"", t1); // 找到token后面的双引号的位置
			token = resBody.substring(t1, t2);// 截取token
			System.out.println(token);
		} finally {
			res.close();
		}
		HttpPost post = new HttpPost("http://101.200.167.51:8080/http/tokenForm");
		List<NameValuePair> paramList = new ArrayList<>();// 将请求数据加入到此list中
		paramList.add(new BasicNameValuePair("_token", token));//
		paramList.add(new BasicNameValuePair("username", "ykp"));
		paramList.add(new BasicNameValuePair("pwd", "123456"));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, Charset.forName("utf-8"));// 根据数据构造请求body部分
		post.setEntity(entity);
		CloseableHttpResponse postres = httpClient.execute(post, context);// 发送请求，帶上cookie信息
		try {
			int statusCode = postres.getStatusLine().getStatusCode();
			System.out.println(statusCode);
			String resBody = EntityUtils.toString(postres.getEntity());
			System.out.println(resBody);
		} finally {
			postres.close();
		}
	}
}
