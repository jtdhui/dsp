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
    <script type="text/javascript" src="${baseurl}dist/js/camp_edit_two.js?d=${activeUser.currentTime}"></script>
    <script type="text/javascript" src="${baseurl}js/campaignDicData.js?d=${activeUser.currentTime}"></script>
    
    <link rel="stylesheet" href="${baseurl}dist/less/delivery-active2-area.css">
    <link rel="stylesheet" href="${baseurl}dist/less/module/bustophedon.css">
    <link rel="stylesheet" href="${baseurl}dist/less/delivery-active2.css">
    
    <link rel="stylesheet" href="${baseurl}css/front/layer.css?v=${activeUser.currentTime}">
</head>
<body>
<!--header 导航 -->
<jsp:include page="/WEB-INF/front/header.jsp?param=campaign"></jsp:include>

<style>
	.check_channel_con label {
		width:114px;height:42px;
	}
	#app_type_window *{font-size:12px;}
	#area_select *{font-size:12px;}
	#app_name_window *{font-size:12px;}
	#domain_name_window *{font-size:12px;}
	#media_type_window *{font-size:12px;}
	/*地域里面*/
	.check_dress {
	  clear: both;
	  margin: 0 auto;
	}
	.layer_blue_tit *{font-size:16px;}
	.btn:hover {
	  background-color: #ea5b1a;
	}
	/**/
	#crowd-select-wrap *{font-size:12px;}
	.handle-view .crowd_con .crowd .crowd-select-wrap .Tab-Container .crowd-content {
    	padding: 10px 40px;
	}
	hr{margin-top:2px;margin-bottom:2px;}
	.handle-view .crowd_con .crowd .crowd-select-wrap .Tab-Container ul {
	    padding-top: 0;
	    overflow: hidden;
	    margin: 0px 0 0px 100px;
	}
	.handle-view .crowd_con .crowd .crowd-subheader{
		margin-top:0px;
	}
	
	/*设备自定义隐藏框*/
	.devicetype-control {
	    margin-left: 310px;
	    overflow: hidden;
	    float: left;
	    width: 650px;
	    line-height: 40px;
	}
	.devicetype-checkbox {
	    float: left;
	    position: relative;
	}
	.devicetype-checkbox input[type=checkbox] {
	    display: none;
	}
	.devicetype-checkbox label {
	    cursor: pointer;
	    position: absolute;
	    width: 12px;
	    height: 12px;
	    background-color: inherit;
	    border: 1px solid;
	    left: 10px;
	    top: 14px;
	    line-height: inherit;
	}
	.devicetype-checkbox .devicetype-checkboxinput label:after {
	    opacity: 0;
	    content: ' ';
	    position: absolute;
	    width: 8px;
	    height: 4px;
	    background: transparent;
	    top: 2px;
	    left: 1px;
	    border: 1px solid;
	    border-top: none;
	    border-right: none;
	    -webkit-transform: rotate(-45deg);
	    -moz-transform: rotate(-45deg);
	    -o-transform: rotate(-45deg);
	    -ms-transform: rotate(-45deg);
	    transform: rotate(-45deg);
	}
	.devicetype-checkbox .devicetype-checkboxinput input[type=checkbox]:checked+label:after {
	    opacity: 1;
	}
	.devicetype-checkbox .devicetype-checkboxlabel {
	    margin-left: 10px;
	    margin-right: 20px;
	    display: block;
	    padding-left: 20px;
	}
</style>

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
           <c:choose>
                <c:when test="${po.editStepStatus>=3}">
                    <p class="steps step_three clearfix">
                    	<a href="${baseurl }front/campManage/camp_edit.action?id=${po.id }&step=3">
                    		<i class="step-num">3</i>
                    	</a>
                    	<span class="step-des">第三步：上传素材</span>
                    </p>
                </c:when>
                <c:otherwise>
                    <p class="steps step_three clearfix">
                    	<i class="step-num">3</i>
                    	<span class="step-des">第三步：上传素材</span>
                	</p>
                </c:otherwise>
           </c:choose>
       </div>
       <!-- 步骤/-->
		
		<input type="hidden" id="id" name="id" value="${po.id }">
        <input type="hidden" id="editStepStatus" value="${po.editStepStatus }">
        <input type="hidden" id="campaignType" name="campaignType" value="${po.campaignType }">
        <input type="hidden" id="adType" name="adType" value="${po.adType }">

       <!--todo: 基本设置-->
       <div class="handle-view clearfix">
           <!-- 交易渠道 -->
           <div class="strategy_tit" style="width: 320px">选择渠道（保存后不可更改）</div>
           
           <input type="hidden" id="channel" value="${ channel_sel }">
            <input type="hidden" id="channel_sel" value="${channel_sel_value}">
           
           <div class="strategy_con clearfix">
               <span>交易渠道：</span>
               <div class="radio-wrap">
                   <div id="channel_0" class="radio-list">
                       <div class="radio-style <c:if test="${ empty channel_sel_value }">active</c:if>">
                           <input type="radio" name="channel_type" value="0" <c:if test="${ empty channel_sel_value }"> checked="checked"</c:if>>
                       </div>
                       <label for="channel_0">不限</label>
                   </div>
                   <div id="channel_1" class="radio-list">
                        <div class="radio-style <c:if test="${ not empty channel_sel_value }">active"</c:if>">
                            <input id="custom_channel_btn" type="radio" name="channel_type" <c:if test="${ not empty channel_sel_value }">checked="checked"</c:if>>
                        </div>
                        <label for="custom_channel_btn">自定义</label>
                    </div>
               </div>
           </div>

           <!-- 地域定向 -->
           <div class="strategy_tit">地域定向</div>
           <div class="strategy_con clearfix">
               <span>地区选择：</span>
               <div class="radio-wrap">
                   <div class="radio-list">
                       <div class="radio-style <c:if test="${ empty areas_sel_value }">active</c:if>">
                           <input id="r21" type="radio" name="area_type" value="0" <c:if test="${ empty areas_sel_value }"> checked="checked"</c:if>>
                       </div>
                       <label for="r21">不限</label>
                   </div>
                   <div class="radio-list">
                       <div class="radio-style <c:if test="${ not empty areas_sel_value }">active</c:if>">
                           <input id="r22" type="radio" name="area_type" value="1" <c:if test="${ not empty areas_sel_value }"> checked="checked"</c:if>>
                       </div>
                       <label for="r22">自定义</label>
                   </div>
               </div>
           </div>

           <!-- todo:人群定向 -->
           <div class="strategy_tit">人群定向</div>
           
           <div class="crowd_con clearfix">
               <div class="crowd peopleContainer">
                   <div class="crowd-header radio-wrap" id="crowd_ul_key">
                       <span id="peopleTab" class="tab-list on"><i class="radio-style active"></i>人群属性定向</span>
                       <span id="interestTab" class="tab-list"><i class="radio-style "></i>兴趣定向</span>
                       <span id="personalityTab" class="tab-list"><i class="radio-style "></i>个性化人群定向</span>
                       <span id="guestTab" class="tab-list"><i class="radio-style "></i>访客找回</span>
                   </div>
                   <div class="crowd-select-wrap clearfix" id="crowd-select-wrap">
                       <div class="crowd-select-left crowd-long" style="height:805px !important;">
                           
                           <!--人群属性定向s-->
                           <input type="hidden" id="populations" value="${populations}">
                           
                           <div class="Tab-Container" id="peopleContainer" style="display: block;">
                               <div class="crowd-header">
                                   请选择人口属性
                                   <a href="javascript:void(0)" id="people_all" selected="selected">全选</a>
                               </div>
                               <div class="clearfix"></div>
                               <div class="crowd-content" id="people">
                               </div>
                               <div class="clearfix"></div>
                               <div class="crowd-subdes">
                               		<!--人群属性定向-高级设置 -->
                                   <input type="hidden" id="matchType_sel" value="${matchType}">
                                   高级设置：
                                   <a class="crowd_subdes_more" id="crowd_subdes_more" href="javascript:void(0)"
                                      selected="selected">显示</a>
                                   <div class="crowd-subdes-more" style="display: none;">
                                       人口属性选择关系
                                       <div class="radio-wrap">
                                            <div class="radio-list">
                                                <div class="radio-style active">
                                                    <input id="matchType_or" type="radio" checked="checked" value="or" name="matchType">
                                                </div>
                                                <label for="matchType_or">或</label>
                                            </div>
                                            <div class="radio-list">
                                                <div class="radio-style">
                                                    <input id="matchType_and" type="radio" value="and" name="matchType">
                                                </div>
                                                <label for="matchType_and">与</label>
                                            </div>
                                        </div>
                                   </div>
                               </div>
                           </div>

                           <!--兴趣定向s-->
                           <div class="Tab-Container" id="interest" style="display: none;">
                           		
                           		<input type="hidden" id="instresting_sel" value="${instresting_sel}">
                           
                               <input type="hidden" id="instresting_sel" value="">
                               <div class="crowd-header">
                                   请选择兴趣定向
                                   <a href="javascript:void(0)" id="interest_all" selected="selected">全选</a>
                               </div>
                               <ul class="list" id="interestList">
                                   
                               </ul>
                           </div>

                           <!--个性化人群定向 s-->
                           <div class="Tab-Container" id="personality" style="display: none;">
                           
                           		<input type="hidden" id="cookiePacket_sel" value="${cookiePacket_sel}">
                           		
                               <div class="crowd-header">
                                   请选择个性化人群包
                                   <a href="javascript:void(0)" id="personality_all"
                                      selected="selected">全选</a>
                               </div>
                               <div class="clearfix"></div>
                               <div class="control">
                                   <div class="radio-wrap" style="text-align: left;">
                                       <span>
	                                       <div class="radio-style active">
	                                           <input type="radio" id="btn_private" name="cookiebtn">
	                                       </div>
	                                       <label for="btn_private">私有人群包</label>
                                       </span>
                                       <span>
	                                       <div class="radio-style">
	                                           <input type="radio" id="btn_public" name="cookiebtn">
	                                       </div>
	                                       <label for="btn_public">公共人群包</label>
                                       </span>
                                   </div>
                                   
                               </div>
                               <ul class="single" id="cookiePrivate" style="display:block">
									<c:if test="${ not empty cookiePacketPersonal}">
	                                    <c:forEach var="cp" items="${cookiePacketPersonal}">
	                                        <li>
	                                            <div class="checkboxcontainer">
	                                                    <span class="checkboxinput">
	                                                    <input id="cpprivate_${cp.cookieGid}" type="checkbox" name="cookiePacket" value="${cp.cookieGid}">
	                                                    <label for="cpprivate_${cp.cookieGid}"></label>
	                                                    </span>
	                                                <span class="checkboxlabel">${cp.packetName}</span>
	                                            </div>
	                                        </li>
	                                    </c:forEach>
                                	</c:if>
                               </ul>
                               <ul class="single" id="cookiePublic" style="display:none">
                                   <c:if test="${ not empty cookiePacketPublic}">
	                                    <c:forEach var="cp" items="${cookiePacketPublic}">
	                                        <li>
	                                            <div class="checkboxcontainer">
	                                                    <span class="checkboxinput">
	                                                    <input id="cppublic_${cp.cookieGid}" type="checkbox" name="cookiePacket" value="${cp.cookieGid}">
	                                                    <label for="cppublic_${cp.cookieGid}"></label>
	                                                    </span>
	                                                <span class="checkboxlabel">${cp.packetName}</span>
	                                            </div>
	                                        </li>
	                                    </c:forEach>
                                	</c:if>
                               </ul>
                           </div>

                           <!--访客找回 s-->
                           <div class="Tab-Container" id="guest" style="display: none;">
                               
                               <input type="hidden" id="retargetPacket_sel" value="${retargetPacket_sel}">
                               
                               <div class="crowd-header">
                                   请选择访客找回定向
                                   <a href="javascript:void(0)" id="retarget_all" selected="selected">全选</a>
                               </div>
                               <div class="control" style="display: none">
                                   <input type="radio" checked="checked" id="typeYes1" name="type1">
                                   <label for="typeYes1">全站回头客</label>
                                   <input type="radio" id="typeNo1" name="type1" disabled="true">
                                   <label for="typeNo1">目标回头客</label>
                               </div>
                               <ul id="retarget" class="single" style="margin-top:45px">
                                   <c:if test="${ not empty retargetPacketList}">
                                        <c:forEach var="rp" items="${retargetPacketList}">
                                            <li style="width:400px">
                                                <div class="checkboxcontainer">
                                                        <span class="checkboxinput">
                                                            <input id="retarget_${rp.cookieGid}" type="checkbox" name="retargetpacket" value="${rp.cookieGid}">
                                                            <label for="retarget_${rp.cookieGid}"></label>
                                                        </span>
                                                    <span class="checkboxlabel">${rp.packetName}</span>
                                                </div>
                                            </li>
                                        </c:forEach>
                                    </c:if>
                               </ul>
                               <div class="clearfix"></div>
                           </div>

                           <!--IP访客定向 s-->
                           <div class="Tab-Container" id="ip" style="display: none;">
                               <div class="crowd-header">请选择IP访客定向</div>
                               <div class="control">
                                   <!--</span>-->
                                   <ul class="single-long">
                                       <li>
                                           <div class="checkboxcontainer">
                                               <span class="checkboxinput">
                                                   <input id="ip0" type="checkbox">
                                                   <label for="ip0"></label>
                                               </span>
                                               <span class="checkboxlabel"></span>
                                           </div>
                                           <input type="text" name="cookie-ip" placeholder="请输入IP段......">
                                           <a href="javascript:void(0)" class="big ipAdd">+</a>
                                           <a href="javascript:void(0)" class="big ipDel">-</a>
                                       </li>
                                   </ul>
                               </div>
                               <div style="margin-left: 20px;line-height:25px;">
                                   注：可输入IP地址，如192.168.0.1；也可输入IP段，如192.168.0.1-192.168.0.5，IP段间用“-”波折号间隔
                               </div>
                           </div>
                           <!--IP访客定向 e-->
                       </div>
                       <div class="crowd-select-middle">
                           <img src="${baseurl}img/delivery/select-img.png" width="25" height="25">
                       </div>
                       <div class="crowd-select-right crowd-long Tab-Container" style="height:805px !important;">
                           <!--人群属性定向s-->
                           <div id="peopleContent" class="Tab-Content">
                               <div class="crowd-header crowd-subheader">
                                   已选人群标签
                                   <a href="javascript:void(0)" id="clearPeople">全部清除</a>
                               </div>
<!--                                <div class="crowd-subheader">人口属性：</div> -->
                               <div id="peopleResult" class="control">
                                   <div class="crowd-title">性别：</div>
                                   <ul dimname="g" class="people">
                                       <li>男</li>
                                       <li>女</li>
                                   </ul>
                                   <div class="clearfix"></div>
                                   <div class="crowd-title">年龄：</div>
                                   <ul dimname="a" class="people"></ul>
                                   <div class="clearfix"></div>
                                   <div class="crowd-title">婚姻状态：</div>
                                   <ul dimname="m" class="people"></ul>
                                   <div class="clearfix"></div>
                                   <div class="crowd-title">育儿阶段：</div>
                                   <ul dimname="ca" class="people"></ul>
                                   <div class="clearfix"></div>
                                   <div class="crowd-title">职业类型：</div>
                                   <ul dimname="job" class="people"></ul>
                                   <div class="clearfix"></div>
                                   <div class="crowd-title">职业状态：</div>
                                   <ul dimname="jt" class="people"></ul>
                                   <div class="clearfix"></div>
                                   <div class="crowd-title">求学阶段：</div>
                                   <ul dimname="e" class="people"></ul>
                                   <div class="clearfix"></div>
                                   <div class="crowd-title">备考阶段：</div>
                                   <ul dimname="fr" class="people"></ul>
                                   <div class="clearfix"></div>
                                   <div class="crowd-title">居住阶段：</div>
                                   <ul dimname="h" class="people"></ul>
                                   <div class="clearfix"></div>
                               </div>
                           </div>

                           <!--兴趣定向结果s-->
                           <div id="interestContent" class="Tab-Content">
                               <div class="crowd-header crowd-subheader">
                                   	已选兴趣定向 
                                   	<a href="javascript:void(0)" id="clearInterest">全部清除</a>
                               </div>
<!--                                <div class="crowd-subheader">兴趣定向：</div> -->
                               <div id="interestResult">
                                   <ul class="list"></ul>
                               </div>
                           </div>

                           <!--个性化人群定向 s-->
                           <div id="personalityContent" class="Tab-Content">
                               <div class="crowd-header crowd-subheader">
                                  	 已选个性化人群包
                                   <a href="javascript:void(0)" id="clearPersonality">全部清除</a>
                               </div>
<!--                                <div class="crowd-subheader">个性化人群包：</div> -->
                               <div id="personalityResult">
                                   <ul class="single"></ul>
                               </div>
                           </div>

                           <!--访客找回 s-->
                           <div id="guestContent" class="Tab-Content">
                               <div class="crowd-header crowd-subheader">
                                   	已选访客找回定向
                                   <a href="javascript:void(0)" id="clearGuest">全部清除</a>
                               </div>
<!--                                <div class="crowd-subheader">访客找回：</div> -->
                               <div id="guestResult">
                                   <ul class="single">
                                   </ul>
                               </div>
                           </div>
                       </div>
                   </div>
               </div>
           </div>

           <!-- 设备定向 -->
           <div id="device" style="display: none;">
               <div class="strategy_tit">设备定向</div>
               <div class="strategy_con clearfix">
                   <!-- 操作系统 -->
                   <div class="clearfix">
                       <span>操作系统：</span>
                       
                       <input type="hidden" id="osType_data" value='${osTypeSet}'>
                       <input type="hidden" id="osType_sel" value=''>
                       
                       <div class="radio-wrap">
                           <div class="radio-list">
                               <div class="radio-style <c:if test="${ osTypeSet!='android' && osTypeSet!='ios' }"> active</c:if>">
                                   <input id="osType_ALL" type="radio" name="osType" value="" <c:if test="${ osTypeSet!='android' && osTypeSet!='ios' }"> checked="checked"</c:if>>
                               </div>
                               <label for="osType_ALL">不限</label>
                           </div>
                           <div class="radio-list">
                               <div class="radio-style <c:if test="${ osTypeSet=='android'}"> active</c:if>">
                                   <input id="osType_ANDROID" type="radio" name="osType" value="android" <c:if test="${ osTypeSet=='android'}"> checked="checked"</c:if>>
                               </div>
                               <label for="osType_ANDROID">Android</label>
                           </div>
                           <div class="radio-list">
                               <div class="radio-style <c:if test="${ osTypeSet=='ios'}"> active</c:if>">
                                   <input id="osType_IOS" type="radio" name="osType" value="ios" <c:if test="${ osTypeSet=='ios'}"> checked="checked"</c:if>>
                               </div>
                               <label for="osType_IOS">IOS</label>
                           </div>
                       </div>
                   </div>

                   <!-- 设备类型 -->
                   <div class="clearfix">
                       <span>设备类型：</span>
                       
                       <input type="hidden" id="deviceType_data" value='${deviceTypeSet}'>
                       
                       <div class="radio-wrap">
                           <div class="radio-list">
                               <div class="radio-style">
                                   <input id="deviceTypeSelect_0" type="radio" name="deviceTypeSelect" value="0">
                               </div>
                               <label for="deviceTypeSelect_0">不限</label>
                           </div>
                           <div class="radio-list">
                               <div class="radio-style">
                                   <input id="deviceTypeSelect_1" type="radio" name="deviceTypeSelect" value="1">
                               </div>
                               <label for="deviceTypeSelect_1">自定义</label>
                           </div>
                       </div>

                       <div class="strategy_custom" id="msg_deviceType_div" style="display:none;">
                           <i id="msg_deviceType" class="custom_tip" style="float:right;">&nbsp;&nbsp;请确保数据不为空，否则活动将不能投放。</i>
                       </div>
                       <!--自定义显示-->
                       <div id="deviceType-Container" class="devicetype-control clearfix" style="display: block;">
                           <div class="devicetype-checkbox">
                               <div class="devicetype-checkboxinput">
                                   <input name="deviceType" id="deviceType_1" type="checkbox" value="1">
                                   <label for="deviceType_1"></label>
                               </div>
                               <div class="devicetype-checkboxlabel">手机</div>
                           </div>
                           <div class="devicetype-checkbox">
                               <div class="devicetype-checkboxinput">
                                   <input name="deviceType" id="deviceType_2" type="checkbox" value="2">
                                   <label for="deviceType_2"></label>
                               </div>
                               <div class="devicetype-checkboxlabel">平板电脑</div>
                           </div>
                           <div class="devicetype-checkbox">
                               <div class="devicetype-checkboxinput">
                                   <input name="deviceType" id="deviceType_0" type="checkbox" value="0">
                                   <label for="deviceType_0"></label>
                               </div>
                               <div class="devicetype-checkboxlabel">未知</div>
                           </div>
                       </div>
                   </div>

                   <!-- 设备品牌 -->
                   <div class="clearfix">
                       <span>设备品牌：</span>
                       
                       <input type="hidden" id="brand_data" value="${brandSet}">
                       
                       <div class="radio-wrap">
                           <div class="radio-list">
                               <div class="radio-style">
                                   <input id="brandSelect_0" type="radio" name="brandSelect" value="0">
                               </div>
                               <label for="brandSelect_0">不限</label>
                           </div>
                           <div class="radio-list">
                               <div class="radio-style">
                                   <input id="brandSelect_1" type="radio" name="brandSelect" value="1">
                               </div>
                               <label for="brandSelect_1">自定义</label>
                           </div>
                       </div>
                       <div class="strategy_custom" id="msg_brand_div" style="display:none;">
                           <i id="msg_brand" class="custom_tip"
                              style="float:right;">&nbsp;&nbsp;请确保数据不为空，否则活动将不能投放。</i>
                       </div>
                       <!--自定义显示-->
                       <div id="deviceBrand-Container" class="devicetype-control clearfix"
                            style="display: none;">
                           <div class="devicetype-checkbox">
                               <div class="devicetype-checkboxinput">
                                   <input name="brand" id="brand_apple" type="checkbox" value="apple">
                                   <label for="brand_apple"></label>
                               </div>
                               <div class="devicetype-checkboxlabel">苹果</div>
                           </div>
                           <div class="devicetype-checkbox">
                               <div class="devicetype-checkboxinput">
                                   <input name="brand" id="brand_samsung" type="checkbox" value="samsung">
                                   <label for="brand_samsung"></label>
                               </div>
                               <div class="devicetype-checkboxlabel">三星</div>
                           </div>
                           <div class="devicetype-checkbox">
                               <div class="devicetype-checkboxinput">
                                   <input name="brand" id="brand_xiaomi" type="checkbox" value="xiaomi">
                                   <label for="brand_xiaomi"></label>
                               </div>
                               <div class="devicetype-checkboxlabel">小米</div>
                           </div>
                           <div class="devicetype-checkbox">
                               <div class="devicetype-checkboxinput">
                                   <input name="brand" id="brand_huawei" type="checkbox" value="huawei">
                                   <label for="brand_huawei"></label>
                               </div>
                               <div class="devicetype-checkboxlabel">华为</div>
                           </div>
                           <div class="devicetype-checkbox">
                               <div class="devicetype-checkboxinput">
                                   <input name="brand" id="brand_oppo" type="checkbox" value="oppo">
                                   <label for="brand_oppo"></label>
                               </div>
                               <div class="devicetype-checkboxlabel">OPPO</div>
                           </div>
                           <div class="devicetype-checkbox">
                               <div class="devicetype-checkboxinput">
                                   <input name="brand" id="brand_coolpad" type="checkbox" value="coolpad">
                                   <label for="brand_coolpad"></label>
                               </div>
                               <div class="devicetype-checkboxlabel">酷派</div>
                           </div>
                           <div class="devicetype-checkbox">
                               <div class="devicetype-checkboxinput">
                                   <input name="brand" id="brand_lenovo" type="checkbox" value="lenovo">
                                   <label for="brand_lenovo"></label>
                               </div>
                               <div class="devicetype-checkboxlabel">联想</div>
                           </div>
                           <div class="devicetype-checkbox">
                               <div class="devicetype-checkboxinput">
                                   <input name="brand" id="brand_meizu" type="checkbox" value="meizu">
                                   <label for="brand_meizu"></label>
                               </div>
                               <div class="devicetype-checkboxlabel">魅族</div>
                           </div>
                           <div class="devicetype-checkbox">
                               <div class="devicetype-checkboxinput">
                                   <input name="brand" id="brand_vivo" type="checkbox" value="vivo">
                                   <label for="brand_vivo"></label>
                               </div>
                               <div class="devicetype-checkboxlabel">VIVO</div>
                           </div>
                           <div class="devicetype-checkbox">
                               <div class="devicetype-checkboxinput">
                                   <input name="brand" id="brand_gionee" type="checkbox" value="gionee">
                                   <label for="brand_gionee"></label>
                               </div>
                               <div class="devicetype-checkboxlabel">金立</div>
                           </div>
                           <div class="devicetype-checkbox">
                               <div class="devicetype-checkboxinput">
                                   <input name="brand" id="brand_zte" type="checkbox" value="zte">
                                   <label for="brand_zte"></label>
                               </div>
                               <div class="devicetype-checkboxlabel">中兴</div>
                           </div>
                           <div class="devicetype-checkbox">
                               <div class="devicetype-checkboxinput">
                                   <input name="brand" id="brand_sony" type="checkbox" value="sony">
                                   <label for="brand_sony"></label>
                               </div>
                               <div class="devicetype-checkboxlabel">索尼</div>
                           </div>
                           <div class="devicetype-checkbox">
                               <div class="devicetype-checkboxinput">
                                   <input name="brand" id="brand_htc" type="checkbox" value="htc">
                                   <label for="brand_htc"></label>
                               </div>
                               <div class="devicetype-checkboxlabel">HTC</div>
                           </div>
                           <div class="devicetype-checkbox">
                               <div class="devicetype-checkboxinput">
                                   <input name="brand" id="brand_lg" type="checkbox" value="lg">
                                   <label for="brand_lg"></label>
                               </div>
                               <div class="devicetype-checkboxlabel">LG</div>
                           </div>
                           <div class="devicetype-checkbox">
                               <div class="devicetype-checkboxinput">
                                   <input name="brand" id="brand_micromax" type="checkbox" value="micromax">
                                   <label for="brand_micromax"></label>
                               </div>
                               <div class="devicetype-checkboxlabel">MICROMAX</div>
                           </div>
                           <div class="devicetype-checkbox">
                               <div class="devicetype-checkboxinput">
                                   <input name="brand" id="brand_motorola" type="checkbox" value="motorola">
                                   <label for="brand_motorola"></label>
                               </div>
                               <div class="devicetype-checkboxlabel">摩托罗拉</div>
                           </div>
                           <div class="devicetype-checkbox">
                               <div class="devicetype-checkboxinput">
                                   <input name="brand" id="brand_nubia" type="checkbox" value="nubia">
                                   <label for="brand_nubia"></label>
                               </div>
                               <div class="devicetype-checkboxlabel">努比亚</div>
                           </div>
                           <div class="devicetype-checkbox">
                               <div class="devicetype-checkboxinput">
                                   <input name="brand" id="brand_tcl" type="checkbox" value="tcl">
                                   <label for="brand_tcl"></label>
                               </div>
                               <div class="devicetype-checkboxlabel">TCL</div>
                           </div>
                           <div class="devicetype-checkbox">
                               <div class="devicetype-checkboxinput">
                                   <input name="brand" id="brand_nokia" type="checkbox" value="nokia">
                                   <label for="brand_nokia"></label>
                               </div>
                               <div class="devicetype-checkboxlabel">诺基亚</div>
                           </div>
                           <div class="devicetype-checkbox">
                               <div class="devicetype-checkboxinput">
                                   <input name="brand" id="brand_microsoft" type="checkbox" value="microsoft">
                                   <label for="brand_microsoft"></label>
                               </div>
                               <div class="devicetype-checkboxlabel">微软</div>
                           </div>
                           <div class="devicetype-checkbox">
                               <div class="devicetype-checkboxinput">
                                   <input name="brand" id="brand_asus" type="checkbox" value="asus">
                                   <label for="brand_asus"></label>
                               </div>
                               <div class="devicetype-checkboxlabel">华硕</div>
                           </div>
                           <div class="devicetype-checkbox">
                               <div class="devicetype-checkboxinput">
                                   <input name="brand" id="brand_acer" type="checkbox" value="acer">
                                   <label for="brand_acer"></label>
                               </div>
                               <div class="devicetype-checkboxlabel">宏基</div>
                           </div>
                           <div class="devicetype-checkbox">
                               <div class="devicetype-checkboxinput">
                                   <input name="brand" id="brand_benq" type="checkbox" value="benq">
                                   <label for="brand_benq"></label>
                               </div>
                               <div class="devicetype-checkboxlabel">明基</div>
                           </div>
                           <div class="devicetype-checkbox">
                               <div class="devicetype-checkboxinput">
                                   <input name="brand" id="brand_dell" type="checkbox" value="dell">
                                   <label for="brand_dell"></label>
                               </div>
                               <div class="devicetype-checkboxlabel">戴尔</div>
                           </div>
                           <div class="devicetype-checkbox">
                               <div class="devicetype-checkboxinput">
                                   <input name="brand" id="brand_hp" type="checkbox" value="hp">
                                   <label for="brand_hp"></label>
                               </div>
                               <div class="devicetype-checkboxlabel">惠普</div>
                           </div>
                           <div class="devicetype-checkbox">
                               <div class="devicetype-checkboxinput">
                                   <input name="brand" id="brand_letv" type="checkbox" value="letv">
                                   <label for="brand_letv"></label>
                               </div>
                               <div class="devicetype-checkboxlabel">乐视</div>
                           </div>
                           <div class="devicetype-checkbox">
                               <div class="devicetype-checkboxinput">
                                   <input name="brand" id="brand_others" type="checkbox" value="others">
                                   <label for="brand_others"></label>
                               </div>
                               <div class="devicetype-checkboxlabel">其他</div>
                           </div>
                       </div>
                       <!--自定义显示/-->
                   </div>

                   <!-- 网络类型 -->
                   <div class="clearfix">
                       <span>网络类型：</span>
                       
                       <input type="hidden" id="networkType_data" value="${networkTypeSet}">
                       
                       <div class="radio-wrap">
                           <div class="radio-list">
                               <div class="radio-style">
                                   <input type="radio" id="networkTypeSelect_0" name="networkTypeSelect" value="0"></div>
                               <label for="networkTypeSelect_0">不限</label>
                           </div>
                           <div class="radio-list">
                               <div class="radio-style">
                                   <input type="radio" id="networkTypeSelect_1" name="networkTypeSelect" value="1">
                               </div>
                               <label for="networkTypeSelect_1">自定义</label>
                           </div>
                       </div>
                       
                       <div class="strategy_custom" id="msg_networkType_div" style="display:none;">
                           <i id="msg_networkType" class="custom_tip" style="float:right; ">&nbsp;&nbsp;请确保数据不为空，否则活动将不能投放。</i>
                       </div>
                       <!--自定义显示-->
                       <div id="networkType-Container" class="devicetype-control clearfix"
                            style="display: none;">
                           <div class="devicetype-checkbox">
                               <div class="devicetype-checkboxinput">
                                   <input name="networkType" id="networkType_1" type="checkbox" value="1">
                                   <label for="networkType_1"></label>
                               </div>
                               <div class="devicetype-checkboxlabel">WIFI</div>
                           </div>
                           <div class="devicetype-checkbox">
                               <div class="devicetype-checkboxinput">
                                   <input name="networkType" id="networkType_2" type="checkbox" value="2">
                                   <label for="networkType_2"></label>
                               </div>
                               <div class="devicetype-checkboxlabel">2G</div>
                           </div>
                           <div class="devicetype-checkbox">
                               <div class="devicetype-checkboxinput">
                                   <input name="networkType" id="networkType_3" type="checkbox" value="3">
                                   <label for="networkType_3"></label>
                               </div>
                               <div class="devicetype-checkboxlabel">3G</div>
                           </div>
                           <div class="devicetype-checkbox">
                               <div class="devicetype-checkboxinput">
                                   <input name="networkType" id="networkType_4" type="checkbox" value="4">
                                   <label for="networkType_4"></label>
                               </div>
                               <div class="devicetype-checkboxlabel">4G</div>
                           </div>
                           <div class="devicetype-checkbox">
                               <div class="devicetype-checkboxinput">
                                   <input name="networkType" id="networkType_0" type="checkbox" value="0">
                                   <label for="networkType_0"></label>
                               </div>
                               <div class="devicetype-checkboxlabel">未知</div>
                           </div>
                       </div>
                   </div>

                   <!-- 网络运营商 -->
                   
                   <div class="clearfix">
                       <span>网络运营商：</span>
                       
                       <input type="hidden" id="operateType_data" value="${operateTypeSet}">
                       
                       <div class="radio-wrap">
                           <div class="radio-list">
                               <div class="radio-style">
                                   <input type="radio" id="operateTypeSelect_0" name="operateTypeSelect" value="0">
                               </div>
                               <label for="operateTypeSelect_0">不限</label>
                           </div>
                           <div class="radio-list">
                               <div class="radio-style">
                                   <input type="radio" id="operateTypeSelect_1" name="operateTypeSelect" value="1">
                               </div>
                               <label for="operateTypeSelect_1">自定义</label>
                           </div>
                       </div>
                       
                       <div class="strategy_custom" id="msg_operateType_div" style="display:none;">
                           <i id="msg_operateType" class="custom_tip" style="float:right; ">&nbsp;&nbsp;请确保数据不为空，否则活动将不能投放。</i>
                       </div>
                       <!--自定义显示-->
                       <div id="operateType-Container" class="devicetype-control clearfix"
                            style="display: none;">
                           <div class="devicetype-checkbox">
                               <div class="devicetype-checkboxinput">
                                   <input name="operateType" id="operateType_1" type="checkbox" value="1">
                                   <label for="operateType_1"></label>
                               </div>
                               <div class="devicetype-checkboxlabel">中国移动</div>
                           </div>
                           <div class="devicetype-checkbox">
                               <div class="devicetype-checkboxinput">
                                   <input name="operateType" id="operateType_2" type="checkbox" value="2">
                                   <label for="operateType_2"></label>
                               </div>
                               <div class="devicetype-checkboxlabel">中国联通</div>
                           </div>
                           <div class="devicetype-checkbox">
                               <div class="devicetype-checkboxinput">
                                   <input name="operateType" id="operateType_3" type="checkbox" value="3">
                                   <label for="operateType_3"></label>
                               </div>
                               <div class="devicetype-checkboxlabel">中国电信</div>
                           </div>
                           <div class="devicetype-checkbox">
                               <div class="devicetype-checkboxinput">
                                   <input name="operateType" id="operateType_0" type="checkbox" value="0">
                                   <label for="operateType_0"></label>
                               </div>
                               <div class="devicetype-checkboxlabel">未知</div>
                           </div>
                       </div>
                   </div>
               </div>
           </div>

           <!--媒体定向-->
           <div id="media" style="display: block;">
               <div class="strategy_tit">媒体定向</div>
               <div class="strategy_con clearfix">
                   <span>媒体选择：</span>
                   <div class="radio-wrap">
                       <div class="radio-list">
                           <div class="radio-style <c:if test="${media_type_value !=1 and media_type_value!=2}">active</c:if>">
                               <input id="mediaType1" type="radio" name="media_type" value="0" <c:if test="${media_type_value !=1 and media_type_value!=2}">checked="checked"</c:if>>
                           </div>
                           <label for="mediaType1">全网智能投放</label>
                       </div>
                       <div class="radio-list">
                           <div class="radio-style <c:if test="${media_type_value == 1 }">active</c:if>">
                               <input type="radio" name="media_type" id="media_type_dirrect" value="1" <c:if test="${media_type_value == 1 }">checked="checked"</c:if>>
                           </div>
                           <label for="media_type_dirrect">按照媒体类型投放</label>
                       </div>
                       <div class="radio-list">
                           <div class="radio-style <c:if test="${media_type_value == 2 }">active</c:if>">
                               <input type="radio" name="media_type" id="media_name_dirrect" value="2" <c:if test="${media_type_value == 2 }">checked="checked"</c:if>>
                           </div>
                           <label for="media_name_dirrect">按照媒体域名投放</label>
                       </div>
                   </div>
               </div>
           </div>

           <!--应用定向-->
           <div id="app" style="display: none;">
               <div class="strategy_tit">应用定向</div>
               
               <input type="hidden" id="appSelectValue" value="${appSelectValue}">
               <input type="hidden" id="appData_radio" value="${appSelectValue}">
               <input type="hidden" id="appData" value='${appData}'>
               
               <div class="strategy_con clearfix">
                   <span>应用定向：</span>
                   <div class="radio-wrap">
                       <div class="radio-list">
                           <div class="radio-style <c:if test="${appSelectValue != 1 && appSelectValue != 2 }">active</c:if>">
                               <input type="radio" name="appSelect" id="appSelect_0" value="0" <c:if test="${appSelectValue != 1 && media_type_value != 2 }">checked="checked"</c:if>>
                           </div>
                           <label for="appSelect_0">不限</label>
                       </div>
                       <div class="radio-list">
                           <div class="radio-style <c:if test="${appSelectValue == 1}">active</c:if>">
                               <input type="radio" name="appSelect" id="appSelect_1" value="1" <c:if test="${appSelectValue == 1}">checked="checked"</c:if>>
                           </div>
                           <label for="appSelect_1">按应用类型投放</label>
                       </div>
                       <div class="radio-list">
                           <div class="radio-style <c:if test="${appSelectValue == 2}">active</c:if>">
                               <input type="radio" name="appSelect" id="appSelect_2" value="2" <c:if test="${appSelectValue == 2}">checked="checked"</c:if>>
                           </div>
                           <label for="appSelect_2">按应用包投放</label>
                       </div>
                   </div>
               </div>
           </div>

           <!--  高级设置 -->
           <div class="strategy_tit">高级设置</div>
           <div class="strategy_con clearfix">
               <div class="clearfix mb10">
                   <span>投放频次：</span>
                   
                   <input type="hidden" id="freq_type" value="${ freq_type }">
                   
                   <div class="radio-wrap">
                       <div class="radio-list">
                           <div class="radio-style <c:if test="${freq_type !=1 }">active</c:if>">
                               <input id="g1" type="radio" name="freq_type" value="0" <c:if test="${freq_type !=1 }">checked="checked"</c:if>>
                           </div>
                           <label for="g1">不限</label>
                       </div>
                       <div class="radio-list">
                           <div class="radio-style <c:if test="${freq_type ==1 }">active</c:if>">
                               <input type="radio" name="freq_type" value="1" <c:if test="${freq_type == 1}">checked="checked"</c:if>>
                           </div>
                           <label for="g2">自定义</label>
                       </div>
                   </div>
                   <div class="strategy_custom" id="custom_freq_type" style="display: none;">
                       <i>每个活动每天对每个用户，展现：
                           <input class="input_text" type="text" id="freqValue" value="${ freqValue }">次
                       </i>
                       <i id="custom_tip_btn" class="custom_tip" style="float:right; display:none;">
                                                                        频次格式非法，请检查设置。
                       </i>
                   </div>
                   <!--自定义显示/-->
               </div>
               <div class="clearfix">
                   <span>屏数控制：</span>
                   <div class="radio-wrap">
                       <div class="radio-list">
                           <div class="radio-style <c:if test="${ empty screen_sel_value or screen_sel_value ==0 }"> active</c:if>">
                               <input id="screen_type1" type="radio" name="screen_type" value="0" <c:if test="${ empty screen_sel_value or screen_sel_value ==0 }"> checked="checked"</c:if>>
                           </div>
                           <label for="screen_type1">不限</label>
                       </div>
                       <div class="radio-list">
                           <div class="radio-style <c:if test="${ screen_sel_value ==1 }"> active</c:if>">
                               <input id="screen_type2" type="radio" name="screen_type" value="1" <c:if test="${ screen_sel_value ==1 }"> checked="checked"</c:if>>
                           </div>
                           <label for="screen_type2">首屏</label>
                       </div>
                       <div class="radio-list">
                           <div class="radio-style <c:if test="${ screen_sel_value ==2 }"> active</c:if>">
                               <input id="screen_type3" type="radio" name="screen_type" value="2" <c:if test="${ screen_sel_value ==2 }"> checked="checked"</c:if>>
                           </div>
                           <label for="screen_type3">非首屏</label>
                       </div>
                   </div>
               </div>
           </div>

           <!-- 计费出价 -->
           <div class="strategy_tit">计费出价</div>
           <div class="strategy_con clearfix"><span>最高限价：</span>
               <div class="charg_input">
                   <input type="text" id="price" name="price" value="<fmt:formatNumber value="${ po.price/100}" pattern="0.00"/>" class="input_text" >
                   <i class="charg_input_yen">元</i>
                   <p class="charg_input-des">建议出价：0.8~3元</p>
               </div>
               <div class="radio-wrap">
                   <div class="radio-list">
                       <div class="radio-style <c:if test="${ po.expendType==1 }"> active</c:if>">
                           <input id="expend_type1" type="radio" name="expend_type" value="1" <c:if test="${ po.expendType==1 }"> checked="checked"</c:if>>
                       </div>
                       <label for="expend_type1">CPC</label>
                   </div>
                   <div class="radio-list">
                       <div class="radio-style <c:if test="${ po.expendType!=1 }"> active</c:if>">
                           <input id="expend_type2" type="radio" name="expend_type" value="0" <c:if test="${ po.expendType!=1 }"> checked="checked"</c:if>>
                       </div>
                       <label for="expend_type2">CPM</label>
                   </div>
                   <div class="radio-list">
                       <span id="price_tip" class="activity_tip" style="display:none;width:160px;">(最高限价不能为空)</span>
                   </div>
               </div>
           </div>
       </div>

       <div class="clearfix new_activity_bt">
        
       	   <a href="${baseurl }front/campManage/camp_edit.action?id=${po.id }&step=1">
       	   	   <span class="btn btn-gray btn-small btn-save" id="save_camp_btn">上一步</span>
       	   </a>
       	   <shiro:hasPermission name="user:operable">
           	   <span class="btn btn-gray btn-small btn-save" id="save_camp_btn_2">下一步（提交中）</span>
	           <c:if test="${po.editStepStatus>=3}">
	               <span id="save_camp_btn_2_back" class="btn btn-gray btn-small btn-save">确认并返回</span>
	           </c:if>
           </shiro:hasPermission>
           <a href="${baseurl }front/campManage/camp_list.action" class="btn btn-small btn-purple">返回</a>
       </div>
   </div>
   <!--新建活动content/-->
	
</div>
<!--index content/-->

<!-- --------------------------------以下为隐藏弹出层--------------------------------------------- -->

<!--选择交易渠道-->
<div id="custom_channel_page" class="" style="display: none;">
    <div class="layer_blue_tit">选择交易渠道<span id="all_clean_channel"><i id="all_channel">全选</i>/<i id="clean_channel">清空</i></span></div>
    <div class="check_channel_con">

        <c:choose>
            <c:when test="${ not empty channel_sel_value }">
                <c:forEach var="ch_po" items="${listChannel}">
                    <c:set var="flag" value="false"></c:set>
                    <c:forEach items="${channel_sel_value}" var="ch_sel">
                        <c:if test="${  ch_sel == ch_po.id }">
                            <c:set var="flag" value="true"></c:set>
                        </c:if>
                    </c:forEach>
                    <c:choose>
                        <c:when test="${flag==true }">

                            <label>
                                <input type="checkbox" name="channel"  checked="checked"  value="${ch_po.id}"><img src="${pageContext.request.contextPath}${ch_po.logo}">
                            </label>
                        </c:when>
                        <c:otherwise>
                            <label>
                                <input type="checkbox" name="channel"  value="${ch_po.id}"><img src="${pageContext.request.contextPath}${ch_po.logo}">
                            </label>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <c:forEach var="ch_po" items="${listChannel}">
                    <label>
                        <input type="checkbox" name="channel" value="${ch_po.id}"><img src="${pageContext.request.contextPath}${ch_po.logo}">
                    </label>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
    <div class="check_channel_line"></div>
    <div class="clearfix layer_two_bt" style="magin-bottom:10px;">
        <input type="button" value="确 定" class="two_blue_button" id="channel_conform">
        <input type="reset" value="取 消" class="two_blue_reset" id="channel_canncel">
    </div>
</div>
<!--选择交易渠道/-->

<!--选择媒体类型投放：-->
<div id="media_type_window" class="layer_check_type" style="display:none;">
    <input name="media_type_data" id="media_type_data" value="" type="hidden">
    <input name="media_type_flag" id="media_type_flag" value="0" type="hidden">

    <div class="layer_blue_tit">选择媒体类型投放<span><i id="medis_type_all">全选</i>/<i id="medis_type_empty">清空</i></span></div>
    <div class="check_type_con">
        <ul id="media_type_one">

        </ul>
    </div>
    <div class="clearfix layer_two_bt">
        <input type="button" value="确 定" class="two_blue_button" id="media_type_conform">
        <input type="reset" value="取 消" class="two_blue_reset" id="media_type_canncel">
    </div>
</div>
<!--选择媒体类型投放/-->

<!--输入媒体域名或名称_搜索-->
<div id="domain_name_window" class="layer_domain_name" style="display:none;">
    <form  id="formId" method="post" action="${baseurl }front/campManage/listMedia.action?camp_id=${po.id }" class="form-inline">
        <input name="media_name" id="media_name" value="" type="hidden">
        <input name="media_save_flag" id="media_save_flag" value="0" type="hidden">

        <div class="domain_name_search">
            <!--add search-->
            <div class="domain_search">
                <span class="domain_tit">排序规则:</span>
                <label><input type="radio" name="manualEntry"  id="manualEntry_2" checked="checked" value="2">默认</label>
                <label><input type="radio" name="manualEntry"  id="manualEntry_0"  value="0">按系统录入排序</label>
                <label><input type="radio" name="manualEntry"  id="manualEntry_1"  value="1">按手工录入排序</label>
            </div>
            <!--add search/-->
            <input type="text" id="domain" name="domain" value="">
            <input type="submit" value="搜 索" id="doamin_search_btn">
        </div>

        <div class="clearfix">
            <div class="domain_name_lt">
                <div class="domain_name_list">
                    <div class="domain_name_tit">
                        <ul>
                            <li class="w_name">域名</li>
                            <li class="w_cname">中文名</li>
                            <li class="w_sel">媒体类型
                                <select id="domain_name_select" name="webSiteType">

                                </select>
                            </li>
                            <li>录入方式</li>
                            <li class="w_sel">流量范围
                                <select name="flow">
                                    <option value="">不限</option>
                                    <option value="5">5万-10万</option>
                                    <option value="10">10万-50万</option>
                                    <option value="50">50万-100万</option>
                                    <option value="100">100万-500万</option>
                                </select>
                            </li>
                        </ul>
                    </div>
                    <div class="domain_name_con">
                        <table id="domain_name_table">

                        </table>
                    </div>
                </div>
                <div id="page_window">

                </div>
            </div>
            <div class="domain_name_rt">
                <div class="domain_name_operation">
                    <div class="layer_blue_tit">已选媒体域名 <span><i id="domain_name_del_all">全部删除</i></span></div>
                    <div class="domain_name_del" >
                        <ul id="domain_name_del">

                        </ul>
                    </div>
                </div>
                <div class="clearfix layer_two_bt">
                    <input type="button" value="确 定" class="two_blue_button" id="media_name_conform">
                    <input type="reset" value="取 消" class="two_blue_reset"  id="media_name_canncel">
                </div>
            </div>
        </div>
    </form>
</div>
<!--输入媒体域名或名称_搜索/-->

<!--选择应用类型投放：-->
<div id="app_type_window" class="layer_check_type" style="display:none;">
    <input name="app_type_data" id="app_type_data" value="" type="hidden">
    <input name="app_type_flag" id="app_type_flag" value="0" type="hidden">

    <div class="layer_blue_tit">选择应用类型投放<span><i id="app_type_all">全选</i>/<i id="app_type_empty">清空</i></span></div>
    <div class="check_type_con">
        <ul id="app_type_one">

        </ul>
    </div>
    <div class="clearfix layer_two_bt">
        <input type="button" value="确 定" class="two_blue_button" id="app_type_conform">
        <input type="reset" value="取 消" class="two_blue_reset" id="app_type_canncel">
    </div>
</div>
<!--选择应用类型投放/-->

<!--应用包投放-->
<div id="app_name_window" class="layer_domain_name" style="display:none;">
    <form method="post" id="form1" action="${baseurl}front/app/list.action?1=1" class="form-inline">

        <input name="app_name" id="app_name" value="" type="hidden">
        <input name="app_save_flag" id="app_save_flag" value="0" type="hidden">

        <div class="domain_name_search">
            <!--add search-->
            <%--<div class="domain_search">--%>
                <%--<span class="domain_tit">排序规则:</span>--%>
                <%--<label><input type="radio" name="manualEntry"  id="manualEntry_2" checked="checked" value="2">默认</label>--%>
                <%--<label><input type="radio" name="manualEntry"  id="manualEntry_0"  value="0">按系统录入排序</label>--%>
                <%--<label><input type="radio" name="manualEntry"  id="manualEntry_1"  value="1">按手工录入排序</label>--%>
            <%--</div>--%>
            <!--add search/-->
            <input type="text" id="appName" name="appName" value="">
            <input type="submit" value="搜 索" id="app_search_btn">
        </div>

        <div class="clearfix">
            <div class="app_name_lt">
                <div class="domain_name_list">
                    <div class="domain_name_tit">
                        <ul>
                            <li class="w_name">ID</li>
                            <li class="w_cname">应用名</li>
                            <li class="w_sel">渠道
                                <select name="channelId">
                                    <option value="">不限</option>
                                    <c:forEach var="chPo" items="${listChannel}">
                                        <option value="${chPo.id}">${chPo.channelName}</option>
                                    </c:forEach>
                                    <option value="0">通用</option>
                                </select>
                            </li>
                            <li class="w_sel">包名</li>
                        </ul>
                    </div>
                    <div class="domain_name_con">
                        <table id="app_name_table">

                        </table>
                    </div>
                </div>
                <div id="app_page_window">

                </div>
            </div>
            <div class="app_name_rt">
                <div class="domain_name_operation">
                    <div class="layer_blue_tit">已选应用名 <span><i id="app_name_del_all">全部删除</i></span></div>
                    <div class="domain_name_del" >
                        <ul id="app_name_del">

                        </ul>
                    </div>
                </div>
                <div class="clearfix layer_two_bt">
                    <input type="button" value="确 定" class="two_blue_button" id="app_name_conform">
                    <input type="reset" value="取 消" class="two_blue_reset"  id="app_name_canncel">
                </div>
            </div>
        </div>
    </form>
</div>
<!--输入媒体域名或名称_搜索/-->

<!--选择地域-->
<div id="area_select" class="layer_check_dress" style="display:none">
    <!-- 保存已经选中的地域id -->
    <input type="hidden" id="area" value="${areas_sel_value }">
    <div class="layer_blue_tit">选择地域<span><i id="select_all" style="cursor:pointer">全选</i>/<i id="clean_all" style="cursor:pointer">清空</i></span></div>
    <div class="check_dress_con">
        <!-- 显示地域 -->
    </div>
    <div class="clearfix layer_two_bt">
        <input type="button" value="确 定" class="two_blue_button" id="area_ok_btn">
        <input type="reset" value="取 消" class="two_blue_reset" id="area_cancel_btn">
    </div>
</div>
<!-- 标识地域的html是否已经创建了，js中会有判断，0否1是 -->
<input type="hidden" id="area_html_created" value="0">
<!--选择地域 /-->

</body>
</html>