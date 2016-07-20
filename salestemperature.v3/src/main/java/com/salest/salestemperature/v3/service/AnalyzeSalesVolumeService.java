package com.salest.salestemperature.v3.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.salest.salestemperature.v3.dao.CategoryDao;
import com.salest.salestemperature.v3.dao.SalesVolumeDao;
import com.salest.salestemperature.v3.model.Category;
import com.salest.salestemperature.v3.model.SalesVolume;
import com.salest.salestemperature.v3.web.request.model.AnnualSalesVolumeSummary;


public class AnalyzeSalesVolumeService {
	
	private CategoryDao categoryDao;
	private SalesVolumeDao salesVolumeDao;
	
	private RedisCacheService redisCacheService;
	
	private Map<String,String> weekOfDayMap;
	
	@PostConstruct
	public void PostBeanCreated(){
		weekOfDayMap = new HashMap<String,String>();
		weekOfDayMap.put("1","Sun");
		weekOfDayMap.put("2","Mon");
		weekOfDayMap.put("3","Tue");
		weekOfDayMap.put("4","Wed");
		weekOfDayMap.put("5","Thu");
		weekOfDayMap.put("6","Fri");
		weekOfDayMap.put("7","Sat");
	}
	
	public void setCategoryDao(CategoryDao categoryDao){
		this.categoryDao = categoryDao;
	}
	
	public void setSalesVolumeDao(SalesVolumeDao salesVolumeDao){
		this.salesVolumeDao = salesVolumeDao;
	}
	
	public void setRedisCacheService(RedisCacheService redisCacheService){
		this.redisCacheService = redisCacheService;
	}

	public Map<String,String> getWeekOfDayMap(){
		return weekOfDayMap;
	}
	
	public AnnualSalesVolumeSummary getAnnualSalesVolume(String queryYear){
		
		long totalNumOfDate = 0L;
		long totalNumOfProduct = 0L;
		long totalAmount = 0L;
		long avrgSalesCount = 0L;
		long avrgSalesAmount = 0L;
		
		Map<String,Object> mapAnnualSalesVolume = salesVolumeDao.getAnnualSalesVolume(queryYear);
		
		totalNumOfDate = (Long) mapAnnualSalesVolume.get("total_num_of_date");
		totalNumOfProduct = (Long) mapAnnualSalesVolume.get("total_num_of_product");
		totalAmount = (Long) mapAnnualSalesVolume.get("total_amount");
		
		if(totalNumOfDate!=0){
			avrgSalesCount = totalNumOfProduct/totalNumOfDate;
			avrgSalesAmount = totalAmount/totalNumOfDate;
		}
		
		AnnualSalesVolumeSummary annualSalesVolumeSummary = new AnnualSalesVolumeSummary();
		
		annualSalesVolumeSummary.setTotalSalesCount((int) totalNumOfProduct);
		annualSalesVolumeSummary.setTotalSalesAmount(totalAmount);
		annualSalesVolumeSummary.setAvrgSalesCount((int) avrgSalesCount);
		annualSalesVolumeSummary.setAvrgSalesAmount(avrgSalesAmount);
		
		return annualSalesVolumeSummary;

	}
	
	public List<SalesVolume> listingMonthlySalesVolume(String queryYear){
		return salesVolumeDao.listingMonthlySalesVolume(queryYear);
	}

	
	private List<SalesVolume> fillNonExistMonthSalesVolumeItems(String queryYear, List<SalesVolume> salesVolumes, List<String> itemKeys){
		
		for(int monthIdx=1; monthIdx<=12; monthIdx++){
			
			final String dateKey = String.format("%s-%02d", queryYear,monthIdx);
			
			SalesVolume findObject = (SalesVolume)CollectionUtils.find(salesVolumes, new org.apache.commons.collections.Predicate() {
		        public boolean evaluate(Object salesVolume) {
		            return ((SalesVolume)salesVolume).getDate().equals(dateKey);
		        }
		    });
			
			if(findObject==null){
				for(String itemName : itemKeys){
					salesVolumes.add(
							new SalesVolume.SalesVolumeBuilder()
							.withDate(dateKey)
							.withOptItemName(itemName)
							.withTotalSalesCount(0)
							.withTotalSalesAmount(0L).build());
				}
			}
		}
		
		Collections.sort(salesVolumes, new Comparator<SalesVolume>() {
			public int compare(SalesVolume first, SalesVolume second) {
				return first.getDate().compareTo(second.getDate());
			}
		});
		
		return salesVolumes;
	}
	
	
	public List<SalesVolume> getMonthlySalesVolumeByCategories(String queryYear){
		
		List<Category> productCategories = categoryDao.listingAllProductCategory();
		List<SalesVolume> categorySalesVolumes = salesVolumeDao.listingCategoriesMonthlySalesVolume(queryYear);
		
		List<String> categoryNames = new ArrayList<String>();
		for(Category category : productCategories){
			categoryNames.add(category.getName());
		}

		return fillNonExistMonthSalesVolumeItems(queryYear, categorySalesVolumes, categoryNames);
	}
	
	public List<SalesVolume> getMonthlySalesVolumeByProducts(String queryYear, final String categoryName){
	
		List<SalesVolume> listResult = salesVolumeDao.listingProductsMonthlySalesVolume(queryYear, categoryName);
		Category category = categoryDao.listingMajorProductsOfCategory(categoryName);

		Set<String> productNameKeySet = category.getProductsMap().keySet();
				
		List<SalesVolume> productsSalesVolumes = new ArrayList<SalesVolume>(); 
			
		Map<String,Integer> etcItemsToatalCountMap = new HashMap<String,Integer>();
		Map<String,Long> etcItemsToatalAmountMap = new HashMap<String,Long>();
			
		for(SalesVolume itemSalesVolume : listResult){
				if( productNameKeySet.contains(itemSalesVolume.getOptItemName())){
					productsSalesVolumes.add(itemSalesVolume);
				} else {
					String dateKey = itemSalesVolume.getDate();
					if(etcItemsToatalCountMap.containsKey(dateKey)){
						etcItemsToatalCountMap.put(dateKey, etcItemsToatalCountMap.get(dateKey) + itemSalesVolume.getTotalSalesCount());
						etcItemsToatalAmountMap.put(dateKey, etcItemsToatalAmountMap.get(dateKey) + itemSalesVolume.getTotalSalesAmount());
					} else {
						etcItemsToatalCountMap.put(dateKey, itemSalesVolume.getTotalSalesCount());
						etcItemsToatalAmountMap.put(dateKey, itemSalesVolume.getTotalSalesAmount());
					}
				}
		}
			
		for(String key : etcItemsToatalCountMap.keySet()){
			productsSalesVolumes.add(new SalesVolume.SalesVolumeBuilder()
						.withDate(key)
						.withOptItemName("ETC.")
						.withTotalSalesCount(etcItemsToatalCountMap.get(key))
						.withTotalSalesAmount(etcItemsToatalAmountMap.get(key))
						.build());
		}

		Collections.sort(productsSalesVolumes, new Comparator<SalesVolume>() {
			public int compare(SalesVolume first, SalesVolume second) {
				return first.getDate().compareTo(second.getDate());
			}
		});
		
		List<String> productNamesList = new ArrayList<String>();
		productNamesList.addAll(productNameKeySet);
		productNamesList.add("ETC.");

		return fillNonExistMonthSalesVolumeItems(queryYear, productsSalesVolumes, productNamesList);
	}

	
	public List<SalesVolume> getTimebaseSalesVolumeOfMonth(String queryYearMonth){
		List<SalesVolume> salesVolumes = salesVolumeDao.listingTimebaseSalesVolumeOfMonth(queryYearMonth);
		
		SalesVolume findObject = (SalesVolume)CollectionUtils.find(salesVolumes, new org.apache.commons.collections.Predicate() {
	        public boolean evaluate(Object salesVolume) {
	            return ((SalesVolume)salesVolume).getOptItemName().equals("00");
	        }
	    });

		if(findObject!=null){
			findObject.setOptItemName("24");
		}
		
		Collections.sort(salesVolumes, new Comparator<SalesVolume>() {
			public int compare(SalesVolume first, SalesVolume second) {
				return first.getOptItemName().compareTo(second.getOptItemName());
			}
		});
		
		return salesVolumes;
	}
	
	public List<SalesVolume> getDayOfWeekSalesVolumeOfMonth(String queryYearMonth){
		List<SalesVolume> salesVolumes = salesVolumeDao.listingDayOfWeekSalesVolumeOfMonth(queryYearMonth);
		
		for(SalesVolume salesVolume : salesVolumes){
			salesVolume.setOptItemName(weekOfDayMap.get(salesVolume.getOptItemName()));
		}
		
		return salesVolumes;
	}
	
	public List<SalesVolume> getTimebaseSalesVolumeOfDate(String queryYearMonthDay){
		
		//Read From Redis Cache.
		String REDIS_KEY = RedisCacheService.PASTDAY_TIMEBASE_SALESAMOUNT_OF  + queryYearMonthDay;
		List<String> salesVolumesStr = redisCacheService.getFromList(REDIS_KEY);
		
		List<SalesVolume> retSalesVolumes = null;
		
		if(salesVolumesStr!=null && salesVolumesStr.size()>0){
			//System.out.println("Read From Redis Cached");
			retSalesVolumes = new ArrayList<SalesVolume>();
			
			for(String salesVolumeStr : salesVolumesStr){
				try {
					retSalesVolumes.add(parseSalesVolumeFromJsonObjectString(salesVolumeStr));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		} else {
			retSalesVolumes = salesVolumeDao.listingTimebaseSalesVolumeOfDate(queryYearMonthDay);
			
			//Store To Redis Cache.
			for(SalesVolume salesVolume : retSalesVolumes){
				redisCacheService.addToList(REDIS_KEY, salesVolume.toJsonString());
			}
		}
		
		SalesVolume findObject = (SalesVolume)CollectionUtils.find(retSalesVolumes, new org.apache.commons.collections.Predicate() {
	        public boolean evaluate(Object salesVolume) {
	            return ((SalesVolume)salesVolume).getOptItemName().equals("00");
	        }
	    });

		if(findObject!=null){
			findObject.setOptItemName("24");
		}
		
		Collections.sort(retSalesVolumes, new Comparator<SalesVolume>() {
			public int compare(SalesVolume first, SalesVolume second) {
				return first.getOptItemName().compareTo(second.getOptItemName());
			}
		});
		
		return retSalesVolumes;
	}
	
	
	public List<SalesVolume> getTimebaseSalesVolumeOfToday(String queryYearMonthDay){
		
		List<SalesVolume> salesVolumes = new ArrayList<SalesVolume>();
		
		Set<String> targetKeys = redisCacheService.readKeys(RedisCacheService.TOTAL_SALES_AMOUNT_OF_DAY +"timebase_of:"+ queryYearMonthDay +":*");
		for(String key : targetKeys){

			String[] keyFields = key.split(":");
			String timeSlot = keyFields[keyFields.length-1];
			
			salesVolumes.add(new SalesVolume.SalesVolumeBuilder()
					.withDate(queryYearMonthDay)
					.withOptItemName(timeSlot)
					.withTotalSalesCount(0)
					.withTotalSalesAmount(Long.parseLong(redisCacheService.readValueByKey(key)))
					.build());
		}
		
		SalesVolume findObject = (SalesVolume)CollectionUtils.find(salesVolumes, new org.apache.commons.collections.Predicate() {
	        public boolean evaluate(Object salesVolume) {
	            return ((SalesVolume)salesVolume).getOptItemName().equals("00");
	        }
	    });

		if(findObject!=null){
			findObject.setOptItemName("24");
		}
		
		Collections.sort(salesVolumes, new Comparator<SalesVolume>() {
			public int compare(SalesVolume first, SalesVolume second) {
				return first.getOptItemName().compareTo(second.getOptItemName());
			}
		});
		
		return salesVolumes;
	}
	
	private SalesVolume parseSalesVolumeFromJsonObjectString(String salesVolumeStr) throws ParseException{
		JSONParser jsonParser = new JSONParser();
		JSONObject salesVolumeJsonObj = (JSONObject)jsonParser.parse(salesVolumeStr);

		return new SalesVolume.SalesVolumeBuilder()
					.withDate((String) salesVolumeJsonObj.get("date"))
					.withOptItemName((String) salesVolumeJsonObj.get("optItemName"))
					.withTotalSalesAmount(((Long)salesVolumeJsonObj.get("totalSalesAmount")).intValue())
					.withTotalSalesCount(((Long)salesVolumeJsonObj.get("totalSalesCount")).intValue()).build();
	}
}
