package cn.testfan.redis;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;

import redis.clients.jedis.Jedis;

public class RedisJavaTest extends AbstractJavaSamplerClient {

	private Jedis jedis = null;
	private String host = null;
	private int port = 0;
	private String password = null;

	@Override
	// 设置传入的参数，可以设置多个，已设置的参数会显示到Jmeter的参数列表中
	public Arguments getDefaultParameters() {
		return super.getDefaultParameters();
	}

	@Override
	// 初始化方法，实际运行时每个线程仅执行一次，在测试方法运行前执行
	public void setupTest(JavaSamplerContext context) {
		System.out.println("start to connect redis!");
		JMeterContext jmctx = JMeterContextService.getContext();
		JMeterVariables vars = jmctx.getVariables();// 从用户自定义变量中取变量值
		host = vars.get("redis-host");
		port = Integer.parseInt(vars.get("redis-port"));
		password = vars.get("redis-password");
		try {
			jedis = new Jedis(host, port, 2000);// 连接redis
			jedis.auth(password);// 登录redis
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("success to connect redis!");
		super.setupTest(context);
	}

	@Override
	public void teardownTest(JavaSamplerContext context) {
		super.teardownTest(context);
		jedis.close();
	}

	// 测试执行的循环体，根据线程数和循环次数的不同可执行多次
	public SampleResult runTest(JavaSamplerContext arg0) {
		SampleResult result = new SampleResult();
		result.sampleStart();// 开始事务，即开始计时
		try {
			String mobile = jedis.get("mobile");
			result.setResponseData(mobile.getBytes());// 设置响应的body
			result.setSuccessful(true);// 设置执行结果为成功
			result.setResponseMessage("OK");// 设置响应消息，类似于http响应的状态行
		} catch (Exception e) {
			result.setSuccessful(false);// 设置执行结果为失败
			result.setResponseMessage(e.getMessage());// 设置响应消息为异常消息
		}
		result.sampleEnd();// 计时结束
		return result;
	}

}
