package com.jtd.web.service.admin;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;

import com.jtd.commons.page.Pagination;
import com.jtd.web.constants.PartnerStatus;
import com.jtd.web.exception.PlatformException;
import com.jtd.web.po.ActiveUser;
import com.jtd.web.po.Partner;
import com.jtd.web.po.PartnerDim;
import com.jtd.web.po.PartnerPreFlow;
import com.jtd.web.service.IBaseService;
import com.jtd.web.vo.ChannelCategory;

public interface IAdminPartnerService extends IBaseService<Partner> {

	/**
	 * 通过partnerId查询行业id，再通过行业id返回内存中的行业映射信息
	 * 
	 * @param partnerId
	 * @return
	 * @throws PlatformException
	 */
	public ChannelCategory getPartnerCategory(Long partnerId);

	/**
	 * 查询
	 * 
	 * @param id
	 * @return
	 * @throws PlatformException
	 */
	public Partner getById(long partnerId);

	/**
	 * 从user的可视数据范围内，返回一个按层级排列好的partner的list (pid=0的partner为第一级)
	 * 
	 * @param partnerId
	 * @param needSelf TODO
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> listPartnerHierarchy(Long partnerId, boolean needSelf)
			throws PlatformException;

	/**
	 * 查找一个partner的最上级partner（如果自己就是没有上级的partner，则返回自己）
	 *
	 * @param partnerId
	 * @return
	 */
	public Partner getAncestorPartner(long partnerId);

	/**
	 * 
	 * 新增或修改的保存(事务)
	 * 
	 * @param partner
	 *            广告主信息
	 * @param ceOperatorUserId
	 *            页面上选择的【jtd公司】运营人员的userId（如果当前操作用户不是【jtd公司】员工，或者当前编辑的partner是【jtd公司】，
	 *            不会有operatorUserId参数）
	 * @param user
	 *            当前操作人员信息
	 * @throws PlatformException
	 */
	public void saveOrUpdate(Partner partner, Long ceOperatorUserId,
			ActiveUser user, HttpServletRequest request, boolean isInsert);

	/**
	 * 向partner_category表保存相关行业映射
	 * 
	 * @param partner
	 * @throws PlatformException
	 */
	public void savePartnerCategory(long partnerId, long categoryId);

	/**
	 * 停用，启用
	 * 
	 * @param id
	 * @param status
	 * @return
	 * @throws PlatformException
	 */
	public long updateStatus(Long partnerId, PartnerStatus status)
			throws PlatformException;

	/**
	 * 
	 * 定时任务taskOfCheckPartnerAccBalance会每10分钟调用
	 * 
	 * 1.查询昨日消费 ，生成一条昨日的广告扣费流水(每个partner每天只做一次)
	 * 
	 * 2.查询今天所有消费 ， 计算账户当前真实余额 （acc_balance字段 -（今天截止当前消费 - 之前消费的快照））
	 * ，并从partner的acc_balance字段，如果真实余额小于等于0，则返回FALSE
	 * 
	 * @param partnerId
	 */
	public boolean checkPartnerAccBalance(Partner partner);

	/**
	 * 修改账户余额(事务)
	 * 
	 * @param partner
	 * @param amountFen
	 * @throws PlatformException
	 */
	public long updateAccountBalance(Partner partner, long amountFen,
			ActiveUser activeUser, int is_finance_recharge, int fromTradeType,
			int toTradeType, PartnerPreFlow partnerPreFlow)
			throws PlatformException;

	/**
	 * 查询广告主黑名单
	 * 
	 * @param partnerId
	 * @return
	 * @throws PlatformException
	 */
	public PartnerDim loadBlacklist(long partnerId) throws PlatformException;

	/**
	 * 修改广告主黑名单
	 * 
	 * @param partnerId
	 * @param dimValue
	 * @throws PlatformException
	 */
	public void saveOrUpdateBlacklist(long partnerId, String dimValue)
			throws PlatformException;

	public List<Map<String, Object>> listAllChildren(Map<String, Object> params);

	public List<Partner> listAll();

	public List<Map<String, Object>> listTreeByMap(Map<String, Object> params);

	public String findChildrenIdsById(Long id);

	public List<Partner> listChildPartnerByMap(Map<String, Object> partnerMap);

	
	/**
	 * 将广告主列表分级排序
	 * 
	 * @param partnerList
	 * @return
	 */
	public List<Map<String, Object>> sortTreeTable(Partner partner,
			List<Map<String, Object>> partnerList, ActiveUser activeUser);

	/**
	 * 
	 * 调用boss广告主开户回调接口（** 在客户状态变为开启以后才能点击同步按钮，并且同步成功之后，不允许再点击同步按钮）
	 * 
	 * @param partnerId
	 * @param bossPartnerCode
	 * @return 返回调用的结果和消息，如：{code:0，msg:"xxxxxx"}，0为成功，非0为失败
	 */
	public HashMap<String, Object> callbackBoss(long partnerId,
			String bossPartnerCode);

	/**
	 * 返回广告主能充值的限额
	 * 
	 * @param partnerId
	 * @return
	 */
	public long validAcountRechargeLimit(Long partnerId);

	/**
	 * 记录广告主上线时间
	 * 
	 * @param partnerId
	 */
	public void updateFirstOnlineTime(long partnerId) throws PlatformException;

	/**
	 * 修改毛利设置
	 *
	 * @param partnerId
	 * @param grossOther
	 * @throws PlatformException
	 *
	 */
	public void updateGrossProfit(long partnerId, Partner partner)
			throws PlatformException;

	public List<Map<String, Object>> findPartnerAndChild(
			Map<String, Object> partnerListQueryMap);

	public List<Partner> findPartnerPOAndChild(Map<String, Object> pMap);

	public Pagination<Partner> findAdminPageBy(HashMap<String, Object> paraMap,
			Integer pageNo, Integer pageSize, ActiveUser user);

	/**
	 * 运营人员-广告主列表
	 * @param paraMap
	 * @param pageNo
	 * @param pageSize
	 * @param user TODO
	 * @param user TODO
	 * @return
	 */
	public void partnerOperatorList(Model model , Map<String, Object> paraMap, Integer pageNo, Integer pageSize , ActiveUser user);
    
    
	/**
     * 
	 *查询广告主下级代理
	 *@param partnerId
	 *@throws PlatformException 
	 *
	 */
    public List<Map<String, Object>> findChildren(Map<String, Object> paraMap);
    
	/**
	 * 根据当前登录用户返回其广告主的数据权限
	 * 
	 * @param activeUser
	 * @return
	 */
	public List<Partner> findPartnerListByLoginUser(ActiveUser activeUser);

	/**
	 * 获取广告主的账户流水总计，单位为元
	 * 
	 * @param partnerId
	 *            广告主
	 * @param startDate
	 *            开始日期
	 * @param endDate
	 *            结束日期
	 * @return
	 */
	public double getPartnerRechargeAmountSumYuan(Long partnerId,
			String startDate, String endDate);

	public List<Partner> listByMap(Map<String, Object> map);

	/**
	 * 查询代理充值明细广告主名称
	 * 
	 * @param Partner p
	 * @return
	 */
    public List<Partner> listOneLevelPartnerBy(Partner p);

    /**
	 * 查询代理商广告主下级
	 * 
	 * @param Partner p
	 * @return
	 */
	public List<Partner> listById(Long pid);
}
