package com.jtd.web.service.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;

import com.jtd.commons.page.Pagination;
import com.jtd.web.exception.PlatformException;
import com.jtd.web.po.ActiveUser;
import com.jtd.web.po.Partner;
import com.jtd.web.po.QualiDoc;
import com.jtd.web.service.IBaseService;

public interface IAdminQualiDocService extends IBaseService<QualiDoc> {

	/**
	 * 分页列表查询
	 * 
	 * @param paramMap
	 * @return
	 * @throws PlatformException  
	 */
	public Pagination<Map<String, Object>> findPageMap(Map<String, Object> paramMap, Integer pageNo, Integer pageSize)
			throws PlatformException;

	/**
	 * 运营人员-资质列表
	 * @param paraMap
	 * @param pageNo
	 * @param pageSize
	 * @param user TODO
	 * @param user TODO
	 * @return
	 */
	public void findOperation(Model model , Map<String, Object> paraMap, Integer pageNo, Integer pageSize , HttpServletResponse response, ActiveUser user)
			throws PlatformException;
	/**
	 * 查看用户的资质详情并可以进行审核
	 * 
	 * @param partnerId
	 * @return
	 * @throws PlatformException
	 */
	public HashMap<String, Object> toAudit(long partnerId)
			throws PlatformException;

	/**
	 * 保存内部审核结果 & 提交adx审核
	 * 
	 * @param partnerId
	 * @param auditStatus
	 * @param rejectRemark
	 */
	public void saveAudit(long auditUserId, long partnerId, int internalAuditStatus,
			String internalRejectRemark);
	
	/**
	 * 提交到adx（如果channelId为空则发送所有渠道）
	 * 
	 * @param submitUserId
	 * @param partnerId
	 * @param channelId
	 */
	public void submitToChannel(long submitUserId, long partnerId, Long channelId) ;

	/**
	 * 重新发送渠道审核相关MQ
	 * 
	 * @param partnerId
	 * @param channelId
	 */
	public void sendChannelAuditMQ(long partnerId, Long channelId);
	
	/**
	 * 重新发送广告主资质内部审核相关MQ
	 * 
	 * @param partnerId
	 * @param channelId
	 */
	public void sendInternalAuditMQ(long partnerId);
	
	/**
	 * 按id查询
	 * 
	 * @param id
	 * @return
	 */
	public QualiDoc getById(long id);

	/**
	 * 查询资质审核信息列表
	 * 
	 * 
	 * */
	public List<Map<String, Object>> findChildren(Map<String, Object> paraMap);
	
	/**
	 * 从user的可视数据范围内，返回一个按层级排列好的partner的list (pid=0的partner为第一级)
	 * 
	 * @param partnerId
	 * @param needSelf TODO
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> listPartnerHierarchy(Long partnerId, boolean needSelf)
			throws PlatformException;
	
}
