package com.salest.salestemperature.v3.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.salest.salestemperature.v3.model.Category;
import com.salest.salestemperature.v3.model.Product;
import com.salest.salestemperature.v3.model.SalesVolume;

public class CategoryDao {
	
	private JdbcTemplate jdbcTemplate;
	
	public void setDataSource(DataSource dataSource){

		this.jdbcTemplate = new JdbcTemplate(dataSource.getDataSource());
	}
	
	public List<Category> listingProductCategory(){
		
		String queryStr = 
        "SELECT cate_name, GROUP_CONCAT(product_info) from (" +
        "SELECT cate_name, CONCAT(product_name,':',cast(product_code as string),':',cast(price as string)) AS product_info from ext_menumap_info WHERE price!=0" +
        ") view_product_categories GROUP BY cate_name";
        
		return this.jdbcTemplate.query(queryStr, new RowMapper<Category>(){
			
			public Category mapRow(ResultSet rs,int rowNumber) throws SQLException{
				
				String cateName = rs.getString(1);
				String[] strProducts= rs.getString(2).split(",");
				
				List<Product> products = new ArrayList<Product>();

				for(int i=0; i<strProducts.length; i++){
					String[] strProductField = strProducts[i].split(":");
					products.add(new Product(strProductField[0],strProductField[1],Integer.parseInt(strProductField[2])));				
				}
				
				return new Category.CategoryBuilder().withName(cateName).withProducts(products).build();
			}
		});
	}
	
	
	public Map<String, Object> listingProductsSalesVolume(String queryYear, String categoryName){
		
		String queryStr =  
		        "SELECT tr_date,cate_name,product_code,product_name, SUM(num_of_product) AS num_of_product, SUM(sales_amount) AS total_amount FROM (" +
		            "SELECT SUBSTR(date_receipt_num,1,7) AS tr_date,cate_name, product_name, product_code,sales_amount,num_of_product FROM (" +
		                "SELECT ext_tr_receipt.date_receipt_num, ext_tr_receipt.product_code, product_name,cate_name, sales_amount, num_of_product " +
		                "FROM ext_tr_receipt JOIN ext_menumap_info USING (product_code)" +
		            ") view_ext_tr_reciept_with_cate_name " +
		            "WHERE SUBSTR(date_receipt_num,1,4)='" + queryYear + "' AND sales_amount != 0" + " AND cate_name='" + categoryName + "'" +
		        ") view_monthly_cate_product_sales_vol " +
		        "GROUP BY tr_date,cate_name,product_code,product_name " + 
		        "ORDER BY tr_date,cate_name,product_code,product_name ASC";
						    
				
		Map<String, Object> productsSalseVolume = this.jdbcTemplate.queryForMap(queryStr);
		
		return productsSalseVolume;
	}
}
