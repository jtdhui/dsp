$(function() {
	
	$(".handle-view input[type=radio]").click(function () {
        $(this).parent().addClass('active');
        $(this).parent().parent().siblings().find('.radio-style').removeClass('active');
    });

	//根据活动类型初始化投放策略
	initCampPage();

	// 初始化弹窗
	bindInitWindow();

	// 保存活动-下一步提交--投放策略
	bindSaveCampTwo();

});

//根据活动类型初始化投放策略
function initCampPage(){

    // 活动类型
	var campaignType = $("#campaignType").val();
    // 广告类型
    var adType = $("#adType").val();

	// 人群定向初始化
	$("#crowd_ul_key li").hide();
	if(campaignType ==1){  //活动类型是APP
		$("#peopleTab").show();
		$("#interestTab").show();
	}else{ //活动类型是PC或WAP
		$("#peopleTab").show();
		$("#interestTab").show();
		$("#personalityTab").show();
		$("#guestTab").show();
	}

	//媒体定向初始化(PC,WAP)
	$("#device").hide();
	$("#deviceType-Container").hide();
	$("#deviceBrand-Container").hide();
	$("#networkType-Container").hide();
	$("#operateType-Container").hide();

    $("#app").hide();
    $("#media").hide();

    if (campaignType == 0 && adType == 0) {// 如果活动类型是PC广告，广告形式是Banner，则隐藏应用定向和设备定向
        $("#media").show();
    }
    if (campaignType == 0 && adType == 1) {
        $("#media").show();
        $("#app").show();
    }
    if (campaignType == 1 && (adType == 4 || adType == 5)) {// 如果活动类型是移动广告，广告形式是Banner
        // In APP或者插屏全屏 In
        // APP，则隐藏媒体定向
        $("#app").show();
        $("#device").show();
    }
    if (campaignType == 1 && adType == 6) {// 如果活动类型是移动广告，广告形式是wap，则隐藏应用定向
        $("#media").show();
        $("#device").show();
    }

	//设备定向初始化
    initDeviceData();

	//应用定向初始化
    // initAppDirect(osType);

    //绑定事件
    bindAppIncident();

    //绑定应用包事件
    bindAppPacketIncident();

}

//设备初始化
function initDeviceData(){

	//操作系统
	initOsType();

	//设备
	initDeviceType();

	//设备品牌
	initDeviceBrand();

	//网络类型
	initNetworkType();

	//网络运营商
	initOperateType();

}

//操作系统
function initOsType(){
	var osType = $("#osType_data").val();
    var url = "../app/list.action";
    $("#form1").attr("action", url+"?osType="+osType);
    initAppDirect(osType);

	if(osType == "ios"){
		$("#deviceBrand-Container div.devicetype-checkbox").each(function() {
			if ($(this).find("input[name=brand]").val() == "apple") {
				$(this).show();
			} else {
				$(this).hide();
			}
		});
	}else if(osType == "android"){
		$("#deviceBrand-Container div.devicetype-checkbox").each(function() {
			if ($(this).find("input[name=brand]").val() != "apple") {
				$(this).show();
			} else {
				$(this).hide();
			}
		});
	}else {
        $("#deviceBrand-Container div.devicetype-checkbox").each(function() {
            $(this).show();
        });
    }


	$("#osType_ANDROID").click(function() {
		//取消手机品牌的自定义选择
        $("input[name=brand]").prop("checked",false);
        //取消手机品牌的自定义选择中的苹果
		$("#deviceBrand-Container div.devicetype-checkbox").each(function() {
			if ($(this).find("input[name=brand]").val() != "apple") {
				$(this).show();
			} else {
				$(this).hide();
			}
		});

        $("#form1").attr("action", url+"?osType=android");

        //如果应用定向当前为按应用包投放，则清空应用定向的选择，重新恢复到不限 
        var appSelectValue = $("#appSelectValue").val(); 
        var appData = $("#appData").val();
        initAppDirect("android");
        if(appSelectValue == "2"){

            $("#appSelect_0").prop("checked",true);
            $("#appSelectValue").val("0");
            $("#app_name_del").empty();
            $("#app_name").val(appData);
        }

	});

	$("#osType_IOS").click(function() {
		//取消手机品牌的自定义选择
        $("input[name=brand]").prop("checked",false);
        //手机品牌的自定义选择中只留苹果
		$("#deviceBrand-Container div.devicetype-checkbox").each(function() {
			if ($(this).find("input[name=brand]").val() == "apple") {
				$(this).show();
			} else {
				$(this).hide();
			}
		});

        $("#form1").attr("action", url+"?osType=ios");

        //如果应用定向当前为按应用包投放，则清空应用定向的选择，重新恢复到不限 
        var appSelectValue = $("#appSelectValue").val(); 
        var appData = $("#appData").val();

        initAppDirect("ios");

        if(appSelectValue == "2"){

            $("#appSelect_0").prop("checked",true);
            $("#appSelectValue").val("0");
            $("#app_name_del").empty();
            $("#app_name").val(appData);
        }
	});

    $("#osType_ALL").click(function() {
        $("input[name=brand]").prop("checked",false);
        $("#deviceBrand-Container div.devicetype-checkbox").each(function() {
            $(this).show();
        });

        $("#form1").attr("action", url+"?osType=");

        initAppDirect("");
    });

}

//设备
function initDeviceType() {
	var deviceType = $("#deviceType_data").val();
	if(deviceType){
		
		$("#deviceTypeSelect_1").prop("checked", true);
		$("#deviceTypeSelect_1").parent().addClass("active");
		$("#deviceType-Container").show();

		deviceType = deviceType.split(",");
		for (var i = 0; i < deviceType.length; i++) {
			$("#deviceType_" + deviceType[i] + "").prop("checked", true);
		}
	}else {
		$("#deviceTypeSelect_0").prop("checked", true);
		$("#deviceTypeSelect_0").parent().addClass("active");
	}

	//不限点击事件
	$("#deviceTypeSelect_0").click(function () {
		$("#deviceType-Container").hide();
	});

	//自定义点击事件
	$("#deviceTypeSelect_1").click(function () {
		$("#deviceType-Container").show();
	});
}

//设备品牌
function initDeviceBrand() {
	// 设备品牌
	var brand = $("#brand_data").val();
	
	if (brand) {
		$("#brandSelect_1").prop("checked", true);
		$("#brandSelect_1").parent().addClass("active");
		
		brand = brand.split(",");
		for (var i = 0; i < brand.length; i++) {
			$("#brand_" + brand[i] + "").prop("checked", true);
		}
		$("#deviceBrand-Container").show();
	}else{
		//默认选中
		$("#brandSelect_0").prop("checked", true);
		$("#brandSelect_0").parent().addClass("active");
	}

	//不限点击事件
	$("#brandSelect_0").click(function () {
		$("#deviceBrand-Container").hide();
	});

	//自定义点击事件
	$("#brandSelect_1").click(function () {
		$("#deviceBrand-Container").show();
	});
}

//网络类型
function initNetworkType() {
	// 网络类型
	var data_val = $("#networkType_data").val();
	if (data_val) {
		$("#networkTypeSelect_1").prop("checked", true);
		$("#networkTypeSelect_1").parent().addClass("active");
		
		data_val = data_val.split(",");
		for (var i = 0; i < data_val.length; i++) {
			$("#networkType_" + data_val[i] + "").prop("checked", true);
		}
		$("#networkType-Container").show();
	}
	else{
		$("#networkTypeSelect_0").prop("checked", true);
		$("#networkTypeSelect_0").parent().addClass("active");
	}

	//不限点击事件
	$("#networkTypeSelect_0").click(function () {
		$("#networkType-Container").hide();
	});

	//自定义点击事件
	$("#networkTypeSelect_1").click(function () {
		$("#networkType-Container").show();
	});
}

//网络运营商
function initOperateType() {
	// 网络运营商
	var data_val = $("#operateType_data").val();
	if (data_val) {
		$("#operateTypeSelect_1").prop("checked", true);
		$("#operateTypeSelect_1").parent().addClass("active");
		
		data_val = data_val.split(",");
		for (var i = 0; i < data_val.length; i++) {
			$("#operateType_" + data_val[i] + "").prop("checked", true);
		}
		$("#operateType-Container").show();
	}
	else{
		$("#operateTypeSelect_0").prop("checked", true);
		$("#operateTypeSelect_0").parent().addClass("active");
	}

	//不限点击事件
	$("#operateTypeSelect_0").click(function () {
		$("#operateType-Container").hide();
	});

	//自定义点击事件
	$("#operateTypeSelect_1").click(function () {
		$("#operateType-Container").show();
	});
}


/** 应用定向开始 */
function initAppDirect(osType) {

    var appSelectValue = $("#appData_radio").val();
    if(appSelectValue == null || appSelectValue == ""){
        appSelectValue = "0";
    }
    var appData = $("#appData").val();
    // appData = appData.substr(1,appData.length-1);
    // var app_name_arr = appData.split(","); //选中的数据
    if(appData == "" || appData == null){
        appData = "0";
    }else {
        appData = $.parseJSON( appData );
    }

    if(appSelectValue == 1){
        $("#appSelect_1").prop("checked",true);
        initAppDirectData(appData);
        initAppPacket(osType,"0");
    }else if(appSelectValue == 2){
        $("#appSelect_2").prop("checked",true);
        $("#app_name").val(JSON.stringify(appData)); //选中的数据
        initAppDirectData("0");
        initAppPacket(osType,JSON.stringify(appData));
    }else{
        $("#appSelect_0").prop("checked",true);
        initAppDirectData("0");
        initAppPacket(osType,"0");
    }

}

//应用定向初始化
function initAppDirectData(appTypeList){

    //遍历应用类型
    $("#app_type_one").empty();
    var content ="";

    // each begin
    $.each(_campaignDicData.appTypes, function(index, obj) {

        // 拼装树
        content +="<li>";
        content +="	<span id=\"app_"+obj.id+"\" class=\"toggle-button i_open\" onclick=\"spread_app_action("+obj.id+")\"></span>";
        content +="	<div class=\"type_list\">";
        content +="	<span class=\"checkbox_input\"><input type=\"checkbox\" name=\"app_father_"+obj.id+"\" onchange=\"app_checked_action("+obj.id+")\" value=\""+obj.id+"\"><label></label></span>";
        content +="	<span class=\"checkbox_label\">"+obj.name+"</span>";
        //是否包含下一级应用类型
        if(obj.hasOwnProperty("subAppTypes")){
            var sub_content ="";
            sub_content +="<ul  id=\"app_type_two_"+obj.id+"\">";

            $.each(obj.subAppTypes, function(index, sub_obj) {

                var flag = false;

                if(appTypeList !="0"){
                    $.each(appTypeList, function(index, data) {
                        if(data == sub_obj.id ){
                            flag = true;
                        }
                    });
                };

                sub_content +="<li>";
                sub_content +="	<span class=\"checkbox_input\">";
                if(flag){
                    sub_content +="	<input type=\"checkbox\" checked=\"checked\" id=\"check1\" onchange=\"apptype_checked_action("+obj.id+")\"   name=\"apptype_check\"  value=\""+sub_obj.id+"\">"
                }else{
                    sub_content +="	<input type=\"checkbox\" id=\"check1\" onchange=\"apptype_checked_action("+obj.id+")\"   name=\"apptype_check\"  value=\""+sub_obj.id+"\">"
                }
                sub_content +="		<label for=\"check1\"></label></span>";
                sub_content +="	<span class=\"checkbox_label\">"+sub_obj.name+"</span>";
                sub_content +="</li>";
            });

            sub_content +="</ul>";
            // 加载子集数据
            content += sub_content;
        };
        content +="	</div>";
        content +="</li>";
    });  // end each

    $("#app_type_one").append(content);

    //判断子集是否有数据,如果有则父级为选中状态
    $.each(_campaignDicData.appTypes, function(index, obj) {
        //是否包含下一级应用类型
        var father_flag = false;
        if(obj.hasOwnProperty("subAppTypes")){
            $.each(obj.subAppTypes, function(index, sub_obj) {
                if(appTypeList !="0"){
                    $.each(appTypeList, function(index, data) {
                        if(data == sub_obj.id ){
                            father_flag = true;
                            return false;
                        }
                    });
                };
                if(father_flag){
                    return false;
                }
            });
        };
        if(father_flag){
            var father_select_id = "input:checkbox[name='app_father_"+obj.id+"']";
            $(father_select_id).prop("checked",true);
        }
    });

}

//绑定事件
function bindAppIncident() {

    //不限点击
    $("#appSelect_0").click(function () {
        $("#appSelectValue").val("0");
    });
    
    // 打开窗口
    $("#appSelect_1").click(function(){

        $("#appSelectValue").val("1");

        var appTypeLayer = layer.open({
            type: 1,
            title:'',
            closeBtn: 0, //不显示关闭按钮
            shadeClose: true,
            shade: 0.4,
            area: ['510px', 'auto'],
            content: $('#app_type_window')
        });

        $("#app_type_window").data("appTypeLayer", appTypeLayer);
    });

    // 应用类型保存按钮
    $("#app_type_conform").click(function() {
        var appTypeLayer = $("#app_type_window").data("appTypeLayer");
        layer.close(appTypeLayer);
        var app_type_arr = [];

        $("input:checkbox[name='apptype_check']").each(function(){
            if ($(this)[0].checked ==true) {
                app_type_arr.push($(this).val());
            }
        });

        $("#app_type_flag").val("1");
        $("#app_type_data").val(app_type_arr);

        return false;
    });

    // 应用类型取消按钮
    $("#app_type_canncel").click(function() {
        var appTypeLayer = $("#app_type_window").data("appTypeLayer");
        var flag = $("#app_type_flag").val();
        layer.close(appTypeLayer);
        return false;
    });

    //全选
    $("#app_type_all").click(function () {
        $("#app_type_one :checkbox").each(function(i, obj) {
            $(obj).prop("checked", true);
        });
    });

    //取消
    $("#app_type_empty").click(function () {
        $("#app_type_one :checkbox").each(function(i, obj) {
            $(obj).prop("checked", false);
        });
    });
}

// 上级复选框选中未选状态的change事件
function app_checked_action(id){
    //spread_action(id);
    var father_select_id = "input:checkbox[name='app_father_"+id+"']";
    var father_status = $(father_select_id).prop("checked");
    var select_id ="#app_type_two_"+id +" input:checkbox[name='apptype_check']";
    $(select_id).prop("checked",father_status);

}

// 下级复选框点击关联事件
function apptype_checked_action(id){
    var father_select_id = "input:checkbox[name='app_father_"+id+"']";
    var select_id ="#app_type_two_"+id +" input:checkbox[name='apptype_check']";

    var chkt=0;
    $(select_id).each(function() {
        if ($(this)[0].checked ==true) {
            chkt++;
        }
    });
    if(chkt>0){
        $(father_select_id).prop("checked",true);
    }else{
        $(father_select_id).prop("checked",false);
    }

}

// 应用类型: 展开/收缩功能
function spread_app_action(id){
    if($("#app_"+id).hasClass("i_open")){
        $("#app_"+id).removeClass("i_open");
        $("#app_type_two_"+id).css('display','block');

    }else{
        $("#app_"+id).addClass("i_open");
        $("#app_type_two_"+id).css('display','none');
    }
}

//初始化应用包
function initAppPacket(osType,appData){

    //应用类型
    var app_type_option = '<option value="" >全部</option>';
    $.each(_campaignDicData.appTypes, function(index, obj) {
        app_type_option +='<option value="'+obj.id+'">'+obj.name+'</option>';
        //是否包含下一级媒体类型
        if(obj.hasOwnProperty("subAppTypes")){
            $.each(obj.subAppTypes, function(index, sub_obj) {
                app_type_option +='<option value="'+sub_obj.id+'">-->'+sub_obj.name+'</option>';
            });
        };
    });
    $("#app_name_select").append(app_type_option);

    // 窗口提交不刷新
    $("#form1").bind('submit', function(){
        ajaxSubmit(this, function(data){
            layer.closeAll('loading');
            if(data.success){
                var app_name_selected = $("#app_name").val();
                // app_name_selected = app_name_selected.split(",");
                if(app_name_selected == null || app_name_selected == ""){
                    app_name_selected = "0";
                }
                appPacketList(data.page,app_name_selected);
            }
        });
        return false;
    });

    //查询
    $("#app_search_btn").click(function () {
        layer.load(2);  //加载层
        var url = $("#form1").attr("action");
        $("#form1").attr("action", url+"&pageNo=1");
        $("#form1").submit();
        $("#form1").attr("action", url);
    });
    //加载域名
    loadAppList(osType,appData);
}

function loadAppList(osType,appData) {
    $.ajax({
        url : "../app/list.action?osType="+osType,
        type : "post",
        contentType : "application/json;charset=utf-8",
        dataType : "json",
        success : function(data) {
            if(data.success){
                // 跳转页面时,要保留已选的数据
                var app_save_flag = $("#app_save_flag").val();
                if(app_save_flag =="0") {
                    appPacketList(data.page,appData );
                }else{
                    var app_name_selected = $("#app_name").val();
                    // app_name_selected = $.parseJSON( app_name_selected );
                    if(app_name_selected == null || app_name_selected == ""){
                        app_name_selected = "0";
                    }
                    appPacketList(data.page,app_name_selected);
                }
            }
        }
    });
}

//绑定应用包事件
function bindAppPacketIncident(){
    // 域名窗口弹出
    $("#appSelect_2").click(function(){

        $("#appSelectValue").val("2");

        var appPacketLayer = layer.open({
            type: 1,
            title:'',
            closeBtn: 0, //不显示关闭按钮
            shadeClose: true,
            shade: 0.4,
            area: ['1070px', '500px;'],
            content: $('#app_name_window')
        });
        $("#app_name_window").data("appPacketLayer", appPacketLayer);
    });

    // 保存按钮
    $("#app_name_conform").click(function() {
        var appPacketLayer = $("#app_name_window").data("appPacketLayer");
        layer.close(appPacketLayer);
        // var app_name_arr = [];
        // $("#app_name_del span").each(function() {
        //     app_name_arr.push('"'+$(this).text()+'"');
        // });
        var app_name_arr = $("#app_name").val();
        $("#appData").val(app_name_arr);
        $("#app_save_flag").val("1");
        return false;
    });

    // 媒体域名取消按钮
    $("#app_name_canncel").click(function() {
        var appPacketLayer = $("#app_name_window").data("appPacketLayer");
        layer.close(appPacketLayer);
        return false;
    });

    //全部删除
    $("#app_name_del_all").click(function () {
        $("#app_name_del").empty();
        $("#app_name").val("");
        $("#app_name_table .w_td_operation").each(function(i, obj) {
            $(obj).removeClass("active");
        });
    });
}

//应用包列表初始化
function appPacketList(page,app_selected){
    $("#app_name_table").empty();
    $("#app_page_window").empty();

    var app_page_window = page.frontPageHtml1;
    $("#app_page_window").append(app_page_window);

    if(page.list.length>0){
        $("#app_name_del").empty();

        var liContent="";
        if(app_selected !="0") {
            app_selected = $.parseJSON(app_selected);
            $.each(app_selected, function (index, selObj) {
                // selObj = $.parseJSON( selObj );
                // pkName = pkName.replace("\"", "").replace("\"", "");
                if(selObj != null && selObj != ""){
                    liContent += '<li id="app_sel_' + selObj.id + '"><span>' + selObj.id+'_'+selObj.app_name+'_'+selObj.pk_name + '</span><i onclick="appAddToLeft('+selObj.id+',\''+selObj.pk_name+'\',\''+selObj.app_name+'\','+selObj.channel+',\''+selObj.app_id+'\')">×</i></li>';
                }
            });
        }

        var content ="";
        $.each(page.list, function(index, appPacket) {
            var flag = false;
            // appPacket.pkName = appPacket.pkName.replace("\"","").replace("\"","");
            if(app_selected !="0"){
                $.each(app_selected, function(index, selObj) {
                    // selObj = $.parseJSON( selObj );
                    // pkName = pkName.replace("\"","").replace("\"","");
                    if(selObj.id == appPacket.id){
                        flag = true;
                    }
                });
            };
            content += "<tr>";
            content += '	<td class="w_td_name elli" title="'+appPacket.id+'"> '+appPacket.id+'</td>';
            content += '	<td class="w_td_cname elli" title="'+appPacket.appName+'"> '+appPacket.appName+' </td>';
            var channel_name = "";
            switch (appPacket.channelId){
                case 0:
                    channel_name = "通用";
                    break;
                case 1:
                    channel_name = "淘宝tanx";
                    break;
                case 2:
                    channel_name = "百度bes";
                    break;
                case 7:
                    channel_name = "Tencent";
                    break;
                case 8:
                    channel_name = "灵集";
                    break;
            }
            content += '	<td class="w_td_sel elli">'+channel_name+'</td>';
            content += '	<td class="w_td_sel elli" title="'+appPacket.pkName+' "> '+appPacket.pkName+' </td>';

            if(flag){
                content += '<td class="w_td_operation active" id="app_'+appPacket.id+'" onclick="appAddToRight('+appPacket.id+',\''+appPacket.pkName+'\',\''+appPacket.appName+'\','+appPacket.channelId+',\''+appPacket.appId+'\')"></td>';
            }else{
                content += '<td class="w_td_operation" id="app_'+appPacket.id+'" onclick="appAddToRight('+appPacket.id+',\''+appPacket.pkName+'\',\''+appPacket.appName+'\','+appPacket.channelId+',\''+appPacket.appId+'\')"></td>';
            }

            content += '</tr>';

        });
        $("#app_name_table").append(content);
        $("#app_name_del").append(liContent);
    }
}

// 将选中的应用移动到右侧,并把选中的行置灰
function appAddToRight(id,pkName,appName,channel_id,app_id){
    // 如果选中则不增加到右侧
    if(!$('[id = "app_'+id+'"]').hasClass("active")){
        var app_name_arr = $("#app_name").val(); //选中的数据

        if(app_name_arr == null ||  app_name_arr == ""){
            app_name_arr = new Array();
        }else {
            app_name_arr = eval(app_name_arr);
            // app_name_arr = JSON.parse(app_name_arr);
        }
        var json = new Object();
        json.id = id;
        json.app_id = app_id;
        json.app_name = appName;
        json.pk_name = pkName;
        json.channel = channel_id;
        // app_name_arr.push('"'+pkName+'"');
        app_name_arr.push(json);
        $("#app_name").val(JSON.stringify(app_name_arr)); //更新选中的数据

        var content = '<li id="app_sel_'+id+'"><span>'+id+'_'+appName+'_'+pkName+'</span><i onclick="appAddToLeft('+id+',\''+pkName+'\',\''+appName+'\','+channel_id+',\''+app_id+'\')">×</i></li>';
        $("#app_name_del").append(content);
        //设置为选中状态
        $('[id = "app_'+id+'"]').addClass("active");
    }
}

// 将右边选中的元素删除
function appAddToLeft(id,pkName,appName,channel_id,app_id){

    //移除元素
    $('[id = "app_sel_'+id+'"]').remove();

    //左边的元素显示未选中
    $('[id = "app_'+id+'"]').removeClass("active");

    var app_name_arr = $("#app_name").val(); //选中的数据
    app_name_arr = eval(app_name_arr);
    var json = new Object();
    json.id = id;
    json.app_id = app_id;
    json.app_name = appName;
    json.pk_name = pkName;
    json.channel = channel_id;

    app_name_arr.splice($.inArray(json,app_name_arr),1);
    $("#app_name").val(JSON.stringify(app_name_arr)); //更新选中的数据

}

/** 应用定向结束 */


// 初始化弹窗
function bindInitWindow() {

	var camp_id = $("#id").val();

	//交易渠道自定义
	transChannelsWindow();

	//投放频次
	freqWindow();

	//媒体定向
	mediaDirect(camp_id);

	//地域定向
	areasClick();

	//人群定向
	peopleDirect(camp_id);
}

//投放频次
function freqWindow(){

	//初始化投放频次
	var freq_type = $("#freq_type").val();
	if(freq_type=="1"){
		$("#custom_freq_type").css('display','block');
	}else{
		$("#custom_freq_type").css('display','none');
	}

	$("input[name=freq_type]").change(function() {
		var value = $(this).attr("value");
		if(value=="0"){
			$("#custom_freq_type").css('display','none');
		}else if(value=="1"){
			$("#custom_freq_type").css('display','block');
		}
	});
}

//交易渠道自定义
function transChannelsWindow(){

    var editStepStatus = $("#editStepStatus").val();

    if(editStepStatus == "3"){

        var id = $("#channel").val();
        if( id.length == 0){
            $("#channel_1").css('display','none');
        }else {
            $("#channel_0").css('display','none');
        }

        $("[name='channel']").attr("disabled","disabled");
        $("#all_clean_channel").css('display','none');
    }

	//自定义点击事件
	$('#custom_channel_btn').on('click', function(){
		var channelLayer = layer.open({
	        type: 1,
	        title:'',
	        shadeClose: true,
            closeBtn: 0, //不显示关闭按钮
	        shade: 0.4,
	        area: ['540px', '280px;'],
	        content: $('#custom_channel_page')
	    });
		$("#custom_channel_btn").data("channelLayer", channelLayer);
	});

	//全选
	$("#all_channel").click(function(){
		$("[name='channel']").prop("checked",true);
	});

	//取消
	$("#clean_channel").click(function(){
		$("[name='channel']").prop("checked",false);
	});

	// 自定义交易渠道保存按钮
	$("#channel_conform").click(function() {
		var channelLayer = $("#custom_channel_btn").data("channelLayer");
		layer.close(channelLayer);
		var channel_arr = [];
		$("input:checkbox[name='channel']").each(function() {
	        if ($(this)[0].checked ==true) {
	        	if($(this)[0].value != 7 ){
					channel_arr.push( $(this)[0].value );
				}
	        }
		});
		$("#channel").val(channel_arr);
		return false;
	});

	// 自定义交易渠道取消按钮
	$("#channel_canncel").click(function() {
		var channelLayer = $("#custom_channel_btn").data("channelLayer");
		layer.close(channelLayer);
		return false;
	});
}

//媒体定向
function  mediaDirect(camp_id){
	//按照媒体类型投放
    mediaTypeWindow(camp_id);

	//按照媒体域名投放
    mediaDomainWindow(camp_id);

}

//按照媒体类型投放
function mediaTypeWindow(camp_id){

	// 初始化媒体类型数据
	var media_type_flag = $("#media_type_flag").val();
	if(media_type_flag =="0"){
		$.ajax({
			url : "listMedia.action?camp_id="+camp_id,
			type : "post",
			contentType : "application/json;charset=utf-8",
			dataType : "json",
			success : function(data) {
				if(data.success){
					// 初始化媒体类型表数据
					if(data.media_type ==null || data.media_type ==""){
						InitmediaTypeData("0");
					}else{
                        $("#media_type_data").val(data.media_type);
						InitmediaTypeData(data.media_type);
					}
				}
			}
		});
	}else{
		var media_type_selected = $("#media_type_data").val();
		media_type_selected = media_type_selected.split(",");
		InitmediaTypeData(media_type_selected);
	}

	// 打开窗口
    $("#media_type_dirrect").click(function(){
        var mediaTypeLayer = layer.open({
            type: 1,
            title:'',
            closeBtn: 0, //不显示关闭按钮
            shadeClose: true,
            shade: 0.4,
            area: ['510px', 'auto'],
            content: $('#media_type_window')
        });
        $("#media_type_window").data("mediaTypeLayer", mediaTypeLayer);
    });


	// 媒体类型保存按钮
	$("#media_type_conform").click(function() {
		var mediaTypeLayer = $("#media_type_window").data("mediaTypeLayer");
		layer.close(mediaTypeLayer);
		var media_type_arr = [];

		$("input:checkbox[name='webwite_check']").each(function(){
			if ($(this)[0].checked ==true) {
				media_type_arr.push($(this).val());
	        }
	 	});

		$("#media_type_data").val(media_type_arr);
		$("#media_type_flag").val("1");
		return false;
	});

	// 媒体类型取消按钮
	$("#media_type_canncel").click(function() {
		var mediaTypeLayer = $("#media_type_window").data("mediaTypeLayer");
		// $("#media_type_flag").val("0");
		layer.close(mediaTypeLayer);
		return false;
	});

	//全选
	$("#medis_type_all").click(function () {
		$("#media_type_one :checkbox").each(function(i, obj) {
			$(obj).prop("checked", true);
		});
	});
	//取消
	$("#medis_type_empty").click(function () {
		$("#media_type_one :checkbox").each(function(i, obj) {
			$(obj).prop("checked", false);
		});
	});
}

//媒体类型数据初始化
function InitmediaTypeData(mediaTypeList){
	//遍历媒体类型
	$("#media_type_one").empty();
	var content ="";

	// each begin
	$.each(_campaignDicData.websiteTypes, function(index, obj) {

		// 拼装树
		content +="<li>";
		content +="	<span id=\"media_"+obj.id+"\" class=\"toggle-button i_open\" onclick=\"spread_action("+obj.id+")\"></span>";
		content +="	<div class=\"type_list\">";
		content +="	<span class=\"checkbox_input\"><input type=\"checkbox\" name=\"media_father_"+obj.id+"\" onchange=\"media_checked_action("+obj.id+")\" value=\""+obj.id+"\"><label></label></span>";
		content +="	<span class=\"checkbox_label\">"+obj.name+"</span>";
		//是否包含下一级媒体类型
		if(obj.hasOwnProperty("subWebsiteTypes")){
			var sub_content ="";
			sub_content +="<ul  id=\"media_type_two_"+obj.id+"\">";

			$.each(obj.subWebsiteTypes, function(index, sub_obj) {

				var flag = false;
				//mediaDomain.domain = mediaDomain.domain.replace("\"","").replace("\"","");
				if(mediaTypeList !="0"){
					$.each(mediaTypeList, function(index, data) {
						if(data == sub_obj.id ){
							flag = true;
						}
					});
				};

				sub_content +="<li>";
				sub_content +="	<span class=\"checkbox_input\">";
				if(flag){
					sub_content +="	<input type=\"checkbox\" checked=\"checked\" id=\"check1\" onchange=\"webwite_checked_action("+obj.id+")\"   name=\"webwite_check\"  value=\""+sub_obj.id+"\">"
				}else{
					sub_content +="	<input type=\"checkbox\" id=\"check1\" onchange=\"webwite_checked_action("+obj.id+")\"   name=\"webwite_check\"  value=\""+sub_obj.id+"\">"
				}
				sub_content +="		<label for=\"check1\"></label></span>";
				sub_content +="	<span class=\"checkbox_label\">"+sub_obj.name+"</span>";
				sub_content +="</li>";
			});

			sub_content +="</ul>";
			// 加载子集数据
			content += sub_content;
		};
		content +="	</div>";
		content +="</li>";
	});  // end each

	$("#media_type_one").append(content);

	//判断子集是否有数据,如果有则父级为选中状态
	$.each(_campaignDicData.websiteTypes, function(index, obj) {
		//是否包含下一级媒体类型
		var father_flag = false;
		if(obj.hasOwnProperty("subWebsiteTypes")){
			$.each(obj.subWebsiteTypes, function(index, sub_obj) {
				if(mediaTypeList !="0"){
					$.each(mediaTypeList, function(index, data) {
						if(data == sub_obj.id ){
							father_flag = true;
							return false;
						}
					});
				};
				if(father_flag){
					return false;
				}
			});
		};
		if(father_flag){
			var father_select_id = "input:checkbox[name='media_father_"+obj.id+"']";
			$(father_select_id).prop("checked",true);
		}
	});

}

// 上级复选框选中未选状态的change事件
function media_checked_action(id){
	//spread_action(id);
	var father_select_id = "input:checkbox[name='media_father_"+id+"']";
	var father_status = $(father_select_id).prop("checked");
	var select_id ="#media_type_two_"+id +" input:checkbox[name='webwite_check']";
	$(select_id).prop("checked",father_status);

}

// 下级复选框点击关联事件
function webwite_checked_action(id){
	var father_select_id = "input:checkbox[name='media_father_"+id+"']";
	var select_id ="#media_type_two_"+id +" input:checkbox[name='webwite_check']";

	var chkt=0;
	$(select_id).each(function() {
        if ($(this)[0].checked ==true) {
        	chkt++;
        }
	});
	if(chkt>0){
		$(father_select_id).prop("checked",true);
	}else{
		$(father_select_id).prop("checked",false);
	}

}


// 展开/收缩功能
function spread_action(id){
	if($("#media_"+id).hasClass("i_open")){
		$("#media_"+id).removeClass("i_open");
		$("#media_type_two_"+id).css('display','block');

	}else{
		$("#media_"+id).addClass("i_open");
		$("#media_type_two_"+id).css('display','none');
	}
}

//按照媒体域名投放
function mediaDomainWindow(camp_id){

	//媒体类型
    var media_type_option = '<option value="" >全部</option>';
	$.each(_campaignDicData.websiteTypes, function(index, obj) {
		media_type_option +='<option value="'+obj.id+'">'+obj.name+'</option>';
		//是否包含下一级媒体类型
		if(obj.hasOwnProperty("subWebsiteTypes")){
			$.each(obj.subWebsiteTypes, function(index, sub_obj) {
				media_type_option +='<option value="'+sub_obj.id+'">-->'+sub_obj.name+'</option>';
			});
		};
	});
	$("#domain_name_select").append(media_type_option);

	// 窗口提交不刷新
	$('#formId').bind('submit', function(){
		ajaxSubmit(this, function(data){
			layer.closeAll('loading');
			if(data.success){
				// 初始化域名列表数据
				var media_name_selected = $("#media_name").val();
				media_name_selected = media_name_selected.split(",");
				mediaDomainList(data.page,media_name_selected);
			}
        });
        return false;
    });

	//查询
	$("#doamin_search_btn").click(function () {
		layer.load(2);  //加载层
		var url = $("#formId").attr("action");
		$("#formId").attr("action", url+"&pageNo=1");
		$("#formId").submit();
		$("#formId").attr("action", url);
	});

	//加载域名
	$.ajax({
		url : "listMedia.action?camp_id="+camp_id,
		type : "post",
		contentType : "application/json;charset=utf-8",
		dataType : "json",
		success : function(data) {
			if(data.success){
				// 初始化域名列表数据
				if(data.type=="2"){
					var media_save_flag = $("#media_save_flag").val();
					if(media_save_flag =="0") {
						$("#media_name").val(data.media_selected);
						$("#media_name").data("meida_name_data",data.media_selected);
						mediaDomainList(data.page, data.media_selected);
					}else{
						var media_name_selected = $("#media_name").val();
						media_name_selected = media_name_selected.split(",");
						mediaDomainList(data.page,media_name_selected);
					}
				}else{
					if(media_save_flag =="0") {
						mediaDomainList(data.page, "0");
					}else {
						var media_name_selected = $("#media_name").val();
						media_name_selected = media_name_selected.split(",");
						mediaDomainList(data.page,media_name_selected);
					}
				}
			}
		}
	});

	// 域名窗口弹出
    $("#media_name_dirrect").click(function(){
        var mediaDomainLayer = layer.open({
            type: 1,
            title:'',
            closeBtn: 0, //不显示关闭按钮
            shadeClose: true,
            shade: 0.4,
            area: ['1070px', '500px;'],
            content: $('#domain_name_window')
        });
        $("#domain_name_window").data("mediaDomainLayer", mediaDomainLayer);
    });

	// 媒体域名保存按钮
	$("#media_name_conform").click(function() {
		var mediaDomainLayer = $("#domain_name_window").data("mediaDomainLayer");
		layer.close(mediaDomainLayer);
		var media_name_arr = [];
		$("#domain_name_del span").each(function() {
	        media_name_arr.push('"'+$(this).text()+'"');
		});
		$("#media_name").val(media_name_arr);
		$("#media_name").data("meida_name_data",media_name_arr);
		$("#media_save_flag").val("1");
		return false;
	});

	// 媒体域名取消按钮
	$("#media_name_canncel").click(function() {
		var mediaDomainLayer = $("#domain_name_window").data("mediaDomainLayer");
		// $("#media_save_flag").val("0");
		layer.close(mediaDomainLayer);
		return false;
	});

	//全部删除
	$("#domain_name_del_all").click(function () {
		$("#domain_name_del").empty();
		$("#media_name").val("");
		$("#domain_name_table .w_td_operation").each(function(i, obj) {
			$(obj).removeClass("active");
		});
	});

}

function ajaxSubmit(frm,fn){
	var dataPara = getFormJson(frm);
	$.ajax({
        url: frm.action,
        type: frm.method,
        data: dataPara,
        success: fn
    });
}

function getFormJson(frm) {
    var o = {};
    var a = $(frm).serializeArray();
    $.each(a, function () {
        if (o[this.name] !== undefined) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });

    return o;
}


//域名列表初始化
function mediaDomainList(page,media_selected){
	$("#domain_name_table").empty();
	$("#page_window").empty();

	var page_window = page.frontPageHtml;
	$("#page_window").append(page_window);

	if(page.list.length>0){
		$("#domain_name_del").empty();

		var liContent="";
		if(media_selected !="0") {
			$.each(media_selected, function (index, media) {
				media = media.replace("\"", "").replace("\"", "");
				if(media != null && media != ""){
					liContent += '<li id="sel_' + media + '"><span>' + media + '</span><i onclick="addToLeft(\'' + media + '\')">×</i></li>';
				}
			});
		}

		var content ="";
		$.each(page.list, function(index, mediaDomain) {
			var flag = false;
			mediaDomain.domain = mediaDomain.domain.replace("\"","").replace("\"","");
			if(media_selected !="0"){
				$.each(media_selected, function(index, media) {
					media = media.replace("\"","").replace("\"","");
					if(media == mediaDomain.domain){
						flag = true;
					}
				});
			};
			content += "<tr>";
			content += '		<td class="w_td_name elli" title="'+mediaDomain.domain+'"> '+mediaDomain.domain+'</td>';
			content += '		<td class="w_td_cname elli" title="'+mediaDomain.siteName+'"> '+mediaDomain.siteName+' </td>';

			for(var id in _campaignDicData.websiteTypes){
				var obj = _campaignDicData.websiteTypes[id];
				if(obj.hasOwnProperty("subWebsiteTypes")) {
					for (var sub_id in obj.subWebsiteTypes) {
						var sub_obj = obj.subWebsiteTypes[sub_id];
						if (sub_obj.id == mediaDomain.webSiteType) {
							content += '<td class="w_td_sel elli" title="' + sub_obj.name + '"> ' + sub_obj.name + ' </td>';
							break;
						}
					}
				}
			}
			if(mediaDomain.manualEntry == 1) {
				content += '	<td>手工录入</td>';
			}else{
				content += '	<td>系统录入</td>';
			}
			content += '		<td class="w_td_sel elli" title="'+mediaDomain.flow+' "> '+mediaDomain.flow+' </td>';

			if(flag){
				content += '		<td class="w_td_operation active" id="domain_'+mediaDomain.domain+'" onclick="addToRight(\''+mediaDomain.domain+'\')"></td>';
			}else{
				content += '		<td class="w_td_operation " id="domain_'+mediaDomain.domain+'" onclick="addToRight(\''+mediaDomain.domain+'\')"></td>';
			}
			content += '</tr>';

		});
		$("#domain_name_table").append(content);
		$("#domain_name_del").append(liContent);
	}
}

// 将选中的域名移动到右侧,并把选中的行置灰
function addToRight(domain){
	// 如果选中则不增加到右侧
	if(!$('[id = "domain_'+domain+'"]').hasClass("active")){
		var media_name_arr = $("#media_name").val().split(","); //选中的数据
		media_name_arr.push('"'+domain+'"');
		$("#media_name").val(media_name_arr); //更新选中的数据

		var content = '<li id="sel_'+domain+'"><span>'+domain+'</span><i onclick="addToLeft(\''+domain+'\')">×</i></li>';
		$("#domain_name_del").append(content);
		//设置为选中状态
		$('[id = "domain_'+domain+'"]').addClass("active");
	}
}

// 将右边选中的元素删除
function addToLeft(domain){
	//移除元素
	$('[id = "sel_'+domain+'"]').remove();
	//左边的元素显示未选中
	$('[id = "domain_'+domain+'"]').removeClass("active");

	var media_name_arr = $("#media_name").val().split(","); //选中的数据
	media_name_arr.splice($.inArray('"'+domain+'"',media_name_arr),1);
	// media_name_arr.remove(domain);
	$("#media_name").val(media_name_arr); //更新选中的数据
}

function areasClick(){

	//绑定打开地域窗口事件
	$("input[name=area_type]").click(function() {
		var value = $(this).attr("value");
		if(value=="1"){

			//展示地域窗口
			var areaLayer = layer.open({
		        type: 1,
		        title:'',
		        shadeClose: true,
                closeBtn: 0, //不显示关闭按钮
		        shade: 0.4,
		        area: ['800px', 'auto'],
		        scrollbar: false,
		        content: $('#area_select')
		    });

			$('#area_select').parent().css("overflow","visible");
			//将layer对象存在一个元素中
			$("#area_ok_btn").data("areaLayer",areaLayer);

			var selectedVal = $("#area").val();
			if(selectedVal != ""){
				var array = selectedVal.split(",");
				for(var i = 0 ; i < array.length ; i++){
					var cityId = array[i];
					var cityCheckbox = $("#area_select :checkbox[level=city][value=" + cityId + "]");
					if(cityCheckbox.prop("checked") == false){
						cityCheckbox.click();
					}
				}
			}
		}
	});

	//延迟1秒加载地域的html，避免造成页面加载缓慢
	setTimeout(areasWindow,1000);
}

//显示地域，绑定地域选择事件
function areasWindow(){

//	if($("#area_html_created").val() == "1"){
//		return ;
//	}

	//遍历大区
	for(var bigAreaId in _campaignDicData.areas){
		var bigAreaName = _campaignDicData.areas[bigAreaId].name ;
		var topDiv = "#area_select .check_dress_con" ;
		//追加大区div
		$(topDiv).append("<div class=\"clearfix check_dress\" bigArea=\"" + bigAreaId + "\"></div>");
		//大区div选择器
		var bigAreaDiv = "#area_select .check_dress[bigArea=" + bigAreaId + "]" ;
		//加载大区选择框
		$(bigAreaDiv).append("<span><label><input type=\"checkbox\" value=\"" + bigAreaId + "\" level=\"bigArea\">" + bigAreaName);
	}

	//遍历省份
	for(var provinceId in _campaignDicData.provinces){
		var provinceName = _campaignDicData.provinces[provinceId].name ;
		var bigAreaId = _campaignDicData.provinces[provinceId].parent ;
		var bigAreaDiv = "#area_select .check_dress[bigArea=" + bigAreaId + "]" ;
		//追加省份div
		$(bigAreaDiv).append("<div class=\"check_dress_rt\" province=\"" + provinceId + "\"></div>");
		//省份div选择器
		var provinceDiv = "#area_select .check_dress_rt[province=" + provinceId + "]" ;
		var checkBoxHtml = "<label province=\"" + provinceId + "\">" ;
		checkBoxHtml += "<input type=\"checkbox\" value=\"" + provinceId + "\" level=\"province\" bigArea=\"" + bigAreaId + "\">" ;
		checkBoxHtml +=  provinceName ;
		//将来显示省份选中城市的数量
		checkBoxHtml +=  "(<span class=\"city_sel\" province=\"" + provinceId + "\">0</span>" ;
		//显示省份城市的总数
		checkBoxHtml +=  "/<span class=\"city_cnt\" province=\"" + provinceId + "\">0</span>)" ;
		checkBoxHtml += "</label>" ;
		//加载省份选择框
		$(provinceDiv).append(checkBoxHtml);
		//加载省份城市div
		$(provinceDiv + " label").append("<div class=\"dress_city\"></div>");
	}

	//遍历城市
	for(var cityId in _campaignDicData.citys){
		var cityName = _campaignDicData.citys[cityId].name ;
		var provinceId = _campaignDicData.citys[cityId].parent ;
		var provinceDivLabel = "#area_select .check_dress_rt[province=" + provinceId + "] label" ;
		var bigAreaId = $(provinceDivLabel).find(":checkbox").attr("bigArea");
		var cityDiv = provinceDivLabel + " .dress_city" ;
		//加载城市选择框
		$(cityDiv).append("<label><input type=\"checkbox\" value=\"" + cityId + "\" level=\"city\" province=\"" + provinceId + "\" bigArea=\"" + bigAreaId + "\">" + cityName + "</label>");

		//累加省份的城市总数
		var provinceCityCnt = parseInt($(provinceDivLabel).find(".city_cnt").html()) + 1 ;
		$(provinceDivLabel).find(".city_cnt").html(provinceCityCnt);
	}

	//鼠标移到省份弹出城市div
	$("#area_select label[province]").mouseover(function(){

		$(this).addClass("active");
		$(this).find("div.dress_city").show();

	}).mouseout(function(){
		$(this).removeClass("active");
		$(this).find("div.dress_city").hide();
	});

	//定义一个函数，用于统计省内选中的城市
	var countCitySel = function(provinceId){
		//如果有provinceId，则查询器为指定的省，否则为所有的省
		var selector = provinceId ? "#area_select .city_sel[province=" + provinceId + "]" : "#area_select .city_sel" ;

		$(selector).each(function(index,obj){
			var provinceId = $(this).attr("province");
			$(this).text($("#area_select :checkbox[province=" + provinceId + "]:checked").length);
		});
	};

	//勾选大区
	$("#area_select :checkbox[level=bigArea]").click(function(){
		var bigAreaId = $(this).val();

		if($(this).prop("checked") == true){
			//让所有的省和市都选中
			$("#area_select :checkbox[bigArea=" + bigAreaId + "]").prop("checked",true);
		}
		else{
			//取消所有的省和市
			$("#area_select :checkbox[bigArea=" + bigAreaId + "]").prop("checked",false);
		}
		//统计所有选中的城市
		countCitySel();
	});

	//勾选省份
	$("#area_select :checkbox[level=province]").click(function(){
		var provinceId = $(this).val();
		var bigAreaId = $(this).attr("bigArea");

		//查询器：所有bigArea为bigAreaId的省
		var allProvinceSelector = "#area_select :checkbox[level=province][bigArea=" + bigAreaId + "]" ;

		if($(this).prop("checked") == true){
			//选中所有城市
			$("#area_select :checkbox[province=" + provinceId + "]").prop("checked",true);
			//如果其大区所有省份都选中了，让大区选中
			if($(allProvinceSelector + ":checked").length == $(allProvinceSelector).length){
				$("#area_select :checkbox[level=bigArea][value=" + bigAreaId + "]").prop("checked",true);
			}
		}
		else{
			//省内所有城市取消
			$("#area_select :checkbox[province=" + provinceId + "]").prop("checked",false);
			//取消大区
			$("#area_select :checkbox[level=bigArea][value=" + bigAreaId + "]").prop("checked",false);
		}
		//统计省内选中的城市
		countCitySel(provinceId);
	});

	//勾选城市
	$("#area_select :checkbox[level=city]").click(function(){
		var provinceId = $(this).attr("province");
		var bigAreaId = $(this).attr("bigArea");

		//查询器：所有province为provinceId的市
		var allProvinceCitySelector = "#area_select :checkbox[level=city][province=" + provinceId + "]" ;
		//查询器：所有bigArea为bigAreaId的市
		var allBigAreaCitySelector = "#area_select :checkbox[level=city][bigArea=" + bigAreaId + "]" ;

		if($(this).prop("checked") == true){
			//如果其大区所有城市都选中了，让大区选中
			if($(allBigAreaCitySelector + ":checked").length == $(allBigAreaCitySelector).length){
				$("#area_select :checkbox[level=bigArea][value=" + bigAreaId + "]").prop("checked",true);
			}
			//如果其省份所有城市都选中了，让省份选中
			if($(allProvinceCitySelector + ":checked").length == $(allProvinceCitySelector).length){
				$("#area_select :checkbox[level=province][value=" + provinceId + "]").prop("checked",true);
			}
		}
		else{
			//取消大区
			$("#area_select :checkbox[level=bigArea][value=" + bigAreaId + "]").prop("checked",false);
			//取消省份
			$("#area_select :checkbox[level=province][value=" + provinceId + "]").prop("checked",false);
		}
		//统计省内选中的城市
		countCitySel(provinceId);
	});

	//全选
	$("#area_select #select_all").click(function(){
		$("#area_select :checkbox").prop("checked",true);
		countCitySel();
	});
	//清空
	$("#area_select #clean_all").click(function(){
		$("#area_select :checkbox").prop("checked",false);
		countCitySel();
	});

	// 地域保存按钮事件
	$("#area_ok_btn").click(function() {
		//关闭layer
		var areaLayer = $("#area_ok_btn").data("areaLayer");
		layer.close(areaLayer);
		//读取所有选中的城市（只要城市级别的）
		var area_arr = [];
		$("#area_select :checkbox[level=city]").each(function() {
	        if ($(this).prop("checked") == true) {
	        	area_arr.push( $(this).val());
	        }
		});
		//console.log("area_arr");
		//console.log(area_arr);
		//将数据放在隐藏域中
		$("#area").val(area_arr);
	});

	// 地域取消按钮事件
	$("#area_cancel_btn").click(function() {
		//关闭layer
		var areaLayer = $("#area_ok_btn").data("areaLayer");
		layer.close(areaLayer);
	});

	//在隐藏域中标识地域html已经加载完成
	//$("#area_html_created").val(1);

}

//人群定向
function peopleDirect(camp_id){

    initPopulation();// 初始化人群
    initInstresting(); //初始化兴趣爱好
	initCookiePacket(); //初始化个性化人群包
	initRetargetPacket(); //初始化访客找回


    // 人群定向事件
    $(".peopleContainer .crowd-header span").click(function() {
        if (!$(this).hasClass("on")) {
            $(".peopleContainer .crowd-header span").removeClass("on");
            $(".peopleContainer .crowd-header span i").removeClass("active");
            $(".peopleContainer .crowd-select-left .Tab-Container").hide();

            var id = $(this).attr("id");
            $(this).addClass("on");
            $(this).find("i").addClass("active");
            switch (id) {
                case "peopleTab":
                    $("#peopleContainer").show();
                    break;
                case "interestTab":
                    $("#interest").show();
                    break;
                case "personalityTab":
                    $("#personality").show();
                    break;
                case "guestTab":
                    $("#guest").show();
                    break;
                case "ipTab":
                    $("#ip").show();
                    break;
            }
        }
    });

	// 人群选择 - 清空
    $("#clearPeople").click(function () {
        $("#peopleContainer input[type='checkbox']").prop("checked", false);
        $("#peopleContainer").trigger("check");
    });
    // 兴趣定向 - 清空
    $("#clearInterest").click(function () {
        $("#interest input[type='checkbox']").prop("checked", false);
        $("#interestList").trigger("check");
    });
    // 个性化人群定向 - 清空
    $("#clearPersonality").click(function () {
        $("#personality input[type='checkbox']").prop("checked", false);
        $("#personality").trigger("check");
    });
    // 访客找回 - 清空
    $("#clearGuest").click(function () {
        $("#guest input[type='checkbox']").prop("checked", false);
        $("#guest").trigger("check");
    });

}

/**
 * 初始化人群定向
 */
function initPopulation() {
	
    var populationOrientation = campaignDicData.populations;// 地区
    for ( var key in populationOrientation) {
        var content = '<div class="clearfix"></div><div class="crowd-title">' + key + '：</div><ul>';
        var population = populationOrientation[key];
        $.each(population, function(i, context) {
            content += '<li>'
                + ' <div class="checkboxcontainer">'
                + '     <span class="checkboxinput">'
                + '         <input id="cookie_item_' + context.id + '" name="cookies" dim-name="' + context.dimName + '" type="checkbox" value="' + context.id + '"/>'
                + '         <label for="cookie_item_' + context.id + '"></label>'
                + '     </span>'
                + '     <span class="checkboxlabel">' + context.ckGroupName + '</span>'
                + ' </div>'
                + '</li>';
        });
        content += '</ul><div class="clearfix"></div><hr>';
        $("#people").append(content);
    }

	// 监测方法
	$("#peopleContainer").bind("check", function() {
		$("#peopleResult ul").empty();
		$("#people :checked").each(function() {
			var dimName = $(this).attr("dim-name");
			$("#peopleResult ul[dimName=" + dimName + "]").append("<li>" + $(this).parent().next().text() + "</li>");
		});
		var isAll = true;
		$("#people :checkbox").each(function(i, obj) {
			if ($(obj).filter(":checked").size() == 0) {
				isAll = false;
			}
		})
		if (isAll) {
			$("#people_all").html("取消");
			$("#people_all").removeAttr("selected");
		} else {
			$("#people_all").html("全选");
			$("#people_all").attr("selected", "true");
		}
	});

	console.log($("#populations").val());
	
    // 已选数据初始化
    var populations_selected = $("#populations").val(); // 人群属性定向
    // 人群属性定向赋值
    if (populations_selected) {
		populations_selected = populations_selected.split(",");
        for (var i = 0; i < populations_selected.length; i++) {
            $("#cookie_item_" + populations_selected[i] + "").prop("checked", true);
        }
    }
	//人口定向匹配模式 (and与,or或)
	var matchType_sel = $("#matchType_sel").val();
	if (matchType_sel) {
		$("#matchType_" + matchType_sel).prop("checked", true);
	} else {
		$("matchType_or").prop("checked", true);
	}
	//初始化数据后,自动触发check事件
	$("#peopleContainer").trigger("check");

    //人群全选事件
    $("#people_all").click(function(){
        var selected = $(this).attr("selected");
        if (selected) {
            $(this).removeAttr("selected");
            $("#people :checkbox").each(function(i, obj) {
                $(obj).prop("checked", true);
            })
        } else {
            $(this).attr("selected", "true");
            $("#people :checkbox").each(function(i, obj) {
                $(obj).prop("checked", false);
            })
        }
        $("#peopleContainer").trigger("check");
    });

    // 人群属性定向之单选框选中事件
    $("#peopleContainer").delegate(":checkbox", "click", function() {
        $("#peopleContainer").trigger("check");
    });

    // 人群属性高级设置
    $("#crowd_subdes_more").click(function() {
        var ck = $(this).attr("selected");
        if (ck == "selected") {
            $(this).attr("selected", null);
            $(this).text("收起");
            $(".crowd-subdes-more").slideDown();
        } else {
            $(this).attr("selected", "selected");
            $(this).text("显示");
            $(".crowd-subdes-more").slideUp();
        }
    });



}

/**
 * 初始化兴趣爱好
 */
function initInstresting() {
    var instresting = campaignDicData.instresting;// 兴趣定向
    var i = 0;
    for ( var key in instresting) {
        i++;
        var content = "<li class='root'>"
            + "<span class='toggle-button' selected='false' style='float: left;cursor: pointer;'>▶</span>"
            + "<div class='checkboxcontainer' style='margin-left:20px;'>"
            + "<span class='checkboxinput'>"
            + "<input id='interest_" + i + "' type='checkbox' level='1' name='instrests'/>"
            + "<label for='interest_" + i + "'></label>"
            + "</span>"
            + "<span class='checkboxlabel'>" + key + "</span>"
            + "</div>"
            + "<ul class='sub-menu' style='display:none;margin-left:45px;padding-top:0px;'>";
        var instrestingOne = instresting[key];
        $.each(instrestingOne, function(n, instrest) {
            content += "<li>"
                + "<div class='checkboxcontainer'>"
                + "<span class='checkboxinput'>"
                + "<input id='interest_" + i + "_" + instrest.id + "' type='checkbox' name='instrests' level='2' value='" + instrest.id + "' />"
                + "<label for='interest_" + i + "_" + instrest.id + "'></label>"
                + "</span>"
                + "<span class='checkboxlabel'>" + instrest.ckGroupName + "</span>"
                + "</div>"
                + "</li>";
        });
        content += "</ul>" + "</li>";
        $("#interestList").append(content);
    }

    //绑定check事件
	$("#interestList").bind("check", function() {
		if ($("#interest input[level=2]:checked").size() > 0) {
			$("#interest input[level=2]:checked").each(function(i, objLevel) {
				$(objLevel).parents("li").find("input[level=1]").prop("checked", true);
			});
		}
		$("#interest input[level=2]").each(function(i, obj) {
			var hasOtherChecked = $(obj).parents("ul:first").find("input[level=2]:checked").size();
			if (hasOtherChecked <= 0) {
				$(obj).parents("ul:first").siblings("div.checkboxcontainer").find("input[level=1]").prop("checked", false);
			}
		});
		$("#interestResult ul").empty();
		if ($("#interest input[level=1]:checked").size() > 0) {
			$("#interest input[level=1]:checked").each(function(i, objLevel) {
				$("#interestResult ul").append("<li>" + $(objLevel).parent().next().text() + "</li>");
			});
		}
		var isAll = true;
		$("#interestList :checkbox").each(function(i, obj) {
			if (!$(obj).prop("checked")) {
				isAll = false;
			}
		})
		if (isAll) {
			$("#interest_all").html("取消");
			$("#interest_all").removeAttr("selected");
		} else {
			$("#interest_all").html("全选");
			$("#interest_all").attr("selected", "true");
		}
	});

	//兴趣定向初始化选择的数据
	var instresting_sel = $("#instresting_sel").val(); // 人群属性定向
	if (instresting_sel) {
		instresting_sel = instresting_sel.split(",");
		var checkboxAll = $("#interestList").find("input[type=checkbox]");
		for (var i = 0; i < checkboxAll.length; i++) {
			for (var j = 0; j < instresting_sel.length; j++) {
				if ($(checkboxAll[i]).val() == instresting_sel[j]) {
					$(checkboxAll[i]).prop("checked", true);
				}
			}
		}
	}
	$("#interestList").trigger("check");// 根据选择项初始化页面
    // 兴趣定向
    $("#interestList span.toggle-button").click(function() {
        var selected = $(this).prop("selected");
        if (selected) {
            $(this).prop("selected", false);
            $(this).html("▶");
        } else {
            $(this).prop("selected", true);
            $(this).html("▼");
        }
        $(this).parent().find("ul").slideToggle();
    });
    // 兴趣定向之选择框点击事件
    $("#interestList").delegate(":checkbox[name=instrests]", "click", function() {
        var level = $(this).attr("level");
        var ck = $(this).prop("checked");
        var id = $(this).attr("id");
        if (level == 1) {
            $("input[id^='" + id + "_']").prop("checked", ck);
        }
        $("#interestList").trigger("check");
    });

    // 兴趣定向全选
    $("#interest_all").click(function() {
        var selected = $(this).attr("selected");
        if (selected) {
            $(this).removeAttr("selected");
            $("#interest :checkbox").each(function(i, obj) {
                $(obj).prop("checked", true);
            })
        } else {
            $(this).attr("selected", "true");
            $("#interest :checkbox").each(function(i, obj) {
                $(obj).prop("checked", false);
            })
        }
        $("#interestList").trigger("check");
    });
}

/**
 * 初始化个性化人群包
 */
function initCookiePacket() {

	$("#personality").bind("check", function() {
		$("#personalityResult ul").empty();
		$("#personality input[name=cookiePacket]:checked").each(function() {
			$("#personalityResult ul").append("<li>" + $(this).parent().next().text() + "</li>");
		});
		var isAll = true;
		$("#personality input[name=cookiePacket]").each(function(i, obj) {
			if (!$(obj).prop("checked")) {
				isAll = false;
			}
		})
		if (isAll) {
			$("#personality_all").html("取消");
			$("#personality_all").removeAttr("selected");
		} else {
			$("#personality_all").html("全选");
			$("#personality_all").attr("selected", "true");
		}
	});

	//初始化数据
	var cookiePacket_sel = $("#cookiePacket_sel").val(); // 人群属性定向
	if (cookiePacket_sel) {
		cookiePacket_sel = cookiePacket_sel.split(",");
		for(var index in cookiePacket_sel){
			var cpId = cookiePacket_sel[index];
			$("#personality input[name=cookiePacket][value="+cpId+"]").prop("checked", true);
		}
	}
	$("#personality").trigger("check");

	// 个性化人群定向
	$("input[name='cookiebtn']").click(function() {
		var id = $(this).attr("id");
		if (id == "btn_private") {
			$("#btn_private").attr("class", "btn btn-default");
			$("#btn_public").attr("class", "btn btn-cancel");
			$("#cookiePrivate").show();
			$("#cookiePublic").hide();
		} else {
			$("#btn_public").attr("class", "btn btn-default");
			$("#btn_private").attr("class", "btn btn-cancel");
			$("#cookiePrivate").hide();
			$("#cookiePublic").show();
		}
	});
	$("#personality").delegate("input[name=cookiePacket]", "click", function() {
		$("#personality").trigger("check");
	});

	// 全选
	$("#personality_all").click(function() {
		var selected = $(this).attr("selected");
		if (selected) {
			$(this).removeAttr("selected");
			$("#personality input[name=cookiePacket]").each(function(i, obj) {
				$(obj).prop("checked", true);
			})
		} else {
			$(this).attr("selected", "true");
			$("#personality input[name=cookiePacket]").each(function(i, obj) {
				$(obj).prop("checked", false);
			})
		}
		$("#personality").trigger("check");
	});
}

/**
 * 初始化访客找回
 */
function initRetargetPacket() {

	$("#guest").bind("check", function() {
		$("#guestResult ul").empty();
		$("#guest input[name=retargetpacket]:checked").each(function() {
			$("#guestResult ul").append("<li>" + $(this).parent().next().text() + "</li>");
		});
		var isAll = true;
		$("#guest :checkbox").each(function(i, obj) {
			if (!$(obj).prop("checked")) {
				isAll = false;
			}
		})
		if (isAll) {
			$("#retarget_all").html("取消");
			$("#retarget_all").removeAttr("selected");
		} else {
			$("#retarget_all").html("全选");
			$("#retarget_all").attr("selected", "true");
		}
	});

	// 访客找回
	var retargetPacket_sel = $("#retargetPacket_sel").val(); // 人群属性定向
	if (retargetPacket_sel) {
		retargetPacket_sel = retargetPacket_sel.split(",");
		for (var i = 0; i < retargetPacket_sel.length; i++) {
			$("#retarget_" + retargetPacket_sel[i] + "").prop("checked", true);
		}
		$("#guest").trigger("check");// 根据选中项初始化页面
	}

	$("#guest").delegate(" input[name=retargetpacket]", "click", function() {
		$("#guest").trigger("check");
	});

	// 访客找回全选
	$("#retarget_all").click(function() {
		var selected = $(this).attr("selected");
		if (selected) {
			$(this).removeAttr("selected");
			$("#guest input[name=retargetpacket]").each(function(i, obj) {
				$(obj).prop("checked", true);
			})
		} else {
			$(this).attr("selected", "true");
			$("#guest input[name=retargetpacket]").each(function(i, obj) {
				$(obj).prop("checked", false);
			})
		}
		$("#guest").trigger("check");
	});
}

// 绑定提交方法
function bindSaveCampTwo() {
	// 下一步提交 begin
	$("#save_camp_btn_2").click(function() {

		layer.load(2);  //加载层

		// 活动ID
		var campid = $("#id").val();
		// 最高限价
		var price = $("#price").val();
		var priceReg = new RegExp("^[0-9]+(.[0-9]{0,2})?$");
		if (price == null || price == '' || ! priceReg.test(price) || price<=0) {
			layer.closeAll('loading');
			$("#price_tip").css('display','block');
			$("#price").focus();
			return false;
		}

		// 交易类型
		var expend_type = $("input[name='expend_type']:checked").val();

		var campArray = new Array();
		// 封装数据
		campArray = bindSaveCampData();
		// ajax请求
		$.ajax({
			url : "saveCampDim.action?campid=" + campid
			+ "&price=" + price + "&expend_type="
			+ expend_type + "&step=2",
			type : "post",
			contentType : "application/json;charset=utf-8",
			// 请求json数据
			data : JSON.stringify(campArray),
			dataType : "json",
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
		// end ajax请求

	});
	// 下一步提交 end
	// 确认并返回 begin
	$("#save_camp_btn_2_back").click(function() {

		layer.load(2);  //加载层
		setTimeout(function(){
			layer.closeAll('loading');
		}, 1300);
		// 活动ID
		var campid = $("#id").val();
		// 最高限价
		var price = $("#price").val();
		var priceReg = new RegExp("^[0-9]+(.[0-9]{0,2})?$");
		if (price == null || price == '' || ! priceReg.test(price) || price<=0 ) {
			$("#price_tip").css('display','block');
			$("#price").focus();
			layer.closeAll('loading');
			return false;
		}

		// 交易类型
		var expend_type = $("input[name='expend_type']:checked").val();

		var campArray = new Array();
		// 封装数据
		campArray = bindSaveCampData();
		// ajax请求
		$.ajax({
			url : "saveCampDim.action?campid=" + campid
			+ "&price=" + price + "&expend_type="
			+ expend_type + "&step=3",
			type : "post",
			contentType : "application/json;charset=utf-8",
			// 请求json数据
			data : JSON.stringify(campArray),
			dataType : "json",
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
		// end ajax请求
	});
	// 确认并返回 end
}

function bindSaveCampData() {

	// 活动ID
	var campid = $("#id").val();

	var campArray = new Array();
	// 交易渠道
	var channel_type = $("input[name='channel_type']:checked").val();
	var dimValue = new Object();
	if (channel_type == "0") {
		dimValue = '{ "selected" : [], "type" : 0 }';
	} else {
		var channel_val = $("#channel").val();
		if(channel_val ==null || channel_val == ""){
			layer.closeAll('loading');
			layer.alert('自定义渠道不能为空,请选择渠道!', {icon: 5});
            return false;
		}
		var channel_selected = "["+$("#channel").val()+ "]";
		dimValue = '{ "selected" : ' + channel_selected + ', "type" : 1 }';
	}
	var obj = new Object();
	obj.campaignId = campid;
	obj.dimName = "channels";
	obj.dimValue = dimValue;
	obj.deleteStatus = 0;

	campArray.push(obj);

	// 地域定向
	// var area_type = $("input[name='area_type']:checked").val();
    var area_type = $("input[name='area_type']:checked").val();
	var cityDimValue = "";
	if (area_type == "0") {
		cityDimValue = '{ "citys" : [], "type" : 0 }';
	} else {
		var area_selected = $("#area").val();
		cityDimValue = '{ "citys" :[' + area_selected + '], "type" : 1 }';
	}
	obj = new Object();
	obj.campaignId = campid;
	obj.dimName = "areas";
	obj.dimValue = cityDimValue;
	obj.deleteStatus = 0;

	campArray.push(obj);

    //人群定向
    var people_flag = false;
    // 人群属性定向
    var peopleEle = $("#peopleContainer input[type=checkbox]:checked");
    if (peopleEle.size() > 0) {

        people_flag = true;

        var peoples = [];
        peopleEle.each(function(i, obj) {
            peoples.push(parseInt($(obj).val()));
        });
        var matchType = $("input[name=matchType]:checked").val();
        obj = new Object();
        obj.campaignId = campid;
        obj.dimName = "populations";
        obj.dimValue = "["+peoples+']';
        obj.deleteStatus = 0;

        campArray.push(obj);
    }

    // 兴趣定向
    var interestEle = $("#interest input[level=2]:checked");
    if (interestEle.size() > 0) {
        people_flag = true;
        var instrestes = [];
        interestEle.each(function(i, obj) {
            instrestes.push(parseInt($(obj).val()));
        });

        obj = new Object();
        obj.campaignId = campid;
        obj.dimName = "instresting";
        obj.dimValue = "["+instrestes+']';
        obj.deleteStatus = 0;

        campArray.push(obj);
    }

	// 个性化人群定向
	var personalityEle = $("#personality input[name=cookiePacket]:checked");
	if (personalityEle.size() > 0) {
		people_flag = true;
		var personalitys = [];
		personalityEle.each(function(i, obj) {
			personalitys.push(parseInt($(obj).val()));
		});
		obj = new Object();
		obj.campaignId = campid;
		obj.dimName = "cookiePacket";
		obj.dimValue = "["+personalitys+']';
		obj.deleteStatus = 0;

		campArray.push(obj);
	}
	//访客回坊
	// 访客找回
	var guestEle = $("#guest input[name=retargetpacket]:checked");
	if (guestEle.size() > 0) {
		people_flag = true;
		var guestes = [];
		guestEle.each(function(i, obj) {
			guestes.push(parseInt($(obj).val()));
		});
		obj = new Object();
		obj.campaignId = campid;
		obj.dimName = "retargetPacket";
		obj.dimValue = "["+guestes+']';
		obj.deleteStatus = 0;

		campArray.push(obj);
	}

    if(people_flag){

        var matchType = $("input[name=matchType]:checked").val();
        obj = new Object();
        obj.campaignId = campid;
        obj.dimName = "matchType";
        obj.dimValue = matchType;
        obj.deleteStatus = 0;

        campArray.push(obj);
    }


	// 媒体定向
	var media_type = $("input[name='media_type']:checked").val();
	var dimValue = "";
	if (media_type == "0") {
		dimValue = '{ "websiteType" : [], "type" : 0 }';
	} else if (media_type == "1") {
		var media_selected = "["+$("#media_type_data").val()+ "]";
        if(media_selected ==null || media_selected == ""){
            layer.closeAll('loading');
            layer.alert('媒体类型不能为空!', {icon: 5});
            return false;
        }
		dimValue = '{ "websiteType" : ' + media_selected + ', "type" : 1 }';

	} else if (media_type == "2") {
		var media_selected = "["+$("#media_name").data("meida_name_data")+ "]";
        if(media_selected ==null || media_selected == ""){
            layer.closeAll('loading');
            layer.alert('媒体域名不能为空!', {icon: 5});
            return false;
        }
		dimValue = '{ "webDomain" : ' + media_selected + ', "type" : 2 }';
	}

	obj = new Object();
	obj.campaignId = campid;
	obj.dimName = "media";
	obj.dimValue = dimValue;
	obj.deleteStatus = 0;

	campArray.push(obj);


    //应用定向
    var appData_radio = $("#appData_radio").val(); // 默认值
    var app_type = $("#appSelectValue").val(); //选中值
    if(app_type == null || app_type == ""){
        app_type = "0";
    }
    var appData = $("#appData").val();

    if(app_type != "0"){
        // appData = "["+appData+"]";
        if(app_type == "1"){
            var flag = $("#app_type_flag").val();
            var app_type_data = $("#app_type_data").val();
            if(flag == "1"){
                if(app_type_data == "" || app_type_data == null){
                    layer.closeAll('loading');
                    layer.alert('应用类型不能为空!', {icon: 5});
                    return false;
                }
                appData = "["+app_type_data+"]";
            }else {
                if( appData_radio != app_type){
                    if(app_type_data == "" || app_type_data == null){
                        layer.closeAll('loading');
                        layer.alert('应用类型不能为空!', {icon: 5});
                        return false;
                    }
                }
            }
            dimValue = '{ "appType" : ' + appData + ', "type" : 1 }';
        }
        if(app_type == "2"){
            var flag = $("#app_save_flag").val();
            var app_name = $("#app_name").val();
            if(flag == "1"){
                if(app_name == "" || app_name == null){
                    layer.closeAll('loading');
                    layer.alert('应用包不能为空!', {icon: 5});
                    return false;
                }
                appData = app_name;
            }else {
                if( appData_radio != app_type){
                    if(app_name == "" || app_name == null){
                        layer.closeAll('loading');
                        layer.alert('应用包不能为空!', {icon: 5});
                        return false;
                    }
                }
            }
            dimValue = '{ "appPacket" : ' + appData + ', "type" : 2 }';
        }
        obj = new Object();
        obj.campaignId = campid;
        obj.dimName = "app";
        obj.dimValue = dimValue;
        obj.deleteStatus = 0;

        campArray.push(obj);
    }


	// 投放频次 freq
	var freq_type = $("input[name='freq_type']:checked").val();
	var dimValue = "";
	if (freq_type == "0") {
		dimValue = '{ "timeInterval" : [], "freqValue" : [], "freqType" : [], "type" : 0 }';
	} else {
		var timeInterval = 24;
		var freqValue = $("#freqValue").val();
		var freqType = "pv";
		var z= /^[0-9]*$/;
		if( freqValue == "" || freqValue == null || !z.test(freqValue) ){
			layer.closeAll('loading');
			$("#custom_tip_btn").css('display','block');
			$("#freqValue").focus();
            layer.closeAll('loading');
            layer.alert('频次格式非法，请检查设置!', {icon: 5});
			return false;
		}
		dimValue = '{ "timeInterval" : ' + timeInterval
			+ ', "freqValue" : ' + freqValue
			+ ', "freqType" : "' + freqType
			+ '", "type" : 1 }';
	}

	obj = new Object();
	obj.campaignId = campid;
	obj.dimName = "freq";
	obj.dimValue = dimValue;
	obj.deleteStatus = 0;

	campArray.push(obj);

	// 屏数控制 screen
	var screen_type = $("input[name='screen_type']:checked").val();
	var dimValue = "";
	if ( screen_type == "0" ) {
		dimValue = '{"type" : 0}';
	} else if ( screen_type == "1" ) {
		dimValue = '{"type" : 1}';
	} else if ( screen_type == "2" ) {
		dimValue = '{"type" : 2}';
	}

	obj = new Object();
	obj.campaignId = campid;
	obj.dimName = "screen";
	obj.dimValue = dimValue;
	obj.deleteStatus = 0;

	campArray.push(obj);

    //操作系统
    var osType = $("input[name=osType]:checked").val();
    console.log("input[name=osType]:checked----" + osType);
    if(osType != null && osType !=""){
        obj = new Object();
        obj.campaignId = campid;
        obj.dimName = "osType";
        obj.dimValue = osType;
        obj.deleteStatus = 0;
        campArray.push(obj);
    }

	// 设备
	var deviceType = $("#device input[name=deviceTypeSelect]:checked").val();
	if (deviceType == "1") {
		var empty = false;
		var deviceList = [];
		var deviceEle = $("#deviceType-Container input[name=deviceType]:checked");
		if (deviceEle.size() > 0) {
			deviceEle.each(function(i, obj) {
				deviceList.push(parseInt($(obj).val()));
			})
		} else {
			empty = true;
		}
		if(empty){
			layer.closeAll('loading');
            layer.alert('请确保设备不为空，否则活动将不能投放!', {icon: 5});
			$("#msg_deviceType_div").show();
			return false;
		}
		dimValue = '{ "list" : [' + deviceList + '], "type" : 1 }';
		obj = new Object();
		obj.campaignId = campid;
		obj.dimName = "deviceType";
		obj.dimValue = dimValue;
		obj.deleteStatus = 0;
		campArray.push(obj);
	}
	// 设备品牌
	var brandType = $("#device input[name=brandSelect]:checked").val();
	if (brandType == "1") {
		var empty = false;
		var dataList = [];
		var dataEle = $("#deviceBrand-Container input[name=brand]:checked");
		if (dataEle.size() > 0) {
			dataEle.each(function(i, obj) {
				dataList.push('"'+$(obj).val()+'"');
			})
		} else {
			empty = true;
		}
		if(empty){
			layer.closeAll('loading');
            layer.alert('请确保设备品牌不为空，否则活动将不能投放!', {icon: 5});
			$("#msg_brand_div").show();
			return false;
		}
		dimValue = '{ "list" : [' + dataList + '], "type" : 1 }';
		obj = new Object();
		obj.campaignId = campid;
		obj.dimName = "brand";
		obj.dimValue = dimValue;
		obj.deleteStatus = 0;
		campArray.push(obj);
	}

	// 网络运营商
	var operatorType = $("#device input[name=operateTypeSelect]:checked").val();
	if (operatorType == "1") {
		var empty = false;
		var dataList = [];
		var dataEle = $("#operateType-Container input[name=operateType]:checked");
		if (dataEle.size() > 0) {
			dataEle.each(function(i, obj) {
				dataList.push(parseInt($(obj).val()));
			})
		} else {
			empty = true;
		}
		if(empty){
			layer.closeAll('loading');
            layer.alert('请确保网络运营商不为空，否则活动将不能投放!', {icon: 5});
			$("#msg_operateType_div").show();
			return false;
		}
		dimValue = '{ "list" : [' + dataList + '], "type" : 1 }';
		obj = new Object();
		obj.campaignId = campid;
		obj.dimName = "operatorType";
		obj.dimValue = dimValue;
		obj.deleteStatus = 0;
		campArray.push(obj);
	}

	// 网络类型
	var networkType = $("#device input[name=networkTypeSelect]:checked").val();
	if (networkType == "1") {
		var empty = false;
		var dataList = [];
		var dataEle = $("#networkType-Container input[name=networkType]:checked");
		if (dataEle.size() > 0) {
			dataEle.each(function(i, obj) {
				dataList.push(parseInt($(obj).val()));
			})
		} else {
			empty = true;
		}
		if(empty){
			layer.closeAll('loading');
            layer.alert('请确保网络类型不为空，否则活动将不能投放!', {icon: 5});
			$("#msg_networkType_div").show();
			return false;
		}
		dimValue = '{ "list" : [' + dataList + '], "type" : 1 }';
		obj = new Object();
		obj.campaignId = campid;
		obj.dimName = "networkType";
		obj.dimValue = dimValue;
		obj.deleteStatus = 0;
		campArray.push(obj);
	}


	return campArray;
}
