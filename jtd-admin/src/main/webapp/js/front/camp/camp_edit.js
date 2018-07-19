$(function() {

	// 活动类型和广告形式
	binnCampType();

	// 定义页面开始时间和结束时间
	bindDateTimePage();

	//自定义投放时段弹出层页面
	// bindCustomPage();
	setTimeout(	bindCustomPage(), 1000);

	//保存活动-基本设置
	bindSaveCamp();

});

// 活动类型和广告形式
function binnCampType(){

    var editStepStatus = $("#editStepStatus").val();

    if(editStepStatus == "3"){
        $("[name='campaignType']").attr("disabled","disabled");
        $("[name='adType']").attr("disabled","disabled");
    }

	var campType = $("#campaignType_data").val();
    $("#adTypeSpan .acitve_radio").hide();
	if (campType == "0") {
		$("#adTypeSpan .acitve_radio").filter("[data-value=0],[data-value=1]").show();
	} else if (campType == "1") {
		$("#adTypeSpan .acitve_radio").filter("[data-value=4],[data-value=5],[data-value=6],[data-value=7],[data-value=8]").show();
	}else if(campType=="2"){
		$("#adTypeSpan .acitve_radio").filter("[data-value=9],[data-value=10]").show();
	}else {
        $("#campaignType_0").prop("checked", true);
        $("#adType_0").prop("checked", true);
        $("#adTypeSpan .acitve_radio").filter("[data-value=0],[data-value=1]").show();
    }

	// $("input[name='campaignType']")
	$("input[name='campaignType']").click(function(){
		campType = $(this).val();
        $("#adTypeSpan .acitve_radio").hide();
		if (campType == "0") {
            $("#adType_0").prop("checked", true);
			$("#adTypeSpan .acitve_radio").filter("[data-value=0],[data-value=1]").show();
		} else if (campType == "1") {
            $("#adType_4").prop("checked", true);
			$("#adTypeSpan .acitve_radio").filter("[data-value=4],[data-value=5],[data-value=6],[data-value=7],[data-value=8]").show();
		}else if(campType=="2"){
            $("#adType_9").prop("checked", true);
			$("#adTypeSpan .acitve_radio").filter("[data-value=9],[data-value=10]").show();
		}
	});
}


// 定义页面开始时间和结束时间
function bindDateTimePage(){
	// 开始时间
	var start = {
		elem : '#start_time',
		format : 'YYYY-MM-DD',
		istoday : false,
		min : laydate.now(), // 设定最小日期为当前日期
		max : '2099-06-16 23:59:59', // 最大日期
		start : laydate.now(),
		choose : function(datas) {
			end.min = datas; // 开始日选好后，重置结束日的最小日期
			end.start = datas // 将结束日的初始值设定为开始日
		}
	};

	// 结束时间
	var end = {
		elem : '#end_time',
		format : 'YYYY-MM-DD',
		min : laydate.now(),
		max : '2099-06-16 23:59:59',
		istime : true,
		istoday : false,
		choose : function(datas) {
			start.max = datas; // 结束日选好后，重置开始日的最大日期
		}
	};

	// laydate.skin("huanglv");
	laydate(start);
	laydate(end);
}

// 自定义时间--选中取消小时按钮的状态
function changeHourSelectedStatus(time, toggle) {
	if (toggle != undefined) {
		if (toggle) {
			if (!$(time).hasClass("active")) {
				$(time).addClass("active");
			}
		} else {
			$(time).removeClass("active");
		}
	} else {
		if ($(time).hasClass("active")) {
			$(time).removeClass("active");
		} else {
			$(time).addClass("active");
		}
	}
};

//自定义投放时段弹出层页面
function bindCustomPage(){

	var weekhour = $("#week_hour").val();

	if(weekhour != null && weekhour!=""){
		weekhour = JSON.parse(weekhour);
		loadWeekhour(weekhour);// 初始化时间段
		loadWeekhourForDay(weekhour); //初始化选择时间段类型
	}else {
		setAllWeek(); //默认选择整周
		var weekhour_arr = [];
		$("#custom_selected_hour tr:gt(0)").each(function(kw, vw) {
			var h_arr = [];
			$(vw).find(".psel i").each(function(kh, vh) {
				h_arr.push($(vh).hasClass("active") ? 1 : 0);
			});
			weekhour_arr.push(h_arr);
		});
		$("#custom_date_btn").data("custom_selected_hour", weekhour_arr);// 把获取的时间数据存储在页面元素上
	}

	// 弹出时间段窗口
	$('#custom_date_btn').on('click', function(){
		var timeLayer = layer.open({
			type: 1,
			closeBtn: 0, //不显示关闭按钮
			shadeClose: true,
			shade: 0.8,
			area: ['910px', '580px;'],
			content: $('#custom_date_page'),
			success : function(layero, index) {
			},
			end : function() {
				$("#custom_date_page").css('display','none');
			}
		});
		$("#custom_date_btn").data("timeLayer", timeLayer);
	});

	//绑定点击事件
	bindTimeEvent(0);

	// 自定义时间段保存按钮
	$(".time_interval_button").click(function() {
		var timeLayer = $("#custom_date_btn").data("timeLayer");
		layer.close(timeLayer);
		var weekhour_arr = [];
		$("#custom_selected_hour tr:gt(0)").each(function(kw, vw) {
			var h_arr = [];
			$(vw).find(".psel i").each(function(kh, vh) {
				h_arr.push($(vh).hasClass("active") ? 1 : 0);
			});
			weekhour_arr.push(h_arr);
		});
		$("#custom_date_btn").data("custom_selected_hour", weekhour_arr);// 把获取的时间数据存储在页面元素上
		$("#custom_date_page").css('display','none');
		return false;
	});

	// 自定义时间段取消按钮
	$(".time_interval_reset").click(function() {
		var timeLayer = $("#custom_date_btn").data("timeLayer");
		layer.close(timeLayer);
		$("#custom_date_page").css('display','none');
		return false;
	});

}

//根据投放时间，获取时间类型
function loadWeekhourForDay(weekhour) {
	var weekHours = 0;
	var workDayHours = 0;
	var weekendHours = 0;

	for (var i = 0; i < weekhour.length; i++) {
		for (var j = 0; j < weekhour[i].length; j++) {
			weekHours += weekhour[i][j];
			if (i >= 0 && i < 5) {
				workDayHours += weekhour[i][j];
			}
			if (i >= 5 && i < 7) {
				weekendHours += weekhour[i][j];
			}
		}
	}
	// 根据投放时间段则选择周状态
	if (weekHours == 7 * 24) { //整周展示
		$("#allWeekDisplay").prop("checked", true);
		setAllWeek();
	} else if (workDayHours == 5 * 24 && weekendHours == 0) { //工作日全天展示
		$("#workDayDisplay").prop("checked", true);
		setWorkDay();
	} else if (weekendHours == 2 * 24 && workDayHours == 0) { //休息日全天展示
		$("#weekEndDisplay").prop("checked", true);
		setWeekend();
	}else if (weekHours == 0) { //全部清空
		$("#clearDayDisplay").prop("checked", true);
	} else {//自定义时段展示
		$("#customDateDisplay").prop("checked", true);
	}
}

//初始化时间段数据
function loadWeekhour(weekhour) {
	$("input[name='data_show']").prop("checked", false);
	$("#custom_selected_hour  tr:gt(0)").each(function(week, tr) {
		$(tr).find(".psel i").each(function(hour, li) {
			if (weekhour[week][hour] == 1) {
				$(li).addClass("active");
			} else {
				$(li).removeClass("active");
			}
		});
	});
}

//绑定点击事件
function bindTimeEvent(clickSpeedController){

	// 上面按钮选择事件
	$("input[name=data_show]").change(function() {
		var id = $(this).attr("id");
		if (id == "allWeekDisplay") {
			setAllWeek();
		} else if (id == "workDayDisplay") {
			setWorkDay();
		} else if (id == "weekEndDisplay") {
			setWeekend();
		} else if (id == "clearDayDisplay") {
			setClearDay();
		} else {

		}
	});

	// 单个点击选中取消事件
	$(".time_interval_table i").unbind();
	$(".time_interval_table i").click(function() {
		changeHourSelectedStatus(this);
		$("#customDateDisplay").prop("checked", true);
	});

	// 复选框选择事件
	$("input[type=checkbox]").click(function() {
		var id = $(this).parent().parent().attr("id");
		var toggle = $(this).filter(":checked").length == 1;
		$("#" + id + " .psel i").each(function(k, v) {
			changeHourSelectedStatus(v, toggle);
		});
		$("#customDateDisplay").prop("checked", true);
	});

	// 复制到整周
	$(".copyAllWeek").click(function() {
		if (clickSpeedController > 0) {
			return false;
		}
		clickSpeedController++;
		var id = $(this).parent().parent().attr("id");
		var org = $("#" + id + " .psel").parent().html();
		copyToggle(org, ".psel");

		setTimeout(function() {
			clickSpeedController = 0;
		}, 100);
	});

	// 复制到工作日
	$(".copyWorkDay").click(function() {
		if (clickSpeedController > 0) {
			return false;
		}
		clickSpeedController++;
		var id = $(this).parent().parent().attr("id");
		var org = $("#" + id + " .psel").parent().html();
		copyToggle(org, "#week_1 .psel");
		copyToggle(org, "#week_2 .psel");
		copyToggle(org, "#week_3 .psel");
		copyToggle(org, "#week_4 .psel");
		copyToggle(org, "#week_5 .psel");

		setTimeout(function() {
			clickSpeedController = 0;
		}, 1000);
	});
	// 复制到休息日
	$(".copyWorkEnd").click(function() {
		if (clickSpeedController > 0) {
			return false;
		}
		clickSpeedController++;
		var id = $(this).parent().parent().attr("id");
		var org = $("#" + id + " .psel").parent().html();
		copyToggle(org, "#week_6 .psel");
		copyToggle(org, "#week_7 .psel");

		setTimeout(function() {
			clickSpeedController = 0;
		}, 1000);
	});

}

//复制方法
function copyToggle(org, target) {
	$(target).replaceWith(org);
	$(".time_interval_table i").unbind();
	$(".time_interval_table i").click(function() {
		changeHourSelectedStatus(this);
		$("#customDateDisplay").prop("checked", true);
	});
	$("#customDateDisplay").prop("checked", true);
};

//设置整周全天展示
function setAllWeek() {
	$(".time_interval_table i").each(function(k, v) {
		changeHourSelectedStatus(v, true);
	});
	$("input[type=checkbox]").prop("checked", true);
};

//全部清空
function setClearDay() {
	$(".time_interval_table i").each(function(k, v) {
		changeHourSelectedStatus(v, false);
	});
	$("input[type=checkbox]").prop("checked", false);
};

//设置工作日全天展示
function setWorkDay() {
	$("#week_1  i").each(function(k, v) {
		changeHourSelectedStatus(v, true);
	});
	$("#week_2  i").each(function(k, v) {
		changeHourSelectedStatus(v, true);
	});
	$("#week_3  i").each(function(k, v) {
		changeHourSelectedStatus(v, true);
	});
	$("#week_4  i").each(function(k, v) {
		changeHourSelectedStatus(v, true);
	});
	$("#week_5  i").each(function(k, v) {
		changeHourSelectedStatus(v, true);
	});
	$("#week_6  i").each(function(k, v) {
		changeHourSelectedStatus(v, false);
	});
	$("#week_7  i").each(function(k, v) {
		changeHourSelectedStatus(v, false);
	});
	$("#Monday").prop("checked", true);
	$("#Tuesday").prop("checked", true);
	$("#Wednesday").prop("checked", true);
	$("#Thursday").prop("checked", true);
	$("#Friday").prop("checked", true);
	$("#Saturday").removeAttr("checked");
	$("#Sunday").removeAttr("checked");
};
//设置休息日全天展示
function setWeekend() {
	$("#week_1  i").each(function(k, v) {
		changeHourSelectedStatus(v, false);
	});
	$("#week_2  i").each(function(k, v) {
		changeHourSelectedStatus(v, false);
	});
	$("#week_3  i").each(function(k, v) {
		changeHourSelectedStatus(v, false);
	});
	$("#week_4  i").each(function(k, v) {
		changeHourSelectedStatus(v, false);
	});
	$("#week_5  i").each(function(k, v) {
		changeHourSelectedStatus(v, false);
	});
	$("#week_6  i").each(function(k, v) {
		changeHourSelectedStatus(v, true);
	});
	$("#week_7  i").each(function(k, v) {
		changeHourSelectedStatus(v, true);
	});
	$("#Monday").removeAttr("checked");
	$("#Tuesday").removeAttr("checked");
	$("#Wednesday").removeAttr("checked");
	$("#Thursday").removeAttr("checked");
	$("#Friday").removeAttr("checked");
	$("#Saturday").prop("checked", true);
	$("#Sunday").prop("checked", true);
};

//保存活动-基本设置
function bindSaveCamp(){

	$("#save_camp_btn").click( function() {

		var obj = new Object();
		obj = bindSaveCampBaseData();
		if(!obj){
			return false;
		}
		// 开始时间
		var startTime = $("#start_time").val();
		if (startTime == null || startTime == '') {
			$("#start_time_tip").css('display','block');
			$("#start_time").focus();
			return false;
		}
		// 结束时间
		var endTime = $("#end_time").val();

		layer.load(2);  //加载层
		setTimeout(function(){
			layer.closeAll('loading');
		}, 1300);

		$.ajax({
			url : "saveCamp.action?startTime="
			+ startTime
			+ "&endTime="
			+ endTime
			+ "&step=1",
			type : "post",
			contentType : "application/json;charset=utf-8",
			// 请求json数据
			data : JSON.stringify(obj),
			success : function(result) {
				layer.closeAll('loading'); //关闭加载层
				if (result.success) {
					location.href = result.url + "?id="
						+ result.id + "&step="
						+ result.step;
				} else {
					layer.msg("操作失败!", {icon: 5});
					return false;
				}
			}
		});
	});

	// 确认并返回
	$("#save_camp_btn_back").click( function() {

		var obj = new Object();
		obj = bindSaveCampBaseData();

		if(!obj){
			return false;
		}
		// 开始时间
		var startTime = $("#start_time").val();
		if (startTime == null || startTime == '') {
			$("#start_time_tip").css('display','block');
			$("#start_time").focus();
			return false;
		}
		// 结束时间
		var endTime = $("#end_time").val();

		layer.load(2);  //加载层
		setTimeout(function(){
			layer.closeAll('loading');
		}, 1300);

		$.ajax({
			url : "saveCamp.action?startTime="
			+ startTime
			+ "&endTime="
			+ endTime
			+ "&step=3",
			type : "post",
			contentType : "application/json;charset=utf-8",
			// 请求json数据
			data : JSON.stringify(obj),
			success : function(result) {
				layer.closeAll('loading');
				if (result.success) {
					location.href = result.url;
				} else {
					layer.msg("操作失败!", {icon: 5});
					return false;
				}
			}
		});

	});
}

function bindSaveCampBaseData() {

	var obj = new Object();
	var id = $("#id").val();
	if (id == null || id == '') {

	} else {
		obj.id = id;
	}

	obj.partnerId = $("#partnerId").val();
	obj.campaignName = $("#campaignName").val();
	if (obj.campaignName == null || obj.campaignName == '') {
		$("#campaignName").focus();
		$("#campaignName_tip").css('display','block');
		return false;
	}else if(obj.campaignName.length>50){
		$("#campaignName").focus();
		layer.alert("活动名称长度不能超过50个字符!");
		return false;
	}

	obj.campaignType = $("input[name='campaignType']:checked").val();
	
	obj.adType = $("input[name='adType']:checked").val();
	obj.budgetctrltype = $("input[name='budgetctrltype']:checked").val();

	// 每日预算
	var daily_budget_flag = $("input[name='daily_budget_flag']:checked").val();
	if (daily_budget_flag == 1) {
		var dbudget= $("#dailyBudget").val();
		var priceReg = new RegExp("^[0-9]+(.[0-9]{0,2})?$");
		if (dbudget == null || dbudget == '' || ! priceReg.test(dbudget) || dbudget<50) {
			layer.alert("日预算不可为空，且最低50元起。 或日预算格式非法，请检查设置。");
			return false;
		}
		obj.dailyBudget = dbudget*100;
	}

	// 投放时间段
	var week_hour_flag = $("input[name='week_hour_flag']:checked").val();
	if (week_hour_flag == 1) {
		var week_hour = $("#custom_date_btn").data("custom_selected_hour");

		var weekHours = 0;
		if(week_hour != undefined) {
			for (var i = 0; i < week_hour.length; i++) {
				for (var j = 0; j < week_hour[i].length; j++) {
					weekHours += week_hour[i][j];
				}
			}
			if(weekHours==0){
				layer.alert("投放时间段不能为空!");
				return false;
			}
		}else{
			week_hour = $("#week_hour").val();
			week_hour = JSON.parse(week_hour);
		}

		if(week_hour=="" || week_hour==null){
			layer.alert("投放时间段不能为空!");
			return false;
		}
		week_hour = JSON.stringify(week_hour);
		// week_hour = week_hour.replace(/\"/g, "");
		obj.weekHour = week_hour;
	}
	//alert(JSON.stringify(obj));
	return obj;
}
