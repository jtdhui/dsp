package com.jtd.web.service.admin.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.commons.page.Pagination;
import com.jtd.utils.ExcelUtil;
import com.jtd.utils.ReportUtil;
import com.jtd.web.constants.Constants;
import com.jtd.web.constants.RoleType;
import com.jtd.web.dao.IAdDao;
import com.jtd.web.dao.ICampChanneldDao;
import com.jtd.web.dao.ICampaignDao;
import com.jtd.web.dao.IChannelDao;
import com.jtd.web.dao.IPardCountDao;
import com.jtd.web.dao.IPartnerDao;
import com.jtd.web.dao.ISysUserRoleDao;
import com.jtd.web.dao.IUserPartnerDao;
import com.jtd.web.po.Channel;
import com.jtd.web.po.Partner;
import com.jtd.web.po.SysUser;
import com.jtd.web.service.admin.IAdminOperatorService;
import com.jtd.web.service.admin.IAdminPartnerService;
import com.jtd.web.service.adx.IBaiduAPIService;

@Service
public class AdminOperatorService implements IAdminOperatorService {

	private Logger log = LoggerFactory.getLogger(IAdminOperatorService.class);

	@Autowired
	private IUserPartnerDao userPartnerDao;

	@Autowired
	private IPardCountDao pardDao;

	@Autowired
	private IAdDao adDao;

	@Autowired
	private ISysUserRoleDao userRoleDao;

	@Autowired
	private IBaiduAPIService baiduAPIService;

	@Autowired
	private IPartnerDao partnerDao;

	@Autowired
	private ICampaignDao campDao;

	@Autowired
	private ICampChanneldDao campChanneldDao;

	@Autowired(required = false)
	private HttpServletRequest request;

	@Autowired
	private IAdminPartnerService partnerService;

	@Autowired
	private IChannelDao channelDao;
	
	private DecimalFormat dfNumber = new DecimalFormat("#,###");
	private DecimalFormat dfPercent = new DecimalFormat("0.000");
	private DecimalFormat dfCurrency = new DecimalFormat("0.00");
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	@Override
	public void partnerReport(Model model, Map<String, Object> paraMap, Integer pageNo, Integer pageSize , HttpServletResponse response, Long userId) {

		CustomerContextHolder
				.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		
		//查当前人员有权限看到的顶级广告主（分页）
		List<Map<String, Object>> firstList = null ;
		//根据页面条件获取最终的广告主树列表（分页）
		List<Map<String,Object>> resultTreeList = null ;

		pageNo = null == pageNo ? 1 : pageNo;
		pageSize = null == pageSize ? 10 : pageSize;
		
		boolean pvNeedFormat = true ;
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pid", "-1");
		if(userId != null){
			map.put("userId", userId);
		}
		
		Pagination<Map<String,Object>> page = new Pagination<Map<String,Object>>();
		
		//如果是分页需按分页查询firstList
		if(response == null){

			page.setPageNo(pageNo);
			page.setPageSize(pageSize);
			page.setCondition(map);
			model.addAttribute("page", page);
			
			firstList = partnerDao.operatorPageListMapByMap(page);
			
		}
		//如果是下载需不按分页查询firstList
		else{
			pvNeedFormat = false ;
			
			firstList = partnerDao.listMapByMap(map) ;
		}
		
		resultTreeList = getPartnerReportResultTreeList(firstList, paraMap);
		
		//为页面处理广告主的扩展信息
		processPartnerReportExtendInfo(paraMap , resultTreeList , pvNeedFormat);
		
		/**
		 * 最后一行总计
		 */
		
		//要加入最后一行的总计
		Map<String, Object> lastRowMap = new HashMap<String, Object>() ;
		lastRowMap.put("id", "总计");
		//“总计”的统计数据
		Map<String, Object> totalSumMap = null;
		
		//查当前人员有权限看到的顶级广告主（总计）
		List<Map<String,Object>> firstListForSum = null ;
		//根据页面条件获取最终的广告主树列表（总计）
		List<Map<String,Object>> resultTreeListForSum = null ;
		
		//如果是分页需重新查询firstList
		if(response == null){
			CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
			//重新查询firstList
			firstListForSum = partnerDao.listMapByMap(map) ;
			//重新获取resultList
			resultTreeListForSum = getPartnerReportResultTreeList(firstListForSum, paraMap);
		}
		//如果是下载则直接使用前面查询出的resultTreeList
		else{
			resultTreeListForSum = resultTreeList ; 
		}
		
		//拼接所有符合条件的广告主的id，并进行统计
		if(resultTreeListForSum != null && resultTreeListForSum.size() > 0){
			
			String totalPartnerIdArray = "" ;
			for (Map<String, Object> partner : resultTreeListForSum) {
				totalPartnerIdArray += partner.get("id") + "," ;
			}
			if(totalPartnerIdArray.lastIndexOf(",") > 0){
				totalPartnerIdArray = totalPartnerIdArray.substring(0, totalPartnerIdArray.lastIndexOf(","));
			}
			
			paraMap.put("partnerIdArray", totalPartnerIdArray);
			
			// 切换到统计库
			CustomerContextHolder
					.setContextType(CustomerContextHolder.SESSION_FACTORY_COUNT);
			totalSumMap = pardDao.getSum(paraMap);
			
		}
		if(totalSumMap == null){
			totalSumMap = getEmptyCountDataMap();
		}
		
		//格式化“总计”的统计数据
		addFormattedCountDateToRow(lastRowMap, totalSumMap , pvNeedFormat);
		
		resultTreeList.add(lastRowMap);
		
		/**
		 * 最后一行总计 END
		 */
		if(response == null){
			page.setListMap(resultTreeList);
			//model.addAttribute("list", resultTreeList);
		}
		else{
			// 输出文件
			ExcelUtil.downloadExcel("partnerReportExcelData", resultTreeList,
					response);
		}
		
	}
	
	/**
	 * 根据查询条件筛选，决定最后要显示出来的广告主
	 * 
	 * @param firstPartnerList
	 * @param paraMap
	 * @return
	 */
	private List<Map<String,Object>> getPartnerReportResultTreeList(List<Map<String,Object>> firstPartnerList, Map<String, Object> paraMap){
		
		ArrayList<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		
		if(firstPartnerList == null || paraMap == null){
			return resultList ;
		}
		
		//加载顶级广告主的所有下级
		ArrayList<Map<String, Object>> allPartnerList = new ArrayList<Map<String,Object>>();
		
		if (firstPartnerList != null) {
			for (Map<String, Object> partner : firstPartnerList) {
				long partnerId = (long)partner.get("id");
				List<Map<String, Object>> partnerHierachyList = partnerService.listPartnerHierarchy(partnerId, false);
				allPartnerList.add(partner);
				allPartnerList.addAll(partnerHierachyList);
			}
		}
			
		//根据查询条件筛选，决定最后要显示出来的广告主
		for (Map<String, Object> partner : allPartnerList) {
			
			if(filter(partner, allPartnerList, paraMap)){
				resultList.add(partner);
			}
		}
		
		return resultList ;
	}
	
	/**
	 * 
	 * @param partner
	 * @param allPartnerList
	 * @param paraMap
	 * @return
	 */
	private boolean filter(Map<String,Object> partner , List<Map<String,Object>> allPartnerList , Map<String, Object> paraMap){
		
		if(partner == null){
			return false ;
		}
		
		long partnerId = partner.get("id") != null ? (long) partner.get("id") : -1 ;
		String partnerIdString = partnerId + "" ;
		String partner_partnerName = partner.get("partner_name") != null ? (String)partner.get("partner_name") : "" ;
		int partner_partnerType = partner.get("partner_type") != null ? (int)partner.get("partner_type") : -1 ;
		int partner_status = partner.get("status") != null ? (int) partner.get("status") : -1 ;
		Date partner_firstOnlineTime = partner.get("first_online_time") != null ? (Date)partner.get("first_online_time") : null ;
		/**
		 * ----------  看partner是否满足条件，如果满足则直接返回 -------------------
		 */
		boolean b = true ;
		if(paraMap.get("partnerIdOrName") != null){
			
			String partnerIdOrName = (String)paraMap.get("partnerIdOrName");
			if(partnerIdString.equals(partnerIdOrName) == false && partner_partnerName.indexOf(partnerIdOrName) < 0){
				b = false ;
			}
		}
		if(b && paraMap.get("partnerType") != null){
			int partnerType = (int)paraMap.get("partnerType");
			if(partner_partnerType != partnerType){
				b = false ;
			}
		}
		if(b && paraMap.get("status") != null){
			int status = (int) paraMap.get("status");
			if(partner_status != status){
				b = false ;
			}
		}
		
//		if(partnerId == 307){
//			System.out.println("aaa");
//		}
		
		if(b && paraMap.get("firstOnlineStartTime") != null){
			Date firstOnlineStartTime = (Date) paraMap.get("firstOnlineStartTime");
			//没有上线时间就不满足条件
			if(partner_firstOnlineTime == null){
				b = false ;
			}
			else{
				int i = partner_firstOnlineTime.compareTo(firstOnlineStartTime) ;
				//小于开始时间就不满足条件
				if(i == -1){
					b = false ;
				}
			}
		}
		if(b && paraMap.get("firstOnlineEndTime") != null){
			Date firstOnlineEndTime = (Date) paraMap.get("firstOnlineEndTime");
			//没有上线时间就不满足条件
			if(partner_firstOnlineTime == null){
				b = false ;
			}
			else{
				int i = partner_firstOnlineTime.compareTo(firstOnlineEndTime) ;
				//大于结束时间就不满足条件
				if(i == 1){
					b = false ;
				}
			}
		}
		
		if(b){
			partner.put("isTarget", "1");
			return true ;
		}
		/**
		 * ----------  看partner是否满足条件，如果满足则直接返回  end -------------------
		 */
		
		/**
		 * ----------  partner不满足条件，找到partner的下级 或下级的下级有没有符合条件的-------------------
		 */
		ArrayList<Map<String,Object>> sublist = new ArrayList<Map<String,Object>>();
		
		for (Map<String, Object> subPartner : allPartnerList) {
			
			long pid = (long)subPartner.get("pid");
			
			if(pid == partnerId){
				sublist.add(subPartner);
			}
			
		}
		
		for (Map<String, Object> subPartner : sublist) {
			
			if(filter(subPartner , allPartnerList , paraMap)){
				return true ;
			}
			
		}
		/**
		 * ----------  partner不满足条件，找到partner的下级 或下级的下级有没有符合条件的  end -------------------
		 */
		
		return false ;
	}
	
	@Override
	public List<Map<String,Object>> getChildrenListForPartnerReport(Long partnerId , Map<String, Object> paraMap){
		
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		
		paraMap.put("pid", partnerId);
		
		List<Map<String,Object>> childrenList = partnerDao.listMapByMap(paraMap);
		
		if(childrenList != null && childrenList.size() > 0){
			processPartnerReportExtendInfo(paraMap , childrenList , true);
		}
		
		return childrenList ;
	}
	
	/**
	 * 为广告主报表处理广告主的扩展信息
	 * 
	 * @param paraMap
	 * @param partnerList
	 * @param needLastSumRow
	 * @param pvNeedFormat
	 */
	private void processPartnerReportExtendInfo(Map<String, Object> paraMap , List<Map<String, Object>> partnerList , boolean pvNeedFormat){
		
		// 把开始结束日期取出来，查询账户充值的时候用
		String startDate = paraMap.get("startDate") != null ? paraMap.get(
				"startDate").toString() : null;
		String endDate = paraMap.get("endDate") != null ? paraMap
				.get("endDate").toString() : null;
		
		/**
		 * 每个广告主的基本信息
		 */
		if(partnerList != null){
			
			for (Map<String, Object> partnerMap : partnerList) {
				// 处理展示信息
				processPartnerReportExtendInfo_baseInfo(partnerMap, startDate,
						endDate);
				
			}
		}
		
		/**
		 * 每个广告主的基本信息 END
		 */

		/**
		 * 处理每个广告主的统计信息
		 */
		if(partnerList != null){
			for (Map<String, Object> partner : partnerList) {

				long partnerId = (long) partner.get("id");
				
				Map<String,Object> partnerSumMap = getPartnerFamilyCountData(partnerId , (Integer)paraMap.get("startDay") , (Integer)paraMap.get("endDay"));

				addFormattedCountDateToRow(partner, partnerSumMap , pvNeedFormat);
			}
		}
		/**
		 * 处理每个广告主的统计信息 END
		 */
		
	}

	/**
	 * 广告主报表，处理查询到的广告主原始数据 step1，广告主基本信息
	 * 
	 * @param paraMap
	 * @param partnerList
	 * @param needLastSumRow
	 * @param isDownloadExcel
	 */
	private void processPartnerReportExtendInfo_baseInfo(Map<String, Object> partnerMap, String startDate, String endDate) {
		
		if (partnerMap != null) {
			
			long partnerId = (long) partnerMap.get("id");
			
			// 如果广告主不是【jtd公司】，就查询当前partner的运营人员
			Map<String, Object> operatorMap = userPartnerDao
					.findByPartnerAndRole(partnerId, RoleType.OPERATE);

			if (operatorMap != null) {
				// 把运营人员信息放到map中
				partnerMap.put("operator_name", operatorMap.get("user_name") + "(ID:" + operatorMap.get("user_id") +  ")");
			}
			
			//查询当前广告主是否有下级（以便判断是否显示折叠图标）
			Map<String, Object> param2 = new HashMap<String, Object>();
			param2.put("pid", partnerId);
			List<Partner> subPartnerList = partnerDao.listByMap(param2);
			if (subPartnerList != null && subPartnerList.size() > 0) {
				partnerMap.put("hasChildren","1");
			}
			
			Date firstOnlineTime = (Date)partnerMap.get("first_online_time");
			if(firstOnlineTime != null){
				partnerMap.put("first_online_time", sdf.format(firstOnlineTime));
			}
			else{
				partnerMap.put("first_online_time", "--");
			}
			/**
			 * 获取广告主充值总和
			 */
			double rechargeSumYuan = partnerService
					.getPartnerRechargeAmountSumYuan(partnerId, startDate, endDate);
			partnerMap.put("recharge_sum_yuan", dfCurrency.format(rechargeSumYuan));

			/**
			 * 获取广告主充值总和 END
			 */
			
			/**
			 * 账户余额
			 */
			if (partnerMap.get("acc_balance") != null) {
				double aby = (long) partnerMap.get("acc_balance") / 100f;
				partnerMap.put("acc_balance_yuan", dfCurrency.format(aby));
			}

			/**
			 * 处理广告主类型
			 */
			if (partnerMap.get("partner_type") != null) {
				int partnerType = (int) partnerMap.get("partner_type");
				switch (partnerType) {
				case 0:
					partnerMap.put("partner_type", "代理");
					break;
				case 1:
					partnerMap.put("partner_type", "直客");
					break;
				case 2:
					partnerMap.put("partner_type", "OEM");
					break;
				default:
					break;
				}
			}
			/**
			 * 处理广告主类型 END
			 */

			/**
			 * 状态
			 */
			if (partnerMap.get("status") != null) {
				int status = (int) partnerMap.get("status");
				switch (status) {
				case 0:
					partnerMap.put("status", "启用");
					break;
				case 1:
					partnerMap.put("status", "停用");
					break;
				default:
					break;
				}
			}
			/**
			 * 状态 END
			 */
			
		}

	}

	/**
	 * 广告主报表，处理查询到的广告主原始数据 step2，广告主基本信息
	 * 
	 * @param paraMap
	 * @param partnerList
	 * @param needLastSumRow
	 * @param isDownloadExcel
	 */
	private void addFormattedCountDateToRow(Map<String, Object> rowMap, Map<String, Object> countDataMap, boolean pvNeedFormat) {

		int pv_sum = ((BigDecimal) countDataMap.get("pv_sum")).intValue();
		int uv_sum = ((BigDecimal) countDataMap.get("uv_sum")).intValue();
		int click_sum = ((BigDecimal) countDataMap.get("click_sum"))
				.intValue();
		double click_rate_sum = ((BigDecimal) countDataMap
				.get("click_rate_sum")).doubleValue();
		double cpm_expend_sum_yuan = ((BigDecimal) countDataMap
				.get("cpm_expend_sum_yuan")).doubleValue();
		double cpm_cost_sum_yuan = ((BigDecimal) countDataMap
				.get("cpm_cost_sum_yuan")).doubleValue();
		double cpc_expend_sum_yuan = ((BigDecimal) countDataMap
				.get("cpc_expend_sum_yuan")).doubleValue();
		double cpc_cost_sum_yuan = ((BigDecimal) countDataMap
				.get("cpc_cost_sum_yuan")).doubleValue();
		double expend_sum_yuan = ((BigDecimal) countDataMap
				.get("expend_sum_yuan")).doubleValue();
		double cost_sum_yuan = ((BigDecimal) countDataMap.get("cost_sum_yuan"))
				.doubleValue();

		if(pvNeedFormat){
			rowMap.put("pv_sum", dfNumber.format(pv_sum));
			rowMap.put("uv_sum", dfNumber.format(uv_sum));
			rowMap.put("click_sum", dfNumber.format(click_sum));
		}
		else{
			rowMap.put("pv_sum", pv_sum);
			rowMap.put("uv_sum", uv_sum);
			rowMap.put("click_sum", click_sum);
		}
		
		rowMap.put("click_rate_sum", dfPercent.format(click_rate_sum));
		rowMap.put("cpm_expend_sum_yuan",
				dfCurrency.format(cpm_expend_sum_yuan));
		rowMap.put("cpm_cost_sum_yuan",
				dfCurrency.format(cpm_cost_sum_yuan));
		rowMap.put("cpc_expend_sum_yuan",
				dfCurrency.format(cpc_expend_sum_yuan));
		rowMap.put("cpc_cost_sum_yuan",
				dfCurrency.format(cpc_cost_sum_yuan));
		rowMap.put("expend_sum_yuan", dfCurrency.format(expend_sum_yuan));
		rowMap.put("cost_sum_yuan", dfCurrency.format(cost_sum_yuan));

	}

	/**
	 * 创建一个空的6项sum值
	 * 
	 * @return
	 */
	private Map<String, Object> getEmptyCountDataMap() {
		HashMap<String, Object> sumMap = new HashMap<String, Object>();
		sumMap.put("bid_sum",new BigDecimal(0));
		sumMap.put("pv_sum", new BigDecimal(0));
		sumMap.put("uv_sum", new BigDecimal(0));
		sumMap.put("click_sum", new BigDecimal(0));
		sumMap.put("click_rate_sum", new BigDecimal(0));
		sumMap.put("cpm_expend_sum_yuan", new BigDecimal(0));
		sumMap.put("cpc_expend_sum_yuan", new BigDecimal(0));
		sumMap.put("cpm_cost_sum_yuan", new BigDecimal(0));
		sumMap.put("cpc_cost_sum_yuan", new BigDecimal(0));
		sumMap.put("expend_sum_yuan", new BigDecimal(0));
		sumMap.put("cost_sum_yuan", new BigDecimal(0));
		return sumMap;
	}
	
	/**
	 * 获取广告主及其所有下级的统计数据
	 * @param partnerId
	 * @param paraMap
	 * @return
	 */
	private Map<String,Object> getPartnerFamilyCountData(long partnerId , Integer startDay , Integer endDay){
		
		/**
		 * 查询该广告主的下级，并拼接广告主家族的id，用于在统计库中查询
		 */
		String partnerFamilyId = "" ;
		List<Map<String,Object>> partnerChildrenList = partnerService.listPartnerHierarchy(partnerId, true);
		if(partnerChildrenList != null){
			for (Map<String,Object> subPartner : partnerChildrenList) {
				partnerFamilyId += subPartner.get("id") + "," ;
			}
		}
		if(partnerFamilyId.lastIndexOf(",") > 0){
			partnerFamilyId = partnerFamilyId.substring(0, partnerFamilyId.lastIndexOf(","));
		}
		/**
		 * 统计这个广告主整个家族的数据
		 */
		HashMap<String, Object> sumParam = new HashMap<String, Object>();
		sumParam.put("partnerIdArray", partnerFamilyId);
		if(startDay != null){
			sumParam.put("startDay", startDay);
		}
		if(endDay != null){
			sumParam.put("endDay", endDay);
		}
		
		// 切换到统计库
		CustomerContextHolder
				.setContextType(CustomerContextHolder.SESSION_FACTORY_COUNT);
		
		// 计算partner的统计数据
		Map<String, Object> partnerSumMap = pardDao.getSum(sumParam);
		if (partnerSumMap == null) {
			partnerSumMap = getEmptyCountDataMap();
		}
		
		return partnerSumMap ;
		
	}

	@Override
	public List<Map<String, Object>> operatorReport(
			Map<String, Object> paraMap, HttpServletResponse response) {

		CustomerContextHolder
				.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

//		pageNo = null == pageNo ? 1 : pageNo;
//		pageSize = null == pageSize ? 10 : pageSize;
//
//		Pagination<SysUser> page = new Pagination<SysUser>();
//		page.setPageNo(pageNo);
//		page.setPageSize(pageSize);
//		page.setCondition(paraMap);
		
		int totalPv = 0 ;
		int totalUv = 0 ;
		int totalClick = 0 ;
		int totalExpendFen = 0 ;
		int totalCostFen = 0 ;

		// 查询得到所有的运营人员
		List<SysUser> operatorUserList = userRoleDao.listUserByRole(paraMap, RoleType.OPERATE);

		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		if (operatorUserList != null) {

			// 查询每个运营人员负责的广告主
			for (SysUser user : operatorUserList) {

				// 切换到业务库
				CustomerContextHolder
						.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

				long userId = user.getId();

				// 查询当期运营人员负责的广告主
//				UserPartner upParam = new UserPartner();
//				upParam.setUserId(userId + "");
//				List<UserPartner> upList = userPartnerDao.listBy(upParam);

				HashMap<String,Object> params = new HashMap<String, Object>();
				params.put("userId", userId);
				List<Partner> firstPartnerList = partnerDao.listByMap(params);
				
				int partner_cnt = 0;
				// 累计当期运营人员负责的广告主的基础数据
				int pv_sum = 0;
				int uv_sum = 0;
				int click_sum = 0;
				int expend_sum = 0;
				int cost_sum = 0;

				if (firstPartnerList != null) {

					//user_partner表中，每个运营会有一条数据是关联【jtd公司】的，表示属于【jtd公司】公司，运营负责的广告主中不包括【jtd公司】
					partner_cnt = firstPartnerList.size() - 1;

					// 切换到统计库
					CustomerContextHolder
							.setContextType(CustomerContextHolder.SESSION_FACTORY_COUNT);

					for (Partner partner : firstPartnerList) {

						long partnerId = partner.getId() ;
						
						//运营负责的公司不包括【jtd公司】，所以不统计【jtd公司】的数据
						if(partnerId == Constants.CE_PARTNER_ID){
							continue ;
						}
						
						Map<String,Object> partnerSumMap = getPartnerFamilyCountData(partnerId , (Integer)paraMap.get("startDay") , (Integer)paraMap.get("endDay"));

						pv_sum += ((BigDecimal) partnerSumMap.get("pv_sum"))
								.intValue();
						uv_sum += ((BigDecimal) partnerSumMap.get("uv_sum"))
								.intValue();
						click_sum += ((BigDecimal) partnerSumMap
								.get("click_sum")).intValue();
						expend_sum += ((BigDecimal) partnerSumMap
								.get("expend_sum")).intValue();
						cost_sum += ((BigDecimal) partnerSumMap.get("cost_sum"))
								.intValue();
						
					}

				}
				
				totalPv += pv_sum ;
				totalUv += uv_sum ;
				totalClick += click_sum ;
				totalExpendFen += expend_sum ;
				totalCostFen += cost_sum ;

				// 合计当期运营人员负责的广告主的其他数据
				double click_rate_sum = ReportUtil.getClickRate(click_sum,
						pv_sum);
				double cpm_expend_sum_yuan = ReportUtil.getCpmSingleYuan(
						expend_sum, pv_sum);
				double cpc_expend_sum_yuan = ReportUtil.getCpcSingleYuan(
						expend_sum, click_sum);
				double cpm_cost_sum_yuan = ReportUtil.getCpmSingleYuan(
						cost_sum, pv_sum);
				double cpc_cost_sum_yuan = ReportUtil.getCpcSingleYuan(
						cost_sum, click_sum);
				double expend_sum_yuan = ReportUtil.getYuan(expend_sum);
				double cost_sum_yuan = ReportUtil.getYuan(cost_sum);

				Map<String, Object> operatorsSumMap = new HashMap<String, Object>();
				operatorsSumMap.put("operator_id", user.getId());
				operatorsSumMap.put("operator_name", user.getUserName());
				operatorsSumMap.put("partner_cnt", partner_cnt);
				operatorsSumMap.put("pv_sum", pv_sum);
				operatorsSumMap.put("uv_sum", uv_sum);
				operatorsSumMap.put("click_sum", click_sum);
				operatorsSumMap.put("expend_sum", expend_sum);
				operatorsSumMap.put("cost_sum", cost_sum);
				operatorsSumMap.put("click_rate_sum", dfCurrency.format(click_rate_sum));
				operatorsSumMap.put("cpm_expend_sum_yuan", dfCurrency.format(cpm_expend_sum_yuan));
				operatorsSumMap.put("cpc_expend_sum_yuan", dfCurrency.format(cpc_expend_sum_yuan));
				operatorsSumMap.put("expend_sum_yuan", dfCurrency.format(expend_sum_yuan));
				operatorsSumMap.put("cpm_cost_sum_yuan", dfCurrency.format(cpm_cost_sum_yuan));
				operatorsSumMap.put("cpc_cost_sum_yuan", dfCurrency.format(cpc_cost_sum_yuan));
				operatorsSumMap.put("cost_sum_yuan", dfCurrency.format(cost_sum_yuan));

				returnList.add(operatorsSumMap);
			}

		}

//		Pagination<Map<String, Object>> returnPage = new Pagination<Map<String, Object>>();
//		returnPage.setPageNo(pageNo);
//		returnPage.setPageSize(pageSize);
//		returnPage.setListMap(returnList);
		
		double totalClickRate = ReportUtil.getClickRate(totalClick,
				totalPv);
		double totalCpmCostYuan = ReportUtil.getCpmSingleYuan(
				totalCostFen, totalPv);
		double totalCpmExpendYuan = ReportUtil.getCpmSingleYuan(totalExpendFen, totalPv);
		double totalCpcCostYuan = ReportUtil.getCpcSingleYuan(
				totalCostFen, totalClick);
		double totalCpcExpendYuan = ReportUtil.getCpcSingleYuan(
				totalExpendFen, totalClick);
		double totalCostYuan = ReportUtil.getYuan(totalCostFen);
		double totalExpendYuan = ReportUtil.getYuan(totalExpendFen);
		
		Map<String, Object> lastSumRow = new HashMap<String, Object>();
		lastSumRow.put("operator_id", "总计");
		lastSumRow.put("pv_sum", totalPv);
		lastSumRow.put("uv_sum", totalUv);
		lastSumRow.put("click_sum", totalClick);
		lastSumRow.put("click_rate_sum", dfCurrency.format(totalClickRate));
		lastSumRow.put("cpm_cost_sum_yuan", dfCurrency.format(totalCpmCostYuan));
		lastSumRow.put("cpm_expend_sum_yuan", dfCurrency.format(totalCpmExpendYuan));
		lastSumRow.put("cpc_cost_sum_yuan", dfCurrency.format(totalCpcCostYuan));
		lastSumRow.put("cpc_expend_sum_yuan", dfCurrency.format(totalCpcExpendYuan));
		lastSumRow.put("cost_sum_yuan", dfCurrency.format(totalCostYuan));
		lastSumRow.put("expend_sum_yuan", dfCurrency.format(totalExpendYuan));
		
		returnList.add(lastSumRow);
		
		if(response != null){
			ExcelUtil.downloadExcel("operatorReportExcelData", returnList, response);
			return null ;
		}
		else{
			return returnList ;
		}

	}

	@Override
	public List<Map<String, Object>> besCreativeRTBReport(
			String startCreativeId, String endCreativeId, String startDate,
			String endDate, final String orderBy, final String desc) {

		long longStartCreativeId = -1;
		if (StringUtils.isEmpty(startCreativeId) == false) {
			try {
				longStartCreativeId = Long.parseLong(startCreativeId);
			} catch (NumberFormatException e) {

				log.error("AdminOperatorService--besCreativeRTBReport--参数错误：startCreativeId="
						+ startCreativeId);
			}
		}

		long longEndCreativeId = -1;
		if (StringUtils.isEmpty(endCreativeId) == false) {
			try {
				longEndCreativeId = Long.parseLong(endCreativeId);
			} catch (NumberFormatException e) {

				log.error("AdminOperatorService--besCreativeRTBReport--参数错误：endCreativeId="
						+ endCreativeId);
			}
		}

		// long paramCampaignId = -1 ;
		// try {
		// paramCampaignId = Long.parseLong(campaignId);
		// } catch (NumberFormatException e) {
		//
		// log.error("AdminOperatorService--besCreativeRTBReport--参数错误：campaignId="
		// + campaignId);
		// }

		ArrayList<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		if (StringUtils.isEmpty(startDate) == false
				&& StringUtils.isEmpty(endDate) == false) {

			// final Class<BesCreativeRTB> classOfBesCreativeRTB =
			// BesCreativeRTB.class ;

			/**
			 * 按照开始结束日期去百度接口查询
			 */
			JSONArray dataArray = baiduAPIService.reportCreativeRTB(startDate,
					endDate);

			if (dataArray != null) {

				// ArrayList<BesCreativeRTB> resultList = new
				// ArrayList<BesCreativeRTB>();

				for (int i = 0; i < dataArray.size(); i++) {

					JSONObject json = dataArray.getJSONObject(i);

					// BesCreativeRTB data =
					// JSONObject.parseObject(json.toJSONString(),
					// BesCreativeRTB.class);
					Map data = JSONObject.parseObject(json.toJSONString(),
							Map.class);

					// long adId = data.getCreativeId();
					Integer adId = (Integer) data.get("creativeId");

					/**
					 * 按照页面条件将数据筛选到resultList中
					 */
					boolean isMatching = true;

					// 两个id都填表示区间
					if (longStartCreativeId != -1 && longEndCreativeId != -1) {

						if (adId >= longStartCreativeId
								&& adId <= longEndCreativeId) {
							isMatching = true;
						} else {
							isMatching = false;
						}
					}
					// 只填一个id表示等于
					else if (longStartCreativeId != -1
							|| longEndCreativeId != -1) {

						if (adId == longStartCreativeId
								|| adId == longEndCreativeId) {
							isMatching = true;
						} else {
							isMatching = false;
						}
					}

					// Ad ad = adDao.getById(adId);
					// if(ad != null){
					// long campId = ad.getCampaignId();
					// data.put("campaignId", campId);
					//
					// if(paramCampaignId != -1){
					// if(paramCampaignId == campId){
					// isMatching = true ;
					// }else{
					// isMatching = false ;
					// }
					// }
					// }

					if (isMatching) {
						resultList.add(data);
					}
					/**
					 * 按照页面条件将数据筛选到resultList中 END
					 */
				}

				/**
				 * 排序
				 */
				if (orderBy != null) {
					// 按日期排序
					if (orderBy.equals("showDate")) {

						final SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy.MM.dd");

						Collections.sort(resultList,
								new Comparator<Map<String, Object>>() {
									@Override
									/**
									 * o1 < o2 则返回-1，o1 > o2则返回1，相等返回0
									 */
									public int compare(Map<String, Object> o1,
											Map<String, Object> o2) {
										try {
											Date d1 = sdf.parse(o1.get(
													"showDate").toString());
											Date d2 = sdf.parse(o2.get(
													"showDate").toString());

											return "asc".equals(desc) ? d1
													.compareTo(d2) : -d1
													.compareTo(d2);

										} catch (Exception e) {
											log.error(
													"besCreativeRTBReport按日期排序发生错误",
													e);
										}
										return 0;
									}
								});
					}
					// 按id排序
					else if (orderBy.equals("creativeId")) {

						Collections.sort(resultList,
								new Comparator<Map<String, Object>>() {
									@Override
									/**
									 * o1 < o2 则返回-1，o1 > o2则返回1，相等返回0
									 */
									public int compare(Map<String, Object> o1,
											Map<String, Object> o2) {

										try {
											Integer id1 = (Integer) o1
													.get("creativeId");
											Integer id2 = (Integer) o2
													.get("creativeId");

											if (id1 < id2) {
												return "asc".equals(desc) ? -1
														: 1;
											} else if (id1 > id2) {
												return "asc".equals(desc) ? 1
														: -1;
											} else {
												return 0;
											}
										} catch (Exception e) {
											log.error(
													"besCreativeRTBReport按id排序发生错误",
													e);
										}
										return 0;
									}
								});
					}
					// 按比率排序
					else if (!orderBy.equals("showDate")
							&& !orderBy.equals("creativeId")
							&& !orderBy.equals("creativeType")) {

						Collections.sort(resultList,
								new Comparator<Map<String, Object>>() {
									@Override
									/**
									 * o1 < o2 则返回-1，o1 > o2则返回1，相等返回0
									 */
									public int compare(Map<String, Object> o1,
											Map<String, Object> o2) {

										try {

											float rate1 = ((BigDecimal) o1
													.get(orderBy)).floatValue();
											float rate2 = ((BigDecimal) o2
													.get(orderBy)).floatValue();

											if (rate1 < rate2) {
												return "asc".equals(desc) ? -1
														: 1;
											} else if (rate1 > rate2) {
												return "asc".equals(desc) ? 1
														: -1;
											} else {
												return 0;
											}

										} catch (Exception e) {
											log.error(
													"besCreativeRTBReport按比率排序发生错误",
													e);
										}

										return 0;
									}
								});
					}
				}
				/**
				 * 排序 END
				 */

				/**
				 * 将小数转换为百分比字符串
				 */
				DecimalFormat df = new DecimalFormat("0.00%");

				for (Map<String, Object> map : resultList) {

					Integer creativeType = (Integer) map.get("creativeType");
					String creativeTypeName = creativeType == 1 ? "静态创意"
							: "动态创意";
					map.put("creativeType", creativeTypeName);

					Set<String> keySet = map.keySet();
					for (String key : keySet) {

						// 只要不是这三项，其他项都是表示百分比的小数
						if (!key.equals("showDate")
								&& !key.equals("creativeId")
								&& !key.equals("creativeType")) {

							try {
								BigDecimal rate = (BigDecimal) map.get(key);

								// 转换成字符串形式
								String rateString = df.format(rate);
								map.put(key, rateString);

							} catch (Exception e) {
								log.error(
										"besCreativeRTBReport将比率小数转换为百分比字符串发生错误",
										e);
							}

						}
					}

				}
				/**
				 * 将小数转换为百分比字符串 END
				 */

				ArrayList<Map<String, Object>> test = new ArrayList<Map<String, Object>>();
				Collections.sort(test, new Comparator<Map<String, Object>>() {

					@Override
					public int compare(Map<String, Object> o1,
							Map<String, Object> o2) {
						return 0;
					}

				});

			}
		}

		/**
		 * 放到session中提供excel下载使用
		 */
		HttpSession session = request.getSession();
		session.setAttribute("besCreativeRTBExcelData", resultList);

		return resultList;
	}

	public void downloadBesCreativeRTBReport(HttpServletResponse response) {

		HttpSession session = request.getSession();

		List<Map<String, Object>> dataList = (List<Map<String, Object>>) session
				.getAttribute("besCreativeRTBExcelData");

		ExcelUtil.downloadExcel("besCreativeRTBExcelData", dataList, response);

	}

	@Override
	public void campChannelReport(Model model, Map<String, Object> params,
			Integer pageNo, Integer pageSize , HttpServletResponse response) {


		// 切换业务库数据源
		CustomerContextHolder
				.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

		/**
		 * 页面上所有渠道的选项
		 */
		List<Channel> channelList = channelDao.listAll();
		model.addAttribute("channelList", channelList);

		// 由于统计库无法按活动状态、活动名称查询，所以先从业务库里查出所有的活动
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
			
			if (campIds.length() > 0) {

				/*
				 * 报表查询
				 */
				List<Map<String, Object>> dataList = null ;
				
				// 切换统计库数据源
				CustomerContextHolder
						.setContextType(CustomerContextHolder.SESSION_FACTORY_COUNT);

				//如果response不为空就认为是下载报表
				if(response == null){
					pageNo = null == pageNo ? 1 : pageNo;
					pageSize = null == pageSize ? 10 : pageSize;
					
					Pagination<Map<String, Object>> page = new Pagination<Map<String, Object>>();
					page.setPageNo(pageNo);
					page.setPageSize(pageSize);
					page.setCondition(params);

					dataList = campChanneldDao.getMapPageBy(page);
					page.setList(dataList);
					model.addAttribute("page", page);
				}
				else{
					dataList = campChanneldDao.listByMap(params);
				}
				
				if (dataList != null) {
					
					/**
					 * 匹配公司名称，渠道名称
					 */
					for (Map<String, Object> map : dataList) {

						long campId = (long) map.get("camp_id");
						String campaignName = "";
						String partnerName = "";
						for (Map<String, Object> campMap : campList) {
							if (campId == (long) campMap.get("id")) {
								campaignName = (String) campMap
										.get("campaignName");
								partnerName = (String) campMap
										.get("partnerName");
								break;
							}
						}
						map.put("campaign_name", campaignName);
						map.put("partner_name", partnerName);

						long channelId = (long) map.get("channel_id");
						String channelName = "";
						if (channelList != null) {
							for (Channel channel : channelList) {
								if (channelId == channel.getId()) {
									channelName = channel.getChannelName();
									break;
								}
							}
						}

						map.put("channel_name", channelName);
						
						int pv = (int)map.get("pv");
						int uv = (int)map.get("uv");
						int click = (int)map.get("click");
						int expendFen = (int)map.get("expend");
						int costFen = (int)map.get("cost");
						
						double click_rate = ReportUtil.getClickRate(click, pv);
						double cpm_expend_yuan = ReportUtil.getCpcSingleYuan(expendFen, click);
						double cpm_cost_yuan = ReportUtil.getCpmSingleYuan(costFen, pv);
						double cpc_expend_yuan = ReportUtil.getCpcSingleYuan(expendFen, click);
						double cpc_cost_yuan = ReportUtil.getCpcSingleYuan(costFen, click);
						double expend_yuan = ReportUtil.getYuan(expendFen);
						double cost_yuan = ReportUtil.getYuan(costFen);

						//reponse为空时视为在页面上显示，需要对数字进行格式化
						if(response == null){
							map.put("pv", dfNumber.format(pv));
							map.put("uv", dfNumber.format(uv));
							map.put("click", dfNumber.format(click));
						}
						
						map.put("click_rate", dfPercent.format(click_rate));
						map.put("cpm_expend_yuan",
								dfCurrency.format(cpm_expend_yuan));
						map.put("cpm_cost_yuan",
								dfCurrency.format(cpm_cost_yuan));
						map.put("cpc_expend_yuan",
								dfCurrency.format(cpc_expend_yuan));
						map.put("cpc_cost_yuan",
								dfCurrency.format(cpc_cost_yuan));
						map.put("expend_yuan", dfCurrency.format(expend_yuan));
						map.put("cost_yuan", dfCurrency.format(cost_yuan));
						
						//map.put("date", sdfDate.format(date));
						
					}
					/**
					 * 匹配渠道名称 END
					 */

					/*
					 * 最后一行总计
					 */
					List<Map<String, Object>> sumDataList = campChanneldDao
							.listSumByMap(params, false);
					if (sumDataList != null && sumDataList.size() == 1) {
						Map<String,Object> lastRow = sumDataList.get(0);
						lastRow.put("camp_id", "总计");
						dataList.add(lastRow);
						
						int pv = ((BigDecimal) lastRow.get("pv")).intValue();
						int uv = ((BigDecimal) lastRow.get("pv")).intValue();
						int click = ((BigDecimal) lastRow.get("pv")).intValue();
						
						double click_rate = ((BigDecimal) lastRow
						.get("click_rate")).doubleValue();
						double cpm_expend_yuan = ((BigDecimal) lastRow
								.get("cpm_expend_yuan")).doubleValue();
						double cpm_cost_yuan = ((BigDecimal) lastRow
								.get("cpm_cost_yuan")).doubleValue();
						double cpc_expend_yuan = ((BigDecimal) lastRow
								.get("cpc_expend_yuan")).doubleValue();
						double cpc_cost_yuan = ((BigDecimal) lastRow
								.get("cpc_cost_yuan")).doubleValue();
						double expend_yuan = ((BigDecimal) lastRow
								.get("expend_yuan")).doubleValue();
						double cost_yuan = ((BigDecimal) lastRow.get("cost_yuan"))
								.doubleValue();
		
						lastRow.put("pv", dfNumber.format(pv));
						lastRow.put("uv", dfNumber.format(uv));
						lastRow.put("click", dfNumber.format(click));
						lastRow.put("click_rate", dfPercent.format(click_rate));
						lastRow.put("cpm_expend_yuan",
								dfCurrency.format(cpm_expend_yuan));
						lastRow.put("cpm_cost_yuan",
								dfCurrency.format(cpm_cost_yuan));
						lastRow.put("cpc_expend_yuan",
								dfCurrency.format(cpc_expend_yuan));
						lastRow.put("cpc_cost_yuan",
								dfCurrency.format(cpc_cost_yuan));
						lastRow.put("expend_yuan", dfCurrency.format(expend_yuan));
						lastRow.put("cost_yuan", dfCurrency.format(cost_yuan));
					}
					/*
					 * 最后一行总计 END
					 */
					
					//如果response不为空就认为是下载报表
					if(response != null){
						ExcelUtil.downloadExcel("campChannelExcelData", dataList, response);
					}
					
				}
				/*
				 * 报表查询 END
				 */

			}

		}

	}
	
	public static void main(String[] args){
		
		List<Map<String,Object>> allPartnerList = new ArrayList<Map<String,Object>>();
		
		Map<String,Object> p1 = new HashMap<String, Object>();
		p1.put("id", 1L);
		p1.put("status", 0);
		p1.put("pid", 0L);
		
		Map<String,Object> p11 = new HashMap<String, Object>();
		p11.put("id", 11L);
		p11.put("status", 0);
		p11.put("pid", 1L);
		
		Map<String,Object> p111 = new HashMap<String, Object>();
		p111.put("id", 111L);
		p111.put("status", 1);
		p111.put("pid", 11L);
		
		
		Map<String,Object> p112 = new HashMap<String, Object>();
		p112.put("id", 112L);
		p112.put("status", 0);
		p112.put("pid", 11L);
		
		Map<String,Object> p12 = new HashMap<String, Object>();
		p12.put("id", 12L);
		p12.put("status", 0);
		p12.put("pid", 1L);
		
		Map<String,Object> p121 = new HashMap<String, Object>();
		p121.put("id", 121L);
		p121.put("status", 0);
		p121.put("pid", 12L);
		
		Map<String,Object> p2 = new HashMap<String, Object>();
		p2.put("id", 2L);
		p2.put("status", 1);
		p2.put("pid", 0L);
		
		allPartnerList.add(p1);
		allPartnerList.add(p11);
		allPartnerList.add(p111);
		allPartnerList.add(p112);
		allPartnerList.add(p12);
		allPartnerList.add(p121);
		allPartnerList.add(p2);
		
		Map<String,Object> paraMap = new HashMap<String, Object>();
		paraMap.put("status", 1);
		
		
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		
		AdminOperatorService aaa = new AdminOperatorService();
		
		for (Map<String, Object> partner : allPartnerList) {
			if(aaa.filter(partner,allPartnerList,paraMap)){
				resultList.add(partner);
			}
		}
		
		//打印最终结果
		for (Map<String, Object> partner : resultList) {
			System.out.println(partner.get("id"));
		}
	}

}