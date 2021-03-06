<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ include file="/WEB-INF/commonjsp/tag.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta HTTP-EQUIV="pragma" CONTENT="no-cache">
    <meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
    <meta HTTP-EQUIV="expires" CONTENT="0">
<title>数据中心</title>
<%@ include file="/WEB-INF/commonjsp/common_css.jsp"%>
<%@ include file="/WEB-INF/commonjsp/common_js.jsp"%>

<link rel="stylesheet" href="${baseurl }dist/less/data.css">

</head>
<script type="text/javascript">

	var seriesData = new Array();
	
	$(function(){

		var obj = {
	            name:'展现量',
	            type:'bar',
	            smooth: true,
	            barWidth:"20%",
	            itemStyle:{normal:{color:"#c23531"}},
	            areaStyle: {normal: {}},  //区域用颜色填充
	            silent:true, //不响应鼠标点击
	            data: ${pvList}
	        };
		seriesData.push(obj);

		loadCharts();
		
		$("#down_btn").click(function(){
			$("#form1").attr("action","${pageContext.request.contextPath}/front/report/getCityExcel.action");
			$("#form1").submit();
			$("#form1").attr("action","${pageContext.request.contextPath}/front/report/city.action");
		});
	});
	
	function selectLegend(){
		seriesData = [] ;
		
		$("input[name=selectLegendCheckbox]:checked").each(function(){
			if($(this).val() == "1"){
				var obj = {
			            name:'展现量',
			            type:'bar',
			            smooth: true,
			            barWidth:"20%",
			            itemStyle:{normal:{color:"#c23531"}},
			            areaStyle: {normal: {}},  //区域用颜色填充
			            data: ${pvList}
			        };
				seriesData.push(obj);
			}
			if($(this).val() == "2"){
				var obj = {
			            name:'点击量',
			            type:'bar',
			            smooth: true,
			            barWidth:"20%",
			            itemStyle:{normal:{color:"#d48265"}},
			            areaStyle: {normal: {}},
			            data: ${clickList}
			        };
				seriesData.push(obj);
			}
			if($(this).val() == "3"){
				var obj = {
			            name:'点击率(%)',
			            type:'bar',
			            smooth: true,
			            barWidth:"20%",
			            itemStyle:{normal:{color:"#bda29a"}},
			            areaStyle: {normal: {}},
			            data: ${clickRateList}
			        };
				seriesData.push(obj);
			}
			if($(this).val() == "4"){
				var obj =  {
			            name:'投放CPM单价(￥)',
			            type:'bar',
			            smooth: true,
			            barWidth:"20%",
			            itemStyle:{normal:{color:"#53a67a"}},
			            areaStyle: {normal: {}},
			            data: ${cpmList}
			        };
				seriesData.push(obj);
			}
			if($(this).val() == "5"){
				var obj =  {
			            name:'投放CPC单价(￥)',
			            type:'bar',
			            smooth: true,
			            barWidth:"20%",
			            itemStyle:{normal:{color:"#91c7ae"}},
			            areaStyle: {normal: {}},
			            data: ${cpcList}
			        };
				seriesData.push(obj);
			}
			if($(this).val() == "6"){
				var obj =  {
			            name:'消耗(￥)',
			            type:'bar',
			            smooth: true,
			            barWidth:"20%",
			            itemStyle:{normal:{color:"#3d85c6"}},
			            areaStyle: {normal: {}},
			            data: ${expendList}
			        };
				seriesData.push(obj);
			}
		});
		
		loadCharts();
	}
	
	function loadCharts(){
		var option = {
			tooltip : {
		        trigger: 'axis',
		        axisPointer : {            
		            type : 'shadow'
		        }
		    },
		    grid: {
		        left: '5%',
		        right: '5%',
		        bottom: '3%',
		        containLabel: true
		    },
		    yAxis: {
		        type: 'category',
		        data: ${axisLabelList},
		        axisTick: {
	                alignWithLabel: true   //柱子对准省份名称
	            },
	            inverse:true,  //反向显示(北京在最上面)
	            axisLabel:{
	            	textStyle:{
	            		color:"#1da7d8",
	            		fontWeight:"lighter",
	            		 fontSize: 16
	            	}
	            }
	            <c:if test="${ not empty isProvince}">
	            ,triggerEvent:true, //省份名称支持点击
	            </c:if>
		    },
		    xAxis: {
		        type: 'value'
		    },
		    series: seriesData
		};
		
		var chart = echarts.init(document.getElementById('charts'));
		chart.setOption(option);
		chart.on('click', clickChartProvince);
	}
	
	function clickChartProvince(param) {
	    if (param.componentType == 'yAxis') {
	        document.location.href = "${pageContext.request.contextPath}/front/report/city.action?provinceName=" + encodeURI(encodeURI(param.value)) ;
	    }
	}
	
	function clickPageProvince(provinceName) {
		 document.location.href = "${pageContext.request.contextPath}/front/report/city.action?provinceName=" + encodeURI(encodeURI(provinceName)) ;
	}
</script>
<body>
	<!--header 导航 -->
	<jsp:include page="/WEB-INF/front/header.jsp?param=report"></jsp:include>
	
	<div class="wrapper">
        <!-- 当前位置 -->
        <div class="current-position row">
            <div class="col-sm-6">
                <ol class="breadcrumb">
                    <li class="breadcrumb-list">
                        <i class="icons-current"></i>当前位置
                    </li>
                    <li class="breadcrumb-list">
                        <a class="nav-breadcrumb" href="home.html">首页</a>
                        <i class="fa fa-angle-right"></i>
                    </li>
                    <li class="breadcrumb-list">
                        <a class="nav-breadcrumb active">数据中心</a>
                    </li>
                </ol>
            </div>
            
            <!-- 选择广告主 -->
       		<jsp:include page="/WEB-INF/front/selectPartner.jsp?fromPage=report"></jsp:include>
       		
        </div>
        
        <form id="form1" action="${pageContext.request.contextPath}/front/report/city.action" method="post">
            
        <!-- 查询条件，汇总显示，二级链接 -->
        <jsp:include page="/WEB-INF/front/report/report_top.jsp?param=city"></jsp:include>
		
		<!-- 如果是按城市显示需要传provinceId -->
        <input type="hidden" name="provinceName" value="${params.provinceName }"/>
		
        <!-- echarts -->
        <div class="data_table_con">
            <div class="data_table_div">
            	
            	<c:if test="${ empty isProvince}">
			          <div class="breadcrumb">
			            ${params.provinceName }&nbsp;&nbsp;<a href="${pageContext.request.contextPath}/front/report/city.action" style="color: #1da7d8;">返回省份统计</a>
			          </div>
			          </c:if>
			          <c:if test="${ not empty isProvince}">
			          <div class="breadcrumb">
			           	 点击省份查看详情
			          </div>
		        </c:if>
            	
                <div class="radio-wrap">
                    <span class="radio-style active">
                        <input id="r1" type="radio" value="1" name="selectLegendCheckbox" onclick="selectLegend()"
                               checked>
                    </span><label for="r1">展现量</label>
                    <span class="radio-style">
                        <input id="r2" type="radio" value="2" name="selectLegendCheckbox" onclick="selectLegend()">
                    </span><label for="r2">点击量</label>
                    <span class="radio-style">
                        <input id="r3" type="radio" value="3" name="selectLegendCheckbox" onclick="selectLegend()">
                    </span><label for="r3">点击率(%)</label>
                    <span class="radio-style">
                        <input id="r4" type="radio" value="4" name="selectLegendCheckbox" onclick="selectLegend()">
                    </span><label for="r4">投放CPM单价(￥)</label>
                    <span class="radio-style">
                        <input id="r5" type="radio" value="5" name="selectLegendCheckbox" onclick="selectLegend()">
                    </span><label for="r5">投放CPC单价(￥)</label>
                    <span class="radio-style">
                        <input id="r6" type="radio" value="6" name="selectLegendCheckbox" onclick="selectLegend()">
                    </span><label for="r6">消耗(￥)</label>
                </div>
                <div id="charts" style="width:1240px;height:${axisLabelListSize * 100}px;">
                	<!-- 显示图表 -->
                </div>
            </div>
        </div>

        <!-- todo：表格  详细数据-->
        <div class="index_preview">
            <div class="index_chart_day clearfix">
                <div class="chart_day_lt">
                    <i class="icons-charts clearfix"></i>
                   	 详细数据
                </div>
                <div class="chart_day_rt clearfix">
                    <span class="btn btn-small btn-purple" id="down_btn">下载报表</span>
                </div>
            </div>
            <div class="index_preview_table">
                <table class="footable table table-stripped" data-filter=#filter>
                    <thead>
                    <tr>
                        <!-- 固定展示列 -->
                        <th width="15%" onclick="orderBy('city_id')">地域 <img id="city_id_desc" class="order"/></th>
                        <!-- 展示自定义列 -->
                        <c:forEach items="${ columnSettingList }" var="col">
	                       <c:if test="${col == 'pv_sum' }">
	                          <th onclick="orderBy('pv_sum')">展现量 <img id="pv_sum_desc" class="order"></th>
	                       </c:if>
	                       <c:if test="${col == 'uv_sum' }">
	                          <th onclick="orderBy('uv_sum')">UV <img id="uv_sum_desc" class="order" ></th>
	                       </c:if>
	                       <c:if test="${col == 'click_sum' }">
	                          <th onclick="orderBy('click_sum')">点击量 <img id="click_sum_desc" class="order"></th>
	                       </c:if>
	                       <c:if test="${col == 'click_rate_sum' }">
	                          <th onclick="orderBy('click_rate_sum')">点击率 <img id="click_rate_sum_desc" class="order"></th>
	                       </c:if>
	                       <c:if test="${col == 'cpm_sum_yuan' }">
	                          <th onclick="orderBy('cpm_sum_yuan')">投放cpm <img id="cpm_sum_yuan_desc" class="order"></th>
	                       </c:if>
	                       <c:if test="${col == 'cpc_sum_yuan' }">
	                          <th onclick="orderBy('cpc_sum_yuan')">投放cpc <img id="cpc_sum_yuan_desc" class="order"></th>
	                       </c:if>
	                       <c:if test="${col == 'expend_sum_yuan' }">
	                          <th onclick="orderBy('expend_sum_yuan')">投放花费 <img id="expend_sum_yuan_desc" class="order"></th>
	                       </c:if>
	                    </c:forEach>
                    </tr>
                    </thead>
                    <tbody>
                    	<!-- 记录行 -->
                    	  <c:forEach items="${ page.list }" var="map" varStatus="vs">
	                           <tr>
	                           	  <!-- 固定展示列 -->
	                              <!-- 按省份显示需要加上进入省份统计的链接 -->
		                            <c:if test="${ not empty isProvince && not empty map.city_id}">
		                              <td><a href="javascript:void(0)" onclick="clickPageProvince('${map.city_name}')">${map.city_name }</a></td>
		                            </c:if>
		                            <!-- 未知省份-->
		                            <c:if test="${ not empty isProvince && empty map.city_id}">
		                              <td>${map.city_name }</td>
		                            </c:if>
		                            <!-- 按城市显示不需要加链接 -->
		                            <c:if test="${ empty isProvince}">
		                              <td>${map.city_name }</td>
		                            </c:if>
	                              
	                              <!-- 展示自定义列 -->
	                              <c:forEach items="${ columnSettingList }" var="col">
	                                  <c:if test="${col == 'pv_sum' }">
		                              <td>${map.pv_sum }
		                              </td>
		                           </c:if>
		                           <c:if test="${col == 'uv_sum' }">
		                              <td>${map.uv_sum }</td>
		                           </c:if>
		                           <c:if test="${col == 'click_sum' }">
		                              <td>${map.click_sum }</td>
		                           </c:if>
		                           <c:if test="${col == 'click_rate_sum' }">
		                              <td><fmt:formatNumber value="${map.click_rate_sum }" type="number" pattern="0.000" />%</td>
		                           </c:if>
		                           <c:if test="${col == 'cpm_sum_yuan' }">
		                              <td><fmt:formatNumber value="${map.cpm_sum_yuan }" type="currency" /></td>
		                           </c:if>
		                           <c:if test="${col == 'cpc_sum_yuan' }">
		                              <td><fmt:formatNumber value="${map.cpc_sum_yuan }" type="currency" /></td>
		                           </c:if>
		                           <c:if test="${col == 'expend_sum_yuan' }">
		                              <td><i><fmt:formatNumber value="${map.expend_sum_yuan }" type="currency" /></i></td>
		                           </c:if>
		                         </c:forEach>
	                           </tr>
	                      </c:forEach>
	                      <!-- 记录行/ -->
                          <!-- 最后总计行 -->
                          <tr>
                        	<!-- 固定展示列 -->
                          	 <td>总计</td>
                          	 <!-- 展示自定义列 -->
                          	 <c:if test="${ not empty isProvince}">
                          	 	<c:set var="sumDataMap" value="${sumSixMap}"></c:set>
                          	 </c:if>
                          	 <c:if test="${ empty isProvince}">
                          	 	<c:set var="sumDataMap" value="${areaAllSumData}"></c:set>
                          	 </c:if>
                          	 <c:forEach items="${ columnSettingList }" var="col">
	                              <c:if test="${col == 'pv_sum' }">
		                            <td><fmt:formatNumber value="${sumDataMap.pv_sum }" pattern="#,###"/></td>
		                         </c:if>
		                         <c:if test="${col == 'uv_sum' }">
		                            <td><fmt:formatNumber value="${sumDataMap.uv_sum }" pattern="#,###"/></td>
		                         </c:if>
		                         <c:if test="${col == 'click_sum' }">
		                            <td><fmt:formatNumber value="${sumDataMap.click_sum }" pattern="#,###"/></td>
		                         </c:if>
		                         <c:if test="${col == 'click_rate_sum' }">
		                            <c:if test="${not empty sumDataMap.click_rate_sum }">
		                              <td><fmt:formatNumber value="${sumDataMap.click_rate_sum }" type="number" pattern="0.000" />%</td>
		                            </c:if>
		                         </c:if>
		                         <c:if test="${col == 'cpm_sum_yuan' }">
		                            <td><fmt:formatNumber value="${sumDataMap.cpm_sum_yuan }" type="currency"/></td>
		                         </c:if>
		                         <c:if test="${col == 'cpc_sum_yuan' }">
		                            <td><fmt:formatNumber value="${sumDataMap.cpc_sum_yuan }" type="currency"/></td>
		                         </c:if>
		                         <c:if test="${col == 'expend_sum_yuan' }">
		                            <td><i><fmt:formatNumber value="${sumDataMap.expend_sum_yuan }" type="currency"/></i></td>
		                         </c:if>
	                        </c:forEach>
                        </tr>
                        <!-- 最后总计行 /-->
                    </tbody>
                </table>
            </div>
        </div>

        <!-- todo:指标设置 -->
        <div class="clearfix index-setting">
            <span data-toggle="modal" data-target="#indexSetting" class="btn btn-small btn-main">指标设置</span>
        </div>
        
        <!--分页-->
        ${page.frontPageHtml }
        <!--分页/-->
        
        </form>

    </div>
	
</body>
</html>