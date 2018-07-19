<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="user-scalable=no, width=device-width,initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0"/>
<script type="text/javascript" src="http://cdn.tanx.com/t/tanxmobile/mraid.js"></script>
<style tyle="text/css">
html, body {
	margin: 0;
	padding: 0;
}
</style>
</head>
<body> 
<img id="demo" style="width:100%; height:100%" src="" alt="" /> 
<script type="text/javascript">
	function loadAndDisplayAd() {
	    var img = document.getElementById("demo");
	    img.onload = function() {
	        mraid.show();
	        document.addEventListener('touchstart', function(){
	    		mraid.open("#CLICK#");
	    	});
	    }
	    img.src = '#ADURL#';
	}
	mraid.addEventListener('viewableChange',function(visiable) {
	    if (visiable) {
	        var img = new Image();
	        img.src = '#PV#';
	        window[new Date()] = img;
	    }
	});
	if ((mraid.getState() == "loading")) {
	    mraid.addEventListener('ready', loadAndDisplayAd);
	} else {
	    loadAndDisplayAd();
	}
</script> 
</body>
</html>