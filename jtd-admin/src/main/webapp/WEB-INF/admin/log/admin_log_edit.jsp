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
<title>域名</title>
<%@ include file="/WEB-INF/commonjsp/admin_common_css.jsp"%>
<%@ include file="/WEB-INF/commonjsp/admin_common_js.jsp"%>
	<script src="${baseurl}js/campaignDicData.js"></script>
    <script src="${baseurl}js/admin/log/log_edit.js?d=${activeUser.currentTime}"></script>

</head>

<body class="skin-2">
	<!--header 导航，搜索 -->
	<jsp:include page="/WEB-INF/admin/admin_header.jsp"></jsp:include>
	<div class="main-container">
		<a class="menu-toggler" id="menu-toggler" href="#"> <span
			class="menu-text"></span>
		</a>

		<jsp:include page="/WEB-INF/admin/admin_menu.jsp"></jsp:include>

        <input type="hidden" id="before" value='${before}'>
        <input type="hidden" id="after" value='${after}'>

		<div class="main-content">
		
			<div class="main-container-inner">
			
			<div class="breadcrumbs" id="breadcrumbs">
				<script type="text/javascript">
					try {
						ace.settings.check('breadcrumbs', 'fixed')
					} catch (e) {
					}
				</script>

				<ul class="breadcrumb">
					<li><i class="icon-home home-icon"></i> <a
						href="${pageContext.request.contextPath}">日志管理</a></li>
					<li class="active">日志明细</li>
				</ul>
				<!-- .breadcrumb -->
			</div>

			<div class="page-content">
				<div class="row">
                    <div class="col-xs-9">
                        <c:if test="${not empty desc}" >
                            <c:forEach items="${desc }" var="vo">
                                <c:choose>
                                    <c:when test="${vo.key == 'weekHour'}">
                                        <input type="hidden" id="weekHour_flag" value="${vo.value}">
                                    </c:when>
                                    <c:otherwise>
                                        <div class="form-group">
                                            <label class="green">${vo.key }</label>
                                            <textarea class="form-control limited" maxlength="50" disabled>${vo.value }</textarea>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </c:if>
					</div>
					<!-- /.col -->
                    <div id="before_week"  class="col-xs-9 time_interval_table" style="display: none;">
                        <div class="form-group">
                            <label class="green">投放周期修改前</label>
                            <table id="before_hour">
                                <!--table 全选 加  class="active"-->
                                <tr>
                                    <th width="10%">日 期</th>
                                    <th width="11%">00:00-06.00</th>
                                    <th width="11%">06:00-12.00</th>
                                    <th width="11%">12:00-18.00</th>
                                    <th width="11%">18:00-24.00</th>
                                </tr>
                                <c:forEach var="i" step="1" begin="1" end="7">
                                    <tr id="week_${ i }" class="<c:if test="i%2==1">odd</c:if><c:if test="i%2==0">even</c:if>">
                                        <td>
                                            <c:choose>
                                                <c:when test="${i==1}">星期一</c:when>
                                                <c:when test="${i==2}">星期二</c:when>
                                                <c:when test="${i==3}">星期三</c:when>
                                                <c:when test="${i==4}">星期四</c:when>
                                                <c:when test="${i==5}">星期五</c:when>
                                                <c:when test="${i==6}">星期六</c:when>
                                                <c:when test="${i==7}">星期日</c:when>
                                                <c:otherwise></c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td colspan="4">
                                            <div id="week_before" class="psel">
                                                <p><i>0</i><i>1</i><i>2</i><i>3</i><i>4</i><i>5</i></p>
                                                <p><i>6</i><i>7</i><i>8</i><i>9</i><i>10</i><i>11</i></p>
                                                <p><i>12</i><i>13</i><i>14</i><i>15</i><i>16</i><i>17</i></p>
                                                <p><i>18</i><i>19</i><i>20</i><i>21</i><i>22</i><i>23</i></p>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </table>
                        </div>
                    </div>

                    <div  id="after_week" class="col-xs-9 time_interval_table" style="display: none;">
                        <div class="form-group">
                            <label class="green">投放周期修改后</label>
                            <table id="after_hour">
                                <!--table 全选 加  class="active"-->
                                <tr>
                                    <th width="10%">日 期</th>
                                    <th width="11%">00:00-06.00</th>
                                    <th width="11%">06:00-12.00</th>
                                    <th width="11%">12:00-18.00</th>
                                    <th width="11%">18:00-24.00</th>
                                </tr>
                                <c:forEach var="i" step="1" begin="1" end="7">
                                    <tr id="week_${ i }" class="<c:if test="i%2==1">odd</c:if><c:if test="i%2==0">even</c:if>">
                                        <td>
                                            <c:choose>
                                                <c:when test="${i==1}">星期一</c:when>
                                                <c:when test="${i==2}">星期二</c:when>
                                                <c:when test="${i==3}">星期三</c:when>
                                                <c:when test="${i==4}">星期四</c:when>
                                                <c:when test="${i==5}">星期五</c:when>
                                                <c:when test="${i==6}">星期六</c:when>
                                                <c:when test="${i==7}">星期日</c:when>
                                                <c:otherwise></c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td colspan="4">
                                            <div id="week_after" class="psel">
                                                <p><i>0</i><i>1</i><i>2</i><i>3</i><i>4</i><i>5</i></p>
                                                <p><i>6</i><i>7</i><i>8</i><i>9</i><i>10</i><i>11</i></p>
                                                <p><i>12</i><i>13</i><i>14</i><i>15</i><i>16</i><i>17</i></p>
                                                <p><i>18</i><i>19</i><i>20</i><i>21</i><i>22</i><i>23</i></p>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </table>
                        </div>
                    </div>
				</div>
			</div>
			<!-- /.page-content -->
			</div>
		</div>
	</div>

</body>
</html>