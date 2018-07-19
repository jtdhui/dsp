package com.jtd.web.dao;
import java.util.List;
import java.util.Map;

import com.jtd.commons.dao.BaseDao;
import com.jtd.web.constants.AdType;
import com.jtd.web.po.CreativeSize;
import com.jtd.web.po.CreativeSizeFlow;

public interface ICreativeSizeFlowDao extends BaseDao<CreativeSizeFlow> {
	
	public List<CreativeSizeFlow> listCreativeSizeFlow(Long[] channelIds, AdType adType);

	public CreativeSizeFlow getTrafficSize(Long sizeId);
	
	public List<CreativeSize> listCreativeSize(List<Long> ids);

	public List<CreativeSizeFlow> listAllCreativeSizeFlow();
	/**
	 * 根据渠道与广告类型查询广告尺寸流量
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> selectListByMap(Map<String, Object> map);
	
	public Map<String,Object> selectSumFlowByMap(Map<String, Object> map);
}
