package com.jtd.web.service.front.impl;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtd.commons.dao.BaseDao;
import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.commons.page.Pagination;
import com.jtd.utils.DateUtil;
import com.jtd.web.dao.IParhCountDao;
import com.jtd.web.po.count.Parh;
import com.jtd.web.service.front.IFrontParhCountService;
import com.jtd.web.service.impl.BaseService;

/**
 * 广告主按小时查询Service
 * @作者 duber
 * @版本 V1.0
 * @创建日期 2016年11月2日
 * @描述
 */
@Service
public class FrontParhCountService extends BaseService<Parh> implements IFrontParhCountService {

	@Autowired
	private IParhCountDao parhCountDao;
	
	@Override
	protected BaseDao<Parh> getDao() {
		// 选择统计库查询
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_COUNT);
		return parhCountDao;
	}

	@Override
	public List<Parh> listByMap(Map<String, Object> phMap,boolean isAllDay) {
		// 选择统计库查询
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_COUNT);
		List<Parh> parhList = parhCountDao.listByMap(phMap);

		//如果没有数据补零

		String today = phMap.get("today").toString();
		String otherDay = phMap.get("otherDay").toString();
		String partnerId = phMap.get("partnerId").toString();
		List<Parh> ret = fillParh(parhList,today,otherDay,partnerId,isAllDay);
		Collections.sort(ret, new Comparator<Parh>() {
			@Override
			public int compare(Parh o1, Parh o2) {
				if(o1.getHour() < o2.getHour()){
					return 1;
				}
				if(o1.getHour() == o2.getHour()){
					return 0;
				}
				return -1;
			}
		});
		return ret;
	}



	@Override
	public Pagination<Parh> findFullPageBy(Map<String, Object> phMap, Integer pageNo, Integer pageSize,boolean isAllDay) {
		List<Parh> parhList = this.listByMap(phMap,isAllDay);//page.getList();

		//根据页数显示数据
		pageNo = null == pageNo ? 1 : pageNo;
		pageSize = null == pageSize ? 20 : pageSize;
		int start = (pageNo-1)*pageSize;
		int end = pageNo*pageSize;
		end = end <= parhList.size() ? end : parhList.size();

		List<Parh> ret = new ArrayList<Parh>();
		Parh parh = null;

		for(int i = start;i<end;i++){
			parh = parhList.get(i);
			ret.add(parh);
		}

		Pagination<Parh> page = new Pagination<Parh>(pageNo,pageSize,parhList.size(),ret);
		return page;
	}

	private List<Parh> fillParh(List<Parh> parhList, String today, String otherDay,String partnerId,boolean isAllDay) {

		List<Parh> ret = new ArrayList<Parh>();
		int hour = 0;
		if(isAllDay){
			hour = 23;
		}else {
			hour = DateUtil.getHourByDay(); //当前时间
		}

		Parh todayParh = null;
		Parh otherParh = null;

		Parh defaultParh = null;
		Parh defaultOtherParh = null;
		for(int i=0;i<=hour;i++){
			//默认
			defaultParh = new Parh();
			defaultParh.setArrpv(0);
			defaultParh.setArruv(0);
			defaultParh.setBid(0);
			defaultParh.setClick(0);
			defaultParh.setCost(0);
			defaultParh.setDatehour(0);
			defaultParh.setExpend(0);
			defaultParh.setUclick(0);
			defaultParh.setUv(0);
			defaultParh.setPv(0);
			defaultParh.setPartnerId(Long.parseLong(partnerId));
			defaultParh.setHour(i);
			defaultParh.setDate(Integer.parseInt(today));

			defaultOtherParh = new Parh();
			defaultOtherParh.setArrpv(0);
			defaultOtherParh.setArruv(0);
			defaultOtherParh.setBid(0);
			defaultOtherParh.setClick(0);
			defaultOtherParh.setCost(0);
			defaultOtherParh.setDatehour(0);
			defaultOtherParh.setExpend(0);
			defaultOtherParh.setUclick(0);
			defaultOtherParh.setUv(0);
			defaultOtherParh.setPv(0);
			defaultOtherParh.setPartnerId(Long.parseLong(partnerId));
			defaultOtherParh.setHour(i);
			defaultOtherParh.setDate(Integer.parseInt(otherDay));

			if(parhList.size()>0){

				todayParh = new Parh();
				otherParh = new Parh();

				todayParh.setDate(Integer.parseInt(today));
				todayParh.setHour(i);

				otherParh.setDate(Integer.parseInt(otherDay));
				otherParh.setHour(i);

				boolean todayFlag = false;
				boolean otherFlag = false;
				for(Parh p: parhList){

					if(todayParh.getHour().equals(p.getHour())  && todayParh.getDate().equals(p.getDate()) ){
						todayFlag = true;
						todayParh = p;
						continue;
					}

					if(otherParh.getHour().equals(p.getHour())  && otherParh.getDate().equals(p.getDate()) ){
						otherFlag = true;
						otherParh = p;
						continue;
					}

					if(todayFlag && otherFlag){
						break;
					}

				}

				if(todayFlag){
					ret.add(todayParh);
				}else { //当天没有数据
					ret.add(defaultParh);
				}

				if(otherFlag){
					ret.add(otherParh);
				}else { //对比日没有数据
					ret.add(defaultOtherParh);
				}
			}else{
				ret.add(defaultParh);
				ret.add(defaultOtherParh);
			}

		}

		return ret;

	}
}
