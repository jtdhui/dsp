package com.jtd.web.dao;

import com.jtd.commons.dao.BaseDao;
import com.jtd.web.po.CeRegion;

/**
 * 分公司所在大区
 */
public interface ICeRegionDao extends BaseDao<CeRegion>{

    public CeRegion getByCode(String code);
}
