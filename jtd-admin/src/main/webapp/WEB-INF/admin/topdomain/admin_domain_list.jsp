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
<title>域名列表</title>
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
					<li class="active">域名列表</li>
				</ul>
			</div>

			<div class="page-content">
				<div class="page-header">
					<a class="btn icon-plus btn-primary" href="${baseurl }admin/domain/edit.action"> 添加域名</a>
				</div>
				<!-- /.page-header -->
				<div class="row">
					<div class="col-xs-12">
							
							<form id="form" class="form-horizontal" action="${baseurl }admin/domain/list.action" method="post">
								<div class="form-group ">
									
									<label class="col-sm-1 control-label">域名：</label>
									<input type="text" name="domain" class="col-sm-3" value="${queryMap.domain }"/>
									
									<label class="col-sm-2 control-label">录入排序规则：</label>
									<select class="col-sm-2" name="manualEntry">
										<option value="">默认</option>
										<option value="0" <c:if test="${queryMap.manualEntry == 0}">selected</c:if>>按系统录入排序</option>
										<option value="1" <c:if test="${queryMap.manualEntry == 1}">selected</c:if>>按手工录入排序</option>
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
													<th>域名</th>
													<th>站点名称</th>
													<th>流量数值</th>
													<th>网站类型</th>
                                                    <th>录入类型</th>
													<th>创建时间</th>
													<th>操作</th>
												</tr>
											</thead>

											<tbody>
												<c:forEach items="${page.list }" var="po">
													<tr>
														<td>${po.id }</td>
														<td>${po.domain }</td>
														<td>${po.siteName }</td>
														<td>${po.flow }</td>
														<td>${po.webSiteType }</td>
                                                        <td>
                                                            <c:choose>
                                                                <c:when test="${po.manualEntry == 0}">
                                                                    系统录入
                                                                </c:when>
                                                                <c:otherwise>
                                                                    手工录入
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </td>
														<td class="hidden-480">
                                                            <fmt:formatDate value="${po.createTime}" pattern="yyyy-MM-dd HH:mm:ss" />
														</td>
														<td>
                                                            <a class="btn btn-xs btn-info" href="${pageContext.request.contextPath}/admin/domain/edit.action?id=${po.id }">编辑 </a>
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