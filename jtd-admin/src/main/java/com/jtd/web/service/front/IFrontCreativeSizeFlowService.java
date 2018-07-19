package com.jtd.web.service.front;

import java.util.List;
import java.util.Map;

import com.jtd.web.po.CreativeSizeFlow;
import com.jtd.web.service.IBaseService;

/**
 * 渠道对应的素材尺寸
 * @作者 duber
 * @版本 V1.0
 * @创建日期 2016年11月10日
 * @描述
 */
public interface IFrontCreativeSizeFlowService extends IBaseService<CreativeSizeFlow> {

	public List<Map<String, Object>> selectListByMap(Map<String, Object> map);
	
	public Map<String, Object> selectSumFlowByMap(Map<String, Object> map);
}
