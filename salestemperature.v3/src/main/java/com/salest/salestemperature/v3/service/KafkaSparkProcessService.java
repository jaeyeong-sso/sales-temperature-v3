package com.salest.salestemperature.v3.service;

import java.util.HashMap;
import java.util.HashSet;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import scala.Tuple2;

import kafka.serializer.StringDecoder;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.function.*;
import org.apache.spark.streaming.api.java.*;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.Durations;

public class KafkaSparkProcessService {
	
	final static String TOPIC = "tr-events";
	final static String MESSAGE_BROKER = "salest-master-server:9092";
	final static String ZOOKEEPER_ENSEMBLE = "salest-master-server:2181";

	private ExecutorService executor;
	
	@PostConstruct
	public void PostBeanCreated(){
		this.executor = Executors.newFixedThreadPool(1);
	    executor.execute(new ProcesssStreamRunner());
	}
	
	@PreDestroy
	public void PreBeanDestroyed(){
		if(this.executor != null) {
			executor.shutdown();
		}
	}
	
	
	public class ProcesssStreamRunner implements Runnable {
 
		public void run() {
			// TODO Auto-generated method stub
			startProcessMessageStreams();
		}
	}
	
	public void startProcessMessageStreams(){
	
	    final String regEx= "[^0-9a-zA-Z-:,]+";
	    
	    Set<String> topicsSet = new HashSet<String>(Arrays.asList(TOPIC));
	    Map<String, String> kafkaParams = new HashMap<String, String>();
	    kafkaParams.put("metadata.broker.list", MESSAGE_BROKER);
	    
	    SparkConf sparkConf = new SparkConf().setAppName("SalesLogReceiver").setMaster("local[2]");	//.setMaster("spark://salest-master-server:7077");
	    JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, Durations.seconds(5));

	    JavaPairInputDStream<String, String> messagesDStream = KafkaUtils.createDirectStream(
	        jssc,
	        String.class,
	        String.class,
	        StringDecoder.class,
	        StringDecoder.class,
	        kafkaParams,
	        topicsSet
	    ); 
	    
	    //messagesDStream.print();

	    messagesDStream.foreachRDD(new Function<JavaPairRDD<String, String>, Void>() {
	    	public Void call(JavaPairRDD<String, String> rdd) throws IOException {
	    		
	    	    String lastMessageUuid = null;
	    	    String curMessageUuid, salesLogMessage;
	    	    
	    		List<Tuple2<String,String>> rddList = rdd.collect();
	    		for(Tuple2<String,String> rddItem : rddList){
	    			
	    			String[] messageTokens = rddItem._2.split(regEx);
	    			curMessageUuid = messageTokens[messageTokens.length-2];
	    			salesLogMessage = messageTokens[messageTokens.length-1];
	    			
	    			if(!curMessageUuid.equals(lastMessageUuid)){
	    				System.out.println(curMessageUuid + " / "+ salesLogMessage);
	    			}
	    			lastMessageUuid = curMessageUuid;
	    		}
	    		return null;
	    	}
	    });
	    
	    jssc.start();
	    jssc.awaitTermination();
	} 
}
