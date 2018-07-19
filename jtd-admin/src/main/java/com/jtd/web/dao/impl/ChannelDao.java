package com.jtd.web.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.jtd.commons.dao.BaseDaoImpl;
import com.jtd.commons.page.Pagination;
import com.jtd.web.dao.IChannelDao;
import com.jtd.web.po.Channel;

/**
 * 
 * @作者 duber
 * @版本 V1.0
 * @创建日期 2016年10月20日
 * @描述 渠道DAO实现类
 */
@Repository
public class ChannelDao extends BaseDaoImpl<Channel>  implements IChannelDao {

	@Override
	public List<Map<String,Object>> listAllByCampId(Map<String, Object> map) {
        List<Map<String,Object>> list = getSqlSession().selectList(getStatement("listAllByCampId"),map);
		return list;
	}


	/**
	 * 多表分页查询也可以单表查询
	 */
	@Override
	public List<Map<String, Object>> getMapPageBy(Pagination<Map<String, Object>> page){
		return getSqlSession().selectList(getStatement("listByAll"),page);
	}

	
	public long insert(Channel channel) {
		return getSqlSession().insert(getStatement("insert"), channel);

	}

	
//	public long getById(Long id) {
//		Map<String,Long> map = new HashMap<String,Long>();
//		map.put("id", id);
//		return getSqlSession().selectOne(getStatement("getById"), map);
//	}

}
