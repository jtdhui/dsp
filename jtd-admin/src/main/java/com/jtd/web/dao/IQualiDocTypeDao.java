package com.jtd.web.dao;

import java.util.List;
import java.util.Map;

import com.jtd.commons.dao.BaseDao;
import com.jtd.web.po.QualiDocType;

public interface IQualiDocTypeDao extends BaseDao<QualiDocType> {

	public List<QualiDocType> listByMap(Map<String, Object> params);

	/**
	 * 针对需要找到icp备案和法人身份证的种类id的特殊情况
	 * @param remark
	 * @return
	 */
	public QualiDocType getByRemark(String remark);
}
