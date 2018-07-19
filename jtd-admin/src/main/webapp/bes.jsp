<%@page import="java.util.HashMap"%>
<%@page import="com.jtd.web.service.adx.bes.BaiduUtil"%>
<%@page import="com.jtd.utils.HTTPUtil"%>
<%@page import="com.jtd.web.service.AuditResult"%>
<%@page import="java.util.Map"%>
<%@page import="com.alibaba.fastjson.JSONObject"%>
<%@page import="com.jtd.utils.SpringContextHolder"%>
<%@page import="com.jtd.web.service.adx.bes.BaiduAPIService"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
	BaiduAPIService service = (BaiduAPIService) SpringContextHolder.getBean("baiduAPIService");
	String result = "" ;

	String action = request.getParameter("action") != null ? request.getParameter("action") : "" ;
	
	int partnerId = request.getParameter("partnerId") != null ? Integer.parseInt(request.getParameter("partnerId")) : 0 ;
	String startDate = request.getParameter("startDate") != null ? request.getParameter("startDate") : "2016-12-01" ;
	String endDate = request.getParameter("endDate") != null ? request.getParameter("endDate") : "2016-12-30" ;
	
	if(action.equals("queryAllAdvertiser")){
		JSONObject jsonObj = service.queryAllAdvertiser(startDate, endDate);
		if(jsonObj != null){
			result = jsonObj.toJSONString();
		}
	}
	
	if(action.equals("queryAdvertiser")){
		JSONObject jsonObj = service.queryAdvertiserById(partnerId);		
		if(jsonObj != null){
			result = jsonObj.toJSONString();
		}
	}
	
	if(action.equals("queryAdvertiserAudit")){
		AuditResult auditResult = service.queryAdvertiserAuditStatus(partnerId);
		if(auditResult != null){
			result = JSONObject.toJSONString(auditResult);
		}
	}
	
	if(action.equals("queryAllCreative")){
		
		JSONObject jsonObj = service.queryAllCreative(startDate, endDate);
		if(jsonObj != null){
			result = jsonObj.toJSONString();
		}
	}
	
	int adId = request.getParameter("adId") != null ? Integer.parseInt(request.getParameter("adId")) : 1 ;
	if(action.equals("queryCreative")){
		JSONObject jsonObj = service.queryCreativeById(adId);
		if(jsonObj != null){
			result = jsonObj.toJSONString();
		}
	}
	if(action.equals("queryCreativeAudit")){
		AuditResult auditResult = service.queryCreativeAuditStatus(adId);
		if(auditResult != null){
			result = JSONObject.toJSONString(auditResult);
		}
	}
	
	int pageIndex = request.getParameter("pageIndex") != null ? Integer.parseInt(request.getParameter("pageIndex")) : 1 ;
	int pageSize = request.getParameter("pageSize")  != null ? Integer.parseInt(request.getParameter("pageSize")) : 10 ;
	if(action.equals("sspSetting")){
		JSONObject jsonObj = service.getSyncSSPSetting(pageIndex, pageSize);
		if(jsonObj != null){
			result = jsonObj.toJSONString();
		}
	}
	
	if(action.equals("reportRTB")){
		JSONObject jsonObj = service.reportRTB(startDate, endDate);
		if(jsonObj != null){
			result = jsonObj.toJSONString();
		}
	}
	
	if(action.equals("reportConsume")){
		JSONObject jsonObj = service.reportConsume(startDate, endDate);
		if(jsonObj != null){
			result = jsonObj.toJSONString();
		}
	}
	
	if(action.equals("reportAdvertiser")){
		JSONObject jsonObj = service.reportAdvertiserConsume(startDate, endDate);
		if(jsonObj != null){
			result = jsonObj.toJSONString();
		}
	}
	
	if(action.equals("reportCreativeRTB")){
		JSONObject jsonObj = service.reportCreativeRTB(startDate, endDate);
		if(jsonObj != null){
			result = jsonObj.toJSONString();
		}
	}
	
	if(action.equals("syncAdvertiser")){
		int i = service.syncAdvertiser(1, partnerId, "");
		result = i + "" ;
	}
	
%>

<!DOCTYPE html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta HTTP-EQUIV="pragma" CONTENT="no-cache">
    <meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
    <meta HTTP-EQUIV="expires" CONTENT="0">
    <title></title>
</head>

<body style="">
	<div>
    	queryAllAdvertiser:
    	<form action="" method="post">
        <input type="hidden" name="action" value="queryAllAdvertiser"/>
        startDate: <input type="text" name="startDate" value="<%=startDate%>"/><br/>
        endDate: <input type="text" name="endDate" value="<%=endDate%>"/><br/>
        <input type="submit" value="ok"/>
        </form>
    </div>
    <div>
    	queryAdvertiser:
    	<form action="" method="post">
        partnerId: <input type="text" name="partnerId" value="<%=partnerId%>"><br/>
        <input type="hidden" name="action" value="queryAdvertiser"/>
        <input type="submit" value="ok"/>
        </form>
    </div>
    <div>
    	queryAdvertiserAudit:
    	<form action="" method="post">
        partnerId: <input type="text" name="partnerId" value="<%=partnerId%>"><br/>
        <input type="hidden" name="action" value="queryAdvertiserAudit"/>
        <input type="submit" value="ok"/>
        </form>
    </div>
    <div>
    	queryAllCreative:
    	<form action="" method="post">
        <input type="hidden" name="action" value="queryAllCreative"/>
        <input type="submit" value="ok"/>
        </form>
    </div>
    <div>
    	queryCreative:
    	<form action="" method="post">
        adId: <input type="text" name="adId" value="<%=adId%>"><br/>
        <input type="hidden" name="action" value="queryCreative"/>
        <input type="submit" value="ok"/>
        </form>
    </div>
    <div>
    	queryCreativeAudit:
    	<form action="" method="post">
        adId: <input type="text" name="adId" value="<%=adId%>"><br/>
        <input type="hidden" name="action" value="queryCreativeAudit"/>
        <input type="submit" value="ok"/>
        </form>
    </div>
    <div>
    	sspSetting:
    	<form action="" method="post">
        pageIndex: <input type="text" name="pageIndex" value="<%=pageIndex%>"><br/>
        pageSize: <input type="text" name="pageSize" value="<%=pageSize%>"><br/>
        <input type="hidden" name="action" value="sspSetting"/>
        <input type="submit" value="ok"/>
        </form>
    </div>
    <div>
    	reportRTB:
    	<form action="" method="post">
        <input type="hidden" name="action" value="reportRTB"/>
        startDate: <input type="text" name="startDate" value="<%=startDate%>"/><br/>
        endDate: <input type="text" name="endDate" value="<%=endDate%>"/><br/>
        <input type="submit" value="ok"/>
        </form>
    </div>
    <div>
    	reportConsume:
    	<form action="" method="post">
        <input type="hidden" name="action" value="reportConsume"/>
        startDate: <input type="text" name="startDate" value="<%=startDate%>"/><br/>
        endDate: <input type="text" name="endDate" value="<%=endDate%>"/><br/>
        <input type="submit" value="ok"/>
        </form>
    </div>
    <div>
    	reportAdvertiser:
    	<form action="" method="post">
        <input type="hidden" name="action" value="reportAdvertiser"/>
        startDate: <input type="text" name="startDate" value="<%=startDate%>"/><br/>
        endDate: <input type="text" name="endDate" value="<%=endDate%>"/><br/>
        <input type="submit" value="ok"/>
        </form>
    </div>
    <div>
    	reportCreativeRTB:
    	<form action="" method="post">
        <input type="hidden" name="action" value="reportCreativeRTB"/>
        startDate: <input type="text" name="startDate" value="<%=startDate%>"/><br/>
        endDate: <input type="text" name="endDate" value="<%=endDate%>"/><br/>
        <input type="submit" value="ok"/>
        </form>
    </div>
    <div>
        <%=action%>:<br/>
        <%=result %>
    </div>
    
    <div>
    	重提资质:
    	<form action="" method="post">
        <input type="hidden" name="action" value="syncAdvertiser"/>
        partnerId: <input type="text" name="partnerId" value="<%=partnerId%>"><br/>
        <input type="submit" value="ok"/>
        </form>
    </div>
    <div>
        <%=action%>:<br/>
        <%=result %>
    </div>
</body>