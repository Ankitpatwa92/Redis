package com.intellect.redis.sample;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * Hello world!
 *
 */
public class App {
	
	
    public static void main( String[] args )
    {    	   	    	
    	
    	performActionWithPipeLine();
    	performActionWithoutPipeline();
    }
    
	
    public static void performActionWithoutPipeline() {
    	
    	JedisPool jedisPool = RedisConnection.getJedisPool();
    	Jedis jedis = jedisPool.getResource();
    	    	
        try {
        
        	//Without Pipeline
            long start = System.currentTimeMillis();
            for (int i = 0; i < 10000; i++) {
              jedis.set("foo" + i, "value" + i);
            }
            System.out.println("Time consumed without pipeline: " + (System.currentTimeMillis() - start));
        } catch (JedisConnectionException jex) {
        	jedis.close();
        } finally {
        	jedis.close();
        }
    }
    
    /**
	 * Pipeline is a way to send multiple commands to the server in one go without
	 * waiting for the replies at all, and finally read the replies in a single
	 * step.The advantage of sending multiple requests in one go is that we can
	 * reduce the bad impact of network latency, this is the output of my test:
	 */
    public static void performActionWithPipeLine(){
    	
    	JedisPool jedisPool = RedisConnection.getJedisPool();
    	Jedis jedis = jedisPool.getResource();
    	
    	final Pipeline pipeline = jedis.pipelined();
        try {        
            //With Pipeline
        	long start = System.currentTimeMillis();
        	
            for (int i = 0; i < 10000; i++) {
              pipeline.set("bar" + i, "value" + i);
            }
            pipeline.sync();
            System.out.println("Time consumed with pipeline: " + (System.currentTimeMillis() - start));
        } catch (JedisConnectionException jex) {
        	jedis.close();  //closing resource
        } finally {
        	jedis.close();  //closing resource
        }    	
    }
    
}

