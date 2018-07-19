package com.jtd.web.dao;

import java.util.List;
import java.util.Map;

import com.jtd.commons.dao.BaseDao;
import com.jtd.web.po.count.Parh;

public interface IParhCountDao extends BaseDao<Parh> {

	public List<Parh> listByMap(Map<String, Object> phMap);

}
