$(function() {

    init();
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
            url: contextPath + "/admin/partner/fatherPartnerTree.action",
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
