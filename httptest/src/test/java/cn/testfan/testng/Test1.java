package cn.testfan.testng;

import org.testng.annotations.Test;

public class Test1 {

	@Test
	public void g1() {
		System.out.println("in function g1...");
	}

	@Test(dependsOnMethods = { "g1" })
	public void f2() {
		System.out.println("in function f2...");
	}

	@Test(dependsOnMethods = { "f2", "g1" })
	public void f1() {
		System.out.println("in function f1...");
	}

}
