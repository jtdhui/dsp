package com.jtd.web.dao;

import com.jtd.commons.dao.BaseDao;
import com.jtd.web.po.CeSalesman;

/**
 *  【jtd公司】销售人员Dao接口类
 */
public interface ICeSalesmanDao extends BaseDao<CeSalesman> {

    public CeSalesman getByCeSalesmanCode(String code);

}
