package com.jtd.web.service.front;

import java.util.Map;

import com.jtd.web.po.Ad;
import com.jtd.web.service.IBaseService;

public interface IFrontAdService extends IBaseService<Ad> {

	public long updateAdListByCampId(Ad ad);

	public void saveDuplicateAd(Long campaignId, Long newCampaignId);

	public Map<String,Object> findFullAdById(Ad adPO);

	public void deleteAdByCamapignId(Long campaignId);

}
