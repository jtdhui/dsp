<%@page import="com.jtd.web.constants.CatgSerial"%>
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

<style type="text/css">
	th>img { cursor:pointer; }
</style>

<script type="text/javascript">
	$(function(){
		
		//开始日期控件
		laydate({
		  elem: '#startDate',
		  format: 'YYYY-MM-DD', 
		  max: laydate.now() //最大日期
		});
		//开始日期控件
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
		
		$("#form1").attr("action","${pageContext.request.contextPath}/admin/operate/downloadCampChannelReport.action");
		$("#form1").submit();
		$("#form1").attr("action","${pageContext.request.contextPath}/admin/operate/campChannelReport.action");
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
					<li class="active">活动渠道报表</li>
				</ul>
			</div>

			<div class="page-content">
				<!-- /.page-header -->
				<div class="row">
					<div class="col-xs-12">
						<form id="form1" class="form-horizontal" action="${pageContext.request.contextPath}/admin/operate/campChannelReport.action" method="post">
							<div class="form-group ">
								
								<label class="col-sm-1 control-label">活动ID/名称：</label>
								<input type="text" id="campaignIdOrName" name="campaignIdOrName" class="col-sm-1" value="${params.campaignIdOrName }"/>
								
								<label class="col-sm-1 control-label">广告主ID/名称：</label>
								<input type="text" id="partnerIdOrName" name="partnerIdOrName" class="col-sm-1" value="${params.partnerIdOrName }"/>
								
								<label class="col-sm-1 control-label">域名：</label>
								<input type="text" id="host" name="host" class="col-sm-1" value="${params.host }"/>
								
								<label class="col-sm-1 control-label">渠道：</label>
								<select class="col-sm-1" id="channelId" name="channelId">
									<option value="">所有</option>
									<c:forEach items="${channelList }" var="channel">
										<c:if test="${ channelId == channel.id }">
											<option value="${channel.id }" selected>${channel.channelName }</option>
										</c:if>
										<c:if test="${ channelId != channel.id }">
											<option value="${channel.id }">${channel.channelName }</option>
										</c:if>
									</c:forEach>
								</select>
								
								<span class="col-sm-1">
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
							<div class="form-group ">	
								<label class="col-sm-1 control-label">统计时间：</label>
								<input type="text" name="startDate" id="startDate" class="col-sm-1" value="${params.startDate }"/>
								<label class="col-sm-1" style="width:10px;">-</label>
								<input type="text" name="endDate" id="endDate" class="col-sm-1" value="${params.endDate }"/>
							</div>
							
							<input type="hidden" id="orderBy" name="orderBy" value="${params.orderBy }"/>
							<input type="hidden" id="desc" name="desc" value="${params.desc }"/>
						</form>
						<div class="row">
							<div class="col-xs-12">
								<div class="table-responsive">
									<table class="table table-striped table-bordered table-hover">
				                        <tr>
<!-- 				                            <th width="5%" onclick="orderBy('camp_id')">活动ID <img id="camp_id_order" class="order"/></th> -->
<!-- 				                            <th width="10%">活动名称</th> -->
<!-- 				                            <th width="10%">广告主</th> -->
<!-- 				                            <th width="5%" onclick="orderBy('channel_id')">渠道 <img id="channel_id_order" class="order"/></th> -->
<!-- 				                            <th width="5%">域名</th> -->
<!-- 				                            <th width="6%" onclick="orderBy('pv')">PV <img id="pv_order" class="order"/></th> -->
<!-- 				                            <th width="6%" onclick="orderBy('uv')">UV <img id="uv_order" class="order"/></th> -->
<!-- 				                            <th width="6%" onclick="orderBy('click')">点击量 <img id="click_order" class="order"/></th> -->
<!-- 				                            <th width="6%" onclick="orderBy('click_rate')">点击率 <img id="click_rate_order" class="order"/></th> -->
<!-- 				                            <th width="6%" onclick="orderBy('cpm_expend_yuan')">投放cpm消耗 <img id="cpm_expend_yuan_order" class="order"/></th> -->
<!-- 				                            <th width="6%" onclick="orderBy('cpm_cost_yuan')">投放cpm成本 <img id="cpm_cost_yuan_order" class="order"/></th> -->
<!-- 				                            <th width="6%" onclick="orderBy('cpc_expend_yuan')">投放cpc消耗 <img id="cpc_expend_yuan_order" class="order"/></th> -->
<!-- 				                            <th width="6%" onclick="orderBy('cpc_cost_yuan')">投放cpc成本 <img id="cpc_cost_yuan_order" class="order"/></th> -->
<!-- 				                            <th width="6%" onclick="orderBy('expend_yuan')">消耗 <img id="expend_yuan_order" class="order"/></th> -->
<!-- 				                            <th width="6%" onclick="orderBy('cost_yuan')">成本 <img id="cost_yuan_order" class="order"/></th> -->
<!-- 				                            <th width="6%" onclick="orderBy('date')">日期 <img id="date_order" class="order"/></th> -->
												
												 <th width="5%">活动ID</th>
					                            <th width="10%">活动名称</th>
					                            <th width="10%">广告主</th>
					                            <th width="5%">渠道</th>
					                            <th width="5%">域名</th>
					                            <th width="6%">PV</th>
					                            <th width="6%">UV</th>
					                            <th width="6%">点击量</th>
					                            <th width="6%">点击率</th>
					                            <th width="6%">投放cpm消耗</th>
					                            <th width="6%">投放cpm成本</th>
					                            <th width="6%">投放cpc消耗</th>
					                            <th width="6%">投放cpc成本</th>
					                            <th width="6%">消耗</th>
					                            <th width="6%">成本</th>
					                            <th width="6%">日期</th>
				                        </tr>
				                        <c:forEach items="${ page.list }" var="map" varStatus="vs">
				                         	<tr>
												 <td>${map.camp_id }</td>
												 <td>${map.campaign_name }</td>
												 <td>${map.partner_name }</td>
												 <td>${map.channel_name }</td>
												 <td>${map.host }</td>
												 <td>${map.pv }</td>
												 <td>${map.uv }</td>
												 <td>${map.click }</td>
												 <td>${map.click_rate }%</td>
												 <td>￥${map.cpm_expend_yuan }</td>
												 <td>￥${map.cpm_cost_yuan }</td>
												 <td>￥${map.cpc_expend_yuan }</td>
												 <td>￥${map.cpc_cost_yuan }</td>
												 <td>￥${map.expend_yuan }</td>
												 <td>￥${map.cost_yuan }</td>
												 <td>${map.date }</td>
					                         </tr>
				                        </c:forEach>
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

</body>
</html>