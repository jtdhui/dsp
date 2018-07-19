package com.jtd.web.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.jtd.commons.dao.BaseDaoImpl;
import com.jtd.web.dao.ICreativeSizeDao;
import com.jtd.web.po.CreativeSize;
/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月25日
 * @项目名称 dsp-admin
 * @描述 <p>素材大小</p>
 */
@Repository
public class CreativeSizeDao extends BaseDaoImpl<CreativeSize> implements ICreativeSizeDao {

	@Override
	public String getSize(Long creativeSizeId) {
		CreativeSize creativeSize = getSqlSession().selectOne(getStatement(SQL_GET_BY_ID), creativeSizeId);
		if (creativeSize != null) {
			return creativeSize.getSize();
		}
		return null;
	}

	@Override
	public CreativeSize getSizeByName(String size) {
		CreativeSize t = new CreativeSize();
		t.setSize(size);
		List<CreativeSize> list=getSqlSession().selectList(getStatement(SQL_LIST_BY), t);
		if(list!=null && list.size()>0)
			return list.get(0);
		return null;
	}

	@Override
	public Map<Long, String> getSizeIdMap() {
		List<CreativeSize> size = listAll();
		Map<Long, String> ret = new HashMap<Long, String>();
		for (CreativeSize s : size) {
			ret.put(s.getId(), s.getSize());
		}
		return ret;
	}

	@Override
	public List<String> listCreativeSize(List<Long> sizeIds) {
		return null;
	}
}
