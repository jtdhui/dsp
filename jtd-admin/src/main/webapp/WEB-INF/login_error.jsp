<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta HTTP-EQUIV="pragma" CONTENT="no-cache">
    <meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
    <meta HTTP-EQUIV="expires" CONTENT="0">
    <title>登录错误</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/front/error.css">
</head>
<body>
    <div class="fzf">
        <div class="w1000">
            <!--其他提示语-->
            <div class="fzf_other" style="display: block;">${ message }</div>
            <!--其他提示语/-->
            <div class="fzf_choose">
                <a href="#" target="_blank"><i id="mes">3</i>秒跳转到登录页</a>
            </div>
            <div class="below"></div>
            <div class="above"></div>
        </div>
    </div>
    <script language="javascript" type="text/javascript">
    var i = 3;
    var intervalid;
    intervalid = setInterval("fun()", 1000);

    function fun() {
        if (i == 0) {
            window.location.href = "${pageContext.request.contextPath}/logout.action";
            clearInterval(intervalid);
        }
        document.getElementById("mes").innerHTML = i;
        i--;
    }
    </script>
</body>