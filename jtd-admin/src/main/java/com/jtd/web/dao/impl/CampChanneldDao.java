package com.jtd.web.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.jtd.commons.dao.BaseDaoImpl;
import com.jtd.web.dao.ICampChanneldDao;
import com.jtd.web.po.count.CampChanneld;

/**
 * 统计库报表数据
 * 
 * @作者 duber
 * @版本 V1.0
 * @创建日期 2016年11月1日
 * @描述
 */
@Repository
public class CampChanneldDao extends BaseDaoImpl<CampChanneld> implements ICampChanneldDao {
	
	@Override
	public List<Map<String, Object>> listSumByMap(Map<String, Object> param,boolean needGroupBy) {
		
		if (param == null)
			throw new RuntimeException("param is null");

		if(needGroupBy){
			param.put("needGroupBy", needGroupBy);
		}
		else{
			param.remove("needGroupBy");
		}
		
		return getSqlSession().selectList(getStatement("listSumByMap"), param);
	}
	
	@Override
	public List<Map<String, Object>> listByMap(Map<String, Object> param) {
		
		if (param == null)
			throw new RuntimeException("param is null");
		
		return getSqlSession().selectList(getStatement("listByMap"), param);
	}
	
}
