package com.jtd.web.dao;

import com.jtd.commons.dao.BaseDao;
import com.jtd.web.constants.RoleType;
import com.jtd.web.po.UserPartner;

import java.util.List;
import java.util.Map;

public interface IUserPartnerDao extends BaseDao<UserPartner> {

	/**
	 * 查询当前用户负责的partner
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> findUserPartnerBy(Map<String, Object> map);
	
	/**
	 * 根据用户角色和partnerid去查询能够看到该partner的用户
	 * @param partnerId 广告主id
	 * @param roleType	角色类型
	 * @return
	 */
	Map<String, Object> findByPartnerAndRole(long partnerId, RoleType roleType);
	
	/**
	 * 修改当前partner的对应的【jtd公司】角色人员（如：财务，运营）
	 * @param partnerId 广告主id
	 * @param roleType	角色类型
	 * @return
	 */
	int updatePartnerUserByRole(long partnerId, long userId, RoleType roleType);
	
	
    List<Map<String,Object>> queryPartnerRoles(String userId, String partnerId);

	/**
	 * 根据广告主ID的集合查询用户与广告主关联的数据
	 * @param map
	 * @return
	 */
    public List<UserPartner> listByPartnerIds(Map<String, Object> map);
}
