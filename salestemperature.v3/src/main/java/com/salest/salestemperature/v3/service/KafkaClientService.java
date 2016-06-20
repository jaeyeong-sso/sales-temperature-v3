package com.salest.salestemperature.v3.service;

import java.util.HashMap;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.producer.KeyedMessage;
import kafka.javaapi.producer.Producer; 
import kafka.producer.ProducerConfig;


public class KafkaClientService {
	
	final static String TOPIC = "tr-events";
	final static String MESSAGE_BROKER = "salest-master-server:9092";
	final static String ZOOKEEPER_ENSEMBLE = "salest-master-server:2181";
	
	private ConsumerConnector consumerConnector;
	private  ExecutorService executor;
	
	@PostConstruct
	public void PostBeanCreated(){
		startProcessMessageWithKafkaConsumerClient();
	}
	
	@PreDestroy
	public void PreBeanDestroyed(){
		if(this.consumerConnector!=null){
			this.consumerConnector.shutdown();
		}
		if(this.executor != null) {
			executor.shutdown();
		}
	}
	
	
	public class ConsumerRunner implements Runnable {
	    
		private KafkaStream msgStream;
	 
	    private String lastMessageUuid;
	    private final String regEx= "[^0-9a-zA-Z-:,]+";
	    
	    private String curMessageUuid;
	    private String salesLogMessage;
	    
	    public ConsumerRunner(KafkaStream msgStream) {
	    	this.msgStream = msgStream;
	    }
	 
	    public void run() {
	        ConsumerIterator<byte[], byte[]> iter = this.msgStream.iterator();
	        while (iter.hasNext()){
	        	String message = new String(iter.next().message());
	        	System.out.println(message);
	        	
	        	/*
	            String[] regexMessageTokens = message.split(regEx);
	            
	            curMessageUuid = regexMessageTokens[regexMessageTokens.length-2];
	            salesLogMessage = regexMessageTokens[regexMessageTokens.length-1];
	            		
	            if(!curMessageUuid.equals(lastMessageUuid)){
	            	System.out.println("Received Message From Kafka : " + salesLogMessage);
	            }
	            
	            lastMessageUuid = curMessageUuid;
	            */
	        }
	    }
	}
	
	
	public void sendMessageWithKafkaProducerClient(){
		
		Properties props = new Properties();
        props.put("group.id", "flume");
        props.put("metadata.broker.list", MESSAGE_BROKER);
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("request.required.acks", "1");
        
        ProducerConfig producerConfig = new ProducerConfig(props);
        Producer<String, String> producer = new Producer<String, String>(producerConfig);
        
        SimpleDateFormat sdf = new SimpleDateFormat();
        KeyedMessage<String, String> message = new KeyedMessage<String, String>(TOPIC,"Test message from java program " + sdf.format(new Date()));
        
        System.out.println("sendMessageWithKafkaClient >> " + message.toString());
        
        producer.send(message);
        producer.close();
    }	

	public void startProcessMessageWithKafkaConsumerClient(){
		
        Properties properties = new Properties();
        properties.put("zookeeper.connect", ZOOKEEPER_ENSEMBLE);
        properties.put("partitioner.class","kafka.producer.DefaultPartitioner");
        properties.put("group.id","flume");
        ConsumerConfig consumerConfig = new ConsumerConfig(properties);
        this.consumerConnector = Consumer.createJavaConsumerConnector(consumerConfig);	    
        
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(TOPIC, new Integer(1));
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumerConnector.createMessageStreams(topicCountMap);
        
        System.out.println("startProcessMessageWithKafkaConsumerClient is OK!");
        
        KafkaStream<byte[], byte[]> stream =  consumerMap.get(TOPIC).get(0);
        
		this.executor = Executors.newFixedThreadPool(1);
	    executor.execute(new ConsumerRunner(stream));
	    
	}
}
