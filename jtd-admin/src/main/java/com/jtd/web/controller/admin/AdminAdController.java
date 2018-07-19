package com.jtd.web.controller.admin;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.jtd.commons.page.Pagination;
import com.jtd.utils.DateUtil;
import com.jtd.web.constants.AuditStatus;
import com.jtd.web.constants.CatgSerial;
import com.jtd.web.constants.Constants;
import com.jtd.web.constants.RoleType;
import com.jtd.web.controller.BaseController;
import com.jtd.web.controller.DicController;
import com.jtd.web.jms.AuditAdMsg;
import com.jtd.web.jms.CommitAdMsg;
import com.jtd.web.po.*;
import com.jtd.web.service.IMQConnectorService;
import com.jtd.web.service.admin.*;
import com.jtd.web.service.adx.bes.BaiduAPIService;
import com.jtd.web.vo.ChannelCategory;

import static com.jtd.utils.DateUtil.getHhMmSs;

/**
 * 
 * @作者 duber
 * @版本 V1.0
 * @创建日期 2016年10月28日
 * @描述 后台创意管理action
 */
@Controller
@RequestMapping("/admin/ad")
public class AdminAdController extends BaseController {

	public static final String PAGE_DIRECTORY = "/admin/ad/";
	public static final String ACTION_PATH = "redirect:/admin/ad/";

	@Autowired
	private IAdminAdService adminAdService;

	@Autowired
	private IAdminAdCategoryService adminAdCategoryService;

	@Autowired
	private IAdminCampaignDimService adminCampaignDimService;

	@Autowired
	private BaiduAPIService baiduAPIService;

	@Autowired
	private IAdminPartnerService adminPartnerService;

	@RequestMapping("/list")
	public String list(Model model, Integer pageNo,Integer pageSize,HttpServletRequest request) {

		// Shiro用户信息
		setUserPageInfo(model);

		HashMap<String, Object> paraMap = new HashMap<String, Object>();
		if (StringUtils.isEmpty(request.getParameter("partnerId")) == false) {
			paraMap.put("partnerId", request.getParameter("partnerId"));
		}
		if (StringUtils.isEmpty(request.getParameter("partnerName")) == false) {
			paraMap.put("partnerName", request.getParameter("partnerName"));
		}
		if (StringUtils.isEmpty(request.getParameter("campaignId")) == false) {
			paraMap.put("campaignId", request.getParameter("campaignId"));
		}
		if (StringUtils.isEmpty(request.getParameter("campaignName")) == false) {
			paraMap.put("campaignName", request.getParameter("campaignName"));
		}
		if (StringUtils.isEmpty(request.getParameter("internalAuditStatus")) == false) {
			paraMap.put("internalAuditStatus", Integer.parseInt(request
					.getParameter("internalAuditStatus")));
		}

		if (StringUtils.isEmpty(request.getParameter("internalAuditStartDate")) == false) {
			paraMap.put("internalAuditStartDate",
					request.getParameter("internalAuditStartDate"));
		}
		if (StringUtils.isEmpty(request.getParameter("internalAuditEndDate")) == false) {
			paraMap.put("internalAuditEndDate",
					request.getParameter("internalAuditEndDate"));
		}

		if (StringUtils.isEmpty(request.getParameter("updateStartDate")) == false) {
			paraMap.put("updateStartDate",
					request.getParameter("updateStartDate"));
		}
		if (StringUtils.isEmpty(request.getParameter("updateEndDate")) == false) {
			paraMap.put("updateEndDate", request.getParameter("updateEndDate"));
		}
		
		model.addAttribute("queryMap", paraMap);

		ActiveUser activeUser = getUserInfo();
        List<Partner> partners = adminPartnerService.findPartnerListByLoginUser(activeUser);

        if(partners.size()>0){
            String partnerIds = "";
            for (Partner po:partners){
                partnerIds += po.getId() + ",";
            }
            if (!StringUtils.isEmpty(partnerIds)) {
                partnerIds = partnerIds.substring(0, partnerIds.length() - 1);
                paraMap.put("partnerIds", partnerIds);
            }
        }

        pageSize = null == pageSize ? 50 : pageSize;
		Pagination<Map<String, Object>> page = adminAdService.findMapPageBy(paraMap, pageNo, pageSize);
		model.addAttribute("page", page);
		return PAGE_DIRECTORY + "admin_ad_list";
	}

	@RequestMapping("/adAuditSet")
	public String adAuditSet(Model model, String id) {
		// Shiro用户信息
		setUserPageInfo(model);
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("id", id);
		// 创意详情
		Map<String, Object> sp = adminAdService.findAdMapById(paraMap);
		model.addAttribute("po", sp);
		// 创意下面的渠道审核状态
		Map<String, Object> pMap = new HashMap<String, Object>();
		pMap.put("id", id);

		CampaignDim cd = new CampaignDim();
		cd.setCampaignId(Long.parseLong(sp.get("campaign_id").toString()));
		cd.setDimName("channels");
		CampaignDim campDim = adminCampaignDimService.selectByCampAndDimName(cd);

		String dimValue = campDim.getDimValue();
		JSONObject jsonObj = JSONObject.parseObject(dimValue);
		String selectVal = jsonObj.getString("selected");
		String typeVal = jsonObj.getString("type");
		if ("1".equals(typeVal)) {
			selectVal = selectVal.substring(1, selectVal.length() - 1);
			String[] ids = selectVal.split(",");
			pMap.put("channel_ids", ids);
		}
		List<Map<String, Object>> listAAS = adminAdCategoryService.selectChannelCatgByAdId(pMap);
		model.addAttribute("listAAS", listAAS);
		// 活动下面渠道设置的行业类型基础数据
		Map<CatgSerial, List<ChannelCategory>> channelDIC = DicController.getChannelCategorysList();
		model.addAttribute("dspChannelDIC", channelDIC.get(CatgSerial.DSP));
		model.addAttribute("tanxChannelDIC", channelDIC.get(CatgSerial.TANX));
		model.addAttribute("besChannelDIC", channelDIC.get(CatgSerial.BES));
		model.addAttribute("vamChannelDIC", channelDIC.get(CatgSerial.VAM));
        model.addAttribute("tencentChannelDIC", channelDIC.get(CatgSerial.TENCENT));
        model.addAttribute("xtraderChannelDIC", channelDIC.get(CatgSerial.XTRADER));
        model.addAttribute("hzengChannelDIC", channelDIC.get(CatgSerial.HZ));

		return PAGE_DIRECTORY + "admin_ad_audit";
	}

	/**
	 * 创意更新
	 *
	 * @param model
	 * @param user
	 * @return
	 */
	@RequestMapping("/update")
	@ResponseBody
	public Map<String, Object> update(Model model, @RequestBody Ad ad) {
		adminAdService.update(ad);
		if (ad.getInternalAudit() == 1) { // 只有审核通过时才同步到渠道
			if(Constants.IS_IGNORE_ADX == false){
				baiduAPIService.syncCreative(ad.getId()); // 同步到引擎
			}
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("url", "list.action");
		resultMap.put("success", true);
		return resultMap;
	}

	@RequestMapping("/adAuditUpdate")
	@ResponseBody
	public Map<String, Object> AdAuditUpdate(Model model, CampaignDim cd, Long adId, int internalAudit,String internalAuditInfo) {

//		System.out.println("----开始----"+DateUtil.getHhMmSs(new Date()));

		long campaign_id = cd.getCampaignId();
		String channelIds = cd.getDimValue();

		adminAdService.saveAuditAdx(campaign_id,channelIds,adId,internalAudit);

		Ad ad = new Ad();
		ad.setId(adId);
		ad.setInternalAudit(internalAudit);
		ad.setInternalAuditInfo(internalAuditInfo);
		adminAdService.update(ad);

//		System.out.println("----结束----"+DateUtil.getHhMmSs(new Date()));
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("url", "list.action");
		resultMap.put("success", true);
		return resultMap;
	}

	/**
	 * 创意批量审核
	 * @param model
	 * @param ad_ids
	 * @return
	 */
	@RequestMapping("/batchAdAudit")
	@ResponseBody
	public Map<String,Object> batchAdAudit(Model model,String ad_ids){

		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(!StringUtils.isEmpty(ad_ids)) {
			adminAdService.batchAdAudit(ad_ids);
			resultMap.put("url", "list.action");
			resultMap.put("success", true);
		}else {
			resultMap.put("success", false);
		}

        return resultMap;
    }
}
