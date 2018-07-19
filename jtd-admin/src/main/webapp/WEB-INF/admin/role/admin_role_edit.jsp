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
</head>

<script type="text/javascript">
	
	$(function(){
		
		$('#subbtn').click(function(){
			
			if($("#name").val()==""){
				layer.msg("请填写角色名称",msgConfig);
				return ;
			}
//			if(nameReg.test($("#name").val()) == false){
//				layer.msg("角色名称只能填写中英文、数字",msgConfig);
//				return ;
//			}
			$("#form").submit();
		});
		
	});
	
</script>

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
					<li class="active">角色管理</li>
				</ul>
				<!-- .breadcrumb -->

			</div>

			<div class="page-content">
				<div class="page-header">
					<h1>修改</h1>
				</div>
				<div class="row">
					<div class="col-xs-12">
						<form id="form" class="form-horizontal" action="${baseurl }admin/role/update.action" method="post">
							<input type="hidden" id="id" name="id" value="${po.id }">
							
							<div class="form-group has-info">
								<label class="col-xs-12 col-sm-3 control-label no-padding-right">用户名</label>
								<div class="col-xs-12 col-sm-5">
									<span class="block input-icon input-icon-right"> 
									<input type="text" id="name" name="name" value="${po.name }"  class="width-100" maxlength="60">
									</span>
								</div>
								<div class="help-block col-xs-12 col-sm-reset inline"></div>
							</div>
							
							<div class="form-group has-info">
								<label class="col-xs-12 col-sm-3 control-label no-padding-right">备注</label>
								<div class="col-xs-12 col-sm-5">
									<span class="block input-icon input-icon-right"> <input
										type="text" id="remark" name="remark" value="${po.remark }"  class="width-100" maxlength="100">
									</span>
								</div>
								<div class="help-block col-xs-12 col-sm-reset inline"></div>
							</div>
							
							<div class="clearfix form-actions">
								<div class="col-md-offset-3 col-md-9">
									<button class="btn btn-info" type="button" id="subbtn">
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
						</form>
					</div>
					<!-- /.col -->
				</div>
			</div>
			<!-- /.page-content -->
			</div>
		</div>
	</div>
	
</body>
</html>