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
<title>资质详情</title>

<%@ include file="/WEB-INF/commonjsp/admin_common_css.jsp"%>
<%@ include file="/WEB-INF/commonjsp/admin_common_js.jsp"%>

<script type="text/javascript">
	$(function(){
		
		//点击小图展现大图
		$(".quali_doc_image").click(function(){
			var tt = $(this).attr("title");
			var src = $(this).find("img").attr("src");
			$("#showImg").find("img").attr("src",src);
			layer.open({
				type: 1,
		        title:tt,
		        shadeClose: true,
		        shade: 0.4,
		        area: ['600px', '600px'],
		        content: $('#showImg')
			});
		});
		
		$("#pass").click(function(){
			$("#rejectRemark").attr("disabled",true);
		});
		$("#reject").click(function(){
			$("#rejectRemark").attr("disabled",false);
		});
		$("#subbtn").click(function(){
			if($("#reject").prop("checked") == true && $("#rejectRemark").val() == ""){
				layer.msg("请填写拒绝原因",msgConfig);
				return ;
			}
			layer.open({
				  type: 3,
				  icon: 2,
				  content: ''
			});
			$("form").submit();
		});
	});
	
	function submitToChannel(partnerId,channelId){
		
		layer.open({
			  type: 3,
			  icon: 2,
			  content: ''
		});
		$("form").attr("action","${baseurl }admin/qualidoc/submitToSingleChannel.action?partnerId=" + partnerId + "&channelId=" + channelId );
		$("form").submit();
	}
	
	function sendMQ(partnerId,channelId){
		
		layer.open({
			  type: 3,
			  icon: 2,
			  content: ''
		});
		$("form").attr("action","${baseurl }admin/qualidoc/sendMqForSingleChannel.action?partnerId=" + partnerId + "&channelId=" + channelId );
		$("form").submit();
	}
	
	function sendInteralMQ(partnerId){
		
		layer.open({
			  type: 3,
			  icon: 2,
			  content: ''
		});
		$("form").attr("action","${baseurl }admin/qualidoc/sendInternalAuditMQ.action?partnerId=" + partnerId );
		$("form").submit();
	}
</script>

</head>

<style type="text/css">
	.help_width { width : 20px ;}
</style>

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
				<script type="text/javascript">
					try {
						ace.settings.check('breadcrumbs', 'fixed')
					} catch (e) {
					}
				</script>

				<ul class="breadcrumb">
					<li><i class="icon-home home-icon"></i><a href="${pageContext.request.contextPath}/admin/qualidoc/list.action">资质管理</a></li>
					<li class="active">资质详情</li>
				</ul>
				<!-- .breadcrumb -->

			</div>
			
			<div class="page-content">
				<div class="row">
					<div class="col-xs-12">
						<form id="form" class="form-horizontal" action="${baseurl }admin/qualidoc/auditSave.action" method="post">
						
							<!-- id -->
							<input type="hidden" name="partnerId" value="${resultMap.partner.id }"/>
							
							<h5 class="lighter block blue">内部审核</h5>
							
							<!-- row 1 -->
							<div class="form-group has-info">
								<c:if test="${ resultMap.internalAuditStatus == 0 }">
									<div class="col-xs-12 col-sm-11">
										<label class="line-height-1 blue">
											<span class="lbl">&nbsp;&nbsp;&nbsp;&nbsp;未提交</span>									
										</label>
									</div>
								</c:if>
								<c:if test="${ resultMap.internalAuditStatus == 1 }">
									<div class="col-xs-12 col-sm-11">
										<label class="line-height-1 blue">
											<input id="pass" name="internalAuditStatus" type="radio" class="ace" value="2" checked>
											<span class="lbl">&nbsp;通过并送渠道审核</span>
										</label>
										<label class="line-height-1 blue">
											<input id="reject" name="internalAuditStatus" type="radio" class="ace" value="3">
											<span class="lbl">&nbsp;拒绝通过</span>
										</label>
										<label class="line-height-1 blue">&nbsp;&nbsp;拒绝原因</label>
										<input type="text" id="rejectRemark" name="rejectRemark" class="width-30"  value="${resultMap.remark }" disabled>
									</div>
								</c:if>
								<c:if test="${ resultMap.internalAuditStatus == 2 }">
									<div class="col-xs-12 col-sm-11">
										<label class="line-height-1 blue">
											<span class="lbl">已通过</span>											
										</label>
										<label class="line-height-1 blue">
											<c:if test="${ resultMap.auditMqSuccess != 1 }">
												<a class="btn btn-xs btn-info" onclick="sendInteralMQ('${partnerId}')">
													重新发送内部审核MQ
												</a>
											</c:if>
										</label>
									</div>
								</c:if>
								<c:if test="${ resultMap.internalAuditStatus == 3 }">
									<div class="col-xs-12 col-sm-11">
										<label class="line-height-1 blue">
											<span class="lbl">已拒绝，</span>											
										</label>
										<label class="line-height-1 blue">拒绝原因：${resultMap.remark }</label>
									</div>
								</c:if>
								
								<c:if test="${ resultMap.internalAuditStatus == 2 || resultMap.internalAuditStatus == 3}">
									<input type="hidden" name="internalAuditStatus" value="${ resultMap.internalAuditStatus }"/>
								</c:if>
							</div>
							
							<h5 class="lighter block blue">广告主基本信息</h5>
							
							<!-- row 2 -->
							<div class="form-group has-info">
								<label class="col-xs-12 col-sm-1 control-label no-padding-right">广告主简称</label>
								<div class="col-xs-12 col-sm-2">
									<input type="text" class="width-100" value="${ resultMap.partner.simpleName }" disabled>					
								</div>
								
								<label class="col-xs-12 col-sm-1 control-label no-padding-right">广告主全称</label>
								<div class="col-xs-12 col-sm-2">
									<input type="text" class="width-100"  value="${ resultMap.partner.partnerName }" disabled>		
								</div>
								
								<label class="col-xs-12 col-sm-1 control-label no-padding-right">所属代理</label>
								<div class="col-xs-12 col-sm-2">
									<input type="text" class="width-100" value="${ resultMap.partner.pName }" disabled>					
								</div>
								
							</div>
							
							<!-- row 3 -->
							<div class="form-group has-info">
								
								<label class="col-xs-12 col-sm-1 control-label no-padding-right">广告主行业</label>
								<div class="col-xs-12 col-sm-2">
									<input type="text" class="width-100" value="${ resultMap.partner.categoryName }" disabled>		
								</div>
								
								<label class="col-xs-12 col-sm-1 control-label no-padding-right">广告主官网名称</label>
								<div class="col-xs-12 col-sm-2">
									<input type="text" class="width-100"  value="${ resultMap.partner.websiteName }" disabled>		
								</div>
								
								<label class="col-xs-12 col-sm-1 control-label no-padding-right">广告主官网地址</label>
								<div class="col-xs-12 col-sm-2">
									<input type="text" class="width-100"  value="${ resultMap.partner.websiteUrl }" disabled>		
								</div>
							</div>
							
							<h5 class="lighter block blue">广告主资质详情</h5>
							
							<div class="table-responsive">
								<table id="sample-table-1" class="table table-striped table-bordered table-hover width-30">
									<thead>
										<tr>
											<th width="30%">资质类型</th>
											<th width="20%">资质文件</th>
											<th width="25%">证件编号</th>
											<th width="25%">有效期</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${resultMap.partnerQualiList }" var="qualiMap">
											<tr>
												<td>${qualiMap.doc_type_name }</td>
												<td>
													<!-- 超链接中是大图 -->
<%-- 													<a class="quali_doc_image" title="${qualiMap.doc_type_name }" href="javascript:void(0)"> --%>
<%-- 														<img src="${pageContext.request.contextPath}/admin/qualidoc/showImage.action?id=${qualiMap.id }" width="50" height="50" alt=""/> --%>
<!-- 													</a> -->
													<a href="${pageContext.request.contextPath}/admin/qualidoc/showImage.action?id=${qualiMap.id }" title="${qualiMap.doc_type_name }" target="_blank">
														<img src="${pageContext.request.contextPath}/admin/qualidoc/showImage.action?id=${qualiMap.id }" width="50" height="50" alt=""/>
													</a>
												</td>
												<td>${qualiMap.doc_number }</td>
												<td>${qualiMap.doc_valid_date }</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>
							
							<h5 class="lighter block blue">渠道审核状态</h5>
							<div class="table-responsive">
								<table id="sample-table-1" class="table table-striped table-bordered table-hover width-50">
									<thead>
										<tr>
											<th width="3%">渠道</th>
											<th width="10%">渠道要求</th>
											<th width="10%">审核状态</th>
											<th width="10%">拒绝原因</th>
											<th width="10%">MQ发送结果</th>
											<th width="10%" align="center">重新提交渠道审核</th>
											<th width="10%" align="center">重新发送渠道MQ</th>
										</tr>
									</thead>

									<tbody>
										<c:forEach items="${resultMap.partnerStatusList }" var="statusMap">
											<tr>
												<td>${statusMap.channel_name }</td>
												<td>${statusMap.remark }</td>
												<td style="font-weight:bold;">
													<c:if test="${ empty statusMap.status }">
														未提交													
													</c:if>
													<c:if test="${ statusMap.status == 1 }">
														<span style="color:#FF9933">待审核</span>															
													</c:if>
													<c:if test="${ statusMap.status == 2 }">
														<span style="color:#009966">通过</span>															
													</c:if>
													<c:if test="${ statusMap.status == 3 }">
														<span style="color:#CC0033">拒绝</span>															
													</c:if>
													<c:if test="${ statusMap.status == 4 }">
														<span style="color:#CC0033">提交失败</span>															
													</c:if>
												</td>
												<td style="font-weight:bold;">${statusMap.audit_info }</td>
												<td style="font-weight:bold;">
													<c:if test="${ empty statusMap.audit_mq_success || statusMap.audit_mq_success == 0 }">
														未发送
													</c:if>
													<c:if test="${ statusMap.audit_mq_success == 1 }">
														<span style="color:#009966">已发送</span>
													</c:if>
												</td>
												<td style="font-weight:bold;">
													<c:if test="${ resultMap.internalAuditStatus == 2 && statusMap.status != 2 }">
														<a class="btn btn-xs btn-info" onclick="submitToChannel('${partnerId}','${statusMap.left_channel_id}')">
															GO
														</a>		
													</c:if>
												</td>
												<td style="font-weight:bold;">
													<c:if test="${ resultMap.internalAuditStatus == 2 && not empty statusMap.audit_mq_success && statusMap.audit_mq_success != 1 }">
														<a class="btn btn-xs btn-info" onclick="sendMQ('${partnerId}','${statusMap.channel_id}')">
															GO
														</a>	
													</c:if>
												</td>
											</tr>
										</c:forEach>
										
									</tbody>
								</table>
							</div>
							
							<div class="clearfix form-actions">
								<div class="col-md-offset-3 col-md-9">
									<button class="btn btn-info" type="button" id="subbtn" <c:if test="${ resultMap.internalAuditStatus == 0 }">disabled</c:if>>
										<i class="icon-ok bigger-110"></i>
										提交
									</button>
								</div>
							</div>
							
						</form>
					</div>
					<!-- /.col -->
				</div>
			</div>
			<!-- /.page-content -->
			</div>
		</div>
	</div>
	
	<div id="showImg" style="display:none">
		<img src="">
	</div>

</body>
</html>