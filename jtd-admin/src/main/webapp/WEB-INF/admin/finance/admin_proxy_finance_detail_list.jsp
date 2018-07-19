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
    <title>代理充值明细</title>
	<link rel="stylesheet" href="${baseurl}css/admin/date.css?d=${activeUser.currentTime}">
    <%@ include file="/WEB-INF/commonjsp/admin_common_css.jsp"%>
    <%@ include file="/WEB-INF/commonjsp/admin_common_js.jsp"%>
</head>

<!-- 选择时间控件 -->

<script type="text/javascript">
	$(function() {
		$("#stat_cycle").dateLayer("startDate","endDate",'${paraMap.startDate}','${paraMap.endDate}',"camp");
		proxy();
	});
	
	function proxy(){
		var id = document.getElementById("pList").value;
		$("#partnerId2").find("option[name='s2']").remove();
		var a = $("#partnerId3").val();
		$.ajax({
			type:"post",
			url:"${pageContext.request.contextPath}/admin/finance/getLevelPartnerName.action",
			data:{"pid":id},
			success:function(data){
				var json = eval("("+data+")");
				var arr = json.list;
				var str = "";
				var sel = "";
				for(var i = 0; i < arr.length; i ++){
					sel = "";
	 				if(arr[i].id == a){
	 					sel = 'selected';
	 				}
					str +="<option name='s2' value='"+arr[i].id+"' "+sel+">"+arr[i].partnerName+"</option>";
				}
				$("#partnerId2").append(str);
			}
		});
	}
	
	function proxyFinaceDetailExcel(){
		$("#form1").attr("action","${pageContext.request.contextPath}/admin/finance/downloadProxyFinaceDetailExcelExcel.action");
		$("#form1").submit();
		$("#form1").attr("action","${pageContext.request.contextPath}/admin/finance/proxyFinaceDetailList.action");
	}
</script>

<body>
	<!--header 导航，搜索 -->
	<jsp:include page="/WEB-INF/admin/admin_header.jsp"></jsp:include>
	<div class="main-container-inner">
		<a class="menu-toggler" id="menu-toggler" href="#"> <span
			class="menu-text"></span>
		</a>
		<input type="hidden" id="partnerId3" value="${paraMap.partnerId2}"/>
		
		<jsp:include page="/WEB-INF/admin/admin_menu.jsp"></jsp:include>
		<div class="main-content">
			<div class="breadcrumbs" id="breadcrumbs">
				<ul class="breadcrumb">
					<li><i class="icon-home home-icon"></i> <a
						href="${pageContext.request.contextPath}">财务管理</a></li>
					<li class="active">代理充值明细</li>
				</ul>
			</div>

			<div class="page-content">

				<div class="row">
					<div class="col-xs-12">
						<form method="post" action="${baseurl }/admin/finance/proxyFinaceDetailList.action" id="form1" class="form-horizontal">
							<div class="form-group">
								<label class="col-sm-1 control-label">代理商名称:</label>
								<select class="col-sm-1" name="partnerId" id="pList" onchange="proxy()" >
									<c:forEach items="${partners }"  var="po">
										<option value="${po.id }"  <c:if test="${paraMap.partnerId == po.id }">selected</c:if>>${po.partnerName }</option>
									</c:forEach>
								</select>

								<label class="col-sm-1 control-label">代理商下级广告主:</label>
								<select class="col-sm-1" id="partnerId2" name="partnerId2">
									<option value="0">--请选择--</option>
								</select>	
								
								<!-- 日期选择 -->
						        <input class="yen_date" value="" id="stat_cycle">
								
                                <span class="col-sm-1 ">
									<button class="btn btn-sm btn-primary icon-search" style="float:right" type="submit">
										查询
									</button>
								</span>
								<span class="col-sm-1">
									<button class="btn btn-sm btn-success icon-download" style="float:right" type="button" onclick="proxyFinaceDetailExcel()">
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
											<th width="10%">代理商名称</th>
											<th width="12%">充值日期</th>
											<th width="9%">充值金额</th>
											<th width="9%">退款金额</th>
											<th width="10%">赠送</th>
											<th width="9%">代理商余额</th>
											<th width="9%">下级广告主名称</th>
											<th width="9%">充值金额</th>
											<th width="9%">退款金额</th>
											<th width="9%">下级账户余额</th>
											<th width="10%">备注</th>
										</tr>
										</thead>

										<tbody>
											<c:forEach items="${list }" var="po">
												<tr>
													<td>${po.partner_name}</td>
													<td><fmt:formatDate value="${po.trade_time}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
													<td>
														<c:if test="${po.type == 0 }">
															<fmt:formatNumber value="${po.amount/100}" pattern="0.00"/>
														</c:if>
														<c:if test="${po.type != 0 }">
															<fmt:formatNumber value="0.00" pattern="0.00"/>
														</c:if>
													</td>
													<td>
														<c:if test="${po.type == 1 }">
															<fmt:formatNumber value="${po.amount/100}" pattern="0.00"/>
														</c:if>	
														<c:if test="${po.type != 1 }">
															<fmt:formatNumber value="0.00" pattern="0.00"/>
														</c:if>
													</td>
													<td><fmt:formatNumber value="${po.pre_gift/100}" pattern="0.00"/></td>
													<td>
														<fmt:formatNumber value="${po.acc_balance_result/100}" pattern="0.00"/></td>
														
													<td>
														${po.two_partnerName}  
													</td>
													<td>  	
														<c:choose>
															<c:when test="${po.two_amount >= 0}">
																<fmt:formatNumber value="${po.two_amount/100}" pattern="0.00"/>
															</c:when>
															<c:otherwise>
																<fmt:formatNumber value="0" pattern="0.00"/>
															</c:otherwise>
														</c:choose>
													</td>
													<td> 
														 <c:choose>
															<c:when test="${po.two_amount < 0}">
																<fmt:formatNumber value="${po.two_amount/100}" pattern="0.00"/>
															</c:when>
															<c:otherwise>
																<fmt:formatNumber value="0" pattern="0.00"/>
															</c:otherwise>
														</c:choose>
													</td>
													<td> 
														<c:if test="${po.two_partnerName == '--'}">
															0.00
														</c:if>
														<c:if test="${po.two_partnerName != '--'}">
															<fmt:formatNumber value="${po.two_acc_balance_result/100}" pattern="0.00"/>
														</c:if>
													</td>
													<td>${po.remark}</td>
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