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
<title>添加用户</title>
</head>
<body class="skin-2">
<form id="form" action="${baseurl }admin/user/addsubmit.action" method="post">
<table border="1">

<tr>
	<td>partnerid：</td>
	<td><input type="text" name="partnerId" value="1" />
</tr>
<tr>
	<td>用户名：</td>
	<td><input type="text" name="userName" />
</tr>
<tr>
	<td>密码：</td>
	<td><input type="text" name="pwd" />
</tr>
<tr>
	<td>登陆名：</td>
	<td><input type="text" name="loginName" />
</tr>
<tr>
	<td>eamil：</td>
	<td><input type="text" name="email" value="aaa@aaa.com"/>
</tr>
<tr>
	<td>mobile：</td>
	<td><input type="text" name="mobile" value="1391009483"/>
</tr>
<tr>
	<td>tell：</td>
	<td><input type="text" name="tell" value="98767564"/>
</tr>
<tr>
	<td>qq：</td>
	<td><input type="text" name="qq" value="546376453"/>
</tr>
<tr>
	<td>创建时间：</td>
	<td><input type="text" name="createTime" value="2016-10-05 00:00:00"/>
</tr>
<tr>
<td colspan="2" align="center">
<input type="submit" value="提交"/>
</td>
</tr>
</table>
</form>
</body>
</html>