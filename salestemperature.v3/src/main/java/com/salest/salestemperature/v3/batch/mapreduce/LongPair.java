package com.salest.salestemperature.v3.batch.mapreduce;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class LongPair implements Writable{

	private long first;
    private long second;
    
    public LongPair(){}
    
    public LongPair(long first, long second){
    	set(first,second);
    }
    
    public void set(long first, long second){
    	this.first = first;
    	this.second = second;
    }
    
    public long getFirst(){
    	return this.first;
    }
    
    public long getSecond(){
    	return this.second;
    }
    
	public void readFields(DataInput in) throws IOException {
		// TODO Auto-generated method stub
		first = in.readLong();
        second = in.readLong();
	}

	public void write(DataOutput out) throws IOException {
		// TODO Auto-generated method stub
		out.writeLong(first);
        out.writeLong(second);
	}
	
	@Override
    public String toString() {
        return this.first + "\t" + this.second;
    }
}
