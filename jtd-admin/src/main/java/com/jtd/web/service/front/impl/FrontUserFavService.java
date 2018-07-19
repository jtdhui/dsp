package com.jtd.web.service.front.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtd.commons.dao.BaseDao;
import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.web.dao.IUserFavDao;
import com.jtd.web.po.UserFav;
import com.jtd.web.service.front.IFrontUserFavService;
import com.jtd.web.service.impl.BaseService;

@Service
public class FrontUserFavService extends BaseService<UserFav> implements IFrontUserFavService {

	private static final String SETTING_NAME = "operatePartnerId";
	
	@Autowired
	private IUserFavDao userFavDao;
	
	@Override
	protected BaseDao<UserFav> getDao() {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		return userFavDao;
	}
	
	/**
	 * 更新登录用户下活动的广告主
	 */
	@Override
	public void saveUserFav(long userId, String partnerId) {
		UserFav userFav = new UserFav();
		userFav.setUserId(userId);
		userFav.setSettingName(SETTING_NAME);
		List<UserFav> list = userFavDao.listBy(userFav);
		userFav.setSetting(partnerId);
		if(list.size()>0){
			userFavDao.update(userFav);
		}else{
			userFavDao.insert(userFav);
		}
	}

}
