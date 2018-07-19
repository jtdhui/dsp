package com.jtd.web.service.front;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;

import com.jtd.web.constants.CookieType;
import com.jtd.web.po.QualiDocType;
import com.jtd.web.po.RetargetPacket;
import com.jtd.web.po.SysUser;

public interface IFrontAccountService {

	/**
	 * 到account_info.jsp
	 * 
	 * @param userId
	 * @param model
	 */
	public void toInfo(long partnerId, Model model);

	/**
	 * 接收account_info.jsp的提交
	 * 
	 * @param userId
	 * @param model
	 */
	public long infoSave(SysUser user);

	/**
	 * 修改用户密码
	 * 
	 * @param userId
	 * @param inputOldPwd
	 * @param newPwd
	 * @return
	 */
	public long changePassword(long userId, String inputOldPwd, String newPwd);

	/**
	 * 到account_qualidoc.jsp
	 * 
	 * @param userId
	 * @return
	 */
	public void toQualiDoc(long partnerId, Model model);

	/**
	 * 通过客户选择资质（客户）类型返回对应的资质种类列表
	 * 
	 * @param qualiDocCustomerTypeId
	 * @return
	 */
	public List<QualiDocType> listQualiDocTypeByCustomerType(
            long qualiDocCustomerTypeId);

	/**
	 * 保存上传的资质文件，并写入quali_doc表
	 * 
	 * @param partnerId
	 * @param request
	 * @return 
	 * @throws Exception
	 */
	public String saveQualiDoc(long uploadUserId, long partnerId,
                               HttpServletRequest request) throws Exception;

	/**
	 * 删除资质文件记录
	 * 
	 * @param partnerId
	 * @param qualiDocTypeId
	 */
	public void deleteQualiDoc(long partnerId, long qualiDocTypeId);

	/**
	 * 到account_finance.jsp
	 * 
	 * @param userId
	 * @param model
	 */
	public void toFinance(long partnerId, Model model,
                          Map<String, Object> paramMap, Integer pageNo, Integer pageSize);
	
	/**
	 * 获取财务明细excel
	 * 
	 * @param params
	 * @param response
	 */
	public void getFinanceExcel(HashMap<String, Object> params,
                                HttpServletResponse response);
	
	/**
	 * 查找partner所有的访客找回包
	 * 
	 * @param partnerId
	 * @param partnerName
	 * @return
	 */
	public void getRetargetPacketMap(Model model, long partnerId, String partnerName);
	
	/**
	 * 插入访客找回包
	 * 
	 * @param rp
	 * @param cookieType
	 */
	public void insertRetargetPacketMap(RetargetPacket rp, CookieType cookieType);
	
}
