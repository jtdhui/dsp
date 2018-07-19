package com.jtd.web.service.front.impl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONObject;
import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.commons.page.Pagination;
import com.jtd.utils.ExcelUtil;
import com.jtd.utils.FileUtil;
import com.jtd.utils.ReportUtil;
import com.jtd.web.constants.AdType;
import com.jtd.web.constants.CampaignStatus;
import com.jtd.web.constants.CampaignType;
import com.jtd.web.constants.Constants;
import com.jtd.web.dao.IAdDao;
import com.jtd.web.dao.ICampAdTpdCountDao;
import com.jtd.web.dao.ICampCitydCountDao;
import com.jtd.web.dao.ICampCredCountDao;
import com.jtd.web.dao.ICampaignDao;
import com.jtd.web.dao.ICampdCountDao;
import com.jtd.web.dao.ICamphCountDao;
import com.jtd.web.service.front.IFrontReportService;

@Service
public class FrontReportService implements IFrontReportService {
	
	@Autowired
	private ICampaignDao campDao;
	@Autowired
	private ICampdCountDao campdCountDao;
	@Autowired
	private ICampAdTpdCountDao campAdTpdCountDao;
	@Autowired
	private ICampCredCountDao campCredCountDao;
	@Autowired
	private IAdDao adDao;
	@Autowired
	private ICampCitydCountDao campCitydCountDao;
	@Autowired
	private ICamphCountDao camphCountDao;
	@Autowired(required=false)
	private HttpServletRequest httpRequest ;

	/**
	 * 创建一个空的6项sum值
	 * 
	 * @return
	 */
	private Map<String, Object> getEmptySumSixMap() {
		HashMap<String, Object> sumSixMap = new HashMap<String, Object>();
		sumSixMap.put("pv_sum", 0);
		sumSixMap.put("click_sum", 0);
		sumSixMap.put("click_rate_sum", 0);
		sumSixMap.put("cpm_sum_yuan", 0);
		sumSixMap.put("cpc_sum_yuan", 0);
		sumSixMap.put("expend_sum_yuan", 0);
		return sumSixMap;
	}

	@Override
	public void time(Model model, long partnerId,
			Map<String, Object> params, Integer pageNo, Integer pageSize) {

		//设置默认自定义排序
		if(params.get("orderBy") == null){
			params.put("orderBy", "date");
			params.put("desc", "desc");
		}
		
		// 切换业务库数据源
		CustomerContextHolder
				.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

		params.put("partnerId", partnerId);

		// 由于统计库无法做名称模糊查询，所以先从业务库里查出所有的活动
		List<Map<String, Object>> campList = campDao.listReportCampBy(params);
		if (campList != null) {
			// 得到所有的活动id
			StringBuffer campIds = new StringBuffer("");
			
			for (Map<String, Object> map : campList) {
				if (campIds.length() == 0) {
					campIds.append(map.get("id"));
				} else {
					campIds.append(",").append(map.get("id"));
				}
			}

			// 用id范围去查询统计库
			params.put("campIds", campIds.toString());

			// 切换统计库数据源
			CustomerContextHolder
					.setContextType(CustomerContextHolder.SESSION_FACTORY_COUNT);

			// -------------------------汇总六格------------------------------
			Map<String, Object> sumSixMap = getEmptySumSixMap();

			// 如果之前通过类型，状态，名称查到了相关活动
			if (campIds.length() != 0) {
				try {
					sumSixMap = campdCountDao
							.getReportSumSix(params);
				} catch (Exception e) {
					sumSixMap = getEmptySumSixMap();
				}
			}

			model.addAttribute("sumSixMap", sumSixMap);
			
			// -------------------------汇总六格 end------------------------------

			// ------------------------图形报表-------------------------------

			//准备给图形报表用的查询条件，为了保持日期的排序，所以不能有其他排序关键字
			HashMap<String, Object> chartParams = new HashMap<String, Object>();
			chartParams.putAll(params);
			chartParams.remove("orderBy");
			chartParams.remove("desc");
			
			List<Map<String, Object>> timeChartDataList = null;
			//不加对campIds的判断，sql会报错
			if (campIds.length() != 0) {
				timeChartDataList = campdCountDao.listByMapForTimeChart(chartParams,true);
			}
			
			//分页下面的一行总计
//			List<Map<String, Object>> timeAllSumData = campdCountDao.listByMapForTimeChart(params,false);
//			if(timeAllSumData != null && timeAllSumData.size() == 1){
//				model.addAttribute("timeAllSumData", timeAllSumData.get(0));
//			}

			ArrayList<String> xAxisDateList = new ArrayList<String>();
			ArrayList<Integer> pvList = new ArrayList<Integer>();
			ArrayList<Integer> clickList = new ArrayList<Integer>();
			ArrayList<Float> clickRateList = new ArrayList<Float>();
			ArrayList<Float> cpmList = new ArrayList<Float>();
			ArrayList<Float> cpcList = new ArrayList<Float>();
			ArrayList<Float> expendList = new ArrayList<Float>();

			if (timeChartDataList != null) {

				for (Map<String, Object> map : timeChartDataList) {

					pvList.add(((BigDecimal) map.get("pv_sum")).intValue());
					clickList.add(((BigDecimal) map.get("click_sum"))
							.intValue());
					clickRateList.add(((BigDecimal) map.get("click_rate_sum"))
							.floatValue());
					cpmList.add(((BigDecimal) map.get("cpm_sum_yuan"))
							.floatValue());
					cpcList.add(((BigDecimal) map.get("cpc_sum_yuan"))
							.floatValue());

					// float expend = (int) map.get("expend") / 100f;
					expendList.add(((BigDecimal) map.get("expend_sum_yuan"))
							.floatValue());

					String dateStr = (int) map.get("date") + "";
					Pattern pattern = Pattern
							.compile("^\\d{4}(\\d{2})(\\d{2})$");
					Matcher matcher = pattern.matcher(dateStr);
					if (matcher != null && matcher.find()) {
						String month = matcher.group(1);
						String day = matcher.group(2);
						dateStr = month + "月" + day + "日";
					}
					xAxisDateList.add(dateStr);
				}
			}

			model.addAttribute("xAxisList",
					JSONObject.toJSONString(xAxisDateList));
			model.addAttribute("pvList", JSONObject.toJSONString(pvList));
			model.addAttribute("clickList", JSONObject.toJSONString(clickList));
			model.addAttribute("clickRateList",
					JSONObject.toJSONString(clickRateList));
			model.addAttribute("cpmList", JSONObject.toJSONString(cpmList));
			model.addAttribute("cpcList", JSONObject.toJSONString(cpcList));
			model.addAttribute("expendList",
					JSONObject.toJSONString(expendList));

			// ------------------------图形报表 end -------------------------------
			
			//------------------------ excel下载 --------------------------------
			//不加对campIds的判断，sql会报错
			if (campIds.length() != 0) {
				List<Map<String, Object>> timeExcleDataList = campdCountDao.listByMapForTimeChart(params,true);
				
				sumSixMap.put("date", "总计");
				timeExcleDataList.add(sumSixMap);
				
				//放入session中提供报表下载使用
				HttpSession session = ((ServletRequestAttributes) RequestContextHolder
						.getRequestAttributes()).getRequest().getSession();
				session.setAttribute("timeReportExcelData", timeExcleDataList);
			}
			
			//------------------------ excel下载  end--------------------------------
			
			// --------------------- 分页列表 --------------------------------

			// 如果之前通过类型，状态，名称查到了相关活动
			if (campIds.length() != 0) {
				pageNo = null == pageNo ? 1 : pageNo;
				pageSize = null == pageSize ? 10 : pageSize;

				Pagination<Map<String, Object>> page = new Pagination<Map<String, Object>>();
				page.setPageNo(pageNo);
				page.setPageSize(pageSize);
				page.setCondition(params);
				
				List<Map<String, Object>> pageList = campdCountDao
						.getMapPageForTimeReport(page);
				page.setList(pageList);

				model.addAttribute("page", page);
			}

			// --------------------- 分页列表 end --------------------------------

		} else {
			model.addAttribute("sumSixMap", getEmptySumSixMap());
		}

	}

	@Override
	public void campType(Model model, long partnerId,
			Map<String, Object> params, Integer pageNo, Integer pageSize) {
		
		//设置默认自定义排序
		if(params.get("orderBy") == null){
			params.put("orderBy", "camp_type");
			params.put("desc", "desc");
		}

		// 切换业务库数据源
		CustomerContextHolder
				.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

		params.put("partnerId", partnerId);

		// 由于统计库无法做名称模糊查询，所以先从业务库里查出所有的活动
		List<Map<String, Object>> campList = campDao.listReportCampBy(params);
		if (campList != null) {
			// 得到所有的活动id
			StringBuffer campIds = new StringBuffer("");
			for (Iterator<Map<String, Object>> iterator = campList.iterator(); iterator
					.hasNext();) {
				Map<String, Object> map = (Map<String, Object>) iterator.next();
				if (campIds.length() == 0) {
					campIds.append(map.get("id"));
				} else {
					campIds.append(",").append(map.get("id"));
				}
			}

			// 用id范围去查询统计库
			params.put("campIds", campIds.toString());
			
			// 切换统计库数据源
			CustomerContextHolder
					.setContextType(CustomerContextHolder.SESSION_FACTORY_COUNT);

			// -------------------------汇总六格------------------------------
			Map<String, Object> sumSixMap = getEmptySumSixMap();

			// 如果之前通过类型，状态，名称查到了相关活动
			if (campIds.length() != 0) {
				try {
					sumSixMap = campdCountDao
							.getReportSumSix(params);
				} catch (Exception e) {
					sumSixMap = getEmptySumSixMap();
				}
			}

			model.addAttribute("sumSixMap", sumSixMap);

			// -------------------------汇总六格 end------------------------------
			
			// ------------------------图形报表-------------------------------

			// 在报表中显示的类别名称列表
			ArrayList<String> typeNameList = new ArrayList<String>();

			// 先查询按活动类型统计的消耗
			HashMap<String, Object> chartWithOrderParams = new HashMap<String, Object>();
			chartWithOrderParams.putAll(params);
			chartWithOrderParams.remove("orderBy");
			chartWithOrderParams.remove("desc");
			
			List<Map<String, Object>> campTypeOriginChartDataList = null ;
			//不加对campIds的判断，sql会报错
			if (campIds.length() != 0) {
				campTypeOriginChartDataList = campdCountDao
						.listByMapForCampTypeChart(chartWithOrderParams,true);
			}
//				//分页下面的一行总计
//				List<Map<String, Object>> campTypeAllSumData = campdCountDao.listByMapForCampTypeChart(params,false);
//				if(campTypeAllSumData != null && campTypeAllSumData.size() == 1){
//					model.addAttribute("campTypeAllSumData", campTypeAllSumData.get(0));
//				}
			
			/**
			 * 按广告类型统计 (pc,,mobile等等)
			 */
			ArrayList<Map<String, Object>> campTypeFinalChartDataList = new ArrayList<Map<String, Object>>();
			
			if (campTypeOriginChartDataList != null) {
				
				for (Map<String, Object> map : campTypeOriginChartDataList) {

					joinCampTypeData(map);
					
					typeNameList.add(""+map.get("camp_type_name"));
					
					//在图表中显示的数据
					Map<String, Object> dataMap = new HashMap<String, Object>();
					dataMap.put("value", ((BigDecimal) map
							.get("expend_sum_yuan")).floatValue());
					dataMap.put("name", map.get("camp_type_name"));
					
					campTypeFinalChartDataList.add(dataMap);
					
				}
				
			}
			
			//用于显示在图标中的数据
			model.addAttribute("campTypeChartDataList", JSONObject.toJSONString(campTypeFinalChartDataList));
			
			/**
			 * 再按展示类型统计 (banner,文字链,等等)
			 */
			ArrayList<Map<String, Object>> adTypeChartDataList = new ArrayList<Map<String, Object>>();
			if (campIds.length() != 0) {
				
				List<Map<String, Object>> adTypeDataList = campAdTpdCountDao
						.listByMapForCampTypeChart(params);
				
				if (adTypeDataList != null) {
					
					for (Map<String, Object> map : adTypeDataList) {

						int adType = (int) map.get("ad_type");
						// 获取展示类型的名称
						AdType enums = AdType.getAdType(adType);
						
						String adTypeName = null ;
						// 列入类别名称列表
						if(enums != null){
							adTypeName = "展示类型-" + enums.getDesc() ;
						}
						else{
							adTypeName = "展示类型-未知" ;
						}
						typeNameList.add(adTypeName);
						
						//在图表中显示的数据项
						Map<String, Object> dataMap = new HashMap<String, Object>();
						dataMap.put("value", ((BigDecimal) map
								.get("expend_sum_yuan")).floatValue());
						dataMap.put("name", adTypeName);

						adTypeChartDataList.add(dataMap);
					}

				}
			}
			
			model.addAttribute("adTypeChartDataList", JSONObject.toJSONString(adTypeChartDataList));

			//显示在报表legend上面的项
			model.addAttribute("typeNameList", JSONObject.toJSONString(typeNameList));
			
			// ------------------------图形报表 end -------------------------------
			
			//-------------------------excel下载 ---------------------------------
			
			//不加对campIds的判断，sql会报错
			if (campIds.length() != 0) {
				List<Map<String, Object>> campTypeExcelDataList = campdCountDao
						.listByMapForCampTypeChart(params,true);
				
				if (campTypeExcelDataList != null) {
					for (Map<String, Object> map : campTypeExcelDataList) {
						joinCampTypeData(map);
					}
				}
				
				sumSixMap.put("camp_type_name", "总计");
				campTypeExcelDataList.add(sumSixMap);
				
				//放入session中提供下载报表使用
				HttpSession session = ((ServletRequestAttributes) RequestContextHolder
						.getRequestAttributes()).getRequest().getSession();
				session.setAttribute("campTypeReportExcelData", campTypeExcelDataList);
			}
			
			//-------------------------excel下载 end -----------------------------
			

			// --------------------- 分页列表 --------------------------------
			pageNo = null == pageNo ? 1 : pageNo;
			pageSize = null == pageSize ? 10 : pageSize;

			if (campIds.length() != 0) {
				Pagination<Map<String, Object>> page = new Pagination<Map<String, Object>>();
				page.setPageNo(pageNo);
				page.setPageSize(pageSize);
				page.setCondition(params);
				
				List<Map<String, Object>> pageList = campdCountDao
						.getMapPageForCampTypeReport(page);
				if (pageList != null) {

					for (Map<String, Object> map : pageList) {

						joinCampTypeData(map);
					}

				}
				page.setList(pageList);

				model.addAttribute("page", page);
			}
			
			// --------------------- 分页列表 end --------------------------------
			
		} else {
			model.addAttribute("sumSixMap", getEmptySumSixMap());
		}

	}
	
	private void joinCampTypeData(Map<String, Object> countMapData){
		
		int campType = (int) countMapData.get("camp_type");
		CampaignType enums = CampaignType.fromCode(campType);
		String campTypeName = enums != null ? enums.getDesc() : "未知" ;
		countMapData.put("camp_type_name", campTypeName);
		
	}
	
	@Override
	public void camp(Model model, long partnerId,
			Map<String, Object> params, Integer pageNo, Integer pageSize) {
		
		//设置默认自定义排序
		if(params.get("orderBy") == null){
			params.put("orderBy", "camp_id");
			params.put("desc", "desc");
		}

		// 切换业务库数据源
		CustomerContextHolder
				.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

		params.put("partnerId", partnerId);

		// 由于统计库无法做名称模糊查询，所以先从业务库里查出所有的活动
		List<Map<String, Object>> campList = campDao.listReportCampBy(params);
		if (campList != null) {
			// 得到所有的活动id
			StringBuffer campIds = new StringBuffer("");
			for (Iterator<Map<String, Object>> iterator = campList.iterator(); iterator
					.hasNext();) {
				Map<String, Object> map = (Map<String, Object>) iterator.next();
				if (campIds.length() == 0) {
					campIds.append(map.get("id"));
				} else {
					campIds.append(",").append(map.get("id"));
				}
			}

			// 用id范围去查询统计库
			params.put("campIds", campIds.toString());

			// 切换统计库数据源
			CustomerContextHolder
					.setContextType(CustomerContextHolder.SESSION_FACTORY_COUNT);

			// -------------------------汇总六格------------------------------
			Map<String, Object> sumSixMap = null;
			if (campIds.length() != 0) {
				try {
					sumSixMap = campdCountDao
							.getReportSumSix(params);
				} catch (Exception e) {
					sumSixMap = getEmptySumSixMap();
				}
			}
			else {
				sumSixMap = getEmptySumSixMap();
			}

			model.addAttribute("sumSixMap", sumSixMap);

			// ------------------------图形报表-------------------------------
				
			List<Map<String, Object>> campChartDataList = null ;
			
			//不加对campIds的判断，sql会报错
			if (campIds.length() != 0) {
				campChartDataList = campdCountDao
						.listByMapForCampChart(params,true);
				
				List<Map<String, Object>> campReportExcelDataList = new ArrayList<Map<String,Object>>(campChartDataList);
				sumSixMap.put("camp_id", "总计");
				campReportExcelDataList.add(sumSixMap);
				//放入session中提供报表下载使用
				HttpSession session = ((ServletRequestAttributes) RequestContextHolder
						.getRequestAttributes()).getRequest().getSession();
				session.setAttribute("campReportExcelData", campReportExcelDataList);
			}
			
//			//分页下面的一行总计
//			List<Map<String, Object>> campAllSumData = campdCountDao.listByMapForCampChart(params,false);
//			if(campAllSumData != null && campAllSumData.size() == 1){
//				model.addAttribute("campAllSumData", campAllSumData.get(0));
//			}

			// 在报表x轴显示的活动名称列表
			ArrayList<String> xAsixCampNameList = new ArrayList<String>();
			ArrayList<Integer> pvList = new ArrayList<Integer>();
			ArrayList<Integer> clickList = new ArrayList<Integer>();
			ArrayList<Float> clickRateList = new ArrayList<Float>();
			ArrayList<Float> cpmList = new ArrayList<Float>();
			ArrayList<Float> cpcList = new ArrayList<Float>();
			ArrayList<Float> expendList = new ArrayList<Float>();

			if (campChartDataList != null) {
				
				for (Map<String, Object> map : campChartDataList) {
					
					//由于统计库中无法与业务库关联，所以只能通过与campList得到活动名称
					joinCampData(map , campList);
					
					xAsixCampNameList.add(map.get("camp_name").toString());

					pvList.add(((BigDecimal) map.get("pv_sum")).intValue());
					clickList.add(((BigDecimal) map.get("click_sum"))
							.intValue());
					clickRateList.add(((BigDecimal) map.get("click_rate_sum"))
							.floatValue());
					cpmList.add(((BigDecimal) map.get("cpm_sum_yuan"))
							.floatValue());
					cpcList.add(((BigDecimal) map.get("cpc_sum_yuan"))
							.floatValue());

					// float expend = (int) map.get("expend") / 100f;
					expendList.add(((BigDecimal) map.get("expend_sum_yuan"))
							.floatValue());
					
				}
				
			}

			model.addAttribute("xAxisList",
					JSONObject.toJSONString(xAsixCampNameList));
			model.addAttribute("pvList", JSONObject.toJSONString(pvList));
			model.addAttribute("clickList", JSONObject.toJSONString(clickList));
			model.addAttribute("clickRateList",
					JSONObject.toJSONString(clickRateList));
			model.addAttribute("cpmList", JSONObject.toJSONString(cpmList));
			model.addAttribute("cpcList", JSONObject.toJSONString(cpcList));
			model.addAttribute("expendList",
					JSONObject.toJSONString(expendList));
			

			// ------------------------图形报表 end -------------------------------

			// --------------------- 分页列表 --------------------------------
			pageNo = null == pageNo ? 1 : pageNo;
			pageSize = null == pageSize ? 10 : pageSize;

			if (campIds.length() != 0) {
				Pagination<Map<String, Object>> page = new Pagination<Map<String, Object>>();
				page.setPageNo(pageNo);
				page.setPageSize(pageSize);
				page.setCondition(params);
				
				List<Map<String, Object>> pageList = campdCountDao
						.getMapPageForCampReport(page);

				if (pageList != null) {

					// 由于统计库中无法与业务库关联，所以只能通过与campList得到活动名称
					for (Map<String, Object> map_count : pageList) {

						joinCampData(map_count , campList);
					}

				}

				page.setList(pageList);

				model.addAttribute("page", page);
			}
			
			// --------------------- 分页列表 end --------------------------------

		} else {
			model.addAttribute("sumSixMap", getEmptySumSixMap());
		}

	}
	
	/**
	 * 由于统计库中无法与业务库关联，所以只能通过与campList得到活动名称
	 * 
	 * @param countDataMap
	 * @param campList
	 */
	private void joinCampData(Map<String, Object> countDataMap , List<Map<String, Object>> campList){
		
		// 统计库活动id
		long countCampId = (long) countDataMap.get("camp_id");

		// 用id去campList之中遍历
		for (Iterator<Map<String, Object>> iterator2 = campList
				.iterator(); iterator2.hasNext();) {
			Map<String, Object> camp = (Map<String, Object>) iterator2
					.next();

			// 如果与campList的id对应
			if (countCampId == (long) camp.get("id")) {

				// 得到活动名称
				countDataMap.put("camp_name", camp.get("campaignName")
						.toString());

				if (camp.get("campaignStatus") != null) {
					// 得到活动状态的名称
					Integer campaignStatus = (Integer) camp
							.get("campaignStatus");
					
					countDataMap.put("campaign_status", CampaignStatus
							.getCampaignStatus(campaignStatus)
							.getDesc());
				}
			}
		}
		
	}

	@Override
	public void creative(Model model, long partnerId,
			Map<String, Object> params, Integer pageNo, Integer pageSize) {
		
		//设置默认自定义排序
		if(params.get("orderBy") == null){
			params.put("orderBy", "creative_id");
			params.put("desc", "desc");
		}

		// 切换业务库数据源
		CustomerContextHolder
				.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

		params.put("partnerId", partnerId);

		// 由于统计库无法做名称模糊查询，所以先从业务库里查出所有的活动
		List<Map<String, Object>> adList = adDao.listByMap(params);
		if (adList != null) {
			// 得到所有的活动id
			
			ArrayList<String> campIdList = new ArrayList<String>();
			for (Iterator<Map<String, Object>> iterator = adList.iterator(); iterator
					.hasNext();) {
				Map<String, Object> map = (Map<String, Object>) iterator.next();
				
				if(map.get("campaign_id") != null){
					
					String id = map.get("campaign_id").toString();
					
					boolean isExist = false ;
					for (String temp : campIdList) {
						if(temp.equals(id)){
							isExist = true ;
							break ;
						}
					}
					if(!isExist){
						campIdList.add(id);
					}
				}
			}

			if (campIdList.size() > 0) {
				StringBuffer campIds = new StringBuffer();
				for (String string : campIdList) {
					campIds.append(string).append(",");
				}
				// 用id范围去查询统计库
				params.put("campIds", campIds.substring(0,campIds.lastIndexOf(",")));
			}

			// 切换统计库数据源
			CustomerContextHolder
					.setContextType(CustomerContextHolder.SESSION_FACTORY_COUNT);

			// -------------------------汇总六格------------------------------

			Map<String, Object> sumSixMap = null;
			if (campIdList.size() > 0) {
				try {
					sumSixMap = campdCountDao
							.getReportSumSix(params);
				} catch (Exception e) {
					sumSixMap = getEmptySumSixMap();
				}
			}
			else {
				sumSixMap = getEmptySumSixMap();
			}

			model.addAttribute("sumSixMap", sumSixMap);
			
			// -------------------------汇总六格 end------------------------------
			
			// ------------------------图形报表-------------------------------
			
			List<Map<String, Object>> creativeChartDataList = null ;
			//不加对campIds的判断，sql会报错
			if (campIdList.size() > 0) {
				creativeChartDataList = campCredCountDao
						.listByMapForCreativeChart(params,true);
				
				List<Map<String, Object>> creativeReportExcelDataList = new ArrayList<Map<String,Object>>(creativeChartDataList);
				
				sumSixMap.put("creative_id", "总计");
				creativeReportExcelDataList.add(sumSixMap);
				
				//放入session中提供报表下载使用
				HttpSession session = ((ServletRequestAttributes) RequestContextHolder
						.getRequestAttributes()).getRequest().getSession();
				session.setAttribute("creativeReportExcelData", creativeReportExcelDataList);
			}
			
			// 在报表x轴显示的活动名称列表
			ArrayList<String> xAsixCreativeNameList = new ArrayList<String>();
			ArrayList<Integer> pvList = new ArrayList<Integer>();
			ArrayList<Integer> clickList = new ArrayList<Integer>();
			ArrayList<Float> clickRateList = new ArrayList<Float>();
			ArrayList<Float> cpmList = new ArrayList<Float>();
			ArrayList<Float> cpcList = new ArrayList<Float>();
			ArrayList<Float> expendList = new ArrayList<Float>();

			
//				//分页下面的一行总计
//				List<Map<String, Object>> creativeAllSumData = campCredCountDao.listByMapForCreativeChart(params,false);
//				if(creativeAllSumData != null && creativeAllSumData.size() == 1){
//					model.addAttribute("creativeAllSumData", creativeAllSumData.get(0));
//				}
			
			if (creativeChartDataList != null) {

				for (Map<String, Object> map : creativeChartDataList) {
					
					//由于统计库中无法与业务库关联，所以只能通过与adList遍历匹配得到创意所属活动的名称，活动状态，尺寸等信息
					joinCreativeData(map , adList);
					
					xAsixCreativeNameList.add("ID:" + map.get("creative_id"));
					
					pvList.add(((BigDecimal) map.get("pv_sum")).intValue());
					clickList.add(((BigDecimal) map.get("click_sum"))
							.intValue());
					clickRateList.add(((BigDecimal) map.get("click_rate_sum"))
							.floatValue());
					cpmList.add(((BigDecimal) map.get("cpm_sum_yuan"))
							.floatValue());
					cpcList.add(((BigDecimal) map.get("cpc_sum_yuan"))
							.floatValue());

					// float expend = (int) map.get("expend") / 100f;
					expendList.add(((BigDecimal) map.get("expend_sum_yuan"))
							.floatValue());
					
					
				}
				
			}
			

			model.addAttribute("xAxisList",
					JSONObject.toJSONString(xAsixCreativeNameList));
			model.addAttribute("pvList", JSONObject.toJSONString(pvList));
			model.addAttribute("clickList", JSONObject.toJSONString(clickList));
			model.addAttribute("clickRateList",
					JSONObject.toJSONString(clickRateList));
			model.addAttribute("cpmList", JSONObject.toJSONString(cpmList));
			model.addAttribute("cpcList", JSONObject.toJSONString(cpcList));
			model.addAttribute("expendList",
					JSONObject.toJSONString(expendList));

			// ------------------------图形报表 end -------------------------------

			// --------------------- 分页列表 --------------------------------
			pageNo = null == pageNo ? 1 : pageNo;
			pageSize = null == pageSize ? 10 : pageSize;

			if (campIdList.size() > 0) {
				Pagination<Map<String, Object>> page = new Pagination<Map<String, Object>>();
				page.setPageNo(pageNo);
				page.setPageSize(pageSize);
				page.setCondition(params);
				
				List<Map<String, Object>> pageList = campCredCountDao
						.getMapPageForCreativeReport(page);
				if (pageList != null) {

					for (Map<String, Object> map_count : pageList) {

						//由于统计库中无法与业务库关联，所以只能通过与adList遍历匹配得到创意所属活动的名称，活动状态，尺寸等信息
						joinCreativeData(map_count , adList);
					}

				}

				page.setList(pageList);
				
				model.addAttribute("page", page);
			}
			
			// --------------------- 分页列表 end --------------------------------

			
		} else {
			model.addAttribute("sumSixMap", getEmptySumSixMap());
		}

	}

	/**
	 * 由于统计库中无法与业务库关联，所以只能通过与adList遍历匹配得到创意所属活动的名称，活动状态，尺寸等信息
	 * @param countDataMap
	 * @param adList
	 */
	private void joinCreativeData(Map<String, Object> countDataMap , List<Map<String, Object>> adList){
		
		// 统计库创意id，就是creative表的id
		long countCreativeId = (long) countDataMap.get("creative_id");

		// 用id去adList遍历
		for (Map<String, Object> ad : adList) {

			// 如果等于adlist的id
			if (countCreativeId == (long) ad.get("creative_id")) {

				// 得到活动名称
				countDataMap.put("campaign_name", ad.get("campaign_name"));
				// 得到活动状态
				int campaignStatus = (int) ad.get("campaign_status");
				countDataMap.put("campaign_status", CampaignStatus
						.getCampaignStatus(campaignStatus)
						.getDesc());
				// 得到尺寸
				countDataMap.put("size", ad.get("size"));
				// 得到创意url
				countDataMap.put("creative_url", ad.get("creative_url"));
			}
		}
		
	}
	
	@Override
	public void city(Model model, long partnerId,
			Map<String, Object> params, Integer pageNo, Integer pageSize) {
		
		//设置默认自定义排序
		if(params.get("orderBy") == null){
			params.put("orderBy", "city_id");
			params.put("desc", "asc");  //城市从北京开始排起
		}
		params.put("partnerId", partnerId);

		// 切换业务库数据源
		CustomerContextHolder
				.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);


		// 由于统计库无法做名称模糊查询，所以先从业务库里查出所有的活动
		List<Map<String, Object>> campList = campDao.listReportCampBy(params);
		if (campList != null) {
			// 得到所有的活动id
			StringBuffer campIds = new StringBuffer("");
			for (Iterator<Map<String, Object>> iterator = campList.iterator(); iterator
					.hasNext();) {
				Map<String, Object> map = (Map<String, Object>) iterator.next();
				if (campIds.length() == 0) {
					campIds.append(map.get("id"));
				} else {
					campIds.append(",").append(map.get("id"));
				}
			}

			// 用id范围去查询统计库
			params.put("campIds", campIds.toString());

			// 切换统计库数据源
			CustomerContextHolder
					.setContextType(CustomerContextHolder.SESSION_FACTORY_COUNT);

			// -------------------------汇总六格------------------------------
			Map<String, Object> campdSumSixMap = null;
			if (campIds.length() != 0) {
				try {
					campdSumSixMap = campdCountDao
							.getReportSumSix(params);
				} catch (Exception e) {
					campdSumSixMap = getEmptySumSixMap();
				}
			}
			else {
				campdSumSixMap = getEmptySumSixMap();
			}
			
			model.addAttribute("sumSixMap", campdSumSixMap);
			
			// -------------------------汇总六格 end------------------------------

			// ------------------------图形报表-------------------------------
			
			// 在报表x轴显示的活动名称列表
			ArrayList<String> axisLabelList = new ArrayList<String>();
			ArrayList<Integer> pvList = new ArrayList<Integer>();
			ArrayList<Integer> clickList = new ArrayList<Integer>();
			ArrayList<Float> clickRateList = new ArrayList<Float>();
			ArrayList<Float> cpmList = new ArrayList<Float>();
			ArrayList<Float> cpcList = new ArrayList<Float>();
			ArrayList<Float> expendList = new ArrayList<Float>();
				
			//要处理的原始list
			List<Map<String, Object>> areaChartDataList = null ;
			String provinceName = (String)params.get("provinceName") ;
			
			if (campIds.length() != 0) {
				//如果没有指定省份则按省份分组查询
				if(StringUtils.isEmpty(provinceName)){
					areaChartDataList = campCitydCountDao
							.listByMapForProvinceChart(params,true);
				}
				else{
					areaChartDataList = campCitydCountDao
							.listByMapForCityChart(params,true);
				}
				
			}
			
			boolean isOrderAsc = params.get("desc") != null && params.get("desc").toString().equals("asc");
			
			Map<String,Object> areaAllSumDataMap = null ;
			//分页下面的一行总计
			if (campIds.length() != 0) {
				List<Map<String, Object>> areaAllSumData = null ;
				
				//如果没有指定省份
				if(StringUtils.isEmpty(provinceName)){
					
					areaAllSumData = campCitydCountDao
							.listByMapForProvinceChart(params,false);
				}
				else{
					
					areaAllSumData = campCitydCountDao
							.listByMapForCityChart(params,false);
				}
				
				if(areaAllSumData != null && areaAllSumData.size() == 1){
					
					areaAllSumData = distributeUnknownAreaData(areaAllSumData, isOrderAsc , provinceName);
				
					areaAllSumDataMap = areaAllSumData.get(0) ;
					
					model.addAttribute("areaAllSumData",areaAllSumDataMap);
				}
			}
			
			if (areaChartDataList != null) {
				
				//因为页面上要求不显示未知区域，已知区域要平摊未知区域的数值
				areaChartDataList = distributeUnknownAreaData(areaChartDataList, isOrderAsc , provinceName);
				
				for (Map<String, Object> map : areaChartDataList) {
					
					String cityName = (String) map.get("city_name");
					int city_pv_sum = (int) map.get("pv_sum");
					int city_click_sum = (int) map.get("click_sum");
					float city_click_rate_sum = (float) map.get("click_rate_sum");
					float city_cpm_sum_yuan = (float) map.get("cpm_sum_yuan");
					float city_cpc_sum_yuan = (float) map.get("cpc_sum_yuan");
					float city_expend_sum_yuan = (float) map.get("expend_sum_yuan");
					
//					int city_pv_sum = ((BigDecimal) map.get("pv_sum")).intValue();
//					int city_click_sum = ((BigDecimal) map.get("click_sum")).intValue();
//					float city_click_rate_sum = ((BigDecimal) map.get("click_rate_sum")).floatValue();
//					float city_cpm_sum_yuan = ((BigDecimal) map.get("cpm_sum_yuan")).floatValue();
//					float city_cpc_sum_yuan = ((BigDecimal) map.get("cpc_sum_yuan")).floatValue();
//					float city_expend_sum_yuan = ((BigDecimal) map.get("expend_sum_yuan")).floatValue();
					
					axisLabelList.add(cityName);
					pvList.add(city_pv_sum);
					clickList.add(city_click_sum);
					clickRateList.add(city_click_rate_sum);
					cpmList.add(city_cpm_sum_yuan);
					cpcList.add(city_cpc_sum_yuan);
					expendList.add(city_expend_sum_yuan);
					
				}
				
			}

			model.addAttribute("axisLabelList",
					JSONObject.toJSONString(axisLabelList));
			model.addAttribute("axisLabelListSize",axisLabelList.size());
			model.addAttribute("pvList", JSONObject.toJSONString(pvList));
			model.addAttribute("clickList", JSONObject.toJSONString(clickList));
			model.addAttribute("clickRateList",
					JSONObject.toJSONString(clickRateList));
			model.addAttribute("cpmList", JSONObject.toJSONString(cpmList));
			model.addAttribute("cpcList", JSONObject.toJSONString(cpcList));
			model.addAttribute("expendList",
					JSONObject.toJSONString(expendList));

			// ------------------------图形报表 end -------------------------------
			
			// --------------------- 分页列表 --------------------------------
			pageNo = null == pageNo ? 1 : pageNo;
			pageSize = null == pageSize ? 10 : pageSize;

			if (campIds.length() != 0) {
				Pagination<Map<String, Object>> page = new Pagination<Map<String, Object>>();
				page.setPageNo(pageNo);
				page.setPageSize(pageSize);
				page.setCondition(params);
				
				List<Map<String, Object>> pageList = null ;
				
				if(provinceName == null ){
					pageList = campCitydCountDao
							.getMapPageForProvinceReport(page);
				}
				else{
					pageList = campCitydCountDao
							.getMapPageForCityReport(page);
				}
				
				//把均摊未知区域后重新计算得到的数据，覆盖到分页的数据里（因为在分页sql里无法分摊，所以只能先用分页sql查出原来的计算结果，再去areaChartDataList里按cityId逐个匹配）
				if(pageList != null){
					
					for (Map<String, Object> pageMap : pageList) {
						
						String pageCityId = pageMap.get("city_id") != null ? pageMap.get("city_id").toString() : "";
						
						for (Map<String,Object> dataMap : areaChartDataList) {
							String cityId = dataMap.get("city_id") != null ? dataMap.get("city_id").toString() : "";
							
							if(pageCityId.equals(cityId)){
								
								pageMap.put("pv_sum", dataMap.get("pv_sum"));
								pageMap.put("uv_sum", dataMap.get("uv_sum"));
								pageMap.put("click_sum", dataMap.get("click_sum"));
								pageMap.put("click_rate_sum", dataMap.get("click_rate_sum"));
								pageMap.put("cpm_sum_yuan", dataMap.get("cpm_sum_yuan"));
								pageMap.put("cpc_sum_yuan", dataMap.get("cpc_sum_yuan"));
								pageMap.put("expend_sum_yuan", dataMap.get("expend_sum_yuan"));
								
							}
						}
					}
					
				}
				
				page.setList(pageList);
				
				model.addAttribute("page", page);
			}
			
			// --------------------- 分页列表 end --------------------------------

			
		} else {
			model.addAttribute("sumSixMap", getEmptySumSixMap());
		}

	}
	
	public void downloadCityReport(Model model, long partnerId,
			Map<String, Object> params, HttpServletResponse response) {
		
		//设置默认自定义排序
		if(params.get("orderBy") == null){
			params.put("orderBy", "city_id");
			params.put("desc", "asc");  //城市从北京开始排起
		}
		params.put("partnerId", partnerId);

		// 切换业务库数据源
		CustomerContextHolder
				.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);


		// 由于统计库无法做名称模糊查询，所以先从业务库里查出所有的活动
		List<Map<String, Object>> campList = campDao.listReportCampBy(params);
		
		if (campList != null) {
			
			// 得到所有的活动id
			StringBuffer campIds = new StringBuffer("");
			for (Iterator<Map<String, Object>> iterator = campList.iterator(); iterator
					.hasNext();) {
				Map<String, Object> map = (Map<String, Object>) iterator.next();
				if (campIds.length() == 0) {
					campIds.append(map.get("id"));
				} else {
					campIds.append(",").append(map.get("id"));
				}
			}

			// 用id范围去查询统计库
			params.put("campIds", campIds.toString());

			// 切换统计库数据源
			CustomerContextHolder
					.setContextType(CustomerContextHolder.SESSION_FACTORY_COUNT);

			String provinceName = (String)params.get("provinceName") ;
			boolean isOrderAsc = params.get("desc") != null && params.get("desc").toString().equals("asc");

			//要处理的原始list
			List<Map<String, Object>> areaChartDataList = null ;
			
			if (campIds.length() != 0) {
				
				areaChartDataList = campCitydCountDao
						.listByMapForCityChart(params,true); 
				
				
				//因为页面上要求不显示未知区域，已知区域要平摊未知区域的数值
				areaChartDataList = distributeUnknownAreaData(areaChartDataList, isOrderAsc , provinceName);
			}
			
			Map<String,Object> lastRowMap = null ;
			//最下面的一行总计
			if (campIds.length() != 0) {
				
				List<Map<String, Object>> areaAllSumData = campCitydCountDao
						.listByMapForCityChart(params,false) ;
				
				if(areaAllSumData != null && areaAllSumData.size() == 1){
					
					lastRowMap = areaAllSumData.get(0);
					
					lastRowMap.put("city_name", "总计");
					
					areaChartDataList.add(lastRowMap);
					
				}
			}
			
			//输出excel
			ExcelUtil.downloadExcel("cityReportExcelData", areaChartDataList, response);
			
		} 
		
		

	}
	
	/**
	 * 如果有未知地域，将未知地域的数值均摊到各个已知地域中：先算出不能整除的余数部分，然后让每一个地域接收能整除的部分，最后选一个地域接收余数部分
	 * 
	 * @param gapMap 地域统计表和活动统计表本身存在的数据差异，为了上下报表一致，这也需要整合到未知地域数值里，一起平摊给其他地域
	 * @param areaDataList
	 * @param isOrderAsc 当前排序是否为升序，这决定用哪一个地域来接受不能整除的部分
	 * @param provinceName 如果当前是按省份查询，则是省份名称，如果不是按省份查询则为空
	 * @return
	 */
	private List<Map<String, Object>> distributeUnknownAreaData(List<Map<String, Object>> areaDataList , boolean isOrderAsc , String provinceName){
		
		HttpSession session = null ;
		if(httpRequest != null){
			session = httpRequest.getSession();
		}
		
		//如果省份名称不为空，则表示未知区域在按省份显示时已经分配完毕并放到session中了
		boolean isProvinceList = StringUtils.isEmpty(provinceName) ;
		
		if(areaDataList != null){
			
			Map<String, Object> unknownAreaData = null ;
			ArrayList<Map<String, Object>> knownAreaDataList = new ArrayList<Map<String,Object>>() ;
			
			//如果是按省份显示，则区分出未知地域和其他地域
			if(isProvinceList){
				
				for (Map<String, Object> map : areaDataList) {
					String cityId = map.get("city_id") != null ? map.get("city_id").toString() : "0" ;
					if(cityId.equals("0")){
						unknownAreaData = map ;
					}
					else{
						knownAreaDataList.add(map);
					}
				}
			}
			//如果是按城市显示，则表示之前为省份分配的未知区域数据已经分配完毕并放到session中了，现在要取出来继续分摊给城市，否则城市的数据和省份的数据会对不上（会比省份的少）
			else{
				if(session != null){
					HashMap<String,HashMap<String,Object>> distributeMap = (HashMap<String,HashMap<String,Object>>)session.getAttribute(Constants.FRONT_REPORT_UNKNOWN_AREA_DISTRIBUTE_MAP);
					if(distributeMap != null){
						unknownAreaData = distributeMap.get(provinceName);
					}
				}
				//如果是按城市显示，已知区域则是传进来的城市列表，要把unknownAreaData再均摊到这些城市中
				knownAreaDataList = (ArrayList<Map<String, Object>>) areaDataList ;
			}
			
			if(unknownAreaData != null && knownAreaDataList.size() > 0){
				
				/*
				 * 因为整数类数值有可能除不尽，所以将未知地域的各项整数类数据都分为两部分：平均数和余数
				 */
				//pv
				int pv = isProvinceList ? ((BigDecimal) unknownAreaData.get("pv_sum")).intValue() : (int)unknownAreaData.get("pv_sum");
				//pv余数
				int pv_remainder = pv % knownAreaDataList.size();
				//pv平均数
				int pv_average = (pv - pv_remainder) / knownAreaDataList.size();
				
				//点击数
				int click = isProvinceList ? ((BigDecimal) unknownAreaData.get("click_sum")).intValue() : (int)unknownAreaData.get("click_sum") ;
				//点击余数
				int click_remainder = click % knownAreaDataList.size();
				//点击平均数
				int click_average = (click - click_remainder) / knownAreaDataList.size();
				
				//uv
				int uv = isProvinceList ? ((BigDecimal) unknownAreaData.get("uv_sum")).intValue() : (int)unknownAreaData.get("uv_sum");
				//uv余数
				int uv_remainder = uv % knownAreaDataList.size();
				//uv平均数
				int uv_average = (uv - uv_remainder) / knownAreaDataList.size();
				
				//花费（非整数类，不算余数）
				Float expend = isProvinceList ? ((BigDecimal) unknownAreaData.get("expend_sum_yuan")).floatValue() : (float)unknownAreaData.get("expend_sum_yuan");
				//花费平均数
				float expend_average = expend / knownAreaDataList.size();
				
				//记录每个地域均摊的未知地域的数值记录到session中
				HashMap<String,HashMap<String,Object>> distributeDataMap = new HashMap<String, HashMap<String,Object>>();
				//如果当前是按省份显示，把给所有省份分配的数值放到session中
				if(isProvinceList && session != null){
					session.setAttribute(Constants.FRONT_REPORT_UNKNOWN_AREA_DISTRIBUTE_MAP , distributeDataMap);
				}
				
				//先摊平均数
				for (Map<String, Object> knownAreaDataMap : knownAreaDataList) {
					
					//调试用代码
//					String cityName = (String)knownAreaDataMap.get("city_name");
//					if(cityName.equals("西藏自治区")){
//						System.out.println("aaaa");
//					}
					
					addDataToArea(pv_average,uv_average,click_average ,expend_average, knownAreaDataMap ,false);
					
					//如果当前是按省份显示，把给当前这个省份分配的数值记录下来，稍后放在session中
					if(isProvinceList && knownAreaDataMap.get("city_name") != null){
						HashMap<String,Object> distributeData = new HashMap<String, Object>();
						distributeData.put("pv_sum", pv_average);
						distributeData.put("uv_sum", uv_average);
						distributeData.put("click_sum", click_average);
						distributeData.put("expend_sum_yuan", expend_average);
						distributeDataMap.put((String)knownAreaDataMap.get("city_name") , distributeData);
					}
					
					//调试用代码
					//addDataToArea(0,0,0,0, knownAreaDataMap ,false);
					
				}
				
				//再摊余数，如果是升序让最后一个地域加上余数，如果降序则让第一个地域加，总之是把余数摊到数值最大的地域里，这样就不会打破排序的结果
				Map<String, Object> acceptRemainderArea = null ; 
				if(isOrderAsc){
					acceptRemainderArea = knownAreaDataList.get(knownAreaDataList.size() - 1);
				}
				else{
					acceptRemainderArea = knownAreaDataList.get(0);
				}
				//让得到的地域接收余数 (由于之前所有地域已经接收过未知地域expend的平均值，这时不需要再次接收，所以expend_average参数为0)
				addDataToArea(pv_remainder, uv_remainder, click_remainder, 0 , acceptRemainderArea ,true);
				
				//如果是按省份显示，把给当前这个省份分配的余数值累加到原来给它分配好的distributeData中
				if(isProvinceList && acceptRemainderArea.get("city_name") != null){
					HashMap<String,Object> distributeData = new HashMap<String, Object>();
					distributeData.put("pv_sum", pv_average + pv_remainder);
					distributeData.put("click_sum", click_average + click_remainder);
					distributeData.put("uv_sum", uv_average + uv_remainder);
					distributeData.put("expend_sum_yuan", expend_average);
					distributeDataMap.put((String)acceptRemainderArea.get("city_name") , distributeData);
				}
				
				return knownAreaDataList ;
			}
			else{
				//注意：即使有时候投放中没有未知地域，但也要调用一下addDataToArea方法去转换一下数据类型，将BigDecimal转成基础类型int或float，这样在方法之外就可以统一取值时的数据类型
				for (Map<String, Object> areaDataMap : areaDataList) {
					
					addDataToArea(0,0,0,0,areaDataMap,false);
					
				}
			}
		}
		return areaDataList ;
	}
	
	private void addDataToArea(int pv_average , int uv_average , int click_average , float expend_average , Map<String, Object> areaData,boolean isAcceptRemainder){
		
		int pv_sum = isAcceptRemainder ? (int)areaData.get("pv_sum") : ((BigDecimal) areaData.get("pv_sum")).intValue();
		int uv_sum = isAcceptRemainder ? (int)areaData.get("uv_sum") : ((BigDecimal) areaData.get("uv_sum")).intValue();
		int click_sum = isAcceptRemainder ? (int)areaData.get("click_sum") : ((BigDecimal) areaData.get("click_sum")).intValue();
		float expend_sum_yuan = isAcceptRemainder ? (float)areaData.get("expend_sum_yuan") : ((BigDecimal) areaData.get("expend_sum_yuan")).floatValue();
		
		pv_sum += pv_average ;
		uv_sum += uv_average ;
		click_sum += click_average ;
		expend_sum_yuan += expend_average ;
		
		float click_rate_sum = pv_sum > 0 ? (click_sum / pv_sum) * 100F : 0F;
		float cpm_sum_yuan = pv_sum > 0 ? expend_sum_yuan / (pv_sum / 1000F) : 0F;
		float cpc_sum_yuan = click_sum > 0 ? expend_sum_yuan / click_sum : 0F ;
		
		areaData.put("pv_sum", pv_sum);
		areaData.put("uv_sum", uv_sum);
		areaData.put("click_sum", click_sum);
		areaData.put("click_rate_sum", click_rate_sum);
		areaData.put("cpm_sum_yuan",cpm_sum_yuan);
		areaData.put("cpc_sum_yuan",cpc_sum_yuan);
		areaData.put("expend_sum_yuan", expend_sum_yuan);
	}
	
	@Override
	public void hour(Model model, long partnerId, Map<String, Object> params) {
		
		//设置默认自定义排序
		if(params.get("orderBy") == null){
			params.put("orderBy", "hour");
			params.put("desc", "asc");  //小时从0点开始排起
		}

		// 切换业务库数据源
		CustomerContextHolder
				.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

		params.put("partnerId", partnerId);

		// 由于统计库无法按活动状态、活动名称查询，所以先从业务库里查出所有的活动
		List<Map<String, Object>> campList = campDao.listReportCampBy(params);
		if (campList != null) {
			// 得到所有的活动id
			StringBuffer campIds = new StringBuffer("");
			for (Iterator<Map<String, Object>> iterator = campList.iterator(); iterator
					.hasNext();) {
				Map<String, Object> map = (Map<String, Object>) iterator.next();
				if (campIds.length() == 0) {
					campIds.append(map.get("id"));
				} else {
					campIds.append(",").append(map.get("id"));
				}
			}

			// 用id范围去查询统计库
			params.put("campIds", campIds.toString());

			// 切换统计库数据源
			CustomerContextHolder
					.setContextType(CustomerContextHolder.SESSION_FACTORY_COUNT);

			// -------------------------汇总六格------------------------------
			Map<String, Object> sumSixMap = null;
			if (campIds.length() != 0) {
				try {
					sumSixMap = campdCountDao
							.getReportSumSix(params);
				} catch (Exception e) {
					sumSixMap = getEmptySumSixMap();
				}
			}
			else {
				sumSixMap = getEmptySumSixMap();
			}

			model.addAttribute("sumSixMap", sumSixMap);
			
			// -------------------------汇总六格 end------------------------------

			// ------------------------图形报表-------------------------------
			
			// 在报表x轴显示的活动名称列表
			ArrayList<String> axisLabelList = new ArrayList<String>();
			ArrayList<Integer> pvList = new ArrayList<Integer>();
			ArrayList<Integer> clickList = new ArrayList<Integer>();
			ArrayList<Float> clickRateList = new ArrayList<Float>();
			ArrayList<Float> cpmList = new ArrayList<Float>();
			ArrayList<Float> cpcList = new ArrayList<Float>();
			ArrayList<Float> expendList = new ArrayList<Float>();
			
			String svmOrAvg = (String)params.get("sumOrAvg");
			if(svmOrAvg == null){
				svmOrAvg = "sum" ; 
			}
			//不加对campIds的判断，sql会报错
			if (campIds.length() != 0) {
				
				//准备给图形报表用的查询条件，为了保持小时数0-23的排序，所以不能有其他排序关键字
				HashMap<String, Object> chartWithoutOrderParams = new HashMap<String, Object>();
				chartWithoutOrderParams.putAll(params);
				chartWithoutOrderParams.remove("orderBy");
				chartWithoutOrderParams.remove("desc");
				
				List<Map<String, Object>> hourChartDataList = null ;
				
				if(svmOrAvg.equals("sum")){
					hourChartDataList = camphCountDao
							.listMapForHourSumChart(chartWithoutOrderParams,true);
				}
				else{
					hourChartDataList = camphCountDao
							.listMapForHourAvgChart(chartWithoutOrderParams,true);
				}
				
				//dataList中不一定每个小时都会有值，如果没有值就显示0
				for (int i = 0; i <= 23; i++) {
					
					Integer pv = 0;
					Integer uv = 0 ;
					Integer click = 0;
					Float click_rate = 0f;
					Float cpm_yuan = 0f;
					Float cpc_yuan = 0f;
					Float expend_yuan = 0f;
					
					if (hourChartDataList != null) {
						
						for (Map<String, Object> map : hourChartDataList) {
							
							int hour = (int)map.get("hour");
							
							if(hour == i){
								
								pv = ((BigDecimal) map.get("pv_sum")).intValue();
								uv = ((BigDecimal) map.get("uv_sum")).intValue();
								click = ((BigDecimal) map.get("click_sum")).intValue();
								click_rate = ((BigDecimal) map.get("click_rate_sum"))
										.floatValue();
								cpm_yuan = ((BigDecimal) map.get("cpm_sum_yuan")).floatValue();
								cpc_yuan = ((BigDecimal) map.get("cpc_sum_yuan")).floatValue();
								expend_yuan = ((BigDecimal) map.get("expend_sum_yuan")).floatValue();
							}
							
						}
						
					}
					
					axisLabelList.add((i % 2 == 0 ? "\n" : "") + getHourLabel(i));
					pvList.add(pv);
					clickList.add(click);
					clickRateList.add(click_rate);
					cpmList.add(cpm_yuan);
					cpcList.add(cpc_yuan);
					expendList.add(expend_yuan);
					
				}
				
			}

			model.addAttribute("axisLabelList",
					JSONObject.toJSONString(axisLabelList));
			model.addAttribute("pvList", JSONObject.toJSONString(pvList));
			model.addAttribute("clickList", JSONObject.toJSONString(clickList));
			model.addAttribute("clickRateList",
					JSONObject.toJSONString(clickRateList));
			model.addAttribute("cpmList", JSONObject.toJSONString(cpmList));
			model.addAttribute("cpcList", JSONObject.toJSONString(cpcList));
			model.addAttribute("expendList",
					JSONObject.toJSONString(expendList));

			// ------------------------图形报表 end -------------------------------
			

			// --------------------- 分页列表 --------------------------------

			if (campIds.length() != 0) {
				
				List<Map<String, Object>> pageList = null ;
				if(svmOrAvg.equals("sum")){
					pageList = camphCountDao
							.getMapPageForHourSumReport(params);
				}else{
					pageList = camphCountDao
							.getMapPageForHourAvgReport(params);
				}
				
				if(pageList != null){
					
					for (Map<String, Object> map : pageList) {
						int hour = (int)map.get("hour");
						map.put("hour", getHourLabel(hour));
					}
					
				}
				
				model.addAttribute("pageList",pageList);
			}
			
			// --------------------- 分页列表 end --------------------------------
			
			//分页下面的一行总计
			List<Map<String, Object>> lastSumRowData = null ;
			Map<String, Object> lastSumRowMap = null ;
			
			//--------------------- 报表下载 -------------------------------
			//不加对campIds的判断，sql会报错
			if (campIds.length() != 0) {
				List<Map<String, Object>> hourExcelDataList = null ;
				
				if(svmOrAvg.equals("sum")){
					hourExcelDataList = camphCountDao
							.listMapForHourSumChart(params,true);
					
					lastSumRowData = camphCountDao
							.listMapForHourSumChart(params,false);
				}
				else{
					hourExcelDataList = camphCountDao
							.listMapForHourAvgChart(params,true);
					
					lastSumRowData = camphCountDao
							.listMapForHourAvgChart(params,false);
				}
				if(hourExcelDataList != null){
					
					for (Map<String, Object> map : hourExcelDataList) {
						int hour = (int)map.get("hour");
						map.put("hour", getHourLabel(hour));
					}
					
				}
				
				if(lastSumRowData != null && lastSumRowData.size() == 1){
					lastSumRowMap = lastSumRowData.get(0);
					//发送到页面
					model.addAttribute("hourAllSumData",lastSumRowMap);
					//添加到excel下载数据中
					lastSumRowMap.put("hour", svmOrAvg.equals("sum") ? "总计" : "总平均");
					hourExcelDataList.add(lastSumRowMap);
				}
				
				
				//放入session中提供报表下载使用
				HttpSession session = ((ServletRequestAttributes) RequestContextHolder
						.getRequestAttributes()).getRequest().getSession();
				session.setAttribute("hourReportExcelData", hourExcelDataList);
			}
			//--------------------- 报表下载  END -----------------------------
			
		} else {
			model.addAttribute("sumSixMap", getEmptySumSixMap());
		}

	}
	
	private String getHourLabel(int hour){
		return hour + ":00" + "-" + (hour+1) + ":00" ; 
	}

	@Override
	public void getExcel(int flag, HttpServletResponse response) {
		
		DecimalFormat dfPercent = new DecimalFormat("0.000");
		DecimalFormat dfCurrency = new DecimalFormat("0.00");

		// 切换业务库数据源
		CustomerContextHolder
				.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

		List<String> firstRowColsList = new ArrayList<String>();

		String excelName = "" ;
		switch (flag) {
		case 0:
			firstRowColsList.add("日期");
			excelName = "时间报表" ;
			break;
		case 1:
			firstRowColsList.add("活动类型");
			excelName = "活动类型报表" ;
			break;
		case 2:
			firstRowColsList.add("活动ID");
			firstRowColsList.add("活动名称");
			firstRowColsList.add("活动状态");
			excelName = "活动报表" ;
			break;
		case 3:
			firstRowColsList.add("ID");
			firstRowColsList.add("属于活动");
			firstRowColsList.add("尺寸");
			excelName = "创意报表" ;
			break;
		case 4:
			firstRowColsList.add("地域");
			excelName = "地域报表" ;
			break;
		case 5:
			firstRowColsList.add("小时");
			excelName = "时段报表" ;
			break;
		default:
			firstRowColsList.add("日期");
			excelName = "时间报表" ;
			break;
		}

		firstRowColsList.add("展现量");
		firstRowColsList.add("UV");
		firstRowColsList.add("点击量");
		firstRowColsList.add("点击率(%)");
		firstRowColsList.add("投放cpm(￥)");
		firstRowColsList.add("投放cpc(￥)");
		firstRowColsList.add("投放花费(￥)");
		
		String[] firstRowCols = new String[firstRowColsList.size()];
		firstRowColsList.toArray(firstRowCols);
		
		HttpSession session = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest().getSession();

		List<Map<String, Object>> list = null ;
		switch (flag) {
		case 0:
			list = (List<Map<String, Object>>) session
					.getAttribute("timeReportExcelData");
			break;
		case 1:
			list = (List<Map<String, Object>>) session
			.getAttribute("campTypeReportExcelData");
			break;
		case 2:
			list = (List<Map<String, Object>>) session
			.getAttribute("campReportExcelData");
			break;
		case 3:
			list = (List<Map<String, Object>>) session
			.getAttribute("creativeReportExcelData");
			break;
		case 4:
			list = (List<Map<String, Object>>) session
			.getAttribute("areaReportExcelData");
			break;
		case 5:
			list = (List<Map<String, Object>>) session
			.getAttribute("hourReportExcelData");
			break;
		default:
			list = (List<Map<String, Object>>) session
			.getAttribute("timeReportExcelData");
			break;
		}

		List<String[]> rowList = new ArrayList<String[]>();
		// 添加第一行
		rowList.add(firstRowCols);

		// date,pv_sum,click_sum,click_rate_sum,cpm_sum_yuan,cpc_sum_yuan,expend_sum_yuan
		if (list != null) {
			for (Map<String, Object> map : list) {

				String[] rowCols = new String[firstRowColsList.size()];
				int i = 0;
				
				switch (flag) {
				case 0:
					rowCols[i++] = map.get("date") + "";
					break;
				case 1:
					rowCols[i++] = map.get("camp_type_name") != null ? map.get("camp_type_name") + "" : "";
					break;
				case 2:
					rowCols[i++] = map.get("camp_id") + "";
					rowCols[i++] = map.get("camp_name") != null ? map.get("camp_name") + "" : "";
					rowCols[i++] = map.get("campaign_status") != null ? map.get("campaign_status") + "" : "";
					break;
				case 3:
					rowCols[i++] = map.get("creative_id") + "";
					rowCols[i++] = map.get("campaign_name") != null ? map.get("campaign_name") + "" : "";
					rowCols[i++] = map.get("size") != null ? map.get("size") + "" : "";
					break;
				case 4:
					rowCols[i++] = map.get("city_name") + "";
					break;
				case 5:
					rowCols[i++] = map.get("hour") + "";
					break;
				default:
					rowCols[i++] = map.get("date") + "";
					break;
				}
				
				rowCols[i++] = map.get("pv_sum") + "";
				rowCols[i++] = map.get("uv_sum") + "";
				rowCols[i++] = map.get("click_sum") + "";
				rowCols[i++] = dfPercent.format(map.get("click_rate_sum")) + "";
				rowCols[i++] = dfCurrency.format(map.get("cpm_sum_yuan")) + "";
				rowCols[i++] = dfCurrency.format(map.get("cpc_sum_yuan")) + "";
				rowCols[i++] = dfCurrency.format(map.get("expend_sum_yuan")) + "";
				
				rowList.add(rowCols);
			}
		}

		Map<String, List<String[]>> sheets = new HashMap<String, List<String[]>>();
		sheets.put(excelName, rowList);

		File excelFile = null;
		try {
			String baseDir = System.getProperty("java.io.tmpdir");
			excelFile = new File(baseDir + "/" + System.currentTimeMillis()
					+ ".xlsx");
			ExcelUtil.create07ExcelFile(excelFile, sheets);
			FileUtil.downloadFile(response, excelName + ".csv", excelFile);
		} catch (IOException e) {
			// log.error(e.getMessage(), e);
		} finally {
			if (excelFile != null && excelFile.exists()) {
				try {
					excelFile.delete();
				} catch (Exception e) {

				}
			}
		}
	}

	public static void main(String[] a) {
		
		System.out.println(ReportUtil.getClickRate(159, 5399));

	}
}
