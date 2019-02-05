/**
 * 
 */
package com.redis.sample;

import java.net.URI;
import java.net.URISyntaxException;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author ankit.patwa
 *
 */
public class RedisConnection {
	
	
	
	public static JedisPool getJedisPool() {
		
	  JedisPoolConfig poolConfig = new JedisPoolConfig();
	  poolConfig.setMaxTotal(128);	
	  poolConfig.setMaxIdle(5);
	  JedisPool jedisPool=new JedisPool(
			  poolConfig,"localhost",6379,30000,"redis123",2);
	  return jedisPool;
	}
	
	
	public Jedis getConnectionWithURI() throws URISyntaxException {		
		return new Jedis(new URI("redis://:r edis123@localhost:6379")); 
	}		
	
	public static Jedis getConnectionWithoutPool() {				
		Jedis jedis=new Jedis("localhost",6379);
		jedis.auth("redis123");
		return jedis;		
	}	

}