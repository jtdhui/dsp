<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ include file="/WEB-INF/commonjsp/tag.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>帮助中心</title>
<%@ include file="/WEB-INF/commonjsp/common_css.jsp"%>
<%@ include file="/WEB-INF/commonjsp/common_js.jsp"%>

<link rel="stylesheet" href="${baseurl }dist/less/home-help.css">

</head>
<%
	String id = request.getParameter("q");
	if(id == null){
		id = "" ;
	}
	pageContext.setAttribute("id", id);
%>

<script type="text/javascript">
	
	//从外面页面跳转进来，直接滚动到指定问题
	function outSideIn(){
		if("${id}" != ""){
			rightScroll("q${id}");
		}
	}
	//滚动到指定问题
	function rightScroll(id,obj){
		
		console.log($("#q1").offset().top);
		console.log($("#" + id).offset().top );
		console.log($("#" + id).offset().top - $("#q1").offset().top);
		
		$("#right").scrollTop($("#" + id).offset().top - $("#q1").offset().top - ($("#right").height()/3));
		
		layer.closeAll();
	}

</script>

<body>
	<!--header 导航 -->
	<jsp:include page="/WEB-INF/front/header.jsp?param=home"></jsp:include>
	<!--index content-->
    <div class="wrapper">
        <!-- todo: left -->
        <div class="index_lt clearfix">
            <h1>文章分类</h1>
            <h2>基础概念</h2>
            <div class="index_lt_nwbie">
                <ul>
                    <li><a href="javascript:void(0)" onclick="rightScroll('q1',this)">关于xxxx</a></li>
                    <li><a href="javascript:void(0)" onclick="rightScroll('q2',this)">xxxx服务的部分客户</a></li>
                    <li><a href="javascript:void(0)" onclick="rightScroll('q3',this)">xxxx旗下产品</a></li>
                    <li><a href="javascript:void(0)" onclick="rightScroll('q4',this)">xxxxDSP产品的优势</a></li>
                    <li><a href="javascript:void(0)" onclick="rightScroll('q5',this)">什么是DSP?</a></li>
                    <li><a href="javascript:void(0)" onclick="rightScroll('q6',this)">什么是RTB?</a></li>
                    <li><a href="javascript:void(0)" onclick="rightScroll('q7',this)">什么是CPM?</a></li>
                    <li><a href="javascript:void(0)" onclick="rightScroll('q8',this)">什么是CPC?</a></li>
                    <li><a href="javascript:void(0)" onclick="rightScroll('q9',this)">我应该选择哪种计费方式?</a></li>
                    <li><a href="javascript:void(0)" onclick="rightScroll('q10',this)">什么是账户余额?</a></li>
                    <li><a href="javascript:void(0)" onclick="rightScroll('q11',this)">什么是回头客定向?</a></li>
                </ul>
            </div>
            <h2>媒体资源</h2>
            <div class="index_lt_nwbie">
                <ul>
                    <li><a href="javascript:void(0)" onclick="rightScroll('q12',this)">xxxx的媒体资源的来源与量级</a></li>
                    <li><a href="javascript:void(0)" onclick="rightScroll('q13',this)">xxxxDSP的广告形式展示</a></li>
                    <li><a href="javascript:void(0)" onclick="rightScroll('q14',this)">xxxx的媒体资源的行业分布</a></li>
                </ul>
            </div>
            <h2>资质创意相关</h2>
            <div class="index_lt_nwbie">
                <ul>
                    <li><a href="javascript:void(0)" onclick="rightScroll('q15',this)">xxxx客户资质文件提交标准</a></li>
                    <!-- 	                    <li><a href="javascript:void(0)" onclick="rightScroll('q16',this)">xxxx创意制作标准</a></li> -->
                    <li><a href="javascript:void(0)" onclick="rightScroll('q17',this)">广告创意审核需要多久？</a></li>
                    <li><a href="javascript:void(0)" onclick="rightScroll('q18',this)">广告创意什么有哪几种状态？分别表示什么？</a></li>
                    <li><a href="javascript:void(0)" onclick="rightScroll('q19',this)">为什么创意审核会显示“未通过”？</a></li>
                </ul>
            </div>
            <h2>广告监测</h2>
            <div class="index_lt_nwbie">
                <ul>
                    <li><a href="javascript:void(0)" onclick="rightScroll('q20',this)">为什么需要在客户网站上部署代码？</a></li>
                    <li><a href="javascript:void(0)" onclick="rightScroll('q21',this)">什么是无效点击？会被计费么？</a></li>
                    <li><a href="javascript:void(0)" onclick="rightScroll('q22',this)">哪些指标可以帮助我评估效果？</a></li>
                    <li><a href="javascript:void(0)" onclick="rightScroll('q23',this)">统计报告中的数据是实时更新的吗？</a></li>
                </ul>
            </div>
        </div>

        <!-- todo: right -->
        <div class="index_rt clearfix">
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
                            <a class="nav-breadcrumb active">帮助中心</a>
                        </li>
                    </ol>
                </div>
                
                <!-- 切换广告主 -->
                <jsp:include page="/WEB-INF/front/selectPartner.jsp?fromPage=home"></jsp:include>
                
            </div>
            <div id="right" class="right">
                <div class="goods" id="q1">
                    <h2>关于xxxx</h2>
                    <div class="goods_con">
                        <ol class="show-list clearfix">
                            <li><img src="${baseurl }img/logo-111.png" alt=""></li>
                            <li><img src="${baseurl }img/logo-211.png" alt=""></li>
                            <li><img src="${baseurl }img/logo-311.png" alt=""></li>
                            <li><img src="${baseurl }img/logo-411.png" alt=""></li>
                            <li><img src="${baseurl }img/logo-511.png" alt=""></li>
                            <li><img src="${baseurl }img/logo-611.png" alt=""></li>
                        </ol>
                    </div>
                </div>
                <div class="goods" id="q2">
                    <h2>xxxx服务的部分客户</h2>
                    <div class="goods_con">
                        <p>
                            <img src="${baseurl }images/front/help/customeaaaars.png"/>
                        </p>
                    </div>
                </div>
                <div class="goods" id="q3">
                    <h2>xxxx旗下产品</h2>
                    <div class="goods_con">
                        <p>
                            xxxxDSP: 自主研发的DSP产品，xxxxxxxxxxxxxxxxxxxxx。
                        </p>
                        <p>
                            xxxxDMP: 依托xxxx在业内的多年积淀，xxxxxxxxx。依托对于传统行业中小企业受众的分析，可帮传统行业中小企业提升数倍广告投放效果。
                        </p>
                    </div>
                </div>
                <div class="goods" id="q4">
                    <h2>xxxxDSP产品的优势</h2>
                    <div class="goods_con">
                        <p>
                            海量媒体覆盖：覆盖100万家网站，每日拥有超过50亿的展示量。
                        </p>
                        <p>
                            精准人群数据：拥有海量的效果数据支撑，经多年数据积累，目前拥有2亿用户行为数据。
                        </p>
                        <p>
                            实时投放技术：使用实时竞价技术（RTB），会根据广告主的实时效果进行出价优化，来保证效果的最大化，根据市场上实时的媒体竞争情况进行出价，来保证最低出价获得最优广告位。
                        </p>
                        <p>
                            完全的控制度与透明度：自主投放界面，根据不同人群设置不同最高限价,对于竞价型媒体购买具有完全控制度与透明度。
                        </p>
                        <p>
                            回头客重定向技术：有效提升老客的留存率。
                        </p>
                    </div>
                </div>
                <div class="goods" id="q5">
                    <h2>什么是DSP？</h2>
                    <div class="goods_con">
                        <p>
                            互联网广告 DSP（Demand-SidePlatform），就是需求方平台。这一概念起源于网络广告发达的欧美，是伴随着互联网和广告业的飞速发展新兴起的网络广告领域。它与Ad
                            Exchange和RTB一起迅速崛起于美国，已在全球快速发展，2011年已经覆盖到了欧美、亚太以及澳洲。在世界网络展示广告领域，DSP方兴未艾。DSP传入中国，迅速成为热潮，成为推动中国网络展示广告RTB市场快速发展的主要动力之一。
                        </p>
                        <p>
                            在互联网广告产业中，DSP是一个系统，也是一种在线广告平台。它服务于广告主，帮助广告主在互联网或者移动互联网上进行广告投放，DSP可以使广告主更简单便捷地遵循统一的竞价和反馈方式，对位于多家广告交易平台的在线广告,以合理的价格实时购买高质量的广告库存。DSP让广告主可以通过一个统一的接口来管理一个或者多个Ad
                            Exchange账号，甚至DSP可以帮助广告主来管理Ad Exchange的账号，提供全方位的服务。
                        </p>
                        <span class="goods_tit">DSP广告特点：</span>
                        <p>
                            区别于传统的广告网络(AdNetwork)，DSP不是从网络媒体那里包买广告位，也不是采用CPD(CostPerDay)的方式获得广告位；而是从广告交易平台(AdExchange)来通过实时竞价的方式获得对广告进行曝光的机会，DSP通过广告交易平台对每个曝光单独购买，即采用CPM(Cost
                            Per Mille)的方式获得广告位。
                        </p>
                        <span class="goods_tit">DSP广告特征：</span>
                        <p>
                            一个真正意义的DSP，拥有两个核心特征，一是拥有强大的RTB(Real-Time Bidding)的基础设施和能力，二是拥有先进的用户定向(AudienceTargeting)技术。
                        </p>
                        <p>
                            首先，DSP对其数据运算技术和速度要求非常之高。从普通用户在浏览器中地址栏输入网站的网址，到用户看到页面上的内容和广告这短短几百毫秒之内，就需要发生了好几个网络往返(Round
                            Trip)的信息交换。Ad Exchange首先要向DSP发竞价(bidding)请求，告知DSP这次曝光的属性，如物料的尺寸、广告位出现的URL和类别、以及用户的Cookie
                            ID等；DSP接到竞价请求后，也必须在几十毫秒之内决定是否竞价这次曝光, 如果决定竞价，出什么样的价格，然后把竞价的响应发回到Ad Exchange。如果Ad
                            Exchange判定该DSP赢得了该次竞价，要在极短时间内把DSP所代表的广告主的广告迅速送到用户的浏览器上。整个过程如果速度稍慢，Ad
                            Exchange就会认为DSP超时而不接受DSP的竞价响应，广告主的广告投放就无法实现。
                        </p>
                        <p>
                            其次，基于数据的用户定向(Audience
                            Targeting)技术，则是DSP另一个重要的核心特征。从网络广告的实质上来说，广告主最终不是为了购买媒体，而是希望通过媒体与他们的潜在客户即目标人群进行广告沟通和投放。服务于广告主或者广告主代理的DSP，则需要对Ad
                            Exchange每一次传过来的曝光机会，根据关于这次曝光的相关数据来决定竞价策略。这些数据包括本次曝光所在网站、页面的信息，以及更为关键本次曝光的受众人群属性，人群定向的分析直接决定DSP的竞价策略。DSP在整个过程中，通过运用自己人群定向技术来分析，所得出的分析结果将直接影响广告主的广告投放效果。
                        </p>
                    </div>
                </div>
                <div class="goods" id="q6">
                    <h2>什么是RTB？</h2>
                    <div class="goods_con">
                        <p>
                            RTB（RealTimeBidding）是一种实时竞价技术，是DSP系统里采用的技术手段。它会在数以百万计的网站或移动端针对每一个用户的行为（搜索行为，购买行为，浏览商品行为，点击行为等）进行评估以及出价的竞价技术。RTB与大量包段购买广告位的购买形式不同，实时竞价技术为广告主分析了哪些受众是对于品牌或是商品转化效果高的客户，只针对有意义的用户进行购买。让广告投放更有效果，更科学。
                        </p>
                        <p>
                            当一个用户在全网浏览过某种商品，或点击过特殊类目的广告后，其浏览痕迹都会通过cookie记录在案，而通过广告交易平台，你在下一次浏览网页的时候，将被推送符合偏好的广告。RTB相关技术的不断发展使得系统侧自身便能实现更好的精准营销，让投放的广告更精准更有价值。
                        </p>
                    </div>
                </div>
                <div class="goods" id="q7">
                    <h2>什么是CPM？</h2>
                    <div class="goods_con">
                        <p>
                            CPM(Cost Per Mille，或者Cost Per Thousand;Cost Per Impressions) 即每千次展现成本。
                        </p>
                    </div>
                </div>
                <div class="goods" id="q8">
                    <h2>什么是CPC？</h2>
                    <div class="goods_con">
                        <p>
                            CPC(Cost Per Click;Cost Per Thousand Click-Through) 即每次点击成本。
                        </p>
                    </div>
                </div>
                <div class="goods" id="q9">
                    <h2>我应该选择哪种计费方式？</h2>
                    <div class="goods_con">
                        <p>
                            如果您更注重效果，选择CPC计费，因为当广告展示后，并不向您收取费用，只有点击后才扣费，为您节约成本。
                        </p>
                        <p>
                            如果您注重品牌效果，让更多的广告展示到网民面前，我们推荐CPM计费。
                        </p>
                        <p>
                            计费方式并没有绝对的准则，您可以按照习惯的广告投放计费方式,选择,并且随着推广的效果调整。
                        </p>
                    </div>
                </div>
                <div class="goods" id="q10">
                    <h2>什么是账户余额？</h2>
                    <div class="goods_con">
                        <p>
                            当前帐户中可供消费的金额。
                        </p>
                    </div>
                </div>
                <div class="goods" id="q11">
                    <h2>什么是xxx定向？</h2>
                    <div class="goods_con">
                        <p>
                            xxx定向是让您锁定品牌忠诚客户自定义行为的用户。
                        </p>
                        <p>
                            。
                        </p>
                    </div>
                </div>
                <div class="goods" id="q12">
                    <h2>xxxx的媒体资源的来源与量级</h2>
                    <div class="goods_con">
                        <p>
                            媒体的采购渠道。
                        </p>
                        <p>
                            媒体资源类型包括：PC、MOBILE。
                        </p>
                        <p>
                            PC端的广告形式包括，BANNER，文字链，等形式。
                        </p>
                        <p>
                            MOBILE端的广告形式包括，BANNER等。
                        </p>
                        <p>
                            媒体资源的量级：每天来自于合作媒体的广告展示机会次数超过150亿次数。
                        </p>
                    </div>
                </div>
                <div class="goods" id="q13">
                    <h2>xxxxDSP的广告形式展示</h2>
                    <div class="goods_con">
                        <span class="goods_tit">1. PC BANNER：</span>
                        <p>
                            PC BANNER指网站上固定位置的广告形式，如顶端的横幅广告，右侧的矩形，双侧的摩天楼，对联，视频播放页的固定位等形式，都属于BANNER的形式。PC
                            BANNER是尺寸最多的一种广告形式，尺寸繁多，主流尺寸也超过30个。
                        </p>
                        <p>
                            PC端的广告形式包括，BANNER，文字链。
                        </p>
                        <p>
                            MOBILE端的广告形式包括，BANNER 文字链等。
                        </p>
                        <p>
                            媒体资源的量级：每天来自于合作媒体的广告展示机会次数超过150亿次数。
                        </p>
                        <img src="${baseurl }images/front/help/anner1.png"/>
                        <img src="${baseurl }images/front/help/baanner2.png"/>
                        <img src="${baseurl }images/front/help/baanner3.png"/>
                        <img src="${baseurl }images/front/help/baanner4.png"/>
                        <img src="${baseurl }images/front/help/baanner5.png"/>

                        <span class="goods_tit">2. 移动端BANNER:</span>
                        <p>
                            指在移动设备上手机或是IPAD上固定位置的广告形式。在APP上通常是顶部，中间，或底部的通栏位，尺寸多数是640*100，320*50等。
                        </p>
                        <img src="${baseurl }images/front/help/mobile_banneibbbr.png"/>


                    </div>

                    <div class="goods" id="q14">
                        <h2>xxxx的媒体资源的行业分布</h2>
                        <div class="goods_con">
                            <p>
                                xxxx的媒体行业包括：
                            </p>
                            <p>
                                综合门户，旅游，汽车，游戏，娱乐，电商，女性，健康，社交，生活，医疗，摄影摄像，教育，军事，母婴，音乐，社区论坛，财经，新闻，房地产，IT类，体育类，时尚类
                            </p>
                        </div>
                    </div>

                    <div class="goods" id="q15">
                        <h2>xxxx客户资质文件提交标准</h2>
                        <div class="goods_con">
                            <p>
                                投放前所有行业都要提交基础资质文件（即企业营业执照）。
                            </p>
                            <div class="yen_details_table">
                                <table>
                                    <tr>
                                        <td width="50%">广告主行业分类
                                        </th>
                                        <td width="50%">文件名
                                        </th>
                                    </tr>
                                    <tr class="even">
                                        <td>包括各类经营销售方式
                                        </th>
                                        <td>
                                        </th>
                                    </tr>
                                    <tr class="odd">
                                        <td>医药研究、药品研发类用户
                                        </th>
                                        <td>《药品生产许可证》
                                        </th>
                                    </tr>
                                    <tr class="even">
                                        <td>医药研究、药品研发类用户
                                        </th>
                                        <td>《药品生产许可证》
                                        </th>
                                    </tr>
                                </table>
                            </div>
                        </div>
                    </div>

                    <!-- 	            	<div class="goods" id="q16"> -->
                    <!-- 		                <h2>xxxx创意制作标准</h2> -->
                    <!-- 		                <div class="goods_con"> -->
                    <!-- 		                    aaaaaaaaaaaa -->
                    <!-- 		                </div> -->
                    <!-- 	            	</div> -->

                    <div class="goods" id="q17">
                        <h2>广告创意审核需要多久？</h2>
                        <div class="goods_con">
                            <p>
                                我们在1-3个工作日内，审核您的广告创意。
                            </p>
                            <p>
                                说明：一旦您在帐户中提交了新的广告创意或者对广告创意进行了修改
                            </p>
                        </div>
                    </div>

                    <div class="goods" id="q18">
                        <h2>广告创意什么有哪几种状态？分别表示什么？</h2>
                        <div class="goods_con">
                            <p>
                                待审核：说明您的广告创意正在审核中，请耐心等待。
                            </p>
                            <p>
                                已通过，说明您的广告创意审核已通过，可以正常投放。
                            </p>
                            <p>
                                已拒绝，说明您的广告创意审核不通过，请查看创意被拒理由。按照要求修改创意并且重新上传。
                            </p>
                        </div>
                    </div>

                    <div class="goods" id="q19">
                        <h2>为什么创意审核会显示“未通过”？</h2>
                        <div class="goods_con">
                            <p>
                                审核结束后，系统会自动更改您创意的状态。如果您的创意审核遭到拒绝，请查阅。
                            </p>
                        </div>
                    </div>

                    <div class="goods" id="q20">
                        <h2>为什么需要在客户网站上部署代码？</h2>
                        <div class="goods_con">
                            <p>
                                部署代码的好处很多：
                            </p>
                            <p>
                                1、部署代码后，可以获知广告投放的效果到底如何。
                            </p>
                            <p>
                                2、部署代码后，让广告投放更科学，有依据的做投放。
                            </p>

                        </div>
                    </div>

                    <div class="goods" id="q21">
                        <h2>什么是无效点击？会被计费么？</h2>
                        <div class="goods_con">
                            <p>
                                无效点击是与正常点击相对而言的，指经系统确认的、不会为您带来有效访问的点击，你无需为此支付额外的费用。为保护您的利益，我们与广告供应商建立了一套完善的过滤机制造，对于重复性的人工点击，或利用计算机进行的自动点击，系统会自动将第一次以后的点击过滤。
                            </p>
                            <p>
                                只有由过滤系统判断为有效访问的正常点击才计费。
                            </p>
                        </div>
                    </div>

                    <div class="goods" id="q22">
                        <h2>哪些指标可以帮助我评估效果？</h2>
                        <div class="goods_con">
                            <p>
                                我们会为您提供完善的报告， 各统计报告的数据指标主要包括：
                            </p>
                            <p>
                                投放量：系统竞价请求的次数；
                            </p>
                            <p>
                                展现量：您的广告被浏览了多少次；
                            </p>
                            <p>
                                展现率：您的广告被展现的几率；
                            </p>
                            <p>
                                点击量：潜在用户点击您的广告的次数；
                            </p>
                            <p>
                                点击率：点击量/展现量*100%，高点击率说明您的广告受认可；
                            </p>

                        </div>
                    </div>

                    <div class="goods" id="q23">
                        <h2>统计报告中的数据是实时更新的吗？</h2>
                        <div class="goods_con">
                            <p>
                                统计报告中的数据是准实时数据，显示1分钟前的投放数据。
                            </p>
                        </div>
                    </div>

                    <!-- 一个不显示的图片，它的作用是用它的onload事件来作为所有图片都加载完毕的标识，因为要等所有图片都加载完，才能执行outSideIn方法 -->
                    <img src="${baseurl }images/loading.gif" style="width:1px;height:1px" onload="outSideIn()"/>
                </div>
            </div>
        </div>

    </div>
    <!--index content/-->
	
</body>
</html>