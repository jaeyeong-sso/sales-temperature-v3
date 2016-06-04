<!DOCTYPE html>
<html>

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Sales Temperature - Do your business with your own insight!</title>

	<!-- JQuery & js-cookie -->	
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.6.1/jquery.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/js-cookie/2.1.0/js.cookie.min.js"></script>
	
	<!-- JSON.stringfy -->	
	<script src="http://ajax.cdnjs.com/ajax/libs/json2/20110223/json2.js"></script>
	
    <!-- Bootstrap Core CSS -->
    <link href="/salestemperature.v3/static/bower_components/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- MetisMenu CSS -->
    <link href="/salestemperature.v3/static/bower_components/metisMenu/dist/metisMenu.min.css" rel="stylesheet">

    <!-- Timeline CSS -->
    <link href="/salestemperature.v3/static/dist/css/timeline.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="/salestemperature.v3/static/dist/css/sb-admin-2.css" rel="stylesheet">

    <!-- Morris Charts CSS -->
    <link href="/salestemperature.v3/static/bower_components/morrisjs/morris.css" rel="stylesheet">

    <!-- Custom Fonts -->
    <link href="/salestemperature.v3/static/bower_components/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
	
	<script type="text/javascript" charset="utf-8">
		
        var funcLoadDescSalesVolumeData;
        var funcLoadMonthlySalesVolumeData;
        var funcLoadCategoriesSalesVolumeData;
        var funcLoadProductsSalesVolumeData;
        var funcLoadTimeBaseSalesVolumeData;
        var funcLoadTimeBaseSalesVolumeDataWithElement;
        var funcLoadDayOfWeekSalesVolumeData;
        var funcLoadDayOfWeekSalesVolumeDataWithElement;
        
		var funcQueryReportPerYear;
		
		var curSelectYear;
        
		function getNumOfMonthByNameOfMonth(month_keys_dict, nameOfMonth){
			return $.grep(month_keys_dict, function(item){ return item.nameOfMonth == nameOfMonth; });
		}
		
		function getNameOfMonthByNumOfMonth(month_keys_dict, numOfMonth){
			return $.grep(month_keys_dict, function(item){ return item.numOfMonth == numOfMonth; });
		}
		
		$(document).ready(function(){
            
			var month_keys_dict = [
			    { nameOfMonth : "Jan.", numOfMonth : "01"},
			    { nameOfMonth : "Feb.", numOfMonth : "02"},
			    { nameOfMonth : "Mar.", numOfMonth : "03"},
			    { nameOfMonth : "Apr.", numOfMonth : "04"},
			    { nameOfMonth : "May.", numOfMonth : "05"},
			    { nameOfMonth : "Jun.", numOfMonth : "06"},
			    { nameOfMonth : "Jul.", numOfMonth : "07"},
			    { nameOfMonth : "Aug.", numOfMonth : "08"},
			    { nameOfMonth : "Sep.", numOfMonth : "09"},
			    { nameOfMonth : "Oct.", numOfMonth : "10"},
			    { nameOfMonth : "Nov.", numOfMonth : "11"},
			    { nameOfMonth : "Dec.", numOfMonth : "12"}
			];

			var menuCategoryItems = [];
			
			var dataArr_PerProductCateSalesVolume = [];
			var keyArr_PerProductCateSalesVolume = [];
			
			var dataArr_montlyTotalSalesVolume = [];
			
			var area_chart = Morris.Area({
		        element: 'per-product-category-sales-volume-chart',
		        data: dataArr_PerProductCateSalesVolume,
		        xkey: 'date',
		        ykeys: keyArr_PerProductCateSalesVolume,
		        labels: keyArr_PerProductCateSalesVolume,
		        pointSize: 2,
		        hideHover: 'auto',
		        resize: true
			});
			
            var line_chart = Morris.Line({
                element: 'monthly-sales-volume-chart',
                data: dataArr_montlyTotalSalesVolume,	//jsonObj,
                xkey: 'date',
                ykeys: ['totalSalesCount','totalSalesAmount'],
                labels: ['Product Count','Total Amount'],
                pointSize: 2,
                hideHover: 'auto',
                hoverCallback: function(index, options, content, row) {
                    var new_content = $("<div class='morris-hover-row-label'><span id='date'></span></div><div class='morris-hover-point' style='color: #0b62a4'><span id='num_of_product'></span></div><div class='morris-hover-point' style='color: #7A92A3'><span id='total_amount'></span></div>");
                    $('#date', new_content).html(row.date);
                    $('#num_of_product',new_content).html(row.totalSalesCount + " items");
                    $('#total_amount',new_content).html(row.totalSalesAmount + " KRW(ten thounsand)");
                    return (new_content);
                },
                
                resize: true
            });
  
			/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            // [STRAT] - Monthly Aggregated SalesVolume
			/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			funcLoadMonthlySalesVolumeData = function(){
                
                $.ajax({
                    type: "GET",
                    dataType: "json",
                    contentType: "application/json",
                    url: "/salestemperature.v3/api/salesvolume/monthly_sales_vol/" + curSelectYear,
                    beforeSend : function(){
                        $('#myModal').modal('show');
                    },
                    success: function (response) {
               
                        dataArr_montlyTotalSalesVolume.length = 0;
                        dataArr_montlyTotalSalesVolume = $.extend(true, [], response);
                        
						line_chart.setData(dataArr_montlyTotalSalesVolume);
						line_chart.redraw();
						
                        $('#myModal').modal('hide');
                    },
                    error: function () {
                        $('#myModal').modal('hide');
                        alert("Error loading data! Please try again.");
                    }	
                });
            }
			/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            // [END] - Monthly Aggregated SalesVolume
			/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			
			/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            // [STRAT] - Annual Summary of Sales Volume
			/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
			funcLoadDescSalesVolumeData = function(){
                
                $.ajax({
                    type: "GET",
                    dataType: "json",
                    contentType: "application/json",
                    url: "/salestemperature.v3/api/salesvolume/annual_sales_vol_sum/" + curSelectYear,
                    beforeSend : function(){
                        $('#myModal').modal('show');
                    },
                    success: function (response) {

                        var num_of_product = response.totalSalesCount;
                        var total_of_amount = parseInt(response.totalSalesAmount / 10000)
                        var avrg_sales_count = response.avrgSalesCount;
                        var avrg_sales_amount = parseInt(response.avrgSalesAmount / 10000)
                        $('#total_sales_count').text(num_of_product);
                        $('#total_sales_amount').text(total_of_amount);
                        $('#avrg_sales_count').text(avrg_sales_count);
                        $('#avrg_sales_amount').text(avrg_sales_amount);
                        $('#myModal').modal('hide');
                    },
                    error: function () {
                        $('#myModal').modal('hide');
                        alert("Error loading data! Please try again.");
                    }
                });
            }
			/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            // [END] - Annual Summary of Sales Volume
			/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	

			/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            // [STRAT] - SalesVolume By Categories/Products
			/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			
			funcSelectCategoryItem = function(element){
				funcLoadProductsSalesVolumeData(element, curSelectYear);
            }
            
			funcBindingCategoryNameItems = function(categoryItemsArr){
				
	            $("#menu_cate_items_list").children().remove();
	            $.each(categoryItemsArr, function(key, value){
	                $("#menu_cate_items_list").append("<li><a href='#' onclick='funcLoadProductsSalesVolumeData(this);return false;'>" + value + "</a></li>");
	            });
	            $("#menu_cate_items_list").append("<li><a href='#' onclick='funcLoadCategoriesSalesVolumeData();return false;'>All</a></li>");
			
			}

            funcLoadCategoriesSalesVolumeData = function(){
                
                $.ajax({
                    type: "GET",
                    dataType: "json",
                    contentType: "application/json",
                    url: "/salestemperature.v3/api/salesvolume/products_sales_vol/" + curSelectYear,
                    beforeSend : function(){
                        $('#myModal').modal('show');
                    },
                    success: function (response) {
      
       					var annualSalesAmount = [];
       			
                    	for(var monthlySalesVolItems in response){
                    		var monthlySalesAmountObj = new Object();
                    		monthlySalesAmountObj.date = response[monthlySalesVolItems].date;
                    		
                    		var itemList = response[monthlySalesVolItems].itemList;
                    		for(var idx in itemList){
                    			monthlySalesAmountObj[itemList[idx].itemName] = itemList[idx].totalSalesAmount;
                    		}
                    		
                    		annualSalesAmount.push(monthlySalesAmountObj)
                    	}
                    	
                        // clear before items
                        dataArr_PerProductCateSalesVolume.length = 0;
                        keyArr_PerProductCateSalesVolume.length = 0;
                        
                        keyArr_PerProductCateSalesVolume = $.extend(true, [], Object.keys(annualSalesAmount[0]));
                        keyArr_PerProductCateSalesVolume = keyArr_PerProductCateSalesVolume.slice(1,keyArr_PerProductCateSalesVolume.length)	// first 'date' item should be removed

                        dataArr_PerProductCateSalesVolume = $.extend(true, [], annualSalesAmount);

                        area_chart.options.labels = keyArr_PerProductCateSalesVolume;
                        area_chart.options.ykeys = keyArr_PerProductCateSalesVolume;
                        area_chart.setData(dataArr_PerProductCateSalesVolume);
                        area_chart.redraw();
                        
 						funcBindingCategoryNameItems(keyArr_PerProductCateSalesVolume);
           				
                        $('#myModal').modal('hide');
                    },
                    error: function () {
                        $('#myModal').modal('hide');
                        alert("Error loading data! Please try again.");
                    }	
                });
            }	

            funcLoadProductsSalesVolumeData = function(element){
                
            	var selCategoryName = $(element).text();
				$("#menu_cate_items_caption").text(selCategoryName);
				
                $.ajax({
                    type: "GET",
                    dataType: "json",
                    contentType: "application/json",
                    url: "/salestemperature.v3/api/salesvolume/products_sales_vol/" + curSelectYear + "/" + selCategoryName,
                    beforeSend : function(){
                        $('#myModal').modal('show');
                    },
                    success: function (response) {
      
       					var annualSalesAmount = [];
       			
                    	for(var monthlySalesVolItems in response){
                    		var monthlySalesAmountObj = new Object();
                    		monthlySalesAmountObj.date = response[monthlySalesVolItems].date;
                    		
                    		var itemList = response[monthlySalesVolItems].itemList;
                    		for(var idx in itemList){
                    			monthlySalesAmountObj[itemList[idx].itemName] = itemList[idx].totalSalesAmount;
                    		}
                    		
                    		annualSalesAmount.push(monthlySalesAmountObj)
                    	}
                    	
                        // clear before items
                        dataArr_PerProductCateSalesVolume.length = 0;
                        keyArr_PerProductCateSalesVolume.length = 0;
                        
                        keyArr_PerProductCateSalesVolume = $.extend(true, [], Object.keys(annualSalesAmount[0]));
                        keyArr_PerProductCateSalesVolume = keyArr_PerProductCateSalesVolume.slice(1,keyArr_PerProductCateSalesVolume.length)	// first 'date' item should be removed

                        dataArr_PerProductCateSalesVolume = $.extend(true, [], annualSalesAmount);

                        area_chart.options.labels = keyArr_PerProductCateSalesVolume;
                        area_chart.options.ykeys = keyArr_PerProductCateSalesVolume;
                        area_chart.setData(dataArr_PerProductCateSalesVolume);
                        area_chart.redraw();
                        
                        $('#myModal').modal('hide');
                    },
                    error: function () {
                        $('#myModal').modal('hide');
                        alert("Error loading data! Please try again.");
                    }	
                });
            }	
            
			/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            // [END] - SalesVolume By Categories/Products
			/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
			
			/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            // [START] - SalesVolume By TimeBase
			/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
			funcLoadTimeBaseSalesVolumeDataWithElement = function(element){
				if(element!=null){
					funcLoadTimeBaseSalesVolumeData(curSelectYear, $(element).text());
				}
			}
			
			funcLoadTimeBaseSalesVolumeData = function(year,monthName){
				
				 var numOfMonthObj = getNumOfMonthByNameOfMonth(month_keys_dict, monthName);
				 var numofMonthValue = numOfMonthObj[0].numOfMonth;
				
				 $.ajax({
	                    type: "GET",
	                    dataType: "json",
	                    contentType: "application/json",
	                    url: "/salestemperature.v3/api/salesvolume/timebase_sales_vol/" + year + "/" + numofMonthValue,
	                    beforeSend : function(){
	                        $('#myModal').modal('show');
	                    },
	                    success: function (response) {
	      
	                    	$("#timebase_sales_amount_items_caption").text(monthName);	
	                    	
	                    	$("#list_timebase_sales_amount").children().remove();
	                        
	                    	for(var idxItemList in response.itemList){
	                    		
	                    		var timeSlotKey = response.itemList[idxItemList].itemName;
	                    		var timeSlotItemId = "#time_" + timeSlotKey;
	                    		var totalSalesAmount = response.itemList[idxItemList].totalSalesAmount;	
									
	                            var strHtmlItem = '<a href="#" class="list-group-item">'+ timeSlotKey +
									'&nbsp;&nbsp;<span id="time_'+ timeSlotKey + '" class="rounded">&nbsp;</span>' +
									'<span class="pull-right text-muted small">' +
									'<i class="fa fa-krw fa-fw"></i><em>' + totalSalesAmount + '</em>' +
									'</span></a>';
	                            
	                            $("#list_timebase_sales_amount").append(strHtmlItem);
	                            $(timeSlotItemId).css("width", Math.round(parseInt(totalSalesAmount)/10000) +"px");
	                    	}
	                    	
	                        $('#myModal').modal('hide');
	                    },
	                    error: function () {
	                        $('#myModal').modal('hide');
	                        alert("Error loading data! Please try again.");
	                    }	
	                });	
			}
			
			/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			// [END] - SalesVolume By TimeBase
			/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				
			/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            // [START] - SalesVolume By DayOfWeek
			/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			funcLoadDayOfWeekSalesVolumeDataWithElement = function(element){
				if(element!=null){
					funcLoadDayOfWeekSalesVolumeData(curSelectYear, $(element).text());
				}
			}

			funcLoadDayOfWeekSalesVolumeData = function(year,monthName){
				
				 var numOfMonthObj = getNumOfMonthByNameOfMonth(month_keys_dict, monthName);
				 var numofMonthValue = numOfMonthObj[0].numOfMonth;
				
				 $.ajax({
	                    type: "GET",
	                    dataType: "json",
	                    contentType: "application/json",
	                    url: "/salestemperature.v3/api/salesvolume/dayofweek_sales_vol/" + year + "/" + numofMonthValue,
	                    beforeSend : function(){
	                        $('#myModal').modal('show');
	                    },
	                    success: function (response) {
	
	                    	$("#dayofweek_sales_amount_items_caption").text(monthName);	
	                    	
	                    	$("#list_weekofday_sales_amount").children().remove();
	                        
	                    	for(var idxItemList in response.itemList){
	                    		
	                    		var key = response.itemList[idxItemList].itemName;
	                    		var itemId = "#dayofweek_" + key;
	                    		var totalSalesAmount = response.itemList[idxItemList].totalSalesAmount;	
									
	                            var strHtmlItem = '<a href="#" class="list-group-item">'+ '<span style="display:inline-block; width:50px">' + key + '</span>' +
									'<span id="dayofweek_'+ key + '" class="rounded">&nbsp;</span>' +
									'<span class="pull-right text-muted small">' +
									'<i class="fa fa-krw fa-fw"></i><em>' + totalSalesAmount + '</em>' +
									'</span></a>';
	                            
	                            $("#list_weekofday_sales_amount").append(strHtmlItem);
	                            $(key).css("width", "500px");
	                            $(itemId).css("width", Math.round(parseInt(totalSalesAmount)/2000) +"px");
	                    	}
	                    	
	                        $('#myModal').modal('hide');
	                    },
	                    error: function () {
	                        $('#myModal').modal('hide');
	                        alert("Error loading data! Please try again.");
	                    }	
	                });	
			}
			/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			// [END] - SalesVolume By DayOfWeek
			/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				
			
    		funcQueryReportPerYear = function(year,nameOfMonth){
    			
    			curSelectYear = year;
    			
    			var year_combo_caption = curSelectYear + '&nbsp;<span class="caret"></span>'
    			
				$("#selected_year_caption").html(year_combo_caption);
				
				funcLoadDescSalesVolumeData();
	        	funcLoadMonthlySalesVolumeData();
	        	funcLoadCategoriesSalesVolumeData();
	        	funcLoadTimeBaseSalesVolumeData(curSelectYear, "Dec.");
	        	funcLoadDayOfWeekSalesVolumeData(curSelectYear, "Dec.");
    		}
		})
            
        $(window).load(function(){
        	funcQueryReportPerYear("2014","12");
        })
		
	</script>
	
	<style type="text/css">
		.rounded {
			display:inline-block;
	  		border-radius: 2px;
	  		background: #606060;
	  		width:30px;
		}
	</style>
	
</head>

<body>

    <div id="wrapper">

 		<!-- Loading Progress Modal -->
		<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<h4 class="modal-title">Now Data is being analyzed...</h4>
 					</div>
					<div class="modal-body">
						<div class="progress">
							<div class="progress-bar progress-bar-striped active" role="progressbar" aria-valuenow="100" aria-valuemin="0" aria-valuemax="100" style="width: 100%">
						  </div>
						</div>
                    </div>
					<div class="modal-footer">
						Please wait to be completed.
					</div>
				</div>
			</div>
		</div>
		<!-- /.modal -->
	
        <!-- Navigation -->
                   
        <nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="/salestemperature.v3/web/stats_report">Sales Temperature 3.0</a>
            </div>
            <!-- /.navbar-header -->

            <div class="navbar-default sidebar" role="navigation">
                <div class="sidebar-nav navbar-collapse">
                    <ul class="nav" id="side-menu">
                        <li>
                            <a href="/salestemperature.v3/web/stats_report"><i class="fa fa-calendar fa-fw"></i> Annual Stats Report</a>
                        </li>
                        <li>
                            <a href="/salestemperature.v3/web/realtime_report"><i class="fa fa-bolt fa-fw"></i> Realtime Sales Analysis</a>
                        </li>
                        <li>
                        	<div class="panel panel-primary" style='width: 80%; object-fit: contain; margin:auto; margin-top:20px; margin-bottom:20px;'>
                        		<div class="panel-heading">
                        			Shop Info.
                        		</div>
		                        <div class="panel-body">
		                        	<form role="form">
                                        <div class="form-group input-group">
                                            <span class="input-group-addon">Shop name</span>
                                            <input type="text" class="form-control" placeholder="Cafe XXXX" disabled>
                                        </div>
                                        <div class="form-group input-group">
                                            <span class="input-group-addon">Location</span>
                                            <input type="text" class="form-control" placeholder="Incheon XXX" disabled>
                                        </div>
 
                        			</form>
                        			<!--
									<img src="{% static 'salest_dashbd/images/cafe-lieto-logo.png' %}" style='height: 100%; width: 100%; object-fit: contain;'>
		                        	-->
		                        </div>
							</div>
                        </li>
                    </ul>
                </div>
                <!-- /.sidebar-collapse -->
            </div>
            <!-- /.navbar-static-side -->
        </nav>

        <div id="page-wrapper">
            <div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header">
                    	<table width="100%">
                    		<tr>
                    			<td>Annual Stats Report</td>
                    			<td align="right"><small></small></td>
	                    		<td align="right">
									<div class="btn-group">
										<button type="button" class="btn btn-default">Year</button>
										<div class="btn-group" role="group">
										  <button id="selected_year_caption" type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
										    2015 <span class="caret"></span>
										  </button>
										  <ul class="dropdown-menu">
										    <li><a href="#" onclick="funcQueryReportPerYear('2016');return false;">2016</a></li>
										    <li role="separator" class="divider"></li>
										    <li><a href="#" onclick="funcQueryReportPerYear('2015');return false;">2015</a></li>
										    <li role="separator" class="divider"></li>
										    <li><a href="#" onclick="funcQueryReportPerYear('2014');return false;">2014</a></li>
										  </ul>
										</div>
									</div>

	                    			<!--
				                	<div class="btn-group" role="group" aria-label="...">
			  						<button type="button" class="btn btn-default" onclick="funcQueryReportPerYear('2014');return false;">2014</button>
				  						<button type="button" class="btn btn-default" onclick="funcQueryReportPerYear('2015');return false;">2015</button>
									</div>
									-->
								</td>
							</tr>
						</table>
	                </h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
            <div class="row">
                <div class="col-lg-3 col-md-6">
                    <div class="panel panel-primary">
                        <div class="panel-heading">
                            <div class="row">
                                <div class="col-xs-3">
                                    <i class="fa fa-krw fa-2x"></i>
                                </div>
                                <div class="col-xs-9 text-right">
                                    <div class="huge">
                                    	<span id="total_sales_amount"></span>
                                    </div>
                                    <div>KRW(ten thounsand)</div>
                                </div>
                            </div>
                        </div>
                        <a href="#">
                            <div class="panel-footer">
                                <span class="pull-left">Amount of Sales</span>
                                <span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
                                <div class="clearfix"></div>
                            </div>
                        </a>
                    </div>
                </div>
                <div class="col-lg-3 col-md-6">
                    <div class="panel panel-green">
                        <div class="panel-heading">
                            <div class="row">
                                <div class="col-xs-3">
                                    <i class="fa fa-credit-card fa-2x"></i>
                                </div>
                                <div class="col-xs-9 text-right">
                                    <div class="huge">
                                    	<span id="total_sales_count"></span>
                                    </div>
                                    <div>items</div>
                                </div>
                            </div>
                        </div>
                        <a href="#">
                            <div class="panel-footer">
                                <span class="pull-left">Number of Items</span>
                                <span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
                                <div class="clearfix"></div>
                            </div>
                        </a>
                    </div>
                </div>
                <div class="col-lg-3 col-md-6">
                    <div class="panel panel-yellow">
                        <div class="panel-heading">
                            <div class="row">
                                <div class="col-xs-3">
                                    <i class="fa fa-krw fa-2x"></i>
                                </div>
                                <div class="col-xs-9 text-right">
                                    <div class="huge">
                                    	<span id="avrg_sales_amount"></span>
                                    </div>
                                    <div>KRW(ten thounsand)</div>
                                </div>
                            </div>
                        </div>
                        <a href="#">
                            <div class="panel-footer">
                                <span class="pull-left">Daily average Amount</span>
                                <span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
                                <div class="clearfix"></div>
                            </div>
                        </a>
                    </div>
                </div>
                <div class="col-lg-3 col-md-6">
                    <div class="panel panel-red">
                        <div class="panel-heading">
                            <div class="row">
                                <div class="col-xs-3">
                                    <i class="fa fa-credit-card fa-2x"></i>
                                </div>
                                <div class="col-xs-9 text-right">
                                    <div class="huge">
                                    	<span id="avrg_sales_count"></span>
                                    </div>
                                    <div>items</div>
                                </div>
                            </div>
                        </div>
                        <a href="#">
                            <div class="panel-footer">
                                <span class="pull-left">Daily average Items</span>
                                <span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
                                <div class="clearfix"></div>
                            </div>
                        </a>
                    </div>
                </div>
            </div>
            <!-- /.row -->
            <div class="row">
                <div class="col-lg-8">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <i class="fa fa-bar-chart-o fa-fw"></i> Monthly sales volume
                        </div>
                        <!-- /.panel-heading -->
                        <div class="panel-body">
                            <div id="monthly-sales-volume-chart"></div>
                        </div>
                        <!-- /.panel-body -->
                    </div>
                    <!-- /.panel -->
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <i class="fa fa-glass fa-fw"></i> Sales contribution per menu
                            <div class="pull-right">
   								<div class="btn-group">
                                    <button id="menu_cate_items_caption" type="button" class="btn btn-default btn-xs dropdown-toggle" data-toggle="dropdown">
										All
                                        <!-- <span class="caret"></span> -->
                                    </button>
                                    <ul id="menu_cate_items_list" class="dropdown-menu pull-right" role="menu">
                                    	<!-- Fill Category Items dynamically -->
                                    </ul>
                                </div>
                            </div>
                        </div>
                        <!-- /.panel-heading -->
                        <div class="panel-body">
                            <div id="per-product-category-sales-volume-chart"></div>
                        </div>
                        <!-- /.panel-body -->
                    </div>
 
                </div>
                <!-- /.col-lg-8 -->
                <div class="col-lg-4">
                	<div class="row">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <i class="glyphicon glyphicon-time"></i> Hourly total sales amount of Month.
                            <div class="pull-right">
   								<div class="btn-group">
                                    <button id="timebase_sales_amount_items_caption" type="button" class="btn btn-default btn-xs dropdown-toggle" data-toggle="dropdown">
										Select.
                                        <!--  <span class="caret"></span> -->
                                    </button>
                                    <ul id="timebase_sales_amount_items_list" class="dropdown-menu pull-right" role="menu">
                                    	<!-- Fill Category Items dynamically -->
                                    	<li><a href='#' onclick='funcLoadTimeBaseSalesVolumeDataWithElement(this);return false;'>Jan.</a></li>
										<li><a href='#' onclick='funcLoadTimeBaseSalesVolumeDataWithElement(this);return false;'>Feb.</a></li>
										<li><a href='#' onclick='funcLoadTimeBaseSalesVolumeDataWithElement(this);return false;'>Mar.</a></li>
										<li><a href='#' onclick='funcLoadTimeBaseSalesVolumeDataWithElement(this);return false;'>Apr.</a></li>
										<li><a href='#' onclick='funcLoadTimeBaseSalesVolumeDataWithElement(this);return false;'>May.</a></li>
										<li><a href='#' onclick='funcLoadTimeBaseSalesVolumeDataWithElement(this);return false;'>Jun.</a></li>
										<li><a href='#' onclick='funcLoadTimeBaseSalesVolumeDataWithElement(this);return false;'>Jul.</a></li>
										<li><a href='#' onclick='funcLoadTimeBaseSalesVolumeDataWithElement(this);return false;'>Aug.</a></li>
										<li><a href='#' onclick='funcLoadTimeBaseSalesVolumeDataWithElement(this);return false;'>Sep.</a></li>
										<li><a href='#' onclick='funcLoadTimeBaseSalesVolumeDataWithElement(this);return false;'>Oct.</a></li>
										<li><a href='#' onclick='funcLoadTimeBaseSalesVolumeDataWithElement(this);return false;'>Nov.</a></li>
										<li><a href='#' onclick='funcLoadTimeBaseSalesVolumeDataWithElement(this);return false;'>Dec.</a></li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                        <!-- /.panel-heading -->
                        <div class="panel-body">
                            <div id="list_timebase_sales_amount" class="list-group">
                            </div>
                        </div>
                        <!-- /.panel-body -->
                    </div>
 
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <i class="glyphicon glyphicon-paperclip"></i> Average sales amount Per Week of day.
                            <div class="pull-right">
   								<div class="btn-group">
                                    <button id="dayofweek_sales_amount_items_caption" type="button" class="btn btn-default btn-xs dropdown-toggle" data-toggle="dropdown">
										Select.
                                        <!--  <span class="caret"></span> -->
                                    </button>
                                    <ul id="dayofweek_sales_amount_items_list" class="dropdown-menu pull-right" role="menu">
                                    	<!-- Fill Category Items dynamically -->
                                    	<li><a href='#' onclick='funcLoadDayOfWeekSalesVolumeDataWithElement(this);return false;'>Jan.</a></li>
										<li><a href='#' onclick='funcLoadDayOfWeekSalesVolumeDataWithElement(this);return false;'>Feb.</a></li>
										<li><a href='#' onclick='funcLoadDayOfWeekSalesVolumeDataWithElement(this);return false;'>Mar.</a></li>
										<li><a href='#' onclick='funcLoadDayOfWeekSalesVolumeDataWithElement(this);return false;'>Apr.</a></li>
										<li><a href='#' onclick='funcLoadDayOfWeekSalesVolumeDataWithElement(this);return false;'>May.</a></li>
										<li><a href='#' onclick='funcLoadDayOfWeekSalesVolumeDataWithElement(this);return false;'>Jun.</a></li>
										<li><a href='#' onclick='funcLoadDayOfWeekSalesVolumeDataWithElement(this);return false;'>Jul.</a></li>
										<li><a href='#' onclick='funcLoadDayOfWeekSalesVolumeDataWithElement(this);return false;'>Aug.</a></li>
										<li><a href='#' onclick='funcLoadDayOfWeekSalesVolumeDataWithElement(this);return false;'>Sep.</a></li>
										<li><a href='#' onclick='funcLoadDayOfWeekSalesVolumeDataWithElement(this);return false;'>Oct.</a></li>
										<li><a href='#' onclick='funcLoadDayOfWeekSalesVolumeDataWithElement(this);return false;'>Nov.</a></li>
										<li><a href='#' onclick='funcLoadDayOfWeekSalesVolumeDataWithElement(this);return false;'>Dec.</a></li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                        <!-- /.panel-heading -->
                        <div class="panel-body">
                            <div id="list_weekofday_sales_amount" class="list-group">
                            </div>
                        </div>
                        <!-- /.panel-body -->
                    </div>
 				</div>
 
                </div>
                <!-- /.col-lg-4 -->
            </div>
            <!-- /.row -->
        </div>
        <!-- /#page-wrapper -->

    </div>
    <!-- /#wrapper -->

    <!-- jQuery -->
    <script src="/salestemperature.v3/static/bower_components/jquery/dist/jquery.min.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="/salestemperature.v3/static/bower_components/bootstrap/dist/js/bootstrap.min.js"></script>

    <!-- Metis Menu Plugin JavaScript -->
    <script src="/salestemperature.v3/static/bower_components/metisMenu/dist/metisMenu.min.js"></script>

    <!-- Morris Charts JavaScript -->
    <script src="/salestemperature.v3/static/bower_components/raphael/raphael-min.js"></script>
    <script src="/salestemperature.v3/static/bower_components/morrisjs/morris.min.js"></script>

    <!-- Custom Theme JavaScript -->
    <script src="/salestemperature.v3/static/dist/js/sb-admin-2.js"></script>

</body>

</html>