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
<title>用户列表</title>
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
					<li class="active">用户列表</li>
				</ul>
			</div>

			<div class="page-content">
				<div class="page-header">
					<a class="btn icon-plus btn-primary" href="${baseurl }admin/user/add.action"> 添加用户</a>
				</div>
				<!-- /.page-header -->
				<div class="row">
					<div class="col-xs-12">
							
							<form id="form" class="form-horizontal" action="${baseurl }admin/user/list.action" method="post">
								<div class="form-group ">
									
									<label class="col-sm-1 control-label">登录名：</label>
									<input type="text" name="loginName" class="col-sm-1" value="${queryMap.loginName }"/>
									
									<label class="col-sm-1 control-label">用户名：</label>
									<input type="text" name="userName" class="col-sm-1" value="${queryMap.userName }"/>
									
									<label class="col-sm-2 control-label">广告主名称：</label>
									<input type="text" name="partnerName" class="col-sm-1" value="${queryMap.partnerName }"/>
									
									<label class="col-sm-1 control-label">状态：</label>
									<select class="col-sm-1" name="status">
										<option value="">全部</option>
										<option value="0" <c:if test="${queryMap.status == 0}">selected</c:if>>开启</option>
										<option value="1" <c:if test="${queryMap.status == 1}">selected</c:if>>停用</option>
									</select>
									
									<span class="col-sm-1 ">
										<button class="btn btn-sm btn-primary icon-search" style="float:right" type="submit">
											查询
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
													<th>ID</th>
													<th>登陆名</th>
													<th>用户名</th>
													<th>广告主</th>
													<th>角色</th>
													<th>创建时间</th>
													<th>状态</th>
													<th>操作</th>
												</tr>
											</thead>

											<tbody>

												<c:forEach items="${page.list }" var="user">
													<tr>
														<td>${user.id }</td>
														<td>${user.login_name }</td>
														<td>${user.user_name }</td>
														<td>${user.partner_name }</td>
														<td>${user.role_name }</td>
														<td class="hidden-480"><fmt:formatDate
																value="${user.create_time}" pattern="yyyy-MM-dd HH:mm:ss" />
														</td>
														<td>
															<c:if test="${user.status == 0}">开启</c:if>
															<c:if test="${user.status == 1}">停用</c:if>
														</td>
														<td>
															<c:if test="${activeUser.roleId == '1' or activeUser.roleId == '10' or activeUser.userId == user.id or user.partner_id != '1'}">
																<shiro:hasPermission name="user:update">
																	<a class="btn btn-xs btn-info"
																		href="${pageContext.request.contextPath}/admin/user/edit.action?id=${user.id }">
																		编辑 </a>
																</shiro:hasPermission>
																<shiro:hasPermission name="user:status">
																	<c:choose>
																		<c:when test="${user.status== 1 }">
																			<a class="btn btn-xs btn-success"
																				href="${pageContext.request.contextPath}/admin/user/changeStatus.action?id=${user.id }&status=0">
																				启用 </a>
																		</c:when>
																		<c:otherwise>
																			<a class="btn btn-xs btn-danger"
																				href="${pageContext.request.contextPath}/admin/user/changeStatus.action?id=${user.id }&status=1">
																				停用 </a>
																		</c:otherwise>
																	</c:choose>
																</shiro:hasPermission>
																<shiro:hasPermission name="user:password">
																	<a class="btn btn-xs btn-info"
																		href="${pageContext.request.contextPath}/admin/user/changePassword.action?id=${user.id }">
																		修改密码 </a>
																</shiro:hasPermission>
															</c:if>
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