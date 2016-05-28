package com.salest.salestemperature.v3.dao;

import java.sql.Connection;
import java.sql.SQLException;

public class DataSource {

	private String connectionUrl;
	private String jdbcDriverName;
	
	private com.cloudera.impala.jdbc41.DataSource dataSource;
	
	
	public void setConnectionUrl(String connectionUrl){
		this.connectionUrl = connectionUrl;
	}
	
	public void setJdbcDriverName(String jdbcDriverName){
		this.jdbcDriverName = jdbcDriverName;
	}
	
	public com.cloudera.impala.jdbc41.DataSource getDataSource(){
		try {
			Class.forName(jdbcDriverName);
			this.dataSource = new com.cloudera.impala.jdbc41.DataSource();
			dataSource.setURL(connectionUrl);
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			dataSource = null;
		}
		return dataSource;
	}
	
	public Connection getConnection(){
		
		try {
			Class.forName(jdbcDriverName);
			this.dataSource = new com.cloudera.impala.jdbc41.DataSource();
			dataSource.setURL(connectionUrl);
			return dataSource.getConnection();
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
