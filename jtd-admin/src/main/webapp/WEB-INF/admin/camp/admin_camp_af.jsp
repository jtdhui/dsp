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
<title>AF设置</title>
<%@ include file="/WEB-INF/commonjsp/admin_common_css.jsp"%>
<%@ include file="/WEB-INF/commonjsp/admin_common_js.jsp"%>
<script type="text/javascript">
function setAF(id){
	
	var afValue = $("#afValue").val();

	if(isNaN(afValue) || afValue.toString().length>4 || afValue<0){
		layer.alert("AF值非法或者AF不能大于10000!");
		return false;
	}
	$.ajax({
		url:"${baseurl }admin/camp/af/saveAfSet.action",
		type:"post",
		dataType: "json",  
		data:{id:id,afValue:afValue},
		success:function(data){
			location.href = data.url;
		}
	});
}
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
						href="${pageContext.request.contextPath}">活动管理</a></li>
					<li class="active">AF设置</li>
				</ul>
				<!-- .breadcrumb -->

			</div>

			<div class="page-content">
				<div class="page-header">
					<h1>AF设置</h1>
				</div>
				<div class="row">
					<div class="col-xs-12">
						
							<input type="hidden" id="id" name="id" value="${po.id }">

							<div class="form-group has-info">
								<label class="col-xs-12 col-sm-3 control-label no-padding-right">名称</label>
								<div class="col-xs-12 col-sm-5">
									<span class="block input-icon input-icon-right"> 
									
									<input type="text" disabled class="width-100" value="${po.campaignName }">
									</span>
								</div>
								<div class="help-block col-xs-12 col-sm-reset inline"></div>
							</div>
							
							<div class="form-group has-info">
								<label class="col-xs-12 col-sm-3 control-label no-padding-right">所属广告主</label>
								<div class="col-xs-12 col-sm-5">
									<span class="block input-icon input-icon-right"> 
										<input type="hidden" id="partnerId" name="partnerId" value="${po.partnerId }">
										<input type="text" disabled class="width-100" value="${po.partnerName }" >
									</span>
								</div>
								<div class="help-block col-xs-12 col-sm-reset inline"></div>
							</div>
							
							<div class="form-group has-info">
								<label class="col-xs-12 col-sm-3 control-label no-padding-right">AF设置</label>
								<div class="col-xs-12 col-sm-5">
									<div>
										<input type="text" class="width-20" value="${po.afValue}" id="afValue" name="afValue">AF设置为0时,AF相当于没有设置
									</div>
								</div>
								<div class="help-block col-xs-12 col-sm-reset inline"></div>
							</div>
							<div class="form-group has-info">
								&nbsp;
							</div>
							<div class="clearfix form-actions">
								<div class="col-md-offset-3 col-md-9">
									<button class="btn btn-info" type="submit" onclick="setAF(${po.id})">
										<i class="icon-ok bigger-110"></i> 提交
									</button>
									&nbsp; &nbsp; &nbsp;
									<button class="btn" type="reset">
										<i class="icon-undo bigger-110"></i> 重置
									</button>
								</div>
							</div>
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