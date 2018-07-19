<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/commonjsp/tag.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta HTTP-EQUIV="pragma" CONTENT="no-cache">
    <meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
    <meta HTTP-EQUIV="expires" CONTENT="0">
    <title>帐户流水</title>
    <%@ include file="/WEB-INF/commonjsp/admin_common_css.jsp"%>
    <%@ include file="/WEB-INF/commonjsp/admin_common_js.jsp"%>
</head>
<body>
<!--header 导航，搜索 -->
<jsp:include page="/WEB-INF/admin/admin_header.jsp"></jsp:include>

<div class="main-container">
    <a class="menu-toggler" id="menu-toggler" href="#"> <span
            class="menu-text"></span>
    </a>

    <jsp:include page="/WEB-INF/admin/admin_menu.jsp"></jsp:include>
    <div class="main-content">
    	
    	<div class="main-container-inner">
    	
        <div class="breadcrumbs" id="breadcrumbs">
            <ul class="breadcrumb">
                <li>
                    <i class="icon-home home-icon"></i>
                    <a href="${pageContext.request.contextPath}">财务管理</a>
                </li>
                <li class="active">帐户流水</li>
            </ul>
        </div>

        <div class="page-content">
            <!-- /.page-header -->
            <div class="row">
                <div class="col-xs-12">
                    <form id="form" class="form-horizontal" action="${baseurl }admin/partnerPre/waterList.action" method="post">
                        <div class="form-group ">
                            <label class="col-sm-1 control-label">状态：</label>
                            <select class="col-sm-1" name="status">
                                <option value="">全部</option>
                                <option value="0" <c:if test="${queryMap.status == 0}">selected</c:if>>已提交</option>
                                <option value="1" <c:if test="${queryMap.status == 1}">selected</c:if>>同意</option>
                                <option value="2" <c:if test="${queryMap.status == 2}">selected</c:if>>拒绝</option>
                            </select>

                            <span class="col-sm-1 ">
                                <button class="btn btn-sm btn-primary icon-search" style="float:right" type="submit">
                                    查询
                                </button>
                            </span>
                        </div>
                    </form>
                    <div class="row">
                        <div class="col-xs-12">
                            <div class="table-responsive">
                                <table id="sample-table-1" class="table table-striped table-bordered table-hover">
                                    <thead>
                                    <tr>
                                        <th width="3%">ID</th>
                                        <th width="5%">客户ID</th>
                                        <th width="10%">客户名称</th>
                                        <th width="6%">提交金额</th>
                                        <th width="10%">提交操作人</th>
                                        <th width="6%">审核金额</th>
                                        <th width="10%">审核人</th>
                                        <th width="6%">状态</th>
                                        <th width="15%">备注</th>
                                    </tr>
                                    </thead>

                                    <tbody>

                                    <c:forEach items="${page.list }" var="po">
                                        <tr>
                                            <td>${po.id }</td>
                                            <td>${po.partnerId }</td>
                                            <td>${po.partnerName }</td>
                                            <td><fmt:formatNumber value="${po.preAmount/100}" pattern="0.00"/></td>
                                            <td>${po.preOperatorName }</td>
                                            <td><fmt:formatNumber value="${po.amount/100}" pattern="0.00"/></td>
                                            <td>${po.operatorName }</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${po.status == 0 }">
                                                        已提交
                                                    </c:when>
                                                    <c:when test="${po.status == 1 }">
                                                        同意
                                                    </c:when>
                                                    <c:otherwise>
                                                        拒绝
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>${po.remark }</td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                                <c:if test="${page != null && fn:length(page.list) gt 0}">
                                    ${page.pageHtml}
                                </c:if>
                            </div>
                            <!-- /.table-responsive -->
                        </div>
                        <!-- /span -->
                    </div>
                    <!-- /row -->
                </div>
            </div>

        </div>
        <!-- /.page-content -->
        </div>
    </div>
</div>


</body>
</html>
