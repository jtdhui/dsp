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
<title>前台首页</title>

<script type="text/javascript" src="${baseurl}js/jquery/jquery-1.9.1.min.js"></script>
<script src="${baseurl}layer/layer.js?d=${d}"></script>
<link rel="stylesheet" href="${baseurl}dist/less/login.css?d=${d}">
	
<script type="text/javascript">

	$(document).ready(function(){
		
		if("${msg}" != ""){
			layer.msg("${msg}");
		}
		
		$("#username,#password,#randomcode").keypress(function(e){
			if(e.keyCode == 13){
				loginsubmit();
			}
		});
		
		$("#randomcode_img").click(function(){
			$(this).attr("src","${baseurl}validatecode.jsp?"+Math.random());
		});
	});

	//登录提示方法
	function loginsubmit() {
		if($("#username").val() == ""){
			layer.msg("请输入用户名");
			return ;
		}
		if($("#password").val() == ""){
			layer.msg("请输入密码");
			return ;
		}
// 		if($("#randomcode").val() == ""){
// 			layer.msg("请输入验证码");
// 			return ;
// 		}
		
		$("#loginform").submit();

	}
	
	//刷新验证码
	function randomcode_refresh(){
		$("#randomcode_img").attr("src","${baseurl}validatecode.jsp?"+Math.random());
	}
</script>
</head>
<body >
	<div class="container">
	    <div class="login-wrap">
	        <h2 class="login-title">JTD</h2>
	        <pre class="logon-des">DSP PLATFORM</pre>
		        <form id="loginform" name="loginform" action="${baseurl}login.action" method="post">
			        <div class="user">
			            <i class="icon-user icon-name"></i>
			            <input class="input" type="text" name="username" id="username" placeholder="请输入用户名">
			        </div>
			        <div class="user">
			            <i class="icon-user icon-pwd"></i>
			            <input class="input" type="password" name="password" id="password" placeholder="请输入密码">
			        </div>
			        <div class="other-wrap">
			            <div class="remember-pwd">
		<!-- 	                <i class="icon-radio"></i> -->
			                <input type="checkbox" name="rememberMe" value="1">
			                &nbsp;<span>30天免登录</span>
			            </div>
		<!-- 	            <a class="forget-pwd" href="">忘记密码?</a> -->
			        </div>
			        
			    </form>
	        	<span class="btn" onclick="loginsubmit()" style="cursor:pointer">登&nbsp;&nbsp;录</span>
	    </div>
	</div>
<script>
    $(function () {
        $('.remember-pwd').on('click', function () {
            if ($(this).is('.active')) {
                $(this).removeClass('active');
            } else {
                $(this).addClass('active');
            }
        })
    });

</script>
</body>
</html>
