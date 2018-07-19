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
    <title>客户账户退款</title>
    <%@ include file="/WEB-INF/commonjsp/admin_common_css.jsp"%>
    <%@ include file="/WEB-INF/commonjsp/admin_common_js.jsp"%>
</head>

<script type="text/javascript">
    $(function(){

        $("#button").click(function(){

            var amount = $("#amount").val() ;

            if(amount == "" || /^\d+(\.\d{1,2})?$/.test(amount) == false || amount == 0){
                layer.msg("请输入退款金额，最多两位小数");
                return ;
            }
            var y_balance = $("#y_balance").val();
            amount = parseFloat(amount*100).toFixed(0);
            if(y_balance-amount<0){
                layer.msg("退款金额大于退款帐户余额,请查看帐户余额再退款!");
                return ;
            }

            $.ajax({
                type:"post",
                url:"${pageContext.request.contextPath}/admin/partner/proxyRefundSave.action",
                data:{"id": ${partner.id } , "amountFen":amount },
                success:function(data){
                    if(data.msg && data.msg == "ok"){
                        layer.msg("退款成功");

                        $("#acc_balance").text(data.newAccBalance + "元");
                        $("#amount").val("");
                    }
                    else{
                        layer.msg(data.msg);
                    }
                },
                error:function(e){
                    layer.msg("退款失败");
                    console.log("error");
                    console.log(msg);
                }
            });
        });

        $("#acc_balance_refresh").click(function(){
            document.location.reload();
        });

        $("#return_btn").click(function () {
            location.href = "${pageContext.request.contextPath}/admin/partner/list.action";
        });
    });

</script>

<body class="skin-2">
<!--header 导航，搜索 -->
<jsp:include page="/WEB-INF/admin/admin_header.jsp"></jsp:include>
<div class="main-container">
    <a class="menu-toggler" id="menu-toggler" href="#"> <span
            class="menu-text"></span>
    </a>

    <jsp:include page="/WEB-INF/admin/admin_menu.jsp"></jsp:include>
    <div class="main-content">
    
    	<div class="main-container-inner">
    	
        <div class="breadcrumbs" id="breadcrumbs">
            <script type="text/javascript">
                try {
                    ace.settings.check('breadcrumbs', 'fixed');
                } catch (e) {
                }
            </script>

            <ul class="breadcrumb">
                <li><i class="icon-home home-icon"></i> <a
                        href="${pageContext.request.contextPath}">客户管理</a></li>
                <li class="active">客户账户退款</li>
            </ul>
            <!-- .breadcrumb -->

        </div>

        <div class="page-content">
            <h1>客户账户退款</h1>
            <div class="row">
                <div class="col-xs-12">

                    <form id="form" class="form-horizontal">

                        <!-- id -->
                        <input type="hidden" name="id" value="${partner.id }"/>

                        <!-- row 1 -->
                        <div class="form-group has-info">

                            <label class="col-xs-12 col-sm-3 control-label no-padding-right">公司全称</label>
                            <div class="col-xs-12 col-sm-5">
                                <label class="control-label">${partner.partnerName }</label>
                            </div>

                        </div>
                        <div class="form-group has-info">
                            <input id="y_balance" type="hidden" value="${partner.accBalance}">
                            <label class="col-xs-12 col-sm-3 control-label no-padding-right">当前余额</label>
                            <div class="col-xs-12 col-sm-1">
                                <label class="control-label" id="acc_balance">${partner.accBalanceYuan }元</label>
                            </div>
                            <div class="col-xs-12 col-sm-1">
                                <label class="control-label" id="acc_balance_refresh">
                                    <button class="btn btn-xs btn-primary icon-refresh">
                                        刷新
                                    </button>
                            </div>
                        </div>
                        <div class="form-group has-info">

                            <label class="col-xs-12 col-sm-3 control-label no-padding-right">退款金额</label>
                            <div class="col-xs-12 col-sm-9">
                                <input type="text" id="amount" name="amount" class="width-20">
                                <label class="control-label">元，最多两位小数</label>
                            </div>
                        </div>


                        <div class="clearfix form-actions">
                            <div class="col-md-offset-3 col-md-9">
                                <button class="btn btn-info" id="button" type="button">
                                    <i class="icon-ok bigger-110"></i>
                                    提交
                                </button>

                                &nbsp; &nbsp; &nbsp;
                                <button class="btn" type="reset" id="return_btn">
                                    <i class="icon-undo bigger-110"></i>
                                    返回
                                </button>
                            </div>
                        </div>
                    </form>

                </div>
                <!-- /.col -->
            </div>
        </div>
        <!-- /.page-content -->
        </div>
    </div>
</div>

</body>
</html>