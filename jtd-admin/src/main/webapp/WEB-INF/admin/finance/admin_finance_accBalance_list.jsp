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
    <title>代理广告主余额</title>
    <link rel="stylesheet" href="${baseurl}css/admin/date.css?d=${activeUser.currentTime}">
    <%@ include file="/WEB-INF/commonjsp/admin_common_css.jsp"%>
    <%@ include file="/WEB-INF/commonjsp/admin_common_js.jsp"%>
</head>

<script type="text/javascript">


</script>

<body>
	<!--header 导航，搜索 -->
	<jsp:include page="/WEB-INF/admin/admin_header.jsp"></jsp:include>

	<div class="main-container-inner">
		<a class="menu-toggler" id="menu-toggler" href="#">
            <span class="menu-text"></span>
		</a>

		<jsp:include page="/WEB-INF/admin/admin_menu.jsp"></jsp:include>
		<div class="main-content">
			<div class="breadcrumbs" id="breadcrumbs">
				<ul class="breadcrumb">
					<li><i class="icon-home home-icon"></i> <a
						href="${pageContext.request.contextPath}">财务管理</a></li>
					<li class="active">代理广告主余额</li>
				</ul>
			</div>

			<div class="page-content">

				<div class="row">
					<div class="col-xs-12">
						<form method="post" action="${baseurl }/admin/finance/list.action" id="form1" class="form-horizontal">
                            <div class="form-group">
                                <span class="col-sm-1">
									<button class="btn btn-sm btn-primary icon-search" style="float:left" type="submit">
										返回
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
											<th width="70%">广告主名称</th>
                                            <th width="30%">帐户余额</th>
										</tr>
										</thead>

										<tbody>
                                        <c:forEach items="${list }" var="po">
                                            <tr>
                                                <td>${po.partnerName}</td>
                                                <td><fmt:formatNumber value="${po.accBalance /100}" pattern="0.00"/> </td>
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