<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script src="${baseurl}assets/js/jquery-2.0.3.min.js"></script>
<script src="${baseurl}assets/js/ace-extra.min.js"></script>
<script src="${baseurl}assets/js/bootstrap.min.js"></script>
<script src="${baseurl}assets/js/typeahead-bs2.min.js"></script>
<script src="${baseurl}assets/js/jquery-ui-1.10.3.custom.min.js"></script>
<script src="${baseurl}assets/js/jquery.ui.touch-punch.min.js"></script>
<script src="${baseurl}assets/js/jquery.slimscroll.min.js"></script>
<script src="${baseurl}assets/js/jquery.easy-pie-chart.min.js"></script>
<script src="${baseurl}assets/js/jquery.sparkline.min.js"></script>
<script src="${baseurl}assets/js/flot/jquery.flot.min.js"></script>
<script src="${baseurl}assets/js/flot/jquery.flot.pie.min.js"></script>
<script src="${baseurl}assets/js/flot/jquery.flot.resize.min.js"></script>
<script src="${baseurl}assets/js/ace-elements.min.js"></script>
<script src="${baseurl}assets/js/ace.min.js"></script>
<script src="${baseurl}layer/layer.js"></script>
<script src="${baseurl}layer/laydate/laydate.js"></script>
<script src="${baseurl}js/campaignDicData.js?d=${activeUser.currentTime}"></script><!-- 根据全局数据配置文件生成的js文件 -->
<script type="text/javascript" src="${baseurl}ztree/js/jquery.ztree.core-3.5.min.js"></script>
<script type="text/javascript" src="${baseurl}ztree/js/jquery.ztree.exhide-3.5.min.js"></script>
<script type="text/javascript" src="${baseurl}js/admin/date_layer.js?d=${activeUser.currentTime}"></script>
<script type="text/javascript">

var msgConfig = {
		  //icon: 0,
		  time: 2000 //2秒关闭（如果不配置，默认是3秒）
		};

var loginNameReg = /^[a-zA-Z0-9_\-]+$/ ;
var nameReg = /^[\u0391-\uFFE5a-zA-Z0-9\(\)]+$/ ;
var nameReg2 = /^[\u0391-\uFFE5a-zA-Z\(\)]+$/ ;
var telReg = /^\d+(-\d+)?$/ ;
var urlReg = /^(http|https):\/\/[\w\-_]+(\.[\w\-_]+)+([\w\-\.,@?^=%&:/~\+#]*[\w\-\@?^=%&/~\+#])?/ ;
var urlReg2 = /^((http|https):\/\/)?[\w\-_]+(\.[\w\-_]+)+([\w\-\.,@?^=%&:/~\+#]*[\w\-\@?^=%&/~\+#])?/ ;
var emailReg = /^[a-zA-Z0-9_\-]+@[a-zA-Z0-9\-]+(\.[a-zA-Z]{2,4})+$/ ;
var numberReg = /^\d+$/ ;
var codeReg = /^[a-zA-Z0-9]+$/ ;
var imgFileReg = /^\S+.(jpg|jpeg|JPG|JPEG|png|PNG)$/;

var contextPath = "${pageContext.request.contextPath}" ;
</script>
