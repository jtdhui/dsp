<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/commonjsp/tag.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta HTTP-EQUIV="pragma" CONTENT="no-cache">
<meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<meta HTTP-EQUIV="expires" CONTENT="0">
<title>创意列表</title>
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
							href="${pageContext.request.contextPath}">创意管理</a></li>
						<li class="active">创意列表</li>
					</ul>
				</div>

				<div class="page-content">

					<div class="row">
						<div class="col-xs-12">

							<div class="row">
								<div class="col-xs-12">

									<form method="post"
										action="${baseurl }admin/channel/addChannel.action"
										id="formId" class="form-horizontal"
										enctype="multipart/form-data">
										<div class="form-group">

											<label class="col-sm-1 control-label">渠道名称：</label> <input
												type="text" name="channelName" class="col-sm-1"
												value="${channel.channelName}"  required>
										</div>
										<div class="form-group">

											<label class="col-sm-1 control-label">渠道类别体系：</label> <input
												type="text" name="catgserial" class="col-sm-1"
												value="${channel.catgserial}" required>
										</div>
										<div class="form-group">

											<label class="col-sm-1 control-label">RTB PMP：</label> <input
												type="text" name="transType" class="col-sm-1"
												value="${channel.transType}" required>
										</div>
										<div class="form-group">

											<label class="col-sm-1 control-label">广告类型：</label> <input
												type="text" name="adType" class="col-sm-1"
												value="${channel.adType}" required>

										</div>
										<div class="form-group">
											<label class="col-sm-1 control-label">渠道展示地址：</label> <input
												type="text" name="websiteUrl" class="col-sm-1"
												value="${channel.websiteUrl}" required>
										</div>
										<div class="form-group">

											<label class="col-sm-1 control-label">渠道logo图片地址：</label> <input
												type="file" name="file" class="col-sm-1" required>
										</div>

										<div class="form-group">
											<img src="${pageContext.request.contextPath}/${channel.logo}">
										</div>

										<div class="form-group">
											<button type="submit" class="btn icon-plus btn-primary">提交</button>
										</div>
										<input type="hidden" name="id" value="${channel.id}">
									</form>

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