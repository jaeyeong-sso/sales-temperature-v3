package com.salest.salestemperature.v3.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.salest.salestemperature.v3.model.SalesVolume;

public class SalesVolumeDao {

	private JdbcTemplate jdbcTemplate;
	
	public void setDataSource(DataSource dataSource){
		this.jdbcTemplate = new JdbcTemplate(dataSource.getDataSource());
	}

	private RowMapper<SalesVolume> salesVolumeListMapper = new RowMapper<SalesVolume>(){
		public SalesVolume mapRow(ResultSet rs,int rowNumber) throws SQLException{
			return new SalesVolume.SalesVolumeBuilder()
					.withDate(rs.getString("tr_date"))
					.withTotalSalesCount(rs.getInt("num_of_product"))
					.withTotalSalesAmount(rs.getLong("total_amount"))
					.build();
		}
	};
	
	private RowMapper<SalesVolume> salesVolumeListMapperWithCategoryOptItemName = new RowMapper<SalesVolume>(){
		public SalesVolume mapRow(ResultSet rs,int rowNumber) throws SQLException{
			return new SalesVolume.SalesVolumeBuilder()
					.withDate(rs.getString("tr_date"))
					.withOptItemName(rs.getString("cate_name"))
					.withTotalSalesCount(rs.getInt("num_of_product"))
					.withTotalSalesAmount(rs.getLong("total_amount"))
					.build();
		}
	};
	
	private RowMapper<SalesVolume> salesVolumeListMapperWithProductOptItemName = new RowMapper<SalesVolume>(){
		public SalesVolume mapRow(ResultSet rs,int rowNumber) throws SQLException{
			return new SalesVolume.SalesVolumeBuilder()
					.withDate(rs.getString("tr_date"))
					.withOptItemName(rs.getString("product_name"))
					.withTotalSalesCount(rs.getInt("num_of_product"))
					.withTotalSalesAmount(rs.getLong("total_amount"))
					.build();
		}
	};
	
	public List<SalesVolume> listingMonthlySalesVolume(String queryYear){
		
		String queryStr = "SELECT tr_date, SUM(num_of_product) AS num_of_product, (SUM(sales_amount)/10000) AS total_amount " +
		        "FROM ( SELECT SUBSTR(date_receipt_num,1,7) AS tr_date, num_of_product, sales_amount FROM ext_tr_receipt WHERE SUBSTR(date_receipt_num,1,4) = '" + queryYear + "') view_tr_recipt " +
				"GROUP BY tr_date ORDER BY tr_date ASC";
		
		return this.jdbcTemplate.query(queryStr, this.salesVolumeListMapper);
	}
	
	public Map<String, Object> getAnnualSalesVolume(String queryYear){
		
		String queryStr = 
		"SELECT COUNT(tr_date) AS total_num_of_date, SUM(total_num_of_product) AS total_num_of_product, SUM(total_amount) AS total_amount  FROM (" +		
			"SELECT tr_date, SUM(num_of_product) AS total_num_of_product, SUM(sales_amount) AS total_amount " +
	        "FROM (" +
	            "SELECT SUBSTR(date_receipt_num,1,10) AS tr_date, num_of_product, sales_amount " +
	            "FROM ext_tr_receipt WHERE SUBSTR(date_receipt_num,1,4) = '" + queryYear + "') view_tr_receipt " +
	        "GROUP BY tr_date ORDER BY tr_date ASC ) view_tr_daily_sales_volume";
    
		Map<String, Object> sumAnnualSalseVolume = this.jdbcTemplate.queryForMap(queryStr);
		
		return sumAnnualSalseVolume;
	}
	
	
	public List<SalesVolume> listingCategoriesMonthlySalesVolume(String queryYear){
		
		String queryStr =     
		        "SELECT tr_date,cate_name, SUM(num_of_product) AS num_of_product, SUM(sales_amount) AS total_amount FROM (" +
		            "SELECT SUBSTR(date_receipt_num,1,7) AS tr_date,cate_name, product_name, product_code,sales_amount,num_of_product FROM (" +
		                "SELECT ext_tr_receipt.date_receipt_num, ext_tr_receipt.product_code, product_name,cate_name, sales_amount, num_of_product " +
		                "FROM ext_tr_receipt JOIN ext_menumap_info USING (product_code)" +
		            ") view_ext_tr_reciept_with_cate_name " +
		            "WHERE SUBSTR(date_receipt_num,1,4)='" + queryYear + "' AND sales_amount != 0" +
		        ") view_monthly_cate_product_sales_vol " +
		        "GROUP BY tr_date,cate_name " + 
		        "ORDER BY tr_date,cate_name ASC";
				
		return this.jdbcTemplate.query(queryStr, salesVolumeListMapperWithCategoryOptItemName);
	}
	
	public List<SalesVolume> listingProductsMonthlySalesVolume(String queryYear, String categoryName){
		
		String queryStr =  
		        "SELECT tr_date, product_name, SUM(num_of_product) AS num_of_product, SUM(sales_amount) AS total_amount FROM (" +
		            "SELECT SUBSTR(date_receipt_num,1,7) AS tr_date,cate_name, product_name, product_code,sales_amount,num_of_product FROM (" +
		                "SELECT ext_tr_receipt.date_receipt_num, ext_tr_receipt.product_code, product_name,cate_name, sales_amount, num_of_product " +
		                "FROM ext_tr_receipt JOIN ext_menumap_info USING (product_code)" +
		            ") view_ext_tr_reciept_with_cate_name " +
		            "WHERE SUBSTR(date_receipt_num,1,4)='" + queryYear + "' AND sales_amount != 0" + " AND cate_name='" + categoryName + "'" +
		        ") view_monthly_cate_product_sales_vol " +
		        "GROUP BY tr_date,cate_name,product_code,product_name " + 
		        "ORDER BY tr_date,cate_name,product_code,product_name ASC";
						    
				
		return this.jdbcTemplate.query(queryStr, salesVolumeListMapperWithProductOptItemName);
	}
}