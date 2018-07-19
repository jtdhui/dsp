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
    <title>财务报表</title>
    <link rel="stylesheet" href="${baseurl}css/admin/date.css?d=${activeUser.currentTime}">
    <%@ include file="/WEB-INF/commonjsp/admin_common_css.jsp"%>
    <%@ include file="/WEB-INF/commonjsp/admin_common_js.jsp"%>

</head>

<script type="text/javascript">
	$(function() {
        $("#stat_cycle").dateLayer("startDate","endDate",'${queryMap.startDate}','${queryMap.endDate}',"camp");
    });

    function exportExcel(){
        $("#formId").attr("action","${pageContext.request.contextPath}/admin/finance/downloadFinanceReport.action");
        $("#formId").submit();
        $("#formId").attr("action","${pageContext.request.contextPath}/admin/finance/list.action");
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
					<li class="active">财务报表</li>
				</ul>
			</div>

			<div class="page-content">

				<div class="row">
					<div class="col-xs-12">
						<form method="post" action="${baseurl }admin/finance/list.action" id="formId" class="form-horizontal">
							<div class="form-group">
								<label class="col-sm-1 control-label">广告主：</label>
								<input type="text" name="partnerName" class="col-sm-1" value="${queryMap.partnerName }"/>

								<label class="col-sm-1 control-label">渠道：</label>
								<select class="col-sm-1" name="partner_type">
									<option value="">全部</option>
									<option value="0" <c:if test="${queryMap.partner_type == 0}">selected</c:if>>JTD-代理</option>
									<option value="1" <c:if test="${queryMap.partner_type == 1}">selected</c:if>>JTD-直客</option>
									<option value="2" <c:if test="${queryMap.partner_type == 2}">selected</c:if>>直客</option>
								</select>
                                <i class="throw_tit"></i><input class="yen_date" value="" id="stat_cycle">
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
                                            <th rowspan="2" width="4%">ID</th>
                                            <th rowspan="2" width="5%">优化师</th>
                                            <th rowspan="2" style="width:90px">渠道</th>
                                            <th rowspan="2" width="5%">区域</th>
                                            <th rowspan="2" width="5%">分公司</th>
                                            <th rowspan="2" width="7%">大客销售</th>
                                            <th rowspan="2" width="10%">充值公司名称(广告主)</th>
                                            <th rowspan="2" width="10%">总帐户实际到账</th>
                                            <th colspan="2" width="10%">充值金额</th>
                                            <th colspan="2" width="10%">开户费用</th>
                                            <th rowspan="2" width="4%">DSP平台帐面金额</th>
                                            <th rowspan="2" width="4%">DSP平台帐户余额</th>
                                            <th rowspan="2" width="4%">DSP平台消耗</th>
                                            <th rowspan="2" width="3%">备注</th>
                                            <th rowspan="2" width="3%">操作</th>
                                        </tr>
                                        <tr>
                                            <th>推广费</th>
                                            <th>赠送</th>
                                            <th>开户费</th>
                                            <th>技服费</th>
                                        </tr>
										</thead>

										<tbody>
                                            <c:forEach items="${mapList }" var="po">
                                                <tr>
                                                    <td>${po.id }</td>
                                                    <td>${po.user_name }</td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${po.partner_type == 1 and empty po.boss_partner_code }">新米迪-直客</c:when>
                                                            <c:when test="${po.partner_type == 1 and not empty po.boss_partner_code }">中企-直客</c:when>
                                                            <c:otherwise>新米迪-代理</c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>${po.region }</td>
                                                    <td>${po.company_name }</td>
                                                    <td>${po.sales_name }</td>
                                                    <td>${po.partner_name }</td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${po.partner_type == 1}"><a href="finaceDetailList.action?partnerId=${po.partner_id}"><fmt:formatNumber value="${(po.amount+po.open_amount+po.service_amount)/100}" pattern="0.00"/></a></c:when>
                                                            <c:otherwise><a href="proxyFinaceDetailList.action?partnerId=${po.partner_id}"><fmt:formatNumber value="${(po.amount+po.open_amount+po.service_amount)/100}" pattern="0.00"/></a></c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td><fmt:formatNumber value="${po.amount/100}" pattern="0.00"/></td>
                                                    <td><fmt:formatNumber value="${po.pre_gift/100}" pattern="0.00"/></td>
                                                    <td><fmt:formatNumber value="${po.open_amount/100}" pattern="0.00"/></td>
                                                    <td><fmt:formatNumber value="${po.service_amount /100}" pattern="0.00"/></td>
                                                    <td><fmt:formatNumber value="${(po.amount+po.pre_gift)/100}" pattern="0.00"/></td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${po.partner_type == 0}">
                                                                <a href="proxyAccBalanceList.action?partnerId=${po.partner_id}"> <fmt:formatNumber value="${po.acc_balance /100}" pattern="0.00"/> </a>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <fmt:formatNumber value="${po.acc_balance /100}" pattern="0.00"/>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${po.partner_type == 1}"><a href="expendList.action?partnerId=${po.partner_id}"><fmt:formatNumber value="${po.total_expend }" pattern="0.00"/></a></c:when>
                                                            <c:otherwise><a href="proxyExpendList.action?partnerId=${po.partner_id}"><fmt:formatNumber value="${po.total_expend}" pattern="0.00"/></a></c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>${po.remark}</td>
                                                    <td>
                                                        <a class="btn btn-xs btn-yellow" href="../partnerPre/list.action?partnerId=${po.partner_id}">发票信息</a>
                                                        <c:if test="${po.partner_type == 1 and not empty po.boss_partner_code}"><a class="btn btn-xs btn-info" href="brokerage.action?partnerId=${po.partner_id}">返佣金详情</a></c:if>
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