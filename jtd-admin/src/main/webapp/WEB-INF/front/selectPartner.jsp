<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
	String fromPage = request.getParameter("fromPage");
	if(fromPage == null){
		fromPage = "" ;
	}
	String hideSelect = request.getParameter("hideSelect");
%>

<!-- 切换广告主 -->
<input type="hidden" id="fromPage" value="<%=fromPage%>">
<input type="hidden" id="msg_v" value="${msg }">
<input type="hidden" id="str_v" value="${str }">
 <div class="check_sponsor col-sm-6">
     <div class="sponsor_tit user-company">
         <i class="icons-user-company"></i>
         <div class="partner_tit company-name ">${activeUser.favPartner.partnerName}
             <%
               	if(hideSelect == null){
             %>
             <div class="sponsor_select">
<!--                  <div class="sponsor_select_tit">切换广告主</div> -->
                 <div class="sponsor_con">
                     <div class="search-bar">
                         <input type="text" id="pName" placeholder="广告主名称" value=""/>
                         <s class="search-btn" id="name_search_btn">搜索</s>
                     </div>
                     <div class="content">
                			<ul id="partner_list_id"  class="ztree" >
                           </ul>
                         
                     </div>
                 </div>
             </div>
             <%
               	}
             %>
         </div>
     </div>
 </div>
 <!-- 切换广告主 /-->