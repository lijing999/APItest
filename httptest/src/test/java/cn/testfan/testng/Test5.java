package cn.testfan.testng;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class Test5 {

	@DataProvider(name = "userpass")
	public Object[][] userPasswordDataProvider() {
		return new Object[][] { { "testfan", "123456" }, { "testng", "111111" } };
	}

	@Test(dataProvider = "userpass")
	public void login(String username, String password) {
		System.out.println("login with " + username + " and " + password);
	}

}
