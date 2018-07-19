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
    <title>返佣金详情</title>
    <link rel="stylesheet" href="${baseurl}css/admin/date.css?d=${activeUser.currentTime}">
    <%@ include file="/WEB-INF/commonjsp/admin_common_css.jsp"%>
    <%@ include file="/WEB-INF/commonjsp/admin_common_js.jsp"%>
</head>

<script type="text/javascript">
	$(function() {
        <%--$("#stat_cycle").dateLayer("startDay","endDay",'${paraMap.startDay}','${paraMap.endDay}',"camp");--%>
    });

    function payMoney(id,partnerId) {
        var parId = $("#parId").val();
        layer.open({
            content: "您确定是要支付佣金么?",
            time: 0, //不自动关闭
            btn: ['确定', '取消'],
            yes: function (index) {
                layer.close(index);
                $.ajax({
                    url: "changeStatus.action?id=" + id,
                    type: "post",
                    contentType: "application/json;charset=utf-8",
                    success: function (result) {
                        if (result.success) {
                            location.href = "brokerage.action?partnerId="+parId;
                        } else {
                            layer.alert(result.message, {icon: 6});
                            return false;
                        }
                    }
                }); // end ajax
            } // end yes
        }); // end layer
    }


    function exportExcel(){
        $("#form1").attr("action","${pageContext.request.contextPath}/admin/finance/downloadBrokerageReport.action");
        $("#form1").submit();
        $("#form1").attr("action","${pageContext.request.contextPath}/admin/finance/brokerage.action");
    }

</script>

<body>
	<!--header 导航，搜索 -->
	<jsp:include page="/WEB-INF/admin/admin_header.jsp"></jsp:include>

	<div class="main-container-inner">
		<a class="menu-toggler" id="menu-toggler" href="#"> <span
			class="menu-text"></span>
		</a>

		<jsp:include page="/WEB-INF/admin/admin_menu.jsp"></jsp:include>
		<div class="main-content">
			<div class="breadcrumbs" id="breadcrumbs">
				<ul class="breadcrumb">
					<li><i class="icon-home home-icon"></i> <a
						href="${pageContext.request.contextPath}">财务管理</a></li>
					<li class="active">返佣金详情</li>
				</ul>
			</div>

			<div class="page-content">
                <input type="hidden" id="parId" value="${paraMap.partnerId}">
				<div class="row">
					<div class="col-xs-12">
						<form method="post" action="${baseurl }/admin/finance/brokerage.action" id="form1" class="form-horizontal">
							<div class="form-group">

								<label class="col-sm-1 control-label">广告主名称:</label>
                                <input type="text" name="partnerName" class="col-sm-3" value="${paraMap.partnerName }"/>
								<%--<select class="col-sm-3" name="partnerId" id="pList">--%>
                                    <%--<option value="" >全部</option>--%>
									<%--<c:forEach items="${partners }"  var="po">--%>
										<%--<option value="${po.id }"  <c:if test="${paraMap.partnerId == po.id }">selected</c:if>>${po.partnerName }</option>--%>
									<%--</c:forEach>--%>
								<%--</select>--%>

                                <label class="col-sm-2 control-label">返佣金状态:</label>
                                <select class="col-sm-1" name="backStatus">
                                    <option value="">全部</option>
                                    <option value="0" <c:if test="${paraMap.backStatus == 0}">selected</c:if>>待返佣金</option>
                                    <option value="1" <c:if test="${paraMap.backStatus == 1}">selected</c:if>>可返佣金</option>
                                    <option value="2" <c:if test="${paraMap.backStatus == 2}">selected</c:if>>已返佣金</option>
                                </select>

                                <span class="col-sm-1 ">
									<button class="btn btn-sm btn-primary icon-search" style="float:right" type="submit">
										查询
									</button>
								</span>

                                <span class="col-sm-1">
									<button class="btn btn-sm btn-success icon-download" style="float:right" type="button" onclick="exportExcel()">
										下载报表
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
											<th width="6%">序号</th>
                                            <th width="16%">日期</th>
                                            <th width="16%">广告主名称</th>
                                            <th width="16%">DSP充值金额</th>
                                            <th width="16%">消耗金额</th>
                                            <th width="15%">返佣金状态</th>
                                            <th width="15%">操作</th>
                                        </tr>
										</thead>

										<tbody>
                                            <c:forEach items="${brokerageList }" var="po">
                                                <tr>
                                                    <td>${po.Id }</td>
                                                    <td><fmt:formatDate pattern="yyyy-MM-dd" value="${po.date }" /></td>
                                                    <td>${po.partnerName }</td>
                                                    <td><fmt:formatNumber value="${po.amount/100}" pattern="0.00"/></td>
                                                    <td><fmt:formatNumber value="${po.expend/100}" pattern="0.00"/></td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${po.status ==0}">
                                                                <c:if test="${po.expend == 0}">待返佣金</c:if>
                                                                <c:if test="${po.expend > 0}">可返佣金</c:if>
                                                            </c:when>
                                                            <c:otherwise>已返佣金</c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>
                                                        <c:if test="${po.status ==0 and po.expend > 0}">
                                                            <button onclick="payMoney(${po.Id},${po.partnerId})" class="btn btn-xs btn-info">支付佣金</button>
                                                        </c:if>
                                                    </td>
										        </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
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

</body>
</html>