package com.jtd.web.dao;

import java.util.List;

import com.jtd.commons.dao.BaseDao;
import com.jtd.web.constants.CookieType;
import com.jtd.web.po.CookieGid;

public interface ICookieGidDao extends BaseDao<CookieGid> {

	public List<CookieGid> listByCkType(CookieType ckType);

    public List<CookieGid> listCookieGidsByIds(List<Long> cookieIds);
}
