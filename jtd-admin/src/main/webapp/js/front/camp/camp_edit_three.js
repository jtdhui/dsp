
// 上传文件
function ajaxFileUpload() {
	layer.load(2);  //加载层
	// 开始上传文件时显示一个图片
	$("#wait_loading").ajaxStart(function() {
		$(this).show();
		// 文件上传完成将图片隐藏起来
	}).ajaxComplete(function() {
		$(this).hide();
	});

	var camp = new Object();
	camp.id = $('#id').val();
	camp.landingPage = $('#landingPage').val();
	camp.pvUrls = $('#pvUrls').val();
	camp.clickUrl = $('#clickUrl').val();

	var elementIds = [ "flag" ]; // flag为id、name属性名
	$.ajaxFileUpload({
		url : 'fileUpload.action?campId=' + camp.id,
		type : 'post',
		secureuri : false, // 一般设置为false
		fileElementId : 'file', // 上传文件的id、name属性名
		dataType : 'application/json', // 返回值类型，一般设置为json、application/json
		contentType : 'application/json',
		data : JSON.stringify(camp),
		headers : {
			'Content-Type' : 'application/json;charset=utf-8'
		},
		elementIds : elementIds, // 传递参数到服务器
		success : function(data, status) {
			layer.closeAll('loading');
			if(status){
				layer.open({
					content: data,
					time: 0, //不自动关闭
					btn: ['确定', '取消'],
					yes: function (index) {
						layer.close(index);
						location.href = "camp_edit.action?id=" + camp.id + "&step=3";
					}
				});
			}
		},
		error : function(data, status, e) {
			layer.alert(e);
		}
	});
};

function deleteCreative(id,camp_id){
	layer.load(2);  //加载层
	layer.open({
		content: '你确定执行此操作么？',
		time: 0, //不自动关闭
		btn: ['确定', '取消'],
		yes: function (index) {
			layer.close(index);
			$.ajax({
				url: "ceative_delete.action?camp_id=" + camp_id + "&id=" + id,
				type: "post",
				contentType: "application/json;charset=utf-8",
				dataType: "json",
				success: function (data) {
					layer.closeAll('loading');
					location.href = data.url;
				}
			});
		}
	});
}

/**
 * 验证广告信息
 */
function validateAdInfo() {

    var camp = new Object();

    var phoneTitle = $("#phoneTitle").val();
    if(phoneTitle == null || phoneTitle == ""){
        layer.closeAll('loading');
        layer.alert("标题不能为空!");
        return false;
    }
    camp.phoneTitle = phoneTitle;

    var phonePropaganda = $("#phonePropaganda").val();
    if(phonePropaganda == null || phonePropaganda == ""){
        layer.closeAll('loading');
        layer.alert("广告语不能为空!");
        return false;
    }
    camp.phonePropaganda = phonePropaganda;

    var phoneDescribe = $("#phoneDescribe").val();
    if(phoneDescribe == null || phoneDescribe == ""){
        layer.closeAll('loading');
        layer.alert("广告描述不能为空!");
        return false;
    }
    camp.phoneDescribe = phoneDescribe;

    var priceReg = new RegExp("^[0-9]+(.[0-9]{0,2})?$");

    var originalPrice = $("#originalPrice").val();
    if(originalPrice == null || originalPrice == "" || ! priceReg.test(originalPrice)){
        layer.closeAll('loading');
        layer.alert("原价不能为空,或者格式不对!");
        return false;
    }
    camp.originalPrice = originalPrice;

    var discountPrice = $("#discountPrice").val();
    if(discountPrice == null || discountPrice == "" || ! priceReg.test(discountPrice)){
        layer.closeAll('loading');
        layer.alert("折后价不能为空!");
        return false;
    }
    camp.discountPrice = discountPrice;

    var salesVolume = $("#salesVolume").val();
    if(salesVolume == null || salesVolume == ""){
        layer.closeAll('loading');
        layer.alert("销量不能为空!");
        return false;
    }
    camp.salesVolume = salesVolume;

    var optional = $("#optional").val();
    if(optional == null || optional == ""){
        layer.closeAll('loading');
        layer.alert("选填不能为空!");
        return false;
    }
    camp.optional = optional;

    var deepLink = $("input[name='deepLink']:checked").val();

    if(deepLink == null || deepLink == ""){
        layer.closeAll('loading');
        layer.alert("启动Deeplink打开不能为空!");
        return false;
    }
    camp.deepLink = deepLink;

    var monitor = $("#monitor").val();
    if(monitor == null || monitor == ""){
        layer.closeAll('loading');
        layer.alert("设置监控不能为空!");
        return false;
    }
    camp.monitor = monitor;

    var phoneDeepLinkUrl = $("#phoneDeepLinkUrl").val();
    if( phoneDeepLinkUrl == null || phoneDeepLinkUrl == "" ){
        layer.closeAll('loading');
        layer.alert("Deeplink目标地址不能为空!");
        return false;
    }
    camp.phoneDeepLinkUrl = phoneDeepLinkUrl;

    return camp;
}

$(function() {

	//全选,全不选
	$("#check_btn").click(function() {

		var flag = $("#check_btn")[0].checked;
		if (!flag) { // 全不选
			$("[name='creative_check']").prop("checked", false);
		} else { // 全选
			$("[name='creative_check']").prop("checked", true);
		}
	});
	
	//批量删除
	$("#delete_size_btn").click(function(){
		layer.load(2);  //加载层
		var chkt=0;
		var creative_id = "";
		var camp_id = $('#id').val();
		$("input:checkbox[name='creative_check']").each(function() {
	        if ($(this)[0].checked ==true) {
	        	chkt++; 
	        	creative_id += $(this).val()+",";
	        }
		});
		if(chkt<1){
			layer.closeAll('loading');
			layer.alert("请选择创意素材!");
			return false;
		}else{
			layer.open({
				content: '你确定执行此操作么？',
				time: 0, //不自动关闭
				btn: ['确定', '取消'],
				yes: function (index) {
					layer.close(index);
					$.ajax({
						url: "ceative_delete.action?camp_id=" + camp_id + "&id=" + creative_id,
						type: "post",
						contentType: "application/json;charset=utf-8",
						dataType: "json",
						success: function (data) {
							location.href = data.url;
						}
					});
				}
			});
		}
		
	});

	// 下一步提交 begin
	$("#save_camp_btn_3").click(function() {

		layer.load(2);  //加载层
		setTimeout(function(){
			layer.closeAll('loading');
		}, 1300);

		var camp = new Object();

		camp.id = $('#id').val();
        var landingPage = $('#landingPage').val();
		if(landingPage == null || landingPage == ""){
			layer.closeAll('loading');
			layer.alert("落地页链接不能为空!");
			return false;
		}

		camp.landingPage = landingPage;

		var pvUrls = $('#pvUrls').val();

        if(pvUrls == null || pvUrls == ""){
            camp.pvUrls = "";
        }else{
            var pvArray = pvUrls.split("\n");
            camp.pvUrls = JSON.stringify(pvArray);
        }

		var clickUrl = $('#clickUrl').val();
		if(clickUrl == null || clickUrl == ""){
			layer.closeAll('loading');
			layer.alert("点击链接不能为空!");
			return false;
		}
		camp.clickUrl = clickUrl;

        // 验证应用信息
        // var val_obj = validateAdInfo();
        // if(val_obj == false){
        //     return false;
        // }
        // var campObj = $.extend(camp,val_obj);
        var campObj = camp;
		var channelIds = "";
		$("input[name='channelCheck']", $('#channel-sets')).each(function() {
			var channel_id = $(this).val();
			var selectVal = $("#channel-select" + channel_id).val();
            if(selectVal == null || selectVal == ""){
                selectVal = "0";
            }
			var channel_catgserial = $("#channel_catgserial"+ channel_id).val();
			channelIds += channel_id + "$$$"+ selectVal + "$$$" + channel_catgserial + ",";
		});

		if (channelIds.length > 1) {
			channelIds = channelIds.substring(0,channelIds.length - 1);
		}

		var chkt=0;
		$("input:checkbox[name='creative_check']").each(function() {
			chkt++;
		});
		if(chkt<1){
			layer.closeAll('loading');
			layer.alert("请上传创意后再保存!");
			return false;
		}

		$.ajax({
			url : "saveCampThree.action?campaignId=" + campObj.id+ "&dimValue=" + channelIds,
			type : "post",
			contentType : "application/json;charset=utf-8",
			dataType : "json",
			data : JSON.stringify(campObj),
			success : function(data) {
				layer.closeAll('loading');
				location.href = data.url;
			}
		});
	});
	// 下一步提交 end
});