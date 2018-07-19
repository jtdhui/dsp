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
	$(function(){
		
		//上线开始日期控件
		laydate({
		  elem: '#firstOnlineStartTime',
		  format: 'YYYY-MM-DD', 
		  max: laydate.now() //最大日期
		});
		//上线结束日期控件
		laydate({
		  elem: '#firstOnlineEndTime',
		  format: 'YYYY-MM-DD', 
		  max: laydate.now() //最大日期
		});
		
		//开始日期控件
		laydate({
		  elem: '#startDate',
		  format: 'YYYY-MM-DD', 
		  max: laydate.now() //最大日期
		});
		//结束日期控件
		laydate({
		  elem: '#endDate',
		  format: 'YYYY-MM-DD', 
		  max: laydate.now() //最大日期
		});

		//显示自定义排序图标
		$("img.order").attr("src","${baseurl }images/admin/order.png");
		var orderByFlag = "${params.orderBy}";
		var descFlag = "${params.desc}";
		if(orderByFlag != ""){
			if(descFlag == "desc"){
				desc(orderByFlag);
			}
			else{
				asc(orderByFlag);
			}
		}
	});
	
	/*
	按某个字段排序，每个字段开始默认为降序，点击同个字段可切换升/降序
	*/
	function orderBy(flag){
		if(flag != ""){
			if($("#orderBy").val() != flag){
				
				$("#orderBy").val(flag);
				$("#desc").val("desc");
				desc(flag);
			}
			else{
				if($("#desc").val() == "desc"){
					asc(flag);
				}
				else{
					desc(flag);
				}
			}
			
		}
		
		$("#form1").submit();
	}
	/*
	控制某一个字段显示为降序	
	*/
	function desc(flag){
		$("#desc").val("desc");
		$("img.order").attr("src","${baseurl }images/admin/order.png");
		$("#" + flag + "_order").attr("src","${baseurl}images/admin/order_desc.png");
	}
	/*
	控制某一个字段显示为升序	
	*/
	function asc(flag){
		$("#desc").val("asc");
		$("img.order").attr("src","${baseurl }images/admin/order.png");
		$("#" + flag + "_order").attr("src","${baseurl}images/admin/order_asc.png");
	}
	
	function exportExcel(){
		
		$("#form1").attr("action","${pageContext.request.contextPath}/admin/operate/downloadPartnerReportExcel.action");
		$("#form1").submit();
		$("#form1").attr("action","${pageContext.request.contextPath}/admin/operate/partnerReport.action");
	}
	
	//运营人员可以用运营账户权限，在代理商账户下新建广告主，创建广告活动等操作
	function lookLevel(partnerId){
		
		var partnerIdOrName = $("#partnerIdOrName").val();
		var partnerType = $("#partnerType option:selected").val();
		var status = $("#status option:selected").val();
		var startDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		var firstOnlineStartTime = $("#firstOnlineStartTime").val();
		var firstOnlineEndTime = $("#firstOnlineEndTime").val();
		
		var minus = $("#look_" + partnerId).attr("isOpen");
		if(minus){
			 findAndRemove(partnerId);
			 $("#look_" + partnerId).attr("isOpen","");
			 $("#look_"+partnerId+ ">img").attr("src","${pageContext.request.contextPath}/images/admin/unfolds.png");
			 
		} else {
			
			$("#look_"+partnerId).attr("isOpen",true);
			$("#look_"+ partnerId + ">img").attr("src","${pageContext.request.contextPath}/images/admin/minus.png");
			
			$.ajax({
				type:"post",
				url:"${pageContext.request.contextPath}/admin/operate/getChildrenListForPartnerReport.action",
				data:{"partnerId":partnerId , "partnerIdOrName": partnerIdOrName , "partnerType": partnerType , "status":status , "startDate":startDate , "endDate": endDate ,
					"firstOnlineStartTime" : firstOnlineStartTime , "firstOnlineEndTime" : firstOnlineEndTime },
	 			success:function(data){
					if(data.code == 1){
					 	$.each(data.list, function (index, vo) { 	
					 		 var levelStr = (vo.partner_level - 1 ) * 15;
						     var htmlStr = '<tr id="'+vo.id+'" pid="' + partnerId + '" style="font-weight:bold">';
							 if(vo.hasChildren == 1){
								 //有下级则显示加号可以继续点击
								 htmlStr += '<td><a style="float:left;margin-right:5px;margin-left:'+levelStr+'px" href="javascript:void(0)" id="look_'+vo.id+'" onclick="lookLevel(\''+vo.id+'\');">' ;
								 htmlStr += '<img alt="展开" src="${pageContext.request.contextPath}/images/admin/unfolds.png"/></a>'+vo.id+'</td>';
							 }else{
								 //无下级则显示减号不能再点击
								 htmlStr +='<td><img alt="展开" src="${pageContext.request.contextPath}/images/admin/minus.png" style="float:left;margin-right:5px;margin-left:'+levelStr+'px"/>'+vo.id+'</td>' ;
							 }
							 
							htmlStr += '<td>'+ vo.partner_name + '</td>';
							htmlStr += '<td></td>';
							htmlStr += '<td>'+vo.partner_type+'</td>';
// 							htmlStr += '<td>'+vo.pname+'</td>';
							htmlStr += '<td>￥'+vo.recharge_sum_yuan+'</td>';
							htmlStr += '<td>￥'+vo.acc_balance_yuan+'</td>';
							htmlStr += '<td>'+vo.status+'</td>';
							htmlStr += '<td>'+vo.first_online_time+'</td>';
							htmlStr += '<td>'+vo.pv_sum+'</td>';
							htmlStr += '<td>'+vo.uv_sum+'</td>';
							htmlStr += '<td>'+vo.click_sum+'</td>';
							htmlStr += '<td>'+vo.click_rate_sum+'</td>';
							htmlStr += '<td>'+vo.cpm_expend_sum_yuan+'</td>';
							htmlStr += '<td>'+vo.cpm_cost_sum_yuan+'</td>';
							htmlStr += '<td>'+vo.cpc_expend_sum_yuan+'</td>';
							htmlStr += '<td>'+vo.cpc_cost_sum_yuan+'</td>';
							htmlStr += '<td>'+vo.expend_sum_yuan+'</td>';
							htmlStr += '<td>'+vo.cost_sum_yuan+'</td>';
							htmlStr += '</tr>';
							$("#"+partnerId).after(htmlStr);
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
						href="${pageContext.request.contextPath}">运营管理</a></li>
					<li class="active">广告主报表</li>
				</ul>
			</div>

			<div class="page-content">
				<!-- /.page-header -->
				<div class="row">
					<div class="col-xs-12">
						<form id="form1" class="form-horizontal" action="${baseurl }admin/operate/partnerReport.action" method="post">
						
							<div class="form-group ">
							
								<label class="col-sm-1 control-label">广告主ID/名称：</label>
								<input type="text" id="partnerIdOrName" name="partnerIdOrName" class="col-sm-1" value="${queryMap.partnerIdOrName }" />
								
								<label class="col-sm-1 control-label">广告主类型：</label>
								<select class="col-sm-1" id="partnerType" name="partnerType">
									<option value="">全部</option>
									<option value="0" <c:if test="${queryMap.partnerType == 0}">selected</c:if>>代理</option>
									<option value="1" <c:if test="${queryMap.partnerType == 1}">selected</c:if>>直客</option>
								</select>
								
								<label class="col-sm-1 control-label">广告主状态：</label>
								<select class="col-sm-1" id="status" name="status">
									<option value="">全部</option>
									<option value="0" <c:if test="${queryMap.status == 0}">selected</c:if>>启用</option>
									<option value="1" <c:if test="${queryMap.status == 1}">selected</c:if>>停用</option>
								</select>
								
								<label class="col-sm-1 control-label">运营人员ID：</label>
								<input type="text" id="partnerId" name="operatorId" class="col-sm-1" value="${queryMap.operatorId }" />
								
							</div>
							
							<div class="form-group ">
								<label class="col-sm-1 control-label">上线时间：</label>
								<input type="text" name="firstOnlineStartTime" id="firstOnlineStartTime" class="col-sm-1" value="${queryMap.updateFristOnlineStratTime }"/>
								<label class="col-sm-1" style="width:10px;">-</label>
								<input type="text" name="firstOnlineEndTime" id="firstOnlineEndTime" class="col-sm-1" value="${queryMap.updateFristOnlineEndTime }"/>
								
								<label class="col-sm-1 control-label">统计周期：</label>
								<input type="text" name="startDate" id="startDate" class="col-sm-1" value="${queryMap.startDate }"/>
								<label class="col-sm-1" style="width:10px;">-</label>
								<input type="text" name="endDate" id="endDate" class="col-sm-1" value="${queryMap.endDate }"/>
								
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
												<th width="10%">广告主名称</th>
												<th width="5.8%">广告主类型</th>
												<th width="5.8%">充值总额</th>
												<th width="5.8%">账户余额</th>
												<th width="5.8%">广告主状态</th>
												<th width="7%">上线时间</th>
												<th width="5%">PV</th>
												<th width="5.8%">点击数</th>
												<th width="5.8%">点击率</th>
												<th width="5.8%">CPC单价成本</th>
												<th width="5.8%">CPC单价消耗</th>
											</tr>
										</thead>

										<tbody>



<tr><td>草莓书城</td><td>直客</td><td>300000</td><td>265501 </td><td>启用</td><td>2017-10-16</td><td>26743406 </td><td>229993 </td><td>0.86%</td><td>0.15</td><td>34499 </td></tr><tr><td>九州书城</td><td>直客</td><td>300000</td><td>263595 </td><td>启用</td><td>2017-10-16</td><td>33246199 </td><td>242697 </td><td>0.73%</td><td>0.15</td><td>36405 </td></tr><tr><td>咖啡书城</td><td>直客</td><td>300000</td><td>267022 </td><td>启用</td><td>2017-10-16</td><td>27829474 </td><td>219853 </td><td>0.79%</td><td>0.15</td><td>32978 </td></tr><tr><td>烈焰书城</td><td>直客</td><td>300000</td><td>267022 </td><td>启用</td><td>2017-10-16</td><td>32331301 </td><td>219853 </td><td>0.68%</td><td>0.15</td><td>32978 </td></tr><tr><td>领音网</td><td>直客</td><td>300000</td><td>299000 </td><td>启用</td><td>2017-10-17</td><td>1481481 </td><td>6667 </td><td>0.45%</td><td>0.15</td><td>1000 </td></tr><tr><td>北京贵友大厦</td><td>直客</td><td>300000</td><td>299583 </td><td>启用</td><td>2017-10-20</td><td>896057 </td><td>2778 </td><td>0.31%</td><td>0.15</td><td>417 </td></tr><tr><td>福田乘用车北京伽途</td><td>直客</td><td>300000</td><td>291776 </td><td>启用</td><td>2017-10-20</td><td>9790712 </td><td>54828 </td><td>0.56%</td><td>0.15</td><td>8224 </td></tr><tr><td>MuscleDogGYM</td><td>直客</td><td>300000</td><td>299738 </td><td>启用</td><td>2017-10-20</td><td>564516 </td><td>1750 </td><td>0.31%</td><td>0.15</td><td>263 </td></tr><tr><td>艾优奇点成长馆</td><td>直客</td><td>300000</td><td>299633 </td><td>启用</td><td>2017-10-20</td><td>906012 </td><td>2446 </td><td>0.27%</td><td>0.15</td><td>367 </td></tr><tr><td>北京隆晟骏达汽车4S店</td><td>直客</td><td>300000</td><td>299625 </td><td>启用</td><td>2017-10-20</td><td>1136364 </td><td>2500 </td><td>0.22%</td><td>0.15</td><td>375 </td></tr><tr><td>宇熙静梵瑜伽SPA会所</td><td>直客</td><td>300000</td><td>299909 </td><td>启用</td><td>2017-10-20</td><td>65532 </td><td>609 </td><td>0.93%</td><td>0.15</td><td>91 </td></tr><tr><td>沁水县全民健身中心</td><td>直客</td><td>300000</td><td>299596 </td><td>启用</td><td>2017-10-21</td><td>349869 </td><td>2694 </td><td>0.77%</td><td>0.15</td><td>404 </td></tr><tr><td>北京玫瑰城堡婚纱摄影牡丹江店</td><td>直客</td><td>300000</td><td>299917 </td><td>启用</td><td>2017-10-21</td><td>135501 </td><td>556 </td><td>0.41%</td><td>0.15</td><td>83 </td></tr><tr><td>艺伙V课</td><td>直客</td><td>300000</td><td>299438 </td><td>启用</td><td>2017-10-21</td><td>1292715 </td><td>3749 </td><td>0.29%</td><td>0.15</td><td>562 </td></tr><tr><td>北汽新能源庞大五方桥店</td><td>直客</td><td>300000</td><td>299526 </td><td>启用</td><td>2017-10-21</td><td>376275 </td><td>3161 </td><td>0.84%</td><td>0.15</td><td>474 </td></tr><tr><td>住墅</td><td>直客</td><td>300000</td><td>299032 </td><td>启用</td><td>2017-10-21</td><td>1172760 </td><td>6450 </td><td>0.55%</td><td>0.15</td><td>968 </td></tr><tr><td>河北逸忠妍汽车服务有限公司</td><td>直客</td><td>300000</td><td>299444 </td><td>启用</td><td>2017-10-22</td><td>514896 </td><td>3707 </td><td>0.72%</td><td>0.15</td><td>556 </td></tr><tr><td>在职研网</td><td>直客</td><td>300000</td><td>299038 </td><td>启用</td><td>2017-10-22</td><td>2211251 </td><td>6413 </td><td>0.29%</td><td>0.15</td><td>962 </td></tr><tr><td>四丰电器</td><td>直客</td><td>300000</td><td>299546 </td><td>启用</td><td>2017-10-22</td><td>487967 </td><td>3025 </td><td>0.62%</td><td>0.15</td><td>454 </td></tr><tr><td>教唱歌</td><td>直客</td><td>300000</td><td>295248 </td><td>启用</td><td>2017-10-22</td><td>3600162 </td><td>31681 </td><td>0.88%</td><td>0.15</td><td>4752 </td></tr>
										
											<!--
											<tr>
												<td>浦发信用卡</td>
												<td>直客</td>
												<td>500000</td>
												<td>481635</td>
												<td>启用</td>
												<td>2017-10-10</td>
												<td>25506944</td>
												<td>122433</td>
												<td>0.48%</td>
												<td>0.15</td>
												<td>18365</td>
												
											</tr>
											<tr>
												<td>中信信用卡</td>
												<td>代理</td>
												<td>1000000</td>
												<td>972127</td>
												<td>启用</td>
												<td>2017-10-10</td>
												<td>27326470</td>
												<td>92910</td>
												<td>0.34%</td>
												<td>0.3</td>
												<td>27873</td>
												
											</tr>
											<tr>
												<td>新网银行好人贷</td>
												<td>直客</td>
												<td>1500000</td>
												<td>1432119</td>
												<td>启用</td>
												<td>2017-10-10</td>
												<td>64648571</td>
												<td>271524</td>
												<td>0.42%</td>
												<td>0.25</td>
												<td>67881</td>
												
											</tr>
											<tr>
												<td>现金侠贷款</td>
												<td>代理</td>
												<td>600000</td>
												<td>547098</td>
												<td>启用</td>
												<td>2017-10-10</td>
												<td>66127500</td>
												<td>211608</td>
												<td>0.32%</td>
												<td>0.25</td>
												<td>52902</td>
												
											</tr>
											<tr>
												<td>环球黑卡</td>
												<td>代理</td>
												<td>400000</td>
												<td>331274</td>
												<td>启用</td>
												<td>2017-10-10</td>
												<td>67378431</td>
												<td>343630</td>
												<td>0.51%</td>
												<td>0.2</td>
												<td>68726</td>
												
											</tr>
											<tr>
												<td>有利网理财平台</td>
												<td>代理</td>
												<td>200000</td>
												<td>200000</td>
												<td>启用</td>
												<td>2017-10-10</td>
												<td>55921839</td>
												<td>162173</td>
												<td>0.29%</td>
												<td>0.15</td>
												<td>24326</td>
											</tr>
											<tr>
												<td>在线书城</td>
												<td>直客</td>
												<td>2000000</td>
												<td>1934761</td>
												<td>关闭</td>
												<td>2017-10-11</td>
												<td>35074731</td>
												<td>326195</td>
												<td>0.93%</td>
												<td>0.2</td>
												<td>65239</td>
												
											</tr>
											<tr>
												<td>住墅民宿</td>
												<td>直客</td>
												<td>500000</td>
												<td>462372</td>
												<td>启用</td>
												<td>2017-10-12</td>
												<td>20858093</td>
												<td>171036</td>
												<td>0.82%</td>
												<td>0.22</td>
												<td>37628</td>
												
											</tr>
											<tr>
												<td>短贷王贷款</td>
												<td>代理</td>
												<td>800000</td>
												<td>785787</td>
												<td>启用</td>
												<td>2017-10-13</td>
												<td>17227878</td>
												<td>56852</td>
												<td>0.33%</td>
												<td>0.25</td>
												<td>14213</td>
											</tr>-->
										</tbody>
										<%--
										<thead>
											<tr>
												<th width="12%">广告主ID</th>
												<th width="10%">广告主名称</th>
												<th width="5.8%">中企运营人员</th>
												<th width="5.8%">广告主类型</th>
<!-- 												<th width="5.8%">上级代理</th> -->
												<th width="5.8%">充值总额</th>
												<th width="5.8%">账户余额</th>
												<th width="5.8%">广告主状态</th>
												<th width="7%">上线时间</th>
												<th width="5%">PV</th>
												<th width="5%">UV</th>
												<th width="5.8%">点击数</th>
												<th width="5.8%">点击率</th>
												<th width="5.8%">CPM单价消耗</th>
												<th width="5.8%">CPM单价成本</th>
												<th width="5.8%">CPC单价消耗</th>
												<th width="5.8%">CPC单价成本</th>
												<th width="5.8%">消耗</th>
												<th width="5.8%">成本</th>
											</tr>
										</thead>

										<tbody>
											<c:forEach items="${page.listMap }" var="map">
												<tr id="${map.id}" pid="${map.pid }" <c:if test="${not empty map.isTarget}"> style="font-weight:bold" </c:if>>
													<td>
														<c:if test="${not empty map.partner_level }">
															<!-- 有下级，就显示减号，并可以点击展开 -->
															<c:if test="${ not empty map.hasChildren }">
																<a style="float:left;margin-right:5px;margin-left:${(map.partner_level - 1) * 15 }px" href="javascript:void(0)" id="look_${map.id}" onclick="lookLevel('${map.id}');" isOpen="true">
																	<img alt="展开"  src="${pageContext.request.contextPath}\images\admin\minus.png" />
																</a>
															</c:if>
															<!-- 有上级，无下级，只显示减号 -->
															<c:if test="${ empty map.hasChildren && (map.pid != 0 && map.pid != 1)  }">
																<img alt="展开" style="float:left;margin-right:5px;margin-left:${(map.partner_level - 1) * 15 }px" src="${pageContext.request.contextPath}\images\admin\minus.png" />
															</c:if>
															<!-- 无下级，并且是一级或直属中企，就不显示减号 -->
															<c:if test="${ empty map.hasChildren && (map.pid == 0 || map.pid == 1)  }">
																<div style="float:left;margin-right:5px;margin-left:0px">&nbsp;</div>
															</c:if>
														</c:if>
														${map.id }
													</td>
													<td>${map.partner_name }</td>
													<td>${map.operator_name }</td>
													<td>${map.partner_type }</td>
<!-- 													<td>${map.pname }</td> -->
													<td>
														<c:if test="${not empty map.recharge_sum_yuan }">
															￥${map.recharge_sum_yuan }
														</c:if>
													</td>
													<td>
														<c:if test="${not empty map.acc_balance_yuan }">
															￥${map.acc_balance_yuan }
														</c:if>	
													</td>
													<td>${map.status }</td>
													<td>${map.first_online_time }</td>
													<td>${map.pv_sum }</td>
													<td>${map.uv_sum }</td>
													<td>${map.click_sum }</td>
													<td>${map.click_rate_sum }%</td>
													<td>￥${map.cpm_expend_sum_yuan }</td>
													<td>￥${map.cpm_cost_sum_yuan }</td>
													<td>￥${map.cpc_expend_sum_yuan }</td>
													<td>￥${map.cpc_cost_sum_yuan }</td>
													<td>￥${map.expend_sum_yuan }</td>
													<td>￥${map.cost_sum_yuan }</td>
												</tr>
											</c:forEach>
										</tbody>
										 --%>
									</table>
									<c:if test="${page != null && fn:length(page.listMap) gt 0}">
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