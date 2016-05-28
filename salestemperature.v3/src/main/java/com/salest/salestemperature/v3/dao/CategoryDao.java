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
}
