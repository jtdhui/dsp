package com.jtd.engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.jtd.engine.message.v1.AVBidRequest;
import com.jtd.engine.message.v1.AdwoBidRequest;


public class HttpRequestor {
    
    private String charset = "utf-8";
    private Integer connectTimeout = null;
    private Integer socketTimeout = null;
    private String proxyHost = null;
    private Integer proxyPort = null;
    
    /**
     * Do GET request
     * @param url
     * @return
     * @throws Exception
     * @throws IOException
     */
    public String doGet(String url) throws Exception {
        
        URL localURL = new URL(url);
        
        URLConnection connection = openConnection(localURL);
        HttpURLConnection httpURLConnection = (HttpURLConnection)connection;
        
        httpURLConnection.setRequestProperty("Accept-Charset", charset);
        httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer resultBuffer = new StringBuffer();
        String tempLine = null;
        
        if (httpURLConnection.getResponseCode() >= 300) {
            throw new Exception("HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
        }
        
        try {
            inputStream = httpURLConnection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader);
            
            while ((tempLine = reader.readLine()) != null) {
                resultBuffer.append(tempLine);
            }
            
        } finally {
            
            if (reader != null) {
                reader.close();
            }
            
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
            
            if (inputStream != null) {
                inputStream.close();
            }
            
        }

        return resultBuffer.toString();
    }
    
    /**
     * Do POST request
     * @param url
     * @param parameterMap
     * @return
     * @throws Exception 
     */
    public String doPost(String url, String param) throws Exception {
        
        /* Translate parameter map to parameter date string 
        StringBuffer parameterBuffer = new StringBuffer();
        if (parameterMap != null) {
            Iterator iterator = parameterMap.keySet().iterator();
            String key = null;
            String value = null;
            while (iterator.hasNext()) {
                key = (String)iterator.next();
                if (parameterMap.get(key) != null) {
                    value = (String)parameterMap.get(key);
                } else {
                    value = "";
                }
                
                parameterBuffer.append(key).append("=").append(value);
                if (iterator.hasNext()) {
                    parameterBuffer.append("&");
                }
            }
        }
        
        System.out.println("POST parameter : " + parameterBuffer.toString());
        */
        URL localURL = new URL(url);
        
        URLConnection connection = openConnection(localURL);
        HttpURLConnection httpURLConnection = (HttpURLConnection)connection;
        
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Accept-Charset", charset);
        httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        //httpURLConnection.setRequestProperty("Content-Length", String.valueOf(parameterBuffer.length()));
        
        OutputStream outputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer resultBuffer = new StringBuffer();
        String tempLine = null;
        
        try {
            outputStream = httpURLConnection.getOutputStream();
            outputStreamWriter = new OutputStreamWriter(outputStream);
            
            outputStreamWriter.append(param);//.write(parameterBuffer.toString());
            outputStreamWriter.flush();
            
            if (httpURLConnection.getResponseCode() >= 300) {
                throw new Exception("HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
            }
            
            inputStream = httpURLConnection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader);
            
            while ((tempLine = reader.readLine()) != null) {
                resultBuffer.append(tempLine);
            }
            
        } finally {
            
            if (outputStreamWriter != null) {
                outputStreamWriter.close();
            }
            
            if (outputStream != null) {
                outputStream.close();
            }
            
            if (reader != null) {
                reader.close();
            }
            
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
            
            if (inputStream != null) {
                inputStream.close();
            }
            
        }

        return resultBuffer.toString();
    }

    private URLConnection openConnection(URL localURL) throws IOException {
        URLConnection connection;
        if (proxyHost != null && proxyPort != null) {
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
            connection = localURL.openConnection(proxy);
        } else {
            connection = localURL.openConnection();
        }
        return connection;
    }
    
    /**
     * Render request according setting
     * @param request
     */
    private void renderRequest(URLConnection connection) {
        
        if (connectTimeout != null) {
            connection.setConnectTimeout(connectTimeout);
        }
        
        if (socketTimeout != null) {
            connection.setReadTimeout(socketTimeout);
        }
        
    }

    /*
     * Getter & Setter
     */
    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Integer getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(Integer socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public Integer getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(Integer proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }
    
    
    public static void main(String[] args) throws Exception
    {
    	//sendAdwo();
    	sendAdView();
    }
    
    public static void sendAdwo() throws Exception
	{
		AdwoBidRequest adwoBid = new AdwoBidRequest();
		
		//曝光对象
		AdwoBidRequest.Imp imp = new AdwoBidRequest.Imp();  
		imp.setId("101");   //曝光ID
		imp.setTagid("1001"); //广告位ID
		imp.setBidfloor((float)5.5); //底价，单位是分/千次曝光，即CPM
		imp.setBidfloorcur("CNY"); // 底价单位，目前CNY
		
		AdwoBidRequest.Imp.Banner banner = new AdwoBidRequest.Imp.Banner(); //banner广告
		banner.setW(600);  //banner宽度
		banner.setH(600);  //banner高度
		imp.setBanner(banner);
		
		//媒体站点对象
		AdwoBidRequest.Site site = new AdwoBidRequest.Site();  
		site.setName("腾讯");  // 媒体网站名称
		site.setPage("http://www.qq.com/new/12343.html");  //当前页面URL
		site.setRef("http://www.qq.com");  // referrer URL
		site.setDomain("www.qq.com");  // 站点域名 
		site.setKeywords("腾讯,新闻,十九大");  //站点关键字，如果是多个使用英文逗号分隔 
		
		//设备信息
		AdwoBidRequest.Device device = new AdwoBidRequest.Device();  
		device.setIp("118.192.170.71"); //设备IP地址
		
		AdwoBidRequest.Device.Geo geo = new AdwoBidRequest.Device.Geo(); //设备地理位置
		geo.setLat((float)134.4444);  //纬度
		geo.setLon((float)55.555);    //经度
		geo.setCountry("中国");       //国家
		geo.setCountry("华北");     //地区
		geo.setCity("北京");  //城市
		device.setGeo(geo);
		
		device.setMake("apple");  //厂商
		device.setModel("iphone7");     //型号
		device.setOs("ios");                //操作系统
		device.setOvs("10.5.1");      //操作系统版本
		device.setDevicetype(1); //设备类型 0 未知，1 iphone，2 android手机，3 ipad，4 WindowsPhone， 5 android平板，6 智能TV 
		device.setConnectiontype(6);  //网络类型 0 未知，1 局域网(PC)，2 WIFI，3 蜂窝网络未知，4 蜂窝网络2G，5 蜂窝网络3G，6蜂窝网络4G
		
		
		//用户信息
		AdwoBidRequest.User user = new AdwoBidRequest.User();  
		user.setId("5000001");  //用户ID
		user.setGender("男");   //用户性别
		user.setYob("1984-11-22"); //出生日期
		
		//APP信息
		AdwoBidRequest.App app = new AdwoBidRequest.App(); 
		app.setId("1001-001-002-003");  //APPID
		app.setName("今日头条"); //app名称
		app.setVer("5.1"); //appA版本
		app.setBundle("com.ximad.snake");  //app包名
		app.setDomain("play.google.com"); //app域名
		app.setKeywords("新闻");   //app关键词
		
		
		adwoBid.setId("1");  /** 请求ID */
		adwoBid.setSite(site); /** 媒体站点对象 */
		adwoBid.setImp(imp); /** 曝光对象(只包含一个banner或video对象，不会同时包含) */
		adwoBid.setDevice(device); /** 设备对象 */
		adwoBid.setUser(user); /** 用户对象 */
		adwoBid.setApp(app); /** 应用对象(请求非移动应用时，此字段为空) */
		
		
		String requestJson = JSON.toJSONString(adwoBid);
		System.out.println("request >>>> json >>> " + requestJson);
		
		String response = new HttpRequestor().doPost("http://47.93.118.247:8080/adwo", requestJson);
		System.out.println("response >>>> json >>> " + response);
		
	}

    public static void sendAdView() throws Exception
    {
    	AVBidRequest avBid = new AVBidRequest();
    	
    	avBid.setId("20150706-104301_bidreq_162-5475-9CUE-287");
    	avBid.setAt(1);
    	
    	//app信息
    	AVBidRequest.App app = new AVBidRequest.App();
    	app.setId("9a348452f909ffafb6a618b09c3a1c4b");
    	app.setPaid(0);
    	app.setCat(new ArrayList<Integer>());
    	app.getCat().add(10102);  // 21,2101,摄影,2101,100301,60016_60513_61055,IAB9-23,10102
    	app.setName("adviewApp");
    	app.setBundle("com.test.adview");
    	app.setVer("1.1.3");
    	
    	avBid.setApp(app);
    	
    	
    	AVBidRequest.Imp imp = new AVBidRequest.Imp();
    	avBid.setImp(new ArrayList<AVBidRequest.Imp>());
    	avBid.getImp().add(imp);
    	
    	imp.setId("20150706-104301_reqimp_162-5475-jKAE-285");
    	imp.setBidfloor(10000);
    	imp.setInstl(0);
    	imp.setBidfloorcur("RMB");
    	
    	AVBidRequest.Imp.Banner banner = new AVBidRequest.Imp.Banner();
    	imp.setBanner(banner);
    	banner.setW(600);
    	banner.setH(600);
    	banner.setPos(1);
    	
    	imp.setBanner(banner);
    	
    	AVBidRequest.Device device = new AVBidRequest.Device();
    	device.setOrientation(0);
    	device.setOs("iOS");
    	device.setModel("iPhone");
    	device.setOsv("7.0.4");
    	device.setJs(1);
    	device.setSw(480);
    	device.setSh(320);
    	//device.setIp("222.44.97.54");
    	device.setS_density((float)2.0);
    	device.setDpidsha1("468ed7fd9f84de7506526b6215010531fe461b2d");
    	device.setConnectiontype(6);
    	device.setUa("Mozilla/5.0 (iPhone; CPU iPhone OS 7_0_4 like Mac OS X) AppleWebKit/537.51.1 (KHTML, like Gecko) Mobile/11B554a");
    	device.setCarrier("46002");
    	device.setDevicetype(1);
    	device.setLanguage("zl");
    	device.setMake("Apple");
    	
    	avBid.setDevice(device);
    	
    	
    	AVBidRequest.User user = new AVBidRequest.User();
    	avBid.setUser(user);
    	
    	
    	String requestJson = JSON.toJSONString(avBid);
		System.out.println("request >>>> json >>> " + requestJson);
		
		String response = new HttpRequestor().doPost("http://192.168.6.216:8080/avb", requestJson);
		System.out.println("response >>>> json >>> " + response);
    }
}
