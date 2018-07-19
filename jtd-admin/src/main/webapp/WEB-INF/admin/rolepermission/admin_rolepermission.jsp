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
<title>角色权限授权</title>
<%@ include file="/WEB-INF/commonjsp/admin_common_css.jsp"%>
<%@ include file="/WEB-INF/commonjsp/admin_common_js.jsp"%>

<script type="text/javascript">

	function sel(id)
	{
		window.location.href="${pageContext.request.contextPath}/admin/rolepermission/roleSelect.action?id="+id;
	}
	
	$(function(){
		
		//授权
		$('#btn_grant').click(function(){
			
			var sysRoleId = "";
			$("[name='checkbox'][checked]").each(function(){
				sysRoleId = $(this).val();
		 	});
			
			var sysPermissionId = "";
			$("input[name='checkpermission']:checked").each(function(){
				sysPermissionId +=  $(this).val() +",";
			});

			layer.open({
				content:'你确定执行此操作么？',
				time: 0, //不自动关闭
				btn: ['确定', '取消'],
				yes: function(index){
					layer.close(index);
					if(sysPermissionId.length>1) {
						sysPermissionId = sysPermissionId.substring(0, sysPermissionId.length - 1);
						window.location.href="${pageContext.request.contextPath}/admin/rolepermission/grant.action?sysPermissionId="+sysPermissionId+"&sysRoleId="+sysRoleId;
					}
				}
			});

		});
	});
</script>
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
					<li class="active">角色授权</li>
				</ul>
			</div>

			<div class="page-content">
				<div class="page-header">
					<a class="btn btn-primary" id="btn_grant">角色授权</a>
				</div>
				<!-- /.page-header -->
				<div class="row">
					<div class="col-xs-12">
						<div class="row">
							<div class="col-xs-4">
								<div class="table-header">
									角色列表
								</div>
								<div class="table-responsive">
									<table id="role-table"
										class="table table-striped table-bordered table-hover">
										<thead>
											<tr>
												<th class="center"><label style="display: none"> <input
														type="checkbox" class="ace" /> <span class="lbl"></span>
												</label></th>
												<th>角色</th>
												<%--<th>创建时间</th>--%>
												<%--<th>状态</th>--%>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${roleList }" var="po">
												<tr onclick="sel(${po.id })">
													<td class="center">
														<input type="hidden" name="id" value="${po.id }"></input> <label> 
														<input name="checkbox" value="${po.id }" type="checkbox" class="ace" <c:if test="${role_id ==po.id }">checked="checked"</c:if> /> 
														<span class="lbl"></span>
													</label></td>

													<td>${po.name }</td>
													<%--<td><fmt:formatDate value="${po.createTime}"--%>
															<%--pattern="yyyy-MM-dd HH:mm:ss" /></td>--%>
													<%--<td><c:choose>--%>
															<%--<c:when test="${po.available == 1 }">--%>
																	<%--启用 --%>
																<%--</c:when>--%>
															<%--<c:otherwise>--%>
																	<%--停用 --%>
																<%--</c:otherwise>--%>
														<%--</c:choose></td>--%>
												</tr>
											</c:forEach>
										</tbody>
									</table>
								</div>
								<!-- /.table-responsive -->
							</div>

							<!-- 权限列表 -->
							<div class="col-xs-4">
								<div class="table-header">
									前台权限
								</div>
								<div class="table-responsive">
								<c:if test="${role_id  != null}">
									<table id="permission-table"
										class="table table-striped table-bordered table-hover">
										<thead>
											<tr>
												<th class="center">
													<label style="display: none" >
													<input type="checkbox" class="ace"  />
													<span class="lbl"></span>
													</label>
												</th>
												<th>权限</th>
											</tr>
										</thead>

										<tbody>
											<c:forEach items="${permissionFrontList }" var="po">
												<tr >
													<td class="center">
														<input type="hidden" name="id" value="${po.id }"></input> 
															<c:choose>
																<c:when test="${sysRolePermissionList != null}">
																	<c:set var="flag" value="false"></c:set>
																	<c:forEach items="${sysRolePermissionList }" var="vo">
																		<c:if test="${vo.sysPermissionId ==po.id }">
																			<c:set var="flag" value="true"></c:set>
																			<input name ="checkpermission" class="ace"  type="checkbox"  value="${po.id }"  checked="checked"/>
																		</c:if>
																	</c:forEach>
																	<c:if test="${flag==false}">
																		<input name ="checkpermission"  class="ace"   type="checkbox" value="${po.id }" />
																	</c:if>
																</c:when>
																<c:otherwise>
																	<input name ="checkpermission"  class="ace"   type="checkbox" value="${po.id }" />
																</c:otherwise>
															</c:choose>
															<span class="lbl"></span>
													</td>

													<td>${po.name }</td>
												</tr>
												
											</c:forEach>
										</tbody>
									</table>
									</c:if>
								</div>
								<!-- /.table-responsive -->
							</div>


							<div class="col-xs-4">
								<div class="table-header">
									后台权限
								</div>
								<div class="table-responsive">
									<c:if test="${role_id  != null}">
										<table id="permission-table"
											   class="table table-striped table-bordered table-hover">
											<thead>
											<tr>
												<th class="center">
													<label style="display: none" >
														<input type="checkbox" class="ace"  />
														<span class="lbl"></span>
													</label>
												</th>
												<th>权限</th>
											</tr>
											</thead>

											<tbody>
											<c:forEach items="${permissionList }" var="po">
												<!-- onclick="check_role(this)" -->
												<tr >
													<td class="center">
														<input type="hidden" name="id" value="${po.id }"></input>
														<c:choose>
															<c:when test="${sysRolePermissionList != null}">
																<c:set var="flag" value="false"></c:set>
																<c:forEach items="${sysRolePermissionList }" var="vo">
																	<c:if test="${vo.sysPermissionId ==po.id }">
																		<c:set var="flag" value="true"></c:set>
																		<input name ="checkpermission" class="ace"  type="checkbox"  value="${po.id }"  checked="checked"/>
																	</c:if>
																</c:forEach>
																<c:if test="${flag==false}">
																	<input name ="checkpermission"  class="ace"   type="checkbox" value="${po.id }" />
																</c:if>
															</c:when>
															<c:otherwise>
																<input name ="checkpermission"  class="ace"   type="checkbox" value="${po.id }" />
															</c:otherwise>
														</c:choose>
														<span class="lbl"></span>
													</td>

													<td>
														<c:choose>
															<c:when test="${po.type=='menu' and po.level=='1'}">${po.name }</c:when>
															<c:when test="${po.type=='menu' and po.level=='2'}">&nbsp;&nbsp;&nbsp;&nbsp;${po.name }</c:when>
															<c:otherwise>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${po.name }</c:otherwise>
														</c:choose>
													</td>
												</tr>

											</c:forEach>
											</tbody>
										</table>
									</c:if>
								</div>
								<!-- /.table-responsive -->
							</div
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