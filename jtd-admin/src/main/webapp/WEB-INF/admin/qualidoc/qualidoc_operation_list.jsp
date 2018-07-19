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
<title>运营资质列表</title>
<%@ include file="/WEB-INF/commonjsp/admin_common_css.jsp"%>
<%@ include file="/WEB-INF/commonjsp/admin_common_js.jsp"%>
</head>
<script type="text/javascript">
function lookLevel(partnerId){
	
	var partnerName = $("#partnerName").val();
	var internalAuditStatus = $("#internalAuditStatus option:selected").val();
	var minus = $("#look_"+partnerId).attr("isOpen");
	if(minus){
		findAndRemove(partnerId);
		$("#look_" + partnerId).attr("isOpen","");
		$("#look_"+partnerId+ ">img").attr("src","${pageContext.request.contextPath}/images/admin/unfolds.png");
	}else{
		$.ajax({
			type:"post",
			url:"${pageContext.request.contextPath}/admin/qualidoc/getChildrenName.action",
			data:{"partnerId":partnerId,"partnerName":partnerName ,"internalAuditStatus":internalAuditStatus},
				success:function(data){
				if(data.success){
				 $.each(data.partner, function (index, vo) {
				     var htmlStr = '<tr id="'+vo.id+'" pid="' + partnerId + '" style="font-weight:bold">';
				     var levelStr = (vo.partner_level-1)*15;
					 if(vo.hasChildren == 1){
						 htmlStr += '<td><a href="javascript:void(0)" style="float:left;margin-right:5px;margin-left:'+levelStr+'px" id="look_'+vo.id+'" onclick="lookLevel(\''+vo.id+'\');">' 
						 htmlStr += '<img alt="展开" src="${pageContext.request.contextPath}/images/admin/unfolds.png"/></a>'+vo.id+'</td>';
					 }else{
						 htmlStr +='<td><img alt="返回" style="float:left;margin-right:5px;margin-left:'+levelStr+'px" src="${pageContext.request.contextPath}/images/admin/minus.png"/>'+vo.id+'</td>'
					 }
						htmlStr += '<td>'+vo.partner_name+'</td>';
						htmlStr += '<td>'+vo.category_name+'</td>';
						htmlStr += '<td>'+vo.pname+'</td>';
						htmlStr += '<td>'+vo.quali_doc_count+'</td>';
						htmlStr += '<td>'+vo.update_time+'</td>';
						htmlStr += '<td>'+vo.internal_audit_time+'</td>';
						if(vo.internal_audit_status == 0){
							htmlStr += '<td style="font-weight:bold;">未提交</td>';
						}
						if(vo.internal_audit_status == 1){
							htmlStr += '<td style="font-weight:bold;"><span style="color:#FF9933">待审核</span></td>';
						}
						if(vo.internal_audit_status == 2){
							htmlStr += '<td style="font-weight:bold;"><span style="color:#009966">已通过</span></td>';
						}
						if(vo.internal_audit_status == 3){
							htmlStr += '<td style="font-weight:bold;"><span style="color:#CC0033">已拒绝</span></td>';
						}
	
						htmlStr += '<td style="font-weight:bold;">';
						htmlStr += '<span style="color:#FF9933">待</span>：'+vo.wait_count+'&nbsp;&nbsp;&nbsp;';
						htmlStr += '<span style="color:#009966">过</span>：'+vo.success_count+'&nbsp;&nbsp;&nbsp;';
						htmlStr += '<span style="color:#CC0033">拒</span>：'+vo.fail_count;
						htmlStr += '</td>'
						//操作
						htmlStr += '<td>';
						htmlStr += '<a class="btn btn-xs btn-info" href="${baseurl}admin/qualidoc/toAudit.action?id='+vo.id+'">进入审核</a>'
						htmlStr += '</td>';
						htmlStr += '</tr>';
						$("#"+partnerId).after(htmlStr);
						$("#look_"+partnerId).attr("isOpen",true);
						$("#look_"+ partnerId + ">img").attr("src","${pageContext.request.contextPath}/images/admin/minus.png");
	                });
				}
			}
		});
	}
}

function findAndRemove(partnerId){
	//console.log($("tr[pid="+partnerId+"]"));
	$("tr[pid="+partnerId+"]").each(function(){
		var id = $(this).attr("id");
		if($("tr[pid="+id+"]").length > 0){
			findAndRemove(id);
		}
		$(this).remove();
	});
}
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
					<li><i class="icon-home home-icon"></i> <a href="${pageContext.request.contextPath}/admin/qualidoc/operationList.action">资质管理</a></li>
					<li class="active">运营资质列表</li>
				</ul>
			</div>

			<div class="page-content">
				<!-- /.page-header -->
				<div class="row">
					<div class="col-xs-12">
						<form id="form" class="form-horizontal" action="${baseurl }admin/qualidoc/operationList.action" method="post">
							<div class="form-group ">
								
								<label class="col-sm-1 control-label">广告主名称：</label>
								<input type="text" id="partnerName" name="partnerName" class="col-sm-2" value="${queryMap.partnerName }"/>
								
								<label class="col-sm-1 control-label">内部审核状态：</label>
								<select class="col-sm-1" name="internalAuditStatus" id="internalAuditStatus">
									<option value="">全部</option>
									<option value="0" <c:if test="${queryMap.internalAuditStatus == 0}">selected</c:if>>未提交</option>
									<option value="1" <c:if test="${queryMap.internalAuditStatus == 1}">selected</c:if>>待审核</option>
									<option value="2" <c:if test="${queryMap.internalAuditStatus == 2}">selected</c:if>>已通过</option>
									<option value="3" <c:if test="${queryMap.internalAuditStatus == 3}">selected</c:if>>已拒绝</option>
								</select>
								
								<span class="col-sm-1 ">
									<button class="btn btn-sm btn-primary icon-search" style="float:right" type="submit">
										查询
									</button>
								</span>
<!-- 								<span class="col-sm-1 "> -->
<!-- 									<button class="btn btn-sm btn-success icon-download-alt" style="float:right" type="submit"> -->
<!-- 										下载报表 -->
<!-- 									</button> -->
<!-- 								</span> -->
							</div>
						</form>
						<div class="row">
							<div class="col-xs-12">
								<div class="table-responsive">
									<table id="sample-table-1" class="table table-striped table-bordered table-hover">
										<thead>
											<tr>
												<th width="12%">广告主ID</th>
												<th width="10%">广告主名称</th>
												<th width="7%">所属行业</th>
												<th width="5">上级代理</th>
												<th width="7%">资质文件数</th>
												<th width="10%">资质更新时间</th>
												<th width="10%">内部审核时间</th>
												<th width="10%">内部审核状态</th>
												<th width="15%">渠道审核状态</th>
												<th width="10%">操作</th>
											</tr>
										</thead>

										<tbody>
											<c:forEach items="${page.listMap }" var="map">
												<tr id="${map.id}" pid="${map.pid }" <c:if test="${not empty map.isTarget}"> style="font-weight:bold" </c:if>>
													<td>
														<c:if test="${not empty map.partner_level }">
															<c:if test="${ not empty map.hasChildren }">
																<a style="float:left;margin-right:5px;margin-left:${(map.partner_level - 1) * 15 }px" href="javascript:void(0)" id="look_${map.id}" onclick="lookLevel('${map.id}');" isOpen="true">
																	<img alt="展开"  src="${pageContext.request.contextPath}\images\admin\minus.png" />
																</a>
															</c:if>
															<c:if test="${ empty map.hasChildren && (map.pid == 0 || map.pid == 1)  }">
																<div style="float:left;margin-right:5px;margin-left:0px">&nbsp;</div>
															</c:if>
															<c:if test="${ empty map.hasChildren && (map.pid != 0 && map.pid != 1)  }">
																<img alt="展开" style="float:left;margin-right:5px;margin-left:${(map.partner_level - 1) * 15 }px" src="${pageContext.request.contextPath}\images\admin\minus.png" />
															</c:if>
														</c:if>
														${map.id }
													</td>
													<td>${map.partner_name }</td>
													<td>${map.category_name }</td>
													<td>${map.pname }</td>
													<td>${map.quali_doc_count }</td>
													<td>${map.update_time }</td>
													<td>${map.internal_audit_time }</td>
													<td style="font-weight:bold;">
														<c:if test="${ map.internal_audit_status == 0 }">
															未提交													
														</c:if>
														<c:if test="${ map.internal_audit_status == 1 }">
															<span style="color:#FF9933">待审核</span>															
														</c:if>
														<c:if test="${ map.internal_audit_status == 2 }">
															<span style="color:#009966">已通过</span>															
														</c:if>
														<c:if test="${ map.internal_audit_status == 3 }">
															<span style="color:#CC0033">已拒绝</span>															
														</c:if>
														
													</td>
													<td style="font-weight:bold;">
														<span style="color:#FF9933">待</span>：${map.wait_count}&nbsp;&nbsp;
														<span style="color:#009966">过</span>：${map.success_count}&nbsp;&nbsp;
														<span style="color:#CC0033">拒</span>：${map.fail_count}
													</td>
													<td>
														<a class="btn btn-xs btn-info" href="${baseurl}admin/qualidoc/toAudit.action?id=${map.id }">
															进入审核
														</a>
													</td>
												</tr>
											</c:forEach>
											
										</tbody>
									</table>
									<c:if test="${page != null && fn:length(page.list) gt 0}">
										${page.adminTreePageHtml}
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