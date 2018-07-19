<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/commonjsp/tag.jsp"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta HTTP-EQUIV="pragma" CONTENT="no-cache">
<meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<meta HTTP-EQUIV="expires" CONTENT="0">
<title>创意列表</title>
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
							href="${pageContext.request.contextPath}">创意管理</a></li>
						<li class="active">创意列表</li>
					</ul>
				</div>

				<div class="page-content">

					<div class="page-header">
						<a class="btn icon-plus btn-primary"
							href="${pageContext.request.contextPath}/admin/channel/addChannel.action">添加渠道</a>
					</div>

					<div class="row">
						<div class="col-xs-12">

							<div class="row">
								<div class="col-xs-12">
									<div class="table-responsive">
										<table id="sample-table-1"
											class="table table-striped table-bordered table-hover">
											<thead>
												<tr>
													<th class="center"><label> <input
															type="checkbox" class="ace" id="check_btn" /> <span
															class="lbl"></span></label></th>
													<th>渠道ID</th>
													<th>渠道名称</th>
													<th>渠道类别体系</th>
													<th>RTB PMP</th>
													<th>广告类型</th>
													<th>渠道logo图片地址</th>
													<th>渠道展示地址</th>
													<th>是否被逻辑删除</th>
													<th>创建时间</th>
													<th>操作</th>
												</tr>
											</thead>

											<tbody>
												<c:forEach items="${page.listMap }" var="po">
													<tr>
														<td><label> <input type="checkbox"
																class="ace" /> <span class="lbl"></span></label></td>
														<td>${po.id }</td>
														<td>${po.channelName }</td>
														<td>${po.catgserial }</td>
														<td>${po.transType }</td>
														<td>${po.adType }</td>
														<td>${po.logo }</td>
														<td>${po.websiteUrl }</td>
														<td><c:if
																test="${po.deleteStatus eq 0 || po.deleteStatus eq null}">未删除</c:if>
															<c:if test="${po.deleteStatus eq 1}">已删除</c:if></td>
														<td><fmt:formatDate value="${po.createTime}"
																pattern="yyyy-MM-dd HH:mm:ss" /></td>
														<td><a
															href="${pageContext.request.contextPath}/admin/channel/updateChannel.action?id=${po.id}">编辑</a>
															<a href="javascript:;;"
															onclick='deleteChannel("${pageContext.request.contextPath}/admin/channel/deleteChannel.action?id=${po.id}")'>删除</a>
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
<script>
	function deleteChannel(url) {
		if (confirm("确定要删除吗？"))
			window.location.href = url
	}
</script>
</html>