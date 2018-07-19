package com.jtd.web.service.admin.impl;

import com.jtd.commons.dao.BaseDao;
import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.web.constants.CatgSerial;
import com.jtd.web.dao.IAdCategoryDao;
import com.jtd.web.jms.ChangeAdCatgMsg;
import com.jtd.web.po.AdCategory;
import com.jtd.web.service.IMQConnectorService;
import com.jtd.web.service.admin.IAdminAdCategoryService;
import com.jtd.web.service.impl.BaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AdminAdCategoryService extends BaseService<AdCategory> implements IAdminAdCategoryService {

	@Autowired
	private IAdCategoryDao adCategoryDao;
	
	@Autowired
	private IMQConnectorService mQConnectorService;
	
	@Override
	protected BaseDao<AdCategory> getDao() {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		return adCategoryDao;
	}
	
	@Override
	public List<Map<String, Object>> selectChannelCatgByAdId(Map<String, Object> map) {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		List<Map<String, Object>> list =adCategoryDao.selectChannelCatgByAdId(map);
		return list;
	}

	@Override
	public void deleteByAdId(Long adId) {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		adCategoryDao.deleteByAdId(adId);
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
    public void deleteAdCategory(AdCategory ac) {
        adCategoryDao.deleteAdCategory(ac);
    }

}
