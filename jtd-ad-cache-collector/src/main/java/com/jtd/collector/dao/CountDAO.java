package com.jtd.collector.dao;

import java.util.Map;
import java.util.Set;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月12日
 * @项目名称 dsp-cache-collector
 * @描述 <p></p>
 */
public interface CountDAO {

	/**
	 * 统计PV 点击和到达
	 * @param campid
	 * @param creativeid
	 */
	public void increBid(long partnerid, long groupid, long campid, long creativeid, int increBy);
	public void increPv(long partnerid, long groupid, long campid, long creativeid, boolean isNew);
	public void increClick(long partnerid, long groupid, long campid, long creativeid, boolean isNew);
	public void increLanding(long partnerid, long groupid, long campid, long creativeid, boolean isNew);

	/**
	 * cost和expend都是分/CPM，如果是CPC计费的，expend是1000*单个点击扣费
	 * @param campid
	 * @param creativeid
	 * @param cost
	 */
	public void increCost(long partnerid, long groupid, long campid, long creativeid, int cost);
	public void increExpend(long partnerid, long groupid, long campid, long creativeid, int expend);

	/**
	 * 获取活动小时的统计值
	 * @param campid
	 * @return
	 */
	public int[] getCampHourCount(long campid, int yyyyMMdd, int hour);

	/**
	 * 获取活动/素材的日期统计
	 * @param campid
	 * @param creativeid
	 * @param yyyyMMdd
	 * @return
	 */
	public int[] getCampCreativeDayCount(long campid, long creativeid, int yyyyMMdd);

	/**
	 * 获取活动的日期统计
	 * @param campid
	 * @param yyyyMMdd
	 * @return
	 */
	public int[] getCampDayCount(long campid, int yyyyMMdd);

	/**
	 * 获取推广组的日期统计
	 * @param yyyyMMdd
	 * @return
	 */
	public int[] getCampGrpDayCount(long groupid, int yyyyMMdd);

	/**
	 * 获取活动的总的统计
	 * @param campid
	 * @return
	 */
	public long[] getCampTotalCount(long campid);

	/**
	 * 获取推广组的总的统计
	 * @return
	 */
	public long[] getCampGrpTotalCount(long groupid);

	/**
	 * 获取当前活动的1000*点击单价
	 * @param campid
	 * @return
	 */
	public int getCampCostPerClick(long campid);

	/**
	 * 获取当前在统计的所有的ID
	 * key  partnerid + "_" + groupid + "_" + campid  value [creativeid]
	 * @return
	 */
	public Map<String, Set<Long>> getAllIds();
	public void removeId(String id);
}
