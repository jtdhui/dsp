package com.jtd.web.dao.impl;

import org.springframework.stereotype.Repository;

import com.jtd.commons.dao.BaseDaoImpl;
import com.jtd.web.dao.IUserFavDao;
import com.jtd.web.po.UserFav;

@Repository
public class UserFavDao extends BaseDaoImpl<UserFav> implements IUserFavDao {

	public UserFav findByUserIdAndSettingName(long userId, String settingName) {

		UserFav uf = new UserFav();
		uf.setUserId(userId);
		uf.setSettingName(settingName);

		return getSqlSession().selectOne(getStatement("listBy"), uf);
	}
	
	public UserFav findOperatePartnerId(long userId) {

		UserFav uf = new UserFav();
		uf.setUserId(userId);
		uf.setSettingName("operatePartnerId");

		return getSqlSession().selectOne(getStatement("listBy"), uf);
	}

	@Override
	public void deleteUserFav(UserFav userFav) {
		getSqlSession().delete(getStatement("deleteUserFav"),userFav);
	}

}
