package com.salest.salestemperature.v3.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salest.salestemperature.v3.api.model.AnnualSalesVolumeSummary;
import com.salest.salestemperature.v3.dao.SalesVolumeDao;
import com.salest.salestemperature.v3.model.SalesVolume;

@Component
@Path("/salesvolume")
public class SalesvolumeResource {

	@Autowired
	SalesVolumeDao salesVolumeDao;
	
	@GET
	@Path("/annual_sales_vol_sum/{queryYear}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAnnualSalesVolumeSummary(@PathParam("queryYear") String queryYear) {
		
		Map<String,Object> mapAnnualSalesVolume = salesVolumeDao.getAnnualSalesVolume(queryYear);
		
		long totalNumOfDate = (Long) mapAnnualSalesVolume.get("total_num_of_date");
		long totalNumOfProduct = (Long) mapAnnualSalesVolume.get("total_num_of_product");
		long totalAmount = (Long) mapAnnualSalesVolume.get("total_amount");
		
		
		AnnualSalesVolumeSummary responseObj = new AnnualSalesVolumeSummary();
		
		responseObj.setTotalSalesCount((int) totalNumOfProduct);
		responseObj.setTotalSalesAmount(totalAmount);
		responseObj.setAvrgSalesCount((int) (totalNumOfProduct/totalNumOfDate));
		responseObj.setAvrgSalesAmount(totalAmount/totalNumOfDate);
		
		return Response.status(200).entity(responseObj).build();
	}
	
	
	@GET
	@Path("/monthly_sales_vol/{queryYear}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMontlySalesVolume(@PathParam("queryYear") String queryYear) {
		
		List<SalesVolume> fullYearSalesVolumes = new ArrayList<SalesVolume>();
		List<SalesVolume> monthlySalesVolumes = salesVolumeDao.listingMonthlySalesVolume(queryYear);
		
		for(int i=1; i<=12; i++){
			
			final String targetMonthDate = String.format("%s-%02d", queryYear, i);
			
			SalesVolume targetCateItem = (SalesVolume) CollectionUtils.find(monthlySalesVolumes, new org.apache.commons.collections.Predicate() {
		        public boolean evaluate(Object monthSalesVolume) {
		            return ((SalesVolume)monthSalesVolume).getDate().equals(targetMonthDate) ;
		        }
		    });
			
			if(targetCateItem!=null){
				fullYearSalesVolumes.add(targetCateItem);
			} else {
				fullYearSalesVolumes.add(new SalesVolume.SalesVolumeBuilder()
						.withDate(targetMonthDate).withTotalSalesAmount(0L).withTotalSalesCount(0).build());
			}
		}
		
		return Response.status(200).entity(fullYearSalesVolumes).build();
	}
}
