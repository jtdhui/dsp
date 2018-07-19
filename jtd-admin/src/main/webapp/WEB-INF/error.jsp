<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="false" %>

<!DOCTYPE html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta HTTP-EQUIV="pragma" CONTENT="no-cache">
    <meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
    <meta HTTP-EQUIV="expires" CONTENT="0">
    <title>发生了错误...</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/front/error.css">
</head>

<body>
    <div class="fzf">
        <div class="w1000">
            <!--500-->
            <div class="fzf_img" style="display: block;"><span style="font-size:60px;line-height:100px;">${message}</span></div>
            <!--500/-->
            <div class="fzf_tip">
                <span><i></i>请您稍后重新再试，或联系我们的工作人员</span>
            </div>
            <div class="fzf_choose">
                <span>您可以：</span>
                <a href="javascript:history.back()">返回上一页</a>
                <a href="#" target="_blank"><i id="mes">3</i>秒跳转到首页</a>
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
            window.location.href = "${pageContext.request.contextPath}/front/home.action";
            clearInterval(intervalid);
        }
        document.getElementById("mes").innerHTML = i;
        i--;
    }
    </script>
</body>