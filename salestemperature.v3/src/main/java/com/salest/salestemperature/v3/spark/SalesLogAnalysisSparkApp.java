package com.salest.salestemperature.v3.spark;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;

import kafka.serializer.StringDecoder;
import scala.Tuple2;

public class SalesLogAnalysisSparkApp {

	final static String TOPIC = "tr-events";
	final static String MESSAGE_BROKER = "salest-master-server:9092";
	final static String ZOOKEEPER_ENSEMBLE = "salest-master-server:2181";
	
    final String regEx= "[^0-9a-zA-Z-:,]+";
    final Pattern pattern = Pattern.compile("[0-9,:-]+");
    
    private static SparkConf sparkConf;
    
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		// Windows Env workaround
		System.setProperty("hadoop.home.dir", "c:\\\\winutil\\\\" );
		//
		
		sparkConf = new SparkConf().setAppName("SalesLogReceiver").setMaster("local[2]");	//.setMaster("spark://192.168.118.132:7077");
		sparkConf.set("spark.driver.allowMultipleContexts" , "true");
		
		JavaSparkContext jctx = prepareContext(sparkConf);
		
		JavaPairRDD<String,String> menuInfoRDD = loadMenuInfoRDD(jctx);
		//menuInfoRDD.persist(StorageLevel.MEMORY_AND_DISK());
		
		JavaStreamingContext jsctx = prepareStreamingContext(sparkConf, 10);
		JavaPairDStream<String, String> kafkaInputDStream = createKafkaDirectStream(jsctx);
		
		extractVaildSalesLogMessageDStream(kafkaInputDStream, menuInfoRDD);
		
		jsctx.start();
		jsctx.awaitTermination();
		
		//menuInfoRDD.unpersist();
	}
	
	// Spark
	private static JavaSparkContext prepareContext(SparkConf sparkConf){
		return new JavaSparkContext(sparkConf);
	}
	
	private static JavaPairRDD<String,String> loadMenuInfoRDD(JavaSparkContext jctx){
		
		JavaRDD<String> inputRDD = jctx.textFile("hdfs://namenode:9000/salest/menu_data/menu_code_out.csv");
		
		JavaPairRDD<String,String> productCodeCategoryPairRDD = inputRDD.mapToPair(new PairFunction<String,String,String>(){
			public Tuple2<String, String> call(String line) throws Exception {
				// TODO Auto-generated method stub
				String[] fields = line.split(",");
				return new Tuple2<String, String>(fields[1], fields[0]);
			}
		});
		
		List<Tuple2<String,String>> inputLines = productCodeCategoryPairRDD.collect();
		for(Tuple2<String,String> line : inputLines){
			System.out.println(line._1 + " / " + line._2);
		}

		return productCodeCategoryPairRDD;
	}
	
	
	// Spark Streaming 
	
	private static JavaStreamingContext prepareStreamingContext(SparkConf sparkConf, long batchInterval){
	    return new JavaStreamingContext(sparkConf, Durations.seconds(batchInterval));
	}
	
	private static JavaPairDStream<String, String> createKafkaDirectStream(JavaStreamingContext jsctx){
		
	    Set<String> topicsSet = new HashSet<String>(Arrays.asList(TOPIC));
	    Map<String, String> kafkaParams = new HashMap<String, String>();
	    kafkaParams.put("metadata.broker.list", MESSAGE_BROKER);
	    
	    return KafkaUtils.createDirectStream(
	    	jsctx,
	        String.class,
	        String.class,
	        StringDecoder.class,
	        StringDecoder.class,
	        kafkaParams,
	        topicsSet
	    );
	}
	
	private static void testPrintKafkaOriginalMessage(JavaPairDStream<String, String> kafkaInputDStream){
		
		kafkaInputDStream.foreachRDD(new Function<JavaPairRDD<String,String>, Void>(){
	    	public Void call(JavaPairRDD<String,String> rdd) throws Exception {
	    		//System.out.println( "[rdd.count()]: " + rdd.count() );
	    		//System.out.println( "[rdd.distinct().count()]: " + rdd.distinct().count());

	    		List<Tuple2<String,String>> tuples = rdd.collect();
	    		for(Tuple2<String,String> tuple : tuples){
	    			System.out.println(tuple._1 + " " + tuple._2);
	    		}
	    		
	    		/*
	    		List<String> rddList = rdd.distinct().collect();
	    		for(String item : rddList){
	    			System.out.println(item);
	    		}
	    		*/

				return null;
	    	}
	    });
	}
	
	private static void extractVaildSalesLogMessageDStream (
			JavaPairDStream<String, String> pairDStream,
			final JavaPairRDD<String,String> menuInfoRDD ){
 
	    JavaPairDStream<String,String> messagesDStream = pairDStream.mapToPair(new PairFunction<Tuple2<String, String>, String, String>() {
	        public Tuple2<String, String> call(Tuple2<String, String> tuple) {
	        	String[] columes = tuple._2.split("[^0-9a-zA-Z-:,]+");
	        	//System.out.println(tuple._1 + " / " + tuple._2 + "   [message] :" + tuple._2);
				return new Tuple2<String, String>(columes[columes.length-1], "1");
	        }
	    }).reduceByKey(new Function2<String, String, String>() {
	    	public String call(String cnt_1, String cnt_2) {
	    		return "duplicated";
	    	}
	    });

	    //messagesDStream.print();
	    
	    messagesDStream.foreachRDD(new Function<JavaPairRDD<String,String>, Void>(){
	    	public Void call(JavaPairRDD<String,String> rdd) throws Exception {
	    		
	    		JavaPairRDD<String,Tuple2<String,String>> rddJoined = rdd.join(menuInfoRDD);

	    		List<Tuple2<String,Tuple2<String,String>>> tuples = rddJoined.collect();
	    		
	    		for( Tuple2<String,Tuple2<String,String>> tuple : tuples){
	    			System.out.println(tuple._1 + "   " + tuple._2._1 + " / " + tuple._2._2);
	    		}
	    		
				return null;
	    	}
	    });
	    
	    /*
	    messagesDStream.foreachRDD(new Function<JavaRDD<String>, Void>(){
	    	public Void call(JavaRDD<String> rdd) throws Exception {
	    		System.out.println( "[rdd.count()]: " + rdd.count() );
	    		System.out.println( "[rdd.distinct().count()]: " + rdd.distinct().count());

	    		rdd.uni
	    		List<String> rddList = rdd.distinct().collect();
	    		for(String item : rddList){
	    			System.out.println(item);
	    		}

				return null;
	    	}
	    });
	    */
	}
}
