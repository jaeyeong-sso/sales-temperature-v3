<!DOCTYPE html>
<html lang="en">

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
		
		var productsDetailsJsonObj;
		
		var funcGetMostPopularProducts;
		var funcGetProductCategory;

		var funcChooseProductItem;
		
		//
		
		var funcDispCurTransactionDateTime;
		var funcLeadingZeros;
		
		var funcWriteTransactionLog;
		
		var funcPollingRealTimeBaseSalesData;
		
		var funcGetTodayDate;
		var timeBaseCompareDataArr;

		$(document).ready(function(){
			func_btn_test_postit = function(){
				alert('OK');
			}
			 
			funcGetTodayDate = function(){
				var dateObj = new Date();
				var tr_date = dateObj.getFullYear() + "-" + funcLeadingZeros((dateObj.getMonth()+1),2) + "-" + funcLeadingZeros(dateObj.getDate(),2);
				$("#today_date_caption").text(tr_date);
			}
			
			funcPollingRealTimeBaseSalesData = function(){
				
	        	 var dateObj = new Date();
				 var tr_date = dateObj.getFullYear() + "-" + funcLeadingZeros((dateObj.getMonth()+1),2) + "-" + funcLeadingZeros(dateObj.getDate(),2);
				
				
	             $.ajax({
	                    type: "GET",
	                    dataType: "json",
	                    contentType: "application/json",
	                    url: "/salest_dashbd/web/get_timebase_data_on_today_specific_date/" + tr_date,
	                    success: function (response) {
	                    	
	                    	var jsonObj = $.parseJSON(response);
	                        
	                    	$.each(jsonObj, function(key, value){
	                    		
		        				$.each(timeBaseCompareDataArr, function(idx,item){
		        					var jsonObj = JSON.parse(JSON.stringify(item));
		        					if(jsonObj.H == key){
		        						jsonObj.today = value;
		        						timeBaseCompareDataArr[idx] = jsonObj;
		        					}
		        				})
	                    	});
	                    	
	   						bar_chart.setData(timeBaseCompareDataArr);
    						bar_chart.redraw();
                 		},
                 		error: function () {
                     		alert("Error loading data! Please try again.");
                 		}	
				});
			}
			
			var bar_chart = Morris.Bar({
		        element: 'hourly-sales-compare-chart',
		        data: timeBaseCompareDataArr,
		        xkey: 'H',
		        ykeys: ['past','today'],
		        labels: ['past','today'],
		        hideHover: 'auto',
		        resize: true
		    });
			
			
			funcGetTimebaseDataOnSpecificDate = function(date){
				
	             $.ajax({
	                    type: "GET",
	                    dataType: "json",
	                    contentType: "application/json",
	                    url: "/salest_dashbd/web/get_timebase_data_on_past_specific_date/" + date,
	                    beforeSend : function(){
	                        $('#ProgressModal').modal('show');
	                    },
	                    success: function (response) {
	                        var jsonObj = $.parseJSON(response);
	
	                        timeBaseCompareDataArr.length = 0;
	                        
							$.each(jsonObj, function(key, value){
								
							   $('#past_date').text(key + " 대비");
							   
							   timeBaseCompareDataArr = $.extend(true, [], jsonObj[key]);			 
							});
							bar_chart.setData(timeBaseCompareDataArr);
							bar_chart.redraw();
							
	                        $('#ProgressModal').modal('hide');
	                    },
	                    error: function () {
	                        $('#ProgressModal').modal('hide');
	                        alert("Error loading data! Please try again.");
	                    }	
	                });
			}
			
			funcEvalProductSelectionState = function(product_name, tr_date, tr_time){
				if( product_name == ''){
					$('#AlertModal').modal('show');
					return false;
				} else {
					
					time_h = tr_time.split(':')[0];
			
					if(time_h < 10 || time_h > 23){
						$('#AlertModal').modal('show');
						return false;
						
					} else {
		
						$("#choosed_product_name").val('');
						$("#choosed_product_price").val('');
						$("#transaction_date").val('');
						$("#transaction_time").val('');
						
						return true;
					}
				}
			}
			
			funcWriteTransactionLog = function(){
				
				var product_name = $("#choosed_product_name").val();
				var tr_date = $("#transaction_date").val();
				var tr_time = $("#transaction_time").val();
				
				if( funcEvalProductSelectionState(product_name, tr_date, tr_time) == false){
					return;
				}
				var jsonParams = "{\"productName\":\"" + product_name + 
					"\",\"trDate\":\"" + tr_date +
					"\",\"trTime\":\"" + tr_time +  "\"}";
				
					$.ajax({
						type: "POST",
						data: jsonParams,
						contentType: 'application/json; charset=UTF-8',
						dataType: "json",
						url: "/salestemperature.v3/web/saleslog/write",
	                    beforeSend : function(){
	                        $('#ProgressModal').modal('show');
	                    },
						success: function (response) {
							
							//setTimeout(funcPollingRealTimeBaseSalesData(),3000);
				        	
							$('#ProgressModal').modal('hide');
						}, 
						error: function (response) {
	                        $('#ProgressModal').modal('hide');
							//alert("Error loading data! Please try again.");
						}
					});
			}
			
			funcGetProductCategory = function(){
				
				$.ajax({
					type: "GET",
                    dataType: "json",
                    contentType: "application/json",
					url:"/salestemperature.v3/web/productinfo/get_products_category",
                    beforeSend : function(){
                        $('#ProgressModal').modal('show');
                    },
					success: function (response) {
			
						$("#menu_cate_items_list").children().remove();
	                        
						$.each(response, function(key, value){
	                    	$("#menu_cate_items_list").append("<li><a href='#' onclick='funcGetMostPopularProducts(this);return false;'>" + value + "</a></li>");
	 					});
	                      
						$('#ProgressModal').modal('hide');
					},
	        		error: function () {
                        $('#ProgressModal').modal('hide');
						alert("Error loading data! Please try again.");
					}
				});
			}
			
			funcGetMostPopularProducts = function(element){
				
				var categoryName = $(element).text();
				$("#choose_cate_caption").html(categoryName);
				
				$.ajax({
					type: "GET",
                    dataType: "json",
                    contentType: "application/json",
					url:"/salestemperature.v3/web/productinfo/get_products_items/" + categoryName,
                    beforeSend : function(){
                        $('#ProgressModal').modal('show');
                    },
					success: function (response) {
						
						productsDetailsJsonObj = response;
						
						$("#product_items_list").children().remove();
						for(var productItemIdx in productsDetailsJsonObj){
							$("#product_items_list").append("<li><a href='#' onclick='funcChooseProductItem(this);return false;'>" + productsDetailsJsonObj[productItemIdx].name + "</a></li>");
						}
	                      
						$('#ProgressModal').modal('hide');
					},
	        		error: function () {
                        $('#ProgressModal').modal('hide');
						alert("Error loading data! Please try again.");
					}
				});
			}
			
			funcChooseProductItem = function(element){
				var productName = $(element).text();
				
				$.each(productsDetailsJsonObj, function(idx,item){
					var productJsonObj = JSON.parse(JSON.stringify(item));
					if(productJsonObj.name == productName){
						$("#choose_product_caption").text(productJsonObj.name);
						$("#choosed_product_name").val(productJsonObj.name);
						$("#choosed_product_price").val(productJsonObj.price);
						funcDispCurTransactionDateTime();
					}
				});
			}
			
			funcDispCurTransactionDateTime = function(){
				
				var dateObj = new Date();
				var tr_date = dateObj.getFullYear() + "-" + funcLeadingZeros((dateObj.getMonth()+1),2) + "-" + funcLeadingZeros(dateObj.getDate(),2);
				var tr_time = funcLeadingZeros(dateObj.getHours(),2) + ":" + funcLeadingZeros(dateObj.getMinutes(),2) + ":" + funcLeadingZeros(dateObj.getSeconds(),2);
				
				$("#transaction_date").val(tr_date);
				$("#transaction_time").val(tr_time);
				
			}
			
			funcLeadingZeros = function(n, digits) {
				  var zero = '';
				  n = n.toString();
				  if (n.length < digits) {
				    for (var i = 0; i < digits - n.length; i++)
				      zero += '0';
				  }
				  return zero + n;
			}
		})
            
        $(window).load(function(){
        	funcGetTodayDate();
        	funcGetProductCategory();
        	
        	//var dateObj = new Date();
			//var tr_date = dateObj.getFullYear() + "-" + funcLeadingZeros((dateObj.getMonth()+1),2) + "-" + funcLeadingZeros(dateObj.getDate(),2);
			
        	//funcGetTimebaseDataOnSpecificDate(tr_date);
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
    
		<!-- Loading Alert Modal -->
		<div id="AlertModal" class="modal fade" tabindex="-1" role="dialog">
		  <div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header">
		        <h4 class="modal-title">Hum...</h4>
		      </div>
		      <div class="modal-body">
		        <p>Please confirm the appropriate business hours(10~23).</p>
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-default" data-dismiss="modal">OK, I know.</button>
		      </div>
		    </div><!-- /.modal-content -->
		  </div><!-- /.modal-dialog -->
		</div><!-- /.modal -->

 		<!-- Loading Progress Modal -->
		<div class="modal fade" id="ProgressModal" tabindex="-1" role="dialog" aria-labelledby="ProgressModalLabel" aria-hidden="true">
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
                <a class="navbar-brand" href="{% url 'salest_dashbd.views.index' %}">Sales Temperature 2.0</a>
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
                    			<td>Realtime Sales Analysis</td>
                    			<td align="right"><small></small></td>
	                    		<td align="right">
									<div class="btn-group">
										<button type="button" class="btn btn-default">Today</button>
										<div class="btn-group" role="group">
										  <button id="today_date_caption" type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
										  </button>
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
             	 <div class="col-lg-12">
                    <div class="panel panel-primary">
                        <div class="panel-heading">
							Register Sales Log Data
                        </div>
                        <div class="panel-body">
                        
                        	<div class="row">
             	 			<div class="col-lg-4">

	                        	<!-- Category list   -->
								<div class="btn-group">
								  <button id="choose_cate_caption" type="button" class="btn btn-danger">Category</button>
								  <button type="button" class="btn btn-danger dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
								    <span class="caret"></span>
								    <span class="sr-only">Toggle Dropdown</span>
								  </button>
								  <ul id="menu_cate_items_list" class="dropdown-menu">
								  	<!-- Fill Category Items dynamically -->
								  </ul>
								</div>
							
								<!-- Product list of each Category  -->
								<div class="btn-group">
								  <button id="choose_product_caption"  type="button" class="btn btn-danger">Product</button>
								  <button type="button" class="btn btn-danger dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
								    <span class="caret"></span>
								    <span class="sr-only">Toggle Dropdown</span>
								  </button>
								  <ul id="product_items_list" class="dropdown-menu">
								  	<!-- Fill Category Items dynamically -->
								  </ul>
								</div>

							</div>
							
							<div class="col-lg-2">
								<div>
									<form role="form">
                                       	<fieldset disabled>
                                            <div class="form-group">
                                                <input id="choosed_product_name" class="form-control" type="text" placeholder="Name" disabled>
                                            </div>
                                        </fieldset>
                                    </form>
                                </div>              
                            </div>
							<div class="col-lg-2">
								<div>
									<form role="form">
                                       	<fieldset disabled>
                                            <div class="form-group">
                                                <input id="choosed_product_price" class="form-control" type="text" placeholder="Price" disabled>
                                            </div>
                                        </fieldset>
                                    </form>
                                </div>              
                            </div>                     
                            <div class="col-lg-2">
								<div>
									<form role="form">
                                       	<fieldset disabled>
                                            <div class="form-group">
                                                <input id="transaction_date" class="form-control" type="text" placeholder="Date" disabled>
                                            </div>
                                        </fieldset>
                                    </form>
                                </div>              
                            </div>  
                             <div class="col-lg-2">
								<div>
									<form role="form">
                                       	<fieldset>
                                            <div class="form-group">
                                                <input id="transaction_time" class="form-control" type="text" placeholder="Time">
                                            </div>
                                        </fieldset>
                                    </form>
                                </div>              
                            </div>                             
							</div>
						</div>
                        <div class="panel-footer">
                            <a class="btn btn-block btn-social btn-github" onclick="funcWriteTransactionLog();return false;">
                                <i class="fa fa-plus"></i> Write the sales log with the above detail.
                            </a>
                        </div>
                    </div>
                </div>
            </div>
            
            <!-- /.row -->
            <div class="row">

                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                        Comparison Timebase-Sales Amount with the past time ( Month/X-th Week/Week Of Day ).
                            <span id="past_date" style='float:right'></span>
                        </div>
                        <!-- /.panel-heading -->
                        <div class="panel-body">
                            <div id="hourly-sales-compare-chart"></div>
                        </div>
                        <!-- /.panel-body -->
                    </div>
                    <!-- /.panel -->
                </div>      
            </div>

            
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