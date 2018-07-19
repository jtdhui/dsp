package com.jtd.web.dao;

import com.jtd.commons.dao.BaseDao;
import com.jtd.web.po.CeCompany;

/**
 * 【jtd公司】分公司Dao接口类
 *
 */
public interface ICeCompanyDao extends BaseDao<CeCompany> {

    public CeCompany getByCompanyCode(String code);
}
