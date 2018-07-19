package com.jtd.web.service.admin;

import com.jtd.web.po.AdCategory;
import com.jtd.web.service.IBaseService;

import java.util.List;
import java.util.Map;

public interface IAdminAdCategoryService extends IBaseService<AdCategory> {

	public List<Map<String, Object>> selectChannelCatgByAdId(Map<String, Object> map);

	public void deleteByAdId(Long adId);

	public void updateAdCategoryByAdId(AdCategory ac);

    public void deleteAdCategory(AdCategory ac);
}
