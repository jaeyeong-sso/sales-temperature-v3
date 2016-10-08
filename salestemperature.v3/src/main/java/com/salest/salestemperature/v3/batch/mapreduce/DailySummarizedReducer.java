package com.salest.salestemperature.v3.batch.mapreduce;

import java.io.IOException;

import org.apache.hadoop.mapreduce.Reducer;

public class DailySummarizedReducer extends Reducer<TextPair,LongPair,TextPair,LongPair>  {
	
	private LongPair result = new LongPair();
	
	public void reduce(TextPair keyPair, Iterable<LongPair> values, Context context)
			throws IOException, InterruptedException {
		
		long transactionCnt = 0;
		long sumOfAmount = 0;
		
		for (LongPair val : values) {
			transactionCnt += val.getFirst();
			sumOfAmount += val.getSecond();
		}
		result.set(transactionCnt, sumOfAmount);
		context.write(keyPair, result);
	}
}
