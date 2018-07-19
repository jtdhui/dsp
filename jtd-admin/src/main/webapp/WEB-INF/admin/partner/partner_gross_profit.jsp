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
<title>毛利设置</title>
<%@ include file="/WEB-INF/commonjsp/admin_common_css.jsp"%>
<%@ include file="/WEB-INF/commonjsp/admin_common_js.jsp"%>
</head>

<script type="text/javascript">
	$(function(){
		//毛利：默认/其他的切换
		$("#gross_type_default").click(function(){
			if(confirm("确定设置为默认毛利?")){
				$("#gross_other").val('');
				$("#gross_other").prop("disabled",true);
			}
			else{
				return false ;
			}
		});
		
		$("#gross_type_other").click(function(){
			$("#gross_other").prop("disabled",false);
		});
		
// 		$("#reset").click(function(){
// 			$("#gross_other").prop("disabled",true);
// 		});
		
		$("#subbtn").click(function(){
			if($("#gross_type_other").prop("checked") == true){
				if($("#gross_other").val() == ""){
					layer.msg("请填写毛利数或选择默认值",msgConfig);
					return ;
				}
				if(numberReg.test($("#gross_other").val()) == false){
					layer.msg("毛利只能填写整数",msgConfig);
					return ;
				}
				if($("#gross_other").val() > 99){
					layer.msg("毛利设置过大",msgConfig);
					return ;
				}
			}
			$("form").submit();
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
					ace.settings.check('breadcrumbs', 'fixed');
				} catch (e) {
				}
			</script>

			<ul class="breadcrumb">
				<li><i class="icon-home home-icon"></i> <a
					href="${pageContext.request.contextPath}/admin/partner/list.action">广告主管理</a></li>
				<li class="active">毛利设置</li>
			</ul>

		</div>
		
		<div class="page-content">
			<div class="page-header">
				<h1>毛利设置</h1>
			</div>
			<div class="row">
				<div class="col-xs-12">
					<form id="form" class="form-horizontal" action="${baseurl }admin/partner/grossProfitSave.action" method="post" enctype="multipart/form-data">
						<input type="hidden" id="partner_id" name="partner_id" value="${partner.id }">
						<div class="form-group has-info" ">
							<label class="col-xs-12 col-sm-1 control-label no-padding-right">毛利设置</label>
							<div class="col-xs-12 col-sm-3">
								<div>
									<label class="line-height-1 blue">
										<input type="radio" name="gross_type" id="gross_type_default" value="0" class="ace" <c:if test="${empty partner.id || partner.grossProfit == 0.3}">checked</c:if>>
										<span class="lbl"> 默认毛利（30%）</span>
									</label>
								</div>
								<div >
									<label class="line-height-1 blue">
										<input type="radio" name="gross_type" id="gross_type_other" value="1" class="ace" <c:if test="${not empty partner.id && partner.grossProfit != 0.3}">checked</c:if>>
										<span class="lbl"> 自定义毛利</span>
									</label>
									<c:if test="${empty partner.id || partner.grossProfit == 0.3}">
										<input type="text" id="gross_other" name="gross_other" class="width-10" maxlength="5" disabled="disabled" >	
									</c:if>
									<c:if test="${ not empty partner.id && partner.grossProfit != 0.3}">
										<input type="text" id="gross_other" name="gross_other" class="width-10" value="${partner.grossProfitString }" maxlength="5" >	
									</c:if>
									<label class="control-label">%</label>
									<div class="help-block col-xs-12 col-sm-reset inline"></div>
								</div>
							</div>
						</div>
						
						<div class="clearfix form-actions">
							<div class="col-md-offset-3 col-md-9">
								<button class="btn btn-info" type="button" id="subbtn">
									<i class="icon-ok bigger-110"></i>
									提交
								</button>
<!-- 								&nbsp; &nbsp; &nbsp; -->
<!-- 								<button class="btn" type="reset" id="reset"> -->
<!-- 									<i class="icon-undo bigger-110"></i> -->
<!-- 									重置 -->
<!-- 								</button> -->
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