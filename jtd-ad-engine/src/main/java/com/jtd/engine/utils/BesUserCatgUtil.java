package com.jtd.engine.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p>百度用户分类工具类</p>
 */
public class BesUserCatgUtil {

	private static final String IDFILE = "resources/data/besUserCatg.db";
	private Map<Long, Integer> idMap = new HashMap<Long, Integer>();

	public void init() {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(IDFILE));
			for (String line = reader.readLine(); line != null; line = reader.readLine()) {
				String[] fileds = line.split("\t");
				if (fileds.length < 2) continue;
				idMap.put(Long.parseLong(fileds[0]), Integer.parseInt(fileds[1]));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (Exception e) {
				}
		}
	}

	public Integer getIdFromBedCatgId(Long besId) {
		return idMap.get(besId);
	}
}
