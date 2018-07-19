package com.jtd.web.controller.front;

import com.alibaba.fastjson.JSONObject;
import com.jtd.commons.exception.PlatformException;
import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.commons.page.Pagination;
import com.jtd.web.constants.*;
import com.jtd.web.controller.BaseController;
import com.jtd.web.po.*;
import com.jtd.web.service.front.*;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 投放管理
 * @作者 duber
 * @版本 V1.0
 * @创建日期 2016年11月4日
 * @描述
 */
@Controller
@RequestMapping("/front/campManage")
public class CampManageController extends BaseController {

	public static final String PAGE_DIRECTORY = "/front/campmanage/";
	public static final String ACTION_PATH = "redirect:/front/campmanage/";

	@Autowired
	private IFrontCampGroupService frontCampGroupService;

	@Autowired
	private IFrontCampService frontCampService;

	@Autowired
	private IFrontCampDimService frontCampDimService;  // 活动投放策略

	@Autowired
	private IFrontCreativeService frontCreativeService;

	@Autowired
	private IFrontCreativeGroupService frontCreativeGroupService;

	@Autowired
	private IFrontAdService frontAdService;

	@Autowired
	private IFrontAdCategoryService adCategoryService;

	@Autowired
	private IFrontTopDomainService frontTopDomainService;

	@Autowired
	private IFrontSysLogService sysLogService;

	private HashMap<String, Object> getParamMap(HttpServletRequest request) {

		HashMap<String, Object> params = new HashMap<String, Object>();

		// 活动名称
		if (StringUtils.isEmpty(request.getParameter("campaignName")) == false) {
			params.put("campaignName", request.getParameter("campaignName"));
		}
		// 活动类型
		if (StringUtils.isEmpty(request.getParameter("campaignType")) == false) {
			String campType = request.getParameter("campaignType");
			params.put("campaignType", campType);
		}
		// 活动状态
		if (StringUtils.isEmpty(request.getParameter("campaignStatus")) == false) {
			params.put("campaignStatus", request.getParameter("campaignStatus"));
		}
		// 开始日期
		if (StringUtils.isEmpty(request.getParameter("startDate")) == false) {
			params.put("startDate", request.getParameter("startDate"));
		}else{
			// 日期默认查最近七天
//			Calendar c = Calendar.getInstance();
//			c.set(Calendar.DATE, c.get(Calendar.DATE) - 6);
//			params.put("startDate",
//					DateUtil.getDateStr(c.getTime(), "yyyy-MM-dd"));
//			params.put("startDateNum", DateUtil.getDateInt(c.getTime()));
		}

		// 结束日期(放到统计库campd表中查询)
		if (StringUtils.isEmpty(request.getParameter("endDate")) == false) {
			params.put("endDate", request.getParameter("endDate"));
		}else {
			// 日期默认查最近七天
//			params.put("endDate", DateUtil.getDateStr(new Date(), "yyyy-MM-dd"));
//			params.put("endDateNum", DateUtil.getDateInt(new Date()));
		}

		return params;

	}


	@RequestMapping("/camp_list")
	public String list(Model model,Integer pageNo,Integer pageSize,HttpServletRequest request){

		// Shiro用户信息
		setUserPageInfo(model);

		//查询参数封装
		Map<String,Object> paraMap = getParamMap(request);
		model.addAttribute("paraMap",paraMap);

		//分页查询活动列表
		ActiveUser activeUser = getUserInfo();
		paraMap.put("partnerId",activeUser.getFavPartner().getId());
		paraMap.put("deleteStatus",0);
		Pagination<Campaign> page = frontCampService.findFrontCampListPageBy(paraMap,pageNo, pageSize);
		model.addAttribute("page",page);

		//活动状态列表
		EnumSet<CampaignStatus> campaignStatusList = EnumSet.allOf(CampaignStatus.class);
		model.addAttribute("campaignStatusList",campaignStatusList);

		//活动类型列表
		EnumSet<CampaignType> campaignTypeList = EnumSet.allOf(CampaignType.class);
		model.addAttribute("campaignTypeList",campaignTypeList);

		return PAGE_DIRECTORY+"camp_list";
	}

	@RequestMapping("/exportCampaign")
	public void exportCampaign(Model model,HttpServletRequest request,HttpServletResponse response) throws IOException{
		//查询参数封装
		Map<String,Object> paraMap = getParamMap(request);
		ActiveUser activeUser = getUserInfo();
		paraMap.put("partnerId",activeUser.getFavPartner().getId());
		List<Map<String,Object>> listCampaigns = frontCampService.getCampaignsBy(paraMap);
		if(listCampaigns == null){

		}else{

			frontCampService.exportCampaign(listCampaigns,response);

		}
	}



	@RequestMapping("/duplicateCampaign")
	@ResponseBody
	public Map<String,Object> duplicateCampaign(Model model,Long campaignId) {
		// Shiro用户信息
		setUserPageInfo(model);
		ActiveUser activeUser = getUserInfo();
		frontCampService.saveDuplicateCampaign(activeUser,campaignId);

		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("url", "camp_list.action");
		resultMap.put("success", true);
		return resultMap;
	}


	@RequestMapping("/isDelivery")
	@ResponseBody
	public Map<String,Object> isDelivery(Model model,String camp_ids){
		// Shiro用户信息
		setUserPageInfo(model);
		ActiveUser activeUser = getUserInfo();
        Map<String, Object> resultMap = new HashMap<String,Object>();
        if(activeUser.getFavPartner().getAccBalance().intValue() >= Constants.CAMP_LOW_ACCOUNT) /** 广告主账户必须大于10000分 */
        {
            String[] campaignIds = camp_ids.split(",");
            resultMap = frontCampService.isDelivery(activeUser, campaignIds);
        }else{
            resultMap.put("message", "广告主余额不足,请先充值再投放广告!");
            resultMap.put("fail", true);
            resultMap.put("success", false);
        }
		return resultMap;
	}

	/**
	 * 
	 * @param model
	 * @param camp_ids 选中的活动ID数组
	 * @param code 活动修改状态
	 * @return
	 */
	@RequestMapping("/changeStatus")
	@ResponseBody
	public Map<String,Object> changeStatus(Model model,String camp_ids,int code ){

		// Shiro用户信息
		setUserPageInfo(model);
		String[] campaignIds = camp_ids.split(",");
		String retCamp = frontCampService.changeCampaignManulStatus(campaignIds,CampaignManulStatus.fromCode(code));

		Map<String,Object> resultMap = new HashMap<String,Object>();
		if(StringUtils.isEmpty(retCamp)){
			resultMap.put("url", "camp_list.action");
			resultMap.put("success", true);
		}else{
			resultMap.put("message", "活动ID:["+retCamp+"]下面的广告未审核或审核未通过,请联系管理员审核");
			resultMap.put("success", false);
		}

		return resultMap;
	}

	@RequestMapping("/deleteCampaign")
	@ResponseBody
	public Map<String,Object> deleteCampaign(Model model,String camp_ids){

		// Shiro用户信息
		setUserPageInfo(model);
		String[] campaignIds = camp_ids.split(",");
		frontCampService.deleteCampaigns(campaignIds);

		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("url", "camp_list.action");
		resultMap.put("success", true);
		return resultMap;
	}


	/**
	 * 活动列表: 批量设置
	 * @param model
	 * @param camp_ids
	 * @param dailyBudget
	 * @param price
	 * @return
	 */
	@RequestMapping("/batchUpdateCampaigns")
	@ResponseBody
	public Map<String,Object> batchUpdateCampaigns(Model model,String camp_ids,String price,String dailyBudget){

		// Shiro用户信息
		setUserPageInfo(model);
		Map<String,Object> resultMap = new HashMap<String,Object>();
		if(camp_ids.length()==0){
			resultMap.put("success", false);
			return resultMap;
		}
		String[] campaignIds = camp_ids.split(",");
		Integer i_price = 0;
		if( !StringUtils.isEmpty(price) ){
			Double d_price = Double.parseDouble(price)*100;
			i_price = Integer.parseInt(new java.text.DecimalFormat("0").format(d_price));
		}
		Long dailyBudgetLong = null;
		if ( !StringUtils.isEmpty(dailyBudget) ) {
			try {
				dailyBudgetLong = new Double((Double.valueOf(dailyBudget) * 100)).longValue();
			} catch (NumberFormatException e) {
				throw new PlatformException("每日预算的值[" + dailyBudget + "]错误");
			}
		}
		frontCampService.batchUpdateCampaigns(campaignIds, i_price, dailyBudgetLong);

		resultMap.put("url", "camp_list.action");
		resultMap.put("success", true);
		return resultMap;
	}


	@RequestMapping("/listMedia")
	@ResponseBody
	public  Map<String,Object> listMedia(Model model,Integer pageNo,String camp_id,Integer pageSize,String domain,String webSiteType,String flow,String manualEntry){

		Map<String,Object> resultMap = new HashMap<String,Object>();
		// Shiro用户信息
		setUserPageInfo(model);

        //已选定向域名
		CampaignDim cd = new CampaignDim();
		cd.setCampaignId(Long.parseLong(camp_id));
		cd.setDimName("media");
		cd =frontCampDimService.selectByCampAndDimName(cd);

		if(cd !=null){
			String dimValue= cd.getDimValue();
			JSONObject jsonObj = JSONObject.parseObject(dimValue);

			String typeVal = jsonObj.getString("type");
			resultMap.put("type", typeVal);
			if("2".equals(typeVal)){

				String selectVal = jsonObj.getString("webDomain");
				selectVal = selectVal.substring(1, selectVal.length()-1);
				String[] ids = selectVal.split(",");
				resultMap.put("media_selected", ids);

			}else if("1".equals(typeVal)){
				String selectVal = jsonObj.getString("websiteType");
				selectVal = selectVal.substring(1, selectVal.length()-1);
				String[] ids = selectVal.split(",");
				resultMap.put("media_type", ids);
			}
        }

        // 查询媒体域名
        Map<String,Object> paraMap = new HashMap<String,Object>();

        if(!StringUtils.isEmpty(domain)) {
            paraMap.put("domain", domain);
        }
        if(!StringUtils.isEmpty(webSiteType)){
            paraMap.put("webSiteType", webSiteType);
        }
        if(!StringUtils.isEmpty(flow)){
            long flow_min = 0;
            long flow_max = 500;
            switch (flow){
                case "5":
                    flow_min = 5;
                    flow_max = 10;
                    break;
                case "10":
                    flow_min = 10;
                    flow_max = 50;
                    break;
                case "50":
                    flow_min = 50;
                    flow_max = 100;
                    break;
                case "100":
                    flow_min = 100;
                    flow_max = 500;
                    break;
            }

            paraMap.put("flow_min", flow_min*10000);
            paraMap.put("flow_max", flow_max*10000);
		}
        if(!StringUtils.isEmpty(manualEntry)){
            paraMap.put("manualEntry", manualEntry);
        }
        CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
        Pagination<TopDomain> page = frontTopDomainService.findPageBy(paraMap,pageNo, pageSize);
        resultMap.put("page", page);
		resultMap.put("success", true);
		return resultMap;
	}

	/**
	 * 跳转到编辑页面
	 * @param model
	 * @param id
	 * @param step
	 * @return
	 */
	@RequestMapping("/camp_edit")
	public String camp_edit(Model model,String id,String step){
		// Shiro用户信息
		setUserPageInfo(model);
		ActiveUser activeUser = getUserInfo();

		if(!StringUtils.isEmpty(id)){ // 编辑

			Campaign camp = frontCampService.getById(Long.parseLong(id));
			if(camp != null) {
				String pvUrls = camp.getPvUrls();
				if (!StringUtils.isEmpty(pvUrls)) {
					pvUrls = pvUrls.substring(1, pvUrls.length() - 1);
					pvUrls = pvUrls.replace(",", "\n");
					pvUrls = pvUrls.replace("\"", "");
					camp.setPvUrls(pvUrls);
				}
				model.addAttribute("po", camp);
			}else{
				throw new PlatformException("该活动不存在!");
			}

			if( StringUtils.isEmpty(step) ){
				switch (camp.getEditStepStatus()) {
					case 1:
						step ="1";
						break;
					case 2:
						step ="2";
						break;
					case 3:
						step ="3";
						break;
					default:
						step ="1";
						break;
				}
			}else if("2".equals(step) ){
				step ="2";
			}else if("3".equals(step) ){
				step ="3";
			}

			if( "1".equals(step) ){
				return PAGE_DIRECTORY+"camp_edit";
			}else if( "2".equals(step)  ){

				// 1. 初始化投放基础数据
				frontCampService.initDirectData(model,camp,activeUser);

				// 2. 查询该活动下的投放定向数据
				CampaignDim cd = new CampaignDim();
				cd.setCampaignId(camp.getId());
				frontCampService.findCampDimData(model,cd);

				// 3. 跳转到页面
				return PAGE_DIRECTORY+"camp_edit_two";

			}else if( "3".equals(step) ){

				//查询并封装数据集合 第三步
				frontCampService.queryAndPackageData(model,camp);

                Subject subject = SecurityUtils.getSubject();
                Session session = subject.getSession();
                List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
                list = (List<Map<String,Object>>)model.asMap().get("listCSF");
                Set<String> cSet = new HashSet<String>();
                if(list != null) {
                    for (Map<String, Object> map : list) {
                        cSet.add(map.get("size").toString());
                    }
                }
                session.setAttribute("creativeSize",cSet);

				return PAGE_DIRECTORY+"camp_edit_three";
			}

		}else {
		    Campaign campaign = new Campaign();
            campaign.setDeleteStatus(0);
            model.addAttribute("po", campaign);
        }
		return PAGE_DIRECTORY+"camp_edit";
	}

	/**
	 * 新建活动保存(第一步)
	 * @param model
	 * @param camp
	 * @param startTime
	 * @param endTime
	 * @param step
	 * @return
	 */
	@RequestMapping("/saveCamp")
	@ResponseBody
	public Map<String,Object> saveCamp(Model model,@RequestBody Campaign camp,String startTime,String endTime,int step){

		// Shiro用户信息
		setUserPageInfo(model);
		ActiveUser user = getUserInfo();
        if(camp.getPartnerId() == null){
            camp.setPartnerId(user.getFavPartner().getId());
        }

		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
		if(!StringUtils.isEmpty(startTime)){
			try {
				camp.setStartTime(sdf.parse(startTime));
			} catch (ParseException e) {
				e.printStackTrace();
			}

		}

		if(!StringUtils.isEmpty(endTime)){
			try {
				camp.setEndTime(sdf.parse(endTime));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		Map<String,Object> resultMap = new HashMap<String,Object>();

		if(!StringUtils.isEmpty(camp.getId())){
			if(step==3){
				camp.setManulStatus(CampaignManulStatus.TOCOMMIT); // 编辑完成状态: 是待提交，就是在活动中没有点开始按钮
			}
            Campaign oldCampaign = frontCampService.getById(camp.getId());
			frontCampService.modifyCampAndSendMessage(camp,oldCampaign);
            sysLogService.saveCampaignStep1(user,camp,oldCampaign);
			resultMap.put("id",camp.getId() );
		}else{

			//1. 默认新增广告组
			CampGroup cg = new CampGroup();
			cg.setPartnerId(camp.getPartnerId());
			cg.setDailybudget(camp.getDailyBudget());
			cg.setGroupName(camp.getCampaignName());
			cg.setDeletestatus(0);
			CampGroup cobj = frontCampGroupService.getByMap(cg);
			if(cobj==null){
				frontCampGroupService.insert(cg);
				cg = frontCampGroupService.getByMap(cg);
				// 发送消息到引擎
				frontCampGroupService.sendCampGroupMessage(cg);
				camp.setGroupId(cg.getId());
			}else{
				resultMap.put("url", "camp_edit.action");
				resultMap.put("success", false);
			}

			camp.setTransType(0);
			camp.setCreatorId(user.getUserId());
			camp.setModifierId(user.getUserId());
			camp.setEditStepStatus(step);
			camp.setAutoStatus(CampaignAutoStatus.READY);
			camp.setManulStatus(CampaignManulStatus.EDIT);
			camp.setExpendType(0);
			camp.setDeleteStatus(0);
            camp.setOrderBy(4);
			//2. 新增广告活动
			Campaign cp = frontCampService.getByMap(camp);
			if(cp == null){
				frontCampService.savaCampAndSendMessage(camp);
				camp = frontCampService.getByMap(camp);
				if(camp !=null){
					resultMap.put("id",camp.getId() );
				}
			}else{
				resultMap.put("url", "camp_edit.action");
				resultMap.put("success", false);
			}

		}

		resultMap.put("step", 2); //跳转到第二步
		if (step ==3){
			resultMap.put("url", "camp_list.action");
		}else{
			resultMap.put("url", "camp_edit.action");
		}

		resultMap.put("success", true);
		return resultMap;
	}

	@RequestMapping("/saveCampDim")
	@ResponseBody
	public Map<String,Object> saveCampDim(Model model,@RequestBody List<CampaignDim> listCD,String price,long campid,Integer expend_type,Integer step){

        Campaign  camp = new Campaign();
        Double d = Double.parseDouble(price)*100;
        Integer p = Integer.parseInt(new java.text.DecimalFormat("0").format(d));
        camp.setPrice(p);
        camp.setId(campid);
        camp.setExpendType(expend_type);
        Campaign  oldCampaign = frontCampService.getById(campid);
        camp.setCampaignType(oldCampaign.getCampaignType());
        camp.setAdType(oldCampaign.getAdType());
        camp.setDailyBudget(oldCampaign.getDailyBudget());
        camp.setPvUrls(oldCampaign.getPvUrls());
        if(oldCampaign.getEditStepStatus() != 3) {
            camp.setEditStepStatus(step);
        }
        if(step==3){
            camp.setManulStatus(CampaignManulStatus.TOCOMMIT); // 编辑完成状态: 是待提交，就是在活动中没有点开始按钮
        }

        //更新活动
        frontCampService.modifyCampAndSendMessage(camp,oldCampaign);

        //变更记录日志
        ActiveUser activeUser = getUserInfo();
        sysLogService.saveCampaignStep2(activeUser,camp,oldCampaign,listCD);

		//投放策略
        //1.黑名单特殊处理
        CampaignDim black = new CampaignDim();
        black.setCampaignId(campid);
        black.setDimName("blacklist");
        CampaignDim blackCampDim = frontCampDimService.selectByCampAndDimName(black);
		frontCampDimService.deleteListByCampId(campid);
        if(blackCampDim != null){ //黑名单定向
            frontCampDimService.insert(blackCampDim);
        }
		for(CampaignDim cd: listCD){
            CampaignDim campDim = frontCampDimService.selectByCampAndDimName(cd);
			if( campDim == null ){
				frontCampDimService.insert(cd);
			}else{
				frontCampDimService.updateByCampAndDimName(cd);
			}
		}

		Map<String,Object> resultMap = new HashMap<String,Object>();
		if(step == 2) {
			resultMap.put("url", "camp_edit.action?id=" + campid + "&step=3");
		}else{
			resultMap.put("url", "camp_list.action");
		}
		resultMap.put("success", true);
		return resultMap;

	}

	@RequestMapping("/fileUpload")
	@ResponseBody
	public String fileUpload(Model model, HttpServletRequest request, long  campId){
		String retStr = "上传素材结果: ";
		ActiveUser activeUser = getUserInfo();
		Campaign  camp =  frontCampService.getById(campId);

		//1. 上传并保存素材
		String uploadDir = Constants.CREATIVE_DIR_PATH + "/"+camp.getId() ;
		File dir = new File(uploadDir);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		retStr += frontCreativeService.uploadFileAndSave( request, camp, activeUser.getFavPartner().getId(), uploadDir);

        sysLogService.saveCampaignStep3(activeUser,camp, camp, retStr);

		//2. 返回结果
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("url", "camp_edit.action?id="+camp.getId()+"&step=3");
		resultMap.put("message", retStr);
		resultMap.put("success", true);
		return retStr;

	}

	@RequestMapping("/saveCampThree")
	@ResponseBody
	public Map<String,Object> saveCampThree(Model model, @RequestBody Campaign camp,CampaignDim cd){

		//1. 更新活动下的广告
		Ad ad = new Ad();
		ad.setCampaignId(camp.getId());
		ad.setClickUrl(camp.getClickUrl());
		ad.setLandingPage(camp.getLandingPage());
		ad.setPvUrls(camp.getPvUrls());
		frontAdService.updateAdListByCampId(ad);

		//2. 保存行业渠道
		frontCampService.modifyCatgoryChannels(cd);

		//3. 更新活动
        Campaign  oldCampaign = frontCampService.getById(camp.getId());
        camp.setCampaignType(oldCampaign.getCampaignType());
        camp.setAdType(oldCampaign.getAdType());
		camp.setManulStatus(CampaignManulStatus.TOCOMMIT); // 编辑完成状态: 是待提交，就是在活动中没有点开始按钮
		camp.setEditStepStatus(3);
        camp.setOrderBy(5);
        camp.setDailyBudget(oldCampaign.getDailyBudget());
        camp.setPvUrls(oldCampaign.getPvUrls());
		frontCampService.modifyCampAndSendMessage(camp,oldCampaign);
        //更新操作记录变更日志
        if(oldCampaign.getEditStepStatus().intValue() == 3) {
            ActiveUser activeUser = getUserInfo();
            sysLogService.saveCampaignStep3(activeUser,camp, oldCampaign, null);
        }
		//4. 返回结果
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("url", "camp_list.action");
		resultMap.put("success", true);
		return resultMap;
	}

	@RequestMapping("/ceative_delete")
	@ResponseBody
	public Map<String,Object> ceative_delete(Model model,long camp_id,String id){

		if(!StringUtils.isEmpty(id)){
			String id_arr[] = id.split(",");
			for(String creativeId_str : id_arr){
				long creativeId = Long.parseLong(creativeId_str);
				Ad ad = new Ad();
				ad.setCampaignId(camp_id);
				ad.setCreativeId(creativeId);
				ad.setDeleteStatus(1);

				// 删除广告的行业类别
				List<Ad> adList = frontAdService.listBy(ad);
				AdCategory adCategory = null;
				for (Ad entity : adList) {
					adCategory = new AdCategory();
					adCategory.setAdId(entity.getId());
					adCategory.setCampaignId(camp_id);
					adCategoryService.deleteAdCategory(adCategory);
				}

				//1. 删除广告活动和创意关系  逻辑删除
				frontAdService.update(ad);

				//2. 删除创意组和创意关系  逻辑删除
				Creative creative = frontCreativeService.getById(creativeId);
				CreativeGroup cg = new CreativeGroup();
				cg.setId(creative.getGroupId());
				cg.setDeleteStatus(1);
				frontCreativeGroupService.update(cg);

				//3. 删除创意  逻辑删除
				creative.setDeleteStatus(1);
				frontCreativeService.update(creative);
			}
		}

		//4. 返回结果
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("url", "camp_edit.action?id="+camp_id+"&step=3");
		resultMap.put("success", true);
		return resultMap;
	}

}
