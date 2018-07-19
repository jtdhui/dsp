package com.jtd.web.dao;

import java.util.List;
import java.util.Map;

import com.jtd.commons.dao.BaseDao;
import com.jtd.web.constants.CatgSerial;
import com.jtd.web.po.AdCategory;

public interface IAdCategoryDao extends BaseDao<AdCategory> {

	public List<Map<String,Object>> selectChannelCatgByAdId(Map<String, Object> map);

	public void deleteByAdId(Long adId);

	public List<AdCategory> selectByAdId(Long adId);

	public AdCategory getAdCategory(AdCategory ac);

    public List<AdCategory> findAdCategoryBy(AdCategory ac);

	/**
	 * 根据活动ID和广告ID删除
	 * @param adCategory
	 */
	public void deleteAdCategory(AdCategory adCategory);

	public void updateCatgId(AdCategory ac);
}
