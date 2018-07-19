package com.jtd.collector.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月12日
 * @项目名称 dsp-cache-collector
 * @描述 <p>加载交易趋势数据</p>
 */
@Component
public class TrafficHoursTrendsUtil {
	
	private static final Log log = LogFactory.getLog(TrafficHoursTrendsUtil.class);
	
	private int[] trends = new int[24];
	
	@PostConstruct
	public void init() {
		BufferedReader r = null;
		try {
			r = new BufferedReader(new InputStreamReader(new FileInputStream("resources/data/trafficHoursTrends.db")));
			for(String line = r.readLine(); line != null; line = r.readLine()) {
				if(line.trim().length() == 0) continue;
				String[] fs = line.split(":");
				if(fs.length != 2) {
					log.error("格式不正确: " + line);
				}
				int h = Integer.parseInt(fs[0].trim());
				int t = Integer.parseInt(fs[1].trim());
				trends[h] = t;
				log.debug("流量趋势 " + h + ": " + t);
			}
		} catch (Exception e) {
			log.error("加载trafficHoursTrends.db发生错误" + e);
		} finally {
			if(r != null) try { r.close(); } catch (IOException e) {}
		}
	}

	/**
	 * @param curhour
	 * @param curminute
	 * @param hours
	 * @param dayleft
	 * @return
	 */
	public int calc5MinuteBudget(int curhour, int curminute, String hours, int dayleft) {

		int sum = 0;

		// 剩下的小时的权重
		for (int i = curhour + 1; i < 24; i++) if (hours.charAt(i) == '1') sum += trends[i];

		// 当前小时剩下的分钟的权重
		double curHourLeftWeight = (trends[curhour] / 60.0) * (60 - curminute);

		double totalW = sum + curHourLeftWeight;

		// 未来5分钟的权重
		double w5 = trends[curhour] / 12.0;

		// 未来5分钟需要完成的预算
		return new BigDecimal(dayleft).multiply(new BigDecimal(w5)).divide(new BigDecimal(totalW), 0, RoundingMode.CEILING).intValue();
	}
	
	/**
	 * @param curhour				当前小时数
	 * @param curminute				当前分钟数
	 * @param hours					活动的投放时段
	 * @param dayleft				剩余的投放预算
	 * @return 返回未来一分钟应该消耗的金额
	 */
	public int calc1MinuteBudget(int curhour, int curminute, String hours, int dayleft) {
		//除了当前小时，剩下小时的权重
		int sum = 0;

		// z剩下的小时的权重
		for (int i = curhour + 1; i < 24; i++) if (hours.charAt(i) == '1') sum += trends[i];

		// z,当前小时剩下的分钟的权重
		// 1、使用当前小时的权重 除以 60，得到当前小时权重，分散到每分钟时的值，就是每分钟的权重
		// 2、使用 60-当前分钟数，得到的是当前小时，还剩下多少分钟
		// 使用第一个值 乘以 第二个值，得到的是剩余分钟的权重
		double curHourLeftWeight = (trends[curhour] / 60.0) * (60 - curminute);
		//剩下小时的权重+当前小时剩下分钟权重
		double totalW = sum + curHourLeftWeight;

		// z，未来1分钟的权重
		double w1 = trends[curhour] / 60.0;

		// z，未来1分钟需要完成的预算
		//当前剩余的预算 * 未来一分钟的权重 / （剩下小时权重+当前小时剩下分钟权重)
		return new BigDecimal(dayleft).multiply(new BigDecimal(w1)).divide(new BigDecimal(totalW), 0, RoundingMode.CEILING).intValue();
	}
	
	/**
	 * 计算当前小时的应该消耗的数目
	 * @param curhour				当前小时数
	 * @param hours					投放时段，值为 24个字符的字符串，由0或1组成，分别代表一天的24个小时，0表示
	 * @param totalleft				一天中还剩下多少pv量，这个数值，是用  活动目标pv+当前小时pv数-当天已经投过的pv数。
	 * @return
	 */
	public int calcHourNum(int curhour, String hours, long totalleft) {
		//从投放趋势表中，按照活动设置的投放时段(就是哪个小时投放，哪个小时不投放)，将趋势表中该时段对应的权重累加
		//放到sum变量中
		int sum = 0;

		// 当天包含当前小时剩下的小时的权重
		for (int i = curhour; i < 24; i++){
			if (hours.charAt(i) == '1'){
				sum += trends[i];
			}
		} 

		// 最后一小时不限量
		//例如：hours
		if(sum == 0) return Integer.MAX_VALUE;

		// 当前小时的权重
		int curw = trends[curhour];

		// 返回当前小时应该消耗的数量
		//先用投放趋势数据中的当前 小时 权重，除以 活动 所选时段权重的总和，得到当前小时要投放的比例，然后使用当天的剩余pv数 乘以 当前小时的投放比例，
		//则得到当前小时应该消耗的pv次数
		//当天剩余pv*(当前小时权重/投放时段权重总和)，这里的投放权重总和，是根据活动选择的投放时段而计算出来的。
		return new BigDecimal(totalleft).multiply(new BigDecimal(curw)).divide(new BigDecimal(sum), 0, RoundingMode.CEILING).intValue();
	}
}
