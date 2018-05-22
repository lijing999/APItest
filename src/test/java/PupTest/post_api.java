package PupTest;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.chinasofti.oauth2.client.util.SignatureUtil;

public class post_api {

public static CloseableHttpClient httpclient=null;
    
    @BeforeClass
    public static void setUp(){

        httpclient=HttpClients.createDefault();
    }
    
    
    @SuppressWarnings("deprecation")
	@Test
    public void phoneTest() throws ClientProtocolException, IOException{        
        
        //1.获取授权码POST请求
        String url="https://oauth.pre.hubpd.com/pup-asserver/authorize";
        HttpPost post=new HttpPost(url);
        List<NameValuePair>parameList=new ArrayList<NameValuePair>();
        parameList.add(new BasicNameValuePair("client_id", "de16dddab9d440b68190f0fad76a6ad2"));
        parameList.add(new BasicNameValuePair("response_type", "code"));
        parameList.add(new BasicNameValuePair("redirect_uri", "json:http://cmpc.test.pdmi.cn/zcms"));
        parameList.add(new BasicNameValuePair("state", "ABCD"));
        parameList.add(new BasicNameValuePair("username", "lijingtest"));
        parameList.add(new BasicNameValuePair("password", "111111"));
        parameList.add(new BasicNameValuePair("available", "0"));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameList, Charset.forName("utf-8"));//根据数据构造请求body部分,带&符号要编码
        post.setEntity(entity);
        CloseableHttpResponse res = httpclient.execute(post);
        String resBody=EntityUtils.toString(res.getEntity());
       
        
        //2.获取用户令牌
        String url1="https://oauth.pre.hubpd.com/pup-asserver/accessToken";
        HttpPost post1=new HttpPost(url1);
        List<NameValuePair>parameList1=new ArrayList<NameValuePair>();
        parameList1.add(new BasicNameValuePair("client_id", "de16dddab9d440b68190f0fad76a6ad2"));
        parameList1.add(new BasicNameValuePair("client_secret", "12ca1f801091457f811eb1942139e7b0"));
        parameList1.add(new BasicNameValuePair("grant_type", "authorization_code"));
        parameList1.add(new BasicNameValuePair("redirect_uri", "http://cmpc.test.pdmi.cn/zcms"));
        parameList1.add(new BasicNameValuePair("code", resBody));
        UrlEncodedFormEntity entity1 = new UrlEncodedFormEntity(parameList1, Charset.forName("utf-8"));//根据数据构造请求body部分,带&符号要编码
        post1.setEntity(entity1);
        CloseableHttpResponse res1 = httpclient.execute(post1);
        String resBody1=EntityUtils.toString(res1.getEntity());
        System.out.println("test info");
        System.out.println(resBody1);
        
        //3.提取access_token
        JSONObject jo = JSONObject.parseObject(resBody1);
        String access_token = jo.getString("access_token");
        System.out.println(access_token);
                //4.用户签名
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("access_token", access_token);
        String paramStr = SignatureUtil.sortParameters(parameters);
        String sign = SignatureUtil.createSignature(paramStr);
        System.out.println(sign);
        
        //5.访问api/orgs
        String url2="https://oauth.pre.hubpd.com/pup/api/orgs?access_token=";
        HttpGet httpGet2=new HttpGet(url2 + access_token + "&sign=" + sign);
        //System.out.println(httpGet2.toString());
        CloseableHttpResponse res2 = httpclient.execute(httpGet2);
        String resBody2=EntityUtils.toString(res2.getEntity());
        System.out.println(resBody2);
        
        //6.输出响应（xml路径/xpath)json数据解析，断言（与数据库的值做比较）
        JSONObject jo1 = JSONObject.parseObject(resBody2);
        String resultcode = jo1.getString("result");
        Assert.assertEquals("200", resultcode);
        System.out.println("result:" +resultcode);
        
        
        
        
        /*
        //GET获取用户令牌
        String url2="https://oauth.pre.hubpd.com/pup-asserver/accessToken?client_id=";
        HttpGet httpGet2=new HttpGet(url2+ "de16dddab9d440b68190f0fad76a6ad2" + "&client_secret=" + "12ca1f801091457f811eb1942139e7b0" + "&grant_type=" + "authorization_code" + "&redirect_uri=" + "http://cmpc.test.pdmi.cn/zcms" +"&code=" + code);
        //System.out.println(httpGet2.toString());
        CloseableHttpResponse res2 = httpclient.execute(httpGet2);
        String resBody2=EntityUtils.toString(res2.getEntity());
        //System.out.println(resBody2);
        */
        
    }

}
