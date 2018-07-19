package com.jtd.web.dao;

import com.jtd.commons.dao.BaseDao;
import com.jtd.web.po.Channel;

import java.util.List;
import java.util.Map;

public interface IChannelDao extends BaseDao<Channel> {

	public List<Map<String,Object>> listAllByCampId(Map<String, Object> map);

}
