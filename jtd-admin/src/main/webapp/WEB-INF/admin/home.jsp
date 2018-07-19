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
<title>后台管理首页</title>
<%@ include file="/WEB-INF/commonjsp/admin_common_css.jsp"%>
<%@ include file="/WEB-INF/commonjsp/admin_common_js.jsp"%>
<script type="text/javascript">
$(function(){
	/*
	var chanelObj = _campaignDicData.channelCategorys;
	$.each(chanelObj.DSP, function (index, e) {
        $("#channel-select").append("<option value=" + e.id + ">" + e.name + "</option>");
    });
	*/
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
						href="${pageContext.request.contextPath}">首页</a></li>
					<li class="active">欢迎页</li>
				</ul>
				<!-- .breadcrumb -->
			</div>

			<div class="page-content">
				<div class="row">
					<div class="col-xs-12">
						<!-- PAGE CONTENT BEGINS -->
						<div class="alert alert-block alert-success">
							<button type="button" class="close" data-dismiss="alert">
								<i class="icon-remove"></i>
							</button>
				
							<i class="green"></i> 欢迎使用 <strong class="green">DSP广告管理系统 <small></small> </strong>
							
<!-- 							<select class="form-control" id="channel-select" onChange=""> -->
									
							</select>
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