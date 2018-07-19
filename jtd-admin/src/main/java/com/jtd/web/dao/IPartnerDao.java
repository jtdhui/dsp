package com.jtd.web.dao;

import java.util.List;
import java.util.Map;

import com.jtd.commons.dao.BaseDao;
import com.jtd.commons.page.Pagination;
import com.jtd.web.constants.PartnerStatus;
import com.jtd.web.po.Partner;

public interface IPartnerDao extends BaseDao<Partner> {
	
	/**
	 * 按id获取map
	 * @param partnerId
	 * @return
	 */
	public Map<String, Object> getMapById(long partnerId);

	/**
	 * 广告主查询
	 * 
	 * @param params
	 * @return
	 */
	public List<Partner> listByMap(Map<String, Object> params);
	
	/**
	 * 广告主查询 by map
	 * 
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> listMapByMap(Map<String, Object> params);
	
	/**
	 * 开启/暂停,修改status
	 */
	public long updateStatus(long partnerId, PartnerStatus status);

	/**
	 * 充值，修改acc_balance字段
	 * 
	 * @param partnerId
	 * @param accBalanceResult
	 * @return
	 */
	public long updateAccountBalance(long partnerId, long accBalanceResult);

	/**
	 * 查询广告主及下面的广告主
	 * 
	 * @param partner
	 * @return 广告主及其子集
	 */
	public List<Map<String, Object>> listAllChildrenByMap(
            Map<String, Object> params);

	public List<Map<String, Object>> listTreeByMap(Map<String, Object> params);

	/**
	 * 根据ID查询广告主的及基子集的ID集合
	 * @param id
	 * @return
	 */
	public String getChildrenIdsById(Long id);

    public List<Partner> listChildPartnerByMap(Map<String, Object> partnerMap);
    
    /**
     * 向targetPartnerId同步sourcePartnerId的个性化设置
     * 
     * @param targetPartnerId
     * @param sourcePartnerId
     * @return
     */
    public long updateOEMSetting(long targetPartnerId, long sourcePartnerId);
    
    /**
     * 根据id记录当前上线时间
     * @param prantnerId
     * 
     */
    public void updateFirstOnlineTime(Map<String, Object> params);
    
    /**
     *修改毛利设置 
     * 
     * @param prantnerId
     */
    public void updateGrossProfit(Map<String, Object> params);

    /**
     *查询所有下级代理
     * @param partnerId 
     * 
     */
    public List<Partner> findChildren(long partnerId);
    
    public List<Map<String,Object>> findPartnerAndChild(Map<String, Object> params);

    public List<Partner> findPartnerPOAndChild(Map<String, Object> pMap);
    
    /**
	 * 运营人员广告主分页列表
	 *
	 * @param params
	 * @return
	 * @see
	 */
	public List<Partner> operatorListByMap(Pagination<Partner> page);
	
	/**
	 * 运营人员广告主列表 by map
	 *
	 * @param params
	 * @return
	 * @see
	 */
	public List<Map<String, Object>> operatorPageListMapByMap(Pagination<Map<String, Object>> page);


    public List<Partner> listOneLevelPartnerBy(Partner p);

	public List<Partner> listById(Long pid);
}
