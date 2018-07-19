package com.jtd.web.dao.impl;

import org.springframework.stereotype.Repository;

import com.jtd.commons.dao.BaseDaoImpl;
import com.jtd.web.dao.ICeSalesmanDao;
import com.jtd.web.po.CeSalesman;

/**
 * Created by duber on 2017/2/17.
 */
@Repository
public class CeSalesmanDao extends BaseDaoImpl<CeSalesman> implements ICeSalesmanDao{

    @Override
    public CeSalesman getByCeSalesmanCode(String code) {
        CeSalesman obj = getSqlSession().selectOne( getStatement("getByCeSalesmanCode"), code);
        return obj;
    }
}
