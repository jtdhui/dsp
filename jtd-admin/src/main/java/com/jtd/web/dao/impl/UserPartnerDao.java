package com.jtd.web.dao.impl;

import com.jtd.commons.dao.BaseDaoImpl;
import com.jtd.web.constants.RoleType;
import com.jtd.web.dao.IUserPartnerDao;
import com.jtd.web.po.UserPartner;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @作者 duber
 * @版本 V1.0
 * @创建日期 2016年10月25日
 * @描述 用户与广告主关联dao
 */
@Repository
public class UserPartnerDao extends BaseDaoImpl<UserPartner > implements IUserPartnerDao {

	@Override
	public List<Map<String, Object>> findUserPartnerBy(Map<String, Object> map) {
		List<Map<String, Object>> list = getSqlSession().selectList(getStatement("getUserPartnerBy"),map);
		return list;
	}

	@Override
	public Map<String, Object> findByPartnerAndRole(long partnerId,RoleType roleType) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("partnerId", partnerId);
		map.put("roleId", roleType.getCode());

		List<Map<String, Object>> list = getSqlSession().selectList(getStatement("findByPartnerAndRole"),map);
		if(list != null && list.isEmpty() == false){
			return list.get(0);
		}
		return null ;
	}
	
	public Map<String, Object> findByPartnerAndRoleAndUser(long partnerId,RoleType roleType,long userId) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("partnerId", partnerId);
		map.put("roleId", roleType.getCode());
		map.put("userId", userId);
		
		List<Map<String, Object>> list = getSqlSession().selectList(getStatement("findByPartnerAndRole"),map);
		if(list != null && list.isEmpty() == false){
			return list.get(0);
		}
		return null ;
	}
	

	@Override
	public int updatePartnerUserByRole(long partnerId, long userId, RoleType roleType) {

		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("partnerId", partnerId);
		map.put("userId", userId);
		map.put("roleId", roleType.getCode());

		return getSqlSession().update(getStatement("updatePartnerUserByRole"),map);

	}


	@Override
	public List<Map<String, Object>> queryPartnerRoles(String userId, String partnerId) {
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("userId",userId);
		map.put("partnerId",partnerId);
		List<Map<String, Object>> list = getSqlSession().selectList(getStatement("queryPartnerRoles"),map);
		return list;
	}

	@Override
	public List<UserPartner> listByPartnerIds(Map<String, Object> map) {
		List<UserPartner> list = getSqlSession().selectList(getStatement("listByPartnerIds"),map);
		return list;
	}

}
