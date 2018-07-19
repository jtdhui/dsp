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
<title>添加菜单</title>
<%@ include file="/WEB-INF/commonjsp/admin_common_css.jsp"%>
<%@ include file="/WEB-INF/commonjsp/admin_common_js.jsp"%>
<script type="text/javascript">
function setSelectVal(){
	var selectVal = $("#menu-select").val();
	$("#parentId").val(selectVal);
}
$(function(){
	
	$("#subbtn").click(function(){
		
		if($("#name").val() == ""){
			layer.msg("请填写菜单名称",msgConfig);
			return ;
		}
		if(nameReg.test($("#name").val()) == false){
			layer.msg("菜单名称只能填写中英文、数字",msgConfig);
			return ;
		}
		//如果不是选择一级菜单则必须填写url
		if($("#menu-select option:selected").val() != "0"){
			if($("#url").val() == ""){
				layer.msg("请填写url",msgConfig);
				return ;
			}
		}
		if($("#sortString").val() != ""){
			if(numberReg.test($("#sortString").val()) == false){
				layer.msg("排序只能填写整数",msgConfig);
				return ;
			}
		}
		$("form").submit();
		
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
				<script type="text/javascript">
					try {
						ace.settings.check('breadcrumbs', 'fixed')
					} catch (e) {
					}
				</script>

				<ul class="breadcrumb">
					<li><i class="icon-home home-icon"></i> <a
						href="${pageContext.request.contextPath}">菜单管理</a></li>
					<li class="active">新增菜单</li>
				</ul>
				<!-- .breadcrumb -->

			</div>

			<div class="page-content">
				<div class="page-header">
					<h1>新增菜单</h1>
				</div>
				<div class="row">
					<div class="col-xs-12">
						<form id="form" class="form-horizontal" action="${baseurl }admin/menu/insert.action" method="post">
							
							<div class="form-group has-info">
								<label class="col-xs-12 col-sm-3 control-label no-padding-right">名称</label>
								<div class="col-xs-12 col-sm-5">
									<span class="block input-icon input-icon-right"> <input
										type="text" id="name" name="name" class="width-100" maxlength="60">
									</span>
								</div>
								<div class="help-block col-xs-12 col-sm-reset inline">*</div>
							</div>
							
							<div class="form-group has-info">
								<label class="col-xs-12 col-sm-3 control-label no-padding-right">上级菜单</label>
								<div class="col-xs-12 col-sm-5">
								<input type="hidden" name="parentId"  id="parentId" value="0"/>
								<select class="form-control" id="menu-select" onChange="setSelectVal()">
									<option value="0">一级菜单</option>
									<c:forEach items="${poList }" var="po">
										<option value="${po.id }" >${po.name }</option>
									</c:forEach>
								</select>
								</div>
								<div class="help-block col-xs-12 col-sm-reset inline">*</div>
							</div>
							
							<div class="form-group has-info">
								<label class="col-xs-12 col-sm-3 control-label no-padding-right">URL</label>
								<div class="col-xs-12 col-sm-5">
									<span class="block input-icon input-icon-right"> <input
										type="text" id="url" name="url" class="width-100" maxlength="100">
									</span>
								</div>
								<div class="help-block col-xs-12 col-sm-reset inline">*</div>
							</div>
							<div class="form-group has-info">
								<label class="col-xs-12 col-sm-3 control-label no-padding-right">排序</label>
								<div class="col-xs-12 col-sm-5">
									<span class="block input-icon input-icon-right"> <input
										type="text" id="sortString" name="sortString" class="width-100" value="1" maxlength="10">
									</span>
								</div>
								<div class="help-block col-xs-12 col-sm-reset inline"></div>
							</div>
							
							<div class="form-group has-info">
								<label class="col-xs-12 col-sm-3 control-label no-padding-right">备注</label>
								<div class="col-xs-12 col-sm-5">
									<span class="block input-icon input-icon-right"> <input
										type="text" id="remark" name="remark" class="width-100" maxlength="100">
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