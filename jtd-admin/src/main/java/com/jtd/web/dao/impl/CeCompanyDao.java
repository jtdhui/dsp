package com.jtd.web.dao.impl;

import org.springframework.stereotype.Repository;

import com.jtd.commons.dao.BaseDaoImpl;
import com.jtd.web.dao.ICeCompanyDao;
import com.jtd.web.po.CeCompany;

/**
 * Created by duber on 2017/2/17.
 */
@Repository
public class CeCompanyDao extends BaseDaoImpl<CeCompany> implements ICeCompanyDao {

    @Override
    public CeCompany getByCompanyCode(String code) {
        CeCompany obj = getSqlSession().selectOne( getStatement("getByCompanyCode"), code);
        return obj;
    }
}
