package com.salest.salestemperature.v3.service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class RedisCacheService {
	
	public static String TOTAL_SALES_COUNTER_OF_DAY = "total_sales_counter_of_day:2016-06-20";
	
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
	
	public void createOrUpdateValueByKey(String key, String value){
		//Jedis jedis = this.jedisPool.getResource(); 
		try { 
			jedis.setex(key, 60, value);
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
				jedis.setex(key, 60, String.valueOf(1));
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
}
