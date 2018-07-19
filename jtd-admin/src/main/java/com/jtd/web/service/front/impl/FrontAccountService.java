package com.jtd.web.service.front.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.alibaba.fastjson.JSONObject;
import com.jtd.commons.PropertyConfig;
import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.commons.page.Pagination;
import com.jtd.utils.DateUtil;
import com.jtd.utils.ExcelUtil;
import com.jtd.utils.FileUtil;
import com.jtd.utils.UserPwd;
import com.jtd.web.constants.AuditStatus;
import com.jtd.web.constants.Constants;
import com.jtd.web.constants.CookieType;
import com.jtd.web.constants.TradeType;
import com.jtd.web.dao.ICookieGidDao;
import com.jtd.web.dao.IPartnerAccFlowDao;
import com.jtd.web.dao.IPartnerDao;
import com.jtd.web.dao.IPartnerStatusDao;
import com.jtd.web.dao.IPartnerStatusQualiDocDao;
import com.jtd.web.dao.IQualiDocCustomerTypeChannelNeedDao;
import com.jtd.web.dao.IQualiDocCustomerTypeDao;
import com.jtd.web.dao.IQualiDocDao;
import com.jtd.web.dao.IQualiDocTypeDao;
import com.jtd.web.dao.IRetargetPacketDao;
import com.jtd.web.dao.ISysUserDao;
import com.jtd.web.dao.IUserPartnerDao;
import com.jtd.web.po.CookieGid;
import com.jtd.web.po.Partner;
import com.jtd.web.po.PartnerAccFlow;
import com.jtd.web.po.PartnerStatusPO;
import com.jtd.web.po.QualiDoc;
import com.jtd.web.po.QualiDocCustomerType;
import com.jtd.web.po.QualiDocType;
import com.jtd.web.po.RetargetPacket;
import com.jtd.web.po.SysUser;
import com.jtd.web.service.front.IFrontAccountService;

@Service
public class FrontAccountService implements IFrontAccountService {

	@Autowired
	private IUserPartnerDao userPartnerDao;

	@Autowired
	private IPartnerStatusDao partnerStatusDao;

	@Autowired
	private ISysUserDao userDao;

	@Autowired
	private IPartnerAccFlowDao partnerAccFlowDao;

	@Autowired
	private IPartnerDao partnerDao;

	@Autowired
	private IQualiDocDao qualiDocDao;

	@Autowired
	private IQualiDocTypeDao qualiDocTypeDao;

	@Autowired
	private IQualiDocCustomerTypeDao qualiDocCustomerTypeDao;

	@Autowired
	private IQualiDocCustomerTypeChannelNeedDao qualiDocCustomerTypeChannelNeedDao;

	@Autowired
	private IPartnerStatusQualiDocDao partnerStatusQualiDocDao;

	@Autowired
	private IRetargetPacketDao retargetPacketDao;

	@Autowired
	private ICookieGidDao cookieGidDDao;

	@Autowired
	private PropertyConfig propertyConfig;

	@Override
	public void toInfo(long userId, Model model) {

		CustomerContextHolder
				.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

		SysUser user = userDao.getById(userId);

		model.addAttribute("user", user);

	}

	@Override
	public long infoSave(SysUser user) {

		CustomerContextHolder
				.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

		user.setUpdateTime(new Date());

		return userDao.update(user);
	}

	@Override
	public long changePassword(long userId, String inputOldPwd, String newPwd) {

		CustomerContextHolder
				.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

		// 读取配置文件中散列的算法和次数
		String hashAlgorithmName = propertyConfig
				.getProperty("shiro.hashAlgorithmName");
		int hashIterations = Integer.parseInt(propertyConfig.getProperty(
				"shiro.hashIterations").trim());

		SysUser user = userDao.getById(userId);
		String oldPwd_Salt = user.getPwd();
		String oldSalt = user.getSalt();

		// 比较输入的原密码和数据库中的原密码
		if (UserPwd.password(inputOldPwd, oldSalt, hashAlgorithmName,
				hashIterations).equals(oldPwd_Salt)) {

			// 生成散列时的干扰字符串
			String newSalt = UserPwd.salt(5);
			// 生成用户密码
			String pwd = UserPwd.password(newPwd, newSalt, hashAlgorithmName,
					hashIterations);

			user = new SysUser();
			user.setId(userId);
			user.setSalt(newSalt);
			user.setPwd(pwd);

			return userDao.update(user);

		} else {
			return -1;
		}
	}

	@Override
	public void toQualiDoc(long partnerId, Model model) {

		CustomerContextHolder
				.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

		// 查询partner在各adx审核情况
		List<Map<String, Object>> partnerStatusList = partnerStatusDao
				.listAllChannelAuditStatusByPartnerId(partnerId);

		model.addAttribute("partnerStatusList", partnerStatusList);

		// 查询主体资质客户类型
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("isForMain", 1);
		List<QualiDocCustomerType> mainCustomerTypeList = qualiDocCustomerTypeDao
				.listByMap(params);

		model.addAttribute("mainCustomerTypeList", mainCustomerTypeList);

		// 查询可选资质客户类型
		params.put("isForMain", 0);
		List<QualiDocCustomerType> optionalCustomerTypeList = qualiDocCustomerTypeDao
				.listByMap(params);

		model.addAttribute("optionalCustomerTypeList", optionalCustomerTypeList);

		// 查询客户信息
		Partner partner = partnerDao.getById(partnerId);
		// 客户上传主体资质时所选客户类型
		model.addAttribute("qualiDocMainCustomerType",
				partner.getQualiDocMainCustomerType());
		// 客户上传可选资质时所选客户类型
		model.addAttribute("qualiDocOptionalCustomerType",
				partner.getQualiDocOptionalCustomerType());

		/**
		 * 查询partner已经上传的资质文件
		 */
		List<Map<String, Object>> uploadedDocList = qualiDocDao
				.listMapByPartnerId(partnerId);
		model.addAttribute("uploadedDocListJson",
				JSONObject.toJSONString(uploadedDocList));
		if (uploadedDocList != null) {
			for (Iterator<Map<String, Object>> iterator = uploadedDocList
					.iterator(); iterator.hasNext();) {
				Map<String, Object> map = (Map<String, Object>) iterator.next();
				if (map.get("doc_type_id") != null) {
					if ((long) map.get("doc_type_id") == 47) {
						model.addAttribute("icpUploadedDoc", map);
					}
					if ((long) map.get("doc_type_id") == 48) {
						model.addAttribute("idcardUploadedDoc", map);
					}
				}
			}
		}
		/**
		 * 查询partner已经上传的资质文件 end
		 */

		/**
		 * 取资质的内部审核信息
		 */
		if (uploadedDocList != null && uploadedDocList.size() > 0) {
			Map<String, Object> map = uploadedDocList.get(0);

			int status = (int) map.get("status");

			model.addAttribute("internalAuditStatus",
					AuditStatus.getName(status));
			model.addAttribute("rejectReason", map.get("remark"));
		} else {
			model.addAttribute("internalAuditStatus",
					AuditStatus.STATUS_NOTCOMMIT.getDesc());
		}
		/**
		 * 取资质的内部审核信息 end
		 */

		/**
		 * 判断用户是否能够修改或删除资质文件（如果没有任何渠道的待审核和审核通过记录，就可以重传或删除）(1为可以修改)
		 */
		int canUpdateQualiDoc = 0;  //先假定所有ADX都已经审核通过了
		params.put("partnerId", partnerId);
		
		//先看有没有ADX审核记录
		List<PartnerStatusPO> psList = partnerStatusDao.listByMap(params);
		if(psList != null && psList.size() > 0){
			for (Iterator<PartnerStatusPO> iterator = psList.iterator(); iterator
					.hasNext();) {
				PartnerStatusPO ps = (PartnerStatusPO) iterator.next();
				//只要有一家ADX未审核通过，就可以继续修改资质 (1为可以修改)
				if (ps.getStatus() != AuditStatus.STATUS_SUCCESS.getCode()) {
					canUpdateQualiDoc = 1;
				}
			}
			
		}
		//如果没有任何审核记录，资质文件可以修改
		else{
			canUpdateQualiDoc = 1 ;
		}
		model.addAttribute("canUpdateQualiDoc", canUpdateQualiDoc);
		/**
		 * 判断用户是否能够修改或删除资质文件（如果没有任何渠道的待审核和审核通过记录，就可以重传或删除）end
		 */
	}

	@Override
	public List<QualiDocType> listQualiDocTypeByCustomerType(
			long qualiDocCustomerTypeId) {

		CustomerContextHolder
				.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

		return qualiDocCustomerTypeChannelNeedDao
				.listDocTypeByCustomerTypeId(qualiDocCustomerTypeId);

	}

	@Override
	public String saveQualiDoc(long uploadUserId, long partnerId,
			HttpServletRequest request) throws Exception {

		CustomerContextHolder
				.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		String str = "";
		// 得到资质文件上传目录，并且每个partner一个文件夹
		if(Constants.QUALIDOC_DIR_PATH == null){
			return null;
		}
		String uploadDir = "";
		if(Constants.QUALIDOC_DIR_PATH.endsWith(File.separator)){
			uploadDir = Constants.QUALIDOC_DIR_PATH + partnerId ;
		}
		else{
			uploadDir = Constants.QUALIDOC_DIR_PATH + File.separator + partnerId ;
		}
		
		File dir = new File(uploadDir);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		// 将用户所选的资质客户类型都存上
		String qualiDocMainCustomerType = request
				.getParameter("qualiDocMainCustomerType");
		String qualiDocOptionalCustomerType = request
				.getParameter("qualiDocOptionalCustomerType");
		Partner partner = new Partner();
		partner.setId(partnerId);
		try {
			partner.setQualiDocMainCustomerType(Long
					.parseLong(qualiDocMainCustomerType));
		} catch (Exception e) {
			partner.setQualiDocMainCustomerType(-1L);
		}
		try {
			partner.setQualiDocOptionalCustomerType(Long
					.parseLong(qualiDocOptionalCustomerType));
		} catch (Exception e) {
			partner.setQualiDocOptionalCustomerType(-1L);
		}
		
		partnerDao.update(partner);

		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		
		if (multipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			// 遍历所有文件名
			Iterator<String> iter = multiRequest.getFileNames();
			for (Iterator<String> iterator = iter; iterator.hasNext();) {
				String fileName = (String) iterator.next();
				// 得到上传的文件
				MultipartFile file = multiRequest.getFile(fileName);
				if (file != null) {
					String orginFileName = file.getOriginalFilename();
					String extend = FileUtil.getExtendName(orginFileName);
					String fileType ="jpg,png";//上传文件的格式jpg,png
					if (orginFileName != "") {
						if ( !Arrays.<String> asList(fileType.split(",")).contains(extend.toLowerCase()))
						{
							str = "文件名称["+orginFileName+"]文件类型不正确，必须为" + fileType + "的文件！";
							break;
						}
						
						if (file.getSize() > 1024*1024*1.5)//大小不能超过1.5M
						{
							str = "文件名称["+orginFileName+"]文件大小超过限制！应小于1.5M";
							break;
						}
					}
					
					// 资质种类id，包含在file控件的name里
					Long qualiDocTypeId = null;
					// 资质对应的证件号
					String docNumber = "";
					// 资质对应的有效期
					String docValidDate = "";

					// icp和法人身份证是产品设计出来的，并没有渠道要求，属于特殊情况，所以单独对待
					if (fileName.equals("icp_file")
							|| fileName.equals("idcard_file")) {

						String qualiDocTypeRemark = fileName.split("_")[0];

						QualiDocType docType = qualiDocTypeDao
								.getByRemark(qualiDocTypeRemark);

						qualiDocTypeId = docType.getId();

						docNumber = request.getParameter(qualiDocTypeRemark
								+ "_number");
						docValidDate = request.getParameter(qualiDocTypeRemark
								+ "_valid_date");
					} else {
						// fileName的规则是 qualiDocTypeId + 下划线 + "file"
						qualiDocTypeId = Long.parseLong(fileName.split("_")[0]);

						docNumber = request.getParameter(qualiDocTypeId
								+ "_number");
						docValidDate = request.getParameter(qualiDocTypeId
								+ "_valid_date");
					}

					//String orginFileName = file.getOriginalFilename();

					// 如果没有上传文件则orginFileName会为空，则不做保存文件相关操作
					if (StringUtils.isEmpty(orginFileName) == false) {

						// * 删除该用户原来的该类型的资质都，保证每类资质只保留一份
						// * 当存在待审核的渠道状态，以及审核通过的渠道状态时，不允许修改或删除资质
						deleteQualiDoc(partnerId, qualiDocTypeId);

						// 生成新文件名
						//String extend = FileUtil.getExtendName(orginFileName);
						String fileNewName = System.currentTimeMillis() + "."
								+ extend;
						
						// 将上传文件保存到资质上传目录
						File localFile = new File(uploadDir + File.separator + fileNewName);
						file.transferTo(localFile);

						// 记入quali_doc表
						QualiDoc doc = new QualiDoc();
						doc.setPartnerId(partnerId);
						doc.setUploadUserId(uploadUserId);
						doc.setDocTypeId(qualiDocTypeId);
						doc.setDocName(fileNewName);
						doc.setDocOldName(orginFileName);
						doc.setDocNumber(docNumber);
						doc.setDocValidDate(docValidDate);
						doc.setDocPath(uploadDir + "/");
						doc.setStatus(AuditStatus.STATUS_WAIT.getCode());
						qualiDocDao.insert(doc);
						
						// 根据产品设计的统一设置内部审核状态的业务逻辑，应将客户所有的资质的内部审核状态重置为未审核
						QualiDoc doc2 = new QualiDoc();
						doc2.setPartnerId(partnerId);
						doc2.setStatus(AuditStatus.STATUS_WAIT.getCode());
						qualiDocDao.update(doc2);
						str = "文件保存成功";

					} else {

						// 不做保存文件操作，只修改资质文件的证件号和证件有效期
						QualiDoc doc = new QualiDoc();
						doc.setPartnerId(partnerId);
						doc.setDocTypeId(qualiDocTypeId);
						doc.setDocNumber(docNumber);
						doc.setDocValidDate(docValidDate);
						qualiDocDao.update(doc);
						str = "文件保存成功";
					}
				}
			}
		}
		return str;
	}

	@Override
	public void deleteQualiDoc(long partnerId, long qualiDocTypeId) {

		CustomerContextHolder
				.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

		Map<String, Object> docMap = qualiDocDao.findByPartnerIdAndDocType(
				partnerId, qualiDocTypeId);
		if (docMap != null) {

			long id = (long) docMap.get("id");

			String fileName = (String) docMap.get("doc_name");
			String fileDir = (String) docMap.get("doc_path");

			deleteQualiDocFile(fileDir, fileName, partnerId);

			// 从quali_doc表中删除
			qualiDocDao.deleteById(id);

			// 从partner_status_quali_doc表中将该类资质送审的记录删掉，因为向渠道提交资质时，只会提交没有送审记录的资质类别
			// partnerStatusQualiDocDao.deleteBy(partnerId, docTypeId);
		}

	}

	private void deleteQualiDocFile(String fileDir, String fileName,
			long partnerId) {

		// 删除该文件
		String filePath = fileDir + fileName;
		File file = new File(filePath);
		// 如果文件按照数据库中保存的路径找不到，则按照配置文件指定的路径再找一次
		if (file.exists() == false) {
			file = new File(Constants.QUALIDOC_DIR_PATH + "/" + partnerId + "/"
					+ fileName);
		}
		file.delete();

	}

	@Override
	public void toFinance(long partnerId, Model model,
			Map<String, Object> paramMap, Integer pageNo, Integer pageSize) {

		CustomerContextHolder
				.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

		pageNo = null == pageNo ? 1 : pageNo;
		pageSize = null == pageSize ? 10 : pageSize;

		Pagination<PartnerAccFlow> page = new Pagination<PartnerAccFlow>();
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);

		page.setCondition(paramMap);
		// 查询账户流水
		List<PartnerAccFlow> list = partnerAccFlowDao.getPageBy(page);

		page.setList(list);

		model.addAttribute("page", page);

		Partner pr = partnerDao.getById(partnerId);

		model.addAttribute("amount", pr.getAccBalanceYuan());

		model.addAttribute("params", paramMap);
	}

	@Override
	public void getFinanceExcel(HashMap<String, Object> params,
			HttpServletResponse response) {

		// 切换业务库数据源
		CustomerContextHolder
				.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

		String[] firstRow = { "日期", "分类", "金额", "账户余额" };

		List<PartnerAccFlow> list = partnerAccFlowDao.listByMap(params);

		List<String[]> rowList = new ArrayList<String[]>();
		// 添加第一行
		rowList.add(firstRow);

		if (list != null) {
			for (PartnerAccFlow partnerAccFlow : list) {

				String[] row = new String[firstRow.length];
				int i = 0;
				row[i++] = DateUtil.getDateStr(partnerAccFlow.getCreateTime(),
						"yyyy-MM-dd HH:mm:ss");

				TradeType tt = TradeType
						.fromCode(partnerAccFlow.getTradeType());
				row[i++] = tt != null ? tt.getDesc() : "未知";
				row[i++] = partnerAccFlow.getAmountYuan();
				row[i++] = partnerAccFlow.getAccBalanceResultYuan();

				rowList.add(row);
			}
		}

		Map<String, List<String[]>> sheets = new HashMap<String, List<String[]>>();
		sheets.put("财务明细", rowList);

		File excelFile = null;
		try {
			String baseDir = System.getProperty("java.io.tmpdir");
			excelFile = new File(baseDir + "/" + System.currentTimeMillis()
					+ ".xlsx");
			ExcelUtil.create07ExcelFile(excelFile, sheets);
			FileUtil.downloadFile(response, "财务明细.xlsx", excelFile);
		} catch (IOException e) {
			// log.error(e.getMessage(), e);
		} finally {
			if (excelFile != null && excelFile.exists()) {
				try {
					excelFile.delete();
				} catch (Exception e) {

				}
			}
		}
	}

	@Override
	public void getRetargetPacketMap(Model model, long partnerId,
			String partnerName) {

		// 使用业务库
		CustomerContextHolder
				.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

		RetargetPacket rpGlobal = retargetPacketDao.findByPartnerIdAndRemark(
				partnerId, "global");
		if (rpGlobal == null) {
			rpGlobal = new RetargetPacket();
			rpGlobal.setOwnerPartnerId(partnerId);
			rpGlobal.setPacketName(partnerName + "-全站访客找回");
			rpGlobal.setRemark("global");
			rpGlobal.setDeleteStatus(0);

			insertRetargetPacketMap(rpGlobal, CookieType.RETARGET);
		}
		// 全站访客找回gid
		model.addAttribute("globalGid", rpGlobal.getCookieGid());

		RetargetPacket rpRegisterI = retargetPacketDao
				.findByPartnerIdAndRemark(partnerId, "register_intention");
		if (rpRegisterI == null) {
			rpRegisterI = new RetargetPacket();
			rpRegisterI.setOwnerPartnerId(partnerId);
			rpRegisterI.setPacketName(partnerName + "-注册意向访客找回");
			rpRegisterI.setRemark("register_intention");
			rpRegisterI.setDeleteStatus(0);

			insertRetargetPacketMap(rpRegisterI, CookieType.ADVANCERETARGET);
		}
		// 注册意向访客找回gid
		model.addAttribute("registerIntentionGid", rpRegisterI.getCookieGid());

		RetargetPacket rpRegisterS = retargetPacketDao
				.findByPartnerIdAndRemark(partnerId, "register_success");
		if (rpRegisterS == null) {
			rpRegisterS = new RetargetPacket();
			rpRegisterS.setOwnerPartnerId(partnerId);
			rpRegisterS.setPacketName(partnerName + "-注册成功访客找回");
			rpRegisterS.setRemark("register_success");
			rpRegisterS.setDeleteStatus(0);

			insertRetargetPacketMap(rpRegisterS, CookieType.ADVANCERETARGET);
		}
		// 注册成功访客找回gid
		model.addAttribute("registerSuccessGid", rpRegisterS.getCookieGid());

		RetargetPacket rpAsk = retargetPacketDao.findByPartnerIdAndRemark(
				partnerId, "ask");
		if (rpAsk == null) {
			rpAsk = new RetargetPacket();
			rpAsk.setOwnerPartnerId(partnerId);
			rpAsk.setPacketName(partnerName + "-咨询访客找回");
			rpAsk.setRemark("ask");
			rpAsk.setDeleteStatus(0);

			insertRetargetPacketMap(rpAsk, CookieType.ADVANCERETARGET);
		}
		// 咨询访客找回gid
		model.addAttribute("askGid", rpAsk.getCookieGid());

		// tracker获取地址
		model.addAttribute("trackerUrl", Constants.TRACKER_URL);
	}

	@Override
	public void insertRetargetPacketMap(RetargetPacket rp, CookieType cookieType) {

		CookieGid cg = new CookieGid();
		cg.setCkGroupName(rp.getPacketName());
		cg.setCkProperty("全站访客找回");
		cg.setCkType(cookieType.getCode());
		cg.setDimName("r");
		cg.setDeleteStatus(0);
		cg.setCreateTime(new Date());

		long gid = cookieGidDDao.insert(cg);

		rp.setCookieGid(gid);

		retargetPacketDao.insert(rp);

	}

}
