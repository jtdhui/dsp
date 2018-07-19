package com.jtd.web.service.front;

import javax.servlet.http.HttpServletRequest;

import com.jtd.web.po.Campaign;
import com.jtd.web.po.Creative;
import com.jtd.web.po.Partner;
import com.jtd.web.service.IBaseService;
import com.jtd.web.shiro.OwnerPartner;

public interface IFrontCreativeService extends IBaseService<Creative> {

	public String uploadFileAndSave(HttpServletRequest request, Campaign camp, long partner_id, String uploadDir);

}
