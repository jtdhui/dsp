<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/commonjsp/tag.jsp"%>
<%
    response.setHeader("Cache-Control","no-store");
    response.setHeader("Pragrma","no-cache");
    response.setDateHeader("Expires",0);
    
	String param = request.getParameter("param");
	if(param == null){
		param = "" ;
	}
%>
<!--头部/-->
<script type="text/javascript">

	//退出系统方法
	function logout() {
		layer.confirm("您确定要退出本系统吗?",{title:'提示'},function(){
			location.href = '${pageContext.request.contextPath}/logout.action';
		});
	}
	$(function(){
		var style_code = String("${activeUser.partner.styleCode}");
		var skin_style = 'danlan';
		switch(style_code){
			case 'blue':
				skin_style = 'danlan';
				break;
			case 'green':
				skin_style = 'huanglv';
				break;
			case 'red':
				skin_style = 'dahong';
				break;
			case 'yellow':
				skin_style = 'qianhuang';
				break;
		}

		laydate.skin(skin_style);

		showPartnerAccount();
		//每10分钟刷新余额
		setInterval(showPartnerAccount,60000 * 10);
	});
	
	function showPartnerAccount(){
		$.ajax({
			type: "post",
            url: "${pageContext.request.contextPath}/front/account/ajaxGetPartner.action",
            success: function (data) {
            	//console.log(data);
            	if(data){
            		$("#partnerAccount").text(data.accBalanceYuan);
            	}
            },
            error: function (msg) {
            	console.log("error");
            	console.log(msg);
            }
		});
	}
</script>
<!--头部-->

<header class="header">
    <div class="navbar-static-top nav-top">
        <ol class="user-list-wrap">
            <li class="user-list"><i class="icons icons-1"></i>您好：<span>${activeUser.userName}</span></li>
            <li class="user-list"><i class="icons icons-2"></i>账户余额 ：<span>${activeUser.favPartner.accBalance/100 }</span></li>
            <shiro:hasPermission name="admin">
                <c:if test="${activeUser.partner.status == 0}">
                	<li class="user-list"><a href="${baseurl}admin/home.action" style="color:#fff"><i class="icons icons-3"></i>切换后台</a></li>
				</c:if>
			</shiro:hasPermission>
            <li class="user-list"><a href="javascript:" id="loginOut" onclick="logout()" style="color:#fff"><i class="icons icons-4"></i>退出</a></li>
        </ol>
    </div>
    <nav class="navbar-static-top nav-center">
        <div class="nav-wrapper">
            <ul class="nav-list-wrap">
                <li class="nav-list" <% if(param.equals("home")){ %> class="active" <% } %>>首页</li>
                <shiro:hasPermission name="camp">
                	<li class="nav-list" <% if(param.equals("campaign")){ %> class="active" <% } %>>投放管理</li>
                </shiro:hasPermission>
                <shiro:hasPermission name="report">
                	<li class="nav-list" <% if(param.equals("report")){ %> class="active" <% } %>>数据中心</li>
                </shiro:hasPermission>
                <shiro:hasPermission name="adPlace">
                	<li class="nav-list" <% if(param.equals("adPlace")){ %> class="active" <% } %>>媒体资源</li>
                </shiro:hasPermission>
                <shiro:hasPermission name="account">
                	<li class="nav-list" <% if(param.equals("account")){ %> class="active" <% } %>>账户管理</li>
                </shiro:hasPermission>
            </ul>
			<% if(param.equals("home")){ %>
				<a class="nav-hover" href="${baseurl}front/home.action">首页</a>
			<% } %>
			<% if(param.equals("campaign")){ %>
				<a class="nav-hover" href="${baseurl}front/campManage/camp_list.action">投放管理</a>
			<% } %>
			<% if(param.equals("report")){ %>
				<a class="nav-hover" href="${baseurl}front/campManage/report/time.action">数据中心</a>
			<% } %>
			<% if(param.equals("adPlace")){ %>
				<a class="nav-hover" href="${baseurl}front/adPlace/toList.action">媒体资源</a>
			<% } %>
			<% if(param.equals("account")){ %>
				<a class="nav-hover" href="${baseurl}front/account/toInfo.action">账户管理</a>
			<% } %>
        </div>
    </nav>
</header>




