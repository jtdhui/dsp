package com.jtd.web.dao.impl;

import org.springframework.stereotype.Repository;

import com.jtd.commons.dao.BaseDaoImpl;
import com.jtd.web.dao.ICeRegionDao;
import com.jtd.web.po.CeRegion;

/**
 * Created by duber on 2017/2/17.
 */
@Repository
public class CeRegionDao extends BaseDaoImpl<CeRegion> implements ICeRegionDao{

    @Override
    public CeRegion getByCode(String code) {
        CeRegion obj = getSqlSession().selectOne( getStatement("getByCode"), code);
        return obj;
    }
}
