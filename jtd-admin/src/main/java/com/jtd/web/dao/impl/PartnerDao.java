package com.jtd.web.dao.impl;

import com.jtd.commons.dao.BaseDaoImpl;
import com.jtd.commons.page.Pagination;
import com.jtd.web.constants.PartnerStatus;
import com.jtd.web.dao.IPartnerDao;
import com.jtd.web.po.Partner;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PartnerDao extends BaseDaoImpl<Partner> implements IPartnerDao{
	
	@Override
	public Map<String, Object> getMapById(long partnerId){
		return getSqlSession().selectOne(getStatement("getMapById"), partnerId);
	}

	@Override
	public List<Partner> listByMap(Map<String, Object> params) {

		if (params == null)
			throw new RuntimeException("params is null");

		return getSqlSession().selectList(getStatement("listByMap"), params);

	}
	
	@Override
	public List<Map<String, Object>> listMapByMap(Map<String, Object> params) {

		if (params == null)
			throw new RuntimeException("params is null");

		return getSqlSession().selectList(getStatement("listMapByMap"), params);

	}
	
	@Override
	public long updateStatus(long partnerId , PartnerStatus status){
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("id", partnerId);
		params.put("status", status.getCode());
		
		int result = getSqlSession().update(getStatement("updateStatus"), params);

		return result;
	}

	@Override
	public long updateAccountBalance(long partnerId , long accBalanceResult){
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("id", partnerId);
		params.put("amount", accBalanceResult);

		int result = getSqlSession().update(getStatement("updateAccountBalance"), params);

		return result;
	}

	@Override
	public List<Map<String, Object>> listAllChildrenByMap(Map<String, Object> params) {
		List<Map<String, Object>> list = getSqlSession().selectList(getStatement("listAllChildrenByMap"), params);
		return list;
	}

	@Override
	public List<Map<String, Object>> listTreeByMap(Map<String, Object> params) {
		List<Map<String, Object>> list = getSqlSession().selectList(getStatement("listTreeByMap"), params);
		return list;
	}

	@Override
	public String getChildrenIdsById(Long id) {
        String ret = getSqlSession().selectOne(getStatement("getChildrenIdsById"),id);
		return ret;
	}

	@Override
	public List<Partner> listChildPartnerByMap(Map<String, Object> partnerMap) {
		List<Partner> list = getSqlSession().selectList(getStatement("listChildPartnerByMap"), partnerMap);
		return list;
	}

	@Override
	public long updateOEMSetting(long targetPartnerId , long sourcePartnerId) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("targetPartnerId", targetPartnerId);
		params.put("sourcePartnerId", sourcePartnerId);
		
		return getSqlSession().update(getStatement("updateOEMSetting"), params);
	}

    @Override
    public List<Map<String, Object>> findPartnerAndChild(Map<String, Object> params) {
        List<Map<String, Object>> list = getSqlSession().selectList(getStatement("findPartnerAndChild"), params);
        return list;
    }

    @Override
    public List<Partner> findPartnerPOAndChild(Map<String, Object> params) {
        List<Partner> list = getSqlSession().selectList(getStatement("findPartnerPOAndChild"), params);
        return list;
    }

	@Override
	public void updateFirstOnlineTime(Map<String, Object> params) {
		getSqlSession().update(getStatement("updateFirstOnlineTime"), params);
	}

	@Override
	public void updateGrossProfit(Map<String, Object> params) {
		
		getSqlSession().update(getStatement("updataGrossProfit"), params);
	}

	@Override
	public List<Partner> findChildren(long partnerId) {
		List<Partner> list = getSqlSession().selectList(getStatement("findChildren"),partnerId);
		return list;
	}

	@Override
	public List<Partner> operatorListByMap(Pagination<Partner> page) {

		if (page == null)
			throw new RuntimeException("page is null");

		return getSqlSession().selectList(getStatement("operatorListByMap"), page);

	}
	
	@Override
	public List<Map<String, Object>> operatorPageListMapByMap(Pagination<Map<String, Object>> page) {

		if (page == null)
			throw new RuntimeException("page is null");

		return getSqlSession().selectList(getStatement("operatorPageListMapByMap"), page);

	}

    @Override
    public List<Partner> listOneLevelPartnerBy(Partner p) {
        List<Partner> list = getSqlSession().selectList(getStatement("listOneLevelPartnerBy"),p);
        return list;
    }

	@Override
	public List<Partner> listById(Long pid) {
		return  getSqlSession().selectList(getStatement("listById"),pid);
	}

}
