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
<title>账号信息</title>
<%@ include file="/WEB-INF/commonjsp/common_css.jsp"%>
<%@ include file="/WEB-INF/commonjsp/common_js.jsp"%>

<link rel="stylesheet" href="${baseurl}dist/less/account.css">

</head>
<script type="text/javascript">

	var msgConfig = {
			  icon: 0,
			  time: 2000 //2秒关闭（如果不配置，默认是3秒）
			};
	
	$(function(){

        <%--$("#name_search_btn").click(function () {--%>
            <%--var partnerName = $("#pName").val();--%>
            <%--location.href =  "${baseurl }front/account/toInfo.action?partnerName="+partnerName;--%>
        <%--});--%>

		//给控件加上错误状态
		var showError = function(obj,msg){
			if($(obj).attr("status") != "error"){
				$(obj).css("border","2px solid #e6686d").after("<span class=\"red_tip\">" + msg + "</span>");
			}
			else{
				$(obj).parent().find("span.red_tip").html(msg);
			}
			$(obj).attr("status","error");
		};
		//清除控件的错误状态
		var cleanError = function(obj){
			$(obj).css("border","1px solid #edeeee");
			$(obj).parent().find("span.red_tip").remove();
			$(obj).attr("status","");
		};
		var cleanAllError = function(form){
			$(form).find("[status=error]").each(function(index,obj){
				cleanError(obj);
			});
		};
		
		$("#email").change(function(){
			if($(this).val()==""){
				showError($(this),"请填写邮箱地址");
				return ;
			}
			if(/^[a-zA-Z0-9_\-]+@[a-zA-Z0-9\-]+(\.[a-zA-Z]{2,4})+$/.test($("#email").val()) == false){
				showError($(this),"邮箱地址格式错误");
				return ;
			}
			cleanError($(this));
		});
		
		$("#userName").change(function(){
			if($("#userName").val()==""){
				showError($(this),"请填写联系人姓名");
				return ;
			}
			if(/^[\u0391-\uFFE5a-zA-Z]{1,50}$/.test($("#userName").val()) == false){
				showError($(this),"联系人姓名只能填写中英文");
				return ;
			}
			cleanError($(this));
		});
		
		$("#mobile").change(function(){
			if($("#mobile").val()==""){
				showError($(this),"请填写手机号");
				return ;
			}
			if(/^\d{11}$/.test($("#mobile").val()) == false){
				showError($(this),"手机号格式错误");
				return ;
			}
			cleanError($(this));
		});
		
		$("#tel").change(function(){
			if($(this).val() !="" ){
				if(/^\d+(-\d+)?$/.test($(this).val()) == false){
					showError($(this),"固定电话格式错误");
					return ;
				}
			}
			cleanError($(this));
		});
		
		$("#fax").change(function(){
			if($(this).val() !="" ){
				if(/^\d+(-\d+)?$/.test($(this).val()) == false){
					showError($(this),"传真电话格式错误");
					return ;
				}
			}
			cleanError($(this));
		});
		
		$("#qq").change(function(){
			if($(this).val() !="" ){
				if(/^\d+$/.test($(this).val()) == false){
					showError($(this),"qq号只能填写数字");
					return ;
				}
			}
			cleanError($(this));
		});
		
		$("#subbtn").click(function(){
			
			$("#email").change();
			$("#userName").change();
			$("#mobile").change();
			$("#tel").change();
			$("#fax").change();
			$("#qq").change();
			
			//console.log($("form").serialize());
			var isValidPass = $("form").find("[status=error]").length > 0 ? false : true ;
			
			if(isValidPass){
				
				$.ajax({
					type: "post",
		            url: "${pageContext.request.contextPath}/front/account/ajaxInfoSave.action",
		            data: $("form").serialize(),
		            success: function (data) {
		            	if(data == "ok"){
		            		layer.msg("修改成功",{icon:1,time:2000});
		            	}
		            	else{
		            		layer.msg("修改失败",msgConfig);
		            	}
		            },
		            error: function (msg) {
		            	layer.msg("修改失败",msgConfig);
		            	console.log("error");
		            	console.log(msg);
		            }
				});
				
			}
			
		});
		
		
		//原密码验证方法
		$("#oldPwd").change(function(){
			
			if($(this).val() == ""){
				//layer.msg("请填写新密码",msgConfig);
				showError($(this),"请填写原密码");
				return ;
			}
			cleanError($(this));
		});
		
		//新密码验证方法
		$("#newPwd").change(function(){
			
			if($(this).val() == ""){
				//layer.msg("请填写新密码",msgConfig);
				showError($(this),"请填写新密码");
				return ;
			}
			var pass = true ;
			
			if($(this).val().match(/^.{6,20}$/) == false){
				pass = false;
			}
			if($(this).val().match(/^\d+$/)){
				pass = false;
			}
			if($(this).val().match(/^[a-zA-Z]+$/)){
				pass = false;
			}
			if(!pass){
				showError($(this),"密码必须是数字字母结合，长度在6-20之间");
				return ;
			}
			
			cleanError($(this));
		});
		
		//确认密码验证方法
		$("#newPwd2").change(function(){
			
			if($(this).val() == ""){
				showError($(this),"请填写确认密码");
				return ;
			}
			
			if($("#newPwd").val() != $(this).val()){
				showError($(this),"两次输入密码不一致");
				return ;
			}
			
			cleanError($(this));
		});
		
		$("#pwdSubbtn").click(function(){
			
			$("#oldPwd").change();
			$("#newPwd").change();
			$("#newPwd2").change();
			
			var isValidPass = $("#changePwd").find("[status=error]").length > 0 ? false : true ;
			
			if(isValidPass){
				$.ajax({
					type: "post",
		            url: "${pageContext.request.contextPath}/front/account/changePassword.action",
		            data: {"oldPassword" : $("#oldPwd").val(),"newPassword" : $("#newPwd").val()} ,
		            success: function (data) {
		            	if(data == "oldpwd"){
		            		showError($("#oldPwd"),"原密码不正确");
		            		//setTimeout(relogin,2000);
		            	}
		            	else if(data == "ok"){
		            		layer.msg("密码修改成功",{icon:1,time:2000});
		            		//setTimeout(relogin,2000);
		            	}
		            	else{
		            		layer.msg("密码修改失败",msgConfig);
		            	}
		            },
		            error: function (msg) {
		            	layer.msg("密码修改失败",msgConfig);
		            	console.log("error");
		            	console.log(msg);
		            }
				});
			}
		});
	});
	/*
	function relogin(){
		location.href = '${pageContext.request.contextPath}/logout.action';
	}
	*/
</script>
<body>
	<!--header 导航 -->
	<jsp:include page="/WEB-INF/front/header.jsp?param=account"></jsp:include>
	
	<div class="wrapper">
        <!-- 当前位置 -->
        <div class="current-position row">
            <div class="col-sm-6">
                <ol class="breadcrumb">
                    <li class="breadcrumb-list">
                        <i class="icons-current"></i>当前位置
                    </li>
                    <li class="breadcrumb-list">
                        <a class="nav-breadcrumb" href="home.html">首页</a>
                        <i class="fa fa-angle-right"></i>
                    </li>
                    <li class="breadcrumb-list">
                        <a class="nav-breadcrumb active">账户管理</a>
                    </li>
                </ol>
            </div>
            
            <jsp:include page="/WEB-INF/front/selectPartner.jsp?fromPage=toInfo"></jsp:include>
            
        </div>
        <!-- 二级连接 -->
        <div class="second-links-wrap">
            <a class="s-links active" href="${pageContext.request.contextPath}/front/account/toInfo.action">账号信息</a>
            <a class="s-links" href="${pageContext.request.contextPath}/front/account/toQualiDoc.action">资质上传</a>
            <a class="s-links" href="${pageContext.request.contextPath}/front/account/toFinance.action">账务明细</a>
            <a class="s-links" href="${pageContext.request.contextPath}/front/account/toGetCode.action">代码获取</a>
        </div>
        <!-- 操作窗 -->
        <div class="handle-view">
            <div class="form-horizontal ">
            	
            	<form id="form1" action="${pageContext.request.contextPath}/front/account/infoSave.action" method="post">
                	
                <input type="hidden" value="${user.id }" name="id">
            	
	                <div class="form-group">
	                    <label class="col-sm-2 control-label">登录账号</label>
	                    <div class="col-sm-10">
	                        <input type="text" class="form-control input_text" value="${user.loginName }" readonly="readonly">
	                    </div>
	                </div>
	                <div class="form-group">
	                    <label class="col-sm-2 control-label">登录密码</label>
	                    <div class="col-sm-10">
	                        <!--<input type="text" class="form-control input_text">-->
	                        <span data-toggle="modal" data-target="#changePwd" class="change-pwd" style="cursor:pointer">修改密码</span>
	                    </div>
	                </div>
	                <div class="form-group">
	                    <label class="col-sm-2 control-label">邮箱地址</label>
	                    <div class="col-sm-10">
	                        <input type="text" class="form-control input_text" name="email" id="email" value="${user.email }" maxlength="50">
	                    </div>
	                </div>
	                <div class="form-group">
	                    <label class="col-sm-2 control-label">用户姓名</label>
	                    <div class="col-sm-10">
	                        <input type="text" class="form-control input_text" name="userName" id="userName" value="${user.userName }" maxlength="50">
	                    </div>
	                </div>
	                <div class="form-group">
	                    <label class="col-sm-2 control-label">手机</label>
	                    <div class="col-sm-10">
	                        <input type="text" class="form-control input_text" name="mobile" id="mobile" value="${user.mobile }" maxlength="20">
	                    </div>
	                </div>
	                <div class="form-group">
	                    <label class="col-sm-2 control-label">固定电话</label>
	                    <div class="col-sm-10">
	                        <input type="text" class="form-control input_text" name="tel" id="tel" value="${user.tel }" maxlength="20">
	                    </div>
	                </div>
	                <div class="form-group">
	                    <label class="col-sm-2 control-label">传真</label>
	                    <div class="col-sm-10">
	                        <input type="text" class="form-control input_text" name="fax" id="fax" value="${user.fax }" maxlength="20">
	                    </div>
	                </div>
	                <div class="form-group">
	                    <label class="col-sm-2 control-label">QQ</label>
	                    <div class="col-sm-10">
	                        <input type="text" class="form-control input_text" name="qq" id="qq" value="${user.qq }" maxlength="20">
	                    </div>
	                </div>
	                <shiro:hasPermission name="user:operable">
		                <div class="btn-arr">
		                    <div class="btn btn-small btn-purple" id="subbtn">确定</div>
		                    <div class="btn btn-small btn-gray">取消</div>
		                </div>
	                </shiro:hasPermission>
	            </form>
            </div>

        </div>
    </div>
    <div class="modal fade" id="changePwd" tabindex="-1" role="dialog" aria-labelledby="changePwd"
         aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title" id="myModalLabel3">
                        修改密码
                    </h4>
                </div>
                <div class="modal-body">
                    <div class="form-horizontal " style="width: 360px">
                        <div class="form-group">
                            <label class="col-sm-2 control-label">用户名</label>
                            <div class="col-sm-10">
                                <span>${user.loginName }</span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">原密码</label>
                            <div class="col-sm-10">
                                <input type="password" class="form-control input_text" name="oldPwd" id="oldPwd" maxlength="20">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">新密码</label>
                            <div class="col-sm-10">
                                <input type="password" class="form-control input_text" name="newPwd" id="newPwd" maxlength="20">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">确认新密码</label>
                            <div class="col-sm-10">
                                <input type="password" class="form-control input_text" name="newPwd2" id="newPwd2" maxlength="20">
                            </div>
                        </div>
                        <div class="clearfix btn-arr">
                            <span class="btn btn-small btn-main" id="pwdSubbtn">确定</span>
                            <span data-dismiss="modal" class="btn btn-small btn-gray">关闭</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- =============================================================================== -->
    
</body>
</html>