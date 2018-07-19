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
<title>广告主运营列表</title>
<%@ include file="/WEB-INF/commonjsp/admin_common_css.jsp"%>
<%@ include file="/WEB-INF/commonjsp/admin_common_js.jsp"%>
</head>
<style type="text/css">
.addTreeList a.btn{margin:0 2px;}
.addTreeList a.btn:first-child{margin-left:0;}
</style>
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
							location.href = "${pageContext.request.contextPath}/admin/partner/operationList.action";
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
							location.href = "${pageContext.request.contextPath}/admin/partner/operationList.action";
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
							location.href = "${pageContext.request.contextPath}/admin/partner/operationList.action";
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
                $("#tip_span").html("");
				$("#tip_span").html("请输入金额!");
				$("#pre_amount").focus();
                return false;
			}

			if(/^\d+(\.\d{1,2})?$/.test(pre_amount) == false || pre_amount == 0){
                $("#tip_span").html("");
				$("#tip_span").html("金额不能为0且最多两位小数");
				$("#pre_amount").focus();
				return false;
			}
            pre_amount = parseFloat(pre_amount*100).toFixed(0);

            var pre_gift = $("#pre_gift").val();
            if(pre_gift == "" || pre_gift == null){
                $("#gift_tip_span").html("");
                $("#gift_tip_span").html("请输入金额!");
                $("#pre_amount").focus();
                return false;
            }

            if(/^\d+(\.\d{1,2})?$/.test(pre_gift) == false || pre_gift == 0){
                $("#gift_tip_span").html("");
                $("#gift_tip_span").html("金额不能为0且最多两位小数");
                $("#pre_gift").focus();
                return false;
            }
            pre_gift = parseFloat(pre_gift*100).toFixed(0);

			$.ajax({
				type:"post",
				url:"${pageContext.request.contextPath}/admin/partnerPre/add.action",
				data:{"partnerId":partnerId ,"partnerName":partnerName , "preAmount":pre_amount, "preGift":pre_gift ,"type":0},
				success:function(data){
					layer.close(preAmountLayer);
					if(data.success){
						layer.alert('保存成功!', {
							closeBtn: 0
						}, function(){
							location.href = "${pageContext.request.contextPath}/admin/partner/operationList.action";
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
							location.href = "${pageContext.request.contextPath}/admin/partner/operationList.action";
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
						location.href = "${pageContext.request.contextPath}/admin/partner/operationList.action";
					});
				}else{
					layer.alert("通知boss失败！原因:" + data.msg, {
						closeBtn: 0
					}, function(){
						location.href = "${pageContext.request.contextPath}/admin/partner/operationList.action";
					});
				}
				
				$(obj).attr("disabled",false);
			}
		});
	}
	
	//运营人员可以用运营账户权限，在代理商账户下新建广告主，创建广告活动等操作
	function lookLevel(partnerId){
		
		var partnerName = $("#partnerName").val();
		var partnerType = $("#partnerType option:selected").val();
		var status = $("#status option:selected").val();
		var partnerPName = $("#partnerPName").val();
		var region = $("#region").val();
		var city = $("#city").val();
		var updateFristOnlineStratTime = $("#updateFristOnlineStratTime").val();
		var updateFristOnlineEndTime = $("#updateFristOnlineEndTime").val();
		
		
		var minus = $("#look_" + partnerId).attr("isOpen");
		if(minus){
			 findAndRemove(partnerId);
			 $("#look_" + partnerId).attr("isOpen","");
			 $("#look_"+partnerId+ ">img").attr("src","${pageContext.request.contextPath}/images/admin/unfolds.png");
		} else {
			$.ajax({
				type:"post",
				url:"${pageContext.request.contextPath}/admin/partner/getChildrenName.action",
				data:{"partnerId":partnerId ,"partnerName":partnerName ,"partnerType":partnerType ,"partnerPName":partnerPName ,"status":status, "region":region ,"city":city 
						,"updateFristOnlineStratTime":updateFristOnlineStratTime , "updateFristOnlineEndTime": updateFristOnlineEndTime},
	 			success:function(data){
					if(data.success){
					 $.each(data.partner, function (index, vo) {
						 var levelStr = (vo.partner_level-1)*15;
					     var htmlStr = '<tr id="'+vo.id+'" pid="' + partnerId + '" style="font-weight:bold">';
						 if(vo.hasChildren == 1){
							 htmlStr += '<td><a style="float:left;margin-right:5px;margin-left:'+levelStr+'px" href="javascript:void(0)" id="look_'+vo.id+'" onclick="lookLevel(\''+vo.id+'\');">' 
							 htmlStr += '<img alt="展开" src="${pageContext.request.contextPath}/images/admin/unfolds.png"/></a>'+vo.id+'</td>';
						 }else{
							 htmlStr +='<td><img alt="返回" style="float:left;margin-right:5px;margin-left:'+levelStr+'px" src="${pageContext.request.contextPath}/images/admin/minus.png"/>'+vo.id+'</td>'
						 }
							htmlStr += '<td>'+vo.partner_name+'</td>';
							if(vo.partner_type == 0){
								htmlStr += '<td>代理</td>';
							}else if (vo.partner_type == 1) {
								htmlStr += '<td>直客</td>';
							}else{
								htmlStr += '<td>OEM</td>';
							}
								htmlStr += '<td>'+vo.category_name+'</td>';
								htmlStr += '<td>'+vo.pname+'</td>';
								htmlStr += '<td>'+vo.acc_balance+'</td>';
							if (vo.status) {
		                        htmlStr += '<td>停用</td>';
		                    } else {
		                        htmlStr += '<td>启用</td>';
		                    }
							if (vo.region == null) {
		                        htmlStr += '<td></td>';
		                    } else {
		                        htmlStr += '<td>'+vo.region+'</td>';
		                    }
							if (vo.city == null) {
		                        htmlStr += '<td></td>';
		                    } else {
		                        htmlStr += '<td>'+vo.city+'</td>';
		                    }
							htmlStr += '<td>'+vo.create_time+'</td>';
	                        htmlStr += '<td>'+vo.first_online_time+'</td>';
							//操作
							htmlStr += '<td class="addTreeList">';
							//启用，停用
							if(vo.id != 1){
								if(vo.status == 0){
									htmlStr += '<a class="btn btn-xs btn-info btn-danger" id="'+vo.id+'_status_btn" onclick="updateStatus(\''+vo.id+'\',\''+vo.pid+'\')" nextStatus="1">停用 </a>';
								}else{
									htmlStr += '<a class="btn btn-xs btn-info btn-success" id="'+vo.id+'_status_btn" onclick="updateStatus(\''+vo.id+'\',\''+vo.pid+'\')" nextStatus="0">启用</a>';
								}
							}
							//编辑	
							htmlStr += '<a class="btn btn-xs btn-info" href="${pageContext.request.contextPath}/admin/partner/edit.action?id='+vo.id+'">编辑 </a>';
							//充值,退款
							if(vo.userRole == 7 && activeUser.partner.id == vo.pid && vo.pid == 1){
								htmlStr += '<a class="btn btn-xs btn-info" href="${pageContext.request.contextPath}/admin/partner/proxyRecharge.action?id='+vo.id+'">充值 </a>';
								htmlStr += '<a class="btn btn-xs btn-info" href="${pageContext.request.contextPath}/admin/partner/proxyRefund.action?id='+vo.id+'">退款 </a>';
							}
							//发起充值，发起退款
							if(vo.bossPartnerCode == null && vo.pid == 1){
								htmlStr += '<a class="btn btn-xs btn-pink" onclick="pre_amount(\''+vo.id+'\',\''+vo.partnerName+'\')">发起充值 </a>';
								htmlStr += '<a class="btn btn-xs btn-grey" onclick="refund_amount(\''+vo.id+'\',\''+vo.partnerName+'\')">发起退款 </a>';
							}
							//黑名单
							htmlStr += '<a class="btn btn-xs btn-inverse" href="${pageContext.request.contextPath}/admin/partner/blacklist.action?id='+vo.id+'">黑名单</a>';
							//同步审核状态
							htmlStr += '<a class="btn btn-xs btn-primary" target="_blank" href="${pageContext.request.contextPath}/init/initPartnerMQ.action?partnerId='+vo.id+'">同步审核状态</a>';
							//记录上线
							if(vo.first_online_time == null){
								htmlStr += '<a class="btn btn-xs btn-info btn-warning" id="'+vo.id+'" onclick="updateFirstOnlineTime('+vo.id+')">上线</a>';
							}else{
								htmlStr += '<a class="btn btn-xs btn-yellow">已上线 </a>';
							}
							//毛利设置
							htmlStr += '<a class="btn btn-xs btn-purple" href="${pageContext.request.contextPath}/admin/partner/grossProfit.action?id='+vo.id+'">毛利设置</a>';
							if(vo.boss_partner_code != null){
								if(vo.status != 0){
									htmlStr += '<button class="btn btn-xs disabled">启用后通知boss系统</button>'
								}
								if(vo.status == 0 && vo.boss_callback_result != 1){
									htmlStr += '<a class="btn btn-xs btn-info" onclick="callbackBoss(this,'+vo.id+','+po.boss_partner_code+')"> 通知boss系统 </a>'
								} 
								if(vo.status == 0 && vo.boss_callback_result == 1){
									htmlStr += '<button class="btn btn-xs disabled">通知boss系统成功</button>'
								}
							}
							//操作结束
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
					<li><i class="icon-home home-icon"></i> <a
						href="${pageContext.request.contextPath}">系统管理</a></li>
					<li class="active">广告主运营列表</li>
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
						<form id="form" class="form-horizontal" action="${baseurl }admin/partner/operationList.action" method="post">
							<div class="form-group ">
								
								<label class="col-sm-1 control-label">广告主名称：</label>
								<input type="text" id="partnerName" name="partnerName" class="col-sm-1" value="${queryMap.partnerName }"/>
								
								<label class="col-sm-1 control-label">广告主类型：</label>
								<select class="col-sm-1" id="partnerType" name="partnerType">
									<option value="">全部</option>
									<option value="0" <c:if test="${queryMap.partnerType == 0}">selected</c:if>>代理</option>
									<option value="1" <c:if test="${queryMap.partnerType == 1}">selected</c:if>>直客</option>
									<option value="2" <c:if test="${queryMap.partnerType == 2}">selected</c:if>>OEM</option>
								</select>
								
								<label class="col-sm-1 control-label">上级代理名称：</label>
								<input type="text" id="partnerPName" name="partnerPName" class="col-sm-1"  value="${queryMap.partnerPName }"/>
								
								
								<label class="col-sm-1 control-label">广告主状态：</label>
								<select class="col-sm-1" id="status" name="status">
									<option value="">全部</option>
									<option value="0" <c:if test="${queryMap.status == 0}">selected</c:if>>启用</option>
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
								<input type="text" id="region" name="region" class="col-sm-1"  value="${queryMap.region }"/>
								
								<label class="col-sm-1 control-label">城市：</label>
								<input type="text" id="city" name="city" class="col-sm-1"  value="${queryMap.city }"/>
								
								<label class="col-sm-1 control-label">上线时间：</label>
								<input type="text" id="updateFristOnlineStratTime" name="updateFristOnlineStratTime" id="updateFristOnlineStratTime" class="col-sm-1" value="${queryMap.updateFristOnlineStratTimeString }"/>
								<label class="col-sm-1" style="width:10px;">-</label>
								<input type="text" id="updateFristOnlineEndTime" name="updateFristOnlineEndTime" id="updateFristOnlineEndTime" class="col-sm-1" value="${queryMap.updateFristOnlineEndTimeString }"/>
							</div>
							
						</form>
						<div class="row">
							<div class="col-xs-12">
								<div class="table-responsive">
									<table id="table-1" class="table table-striped table-bordered table-hover">
										<thead>
											<tr>
												<th width="12%">ID</th>
												<th width="10%">广告主名称</th>
												<th width="5%">广告主类型</th>
												<th width="6%">所属行业</th>
												<th width="9%">上级代理</th>
												<th width="6%">账户余额</th>
												<th width="5%">广告主状态</th>
												<th width="5">区域</th>
												<th width="5">城市</th>
												<th width="7">创建时间</th>
												<th width="7">上线时间</th>
												<th width="20%">操作</th>
											</tr>
										</thead>

										<tbody>

											<c:forEach items="${page.listMap }" var="po" varStatus="s">
												<tr id="${po.id}" pid="${po.pid }" <c:if test="${not empty po.isTarget}"> style="font-weight:bold" </c:if>>
													<td>
														<c:if test="${not empty po.partner_level }">
															<c:if test="${ not empty po.hasChildren }">
																<a style="float:left;margin-right:5px;margin-left:${(po.partner_level - 1) * 15 }px" href="javascript:void(0)" id="look_${po.id}" onclick="lookLevel('${po.id}');" isOpen="true">
																	<img alt="展开"  src="${pageContext.request.contextPath}\images\admin\minus.png" />
																</a>
															</c:if>
															<c:if test="${ empty po.hasChildren && (po.pid == 0 || po.pid == 1)  }">
																<div style="float:left;margin-right:5px;margin-left:0px">&nbsp;</div>
															</c:if>
															<c:if test="${ empty po.hasChildren && (po.pid != 0 && po.pid != 1)  }">
																<img alt="展开" style="float:left;margin-right:5px;margin-left:${(po.partner_level - 1) * 15 }px" src="${pageContext.request.contextPath}\images\admin\minus.png" />
															</c:if>
														</c:if>
														${po.id }
													</td>
													<td>${po.partner_name }</td>
													<td>
													<c:if test="${ po.partner_type == 0 }">
														代理
													</c:if>
													<c:if test="${ po.partner_type == 1 }">
														直客
													</c:if>
													<c:if test="${ po.partner_type == 2 }">
														OEM
													</c:if>
													</td>
													<td>${po.category_name }</td>
													<td>${po.pname }</td>
												<!--<td> -->
												<%--${po.loginUrl }/login.action?p=${po.id} --%>
												<!--</td> -->
													<td>${po.acc_balance }</td> 
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
														<fmt:formatDate value="${po.create_time }" pattern="yyyy-MM-dd HH:mm:ss"/>
													</td>
													<td>
														${po.first_online_time}
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
															<c:if test="${po.id != 1 }">
																<a class="btn btn-xs btn-info"
																	href="${pageContext.request.contextPath}/admin/partner/edit.action?id=${po.id }">
																	编辑
																</a>
															</c:if>
														
														</shiro:hasPermission>
														<c:if test="${userRole == 7 and activeUser.partner.id == po.pid and po.id != 1} ">
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
															<c:if test="${ empty po.bossPartnerCode and po.id != 1}">
																<a class="btn btn-xs btn-pink" onclick="pre_amount('${po.id}','${po.partnerName}')">
																	发起充值
																</a>
																<a class="btn btn-xs btn-grey" onclick="refund_amount('${po.id}','${po.partnerName}')">
																	发起退款
																</a>
															</c:if>
														</shiro:hasPermission>
														<c:if test="${po.id != 1 }">
															<a class="btn btn-xs btn-inverse" href="${pageContext.request.contextPath}/admin/partner/blacklist.action?id=${po.id }">
																黑名单
															</a>
														</c:if>
														<c:if test="${po.id != 1 }">
															<a class="btn btn-xs btn-primary" target="_blank" href="${pageContext.request.contextPath}/init/initPartnerMQ.action?partnerId=${po.id }">
																同步审核状态
															</a>
														</c:if>
														<!-- 记录首次上线时间 -->
														<shiro:hasPermission name="partner:firstOnlineTime"> 
															<c:if test="${po.id != 1 }">
																<c:choose>
																	<c:when test="${po.firstOnlineTime == null}">
																		<a class="btn btn-xs btn-info btn-warning" id="${po.id }" onclick="updateFirstOnlineTime(${po.id })">上线</a>
																	</c:when>
																	<c:otherwise>
																		<a class="btn btn-xs btn-yellow">已上线 </a>
																	</c:otherwise>
																</c:choose>
															</c:if>
														</shiro:hasPermission>
														
														<!-- 毛利设置 -->
														<shiro:hasPermission name="partner:firstOnlineTime"> 
															<c:if test="${po.id != 1 }">
																<a class="btn btn-xs btn-purple" href="${pageContext.request.contextPath}/admin/partner/grossProfit.action?id=${po.id }">
																毛利设置
																</a>
															</c:if>
														</shiro:hasPermission>
														
														<shiro:hasPermission name="partner:callbackBoss">
															<!-- 在客户状态变为开启以后才能点击同步按钮，并且同步成功之后，不允许再点击同步按钮 -->
															<c:if test="${ not empty po.boss_partner_code }">
																<c:if test="${po.status != 0 }">
																	<button class="btn btn-xs disabled">启用后通知boss系统</button>
																</c:if>
																<c:if test="${po.status == 0 && po.boss_callback_result != 1 }">
																	<a class="btn btn-xs btn-info" onclick="callbackBoss(this,'${po.id}','${po.boss_partner_code}')">
																		通知boss系统
																	</a>
																</c:if>
																<c:if test="${po.status == 0 && po.boss_callback_result == 1 }">
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

	<!--预充值-->
	<div id="pre_amount_window" class="layer_check_type" style="display:none;height: 180px;">
		<input name="pre_partner_id" id="pre_partner_id" value="" type="hidden">
		<input name="pre_partner_name" id="pre_partner_name" value="" type="hidden">

        <table class="table">
            <tbody>
                <tr>
                    <td width="25%">充值金额</td>
                    <td width="30%"><input type="text" id="pre_amount" name="pre_amount" class="width-100" value="" ></td>
                    <td width="45%"><div id="tip_span">实际充值金额，如无请填写“0”</div></td>
                </tr>
                <tr>
                    <td width="25%">赠送</td>
                    <td width="30%"><input type="text" id="pre_gift" name="pre_gift" class="width-100" value="" ></td>
                    <td width="45%"><div id="gift_tip_span">赠送金额，如无请填写“0”</div></td>
                </tr>
            </tbody>
        </table>
        <div class="clearfix">
            <div class="col-md-offset-3 col-md-9" style="margin-top:10px;">
                <input type="button" value="确 定" class="btn btn-info" id="pre_amount_conform">
                <input type="reset" value="取 消" class="btn" id="pre_amount_canncel">
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