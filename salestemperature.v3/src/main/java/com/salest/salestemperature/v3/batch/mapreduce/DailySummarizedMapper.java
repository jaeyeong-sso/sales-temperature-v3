package com.salest.salestemperature.v3.batch.mapreduce;

import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.io.Text;
import java.io.IOException;

// 2015-12-31-24,22:15:38,103,1,4500

public class DailySummarizedMapper extends Mapper<Object, Text, TextPair, LongPair> {
	
	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		
		String[] columns = value.toString().split(",");
		String date = columns[0].substring(0, columns[0].lastIndexOf("-"));
		
		String productCode = columns[2];
		long numOfProduct = Long.parseLong(columns[3]);
		long amount = Long.parseLong(columns[4]);
		
		// NumOfProduct,Amount Per Date,Product 
		context.write(new TextPair(date,productCode), new LongPair(numOfProduct, amount));
	}	
}
