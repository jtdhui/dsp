package com.jtd.web.service.admin.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.jtd.commons.dao.BaseDao;
import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.commons.page.Pagination;
import com.jtd.web.constants.AuditStatus;
import com.jtd.web.constants.CatgSerial;
import com.jtd.web.constants.Constants;
import com.jtd.web.constants.RoleType;
import com.jtd.web.dao.IPartnerDao;
import com.jtd.web.dao.IUserPartnerDao;
import com.jtd.web.dao.impl.ChannelDao;
import com.jtd.web.dao.impl.PartnerStatusDao;
import com.jtd.web.dao.impl.QualiDocDao;
import com.jtd.web.exception.PlatformException;
import com.jtd.web.po.ActiveUser;
import com.jtd.web.po.Channel;
import com.jtd.web.po.Partner;
import com.jtd.web.po.PartnerStatusPO;
import com.jtd.web.po.QualiDoc;
import com.jtd.web.service.IMQConnectorService;
import com.jtd.web.service.admin.IAdminPartnerService;
import com.jtd.web.service.admin.IAdminQualiDocService;
import com.jtd.web.service.adx.bes.BaiduAPIService;
import com.jtd.web.service.adx.hzeng.HZengAPIService;
import com.jtd.web.service.adx.vam.VamAPIService;
import com.jtd.web.service.impl.BaseService;
import com.jtd.web.vo.ChannelCategory;

@Service
public class AdminQualiDocService extends BaseService<QualiDoc> implements
		IAdminQualiDocService {

	Logger logger = Logger.getLogger("errorLog");

	@Autowired
	private IAdminPartnerService partnerService;

	@Autowired
	private QualiDocDao qualiDocDao;

	@Autowired
	private PartnerStatusDao partnerStatusDao;

	@Autowired
	private ChannelDao channelDao;

	@Autowired
	private BaiduAPIService baiduAPIService;
	
	@Autowired
	private HZengAPIService hzengAPIService;
	
	@Autowired
	private IMQConnectorService mqConnectorService;

	@Autowired
	private IPartnerDao partnerDao;

	@Autowired
	private IUserPartnerDao userPartnerDao;

	@Override
	protected BaseDao<QualiDoc> getDao() {
		CustomerContextHolder
				.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		return qualiDocDao;
	}

	@Override
	public Pagination<Map<String, Object>> findPageMap(
			Map<String, Object> paramMap, Integer pageNo, Integer pageSize)
			throws PlatformException {

		pageNo = null == pageNo ? 1 : pageNo;
		pageSize = null == pageSize ? 10 : pageSize;

		CustomerContextHolder
				.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

		/**
		 * 先查找广告主的分页信息
		 * 
		 */
		Pagination<Partner> partnerPage = partnerService.findPageBy(paramMap,
				pageNo, pageSize);

		if (partnerPage != null && partnerPage.getList() != null) {

			ArrayList<Map<String, Object>> resultMapList = new ArrayList<Map<String, Object>>();

			/**
			 * 开始查找内部&渠道审核信息
			 */
			for (Partner partner : partnerPage.getList()) {

				/**
				 * 开始查找内部审核状态
				 */
				List<QualiDoc> partnerQualiList = qualiDocDao
						.listByPartnerId(partner.getId());

				int qualiDocCount = 0; // 资质文件数量
				int internalAuditStatus = 0; // 内部审核状态默认为未提交
				String updateTime = "";
				String internalAuditTime = "";
				if (partnerQualiList != null && partnerQualiList.size() > 0) {

					qualiDocCount = partnerQualiList.size();

					QualiDoc doc0 = partnerQualiList.get(0);

					Date d1 = doc0.getUpdateTime(); // 资质更新时间
					Date d2 = doc0.getInternalAuditTime(); // 内部审核时间
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyy-MM-dd HH:mm:ss");
					if (d1 != null) {
						updateTime = sdf.format(d1);
					}
					if (d2 != null) {
						internalAuditTime = sdf.format(d2);
					}

					internalAuditStatus = doc0.getStatus();
				}

				// 如果页面上有内部审核状态的查询条件，则要符合查询条件才能继续进行
				if (paramMap.get("internalAuditStatus") != null) {
					if (internalAuditStatus != (int) paramMap
							.get("internalAuditStatus")) {
						continue;
					}
				}

				/**
				 * 开始查找广告主的渠道审核状态
				 */
				int notCommitCount = 0;
				int successCount = 0;
				int failCount = 0;
				int waitCount = 0;

				List<Map<String, Object>> partnerStatusList = partnerStatusDao
						.listAllChannelAuditStatusByPartnerId(partner.getId());

				// int adxAuditStatus = 0; // 渠道审核状态默认为全部未提交

				if (partnerStatusList != null && partnerStatusList.size() > 0) {

					for (Map<String, Object> partnerStatusMap : partnerStatusList) {

						if (partnerStatusMap.get("status") == null) {
							notCommitCount++;
						} else {

							int status = (int) partnerStatusMap.get("status");

							if (status == AuditStatus.STATUS_WAIT.getCode()) {
								waitCount++;
							}
							if (status == AuditStatus.STATUS_SUCCESS.getCode()) {
								successCount++;
							}
							if (status == AuditStatus.STATUS_FAIL.getCode()) {
								failCount++;
							}
						}

					}

					// if (waitCount > 0) {
					// adxAuditStatus = 1; // 存在等待中
					// }
					// if (waitCount == partnerStatusList.size() ) {
					// adxAuditStatus = 2; // 全部adx等待中
					// }
					// if (successCount > 0) {
					// adxAuditStatus = 3; // 部分adx已通过
					// }
					// if (successCount == partnerStatusList.size()) {
					// adxAuditStatus = 4; // 全部adx已通过
					// }
					// if (failCount > 0) {
					// adxAuditStatus = 5; // 部分adx已拒绝
					// }
					// if (failCount == partnerStatusList.size()) {
					// adxAuditStatus = 6; // 全部adx已拒绝
					// }
				}
				// // 如果页面上有内部审核状态的查询条件，则要符合查询条件才能继续进行
				// if (paramMap.get("adxAuditStatus") != null) {
				// if (adxAuditStatus != (int) paramMap
				// .get("adxAuditStatus")) {
				// continue;
				// }
				// }

				/*
				 * 查询广告主行业信息
				 */
				String categoryName = "";
				ChannelCategory cc = partnerService.getPartnerCategory(partner
						.getId());
				if (cc != null) {
					categoryName = cc.getName();
				}

				HashMap<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("partnerId", partner.getId());
				// 简称
				resultMap.put("simpleName", partner.getSimpleName());
				// 广告主名称
				resultMap.put("partnerName", partner.getPartnerName());
				// 上级代理名称
				resultMap.put("pName", partner.getpName());
				// 行业名称
				resultMap.put("categoryName", categoryName);
				// 资质文件数量
				resultMap.put("qualiDocCount", qualiDocCount);
				// 资质更新时间
				resultMap.put("updateTime", updateTime);
				// 资质审核结束时间（adx审核通过或结束）
				resultMap.put("internalAuditTime", internalAuditTime);
				// 资质内部审核情况
				resultMap.put("internalAuditStatus", internalAuditStatus);
				// adx审核情况
				// resultMap.put("adxAuditStatus", adxAuditStatus);

				resultMap.put("notCommitCount", notCommitCount);
				resultMap.put("successCount", successCount);
				resultMap.put("failCount", failCount);
				resultMap.put("waitCount", waitCount);

				resultMapList.add(resultMap);

			}

			Pagination<Map<String, Object>> page = new Pagination<Map<String, Object>>();
			page.setPageNo(pageNo);
			page.setPageSize(pageSize);
			page.setTotalCount(partnerPage.getTotalCount());
			page.setList(resultMapList);

			return page;
		}

		return null;
	}

	@Override
	public void findOperation(Model model, Map<String, Object> paraMap,
			Integer pageNo, Integer pageSize, HttpServletResponse response,
			ActiveUser user) {

		CustomerContextHolder
				.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

		pageNo = null == pageNo ? 1 : pageNo;
		pageSize = null == pageSize ? 10 : pageSize;

		List<Map<String, Object>> firstPartnerList = null;
		Pagination<Map<String, Object>> page = new Pagination<Map<String, Object>>();
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		model.addAttribute("page", page);

		if (response == null) {

			Map<String, Object> map = new HashMap<String, Object>();
			if (user.isAdminOrManager() == false
					&& user.isOperateDirectorOrManager() == false) {
				map.put("userId", user.getUserId());
			}
			page.setCondition(map);
			// 先查用户权限范围内所有顶级广告主
			firstPartnerList = partnerDao.operatorPageListMapByMap(page);
		} else {
			firstPartnerList = partnerDao.listMapByMap(paraMap);
		}

		if (firstPartnerList != null) {

			// 加载顶级广告主的所有下级
			ArrayList<Map<String, Object>> allPartnerList = new ArrayList<Map<String, Object>>();
			for (Map<String, Object> partner : firstPartnerList) {
				long partnerId = (long) partner.get("id");
				List<Map<String, Object>> partnerHierachyList = listPartnerHierarchy(
						partnerId, false);
				allPartnerList.add(partner);
				allPartnerList.addAll(partnerHierachyList);
			}

			// 根据查询条件筛选，决定最后要显示出来的广告主
			ArrayList<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			for (Map<String, Object> partner : allPartnerList) {
				List<QualiDoc> partnerQualiList = qualiDocDao
						.listByPartnerId((long) partner.get("id"));
				int internalAuditStatus = 0;
				if (partnerQualiList != null && partnerQualiList.size() > 0) {
					QualiDoc doc = partnerQualiList.get(0);
					internalAuditStatus = doc.getStatus();
				}
				partner.put("internal_audit_status", internalAuditStatus);
			}
			
			for (Map<String, Object> partner : allPartnerList) {
				if (filter(partner, allPartnerList, paraMap)) {
					resultList.add(partner);
				}
			}

			// 处理广告主的扩展信息
			processPartnerExtendInfo(paraMap, resultList, true, false);

			if (response == null) {
				page.setListMap(resultList);
			}
		}
	}

	/**
	 * 
	 * @param partner
	 * @param allPartnerList
	 * @param paraMap
	 * @return
	 */
	private boolean filter(Map<String, Object> partner,
			List<Map<String, Object>> allPartnerList,
			Map<String, Object> paraMap) {

		if (partner == null) {
			return false;
		}

		long partnerId = partner.get("id") != null ? (long) partner.get("id")
				: -1;
		String partner_partnerName = partner.get("partner_name") != null ? (String) partner
				.get("partner_name") : "";
		int internal_audit_status = partner.get("internal_audit_status") != null ? (int) partner
				.get("internal_audit_status") : -1;
		if (partnerId == 293) {
			System.out.println("qqqqqq");
		}

		/**
		 * ---------- 看partner是否满足条件，如果满足则直接返回 -------------------
		 */
		boolean b = true;
		if (paraMap.get("partnerName") != null) {
			String partnerName = (String) paraMap.get("partnerName");
			if (partner_partnerName.indexOf(partnerName) < 0) {
				b = false;
			}
		}
		if (paraMap.get("internalAuditStatus") != null) {
			int internalAuditStatus = (int) paraMap.get("internalAuditStatus");
			if (internal_audit_status != internalAuditStatus) {
				b = false;
			}
		}
		if (b) {
			partner.put("isTarget", "1");
			return true;
		}
		/**
		 * ---------- 看partner是否满足条件，如果满足则直接返回 end -------------------
		 */

		/**
		 * ---------- partner不满足条件，找到partner的下级
		 * 或下级的下级有没有符合条件的-------------------
		 */
		ArrayList<Map<String, Object>> sublist = new ArrayList<Map<String, Object>>();

		for (Map<String, Object> subPartner : allPartnerList) {

			long pid = (long) subPartner.get("pid");

			if (pid == partnerId) {
				sublist.add(subPartner);
			}

		}

		for (Map<String, Object> subPartner : sublist) {

			if (filter(subPartner, allPartnerList, paraMap)) {
				return true;
			}

		}
		/**
		 * ---------- partner不满足条件，找到partner的下级 或下级的下级有没有符合条件的 end
		 * -------------------
		 */

		return false;
	}

	/**
	 * 为资质列表处理广告主的扩展信息
	 * 
	 * @param paraMap
	 * @param partnerList
	 * @param needLastSumRow
	 * @param pvNeedFormat
	 */
	private void processPartnerExtendInfo(Map<String, Object> paraMap,
			List<Map<String, Object>> partnerList, boolean needLastSumRow,
			boolean pvNeedFormat) {

		/**
		 * 每个广告主的基本信息
		 */
		for (Map<String, Object> partnerMap : partnerList) {
			// 处理展示信息
			long partnerId = (long) partnerMap.get("id");

			// 如果广告主不是【jtd公司】，就查询当前partner的运营人员
			Map<String, Object> operatorMap = userPartnerDao
					.findByPartnerAndRole(partnerId, RoleType.OPERATE);

			if (operatorMap != null) {
				// 把运营人员信息放到map中
				partnerMap.put("operator_name", operatorMap.get("user_name"));
			}

			// 查询当前广告主是否有下级（以便判断是否显示折叠图标）
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("pid", partnerId);
			List<Map<String, Object>> partnerLevel = partnerDao
					.listMapByMap(params);
			if (partnerLevel != null && partnerLevel.size() > 0) {
				partnerMap.put("hasChildren", "1");
			}

			// 读取行业信息
			ChannelCategory cc = partnerService.getPartnerCategory(partnerId);
			if (cc != null) {
				partnerMap.put("category_name", cc.getName());
			}

			/*
			 * 开始查找内部审核状态
			 */
			List<QualiDoc> partnerQualiList = qualiDocDao
					.listByPartnerId(partnerId);

			int qualiDocCount = 0; // 资质文件数量
			int internalAuditStatus = 0; // 内部审核状态默认为未提交
			String updateTime = "";
			String internalAuditTime = "";
			if (partnerQualiList != null && partnerQualiList.size() > 0) {

				qualiDocCount = partnerQualiList.size();

				QualiDoc doc0 = partnerQualiList.get(0);

				Date d1 = doc0.getUpdateTime(); // 资质更新时间
				Date d2 = doc0.getInternalAuditTime(); // 内部审核时间
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyy-MM-dd HH:mm:ss");
				if (d1 != null) {
					updateTime = sdf.format(d1);
				}
				if (d2 != null) {
					internalAuditTime = sdf.format(d2);
				}

				internalAuditStatus = doc0.getStatus();
			}

			// 如果页面上有内部审核状态的查询条件，则要符合查询条件才能继续进行
			// if (paraMap.get("internalAuditStatus") != null) {
			// if (internalAuditStatus != (int) paraMap
			// .get("internalAuditStatus")) {
			// continue;
			// }
			// }

			/**
			 * 开始查找广告主的渠道审核状态
			 */
			int notCommitCount = 0;
			int successCount = 0;
			int failCount = 0;
			int waitCount = 0;

			List<Map<String, Object>> partnerStatusList = partnerStatusDao
					.listAllChannelAuditStatusByPartnerId(partnerId);

			// int adxAuditStatus = 0; // 渠道审核状态默认为全部未提交

			if (partnerStatusList != null && partnerStatusList.size() > 0) {

				for (Map<String, Object> partnerStatusMap : partnerStatusList) {

					if (partnerStatusMap.get("status") == null) {
						notCommitCount++;
					} else {

						int status = (int) partnerStatusMap.get("status");

						if (status == AuditStatus.STATUS_WAIT.getCode()) {
							waitCount++;
						}
						if (status == AuditStatus.STATUS_SUCCESS.getCode()) {
							successCount++;
						}
						if (status == AuditStatus.STATUS_FAIL.getCode()) {
							failCount++;
						}
					}

				}
				partnerMap.put("quali_doc_count", qualiDocCount);
				partnerMap.put("update_time", updateTime);
				partnerMap.put("internal_audit_time", internalAuditTime);
				partnerMap.put("internal_audit_status", internalAuditStatus);
				partnerMap.put("not_commit_count", notCommitCount);
				partnerMap.put("success_count", successCount);
				partnerMap.put("fail_count", failCount);
				partnerMap.put("wait_count", waitCount);

			}
		}
	}

	@Override
	public List<Map<String, Object>> listPartnerHierarchy(Long partnerId,
			boolean needSelf) throws PlatformException {

		CustomerContextHolder
				.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		if (needSelf) {
			Map<String, Object> self = partnerDao.getMapById(partnerId);
			resultList.add(self);
		}

		listPartnerHierarchy_LoadFunc(partnerId, resultList, 1);

		return resultList;
	}

	/**
	 * 递归加载partner的后代
	 *
	 * @param partnerId
	 * @param resultList
	 * @param level
	 */
	private void listPartnerHierarchy_LoadFunc(long partnerId,
			List<Map<String, Object>> resultList, int level) {

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("pid", partnerId);

		List<Map<String, Object>> descendantPartnerList = partnerDao
				.listMapByMap(param);

		if (descendantPartnerList != null && descendantPartnerList.size() > 0) {

			for (Map<String, Object> partner : descendantPartnerList) {

				// logger.info("读取到" + level + "级partner[" + partner.getId() +
				// "]，属于[" + partnerId + "]");

				resultList.add(partner);

				long subPartnerId = (long) partner.get("id");

				listPartnerHierarchy_LoadFunc(subPartnerId, resultList,
						level + 1);

			}

		}
	}

	@Override
	public List<Map<String, Object>> findChildren(Map<String, Object> paraMap) {

		CustomerContextHolder
				.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

		ArrayList<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> childrenList = partnerDao.listMapByMap(paraMap);
		for (Map<String, Object> partner : childrenList) {
			List<QualiDoc> partnerQualiList = qualiDocDao
					.listByPartnerId((long) partner.get("id"));
			int internalAuditStatus = 0;
			if (partnerQualiList != null && partnerQualiList.size() > 0) {
				QualiDoc doc = partnerQualiList.get(0);
				internalAuditStatus = doc.getStatus();
			}
			partner.put("internal_audit_status", internalAuditStatus);
		}
		
		for (Map<String, Object> partner : childrenList) {
			if (filter(partner, childrenList, paraMap)) {
				resultList.add(partner);
			}
		}
		
		
		if (resultList != null && resultList.size() > 0) {
			processPartnerExtendInfo(paraMap, resultList, false, true);
		}

		return resultList;
	}

	@Override
	public HashMap<String, Object> toAudit(long partnerId)
			throws PlatformException {

		CustomerContextHolder
				.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

		Partner partner = null;

		try {
			partner = partnerService.getById(partnerId);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("detail发生错误");
			throw new PlatformException(
					PlatformException.DATABASE_ERROR_MESSAGE);
		}

		List<Map<String, Object>> partnerQualiList = null;
		List<Map<String, Object>> partnerChannelStatusList = null;
		try {

			partnerQualiList = qualiDocDao.listMapByPartnerId(partner.getId());
			partnerChannelStatusList = partnerStatusDao
					.listAllChannelAuditStatusByPartnerId(partner.getId());

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("toAudit发生错误");
			throw new PlatformException(
					PlatformException.DATABASE_ERROR_MESSAGE);
		}

		int internalAuditStatus = 0; // 未提交
		String remark = "";
		int auditMqSuccess = 0; // mq未发送
		if (partnerQualiList != null && partnerQualiList.size() > 0) {

			Map<String, Object> doc0 = partnerQualiList.get(0);
			internalAuditStatus = (int) doc0.get("status");
			remark = (String) doc0.get("remark");
			auditMqSuccess = (int) doc0.get("audit_mq_success");
		}

		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("partner", partner);
		resultMap.put("internalAuditStatus", internalAuditStatus);
		resultMap.put("remark", remark);
		resultMap.put("auditMqSuccess", auditMqSuccess);
		resultMap.put("partnerQualiList", partnerQualiList);
		resultMap.put("partnerStatusList", partnerChannelStatusList);

		return resultMap;
	}

	@Override
	public void saveAudit(long auditUserId, long partnerId,
			int internalAuditStatus, String internalRejectRemark) {

		CustomerContextHolder
				.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

		HashMap<String, Object> map = new HashMap<String, Object>();

		int auditMqSuccess = 0;
		if (internalAuditStatus == AuditStatus.STATUS_SUCCESS.getCode()) {
			// 发送内部审核MQ消息
			try {
				mqConnectorService.sendAuditPartnerMessage(partnerId, null,
						AuditStatus.STATUS_SUCCESS);

				auditMqSuccess = 1;

			} catch (Exception e) {
				logger.error(
						"AdminQualiDocService------saveAudit---发送内部审核MQ消息发生错误，partnerId="
								+ partnerId, e);
			}
		}

		map.put("partnerId", partnerId);
		map.put("auditUserId", auditUserId);
		map.put("internalAuditTime", new Date());
		map.put("status", internalAuditStatus);
		map.put("remark", internalRejectRemark);
		map.put("auditMqSuccess", auditMqSuccess);

		qualiDocDao.updateStatus(map);

		// 向adx提交
		if (internalAuditStatus == AuditStatus.STATUS_SUCCESS.getCode()) {

			submitToChannel(auditUserId, partnerId, null);

		}

	}

	/**
	 * 向adx提交广告主信息及资质，如果资质已经审批通过则不再提交
	 * 
	 * @param partnerId
	 */
	public void submitToChannel(long submitUserId, long partnerId,
			Long channelId) {

		CustomerContextHolder
				.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

		Channel channelParam = new Channel();
		// 如果指定了渠道id，则渠道列表只会查到一条记录
		if (channelId != null) {
			channelParam.setId(channelId);
		}

		List<Channel> channelList = channelDao.listBy(channelParam);

		for (Iterator<Channel> i = channelList.iterator(); i.hasNext();) {

			Channel channel = (Channel) i.next();

			CatgSerial channelEnum = CatgSerial.fromChannelId(channel.getId());

			// 先遍历一下看看partner是否已经在当前平台提交了，并且状态是已通过或者待审核，则可以跳过
			boolean needPostChannel = true;
			// 是否存在之前审核记录
			boolean hasPostedChannel = false;

			// 查询广告主在当前adx是否有提交记录
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("partnerId", partnerId);
			params.put("channelId", channel.getId());
			List<PartnerStatusPO> partnerStatusList = partnerStatusDao
					.listByMap(params);

			if (partnerStatusList != null && partnerStatusList.size() > 0) {

				PartnerStatusPO ps = partnerStatusList.get(0);

				if (ps.getChannelId() == channel.getId()) {

					hasPostedChannel = true;

					// 状态是已通过则不再提交
					if (ps.getStatus() == AuditStatus.STATUS_SUCCESS.getCode()) {
						needPostChannel = false;
					}
				}
			}

			if (needPostChannel == false) {
				continue;
			}

			int result = -1;
			
			// 如果没有配置忽略adx审核，则开始提交adx
			if (Constants.IS_IGNORE_ADX && channelEnum != CatgSerial.HZ) {
				result = 100;
			}
			else{
				switch(channelEnum){
				case BES :
					
					result = baiduAPIService.syncAdvertiser(submitUserId,
							partnerId, "");

					logger.info("AdminQualiDocService------sumbitToChannel---bes---result=="
							+ result);
					
					break ;

				case HZ :
					
					result = hzengAPIService.syncAdvertiser(submitUserId, partnerId) ? 0 : -1 ;
					
					logger.info("AdminQualiDocService------sumbitToChannel----hzeng---result=="
							+ result);
				
					break ;
				
				case VAM :
					
					result = 100;

					logger.info("AdminQualiDocService------sumbitToChannel----vam---result=="
							+ result);
					
					break ;
					
				case XTRADER :
					
					result = 100;

					logger.info("AdminQualiDocService------sumbitToChannel----xtrader---result=="
							+ result);
					
					break ;
					
				default :
					break ;
				}
			}

			int auditStatus = -1;
			String auditInfo = "";
			int auditMqSuccess = 0;

			// result为0则表示提交成功，则开始往partner_status表插入/修改数据
			if (result == 0) {
				auditStatus = AuditStatus.STATUS_WAIT.getCode();
				auditInfo = "待审核";
			} else if (result == 100) {
				auditStatus = AuditStatus.STATUS_SUCCESS.getCode();
				auditInfo = "审核通过";
			} else {
//				auditStatus = AuditStatus.STATUS_COMMIT_FAIL.getCode();
//				auditInfo = "提交失败";
				auditStatus = AuditStatus.STATUS_NOTCOMMIT.getCode();
				auditInfo = "未提交";
			}

			// 向MQ发送广告主提交渠道的消息
			if (auditStatus == AuditStatus.STATUS_WAIT.getCode()
					|| auditStatus == AuditStatus.STATUS_SUCCESS.getCode()) {
				try {
					mqConnectorService.sendPartnerCommitChannelMessage(
							partnerId, channelEnum);
					// 发送mq成功
					auditMqSuccess = 1;
				} catch (Exception e) {
					logger.error("AdminQualiDocService------sumbitToChannel-----发送提交渠道审核MQ发生错误,partnerId[id="
							+ partnerId
							+ "],channel[id="
							+ channel.getId()
							+ "],[status=" + auditStatus + "]");
				}
			}

			// 如果存在之前在当前渠道的审核记录则修改
			if (hasPostedChannel) {

				logger.info("AdminQualiDocService------sumbitToChannel-----update , partnerId[id="
						+ partnerId
						+ "],channel[id="
						+ channel.getId()
						+ "],[status=" + auditStatus + "]");

				// 将审核状态重新置为待审核
				partnerStatusDao.updateStatus(auditStatus, auditInfo,
						submitUserId, partnerId, channel.getId(),
						auditMqSuccess);

			}
			// 否则新增一条当前渠道的审核记录
			else {

				logger.info("AdminQualiDocService------sumbitToChannel-----insert , partnerId[id="
						+ partnerId
						+ "],channel[id="
						+ channel.getId()
						+ "],[status=" + auditStatus + "]");

				PartnerStatusPO ps = new PartnerStatusPO();
				ps.setPartnerId(partnerId); // 广告主id
				ps.setChannelId(channel.getId()); // 当前渠道id
				ps.setStatus(auditStatus); // 待审核状态
				ps.setAuditInfo(auditInfo); // 审核信息
				ps.setUpdateTime(new Date()); // 更新时间
				ps.setCreateTime(new Date()); // 创建时间
				ps.setSubmitUserId(submitUserId); // 提交审核人
				ps.setAuditMqSuccess(auditMqSuccess);

				partnerStatusDao.insert(ps);

			}
		}

	}

	/**
	 * 重新发送渠道审核相关MQ
	 * 
	 * @param partnerId
	 * @param channelId
	 */
	public void sendChannelAuditMQ(long partnerId, Long channelId) {

		CustomerContextHolder
				.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

		// 查询广告主在当前adx是否有提交记录
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("partnerId", partnerId);
		params.put("channelId", channelId);
		List<PartnerStatusPO> partnerStatusList = partnerStatusDao
				.listByMap(params);

		if (partnerStatusList != null && partnerStatusList.size() > 0) {

			PartnerStatusPO ps = partnerStatusList.get(0);

			CatgSerial channelEnum = CatgSerial.fromChannelId(channelId);

			// 只要不是上传失败，就要发送广告主已经向ADX提交审核的MQ
			if (ps.getStatus() != AuditStatus.STATUS_COMMIT_FAIL.getCode()) {

				int auditMqSuccess = 0;
				try {
					// 向MQ发送广告主提交渠道的消息
					mqConnectorService.sendPartnerCommitChannelMessage(
							partnerId, channelEnum);

					// 向MQ发送广告主审核渠道状态变更的消息
					mqConnectorService.sendAuditPartnerMessage(partnerId,
							channelEnum,
							AuditStatus.getAuditStatus(ps.getStatus()));

					auditMqSuccess = 1;

				} catch (Exception e) {
					logger.error("AdminQualiDocService------sendChannelAuditMQ-----发送提交渠道审核MQ发生错误,partnerId[id="
							+ partnerId
							+ "],channel[id="
							+ channelId
							+ "],[status=" + ps.getStatus() + "]");
				}

				// 更新MQ发送状态
				partnerStatusDao.updateStatus(null, null, null, partnerId,
						channelId, auditMqSuccess);
			}

		}

	}

	/**
	 * 重新发送广告主资质内部审核相关MQ
	 * 
	 * @param partnerId
	 * @param channelId
	 */
	public void sendInternalAuditMQ(long partnerId) {

		int auditMqSuccess = 0;
		// 发送内部审核MQ消息
		try {
			mqConnectorService.sendAuditPartnerMessage(partnerId, null,
					AuditStatus.STATUS_SUCCESS);

			auditMqSuccess = 1;

		} catch (Exception e) {
			logger.error(
					"AdminQualiDocService------saveAudit---发送内部审核MQ消息发生错误，partnerId="
							+ partnerId, e);
		}

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("partnerId", partnerId);
		map.put("auditMqSuccess", auditMqSuccess);

		qualiDocDao.updateStatus(map);

	}

	@Override
	public QualiDoc getById(long id) {

		CustomerContextHolder
				.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

		return qualiDocDao.getById(id);
	}

}
