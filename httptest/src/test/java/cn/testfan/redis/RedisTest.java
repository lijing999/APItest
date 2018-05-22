package cn.testfan.redis;

import java.util.List;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

public class RedisTest {

	@Test
	public void stringTest() {
		Jedis jedis = new Jedis("101.200.167.51", 6379);
		jedis.auth("testfan");
		// 类型1：String
		jedis.set("articlecomment:2", "1"); // 设置value为1
		String value = jedis.get("articlecomment:2"); // 查询
		System.out.println(value);
		Long value1 = jedis.incr("articlecomment:2"); // value=value+1
		System.out.println(value1);
		jedis.close();
	}

	@Test
	public void listTest() {
		Jedis jedis = new Jedis("101.200.167.51", 6379);
		jedis.auth("testfan");
		jedis.lpush("uarticle:2", "1", "5");
		List<String> articles = jedis.lrange("uarticle:2", 0, -1);
		System.out.println(articles);
	}

	@Test
	public void setTest() {
		Jedis jedis = new Jedis("101.200.167.51", 6379);
		jedis.auth("testfan");
		jedis.sadd("unicknames", "testfan");
		jedis.sadd("unicknames", "testfan1");
		System.out.println(jedis.sismember("unicknames", "testfan"));
	}

	@Test
	public void hashTest() {
		Jedis jedis = new Jedis("101.200.167.51", 6379);
		jedis.auth("testfan");
		jedis.hset("u:2", "nickname", "xiaojiang");
		jedis.hset("u:2", "age", "25");
		System.out.println(jedis.hgetAll("u:2"));
	}

	@Test
	public void sortedSetTest() {
		Jedis jedis = new Jedis("101.200.167.51", 6379);
		jedis.auth("testfan");
		jedis.zadd("invest", 1000, "xiaojiang");
		jedis.zadd("invest", 2000, "yaoer");
		jedis.zadd("invest", 20000, "sicong");
		jedis.zadd("invest", 500, "devlin");
		System.out.println(jedis.zrevrange("invest", 0, -1));
		Set<Tuple> invests = jedis.zrevrangeWithScores("invest", 0, -1);
		for (Tuple t : invests) {
			System.out.println(t.getElement() + "\t" + t.getScore());
		}
	}
}
