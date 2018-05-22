package PupTest;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import com.alibaba.fastjson.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
//import com.chinasofti.oauth2.client.util.SignatureUtil;

public class get_api {
public static CloseableHttpClient httpclient=null;
    
    @BeforeClass
    public static void setUp(){
        httpclient=HttpClients.createDefault();
        
    }
      
    
    @Test
    public void phoneTest() throws ClientProtocolException, IOException{
        //1.获取access_token
//        String client_id="261a7f2196c7450580d200720c1b8b0d";
//        String client_secret="1f26e53d0fce418793cd6e822ef2ba22";
        
      //clientid和clientSecret参数用properties文件
        Properties prop=new Properties();
        FileInputStream in=new FileInputStream("C:\\jworkspace\\Httptest1\\client.properties");
        prop.load(in);
        String client_id=prop.getProperty("clientId");
        String client_secret=prop.getProperty("clientSecret");
        System.out.println(client_id);
        System.out.println(client_secret);
        String url="https://oauth.pre.hubpd.com/pup-asserver/appToken?client_id=";
        HttpGet httpGet=new HttpGet(url+ client_id + "&client_secret=" + client_secret);
        CloseableHttpResponse res = httpclient.execute(httpGet);
        String resBody=EntityUtils.toString(res.getEntity());
        System.out.println(resBody);
        
        
        
        //2.提取access_token的值
        JSONObject jo = JSONObject.parseObject(resBody); 
        System.out.println(jo.toString());
        String Access_token=jo.getString("access_token");
        System.out.println(Access_token);
        
        //3. 用户签名 --------- 需要中软的jar包放到maven的pom.xml配置文件
    }
    

}
