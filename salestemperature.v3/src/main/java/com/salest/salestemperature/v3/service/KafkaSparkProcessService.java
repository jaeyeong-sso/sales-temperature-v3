package com.salest.salestemperature.v3.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import scala.Tuple2;

import kafka.serializer.StringDecoder;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.*;
import org.apache.spark.streaming.api.java.*;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.Durations;

public class KafkaSparkProcessService {
	
	final static String TOPIC = "tr-events";
	final static String MESSAGE_BROKER = "salest-master-server:9092";
	final static String ZOOKEEPER_ENSEMBLE = "salest-master-server:2181";
	
	public void processMessage(){
		/*		
		SparkConf sparkConf = new SparkConf().setAppName("SalesLogReceiver").setMaster("yarn");
		
		JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, Durations.seconds(5));
		
		final Pattern SPACE = Pattern.compile(" ");

	    Set<String> topicsSet = new HashSet<String>(Arrays.asList(TOPIC));
	    Map<String, String> kafkaParams = new HashMap<String, String>();
	    kafkaParams.put("metadata.broker.list", MESSAGE_BROKER);
	    
	    JavaPairInputDStream<String, String> messages = KafkaUtils.createDirectStream(
	        jssc,
	        String.class,
	        String.class,
	        StringDecoder.class,
	        StringDecoder.class,
	        kafkaParams,
	        topicsSet
	    );
		 */    
	} 
}
