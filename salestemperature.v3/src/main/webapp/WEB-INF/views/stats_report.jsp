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
    <link href="/salestemperature.v3/static/salest_dashbd/bower_components/morrisjs/morris.css" rel="stylesheet">

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
        var funcLoadProductCategorySalesVolumeData;
        
        var funcSelectCategoryItem;
		var funcSelectDayOfWeekItem;
        
        var funcDefaultSelectCategoryItem;
        
		var reverseArr;
		var selectDayOfWeekItem;
		var sortObjectByKey;
		
		var funcQueryReportPerYear;
		
		var curSelectYear;
        
		$(document).ready(function(){
            
			var menuCategoryItems = [];
			
			var dataArr_PerProductCateSalesVolume = [];
			var keyArr_PerProductCateSalesVolume = [];
			
			var dataArr_montlyTotalSalesVolume = [];
			
			var area_chart = Morris.Area({
		        element: 'per-product-category-sales-volume-chart',
		        data: dataArr_PerProductCateSalesVolume,
		        xkey: 'year_month',
		        ykeys: keyArr_PerProductCateSalesVolume,
		        labels: keyArr_PerProductCateSalesVolume,
		        pointSize: 2,
		        hideHover: 'auto',
		        resize: true
			});
			
            var line_chart = Morris.Line({
                element: 'monthly-sales-volume-chart',
                data: dataArr_montlyTotalSalesVolume,	//jsonObj,
                xkey: 'year_month',
                ykeys: ['num_of_product','total_amount'],
                labels: ['num_of_product','total_amount'],
                pointSize: 2,
                hideHover: 'auto',
                hoverCallback: function(index, options, content, row) {
                    var new_content = $("<div class='morris-hover-row-label'><span id='year_month'></span></div><div class='morris-hover-point' style='color: #0b62a4'><span id='num_of_product'></span></div><div class='morris-hover-point' style='color: #7A92A3'><span id='total_amount'></span></div>");
                    $('#year_month', new_content).html(row.year_month);
                    $('#num_of_product',new_content).html(row.num_of_product + " 건");
                    $('#total_amount',new_content).html(row.total_amount + " 만원");
                    return (new_content);
                },
                
                resize: true
            });
        	
            funcSelectDayOfWeekItem = function(element){
				
				var selectedItem;
				var jsonParams;
                
				if(element==null){
					selectedItem = 'All';
				} else {
					selectedItem = $(element).text();
					$("#timebase_sales_amount_items_caption").text(selectedItem);	
				}
				
				jsonParams = "{\"dayOfWeek\":\"" + selectedItem + "\"}";
				
				$.ajax({
					type: "POST",
					data: jsonParams,
					contentType: 'application/json',
					dataType: "json",
					url: "/salest_dashbd/api/timebase_sales_amount/" + curSelectYear,
                    beforeSend : function(){
                        $('#myModal').modal('show');
                    },
					success: function (response) {
						var jsonObj = $.parseJSON(response);
                        $("#list_timebase_sales_amount").children().remove();
                        
                        var keyTimeRange;
                        var rangeStartTime;
                        var rangeEndTime;
                        
                        $.each(jsonObj.total_amount, function(key, value){
                            
                            rangeStartTime = parseInt(key);
							rangeEndTime = rangeStartTime + 1;
                            
                            if(rangeStartTime < 10){
                                rangeStartTime = "0" + rangeStartTime;
                            }                        
                            if(rangeEndTime < 10){
                                rangeEndTime = "0" + rangeEndTime;
                            }
                            
							keyTimeRange = rangeStartTime + ":00" + "~"  + rangeEndTime + ":00";
							
                            var spanId = "#item_" + rangeStartTime;
                            
                            var strHtmlItem = '<a href="#" class="list-group-item">'+
								keyTimeRange +
								'&nbsp;&nbsp;<span id="item_'+ rangeStartTime +'" class="rounded">&nbsp;</span>' +
								'<span class="pull-right text-muted small">' +
								'<i class="fa fa-krw fa-fw"></i><em>' + value + '</em>' +
								'</span></a>';
                            
                            $("#list_timebase_sales_amount").append(strHtmlItem);
                            $(spanId).css("width", Math.round(parseInt(value)/350) +"px");
                        });
                        $('#myModal').modal('hide');
                    },
                    error: function () {
                        $('#myModal').modal('hide');
						alert("Error loading data! Please try again.");
					}	
				});
            }
            
                                     
            funcDefaultSelectCategoryItem = function(element, year){ 
				
				var selectedItem = $(element).text();
				$("#menu_cate_items_caption").text(selectedItem);
				
				var jsonParams = "{\"category\":\"" + selectedItem + "\"}";
				
				$.ajax({
					type: "POST",
					data: jsonParams,
					contentType: 'application/json; charset=UTF-8',
					dataType: "json",
					url: "/salest_dashbd/api/monthly_product_cate_detail_sales_amount/" + year,
                    beforeSend : function(){
                        $('#myModal').modal('show');
                    },
					success: function (response) {
						var jsonObj = $.parseJSON(response);
						var dataArr = jsonObj.total_amount;
						var dateKeyArr = [];	
						
						$.each(dataArr, function(key, value){
							for (var key in value) {
								if(key!='year_month'){
									dateKeyArr.push(key);
								}
							}
						});
						
						menuCategoryItems = dateKeyArr.filter(function(item,idx,arr){
						    return idx==arr.indexOf(item);
						});
										
						// clear before items
						dataArr_PerProductCateSalesVolume.length = 0;
						keyArr_PerProductCateSalesVolume.length = 0;
						
						dataArr_PerProductCateSalesVolume = $.extend(true, [], dataArr);
						keyArr_PerProductCateSalesVolume = $.extend(true, [], menuCategoryItems);	
						
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
            funcSelectCategoryItem = function(element){            
                funcDefaultSelectCategoryItem(element, curSelectYear);
            }
                        
			funcLoadMonthlySalesVolumeData = function(year){
                
                $.ajax({
                    type: "GET",
                    dataType: "json",
                    contentType: "application/json",
                    url: "/salest_dashbd/api/monthly_sales_vol/" + year,
                    beforeSend : function(){
                        $('#myModal').modal('show');
                    },
                    success: function (response) {
                        var jsonObj = $.parseJSON(response);
                        dataArr_montlyTotalSalesVolume.length = 0;
                        dataArr_montlyTotalSalesVolume = $.extend(true, [], jsonObj);
                        
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
            
			funcLoadDescSalesVolumeData = function(year){
                
                $.ajax({
                    type: "GET",
                    dataType: "json",
                    contentType: "application/json",
                    url: "/salest_dashbd/api/desc_total_sales_vol/" + year,
                    beforeSend : function(){
                        $('#myModal').modal('show');
                    },
                    success: function (response) {
                        var jsonObj = $.parseJSON(response);
                        var num_of_product = jsonObj.num_of_product.sum;
                        var total_of_amount = parseInt(jsonObj.total_amount.sum / 10000)
                        var avrg_sales_count = jsonObj.num_of_product.mean;
                        var avrg_sales_amount = parseInt(jsonObj.total_amount.mean / 10000)
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
            
			///////////////////////////////////////////////////////////////////////////////////////////////
            funcLoadProductCategorySalesVolumeData = function(year){
                
                $.ajax({
                    type: "GET",
                    dataType: "json",
                    contentType: "application/json",
                    url: "/salest_dashbd/api/monthly_product_cate_sales_amount/" + year,
                    beforeSend : function(){
                        $('#myModal').modal('show');
                    },
                    success: function (response) {
                        var jsonObj = $.parseJSON(response);
                        var dataArr = jsonObj.total_amount;
                        var dateKeyArr = [];	
                        $.each(dataArr, function(key, value){
                            for (var key in value) {
                                if(key!='year_month'){
                                    dateKeyArr.push(key);
                                }
                            }
                        });
                        menuCategoryItems = dateKeyArr.filter(function(item,idx,arr){
                            return idx==arr.indexOf(item);
                        });
                        // clear before items
                        dataArr_PerProductCateSalesVolume.length = 0;
                        keyArr_PerProductCateSalesVolume.length = 0;
                        dataArr_PerProductCateSalesVolume = $.extend(true, [], dataArr);
                        keyArr_PerProductCateSalesVolume = $.extend(true, [], menuCategoryItems);	
                        area_chart.options.labels = keyArr_PerProductCateSalesVolume;
                        area_chart.options.ykeys = keyArr_PerProductCateSalesVolume;
                        area_chart.setData(dataArr_PerProductCateSalesVolume);
                        area_chart.redraw();
                        
                        $("#menu_cate_items_list").children().remove();
                        
                        $.each(menuCategoryItems, function(key, value){
                            $("#menu_cate_items_list").append("<li><a href='#' onclick='funcSelectCategoryItem(this);return false;'>" + value + "</a></li>");
                        });
                        $("#menu_cate_items_list").append("<li><a href='#' onclick='funcSelectCategoryItem(this);return false;'>All</a></li>");
                        
                        $('#myModal').modal('hide');
                    },
                    error: function () {
                        $('#myModal').modal('hide');
                        alert("Error loading data! Please try again.");
                    }	
                });
            }	
			
			
    		funcQueryReportPerYear = function(year){
    			
    			curSelectYear = year;
    			
    			var year_combo_caption = curSelectYear + '&nbsp;<span class="caret"></span>'
    			
				$("#selected_year_caption").html(year_combo_caption);
				
				funcLoadDescSalesVolumeData(curSelectYear);
				funcLoadMonthlySalesVolumeData(curSelectYear);
				funcLoadProductCategorySalesVolumeData(curSelectYear);
				funcSelectDayOfWeekItem(null);
    		}
		})
            
        $(window).load(function(){
        	//funcQueryReportPerYear('2015');
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
                <a class="navbar-brand" href="{% url 'salest_dashbd.views.index' %}">Sales Temperature 3.0</a>
            </div>
            <!-- /.navbar-header -->

            <div class="navbar-default sidebar" role="navigation">
                <div class="sidebar-nav navbar-collapse">
                    <ul class="nav" id="side-menu">
                        <li>
                            <a href="{% url 'salest_dashbd.views.stats_report' %}"><i class="fa fa-calendar fa-fw"></i> Annual stats report</a>
                        </li>
                        <li>
                            <a href="{% url 'salest_dashbd.views.realtime_report' %}"><i class="fa fa-bolt fa-fw"></i> Realtime sales timebase comparison</a>
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
                    			<td>Annual stats report</td>
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
                                    <div>KRW</div>
                                </div>
                            </div>
                        </div>
                        <a href="#">
                            <div class="panel-footer">
                                <span class="pull-left">Amount of sales</span>
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
                                    <div>times</div>
                                </div>
                            </div>
                        </div>
                        <a href="#">
                            <div class="panel-footer">
                                <span class="pull-left">Number of sales</span>
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
                                    <div>KRW</div>
                                </div>
                            </div>
                        </div>
                        <a href="#">
                            <div class="panel-footer">
                                <span class="pull-left">Daily average amount</span>
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
                                    <div>times</div>
                                </div>
                            </div>
                        </div>
                        <a href="#">
                            <div class="panel-footer">
                                <span class="pull-left">Daily average number</span>
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
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <i class="glyphicon glyphicon-time"></i> Hourly average sales.Amount
                            <div class="pull-right">
   								<div class="btn-group">
                                    <button id="timebase_sales_amount_items_caption" type="button" class="btn btn-default btn-xs dropdown-toggle" data-toggle="dropdown">
										All
                                        <!--  <span class="caret"></span> -->
                                    </button>
                                    <ul id="timebase_sales_amount_items_list" class="dropdown-menu pull-right" role="menu">
                                    	<!-- Fill Category Items dynamically -->
                                    	<li><a href='#' onclick='funcSelectDayOfWeekItem(this);return false;'>W-MON</a></li>
										<li><a href='#' onclick='funcSelectDayOfWeekItem(this);return false;'>W-TUE</a></li>
										<li><a href='#' onclick='funcSelectDayOfWeekItem(this);return false;'>W-WED</a></li>
										<li><a href='#' onclick='funcSelectDayOfWeekItem(this);return false;'>W-THU</a></li>
										<li><a href='#' onclick='funcSelectDayOfWeekItem(this);return false;'>W-FRI</a></li>
										<li><a href='#' onclick='funcSelectDayOfWeekItem(this);return false;'>W-SAT</a></li>
										<li><a href='#' onclick='funcSelectDayOfWeekItem(this);return false;'>W-SUN</a></li>
										<li><a href='#' onclick='funcSelectDayOfWeekItem(this);return false;'>All</a></li>
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
    <script src="/salestemperature.v3/static/dist/js/sb-admin-2.js' %}"></script>

</body>

</html>