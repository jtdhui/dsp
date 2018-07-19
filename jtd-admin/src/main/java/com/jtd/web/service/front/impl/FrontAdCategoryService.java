package com.jtd.web.service.front.impl;

import com.alibaba.fastjson.JSONObject;
import com.jtd.commons.dao.BaseDao;
import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.web.constants.CatgSerial;
import com.jtd.web.dao.IAdCategoryDao;
import com.jtd.web.dao.IChannelDao;
import com.jtd.web.jms.ChangeAdCatgMsg;
import com.jtd.web.po.Ad;
import com.jtd.web.po.AdCategory;
import com.jtd.web.po.CampaignDim;
import com.jtd.web.po.Channel;
import com.jtd.web.service.IMQConnectorService;
import com.jtd.web.service.front.IFrontAdCategoryService;
import com.jtd.web.service.front.IFrontCampDimService;
import com.jtd.web.service.impl.BaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FrontAdCategoryService extends BaseService<AdCategory> implements IFrontAdCategoryService {

	@Autowired
	private IAdCategoryDao adCategoryDao;
	
	@Autowired
	private IMQConnectorService mQConnectorService;

	@Autowired
	private IFrontCampDimService frontCampDimService;  // 活动投放策略

	@Autowired
	private IChannelDao channelDao;

	@Override
	protected BaseDao<AdCategory> getDao() {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		return adCategoryDao;
	}
	
	@Override
	public void updateAdCategoryByAdId(AdCategory ac) {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		AdCategory adCategory = adCategoryDao.getAdCategory( ac );
		adCategory.setCatgId(ac.getCatgId());
		adCategoryDao.update(adCategory);
		ChangeAdCatgMsg changeAdCatgMsg = new ChangeAdCatgMsg();
		changeAdCatgMsg.setAdid(ac.getAdId());
		changeAdCatgMsg.setCatgserial(CatgSerial.fromCode(ac.getCatgserial()));
		changeAdCatgMsg.setCatgid(ac.getCatgId());
		mQConnectorService.sendMessage(changeAdCatgMsg);
	}

	@Override
	public List<AdCategory>  findAdCategoryBy(AdCategory ac) {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		return adCategoryDao.findAdCategoryBy(ac);
	}

	@Override
	public void saveAdCategory(Ad ad) {

		CampaignDim cd = new CampaignDim();
		cd.setCampaignId(ad.getCampaignId());
		cd.setDimName("channels");
		CampaignDim campDim = frontCampDimService.selectByCampAndDimName(cd);

		String dimValue= campDim.getDimValue();
		JSONObject jsonObj = JSONObject.parseObject(dimValue);
		String selectVal = jsonObj.getString("selected");
		String typeVal = jsonObj.getString("type");
		AdCategory ac = null;

		if("1".equals(typeVal)){//已选渠道
			selectVal = selectVal.substring(1, selectVal.length()-1);
			String[] ids = selectVal.split(",");

            ac = new AdCategory();
            ac.setCampaignId(ad.getCampaignId());
            ac.setAdId(ad.getId());
            List<AdCategory> oldAdCategoryList = adCategoryDao.findAdCategoryBy(ac);
            // 去除原先投放的渠道而后来不投放的渠道
            for(AdCategory entity: oldAdCategoryList){
                boolean flag = false;
                for (String id:ids) {
                    if(CatgSerial.fromChannelId(Long.parseLong(id)).getCode() == entity.getCatgserial()){
                        flag = true;
                    }
                }
                if(!flag){
                    adCategoryDao.deleteById(entity.getId());
                }
            }

			for (String id:ids) {
				ac = new AdCategory();
				ac.setCampaignId(ad.getCampaignId());
				ac.setAdId(ad.getId());
				ac.setCatgserial(CatgSerial.fromChannelId(Long.parseLong(id)).getCode());
				ac.setCatgId("0");
				ac.setCreateTime(ad.getCreateTime());

				List<AdCategory> adCategoryList = adCategoryDao.findAdCategoryBy(ac);
				if(adCategoryList.size()==0){
					adCategoryDao.insert(ac);
				}else{
					adCategoryDao.update(ac);
				}
			}
		}else {// 渠道不限
			List<Channel> listChannel = channelDao.listAll();
			for(Channel ch: listChannel){
				ac = new AdCategory();
				ac.setCampaignId(ad.getCampaignId());
				ac.setAdId(ad.getId());
				ac.setCatgserial(ch.getCatgserial());
				ac.setCatgId("0");
				ac.setCreateTime(ad.getCreateTime());

				List<AdCategory> adCategory = adCategoryDao.findAdCategoryBy(ac);
				if(adCategory.size()==0){
					adCategoryDao.insert(ac);
				}else{
					adCategoryDao.update(ac);
				}
			}
		}
	}

	@Override
	public void deleteAdCategory(AdCategory adCategory) {
		adCategoryDao.deleteAdCategory(adCategory);
	}

	@Override
	public void updateCatgId(AdCategory ac) {
		adCategoryDao.updateCatgId(ac);
	}

}
