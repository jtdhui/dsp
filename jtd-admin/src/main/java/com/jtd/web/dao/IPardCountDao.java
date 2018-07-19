package com.jtd.web.dao;

import com.jtd.commons.dao.BaseDao;
import com.jtd.web.po.count.Pard;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IPardCountDao extends BaseDao<Pard> {

	public Pard getByMap(Map<String, Object> pdMap);
	
	public Map<String,Object> getSum(Long partnerId, Date startDay, Date endDay);
	
	public Map<String,Object> getSum(Map<String, Object> params) ;

    public List<Pard> listByMap(Map<String, Object> paraMap);
}
