package com.jtd.web.service.front.impl;

import com.jtd.commons.dao.BaseDao;
import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.web.constants.AuditStatus;
import com.jtd.web.constants.CatgSerial;
import com.jtd.web.constants.RoleType;
import com.jtd.web.dao.IPartnerDao;
import com.jtd.web.dao.IPartnerStatusDao;
import com.jtd.web.po.ActiveUser;
import com.jtd.web.po.Partner;
import com.jtd.web.po.UserPartner;
import com.jtd.web.service.front.IFrontPartnerService;
import com.jtd.web.service.front.IFrontUserPartnerService;
import com.jtd.web.service.impl.BaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 
 * @作者 duber
 * @版本 V1.0
 * @创建日期 2016年11月23日
 * @描述
 */
@Service
public class FrontPartnerService extends BaseService<Partner> implements IFrontPartnerService {

	@Autowired
	private IPartnerDao partnerDao;

	@Autowired
	private IFrontUserPartnerService frontUserPartnerService;

	@Autowired
	private IPartnerStatusDao partnerStatusDao;
	
	@Override
	protected BaseDao<Partner> getDao() {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		return partnerDao;
	}


	@Override
	public List<Partner> findPartnerSideList(ActiveUser activeUser) {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

		int roleId = Integer.parseInt(activeUser.getRoleId());
        /**
         * 超级管理员,管理员,运营总监,运营主管
         */
		boolean allPartnerFlag = (roleId == RoleType.ADMIN.getCode() || roleId == RoleType.MANAGER.getCode() || roleId == RoleType.OPERATE_MANAGER.getCode() || roleId == RoleType.OPERATE_DIRECTOR.getCode()) ;
        List<Partner> partners = new ArrayList<Partner>();
		if(allPartnerFlag) { // 所有广告主数据
			partners = partnerDao.listAll();
		}else { // 其它用户查询用户与广告主关系表

			UserPartner up = new UserPartner();
			up.setUserId(activeUser.getUserId().toString());

            // 查看当前用户下的广告主数据权限
            List<UserPartner> upList = frontUserPartnerService.listBy(up);
            List<Partner> partnerList = new ArrayList<Partner>();
            Map<String,Object> map = null;
            for(UserPartner userPartner: upList){

                if(roleId == RoleType.OPERATE.getCode()) { //运营角色级联查询子集
                    map = new HashMap<String, Object>();
                    map.put("id", userPartner.getPartnerId());
                    map.put("pid", "0");
                    if(userPartner.getPartnerId().equals("1")) {
                        //【jtd公司】不级联
                        Partner p = partnerDao.getById(Long.parseLong(userPartner.getPartnerId().toString()));
                        partners.add(p);
                    }else{
                        partnerList = partnerDao.listChildPartnerByMap(map);
                        for (Partner po : partnerList) {
                            partners.add(po);
                        }
                    }
                }else{ //其它角色只能查看直接子集
                    Partner p = partnerDao.getById(Long.parseLong(userPartner.getPartnerId().toString()));
                    partners.add(p);
                }

            }
		}
        List<Partner> listnewList = new ArrayList<Partner>();
        for (Partner all:partners) {
            boolean flag = true;
            for(Partner p:listnewList){
                if(p.getId().equals(all.getId())){
                    flag = false;
                }
            }
            if(flag){
                listnewList.add(all);
            }
        }
        return listnewList;
	}
	
	@Override
	public List<Long> checkPartnerWhenStartCampaign(long partnerId){
		
		ArrayList<Long> resultList = new ArrayList<Long>();
		
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		
		/** 根据所属广告主ID，获取渠道审核广告广告主列表，不是所有渠道都会为广告主投放广告，个别需要渠道审核，比如百度BES，channel与partner_status表关联 */
		List<Map<String, Object>> allAuditStatusList = partnerStatusDao.listAllChannelAuditStatusByPartnerId(partnerId);
		
		if(allAuditStatusList != null){
			
			for (Map<String, Object> partnerStatusDataMap : allAuditStatusList) {
				
				String channelCode = (String)partnerStatusDataMap.get("catgserial"); /** 渠道代码 ,channel表catgserial字段 */
				Integer auditStatus = (Integer)partnerStatusDataMap.get("status");  /** 渠道审核广告主状态，partner_status表status字段 */
				
				CatgSerial channelEnum = CatgSerial.fromCode(channelCode);   /** 根据渠道代码获取渠道枚举对象  */

				//百度要求广告主必须审核通过才能进行投放
				if(CatgSerial.BES == channelEnum){
					if(auditStatus != null && auditStatus.intValue() == AuditStatus.STATUS_SUCCESS.getCode()) /** 百度审核广告主状态为2，审核通过 */
					{
						resultList.add(CatgSerial.BES.getChannelId());
					}
				}
				if(CatgSerial.TANX == channelEnum){
					//TODO:淘宝的广告主投放要求
				}
				//万流客不需要广告主审核
				if(CatgSerial.VAM == channelEnum){
					resultList.add(CatgSerial.VAM.getChannelId());
				}
				//灵集不需要广告主审核
				if(CatgSerial.XTRADER == channelEnum){
					resultList.add(CatgSerial.XTRADER.getChannelId());
				}
				//互众要求广告主必须审核通过才能进行投放
				if(CatgSerial.HZ == channelEnum){
					if(auditStatus != null && auditStatus.intValue() == AuditStatus.STATUS_SUCCESS.getCode()){
						resultList.add(CatgSerial.HZ.getChannelId());
					}
				}
				
				/** 新汇达通，默认不需要广告主审核*/
				if(CatgSerial.XHDT == channelEnum)
				{
					resultList.add(CatgSerial.XHDT.getChannelId());
				}
				
				/** adVew*/
				if(CatgSerial.ADVIEW == channelEnum)
				{
					resultList.add(CatgSerial.ADVIEW.getChannelId());
				}
				
				
				/** 安沃，默认不需要广告主审核 */
				if(CatgSerial.ADWO == channelEnum)
				{
					resultList.add(CatgSerial.ADWO.getChannelId());
				}
			}
			
		}
		
		return resultList ;
	}
}
