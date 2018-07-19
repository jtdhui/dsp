package com.jtd.web.controller.front;

import com.jtd.commons.page.Pagination;
import com.jtd.utils.DateUtil;
import com.jtd.web.constants.CampaignStatus;
import com.jtd.web.controller.BaseController;
import com.jtd.web.exception.PlatformException;
import com.jtd.web.po.ActiveUser;
import com.jtd.web.po.Campaign;
import com.jtd.web.po.Partner;
import com.jtd.web.po.count.Pard;
import com.jtd.web.po.count.Parh;
import com.jtd.web.service.front.*;
import com.jtd.web.vo.TreeNodeVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import java.util.*;

@Controller
@RequestMapping("/front")
public class HomeController extends BaseController{

	public static final String DIRECTORY="/front/";
	public static final String ACTION_PATH = "redirect:/front/";

	@Autowired
	private IFrontParhCountService frontParhCountService;

	@Autowired
	private IFrontPardCountService frontPardCountService;

	@Autowired
	private IFrontUserFavService frontUserFavService;

	@Autowired
	private IFrontPartnerService frontPartnerService;

	@Autowired
	private IFrontCampService frontCampService;

	@RequestMapping("/home")
	public String home(Model model,HttpServletRequest request,Integer pageNo,Integer pageSize) throws PlatformException{
		// Shiro用户信息
		setUserPageInfo(model);
		ActiveUser activeUser = getUserInfo();

		//广告主列表(侧边切换)
//        String partnerName = "";
//        if ( StringUtils.isEmpty(request.getParameter("partnerName")) == false ) {
//            partnerName = request.getParameter("partnerName").toString();
//        }
//		frontPartnerService.findPartnerSideList(model,activeUser,partnerName);

		long parterId =0;
		if ( StringUtils.isEmpty(request.getParameter("parterId")) ) {
			if(activeUser.getFavPartner()== null){
				// 该用户没有相关的广告主数据
				throw new PlatformException("该用户没有相关的广告主数据权限,请联系管理员分配相应权限!");
			} else{
				parterId = activeUser.getFavPartner().getId();
			}
		}else{
			parterId = Long.parseLong(request.getParameter("parterId").toString());
		}

		Date date=new Date();
		int today = DateUtil.getDateInt(date);
		int otherDay = 0;

		if ( StringUtils.isEmpty(request.getParameter("otherDay")) ) { //默认是昨天
			Date yesterday= DateUtil.getNextDate(date, -1);
			otherDay = DateUtil.getDateInt(yesterday);
		}else{
			String otherDayStr = request.getParameter("otherDay").toString();
			otherDay = Integer.parseInt(otherDayStr);
		}

		model.addAttribute("today", today);
		model.addAttribute("otherDay", otherDay);

		// 广告主按小时查询
		Map<String,Object> phMap = new HashMap<String,Object>();
		phMap.put("partnerId", parterId);
		phMap.put("today", today);
		phMap.put("otherDay", otherDay);

		if(parterId>0){
			List<Parh> phList = frontParhCountService.listByMap(phMap,true);

			Parh ph = null;
			List<Integer> todayList = new ArrayList<Integer>();
			List<Integer> otherList = new ArrayList<Integer>();
			todayList.add(0);
			otherList.add(0);
			for(int i=phList.size()-1;i>=0;i--){
				ph = new Parh();
				ph = phList.get(i);
				if(ph.getDate()==today){
					todayList.add(ph.getPv());
				}else{
					otherList.add(ph.getPv());
				}
			}

			model.addAttribute("todayList", todayList);
			model.addAttribute("otherList", otherList);

			//分页查询

			phMap.put("hour", DateUtil.getHourByDay());
			Pagination<Parh> page = frontParhCountService.findFullPageBy(phMap,pageNo, pageSize,false);
			model.addAttribute("page",page);

			//广告主当天查询
			Map<String,Object> pdMap = new HashMap<String,Object>();
			pdMap.put("partnerId", parterId);
			pdMap.put("today", today);
			Pard pd = frontPardCountService.getByMap(pdMap);
			model.addAttribute("pd", pd);
			//当前广告主 在投活动数量
			Campaign t = new Campaign();
			t.setPartnerId(parterId);
			t.setCampaignstatus(CampaignStatus.ONLINE);

			List<Campaign> listC = frontCampService.listBy(t);
			if(listC ==null){
				model.addAttribute("camp_online_count",0);
			}else{
				model.addAttribute("camp_online_count",listC.size());
			}

		}
		return DIRECTORY+"home";
	}

	@RequestMapping("/userFav")
	public String userFav(Model model,long userId,long partnerId,String from){

		// Shiro用户信息
		setUserPageInfo(model);

		ActiveUser activeUser = getUserInfo();
		Partner partner = frontPartnerService.getById(partnerId);
		activeUser.setFavPartner(partner);

		//保存登录用户下活动的广告主
		frontUserFavService.saveUserFav(userId, String.valueOf(partnerId));
		String retUrl = "home.action";
		if(!StringUtils.isEmpty(from)){
			switch (from){
				case "home":
					retUrl = "home.action";
					break;
				case "camp":
					retUrl = "campManage/camp_list.action";
					break;
				case "report":
					retUrl = "report/time.action";
					break;
				case "toInfo":
					retUrl = "account/toInfo.action";
					break;
				case "toQualiDoc":
					retUrl = "account/toQualiDoc.action";
					break;
				case "toFinance":
					retUrl = "account/toFinance.action";
					break;
				case "toGetCode":
					retUrl = "account/toGetCode.action";
					break;
			}
		}
		return ACTION_PATH+retUrl;
	}

    @RequestMapping("/partnerList")
    @ResponseBody
    public List<TreeNodeVo> partnerList(Model model,String fromPage){

        ActiveUser activeUser = getUserInfo();
        List<TreeNodeVo> resultList = new ArrayList<TreeNodeVo>();

        //广告主列表(侧边切换)
        List<Partner> list = frontPartnerService.findPartnerSideList(activeUser);
        TreeNodeVo vo = null;
        for(Partner p : list){
            vo = new TreeNodeVo();
            vo.setId(p.getId());
            vo.setpId(p.getPid());
            vo.setName(p.getPartnerName());
            vo.setOpen(true);

            String before = "";
            if("home".equals(fromPage)) {
                vo.setUrl("userFav.action?userId=" + activeUser.getUserId() + "&partnerId=" + p.getId() + "&from=" + fromPage);
            }else{
                before = "../";
                vo.setUrl("../userFav.action?userId=" + activeUser.getUserId() + "&partnerId=" + p.getId() + "&from=" + fromPage);
            }

            if(p.getStatus().equals(0)){
                vo.setIcon(before+"../images/front/qi.jpg");
            }else {
                vo.setIcon(before+"../images/front/ting.jpg");
            }

            vo.setTarget("_self");
            resultList.add(vo);
        }

        return resultList;
    }
}
