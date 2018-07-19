package com.jtd.web.service.admin;

import java.util.List;
import java.util.Map;

import com.jtd.commons.page.Pagination;
import com.jtd.web.po.Campaign;
import com.jtd.web.service.IBaseService;

/**
 * 
 * @作者 duber
 * @版本 V1.0
 * @创建日期 2016年10月19日
 * @描述	后台广告活动Service接口类
 */
public interface IAdminCampService  extends IBaseService<Campaign>{

	/**
	 * 后台活动列表
	 * @param map
	 * @return
	 */
	public Pagination<Map<String, Object>> listCampBy(Map<String, Object> paraMap, Integer pageNo, Integer pageSize) ;

	/**
	 * 后台活动按ID查询
	 * @param id
	 * @return
	 */
	public Map<String, Object> findCampBy(long id);
	
	/**
	 * 后台活动关联查询
	 * @param id
	 * @return
	 */
	public Map<String, Object> findCampByMap(Map<String, Object> map);

	/**
	 * 毛利设置查询
	 * @param id
	 * @return
	 */
	public Map<String, Object> findCampPartnerBy(long id);

	/**
	 * 把指定广告主在投的活动手动暂停
	 * 
	 * @param partnersNeedToStopCampList
	 * @return
	 */
    public String stopCampaigns(Long[] partnerIds);

    public void updateProfit(Campaign camp);

    public String checkCampaignAndSendMq(Campaign campaign);
}
