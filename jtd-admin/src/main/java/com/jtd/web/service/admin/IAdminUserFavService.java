package com.jtd.web.service.admin;

import com.jtd.web.po.UserFav;
import com.jtd.web.service.IBaseService;

/**
 * Created by duber on 16/12/8.
 */
public interface IAdminUserFavService extends IBaseService<UserFav> {

    public void deleteUserFav(UserFav userFav);
}
