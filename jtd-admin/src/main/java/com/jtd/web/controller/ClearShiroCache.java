package com.jtd.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jtd.web.shiro.PlatformRealm;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年10月9日
 * @项目名称 dsp-admin
 * @描述 <p>手动清除缓存</p>
 */
@Controller
public class ClearShiroCache {
	
	@Autowired
	private PlatformRealm platformRealm;
	
	@RequestMapping("clearShiroCache")
	public String clearShiroCache(){
		//清除缓存，将来正常开发要在service调用 platformRealm.clearCached()
		platformRealm.clearCached();
		return "/success";
	}
}
