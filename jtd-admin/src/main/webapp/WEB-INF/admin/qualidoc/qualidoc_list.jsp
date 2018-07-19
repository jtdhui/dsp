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
<title>资质列表</title>
<%@ include file="/WEB-INF/commonjsp/admin_common_css.jsp"%>
<%@ include file="/WEB-INF/commonjsp/admin_common_js.jsp"%>
</head>
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
					<li><i class="icon-home home-icon"></i> <a href="${pageContext.request.contextPath}/admin/qualidoc/list.action">资质管理</a></li>
					<li class="active">资质列表</li>
				</ul>
			</div>

			<div class="page-content">
				<!-- /.page-header -->
				<div class="row">
					<div class="col-xs-12">
						<form id="form" class="form-horizontal" action="${baseurl }admin/qualidoc/list.action" method="post">
							<div class="form-group ">
								
								<label class="col-sm-1 control-label">广告主名称：</label>
								<input type="text" name="partnerName" class="col-sm-2" value="${queryMap.partnerName }"/>
								
								<label class="col-sm-1 control-label">内部审核状态：</label>
								<select class="col-sm-1" name="internalAuditStatus">
									<option value="">全部</option>
									<option value="0" <c:if test="${queryMap.internalAuditStatus == 0}">selected</c:if>>未提交</option>
									<option value="1" <c:if test="${queryMap.internalAuditStatus == 1}">selected</c:if>>待审核</option>
									<option value="2" <c:if test="${queryMap.internalAuditStatus == 2}">selected</c:if>>已通过</option>
									<option value="3" <c:if test="${queryMap.internalAuditStatus == 3}">selected</c:if>>已拒绝</option>
								</select>
								
<!-- 								<label class="col-sm-1 control-label">渠道审核状态：</label> -->
<!-- 								<select class="col-sm-1" name="adxAuditStatus"> -->
<!-- 									<option value="">全部</option> -->
<%-- 									<option value="0" <c:if test="${queryMap.adxAuditStatus == 0}">selected</c:if>>全部未提交</option> --%>
<%-- 									<option value="1" <c:if test="${queryMap.adxAuditStatus == 1}">selected</c:if>>部分等待中</option> --%>
<%-- 									<option value="2" <c:if test="${queryMap.adxAuditStatus == 2}">selected</c:if>>全部等待中</option> --%>
<%-- 									<option value="3" <c:if test="${queryMap.adxAuditStatus == 3}">selected</c:if>>部分已通过</option> --%>
<%-- 									<option value="4" <c:if test="${queryMap.adxAuditStatus == 4}">selected</c:if>>全部已通过</option> --%>
<%-- 									<option value="5" <c:if test="${queryMap.adxAuditStatus == 5}">selected</c:if>>部分已拒绝</option> --%>
<%-- 									<option value="6" <c:if test="${queryMap.adxAuditStatus == 5}">selected</c:if>>全部已拒绝</option> --%>
<!-- 								</select> -->
								<span class="col-sm-1 ">
									<button class="btn btn-sm btn-primary icon-search" style="float:right" type="submit">
										查询
									</button>
								</span>
<!-- 								<span class="col-sm-1 "> -->
<!-- 									<button class="btn btn-sm btn-success icon-download-alt" style="float:right" type="submit"> -->
<!-- 										下载报表 -->
<!-- 									</button> -->
<!-- 								</span> -->
							</div>
						</form>
						<div class="row">
							<div class="col-xs-12">
								<div class="table-responsive">
									<table id="sample-table-1" class="table table-striped table-bordered table-hover">
										<thead>
											<tr>
												<th width="5%">广告主ID</th>
												<th width="10%">广告主名称</th>
												<th width="7%">所属行业</th>
												<th width="5">上级代理</th>
												<th width="7%">资质文件数</th>
												<th width="15%">资质更新时间</th>
												<th width="15%">内部审核时间</th>
												<th width="10%">内部审核状态</th>
												<th width="10%">渠道审核状态</th>
												<th width="10%">操作</th>
											</tr>
										</thead>

										<tbody>
											<c:forEach items="${page.list }" var="map">
												<tr>
													<td>${map.partnerId }</td>
													<td>${map.partnerName }</td>
													<td>${map.categoryName }</td>
													<td>${map.pName }</td>
													<td>${map.qualiDocCount }</td>
													<td>${map.updateTime }</td>
													<td>${map.internalAuditTime }</td>
													<td style="font-weight:bold;">
														<c:if test="${ map.internalAuditStatus == 0 }">
															未提交													
														</c:if>
														<c:if test="${ map.internalAuditStatus == 1 }">
															<span style="color:#FF9933">待审核</span>															
														</c:if>
														<c:if test="${ map.internalAuditStatus == 2 }">
															<span style="color:#009966">已通过</span>															
														</c:if>
														<c:if test="${ map.internalAuditStatus == 3 }">
															<span style="color:#CC0033">已拒绝</span>															
														</c:if>
														
													</td>
													<td style="font-weight:bold;">
<%-- 														<c:if test="${ map.adxAuditStatus == 0 }"> --%>
<!-- 															全部未提交													 -->
<%-- 														</c:if> --%>
<%-- 														<c:if test="${ map.adxAuditStatus == 1 }"> --%>
<!-- 															<span style="color:#FF9933">已部分提交</span>															 -->
<%-- 														</c:if> --%>
<%-- 														<c:if test="${ map.adxAuditStatus == 2 }"> --%>
<!-- 															<span style="color:#FF9933">已全部提交</span>															 -->
<%-- 														</c:if> --%>
<%-- 														<c:if test="${ map.adxAuditStatus == 3 }"> --%>
<!-- 															<span style="color:#FF9933">已部分通过</span>															 -->
<%-- 														</c:if> --%>
<%-- 														<c:if test="${ map.adxAuditStatus == 4 }"> --%>
<!-- 															<span style="color:#009966">已全部通过</span>														 -->
<%-- 														</c:if> --%>
<%-- 														<c:if test="${ map.adxAuditStatus == 5 }"> --%>
<!-- 															<span style="color:#CC0033">已全部拒绝</span>														 -->
<%-- 														</c:if> --%>
															
														<span style="color:#FF9933">待</span>：${map.waitCount}&nbsp;&nbsp;
														<span style="color:#009966">过</span>：${map.successCount}&nbsp;&nbsp;
														<span style="color:#CC0033">拒</span>：${map.failCount}
													</td>
													<td>
														<a class="btn btn-xs btn-info" href="${baseurl}admin/qualidoc/toAudit.action?id=${map.partnerId }">
															进入审核
														</a>
													</td>
												</tr>
											</c:forEach>
											
										</tbody>
									</table>
									<c:if test="${page != null && fn:length(page.list) gt 0}">
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