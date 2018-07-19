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
    <title>修改用户</title>
    <%@ include file="/WEB-INF/commonjsp/admin_common_css.jsp"%>
    <%@ include file="/WEB-INF/commonjsp/admin_common_js.jsp"%>
    <script type="text/javascript">

        function savePO() {

            var obj = new Object();

            var minPv = $("#minPv").val();
            if(isNaN(minPv) || minPv<0){
                layer.alert("展示数最小值非法!");
                return false;
            }
            minPv = parseFloat(minPv*10000).toFixed(0);
            obj.minPv = minPv;

            var maxPv = $("#maxPv").val();
            if(isNaN(maxPv) || maxPv<0){
                layer.alert("展示数最大值非法!");
                return false;
            }
            if(minPv > maxPv){
                layer.alert("展示数最小值大于最大值!");
                return false;
            }
            maxPv = parseFloat(maxPv*10000).toFixed(0);
            obj.maxPv = maxPv;

            var minUvRatio = $("#minUvRatio").val();
            if(isNaN(minUvRatio) || minUvRatio.toString().length>2 || minUvRatio<0){
                layer.alert("UV占比数值非法或者UV占比数不能大于100!");
                return false;
            }
            minUvRatio = parseFloat(minUvRatio/100).toFixed(2);
            obj.minUvRatio = minUvRatio;
            var maxUvRatio = $("#maxUvRatio").val();
            if(isNaN(maxUvRatio) || maxUvRatio.toString().length>2 || maxUvRatio<0){
                layer.alert("UV占比数值非法或者UV占比数不能大于100!");
                return false;
            }

            if(minUvRatio > maxUvRatio){
                layer.alert("UV占比最小值大于最大值!");
                return false;
            }
            maxUvRatio = parseFloat(maxUvRatio/100).toFixed(2);
            obj.maxUvRatio = maxUvRatio;

            var minUclickRatio = $("#minUclickRatio").val();
            if(isNaN(minUclickRatio) || minUclickRatio.toString().length>2 || minUclickRatio<0){
                layer.alert("有效点击占比数值非法或者有效点击占比数不能大于100!");
                return false;
            }
            minUclickRatio = parseFloat(minUclickRatio/100).toFixed(2);
            obj.minUclickRatio = minUclickRatio;

            var maxUclickRatio = $("#maxUclickRatio").val();
            if(isNaN(maxUclickRatio) || maxUclickRatio.toString().length>2 || maxUclickRatio<0){
                layer.alert("有效点击占比数值非法或者有效点击占比数不能大于100!");
                return false;
            }
            if(minUclickRatio > maxUclickRatio){
                layer.alert("有效点击占比最小值大于最大值!");
                return false;
            }
            maxUclickRatio = parseFloat(maxUclickRatio/100).toFixed(2);
            obj.maxUclickRatio = maxUclickRatio;

            var grossProfit = $("#grossProfit").val();
            if(isNaN(grossProfit) || grossProfit.toString().length>2 || grossProfit<0){
                layer.alert("毛利数值非法或者毛利数不能大于100!");
                return false;
            }
            grossProfit = parseFloat(grossProfit/100).toFixed(2);
            obj.grossProfit = grossProfit;

            var partnerId = $("#partnerId").val();
            obj.partnerId = partnerId;

            var id = $("#id").val();
            obj.id = id;

            $.ajax({
                url:"save.action",
                type:"post",
                contentType : "application/json;charset=utf-8",
                data:JSON.stringify(obj),
                success:function(data){
                    location.href = data.url;
                }
            });
        }

    </script>
</head>
<body>
	<!--header 导航，搜索 -->
	<jsp:include page="/WEB-INF/admin/admin_header.jsp"></jsp:include>
	<div class="main-container-inner">
		<a class="menu-toggler" id="menu-toggler" href="#"> <span
			class="menu-text"></span>
		</a>

		<jsp:include page="/WEB-INF/admin/admin_menu.jsp"></jsp:include>
		<div class="main-content">
			<div class="breadcrumbs" id="breadcrumbs">
				<script type="text/javascript">
					try {
						ace.settings.check('breadcrumbs', 'fixed')
					} catch (e) {
					}
				</script>

				<ul class="breadcrumb">
					<li>
                        <i class="icon-home home-icon"></i>
                        <a href="${pageContext.request.contextPath}">系统工具</a>
                    </li>
					<li class="active">测试帐户自动生成</li>
				</ul>
				<!-- .breadcrumb -->
			</div>

			<div class="page-content">
				<div class="page-header">
					<h1>测试帐户自动生成</h1>
				</div>
				
				<form id="form" class="form-horizontal" >
				
				<div class="row">
					<div class="col-xs-12">
                        <input type="hidden" id="id" name="id" value="${ po.id }">
						<input type="hidden" id="partnerId" name="partnerId" value="${ po.partnerId }">
                        <div class="form-group has-info">
                            <label class="col-xs-12 col-sm-2 control-label ">展示数(单位:万)</label>
                            <div class="col-xs-12 col-sm-2">
									<span class="block input-icon input-icon-right">
                                        <input type="text" id="minPv" name="minPv" class="width-100" value="<fmt:formatNumber value="${po.minPv/10000}" maxFractionDigits="0"/>" >
									</span>
                            </div>
                            <label class="col-sm-1">-</label>
                            <div class="col-xs-12 col-sm-2">
									<span class="block input-icon input-icon-right">
                                        <input type="text" id="maxPv" name="maxPv" class="width-100" value="<fmt:formatNumber value="${po.maxPv/10000}" maxFractionDigits="0"/>">
									</span>
                            </div>
                            <label class="col-sm-1 control-label">* 每活动每小时</label>
                        </div>
                        <div class="form-group has-info">
                            <label class="col-xs-12 col-sm-2 control-label ">UV占比(单位%)</label>
                            <div class="col-xs-12 col-sm-2">
									<span class="block input-icon input-icon-right">
                                        <input type="text" id="minUvRatio" name="minUvRatio" class="width-100" value="<fmt:formatNumber value="${po.minUvRatio*100}" maxFractionDigits="0"/>" >
									</span>
                            </div>
                            <label class="col-sm-1">-</label>
                            <div class="col-xs-12 col-sm-2">
									<span class="block input-icon input-icon-right">
                                        <input type="text" id="maxUvRatio" name="maxUvRatio" class="width-100" value="<fmt:formatNumber value="${po.maxUvRatio*100}" maxFractionDigits="0"/>">
									</span>
                            </div>
                        </div>
                        <div class="form-group has-info">
                            <label class="col-xs-12 col-sm-2 control-label ">有效点击占比(单位%)</label>
                            <div class="col-xs-12 col-sm-2">
									<span class="block input-icon input-icon-right">
                                        <input type="text" id="minUclickRatio" name="minUclickRatio" class="width-100" value="<fmt:formatNumber value="${po.minUclickRatio*100}" maxFractionDigits="0"/>" >
									</span>
                            </div>
                            <label class="col-sm-1">-</label>
                            <div class="col-xs-12 col-sm-2">
									<span class="block input-icon input-icon-right">
                                        <input type="text" id="maxUclickRatio" name="maxUclickRatio" class="width-100" value="<fmt:formatNumber value="${po.maxUclickRatio*100}" maxFractionDigits="0"/>">
									</span>
                            </div>
                        </div>

                        <div class="form-group has-info">
                            <label class="col-xs-12 col-sm-2 control-label ">毛利率(单位%)</label>
                            <div class="col-xs-12 col-sm-2">
									<span class="block input-icon input-icon-right">
                                        <input type="text" id="grossProfit" name="grossProfit" class="width-100" value="<fmt:formatNumber value="${po.grossProfit*100}" maxFractionDigits="0"/>" >
									</span>
                            </div>
                        </div>

                        <div class="clearfix form-actions">
                            <div class="col-md-offset-3 col-md-9">
                                <button class="btn btn-info" type="button" id="btn_submit" onclick="savePO()">
                                    <i class="icon-ok bigger-110"></i>
                                    提交
                                </button>
                                &nbsp; &nbsp; &nbsp;
                                <button class="btn" type="reset">
                                    <i class="icon-undo bigger-110"></i>
                                    返回
                                </button>
                            </div>
                        </div>
					</div>
					<!-- /.col -->
				</div>
				
				</form>
				
			</div>
			<!-- /.page-content -->
		</div>
	</div>

</body>
</html>