package com.jtd.support.redis;

import com.jtd.commons.PropertyConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.FactoryBean;
import redis.clients.jedis.JedisShardInfo;

import javax.annotation.Resource;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月22日
 * @项目名称 dsp-common-redis
 * @描述 
 */
public class RedisGroupFactory implements FactoryBean<List<JedisShardInfo>>{
	private static final Log log = LogFactory.getLog(RedisGroupFactory.class);

	@Resource
	private PropertyConfig propertyConfig;

	private String groupName;

	public RedisGroupFactory(String groupName) {
		super();
		this.groupName = groupName;
	}

	@Override
	public List<JedisShardInfo> getObject() throws Exception {
		List<JedisShardInfo> jedisShardInfos = new ArrayList<JedisShardInfo>();
		List<String[]> properties = propertyConfig.findProperty(groupName);
		if (properties == null || properties.isEmpty()) {
			throw new RuntimeException("未找到redis配置组名称[" + groupName + "]的值");
		}
		for (String[] property : properties) {
			String key = property[0];
			String value = property[1];
			log.debug("节点名称[" + key + "]，节点uri[" + value + "]");
			JedisShardInfo jedisShardInfo = new JedisShardInfo(new URI(value));
			jedisShardInfos.add(jedisShardInfo);
		}
		log.info("redis配置数组[" + groupName + "]初始化完成,共" + jedisShardInfos.size() + "个节点");
		return jedisShardInfos;
	}

	@Override
	public Class<?> getObjectType() {
		return List.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
}
