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
    <title>代理消费明细</title>
    <link rel="stylesheet" href="${baseurl}css/admin/date.css?d=${activeUser.currentTime}">
    <%@ include file="/WEB-INF/commonjsp/admin_common_css.jsp"%>
    <%@ include file="/WEB-INF/commonjsp/admin_common_js.jsp"%>
</head>

<script type="text/javascript">
	$(function() {
        $("#stat_cycle").dateLayer("startDay","endDay",'${paraMap.startDay}','${paraMap.endDay}',"camp");
    });

    function exportExcel(){
        $("#form1").attr("action","${pageContext.request.contextPath}/admin/finance/downloadExpendReport.action");
        $("#form1").submit();
        $("#form1").attr("action","${pageContext.request.contextPath}/admin/finance/proxyExpendList.action");
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
					<li class="active">代理消费明细</li>
				</ul>
			</div>

			<div class="page-content">

				<div class="row">
					<div class="col-xs-12">
						<form method="post" action="${baseurl }/admin/finance/proxyExpendList.action" id="form1" class="form-horizontal">
                            <input type="hidden" id="proxyFlag" name="proxyFlag" value="1">
                            <div class="form-group">
								<label class="col-sm-1 control-label">广告主名称:</label>
								<select class="col-sm-1" name="partnerId" id="pList">
									<c:forEach items="${partners }"  var="po">
										<option value="${po.id }"  <c:if test="${paraMap.partnerId == po.id }">selected</c:if>>${po.partnerName }</option>
									</c:forEach>
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
											<th width="16%">广告主名称</th>
                                            <c:forEach items="${keySet}" var="key" >
											    <th width="7%">${key}</th>
                                            </c:forEach>
										</tr>
										</thead>

										<tbody>
                                        <c:forEach items="${resultList }" var="resultMap">
                                            <tr>
                                                <c:forEach items="${resultMap }" var="map">
                                                    <c:if test="${map.key == 'partnerName'}">
                                                        <td>${map.value}</td>
                                                    </c:if>
                                                </c:forEach>

                                                <c:forEach items="${keySet }" var="key">
                                                    <c:set var="key_val" value="0"></c:set>
                                                    <c:forEach items="${resultMap }" var="map">
                                                        <c:if test="${map.key == key}">
                                                            <c:set var="key_val" value="${map.value}"></c:set>
                                                        </c:if>
                                                    </c:forEach>
                                                    <td><fmt:formatNumber value="${key_val/100}" pattern="0.00"/></td>
                                                </c:forEach>
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