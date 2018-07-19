<%@page import="com.jtd.web.constants.WebsiteStyleType"%>
<%@page import="com.jtd.web.po.Partner"%>
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
<c:if test="${empty partner}">
	<title>新增广告主</title>
</c:if>
<c:if test="${not empty partner}">
	<title>修改广告主</title>
</c:if>
<%@ include file="/WEB-INF/commonjsp/admin_common_css.jsp"%>
<%@ include file="/WEB-INF/commonjsp/admin_common_js.jsp"%>
<script src="${baseurl}js/admin/partner/partnerEdit.js?d=${activeUser.currentTime}"></script>
</head>

<script type="text/javascript">

$(function(){
	
	//广告主类型：oem和其他类型的切换 2017-1-11
	$("input[name=partnerType][value=2]").click(function(){
		$("#oem_setting_div").show();
	});
	$("input[name=partnerType][value!=2]").click(function(){
		$("#oem_setting_div").hide();
	});
	
	
	//读取行业选项，如果是修改页面则加上选中状态
	var chanelObj = _campaignDicData.channelCategorys;
	$("#categoryId").html("");
	$("#categoryId").append("<option value=''>请选择</option>");
	$.each(chanelObj.DSP, function (index, obj) {
		var sel = "" ;
		if(obj.id == "${partner.categoryId}"){
			sel = "selected";
		}
        $("#categoryId").append("<option value='" + obj.id + "' " + sel + ">" + obj.name + "</option>");
    });
	//提交验证
	$("#subbtn").click(function(){
		if($("#partnerName").val()==""){
			layer.msg("请填写广告主全称",msgConfig);
			return ;
		}
		if($("#salesName").val()==""){
			layer.msg("请填写销售姓名",msgConfig);
			return ;
		}
		if($("#salesTelephone").val()==""){
			layer.msg("请填写销售电话",msgConfig);
			return ;
		}
		if($("#salesEmail").val()==""){
			layer.msg("请填写销售email",msgConfig);
			return ;
		}  
		//上级代理
		if($("#partner-select").val()==""){
			layer.msg("请选择上级代理",msgConfig);
			return ;
		}
		if(nameReg.test($("#partnerName").val()) == false){
			layer.msg("广告主全称只能填写中英文、数字",msgConfig);
			return ;
		}
		
		//公司全称不能重复
		$.ajax({
			url:"${pageContext.request.contextPath}/admin/partner/validatePartnerName.action",
			type:"get",
			data:{"partnerName":encodeURI($("#partnerName").val()),"editingPartnerId":"${partner.id}"},
			success:function(data){
				if(data == "0"){
					layer.msg("当前广告主名称已经被使用，不允许重复存在",msgConfig);
					return ;
				}
				else{
					validForm();
				}
			},
			 error:function(msg){
				layer.msg("广告主名称验证失败，请稍后再试",msgConfig);
				return ;
		    },
		});
		
	});
	
	var validForm = function(){
		if($("#simpleName").val()==""){
			layer.msg("请填写缩略名",msgConfig);
			return ;
		}
		if(nameReg.test($("#simpleName").val()) == false){
			layer.msg("缩略名只能填写中英文、数字",msgConfig);
			return ;
		}
		
		if($("input[name=partnerType]:checked").length == 0){
			layer.msg("请选择广告主类型",msgConfig);
			return ;
		}
		if($("#categoryId option:selected").val() == ""){
			layer.msg("请选择所属行业",msgConfig);
			return ;
		}
		
		if($("#websiteName").val()==""){
			layer.msg("请填写官网名称",msgConfig);
			return ;
		}
		if(nameReg.test($("#websiteName").val()) == false){
			layer.msg("官网名称只能填写中英文、数字",msgConfig);
			return ;
		}
		if($("#websiteUrl").val()==""){
			layer.msg("请填写官网url",msgConfig);
			return ;
		}
		if(urlReg.test($("#websiteUrl").val()) == false){
			layer.msg("官网url格式不正确",msgConfig);
			return ;
		}
		
		if($("#contactName").val() != ""){
			if(nameReg2.test($("#contactName").val()) == false){
				layer.msg("联系人姓名只能填写中英文",msgConfig);
				return ;
			}
		}
		if($("#contactTelephone").val() != ""){
			if(telReg.test($("#contactTelephone").val()) == false){
				layer.msg("联系人电话格式不正确",msgConfig);
				return ;
			}
		}
		if($("#contactEmail").val() != ""){
			if(emailReg.test($("#contactEmail").val()) == false){
				layer.msg("联系人邮件格式不正确",msgConfig);
				return ;
			}
		}
		
		if($("#salesName").val() != ""){
			if(nameReg2.test($("#salesName").val()) == false){
				layer.msg("销售姓名只能填写中英文",msgConfig);
				return ;
			}
		}
		if($("#salesTelephone").val() != ""){
			if(telReg.test($("#salesTelephone").val()) == false){
				layer.msg("销售电话格式不正确",msgConfig);
				return ;
			}
		}
		if($("#salesEmail").val() != ""){
			if(emailReg.test($("#salesEmail").val()) == false){
				layer.msg("销售邮件格式不正确",msgConfig);
				return ;
			}
		}
		
		if($('#pid option:selected').val() == $("#parId").val()){
			console.log($("#pid").val());
			if(emailReg.test($("#pid").val()) == false){
				layer.msg("不能选择自己！",msgConfig);
				return ;
			}
		}
		
		/* 不应该限制必须选择运营人员 2017-3-21
		if($("#operatorUserId option:selected").val() == "0"){
			layer.msg("请选择中企运营人员",msgConfig);
			return ;
		}
		*/
		
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
		
		//如果选中OEM类型
		if($("input[name=partnerType][value=2]:checked").length > 0){
			
			if($("input[name=styleCode]:checked").length == 0){
				layer.msg("请选择个性化样式",msgConfig);
				return ;
			}
			if($("#copyrightStartYear").val() == ""){
				layer.msg("请填写版权起始年份",msgConfig);
				return ;
			}
			if(numberReg.test($("#copyrightStartYear").val()) == false || $("#copyrightStartYear").val() < 1900){
				layer.msg("版权起始年份应为4位整数，并请输入合理的年份",msgConfig);
				return ;
			}
			if($("#websiteRecordCode").val() == ""){
				layer.msg("请填写网站备案号",msgConfig);
				return ;
			}
			if($("#loginUrl").val() == ""){
				layer.msg("请填写访问系统URL",msgConfig);
				return ;
			}
			if(urlReg2.test($("#loginUrl").val())==false){
				layer.msg("访问系统URL格式不正确",msgConfig);
				return ;
			}
			//如果选中OEM类型 && logoImg不存在,即为创建oem广告主，就必须上传logo
			if($("#logoImg").length == 0){
				if($("#logoFile").val() == ""){
					layer.msg("请上传logo图片",msgConfig);
					return ;
				}
				
			}
			if($("#logoFile").val() != ""){
				if(imgFileReg.test($("#logoFile").val())==false){
					layer.msg("只能上传jpg或png图片",msgConfig);
					return ;
				}
			}
		}
		
		$("form").submit();
	};
});
</script>

<style type="text/css">
	.help_width { width : 20px ;}
</style>

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
						href="${pageContext.request.contextPath}/admin/partner/list.action">广告主管理</a></li>
					<li class="active">添加广告主</li>
				</ul>
				<!-- .breadcrumb -->

			</div>
			
			<div class="page-content">
				<div class="page-header">
					<c:if test="${empty partner.id}">
						<h1>新增广告主</h1>
					</c:if>
					<c:if test="${not empty partner.id}">
						<h1>修改广告主</h1>
					</c:if>
				</div>
				<div class="row">
					<div class="col-xs-12">
						<form id="form" class="form-horizontal" action="${baseurl }admin/partner/save.action" method="post" enctype="multipart/form-data" >
						
							<!-- id -->
							<input type="hidden" name="id" id="parId" value="${partner.id }"/>
							<!-- row 1 -->
							<div class="form-group has-info">
								
								<label class="col-xs-12 col-sm-1 control-label no-padding-right">缩略名</label>
								<div class="col-xs-12 col-sm-2">
									<input type="text" id="simpleName" name="simpleName" class="width-100" value="${ partner.simpleName }" maxlength="15">
								</div>
								<div class="help-inline col-xs-12 col-sm-1 inline help_width">*</div>
								
								<label class="col-xs-12 col-sm-1 control-label no-padding-right">广告主全称</label>
								<div class="col-xs-12 col-sm-2">
									<input type="text" id="partnerName" name="partnerName" class="width-100" value="${ partner.partnerName }" maxlength="25">					
								</div>
								<div class="help-inline col-xs-12 col-sm-2 inline">*&nbsp;(要使用营业执照上的公司名称，否则影响资质审核)</div>
								
							</div>
							
							<!-- row 2 -->
							<div class="form-group has-info">
								<label class="col-xs-12 col-sm-1 control-label no-padding-right">广告主类型</label>
								<div class="col-xs-12 col-sm-2">
									<!-- 如果当前partner的类型已经为oem，则不能再变更广告主类型-->
									<c:if test="${empty partner.partnerType || partner.partnerType != 2 }">
										<label class="line-height-1 blue">
											<input name="partnerType" type="radio" class="ace" value="0" <c:if test="${ partner.partnerType == 0 }"> checked </c:if> >
											<span class="lbl"> 代理</span>
										</label>
										<label class="line-height-1 blue">
											<input name="partnerType" type="radio" class="ace" value="1" <c:if test="${ partner.partnerType == 1 }"> checked </c:if>>
											<span class="lbl"> 直客</span>
										</label>
									</c:if>
									
								</div>
								<div class="help-inline col-xs-12 col-sm-1 inline help_width">*</div>
								
								<label class="col-xs-12 col-sm-1 control-label no-padding-right">所属行业</label>
								<div class="col-xs-12 col-sm-2">
									<select id="categoryId" name="categoryId" class="width-100">
									</select>
								</div>
								<div class="help-inline col-xs-12 col-sm-1 inline help_width">*</div>
							</div>
							
							<!-- row 3 -->
							<div class="form-group has-info">
								<label class="col-xs-12 col-sm-1 control-label no-padding-right">官网名称</label>
								<div class="col-xs-12 col-sm-2">
									<input type="text" id="websiteName" name="websiteName" class="width-100" value="${partner.websiteName }" maxlength="25">					
								</div>
								<div class="help-inline col-xs-12 col-sm-1 inline help_width">*</div>
								
								<label class="col-xs-12 col-sm-1 control-label no-padding-right">官网URL</label>
								<div class="col-xs-12 col-sm-2">
									<input type="text" id="websiteUrl" name="websiteUrl" class="width-100"  value="${partner.websiteUrl }" maxlength="100">		
								</div>
								<div class="help-inline col-xs-12 col-sm-3 inline">*&nbsp;(格式：http://www.xxx.xxx&nbsp;或&nbsp;https://www.xxx.xxx)</div>
							</div>
							<!-- row 4 -->
							<div class="form-group has-info">
								<label class="col-xs-12 col-sm-1 control-label no-padding-right">联系人姓名</label>
								<div class="col-xs-12 col-sm-2">
									<input type="text" id="contactName" name="contactName" class="width-100" value="${partner.contactName }" maxlength="25">					
								</div>
								<div class="help-inline col-xs-12 col-sm-1 inline help_width"></div>
								
								<label class="col-xs-12 col-sm-1 control-label no-padding-right">联系人电话</label>
								<div class="col-xs-12 col-sm-2">
									<input type="text" id="contactTelephone" name="contactTelephone" class="width-100" value="${partner.contactTelephone }" maxlength="20">		
								</div>
								<div class="help-inline col-xs-12 col-sm-1 inline help_width"></div>
								
								<label class="col-xs-12 col-sm-1 control-label no-padding-right">联系人email</label>
								<div class="col-xs-12 col-sm-2">
									<input type="text" id="contactEmail" name="contactEmail" class="width-100"  value="${partner.contactEmail }" maxlength="50"> 		
								</div>
								<div class="help-inline col-xs-12 col-sm-1 inline help_width"></div>
							</div>
							<!-- row 5 -->
							<div class="form-group has-info">
								<label class="col-xs-12 col-sm-1 control-label no-padding-right">销售姓名</label>
								<div class="col-xs-12 col-sm-2">
									<input type="text" id="salesName" name="salesName" class="width-100"  value="${partner.salesName }" maxlength="25">					
								</div>
								<div class="help-inline col-xs-12 col-sm-1 inline help_width">*</div>
								
								<label class="col-xs-12 col-sm-1 control-label no-padding-right">销售电话</label>
								<div class="col-xs-12 col-sm-2">
									<input type="text" id="salesTelephone" name="salesTelephone" class="width-100" value="${partner.salesTelephone }" maxlength="20">		
								</div>
								<div class="help-inline col-xs-12 col-sm-1 inline help_width">*</div>
								
								<label class="col-xs-12 col-sm-1 control-label no-padding-right">销售email</label>
								<div class="col-xs-12 col-sm-2">
									<input type="text" id="salesEmail" name="salesEmail" class="width-100"  value="${partner.salesEmail }" maxlength="50">		
								</div>
								<div class="help-inline col-xs-12 col-sm-1 inline help_width">*</div>
							</div>
							<!-- row 6 -->
							<div class="form-group has-info">
								<label class="col-xs-12 col-sm-1 control-label no-padding-right">上级代理</label>
								<div class="col-xs-10 col-sm-2 zTreeDemoBackground">
									<span class="block input-icon input-icon-right">
                                        <input id="partner-select" type="text" readonly value="${partner.pName}" style="width:200px;"/>
		                                <input type="hidden" id="partnerId" name="pid" value="${partner.pid}">
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
								
								
<!-- 								<div class="col-xs-12 col-sm-2"> -->
									
<!-- 									<select id="pid" name="pid" class="width-100"> -->
<!-- 										只有当前操作用户是中企动力员工（比如admin和其他运营人员），才能给上级公司选择“无” -->
<%-- 										<c:if test="${userBelongingPartnerId == 1 }"> --%>
<!-- 											<option value="0">无</option> -->
<%-- 										</c:if> --%>
<!-- 										如果当前编辑的partner是中企动力，就不显示pList中的数据，因为中企动力的上级公司只能是“无” -->
<%-- 										<c:if test="${partner.id != 1 }"> --%>
<%-- 											<c:forEach items="${pList }" var="p"> --%>
											
<%-- 												<c:if test="${p.id == partner.pid}"> --%>
<%-- 													<option value="${p.id }" selected>${p.partnerName } </option> --%>
<%-- 												</c:if> --%>
<%-- 												<c:if test="${p.id != partner.pid}"> --%>
<%-- 													<option value="${p.id }">${p.partnerName } </option> --%>
<%-- 												</c:if> --%>
<%-- 											</c:forEach> --%>
<%-- 										</c:if> --%>
<!-- 									</select>				 -->
<!-- 								</div> -->
								<div class="help-inline col-xs-12 col-sm-1 inline help_width">*</div>
								
								<!-- 只有当前操作用户是中企动力员工（比如管理员或运营经理、运营），才会提供operatorList数据，并且当前编辑的partner不是中企动力，才会显示"中企运营人员"选择框 -->
								<c:if test="${ not empty operatorList && partner.id != 1 }">
									<label class="col-xs-12 col-sm-1 control-label no-padding-right">中企运营人员</label>
									<div class="col-xs-12 col-sm-2">
										<select id="operatorUserId" name="operatorUserId" class="width-100">
											<!-- 如果只有一个选项，那么就没有“无” -->
											<c:if test="${ fn:length(operatorList) != 1 }">
												<option value="0">无</option>
											</c:if>
											<c:forEach items="${operatorList }" var="operator">
											
												<c:if test="${operator.id == operatorId}">
													<option value="${operator.id }" selected>${operator.userName } </option>
												</c:if>
												<c:if test="${operator.id != operatorId}">
													<option value="${operator.id }">${operator.userName } </option>
												</c:if>
												
											</c:forEach>
										</select>			
									</div>
									<div class="help-inline col-xs-12 col-sm-1 inline help_width">*</div>
								</c:if>
								
							</div>
							<!-- row 7 -->
							<div class="form-group has-info">
								<label class="col-xs-12 col-sm-1 control-label no-padding-right">区域</label>
								<div class="col-xs-12 col-sm-2">
									<input type="text" id="region" name="region" class="width-100" value="${partner.region }" maxlength="25">					
								</div>
								<div class="help-inline col-xs-12 col-sm-1 inline help_width"></div>
								<label class="col-xs-12 col-sm-1 control-label no-padding-right">城市</label>
								<div class="col-xs-12 col-sm-2">
									<input type="text" id="city" name="city" class="width-100" value="${partner.city }" maxlength="25">					
								</div>
							</div>
							
							<!-- oem个性化设置 -->
							<div id="oem_setting_div" <c:if test="${ empty partner.partnerType || partner.partnerType != 2 }"> style="display:none"  </c:if>>
								<div class="page-header">
									<h1>个性化设置</h1>
								</div>
								<!-- row 1 -->
								<div class="form-group has-info">
									<label class="col-xs-12 col-sm-1 control-label no-padding-right">个性化样式：</label>
									<div class="col-xs-12 col-sm-2">
										<%
											Partner partner = (Partner)request.getAttribute("partner");
										
											for(WebsiteStyleType style : WebsiteStyleType.values()){
												String checked = "" ;
												if(partner != null){
													if(partner.getStyleCode() != null && partner.getStyleCode().equals(style.getCode())){
														checked = "checked" ;
													}
												}
										%>
											<label class="line-height-1 blue">
												<input name="styleCode" type="radio" class="ace" value="<%=style.getCode() %>" <%=checked %>>
												<span class="lbl"> <%=style.getDesc() %></span>
											</label>
										<%													
											}
										%>
										
									</div>
									<div class="help-inline col-xs-12 col-sm-1 inline help_width">*</div>
									
									<label class="col-xs-12 col-sm-1 control-label no-padding-right">版权起始年份</label>
									<div class="col-xs-12 col-sm-2">
										<input type="text" id="copyrightStartYear" name="copyrightStartYear" class="width-100" value="${partner.copyrightStartYear }" maxlength="4" placeholder="yyyy">		
									</div>
									<div class="help-inline col-xs-12 col-sm-1 inline help_width">*</div>
									
									<label class="col-xs-12 col-sm-1 control-label no-padding-right">网站备案号</label>
									<div class="col-xs-12 col-sm-2">
										<input type="text" id="websiteRecordCode" name="websiteRecordCode" class="width-100"  value="${partner.websiteRecordCode }" maxlength="50">		
									</div>
									<div class="help-inline col-xs-12 col-sm-1 inline help_width">*</div>
								</div>
								
								<!-- row 2 -->
								<div class="form-group has-info">
									
									<label class="col-xs-12 col-sm-1 control-label no-padding-right">访问系统URL：</label>
									<div class="col-xs-12 col-sm-2">
										<input type="text" id="loginUrl" name="loginUrl" class="width-100" value="${partner.loginUrl }" maxlength="100">	
									</div>
									<div class="help-inline col-xs-12 col-sm-4 inline">* (格式：http://www.xxx.xxx&nbsp;或&nbsp;https://www.xxx.xxx&nbsp;或&nbsp;www.xxx.xxx)</div>
								</div>
								
								<!-- row 3 -->
								<div class="form-group has-info">
									
									<label class="col-xs-12 col-sm-1 control-label no-padding-right">LOGO：</label>
									<div class="col-xs-12 col-sm-2">
										<c:if test="${ not empty partner.logoImg }">
											<img src="${pageContext.request.contextPath}/admin/partner/showLogoImage.action?partnerId=${partner.id}" id="logoImg" width="100" height="100"/>
										</c:if>
										<input type="file" name="logoFile" id="logoFile" />	
									</div>
									<div class="help-inline col-xs-12 col-sm-4 inline">*（jpg或png图片）</div>
								</div>
							
							</div>
							<!-- oem个性化设置 end-->
							
							
							
							
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
	<div id="menuContent" class="menuContent" style="display:none; position: absolute;">
        <ul id="treeDemo" class="ztree" style="margin-top:0; width:260px;"></ul>
    </div>
</body>
</html>