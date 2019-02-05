/**
 * 
 */
package com.intellect.redis.sample;

import java.util.Map;

/**
 * @author ankit.patwa 
 */
public class RedisUtil {
	
    /**
     * @param key
     * @param array
     */
    public static void addToSet(String key,String ...array){    	
    	RedisConnection.getConnectionWithoutPool().sadd(key, array);
    	
    }
    
    /** 
     * @param key
     * @param array
     */
    public static void addToList(String key,String ...array){
    	RedisConnection.getConnectionWithoutPool().lpush(key, array);
    }
    
    /**     
     * @param key
     * @param array
     */
    public static void addToHash(String key,Map<String,String> map) {
    	RedisConnection.getConnectionWithoutPool().hmset(key,map);    		
    }
    
    /**
     * @param key
     * @param value
     * @param score
     */
    public static void addToSoretedSet(String key,String value,Double score){
    	RedisConnection.getConnectionWithoutPool().zadd(key,score,value);
    }
      
    public Map<String, String> getFromHash(String key) {
    	return RedisConnection.getConnectionWithoutPool().hgetAll(key);
    }

}
