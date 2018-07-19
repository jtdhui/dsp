<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/commonjsp/tag.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta HTTP-EQUIV="pragma" CONTENT="no-cache">
    <meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
    <meta HTTP-EQUIV="expires" CONTENT="0">
    <title>充值明细</title>
    <link rel="stylesheet" href="${baseurl}css/admin/date.css?d=${activeUser.currentTime}">
    <%@ include file="/WEB-INF/commonjsp/admin_common_css.jsp"%>
    <%@ include file="/WEB-INF/commonjsp/admin_common_js.jsp"%>
</head>

<!-- 选择时间控件 -->
<script src="${baseurl}js/front/date_layer.js"></script>

<script type="text/javascript">
	$(function() {
		$("#stat_cycle").dateLayer("startDate","endDate",'${paraMap.startDate}','${paraMap.endDate}',"camp");
		
    });
	
	function finaceDetailExcel(){
		$("#form1").attr("action","${pageContext.request.contextPath}/admin/finance/downloadFinaceDetailExcelExcel.action");
		$("#form1").submit();
		$("#form1").attr("action","${pageContext.request.contextPath}/admin/finance/finaceDetailList.action");
	}
</script>

<body>
	<!--header 导航，搜索 -->
	<jsp:include page="/WEB-INF/admin/admin_header.jsp"></jsp:include>

	<div class="main-container-inner">
		<a class="menu-toggler" id="menu-toggler" href="#"> <span
			class="menu-text"></span>
		</a>

		<jsp:include page="/WEB-INF/admin/admin_menu.jsp"></jsp:include>
		<div class="main-content">
			<div class="breadcrumbs" id="breadcrumbs">
				<ul class="breadcrumb">
					<li><i class="icon-home home-icon"></i> <a
						href="${pageContext.request.contextPath}">财务管理</a></li>
					<li class="active">充值明细</li>
				</ul>
			</div>

			<div class="page-content">

				<div class="row">
					<div class="col-xs-12">
						<form method="post" action="${baseurl }/admin/finance/finaceDetailList.action" id="form1" class="form-horizontal">
							<div class="form-group">
								<label class="col-sm-1 control-label">广告主名称:</label>
										<input type="text" id="partnerName" name="partnerName" class="col-sm-1"  value="${po.partnerName }">
<!-- 								<select class="col-sm-1" name="partnerId" id="pList"> -->
<%-- 									<c:forEach items="${partners }"  var="po"> --%>
<%-- 										<option value="${po.id }"  <c:if test="${paraMap.partnerId == po.id }">selected</c:if>>${po.partnerName }</option> --%>
<%-- 									</c:forEach> --%>
<!-- 								</select> -->
								<!-- 日期选择 -->
								 <input class="yen_date" value="" id="stat_cycle">
								
                                <span class="col-sm-1 ">
									<button class="btn btn-sm btn-primary icon-search" style="float:right" type="submit">
										查询
									</button>
								</span>
								<span class="col-sm-1">
									<button class="btn btn-sm btn-success icon-download" style="float:right" type="button" onclick="finaceDetailExcel()">
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
											<th width="10%">ID</th>
											<th width="10%">广告主名称</th>
											<th width="15%">充值日期</th>
											<th width="10%">总金额</th>
											<th width="15%">充值明细推广费</th>
											<th width="15%">充值明细赠送</th>
											<th width="15%">充值明细开户费</th>
											<th width="10%">充值明细计服费</th>
										</tr>
										</thead>

										<tbody>
											<c:forEach items="${list }" var="po">
												<tr>
													<td>${po.partner_id}</td>
													<td>${po.partner_name}</td>
													<td><fmt:formatDate value="${po.trade_time}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
													<td><fmt:formatNumber value="${(po.amount+po.open_amount+po.service_amount)/100} " pattern="0.00"/></td>
													<td><fmt:formatNumber value="${po.amount/100}" pattern="0.00"/></td>
													<td><fmt:formatNumber value="${po.pre_gift/100}" pattern="0.00"/></td>
													<td><fmt:formatNumber value="${po.open_amount/100}" pattern="0.00"/></td>
													<td><fmt:formatNumber value="${po.service_amount/100}" pattern="0.00"/></td>
												</tr>
											</c:forEach>
										
										
                                        </tbody>
                                    </table>
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

</body>
</html>