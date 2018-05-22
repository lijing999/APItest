package cn.testfan.testng;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

public class Test2 {
	@BeforeSuite
	public void beforSuite() {
		System.out.println("beforSuite ...");
	}

	@AfterSuite
	public void afterSuite() {
		System.out.println("afterSuite ...");
	}

	@BeforeClass
	public void beforeClass() {
		System.out.println("beforeClass...");
	}

	@AfterClass
	public void afterClass() {
		System.out.println("afterClass...");
	}

	@BeforeMethod
	public void beforeMethod() {
		System.out.println("beforeMethod...");
	}

	@AfterMethod
	public void afterMethod() {
		System.out.println("afterMethod...");
	}

	@Test
	public void f1() {
		System.out.println("Test2 . in function f1...");
	}

	@Test
	public void f2() {
		System.out.println("Test2 . in function f2...");
	}
}
