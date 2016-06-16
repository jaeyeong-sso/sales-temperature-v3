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
	    private KafkaStream m_stream;
	    private int m_threadNumber;
	 
	    public ConsumerRunner(KafkaStream a_stream, int a_threadNumber) {
	        m_threadNumber = a_threadNumber;
	        m_stream = a_stream;
	    }
	 
	    public void run() {
	        ConsumerIterator<byte[], byte[]> it = m_stream.iterator();
	        while (it.hasNext()){
	            System.out.println("Thread " + m_threadNumber + ": " + new String(it.next().message()));
	        }
	        System.out.println("Shutting down Thread: " + m_threadNumber);
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
	    executor.execute(new ConsumerRunner(stream, 1));
	    
	}
}
