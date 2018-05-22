package cn.testfan.testng;

import java.util.Random;

import org.testng.annotations.Test;

public class UserTest {
	private String username;
	private String password;
	private int userId;

	public UserTest(String username, String password) {
		this.username = username;
		this.password = password;
	}

	@Test
	public void login() {
		Random r = new Random();
		userId = r.nextInt(100);
		System.out.println("login with " + username + " and userId " + userId);
	}

	@Test(dependsOnMethods = { "login" }, enabled = false)
	public void publishArticle() {
		System.out.println("publish article with " + username + " and userId:" + userId);
	}

	@Test(dependsOnMethods = { "login" })
	public void publishArticle2() {
		System.out.println("publish article with " + username + " and userId:" + userId);
	}
}
