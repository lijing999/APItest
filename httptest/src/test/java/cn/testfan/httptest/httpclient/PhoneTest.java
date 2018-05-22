package cn.testfan.httptest.httpclient;

import java.io.IOException;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.BeforeClass;
import org.junit.Test;

public class PhoneTest {

	public static CloseableHttpClient httpClient = null;

	@BeforeClass
	public static void setUp() {
		httpClient = HttpClients.createDefault();
	}

	/**
	 * 利用httpclient测试手机号码归属地接口
	 * 
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@Test
	public void phoneTest() throws ClientProtocolException, IOException {
		String phone = "15210011578";
		HttpGet httpGet = new HttpGet("http://apis.baidu.com/apistore/mobilenumber/mobilenumber?phone=" + phone);
		httpGet.addHeader("apikey", "248b30ecdf493be9d5fb818fe3b5e93d");
		CloseableHttpResponse res = httpClient.execute(httpGet);
		try {
			int statusCode = res.getStatusLine().getStatusCode();
			System.out.println(statusCode);
			String resBody = EntityUtils.toString(res.getEntity()); //得到响应的body部分
			// {"errNum":0,"retMsg":"success","retData":{"phone":"15210011578","prefix":"1521001","supplier":"\u79fb\u52a8","province":"\u5317\u4eac","city":"\u5317\u4eac","suit":"152\u5361"}}
			System.out.println(resBody);
			// "city":"\u5317\u4eac" t1:是冒号后面的"的位置 t2：是t1位置后面的双引号的位置
			int t1 = resBody.indexOf("city") + 7;
			int t2 = resBody.indexOf("\"", t1);
			String city = resBody.substring(t1, t2);// 根据两个双引号，截取city的内容：\u5317\u4eac
			city = StringEscapeUtils.unescapeJava(city);// 将\u5317\u4eac转为中文,即可得到归属地
			System.out.println(city);
		} finally {
			res.close();
		}
	}

	@Test
	public void test() {
		System.out.println("in test");
	}
}
