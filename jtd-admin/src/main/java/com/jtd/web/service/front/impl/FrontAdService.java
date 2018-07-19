package com.jtd.web.service.front.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtd.commons.dao.BaseDao;
import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.utils.BeanUtils;
import com.jtd.web.dao.IAdCategoryDao;
import com.jtd.web.dao.IAdDao;
import com.jtd.web.po.Ad;
import com.jtd.web.po.AdCategory;
import com.jtd.web.service.front.IFrontAdService;
import com.jtd.web.service.impl.BaseService;

/**
 * 创意
 * @作者 duber
 * @版本 V1.0
 * @创建日期 2016年11月12日
 * @描述
 */
@Service
public class FrontAdService extends BaseService<Ad> implements IFrontAdService {

	@Autowired
	private IAdDao adDao;
	
	@Autowired
	private IAdCategoryDao adCategoryDao;
	
	@Override
	protected BaseDao<Ad> getDao() {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		return adDao;
	}

	@Override
	public long updateAdListByCampId(Ad ad) {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		return adDao.updateAdListByCampId(ad);
	}

	@Override
	public void saveDuplicateAd(Long campaignId, Long newCampaignId) {
		
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		Ad adEntity = new Ad();
		adEntity.setCampaignId(campaignId);
		List<Ad> ads = adDao.listBy(adEntity);
		for (Ad ad : ads) {
			
			// 复制广告
			Ad newAd = new Ad();
			BeanUtils.copyProperties(ad, newAd);
			newAd.setId(null);
			newAd.setCampaignId(newCampaignId);
			long newAdId = adDao.insert(newAd);
			
			// 复制广告行业类别
			Long adId = ad.getId();
			List<AdCategory> adCategories = adCategoryDao.selectByAdId(adId);
			for (AdCategory adCategory : adCategories) {
				AdCategory newAdCategory = new AdCategory();
				BeanUtils.copyProperties(adCategory, newAdCategory);
				newAdCategory.setId(null);
				newAdCategory.setAdId(newAdId);
				newAdCategory.setCampaignId(newCampaignId);
				adCategoryDao.insert(newAdCategory);
			}
			
		}
	}

	@Override
	public Map<String,Object> findFullAdById(Ad adPO) {
		return adDao.getFullAdById(adPO);
	}

	@Override
	public void deleteAdByCamapignId(Long campaignId) {
		adDao.deleteAdByCamapignId(campaignId);
	}

}
