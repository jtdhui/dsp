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
<title>修改用户</title>
<%@ include file="/WEB-INF/commonjsp/admin_common_css.jsp"%>
<%@ include file="/WEB-INF/commonjsp/admin_common_js.jsp"%>
	<script src="${baseurl}js/admin/user/user_edit.js?d=${activeUser.currentTime}"></script>
<style type="text/css">
	.tree_icon{
		width: 12px;
		height: 12px;
		display: inline-block;
		float: left;
		margin: 3px 9px 0 0;
		vertical-align: baseline;
		background: url(./../../images/tree_icon.png) no-repeat;
	}

</style>
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
				<script type="text/javascript">
					try {
						ace.settings.check('breadcrumbs', 'fixed')
					} catch (e) {
					}
				</script>

				<ul class="breadcrumb">
					<li><i class="icon-home home-icon"></i> <a
						href="${pageContext.request.contextPath}">系统管理</a></li>
					<li class="active">用户管理</li>
				</ul>
				<!-- .breadcrumb -->

			</div>

			<div class="page-content">
				<div class="page-header">
					<h1>修改用户</h1>
				</div>
				
				<form id="form" class="form-horizontal" >
				
				<div class="row">
					<div class="col-xs-12">
						<input type="hidden" id="id" name="id" value="${ po.id }">
						<input type="hidden" id="roleId" name="roleId" value="${ activeUser.roleId }">

							<div class="form-group has-info">
								<label class="col-xs-12 col-sm-2 control-label ">登录名</label>
								<div class="col-xs-12 col-sm-3">
									<span class="block input-icon input-icon-right"> <input
										type="text" id="loginName" name="loginName" class="width-100" value="${ po.loginName }" readonly>
									</span>
								</div>
								<div class="help-inline col-xs-12 col-sm-1 inline help_width">*</div>
								
								<label class="col-xs-12 col-sm-1 control-label ">用户名</label>
								<div class="col-xs-12 col-sm-3">
									<span class="block input-icon input-icon-right"> <input
										type="text" id="userName" name="userName" class="width-100" value="${ po.userName }">
									</span>
								</div>
								<div class="help-inline col-xs-12 col-sm-1 inline help_width">*</div>
								
							</div>
							
							<div class="form-group has-info">
                                <input type="hidden" id="partnerId" value="${partner.id}">
								<label class="col-xs-12 col-sm-2 control-label ">归属公司</label>
								<div class="col-xs-10 col-sm-3 zTreeDemoBackground">
									<span class="block input-icon input-icon-right">
                                        <input id="partner-select" type="text" readonly value="${partner.partnerName}" style="width:200px;"/>
		                                &nbsp;<a id="menuBtn">选择</a>
										<%--<select class="form-control" id="partner-select" <c:if test="${po.id==1 or po.id==activeUser.userId}">disabled</c:if> onChange="setSelectVal()">--%>
											<%--<option value="0" >请选择归属公司</option>--%>
											<%--<c:forEach items="${poList }" var="vo">--%>
												<%--<c:choose>--%>
													<%--<c:when test="${vo.id == 1}">--%>
														<%--<c:if test="${po.id==1 or po.id == activeUser.userId or activeUser.roleId == 1 or activeUser.roleId == 10}">--%>
															<%--<option value="${vo.id }" <c:if test="${po.partnerId==vo.id}">selected='selected'</c:if>>${vo.partnerName }</option>--%>
														<%--</c:if>--%>
													<%--</c:when>--%>
													<%--<c:otherwise>--%>
														<%--<option value="${vo.id }" <c:if test="${po.partnerId==vo.id}">selected='selected'</c:if>>${vo.partnerName }</option>--%>
													<%--</c:otherwise>--%>
												<%--</c:choose>--%>
											<%--</c:forEach>--%>
										<%--</select>--%>
									</span>
								</div>
								<div class="help-inline col-xs-12 col-sm-1 inline help_width">*</div>

								<label class="col-xs-12 col-sm-1 control-label ">用户角色</label>
								<div class="col-xs-10 col-sm-3">
									<span class="block input-icon input-icon-right">
										<select class="form-control" id="role-select" <c:if test="${po.id==1 or po.id==activeUser.userId}">disabled</c:if> onChange="setRoleSelectVal()">
											<option value="0" >请选择角色</option>
											<c:forEach items="${roleTypeList}" var="rt">
												<c:choose>
													<c:when test="${not empty userRole and userRole.sysRoleId == rt.code }">
														<option value="${rt.code}" selected="selected">${rt.desc}</option>
													</c:when>
													<c:otherwise>
														<option value="${rt.code}" >${rt.desc}</option>
													</c:otherwise>
												</c:choose>
											</c:forEach>
										</select>
									</span>
								</div>
								<div class="help-inline col-xs-12 col-sm-1 inline help_width">*</div>
								
							</div>


							
							<div class="form-group has-info">
								
								<label class="col-xs-12 col-sm-2 control-label ">手机号</label>
								<div class="col-xs-12 col-sm-3">
									<span class="block input-icon input-icon-right"> <input
										type="text" id="mobile" name="mobile" class="width-100" value="${ po.mobile }">
									</span>
								</div>
								<div class="help-inline col-xs-12 col-sm-1 inline help_width">*</div>

								<label class="col-xs-12 col-sm-1 control-label ">email</label>
								<div class="col-xs-12 col-sm-3">
									<span class="block input-icon input-icon-right"> <input
											type="text" id="email" name="email" class="width-100" value="${ po.email }">
									</span>
								</div>
								<div class="help-inline col-xs-12 col-sm-1 inline help_width">*</div>
							</div>

							<div class="form-group has-info">
								<label class="col-xs-12 col-sm-2 control-label ">电话</label>
								<div class="col-xs-12 col-sm-3">
									<span class="block input-icon input-icon-right"> <input
											type="text" id="tel" name="tel" class="width-100" value="${ po.tel }">
									</span>
								</div>
								<div class="help-inline col-xs-12 col-sm-1 inline help_width"></div>
								<label class="col-xs-12 col-sm-1 control-label ">QQ</label>
								<div class="col-xs-12 col-sm-3">
									<span class="block input-icon input-icon-right"> <input
											type="text" id="qq" name="qq" class="width-100"  value="${ po.qq }">
									</span>
								</div>
								<div class="help-inline col-xs-12 col-sm-1 inline help_width"></div>
							</div>
							<div class="form-group has-info">
								
								<label class="col-xs-12 col-sm-2 control-label ">传真</label>
								<div class="col-xs-12 col-sm-3">
									<span class="block input-icon input-icon-right"> <input
										type="text" id="fax" name="fax" class="width-100" value="${ po.fax }">
									</span>
								</div>
								<div class="help-inline col-xs-12 col-sm-1 inline help_width"></div>
							</div>
							
							<!-- 用户下广告主数据 -->
							<div class="hr hr-24"></div>
							<div class="row">
								<div class="col-xs-12">
									<div class="table-responsive">
										<table id="partner-table"
											class="table table-striped table-bordered table-hover">
											<thead>
												<tr>
													<th>ID</th>
													<th>广告主</th>
                                                    <th>是否分配给其他运营</th>
												</tr>
											</thead>
											<tbody id="partner_list">
	
											</tbody>
										</table>
									</div>
									<!-- /.table-responsive -->
								</div>
								<!-- /span -->
							</div>
						<!-- /row -->
							
							<div class="clearfix form-actions">
								<div class="col-md-offset-3 col-md-9">
									<button class="btn btn-info" type="button" id="user_submit">
										<i class="icon-ok bigger-110"></i>
										提交
									</button>

									&nbsp; &nbsp; &nbsp;
									<button class="btn" type="reset">
										<i class="icon-undo bigger-110"></i>
										重置
									</button>
								</div>
							</div>
					</div>
					<!-- /.col -->
				</div>
				
				</form>
				
			</div>
			<!-- /.page-content -->
			</div>
		</div>
	</div>

    <div id="menuContent" class="menuContent" style="display:none; position: absolute; height: 260px; overflow: auto;">
        <ul id="treeDemo" class="ztree" style="margin-top:0; width:260px;"></ul>
    </div>

</body>
</html>