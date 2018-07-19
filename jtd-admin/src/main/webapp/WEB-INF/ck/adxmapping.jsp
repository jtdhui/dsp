<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.jtd.web.constants.Constants"%>
<%@ include file="/WEB-INF/commonjsp/tag.jsp"%>
<%@ include file="/WEB-INF/commonjsp/common_js.jsp"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta HTTP-EQUIV="pragma" CONTENT="no-cache">
    <meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
    <meta HTTP-EQUIV="expires" CONTENT="0">
<title>优先展示广告</title>

<%
	String partnerId = request.getParameter("pid");
	String campaignId = request.getParameter("campid");
%>

<script type="text/javascript">
	
	$(function(){
		
		$.ajax({
			type:"post",
			url:"${pageContext.request.contextPath}/ck/getParChanneldList.action",
			data:{"partnerId":"${pid}" },
 			success:function(data){
 				
 				$("#tb1 tbody").html("");
 				
				if(data.code == 1){
				 	$.each(data.list, function (index, vo) { 
					    var htmlStr = '<tr>';
						htmlStr += '<td align="center">'+ vo.partner_id + '</td>';
						htmlStr += '<td align="center"><a href="http://'+ vo.host +'" target="_blank">' + vo.host + '</a></td>';
						htmlStr += '<td align="center">'+vo.date +'</td>';
						htmlStr += '</tr>';
						$("#tb1 tbody").html(htmlStr);
                    });
				}
				else{
					layer.msg("获取广告主[<%=partnerId %>]展示位置失败");
				}
			}
		});
		
	});
	
</script>
</head>
<body >
<%

if(Constants.BES_ISMAPPING.equals("1")){
	String url=Constants.BES_MAPPING_URL;
	String id=Constants.BES_MAPPING_DSPID;
	String fullUrl=url+id;
	out.println("<img src='"+fullUrl+"' />");
}

if(Constants.XTRADER_ISMAPPING.equals("1")){
	String url=Constants.XTRADER_MAPPING_URL;
	String id=Constants.XTRADER_MAPPING_DSPID;
	String fullUrl=url+id;
	out.println("<img src='"+fullUrl+"' />");
}

if(Constants.TANX_ISMAPPING.equals("1")){
	String url=Constants.TANX_MAPPING_URL;
	String id=Constants.TANX_MAPPING_DSPID;
	String fullUrl=url+id;
	out.println("<img src='"+fullUrl+"' />");
}

if(Constants.VAM_ISMAPPING.equals("1")){
	String url=Constants.VAM_MAPPING_URL;
	String id=Constants.VAM_MAPPING_DSPID;
	String fullUrl=url+id;
	out.println("<img src='"+fullUrl+"' />");
}

if(Constants.HZ_ISMAPPING.equals("1")){
	String url=Constants.HZ_MAPPING_URL;
	String id=Constants.HZ_MAPPING_DSPID;
	String fullUrl=url+id;
	out.println("<img src='"+fullUrl+"' />");
}

out.println("<h1>cookie mapping设置成功</h1>");
//out.println("<h2>请从优化师处获取PC端展示的媒体地址。</h2>");
%>

<table id="tb1" width="500">
	<thead>
		<tr>
			<td align="center">广告主id</td>
			<td align="center">展示位置</td>
			<td align="center">日期</td>
		</tr>
	</thead>
	<tbody>
		
	</tbody>
</table>

</body>
</html>
