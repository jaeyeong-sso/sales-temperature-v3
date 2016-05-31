package com.salest.salestemperature.v3.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
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
	
	public List<Category> listingAllProductCategory(){
		
		String queryStr = 
        "SELECT cate_name, GROUP_CONCAT(product_info) AS product_info from (" +
        "SELECT cate_name, CONCAT(cast(product_code as string),':',product_name,':',cast(price as string)) AS product_info from ext_menumap_info WHERE price!=0" +
        ") view_product_categories GROUP BY cate_name";
        
		return this.jdbcTemplate.query(queryStr, new RowMapper<Category>(){
			
			public Category mapRow(ResultSet rs,int rowNumber) throws SQLException{
				
				String cateName = rs.getString("cate_name");				
				String[] strProducts= rs.getString("product_info").split(",");
				
				Map<String,Product> productsMap = new HashMap<String,Product>();

				for(int i=0; i<strProducts.length; i++){
					String[] strProductField = strProducts[i].split(":");
					
					productsMap.put(strProductField[1], 
							new Product.ProductBuilder()
								.withId(strProductField[0])
								.withName(strProductField[1])
								.withPrice(Integer.parseInt(strProductField[2])).build());		
				}
				
				return new Category.CategoryBuilder().withName(cateName).withProductsMap(productsMap).build();
			}
		});
	}
	
	public Category listingMajorProductsOfCategory(String categoryName){
				
		String queryStr = 
			    "SELECT product_code, product_name, price, SUM(sales_amount) AS total_amount, SUM(num_of_product) AS num_of_product " +
			    	    "FROM (" +
			    	        "SELECT ext_tr_receipt.product_code,product_name,cate_name, sales_amount, num_of_product, price FROM ext_tr_receipt JOIN ext_menumap_info USING (product_code)" +
			    	    ") view_ext_tr_reciept_with_cate_name WHERE sales_amount != 0 AND cate_name = '" + categoryName + "' " +
			    "GROUP BY product_code, product_name, price " +
			    "ORDER BY (SUM(sales_amount)) DESC LIMIT 10";
	
		
		List<Product> productsList = this.jdbcTemplate.query(queryStr, new RowMapper<Product>(){
			public Product mapRow(ResultSet rs,int rowNumber) throws SQLException{
				return new Product.ProductBuilder()
					.withId(rs.getString("product_code"))
					.withName(rs.getString("product_name"))
					.withPrice(rs.getInt("price")).build();
			}
		});
		
		Map<String,Product> productsMap = new HashMap<String,Product>();
		for(Product product : productsList){
			productsMap.put(product.getName(), product);
		}
		
		return new Category.CategoryBuilder().withName(categoryName).withProductsMap(productsMap).build();
	}
}
