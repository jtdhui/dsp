package com.jtd.web.controller.front;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jtd.commons.page.Pagination;
import com.jtd.web.constants.AdType;
import com.jtd.web.controller.BaseController;
import com.jtd.web.controller.DicController;
import com.jtd.web.service.front.IFrontAdPlaceService;

@Controller
@RequestMapping("/front/adPlace")
public class FrontAdPlaceController extends BaseController {

	public static final String PAGE_DIRECTORY = "/front/ad_place/";
	public static final String ACTION_PATH = "redirect:/front/ad_place/";

	@Autowired
	private IFrontAdPlaceService service;

	/**
	 * 到adplace_list.jsp
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/toList")
	public String toList(Model model, HttpServletRequest request) {

		setUserPageInfo(model);

		HashMap<String, Object> params = new HashMap<String, Object>();

		if (StringUtils.isEmpty(request.getParameter("channelId")) == false) {
			params.put("channelId", request.getParameter("channelId"));
		}
		if (StringUtils.isEmpty(request.getParameter("adType")) == false) {
			params.put("adType", request.getParameter("adType"));
		}
		if (StringUtils.isEmpty(request.getParameter("webSiteTypeId")) == false) {
			params.put("webSiteTypeId", request.getParameter("webSiteTypeId"));
		}
		if (StringUtils.isEmpty(request.getParameter("filterName")) == false) {
			params.put("filterName", request.getParameter("filterName"));
		}
		int pageNo = 1 ;
		// 分页页数(放到统计库campd表中查询)
		if (StringUtils.isEmpty(request.getParameter("pageNo")) == false) {
			pageNo = Integer.parseInt(request.getParameter("pageNo"));
		}
		if (StringUtils.isEmpty(request.getParameter("pageSize")) == false) {
			pageSize = Integer.parseInt(request.getParameter("pageSize"));
		}

		model.addAttribute("params", params);

		Pagination<Map<String, Object>> page = service.findMapPageBy(params,
				pageNo, pageSize);

		if (page != null && page.getList() != null) {

			for (Map<String, Object> map : page.getList()) {

				if (map.get("web_site_type_id") != null) {
					String webSiteTypeId = (String) map.get("web_site_type_id");
					String websiteTypeDescInfo = DicController
							.getWebsiteTypeDescInfo(webSiteTypeId);
					map.put("web_site_type",websiteTypeDescInfo);
				}
				else{
					map.put("web_site_type","未知");
				}
				
				if (map.get("ad_type") != null) {
					int adType = (int)map.get("ad_type");
					AdType em = AdType.getAdType(adType);
					if(em != null){
						map.put("ad_type",em.getDesc());
					}
					else{
						map.put("ad_type","未知");
					}
				}
				else{
					map.put("ad_type","未知");
				}
			}

		}

		model.addAttribute("page", page);

		return PAGE_DIRECTORY + "ad_place_list";
	}

}
