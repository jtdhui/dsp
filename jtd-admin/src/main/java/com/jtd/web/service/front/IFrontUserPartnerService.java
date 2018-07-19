package com.jtd.web.service.front;

import com.jtd.web.po.UserPartner;
import com.jtd.web.service.IBaseService;

import java.util.List;
import java.util.Map;

public interface IFrontUserPartnerService extends IBaseService<UserPartner> {

	public List<Map<String, Object>> findUserPartnerBy(Map<String,Object> map);

}
