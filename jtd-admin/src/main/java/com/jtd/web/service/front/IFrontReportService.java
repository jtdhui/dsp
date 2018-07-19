package com.jtd.web.service.front;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;

public interface IFrontReportService {

	/**
	 * 读取时间报表的数据
	 * 
	 * @param model
	 * @param partnerId
	 * @param params
	 * @param pageNo
	 * @param pageSize
	 */
	public void time(Model model, long partnerId,
                     Map<String, Object> params, Integer pageNo, Integer pageSize);
	
	
	/**
	 * 读取活动类型报表的数据
	 * 
	 * @param model
	 * @param partnerId
	 * @param params
	 * @param pageNo
	 * @param pageSize
	 */
	public void campType(Model model, long partnerId,
                         Map<String, Object> params, Integer pageNo, Integer pageSize);

	/**
	 * 读取活动报表的数据
	 * 
	 * @param model
	 * @param partnerId
	 * @param params
	 * @param pageNo
	 * @param pageSize
	 */
	public void camp(Model model, long partnerId,
                     Map<String, Object> params, Integer pageNo, Integer pageSize);

	/**
	 * 读取创意报表的数据
	 * 
	 * @param model
	 * @param partnerId
	 * @param params
	 * @param pageNo
	 * @param pageSize
	 */
	public void creative(Model model, long partnerId,
                         Map<String, Object> params, Integer pageNo, Integer pageSize);
	
	/**
	 * 读取地域（城市）报表的数据
	 * 
	 * @param model
	 * @param partnerId
	 * @param params
	 * @param pageNo
	 * @param pageSize
	 */
	public void city(Model model, long partnerId,
                     Map<String, Object> params, Integer pageNo, Integer pageSize);
	
	/**
	 * 读取时段（小时）报表的数据
	 * 
	 * @param model
	 * @param partnerId
	 * @param params
	 * @param pageNo
	 * @param pageSize
	 */
	public void hour(Model model, long partnerId, Map<String, Object> params);
	
	/**
	 * 获取各报表的excel
	 * 
	 * @param flag
	 * @param response
	 */
	public void getExcel(int flag, HttpServletResponse response);
	
	public void downloadCityReport(Model model, long partnerId,
			Map<String, Object> params, HttpServletResponse response);
}
