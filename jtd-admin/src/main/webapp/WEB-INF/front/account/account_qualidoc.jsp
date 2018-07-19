<%@page import="java.net.URLDecoder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ include file="/WEB-INF/commonjsp/tag.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta HTTP-EQUIV="pragma" CONTENT="no-cache">
    <meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
    <meta HTTP-EQUIV="expires" CONTENT="0">
<title>资质上传</title>
<%@ include file="/WEB-INF/commonjsp/common_css.jsp"%>
<%@ include file="/WEB-INF/commonjsp/common_js.jsp"%>
<link rel="stylesheet" href="${baseurl}dist/less/account-toQualiDoc.css">

<script type="text/javascript">

	$(function(){

        <%--$("#name_search_btn").click(function () {--%>
            <%--var partnerName = $("#pName").val();--%>
            <%--location.href =  "${baseurl }front/account/toQualiDoc.action?partnerName="+partnerName;--%>
        <%--});--%>
		
		//如果用户的资质客户类型已经确定，则直接加载资质上传列表，并将已经上传过的资质显示出来
		//加载主体资质
		loadQualiDocType("${qualiDocMainCustomerType}",true);
		//加载可选资质
		loadQualiDocType("${qualiDocOptionalCustomerType}",false);
		
		$("#qualiDocMainCustomerType").change(function(){
			//console.log($(this).val());
			loadQualiDocType($(this).val(),true);
		});
		$("#qualiDocOptionalCustomerType").change(function(){
			//console.log($(this).val());
			loadQualiDocType($(this).val(),false);
		});
		
		$("input[type=file]").change(function(e){
			showFileNameOnChanged(this,$(this).attr("name"));
		});
		
		$("#reset_button").click(function(){
			window.location.href = "${pageContext.request.contextPath}/front/account/toQualiDoc.action" ;
		});
		
		$("#login_button").click(function(){
			
			if($("#qualiDocMainCustomerType option:selected").val() == ""){
				layer.msg("请选择主体资质客户类型",{icon:0,time:1500});	
				return ;
			}
			//得到选中的主体资质类型
			var selectedMainDocType = $("#qualiDocMainCustomerType option:selected").val() ;
			//如果相应资质的删除链接不存在，也就是说用户之前没有上传过相关资质，提交前就需要检查用户是否已选择好要上传的文件
			if($("#" + selectedMainDocType + "_dellink").length == 0){
				if($("input[type=file][name='" + selectedMainDocType + "_file']").val() == ""){
					layer.msg("请选择要上传的主体资质图片",{icon:0,time:1500});	
					return ;
				}
			}
			
			
			//loading层
			layer.open({
				  type: 3,
				  icon: 2,
				  content: ''
			});
			
			//setTimeout("",500);
			$('form').submit();
			
		});
		<%
			String msg = request.getParameter("msg");
			if( msg != null && msg != ""){
				msg = URLDecoder.decode(msg, "utf-8");
		%>
			setTimeout("layer.msg('<%=msg%>',{icon:1,time:1500});",500);
		<%
			}
			//if(msg != null && msg.equals("deleteOk")){
		%>
			//setTimeout("layer.msg(\"资质删除成功\",{icon:1,time:1500});",500);
			//setTimeout(reload,1500);
		<%
			//}
		%>
		
	});
	
	//选中一个文件后，在上传按钮下显示该文件的名称
	function showFileNameOnChanged(obj,inputerName){
		
		//console.log(inputerName);
		//console.log($(obj));
		//console.log($(obj).attr("name"));
		
		$(obj).parent().parent().parent().find("[for='" + inputerName + "']").remove();
		var html = "" ;
		html += "<div class=\"upload-img\">" ;
		html += "<i class=\"img-logo clearfix\"></i>" ;
		html += "<span class=\"img-info\">" ;
		html += $(obj).val() + "(待上传)";
		html += "</span>" ;
		html += "<a href=\"javascript:;\" class=\"img-handle img-del\" onclick=\"clearFileInput('" + inputerName + "')\">删除</a>" ;
		html += "</div>" ;
		
		$(obj).parent().parent().after(html);
		
	}
	//清除选中的待上传的文件
	function clearFileInput(inputerName){
		
		$("div[for='" + inputerName + "']").remove();
		$("input[name='" + inputerName + "']").val("");
		
	}
	
	/**
	* qualiDocCustomerTypeId 资质客户类型id
	* isMain 是否为加载主体资质
	*/
	function loadQualiDocType(qualiDocCustomerTypeId,isMain){
		if(qualiDocCustomerTypeId != ""){
			
			layer.open({
				  type: 3,
				  icon: 2,
				  content: ''
			});
			
			$.ajax({
				type: "post",
	            url: "${pageContext.request.contextPath}/front/account/ajaxGetDocTypeList.action",
	            data: {"qualiDocCustomerTypeId" : qualiDocCustomerTypeId},
	            success: function (data) {
	            	//console.log(data);
	            	
	            	layer.closeAll();
	            	
	            	//从后台得到的用户已经上传过的资质列表
	            	var uploadedDocInfo = {docList : ${uploadedDocListJson} } ; //使用json是为了避免js出错和方便判断已上传数据是否为空
	            	
	            	//将拼好的html显示在主体/可选资质区域
	            	if(isMain){
	            		$("#mainQualiDocUpload").html("");
            		}
            		else{
            			$("#optionalQualiDocUpload").html("");
            		}
	            	
	            	if(data != null){
	            		var html = "";
	            		
	            		//遍历从后台得到的资质类型
	            		for(var i = 0 ; i < data.length ; i++){
	            			
	            			//当前文档类型对象
	            			var docType = data[i];
	            			
	            			//查看用户是否已经上传过当前类型的资质
	            			var uploadedDocName = "" ;
	            			var uploadedDocId = "" ;
	            			var uploadedDocNumber = "" ;
	            			var uploadedDocValidDate = "" ;
	            			if(uploadedDocInfo["docList"] != null){
	            				var docList = uploadedDocInfo["docList"] ;
		            			for(var n = 0 ; n < docList.length ; n++){
		            				var uploadedDoc = docList[n];
		            				if(uploadedDoc.doc_type_id == docType.id){
		            					uploadedDocName = uploadedDoc.doc_old_name ;
		            					uploadedDocId = uploadedDoc.id ;
		            					uploadedDocNumber = uploadedDoc.doc_number ;
		            					uploadedDocValidDate = uploadedDoc.doc_valid_date ;
		            					break ;
		            				}
		            			}
	            			}
	            			
	            			//开始拼装资质上传列表的html
	            			html += "<div class=\"form-group\">" ;
	            			html += "<div class=\"papers clearfix\">" ;
	            			html += "<span class=\"papers_tit\">" + docType.docTypeName + "</span>" ;
	            			
	            			//如果当前类型上传过的资质，则按钮显示为重传（如果存在审核中和已通过的渠道则不能重传）
	            			if(uploadedDocId != ""){
	            				var disabled = "" ;
	            				//在后台会判断是否能修改或删除资质(1为可以修改)
	            				if("${canUpdateQualiDoc}" == "0"){
	            					$("#qualiDocMainCustomerType").attr("disabled","disabled");
	            					$("#qualiDocOptionalCustomerType").attr("disabled","disabled");
	            					disabled = "disabled" ;
	            				}
	            				html += "<a href=\"javascript:;\" class=\"btn btn-small-s btn-main papers_file\">" ;
            					html += "<input type=\"file\" id=\"" + docType.id + "_dellink\" name=\"" + docType.id + "_file\" " + disabled + ">" + (disabled == "" ? "重传":"不可改动") ;
            					html += "</a>" ;
	            			}
	            			else{
	            				//如果当前类型没有上传过任何资质，则任何时候可以上传
	            				html += "<a href=\"javascript:;\" class=\"btn btn-small-s btn-main papers_file\">" ;
	            				html += "<input type=\"file\" id=\"doc\" name=\"" + docType.id + "_file\">上传" ;
	            				html += "</a>" ;
	            			}
	            			
            				html += "<input type=\"text\" class=\"input_text\" placeholder=\"请输入证书号\" name=\"" + docType.id + "_number\" value=\"" + uploadedDocNumber + "\" " + disabled + ">" ;
            				html += "<input type=\"text\" class=\"input_text\" placeholder=\"证书有效期\" name=\"" + docType.id + "_valid_date\" value=\"" + uploadedDocValidDate + "\"" + disabled + ">" ;
            				html += "</div>" ; //<div class=\"papers clearfix\">

            				//如果uploadedDocId不为空则表示当前类型存在已上传资质，显示已上传内容，并且带删除按钮（如果存在审核中和已通过的渠道则不能删除）
            				if(uploadedDocId != ""){
            					html += "<div class=\"upload-img\">" ;
            					html += "<i class=\"img-logo clearfix\"></i>" ;
            					html += "<span class=\"img-info\">" + uploadedDocName + "(已上传)" ;
            					html += "</span>"
            					html += "<a href=\"${pageContext.request.contextPath}/admin/qualidoc/showImage.action?id=" + uploadedDocId +"\" class=\"img-handle img-look\" target=\"_blank\">查看</a>";
            					//从后台会发来是否能修改或删除资质的标记
            					if("${canUpdateQualiDoc}" == "1"){
            						html += "<a href=\"javascript:;\" class=\"img-handle img-del\" id=\"" + docType.id + "_dellink\" onclick=\"deleteQualiDoc(" + docType.id + ")\">删除</a>" ;
            					}
            					html += "</div>" ;
            				}
            				
            				html += "</div>" ;  //<div class=\"form-group\">
	            		}
	            		//将拼好的html显示在主体/可选资质区域
	            		if(isMain){
		            		$("#mainQualiDocUpload").html(html);
		            		//为新生成的上传按钮绑定事件
		            		$("#mainQualiDocUpload input[type=file]").change(function(e){
		            			showFileNameOnChanged(this,$(this).attr("name"));
		            		});
	            		}
	            		else{
	            			$("#optionalQualiDocUpload").html(html);
	            			//为新生成的上传按钮绑定事件
	            			$("#optionalQualiDocUpload input[type=file]").change(function(e){
		            			showFileNameOnChanged(this,$(this).attr("name"));
		            		});
	            		}
	            		
	            	}
	            },
	            error: function (msg) {
	            	console.log("error");
	            	console.log(msg);
	            }
			});
			
		}
	}
	
	function deleteQualiDoc(qualiDocTypeId){
		
		layer.confirm("您确认删除此资质？",function(){
			document.location.href = "${pageContext.request.contextPath}/front/account/deleteQualiDoc.action?qualiDocTypeId=" + qualiDocTypeId ;
		});
		
	}
	
</script>

</head>
<body>
	<!--header 导航 -->
	<jsp:include page="/WEB-INF/front/header.jsp?param=account"></jsp:include>
	<!--index content-->
    
    <div class="wrapper">
        <!-- 当前位置 -->
        <div class="current-position row">
            <div class="col-sm-6">
                <ol class="breadcrumb">
                    <li class="breadcrumb-list">
                        <i class="icons-current"></i>当前位置
                    </li>
                    <li class="breadcrumb-list">
                        <a class="nav-breadcrumb" href="home.html">首页</a>
                        <i class="fa fa-angle-right"></i>
                    </li>
                    <li class="breadcrumb-list">
                        <a class="nav-breadcrumb" href="account.html">账户管理</a>
                        <i class="fa fa-angle-right"></i>
                    </li>
                    <li class="breadcrumb-list">
                        <a class="nav-breadcrumb active">资质上传</a>
                    </li>
                </ol>
            </div>
            
            <jsp:include page="/WEB-INF/front/selectPartner.jsp?fromPage=toQualiDoc"></jsp:include>
            
        </div>
        <!-- 二级连接 -->
        <div class="second-links-wrap">
            <a class="s-links" href="${pageContext.request.contextPath}/front/account/toInfo.action">账号信息</a>
            <a class="s-links active" href="${pageContext.request.contextPath}/front/account/toQualiDoc.action">资质上传</a>
            <a class="s-links" href="${pageContext.request.contextPath}/front/account/toFinance.action">账务明细</a>
            <a class="s-links" href="${pageContext.request.contextPath}/front/account/toGetCode.action">代码获取</a>
        </div>
        <!-- 操作窗 -->
        <div class="handle-view">
            <div class="q-status">
                <p class="q-title">渠道审核状态</p>
                <p style="margin:10px auto ;width:500px;font-size:14px;color:red;">
                	<c:if test="${not empty rejectReason }">* 您的资质未通过我方系统内部审核，原因为：${rejectReason }
                	</c:if>
                </p>
                <table class="table table-bordered">
                    <thead>
                    <tr>
                        <th width="10%">渠道</th>
                        <th width="40%">资质要求</th>
                        <th width="10%">审核状况</th>
                        <th width="40%">审核信息</th>
                    </tr>
                    <tbody>
	                    <c:forEach items="${partnerStatusList }" var="statusMap" varStatus="vs">
	                    	<tr>
		                        <td>${statusMap.channel_name }</td>
		                        <td>${statusMap.remark }</td>
		                        <td>
		                            <c:if test="${ empty statusMap.status }">
		                                <span style="color:#000000">未提交</span>                                                  
		                            </c:if>
		                            <c:if test="${ statusMap.status == 1 }">
		                                <span style="color:#FF9933">待审核</span>                                                          
		                            </c:if>
		                            <c:if test="${ statusMap.status == 2 }">
		                                <span style="color:#009966">通过</span>                                                           
		                            </c:if>
		                            <c:if test="${ statusMap.status == 3 }">
		                                <span style="color:#CC0033">拒绝</span>                                                           
		                            </c:if>
		                            <c:if test="${ statusMap.status == 4 }">
		                                <span style="color:#CC0033">提交失败</span>                                                         
		                            </c:if>
		                        </td>
		                        <td>${statusMap.audit_info }</td>
		                    </tr>
	                    </c:forEach>
                    </tbody>
                </table>
            </div>
            <div class="q-upload">
                <p class="q-title">资质上传</p>
                
                <form name="userForm1" action="${pageContext.request.contextPath}/front/account/saveQualiDoc.action" enctype="multipart/form-data" method="post">
                
                <div class="row">
                    <div class="col-sm-12">
                        <div class="form-horizontal">
                            <div class="form-group">
                                <p class="title">请您务必完善贵公司的资质信息，以便各方渠道进行审核（请上传jpg/png格式的图片文件，大小不超过1.5M）</p>
                            </div>
                            <div class="form-group">
                                <p class="title">主体资质：</p>
                                <select class="select-box" name="qualiDocMainCustomerType" id="qualiDocMainCustomerType">
                                    <option value="">请选择主体资质客户类型</option>
                                    <c:forEach items="${mainCustomerTypeList }" var="po">
                                        <option value="${po.id }" <c:if test="${qualiDocMainCustomerType == po.id }">selected</c:if>>${po.customerTypeName }</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <!-- ajax查询主体资质 -->
                            <div id="mainQualiDocUpload">

							</div>
                            <!-- icp备案 -->
                            <div class="form-group">
                                <div class="papers clearfix">
                                    <span class="papers_tit">ICP备案证书：</span>
                                    <a href="javascript:;" class="btn btn-small-s btn-main papers_file">
                                        <input type="file" name="icp_file">
                                        <c:if test="${empty icpUploadedDoc }">上传</c:if>
                                        <c:if test="${not empty icpUploadedDoc }">重传</c:if>
                                    </a>
                                    <input type="text" class="input_text" placeholder="请输入证书号" name="icp_number"
                                           value="${icpUploadedDoc.doc_number }">
                                    <input type="text" class="input_text" placeholder="证书有效期" name="icp_valid_date"
                                           value="${icpUploadedDoc.doc_valid_date }">
                                </div>
                                <c:if test="${not empty icpUploadedDoc }">
	                                <div class="upload-img">
	                                    <i class="img-logo clearfix"></i>
	                                    <span class="img-info">${icpUploadedDoc.doc_old_name }(已上传)</span>
	                                    <a href="${pageContext.request.contextPath}/admin/qualidoc/showImage.action?id=${icpUploadedDoc.id }" class="img-handle img-look" target="_blank">查看</a>
	                                    <shiro:hasPermission name="user:operable">
	                                    	<a href="javascript:void(0)" class="img-handle img-del" onclick="deleteQualiDoc(${icpUploadedDoc.doc_type_id })">删除</a>
	                                    </shiro:hasPermission>
	                                </div>
                                </c:if>
                            </div>
                            <!-- icp备案 /-->
                            <!-- 法人身份证 -->
                            <div class="form-group">
                                <div class="papers  clearfix">
                                    <span class="papers_tit">法人身份证复印件：</span>
                                    <a href="javascript:;" class="btn btn-small-s btn-main papers_file">
                                        <input type="file" name="idcard_file">
                                        <c:if test="${empty idcardUploadedDoc }">上传</c:if>
                                        <c:if test="${not empty idcardUploadedDoc }">重传</c:if>
                                    </a>
                                    <input type="text" class="input_text" placeholder="请输入证书号" name="idcard_number"
                                           value="${idcardUploadedDoc.doc_number }">
                                    <input type="text" class="input_text" placeholder="证书有效期" name="idcard_valid_date"
                                           value="${idcardUploadedDoc.doc_valid_date }">
                                </div>
                                <c:if test="${not empty idcardUploadedDoc }">
	                                <div class="upload-img">
	                                    <i class="img-logo clearfix"></i>
	                                    <span class="img-info">${idcardUploadedDoc.doc_old_name }(已上传)</span>
	                                    <a href="${pageContext.request.contextPath}/admin/qualidoc/showImage.action?id=${idcardUploadedDoc.id }" class="img-handle img-look" target="_blank">查看</a>
	                                    <shiro:hasPermission name="user:operable">
	                                    	<a href="javascript:void(0)" class="img-handle img-del" onclick="deleteQualiDoc(${idcardUploadedDoc.doc_type_id })">删除</a>
	                                    </shiro:hasPermission>
	                                </div>
                                </c:if>
                            </div>
                            <!-- 法人身份证 /-->

                            <div class="form-group">
                                <p class="title">特殊行业可选资质：</p>
                                <select class="select-box" name="qualiDocOptionalCustomerType" id="qualiDocOptionalCustomerType">
                                    <option value="">请选择可选资质客户类型</option>
                                    <c:forEach items="${optionalCustomerTypeList }" var="po">
                                        <option value="${po.id }" <c:if test="${qualiDocOptionalCustomerType == po.id }">selected</c:if>>
                                            ${po.customerTypeName }
                                        </option>
                                    </c:forEach>
                                </select>
                                <div id="optionalQualiDocUpload">
	                                <!-- ajax查询要显示的可选资质 -->
	                            </div>
                            </div>
                            <div class="btn-arr">
                                <div class="btn btn-small btn-purple" id="login_button">确定</div>
                                <div class="btn btn-small btn-gray" id="reset_button">取消</div>
                            </div>
                        </div>
                    </div>
                </div>
                
                </form>
                
            </div>
        </div>
    </div>
    <!--index content/-->
</body>
</html>