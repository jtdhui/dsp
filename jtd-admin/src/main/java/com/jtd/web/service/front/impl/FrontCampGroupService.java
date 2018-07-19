package com.jtd.web.service.front.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtd.commons.dao.BaseDao;
import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.web.dao.ICampGroupDao;
import com.jtd.web.dao.ICampaignDao;
import com.jtd.web.jms.ChangeCampGrpMsg;
import com.jtd.web.po.CampGroup;
import com.jtd.web.po.Campaign;
import com.jtd.web.service.IMQConnectorService;
import com.jtd.web.service.front.IFrontCampGroupService;
import com.jtd.web.service.impl.BaseService;

@Service
public class FrontCampGroupService extends BaseService<CampGroup> implements IFrontCampGroupService {

	@Autowired
	private ICampGroupDao campGroupDao;
	
	@Autowired
	private ICampaignDao campaignDao;
	
	@Autowired
	private IMQConnectorService mQConnectorService;
	
	@Override
	protected BaseDao<CampGroup> getDao() {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		return campGroupDao;
	}

	@Override
	public CampGroup getByMap(CampGroup cg) {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		return campGroupDao.getByMap(cg);
	}
	
	/**
	 * 发送消息到引擎
	 * @param entity
	 */
	@Override
	public void sendCampGroupMessage(CampGroup campGroup){
		ChangeCampGrpMsg changeCampGrpMsg = new ChangeCampGrpMsg();
		changeCampGrpMsg.setGroupid(campGroup.getId());
		changeCampGrpMsg.setDailyBudgetGoal(campGroup.getDailybudget());
		changeCampGrpMsg.setDailyClickGoal(campGroup.getDailyclick());
		changeCampGrpMsg.setDailyPvGoal(campGroup.getDailypv());
		changeCampGrpMsg.setTotalBudgetGoal(campGroup.getTotalbudget());
		changeCampGrpMsg.setTotalClickGoal(campGroup.getTotalclick());
		changeCampGrpMsg.setTotalPvGoal(campGroup.getTotalpv());
		mQConnectorService.sendMessage(changeCampGrpMsg);
	}

	@Override
	public void saveCampaign(Campaign entity) {
		campaignDao.insert(entity);
	}

}
