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
<title>用户角色授权</title>
<%@ include file="/WEB-INF/commonjsp/admin_common_css.jsp"%>
<%@ include file="/WEB-INF/commonjsp/admin_common_js.jsp"%>

<script type="text/javascript">

	function sel(id)
	{
		window.location.href="${pageContext.request.contextPath}/admin/userrole/userSelect.action?id="+id;
	}
	
	$(function(){
		
		//角色授权
		$('#btn_grant').click(function(){
			
			var sysUserId = "";
			$("[name='checkbox'][checked]").each(function(){
			   sysUserId = $(this).val();
		 	});
			
			var sysRoleId = "";
            var count = 0;
			$("input[name='checkrole']:checked",$('#role-table')).each(function(){
				sysRoleId +=  $(this).val() +",";
				count ++;
			});
			if(count>1){
				layer.msg("只能授权一个角色!",msgConfig);
				return;
			}

			if(sysRoleId.length>1){
				sysRoleId = sysRoleId.substring(0,sysRoleId.length-1);
				window.location.href="${pageContext.request.contextPath}/admin/userrole/grant.action?sysUserId="+sysUserId+"&sysRoleId="+sysRoleId;
			}else{
				layer.msg("请选择授权的角色!",msgConfig);
				return;
			}
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
					<li class="active">用户角色授权</li>
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
							<div class="col-xs-6">
								<div class="table-responsive">
									<table id="user-table" class="table table-striped table-bordered table-hover">
										<thead>
											<tr>
												<th class="center">
													<label>
														<input type="checkbox" class="ace" style="display: none;"/>
														<span class="lbl"></span>
													</label>
												</th>
												<th>用户名</th>
												<th>创建时间</th>
												<%--<th>状态</th>--%>
											</tr>
										</thead>

										<tbody>
											<c:forEach items="${userList }" var="po">
												<tr onclick="sel(${po.id })">
													<td class="center">
														<input type="hidden" name="id" value="${po.id }"></input>
														<label>
															<input name="checkbox" value="${po.id }" type="checkbox" class="ace" <c:if test="${user_id ==po.id }">checked="checked"</c:if> />
															<span class="lbl"></span>
														</label>
													</td>
													<td>${po.userName }</td>
													<td><fmt:formatDate value="${po.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
													<%--<td><c:choose>--%>
															<%--<c:when test="${po.status == 1 }">--%>
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

							<!-- 角色列表 -->
							<div class="col-xs-6">
								<div class="table-responsive">
								<c:if test="${user_id  != null}">
									<table id="role-table"
										class="table table-striped table-bordered table-hover">
										<thead>
											<tr>
												<th class="center">
													<label style="display: none;">
													<input type="checkbox" class="ace"  /> 
													<span class="lbl"></span>
													</label>
												</th>
												<th>角色名称</th>
												<th>创建时间</th>
												<%--<th>状态</th>--%>
											</tr>
										</thead>

										<tbody>
											<c:forEach items="${roleList }" var="po">
												<!-- onclick="check_role(this)" -->
												<tr >
													<td class="center">
														<input type="hidden" name="id" value="${po.id }"></input> 
															<c:choose>
																<c:when test="${sysUserRoleList != null}">
																	<c:set var="flag" value="false"></c:set>
																	<c:forEach items="${sysUserRoleList }" var="vo">
																		<c:if test="${vo.sysRoleId ==po.id }">
																			<c:set var="flag" value="true"></c:set>
																			<input name ="checkrole" class="ace"  type="checkbox"  value="${po.id }"  checked="checked"/>
																		</c:if>
																	</c:forEach>
																	<c:if test="${flag==false}">
																		<input name ="checkrole"  class="ace"   type="checkbox" value="${po.id }" />
																	</c:if>
																</c:when>
																<c:otherwise>
																	<input name ="checkrole"  class="ace"   type="checkbox" value="${po.id }" />
																</c:otherwise>
															</c:choose>
															<span class="lbl"></span>
													</td>

													<td>${po.name }</td>
													<td><fmt:formatDate value="${po.createTime}"
															pattern="yyyy-MM-dd HH:mm:ss" /></td>
													<%--<td><c:choose>--%>
															<%--<c:when test="${po.available == 1 }">--%>
																	<%--启用--%>
																<%--</c:when>--%>
															<%--<c:otherwise>--%>
																	<%--停用 --%>
																<%--</c:otherwise>--%>
														<%--</c:choose></td>--%>
												</tr>
												
											</c:forEach>
										</tbody>
									</table>
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