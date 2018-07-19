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
<title>域名</title>
<%@ include file="/WEB-INF/commonjsp/admin_common_css.jsp"%>
<%@ include file="/WEB-INF/commonjsp/admin_common_js.jsp"%>
	<script src="${baseurl}js/campaignDicData.js"></script>
	<script type="text/javascript">

		$(function(){

			//媒体类型
			var webSiteType = $("#webSiteType").val();
			var media_type_option = '';
			$.each(_campaignDicData.websiteTypes, function(index, obj) {
				//是否包含下一级媒体类型
				if(obj.hasOwnProperty("subWebsiteTypes")){
					$.each(obj.subWebsiteTypes, function(index, sub_obj) {
						if(sub_obj.id == webSiteType){
							media_type_option +='<option value="'+sub_obj.id+'" selected = "selected" >'+sub_obj.name+'</option>';
						}else {
							media_type_option +='<option value="'+sub_obj.id+'">'+sub_obj.name+'</option>';
						}
					});
				};
			});
			$("#domain_name_select").append(media_type_option);

			// 提交
			$('#subbtn').click(function(){
				$("#form").submit();
			});

			// 返回
			$("#back_btn").click(function () {
				location.href = "${pageContext.request.contextPath}/admin/domain/list.action";
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
						href="${pageContext.request.contextPath}">运营管理</a></li>
					<li class="active">域名管理</li>
				</ul>
				<!-- .breadcrumb -->

			</div>

			<div class="page-content">
				<div class="page-header">
					<c:choose>
						<c:when test="${empty po.id}">
							<h1>新增域名</h1>
						</c:when>
						<c:otherwise>
							<h1>修改域名</h1>
						</c:otherwise>
					</c:choose>

				</div>
				<div class="row">
					<div class="col-xs-12">
						<form id="form" class="form-horizontal" action="${baseurl }admin/domain/save.action" method="post">
							<input type="hidden" id="id" name="id" value="${po.id }">
							<div class="form-group has-info">
								<label class="col-xs-12 col-sm-3 control-label no-padding-right">域名</label>
								<div class="col-xs-12 col-sm-5">
									<span class="block input-icon input-icon-right"> 
									<input type="text" id="domain" name="domain" value="${po.domain }"  class="width-100" maxlength="60">
									</span>
								</div>
								<div class="help-block col-xs-12 col-sm-reset inline"></div>
							</div>
							<div class="form-group has-info">
								<label class="col-xs-12 col-sm-3 control-label no-padding-right">站点名称</label>
								<div class="col-xs-12 col-sm-5">
									<span class="block input-icon input-icon-right">
									<input type="text" id="siteName" name="siteName" value="${po.siteName }"  class="width-100" maxlength="60">
									</span>
								</div>
								<div class="help-block col-xs-12 col-sm-reset inline"></div>
							</div>
							<div class="form-group has-info">
								<label class="col-xs-12 col-sm-3 control-label no-padding-right">流量数值</label>
								<div class="col-xs-12 col-sm-5">
									<span class="block input-icon input-icon-right">
									<input type="text" id="flow" name="flow" value="${po.flow }"  class="width-100" maxlength="60">
									</span>
								</div>
								<div class="help-block col-xs-12 col-sm-reset inline"></div>
							</div>
							<div class="form-group has-info">
								<label class="col-xs-12 col-sm-3 control-label no-padding-right">网站类型</label>
								<input type="hidden" id="webSiteType" value="${po.webSiteType }">
								<div class="col-xs-12 col-sm-5">
									<span class="block input-icon input-icon-right">
										<select id="domain_name_select" name="webSiteType">

                                		</select>
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
									<button class="btn" type="reset" id="back_btn">
										<i class="icon-undo bigger-110"></i>
										返回
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