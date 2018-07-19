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
<title>广告主列表</title>
<%@ include file="/WEB-INF/commonjsp/admin_common_css.jsp"%>
<%@ include file="/WEB-INF/commonjsp/admin_common_js.jsp"%>
</head>
<script type="text/javascript">

	function updateStatus(partnerId,pid){
		
		var status = $("#" + partnerId + "_status_btn").attr("nextStatus");
		
		$.ajax({
			type:"post",
			url:"${pageContext.request.contextPath}/admin/partner/changeStatus.action",
			data:{"id":partnerId , "status":status, "pid":pid },
			success:function(data){
				
				if(data.flag){
					if(data.msg == "ok"){
/*
						layer.open({
							content: "广告主状态修改成功",
							time: 0, //不自动关闭
								btn: ['确定', '取消'],
								yes: function (index) {
									layer.close(index);
									
								}
						});
						*/
						
						layer.alert('广告主状态修改成功', {
							closeBtn: 0
						}, function(){
							location.href = "${pageContext.request.contextPath}/admin/partner/list.action";
						});
					}else{
						/*
						layer.open({
							content: "当前广告主修改成功,该广告主下面的广告主部分失败,明细如下: " + data.msg,
							time: 0, //不自动关闭
							btn: ['确定', '取消'],
							yes: function (index) {
								layer.close(index);
								location.href = "${pageContext.request.contextPath}/admin/partner/list.action";
							}
						});
						*/
						
						layer.alert("广告主状态修改存在失败情况,明细如下: " + data.msg , {
							closeBtn: 0
						}, function(){
							location.href = "${pageContext.request.contextPath}/admin/partner/list.action";
						});
					}

//					if(status == 0){
//						$("#" + partnerId + "_status").text("启用");
//						$("#" + partnerId + "_status_btn").text("停用").attr("nextStatus",1).removeClass("btn-success").addClass("btn-danger");
//					}
//					else{
//						$("#" + partnerId + "_status").text("停用");
//						$("#" + partnerId + "_status_btn").text("启用").attr("nextStatus",0).removeClass("btn-danger").addClass("btn-success");
//					}
				}
				else{
					layer.alert("广告主状态修改失败，原因：" + data.msg);
				}
			},
			error:function(msg){
				console.log("error");
            	console.log(msg);
			}
		});
		
	}
	
	function updateFirstOnlineTime(partnerId){
		var status = $("#" + partnerId + "_status_btn").attr("nextStatus");
		
		if(status == 1){
			$.ajax({
				type:"post",
				url:"${pageContext.request.contextPath}/admin/partner/updateFirstOnlineTime.action",
				data:{"partnerId":partnerId},
				success:function(data){
					if(data.msg == "ok"){
						layer.alert('广告主上线成功！', {
							closeBtn: 0
						}, function(){
							location.href = "${pageContext.request.contextPath}/admin/partner/list.action";
						});
					}
					
				},
				error:function(msg){
					console.log("error");
	            	console.log(msg);
				}
			});
		} else {
			layer.msg("启用广告主后可上线!",msgConfig);
		}
	}
	
	function pre_amount(id,name) {
		$("#pre_partner_id").val(id);
		$("#pre_partner_name").val(name);

		var preAmountLayer = layer.open({
			type: 1,
			title:'预充值',
			shadeClose: true,
			shade: 0.4,
			area: ['550px', 'auto'],
			content: $('#pre_amount_window')
		});
		$("#pre_amount_window").data("preAmountLayer", preAmountLayer);
	}

	function refund_amount(id,name) {
		$("#refund_partner_id").val(id);
		$("#refund_partner_name").val(name);

		var refundAmountLayer = layer.open({
			type: 1,
			title:'发起退款',
			shadeClose: true,
			shade: 0.4,
			area: ['550px', 'auto'],
			content: $('#refund_amount_window')
		});
		$("#refund_amount_window").data("refundAmountLayer", refundAmountLayer);
	}

	$(function() {
		
		//开始日期控件
		laydate({
		  elem: '#updateFristOnlineStratTime',
		  format: 'YYYY-MM-DD', 
		  max: laydate.now() //最大日期
		});
		//开始日期控件
		laydate({
		  elem: '#updateFristOnlineEndTime',
		  format: 'YYYY-MM-DD', 
		  max: laydate.now() //最大日期
		});
		// 保存按钮
		$("#pre_amount_conform").click(function() {
			var preAmountLayer = $("#pre_amount_window").data("preAmountLayer");

			var partnerId = $("#pre_partner_id").val();
			var partnerName = $("#pre_partner_name").val();
			var pre_amount = $("#pre_amount").val();
			if(pre_amount == "" || pre_amount == null){
				$("#tip_span").html("请输入金额!");
				$("#tip_span").css('display','block');
				$("#pre_amount").focus();
                return false;
			}

			if(/^\d+(\.\d{1,2})?$/.test(pre_amount) == false || pre_amount == 0){
				$("#tip_span").html("金额不能为0且最多两位小数");
				$("#tip_span").css('display','block');
				$("#pre_amount").focus();
				return false;
			}
            pre_amount = parseFloat(pre_amount*100).toFixed(0);
			$.ajax({
				type:"post",
				url:"${pageContext.request.contextPath}/admin/partnerPre/add.action",
				data:{"partnerId":partnerId ,"partnerName":partnerName , "preAmount":pre_amount ,"type":0},
				success:function(data){
					layer.close(preAmountLayer);
					if(data.success){
						layer.alert('保存成功!', {
							closeBtn: 0
						}, function(){
							location.href = "${pageContext.request.contextPath}/admin/partner/list.action";
						});
					}else{
						layer.alert(data.msg);
					}
				}
			});

		});
		// 取消按钮
		$("#pre_amount_canncel").click(function() {
			var preAmountLayer = $("#pre_amount_window").data("preAmountLayer");
			layer.close(preAmountLayer);
			return false;
		});


		// 保存按钮
		$("#refund_amount_conform").click(function() {
			var refundAmountLayer = $("#refund_amount_window").data("refundAmountLayer");

			var partnerId = $("#refund_partner_id").val();
			var partnerName = $("#refund_partner_name").val();
			var refund_amount = $("#refund_amount").val();
			if(refund_amount == "" || refund_amount == null){
				$("#refund_span").html("请输入金额!");
				$("#refund_span").css('display','block');
				$("#refund_amount").focus();
				return false;
			}

			if(/^\d+(\.\d{1,2})?$/.test(refund_amount) == false || refund_amount == 0){
				$("#refund_span").html("金额不能为0且最多两位小数");
				$("#refund_span").css('display','block');
				$("#refund_amount").focus();
				return false;
			}
            refund_amount = parseFloat(refund_amount*100).toFixed(0);
			$.ajax({
				type:"post",
				url:"${pageContext.request.contextPath}/admin/partnerPre/add.action",
				data:{"partnerId":partnerId ,"partnerName":partnerName , "preAmount":refund_amount,"type":1 },
				success:function(data){
					layer.close(refundAmountLayer);
					if(data.success){
						layer.alert('保存成功!', {
							closeBtn: 0
						}, function(){
							location.href = "${pageContext.request.contextPath}/admin/partner/list.action";
						});
					}else{
						layer.alert(data.msg);
					}
				}
			});

		});
		// 取消按钮
		$("#refund_amount_canncel").click(function() {
			var refundAmountLayer = $("#refund_amount_window").data("refundAmountLayer");
			layer.close(refundAmountLayer);
			return false;
		});
	});
	
	//如果是通过boss系统创建的广告主，开启成功后要调用boss接口通知开启成功
	function callbackBoss(obj,partnerId,bossPartnerCode){
		
		$(obj).attr("disabled",true);
		
		$.ajax({
			type:"post",
			url:"${pageContext.request.contextPath}/admin/partner/callbackBoss.action",
			data:{"partnerId":partnerId ,"bossPartnerCode":bossPartnerCode},
			success:function(data){
				
				if(data.code==1){
					layer.alert('通知boss成功!', {
						closeBtn: 0
					}, function(){
						location.href = "${pageContext.request.contextPath}/admin/partner/list.action";
					});
				}else{
					layer.alert("通知boss失败！原因:" + data.msg, {
						closeBtn: 0
					}, function(){
						location.href = "${pageContext.request.contextPath}/admin/partner/list.action";
					});
				}
				
				$(obj).attr("disabled",false);
			}
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
					<li><i class="icon-home home-icon"></i> <a
						href="${pageContext.request.contextPath}">系统管理</a></li>
					<li class="active">广告主列表</li>
				</ul>
			</div>

			<div class="page-content">
				<shiro:hasPermission name="partner:create">
				<div class="page-header">
					<a class="btn icon-plus btn-primary" href="${baseurl }admin/partner/add.action"> 添加广告主</a>
				</div>
				</shiro:hasPermission>
				<!-- /.page-header -->
				<div class="row">
					<div class="col-xs-12">
						<form id="form" class="form-horizontal" action="${baseurl }admin/partner/list.action" method="post">
							<div class="form-group ">
								
								<label class="col-sm-1 control-label">广告主名称：</label>
								<input type="text" name="partnerName" class="col-sm-1" value="${queryMap.partnerName }"/>
								
								<label class="col-sm-1 control-label">广告主类型：</label>
								<select class="col-sm-1" name="partnerType">
									<option value="">全部</option>
									<option value="0" <c:if test="${queryMap.partnerType == 0}">selected</c:if>>代理</option>
									<option value="1" <c:if test="${queryMap.partnerType == 1}">selected</c:if>>直客</option>
									<option value="2" <c:if test="${queryMap.partnerType == 2}">selected</c:if>>OEM</option>
								</select>
								
								<label class="col-sm-1 control-label">上级代理名称：</label>
								<input type="text" name="partnerPName" class="col-sm-1"  value="${queryMap.partnerPName }"/>
								
								
								<label class="col-sm-1 control-label">广告主状态：</label>
								<select class="col-sm-1" name="status">
									<option value="">全部</option>
									<option value="0" <c:if test="${queryMap.status == 0}">selected</c:if>>开启</option>
									<option value="1" <c:if test="${queryMap.status == 1}">selected</c:if>>停用</option>
								</select>
								
								
								<span class="col-sm-1 ">
									<button class="btn btn-sm btn-primary icon-search" style="float:right" type="submit">
										查询
									</button>
								</span>
							</div>
							
							<div class="form-group ">
								<label class="col-sm-1 control-label">区域：</label>
								<input type="text" name="region" class="col-sm-1"  value="${queryMap.region }"/>
								
								<label class="col-sm-1 control-label">城市：</label>
								<input type="text" name="city" class="col-sm-1"  value="${queryMap.city }"/>
								
								<label class="col-sm-1 control-label">上线时间：</label>
								<input type="text" name="updateFristOnlineStratTime" id="updateFristOnlineStratTime" class="col-sm-1" value="${queryMap.updateFristOnlineStratTime }"/>
								<label class="col-sm-1" style="width:10px;">-</label>
								<input type="text" name="updateFristOnlineEndTime" id="updateFristOnlineEndTime" class="col-sm-1" value="${queryMap.updateFristOnlineEndTime }"/>
							</div>
							
						</form>
						<div class="row">
							<div class="col-xs-12">
								<div class="table-responsive">
									<table id="table-1" class="table table-striped table-bordered table-hover">
										<thead>
											<tr>
												<th width="3%">ID</th>
												<th width="7%">广告主名称</th>
												<th width="5%">广告主类型</th>
												<th width="10%">所属行业</th>
												<th width="10%">上级代理</th>
										<!--	<th width="10%">访问系统url</th> -->
												<th width="7%">账户余额</th>
												<th width="5%">广告主状态</th>
												<th width="5">区域</th>
												<th width="5">城市</th>
												<th width="7">创建时间</th>
												<th width="9">上线时间</th>
												<th width="27%">操作</th>
											</tr>
										</thead>

										<tbody>

											<c:forEach items="${page.list }" var="po" varStatus="s">
												<tr id="partner_list${s.index }">
													<td>${po.id } </td>	
													<td>${po.partnerName }</td>
													<td>
													<c:if test="${ po.partnerType == 0 }">
														代理
													</c:if>
													<c:if test="${ po.partnerType == 1 }">
														直客
													</c:if>
													<c:if test="${ po.partnerType == 2 }">
														OEM
													</c:if>
													</td>
													<td>${po.categoryName }</td>
													<td>${po.pName }</td>
												<!--<td> -->
												<%--${po.loginUrl }/login.action?p=${po.id} --%>
												<!--</td> -->
													<td>${po.accBalanceYuan }</td> 
													<td id="${po.id }_status">
														<c:choose>
															<c:when test="${po.status == 0 }">
																启用
															</c:when>
															<c:otherwise>
																停用
															</c:otherwise>
														</c:choose>
													</td>
													<td>
														${po.region }
													</td>
													<td>
														${po.city }
													</td>
													<td>
														<fmt:formatDate value="${po.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/>
													</td>
													<td id="${po.id }">
														<c:choose>
															<c:when test="${po.firstOnlineTime == null }">
																 -- 
															</c:when>
															<c:otherwise>
																<fmt:formatDate value="${po.firstOnlineTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
															</c:otherwise>
														</c:choose>
													</td>
													<td>
														<!-- 如果是中企动力则不能开启或停用，id等于1就是指中企动力 -->
														<c:if test="${po.id != 1 }">
															<shiro:hasPermission name="partner:changeStatus">
																<c:choose>
																	<c:when test="${po.status == 0 }">
																		<a class="btn btn-xs btn-info btn-danger" id="${po.id }_status_btn" onclick="updateStatus(${po.id },${po.pid })" nextStatus="1">停用</a>
																	</c:when>
																	<c:otherwise>
																		<a class="btn btn-xs btn-info btn-success" id="${po.id }_status_btn" onclick="updateStatus(${po.id },${po.pid })" nextStatus="0">启用</a>
																	</c:otherwise>
																</c:choose>
															</shiro:hasPermission>
														</c:if>

														<shiro:hasPermission name="partner:update">
															<a class="btn btn-xs btn-info"
																href="${pageContext.request.contextPath}/admin/partner/edit.action?id=${po.id }">
																编辑
															</a>
														</shiro:hasPermission>
														<c:if test="${userRole == 7 and activeUser.partner.id == po.pid}">
															<a class="btn btn-xs btn-danger"
																	href="${pageContext.request.contextPath}/admin/partner/proxyRecharge.action?id=${po.id }">
																充值
															</a>
															<a class="btn btn-xs btn-grey"
															   href="${pageContext.request.contextPath}/admin/partner/proxyRefund.action?id=${po.id }">
																退款
															</a>
														</c:if>
														<shiro:hasPermission name="partner:preRecharge">
															<c:if test="${ empty po.bossPartnerCode }">
																<a class="btn btn-xs btn-danger" onclick="pre_amount('${po.id}','${po.partnerName}')">
																	发起充值
																</a>
																<a class="btn btn-xs btn-grey" onclick="refund_amount('${po.id}','${po.partnerName}')">
																	发起退款
																</a>
															</c:if>
														</shiro:hasPermission>
														<a class="btn btn-xs btn-inverse" href="${pageContext.request.contextPath}/admin/partner/blacklist.action?id=${po.id }">
															黑名单
														</a>
														<!-- 记录首次上线时间 -->
														<shiro:hasPermission name="partner:firstOnlineTime"> 
															<c:choose>
																<c:when test="${po.firstOnlineTime == null }">
																	<a class="btn btn-xs btn-info btn-warning" id="${po.id }" onclick="updateFirstOnlineTime(${po.id })">上线</a>
																</c:when>
																<c:otherwise>
																	<a class="btn btn-xs btn-yellow">已上线 </a>
																</c:otherwise>
															</c:choose>
														</shiro:hasPermission>
														
														<!-- 毛利设置 -->
														<shiro:hasPermission name="partner:firstOnlineTime"> 
															<a class="btn btn-xs btn-danger" href="${pageContext.request.contextPath}/admin/partner/grossProfit.action?id=${po.id }">
															毛利设置
															</a>
														</shiro:hasPermission>
														
														<shiro:hasPermission name="partner:callbackBoss">
															<!-- 在客户状态变为开启以后才能点击同步按钮，并且同步成功之后，不允许再点击同步按钮 -->
															<c:if test="${ not empty po.bossPartnerCode }">
																<c:if test="${po.status != 0 }">
																	<button class="btn btn-xs disabled">启用后通知boss系统</button>
																</c:if>
																<c:if test="${po.status == 0 && po.bossCallbackResult != 1 }">
																	<a class="btn btn-xs btn-info" onclick="callbackBoss(this,'${po.id}','${po.bossPartnerCode}')">
																		通知boss系统
																	</a>
																</c:if>
																<c:if test="${po.status == 0 && po.bossCallbackResult == 1 }">
																	<button class="btn btn-xs disabled">通知boss系统成功</button>
																</c:if>
															</c:if>
														</shiro:hasPermission>
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