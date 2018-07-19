$.fn.extend({
	dateLayer:function(_startName,_endName,startDate,endDate,type){
		
		var msgConfig = {
		  icon: 0,
		  time: 2000 //2秒关闭（如果不配置，默认是3秒）
		};
		
		var text = $(this);
		
		var layerId = "date_layer" ;
		var startId = _startName ? _startName : "startDate" ;
		var endId = _endName ? _endName : "endDate" ;
		
		var html = "<div class=\"layer_date_range\" id=\"" + layerId + "\" style=\"display:none\">" ;
		html += "<div class=\"date_range_con\">" ;
		html += "<div class=\"date_range_change clearfix\">" ;
		html += "<input type=\"text\" name=\"" + startId + "\" id=\"" + startId + "\" class=\"laydate-icon\">" ;
		html += "<i>-</i>" ;
		html += "<input type=\"text\" name=\"" + endId + "\" id=\"" + endId + "\" class=\"laydate-icon\">" ;
		html += "</div>" ;
		html += "<div class=\"date_range_other\">" ;
		html += "<ul>" ;
		html += "<li id=\"" + layerId + "_today\"><i>今 天</i></li>" ;
		html += "<li id=\"" + layerId + "_yesterday\"><i>昨 天</i></li>" ;
		html += "<li id=\"" + layerId + "_recent_3_day\"><i>最近3天</i></li>" ;
		html += "<li id=\"" + layerId + "_recent_7_day\"><i>最近7天</i></li>" ;
		html += "<li class=\"clearfix\" id=\"" + layerId + "_last_week_0_to_6\"><i>上周（周日至周六）</i></li>" ;
		html += "<li class=\"clearfix\" id=\"" + layerId + "_last_week_1_to_0\"><i>上周（周一至周日）</i></li>" ;
		html += "</ul>" ;
		html += "</div>" ;
		html += "</div>" ;
		html += "<div class=\"clearfix layer_two_bt\">" ;
		html += "<input type=\"button\" value=\"确 定\" class=\"two_blue_button\" id=\"" + layerId + "_ok_button\">&nbsp;" ;
		html += "<input type=\"button\" value=\"取 消\" class=\"two_blue_reset\" id=\"" + layerId + "_cancel_button\">" ;
		html += "</div>" ;
		html += "</div>" ;
		
		text.after(html).attr("readonly","readonly").css("cursor","pointer");
		
		var datelayer = $("#" + layerId);
		var start = $("#" + startId);
		var end = $("#" + endId);
		
		var left = text.offset().left;
		var top = text.offset().top + text.outerHeight();
		//console.log(left);
		//console.log(top);
		//console.log(text.outerHeight());
		datelayer.css("left",left).css("top",top).css("z-index",100);
		
		//如果是报表页面调用
		if(type != null && type == "report"){
			//默认显示最近七天，如果有传入值，则按传入值显示
			if(startDate){
				start.val(startDate);
			}else{
				start.val(laydate.now(-6));
			}
			if(endDate){
				end.val(endDate);
			}else{
				end.val(laydate.now());
			}
		}
		//如果是其他页面调用
		else{
			//如果有传入值，则按传入值显示
			if(startDate){
				start.val(startDate);
			}
			if(endDate){
				end.val(endDate);
			}
		}
		
		//处理日期显示格式
		if(start.val() == "" && end.val() == ""){
			text.val("");
		}
		if(startDate != "" && endDate != ""){
			text.val(start.val() + "至" + end.val());
		}
		if(startDate != "" && endDate == ""){
			text.val(start.val() + "至今");
		}
		if(startDate == "" && endDate != ""){
			text.val("截止到" + end.val());
		}
		
		var concatDate = function(){
			var s = new Date(start.val());
			var e = new Date(end.val());
			if(s > e){
				layer.msg("开始日期不能大于结束日期",msgConfig);
				return false ;
			}
			//text.val(start.val() + "至" + end.val());
			if(start.val() == "" && end.val() == ""){
				text.val("");
			}
			if(start.val() != "" && end.val() != ""){
				text.val(start.val() + "至" + end.val());
			}
			if(start.val() != "" && end.val() == ""){
				text.val(start.val() + "至今");
			}
			if(start.val() == "" && end.val() != ""){
				text.val("截止到" + end.val());
			}
			
			datelayer.hide();
			
			//$("form").submit();
		};
		
		//日期控件皮肤
		// laydate.skin("huanglv");
		//开始日期控件
		laydate({
		  elem: '#' + startId,
		  format: 'YYYY-MM-DD', 
		  max: laydate.now() //最大日期
		});
		//结束日期控件
		laydate({
		  elem: '#' + endId,
		  format: 'YYYY-MM-DD', 
		  max: laydate.now(), //最大日期
		  choose: function(datas){ //选择结束日期完毕的回调
			  concatDate();
		  }
		});
		
		$("body").click(function(e){
			var event = e || window.event;
			//console.log(event);
			if (event == null || event == undefined) return;
			//console.log(e.target);
			var target = event.target || event.srcElement;
			if(target.id == text.attr("id")){
				datelayer.show();
			}
			else{
				datelayer.hide();
			}
		});
		
		/*
		$("#report_date").mouseover(function(){
			$(this).show();
		});
		$("#report_date").mouseout(function(){
			$(this).hide();
		});
		*/
		
		$("#" + layerId + "_today").click(function(){
			start.val(laydate.now());
			end.val(laydate.now());
			concatDate();
		});
		$("#" + layerId + "_yesterday").click(function(){
			start.val(laydate.now(-1));
			end.val(laydate.now(-1));
			concatDate();
		});
		$("#" + layerId + "_recent_3_day").click(function(){
			start.val(laydate.now(-2));
			end.val(laydate.now());
			concatDate();
		});
		$("#" + layerId + "_recent_7_day").click(function(){
			start.val(laydate.now(-6));
			end.val(laydate.now());
			concatDate();
		});
		//上周模式1：上上周日到上周六
		$("#" + layerId + "_last_week_0_to_6").click(function(){
			var i = 0 ;
			var d = null ;
			//一直倒数直到上周六
			while((d = new Date(laydate.now(i))).getDay() != 6){
				i-- ;
			}
			//console.log("找到上周六");
			//console.log(laydate.now(d.getTime()));
			end.val(laydate.now(d.getTime()));
			//console.log("找到上周日");
			//console.log(laydate.now(d.setDate(d.getDate()-6)));
			//再往前数6天找到上周日
			start.val(laydate.now(d.setDate(d.getDate()-6)));
			concatDate();
		});
		//上周模式2：上周一到上周日
		$("#" + layerId + "_last_week_1_to_0").click(function(){
			var i = 0 ;
			var d = null ;
			//一直倒数直到上周日
			while((d = new Date(laydate.now(i))).getDay() != 0){
				i-- ;
			}
			//console.log("找到上周日");
			//console.log(laydate.now(d.getTime()));
			end.val(laydate.now(d.getTime()));
			//console.log("找到上周一");
			//console.log(laydate.now(d.setDate(d.getDate()-6)));
			//再往前数6天找到上周一
			start.val(laydate.now(d.setDate(d.getDate()-6)));
			concatDate();
		});
		$("#" + layerId + "_ok_button").click(function(){
			concatDate();
		});
		$("#" + layerId + "_cancel_button").click(function(){
			datelayer.hide();
		});
		
	}
});

