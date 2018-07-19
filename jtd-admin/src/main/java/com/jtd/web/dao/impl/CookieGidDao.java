package com.jtd.web.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.jtd.commons.dao.BaseDaoImpl;
import com.jtd.web.constants.CookieType;
import com.jtd.web.dao.ICookieGidDao;
import com.jtd.web.po.CookieGid;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月25日
 * @项目名称 dsp-admin
 * @描述 <p>人群属性</p>
 */
@Repository
public class CookieGidDao extends BaseDaoImpl<CookieGid> implements ICookieGidDao {

	@Override
	public List<CookieGid> listByCkType(CookieType ckType) {
		
		HashMap<String,Object> params = new HashMap<String, Object>();
		params.put("ckType", ckType.getCode());
		
		return getSqlSession().selectList(getStatement("listByMap"),params);
	}

	@Override
	public List<CookieGid> listCookieGidsByIds(List<Long> cookieIds) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("cookieIds",cookieIds);
		return getSqlSession().selectList(getStatement("listCookieGidsByIds"),map);
	}

}
