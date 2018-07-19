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
<title>财务明细</title>
<%@ include file="/WEB-INF/commonjsp/common_css.jsp"%>
<%@ include file="/WEB-INF/commonjsp/common_js.jsp"%>

<link rel="stylesheet" href="${baseurl}dist/less/account-toFinance.css">
<script src="${baseurl}js/front/date_layer.js"></script>

</head>


<script type="text/javascript">
	$(function(){
		
		$("#form_date").dateLayer("startDate","endDate","${params.startDate}","${params.endDate}","report");
		$("#subbtn").click(function(){
			$("#form1").submit();
		});
		
		$("#reportbtn").click(function(){
			
			$("#form1").attr("action", "${pageContext.request.contextPath}/front/account/getFinanceExcel.action");
			$("#form1").submit();
			$("#form1").attr("action", "${pageContext.request.contextPath}/front/account/toFinance.action");
			
		});

        <%--$("#name_search_btn").click(function () {--%>
            <%--var partnerName = $("#pName").val();--%>
            <%--location.href =  "${baseurl }front/account/toFinance.action?partnerName="+partnerName;--%>
        <%--});--%>
	});
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
                        <a class="nav-breadcrumb" href="account.html">账户管理</a>
                        <i class="fa fa-angle-right"></i>
                    </li>
                    <li class="breadcrumb-list">
                        <a class="nav-breadcrumb active">账务明细</a>
                    </li>
                </ol>
            </div>
            
            <jsp:include page="/WEB-INF/front/selectPartner.jsp?fromPage=toFinance"></jsp:include>
            
        </div>

        <!-- 二级连接 -->
        <div class="second-links-wrap">
            <a class="s-links" href="${pageContext.request.contextPath}/front/account/toInfo.action">账号信息</a>
            <a class="s-links" href="${pageContext.request.contextPath}/front/account/toQualiDoc.action">资质上传</a>
            <a class="s-links active" href="${pageContext.request.contextPath}/front/account/toFinance.action">账务明细</a>
            <a class="s-links" href="${pageContext.request.contextPath}/front/account/toGetCode.action">代码获取</a>
        </div>

        

        <!-- 操作窗 -->
        <div class="handle-view">
        	
        	<form id="form1" action="${pageContext.request.contextPath}/front/account/toFinance.action" method="post">
        	
            <div class="account-money"><span>账户余额：</span><span class="money">￥${amount }元</span></div>
            
            <!-- 搜索条件 -->
            <div class="form-horizontal clear">
                <div class="select-wrap m-t-xs ml-167">
                    <label class="select-label">分类：</label>
                    <select class="select-box" name="tradeType">
                        <option value="">所有</option>
                        <option value="0" <c:if test="${params.tradeType==0}">selected</c:if>>充值</option>
                        <option value="1" <c:if test="${params.tradeType==1}">selected</c:if>>广告消费</option>
                        <option value="2" <c:if test="${params.tradeType==2}">selected</c:if>>其他消费</option>
                    </select>
                </div>
                <div class="select-wrap m-t-xs mt-50">
                    <label class="select-label">日期：</label>
                    <input class="yen_date" value="" id="form_date" readonly="readonly" style="cursor: pointer;">
                </div>
                <div class="select-wrap">
                    <div class="btn btn-small btn-purple" id="subbtn">查询</div>
                </div>
                <div class="select-wrap">
                    <div class="btn btn-small btn-gray" id="reportbtn">下载报表</div>
                </div>
            </div>
	            
            <!-- 搜索结果 -->
            <div class="details_table">
                <table class="table table-striped table-hover">
                    <thead>
                    <tr>
                        <th width="20%">日期</th>
                        <th width="20%">分类</th>
                        <th width="30%">金额</th>
                        <th width="30%">账户余额</th>
                    </tr>
                    </thead>
                    <tbody>
	                    <c:forEach items="${page.list }" var="po"> 
	                      	<tr>
	                           <td><fmt:formatDate value="${po.tradeTime }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
	                           <td>
	                           	<c:if test="${po.tradeType == 0 }">
	                           		充值
	                           	</c:if>
	                           	<c:if test="${po.tradeType == 1 }">
	                           		广告消费
	                           	</c:if>
	                           	<c:if test="${po.tradeType == 2 }">
	                           		其他消费
	                           	</c:if>
	                           </td>
	                           <td>￥${po.amountYuan }</td>
	                           <td class="balance">￥${po.accBalanceResultYuan }</td>
                    	 	</tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
        
        <!-- 分页 -->
        ${page.frontPageHtml }
        
        </form>
        
	</div>
	
</body>
</html>