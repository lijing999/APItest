package cn.testfan.httptest.res;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import com.alibaba.fastjson.JSONObject;

public class JsonExtractorTest {

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

			JSONObject jo = JSONObject.parseObject(resBody);
			int errNum = jo.getIntValue("errNum");
			String retMsg = jo.getString("retMsg");
			System.out.println("errNum: " + errNum);
			System.out.println("retMsg: " + retMsg);

			JSONObject retData = jo.getJSONObject("retData");
			String city = retData.getString("city");
			System.out.println(city);
		} finally {
			res.close();
		}
	}

	/**
	 * 一个接口的输出做处理，然后才可以作为下一个用例的输入
	 */
	@Test
	public void phoneTest1() throws ClientProtocolException, IOException {
		String phone = "15210011578";
		HttpGet httpGet = new HttpGet("http://apis.baidu.com/apistore/mobilenumber/mobilenumber?phone=" + phone);
		httpGet.addHeader("apikey", "248b30ecdf493be9d5fb818fe3b5e93d");
		CloseableHttpResponse res = httpClient.execute(httpGet);
		try {
			String resBody = EntityUtils.toString(res.getEntity());
			// {"errNum":0,"retMsg":"success","retData":{"phone":"15210011578","prefix":"1521001","supplier":"\u79fb\u52a8","province":"\u5317\u4eac","city":"\u5317\u4eac","suit":"152\u5361"}}
			System.out.println(resBody);

			JSONObject jo = JSONObject.parseObject(resBody);
			System.out.println(jo.toString());
			JSONObject retData = jo.getJSONObject("retData"); //retData.getString可以获取到字段值
			System.out.println(retData.toString());
			retData.remove("suit");
			System.out.println(retData.toString());
			retData.put("month", 12);
			System.out.println(retData.toString());
			retData.put("supplier", "联通");
			System.out.println(retData.toString());
			List phones = new ArrayList();
			phones.add("1820000001");
			phones.add("1820000002");
			phones.add("1820000003");
			retData.put("contacthistory", phones);
			System.out.println(retData.toString());
		} finally {
			res.close();
		}
	}
}
