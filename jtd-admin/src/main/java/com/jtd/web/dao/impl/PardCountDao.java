package com.jtd.web.dao.impl;

import com.jtd.commons.dao.BaseDaoImpl;
import com.jtd.utils.DateUtil;
import com.jtd.web.dao.IPardCountDao;
import com.jtd.web.po.count.Pard;

import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 广告主按天统计
 * @作者 duber
 * @版本 V1.0
 * @创建日期 2016年11月2日
 * @描述
 */
@Repository
public class PardCountDao extends BaseDaoImpl<Pard> implements IPardCountDao {

	@Override
	public Pard getByMap(Map<String, Object> pdMap) {
		Pard obj = getSqlSession().selectOne(getStatement("getByMap"),pdMap);
		return obj;
	}
	
	public Map<String,Object> getSum(Long partnerId,Date startDay,Date endDay) {
		
		HashMap<String,Object> params = new HashMap<String, Object>();
		params.put("partnerId", partnerId);
		if(startDay != null){
			params.put("startDay", DateUtil.getDateInt(startDay));
		}
		if(endDay != null){
			params.put("endDay", DateUtil.getDateInt(endDay));
		}
		
		return getSqlSession().selectOne(getStatement("getSum"),params);
		
	}

	public Map<String,Object> getSum(Map<String, Object> params) {
		
		if (params == null)
			throw new RuntimeException("params is null");
		
		return getSqlSession().selectOne(getStatement("getSum"),params);
		
	}

    @Override
    public List<Pard> listByMap(Map<String, Object> paraMap) {
        return getSqlSession().selectList(getStatement("listByMap"), paraMap);
    }

}
