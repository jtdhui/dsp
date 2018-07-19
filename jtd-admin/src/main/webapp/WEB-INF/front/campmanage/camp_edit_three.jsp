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
<title>新建活动</title>
<%@ include file="/WEB-INF/commonjsp/common_css.jsp"%>
<%@ include file="/WEB-INF/commonjsp/common_js.jsp"%>

<script src="${baseurl}js/jquery/jquery.ajaxfileupload.js"></script>
<script src="${baseurl}dist/js/camp_edit_three.js?d=${activeUser.currentTime}"></script>
<link rel="stylesheet" href="${baseurl}dist/less/delivery-active3.css">

</head>
<body>
	<!--header 导航 -->
	<jsp:include page="/WEB-INF/front/header.jsp?param=campaign"></jsp:include>
	
	<input type="hidden" id="id" name="id" value="${po.id }">
	
	<!--index content-->
    <div class="wrapper">
        <!-- 当前位置 -->
        <div class="current-position row">
            <div class="col-sm-6">
                <ol class="breadcrumb">
                    <li class="breadcrumb-list">
                        <i class="icons-current"></i>当前位置
                    </li>
                    <li class="breadcrumb-list">
                        <a class="nav-breadcrumb" href="delivery.html">投放管理</a>
                        <i class="fa fa-angle-right"></i>
                    </li>
                    <li class="breadcrumb-list">
                        <a class="nav-breadcrumb" href="javascript:void(0)">广告活动列表</a>
                        <i class="fa fa-angle-right"></i>
                    </li>
                    <li class="breadcrumb-list">
                        <a class="nav-breadcrumb active">新建活动</a>
                    </li>
                </ol>
            </div>
            
            <!-- 选择广告主 -->
       		<jsp:include page="/WEB-INF/front/selectPartner.jsp?hideSelect=1"></jsp:include>
        </div>

        <!--新建活动content-->
        <div class="new_activity">
            <!--步骤 相应步加acitve-->
            <div class="step-wrap clearfix">
                <p class="steps step_one clearfix active">
	               <s class="line"></s>
	               <a href="${baseurl }front/campManage/camp_edit.action?id=${po.id }&step=1">
	               	<i class="step-num">1</i>
	               </a>
	               <span class="step-des">第一步：基本设置</span>
	           </p>
                <p class="steps step_two clearfix active">
	               <s class="line"></s>
	           		<a href="${baseurl }front/campManage/camp_edit.action?id=${po.id }&step=2">
	               		<i class="step-num">2</i>
	                </a>
	               <span class="step-des">第二步：投放策略</span>
	           </p>
                <p class="steps step_three clearfix active">
                    <i class="step-num">3</i>
                    <span class="step-des">第三步：上传素材</span>
                </p>
            </div>

            <!--todo: 基本设置-->
            <div class="handle-view clearfix">
                <div class="clearfix upload_material">
                    <div class="throw_manage_tit ">
                        <span class="" style="cursor:auto">上传创意</span>
                        <a href="javascipt:;" style="display: none;">下载创意尺寸制作标准</a>
                    </div>
                    <div class="upload_size clearfix">
                        <div class="upload_size_fl">
                            <p>可选创意尺寸(PX)：</p>
                            <p>约占流量：</p>
                        </div>
                        <div class="upload_size_rt">
                            <c:forEach var="item" items="${listCSF}">
	                            <c:set var="flag" value="0"></c:set>
	                            <c:forEach var="cPO" items="${creativeList}">
	                                <c:if test="${cPO.size == item.size }">
	                                    <c:set var="flag" value="1"></c:set>
	                                </c:if>
	                            </c:forEach>
	                            <p <c:if test="${flag==1 }">class="active"</c:if>>
	                            	<span>${item.size}</span>
	                            	<i><fmt:formatNumber value="${item.cp}" type="percent" minFractionDigits="2"/></i>
	                            </p>
                        	</c:forEach>
                        </div>
                    </div>
                    
                    <input type="hidden" id="flag" name="flag" value="文件上传"/>
                    
                    <div class="upload_size_tip clearfix">
                        <p>PC创意要求： 支持创意格式jpg，创意大小150K内</p>
                        <p>MOBILE创意要求：支持创意格式jpg，创意大小50K内</p>
                    </div>
                </div>
            </div>

            <!-- 批量上传 -->
            <div>
            	<shiro:hasPermission name="user:operable">
	                <div class="upload_size_bt">
	                    <span class="upload_size_file btn btn-small btn-main">
	                        <input type="file" id="file" name="file" multiple="multiple" onchange="ajaxFileUpload()">
							批量上传
						</span>
	                    <span class="upload_size_del btn btn-small btn-gray" id="delete_size_btn" data-toggle="modal" >删除</span>
	                </div>
				</shiro:hasPermission>
				
                <!-- 加载延迟 -->
                <div id="wait_loading" style="padding: 50px 0 0 0; display: none; text-align: center">
                    <div style="width: 103px; margin: 0 auto;">
                        <img src="${baseurl}img/loading.gif">
                    </div>
                    <br>
                    <div style="width: 103px; margin: 0 auto; text-align: center">
                        <span>请稍等</span>
                    </div>
                    <br>
                </div>

                <!-- 上传列表 -->
                <div class="upload_size_table clearfix">
                    <table class="table table-striped">
                        <thead>
                        <tr>
                            <th width="5%">
                                <input type="checkbox" class="table_checkbox" id="check_btn">
                            </th>
                            <th width="5%">广告ID</th>
                            <th width="8%">缩略图</th>
                            <th width="25%">创意名称</th>
                            <th width="10%">创意ID</th>
                            <th width="10%">创意尺寸</th>
                            <th width="10%">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        	<c:forEach var="item" items="${creativeList}">
	                        	<tr class="odd">
                                     <td>
                                         <input name="creative_check" type="checkbox" class="table_checkbox" value="${item.id}">
                                     </td>
                                     <td>${item.ad_id}</td>
                                     <td>
                                         <img src="${item.creative_url }" style="width:50px;height:50px">
                                     </td>
                                     <td>${item.creative_name}</td>
                                     <td>${item.id}</td>
                                     <td>${item.size}</td>
                                     <td>
                                         <shiro:hasPermission name="user:operable">
                                             <a onclick="deleteCreative(${item.id},${po.id})">×</a>
                                         </shiro:hasPermission>
                                     </td>
                                 </tr>
	                       </c:forEach>
                        </tbody>
                    </table>
                </div>

                <!-- 落地页链接 -->
                <div class="upload_size_other clearfix">
                    <p><i>落地页链接：</i>
                        <input class="input_text" type="text" id="landingPage" name="landingPage" value="${po.landingPage}">
                    </p>
                    <p><i>点击链接：</i>
                        <input class="input_text" type="text" id="clickUrl" name="clickUrl" value="${po.clickUrl}">
                    </p>
                    <p><i>第三方展现监测：</i>
                        <textarea id="pvUrls" class="pv-url" name="pvUrls">${po.pvUrls }</textarea>
                    </p>
                </div>
            </div>
            
            <div class="throw_manage_tit " style="display: none;"><span>活动行业类型设置</span></div>
                <div class="type_set" id="channel-sets" style="display: none;">
                    <p><span>渠道ID</span><span>渠道名称</span><span>行业类型</span></p>
                    <c:forEach var="ch" items="${adCatgList }">
                        <p>
                            <span>${ch.channel_id}</span>
                            <span>${ch.channel_name}</span>
                            <span>
                                <input type="hidden" id="channel_catgserial${ch.channel_id }" value="${ch.catgserial }"/>
                                <input type="hidden" name="channelCheck" value="${ch.channel_id }" checked="checked"/> 
                                    <select class="form-control" name="channel-select" id="channel-select${ch.channel_id }">
                                        <c:if test="${ch.catgserial=='TANX'}">
                                            <c:set var="channalName" value="${tanxChannelDIC}"></c:set>
                                        </c:if>
                                        <c:if test="${ch.catgserial=='BES'}">
                                            <c:set var="channalName" value="${besChannelDIC}"></c:set>
                                        </c:if>
                                        <c:if test="${ch.catgserial=='VAM'}">
                                            <c:set var="channalName" value="${vamChannelDIC}"></c:set>
                                        </c:if>
                                        <c:if test="${ch.catgserial=='XTRADER'}">
                                            <c:set var="channalName" value="${xtraderChannelDIC}"></c:set>
                                        </c:if>
                                        <c:forEach items="${channalName }" var="dicVO">
                                            <c:choose>
                                                <c:when test="${ch.catgid ==dicVO.id }">
                                                    <option value="${dicVO.id}" selected='selected'>${ dicVO.name}</option>
                                                </c:when>
                                                <c:otherwise>
                                                    <option value="${dicVO.id}">${ dicVO.name}</option>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                    </select>
                            </span>
                        </p>
                    </c:forEach>
                </div>
                <div class="throw_manage_tit " id="ad_info" style="display:none;"><span>广告信息</span></div>
                <div class="upload_size_other clearfix ad_meg_con" style="display:none;" >

                    <div class="clearfix">
                        <p class="fl"><i><span class="red">*</span>标题：</i>
                            <input type="text" id="phoneTitle" name="phoneTitle" value="${po.phoneTitle}" >
                        </p>
                        <p class="fr"><i><span class="red">*</span>广告语：</i>
                            <input type="text" id="phonePropaganda" name="phonePropaganda" value="${po.phonePropaganda}" >
                        </p>
                    </div>
                    <div class="clearfix">
                        <p class="fl"><i><span class="red">*</span>广告描述：</i>
                            <input type="text" id="phoneDescribe" name="phoneDescribe" value="${po.phoneDescribe}" >
                        </p>
                        <p class="fl"><i><span class="red">*</span>原价：</i>
                            <input type="text" id="originalPrice" name="originalPrice" value="${po.originalPrice}" >
                        </p>
                    </div>
                    <div class="clearfix">
                        <p class="fl"><i><span class="red">*</span>折后价：</i>
                            <input type="text" id="discountPrice" name="discountPrice" value="${po.discountPrice}" >
                        </p>
                        <p class="fl"><i><span class="red">*</span>销量：</i>
                            <input type="text" id="salesVolume" name="salesVolume" value="${po.salesVolume}" >
                        </p>
                    </div>
                    <div class="clearfix">
                        <p class="fl"><i><span class="red">*</span>选填：</i>
                            <input type="text" id="optional" name="optional" value="${po.optional}" >
                        </p>
                        <p class="fl"><i><span class="red">*</span>启动Deeplink打开：</i>
                            <label><input type="radio" name="deepLink" value="1" <c:if test="${ po.deepLink != 0 }"> checked="checked"</c:if>>是</label>
                            <label><input type="radio" name="deepLink" value="0" <c:if test="${ po.deepLink == 0 }"> checked="checked"</c:if>>否</label>
                        </p>
                    </div>
                    <div class="clearfix">
                        <p class="fl"><i><span class="red">*</span>设置监控：</i>
                            <input type="text" id="monitor" name="monitor" value="${po.monitor}" >
                        </p>
                        <p class="fl"><i><span class="red">*</span>Deeplink目标地址:</i>
                            <input type="text" id="phoneDeepLinkUrl" name="phoneDeepLinkUrl" value="${po.phoneDeepLinkUrl}" >
                        </p>
                    </div>
                </div>
            </div>

            <!--基本设置/-->
            <div class="clearfix new_activity_bt">
                <a href="${baseurl }front/campManage/camp_edit.action?id=${po.id }&step=2" class="btn btn-small btn-purple"><span>上一步</span></a>
                <shiro:hasPermission name="user:operable">
                	<span class="btn btn-small btn-purple btn-save" id="save_camp_btn_3">确定</span>
                </shiro:hasPermission>
                <a href="${baseurl }front/campManage/camp_list.action" class="btn btn-small btn-gray">返回</a>
            </div>
        </div>
        <!--新建活动content/-->
    </div>
    <!--index content/-->
	
</body>
</html>