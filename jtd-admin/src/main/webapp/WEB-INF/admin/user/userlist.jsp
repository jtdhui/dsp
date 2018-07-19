<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/commonjsp/tag.jsp"%>
<%@ include file="/WEB-INF/commonjsp/common_js.jsp"%>
<%@ taglib uri="http://shiro.apache.org/tags" prefix="shiro" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta HTTP-EQUIV="pragma" CONTENT="no-cache">
    <meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
    <meta HTTP-EQUIV="expires" CONTENT="0">
<title>用户列表</title>

<script type="text/javascript">
	function update(id){
		var obj=new Object();
		obj.id=$("#uid"+id).val();
		obj.partnerId=$("#pid"+id).val();
		obj.loginName=$("#loginName"+id).val();
		obj.pwd=$("#pwd"+id).val();
		obj.userName=$("#userName"+id).val();
		obj.createTime=$("#createTime"+id).val();
		$.ajax({
			url:"${baseurl }admin/user/edit.action",
			type:"post",
			contentType:"application/json;charset=utf-8",
			//请求json数据,使用json表示商品信息
			data:JSON.stringify(obj),
			success:function(data){
				location.href = data.url;
			}
		});
	}
	
	function deleteById(id){
		var obj=new Object();
		obj.id=id;
		$.ajax({
			url:"${baseurl }admin/user/delete.action",
			type:"post",
			contentType:"application/json;charset=utf-8",
			//请求json数据,使用json表示商品信息
			data:JSON.stringify(obj),
			success:function(data){
				location.href = data.url;
			}
		});
	}
</script>
</head>
<body class="skin-2">
<br/>&nbsp;&nbsp;&nbsp;&nbsp;
<a href="${baseurl }admin/user/add.action">添加用户</a>&nbsp;&nbsp;&nbsp;&nbsp;
<a href="${baseurl }admin/user/batchadd.action">批量添加用户</a>
<br/>
<table border="1">
<tr>
	<td>选择</td>
	<td>id</td>
	<td>partnerId</td>
	<td>登陆名</td>
	<td>密码</td>
	<td>用户名</td>
	<td>创建时间</td>
	<td>操作</td>
</tr>
<c:forEach items="${userList }" var="user">
	<tr>
		<input type="hidden" id="uid${user.id }" name="id" value="${user.id }" />
		<td><input type="checkbox" /></td>
		<td>${user.id }</td>
		<td><input id="pid${user.id }" name="partnerId" type="text" value="${user.partnerId }" /></td>
		<td><input id="loginName${user.id }" name="loginName" type="text" value="${user.loginName }" /></td>
		<td><input id="pwd${user.id }" name="pwd" type="text" value="${user.pwd }" /></td>
		<td><input id="userName${user.id }" name="userName" type="text" value="${user.userName }" /></td>
		<td><input id="createTime${user.id }" type="text" name="createTime" value="<fmt:formatDate value="${user.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" /></td>
		<td>
			<shiro:hasPermission name="user:update">
				<a href="javascript:void(0)" onclick="update(${user.id })">修改</a>
			</shiro:hasPermission>
			<a href="javascript:void(0)" onclick="deleteById(${user.id})">删除</a>
		</td>
	</tr>
</c:forEach>
</table>
</body>
</html>