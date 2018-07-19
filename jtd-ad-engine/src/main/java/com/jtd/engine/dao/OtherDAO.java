package com.jtd.engine.dao;

import java.util.List;

import com.jtd.web.constants.CampaignAutoStatus;
import com.jtd.web.constants.CampaignManulStatus;
import com.jtd.web.model.Campaign;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p></p>
 */
public interface OtherDAO {
	
	/**
	 * 获取当前节点，上一次总请求数
	 * @return
	 */
	public int getReq(String key);
	
	/**
	 * 设置当前节点，总请求书
	 * @param key
	 * @param reqNum
	 * @return
	 */
	public boolean setReq(String key, String reqNum);
	
	/**
	 * 从redis中获取是否进行特殊用户匹配标识
	 * @param key
	 * @return
	 */
	public int getIsCustom();
	
	/**
	 * 向redis中写入是否进行特殊用户匹配标识
	 * @param key
	 */
	public void setIsCustom(String value);
	
}
