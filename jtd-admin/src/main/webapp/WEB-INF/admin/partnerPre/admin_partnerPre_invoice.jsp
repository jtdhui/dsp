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
<title>添加角色</title>
<%@ include file="/WEB-INF/commonjsp/admin_common_css.jsp"%>
<%@ include file="/WEB-INF/commonjsp/admin_common_js.jsp"%>

<script src="${baseurl}assets/js/jquery.autosize.min.js"></script>

</head>

<script type="text/javascript">
	$(function(){
		
		$('textarea[class*=autosize]').autosize({append: "\n"});

        $("input[name='isInvoice']").click(function(){
            var isInvoice = $(this).val();
            if (isInvoice == "1") {
                $("#invoice_btn").css('display','block');
            } else if (isInvoice == "2") {
                $("#invoice_btn").css('display','none');
            }
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

				<ul class="breadcrumb">
					<li><i class="icon-home home-icon"></i> 财务管理</li>
					<li class="active">发票详情</li>
				</ul>
				<!-- .breadcrumb -->

			</div>
			
			<div class="page-content">
				<div class="page-header">
					<h1>发票详情</h1>
				</div>
				<div class="row">
					<div class="col-xs-12">
						<form id="form" class="form-horizontal" action="${baseurl }admin/partnerPre/saveInvoice.action" method="post">
							
							<input type="hidden" id="id" name="id" value="${po.id }">
							
							<div class="form-group has-info">
								<label class="col-xs-12 col-sm-3 control-label no-padding-right">广告主名称</label>
								<div class="col-xs-12 col-sm-5">
									<span class="block input-icon input-icon-right"> 
										<input type="text" id="partnerName" name="partnerName" class="width-100" value="${po.partnerName }" disabled>
									</span>
								</div>
								<div class="help-block col-xs-12 col-sm-reset inline"></div>
							</div>
                            <div class="form-group has-info">
                                <label class="col-xs-12 col-sm-3 control-label no-padding-right"></label>
                                <div class="col-xs-12 col-sm-5">
                                    <div>
                                        <label class="line-height-1 blue">
                                            <input type="radio" name="isInvoice" value="1" class="ace" checked>
                                            <span class="lbl">开发票</span>
                                        </label>
                                        <label class="line-height-1 blue">
                                            <input type="radio" name="isInvoice" value="2" class="ace">
                                            <span class="lbl">不开发票</span>
                                        </label>
                                    </div>
                                </div>
							</div>

                            <div class="form-group has-info" id="invoice_btn">
                                <label class="col-xs-12 col-sm-3 control-label no-padding-right">发票号码</label>
                                <div class="col-xs-12 col-sm-5">
									<span class="block input-icon input-icon-right">
										<input type="text" id="invoice" name="invoice" class="width-100" value="${po.invoice }">
									</span>
                                </div>
                                <div class="help-block col-xs-12 col-sm-reset inline"></div>
                            </div>
							
							<div class="clearfix form-actions">
								<div class="col-md-offset-3 col-md-9">
									<button class="btn btn-info" type="submit">
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