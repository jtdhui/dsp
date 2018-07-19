<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/commonjsp/tag.jsp"%>

	<div class="sidebar" id="sidebar">
<!-- 		<div class="sidebar-shortcuts" id="sidebar-shortcuts"> -->
<!-- 			<div class="sidebar-shortcuts-large" id="sidebar-shortcuts-large"> -->
<!-- 				<button class="btn btn-success"> -->
<!-- 					<i class="icon-signal"></i> -->
<!-- 				</button> -->

<!-- 				<button class="btn btn-info"> -->
<!-- 					<i class="icon-pencil"></i> -->
<!-- 				</button> -->

<!-- 				<button class="btn btn-warning"> -->
<!-- 					<i class="icon-group"></i> -->
<!-- 				</button> -->

<!-- 				<button class="btn btn-danger"> -->
<!-- 					<i class="icon-cogs"></i> -->
<!-- 				</button> -->
<!-- 			</div> -->

<!-- 			<div class="sidebar-shortcuts-mini" id="sidebar-shortcuts-mini"> -->
<!-- 				<span class="btn btn-success"></span> <span class="btn btn-info"></span> -->
<!-- 				<span class="btn btn-warning"></span> <span class="btn btn-danger"></span> -->
<!-- 			</div> -->
<!-- 		</div> -->
		<!-- #sidebar-shortcuts -->

		<ul class="nav nav-list">

			<c:if test="${activeUser.menus!=null }">
				<c:forEach items="${activeUser.menus }" var="p">
					<c:if test="${p.level == 1  }">
						<li class="active open">
							<a href="#" class="dropdown-toggle">
								<i class="icon-list"></i> <span class="menu-text">
									${p.name }</span> <b class="arrow icon-angle-down"></b>
							</a>
							<ul class="submenu">
								<c:forEach items="${activeUser.menus }" var="menu">
									<c:if test="${menu.level == 2 and menu.parentId == p.id}">
										<li pid="${menu.parentId }" id="${menu.id }" <c:if test="${menu.id == menuId}">class="active"</c:if>>
											<a href="${baseurl }${menu.url }?menuId=${menu.id }" >
												<i class="icon-double-angle-right"></i>${menu.name }
											</a>
										</li>
									</c:if>
								</c:forEach>
							</ul>
						</li>
					</c:if>
				</c:forEach>
			</c:if>
			<c:if test="${activeUser.menus==null }">
				无菜单
			</c:if>
		</ul>
		<!-- /.nav-list -->

		<div class="sidebar-collapse" id="sidebar-collapse">
			<i class="icon-double-angle-left" data-icon1="icon-double-angle-left"
				data-icon2="icon-double-angle-right"></i>
		</div>

	</div>
