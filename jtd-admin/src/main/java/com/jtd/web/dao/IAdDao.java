package com.jtd.web.dao;

import java.util.List;
import java.util.Map;

import com.jtd.commons.dao.BaseDao;
import com.jtd.web.po.Ad;

/**
 * 
 * @作者 duber
 * @版本 V1.0
 * @创建日期 2016年10月27日
 * @描述 后台广告DAO接口
 */
public interface IAdDao extends BaseDao<Ad> {

	public Map<String, Object> findAdMapById(Map<String, Object> paraMap);

	public List<Map<String, Object>> listByMap(Map<String, Object> paraMap);

	public long updateAdListByCampId(Ad ad);

	public Map<String, Object> getFullAdById(Ad ad);

	public void deleteAdByCamapignId(Long campaignId);
}
