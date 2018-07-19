package com.jtd.web.service.admin.impl;

import com.jtd.commons.dao.BaseDao;
import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.commons.page.Pagination;
import com.jtd.web.dao.IChannelDao;
import com.jtd.web.po.CampaignCategory;
import com.jtd.web.po.Channel;
import com.jtd.web.service.admin.IAdminChannelService;
import com.jtd.web.service.impl.BaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 
 * @作者 duber
 * @版本 V1.0
 * @创建日期 2016年10月20日
 * @描述 后台渠道Service 实现类
 */
@Service
public class AdminChannelService  extends BaseService<Channel> implements IAdminChannelService {

	@Autowired
	private IChannelDao channelDao;
	
	@Override
	protected BaseDao<Channel> getDao() {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		return channelDao;
	}
	
	@Override
	public List<Map<String,Object>> listAllByCampId(Map<String, Object> map) {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		return channelDao.listAllByCampId(map);
	}
	


	/**
	 * 后台活动列表
	 * @param paraMap
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@Override
	public Pagination<Map<String, Object>> listChannelBy(Map<String, Object> paraMap, Integer pageNo, Integer pageSize) {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		Pagination<Map<String, Object>> page = new Pagination<Map<String, Object>>();
		page = this.findMapPageBy(paraMap,pageNo, pageSize);
		return page;
	}

}
