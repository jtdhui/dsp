package com.jtd.engine.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p></p>
 */
public class DynamicProperties {

	private static final Log log = LogFactory.getLog(DynamicProperties.class);

	// 缺省的更新时间
	private static final long DEFAULT_REFRESH_INTERVAL = 15000;

	// 配置文件的位置
	private String location;
	private String[] locations;

	// 存放配置的map
	@SuppressWarnings("unchecked")
	private Map<String, String>[] properties = new HashMap[2];

	// 当前使用哪个map
	private volatile int index;

	// 更新配置的计时器和TimerFuture
	private Timer timer;
	private TimerFuture timerFuture;

	// 更新时间
	private long refreshInterval = DEFAULT_REFRESH_INTERVAL;

	/** 不同参数的构造函数 */
	public DynamicProperties() {
		properties[0] = new HashMap<String, String>();
		properties[1] = new HashMap<String, String>();
	}

	public DynamicProperties(String location) {
		this.location = location;
		properties[0] = new HashMap<String, String>();
		properties[1] = new HashMap<String, String>();
	}

	public DynamicProperties(String[] locations) {
		this.locations = locations;
		properties[0] = new HashMap<String, String>();
		properties[1] = new HashMap<String, String>();
	}

	/**
	 * 初始化配置
	 */
	public void init() {
		index = 0;
		try {
			load(index);
		} catch (Exception e) {
			// 初始化失败
			throw new IllegalStateException("初始化失败", e);
		}

		timerFuture = timer.timing(new TimerTask() {

			/**
			 * (non-Javadoc)
			 * 
			 * @see com.asme.adserver.util.TimerTask#delayOrIntervalMillis()
			 */
			public long delayOrIntervalMillis() {
				return refreshInterval;
			}

			/**
			 * (non-Javadoc)
			 * 
			 * @see com.asme.adserver.util.TimerTask#isTriggerIndependently()
			 */
			public boolean isTriggerIndependently() {
				return false;
			}

			/**
			 * (non-Javadoc)
			 * 
			 * @see com.asme.adserver.util.TimerTask#type()
			 */
			public Type type() {
				return Type.INTERVAL;
			}

			/** 
			 * (non-Javadoc)
			 *
			 * @see java.lang.Runnable#run()
			 */
			public void run() {

				// 切换
				int newIndex = index ^ 1;
				int oldIndex = index;

				try {

					// 加载
					load(newIndex);
					index = newIndex;
					different(oldIndex, newIndex);

					// 清空旧表
					properties[oldIndex].clear();
				} catch (Exception e) {
					log.error("刷新配置出错", e);
				}
			}

			/**
			 * 显示更改
			 * @param oldIndex
			 * @param newIndex
			 */
			private void different(int oldIndex, int newIndex) {

				// 被删除的key
				Set<String> removedKeys = new HashSet<String>(properties[oldIndex].keySet());
				removedKeys.removeAll(properties[newIndex].keySet());

				// 新增的key
				Set<String> addedKeys = new HashSet<String>(properties[newIndex].keySet());
				addedKeys.removeAll(properties[oldIndex].keySet());

				// 可能修改了值的key
				Set<String> mayModifiedKeys = new HashSet<String>(properties[oldIndex].keySet());
				mayModifiedKeys.retainAll(properties[newIndex].keySet());

				// 显示
				for (String removedKey : removedKeys) {
					log.info("删除配置项: [" + removedKey + " : " + properties[oldIndex].get(removedKey) + "]");
				}
				for (String addedKey : addedKeys) {
					log.info("新增配置项: [" + addedKey + " : " + properties[newIndex].get(addedKey) + "]");
				}
				for (String mayModifiedKey : mayModifiedKeys) {
					String oldValue = properties[oldIndex].get(mayModifiedKey);
					String newValue = properties[newIndex].get(mayModifiedKey);
					if (!oldValue.equals(newValue)) {
						log.info("修改配置项: [" + mayModifiedKey + " : " + oldValue + " --> " + newValue + "]");
					}
				}
			}
		});
	}

	/**
	 * 加载配置
	 * @param index
	 * @throws Exception 
	 */
	private void load(int index) throws Exception {

		String[] unionLocation = null;
		if(locations == null) {
			if(location == null) {
				log.error("locations属性和location属性都为Null");
				return;
			} else {
				unionLocation = new String[]{location};
			}
		} else {
			if(location == null) {
				unionLocation = locations;
			} else {
				unionLocation = new String[locations.length + 1];
				System.arraycopy(locations, 0, unionLocation, 0, locations.length);
				unionLocation[locations.length] = location;
			}
		}

		for (String path : unionLocation) {
			BufferedInputStream in = null;
			try {
				Properties p = new Properties();
				in = new BufferedInputStream(new FileInputStream(path));
				p.load(in);
				for(Iterator<Map.Entry<Object,Object>> it = p.entrySet().iterator();it.hasNext();) {
					Map.Entry<Object,Object> me = it.next();
					properties[index].put(me.getKey().toString(), me.getValue().toString());
				}
			} catch (FileNotFoundException e) {
				log.error("配置文件[" + path + "]不存在", e);
				throw e;
			} catch (IOException e) {
				log.error("加载配置文件[" + path + "]出现错误", e);
				throw e;
			} finally {
				try {
					if(in != null) in.close();
				} catch (Exception e) {
					// nothing to do
				}
			}
		}
	}

	/**
	 * 销毁
	 */
	public void destroy() {
		if (timerFuture != null)
			timerFuture.cancel();
		properties[0].clear();
		properties[1].clear();
	}

	/**
	 * 获取配置
	 * 
	 * @param key
	 * @return
	 */
	public final String get(String key) {
		String v = properties[index].get(key);
		if (v == null) throw new IllegalStateException("没有名为:" + key + " 的配置属性");
		return v;
	}

	/**
	 * 获取int类型配置
	 * 
	 * @param key
	 * @return
	 */
	public final int getInt(String key) {
		return Integer.parseInt(get(key));
	}

	/**
	 * 获取long类型配置
	 * 
	 * @param key
	 * @return
	 */
	public final long getLong(String key) {
		return Long.parseLong(get(key));
	}

	/**
	 * 获取double类型配置
	 * 
	 * @param key
	 * @return
	 */
	public final double getDouble(String key) {
		return Double.parseDouble(get(key));
	}

	/**
	 * 获取boolean类型配置
	 * 
	 * @param key
	 * @return
	 */
	public final boolean getBoolean(String key) {
		return Boolean.parseBoolean(get(key));
	}

	/**
	 * @param timer
	 *            the timer to set
	 */
	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	/**
	 * @param refreshInterval
	 *            the refreshInterval to set
	 */
	public void setRefreshInterval(long refreshInterval) {
		this.refreshInterval = refreshInterval;
	}

	/**
	 * @param location
	 *            the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @param locations
	 *            the locations to set
	 */
	public void setLocations(String[] locations) {
		this.locations = locations;
	}
}
