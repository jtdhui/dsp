package com.jtd.web.service.admin;

import java.util.Map;

import com.jtd.web.po.Ad;
import com.jtd.web.service.IBaseService;

/**
 * 
 * @作者 duber
 * @版本 V1.0
 * @创建日期 2016年10月28日
 * @描述 后台创意管理Servicer接口类
 */
public interface IAdminAdService extends IBaseService<Ad> {

	/**
	 * 后台渠道审核页面
	 * @param paraMap
	 * @return
	 */
	public Map<String, Object> findAdMapById(Map<String, Object> paraMap);

    public void saveAuditAdx(long campaign_id, String channelIds, Long adId, int internalAudit);

    public void batchAdAudit(String ad_ids);
}
