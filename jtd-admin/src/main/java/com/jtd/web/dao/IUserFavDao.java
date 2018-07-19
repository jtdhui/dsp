package com.jtd.web.dao;

import com.jtd.commons.dao.BaseDao;
import com.jtd.web.po.UserFav;

public interface IUserFavDao extends BaseDao<UserFav> {
	
	/**
	 * 根据userid和settingName找到一条记录
	 * 
	 * @param userId
	 * @param settingName
	 * @return
	 */
	public UserFav findByUserIdAndSettingName(long userId, String settingName) ;
	
	/**
	 * 根据userid和settingName=operatePartnerId找到当前用户选定的partner
	 * 
	 * @param userId
	 * @return
	 */
	public UserFav findOperatePartnerId(long userId);

    public void deleteUserFav(UserFav userFav);
}
