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
	$(function(){

		loadCharts();
		
		$("#down_btn").click(function(){
			$("#form1").attr("action","${pageContext.request.contextPath}/front/report/getCampTypeExcel.action");
			$("#form1").submit();
			$("#form1").attr("action","${pageContext.request.contextPath}/front/report/campType.action");
		});
	});
	
	function loadCharts(){
		var option = {
			tooltip: {
		        trigger: 'item',
		        formatter: "{a}：<br/>{b}： ￥{c} (占{d}%)"
		    },
		    legend: {
		        x: '200',
                y: '20',
                orient: 'vertical',
                itemGap: 20,
                textStyle: {
                    fontSize: 16
                },
		        data:${typeNameList}
		    },
		    series: [
		        {
		            name:'消耗',
		            type:'pie',
		            selectedMode: 'single',
		            radius: [0, '60%'],
		            hoverAnimation:false,
		            label: {
		                normal: {
		                	show:true,
		                    position: 'inner',
		                    textStyle: {
                                fontSize: 16
                            }
		                }
		            },
		            labelLine: {
		                normal: {
		                    show: false
		                }
		            },
		            data: ${campTypeChartDataList},
		            itemStyle: {
                        emphasis: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    }
		        },
		        {
		            name:'消耗',
		            type:'pie',
		            radius: ['70%', '85%'],
		            hoverAnimation:false,
		            label: {
		                normal: {
		                	show:true,
		                    textStyle: {
                                fontSize: 16
                            }
		                }
		            },
		            data: ${adTypeChartDataList},
		            itemStyle: {
                        emphasis: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    }
		        }
		    ]
		};
		
		var chart = echarts.init(document.getElementById('charts'));
		chart.setOption(option);
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
        
        <form id="form1" action="${pageContext.request.contextPath}/front/report/campType.action" method="post">
            
        <!-- 查询条件，汇总显示，二级链接 -->
        <jsp:include page="/WEB-INF/front/report/report_top.jsp?param=campType"></jsp:include>

        <!-- echarts -->
        <div class="data_table_con">
            <div class=" data_table_div">
                <div id="charts" class="handle-view" style="width:1240px;height:380px;">
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
                        <!-- 固定展示日期 -->
                        <th width="15%" onclick="orderBy('camp_type')">活动类型 <img id="camp_type_desc" class="order" /> </th>
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
	                              <td>${map.camp_type_name }</td>
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
                          	 <c:forEach items="${ columnSettingList }" var="col">
                             	    <c:if test="${col == 'pv_sum' }">
		                          		<td><fmt:formatNumber value="${sumSixMap.pv_sum }" pattern="#,###"/></td>
			                       </c:if>
			                       <c:if test="${col == 'uv_sum' }">
			                          <td><fmt:formatNumber value="${sumSixMap.uv_sum }" pattern="#,###"/></td>
			                       </c:if>
			                       <c:if test="${col == 'click_sum' }">
			                          <td><fmt:formatNumber value="${sumSixMap.click_sum }" pattern="#,###"/></td>
			                       </c:if>
			                       <c:if test="${col == 'click_rate_sum' }">
			                          <c:if test="${not empty sumSixMap.click_rate_sum }">
			                            <td><fmt:formatNumber value="${sumSixMap.click_rate_sum }" type="number" pattern="0.000" />%</td>
			                          </c:if>
			                       </c:if>
			                       <c:if test="${col == 'cpm_sum_yuan' }">
			                          <td><fmt:formatNumber value="${sumSixMap.cpm_sum_yuan }" type="currency"/></td>
			                       </c:if>
			                       <c:if test="${col == 'cpc_sum_yuan' }">
			                          <td><fmt:formatNumber value="${sumSixMap.cpc_sum_yuan }" type="currency"/></td>
			                       </c:if>
			                       <c:if test="${col == 'expend_sum_yuan' }">
			                          <td><i><fmt:formatNumber value="${sumSixMap.expend_sum_yuan }" type="currency"/></i></td>
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