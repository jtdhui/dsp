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
<title>前台首页</title>
<%@ include file="/WEB-INF/commonjsp/common_css.jsp"%>
<%@ include file="/WEB-INF/commonjsp/common_js.jsp"%>

<link rel="stylesheet" href="${baseurl}dist/less/home.css">
<script type="text/javascript" src="${baseurl}dist/js/header.js"></script>

<SCRIPT type="text/javascript">

	$(function(){
		laydate({
			elem: '#date_select_btn',
			format: 'YYYYMMDD',
			max: laydate.now(-1),
			festival: true, //显示节日
			choose: function(datas){ //选择日期完毕的回调
				$("#formId").attr("action", "home.action?otherDay="+datas);
				$("#formId").submit();
			}
		});

	 	var otherList = ${otherList};
	 	var todayList = ${todayList};
	 	var otherDay = ${otherDay};
	 	var today = ${today};
		option = {
		    title: {
		        text: ''
		    },
		    tooltip: {
		        trigger: 'axis'
		    },
		    legend: {
		        data:['今天','今天以前的某一天']
		    },
		    toolbox: {
		    },
			xAxis: {
				type: 'category',
				boundaryGap: false,
				data: ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23','']
			},
		    yAxis: {
		        type: 'value'
		    },
		    series: [
		        {
		            name: '展现量',
		            type:'line',
		            smooth: true,
		            itemStyle:{
		            	color:'#f7f7f7',
		            	normal:{
		            		color:'#cccccc',
		            	}
		            },
		            areaStyle: {normal: {}},
		            data: otherList
		        },
		        {
		            name: '展现量',
		            type:'line',
		            smooth: true,
		            itemStyle:{
		            	normal:{
		            		color:'#81c045',
		            		lineStyle:{
		            			color:'green',
		            			width:2
		            		}
		            	}
		            },
		            areaStyle: {normal: {}},
		            data: todayList
		        }
		    ]
		};

		var chart = echarts.init(document.getElementById('main'));
		chart.setOption(option);

        <%--$("#name_search_btn").click(function () {--%>
            <%--var partnerName = $("#pName").val();--%>
            <%--location.href =  "${baseurl }front/home.action?partnerName="+partnerName;--%>
        <%--});--%>
	});
</SCRIPT>

</head>
<body>
	<!--header 导航 -->
	<jsp:include page="/WEB-INF/front/header.jsp?param=home"></jsp:include>
	
	<div class="wrapper">
        <!-- todo: left -->
        <div class="index_lt">
            <h2>投放概况</h2>
            <div class="index_lt_con">
                <p>今日花费：￥
                	<c:choose>
                        <c:when test="${ empty pd.expend or pd.expend ==0 }">0.00</c:when>
                        <c:otherwise><fmt:formatNumber value="${pd.expend/100}" pattern="0.00"/></c:otherwise>
                    </c:choose>元
                </p>
                <p>账户余额：￥
                <c:choose>
                        <c:when test="${ empty activeUser.favPartner.accBalance or activeUser.favPartner.accBalance==0}">0.00</c:when>
                        <c:otherwise><fmt:formatNumber value="${activeUser.favPartner.accBalance/100}" pattern="0.00"/></c:otherwise>
                    </c:choose>元
                </p>
                <p>预计消费天数：<c:choose>
                        <c:when test="${ empty pd.expend or pd.expend ==0 }">0</c:when>
                        <c:otherwise><fmt:formatNumber value="${activeUser.favPartner.accBalance/pd.expend}" type="number"/></c:otherwise>
                    </c:choose>天
                </p>
                <p>在投活动数量：${camp_online_count}个</p>
            </div>

            <h2>联系我们</h2>
            <div class="index_lt_con">
                <p>设计支持：400-100-300 </p>
                <p>技术支持：400-100-500 </p>
            </div>

            <h2>新手上路</h2>
            <div class="index_lt_nwbie">
                <ul>
                    <li><a href="${baseurl}help/index.jsp?q=1">关于JTD</a></li>
					<li><a href="${baseurl}help/index.jsp?q=5">什么是DSP?</a></li>
					<li><a href="${baseurl}help/index.jsp?q=12">新米迪的媒体资源的来源与量级</a></li>
					<li><a href="${baseurl}help/index.jsp?q=15">新米迪客户资质文件提交标准</a></li>
					<li><a href="${baseurl}help/index.jsp?q=20">为什么需要在客户网站上部署代码？</a></li>
                </ul>
            </div>
        </div>

        <!-- todo: right -->
        <div class="index_rt">
            <!-- 当前位置 -->
            <div class="current-position row">
                <div class="col-sm-6">
                    <ol class="breadcrumb">
                        <li class="breadcrumb-list">
                            <i class="icons-current"></i>当前位置
                        </li>
                        <li class="breadcrumb-list">
                            <a class="nav-breadcrumb active">首页</a>
                        </li>
                    </ol>
                </div>
                
                <!-- 切换广告主 -->
                <jsp:include page="/WEB-INF/front/selectPartner.jsp?fromPage=home"></jsp:include>
                
            </div>

			<form method="post" action="${baseurl }front/home.action" id="formId" class="form-inline">
			
            <!-- todo: 图表 -->
            <div class="index_chart">
                <div class="index_chart_day clearfix">
                    <div class="chart_day_rt">
                        <span class="chart_lt"><i></i>今天（截至-1点）</span>
                        <span class="chart_rt"><i></i>对比日</span>
                        <input class="date" id="date_select_btn" name="otherDay" value="${otherDay}" >
                    </div>
                </div>
                <div class="index_chart_con">
                    <div class="index_chart_tit clearfix">
                        <p>展现量
                        	<span>
	                        	<c:choose>
	                                <c:when test="${ pd.pv==0 or empty pd.pv}">0</c:when>
	                                <c:otherwise>${pd.pv}</c:otherwise>
	                            </c:choose>
	                        </span>
                        </p>
                        <p>点击量
                        	<span>
                        		<c:choose>
	                                <c:when test="${ pd.click==0 or empty pd.click}">0</c:when>
	                                <c:otherwise>${pd.click}</c:otherwise>
                            	</c:choose>
                        	</span>
                        </p>
                        <p>点击率
                        	<span>
                        		<c:choose>
                                	<c:when test="${ pd.pv==0 or empty pd.click}"><fmt:formatNumber  minFractionDigits="2"  value="0.00" type="percent"/></c:when>
                                	<c:otherwise><fmt:formatNumber value="${pd.click/pd.pv}" minFractionDigits="2" type="percent"/></c:otherwise>
                            	</c:choose>
                        	</span>
                        </p>
                        <p>投放CPM
                        	<span>
                        		<c:choose>
                                	<c:when test="${ pd.pv==0 or empty pd.click}"><fmt:formatNumber value="0" pattern="0.00"/></c:when>
                                	<c:otherwise><fmt:formatNumber value="${pd.expend/100/pd.pv*1000}" pattern="0.00"/></c:otherwise>
                            	</c:choose>
                        	</span>
                        </p>
                        <p>投放CPC
                        	<span>
                        		<c:choose>
                            		<c:when test="${ pd.click==0 or empty pd.click}"><fmt:formatNumber value="0" pattern="0.00"/></c:when>
                            		<c:otherwise><fmt:formatNumber value="${pd.expend/100/pd.click}" pattern="0.00"/></c:otherwise>
                        		</c:choose>
                        	</span>
                        </p>
                        <p>消耗（元）
                        	<span>
                        		<c:choose>
	                                <c:when test="${ pd.expend==0 or empty pd.expend}">0.00</c:when>
	                                <c:otherwise><fmt:formatNumber value="${pd.expend/100}" pattern="0.00"/></c:otherwise>
	                            </c:choose>
                        	</span>
                        </p>
                    </div>
                    <div class="" style="margin-top:10px;">
                        <div id="main" style="width: 820px;height:280px;"></div>
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
                </div>
                <div class="index_preview_table">
                    <table class="table table-striped">
                        <thead>
                        <tr>
                            <th width="10%">时 段</th>
                            <th width="10%">日 期</th>
                            <th width="10%">展现量</th>
                            <th width="10%">点击量</th>
                            <th width="15%">点击率（%）</th>
                            <th width="15%">投 放cpm</th>
                            <th width="15%">投 放cpc</th>
                            <th width="15%">消 耗（元）</th>
                        </tr>
                        </thead>
                        <tbody>
                        	<c:forEach var="item" items="${page.list}" varStatus="status" step="2">
	                            <c:set var="i" value="${status.index}"></c:set>
	                            <c:if test="${page.list[i].hour==page.list[i+1].hour}">
	                                <tr>
	                                    <td rowspan="2" class="rowspan">${page.list[i].hour}</td>
	                                    <td>${page.list[i].date}</td>
	                                    <td>${page.list[i].pv}</td>
	                                    <td>${page.list[i].click}</td>
	                                    <td>
	                                        <c:choose>
	                                            <c:when test="${ page.list[i].click==0 or empty page.list[i].pv}"><fmt:formatNumber value="0.00"  minFractionDigits="2" type="percent" /></c:when>
	                                            <c:otherwise><fmt:formatNumber value="${page.list[i].click /page.list[i].pv}" minFractionDigits="2"  type="percent" /></c:otherwise>
	                                        </c:choose>
	                                    </td>
	                                    <td>
	                                        <c:choose>
	                                            <c:when test="${ page.list[i].expend==0 or empty page.list[i].pv}"><fmt:formatNumber value="0" pattern="0.00"/></c:when>
	                                            <c:otherwise><fmt:formatNumber value="${page.list[i].expend/100/page.list[i].pv*1000 }" pattern="0.00"/></c:otherwise>
	                                        </c:choose>
	                                    </td>
	                                    <td>
	                                        <c:choose>
	                                            <c:when test="${ page.list[i].expend==0 or empty page.list[i].click}"><fmt:formatNumber value="0" pattern="0.00"/></c:when>
	                                            <c:otherwise><fmt:formatNumber value="${page.list[i].expend/100/page.list[i].click}" pattern="0.00"/></c:otherwise>
	                                        </c:choose>
	                                    </td>
	                                    <td><fmt:formatNumber value="${page.list[i].expend/100}" pattern="0.00"/></td>
	                                </tr>
	                                <tr>
	                                    <td>${page.list[i+1].date}</td>
	                                    <td>${page.list[i+1].pv}</td>
	                                    <td>${page.list[i+1].click}</td>
	                                    <td>
	                                        <c:choose>
	                                            <c:when test="${ page.list[i+1].click==0 or empty page.list[i+1].pv}"><fmt:formatNumber minFractionDigits="2"  value="0.00" type="percent"/></c:when>
	                                            <c:otherwise><fmt:formatNumber value="${page.list[i+1].click /page.list[i+1].pv}" minFractionDigits="2"  type="percent" /></c:otherwise>
	                                        </c:choose>
	                                    </td>
	                                    <td>
	                                        <c:choose>
	                                            <c:when test="${ page.list[i+1].expend==0 or empty page.list[i+1].pv}"><fmt:formatNumber value="0" pattern="0.00"/></c:when>
	                                            <c:otherwise><fmt:formatNumber value="${page.list[i+1].expend/100/page.list[i+1].pv*1000 }" pattern="0.00"/></c:otherwise>
	                                        </c:choose>
	                                    </td>
	                                    <td>
	                                        <c:choose>
	                                            <c:when test="${ page.list[i+1].expend==0 or empty page.list[i+1].click}"><fmt:formatNumber value="0" pattern="0.00"/></c:when>
	                                            <c:otherwise><fmt:formatNumber value="${page.list[i+1].expend/100/page.list[i+1].click}" pattern="0.00"/></c:otherwise>
	                                        </c:choose>
	                                    </td>
	                                    <td><fmt:formatNumber value="${page.list[i+1].expend/100}" pattern="0.00"/></td>
	                                </tr>
	                            </c:if>
	                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>

             <!--分页-->
             <c:if test="${page != null && fn:length(page.list) gt 0}">
                 ${page.frontPageHtml}
             </c:if>
             <!--分页/-->
            
            </form>
            
        </div>

    </div>
</body>
</html>