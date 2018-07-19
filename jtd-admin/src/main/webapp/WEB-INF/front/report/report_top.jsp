<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/commonjsp/tag.jsp"%>

<%
	String param = request.getParameter("param");
	if(param==null){
		param = "time" ;
	}
%>
<!-- 选择时间控件 -->
<script src="${baseurl}js/front/date_layer.js"></script>

<script type="text/javascript">
	$(function(){
			
		$("#form_date").dateLayer("startDate","endDate","${params.startDate}","${params.endDate}","report");
		
		//查询按钮
		$("#subbtn").click(function(){
			$("#form1").submit();
		});
		
		//打开指标设置窗口
		$(".yen_details_bt span").click(function(){
			layer.open({
				type: 1,
		        title:'指标设置',
		        shadeClose: true,
		        shade: 0.4,
		        area: ['560px'],
		        content: $('#layer_index_set'),
			});
			
		});
		
		//设置自定义列
		$("#indexSetting #columnSetting_btn").click(function(){
			if($("#indexSetting :checkbox:checked").length == 0){
				layer.msg("请至少选择一项指标",{icon:0,time:1500});
				return ;
			}
			//清除自定义排序，使页面恢复为按默认字段排序
			$("#orderBy").val("clear");
			$("#form1").submit();
			
		});
		
		var orderByFlag = "${params.orderBy}";
		var descFlag = "${params.desc}";
		//显示自定义排序图标
		$("img.order").attr("src","${baseurl }images/front/order.png");
		if(orderByFlag != ""){
			if(descFlag == "desc"){
				desc(orderByFlag);
			}
			else{
				asc(orderByFlag);
			}
		}
		
	});

	/*
	按某个字段排序，每个字段开始默认为降序，点击同个字段可切换升/降序
	*/
	function orderBy(flag){
		if(flag != ""){
			if($("#orderBy").val() != flag){
				
				$("#orderBy").val(flag);
				$("#desc").val("desc");
			}
			else{
				if($("#desc").val() == "desc"){
					asc(flag);
				}
				else{
					desc(flag);
				}
			}
		}
		
		$("#form1").submit();
	}
	/*
	控制某一个字段显示为降序	
	*/
	function desc(flag){
		$("#desc").val("desc");
		$("#" + flag + "_desc").attr("src","${baseurl}images/front/order_desc.png");
	}
	/*
	控制某一个字段显示为升序	
	*/
	function asc(flag){
		$("#desc").val("asc");
		$("#" + flag + "_desc").attr("src","${baseurl}images/front/order_asc.png");
	}
	
	//单选框选中效果
	$(function(){
		$('.data_table_con .radio-style').click(function () {
	        $(this).addClass('active');
	        $(this).siblings().removeClass('active');
	    })
	});
	
</script>

<style type="text/css">
	th { cursor:pointer; }
</style>

	<!-- 记录分页的自定义排序 -->
    <input type="hidden" name="orderBy" id="orderBy" value="${params.orderBy}">
    <!-- 是否为降序（从大到小)，默认为true-->
    <input type="hidden" name="desc" id="desc" value="${params.desc}">

  <!-- 条件查询 -->
  <div class="form-horizontal clearfix">
      <div class="select-wrap clearfix">
          <label class="select-label">活动类型：</label>
          <select class="select-box" name="campaignType">
              <option value="-1">全部</option>
              <option value="0" <c:if test="${params.campaignType==0}">selected</c:if>>pc广告</option>
              <option value="1" <c:if test="${params.campaignType==1}">selected</c:if>>手机广告</option>
          </select>
      </div>
      <div class="select-wrap clearfix">
          <label class="select-label">活动状态：</label>
          <select class="select-box" name="campaignStatus">
              <option value="-1">全部</option>
              <option value="3" <c:if test="${params.campaignStatus==3}">selected</c:if>>投放中</option>
              <option value="4" <c:if test="${params.campaignStatus==4}">selected</c:if>>已暂停</option>
              <option value="5" <c:if test="${params.campaignStatus==5}">selected</c:if>>已下线</option>
              <option value="6" <c:if test="${params.campaignStatus==6}">selected</c:if>>已结束</option>
              <option value="7" <c:if test="${params.campaignStatus==7}">selected</c:if>>已删除</option>
          </select>
      </div>
      <div class="select-wrap clearfix">
          <label class="select-label">活动名称：</label>
          <input type="text" name="campaignName" value="${params.campaignName }" class="input_text">
      </div>
      <div class="select-wrap clearfix">
          <label class="select-label">统计周期：</label>
          <input type="text" class="yen_date" id="form_date">
      </div>
      <div class="f-r clearfix">
          <span class="btn btn-search btn-purple" id="subbtn">查询</span>
      </div>
  </div>

  <!-- 展示数据 -->
  <div class="clearfix date_six">
      <ul>
          <li>
              <span><i></i><fmt:formatNumber value="${sumSixMap.pv_sum }" pattern="#,###"/></span>
              <p>展现数</p>
          </li>
          <li>
              <span><i></i><fmt:formatNumber value="${sumSixMap.click_sum }" pattern="#,###"/></span>
              <p>点击数</p>
          </li>
          <li>
              <span><i></i><fmt:formatNumber value="${sumSixMap.click_rate_sum }" type="number" pattern="0.##" />%</span>
              <p>点击率</p>
          </li>
          <li>
              <span><i></i><fmt:formatNumber value="${sumSixMap.cpm_sum_yuan }" type="currency" /></span>
              <p>投放CPM单价</p>
          </li>
          <li>
              <span><i></i><fmt:formatNumber value="${sumSixMap.cpc_sum_yuan }" type="currency" /></span>
              <p>投放CPC单价</p>
          </li>
          <li>
              <span><i></i><fmt:formatNumber value="${sumSixMap.expend_sum_yuan }" type="currency" /></span>
              <p>消耗</p>
          </li>
      </ul>
  </div>
  
  <!-- 二级连接 -->
  <div class="second-links-wrap">
      <a class="s-links <% if(param.equals("time")){%>active<%}%>" href="${pageContext.request.contextPath}/front/report/time.action">时间报表</a>
      <a class="s-links <% if(param.equals("campType")){%>active<%}%>" href="${pageContext.request.contextPath}/front/report/campType.action">活动类型报表</a>
      <a class="s-links <% if(param.equals("camp")){%>active<%}%>" href="${pageContext.request.contextPath}/front/report/camp.action">活动报表</a>
      <a class="s-links <% if(param.equals("creative")){%>active<%}%>" href="${pageContext.request.contextPath}/front/report/creative.action">创意报表</a>
      <a class="s-links <% if(param.equals("city")){%>active<%}%>" href="${pageContext.request.contextPath}/front/report/city.action">地域报表</a>
      <a class="s-links <% if(param.equals("hour")){%>active<%}%>" href="${pageContext.request.contextPath}/front/report/hour.action">时段报表</a>
  </div>
  
  <!-- 指标设置div -->
  <section class="modal fade" id="indexSetting" tabindex="-1" role="dialog" aria-labelledby="indexSetting"
         aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" id="indexSetting1">
                    指标设置
                </h4>
            </div>
            <div class="modal-body">
                <div class="index_set_con">
                    <label>
                        <input type="checkbox" name="columnSetting" value="pv_sum" 
                        <c:if test="${ not empty columnSettingMap.pv_sum }">checked</c:if>>展现量
                    </label>
                    <label>
                        <input type="checkbox" name="columnSetting" value="uv_sum" 
                        <c:if test="${ not empty columnSettingMap.uv_sum }">checked</c:if>>UV
                    </label>
                    <label>
                        <input type="checkbox" name="columnSetting" value="click_sum" 
                        <c:if test="${ not empty columnSettingMap.click_sum }">checked</c:if>>点击量
                    </label>
                    <label>
                        <input type="checkbox" name="columnSetting" value="click_rate_sum" 
                        <c:if test="${ not empty columnSettingMap.click_rate_sum }">checked</c:if>>点击率
                    </label>
                    <label>
                        <input type="checkbox" name="columnSetting" value="cpm_sum_yuan" 
                        <c:if test="${ not empty columnSettingMap.cpm_sum_yuan }">checked</c:if>>投放CPM
                    </label>
                    <label>
                        <input type="checkbox" name="columnSetting" value="cpc_sum_yuan" 
                        <c:if test="${ not empty columnSettingMap.cpc_sum_yuan }">checked</c:if>>投放CPC
                    </label>
                    <label>
                        <input type="checkbox" name="columnSetting" value="expend_sum_yuan" 
                        <c:if test="${ not empty columnSettingMap.expend_sum_yuan }">checked</c:if>>投放花费
                    </label>
                </div>
                <div class="clearfix layer_two_bt">
                    <span class="btn btn-small btn-main" id="columnSetting_btn">确定</span>
                </div>
            </div>
        </div>
    </div>
</section>