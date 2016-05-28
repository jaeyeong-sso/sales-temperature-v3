package com.salest.salestemperature.v3.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.salest.salestemperature.v3.model.ProductSalesVolume;
import com.salest.salestemperature.v3.model.SalesVolume;

public class ProductSalesVolumeDao {
	
	private JdbcTemplate jdbcTemplate;
	
	public void setDataSource(DataSource dataSource){
		this.jdbcTemplate = new JdbcTemplate(dataSource.getDataSource());
	}
	
	private RowMapper<ProductSalesVolume> productSalesVolumeListMapper = new RowMapper<ProductSalesVolume>(){
		public ProductSalesVolume mapRow(ResultSet rs,int rowNumber) throws SQLException{
			return new ProductSalesVolume.ProductSalesVolumeBuilder()
					.withProductId(rs.getString("product_code"))
					.withCateName(rs.getString("cate_name"))
					.withTotalSalesCount(rs.getInt("num_of_product"))
					.withTotalSalesAmount(rs.getLong("total_amount"))
					.build();
		}
	};
	
	public List<ProductSalesVolume> listingProductsBySalesVolume(){
		
		String queryStr =
		"SELECT product_code, cate_name, SUM(sales_amount) AS total_amount, SUM(num_of_product) AS num_of_product " +
		"FROM (" +
			"SELECT ext_tr_receipt.product_code, cate_name, sales_amount, num_of_product FROM ext_tr_receipt JOIN ext_menumap_info USING (product_code)" +
		    ") view_ext_tr_reciept_with_cate_name WHERE sales_amount != 0 " +
		"GROUP BY product_code, cate_name " +
		"ORDER BY (SUM(sales_amount)) DESC";

		return this.jdbcTemplate.query(queryStr, productSalesVolumeListMapper);
	}
}
