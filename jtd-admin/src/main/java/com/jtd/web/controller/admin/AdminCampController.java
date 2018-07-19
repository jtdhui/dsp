package com.jtd.web.controller.admin;

import com.jtd.commons.page.Pagination;
import com.jtd.utils.JsonUtil;
import com.jtd.web.constants.CatgSerial;
import com.jtd.web.controller.BaseController;
import com.jtd.web.controller.DicController;
import com.jtd.web.jms.ChangeAfMsg;
import com.jtd.web.jms.SetCampBlackListMsg;
import com.jtd.web.jms.SetCampGrossProfitMsg;
import com.jtd.web.po.*;
import com.jtd.web.service.IMQConnectorService;
import com.jtd.web.service.admin.*;
import com.jtd.web.vo.ChannelCategory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import java.net.URLDecoder;
import java.util.*;

/**
 * 
 * @作者 duber
 * @版本 V1.0
 * @创建日期 2016年10月18日
 * @描述 后台活动管理
 */
@Controller
@RequestMapping("/admin/camp")
public class AdminCampController extends BaseController{

	public static final String PAGE_DIRECTORY="/admin/camp/";
	public static final String ACTION_PATH="redirect:/admin/camp/";

	//广告活动
	@Autowired
	private IAdminCampService adminCampService;
	// 渠道
	@Autowired
	private IAdminChannelService adminChannelService;
	
	@Autowired
	private IAdminCampaignCategoryService adminCampaignCategoryService;

    @Autowired
    private IAdminAdCategoryService adCategoryService;
	
	@Autowired
	private IAdminCampaignDimService adminCampaignDimService;
	
	@Autowired
	private IMQConnectorService mQConnectorService;

	@Autowired
	private IAdminAdService adService;

    @Autowired
    private IAdminPartnerService partnerService;
	
	
	@RequestMapping("/list")
	public String list(Model model,Integer pageNo,Integer pageSize,HttpServletRequest request){
		
		// Shiro用户信息
		setUserPageInfo(model);
		
		HashMap<String, Object> paraMap = new HashMap<String, Object>();
		if (StringUtils.isEmpty(request.getParameter("campaignId")) == false) {
			paraMap.put("campaignId", request.getParameter("campaignId"));
		}
		if (StringUtils.isEmpty(request.getParameter("partnerName")) == false) {
			paraMap.put("partnerName", request.getParameter("partnerName"));
		}
		if (StringUtils.isEmpty(request.getParameter("campaignName")) == false) {
			paraMap.put("campaignName", request.getParameter("campaignName"));
		}
		if (StringUtils.isEmpty(request.getParameter("campaignAutoStatus")) == false) {
			paraMap.put("campaignAutoStatus", Integer.parseInt(request
					.getParameter("campaignAutoStatus")));
		}
		
		model.addAttribute("queryMap", paraMap);

		ActiveUser activeUser = getUserInfo();
        List<Partner> partnerList = partnerService.findPartnerListByLoginUser(activeUser);
        if(partnerList.size()>0){
            String partnerIds = "";
            for (Partner po:partnerList){
                partnerIds += po.getId() + ",";
            }
            if (!StringUtils.isEmpty(partnerIds)) {
                partnerIds = partnerIds.substring(0, partnerIds.length() - 1);
                paraMap.put("partnerIds", partnerIds);
            }
        }
		
		Pagination<Map<String,Object>> page = adminCampService.listCampBy(paraMap,pageNo, pageSize);
		model.addAttribute("page",page);
		return PAGE_DIRECTORY+"admin_camp_list";
	}
	
	@RequestMapping("/blackSet")
	public String blackSet(Model model,String id){
		Map<String,Object> paraMap = new HashMap<String,Object>();
		paraMap.put("dimName", "blacklist");
		paraMap.put("id", id);
		Map<String,Object> sp = adminCampService.findCampByMap(paraMap);
		
		model.addAttribute("po", sp);
		// Shiro用户信息
		setUserPageInfo(model);
		
		return PAGE_DIRECTORY+"admin_camp_black";
	}
	
	@RequestMapping("/black/update")
	@ResponseBody
	public Map<String,Object> blackUpdate(Model model,long id, String blackUrl){
		
		CampaignDim obj = new CampaignDim();
		obj.setCampaignId(id);
		obj.setDimName("blacklist");
		obj.setDimValue(blackUrl);
		String[] blackList= blackUrl.split("\n");
		List<String> list = null;
		if (blackList != null && blackList.length > 0) {
			list=new ArrayList<String>();
			for(String str : blackList){
				try{
					list.add(URLDecoder.decode(str,"UTF-8"));
				}catch(Exception ex){
				}
			}
			CampaignDim campaignDim = adminCampaignDimService.selectByCampAndDimName(obj);
			if(campaignDim==null){
				adminCampaignDimService.insert(obj);
			}else{
				adminCampaignDimService.updateByCampAndDimName(obj);
			}
		}
		
		// 发送黑名单 为空的时候也要发消息，是清空已设置的黑名单
		SetCampBlackListMsg setCampBlackListMsg = new SetCampBlackListMsg();
		setCampBlackListMsg.setCampid(id);
		setCampBlackListMsg.setBlackList(list);
		mQConnectorService.sendMessage(setCampBlackListMsg);
		
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("url", "list.action");
		return resultMap;
	}
	
	@RequestMapping("/channelSet")
	public String channelSet(Model model,String id){
		Map<String,Object> paraMap = new HashMap<String,Object>();
		paraMap.put("dimName", "channels");
		paraMap.put("id", id);
		Map<String,Object> sp = adminCampService.findCampByMap(paraMap);
		model.addAttribute("po", sp);
		//活动下面渠道设置的行业类型基础数据
		Map<CatgSerial, List<ChannelCategory>> channelDIC = DicController.getChannelCategorysList();
		model.addAttribute("channelDIC", channelDIC.get(CatgSerial.DSP));
        model.addAttribute("tanxChannelDIC", channelDIC.get(CatgSerial.TANX));
        model.addAttribute("besChannelDIC", channelDIC.get(CatgSerial.BES));
        model.addAttribute("vamChannelDIC", channelDIC.get(CatgSerial.VAM));
        model.addAttribute("tencentChannelDIC", channelDIC.get(CatgSerial.TENCENT));
        model.addAttribute("xtraderChannelDIC", channelDIC.get(CatgSerial.XTRADER));
		// Shiro用户信息
		setUserPageInfo(model);
		//渠道信息
		Map<String,Object> pMap = new HashMap<String,Object>();
		pMap.put("id", id);
		List<Map<String,Object>> channelList = adminChannelService.listAllByCampId(pMap);
        for(int i =0;i<channelList.size();i++){
            Map<String,Object> map = channelList.get(i);
            if(map.get("id").toString().equals("7")){
                channelList.remove(map);
                i--;
            }
        }

		model.addAttribute("channelList", channelList);
		
		//活动与行业类型关联
		return PAGE_DIRECTORY+"admin_camp_channel";
	}
	
	@RequestMapping("/channel/update")
	@ResponseBody
	public Map<String,Object> channelUpdate(Model model,CampaignDim cd){
		
		long campaign_id = cd.getCampaignId();
		String channelIds = cd.getDimValue();
		List<CampaignCategory> listCC = new ArrayList<CampaignCategory>();
        List<AdCategory> listAdCategory = new ArrayList<AdCategory>();
		List<Integer> listID = new ArrayList<Integer>();

        Ad ad = new Ad();
        ad.setCampaignId(campaign_id);
        List<Ad> adList = adService.listBy(ad);
		//处理页面参数
		CampaignCategory cc = null;
        AdCategory adCategory = null;
		if(channelIds.length()>0){
			String[] channels = channelIds.split(",");
			for(int i =0 ;i<channels.length;i++){
				cc = new CampaignCategory();
				String ccStr = channels[i].toString();
				String[]  ccArray= ccStr.split("\\$\\$\\$");
				Integer channelID = Integer.parseInt(ccArray[0]);
				cc.setCampaignId(campaign_id);
				cc.setCatgid(ccArray[1]);
				cc.setCatgserial(ccArray[2]);
				listID.add(channelID);
				listCC.add(cc);

                // 关联广告的行业类别
                for(Ad adEntity: adList){
                    adCategory = new AdCategory();
                    adCategory.setCampaignId(campaign_id);
                    adCategory.setAdId(adEntity.getId());
                    adCategory.setCatgId(ccArray[1]);
                    adCategory.setCatgserial(ccArray[2]);
                    adCategory.setCreateTime(new Date());
                    listAdCategory.add(adCategory);
                }
			}
			
		}
		//封装成json对象
		Map<String,Object> map = new HashMap<String,Object>();
		if(listID.size()>0){
			map.put("selected", listID);
			map.put("type", 1);
		}else{
			map.put("selected", "[]");
			map.put("type", 0);
		}
		
		CampaignDim obj = new CampaignDim();
		obj.setCampaignId(campaign_id);
		obj.setDimName("channels");
		obj.setDimValue(JsonUtil.toJSONString(map));
		CampaignDim campaignDim = adminCampaignDimService.selectByCampAndDimName(obj);
		if(campaignDim==null){
			adminCampaignDimService.insert(obj);
		}else{
			adminCampaignDimService.updateByCampAndDimName(obj);
		}
		
		//删除活动关联的行业类型
		adminCampaignCategoryService.deleteByCampaignId(campaign_id);
		//增加活动关联的行业类型
		adminCampaignCategoryService.insertBatch(listCC);

        //该活动下的广告行业类型
        AdCategory ac = new AdCategory();
        ac.setCampaignId(campaign_id);
        adCategoryService.deleteAdCategory(ac);
        adCategoryService.insertBatch(listAdCategory);

        //查询该活动是否是投放中,如果是投放中则暂停该活动
        Campaign campaign = adminCampService.getById(campaign_id);
        String ret = adminCampService.checkCampaignAndSendMq(campaign);

		Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("msg", "渠道保存成功!"+ret);
		resultMap.put("url", "list.action");
		return resultMap;
	}
	
	/**
	 * 毛利页面
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping("/profitSet")
	public String profitSet(Model model,String id){
		Map<String,Object> sp = adminCampService.findCampPartnerBy(Long.parseLong(id));
		model.addAttribute("po", sp);
		// Shiro用户信息
		setUserPageInfo(model);
		
		return PAGE_DIRECTORY+"admin_camp_profit";
	}
	
	/**
	 * 毛利更新
	 * @param model
	 * @param grossProfit
	 * @return 
	 */
	@RequestMapping("/profit/update")
	@ResponseBody
	public Map<String,Object> profitSetUpdate(Model model,long id, Integer grossProfit){

		Campaign camp = new Campaign();
		camp.setId(id);
		camp.setGrossProfit(grossProfit);
		adminCampService.updateProfit(camp);
		if(grossProfit != null) {
			SetCampGrossProfitMsg setCampGrossProfitMsg = new SetCampGrossProfitMsg();
			setCampGrossProfitMsg.setCampid(id);
			setCampGrossProfitMsg.setGrossProfit(grossProfit);
			mQConnectorService.sendMessage(setCampGrossProfitMsg);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("url", "list.action");
		return resultMap;
	}

	/**
	 * AF页面
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping("/afSet")
	public String afSet(Model model,String id){
		Map<String,Object> sp = adminCampService.findCampPartnerBy(Long.parseLong(id));
		model.addAttribute("po", sp);
		// Shiro用户信息
		setUserPageInfo(model);

		return PAGE_DIRECTORY+"admin_camp_af";
	}

	/**
	 * AF更新
	 * @param model
	 * @param afValue
	 * @return
	 */
	@RequestMapping("/af/saveAfSet")
	@ResponseBody
	public Map<String,Object> saveAfSet(Model model,long id, Integer afValue){

		Campaign camp = adminCampService.getById(id);
        if(afValue == null){
            afValue = 0;
        }
		camp.setAfValue(afValue);
		adminCampService.update(camp);
		ChangeAfMsg changeAfMsg = new ChangeAfMsg();
		changeAfMsg.setAf(afValue);
		changeAfMsg.setCampid(id);
        mQConnectorService.sendMessage(changeAfMsg);
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("url", "list.action");
		return resultMap;
	}
}
