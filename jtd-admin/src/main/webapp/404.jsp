<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="false" %>
<!DOCTYPE html>
<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta HTTP-EQUIV="pragma" CONTENT="no-cache">
    <meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
    <meta HTTP-EQUIV="expires" CONTENT="0">
    <title>页面找不到了</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/front/error.css">
</head>

<body>
    <div class="fzf">
        <div class="w1000">
            <!--404-->
            <div class="fzf_img" style="display: block;"><span>404</span><i>Not Fond</i></div>
            <!--404/-->
            <div class="fzf_tip">
                <span><i></i>请检查您输入的地址是否正确</span>
                <span><i></i>可能网速过慢，或网络中断</span>
                <span><i></i>可能页面已被删除</span>
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

</html>