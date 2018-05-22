package cn.testfan.testng;

import org.testng.annotations.Test;

public class Test4 {

	@Test(groups = { "reg", "login" })
	public void f1() {
		System.out.println("in function f1...");
	}

	@Test(groups = { "login" })
	public void f2() {
		System.out.println("in function f2...");
	}

	@Test(groups = { "reg" })
	public void f3() {
		System.out.println("in function f3...");
	}

	@Test(dependsOnGroups = { "login", "reg" })
	public void f4() {
		System.out.println("in function f4...");
	}
}
