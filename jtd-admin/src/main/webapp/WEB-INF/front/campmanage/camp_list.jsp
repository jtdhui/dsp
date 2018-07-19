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
    <title>活动管理</title>
    <%@ include file="/WEB-INF/commonjsp/common_css.jsp"%>
    <%@ include file="/WEB-INF/commonjsp/common_js.jsp"%>
    <script type="text/javascript" src="${baseurl}js/front/date_layer.js?d=${activeUser.currentTime}"></script>
    <script type="text/javascript" src="${baseurl}dist/js/camp_list.js?d=${activeUser.currentTime}"></script>
    
    <link rel="stylesheet" href="${baseurl}dist/less/delivery.css?d=${activeUser.currentTime}">
    
    <script type="text/javascript">
        $(function(){
            $("#stat_cycle").dateLayer("startDate","endDate",'${paraMap.startDate}','${paraMap.endDate}',"camp");
        });
    </script>
</head>

<style>
.btn2 {
    display: inline-block;
    padding: 0;
    font-size: 20px;
    text-align: center;
    color: #fff;
    font-style: normal;
}
.condition-btns .btn2 {
  width: 126px;
  height: 42px;
  line-height: 40px;
  border-radius: 20px;
  margin: 0 15px;
}
</style>

<body>
<!--header 导航 -->
<jsp:include page="/WEB-INF/front/header.jsp?param=campaign"></jsp:include>

<!--index content -->
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
                        <a class="nav-breadcrumb active" href="javascript:void(0)">广告活动列表</a>
                    </li>
                </ol>
            </div>
            
            <!-- 切换广告主 -->
            <jsp:include page="/WEB-INF/front/selectPartner.jsp?fromPage=camp"></jsp:include>
            
        </div>

		<form method="post" action="${baseurl }front/campManage/camp_list.action" id="formId" class="form-inline">
		
        <!-- 条件查询 -->
        <div class="form-horizontal clearfix">
            <div class="select-wrap clearfix">
                <label class="select-label">流量类型：</label>
                <select class="select-box" name="campaignType">
                    <option value="">全部</option>
                    <c:forEach var="campaignType" items="${campaignTypeList }">
                        <option <c:if test="${paraMap.campaignType==campaignType.code}">selected</c:if> value="${campaignType.code }" >${campaignType.desc }</option>
                    </c:forEach>
                </select>
            </div>
            <div class="select-wrap clearfix">
                <label class="select-label">活动状态：</label>
                <select class="select-box" name="campaignStatus">
                    <option <c:if test="${ empty paraMap.campaignStatus}">selected</c:if> value="">全部</option>
                    <c:forEach var="campaignStatus" items="${campaignStatusList }">
                        <option <c:if test="${paraMap.campaignStatus==campaignStatus.code}">selected</c:if>  value="${campaignStatus.code }" >${campaignStatus.desc }</option>
                    </c:forEach>
                </select>
            </div>
            <div class="select-wrap clearfix">
                <label class="select-label">活动ID或名称：</label>
                <input type="text" value="${paraMap.campaignName}" class="input_text"  name="campaignName" >
            </div>
            <div class="select-wrap clearfix">
                <label class="select-label">创建时间：</label>
                <input type="text" class="yen_date" id="stat_cycle">
            </div>
            <div class="f-r clearfix">
                <span class="btn btn-search btn-purple" id="search_btn">查询</span>
                <span class="btn btn-search btn-gray" id="export_report_btn">下载</span>
            </div>
        </div>

        <!-- 操作按钮 -->
        <div class="condition-btns">
        	<c:choose>
                <c:when test="${activeUser.favPartner.status == 0}">
                	<a href="${baseurl }front/campManage/camp_edit.action">
		                <span class="btn btn-gray">创建活动</span>
		            </a>
                </c:when>
                <c:otherwise>
                    <span class="btn2 btn-gray">创建活动</span>
                </c:otherwise>
            </c:choose>
             <c:choose>
                 <c:when test="${activeUser.favPartner.status == 0}">
                     <span id="duplicate_campaign_btn" class="data-target btn btn-gray" data-toggle="modal">复 制</span>
                 </c:when>
                 <c:otherwise>
                     <span class="btn2 btn-gray">复 制</span>
                 </c:otherwise>
             </c:choose>

             <c:choose>
                 <c:when test="${activeUser.favPartner.status == 0}">
                     <span onclick="changeStatus(2)" class="changeStatus btn btn-gray" data-toggle="modal" >开 启</span>
                 </c:when>
                 <c:otherwise>
                     <span class="btn2 btn-gray">开 启</span>
                 </c:otherwise>
             </c:choose>

             <c:choose>
                 <c:when test="${activeUser.favPartner.status == 0}">
                     <span onclick="changeStatus(3)" class="changeStatus btn btn-gray" data-toggle="modal">暂 停</span>
                 </c:when>
                 <c:otherwise>
                     <span class="btn2 btn-gray">暂 停</span>
                 </c:otherwise>
             </c:choose>
             <span onclick="changeStatus(4)" class="changeStatus btn btn-gray" data-toggle="modal">终 止</span>
             <span id="deleteCampaign" class="btn btn-gray">删 除</span>
             <c:choose>
                 <c:when test="${activeUser.favPartner.status == 0}">
                     <span id="batch_set" class="btn btn-gray" data-target="#getCodeModal" data-toggle="modal" >批量设置</span>
                 </c:when>
                 <c:otherwise>
                     <span class="btn2 btn-gray">批量设置</span>
                 </c:otherwise>
             </c:choose>
             <c:if test="${activeUser.favPartner.status == 1}">
             	<div>
             		<b style="color: red;">该用户关联的广告主未开启,请联系管理员!</b>
             	</div>
             </c:if>
        </div>

        <!-- 表格 -->
        <div class="yen_details_table">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th width="8%">
                        <input type="checkbox" class="table_checkbox" id="check_btn">活动ID
                    </th>
                    <th width="20%">活动名称</th>
                    <th width="7%">类 型</th>
                    <th width="5%">状 态</th>
                    <th width="8%">结算方式</th>
                    <th width="7%">日预算</th>
                    <th width="7%">展现量</th>
                    <th width="7%">点击量</th>
                    <th width="7%">点击率</th>
                    <th width="9%">投放CPM</th>
                    <th width="9%">投放CPC</th>
                    <th width="6%">消 耗</th>
                </tr>
                </thead>
                <tbody>
                	<c:forEach var="item" items="${page.listMap}" varStatus="status">
                		<tr>
		                    <td>
		                        <input name="camp_check" type="checkbox" class="table_checkbox" value="${item.camp_id}" 
		                        <c:if test="${item.status ==7}">disabled</c:if>>${item.camp_id}
		                    </td>
		                    <td>
		                        <a href="${baseurl }front/campManage/camp_edit.action?id=${item.camp_id}">${item.camp_name}</a>
		                    </td>
		                    <td>${item.camp_type}</td>
		                    <td>${item.campaign_status}</td>
		                    <td>${item.expend_type}</td>
		                    <td><fmt:formatNumber value="${ item.daily_budget/100}" pattern="0.00"/></td>
		                    <td>${item.pv}</td>
		                    <td>${item.click}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${ item.pv==0 }"><fmt:formatNumber value="0"  minFractionDigits="2" type="percent" /></c:when>
                                    <c:otherwise><fmt:formatNumber value="${item.click /item.pv}"  minFractionDigits="2" type="percent" /></c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${ item.pv==0 }"><fmt:formatNumber value="0" pattern="0.00"/></c:when>
                                    <c:otherwise><fmt:formatNumber value="${item.expend/100/item.pv*1000 }" pattern="0.00"/></c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${ item.pv==0 }"><fmt:formatNumber value="0" pattern="0.00"/></c:when>
                                    <c:otherwise><fmt:formatNumber value="${item.expend/100/item.click}" pattern="0.00"/></c:otherwise>
                                </c:choose>
                            </td>
                            <td><fmt:formatNumber value="${item.expend/100}" pattern="0.00"/></td>
		                </tr>
                	</c:forEach>
                </tbody>
            </table>
        </div>

        <!-- 分页 -->
        ${page.frontPageHtml }
        
        </form>
        
    </div>
</div>

<!--index content/-->

<!-- todo:提示窗 -->
<section class="modal fade warning-wrap" id="warningTip" tabindex="-1" role="dialog" aria-labelledby="warningTip"
         aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" id="warningTip1">
                    提示
                </h4>
            </div>
            <div class="modal-body">
                <p class="tip">只能选择一个活动</p>
                <button data-dismiss="modal" class="btn btn-small btn-purple">确定</button>
            </div>
        </div>
    </div>
</section>

<!-- todo:删除 -->
<section class="modal fade del-modal" id="delModal" tabindex="-1" role="dialog" aria-labelledby="delModal"
         aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" id="delModal1">
                    操作
                </h4>
            </div>
            <div class="modal-body">
                <p>你确定执行此操作吗？</p>
                <div>
                    <button id="button_yes" data-dismiss="modal" class="btn btn-small btn-purple">确定</button>
                    <button data-dismiss="modal" class="btn btn-small btn-gray">取消</button>
                </div>
            </div>
        </div>
    </div>
</section>

<!-- todo:批量操作 -->
<section class="modal fade batch_set" id="batchSet" tabindex="-1" role="dialog" aria-labelledby="batchSet"
         aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" id="batchSet1">
                    操作
                </h4>
            </div>
            <div class="modal-body">
                <div class="form-horizontal ">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">日预算</label>
                        <div class="col-sm-10">
                            <input id="daily_budget" type="text" class="form-control input_text">
                            <span id="budget_tip" class="warning-tip text-danger">日预算不可为空，且最低50元起。或日预算格式非法，请检查设置。</span>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-2 control-label">最高限价</label>
                        <div class="col-sm-10">
                            <input id="price" type="text" class="form-control input_text">
                            <span class="warning-tip text-danger" id="price_tip">最高限价不可为空，0~正无穷。 或最高限价格式非法，请检查设置。</span>
                        </div>
                    </div>
                    <div class="btn-arr">
                        <div id="batch_confirm" class="btn btn-small btn-main">确定</div>
                        <div id="batch_cancel" data-dismiss="modal" class="btn btn-small btn-gray">取消</div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<!--活动设置-->
<div class="layer_activity_set" id="batch_set_window" style="display:none;">
    <div class="activity_set">
        <p class="clearfix">
            <label>日预算：</label>
            <input type="text" id="daily_budget" value="">
        </p>
        <p class="activity_set_tip" id="budget_tip">日预算不可为空，且最低50元起。 或日预算格式非法，请检查设置。
        </p>
        <p class="clearfix">
            <label>最高限价：</label>
            <input type="text" id="price" value="">
        </p>
        <p class="activity_set_tip" id="price_tip">最高限价不可为空，0~正无穷。 或最高限价格式非法，请检查设置。
        </p>
    </div>
    <div class="clearfix layer_two_bt">
        <input type="button" value="确 定" class="two_blue_button" id="batch_confirm">
        <input type="reset" value="取 消" class="two_blue_reset"  id="batch_cancel">
    </div>
</div>
<!--活动设置/-->

</body>
</html>