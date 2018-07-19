package com.jtd.web.dao.impl;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.jtd.commons.dao.BaseDaoImpl;
import com.jtd.web.constants.AdType;
import com.jtd.web.dao.ICreativeSizeFlowDao;
import com.jtd.web.po.CreativeSize;
import com.jtd.web.po.CreativeSizeFlow;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月25日
 * @项目名称 dsp-admin
 * @描述 <p>渠道对应的素材尺寸</p>
 */
@Repository
public class CreativeSizeFlowDao extends BaseDaoImpl<CreativeSizeFlow> implements ICreativeSizeFlowDao {

	
	@Override
	public List<CreativeSizeFlow> listCreativeSizeFlow(Long[] channelIds,AdType adType) {
		List<CreativeSizeFlow> list = new ArrayList<CreativeSizeFlow>();
		
		return list;
	}
	
	@Override
	public CreativeSizeFlow getTrafficSize(Long sizeId) {
		CreativeSizeFlow obj = new CreativeSizeFlow();
		obj.setSizeId(sizeId);
		List<CreativeSizeFlow> list = listBy(obj);
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}

	@Override
	public List<CreativeSizeFlow> listAllCreativeSizeFlow() {
		return listAll();
	}

	@Override
	public List<CreativeSize> listCreativeSize(List<Long> ids) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<Map<String, Object>> selectListByMap(Map<String, Object> map) {
		List<Map<String, Object>> list = getSqlSession().selectList( getStatement("selectListByMap"),map);
		return list;
	}

	@Override
	public Map<String, Object> selectSumFlowByMap(Map<String, Object> map) {
		Map<String, Object> ret = getSqlSession().selectOne(getStatement("selectSumFlowByMap"), map);
		return ret;
	}
	
}
