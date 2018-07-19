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
<title>渠道设置</title>
<%@ include file="/WEB-INF/commonjsp/admin_common_css.jsp"%>
<%@ include file="/WEB-INF/commonjsp/admin_common_js.jsp"%>
<script type="text/javascript">

// 保存
function setChannel(id){
	var obj=new Object();
	obj.campaignId =id+"";
	var channelIds = "";
	$("input[name='channelCheck']:checked",$('#channel-table')).each(function(){
		var channel_id = $(this).val();
		var selectVal = $("#channel-select"+channel_id).val();
		var channel_catgserial = $("#channel_catgserial"+channel_id).val();
		channelIds +=  channel_id+"$$$"+selectVal+"$$$"+channel_catgserial+",";
	});
	
	if(channelIds.length>1){
		channelIds = channelIds.substring(0,channelIds.length-1);
	}
	obj.dimValue = channelIds+"";
	 $.ajax({
		url:"${baseurl }admin/camp/channel/update.action",
		type:"post",
		dataType: "json",
		data:{campaignId:obj.campaignId,dimValue:obj.dimValue},
		success:function(data){
            layer.open({
                content: data.msg,
                time: 0, //不自动关闭
                btn: ['确定'],
                yes: function (index) {
                    layer.close(index);
                    location.href = data.url;
                }
            });
		}
	}); 
}

$(function(){
	/* var chanelObj = _campaignDicData.channelCategorys;
	$.each(chanelObj.DSP, function (index, e) {
        $("select[name='channel-select']").append("<option value=" + e.id + ">" + e.name + "</option>");
    }); */
});
</script>
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
				<script type="text/javascript">
					try {
						ace.settings.check('breadcrumbs', 'fixed')
					} catch (e) {
					
					}
				</script>

				<ul class="breadcrumb">
					<li><i class="icon-home home-icon"></i> <a
						href="${pageContext.request.contextPath}">活动管理</a></li>
					<li class="active">渠道设置</li>
				</ul>
				<!-- .breadcrumb -->

			</div>

			<div class="page-content">
				<div class="page-header">
					<h1>渠道设置</h1>
				</div>
				<div class="row">
					<div class="col-xs-12">
							<input type="hidden" id="id" name="id" value="${po.id }">
							<div class="form-group has-info">
								<label class="col-xs-12 col-sm-3 control-label no-padding-right">名称</label>
								<div class="col-xs-12 col-sm-5">
									<span class="block input-icon input-icon-right"> 
										<input type="text" disabled class="width-100" value="${po.campaignName }">
									</span>
								</div>
								<div class="help-block col-xs-12 col-sm-reset inline"></div>
							</div>
							
							<div class="form-group has-info">
								<label class="col-xs-12 col-sm-3 control-label no-padding-right">所属广告主</label>
								<div class="col-xs-12 col-sm-5">
									<span class="block input-icon input-icon-right"> 
										<input type="hidden" id="partnerId" name="partnerId" value="${po.partnerId }">
										<input type="text" disabled class="width-100" value="${po.partnerName }" >
									</span>
								</div>
								<div class="help-block col-xs-12 col-sm-reset inline"></div>
							</div>
							
							<div class="form-group has-info">
								<label class="col-xs-12 col-sm-3 control-label no-padding-right">所属行业</label>
								<div class="col-xs-12 col-sm-5">
									<span class="block input-icon input-icon-right"> 
										<input type="text" disabled class="width-100" value="${po.campaignType }" >
									</span>
								</div>
								<div class="help-block col-xs-12 col-sm-reset inline"></div>
							</div>
							
							<div class="form-group has-info">
								<label class="col-xs-12 col-sm-3 control-label no-padding-right">渠道设置</label>
								<div class="col-xs-12 col-sm-5">
									<table id="channel-table" class="table table-striped table-bordered table-hover">
										<thead>
											<tr>
												<th class="center">
													<!-- <label>
														<input type="checkbox" class="ace" /> <span class="lbl"></span>
													</label> -->
												</th>
												<th>渠道ID</th>
												<th>渠道名称</th>
												<th>行业类别</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${channelList }" var="vo">
												<tr>
													<td class="center">
														<label>  
															<c:choose>
																<c:when test="${vo.campaign_id==po.id and vo.catgid !=null }">
																	<input type="checkbox" class="ace" name="channelCheck" value="${vo.id }" checked="checked"/> 
																</c:when>
																<c:otherwise>
																	<input type="checkbox" class="ace" name="channelCheck" value="${vo.id }"/> 
																</c:otherwise>
															</c:choose>
															<span class="lbl"></span>
														</label>
													</td>
													<td>${vo.id }</td>
													<td>${vo.channelName }
														<input type="hidden" id="channel_catgserial${vo.id }" value="${vo.catgserial }"/>
													</td>
													<td>
														<select class="form-control" name="channel-select" id="channel-select${vo.id }" >
                                                            <c:if test="${vo.catgserial=='TANX'}">
                                                                <c:set var="channalName" value="${tanxChannelDIC}"></c:set>
                                                            </c:if>
                                                            <c:if test="${vo.catgserial=='BES'}">
                                                                <c:set var="channalName" value="${besChannelDIC}"></c:set>
                                                            </c:if>
                                                            <c:if test="${vo.catgserial=='VAM'}">
                                                                <c:set var="channalName" value="${vamChannelDIC}"></c:set>
                                                            </c:if>
                                                            <c:if test="${vo.catgserial=='TENCENT'}">
                                                                <c:set var="channalName" value="${tencentChannelDIC}"></c:set>
                                                            </c:if>
                                                            <c:if test="${vo.catgserial=='XTRADER'}">
                                                                <c:set var="channalName" value="${xtraderChannelDIC}"></c:set>
                                                            </c:if>
															<c:forEach items="${channalName }" var="dicVO">
																<c:choose>
																	<c:when test="${vo.campaign_id==po.id and vo.catgid ==dicVO.id }">
																		<option value="${dicVO.id}" selected='selected'>${ dicVO.name}</option>
																	</c:when>
																	<c:otherwise>
																		<option value="${dicVO.id}">${ dicVO.name}</option>
																	</c:otherwise>
																</c:choose>
															</c:forEach>
														</select>
													</td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
								</div>
								<div class="help-block col-xs-12 col-sm-reset inline"></div>
							</div>
							
							<div class="clearfix form-actions" style="margin-top:250px;">
								<div class="col-md-offset-3 col-md-9">
									<button class="btn btn-info" type="submit" onclick="setChannel(${po.id})">
										<i class="icon-ok bigger-110"></i> 提交
									</button>
									&nbsp; &nbsp; &nbsp;
									<button class="btn" type="reset">
										<i class="icon-undo bigger-110"></i> 重置
									</button>
								</div>
							</div>
					</div>
					<!-- /.col -->
				</div>
			</div>
			<!-- /.page-content -->
			</div>
		</div>
	</div>
	
</body>
</html>