package com.jtd.engine.ad.matcher;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.jtd.engine.utils.SystemTime;
import com.jtd.engine.utils.Timer;
import com.jtd.engine.utils.TimerTask;

public class BesSettings {

	private static final Log log = LogFactory.getLog(BesSettings.class);
	private static final String SETTING_DIR = "/data/resource/settings/";

	private ConcurrentHashMap<Long, Setting> settings = new ConcurrentHashMap<Long, Setting>();

	// 计时器
	private Timer timer;

	// 系统时间
	private SystemTime systemTime;
	
	private String settingDir = SETTING_DIR;

	public void init() {

		try {
			loadSetting();
		} catch (Exception e) {
			log.error("加载Setting发生错误", e);
		}

		timer.timing(new TimerTask() {

			private int date = systemTime.getYyyyMMdd();
			private boolean loaded = true;

			@Override
			public void run() {
				
				int nowd = systemTime.getYyyyMMdd();
				if(date != nowd) {
					date = nowd;
					loaded = false;
				}
				int h = systemTime.getHour();
				if(!loaded && h == 7) {
					try {
						loadSetting();
					} catch (Exception e) {
						log.error("加载Setting发生错误", e);
					}
					loaded = true;
				}
			}
			
			@Override
			public Type type() { return Type.INTERVAL; }
			@Override
			public boolean isTriggerIndependently() { return false; }
			@Override
			public long delayOrIntervalMillis() { return 180000; }
		});
	}

	private void loadSetting() throws Exception {
		int h = systemTime.getHour();
		String ymd = String.valueOf(systemTime.getYyyyMMdd());
		if(h <= 6) {
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DATE, -1);
			ymd = new SimpleDateFormat("yyyyMMdd").format(c.getTime());
		}
		BufferedReader r = null;
		try {		
			r = new BufferedReader(new InputStreamReader(new FileInputStream(settingDir + ymd + ".txt")));
			for(String line = r.readLine(); line != null; line = r.readLine()) {
				try {
					Setting s = JSON.parseObject(line, Setting.class);
					settings.put(s.getSetting_id(), s);
				} catch (Exception e) {
					//log.error("解析setting发生错误: " + line, e);
					//log.error("解析setting发生错误: ");
				}
			}
		} finally {
			if(r != null) r.close();
		}
		log.info("加载" + settings.size() + "条Setting");
	}

	public List<String> getExcludeUrls(long settingid) {
		Setting s = settings.get(settingid);
		if(s == null) return null;
		return s.getExcluded_advertiser_website_url();
	}
	
	public List<Integer> getExcludeCatgids(long settingid) {
		Setting s = settings.get(settingid);
		if(s == null) return null;
		return s.getExcluded_product_category_id_list();
	}
	
	public List<Integer> getExcludeAdType(long settingid) {
		Setting s = settings.get(settingid);
		if(s == null) return null;
		return s.getExcluded_creative_type();
	}

	public static class Setting {

		private long setting_id;
		private List<String> excluded_advertiser_website_url;
		private List<Integer> excluded_product_category_id_list;
		private List<Integer> excluded_creative_type;

		/**
		 * @return the setting_id
		 */
		public long getSetting_id() {
			return setting_id;
		}

		/**
		 * @param setting_id
		 *            the setting_id to set
		 */
		public void setSetting_id(long setting_id) {
			this.setting_id = setting_id;
		}

		/**
		 * @return the excluded_advertiser_website_url
		 */
		public List<String> getExcluded_advertiser_website_url() {
			return excluded_advertiser_website_url;
		}

		/**
		 * @param excluded_advertiser_website_url
		 *            the excluded_advertiser_website_url to set
		 */
		public void setExcluded_advertiser_website_url(
				List<String> excluded_advertiser_website_url) {
			this.excluded_advertiser_website_url = excluded_advertiser_website_url;
		}

		/**
		 * @return the excluded_product_category_id_list
		 */
		public List<Integer> getExcluded_product_category_id_list() {
			return excluded_product_category_id_list;
		}

		/**
		 * @param excluded_product_category_id_list
		 *            the excluded_product_category_id_list to set
		 */
		public void setExcluded_product_category_id_list(
				List<Integer> excluded_product_category_id_list) {
			this.excluded_product_category_id_list = excluded_product_category_id_list;
		}

		/**
		 * @return the excluded_creative_type
		 */
		public List<Integer> getExcluded_creative_type() {
			return excluded_creative_type;
		}

		/**
		 * @param excluded_creative_type
		 *            the excluded_creative_type to set
		 */
		public void setExcluded_creative_type(List<Integer> excluded_creative_type) {
			this.excluded_creative_type = excluded_creative_type;
		}
	}

	/**
	 * @param timer the timer to set
	 */
	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	/**
	 * @param systemTime the systemTime to set
	 */
	public void setSystemTime(SystemTime systemTime) {
		this.systemTime = systemTime;
	}

	/**
	 * @param settingDir the settingDir to set
	 */
	public void setSettingDir(String settingDir) {
		this.settingDir = settingDir;
	}
}
