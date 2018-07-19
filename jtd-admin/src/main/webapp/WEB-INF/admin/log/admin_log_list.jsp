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
<title>日志列表</title>
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
					<li class="active">日志列表</li>
				</ul>
			</div>

			<div class="page-content">
				<!-- /.page-header -->
				<div class="row">
					<div class="col-xs-12">
							
							<form id="form" class="form-horizontal" action="${baseurl }admin/log/list.action" method="post">
								<div class="form-group ">
									
									<label class="col-sm-1 control-label">名称：</label>
									<input type="text" name="name" class="col-sm-3" value="${queryMap.name }"/>
									
									<label class="col-sm-2 control-label">日志类型：</label>
									<select class="col-sm-2" name="type">
										<option value="">全部</option>
										<option value="1" <c:if test="${queryMap.type == 1}">selected</c:if>>系统日志</option>
										<option value="2" <c:if test="${queryMap.type == 2}">selected</c:if>>业务日志</option>
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
													<th>名称</th>
													<th>业务ID</th>
													<th>日志类型</th>
													<th>操作类型</th>
                                                    <th>操作人ID</th>
													<th>创建时间</th>
													<th>操作</th>
												</tr>
											</thead>

											<tbody>
												<c:forEach items="${page.list }" var="po">
													<tr>
														<td>${po.id }</td>
														<td>${po.name }</td>
														<td>${po.businessId }</td>
														<td>
                                                            <c:choose>
                                                                <c:when test="${po.type == 1}">系统日志</c:when>
                                                                <c:otherwise>业务日志</c:otherwise>
                                                            </c:choose>
														</td>
														<td>
                                                            <c:choose>
                                                                <c:when test="${po.operateType == 1 }">新增</c:when>
                                                                <c:when test="${po.operateType == 2 }">修改</c:when>
                                                                <c:otherwise>删除</c:otherwise>
                                                            </c:choose>
                                                        </td>
                                                        <td>${po.creatorId }</td>
														<td class="hidden-480">
                                                            <fmt:formatDate value="${po.createTime}" pattern="yyyy-MM-dd HH:mm:ss" />
														</td>
														<td>
                                                            <a class="btn btn-xs btn-info" href="${pageContext.request.contextPath}/admin/log/edit.action?id=${po.id }">查看 </a>
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