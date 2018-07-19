var hiddenNodes=[];	//用于存储被隐藏的结点

$(function(){
	init();
})

function init(){

    var fromPage = $("#fromPage").val();
	var setting = {		//ztree配置选项
        async: {
            enable: true,
            url: baseurl +"front/partnerList.action?fromPage="+fromPage,
            autoParam:["id"],
            dataFilter: null
        },
        view: {
            showIcon: showIconForTree,
            fontCss: { 'font-family': '微软雅黑' }
        },
        data: {
            simpleData: {
                enable: true
            }
        }
	};

	zTreeObj = $.fn.zTree.init($("#partner_list_id"), setting);

	$("#name_search_btn").click(filter);
};

function showIconForTree(treeId, treeNode) {
    return true;
};

//过滤ztree显示数据
function filter(){
	//显示上次搜索后背隐藏的结点
	zTreeObj.showNodes(hiddenNodes);

	//查找不符合条件的叶子节点
	function filterFunc(node){
		var _keywords=$("#pName").val();
		if(node.isParent||node.name.indexOf(_keywords)!=-1) return false;
		return true;		
	};

	//获取不符合条件的叶子结点
	hiddenNodes=zTreeObj.getNodesByFilter(filterFunc);
	
	//隐藏不符合条件的叶子结点
	zTreeObj.hideNodes(hiddenNodes);
};