package com.jtd.web.service.front;

import java.util.List;
import java.util.Map;

import com.jtd.web.po.CampaignCategory;
import com.jtd.web.service.IBaseService;

public interface IFrontCampCategoryService extends IBaseService<CampaignCategory> {

	public List<Map<String,Object>> findChannelCatgByCampId(Map<String, Object> map);
}
