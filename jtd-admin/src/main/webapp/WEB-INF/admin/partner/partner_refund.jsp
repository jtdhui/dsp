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
<title>发起退款</title>
<%@ include file="/WEB-INF/commonjsp/admin_common_css.jsp"%>
<%@ include file="/WEB-INF/commonjsp/admin_common_js.jsp"%>

</head>

<script type="text/javascript">
	$(function(){
		
		$("#button").click(function(){
			
			var amount = $("#amount").val() ;
            var status = $("input[name='status']:checked").val();
			var remark = $("#remark").val();
			var id = $("#id").val();
			var partnerId = $("#partnerId").val();
			if(status==2){ //如果拒绝,备忘须填写原因
				if(remark=="" || remark ==null){
					layer.msg("您选择的是拒绝退款,需在备注里填写拒绝原因!");
					return ;
				}
			}else{
				if( amount == "" || /^\d+(\.\d{1,2})?$/.test(amount) == false || amount == 0){
					layer.msg("请输入退款金额，最多两位小数");
					return ;
				}
			}
            amount = parseFloat(amount*100).toFixed(0);
			var obj = new Object();
			obj.id = id;
			obj.partnerId = partnerId;
			obj.status = status;
			obj.amount = amount;
			obj.remark = remark;
			obj.type = 1;

			$.ajax({
				type:"post",
				url:"${pageContext.request.contextPath}/admin/partner/rechargeSave.action",
				contentType : "application/json;charset=utf-8",
				data:JSON.stringify(obj),
				success:function(data){
					if(data.msg && data.msg == "ok"){
	            		layer.msg("操作成功");
	            		
	            		setTimeout(function(){
							location.href = "${pageContext.request.contextPath}/admin/partnerPre/list.action";
						}, 1300);
	            	}
	            	else{
	            		layer.msg(data.msg);
	            	}
					
				},
				error:function(e){
					layer.msg("退款失败");
					console.log("error");
	            	console.log(msg);
				}
			});
			
		});
		
		$("#acc_balance_refresh").click(function(){
			document.location.reload();
		});
		
	});
	
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
				<script type="text/javascript">
					try {
						ace.settings.check('breadcrumbs', 'fixed');
					} catch (e) {
					}
				</script>
				<!-- .breadcrumb -->

			</div>
			
			<div class="page-content">
				<h1>退款</h1>
				<div class="row">
					<div class="col-xs-12">
					
						<form id="form" class="form-horizontal">
						
							<!-- id -->
							<input type="hidden" id="id" name="id" value="${partnerPreFlow.id }"/>
							<input type="hidden" id="partnerId" name="partnerId" value="${partnerPreFlow.partnerId }"/>

							<!-- row 1 -->
							<div class="form-group has-info">
								
								<label class="col-xs-12 col-sm-3 control-label no-padding-right">公司全称</label>
								<div class="col-xs-12 col-sm-5">
									<label class="control-label">${partnerPreFlow.partnerName }</label>
								</div>
								
							</div>
							<div class="form-group has-info">
								
								<label class="col-xs-12 col-sm-3 control-label no-padding-right">预充金额</label>
								<div class="col-xs-12 col-sm-1">
									<label class="control-label" id="acc_balance"><fmt:formatNumber value="${partnerPreFlow.preAmount/100}" pattern="0.00"/>元</label>
								</div>
								<%--<div class="col-xs-12 col-sm-1">--%>
									<%--<label class="control-label" id="acc_balance_refresh">--%>
									<%--<button class="btn btn-xs btn-primary icon-refresh">--%>
										<%--刷新--%>
									<%--</button>--%>
								<%--</div>--%>
							</div>
							<div class="form-group has-info">
								<label class="col-xs-12 col-sm-3 control-label no-padding-right">退款状态</label>
								<div class="col-xs-12 col-sm-9">
									<label class="line-height-1 blue">
										<input name="status" type="radio" class="ace" value="1" checked >
										<span class="lbl"> 同意</span>
									</label>
									<label class="line-height-1 blue">
										<input name="status" type="radio" class="ace" value="2">
										<span class="lbl"> 拒绝</span>
									</label>
								</div>
							</div>
							<div class="form-group has-info">
								<label class="col-xs-12 col-sm-3 control-label no-padding-right">退款金额</label>
								<div class="col-xs-12 col-sm-9">
									<input type="text" id="amount" name="amount" class="width-20" value="<fmt:formatNumber value="${partnerPreFlow.preAmount/100}" pattern="0.00"/>">
									<label class="control-label">元，最多两位小数</label>
								</div>
							</div>
							<div class="form-group has-info">
								<label class="col-xs-12 col-sm-3 control-label no-padding-right">备注:</label>
								<div class="col-xs-12 col-sm-9">
									<textarea  id="remark" name="remarks" style="height: 100px; width: 400px;"></textarea>
								</div>
							</div>

							<div class="clearfix form-actions">
								<div class="col-md-offset-3 col-md-9">
									<button class="btn btn-info" id="button" type="button">
										<i class="icon-ok bigger-110"></i>
										提交
									</button>

									&nbsp; &nbsp; &nbsp;
									<button class="btn" type="reset">
										<i class="icon-undo bigger-110"></i>
										重置
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

</body>
</html>