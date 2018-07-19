package com.jtd.web.service.admin;

import com.jtd.commons.page.Pagination;
import com.jtd.web.po.TopDomain;
import com.jtd.web.service.IBaseService;

import org.springframework.ui.Model;

import java.util.Map;

public interface IAdminTopDomainService extends IBaseService<TopDomain> {

    public Pagination<TopDomain> findDomainList(Model model, Integer pageNo, Integer pageSize, Map<String, Object> paraMap);
}
