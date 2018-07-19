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
    <c:choose>
		<c:when test="${new_page == true }"><title>新建活动</title></c:when>
		<c:otherwise><title>编辑活动</title></c:otherwise>
	</c:choose>
	<%@ include file="/WEB-INF/commonjsp/common_css.jsp"%>
	<%@ include file="/WEB-INF/commonjsp/common_js.jsp"%>
	
	<link rel="stylesheet" href="${baseurl}dist/less/delivery-active1.css">
	<script type="text/javascript" src="${baseurl}dist/js/camp_edit.js?d=${activeUser.currentTime}"></script>
	
<%-- 	<link rel="stylesheet" href="${baseurl}/css/front/layer.css"> --%>
	
	<link rel="stylesheet" href="${baseurl}/static/css/layer.css">

</head>

<style>
	#custom_date_page *{font-size:12px;}
	#custom_date_page p{margin-bottom:0px;}
	.btn:hover {
  background-color: #ea5b1a;
}
</style>

<body>
<!--header 导航 -->
<jsp:include page="/WEB-INF/front/header.jsp?param=campaign"></jsp:include>

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
                  <c:choose>
		            <c:when test="${new_page == true }"><a class="nav-breadcrumb active">新建活动</a></c:when>
		            <c:otherwise><a class="nav-breadcrumb active">编辑活动</a></c:otherwise>
		          </c:choose>
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
              <i class="step-num">1</i>
              <span class="step-des">第一步：基本设置</span>
          </p>
    	  
          <c:choose>
              <c:when test="${po.editStepStatus>=2}">
                  <p class="steps step_two clearfix">
		              <s class="line"></s>
		              <a href="${baseurl }front/campManage/camp_edit.action?id=${po.id }&step=2">
		              	<i class="step-num">2</i>
		              </a>
		              <span class="step-des">第二步：投放策略</span>
		          </p>
              </c:when>
              <c:otherwise>
                  <p class="steps step_two clearfix">
		              <s class="line"></s>
		              <i class="step-num">2</i>
		              <span class="step-des">第二步：投放策略</span>
		          </p>
              </c:otherwise>
          </c:choose>
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
      <!--步骤/-->

      <!--基本设置-->
      <div class="handle-view clearfix">
          <div class="new_activity_lt ">
              <!-- 活动名称 -->
              <div class="new_active">
                  <span class="tit">* 活动名称：</span>
                  <input type="hidden" id="id" name="id" value="${po.id }">
                  <input type="hidden" id="editStepStatus" value="${po.editStepStatus }">
                  <input type="hidden" id="partnerId" name="partnerId" value="${po.partnerId }">
                  <input type="text" id="campaignName" name="campaignName" value="${po.campaignName }" size="45" maxlength="500" class="input_text" >
                  <span id="campaignName_tip" class="warning-tip" style="display:none;">（活动名称不能为空）</span>
                  <span class="warning-tip" id="alert_campaignName_info"></span>
              </div>

              <!-- 活动类型 -->
              <div class="new_active">
                  <span class="tit">* 活动类型：</span>
                  <input type="hidden" id="campaignType_data" value="${po.campaignType}">
                  <div class="radio-wrap">
                      <div class="radio-list">
                          <div class="radio-style <c:if test="${po.campaignType == 0}">active</c:if>">
                              <input id="campaignType_0" type="radio" name="campaignType" value="0" <c:if test="${po.campaignType == 0}">checked="checked"</c:if>>
                          </div>
                          <label for="r1">PC广告</label>
                      </div>
                      <div class="radio-list">
                          <div class="radio-style <c:if test="${po.campaignType == 1}">active</c:if>">
                              <input id="campaignType_1" type="radio" name="campaignType" value="1" <c:if test="${po.campaignType == 1}">checked="checked"</c:if>>
                          </div>
                          <label for="r2">APP广告</label>
                      </div>
                      <div class="radio-list">
                          <div class="radio-style <c:if test="${po.campaignType == 2}">active</c:if>">
                              <input id="campaignType_2" type="radio" name="campaignType" value="2" <c:if test="${po.campaignType == 2}">checked="checked"</c:if> >
                          </div>
                          <label for="r3">WAP广告</label>
                      </div>

                  </div>
              </div>

              <!-- 广告形式 -->
              <div class="new_active ad-type" id="adTypeSpan">
                  <span class="tit">* 广告形式：</span>
                  <div class="radio-wrap">
                      <div class="radio-list acitve_radio" data-value="0">
                          <div class="radio-style active">
                              <input id="adType_0" type="radio" name="adType" value="0" <c:if test="${po.adType == 0}">checked="checked"</c:if>>
                          </div>
                          <label for="adType_0">PC BANNER</label>
                      </div>
                      <div class="radio-list acitve_radio" data-value="4">
                          <div class="radio-style active">
                              <input id="adType_4" type="radio" name="adType" value="4" <c:if test="${po.adType == 4}">checked="checked"</c:if>>
                          </div>
                          <label for="adType_4">BANNER IN APP</label>
                      </div>
                      <div class="radio-list acitve_radio" data-value="9">
                          <div class="radio-style active">
                              <input id="adType_9" type="radio" name="adType" value="9" <c:if test="${po.adType == 9}">checked="checked"</c:if>>
                          </div>
                          <label for="adType_9">WAP BANNER</label>
                      </div>
                  </div>
              </div>

              <!-- 预算控制 -->
              <div class="new_active  budget-control">
                  <span class="tit">* 预算控制：</span>
                  <div class="radio-wrap">
                      <div class="radio-list">
                          <div class="radio-style <c:if test="${po.budgetctrltype != '1' }">active</c:if>">
                              <input id="r31" type="radio" name="budgetctrltype" value="0" <c:if test="${po.budgetctrltype != '1' }">checked="checked"</c:if>>
                          </div>
                          <label for="r31">标准：将每日预算均匀地分配到整个投放日程中</label>
                      </div>
                      <div class="radio-list">
                          <div class="radio-style <c:if test="${po.budgetctrltype == '1' }">active</c:if>">
                              <input id="r32" type="radio" name="budgetctrltype" value="1" <c:if test="${po.budgetctrltype == '1' }">checked="checked"</c:if>>
                          </div>
                          <label for="r32">尽速：在投放日程中尽快的去消耗预算</label>
                      </div>
                  </div>
              </div>

              <!-- 时间选择 -->
              <div class="new_active">
                  <span class="tit">* 开始时间：</span>
                  <input class="yen_date" name="start_time" id="start_time" value='<fmt:formatDate value="${po.startTime}" pattern="yyyy-MM-dd"/>' >
                  <span id="start_time_tip" class="warning-tip" style="display:none;">（开始时间不能为空）</span>
              </div>
              <!--时间为空就加 class="kong"-->
              <div class="new_active mt_zero kong">
                  <span class="tit">结束时间：</span>
                  <input class="yen_date" id="end_time" name="end_time" value='<fmt:formatDate value="${po.endTime}" pattern="yyyy-MM-dd"/>'>
                  <p class="new_acitve_tip">为空表示不限结束时间</p>
              </div>

              <!-- 投放时间 -->
              <div class="new_active market-time">
                  <span class="tit">* 投放时间段：</span>
                  <input type="hidden" id="week_hour" name="week_hour" value='${po.weekHour}'>
                  <div class="radio-wrap">
                      <div class="radio-list">
                          <div class="radio-style active">
                              <input type="radio" id="custom_date_btn" name="week_hour_flag" value="1" checked="checked" >
                          </div>
                          <label for="custom_date_btn">投放时间选择</label>
                      </div>
                  </div>
              </div>

              <!-- 每日预算 -->
              <div class="new_active daily-budget">
                  <span class="tit">* 每日预算(￥)：</span>
                  <div class="radio-wrap">
                      <div class="radio-list">
                          <div class="radio-style <c:if test="${empty po.dailyBudget }">active</c:if>">
                              <input type="radio" id="daily_budget_flag" name="daily_budget_flag" value="0" <c:if test="${empty po.dailyBudget }">checked="checked"</c:if>>
                          </div>
                          <label for="daily_budget_flag">不限预算</label>
                      </div>
                      <div class="radio-list">
                          <div class="radio-style <c:if test="${ not empty po.dailyBudget }">active</c:if>"">
                              <input id="budgetCustom" type="radio" name="daily_budget_flag" value="1" <c:if test="${ not empty po.dailyBudget }">checked="checked"</c:if>>
                          </div>
                          <label for="budgetCustom">自定义</label>
                          <input type="text" class="input_text" id="dailyBudget" name="dailyBudget" <c:if test="${ not empty po.dailyBudget }">value="<fmt:formatNumber value="${po.dailyBudget/100}" pattern="0.00"/>"</c:if>>
                          <label>元</label>
                      </div>
                  </div>
              </div>
          </div>
          <div class="new_activity_rt">
              <p class="news-title">示例和说明</p>
              <div class="explain">
                  <img src="${baseurl }img/explain.jpg">
              </div>
          </div>
      </div>
      <!--基本设置/-->
      <div class="clearfix new_activity_bt">
      	  <shiro:hasPermission name="user:operable">
                <c:if test="${po.deleteStatus == 0 }">
                    <button class="btn btn-gray btn-small btn-save" id="save_camp_btn">下一步</button>
                    <c:if test="${po.editStepStatus>=3}">
                        <button class="btn btn-gray btn-small btn-save" id="save_camp_btn_back">确认并返回</button>
                    </c:if>
                </c:if>
          </shiro:hasPermission>
          <a href="${baseurl }front/campManage/camp_list.action" class="btn btn-small btn-purple">返回</a>
      </div>
  </div>
  <!--新建活动content/-->

</div>

<!--index content/-->

<!--投放-新建活动-第一步-弹框 s-->
<div class="" style="display: none; " id="custom_date_page">
	<div class="time_interval_check">
		<div class="timecheck_lt">
			<label>
				<input type="radio" name="data_show" id="allWeekDisplay">整周展示</label>
			<label>
				<input type="radio" name="data_show" id="workDayDisplay">工作日全天展示</label>
			<label>
				<input type="radio" name="data_show" id="weekEndDisplay">休息日全天展示</label>
			<label>
				<input type="radio" name="data_show" id="clearDayDisplay">全部清空</label>
			<label>
				<input type="radio" name="data_show" id="customDateDisplay">自定义</label>
		</div>
		<div class="timecheck_rt">
			<span class="active"><i></i>投放时间段</span>
			<span><i></i>暂停时间段</span>
			<span>当前时间区：GMT+08：00</span>
		</div>
	</div>
	<div class="time_interval_table">
		<table id="custom_selected_hour">
			<!--table 全选 加  class="active"-->
			<tr>
				<th width="9%">日 期</th>
				<th width="15%">时间段</th>
				<th width="16%">复制到</th>
				<th width="15%">00:00-06.00</th>
				<th width="15%">06:00-12.00</th>
				<th width="15%">12:00-18.00</th>
				<th width="15%">18:00-24.00</th>
			</tr>
			<c:forEach var="i" step="1" begin="1" end="7">
				<tr id="week_${ i }" class="<c:if test="i%2==1">odd</c:if><c:if test="i%2==0">even</c:if>">
					<td>
						<c:choose>
							<c:when test="${i==1}"><input type="checkbox" value="${i}" id="Monday">星期一</c:when>
							<c:when test="${i==2}"><input type="checkbox" value="${i}" id="Tuesday">星期二</c:when>
							<c:when test="${i==3}"><input type="checkbox" value="${i}" id="Wednesday">星期三</c:when>
							<c:when test="${i==4}"><input type="checkbox" value="${i}" id="Thursday">星期四</c:when>
							<c:when test="${i==5}"><input type="checkbox" value="${i}" id="Friday">星期五</c:when>
							<c:when test="${i==6}"><input type="checkbox" value="${i}" id="Saturday">星期六</c:when>
							<c:when test="${i==7}"><input type="checkbox" value="${i}" id="Sunday">星期日</c:when>
							<c:otherwise></c:otherwise>
						</c:choose>
					</td>
					<td>00:00 - 24:00</td>
					<td><a href="javascript:void(0);" class="copyAllWeek">整周</a>&nbsp;<a href="javascript:void(0);" class="copyWorkDay">工作日</a>&nbsp;<a href="javascript:void(0);" class="copyWorkEnd">休息日</a></td>
					<td colspan="4">
						<div class="psel">
							<p><i>0</i><i>1</i><i>2</i><i>3</i><i>4</i><i>5</i></p>
							<p><i>6</i><i>7</i><i>8</i><i>9</i><i>10</i><i>11</i></p>
							<p><i>12</i><i>13</i><i>14</i><i>15</i><i>16</i><i>17</i></p>
							<p><i>18</i><i>19</i><i>20</i><i>21</i><i>22</i><i>23</i></p>
						</div>
					</td>
				</tr>
			</c:forEach>
		</table>
	</div>
	<div class="time_interval_bt">
		<input type="button" value="确 定" class="time_interval_button">
		<input type="reset" value="取 消" class="time_interval_reset">
	</div>
</div>
<!--投放-新建活动-第一步-弹框 e-->

</body>
</html>