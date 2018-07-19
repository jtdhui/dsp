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
<title>创意审核</title>
<%@ include file="/WEB-INF/commonjsp/admin_common_css.jsp"%>
<%@ include file="/WEB-INF/commonjsp/admin_common_js.jsp"%>
<script type="text/javascript">
	function setSelectVal() {
		var selectVal = $("#menu-select").val();
		$("#parentId").val(selectVal);
	}
	function setAdAudit(id){
		
		//渠道审核参数
		var obj=new Object();
		obj.campaignId =id+"";
		var channelIds = "";
		$("input[name='channelCheck']",$('#channel-sets')).each(function(){
			var channel_id = $(this).val();
			var selectVal = $("#channel-select"+channel_id).val();
			var channel_catgserial = $("#channel_catgserial"+channel_id).val();
			channelIds +=  channel_id+"$$$"+selectVal+"$$$"+channel_catgserial+",";
		});
		
		if(channelIds.length>1){
			channelIds = channelIds.substring(0,channelIds.length-1);
		}
		obj.dimValue = channelIds+"";
		
		//创意参数收集
		var adObj=new Object();
		adObj.id= $("#id").val();
		var interSelectVal= $("input[name='InAudit']:checked").val();
		var msg = "";
		if(interSelectVal==1){
			adObj.internalAudit = interSelectVal;
			msg = "你确定将该创意审核通过么?";
		}else{
			msg = "你确定将该创意审核拒绝么?";
			adObj.internalAudit = interSelectVal;
			var audit_info = $("#audit_info").val();
			adObj.internalAuditInfo = audit_info;
			
			obj.dimValue = ""; //如果内部审核拒绝,则渠道置为空. 也就是不向ADX同步数据.
		}
		
		//提交渠道审核信息
		layer.open({
			content:msg,
			time: 0, //不自动关闭
			btn: ['确定', '取消'],
			yes: function(index){
				layer.close(index);

				layer.load(2);  //加载层
				//默认等待20秒
				setTimeout(function(){
					layer.closeAll('loading');
				}, 20000);

				$.ajax({
					url:"${baseurl }admin/ad/adAuditUpdate.action",
					type:"post",
					dataType: "json",
					data:{campaignId:obj.campaignId,dimValue:obj.dimValue,adId:adObj.id,internalAudit:adObj.internalAudit,internalAuditInfo:adObj.internalAuditInfo },
					success:function(ret){

						layer.closeAll('loading'); //关闭加载层

						if(ret.success){
							location.href = ret.url;
						}else{
							layer.alert("创意提交渠道审核失败!");
							return false;
						}
					}
				});
			}
		});
		
	}
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
						href="${pageContext.request.contextPath}">创意管理</a></li>
					<li class="active">创意列表</li>
					<li class="active">创意审核</li>
				</ul>
				<!-- .breadcrumb -->
			</div>

			<div class="page-content">
				<div class="page-header">
					<h1>创意审核</h1>
				</div>
				<div class="row">
					<div class="col-xs-12 form-horizontal" id="channel-sets">
						
							<input type="hidden" id="id" name="id" value="${po.id }">
							创意内部审核状态
							<div class="hr hr-5"></div>
							<div class="form-group has-info">
								<label class="col-xs-12 col-sm-1 control-label no-padding-right"></label>
								<div class="col-xs-12 col-sm-7">
									<span class="block input-icon input-icon-right">
										<input name="InAudit" type="radio" <c:if test="${ po.internal_audit != 2 }"> checked </c:if> value="1" />&nbsp;通过
										<input name="InAudit" type="radio" <c:if test="${ po.internal_audit == 2 }"> checked </c:if> value="2" />&nbsp;拒绝 &nbsp;&nbsp;原因:
										<input type="text" id="audit_info" name="audit_info" class="width-50"   value="<c:if test="${po.internal_audit ==2}">${ po.internal_audit_info}</c:if>">
									</span>
								</div>
								<div class="help-block col-xs-12 col-sm-reset inline"></div>
							</div>
							创意详情:
							<div class="hr hr-5"></div>
							<div class="form-group has-info">
								<label class="col-xs-12 col-sm-1 control-label no-padding-right">活动ID:</label>
								<div class="col-xs-12 col-sm-7">
									<span class="block input-icon input-icon-right"> <input
										type="text"  class="col-sm-4" readonly style="border:0px solid #d5d5d5;"
										value="${po.campaign_id }">
									</span>
								</div>
								<div class="help-block col-xs-12 col-sm-reset inline"></div>
							</div>
							<div class="form-group has-info">
								<label class="col-xs-12 col-sm-1 control-label no-padding-right">活动名称:</label>
								<div class="col-xs-12 col-sm-7">
									<span class="block input-icon input-icon-right"> <input
										type="text"  class="col-sm-4" readonly style="border:0px solid #d5d5d5;"
										value="${po.campaign_name }">
									</span>
								</div>
								<div class="help-block col-xs-12 col-sm-reset inline"></div>
							</div>
							<div class="form-group has-info">
								<label class="col-xs-12 col-sm-1 control-label no-padding-right">所属广告主:</label>
								<div class="col-xs-12 col-sm-7">
									<span class="block input-icon input-icon-right"> <input
										type="text" class="col-sm-4" readonly style="border:0px solid #d5d5d5;"
										value="${po.partner_name }">
									</span>
								</div>
								<div class="help-block col-xs-12 col-sm-reset inline"></div>
							</div>
							<div class="form-group has-info">
								<label class="col-xs-12 col-sm-1 control-label no-padding-right">广告主行业:</label>
								<div class="col-xs-12 col-sm-7">
									<span class="block input-icon input-icon-right"> 
									<c:forEach items="${dspChannelDIC }" var="dicVO">
										<c:if test="${dicVO.id == po.parter_catgid}">
											<input type="text"  class="col-sm-4" readonly style="border:0px solid #d5d5d5;"
										value="${dicVO.name }">
										</c:if>
									</c:forEach>
									</span>
								</div>
								<div class="help-block col-xs-12 col-sm-reset inline"></div>
							</div>
							
							<div class="form-group has-info">
								<label class="col-xs-12 col-sm-1 control-label no-padding-right">创意ID:</label>
								<div class="col-xs-12 col-sm-7">
									<span class="block input-icon input-icon-right"> <input
										type="text" class="col-sm-4" readonly style="border:0px solid #d5d5d5;"
										value="${po.id }">
									</span>
								</div>
								<div class="help-block col-xs-12 col-sm-reset inline"></div>
							</div>
							
							<div class="form-group has-info">
								<label class="col-xs-12 col-sm-1 control-label no-padding-right">创意规格:</label>
								<div class="col-xs-12 col-sm-7">
									<span class="block input-icon input-icon-right"> <input
										type="text" class="col-sm-4" readonly style="border:0px solid #d5d5d5;"
										value="${po.size }">
									</span>
								</div>
								<div class="help-block col-xs-12 col-sm-reset inline"></div>
							</div>
							
							<div class="form-group has-info">
								<label class="col-xs-12 col-sm-1 control-label no-padding-right">预览图:</label>
								<div class="col-xs-12 col-sm-7">
									<span class="block input-icon input-icon-right"> 
										<img src="${po.creative_url }"/>
									</span>
								</div>
								<div class="help-block col-xs-12 col-sm-reset inline"></div>
							</div>
							
							<div class="form-group has-info">
								<label class="col-xs-12 col-sm-1 control-label no-padding-right">点击链接:</label>
								<div class="col-xs-12 col-sm-11">
									<span class="block input-icon input-icon-right"> <input
										type="text" class="col-sm-11" readonly style="border:0px solid #d5d5d5;"
										value="${po.click_url }">
									</span>
								</div>
								<div class="help-block col-xs-12 col-sm-reset inline"></div>
							</div>
							<c:forEach items="${listAAS }" var="vo">
							<div class="form-group has-info">
								<label class="col-xs-12 col-sm-1 control-label no-padding-right">${vo.channel_name }:</label>
								<input type="hidden" id="channel_catgserial${vo.channel_id }" value="${vo.catgserial }"/>
								<label class="col-xs-12 col-sm-1 control-label no-padding-right">
                                    &nbsp;&nbsp;&nbsp;
									<c:if test="${vo.status ==0}">未提交</c:if>
                                    <c:if test="${vo.status ==1}">已提交</c:if>
                                    <c:if test="${vo.status ==2}">审核通过</c:if>
                                    <c:if test="${vo.status ==3}">审核拒绝</c:if>
                                    &nbsp;&nbsp;&nbsp;
								</label>
								<div class="col-xs-12 col-sm-2" >
									<input type="hidden" name="channelCheck" value="${vo.channel_id }" checked="checked"/> 
									<select class="form-control" name="channel-select" id="channel-select${vo.channel_id }">
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
                                        <c:if test="${vo.catgserial=='HZENG'}">
                                            <c:set var="channalName" value="${hzengChannelDIC}"></c:set>
                                        </c:if>
										<c:forEach items="${channalName }" var="dicVO">
											<c:choose>
												<c:when test="${vo.catgid ==dicVO.id }">
													<option value="${dicVO.id}" selected='selected'>${ dicVO.name}</option>
												</c:when>
												<c:otherwise>
													<option value="${dicVO.id}">${ dicVO.name}</option>
												</c:otherwise>
											</c:choose>
										</c:forEach>
									</select>
								</div>
								<div class="col-xs-12 col-sm-8">
									<label class="col-xs-12 col-sm-12 no-padding-right" style="color:red">${vo.audit_info}</label>
								</div>
								<div class="help-block col-xs-12 col-sm-reset inline"></div>
							</div>
							</c:forEach>
							<div class="clearfix form-actions">
								<div class="col-md-offset-3 col-md-9">
									<button class="btn btn-info" type="submit" onclick="setAdAudit(${po.campaign_id})">
										<i class="icon-ok bigger-110"></i> 提交
									</button>
									&nbsp; &nbsp; &nbsp;
									<button class="btn" type="reset">
										<i class="icon-undo bigger-110"></i> 取消
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