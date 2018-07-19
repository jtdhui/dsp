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
<title>菜单列表</title>
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
					<li><i class="icon-home home-icon"></i> <a
						href="${pageContext.request.contextPath}">系统管理</a></li>
					<li class="active">菜单列表</li>
				</ul>
			</div>

			<div class="page-content">
				<div class="page-header">
					<a class="btn icon-plus btn-primary" href="${baseurl }admin/menu/add.action"> 添加菜单</a>
				</div>
				<!-- /.page-header -->
				<div class="row">
					<div class="col-xs-12">

						<div class="row">
							<div class="col-xs-12">
								<div class="table-responsive">
									<table id="sample-table-1"
										class="table table-striped table-bordered table-hover">
										<thead>
											<tr>
												<th>id</th>
												<th class="hidden-480">菜单名称</th>
												<th>URL</th>
												<th>创建时间</th>
												<th>备注</th>
												<%--<th>状态</th>--%>
												<th>操作</th>
											</tr>
										</thead>

										<tbody>

											<c:forEach items="${poList }" var="po">
												<tr>
													<td>${po.id }</td>
													<td class="hidden-480">${po.name }</td>
													<td>${po.url }</td>
													<td><fmt:formatDate
															value="${po.createTime}" pattern="yyyy-MM-dd HH:mm:ss" />
													</td>
													<td>${po.remark }</td>
													<%--<td>--%>
															<%--<c:choose>--%>
																<%--<c:when test="${po.available == 1 }">--%>
																	<%--<a class="btn btn-xs btn-danger"--%>
																	   <%--href="${pageContext.request.contextPath}/admin/menu/changeStatus.action?id=${po.id }&available=0">--%>
																		<%--停用 </a>--%>
																<%--</c:when>--%>
																<%--<c:otherwise>--%>
																	<%--<a class="btn btn-xs btn-success"--%>
																	   <%--href="${pageContext.request.contextPath}/admin/menu/changeStatus.action?id=${po.id }&available=1">--%>
																		<%--启用 </a>--%>
																<%--</c:otherwise>--%>
															<%--</c:choose>--%>
													<%--</td>--%>
													<td>
															<a class="btn btn-xs btn-info"
																href="${pageContext.request.contextPath}/admin/menu/edit.action?id=${po.id }">
																<i class="icon-edit bigger-120"></i>
															</a>
													</td>
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
	</div>

</body>
</html>