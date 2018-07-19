$(function() {

    init();
    //保存
    saveUser();
});

function  getPath(){
    var pathName = document.location.pathname;
    var index = pathName.substr(1).indexOf("/");
    var result = pathName.substr(0,index+1);
    return result;
}

function init(){

    var setting = {		//ztree配置选项
        async: {
            enable:true,
            url:getPath()+"/admin/partner/partnerTree.action",
            autoParam:["id"],
            dataFilter: null
        },
        view: {
            showIcon:false,
            fontCss: { 'font-family': '微软雅黑' },
            dblClickExpand: false
        },
        data: {
            simpleData: {
                enable: true
            }
        },
        callback: {
            beforeClick: beforeClick,
            onClick: onClick
        }
    };

    zTreeObj = $.fn.zTree.init($("#treeDemo"), setting);

    $("#menuBtn").click(showMenu);
    $("#menuContent").css("background","#f0f6e4");
}

function beforeClick(treeId, treeNode) {
    var check = (treeNode && !treeNode.isParent);
    return true;
}

function onClick(e, treeId, treeNode) {
    var zTree = $.fn.zTree.getZTreeObj("treeDemo"),
        nodes = zTree.getSelectedNodes(),
        v = "";
        partnerId = "";
    nodes.sort(function compare(a,b){return a.id-b.id;});
    for (var i=0, l=nodes.length; i<l; i++) {
        v += nodes[i].name + ",";
    }
    if (v.length > 0 ) {
        v = v.substring(0, v.length-1);
        partnerId = nodes[0].id;
    }
    var cityObj = $("#partner-select");
    cityObj.attr("value", v);

    $("#partnerId").val(partnerId);
    setSelectVal();
}

function showMenu() {
    var cityObj = $("#partner-select");
    var cityOffset = $("#partner-select").offset();
    $("#menuContent").css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");

    $("body").bind("mousedown", onBodyDown);
}
function hideMenu() {
    $("#menuContent").fadeOut("fast");
    $("body").unbind("mousedown", onBodyDown);
}
function onBodyDown(event) {
    if (!(event.target.id == "menuBtn" || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length>0)) {
        hideMenu();
    }
}

function treeSelect(id) {
    var flag = $("#check_"+id)[0].checked;
    if (!flag) { // 全不选
        $("[name='checkPartner_"+id+"']").prop("checked",false);
    } else { // 全选
        $("[name='checkPartner_"+id+"']").prop("checked",true);
    }

}

//所属公司切换时加载公司数据
function setSelectVal(){
    var partnerId = $("#partnerId").val();
    var userId = $("#id").val();
    $("#partner_list").html("");
    $("#role-select").html("");
    $.ajax({
        url:"../partner/listTree.action?id="+partnerId+"&userId="+$("#id").val(),
        type:"post",
        contentType:"application/json;charset=utf-8",
        success:function(result){
            if(result.success){
                //广告主列表
                $("#partner_list").html("");
                $.each(result.partnerList, function (index, obj) {
                    //广告主列表
                    var sel = "";
                    var disabled = "";
                    var flag = false;

                    $.each(result.upList, function (index, vo) {
                        if (vo.partnerId == obj.id || userId == 1) {
                            flag = true;
                        }
                    });

                    if (partnerId == obj.id) { // 用户所在的公司必选且不可编辑
                        flag = true;
                        disabled = 'disabled';
                    }
                    if (userId == 1 || (obj.pid != partnerId && obj.pid !=0) ) { //用户不能跨级操作子公司,只可能查看, 如果是选择的广告是中企,则显示所有根结点
                        disabled = 'disabled';
                    }
                    if (flag) {
                        sel = 'checked="checked"';
                    }
                    var level = obj.level;
                    var levelStr = ' style="margin-left:'+(level-1)*18+'px"';

                    if( (level == 2 && obj.pid ==1 )|| (level ==1 && obj.pid ==0 && obj.id != partnerId )){
                        disabled = '';
                    }

                    var key_s = "";
                    if(level>2 || (level == 2 && obj.pid > 1 )){
                        key_s = "_"+obj.key_id;
                    }

                    var htmlStr ='<tr> <td><i class="tree_icon" '+levelStr+'></i> <input id="check_'+obj.id+'" name ="checkPartner'+key_s+'" onclick="treeSelect('+obj.id+')"  class="ace"  type="checkbox" '+disabled+' value="'+obj.id+'" '+ sel + '/>'
                    htmlStr+='<span class="lbl"></span><label style="margin-left:10px;">'+obj.id+'</label></td> <td>'+ obj.partner_name + '</td> ';
                    if (obj.flag) {
                        htmlStr += '<td>是</td>';
                    } else {
                        htmlStr += '<td>否</td>';
                    }

                    if(level>2 || (level == 2 && obj.pid > 1 )){

                    }else {
                        htmlStr+='<input type="hidden" id="other_flag_'+obj.id+'" value="'+obj.flag+'"/>';
                    }
                    htmlStr += '</tr>';
                    $("#partner_list").append(htmlStr);
                    //角色列表
                    var roleHtml = '<option value="0" >请选择角色</option>';
                    $.each(result.roleTypeList, function (index, obj) {
                        roleHtml += '<option value="' + obj.code + '" >' + obj.desc + '</option>';
                    });
                    $("#role-select").html(roleHtml);
                });
            }
        }
    });
}

/**角色选择事件*/
function setRoleSelectVal() {
    $("[name^='checkPartner']").prop("checked",false);
    var selectVal = $("#role-select").val();
    if(selectVal ==8 || selectVal==9 || selectVal ==10 ){
        $("[name='checkPartner']").prop("checked",true);
    }
    var selectVal = $("#partnerId").val();
    $("input[name='checkPartner']").each(function(){
        if(selectVal == $(this).val()){
            $(this).prop("checked",true);
        }
    });

    $("input[name='checkPartner']:checked",$('#partner-table')).each(function(){
        var partnerId =  $(this).val();
        $("[name='checkPartner_"+partnerId+"']").prop("checked",true);
    });
}

/**保存*/
function saveUser() {

    //用户提交
    $('#user_submit').click(function(){

        layer.load(2);  //加载层

        if($("#loginName").val()==""){
            layer.closeAll('loading');
            layer.msg("请填写登录名",msgConfig);
            return ;
        }
        if(loginNameReg.test($("#loginName").val()) == false){
            layer.closeAll('loading');
            layer.msg("登录名只能填写英文、数字、下划线、横杠",msgConfig);
            return ;
        }

        if($("#pwd").val()==""){
            layer.closeAll('loading');
            layer.msg("请填写密码",msgConfig);
            return ;
        }
        var rule = "密码必须是数字字母结合，长度在6-20之间" ;
        if($("#pwd").val().match(/^.{6,20}$/) == false){
            layer.closeAll('loading');
            layer.msg(rule,msgConfig);
            return ;
        }
        if($("#pwd").val().match(/^\d+$/)){
            layer.closeAll('loading');
            layer.msg(rule,msgConfig);
            return ;
        }
        if($("#pwd").val().match(/^[a-zA-Z]+$/)){
            layer.closeAll('loading');
            layer.msg(rule,msgConfig);
            return ;
        }

        if($("#userName").val()==""){
            layer.closeAll('loading');
            layer.msg("请填写用户姓名",msgConfig);
            return ;
        }
        if(nameReg2.test($("#userName").val()) == false){
            layer.closeAll('loading');
            layer.msg("用户姓名只能填写中英文",msgConfig);
            return ;
        }

        if($("#email").val()==""){
            layer.closeAll('loading');
            layer.msg("请填写邮箱地址",msgConfig);
            return ;
        }
        if(emailReg.test($("#email").val()) == false){
            layer.closeAll('loading');
            layer.msg("邮箱地址格式错误",msgConfig);
            return ;
        }

        if($("#mobile").val()==""){
            layer.closeAll('loading');
            layer.msg("请填写手机号",msgConfig);
            return ;
        }
        if(/^\d{11}$/.test($("#mobile").val()) == false){
            layer.closeAll('loading');
            layer.msg("手机号格式错误",msgConfig);
            return ;
        }
        if($("#tel").val() != ""){
            if(telReg.test($("#tel").val()) == false){
                layer.msg("固定电话格式错误",msgConfig);
                return ;
            }
        }
        if($("#fax").val() != ""){
            if(telReg.test($("#fax").val()) == false){
                layer.closeAll('loading');
                layer.msg("传真电话格式错误",msgConfig);
                return ;
            }
        }
        if($("#qq").val() != ""){
            if(numberReg.test($("#qq").val()) == false){
                layer.closeAll('loading');
                layer.msg("qq号只能填写数字",msgConfig);
                return ;
            }
        }

        var obj=new Object();
        obj.partnerId=$("#partnerId").val();
        obj.loginName=$("#loginName").val();
        obj.pwd=$("#pwd").val();
        obj.userName=$("#userName").val();
        obj.tel=$("#tel").val();
        obj.fax=$("#fax").val();
        obj.qq=$("#qq").val();
        obj.mobile=$("#mobile").val();
        obj.email=$("#email").val();

        if(obj.partnerId==0 || obj.partnerId=='' || obj.partnerId==null){
            layer.closeAll('loading');
            layer.msg("请选择用户所属的广告公司!",msgConfig);
            return false;
        }

        //封装用户与广告主数据
        var partnerIds = "";
        var partner_flag = false;
        var partner_flag_ids = "";
        $("input[name='checkPartner']:checked",$('#partner-table')).each(function(){
            partnerIds +=  $(this).val() +",";
            if($("#other_flag_"+$(this).val()).val() == "true"){
                partner_flag = true;
                partner_flag_ids += $(this).val() +",";
            }
        });

        var role_val = $("#role-select").val();
        if(partner_flag && role_val == 2){
            partner_flag_ids = partner_flag_ids.substring(0,partner_flag_ids.length-1);
            layer.alert("该广告主ID["+partner_flag_ids+"]已分配给其它运营人员,请选择其它广告主!");
            layer.closeAll('loading');
            return false;
        }

        if(partnerIds.length>1){
            partnerIds = partnerIds.substring(0,partnerIds.length-1);
        }else{
            layer.closeAll('loading');
            layer.msg("请选择授权的广告公司!",msgConfig);
            return;
        }

        $.ajax({
            url:"addsubmit.action?roleId="+role_val,
            type:"post",
            contentType:"application/json;charset=utf-8",
            //请求json数据,使用json表示用户信息
            data:JSON.stringify(obj),
            success:function(data){
                if(data.success){
                    $.ajax({
                        url:"../userPartner/save.action?partnerId="+partnerIds+"&userId="+data.userId,
                        type:"post",
                        contentType:"application/json;charset=utf-8",
                        //请求json数据,使用json表示用户与广告主信息
                        data:JSON.stringify(obj),
                        success:function(result){
                            layer.closeAll('loading');
                            if(result.success){
                                location.href = data.url;
                            }else{
                                layer.msg("用户关联广告主失败!",msgConfig);
                                return false;
                            }
                        }
                    });
                }else{
                    layer.closeAll('loading');
                    layer.msg(data.msg,msgConfig);
                    return false;
                }
            }
        });
    });
}