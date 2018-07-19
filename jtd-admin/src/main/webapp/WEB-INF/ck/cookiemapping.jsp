<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.jtd.web.constants.Constants"%>
<%@ include file="/WEB-INF/commonjsp/tag.jsp"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta HTTP-EQUIV="pragma" CONTENT="no-cache">
    <meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
    <meta HTTP-EQUIV="expires" CONTENT="0">
<title>优先展示广告</title>


<script type="text/javascript">
	
</script>
</head>
<body >
	<br/>
	<br/>
	<div>
		
		<%
			String info=(String)request.getAttribute("info");
			if(info!=null){
				out.print("<div>");
				out.print(info);
				out.print("</div>");
			}
			
			String tag=(String)request.getAttribute("tag");
			if(tag!=null&&tag.equals("1")){
				String pid=(String)request.getAttribute("pid");
				String campid=(String)request.getAttribute("campid");
				String trackerUrl=Constants.TRACKER_URL_CC;
				trackerUrl=trackerUrl+"?pid="+pid+"&cid="+campid+"&callback="+Constants.CALLBACK_URL_CC;
				response.sendRedirect(trackerUrl);
			}
		%>
	
		<form action="${baseurl }ck/writecookie.action">
		<table>
			<tr>
				<td>广告主id:</td>
				<td>
					<input type="text" name="pid" />
					
					<input type="hidden" name="campid" value="0" />
				</td>
			</tr>
<!-- 			<tr> -->
<!-- 				<td>活动id:</td> -->
<!-- 				<td> -->
<!-- 					<input type="text" name="campid" value="0" /> -->
<!-- 				</td> -->
<!-- 			</tr> -->
			<tr>
				<td>
				<input type="submit" onclick="提交"/>
				</td>
			</tr>
		</table>
		</form>
		
		<br/>
		<br/>
		<%
			String trackerUrlx=Constants.TRACKER_URL_CC;
			trackerUrlx=trackerUrlx+"?del=1&callback="+Constants.CALLBACK_URL_CC;
		%>
		<a href="<%=trackerUrlx %>" >解除当前用户优先权</a>
	
	</div>
</body>
</html>
