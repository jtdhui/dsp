package com.jtd.web.service.admin;

import com.jtd.commons.page.Pagination;
import com.jtd.web.po.Channel;
import com.jtd.web.service.IBaseService;

import java.util.List;
import java.util.Map;

public interface IAdminChannelService extends IBaseService<Channel>{
	
	public List<Map<String,Object>> listAllByCampId(Map<String, Object> map);

	public Pagination<Map<String, Object>> listChannelBy(Map<String, Object> paraMap, Integer pageNo, Integer pageSize) ;
	
}
