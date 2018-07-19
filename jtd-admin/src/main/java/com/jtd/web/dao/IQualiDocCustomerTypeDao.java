package com.jtd.web.dao;

import java.util.List;
import java.util.Map;

import com.jtd.commons.dao.BaseDao;
import com.jtd.web.po.QualiDocCustomerType;

public interface IQualiDocCustomerTypeDao extends BaseDao<QualiDocCustomerType> {

	public List<QualiDocCustomerType> listByMap(Map<String, Object> params);

}
