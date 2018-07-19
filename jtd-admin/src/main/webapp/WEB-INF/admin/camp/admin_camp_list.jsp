<%@page import="com.jtd.web.constants.CampaignType"%>
<%@page import="java.util.Map"%>
<%@page import="com.jtd.web.constants.CampaignStatus"%>
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
<title>活动列表</title>
<%@ include file="/WEB-INF/commonjsp/admin_common_css.jsp"%>
<%@ include file="/WEB-INF/commonjsp/admin_common_js.jsp"%>
</head>
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
						href="${pageContext.request.contextPath}">活动管理</a></li>
					<li class="active">活动列表</li>
				</ul>
			</div>

			<div class="page-content">
					<!-- /.page-header -->
					<div class="row">
						<div class="col-xs-12">
						
							<form method="post" action="${baseurl }admin/camp/list.action" id="formId" class="form-horizontal">
							
							<div class="form-group">
							
								<label class="col-sm-1 control-label">活动ID：</label>
								<input type="text" name="campaignId" class="col-sm-1" value="${queryMap.campaignId }"/>
								
								<label class="col-sm-1 control-label">活动名称：</label>
								<input type="text" name="campaignName" class="col-sm-1" value="${queryMap.campaignName }"/>
								
								<label class="col-sm-1 control-label">广告主名称：</label>
								<input type="text" name="partnerName" class="col-sm-1" value="${queryMap.partnerName }"/>
								
								<label class="col-sm-1 control-label">活动状态：</label>
								<select class="col-sm-2" name="campaignAutoStatus">
									<option value="">全部</option>
									<%
										CampaignStatus[] casArray = CampaignStatus.values();
										for(CampaignStatus cas : casArray){
											pageContext.setAttribute("casCode", cas.getCode());
											pageContext.setAttribute("casDesc", cas.getDesc());
									%>
										<option value="${casCode}" <c:if test="${queryMap.campaignStatus == casCode}">selected</c:if>>${casDesc}</option>
									<%		
										}
									%>
								</select>
								
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
										<table id="sample-table-1"
											class="table table-striped table-bordered table-hover">
											<thead>
												<tr>
													<th>活动ID</th>
													<th class="hidden-480">活动名称</th>
													<th>所属广告主</th>
													<th>活动类型</th>
													<th>活动状态</th>
													<th>渠道设置</th>
													<th>操作</th>
												</tr>
											</thead>
	
											<tbody>
												<c:forEach items="${page.listMap }" var="po">
													<tr>
														<td>${po.id }</td>
														<td class="hidden-480">${po.campaignName }</td>
														<td>${po.partnerName }</td>
														<%
															Map<String,Object> po = (Map<String,Object>)pageContext.getAttribute("po");
														%>
														<td>
															<%
																Integer campType = (Integer)po.get("campaignType");
																if(campType != null){
																	CampaignType ct = CampaignType.fromCode(campType);
																	out.print(ct.getDesc());
																} 
															%>
														</td>
														<td>
															<%
																Integer campaignStatus = (Integer)po.get("campaignStatus");
																if(campaignStatus != null){
																	CampaignStatus cas = CampaignStatus.getCampaignStatus(campaignStatus);
																	out.print(cas.getDesc());
																} 
															%>
														</td>
														<td>
															<c:choose>
																<c:when test="${po.channel_flag}">已设置</c:when>
																<c:otherwise>未设置</c:otherwise>
															</c:choose>
														</td>
														<td>
															<a class="btn btn-xs btn-info" href="${pageContext.request.contextPath}/front/campManage/camp_edit.action?id=${po.id }">
																编辑
															</a>	
															<a class="btn btn-xs btn-inverse" href="${pageContext.request.contextPath}/admin/camp/blackSet.action?id=${po.id }">
																黑名单
															</a>
															<a class="btn btn-xs btn-success" href="${pageContext.request.contextPath}/admin/camp/channelSet.action?id=${po.id }">
																渠道
															</a>
                                                            <c:if test="${activeUser.roleId == 1 or activeUser.roleId == 2 or activeUser.roleId == 8 or activeUser.roleId == 9 or activeUser.roleId == 10}">
                                                                <a class="btn btn-xs btn-pink" href="${pageContext.request.contextPath}/admin/camp/profitSet.action?id=${po.id }">
                                                                    毛利
                                                                </a>
                                                            </c:if>
                                                            <c:if test="${activeUser.roleId == 1}">
                                                                <a class="btn btn-xs btn-pink" href="${pageContext.request.contextPath}/admin/camp/afSet.action?id=${po.id }">
                                                                    AF值
                                                                </a>
                                                            </c:if>
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