package com.jtd.web.dao;

import com.jtd.commons.dao.BaseDao;
import com.jtd.web.po.Creative;

import java.util.Map;

public interface ICreativeDao extends BaseDao<Creative> {

	public Creative getByMap(Creative entity);

    public Map<String,Object> getAdsBy(Map<String, Object> map);
}
