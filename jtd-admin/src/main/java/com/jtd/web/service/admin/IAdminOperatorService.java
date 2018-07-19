package com.jtd.web.service.admin;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;

public interface IAdminOperatorService {

	/**
	 * 运营人员-广告主报表
	 * @param paraMap
	 * @param pageNo
	 * @param pageSize
	 * @param userId TODO
	 * @param userId TODO
	 * @return
	 */
	public void partnerReport(Model model , Map<String, Object> paraMap, Integer pageNo, Integer pageSize , HttpServletResponse response, Long userId);
	
	/**
	 * 广告主报表，点击折叠图标查询广告主的下级
	 * 
	 * @param partnerId
	 * @param paraMap
	 * @return
	 */
	public List<Map<String,Object>> getChildrenListForPartnerReport(Long partnerId , Map<String, Object> paraMap);
	
	/**
	 * 运营总监-运营人员报表
	 * 
	 * @param paraMap
	 * @param response TODO
	 * @return
	 */
	public List<Map<String, Object>> operatorReport(
			Map<String, Object> paraMap, HttpServletResponse response);

	/**
	 * 获取百度分创意报表
	 * 
	 * @param startCreativeId
	 * @param endCreativeId 
	 * @param startDate
	 * @param endDate
	 * @param orderBy 
	 * @param desc 
	 * @return
	 */
	public List<Map<String,Object>> besCreativeRTBReport(String startCreativeId,
			String endCreativeId, String startDate, String endDate, final String orderBy, final String desc);

	/**
	 * 下载百度分创意报表
	 * 
	 * @param response
	 */
	public void downloadBesCreativeRTBReport(HttpServletResponse response);
	
	/**
	 * 活动渠道报表
	 * 
	 * @param model
	 * @param params
	 * @param pageNo
	 * @param pageSize
	 */
	public void campChannelReport(Model model, Map<String, Object> params ,
			Integer pageNo, Integer pageSize , HttpServletResponse response);
	
}
