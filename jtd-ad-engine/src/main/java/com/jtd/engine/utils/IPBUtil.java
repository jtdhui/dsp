package com.jtd.engine.utils;

import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.*;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p></p>
 */
public class IPBUtil {
	
	private static final String IPFILE = "resources/data/superadmin.csv";
	private static final String MAPFILE = "resources/data/ipbcode2bia.db";
//	private static final String IPFILE = "/Users/xutao/012IdeaProjects/jtdx/jtd-ad-engine/target/classes/data/superadmin.csv";
//	private static final String MAPFILE = "/Users/xutao/012IdeaProjects/jtdx/jtd-ad-engine/target/classes/data/ipbcode2bia.db";

	private Map<String, Integer> cityIdMap = new HashMap<String, Integer>();
	private IP[] ipsegs;
	private long[] ipstarts;

	public void init() {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(IPFILE));
			List<IP> ips = new ArrayList<IP>();
			for(String line = reader.readLine(); line!=null; line = reader.readLine()){
				String[] fileds = line.split(",");
				if(fileds.length != 4){
					System.out.println("格式错误: " + line);
				}
				ips.add(new IP(str2Ip(fileds[0]), str2Ip(fileds[1]), fileds[2].trim(), fileds[3].trim()));
			}
			ipsegs = ips.toArray(new IP[ips.size()]);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) try { reader.close(); } catch (Exception e) {}
		}
		//给ipsegs  数组排序
		Arrays.sort(ipsegs, new Comparator<IP>() {
			@Override
			public int compare(IP o1, IP o2) {
				if(o1.ipstart > o2.ipstart) return 1;
				if(o1.ipstart < o2.ipstart) return -1;
				return 0;
			}
		});
		//把所有的ip开始值放到pistarts数组中
		ipstarts = new long[ipsegs.length];
		for(int i = 0 ; i < ipsegs.length; i++) {
			ipstarts[i] = ipsegs[i].ipstart;
		}

		try {
			//读取ip映射库，经过下面的操作，在cityIdMap集合中存储的数据格式为
			//{1156110000=1,110110001=1}其中key是ip地址通过str2ip方法计算后的唯值，value是城市编码，1表示北京
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(MAPFILE), "UTF-8"));
			for(String line = reader.readLine(); line!=null; line = reader.readLine()){
				String[] fileds = line.split("\t");
				if(fileds.length != 4){
					System.out.println("格式错误: " + line);
				}
				if(!"null".equals(fileds[2])) {
					cityIdMap.put(fileds[2], Integer.parseInt(fileds[1]));
				}
				if(!"null".equals(fileds[3])) {
					List<String> l = JSON.parseArray(fileds[3], String.class);
					for(String eduid : l) {
						cityIdMap.put(eduid, Integer.parseInt(fileds[1]));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) try { reader.close(); } catch (Exception e) {}
		}
	}

	public Integer getCityIdByIp(String ip) {
		String[] ipcode = getIPCode(ip);
		if(ipcode == null) return null;
		Integer ret = cityIdMap.get(ipcode[0]);
		if(ret != null) return ret;
		ret = cityIdMap.get(ipcode[1]);
		return ret;
	}

	public String[] getIPCode(String ip) {
		long lip = str2Ip(ip);
		int index = Arrays.binarySearch(ipstarts, lip);
		if(index > 0) {
			if(lip >= ipsegs[index].ipstart && lip <= ipsegs[index].ipend) {
				return ipsegs[index].ipcode;
			} else {
				return null;
			}
		} else {
			index = -index - 2;
			if(index >= 0) {
				if(index >= ipsegs.length) return null;
				else return ipsegs[index].ipcode;
			} else {
				return null;
			}
		}
	}

	public String getAreaCode(String ip) {
		long lip = str2Ip(ip);
		int index = Arrays.binarySearch(ipstarts, lip);
		if(index > 0) {
			if(lip >= ipsegs[index].ipstart && lip <= ipsegs[index].ipend) {
				return ipsegs[index].areacode;
			} else {
				return null;
			}
		} else {
			index = -index - 2;
			if(index >= 0) {
				if(index >= ipsegs.length) return null;
				else return ipsegs[index].areacode;
			} else {
				return null;
			}
		}
	}

	public String getEduCode(String ip) {
		long lip = str2Ip(ip);
		int index = Arrays.binarySearch(ipstarts, lip);
		if(index > 0) {
			if(lip >= ipsegs[index].ipstart && lip <= ipsegs[index].ipend) {
				return ipsegs[index].areacode;
			} else {
				return null;
			}
		} else {
			index = -index - 2;
			if(index >= 0) {
				if(index >= ipsegs.length) return null;
				else return ipsegs[index].educode;
			} else {
				return null;
			}
		}
	}

	/**
	 * 点分IP转整型IP,转不成功返回-1
	 * @param str
	 * @return
	 */
	private final long str2Ip(String str) {
		if (null == str) return -1;
		String[] strs = str.split("[.]");
		if (strs.length != 4) {
			return -1;
		}
		try {
			int[] ips = new int[4];
			for (int i = 0; i < 4; i++) {
				ips[i] = Integer.parseInt(strs[i]);
			}
			return (long)ips[0] << 24 | ips[1] << 16 | ips[2] << 8 | ips[3];
		} catch (Exception e) {
			return -1;
		}
	}

	private class IP {
		private long ipstart;
		private long ipend;
		private String areacode;
		private String educode;
		private String[] ipcode;
		private IP(long ipstart, long ipend, String areacode, String educode) {
			this.ipstart = ipstart;
			this.ipend = ipend;
			this.areacode = areacode;
			this.educode = educode;
			this.ipcode = new String[]{areacode, educode};
		}
	}

	public static void main(String[] args) {
		IPBUtil u = new IPBUtil();
		u.init();
		System.out.println(u.str2Ip("192.168.1.1"));
		System.out.println(u.getCityIdByIp("218.30.29.255"));
	}
}
