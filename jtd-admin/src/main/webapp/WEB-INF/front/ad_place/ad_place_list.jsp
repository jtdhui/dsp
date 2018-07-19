<%@page import="com.jtd.web.constants.CatgSerial"%>
<%@page import="com.jtd.web.constants.AdType"%>
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
<title>媒体资源</title>
<%@ include file="/WEB-INF/commonjsp/common_css.jsp"%>
<%@ include file="/WEB-INF/commonjsp/common_js.jsp"%>

<link rel="stylesheet" href="${baseurl}dist/less/media.css">

<script src="${baseurl}js/campaignDicData.js"></script>

</head>

<script type="text/javascript">
	
	$(function(){
		
		$("#subbtn").click(function(){
			$("form").attr("action","${baseurl}front/adPlace/toList.action?pageNo=1");
			$("form").submit();
		});
		
		//加载网站类型选项
		var webSiteTypeOption = "<option value=\"\">全部</option>" ;
		for(var key1 in _campaignDicData.websiteTypes){
			var levelOne = _campaignDicData.websiteTypes[key1];
			
			webSiteTypeOption += "<optgroup label=\"" + levelOne.name + "\">" ;
			
			var levelTwoMap = levelOne.subWebsiteTypes ;
			for(var key2 in levelTwoMap){
				var levelTwo = levelTwoMap[key2];
				
				var select = ("${params.webSiteTypeId}" == levelTwo.id) ? "selected" : "" ;
				webSiteTypeOption += "<option value=\"" + levelTwo.id + "\" " + select + ">" + levelTwo.name + "</option>" ;
			}	
			
			webSiteTypeOption += "</optgroup>" ;
		}
		
		$("#webSiteTypeId").html(webSiteTypeOption);
	});

</script>

<body>
	<!--header 导航 -->
	<jsp:include page="/WEB-INF/front/header.jsp?param=adPlace"></jsp:include>
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
                        <a class="nav-breadcrumb active">媒体资源</a>
                    </li>
                </ol>
            </div>
            
            <!-- 切换广告主 -->
            <jsp:include page="/WEB-INF/front/selectPartner.jsp?fromPage=media"></jsp:include>
            
        </div>
        
        <form id="form1" action="${baseurl}front/adPlace/toList.action" method="post">

        <!-- 条件查询 -->
        <div class="form-horizontal clearfix">
            <div class="select-wrap clearfix">
                <label class="select-label">渠道：</label>
                <select class="select-box" name="channelId">
                    <option value="">全部</option>
                    <option value="2" <c:if test="${params.channelId == 2 }">selected</c:if>>BES</option>
                </select>
            </div>
            <div class="select-wrap clearfix">
                <label class="select-label">流量类型：</label>
                <select class="select-box" name="adType">
                    <option value="">全部</option>
					<%
                      AdType[] adtypes = AdType.values();
                      for(AdType at : adtypes){
                        pageContext.setAttribute("atCode", at.getCode());
                    %>
                      <option value="<%=at.getCode() %>" <c:if test="${params.adType == atCode }">selected</c:if>><%=at.getDesc() %></option>
                    <%    
                      }
                    %>
                </select>
            </div>
            <div class="select-wrap clearfix">
                <label class="select-label">网站类型：</label>
                <select class="select-box" name="webSiteTypeId" id="webSiteTypeId">
                </select>
            </div>
            <div class="select-wrap clearfix">
                <label class="select-label">媒体名或广告位名：</label>
                <input type="text" name="filterName" value="${params.filterName }" class="input_text" placeholder="搜索媒体名或广告位名">
            </div>
            <div class="f-r clearfix">
                <span class="btn btn-search btn-purple" id="subbtn">查询</span>
            </div>
        </div>

        <!-- 查询结果 -->
        <div class="details_table">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th width="10%">广告位ID</th>
                    <th width="10%">广告位名称</th>
                    <th width="10%">渠道</th>
                    <th width="10%">流量类型</th>
                    <th width="10%">媒体名称</th>
                    <th width="10%">媒体类型</th>
                    <th width="5%">尺寸</th>
                    <th width="10%">日展现量</th>
                    <th width="10%">参考点击率</th>
                </tr>
                </thead>
                <tbody>
                	<c:forEach items="${page.list }" var="map" varStatus="vs">
                      <tr>
                          <td>${map.channel_ad_place_id }</td>
                          <td>${map.channel_ad_place_name }</td>
                          <td>${map.channel_name }</td>
                          <td>${map.ad_type }</td>
                          <td>${map.web_site }</td>
                          <td>${map.web_site_type }</td>
                          <td>${map.size }</td>
                          <td>${map.daily_traffics }</td>
                          <td>${map.ctr }</td>
                      </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

		<!--分页-->
        ${page.frontPageHtml }
        <!--分页/-->  
        
        </form>
              
    </div>
    <!--index content/-->

</body>
</html>