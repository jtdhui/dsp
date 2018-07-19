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
		
		//默认先查当天的
		getReport();
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
		
		getReport();
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
	
	function getReport(){
		
		var startDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		if(startDate == "" || endDate == ""){
			layer.msg("请填写开始时间和结束时间，<br\>并且前后跨度不超过10天");
			return ;
		}
		var ds = new Date(startDate);
		var de = new Date(endDate);
		//console.log(ds);
		//console.log((de - ds)/86400000);
		if(((de - ds)/86400000) + 1 > 10){
			layer.msg("时间跨度不能超过10天");
			return ;
		}
		
		var orderByFlag = $("#orderBy").val() ;
		var descFlag = $("#desc").val();
		if(orderByFlag == ""){
			orderByFlag = "showDate" ;
			descFlag = "desc" ;
			$("#orderBy").val(orderByFlag);
			$("#desc").val(descFlag);
			desc(orderByFlag);
		}
		
		//loading层
		var load = layer.open({
			  type: 3,
			  icon: 2,
			  content: ''
		});
		
		$.ajax({
			type: "post",
            url: "${pageContext.request.contextPath}/admin/operate/ajaxBesCreativeRTBReport.action",
            data:{"startCreativeId":$("#startCreativeId").val() , "endCreativeId":$("#endCreativeId").val(),"startDate":startDate,"endDate":endDate,"orderBy":orderByFlag,"desc":descFlag },
            success: function (data) {
            	//console.log(data);
            	$("#table1 tbody").html("");
            	
            	if(data.code == 1 && data.resultList){
            		
            		var list = data.resultList ;
            		
            		var html = "" ;
            		for(var i = 0 ; i < list.length ; i++){
            			html += "<tr>";
            			html += "<td>" + list[i].showDate + "</td>";
            			html += "<td>" + list[i].creativeId + "</td>";
            			html += "<td>" + list[i].creativeType + "</td>";
            			html += "<td>" + list[i].winRate + "</td>";
            			html += "<td>" + list[i].loseRate + "</td>";
            			html += "<td>" + list[i].sizeIncorrectRate + "</td>";
            			html += "<td>" + list[i].adTypeIncorrectRate + "</td>";
            			html += "<td>" + list[i].tradeRejectRate + "</td>";
            			html += "<td>" + list[i].advertiserIncorrectRate + "</td>";
            			html += "<td>" + list[i].minPriceBadRate + "</td>";
            			html += "<td>" + list[i].advertiserRejectRate + "</td>";
            			html += "<td>" + list[i].adRejectRate + "</td>";
            			html += "<td>" + list[i].snippetIncorrectRate + "</td>";
            			html += "<td>" + list[i].notWhitelistRate + "</td>";
            			html += "<td>" + list[i].advertiserNotExistRate + "</td>";
            			html += "<td>" + list[i].flashVerIncorrectRate + "</td>";
            			html += "<td>" + list[i].idMisMatchRate + "</td>";
            			html += "<td>" + list[i].incorrectOrderIdRate + "</td>";
            			html += "<td>" + list[i].bidderAbandonBiddingRate + "</td>";
            			html += "<td>" + list[i].bidderResponseDataErrorRate + "</td>";
            			html += "<td>" + list[i].bidderInvalidHtmlSnippetRate + "</td>";
            			html += "<td>" + list[i].bidderMissingCategoryRate + "</td>";
            			html += "</tr>";
            			
            			
            		}
            		$("#table1 tbody").html(html);
            	}
            	else{
            		if(data.msg){
            			layer.msg("获取报表错误");
            		}
            	}
            	
            	layer.close(load);
            },
            error: function (msg) {
            	
            	layer.msg("获取报表错误");
            	
            	console.log(msg);
            	
            	layer.close(load);
            }
		});
	}
	
	function exportExcel(){
		
		$("#form1").attr("action","${pageContext.request.contextPath}/admin/operate/exportBesCreativeRTBReport.action");
		$("#form1").submit();
		
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
					<li class="active">百度创意竞价情况</li>
				</ul>
			</div>

			<div class="page-content">
				<!-- /.page-header -->
				<div class="row">
					<div class="col-xs-12">
						<form id="form1" class="form-horizontal" action="" method="post">
							<div class="form-group ">
								
								<label class="col-sm-1 control-label">创意id：</label>
								<input type="text" id="startCreativeId" name="startCreativeId" class="col-sm-1" value=""/>
								<label class="col-sm-1" style="width:10px;">-</label>
								<input type="text" id="endCreativeId" name="endCreativeId" id="endDate" class="col-sm-1" value=""/>
								<label class="col-sm-2">(填写单个为指定，填写两个为区间)</label>
								
<!-- 								<label class="col-sm-1 control-label">活动id：</label> -->
<%-- 								<input type="text" name="campaignId" class="col-sm-1" value="${campaignId }"/> --%>
								
								<label class="col-sm-1 control-label">统计时间：</label>
								<input type="text" name="startDate" id="startDate" class="col-sm-1" value="${startDate }"/>
								<label class="col-sm-1" style="width:10px;">-</label>
								<input type="text" name="endDate" id="endDate" class="col-sm-1" value="${endDate }"/>
								
								<span class="col-sm-1">
									<button class="btn btn-sm btn-primary icon-search" style="float:right" type="button" onclick="getReport()">
										查询
									</button>
								</span>
								
								<span class="col-sm-1">
									<button class="btn btn-sm btn-success icon-download" style="float:right" type="button" onclick="exportExcel()">
										下载报表
									</button>
								</span>
								
								<input type="hidden" id="orderBy" name="orderBy" value=""/>
								<input type="hidden" id="desc" name="desc" value=""/>
							</div>
						</form>
						<div class="row">
							<div class="col-xs-12">
								<div class="table-responsive">
									<table id="table1" class="table table-striped table-bordered table-hover">
										<thead>
											<tr>
												<th width="4.5%" onclick="orderBy('showDate')">日期 <img id="showDate_order" class="order"/></th>
												<th width="4.5%" onclick="orderBy('creativeId')">创意ID <img id="creativeId_order" class="order"/></th>
												<th width="4.5%">创意类型</th>
												<th width="4.5%" onclick="orderBy('winRate')">竞价成功 <img id="winRate_order" class="order"/></th>
												<th width="4.5%" onclick="orderBy('loseRate')">竞价非最高 <img id="loseRate_order" class="order"/></th>
												<th width="4.5%" onclick="orderBy('sizeIncorrectRate')">尺寸不符 <img id="sizeIncorrectRate_order" class="order"/></th>
												<th width="4.5%" onclick="orderBy('adTypeIncorrectRate')">创意类型不符 <img id="adTypeIncorrectRate_order" class="order"/></th>
												<th width="4.5%" onclick="orderBy('tradeRejectRate')">行业不符媒体要求 <img id="tradeRejectRate_order" class="order"/></th>
												<th width="4.5%" onclick="orderBy('advertiserIncorrectRate')">广告主不符媒体要求 <img id="advertiserIncorrectRate_order" class="order"/></th>
												<th width="4.5%" onclick="orderBy('minPriceBadRate')">价格低于媒体设置 <img id="minPriceBadRate_order" class="order"/></th>
												<th width="4.5%" onclick="orderBy('advertiserRejectRate')">广告主未通过检查 <img id="advertiserRejectRate_order" class="order"/></th>
												<th width="4.5%" onclick="orderBy('adRejectRate')">创意未通过检查 <img id="adRejectRate_order" class="order"/></th>
												<th width="4.5%" onclick="orderBy('snippetIncorrectRate')">html宏不符合规范 <img id="snippetIncorrectRate_order" class="order"/></th>
												<th width="4.5%" onclick="orderBy('notWhitelistRate')">snippet DSP非白名单 <img id="notWhitelistRate_order" class="order"/></th>
												<th width="4.5%" onclick="orderBy('advertiserNotExistRate')">广告主不存在 <img id="advertiserNotExistRate_order" class="order"/></th>
												<th width="4.5%" onclick="orderBy('flashVerIncorrectRate')">flash版本不符 <img id="flashVerIncorrectRate_order" class="order"/></th>
												<th width="4.5%" onclick="orderBy('idMisMatchRate')">响应ID不对应 <img id="idMisMatchRate_order" class="order"/></th>
												<th width="4.5%" onclick="orderBy('incorrectOrderIdRate')">DSP返回了错误的order id <img id="incorrectOrderIdRate_order" class="order"/></th>
												<th width="4.5%" onclick="orderBy('bidderAbandonBiddingRate')">DSP放弃竞价 <img id="bidderAbandonBiddingRate_order" class="order"/></th>
												<th width="4.5%" onclick="orderBy('bidderResponseDataErrorRate')">返回数据无法解析 <img id="bidderResponseDataErrorRate_order" class="order"/></th>
												<th width="4.5%" onclick="orderBy('bidderInvalidHtmlSnippetRate')">html为空或超过大小限制 <img id="bidderInvalidHtmlSnippetRate_order" class="order"/></th>
												<th width="4.5%" onclick="orderBy('bidderMissingCategoryRate')">缺少行业id <img id="bidderMissingCategoryRate_order" class="order"/></th>
											</tr>
										</thead>
										<tbody>

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