package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by lijing on 2018/5/17.
 */
public class PropUtil {
    public static Properties getProperties(String filepath) throws FileNotFoundException, IOException {

        FileInputStream file=new FileInputStream(filepath);
        Properties prop=new Properties();
        prop.load(file);
        return prop;
    }

    public static void main(String[] args){
        Properties prop= null;
        try {
            prop = PropUtil.getProperties("C:\\jworkspace\\Httptest1\\client.properties");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("client_id: " + prop.getProperty("clientId"));
        System.out.println("client_secret: " + prop.getProperty("clientSecret"));
    }
}
