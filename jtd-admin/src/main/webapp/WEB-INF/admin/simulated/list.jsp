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
    <title>测试帐户模拟投放</title>
    <%@ include file="/WEB-INF/commonjsp/admin_common_css.jsp"%>
    <%@ include file="/WEB-INF/commonjsp/admin_common_js.jsp"%>
</head>
<script type="text/javascript">

    function edit(id,partnerId) {
        location.href="edit.action?id="+id+"&partnerId="+partnerId;
    }

    function closeData(id) {
        layer.open({
            content: '你确定停止自动生成广告主投放数据么？',
            time: 0, //不自动关闭
            btn: ['确定', '取消'],
            yes: function (index) {
                layer.close(index);
                location.href = "delete.action?id=" + id;
            }
        });
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
						href="${pageContext.request.contextPath}">系统工具</a></li>
					<li class="active">测试帐户模拟投放</li>
				</ul>
			</div>

			<div class="page-content">
				<!-- /.page-header -->
				<div class="row">
					<div class="col-xs-12">
						<form id="form" class="form-horizontal" action="${baseurl }admin/simulated/list.action" method="post">
							<div class="form-group ">
								<label class="col-sm-2 control-label">广告主名称：</label>
								<input type="text" name="partnerName" class="col-sm-2" value="${queryMap.partnerName }"/>
								
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
												<th width="5%">ID</th>
                                                <th width="10%">广告主ID</th>
												<th width="20%">广告主名称</th>
                                                <th width="13%">PV（每活动每小时）</th>
												<th width="13%">PV转化UV</th>
                                                <th width="20%">CLICK转化UNCLICK</th>
                                                <th width="10%">毛利率</th>
												<th width="13%">自动生成</th>
											</tr>
										</thead>

										<tbody>
											<c:forEach items="${page.list }" var="po">
												<tr>
													<td>${po.id }</td>
                                                    <td>${po.partner_id }</td>
													<td>${po.partnerName }</td>
													<td>${po.min_pv }-${po.max_pv }</td>
													<td>${po.min_uv_ratio }-${po.max_uv_ratio }</td>
													<td>${po.min_uclick_ratio }-${po.max_uclick_ratio }</td>
                                                    <td>${po.gross_profit}</td>
													<td>
                                                        <c:if test="${po.id == null}">
                                                            <button class="btn btn-xs btn-success" onclick="edit('',${po.partner_id})">
                                                                开启
                                                            </button>
                                                        </c:if>
                                                        <c:if test="${po.id != null}">
                                                            <button class="btn btn-xs btn-success" onclick="edit(${po.id},${po.partner_id})">
                                                                修改
                                                            </button>
                                                            <button class="btn btn-xs btn-danger" onclick="closeData(${po.id})">
                                                                关闭
                                                            </button>
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

	<!--预充值-->
	<div id="pre_amount_window" class="layer_check_type" style="display:none;">
		<input name="pre_partner_id" id="pre_partner_id" value="" type="hidden">
		<input name="pre_partner_name" id="pre_partner_name" value="" type="hidden">
		<div class="page-content">
			<div class="row">
				<div class="col-xs-12">
					<div class="form-group has-info" style="margin-top:20px;">
						<label class="col-xs-12 col-sm-2 control-label no-padding-right">充值金额</label>
						<div class="col-xs-12 col-sm-4">
							<input type="text" id="pre_amount" name="pre_amount" class="width-100" value="" >
						</div>
						<div class="help-inline col-xs-12 col-sm-1 inline help_width">*</div>
						<div style="display:none;" id="tip_span"></div>
					</div>

					<div class="clearfix">
						<div class="col-md-offset-3 col-md-9" style="margin-top:10px;">
							<input type="button" value="确 定" class="btn btn-info" id="pre_amount_conform">
							<input type="reset" value="取 消" class="btn" id="pre_amount_canncel">
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!--预充值/-->

	<!--发起退款-->
	<div id="refund_amount_window" class="layer_check_type" style="display:none;">
		<input name="refund_partner_id" id="refund_partner_id" value="" type="hidden">
		<input name="refund_partner_name" id="refund_partner_name" value="" type="hidden">
		<div class="page-content">
			<div class="row">
				<div class="col-xs-12">
					<div class="form-group has-info" style="margin-top:20px;">
						<label class="col-xs-12 col-sm-2 control-label no-padding-right">退款金额</label>
						<div class="col-xs-12 col-sm-4">
							<input type="text" id="refund_amount" name="pre_amount" class="width-100" value="" >
						</div>
						<div class="help-inline col-xs-12 col-sm-1 inline help_width">*</div>
						<div style="display:none;" id="refund_span"></div>
					</div>

					<div class="clearfix">
						<div class="col-md-offset-3 col-md-9" style="margin-top:10px;">
							<input type="button" value="确 定" class="btn btn-info" id="refund_amount_conform">
							<input type="reset" value="取 消" class="btn" id="refund_amount_canncel">
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!--发起退款-->
</body>
</html>