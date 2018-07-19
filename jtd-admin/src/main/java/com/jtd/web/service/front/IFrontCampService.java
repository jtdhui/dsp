package com.jtd.web.service.front;

import com.jtd.commons.page.Pagination;
import com.jtd.web.constants.CampaignManulStatus;
import com.jtd.web.po.ActiveUser;
import com.jtd.web.po.Campaign;
import com.jtd.web.po.CampaignDim;
import com.jtd.web.service.IBaseService;

import org.springframework.ui.Model;

import javax.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Map;

public interface IFrontCampService extends IBaseService<Campaign> {

	public Pagination<Campaign> findFrontCampListPageBy(Map<String, Object> paraMap, Integer pageNo, Integer pageSize);

	public List<Map<String, Object>> findCreativesByCampId(Map<String, Object> map);

	public void modifyCatgoryChannels(CampaignDim cd);

	public Campaign getByMap(Campaign camp);

	/**
	 * 保存活动并发送到引擎
	 * @param camp
	 */
	public void savaCampAndSendMessage(Campaign camp);
	
	/**
	 * 修改活动手动状态
	 * @param campaignIds 前台选取的活动ID数组
	 * @param campaignManulStatus 修改的手动状态枚举
	 * @return
	 */
	public String changeCampaignManulStatus(String[] campaignIds, CampaignManulStatus campaignManulStatus);

	public void modifyCampAndSendMessage(Campaign camp,Campaign oldCampaign);

	public void batchUpdateCampaigns(String[] campaignIds, Integer price, Long dailyBudget);

	/**
	 * 活动复制
	 * @param activeUser 
	 * @param campaignId
	 */
	public void saveDuplicateCampaign(ActiveUser activeUser, Long campaignId);

	public List<Map<String,Object>> getCampaignsBy(Map<String, Object> paraMap);

	public void deleteCampaigns(String[] campaignIds);

	public void exportCampaign(List<Map<String, Object>> listCampaigns, HttpServletResponse response);

	/**
	 * 查询该活动下的投放定向数据
	 * @param model
	 * @param cd
	 */
    public void findCampDimData(Model model, CampaignDim cd);

	/**
	 * 查询并封装数据集合 第三步
	 * @param model
	 * @param camp
	 */
	public void queryAndPackageData(Model model, Campaign camp);

	/**
	 * 判断是否可以投入广告
	 * @param activeUser
	 * @param campaignIds
	 * @return
	 */
    public Map<String,Object> isDelivery(ActiveUser activeUser, String[] campaignIds);

	/**
	 * 初始化投放基础数据
	 * @param camp
	 * @param activeUser
	 */
	public void initDirectData(Model model, Campaign camp, ActiveUser activeUser);
}
