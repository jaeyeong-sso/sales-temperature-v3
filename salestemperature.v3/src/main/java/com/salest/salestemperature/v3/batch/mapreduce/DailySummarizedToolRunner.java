package com.salest.salestemperature.v3.batch.mapreduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.hadoop.mapreduce.Job;

import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class DailySummarizedToolRunner extends Configured implements Tool {
	
    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new Configuration(), new DailySummarizedToolRunner(), args);
        System.exit(res);
    }
 
	public void setConf(Configuration conf) {
		// TODO Auto-generated method stub
		
	}

	public Configuration getConf() {
		// TODO Auto-generated method stub
		return null;
	}
	
    public int run(String[] args) throws Exception {
 
    	Configuration conf = getConf();
    	
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
    	
        Job job = Job.getInstance(conf, this.getClass().toString());
   
		job.setJarByClass(DailySummarizedToolRunner .class);
		job.setMapperClass(DailySummarizedMapper.class);
		job.setReducerClass(DailySummarizedReducer.class);
		job.setOutputKeyClass(TextPair.class);
		job.setOutputValueClass(LongPair.class);
		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
		
		return job.waitForCompletion(true) ? 0 : 1;
    }
}
