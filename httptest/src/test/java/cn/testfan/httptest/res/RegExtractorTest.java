package cn.testfan.httptest.res;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.BeforeClass;
import org.junit.Test;

public class RegExtractorTest {

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
			String resBody = EntityUtils.toString(res.getEntity());
			// {"errNum":0,"retMsg":"success","retData":{"phone":"15210011578","prefix":"1521001","supplier":"\u79fb\u52a8","province":"\u5317\u4eac","city":"\u5317\u4eac","suit":"152\u5361"}}
			System.out.println(resBody);

			// Pattern cityPattern = Pattern.compile("\"city\":\"(.+?)\""); //
			// 得到要匹配内容的模式
			// Matcher matcher = cityPattern.matcher(resBody); //
			// 通过对响应内容进行匹配，得到一个匹配器（搜索器）
			// if (matcher.find()) {// 如果搜索到匹配的字符串
			// int groupCount = matcher.groupCount();// group的数量
			// System.out.println(groupCount);
			// String str = matcher.group(0); // city_g0="city":"\u5317\u4eac"
			// String city = matcher.group(1); // city_g1=\u5317\u4eac
			// System.out.println("str:" + str);
			// System.out.println("city:" + city);
			// }

			Pattern cityPattern = Pattern.compile("\"supplier\":\"(.+?)\".*?\"city\":\"(.+?)\"");
			Matcher matcher = cityPattern.matcher(resBody);
			while (matcher.find()) {
				int groupCount = matcher.groupCount();// group的数量
				System.out.println(groupCount);
				String str = matcher.group(0); // city_g0="city":"\u5317\u4eac"
				String supplier = matcher.group(1); // city_g1=\u5317\u4eac
				String city = matcher.group(2); // city_g1=\u5317\u4eac
				System.out.println("str:" + str);
				System.out.println("supplier:" + supplier);
				System.out.println("city:" + city);
			}

		} finally {
			res.close();
		}
	}

}
