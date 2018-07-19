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
    <title>充值/退款</title>
    <%@ include file="/WEB-INF/commonjsp/admin_common_css.jsp"%>
    <%@ include file="/WEB-INF/commonjsp/admin_common_js.jsp"%>
</head>
<body class="skin-2">
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
                <li><i class="icon-home home-icon"></i> <a
                        href="${pageContext.request.contextPath}">财务管理</a></li>
                <li class="active">充值/退款</li>
            </ul>
        </div>

        <div class="page-content">
            <!-- /.page-header -->
            <div class="row">
                <div class="col-xs-12">
                    <form id="form" class="form-horizontal" action="${baseurl }admin/partnerPre/list.action" method="post">
                        <div class="form-group ">
                            <label class="col-sm-1 control-label">广告主：</label>
                            <input type="text" name="partnerName" class="col-sm-2" value="${queryMap.partnerName }"/>
                            <label class="col-sm-2 control-label">状态：</label>
                            <select class="col-sm-1" name="status">
                                <option value="">全部</option>
                                <option value="0" <c:if test="${queryMap.status == 0}">selected</c:if>>已提交</option>
                                <option value="1" <c:if test="${queryMap.status == 1}">selected</c:if>>同意</option>
                                <option value="2" <c:if test="${queryMap.status == 2}">selected</c:if>>拒绝</option>
                            </select>
                            <label class="col-sm-2 control-label">流水类型：</label>
                            <select class="col-sm-1" name="type">
                                <option value="">全部</option>
                                <option value="0" <c:if test="${queryMap.type == 0}">selected</c:if>>充值</option>
                                <option value="1" <c:if test="${queryMap.type == 1}">selected</c:if>>退款</option>
                            </select>

                        </div>
                        <div class="form-group ">

                            <label class="col-sm-1 control-label">发票号码：</label>
                            <input type="text" name="invoice" class="col-sm-2"  value="${queryMap.invoice }"/>

                            <label class="col-sm-2 control-label">付款方式：</label>
                            <select class="col-sm-1" name="payType">
                                <option value="">全部</option>
                                <option value="0" <c:if test="${queryMap.payType == 0}">selected</c:if>>未付</option>
                                <option value="1" <c:if test="${queryMap.payType == 1}">selected</c:if>>已付</option>
                                <option value="2" <c:if test="${queryMap.payType == 2}">selected</c:if>>后付</option>
                            </select>

                            <label class="col-sm-2 control-label">发票状态：</label>
                            <select class="col-sm-1" name="isInvoice">
                                <option value="">全部</option>
                                <option value="0" <c:if test="${queryMap.isInvoice == 0}">selected</c:if>>未开</option>
                                <option value="1" <c:if test="${queryMap.isInvoice == 1}">selected</c:if>>已开</option>
                                <option value="2" <c:if test="${queryMap.isInvoice == 2}">selected</c:if>>不开</option>
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
                                        <th width="5%">广告主ID</th>
                                        <th width="10%">广告主名称</th>
                                        <th width="6%">提交金额</th>
                                        <th width="10%">提交操作人</th>
                                        <th width="6%">流水类型</th>
                                        <th width="6%">审核金额</th>
                                        <th width="10%">审核操作人</th>
                                        <th width="6%">状态</th>
                                        <th width="6%">是否开具发票</th>
                                        <th width="10%">发票号码</th>
                                        <th width="5%">付款方式</th>
                                        <th width="10%">备注</th>
                                        <th width="7%">操作</th>
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
                                            <td>
                                                <c:choose>
                                                    <c:when test="${po.type == 0 }">
                                                        充值
                                                    </c:when>
                                                    <c:otherwise>
                                                        退款
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
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
                                            <td>
                                                <c:choose>
                                                    <c:when test="${po.isInvoice == 0 }">
                                                        未开
                                                    </c:when>
                                                    <c:when test="${po.isInvoice == 1 }">
                                                        已开
                                                    </c:when>
                                                    <c:otherwise>
                                                        不开
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>${po.invoice }</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${po.payType == 0 }">
                                                        未付
                                                    </c:when>
                                                    <c:when test="${po.payType == 1 }">
                                                        先付
                                                    </c:when>
                                                    <c:otherwise>
                                                        后付
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>${po.remark }</td>
                                            <td>
                                                <c:if test="${po.status ==0 and po.type==0}">
                                                    <a class="btn btn-xs btn-success"  href="${pageContext.request.contextPath}/admin/partner/recharge.action?id=${po.id}">
                                                        充值
                                                    </a>
                                                </c:if>
                                                <c:if test="${po.status ==0 and po.type==1}">
                                                    <a class="btn btn-xs btn-danger"  href="${pageContext.request.contextPath}/admin/partner/refund.action?id=${po.id}">
                                                        退款
                                                    </a>
                                                </c:if>
                                                <c:if test="${po.status ==1 and po.type==0}">
                                                    <a class="btn btn-xs btn-success"  href="${pageContext.request.contextPath}/admin/partnerPre/invoice.action?id=${po.id}">
                                                        发票明细
                                                    </a>
                                                </c:if>
                                            </td>
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
