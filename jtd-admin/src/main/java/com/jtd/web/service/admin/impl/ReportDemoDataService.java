package com.jtd.web.service.admin.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtd.commons.dao.BaseDao;
import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.utils.DateUtil;
import com.jtd.utils.NumberUtil;
import com.jtd.utils.ReportUtil;
import com.jtd.web.dao.IAdDao;
import com.jtd.web.dao.ICampCitydCountDao;
import com.jtd.web.dao.ICampCredCountDao;
import com.jtd.web.dao.ICampdCountDao;
import com.jtd.web.dao.ICamphCountDao;
import com.jtd.web.dao.IPardCountDao;
import com.jtd.web.dao.IParhCountDao;
import com.jtd.web.dao.IReportDemoDataSettingDao;
import com.jtd.web.po.Ad;
import com.jtd.web.po.Campaign;
import com.jtd.web.po.Partner;
import com.jtd.web.po.ReportDemoDataSetting;
import com.jtd.web.po.count.CampCityd;
import com.jtd.web.po.count.CampCred;
import com.jtd.web.po.count.Campd;
import com.jtd.web.po.count.Camph;
import com.jtd.web.po.count.Pard;
import com.jtd.web.po.count.Parh;
import com.jtd.web.service.admin.IReportDemoDataService;
import com.jtd.web.service.impl.BaseService;

@Service
public class ReportDemoDataService extends BaseService<ReportDemoDataSetting>
		implements IReportDemoDataService {

	@Autowired
	private IReportDemoDataSettingDao reportDemoDataSettingDao;
	@Autowired
	private ICamphCountDao camphCountDao;
	@Autowired
	private ICampdCountDao campdCountDao;
	@Autowired
	private IParhCountDao parhCountDao;
	@Autowired
	private IPardCountDao pardCountDao;
	@Autowired
	private ICampCredCountDao campCredCountDao;
	@Autowired
	private ICampCitydCountDao campCitydCountDao;
	@Autowired
	private IAdDao adDao;

	@Override
	protected BaseDao<ReportDemoDataSetting> getDao() {
		CustomerContextHolder
				.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

		return reportDemoDataSettingDao;
	}

	public void saveCampDataPerHour(ReportDemoDataSetting rdds, Campaign camp,
			Partner partner,int hour) {

		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_COUNT);
		
		String sHour = hour < 10 ? ("0" + hour) : (hour + "");
		int date = DateUtil.getDateInt(new Date());
		int datehour = Integer.parseInt(date + sHour);

		int priceFen = camp.getPrice() != null ? camp.getPrice() : 100; // 广告活动的出价，分为单位，默认为1块
		int expendType = camp.getExpendType() != null ? camp.getExpendType()
				: 0; // 活动计价类型，0为cpm，1为cpc。默认为cmp2
		// * 需不需要考虑日预算?

		// 毛利率:页面毛利设置 > 活动毛利设置 > 广告主毛利设置 > 30%
		double grossProfitRatio = rdds.getGrossProfit() != null ? rdds
				.getGrossProfit().doubleValue()
				: (camp.getGrossProfit() != null ? (camp.getGrossProfit() / 100d)
						: (partner.getGrossProfit() != null ? partner
								.getGrossProfit() : 0.3d));

		Camph chParam = new Camph();
		chParam.setPartnerId(partner.getId());
		chParam.setCampId(camp.getId());
		chParam.setDate(date);
		chParam.setHour(hour);
		chParam.setDatehour(datehour);
		List<Camph> campHList = camphCountDao.listBy(chParam);
		
		/*
		 * * 如果该活动已经有这个小时的数据，就不用再继续执行
		 */
		if(campHList != null && campHList.size() > 0){
			return ;
		}
				
		// 添加到活动小时报表或新增
		Camph ch = getCampHourData(rdds, priceFen, expendType, grossProfitRatio);
		ch.setPartnerId(partner.getId());
		ch.setCampgroupId(camp.getGroupId());
		ch.setCampId(camp.getId());
		ch.setCampType(camp.getCampaignType());
		ch.setDate(date);
		ch.setHour(hour);
		ch.setDatehour(datehour);
		camphCountDao.insert(ch);
		
		//throw new RuntimeException("aaaaaa");

		// 累加到活动日报表或新增
		Campd cd = new Campd();
		cd.setPartnerId(ch.getPartnerId());
		cd.setCampgroupId(camp.getGroupId());
		cd.setCampId(camp.getId());
		cd.setCampType(camp.getCampaignType());
		cd.setDate(ch.getDate());
		List<Campd> campDList = campdCountDao.listBy(cd);
		if (campDList != null && campDList.size() == 1) {
			cd = campDList.get(0);

			cd.setBid(cd.getBid() != null ? cd.getBid() + ch.getBid() : 0);
			cd.setPv(cd.getPv() != null ? cd.getPv() + ch.getPv() : 0);
			cd.setUv(cd.getUv() != null ? cd.getUv() + ch.getUv() : 0);
			cd.setClick(cd.getClick() != null ? cd.getClick() + ch.getClick()
					: 0);
			cd.setUclick(cd.getUclick() != null ? cd.getUclick()
					+ ch.getUclick() : 0);
			cd.setArrpv(cd.getArrpv() != null ? cd.getArrpv() + ch.getArrpv()
					: 0);
			cd.setArruv(cd.getArruv() != null ? cd.getArruv() + ch.getArruv()
					: 0);
			cd.setExpend(cd.getExpend() != null ? cd.getExpend()
					+ ch.getExpend() : 0);
			cd.setCost(cd.getCost() != null ? cd.getCost() + ch.getCost() : 0);

			campdCountDao.update(cd);
		} else {

			cd.setBid(ch.getBid());
			cd.setPv(ch.getPv());
			cd.setUv(ch.getClick());
			cd.setClick(ch.getArrpv());
			cd.setUclick(ch.getUclick());
			cd.setArrpv(ch.getArrpv());
			cd.setArruv(ch.getArruv());
			cd.setExpend(ch.getExpend());
			cd.setCost(ch.getCost());

			campdCountDao.insert(cd);
		}

		creativeDataPerHour(ch,camp.getId(),priceFen,expendType,grossProfitRatio);
		partnerDataPerHour(partner.getId(), ch);
		cityDataPerHour(ch, camp.getId(), priceFen, expendType, grossProfitRatio);
		
	}

	/**
	 * 根据rdds的设置，生成一个活动一小时的数据
	 * 
	 * @param rdds
	 * @param priceFen
	 * @param expendType
	 * @param grossProfitRatio
	 * @return
	 */
	private Camph getCampHourData(ReportDemoDataSetting rdds, int priceFen,
			int expendType, double grossProfitRatio) {

		int pv = 0;
		if (rdds.getMaxPv() != null && rdds.getMinPv() != null) {
			pv = NumberUtil.getRandomInt(rdds.getMinPv(), rdds.getMaxPv());
		}

		int bid = pv * NumberUtil.getRandomInt(8, 11); // bid对pv的随机倍数，通过pv来倒算bid

		int uv = 0;
		if (rdds.getMaxUvRatio() != null && rdds.getMinUvRatio() != null) {
			int min = (int) (rdds.getMinUvRatio().floatValue() * 100);
			int max = (int) (rdds.getMaxUvRatio().floatValue() * 100);
			int uvRatio = NumberUtil.getRandomInt(min, max);
			uv = getNumberByPercent(pv, uvRatio);
		}

		int clickThroughRatio = NumberUtil.getRandomInt(8, 100); // ctr(即点击率)从万分之八到百分之一之间取一个随机数
		int click = (int) (pv * (clickThroughRatio / 10000f));

		int uclick = 0;
		if (rdds.getMaxUclickRatio() != null
				&& rdds.getMinUclickRatio() != null) {
			int min = (int) (rdds.getMinUclickRatio().floatValue() * 100);
			int max = (int) (rdds.getMaxUclickRatio().floatValue() * 100);
			int uclickRatio = NumberUtil.getRandomInt(min, max);
			uclick = getNumberByPercent(click, uclickRatio);
		}

		int arrpvRatio = NumberUtil.getRandomInt(60, 99); // 有效到达对点击数的占比
		int arrpv = getNumberByPercent(click, arrpvRatio);

		int arruv = arrpv;

		// 消耗
		int expend = ReportUtil.getExpend(priceFen, pv, click, expendType);
		// 成本
		int cost = ReportUtil.getCost(expend, grossProfitRatio);

		Camph ch = new Camph();
		ch.setBid(bid);
		ch.setPv(pv);
		ch.setUv(uv);
		ch.setClick(click);
		ch.setUclick(uclick);
		ch.setArrpv(arrpv);
		ch.setArruv(arruv);
		ch.setExpend(expend);
		ch.setCost(cost);

		return ch;
	}

	/**
	 * 取百分比
	 * 
	 * @param number
	 *            要取百分比的数
	 * @param ratio
	 *            用整数表示百分比，比如30就是百分之30
	 * @return
	 */
	private int getNumberByPercent(int number, int ratio) {
		return (int) (number * (ratio / 100f));
	}

	private void partnerDataPerHour(long partnerId, Camph camph) {
		
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_COUNT);

		Parh ph = new Parh();
		ph.setPartnerId(partnerId);
		ph.setDate(camph.getDate());
		ph.setHour(camph.getHour());
		ph.setDatehour(camph.getDatehour());

		List<Parh> parHList = parhCountDao.listBy(ph);

		// 累计到广告主小时报表或新增
		if (parHList != null && parHList.size() == 1) {
			ph = parHList.get(0);

			ph.setBid(ph.getBid() != null ? ph.getBid() + camph.getBid() : 0);
			ph.setPv(ph.getPv() != null ? ph.getPv() + camph.getPv() : 0);
			ph.setUv(ph.getUv() != null ? ph.getUv() + camph.getUv() : 0);
			ph.setClick(ph.getClick() != null ? ph.getClick() + camph.getClick() : 0);
			ph.setUclick(ph.getUclick() != null ? ph.getUclick() + camph.getUclick() : 0);
			ph.setArrpv(ph.getArrpv() != null ? ph.getArrpv() + camph.getArrpv() : 0);
			ph.setArruv(ph.getArruv() != null ? ph.getArruv() + camph.getArruv() : 0);
			ph.setExpend(ph.getExpend() != null ? ph.getExpend() + camph.getExpend() : 0);
			ph.setCost(ph.getCost() != null ? ph.getCost() + camph.getCost() : 0);

			parhCountDao.update(ph);

		} else {

			ph.setBid(camph.getBid());
			ph.setPv(camph.getPv());
			ph.setUv(camph.getUv());
			ph.setClick(camph.getClick());
			ph.setUclick(camph.getUclick());
			ph.setArrpv(camph.getArrpv());
			ph.setArruv(camph.getArruv());
			ph.setExpend(camph.getExpend());
			ph.setCost(camph.getCost());

			parhCountDao.insert(ph);

		}

		// 累计到广告主日报表或新增
		Pard pd = new Pard();
		pd.setPartnerId(partnerId);
		pd.setDate(camph.getDate());
		List<Pard> parDList = pardCountDao.listBy(pd);
		if (parDList != null && parDList.size() == 1) {
			pd = parDList.get(0);

			pd.setBid(pd.getBid() != null ? pd.getBid() + camph.getBid() : 0);
			pd.setPv(pd.getPv() != null ? pd.getPv() + camph.getPv() : 0);
			pd.setUv(pd.getUv() != null ? pd.getUv() + camph.getUv() : 0);
			pd.setClick(pd.getClick() != null ? pd.getClick() + camph.getClick() : 0);
			pd.setUclick(pd.getUclick() != null ? pd.getUclick() + camph.getUclick() : 0);
			pd.setArrpv(pd.getArrpv() != null ? pd.getArrpv() + camph.getArrpv() : 0);
			pd.setArruv(pd.getArruv() != null ? pd.getArruv() + camph.getArruv() : 0);
			pd.setExpend(pd.getExpend() != null ? pd.getExpend() + camph.getExpend() : 0);
			pd.setCost(pd.getCost() != null ? pd.getCost() + camph.getCost() : 0);

			pardCountDao.update(pd);

		} else {

			pd.setBid(camph.getBid());
			pd.setPv(camph.getPv());
			pd.setUv(camph.getUv());
			pd.setClick(camph.getClick());
			pd.setUclick(camph.getUclick());
			pd.setArrpv(camph.getArrpv());
			pd.setArruv(camph.getArruv());
			pd.setExpend(camph.getExpend());
			pd.setCost(camph.getCost());

			pardCountDao.insert(pd);
		}

	}

	private void cityDataPerHour(Camph camph, long campId, int priceFen,
			int expendType, double grossProfitRatio) {
		
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_COUNT);
		
		HashSet<Long> cityKeySet = new HashSet<Long>();
		cityKeySet.add(1L); //北京
		cityKeySet.add(73L); //上海
		cityKeySet.add(252L); //广州
		cityKeySet.add(254L); //深圳
		cityKeySet.add(198L); //成都
		
		HashMap<Long, Integer> bidMap = distributeCampData(camph.getBid(),
				cityKeySet);
		HashMap<Long, Integer> pvMap = distributeCampData(camph.getPv(),
				cityKeySet);
		HashMap<Long, Integer> uvMap = distributeCampData(camph.getUv(),
				cityKeySet);
		HashMap<Long, Integer> clickMap = distributeCampData(
				camph.getClick(), cityKeySet);
		HashMap<Long, Integer> uclickMap = distributeCampData(
				camph.getUclick(), cityKeySet);
		HashMap<Long, Integer> arrpvMap = distributeCampData(
				camph.getArrpv(), cityKeySet);
		HashMap<Long, Integer> arruvMap = distributeCampData(
				camph.getArruv(), cityKeySet);
		
		for (Long cityId : cityKeySet) {
			
			int bid = bidMap.get(cityId);
			int pv = pvMap.get(cityId);
			int uv = uvMap.get(cityId);
			int click = clickMap.get(cityId);
			int uclick = uclickMap.get(cityId);
			int arrpv = arrpvMap.get(cityId);
			int arruv = arruvMap.get(cityId);
			int expend = ReportUtil.getExpend(priceFen, pv, click, expendType);
			int cost = ReportUtil.getCost(expend, grossProfitRatio);
			
			
			// 累计到地域日报表或新增
			CampCityd cityd = new CampCityd();
			cityd.setCampId(campId);
			cityd.setCityId(cityId);
			cityd.setDate(camph.getDate());
			List<CampCityd> citydList = campCitydCountDao.listBy(cityd);
			if (citydList != null && citydList.size() == 1) {
				cityd = citydList.get(0);

				cityd.setBid(cityd.getBid() != null ? cityd.getBid() + bid : 0);
				cityd.setPv(cityd.getPv() != null ? cityd.getPv() + pv : 0);
				cityd.setUv(cityd.getUv() != null ? cityd.getUv() + uv : 0);
				cityd.setClick(cityd.getClick() != null ? cityd.getClick() + click
						: 0);
				cityd.setUclick(cityd.getUclick() != null ? cityd.getUclick()
						+ uclick : 0);
				cityd.setArrpv(cityd.getArrpv() != null ? cityd.getArrpv() + arrpv : 0);
				cityd.setArruv(cityd.getArruv() != null ? cityd.getArruv() + arruv : 0);
				cityd.setExpend(cityd.getExpend() != null ? cityd.getExpend()
						+ expend : 0);
				cityd.setCost(cityd.getCost() != null ? cityd.getCost() + cost : 0);

				campCitydCountDao.update(cityd);

			} else {

				cityd.setBid(bid);
				cityd.setPv(pv);
				cityd.setUv(uv);
				cityd.setClick(click);
				cityd.setUclick(uclick);
				cityd.setArrpv(arrpv);
				cityd.setArruv(arruv);
				cityd.setExpend(expend);
				cityd.setCost(cost);

				campCitydCountDao.insert(cityd);
			}
		}
		
		

	}

	private void creativeDataPerHour(Camph camph, long campId, int priceFen,
			int expendType, double grossProfitRatio) {
		
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

		Ad adParam = new Ad();
		adParam.setCampaignId(campId);
		List<Ad> adList = adDao.listBy(adParam);
		if (adList != null && adList.size() > 0) {

			HashSet<Long> creativeKeySet = new HashSet<Long>();
			for (Ad ad : adList) {
				creativeKeySet.add(ad.getCreativeId());
			}

			HashMap<Long, Integer> bidMap = distributeCampData(camph.getBid(),
					creativeKeySet);
			HashMap<Long, Integer> pvMap = distributeCampData(camph.getPv(),
					creativeKeySet);
			HashMap<Long, Integer> uvMap = distributeCampData(camph.getUv(),
					creativeKeySet);
			HashMap<Long, Integer> clickMap = distributeCampData(
					camph.getClick(), creativeKeySet);
			HashMap<Long, Integer> uclickMap = distributeCampData(
					camph.getUclick(), creativeKeySet);
			HashMap<Long, Integer> arrpvMap = distributeCampData(
					camph.getArrpv(), creativeKeySet);
			HashMap<Long, Integer> arruvMap = distributeCampData(
					camph.getArruv(), creativeKeySet);

			CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_COUNT);
			
			for (Ad ad : adList) {
				
				long creativeId = ad.getCreativeId();

				int bid = bidMap.get(creativeId);
				int pv = pvMap.get(creativeId);
				int uv = uvMap.get(creativeId);
				int click = clickMap.get(creativeId);
				int uclick = uclickMap.get(creativeId);
				int arrpv = arrpvMap.get(creativeId);
				int arruv = arruvMap.get(creativeId);

				int expend = ReportUtil.getExpend(priceFen, pv, click, expendType);
				int cost = ReportUtil.getCost(expend, grossProfitRatio);

				// 累计到创意日报表或新增
				CampCred cred = new CampCred();
				cred.setCreativeId(creativeId);
				cred.setCampId(campId);
				cred.setDate(camph.getDate());
				List<CampCred> credList = campCredCountDao.listBy(cred);

				if (credList != null && credList.size() == 1) {
					cred = credList.get(0);

					cred.setBid(cred.getBid() != null ? cred.getBid() + bid : 0);
					cred.setPv(cred.getPv() != null ? cred.getPv() + pv : 0);
					cred.setUv(cred.getUv() != null ? cred.getUv() + uv : 0);
					cred.setClick(cred.getClick() != null ? cred.getClick() + click : 0);
					cred.setUclick(cred.getUclick() != null ? cred.getUclick() + uclick : 0);
					cred.setArrpv(cred.getArrpv() != null ? cred.getArrpv() + arrpv : 0);
					cred.setArruv(cred.getArruv() != null ? cred.getArruv() + arruv : 0);
					cred.setExpend(cred.getExpend() != null ? cred.getExpend() + expend : 0);
					cred.setCost(cred.getCost() != null ? cred.getCost() + cost : 0);

					campCredCountDao.update(cred);

				} else {

					cred.setBid(bid);
					cred.setPv(pv);
					cred.setUv(uv);
					cred.setClick(click);
					cred.setUclick(uclick);
					cred.setArrpv(arrpv);
					cred.setArruv(arruv);
					cred.setExpend(expend);
					cred.setCost(cost);

					campCredCountDao.insert(cred);
				}
			}
		}

	}

	/**
	 * 将活动的数值某项数值随机均摊
	 * 
	 * @param data
	 *            要均摊的数值
	 * @param keySet
	 *            所有均摊对象的主键的set
	 * @return
	 */
	private HashMap<Long, Integer> distributeCampData(int data,
			HashSet<Long> keySet) {

		HashMap<Long, Integer> map = new HashMap<Long, Integer>();

		// 1.先除以均摊对象数量得到一个平均百分比
		int averagePercent = 100 / keySet.size();
		int cnt = 0;
		int notLastSum = 0;
		for (Iterator<Long> iterator = keySet.iterator(); iterator.hasNext(); cnt++) {

			Long key = (Long) iterator.next();
			if (cnt != keySet.size() - 1) {
				//2.如果不是最后一个对象，在1到平均值之间找一个随机百分比，然后用它计算结果之后分配给对象
				int randomPercent = NumberUtil.getRandomInt(1, averagePercent);
				int num = getNumberByPercent(data, randomPercent);
				//3.并累计这些随机百分比
				notLastSum += num;
				map.put(key, num);
			} else {
				//3.如果是最后一个对象，用总数减去
				int lastNum = data - notLastSum;
				map.put(key, lastNum);
			}
		}

		return map;
	}

	public static void main(String[] args) {

		HashSet<Long> set = new HashSet<Long>();
		set.add(101L);
		set.add(102L);
		set.add(103L);
		set.add(104L);
		set.add(105L);
		set.add(106L);

		ReportDemoDataService s = new ReportDemoDataService();
		HashMap<Long, Integer> map = s.distributeCampData(1000, set);

		int sum = 0;
		Set<Long> keySet = map.keySet();
		for (Long key : keySet) {
			System.out.println(key + " " + map.get(key));
			sum += map.get(key);
		}

		System.out.println(sum);

	}
}
