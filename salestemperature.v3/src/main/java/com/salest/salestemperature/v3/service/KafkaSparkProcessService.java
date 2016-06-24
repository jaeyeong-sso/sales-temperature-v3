package com.salest.salestemperature.v3.service;

import java.util.HashMap;
import java.util.HashSet;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import scala.Tuple2;

import kafka.serializer.StringDecoder;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.*;
import org.apache.spark.streaming.api.java.*;
import org.apache.spark.streaming.dstream.DStream;
import org.apache.spark.streaming.kafka.KafkaUtils;

import com.salest.salestemperature.v3.service.model.SalesLogRecord;

import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.Durations;

public class KafkaSparkProcessService {
	
	final static String TOPIC = "tr-events";
	final static String MESSAGE_BROKER = "salest-master-server:9092";
	final static String ZOOKEEPER_ENSEMBLE = "salest-master-server:2181";

	final static String HDFS_CHECKPOINT_DIR = "hdfs://namenode:9000/salest/spark_checkpoint";
	final static String LOCAL_CHECKPOINT_DIR = "/salest/spark_checkpoint";
	
	private SparkConf sparkConf;
	private ExecutorService executor;
	
	private JavaStreamingContextFactory ctxFactory;
    		
	@PostConstruct
	public void PostBeanCreated(){ 
		
		/*
	    Set<String> topicsSet = new HashSet<String>(Arrays.asList(TOPIC));
	    Map<String, String> kafkaParams = new HashMap<String, String>();
	    
		this.sparkConf = new SparkConf().setAppName("SalesLogReceiver").setMaster("local[2]");	//.setMaster("spark://salest-master-server:7077");
	    this.jssc = new JavaStreamingContext(sparkConf, Durations.seconds(5));
	    this.readMessagesDStream = KafkaUtils.createDirectStream(
		        jssc,
		        String.class,
		        String.class,
		        StringDecoder.class,
		        StringDecoder.class,
		        kafkaParams,
		        topicsSet
	    );
	    */
		/*
	    this.ctxFactory = new JavaStreamingContextFactory(){
			public JavaStreamingContext create() {
				JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, Durations.seconds(10));
				//jssc.checkpoint(LOCAL_CHECKPOINT_DIR);
				return jssc;
			}
	    };
		*/
		this.executor = Executors.newFixedThreadPool(1);
	    executor.execute(new ProcesssStreamRunner());
	}
	
	@PreDestroy
	public void PreBeanDestroyed(){
		if(this.executor != null) {
			executor.shutdown();
		}
	}

	
	private class ProcesssStreamRunner implements Runnable {
		public void run() {
			startProcessMessageStreams();
		}
	}
	
	public void startProcessMessageStreams(){
	
	    final String regEx= "[^0-9a-zA-Z-:,]+";
	    final String regEx2= "^([0-9]{4}-[0-9]{2}-[0-9]{2})$";
	    
	    final String regExSalesLogOnly = "[0-9,:-]+";
	    
	    final Pattern pattern = Pattern.compile(regExSalesLogOnly);
		
	    Set<String> topicsSet = new HashSet<String>(Arrays.asList(TOPIC));
	    Map<String, String> kafkaParams = new HashMap<String, String>();
	    kafkaParams.put("metadata.broker.list", MESSAGE_BROKER);
	    		
	    SparkConf sparkConf = new SparkConf().setAppName("SalesLogReceiver").setMaster("local[2]");
		
	    //JavaStreamingContext jssc = JavaStreamingContext.getOrCreate(LOCAL_CHECKPOINT_DIR, ctxFactory);
	    
	    JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, Durations.seconds(10));
	    JavaPairDStream<String, String> readMessagesDStream = KafkaUtils.createDirectStream(
	        jssc,
	        String.class,
	        String.class,
	        StringDecoder.class,
	        StringDecoder.class,
	        kafkaParams,
	        topicsSet
	    );

	    readMessagesDStream.foreachRDD(new Function<JavaPairRDD<String, String>,Void>(){
	    	public Void call(JavaPairRDD<String,String> rdd) throws Exception {
	    		List<Tuple2<String,String>> tuples = rdd.collect();
	    		for(Tuple2<String,String> tuple : tuples){
	    			System.out.println(tuple._1 + " " + tuple._2);
	    		}
				return null;
	    	}
	    });
	    
	    /*
	    JavaDStream<String> messagesDStream = readMessagesDStream.map(new Function<Tuple2<String, String>, String>() {
	        public String call(Tuple2<String, String> tuple2) {
	        	//String[] columes = tuple2._2.split(regEx);
				//return columes[columes.length-1];
	        	return tuple2._2;
	        }
	      });
	    */
	    
	    
	    readMessagesDStream.print();
	    
/*	    
	    readMessagesDStream.foreachRDD(new Function<JavaPairRDD<String,String>, Void>(){
			public Void call(JavaPairRDD<String,String> pairRDD) throws Exception {
				// TODO Auto-generated method stub
		
				JavaRDD<String> messages = pairRDD.values();
			
				System.out.println(messages.count());

				JavaRDD<String> messageBody = messages.mapPartitions(new FlatMapFunction<Iterator<String>,String>(){
					public Iterable<String> call(Iterator<String> messages) throws Exception {
						// TODO Auto-generated method stub
						List<String> messagesBody = new ArrayList<String>();
						while(messages.hasNext()){
							String[] columes = messages.next().split(regEx);
							messagesBody.add(columes[columes.length-1]);
						}
						return messagesBody;
					}
				});

				System.out.println(messageBody.count());

				JavaRDD<String> saleslog = messages.map(new Function<String,String>(){
					public String call(String x) {
						// TODO Auto-generated method stub
		    			//String[] columes = messages.split(regEx);
						return x;	//columes[columes.length-1];
					}
				});
				
			
				return null;
			}
	    });
*/
		
	    /*
	    JavaDStream<String> messageColumes = messagesDStream.flatMap(new FlatMapFunction<String, String>() {
	    	public Iterable<String> call(String message) {
	        	
    			String[] columes = message.split(regEx);
    			String curMessageUuid = columes[columes.length-2];
    			String salesLogMessage = columes[columes.length-1];
    			
    			Matcher matcher = pattern.matcher(salesLogMessage);
    			
    			SalesLogRecord.parseProductCodeFromRecord(matcher.group());
    			
    			return Arrays.asList(SalesLogRecord.parseProductCodeFromRecord(matcher.group()));

	        }
	    });
	    
	    */
	    
	    /*
	    messageColumes.foreachRDD(new Function<JavaRDD<String>, Void>(){
			public Void call(JavaRDD<String> salesLogRecordRdd) throws Exception {
				// TODO Auto-generated method stub
				List<String> salesLogRecords = salesLogRecordRdd.collect();
				for(String item : salesLogRecords){
					System.out.println("[messageColumes]: " + item );
				}
				return null;
			}
	    	
	    });
	    */

	    /*
	    JavaDStream<SalesLogRecord> messageColumes = messagesDStream.flatMap(new FlatMapFunction<String, SalesLogRecord>() {
	    	public Iterable<SalesLogRecord> call(String message) {
	        	
    			String[] columes = message.split(regEx);
    			String curMessageUuid = columes[columes.length-2];
    			String salesLogMessage = columes[columes.length-1];
    			
    			Matcher matcher = pattern.matcher(salesLogMessage);
    			
    			return Arrays.asList(new SalesLogRecord(matcher.group()));
	        }
	    });
	    
	    
	    messageColumes.foreachRDD(new Function<JavaRDD<SalesLogRecord>, Void>(){
			public Void call(JavaRDD<SalesLogRecord> salesLogRecordRdd) throws Exception {
				// TODO Auto-generated method stub
				List<SalesLogRecord> salesLogRecords = salesLogRecordRdd.collect();
				for(SalesLogRecord item : salesLogRecords){
					System.out.println("[messageColumes]: " + item.getTrDate() + " / " + item.getTrProductCode() + " / " + item.getTrSalesAmount());
				}
				return null;
			}
	    	
	    });
	    */
	    
	    /*
	    messagesDStream.foreachRDD(new Function<JavaRDD<String>, Void>() {
	    	
			public Void call(JavaRDD<String> rdd) throws Exception {
				// TODO Auto-generated method stub
				
	    	    String lastMessageUuid = null;
	    	    String curMessageUuid, salesLogMessage;
	    	    
				String[] messageTokens = rdd.split(regEx);
    			curMessageUuid = messageTokens[messageTokens.length-2];
    			salesLogMessage = messageTokens[messageTokens.length-1];
    			
    			
				return null;
			}
			
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
	    				System.out.println("[rddItem._2] : " + rddItem._2);
	    				
	    				Pattern pattern = Pattern.compile(regExSalesLogOnly);
	    				Matcher matcher = pattern.matcher(salesLogMessage);
	    				
	    				if(matcher.find()){
	    					//System.out.println( matcher.group() );
	    					
	    					
	    					JavaDStream<SalesLogRecord> salesLogRecordStream = rddItem.map();
	    				}
	    			}
	    			lastMessageUuid = curMessageUuid;
	    		}
	    		return null;
	    	}
	    });
	    */
	    
	    jssc.start();
	    jssc.awaitTermination();
	} 
}
