package com.jtd.web.service.admin.impl;

import com.jtd.commons.dao.BaseDao;
import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.commons.page.Pagination;
import com.jtd.web.controller.DicController;
import com.jtd.web.dao.ITopDomainDao;
import com.jtd.web.po.TopDomain;
import com.jtd.web.service.admin.IAdminTopDomainService;
import com.jtd.web.service.admin.IAdminUserRoleService;
import com.jtd.web.service.front.IFrontTopDomainService;
import com.jtd.web.service.impl.BaseService;
import com.jtd.web.vo.WebsiteType;

import org.apache.kahadb.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminTopDomainService extends BaseService<TopDomain> implements IAdminTopDomainService {

	@Autowired
	private ITopDomainDao topDomainDao;
	
	@Override
	protected BaseDao<TopDomain> getDao() {
		// 切换业务库数据源
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		return topDomainDao;
	}

	@Override
	public Pagination<TopDomain> findDomainList(Model model, Integer pageNo, Integer pageSize, Map<String,Object> paraMap) {
		Pagination<TopDomain> page = this.findPageBy(paraMap,pageNo,pageSize);
		Map<String, WebsiteType> allWebsiteType = DicController.getAllWebSiteTypes();
		List<TopDomain> list = page.getList();
		for (TopDomain topDomain: list) {
			for (WebsiteType websiteType: allWebsiteType.values()){
				if(websiteType.getSubWebsiteTypes() == null){
					if(websiteType.getId().equals(topDomain.getWebSiteType())){
						topDomain.setWebSiteType(websiteType.getName());
					}
				}
			}
		}
		return page;
	}
}
