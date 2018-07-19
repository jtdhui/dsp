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
<title>广告主列表</title>
<%@ include file="/WEB-INF/commonjsp/admin_common_css.jsp"%>
<%@ include file="/WEB-INF/commonjsp/admin_common_js.jsp"%>
</head>

<script type="text/javascript">
	$(function(){
		
		//开始日期控件
		laydate({
		  elem: '#startDate',
		  format: 'YYYY-MM-DD', 
		  max: laydate.now() //最大日期
		});
		//结束日期控件
		laydate({
		  elem: '#endDate',
		  format: 'YYYY-MM-DD', 
		  max: laydate.now() //最大日期
		});
		
	});
	
	function exportExcel(){
		
		$("#form1").attr("action","${pageContext.request.contextPath}/admin/operate/downloadOperatorReportExcel.action");
		$("#form1").submit();
		$("#form1").attr("action","${pageContext.request.contextPath}/admin/operate/operatorReport.action");
	}
</script>

<body class="skin-2">
	<!--header 导航，搜索 -->
	<jsp:include page="/WEB-INF/admin/admin_header.jsp"></jsp:include>

	<div class="main-container">
		<a class="menu-toggler" id="menu-toggler" href="#"> <span
			class="menu-text"></span>
		</a>

		<jsp:include page="/WEB-INF/admin/admin_menu.jsp"></jsp:include>
		<div class="main-content">
			
			<div class="main-container-inner">
			
			<div class="breadcrumbs" id="breadcrumbs">
				<ul class="breadcrumb">
					<li><i class="icon-home home-icon"></i> <a
						href="${pageContext.request.contextPath}">系统管理</a></li>
					<li class="active">运营人员报表</li>
				</ul>
			</div>

			<div class="page-content">
				<!-- /.page-header -->
				<div class="row">
					<div class="col-xs-12">
						<form id="form1" class="form-horizontal" action="${baseurl }admin/operate/operatorReport.action" method="post">
							<div class="form-group ">
								
								<label class="col-sm-1 control-label">运营人员名称：</label>
								<input type="text" name="userName" class="col-sm-1" value="${queryMap.userName }"/>
								
								<label class="col-sm-1 control-label">统计周期：</label>
								<input type="text" name="startDate" id="startDate" class="col-sm-1" value="${queryMap.startDate }"/>
								<label class="col-sm-1" style="width:10px;">-</label>
								<input type="text" name="endDate" id="endDate" class="col-sm-1" value="${queryMap.endDate }"/>
								
								<span class="col-sm-1 ">
									<button class="btn btn-sm btn-primary icon-search" style="float:right" type="submit">
										查询
									</button>
								</span>
								<span class="col-sm-1">
									<button class="btn btn-sm btn-success icon-download" style="float:right" type="button" onclick="exportExcel()">
										下载报表
									</button>
								</span>
							</div>
						</form>
						<div class="row">
							<div class="col-xs-12">
								<div class="table-responsive">
									<table id="sample-table-1" class="table table-striped table-bordered table-hover">
										<thead>
											<tr>
												<th width="3%">运营人员ID</th>
												<th width="8%">运营人员名称</th>
												<th width="4%">广告主数</th>
												<th width="5%">PV</th>
												<th width="5%">UV</th>
												<th width="5%">点击数</th>
												<th width="5%">点击率</th>
												<th width="5%">CPM单价成本</th>
												<th width="5%">CPM单价消耗 </th>
												<th width="5%">CPC单价成本</th>
												<th width="5%">CPC单价消耗</th>
												<th width="5%">消耗</th>
												<th width="5%">成本</th>
												<th width="10%"></th>
											</tr>
										</thead>

										<tbody>

											<c:forEach items="${ list }" var="map">
												<tr>
													<td>${map.operator_id }</td>
													<td>${map.operator_name }</td>
													<td>${map.partner_cnt }</td>
													<td><fmt:formatNumber value="${map.pv_sum }" type="number" pattern="#,###"/></td>
													<td><fmt:formatNumber value="${map.uv_sum }" type="number" pattern="#,###"/></td>
													<td><fmt:formatNumber value="${map.click_sum }" type="number" pattern="#,###"/></td>
													<td><fmt:formatNumber value="${map.click_rate_sum }" type="number" pattern="0.00" /></td>
													<td><fmt:formatNumber value="${map.cpm_cost_sum_yuan }" type="currency" /></td>
													<td><fmt:formatNumber value="${map.cpm_expend_sum_yuan }" type="currency" /></td>
													<td><fmt:formatNumber value="${map.cpc_cost_sum_yuan }" type="currency"/></td>
													<td><fmt:formatNumber value="${map.cpc_expend_sum_yuan }" type="currency" /></td>
													<td><fmt:formatNumber value="${map.expend_sum_yuan }" type="currency" /></td>
													<td><fmt:formatNumber value="${map.cost_sum_yuan }" type="currency" /></td>
													<td>
														<a class="btn btn-xs btn-info"
															href="${pageContext.request.contextPath}/admin/operate/partnerReport.action?operatorId=${map.operator_id }&operatorName=${map.operator_name }&startDate=${queryMap.startDate }&endDate=${queryMap.endDate }">
															查看
														</a>
													</td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
									<c:if test="${page != null && fn:length(page.listMap) gt 0}">
										${page.pageHtml}
									</c:if>
								</div>
								<!-- /.table-responsive -->
							</div>
							<!-- /span -->
						</div>
						<!-- /row -->
					</div>
				</div>

			</div>
			<!-- /.page-content -->
			</div>
		</div>
	</div>

</body>
</html>