
$(function(){

	//绑定事件
	bindCampAction();

	//活动: 批量设置
	initbatchSetWindow();
});

function bindCampAction(){

	//复制
	$("#duplicate_campaign_btn").click(function(){

		var chkt=0;
		$("input:checkbox[name='camp_check']").each(function() {
			if ($(this)[0].checked ==true) {
				chkt++;
			}
		});

		if( chkt==1 ){

			var camp_id = getCampIds();

			$.ajax({
				url: "duplicateCampaign.action?campaignId="+camp_id,
				type:"post",
				contentType:"application/json;charset=utf-8",
				success:function(result){
					if(result.success){
						location.href = result.url;
					}else{
						layer.msg("操作失败!", {icon: 5});
						return false;
					}
				}
			});
		}else{
			layer.alert('只能选择一个活动!', {icon: 6});
		}
	});

	//点击查询按钮
	$("#search_btn").click(function(){
		$("#formId").attr("action", "camp_list.action?pageNo=1");
		$("#formId").submit();
	});

	//导出报表功能
	$("#export_report_btn").click(function(){
		$("#formId").attr("action", "exportCampaign.action");
		$("#formId").submit();
		$("#formId").attr("action", "camp_list.action");
	});

	// 全选 反选
	$("#check_btn").click(function(){

		var flag = $("#check_btn")[0].checked;

		if(!flag ){		//全不选
			$("[name='camp_check']").prop("checked",false);
		}else{		//全选
			$("[name='camp_check']").prop("checked",true);
			$("input:checkbox[name='camp_check']").each(function() {
				if(typeof($(this).attr("disabled"))!="undefined"){
					$(this)[0].checked = false;
				}
			});
		}
	});


}

//批量设置事件处理
function initbatchSetWindow(){
	// 批量设置点击
	$("#batch_set").click(function(){
		var chkt=0;
		$("input:checkbox[name='camp_check']").each(function() {
			if ($(this)[0].checked ==true) {
				chkt++;
			}
		});

		if(chkt<1){
			layer.alert('请至少选择一个活动!', {icon: 6});
			return ;
		}else{
			/*
			var batchLayer = layer.open({
				type: 1,
				title:'批量设置',
				shadeClose: true,
				shade: 0.4,
				area: ['350px', '320px;'],
				content: $('#batch_set_window')
			});
			$("#batch_set_window").data("batchLayer", batchLayer);
			*/
		}
	});

	//批量设置保存按钮
	$("#batch_confirm").click(function(){

		var camp_ids = getCampIds();
		var daily_budget = $("#daily_budget").val();
		var price = $("#price").val();
		var priceReg = new RegExp("^[0-9]+(.[0-9]{0,2})?$");

		if(daily_budget == null || daily_budget == '' || ! priceReg.test(daily_budget)){
			$("#budget_tip").css('display','block');
			$("#daily_budget").focus();
			return false;
		}

		if(daily_budget<50){
			$("#budget_tip").css('display','block');
			$("#daily_budget").focus();
			return false;
		}

		if (price == null || price == '' || ! priceReg.test(price) ) {
			$("#price_tip").css('display','block');
			$("#price").focus();
			return false;
		}
		// 批量设置ajax
		$.ajax({
			url: "batchUpdateCampaigns.action?camp_ids="+camp_ids+"&dailyBudget="+daily_budget+"&price="+price,
			type:"post",
			contentType:"application/json;charset=utf-8",
			//请求json数据
			//data:JSON.stringify(obj),
			success:function(result){
				if(result.success){
					var batchLayer = $("#batch_set_window").data("batchLayer");
					layer.close(batchLayer);
					$("#formId").submit();
				}else{
					layer.msg("操作失败!");
					return false;
				}
			}
		});
	});

	//批量设置取消按钮
	$("#batch_cancel").click(function(){
		var batchLayer = $("#batch_set_window").data("batchLayer");
		layer.close(batchLayer);
		return false;
	});

}

function getCampIds(){
	var camp_id = "";
	$("input:checkbox[name='camp_check']").each(function(){
		if ($(this)[0].checked ==true) {
			camp_id += $(this).val()+",";
		}
	});
	if (camp_id.length > 1) {
		camp_id = camp_id.substring(0,camp_id.length - 1);
	}
	return  camp_id;
}

// 开启 暂停 终止 删除
function changeStatus(status){
	var chkt=0;
	$("input:checkbox[name='camp_check']").each(function() {
		if ($(this)[0].checked ==true) {
			chkt++;
		}
	});

	if(chkt>=1){

		var camp_id = getCampIds();

		//如果开启按钮
		if(status ==2){
			$.ajax({
				url: "isDelivery.action?camp_ids=" + camp_id,
				type: "post",
				contentType: "application/json;charset=utf-8",
				success: function (result) {
					var content = '';
					if (result.success) {
						content = '你确定执行此操作么？';
					} else {
                        content = result.message;
                        if(result.fail){
                            layer.alert(content, {icon: 6});
					        return false;
                        }
					}
					layer.open({
						content: content,
						time: 0, //不自动关闭
						btn: ['确定', '取消'],
						yes: function (index) {
							layer.close(index);
							$.ajax({
								url: "changeStatus.action?camp_ids=" + camp_id + "&code=" + status,
								type: "post",
								contentType: "application/json;charset=utf-8",
								//请求json数据
								//data:JSON.stringify(obj),
								success: function (result) {
									if (result.success) {
										$("#formId").submit();
									} else {
										layer.alert(result.message, {icon: 6});
										return false;
									}
								}
							}); // end ajax
						} // end yes
					}); // end layer
				}
			});
		}else{
			layer.open({
				content: '你确定执行此操作么？',
				time: 0, //不自动关闭
				btn: ['确定', '取消'],
				yes: function (index) {
					$.ajax({
						url: "changeStatus.action?camp_ids=" + camp_id + "&code=" + status,
						type: "post",
						contentType: "application/json;charset=utf-8",
						//请求json数据
						//data:JSON.stringify(obj),
						success: function (result) {
							layer.close(index);
							if (result.success) {
								// location.href = result.url;
								$("#formId").submit();
							} else {
								layer.alert(result.message, {icon: 6});
								return false;
							}
						}
					});
				}
			});
		}
	}else{
		layer.alert("请至少选择一个活动!", {icon: 6});
	}

}

function deleteCampaign(){
	var chkt=0;
	$("input:checkbox[name='camp_check']").each(function() {
		if ($(this)[0].checked ==true) {
			chkt++;
		}
	});

	if(chkt>=1){
		var camp_id = getCampIds();
		layer.open({
			content: '你确定执行此操作么？',
			time: 0, //不自动关闭
			btn: ['确定', '取消'],
			yes: function (index) {
				layer.close(index);
				$.ajax({
					url: "deleteCampaign.action?camp_ids=" + camp_id,
					type: "post",
					contentType: "application/json;charset=utf-8",
					success: function (result) {
						if (result.success) {
							// location.href = result.url;
							$("#formId").submit();
						} else {
							layer.msg("操作失败!");
							return false;
						}
					}
				});
			}
		});
	}else{
		layer.alert("请至少选择一个活动!", {icon: 6});
	}
}