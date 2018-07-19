package com.jtd.web.service.front;

import com.jtd.web.po.UserFav;
import com.jtd.web.service.IBaseService;

public interface IFrontUserFavService extends IBaseService<UserFav> {

	/**
	 * 更新登录用户下活动的广告主
	 * @param userId
	 * @param partnerId
	 */
	public void saveUserFav(long userId, String partnerId);

}
