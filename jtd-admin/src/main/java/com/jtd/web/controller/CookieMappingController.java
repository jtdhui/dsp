package com.jtd.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.utils.mk.CookieUtil;
import com.jtd.utils.mk.HandlerUtil;
import com.jtd.web.dao.IParChanneldDao;

@Controller
@RequestMapping("/ck")
public class CookieMappingController {
	
	public static final String PAGE_DIRECTORY = "/ck/";
	
	@Autowired
	private IParChanneldDao parChanneldDao ;
	
	@RequestMapping("/index")
	public String index() throws Exception{
		return PAGE_DIRECTORY + "cookiemapping";
	}
	
	@RequestMapping("/test")
	public String test(HttpServletRequest request) throws Exception{
		request.setAttribute("pid", 178);
		return PAGE_DIRECTORY + "adxmapping";
	}
	
	/**
	 * 调用这个方法可以根据所有partner的信息重新发送一遍MQ
	 * 
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/ckmapping")
	public String partnerInfo(String campid,String pid,HttpServletRequest request , HttpServletResponse response) throws Exception{
		
		return PAGE_DIRECTORY + "adxmapping";
	}
	
	@RequestMapping("/writecookie")
	public String writeCookie(String campid,String pid,HttpServletRequest request , HttpServletResponse response) throws Exception{
		if(pid==null||pid.length()<=0){
			request.setAttribute("info", "广告主id输入错误,不能为空且只能是数字。");
			return PAGE_DIRECTORY + "cookiemapping";
		}
		pid=pid.trim();
		try{
			Integer.parseInt(pid);
		}catch(Exception ex){
			request.setAttribute("info", "广告主id输入错误，不能为空且只能是数字。");
			return PAGE_DIRECTORY + "cookiemapping";
		}
		if(campid==null||campid.length()<=0){
			request.setAttribute("info", "活动id输入错误，不能为空且只能是数字。");
			return PAGE_DIRECTORY + "cookiemapping";
		}
		campid=campid.trim();
		try{
			Integer.parseInt(campid);
		}catch(Exception ex){
			request.setAttribute("info", "活动id输入错误，不能为空且只能是数字。");
			return PAGE_DIRECTORY + "cookiemapping";
		}
		
		//获取cookieid
		String cookieid=CookieUtil.getCookieId(request);
		if(cookieid==null){
			//写入cookie
			Map<String,String> cookies = CookieUtil.createCookie();
			HandlerUtil.setCookie(response, cookies);
		}
		
//		Map<String,String> cookies = CookieUtil.createCookie();
//		System.out.println("写入的cookie信息:"+cookies.get("i"));
//		HandlerUtil.setCookie(response, cookies);
//		System.out.println("写入cookie");
		//return "redirect:/ck/ckmapping.action?pid="+pid+"&campid="+campid;
		
		request.setAttribute("tag", "1");
		request.setAttribute("pid", pid);
		request.setAttribute("campid", campid);
		return PAGE_DIRECTORY + "cookiemapping";
		//return PAGE_DIRECTORY + "adxmapping";
	}
	
	
	
	@RequestMapping("/getParChanneldList")
	@ResponseBody
	public Map<String,Object> getParChanneldList(String partnerId){
		
		Map<String,Object> resultMap = new HashMap<String, Object>();
		
		HashMap<String,Object> param = new HashMap<String, Object>();
		param.put("partnerId", partnerId);
		//param.put("startDateNum", DateUtil.getDateInt(DateUtil.getYestoday()));  //从昨天开始
		//param.put("endDateNum", DateUtil.getDateInt(new Date())); //到今天结束
		
		param.put("startDateNum", 20170417);
		param.put("endDateNum", 20170418); 
		
		CustomerContextHolder
		.setContextType(CustomerContextHolder.SESSION_FACTORY_COUNT);
		
		List<Map<String,Object>> list = parChanneldDao.listByMap(param);
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		if(list != null){
			for (Map<String, Object> map : list) {
				String host = (String)map.get("host");
				if(host != null && 
						(host.endsWith(".com") || host.endsWith(".net") || host.endsWith(".cn") || host.endsWith(".org") 
								|| host.endsWith(".cc"))){
					resultList.add(map);
				}
			}
			resultMap.put("code", 1);
			resultMap.put("list", resultList);
		}
		else{
			resultMap.put("code", 0);
		}
		
		return resultMap ;
	}

}
