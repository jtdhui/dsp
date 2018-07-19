<iframe width="#WIDTH#" height="#HEIGHT#" frameborder="0" marginwidth="0" marginheight="0" allowtransparency="true" scrolling="no" src="#URL#" id="__xinmt_xtrader_20170328"></iframe>
<script type="text/javascript">
	var ifr = document.getElementById("__xinmt_xtrader_20170328");
	//console.log(ifr.contentWindow.document);
	var doc = ifr.contentWindow.document ;

	var html = "<div style=\"border:0px;position:relative;width:#WIDTH#px;height:#HEIGHT#px;z-index:2147483647;\>" ;
	html+= "<a href=\"http://www.xinmt.com\" target=\"_blank\" style=\"cursor:pointer\">" ;
	html+= "<div onmouseover=\"this.style.display='none';this.parentNode.lastChild.style.display='block'\" style=\"position:absolute;right:0;top:0;width:36px;height:20px;z-index:2147483646;background-image:url('http://cefileimg2016.b0.upaiyun.com/c1.png');\"></div>" ;
	html+= "<div onmouseout=\"setTimeout((function(me){return function(){me.style.display='none';me.parentNode.firstChild.style.display='block';};})(this), 500);\" style=\"display:none;position:absolute;right:0;top:0;width:91px;height:20px;z-index:2147483646;background-image:url('http://cefileimg2016.b0.upaiyun.com/c2.png');\"></div>" ;
	html+= "</a>" ;
	html+= "<div style=\"border:0px;position:absolute;top:0px;left:0;width:#WIDTH#px;height:#HEIGHT#px;z-index:2147483645;\">" ;
	html+= "<a href=\"#CLICKDSP#\" target=\"_blank\"><img border=\"0\" width=\"#WIDTH#\" height=\"#HEIGHT#\" src=\"#ADURL#\" /></a>" ;
	html+= "</div>#IFM#" ;
	html+= "</div>" ;
	html+= "<img width=\"0\" height=\"0\" src=\"#PV#\" />" ;
	doc.write(html);
</script>