package com.jtd.engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonAnyFormatVisitor;
import com.jtd.engine.message.v1.AdwoBidRequest;

public class CallServiceUtil {

	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 */
	public static String sendGet(String url, String param) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url;// + "?" + param;
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
			Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段
			/*
			for (String key : map.keySet()) {
				System.out.println(key + "--->" + map.get(key));
			}*/
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}
	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url1, String param) {
        try {  
            URL url = new URL(url1);// 创建连接  
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();  
            connection.setDoOutput(true);  
            connection.setDoInput(true);  
            connection.setUseCaches(false);  
            connection.setInstanceFollowRedirects(true);  
            connection.setRequestMethod("POST"); // 设置请求方式  
            connection.setRequestProperty("Accept", "application/json"); // 设置接收数据的格式  
            connection.setRequestProperty("Content-Type", "application/json"); // 设置发送数据的格式  
            connection.connect();  
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8"); // utf-8编码  
            out.append(param);  
            out.flush();  
            out.close();  
            // 读取响应  
            int length = (int) connection.getContentLength();// 获取长度  
            InputStream is = connection.getInputStream();  
            if (length != -1) {  
                byte[] data = new byte[length];  
                byte[] temp = new byte[512];  
                int readLen = 0;  
                int destPos = 0;  
                while ((readLen = is.read(temp)) > 0) {  
                    System.arraycopy(temp, 0, data, destPos, readLen);  
                    destPos += readLen;  
                }  
                String result = new String(data, "UTF-8"); // utf-8编码  
                return result;  
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
            return "error";
        }  
        return "error"; // 自定义错误信息  
    }  
	
	public static void main(String[] args) throws Exception 
	{
		sendAdwo();
		
	}
	
	public static void sendAdwo()
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
		
		String response = CallServiceUtil.sendPost("http://147.93.118.247:8080/adwo", requestJson);
		System.out.println("response >>>> json >>> " + response);
		
	}
	
	/** 新汇达通广告请求  */
	public static void sendXHDT()
	{

		HashMap<String,Object> param = new HashMap<String,Object>();
		
		HashMap<String,String> device = new HashMap<String,String>();
		device.put("androidid", "100");
		device.put("brand", "华为");
		device.put("model", "P9000");
		device.put("imei", "IMEI001");
		device.put("imsi", "IMSI001");
		device.put("mac", "X00-333-4445-222");
		device.put("systupe", "android");
		device.put("resolution", "1024*768");
		device.put("screen", "screen100");
		device.put("iccid", "iccid1001001");
		device.put("sysver", "5.6.1");
		
		HashMap<String,Object> app = new HashMap<String,Object>();
		app.put("appkey", "appkey123");
		app.put("packaged", "王者荣耀");
		List<String> types = new ArrayList<String>();
		types.add("游戏");
		types.add("体育");
		
		app.put("apptype", types);
		
		HashMap<String,String> user = new HashMap<String,String>();
		user.put("ip", "192.168.1.1");
		user.put("language", "cn");
		user.put("network", "4g");
		user.put("operator", "移动");
		user.put("telnum", "13400000001");
		user.put("lon", "123.11111");
		user.put("lat", "33.5555");
		user.put("gender", "男");
		user.put("age", "32");
		
		HashMap<String,Object> imp = new HashMap<String,Object>();
		imp.put("adtype", 1);
		imp.put("width", "600");
		imp.put("height", "600");
		
		param.put("device", device);
		param.put("app", app);
		param.put("user", user);
		param.put("imp", imp);
		
		//System.out.println(CallServiceUtil.sendPost("https://www.baidu.com", JSON.toJSONString(param)));
		System.out.println(CallServiceUtil.sendPost("http://192.168.6.216:8080/xhdt", JSON.toJSONString(param)));
	}
}
