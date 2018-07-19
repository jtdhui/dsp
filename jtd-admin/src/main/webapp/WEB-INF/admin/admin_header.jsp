<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/commonjsp/tag.jsp"%>
<%
    response.setHeader("Cache-Control","no-store");
    response.setHeader("Pragrma","no-cache");
    response.setDateHeader("Expires",0);
%>
<div class="navbar navbar-default" id="navbar">
	<script type="text/javascript">
		try {
			ace.settings.check('navbar', 'fixed')
		} catch (e) {
		}
		//退出系统方法
		function logout() {
			if (confirm("您确定要退出本系统吗?")) {
				//如果是true ，那么就把页面转向thcjp.cnblogs.com
				location.href = '${pageContext.request.contextPath}/logout.action';
			}
		}
	</script>

	<div class="navbar-container" id="navbar-container">
		<div class="navbar-header pull-left">
			<a href="#" class="navbar-brand"> <small> 
				<i class="icon-leaf"></i>JTD_DSP广告管理系统
			</small>
			</a>
			<!-- /.brand -->
		</div>
		<!-- /.navbar-header -->

		<div class="navbar-header pull-right" role="navigation">
			<ul class="nav ace-nav">
<!-- 				<li class="grey"> -->
<!-- 					<a data-toggle="dropdown" -->
<!-- 					class="dropdown-toggle" href="#"> <i class="icon-tasks"></i> <span -->
<!-- 						class="badge badge-grey">4</span> -->
<!-- 				</a> -->

<!-- 					<ul -->
<!-- 						class="pull-right dropdown-navbar dropdown-menu dropdown-caret dropdown-close"> -->
<!-- 						<li class="dropdown-header"><i class="icon-ok"></i> 还有4个任务完成</li> -->

<!-- 						<li><a href="#"> -->
<!-- 								<div class="clearfix"> -->
<!-- 									<span class="pull-left">软件更新</span> <span class="pull-right">65%</span> -->
<!-- 								</div> -->
<!-- 								<div class="progress progress-mini "> -->
<!-- 									<div style="width: 65%" class="progress-bar "></div> -->
<!-- 								</div> -->
<!-- 						</a></li> -->

<!-- 						<li><a href="#"> -->
<!-- 								<div class="clearfix"> -->
<!-- 									<span class="pull-left">硬件更新</span> <span class="pull-right">35%</span> -->
<!-- 								</div> -->

<!-- 								<div class="progress progress-mini "> -->
<!-- 									<div style="width: 35%" -->
<!-- 										class="progress-bar progress-bar-danger"></div> -->
<!-- 								</div> -->
<!-- 						</a></li> -->

<!-- 						<li><a href="#"> -->
<!-- 								<div class="clearfix"> -->
<!-- 									<span class="pull-left">单元测试</span> <span class="pull-right">15%</span> -->
<!-- 								</div> -->

<!-- 								<div class="progress progress-mini "> -->
<!-- 									<div style="width: 15%" -->
<!-- 										class="progress-bar progress-bar-warning"></div> -->
<!-- 								</div> -->
<!-- 						</a></li> -->

<!-- 						<li><a href="#"> -->
<!-- 								<div class="clearfix"> -->
<!-- 									<span class="pull-left">错误修复</span> <span class="pull-right">90%</span> -->
<!-- 								</div> -->

<!-- 								<div class="progress progress-mini progress-striped active"> -->
<!-- 									<div style="width: 90%" -->
<!-- 										class="progress-bar progress-bar-success"></div> -->
<!-- 								</div> -->
<!-- 						</a></li> -->

<!-- 						<li><a href="#"> 查看任务详情 <i class="icon-arrow-right"></i> -->
<!-- 						</a></li> -->
<!-- 					</ul></li> -->

<!-- 				<li class="purple"><a data-toggle="dropdown" -->
<!-- 					class="dropdown-toggle" href="#"> <i -->
<!-- 						class="icon-bell-alt icon-animated-bell"></i> <span -->
<!-- 						class="badge badge-important">8</span> -->
<!-- 				</a> -->

<!-- 					<ul -->
<!-- 						class="pull-right dropdown-navbar navbar-pink dropdown-menu dropdown-caret dropdown-close"> -->
<!-- 						<li class="dropdown-header"><i class="icon-warning-sign"></i> -->
<!-- 							8条通知</li> -->

<!-- 						<li><a href="#"> -->
<!-- 								<div class="clearfix"> -->
<!-- 									<span class="pull-left"> <i -->
<!-- 										class="btn btn-xs no-hover btn-pink icon-comment"></i> 新闻评论 -->
<!-- 									</span> <span class="pull-right badge badge-info">+12</span> -->
<!-- 								</div> -->
<!-- 						</a></li> -->

<!-- 						<li><a href="#"> <i -->
<!-- 								class="btn btn-xs btn-primary icon-user"></i> 切换为编辑登录.. -->
<!-- 						</a></li> -->

<!-- 						<li><a href="#"> -->
<!-- 								<div class="clearfix"> -->
<!-- 									<span class="pull-left"> <i -->
<!-- 										class="btn btn-xs no-hover btn-success icon-shopping-cart"></i> -->
<!-- 										新订单 -->
<!-- 									</span> <span class="pull-right badge badge-success">+8</span> -->
<!-- 								</div> -->
<!-- 						</a></li> -->

<!-- 						<li><a href="#"> -->
<!-- 								<div class="clearfix"> -->
<!-- 									<span class="pull-left"> <i -->
<!-- 										class="btn btn-xs no-hover btn-info icon-twitter"></i> 粉丝 -->
<!-- 									</span> <span class="pull-right badge badge-info">+11</span> -->
<!-- 								</div> -->
<!-- 						</a></li> -->

<!-- 						<li><a href="#"> 查看所有通知 <i class="icon-arrow-right"></i> -->
<!-- 						</a></li> -->
<!-- 					</ul></li> -->

<!-- 				<li class="green"><a data-toggle="dropdown" -->
<!-- 					class="dropdown-toggle" href="#"> <i -->
<!-- 						class="icon-envelope icon-animated-vertical"></i> <span -->
<!-- 						class="badge badge-success">5</span> -->
<!-- 				</a> -->

<!-- 					<ul -->
<!-- 						class="pull-right dropdown-navbar dropdown-menu dropdown-caret dropdown-close"> -->
<!-- 						<li class="dropdown-header"><i class="icon-envelope-alt"></i> -->
<!-- 							5条消息</li> -->

<!-- 						<li><a href="#"> <img -->
<%-- 								src="${pageContext.request.contextPath}/assets/avatars/avatar.png" class="msg-photo" --%>
<!-- 								alt="Alex's Avatar" /> <span class="msg-body"> <span -->
<!-- 									class="msg-title"> <span class="blue">Alex:</span> 不知道写啥 -->
<!-- 										... -->
<!-- 								</span> <span class="msg-time"> <i class="icon-time"></i> <span>1分钟以前</span> -->
<!-- 								</span> -->
<!-- 							</span> -->
<!-- 						</a></li> -->

<!-- 						<li><a href="#"> <img -->
<%-- 								src="${pageContext.request.contextPath}/assets/avatars/avatar3.png" class="msg-photo" --%>
<!-- 								alt="Susan's Avatar" /> <span class="msg-body"> <span -->
<!-- 									class="msg-title"> <span class="blue">Susan:</span> -->
<!-- 										不知道翻译... -->
<!-- 								</span> <span class="msg-time"> <i class="icon-time"></i> <span>20分钟以前</span> -->
<!-- 								</span> -->
<!-- 							</span> -->
<!-- 						</a></li> -->

<!-- 						<li><a href="#"> <img -->
<%-- 								src="${pageContext.request.contextPath}/assets/avatars/avatar4.png" class="msg-photo" --%>
<!-- 								alt="Bob's Avatar" /> <span class="msg-body"> <span -->
<!-- 									class="msg-title"> <span class="blue">Bob:</span> -->
<!-- 										到底是不是英文 ... -->
<!-- 								</span> <span class="msg-time"> <i class="icon-time"></i> <span>下午3:15</span> -->
<!-- 								</span> -->
<!-- 							</span> -->
<!-- 						</a></li> -->

<!-- 						<li><a href="inbox.html"> 查看所有消息 <i -->
<!-- 								class="icon-arrow-right"></i> -->
<!-- 						</a></li> -->
<!-- 					</ul> -->
<!-- 				</li> -->

				<li>
					<a href="#" style="cursor:default;background: #c6487e;"> 
<!-- 					<img class="nav-user-photo" -->
<%-- 						src="${pageContext.request.contextPath}/assets/avatars/user.jpg" alt="Jason's Photo" /> --%>
					<i class="icon-user"></i>
					<span class="user-info" style="line-height:32px;max-width:300px;">
						欢迎光临：${activeUser.userName}
					</span>
<!-- 						<i class="icon-caret-down"></i> -->
					</a>

<!-- 					<ul class="user-menu pull-right dropdown-menu dropdown-yellow dropdown-caret dropdown-close"> -->
<!-- 						<ul class="user-menu"> -->
<!-- 						<li><a href="#"> <i class="icon-cog"></i> 设置 -->
<!-- 						</a></li> -->
<!-- 						<li><a href="#"> <i class="icon-user"></i> 个人资料 -->
<!-- 						</a></li> -->
<!-- 						<li class="divider"></li> -->
<%-- 						<shiro:hasPermission name="admin"> --%>
<%-- 						<li><a href="${pageContext.request.contextPath}/front/home.action"><i --%>
<!-- 								class="icon-cog"></i> 切换前台</a></li> -->
<%-- 						</shiro:hasPermission> --%>
<!-- 						<li><a id="loginOut" href=javascript:logout()> <i -->
<!-- 								class="icon-off"></i> 退出 -->
<!-- 						</a></li> -->
<!-- 					</ul> -->
				</li>
				<li>
					<a href="#" style="cursor:default;background: #c6487e;">
						所属公司：${activeUser.partner.partnerName}
					</a>
				</li>
				<shiro:hasPermission name="admin">
					<li>
						<a href="${pageContext.request.contextPath}/front/home.action" style="background: #c6487e;"><i class="icon-refresh"></i> 切换前台</a>
					</li>
				</shiro:hasPermission>
				<li>
					<a id="loginOut" href="javascript:logout()" style="background: #c6487e;"> <i class="icon-off"></i> 退出 </a>
				</li>
			</ul>
			<!-- /.ace-nav -->
		</div>
		<!-- /.navbar-header -->
	</div>
	<!-- /.container -->
</div>

<!-- <div class="ace-settings-container" id="ace-settings-container"> -->
<!-- 	<div class="btn btn-app btn-xs btn-warning ace-settings-btn" -->
<!-- 		id="ace-settings-btn"> -->
<!-- 		<i class="icon-cog bigger-150"></i> -->
<!-- 	</div> -->

<!-- 	<div class="ace-settings-box" id="ace-settings-box"> -->
<!-- 		<div> -->
<!-- 			<div class="pull-left"> -->
<!-- 				<select id="skin-colorpicker" class="hide"> -->
<!-- 					<option data-skin="default" value="#438EB9">#438EB9</option> -->
<!-- 					<option data-skin="skin-1" value="#222A2D">#222A2D</option> -->
<!-- 					<option data-skin="skin-2" value="#C6487E">#C6487E</option> -->
<!-- 					<option data-skin="skin-3" value="#D0D0D0">#D0D0D0</option> -->
<!-- 				</select> -->
<!-- 			</div> -->
<!-- 			<span>&nbsp; 选择皮肤</span> -->
<!-- 		</div> -->

<!-- 		<div> -->
<!-- 			<input type="checkbox" class="ace ace-checkbox-2" -->
<!-- 				id="ace-settings-navbar" /> <label class="lbl" -->
<!-- 				for="ace-settings-navbar"> 固定导航条</label> -->
<!-- 		</div> -->

<!-- 		<div> -->
<!-- 			<input type="checkbox" class="ace ace-checkbox-2" -->
<!-- 				id="ace-settings-sidebar" /> <label class="lbl" -->
<!-- 				for="ace-settings-sidebar"> 固定滑动条</label> -->
<!-- 		</div> -->

<!-- 		<div> -->
<!-- 			<input type="checkbox" class="ace ace-checkbox-2" -->
<!-- 				id="ace-settings-breadcrumbs" /> <label class="lbl" -->
<!-- 				for="ace-settings-breadcrumbs">固定面包屑</label> -->
<!-- 		</div> -->

<!-- 		<div> -->
<!-- 			<input type="checkbox" class="ace ace-checkbox-2" -->
<!-- 				id="ace-settings-rtl" /> <label class="lbl" for="ace-settings-rtl">切换到左边</label> -->
<!-- 		</div> -->

<!-- 		<div> -->
<!-- 			<input type="checkbox" class="ace ace-checkbox-2" -->
<!-- 				id="ace-settings-add-container" /> <label class="lbl" -->
<!-- 				for="ace-settings-add-container"> 切换窄屏 <b></b> -->
<!-- 			</label> -->
<!-- 		</div> -->
<!-- 	</div> -->
<!-- </div> -->


