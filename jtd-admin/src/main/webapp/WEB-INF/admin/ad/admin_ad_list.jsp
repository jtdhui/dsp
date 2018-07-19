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
    <title>创意列表</title>
<%@ include file="/WEB-INF/commonjsp/admin_common_css.jsp"%>
<%@ include file="/WEB-INF/commonjsp/admin_common_js.jsp"%>
</head>

<script type="text/javascript">
	$(function(){
		
		//开始日期控件
		laydate({
		  elem: '#internalAuditStartDate',
		  format: 'YYYY-MM-DD', 
		  max: laydate.now() //最大日期
		});
		//开始日期控件
		laydate({
		  elem: '#internalAuditEndDate',
		  format: 'YYYY-MM-DD', 
		  max: laydate.now() //最大日期
		});
		//开始日期控件
		laydate({
		  elem: '#updateStartDate',
		  format: 'YYYY-MM-DD', 
		  max: laydate.now() //最大日期
		});
		//开始日期控件
		laydate({
		  elem: '#updateEndDate',
		  format: 'YYYY-MM-DD', 
		  max: laydate.now() //最大日期
		});

		// 全选 反选
		$("#check_btn").click(function(){

			var flag = $("#check_btn")[0].checked;

			if(!flag ){		//全不选
				$("[name='checkbox']").prop("checked",false);
			}else{		//全选
				$("[name='checkbox']").prop("checked",true);
				$("input:checkbox[name='checkbox']").each(function() {
				    var id = $(this).val();
                    var flag = $("#check_"+id).val();
					if($(this).data("audit")!="0" || flag == "1"){
						$(this)[0].checked = false;
					}
				});
			}
		});

		// 批量审核通过
		$("#batch_agree").click(function () {
			layer.load(2);  //加载层
			var ad_id = "";
			$("input:checkbox[name='checkbox']").each(function(){
				if ($(this)[0].checked ==true) {
					ad_id += $(this).val()+",";
				}
			});

			if (ad_id.length > 1) {
				ad_id = ad_id.substring(0,ad_id.length - 1);
			}else {
				layer.closeAll('loading');
				layer.alert('请至少选择一个广告!', {icon: 6});
				return false;
			}

			$.ajax({
				url: "batchAdAudit.action?ad_ids=" + ad_id,
				type: "post",
				contentType: "application/json;charset=utf-8",
				//请求json数据
				success: function (result) {
					layer.closeAll('loading');
					if (result.success) {
						layer.alert('批量操作成功!', {
							closeBtn: 0
						}, function(){
							location.href=result.url;
						});
					} else {
						layer.alert("批量操作失败!", {icon: 6});
						return false;
					}
				}
			}); // end ajax
		});
		
	});
</script>

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
						href="${pageContext.request.contextPath}">创意管理</a></li>
					<li class="active">创意列表</li>
				</ul>
			</div>

			<div class="page-content">

				<div class="page-header">
					<button class="btn icon-plus btn-primary" id="batch_agree">创意批量审核通过</button>
				</div>

				<div class="row">
					<div class="col-xs-12">
						<form method="post" action="${baseurl }admin/ad/list.action" id="formId" class="form-horizontal">
							<div class="form-group">
								<label class="col-sm-1 control-label">广告主ID：</label>
								<input type="text" name="partnerId" class="col-sm-1" value="${queryMap.partnerId }"/>

								<label class="col-sm-1 control-label">广告主名称：</label>
								<input type="text" name="partnerName" class="col-sm-1" value="${queryMap.partnerName }"/>

								<label class="col-sm-1 control-label">活动ID：</label>
								<input type="text" name="campaignId" class="col-sm-1" value="${queryMap.campaignId }"/>

								<label class="col-sm-1 control-label">活动名称：</label>
								<input type="text" name="campaignName" class="col-sm-1" value="${queryMap.campaignName }"/>

								<label class="col-sm-1 control-label">内部审核状态：</label>
								<select class="col-sm-1" name="internalAuditStatus">
									<option value="">全部</option>
									<option value="0" <c:if test="${queryMap.internalAuditStatus == 0}">selected</c:if>>待审核</option>
									<option value="1" <c:if test="${queryMap.internalAuditStatus == 1}">selected</c:if>>已通过</option>
									<option value="2" <c:if test="${queryMap.internalAuditStatus == 2}">selected</c:if>>已拒绝</option>
								</select>
							</div>

							<div class="form-group">

								<label class="col-sm-1 control-label">内部审核时间：</label>
								<input type="text" name="internalAuditStartDate" id="internalAuditStartDate" class="col-sm-1" value="${queryMap.internalAuditStartDate }"/>
								<label class="col-sm-1" style="width:10px;">-</label>
								<input type="text" name="internalAuditEndDate" id="internalAuditEndDate" class="col-sm-1" value="${queryMap.internalAuditEndDate }"/>

								<label class="col-sm-1 control-label">创意更新时间：</label>
								<input type="text" name="updateStartDate" id="updateStartDate" class="col-sm-1" value="${queryMap.updateStartDate }"/>
								<label class="col-sm-1" style="width:10px;">-</label>
								<input type="text" name="updateEndDate" id="updateEndDate" class="col-sm-1" value="${queryMap.updateEndDate }"/>

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
											<th class="center">
												<label> <input type="checkbox" class="ace" id="check_btn"/> <span class="lbl"></span></label>
											</th>
											<th>创意ID</th>
											<th>广告ID</th>
											<th>尺寸</th>
											<th>广告主ID</th>
											<th>广告主</th>
											<th>活动ID</th>
											<th class="hidden-480">活动名称</th>
											<th>创意更新时间</th>
											<th>内部审核时间</th>
											<th>内部审核状态</th>
											<th>渠道审核状态</th>
										</tr>
										</thead>

										<tbody>
										<c:forEach items="${page.listMap }" var="po">
											<tr>
												<td class="center">
													<label>
														<c:choose>
															<c:when test="${po.partner_id != activeUser.partner.id or  activeUser.roleId==1 or activeUser.roleId==10  }">
                                                                <input type="hidden" id="check_${po.ad_id }" value="0">
																<input name="checkbox" value="${po.ad_id }" type="checkbox" class="ace" data-audit="${po.internal_audit}" />
															</c:when>
															<c:otherwise>
                                                                <input type="hidden" id="check_${po.ad_id }" value="1">
																<input name="checkbox" value="${po.ad_id }" type="checkbox" class="ace" disabled data-audit="${po.internal_audit}" />
															</c:otherwise>
														</c:choose>
														<span class="lbl"></span>
													</label>
												</td>
												<td>${po.id }</td>
												<td>${po.ad_id }</td>
												<td>${po.size }</td>
												<td>${po.partner_id }</td>
												<td>${po.partner_name }</td>
												<td>${po.campaign_id }</td>
												<td class="hidden-480">${po.campaign_name }</td>
												<td>${po.update_time }</td>
												<td>${po.internal_audit_time }</td>
												<td class="center">
													<c:if test="${po.internal_audit ==0}"><div class="text-warning">待审核</div></c:if>
													<c:if test="${po.internal_audit ==1}"><div class="text-success">已通过</div></c:if>
													<c:if test="${po.internal_audit ==2}"><div class="text-danger">已拒绝</div></c:if>
												</td>
												<td class="center">
													<c:choose>
														<c:when test="${po.partner_id != activeUser.partner.id or  activeUser.roleId==1 or activeUser.roleId==10  }">
															<a href="${pageContext.request.contextPath}/admin/ad/adAuditSet.action?id=${po.ad_id }">
																<c:if test="${po.total==0}"><div class="text-warning">未提交</div></c:if>
																<c:if test="${po.sucess_num>=0 and po.total>0 and po.sucess_num < po.total and po.failed_num <po.total}"><div class="text-warning">部分未通过[${po.sucess_num}/${po.total}]</div></c:if>
																<c:if test="${po.sucess_num ==po.total and po.total>0}"><div class="text-success">已通过[${po.sucess_num}/${po.total}]</div></c:if>
																<c:if test="${po.failed_num ==po.total and po.total>0}"><div class="text-danger">已拒绝[${po.sucess_num}/${po.total}]</div></c:if>
															</a>
														</c:when>
														<c:otherwise>
															<c:if test="${po.total==0}"><div class="text-warning">未提交</div></c:if>
															<c:if test="${po.sucess_num>=0 and po.total>0 and po.sucess_num < po.total and po.failed_num <po.total}"><div class="text-warning">部分未通过[${po.sucess_num}/${po.total}]</div></c:if>
															<c:if test="${po.sucess_num ==po.total and po.total>0}"><div class="text-success">已通过[${po.sucess_num}/${po.total}]</div></c:if>
															<c:if test="${po.failed_num ==po.total and po.total>0}"><div class="text-danger">已拒绝[${po.sucess_num}/${po.total}]</div></c:if>
														</c:otherwise>
													</c:choose>
												</td>
											</tr>
										</c:forEach>
										</tbody>
									</table>
									<c:if test="${page != null && fn:length(page.listMap) gt 0}">
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