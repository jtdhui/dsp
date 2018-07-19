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
<title>代码获取</title>
<%@ include file="/WEB-INF/commonjsp/common_css.jsp"%>
<%@ include file="/WEB-INF/commonjsp/common_js.jsp"%>
    <script type="text/javascript">
        $(function() {
            $(".tool_rt_tit").hide();
        });
    </script>
</head>

<body>
	<!--header 导航 -->
	<jsp:include page="/WEB-INF/front/header.jsp?param=sales"></jsp:include>
	<!--index content-->
    <div class="w1000 clearfix">
        <!--面包屑-->
        <div class="breadcrumb"><span>当前位置</span><a href="">首页</a>&gt;<a href="">营销工具</a></div>
        <!--面包屑/-->
        <!--营销工具content-->
        <div class="tool_con">
            <!--基础应用-->
            <div class="clearfix">
                <div class="tool_con_tit">基础应用</div>
                <div class="tool_div">
                    <ul>
                        <li>
                            <img src="${baseurl}images/front/temp/gj_img.png">
                            <div class="tool_rt">
                                <p class="tool_rt_tit">人群管家（建设中）</p>
                                <p class="tool_rt_pre">建设中</p>
                            </div>
                        </li>
                        <li>
                            <img src="${baseurl}images/front/temp/gj_img.png">
                            <div class="tool_rt">
                                <p class="tool_rt_tit">创意助手（建设中）</p>
                                <p class="tool_rt_pre">建设中</p>
                            </div>
                        </li>
                        <li>
                            <img src="${baseurl}images/front/temp/gj_img.png">
                            <div class="tool_rt">
                                <p class="tool_rt_tit">转化跟踪</p>
                                <p class="tool_rt_pre">建设中</p>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
            <!--基础应用/-->
            <!--优化服务-->
            <div class="clearfix">
                <div class="tool_con_tit">优化服务</div>
                <div class="tool_div">
                    <ul>
                        <li>
                            <a href="">
                                <img src="${baseurl}images/front/temp/gj_img.png">
                                <div class="tool_rt">
                                    <p class="tool_rt_tit">品牌保护盾（建设中）</p>
                                    <p class="tool_rt_pre">建设中</p>
                                </div>
                            </a>
                        </li>
                        <li>
                            <a href="">
                                <img src="${baseurl}images/front/temp/gj_img.png">
                                <div class="tool_rt">
                                    <p class="tool_rt_tit">媒体锦囊（建设中）</p>
                                    <p class="tool_rt_pre">建设中</p>
                                </div>
                            </a>
                        </li>
                        <li>
                            <a href="">
                                <img src="${baseurl}images/front/temp/gj_img.png">
                                <div class="tool_rt">
                                    <p class="tool_rt_tit">中期数据分析工具（建设中）</p>
                                    <p class="tool_rt_pre">建设中</p>
                                </div>
                            </a>
                        </li>
                        <li>
                            <a href="">
                                <img src="${baseurl}images/front/temp/gj_img.png">
                                <div class="tool_rt">
                                    <p class="tool_rt_tit">中企DMP产品（建设中）</p>
                                    <p class="tool_rt_pre">建设中</p>
                                </div>
                            </a>
                        </li>
                        <li>
                            <a href="">
                                <img src="${baseurl}images/front/temp/gj_img.png">
                                <div class="tool_rt">
                                    <p class="tool_rt_tit">投放体验报告（建设中）</p>
                                    <p class="tool_rt_pre">建设中</p>
                                </div>
                            </a>
                        </li>
                        <li>
                            <a href="">
                                <img src="${baseurl}images/front/temp/gj_img.png">
                                <div class="tool_rt">
                                    <p class="tool_rt_tit">自定义（建设中）</p>
                                    <p class="tool_rt_pre">建设中</p>
                                </div>
                            </a>
                        </li>
                    </ul>
                </div>
            </div>
            <!--优化服务/-->
        </div>
        <!--营销工具content/-->
    </div>
    <!--index content/-->
	<jsp:include page="/WEB-INF/front/footer.jsp"></jsp:include>

</body>
</html>