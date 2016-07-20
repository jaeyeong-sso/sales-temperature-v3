package com.salest.salestemperature.v3.service;

import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class RedisCacheService {
	
	public static String TOTAL_SALES_AMOUNT_OF_DAY  = "saleslog_totalamount_of:";
	public static String TOTAL_SALES_COUNTER_OF_DAY = "total_sales_counter_of_day:";
	public static String CATEGORIES_INFO  = "categories_info";
	public static String MAJOR_PRODUCTS_INFO_OF  = "major_products_info_of:";
	public static String PASTDAY_TIMEBASE_SALESAMOUNT_OF = "pastday_timebase_salesamount_of:";
	
	private static int KEY_EXPIRED_PERIOD = 60*60*24;
	
	private JedisPool jedisPool;
	private Jedis jedis;
	
	@PostConstruct
	public void PostBeanCreated(){
		this.jedisPool = new JedisPool(new JedisPoolConfig(), "salest-master-server", 6300); 
		this.jedis = this.jedisPool.getResource(); 
	}
	
	@PreDestroy
	public void PreBeanDestroyed(){
		if(this.jedisPool != null) {
			this.jedisPool.close();
			this.jedisPool.destroy();
		}
	}
	
	public String readValueByKey(String key){
		
		String readKey = null;
		
		//Jedis jedis = this.jedisPool.getResource(); 
		try { 
			readKey = jedis.get(key); 
			
		} catch(JedisConnectionException e){ 
			if(null != jedis){ 
				readKey = null;
			} 
		} finally { 
			//jedisPool.close();
		} 
		
		return readKey;
	}
	
	public Set<String> readKeys(String pattern){
		
		Set<String> keysSet = null;
		
		//Jedis jedis = this.jedisPool.getResource(); 
		try { 
			keysSet = jedis.keys(pattern);
			
		} catch(JedisConnectionException e){ 
			if(null != jedis){ 
				keysSet = null;
			} 
		} finally { 
			//jedisPool.close();
		} 
		
		return keysSet;
	}
	
	public void createOrUpdateValueByKey(String key, String value){
		//Jedis jedis = this.jedisPool.getResource(); 
		try { 
			jedis.setex(key, KEY_EXPIRED_PERIOD, value);
		} catch(JedisConnectionException e){ 
			
		} finally { 
			//jedisPool.close();
		} 
		
	}
	
	public long createOrIncrCounterValue(String key, long offset){
		
		long incrResultCounterValue = 0L;
		
		//Jedis jedis = this.jedisPool.getResource(); 
		
		try { 
			if(jedis.exists(key)){
				jedis.incrBy(key, offset);
			} else {
				jedis.setex(key, KEY_EXPIRED_PERIOD, String.valueOf(1));
			}
			
			incrResultCounterValue = Long.parseLong(jedis.get(key)); 
			
		} catch(JedisConnectionException e){ 
			System.out.println(e.getMessage());
		} finally { 
			//jedisPool.close();
		} 
		
		return incrResultCounterValue;
	}
	
	public void deleteKey(String key){
		
		//Jedis jedis = this.jedisPool.getResource(); 
		try { 
			if(jedis.exists(key)){
				jedis.del(key);
			}
			
		} catch(JedisConnectionException e){ 
			
		} finally { 
			//jedisPool.close();
		} 
	}
	
	public void addToList(String key, String listItem){
		jedis.rpush(key, listItem);
		jedis.expire(key,  60*60*24);
	}
	
	public List<String> getFromList(String key){
		return jedis.lrange(key, 0, -1);
	}
}
