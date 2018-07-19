(function() {

	var cmurls = [
		//'http://cms.tanx.com/t.gif?id=34462052',
		'http://cm.pos.baidu.com/pixel?dspid=22684639'
	];

	var proto = 'https:' == document.location.protocol ? 'https' : 'http';
	var countbase = proto + '://counter.xinmt.com';
	var trackbase = proto + '://tracker.xinmt.com';

	var encu = encodeURIComponent(window.location.href);
	var encr = encodeURIComponent(document.referrer);

	var isNum = function(n) { return typeof(n) === 'number'; };
	var isStr = function(s) { return typeof(s) === 'string'; };
	var isArr = function(a) { return Object.prototype.toString.call(a) === '[object Array]'; };
	var imgSend = function(u) {
		var t = new Image();
		t.src = u;
		var p = '_' + new Date().getTime() + parseInt(Math.random() * 1000);
		t.onload = function(){ delete window[p]; };
		window[p] = t;
	};

	var track = function(t) {
		if (!isNum(t.type)) return;

		var param = [];
		var tp = parseInt(t.type);
		switch (tp) {
			case 1 :
			case 2 :
			    
				if (!(isNum(t.pid) && isNum(t.gid))) return;
				if(!isStr(t.orderid)) t.orderid = '';
					
				param.push('t=' + tp);
				param.push('p=' + parseInt(t.pid));
				param.push('c=' + parseInt(t.gid));
				if (encu != '') param.push('u=' + encu);
				if (encr != '') param.push('r=' + encr);
				param.push('o=' + encodeURIComponent(t.orderid));
				param.push('gn=' + parseInt(t.goodsnum));
				param.push('ta=' + parseInt(t.totalamount));
				if(isArr(t.detail)) {
					for(var i = 0; i < t.detail.length; i++) {
						var d = t.detail[i];
						if(!(isStr(d.goodsid) && (isNum(d.goodsnum)||isStr(d.goodsnum)) && (isNum(d.price)||isStr(d.price)))) return;
						var dp = [];
						dp.push('dgi:' + encodeURIComponent(d.goodsid));
						dp.push('dgn:' + parseInt(d.goodsnum));
						dp.push('dgp:' + parseInt(d.price));
						if(isStr(d.pageurl)) dp.push('dpu:' + encodeURIComponent(d.pageurl));
						if(isStr(d.imgurl)) dp.push('diu:' + encodeURIComponent(d.imgurl));
						param.push('d=' + dp.join(';'));
					}
				}
				imgSend(trackbase + '/track?' + param.join('&'));
				break;

			case 3 :
				if (!(isNum(t.pid) && isNum(t.gid))) return;
				param.push('t=' + tp);
				param.push('p=' + parseInt(t.pid));
				param.push('c=' + parseInt(t.gid));
				if (encu != '') param.push('u=' + encu);
				if (encr != '') param.push('r=' + encr);
			  	imgSend(trackbase + '/track?' + param.join('&'));
				break;

			case 4 :
				if (!(isNum(t.pid) && isNum(t.gid) && isStr(t.userid))) return;
				param.push('t=' + tp);
				param.push('p=' + parseInt(t.pid));
				param.push('c=' + parseInt(t.gid));
				param.push('ui=' + encodeURIComponent(t.userid));
				if (encu != '') param.push('u=' + encu);
				if (encr != '') param.push('r=' + encr);
			  	imgSend(trackbase + '/track?' + param.join('&'));
				break;

			case 5 :
				if (!(isNum(t.pid) && isNum(t.gid))) return;
				param.push('t=' + tp);
				param.push('p=' + parseInt(t.pid));
				param.push('c=' + parseInt(t.gid));
				if (isStr(t.shareid)) param.push('si=' + encodeURIComponent(t.shareid));
				if (encu != '') param.push('u=' + encu);
				if (encr != '') param.push('r=' + encr);
			  	imgSend(trackbase + '/track?' + param.join('&'));
				break;

			case 6 :
				if (!(isNum(t.pid) && isNum(t.gid))) return;
				param.push('t=' + tp);
				param.push('p=' + parseInt(t.pid));
				param.push('c=' + parseInt(t.gid));
				if (isStr(t.favid)) param.push('fi=' + encodeURIComponent(t.favid));
				if (encu != '') param.push('u=' + encu);
				if (encr != '') param.push('r=' + encr);
			  	imgSend(trackbase + '/track?' + param.join('&'));
				break;

			case 7 :
				if (!(isNum(t.pid) && isNum(t.gid))) return;
				param.push('t=' + tp);
				param.push('p=' + parseInt(t.pid));
				param.push('c=' + parseInt(t.gid));
				if (isStr(t.askid)) param.push('ai=' + encodeURIComponent(t.askid));
				if (encu != '') param.push('u=' + encu);
				if (encr != '') param.push('r=' + encr);
			  	imgSend(trackbase + '/track?' + param.join('&'));
				break;

			case 8 :
				if(!(isNum(t.pid) && isNum(t.gid) && isStr(t.userid))) return;
				param.push('t=' + tp);
				param.push('p=' + parseInt(t.pid));
				param.push('c=' + parseInt(t.gid));
				param.push('ui=' + encodeURIComponent(t.userid));
				if (encu != '') param.push('u=' + encu);
				if (encr != '') param.push('r=' + encr);
			  	imgSend(trackbase + '/track?' + param.join('&'));
				break;

			case 9 :
				if (!(isNum(t.pid) && isNum(t.gid) && isStr(t.userid))) return;
				param.push('t=' + tp);
				param.push('p=' + parseInt(t.pid));
				param.push('c=' + parseInt(t.gid));
				param.push('ui=' + encodeURIComponent(t.userid));
				if (encu != '') param.push('u=' + encu);
				if (encr != '') param.push('r=' + encr);
			  	imgSend(trackbase + '/track?' + param.join('&'));
				break;

			default :
				break;
		}
	};
	
	(function() {
		if (document.readyState == 'complete') {
			if (proto == 'http') {
				for (var i = 0; i < cmurls.length; i++) {
					imgSend(cmurls[i]);
				}
			}
			var ldurl = countbase + '/nl';
			if (encu != '') {
				ldurl = ldurl + '?u=' + encu;
				if (encr != '') {
					ldurl = ldurl + '&r=' + encr;
				}
			}
			imgSend(ldurl);
		} else {
			setTimeout(arguments.callee, 100);
		}
	})();

	window._gktracker = { track : track };
	
	window._gkarr = window._gkarr || [];

	window.setInterval(function() {
		try {
			var o = window._gkarr.pop();
			if(o) {track(o);}
		}catch(e){}
	}, 1000);
})();
