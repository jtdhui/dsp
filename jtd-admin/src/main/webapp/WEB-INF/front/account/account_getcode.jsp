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
<title>代码获取</title>
<%@ include file="/WEB-INF/commonjsp/common_css.jsp"%>
<%@ include file="/WEB-INF/commonjsp/common_js.jsp"%>

<link rel="stylesheet" href="${baseurl}dist/less/account-toGetCode.css">

</head>

<script type="text/javascript">
	

	$(function(){
		
		$(".copy_code_btn").click(function(){
			
			var input = $("#code_div textarea:visible")[0];
			input.select();
			document.execCommand("Copy");

			layer.msg("复制成功！", {
				icon : 1,
				time : 1000
			});
			
		});

        <%--$("#name_search_btn").click(function () {--%>
            <%--var partnerName = $("#pName").val();--%>
            <%--location.href =  "${baseurl }front/account/toGetCode.action?partnerName="+partnerName;--%>
        <%--});--%>
        
        $("#code1").click(function(){
        	
        	$("#code_div textarea").hide();
			$('#global_text').show();
			$("#code_title").text("全站访客找回");
        	
        });
	 	$("#code2").click(function(){
        	
	 		$("#code_div textarea").hide();
			$('#register_intention_text').show();
			$("#code_title").text("注册意向找回");
        	
        });
		$("#code3").click(function(){
        	
			$("#code_div textarea").hide();
			$('#register_success_text').show();	
			$("#code_title").text("注册成功找回");
        	
        });
		$("#code4").click(function(){
        	
			$("#code_div textarea").hide();
			$('#ask_text').show();
			$("#code_title").text("咨询找回");
        	
        });
	}); 

</script>

<body>
	<!--header 导航 -->
	<jsp:include page="/WEB-INF/front/header.jsp?param=account"></jsp:include>
	<!--index content-->
    
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
                        <a class="nav-breadcrumb" href="account.html">账户管理</a>
                        <i class="fa fa-angle-right"></i>
                    </li>
                    <li class="breadcrumb-list">
                        <a class="nav-breadcrumb active">代码获取</a>
                    </li>
                </ol>
            </div>
            
            <jsp:include page="/WEB-INF/front/selectPartner.jsp?fromPage=toGetCode"></jsp:include>
            
        </div>
        <!-- 二级连接 -->
        <div class="second-links-wrap">
            <a class="s-links" href="${pageContext.request.contextPath}/front/account/toInfo.action">账号信息</a>
            <a class="s-links" href="${pageContext.request.contextPath}/front/account/toQualiDoc.action">资质上传</a>
            <a class="s-links" href="${pageContext.request.contextPath}/front/account/toFinance.action">账务明细</a>
            <a class="s-links active" href="${pageContext.request.contextPath}/front/account/toGetCode.action">代码获取</a>
        </div>
        <!-- 操作窗 -->
        <div class="handle-view">
            <table class="table table-striped">
                <thead>
	                <tr>
	                    <th width="20%">代码名称</th>
	                    <th width="20%">代码类型</th>
	                    <th width="30%">加载方式</th>
	                    <th width="30%">获取代码</th>
	                </tr>
				</thead>
                <tbody>
	                <tr>
                        <td>全站访客找回</td>
                        <td>基本监测代码</td>
                        <td>内置</td>
                        <td><a class="getCode" data-toggle="modal" data-target="#getCodeModal" id="code1">获取代码</a></td>
                    </tr>
                    <tr>
                        <td>注册意向找回</td>
                        <td>高级监测代码</td>
                        <td>内置</td>
                        <td><a class="getCode" data-toggle="modal" data-target="#getCodeModal" id="code2">获取代码</a></td>
                    </tr>
                    <tr>
                        <td>注册成功找回</td>
                        <td>高级监测代码</td>
                        <td>内置</td>
                        <td><a class="getCode" data-toggle="modal" data-target="#getCodeModal" id="code3">获取代码</a></td>
                    </tr>
                    <tr>
                        <td>咨询找回</td>
                        <td>高级监测代码</td>
                        <td>内置</td>
                        <td><a class="getCode" data-toggle="modal" data-target="#getCodeModal" id="code4">获取代码</a></td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
    
    <!--index content/-->
	
	<!--获取代码-->
<div class="modal fade" id="getCodeModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel3" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">
                    获取代码
                </h4>
            </div>
            <div class="modal-body">
                <div id="code_div" class="layer_get_code">
                    <div class="get_code_con">
                        <p class="get_code_tit">
                            <span id="code_title">全站访客找回</span>
                            <i id="copyCode" class="btn btn-small btn-main">复制代码</i>
                        </p>
<textarea id="global_text" style="display:none;">
<c:if test="${ not empty globalGid }">
&lt;script type="text/javascript"&gt;
(function() {
var s = document.createElement('script');
s.type = 'text/javascript';
s.async = true;
s.src = ('https:'==document.location.protocol ? 'https' : 'http') + '://${ trackerUrl }?p=${ activeUser.partner.id }&c=${ globalGid }';
var h = document.getElementsByTagName('head')[0];
if(h) h.appendChild(s);
})();
&lt;/script&gt;</c:if><c:if test="${ empty globalGid }">生成代码失败，请重刷页面或联系工作人员</c:if></textarea>
            
            <textarea id="register_intention_text" style="display:none;">
<c:if test="${ not empty registerIntentionGid }">
&lt;script type="text/javascript"&gt;
window._gkarr = window._gkarr || [];
window._gkarr.push({
type: 3,
pid: ${ activeUser.partner.id },
gid: ${ registerIntentionGid }
});
&lt;/script&gt;</c:if><c:if test="${ empty registerIntentionGid }">生成代码失败，请重刷页面或联系工作人员</c:if></textarea>
            
            <textarea id="register_success_text" style="display:none;">
<c:if test="${ not empty registerSuccessGid }">
&lt;script type="text/javascript"&gt;
window._gkarr = window._gkarr || [];
window._gkarr.push({
type: 4,
pid: ${ activeUser.partner.id },
gid: ${ registerSuccessGid }, 
userid:'#YOUR_USER_ID#' // 需要替换为注册成功后，贵方网站为用户分配的ID，字符串 - 必填
});
&lt;/script&gt;</c:if><c:if test="${ empty registerSuccessGid }">生成代码失败，请重刷页面或联系工作人员</c:if></textarea>
            
            <textarea id="ask_text" style="display:none;">
<c:if test="${ not empty askGid }">
&lt;script type="text/javascript"&gt;
window._gkarr = window._gkarr || [];
window._gkarr.push({
type: 7,
pid: ${ activeUser.partner.id },
gid: ${ askGid },
askid:"#YOUR_ASK_ID#"  // 需要替换为贵方网站为用户分配的咨询ID，字符串 - 选填
});
&lt;/script&gt;
</c:if><c:if test="${ empty askGid }">生成代码失败，请联系重刷页面或工作人员</c:if></textarea>
                        <div class="get_code_explain">
                            <p>部署说明：</p>
                            <p>1、将代码添加至网站全部页面的&lt;/head&gt;标签前，如果您还使用了其他统计代码，请将代码放在其他代码之前。</p>
                            <p>2、如果网页有共同的包含文件，如类似header.jsp、footer.htm等的页脚文件，建议在此文件中安装。</p>
                            <p>3、如需要在js文件中调用系统分析代码，请直接去掉代码首尾的&lt;script type="text/javascript"&gt;与&lt;/script&gt;后，放入js文件中即可。</p>
                            <p>4、如果网站使用框架&lt;iframe&gt;，根据需要在框架或者子页面中分别加入跟踪代码。</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div> 
<!--获取代码 /-->
	
</body>
</html>