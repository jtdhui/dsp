package com.jtd.web.dao;

import java.util.List;
import java.util.Map;

import com.jtd.commons.dao.BaseDao;
import com.jtd.web.po.CreativeSize;

public interface ICreativeSizeDao extends BaseDao<CreativeSize>{

	public String getSize(Long creativeSizeId);
	
	public CreativeSize getSizeByName(String size);
	
	public Map<Long, String> getSizeIdMap();

	public List<String> listCreativeSize(List<Long> matchSizeIds);

}
