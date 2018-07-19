package com.jtd.web.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.jtd.commons.dao.BaseDao;
import com.jtd.web.po.QualiDoc;

public interface IQualiDocDao extends BaseDao<QualiDoc> {

	public List<QualiDoc> listByPartnerId(Long partnerId);

	public List<Map<String, Object>> listMapByPartnerId(Long partnerId);
	
	public Map<String, Object> findByPartnerIdAndDocType(Long partnerId, Long docTypeId);

	public long updateStatus(HashMap<String, Object> map);

}
