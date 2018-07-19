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
	
	$("input[name=legend][value=${legend}]").attr("checked",true);
	
	selectLegend();
	
	loadCharts();
	
	$("#down_btn").click(function(){
		$("#form1").attr("action","${pageContext.request.contextPath}/front/report/getHourExcel.action");
		$("#form1").submit();
		$("#form1").attr("action","${pageContext.request.contextPath}/front/report/hour.action");
	});
	
});


function selectLegend(){
	seriesData = [] ;
	
	$("input[name=legend]:checked").each(function(){
		if($(this).val() == "1"){
			var obj = {
		            name:'展现量',
		            type:'line',
		            smooth: true,
		            itemStyle:{normal:{color:"#c23531"}},
		            data: ${pvList}
		        };
			seriesData.push(obj);
		}
		if($(this).val() == "2"){
			var obj = {
		            name:'点击量',
		            type:'line',
		            smooth: true,
		            itemStyle:{normal:{color:"#d48265"}},
		            data: ${clickList}
		        };
			seriesData.push(obj);
		}
		if($(this).val() == "3"){
			var obj = {
		            name:'点击率(%)',
		            type:'line',
		            smooth: true,
		            itemStyle:{normal:{color:"#bda29a"}},
		            data: ${clickRateList}
		        };
			seriesData.push(obj);
		}
		if($(this).val() == "4"){
			var obj =  {
		            name:'投放CPM单价(￥)',
		            type:'line',
		            smooth: true,
		            itemStyle:{normal:{color:"#53a67a"}},
		            data: ${cpmList}
		        };
			seriesData.push(obj);
		}
		if($(this).val() == "5"){
			var obj =  {
		            name:'投放CPC单价(￥)',
		            type:'line',
		            smooth: true,
		            itemStyle:{normal:{color:"#91c7ae"}},
		            data: ${cpcList}
		        };
			seriesData.push(obj);
		}
		if($(this).val() == "6"){
			var obj =  {
		            name:'消耗(￥)',
		            type:'line',
		            smooth: true,
		            itemStyle:{normal:{color:"#3d85c6"}},
		            data: ${expendList}
		        };
			seriesData.push(obj);
		}
	});
	
	loadCharts();
	
}

function loadCharts(){
	var option = {
	    title: {
	        text: ''
	    },
	    tooltip: {
	        trigger: 'axis'
	    },
	    xAxis: {
	        type: 'category',
	        boundaryGap: true,
	        axisTick: {
                alignWithLabel: true,
                interval:0
            },
	        data: ${axisLabelList}
	    },
	    yAxis: {
	        type: 'value'
	    },
	    series: seriesData
	};
	
	var chart = echarts.init(document.getElementById('charts'));
	chart.setOption(option);
}

function showSum(){
	$("#sumOrAvg").val("sum");
	$("form").submit();
}

function showAvg(){
	$("#sumOrAvg").val("avg");
	$("form").submit();
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
        
        <form id="form1" action="${pageContext.request.contextPath}/front/report/hour.action" method="post">
        
        <!-- 查询条件，汇总显示，二级链接 -->
        <jsp:include page="/WEB-INF/front/report/report_top.jsp?param=hour"></jsp:include>
		
		<!-- 当前是汇总还是平均 -->
		<input type="hidden" name="sumOrAvg" id="sumOrAvg" value="${params.sumOrAvg }"/>
		
        <!-- echarts -->
        <div class="data_table_con">
            <div class="data_table_div">
            	<div class="breadcrumb">
		          <c:if test="${params.sumOrAvg == 'sum' }">
		            	汇总显示
		            &nbsp;&nbsp;
		            <a href="javascript:void(0)" onclick="showAvg()" style="color: #1da7d8;">平均显示</a>
		          </c:if>
		          <c:if test="${params.sumOrAvg == 'avg' }">
		            <a href="javascript:void(0)" onclick="showSum()" style="color: #1da7d8;">汇总显示</a>
		            &nbsp;&nbsp;
		            	平均显示
		          </c:if>
		        </div>
                <div class="radio-wrap">
                    <span class="radio-style <c:if test="${legend == 1 }">active</c:if>">
                        <input id="r1" type="radio" value="1" name="legend" onclick="selectLegend()" <c:if test="${legend == 1 }">checked</c:if>>
                    </span>
                    <label for="r1">展现量</label>
                    <span class="radio-style <c:if test="${legend == 2 }">active</c:if>">
                        <input id="r2" type="radio" value="2" name="legend" onclick="selectLegend()" <c:if test="${legend == 2 }">checked</c:if>>
                    </span>
                    <label for="r2">点击量</label>
                    <span class="radio-style <c:if test="${legend == 3 }">active</c:if>">
                        <input id="r3" type="radio" value="3" name="legend" onclick="selectLegend()" <c:if test="${legend == 3 }">checked</c:if>>
                    </span>
                    <label for="r3">点击率(%)</label>
                    <span class="radio-style <c:if test="${legend == 4 }">active</c:if>">
                        <input id="r4" type="radio" value="4" name="legend" onclick="selectLegend()" <c:if test="${legend == 4 }">checked</c:if>>
                    </span>
                    <label for="r4">投放CPM单价(￥)</label>
                    <span class="radio-style <c:if test="${legend == 5 }">active</c:if>">
                        <input id="r5" type="radio" value="5" name="legend" onclick="selectLegend()" <c:if test="${legend == 5 }">checked</c:if>>
                    </span>
                    <label for="r5">投放CPC单价(￥)</label>
                    <span class="radio-style <c:if test="${legend == 6 }">active</c:if>">
                        <input id="r6" type="radio" value="6" name="legend" onclick="selectLegend()" <c:if test="${legend == 6 }">checked</c:if>>
                    </span>
                    <label for="r6">消耗(￥)</label>
                </div>

                <div id="charts" style="width:1240px;height:280px;">
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
	                        <th width="10%" onclick="orderBy('hour')">小时<img id="hour_desc" class="order"/></th>
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
                    	  <c:forEach items="${ pageList }" var="map" varStatus="vs">
	                           <tr>
	                           	  <!-- 固定展示列 -->
	                              <td>${map.hour }</td>
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
                          	 <c:if test="${params.sumOrAvg == 'sum' }">
                          	 	<c:set var="type" value="总计"></c:set>
                          	 	<c:set var="dataMap" value="${sumSixMap }"></c:set>
                          	 </c:if>
                          	 <c:if test="${params.sumOrAvg == 'avg' }">	
                          	 	<c:set var="type" value="总平均"></c:set>
                          	 	<c:set var="dataMap" value="${hourAllSumData }"></c:set>
                          	 </c:if>
                        	<!-- 固定展示列 -->
                          	 <td>${type }</td>
                          	 <!-- 展示自定义列 -->
                          	 <c:forEach items="${ columnSettingList }" var="col">
                             	    <c:if test="${col == 'pv_sum' }">
		                          		<td><fmt:formatNumber value="${dataMap.pv_sum }" pattern="#,###"/></td>
			                       </c:if>
			                       <c:if test="${col == 'uv_sum' }">
			                          <td><fmt:formatNumber value="${dataMap.uv_sum }" pattern="#,###"/></td>
			                       </c:if>
			                       <c:if test="${col == 'click_sum' }">
			                          <td><fmt:formatNumber value="${dataMap.click_sum }" pattern="#,###"/></td>
			                       </c:if>
			                       <c:if test="${col == 'click_rate_sum' }">
			                          <c:if test="${not empty dataMap.click_rate_sum }">
			                            <td><fmt:formatNumber value="${dataMap.click_rate_sum }" type="number" pattern="0.000" />%</td>
			                          </c:if>
			                       </c:if>
			                       <c:if test="${col == 'cpm_sum_yuan' }">
			                          <td><fmt:formatNumber value="${dataMap.cpm_sum_yuan }" type="currency"/></td>
			                       </c:if>
			                       <c:if test="${col == 'cpc_sum_yuan' }">
			                          <td><fmt:formatNumber value="${dataMap.cpc_sum_yuan }" type="currency"/></td>
			                       </c:if>
			                       <c:if test="${col == 'expend_sum_yuan' }">
			                          <td><i><fmt:formatNumber value="${dataMap.expend_sum_yuan }" type="currency"/></i></td>
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
        
        <!--小时报表无分页-->
        
        </form>

    </div>
	
</body>
</html>