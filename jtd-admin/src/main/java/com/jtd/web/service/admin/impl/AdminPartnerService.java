package com.jtd.web.service.admin.impl;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import com.jtd.web.service.adx.bes.BaiduAPIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.alibaba.fastjson.JSONObject;
import com.jtd.commons.dao.BaseDao;
import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.commons.page.Pagination;
import com.jtd.utils.AccountAmountUtil;
import com.jtd.utils.DateUtil;
import com.jtd.utils.FileUtil;
import com.jtd.utils.HTTPUtil;
import com.jtd.utils.ReportUtil;
import com.jtd.web.constants.AuditStatus;
import com.jtd.web.constants.BossApiType;
import com.jtd.web.constants.CatgSerial;
import com.jtd.web.constants.Constants;
import com.jtd.web.constants.PartnerStatus;
import com.jtd.web.constants.PartnerType;
import com.jtd.web.constants.RoleType;
import com.jtd.web.constants.TradeType;
import com.jtd.web.controller.DicController;
import com.jtd.web.dao.IBossRequestLogDao;
import com.jtd.web.dao.IDspSalesProductFlowDao;
import com.jtd.web.dao.IPardCountDao;
import com.jtd.web.dao.IPartnerAccFlowDao;
import com.jtd.web.dao.IPartnerDao;
import com.jtd.web.dao.IPartnerExpendDailySnapshotDao;
import com.jtd.web.dao.IPartnerStatusDao;
import com.jtd.web.dao.IQualiDocDao;
import com.jtd.web.dao.IUserPartnerDao;
import com.jtd.web.dao.impl.PartnerCategoryDao;
import com.jtd.web.dao.impl.PartnerDimDao;
import com.jtd.web.exception.PlatformException;
import com.jtd.web.po.ActiveUser;
import com.jtd.web.po.BossRequestLog;
import com.jtd.web.po.DspSalesProductFlow;
import com.jtd.web.po.Partner;
import com.jtd.web.po.PartnerAccFlow;
import com.jtd.web.po.PartnerCategory;
import com.jtd.web.po.PartnerDim;
import com.jtd.web.po.PartnerPreFlow;
import com.jtd.web.po.QualiDoc;
import com.jtd.web.po.UserPartner;
import com.jtd.web.po.count.PartnerExpendDailySnapshot;
import com.jtd.web.service.IMQConnectorService;
import com.jtd.web.service.admin.IAdminPartnerPreFlowService;
import com.jtd.web.service.admin.IAdminPartnerService;
import com.jtd.web.service.impl.BaseService;
import com.jtd.web.vo.ChannelCategory;

@Service
public class AdminPartnerService extends BaseService<Partner> implements
        IAdminPartnerService {

    private Logger logger = LoggerFactory.getLogger(AdminPartnerService.class);
    private Logger balanceLogger = LoggerFactory.getLogger("balance");

    @Autowired
    private IPartnerDao partnerDao;

    @Autowired
    private PartnerCategoryDao partnerCategoryDao;

    @Autowired
    private IPartnerAccFlowDao partnerAccFlowDao;

    @Autowired
    private PartnerDimDao partnerDimDao;

    @Autowired
    private IPartnerStatusDao partnerStatusDao;

    @Autowired
    private IPardCountDao pardCountDao;

    @Autowired
    private IMQConnectorService mqConnectorService;

    @Autowired
    private IUserPartnerDao userPartnerDao;

    @Autowired
    private IPartnerExpendDailySnapshotDao expendDailysnapshotDao;

    @Autowired
    private IDspSalesProductFlowDao dspSalesProductFlowDao;

    @Autowired
    private IBossRequestLogDao bossRequestLogDao;

    @Autowired
    private IAdminPartnerPreFlowService adminPartnerPreFlowService;
    
    @Autowired
    private IQualiDocDao qualiDocDao ;

    @Autowired
    private BaiduAPIService baiduAPIService;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected BaseDao<Partner> getDao() {
        return partnerDao;
    }

    @Override
    public ChannelCategory getPartnerCategory(Long partnerId) {

        CustomerContextHolder
                .setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

        // 查询广告主在我dsp的行业类型：先获取行业映射，从中查找类型是dsp的，即为此广告主在我dsp的行业类型
        List<PartnerCategory> pcList = null;
        try {
            pcList = partnerCategoryDao.selectByPartnerId(partnerId);
        } catch (Exception e) {
            logger.error("getPartnerCategory出现错误", e);
        }

        if (pcList != null) {
            for (Iterator<PartnerCategory> iterator = pcList.iterator(); iterator
                    .hasNext(); ) {
                PartnerCategory pc = (PartnerCategory) iterator.next();
                if (CatgSerial.DSP.getCode().equals(pc.getCatgserial())) {
                    // 找到类型是dsp的，立即返回
                    return DicController.getDSPChannelCategory(Long.valueOf(pc
                            .getCatgid()));
                }
                break;
            }

            logger.error("getPartnerCategory dsp行业信息为空");

        } else {
            logger.error("getPartnerCategory 行业信息为空");
        }

        return null;
    }

    /**
     * 装载/处理Partner其他信息
     *
     * @param partner
     * @throws PlatformException
     */
    private void loadPartnerOtherInfo(Partner partner) {

        CustomerContextHolder
                .setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

        if (partner == null) {
            return;
        }

        if (partner.getGrossProfit() != null) {

            int gross = (int) (partner.getGrossProfit() * 100);

            partner.setGrossProfitString(gross + "");
        }

        // 查找行业信息
        ChannelCategory cc = getPartnerCategory(partner.getId());

        if (cc != null) {
            // 行业id
            partner.setCategoryId(cc.getId());
            // 行业名称
            partner.setCategoryName(cc.getName());
        }
    }

    @Override
    public Partner getById(long partnerId) {

        CustomerContextHolder
                .setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

        Partner partner = partnerDao.getById(partnerId);

        loadPartnerOtherInfo(partner);

        return partner;
    }

    @Override
    public List<Map<String, Object>> listPartnerHierarchy(Long partnerId, boolean needSelf)
            throws PlatformException {

        CustomerContextHolder
                .setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        
        if(needSelf){
        	Map<String, Object> self = partnerDao.getMapById(partnerId);
        	resultList.add(self);
        }

        listPartnerHierarchy_LoadFunc(partnerId, resultList, 1);

        return resultList;
    }

    /**
     * 递归加载partner的后代
     *
     * @param partnerId
     * @param resultList
     * @param level
     */
    private void listPartnerHierarchy_LoadFunc(long partnerId, List<Map<String, Object>> resultList, int level) {

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("pid", partnerId);

        List<Map<String, Object>> descendantPartnerList = partnerDao.listMapByMap(param);

        if (descendantPartnerList != null && descendantPartnerList.size() > 0) {

            for (Map<String, Object> partner : descendantPartnerList) {

                // logger.info("读取到" + level + "级partner[" + partner.getId() +
                // "]，属于[" + partnerId + "]");

                resultList.add(partner);

                long subPartnerId = (long)partner.get("id");
                
                listPartnerHierarchy_LoadFunc(subPartnerId, resultList,
                        level + 1);

            }

        }
    }

    @Override
    public Partner getAncestorPartner(long partnerId) {

        CustomerContextHolder
                .setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

        Partner p = partnerDao.getById(partnerId);
        if (p != null) {
            long pid = p.getPid();
            if (pid == 0) {
                return p;
            } else {
                return getAncestorPartner(pid);
            }
        }
        return null;
    }

    @Override
    public void saveOrUpdate(Partner partner, Long ceOperatorUserId,
                             ActiveUser user, HttpServletRequest request, boolean isInsert) {
        if (partner != null) {
            try {

                boolean hasUploadedLogo = false;

                File logoUploadDir = new File(Constants.LOGO_IMG_PATH);
                if (!logoUploadDir.exists()) {
                    logoUploadDir.mkdirs();
                }

                // 1.首先要分别针对普通类型的partner和OEM类型的partner的个性化设置部分做一下处理

                // 如果是OEM则需要检查上传的logo文件
                if (partner.getPartnerType() == PartnerType.OEM.getCode()) {

                    CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                            request.getSession().getServletContext());

                    if (multipartResolver.isMultipart(request)) {
                        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
                        // 遍历所有文件名
                        Iterator<String> iter = multiRequest.getFileNames();
                        for (Iterator<String> iterator = iter; iterator
                                .hasNext(); ) {
                            String fileName = (String) iterator.next();
                            // 得到上传的文件
                            MultipartFile file = multiRequest.getFile(fileName);
                            if (file != null) {
                                String orginFileName = file
                                        .getOriginalFilename();

                                // 如果没有上传文件则orginFileName会为空，则不做保存文件相关操作
                                if (StringUtils.isEmpty(orginFileName) == false) {

                                    // 生成新文件名
                                    String extend = FileUtil
                                            .getExtendName(orginFileName);

                                    if (extend.toLowerCase().equals("jpg")
                                            || extend.toLowerCase().equals(
                                            "jpeg")
                                            || extend.toLowerCase().equals(
                                            "png")) {

                                        hasUploadedLogo = true;

                                        String fileNewName = System
                                                .currentTimeMillis()
                                                + "."
                                                + extend;

                                        // 将文件名设置到partner中
                                        partner.setLogoImg(fileNewName);

                                        // 将上传文件保存到资质上传目录
                                        File localFile = new File(
                                                Constants.LOGO_IMG_PATH
                                                        + File.separator
                                                        + fileNewName);
                                        file.transferTo(localFile);
                                    }
                                }
                            }
                        }
                    }

                }
                // 如果是非OEM类型，需要取userBelongingPartner的logoImg
                else {
                    partner.setLogoImg(user.getPartner().getLogoImg());
                }

                // 2.开始进行新增或修改的操作

                CustomerContextHolder
                        .setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);


                //广告主层级，无上级为1，1级下属广告主即为2，以此类推 
                //没有【jtd公司】
                if (partner.getPid() == 0 ) {
                	Partner pidPartner = new Partner();
                	pidPartner.setId(0l);
                	pidPartner.setPartnerName("无");
                	pidPartner.setPartnerLevel(1);
				}else {
					 Partner pidPartner = partnerDao.getById(partner.getPid());
					partner.setPartnerLevel(pidPartner.getPartnerLevel() + 1);
				}
                
                // 新增
                if (isInsert) {

                    partner.setStatus(PartnerStatus.STOP.getCode()); // 新建时为暂停，等到审核通过了之后才改为开启
                    
                	//毛利设置默认30%
            		partner.setGrossProfit(0.3);
            		
                    //获取当前登录人id
            		if(user.isAdminOrManager() || user.isOperateDirectorOrManager()){
            			partner.setCreateUserId(null);
            		} else {
            			partner.setCreateUserId(user.getUserId());
					}
                    
                    // 新增
                    long partnerId = partnerDao.insert(partner);
                    
                    
                    // 获得新id
                    partner.setId(Long.valueOf(partnerId));

                    // 保存行业映射
                    savePartnerCategory(partnerId, partner.getCategoryId());

                    /**
                     * 如果页面选择了运营人员，则为运营添加访问partner权限
                     *
                     * 运营人员参数为空的两种情况：
                     *
                     * 1.当前新增广告主的操作用户不是【jtd公司】的员工，没有运营人员选项
                     *
                     * 2.正在编辑的partner是【jtd公司】，不用选择运营人员
                     *
                     */
                    if (ceOperatorUserId != null) {
                        UserPartner ceOperatorPartnerRelation = new UserPartner();
                        ceOperatorPartnerRelation.setPartnerId(partner.getId()
                                + "");
                        ceOperatorPartnerRelation.setUserId(ceOperatorUserId
                                + "");
                        ceOperatorPartnerRelation.setCreateTime(new Date());
                        userPartnerDao.insert(ceOperatorPartnerRelation);
                    }

                    /*
                     * 如果是admin，管理员，运营主管经理都不受访问权限控制，不用添加访问此广告主的权限，如果是运营，在上面也已经加权限了
                     * 
                     * 所以可以判断，只要是属于【jtd公司】的用户，都不用再添加访问权限，相反如果创建操作者是非【jtd公司】的用户，就要添加访问权限，否则他创建完这个广告主之后，无法从广告主列表中看到
                     */
                    if (user.getPartner().getId() != Constants.CE_PARTNER_ID) {
                        UserPartner currentUserPartnerRelation = new UserPartner();
                        currentUserPartnerRelation.setPartnerId(partner.getId() + "");
                        currentUserPartnerRelation.setUserId(user.getUserId() + "");
                        currentUserPartnerRelation.setCreateTime(new Date());
                        userPartnerDao.insert(currentUserPartnerRelation);
                    }
                    
                    // 发送广告主毛利率变更消息
                    try {
                        mqConnectorService.sendSetPartnerGrossProfitMessage(
                        		partnerId,
                                (int) Math.round(partner.getGrossProfit() * 100));
                    } catch (Exception e) {
                        logger.error("saveOrUpdate发送毛利设置JMS发生错误", e);
                    }

                }
                // 修改
                else {

                    Partner oldData = this.getById(partner.getId());
                    
                    // 保证中启的pid永远为-1，不会被修改成其他的值
                    if (oldData.getPid() == Constants.CE_PID) {
                        partner.setPid(null);
                    }
                    
                    // 修改
                    partnerDao.update(partner);

                    // 如果行业发生变化，才重新设置行业映射
                    if (oldData.getCategoryId() != null
                            && oldData.getCategoryId().equals(
                            partner.getCategoryId()) == false) {
                        // 保存行业映射
                        savePartnerCategory(partner.getId(), partner.getCategoryId());
                    }

                    /**
                     * 如果页面选择了运营人员，则为运营添加访问权限
                     *
                     * 运营人员参数为空的两种情况：
                     *
                     * 1.当前修改广告主的操作用户不是【jtd公司】的员工，没有选择运营人员的权限
                     *
                     * 2.正在编辑的广告主是【jtd公司】，不用选择运营人员
                     *
                     */
                    if (ceOperatorUserId != null) {

                        // 查询该partner是否之前存在运营人员
                        Map<String, Object> partnerOperator = userPartnerDao
                                .findByPartnerAndRole(partner.getId(),
                                        RoleType.OPERATE);
                        if (partnerOperator != null) {
                            // 如果存在则修改运营人员人选
                            userPartnerDao.updatePartnerUserByRole(
                                    partner.getId(), ceOperatorUserId,
                                    RoleType.OPERATE);
                        } else {
                            // 添加运营人员关联当前partner的记录，使该运营人员有权限看到这个partner
                            UserPartner ceOperatorPartnerRelation = new UserPartner();
                            ceOperatorPartnerRelation.setPartnerId(partner
                                    .getId() + "");
                            ceOperatorPartnerRelation
                                    .setUserId(ceOperatorUserId + "");
                            ceOperatorPartnerRelation.setCreateTime(new Date());
                            userPartnerDao.insert(ceOperatorPartnerRelation);
                        }

                    }

                    // 如果是OEM类型的广告主，需要同步修改下级广告主的个性化设置
                    if (partner.getPartnerType() == PartnerType.OEM.getCode()) {
                    	
                    	// 查询所有的级联下级广告主
                        List<Map<String,Object>> partnerHierarchyList = this.listPartnerHierarchy(partner.getId(), false);
                        
                        for (Map<String,Object> descendantPartner : partnerHierarchyList) {
                        	
                        	long descendantPartnerId = (long)descendantPartner.get("id");
                        	
                            // 同步oemPartner和descendantPartner的个性化设置
                            partnerDao.updateOEMSetting(descendantPartnerId , partner.getId());
                        }
                    }
                    // 如果上传了新的logo，把旧logo删掉
                    if (hasUploadedLogo) {

                        if (oldData.getLogoImg() != null) {
                            String fileName = oldData.getLogoImg();
                            File oldLogo = new File(Constants.LOGO_IMG_PATH
                                    + File.separator + fileName);
                            if (oldLogo.exists()) {
                                oldLogo.delete();
                            }
                        }

                    }                     
                }

            } catch (Exception e) {
                e.printStackTrace();
                logger.error("saveOrUpdate出现错误", e);
                throw new PlatformException(
                        PlatformException.DATABASE_ERROR_MESSAGE);
            }
        } else {
            logger.error("saveOrUpdate参数为空");
            throw new PlatformException(
                    PlatformException.PARAMETER_ERROR_MESSAGE);
        }
    }

    @Override
    public void savePartnerCategory(long partnerId, long categoryId) {

        if (DicController.getDSPChannelCategory(categoryId) != null) {

            CustomerContextHolder
                    .setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

            try {

                // 先把原有行业映射删除
                partnerCategoryDao.deleteByPartnerId(partnerId);

                // 通过行业id从全局缓存中取得对应的我dsp对各adx的行业映射对象
                ChannelCategory cc = DicController
                        .getDSPChannelCategory(categoryId);

                if (cc != null) {

                    // 先将广告主在dsp的行业代码插入partner_category表
                    PartnerCategory dsp = new PartnerCategory();
                    dsp.setPartnerId(partnerId);
                    dsp.setCatgserial(CatgSerial.DSP.getCode());
                    dsp.setCatgid(cc.getId().toString());
                    partnerCategoryDao.insert(dsp);

                    // 取得各adx的对应行业对象
                    Map<CatgSerial, Long> ccMap = cc.getChannelCategorys();
                    if (ccMap != null) {

                        // 遍历各adx的对应行业对象，并逐个插入partner_category表
                        for (Iterator<Entry<CatgSerial, Long>> iterator = ccMap
                                .entrySet().iterator(); iterator.hasNext(); ) {

                            Entry<CatgSerial, Long> entry = (Entry<CatgSerial, Long>) iterator
                                    .next();

                            PartnerCategory adx = new PartnerCategory();
                            adx.setPartnerId(partnerId);
                            adx.setCatgserial(entry.getKey().getCode());
                            adx.setCatgid(entry.getValue().toString());

                            partnerCategoryDao.insert(adx);
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                logger.error("savePartnerCategory出现错误", e);
                throw new PlatformException("保存行业信息发生错误");
            }
        } else {
            logger.error("savePartnerCategory找不到行业信息");
            throw new PlatformException("找不到行业信息");
        }

    }

    @Override
    public long updateStatus(Long partnerId, PartnerStatus status){

        CustomerContextHolder
                .setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

        
        long result = 0;

        if (status != null) {
        	
        	//如果是开启动作，则要检查广告主的资质审核状态
            if (status == PartnerStatus.START) {

                //检查内部资质内部审核通过
                QualiDoc params = new QualiDoc();
                params.setPartnerId(partnerId);
                params.setStatus(AuditStatus.STATUS_SUCCESS.getCode());
                List<QualiDoc> list = qualiDocDao.listBy(params);
                if(list != null && list.size() > 0){
                	
                	// 如果没有配置忽略ADX，则检查是否提交过adx审核，或审核存在拒绝状态
                	if (Constants.IS_IGNORE_ADX == false) {
                        if (checkChannelAuditStatus(partnerId) == false) {
                        	//返回-2表示ADX审核方面检查不通过
                            return -2;
                        }
                    }
                }
                else{
                	//返回-1表示内部审核方面检查不通过
                	return -1 ;
                }

            }

            result = partnerDao.updateStatus(partnerId, status);

            // 向MQ发送广告主修改状态消息
            try {
				mqConnectorService.sendPartnerStatusChangeMessage(partnerId, status);
				
			} catch (Exception e) {
				logger.error("AdminPartnerService ----updateStatus---修改广告主[id=" + partnerId + "]状态为" + status.getCode() + "，发送MQ出现错误", e);
			}
        }
        else{
        	result = -3 ;
        }

        return result;
    }
    
    
    
    /**
     * 检查广告主在各adx的审核情况，如果从来未提交过adx，返回false，如果存在拒绝的情况，返回false
     *
     * @param partnerId
     * @return
     */
    private boolean checkChannelAuditStatus(long partnerId) {

        boolean pass = true;

        CustomerContextHolder
                .setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

        List<Map<String, Object>> auditStatusList = partnerStatusDao
                .listAllChannelAuditStatusByPartnerId(partnerId);
        if (auditStatusList != null && auditStatusList.size() > 0 ) {

            for (Map<String, Object> map : auditStatusList) {

                if (map.get("status") != null) {

                	int status = (int) map.get("status");
                	//如果存在通过的记录，就可以直接通过审核状态验证
                    if (status == AuditStatus.STATUS_SUCCESS.getCode()) {
                        break;
                    }
                }
            }

        } else {
            pass = false;
        }
        return pass;
    }

    @Override
    public boolean checkPartnerAccBalance(Partner partner) {

        if (partner == null) {
            throw new NullPointerException(
                    "执行定时任务 checkPartnerAccBalance ，partner参数为空");
        }

        long partnerId = partner.getId();
        long accBalance = partner.getAccBalance();
        
        //查看是否要为广告主生成昨日扣费流水，并得到广告主最新的余额（在生成流水前要最后比对一次消费和消费快照，广告主余额也许会发生改变）
        accBalance = ifGenerateYesterdayExpendFlow(partnerId , accBalance);
        
        //每十分钟扣除广告余额，并在扣余额之前生成一个当前消费与余额快照，并返回广告主最新的余额
        accBalance = checkExpendAndSnapshot(partnerId, accBalance, null);
        
        //如果真实余额小于等于0，通知引擎暂停该用户投放
        if (accBalance <= 0L) {
            // 返回检查结果
            return false;
        }
        
        return true;
    }
    
    /**
     * 生成昨日扣费流水（每天只生成一次），并且返回广告主最新的余额值
     * @param partnerId
     */
    private long ifGenerateYesterdayExpendFlow(long partnerId,long accBalance){
    	
    	if(partnerId == 228){
        	System.out.println("");
        }
    	
    	//切换到业务库准备查询 PartnerAccFlow
    	CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
    	
    	//查询今日生成的"系统检查昨日消费"
    	PartnerAccFlow yesterdayAdConsume = partnerAccFlowDao
                .getLastRecord(partnerId, null,TradeType.AD_CONSUME, new Date());

        // 如果当前partner还没有生成昨天的广告扣费流水，则开始生成，每天只生成一次
        if (yesterdayAdConsume == null) {
        	
            // 查询距昨天最近一次广告消费流水
            PartnerAccFlow lastAccFlow = partnerAccFlowDao
                    .getLastRecord(partnerId, null,
                            TradeType.AD_CONSUME, DateUtil.getYestoday());

            Date tradeTime = null ;
            
            //如果从未有过广告消费流水，则要从pard表统计广告主从起始到昨天的消费
            if (lastAccFlow != null) { 

            	//如果以前曾经有过广告消费流水，开始从pard统计广告主从tradeTime到昨天的消费
            	tradeTime = lastAccFlow.getTradeTime();
            }
            
            CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_COUNT);
            
            //从统计库的pard表统计：当前广告主的自从“tradeTime”到昨天的广告消费
            Map<String, Object> yesterdaySumMap = pardCountDao.getSum(partnerId, tradeTime,
                    DateUtil.getYestoday());
            //取出消费数额
            Long yesterdayExpend = null ;
            if(yesterdaySumMap != null && yesterdaySumMap.get("expend_sum") != null){
            	yesterdayExpend = ((BigDecimal) yesterdaySumMap
                        .get("expend_sum")).longValue();
            }
            
            if (yesterdayExpend > 0) {
            	
            	if(tradeTime != null){
            		balanceLogger.info("partner[id=" + partnerId + "]从上一次" + DateUtil.getDateStr(tradeTime, "yyyy-MM-dd") + "消费到昨天的消费是："
                            + yesterdayExpend);
            	}
            	else{
            		balanceLogger.info("partner[id=" + partnerId + "]从最初到昨天的消费是："
                            + yesterdayExpend);
            	}

                try {
					//最后在检查一次昨天的消费和快照（因为昨天最后一次任务到今天第一次任务之间的十分钟，可能还会产生广告消费）
					accBalance = checkExpendAndSnapshot(partnerId, accBalance , yesterdayExpend);
				} catch (Exception e) {
					throw e ;
				}
                
                // 创建一条昨日的扣费流水
                PartnerAccFlow yesterdayAccFlow = new PartnerAccFlow();
                yesterdayAccFlow.setPartnerId(partnerId);
                yesterdayAccFlow.setTradeId(PartnerAccFlow
                        .generateNewTradeId(Constants.ADMIN_USER_ID,
                                partnerId));
                yesterdayAccFlow.setAmount(yesterdayExpend);
                yesterdayAccFlow.setAccBalanceResult(accBalance);
                yesterdayAccFlow.setOperatorName("系统检查昨日消费");
                yesterdayAccFlow.setOperatorId(Constants.ADMIN_USER_ID);
                yesterdayAccFlow.setTradeTime(new Date());
                yesterdayAccFlow.setCreateTime(new Date());
                yesterdayAccFlow.setTradeType(TradeType.AD_CONSUME
                        .getCode());

                partnerAccFlowDao.insert(yesterdayAccFlow);

            }

        }
    	
        return accBalance ;
    }
    
    /**
     * 每十分钟扣除广告余额，并在扣余额之前生成一个当天（相对于date参数）当前的消费与余额快照，并返回广告主目前最新的余额
     * 
     * @param partnerId
     */
    private long checkExpendAndSnapshot(Long partnerId , long accBalance , Long yesterdayExpend){
    	
        // 开始用今日消费实时扣除partner的账户余额，如果账户余额小于0，则返回错误状态
        long dateCurrentExpend = 0;
        Date date = null ;
        
        if(partnerId == 228){
        	System.out.println("");
        }
        
        //如果yesterdayExpend不为空，则表示是ifGenerateYesterdayParnterAccFlow方法调用，该方法已经查询了昨天最后的消费结果，在这里只需要直接使用即可
        if(yesterdayExpend != null){
        	dateCurrentExpend = yesterdayExpend;
        	Calendar c = Calendar.getInstance();
        	c.set(Calendar.DATE, c.get(Calendar.DATE) - 1);
        	c.set(Calendar.HOUR_OF_DAY, 23);
        	c.set(Calendar.MINUTE, 59);
        	c.set(Calendar.SECOND, 00);
        	
        	date = c.getTime();
        }
        else{
        	
        	date = new Date();
        	
        	//切换到统计库准备查询pard
            CustomerContextHolder
                    .setContextType(CustomerContextHolder.SESSION_FACTORY_COUNT);
        	
        	 // 从投放统计库检查当天（相对于date参数）当前的消费总和
            Map<String, Object> dateCurrentExpendSumMap = pardCountDao.getSum(partnerId, date, date);
            // 取出消费数额
            if (dateCurrentExpendSumMap != null
                    && dateCurrentExpendSumMap.get("expend_sum") != null) {
                dateCurrentExpend = ((BigDecimal) dateCurrentExpendSumMap.get("expend_sum"))
                        .longValue();
            }
        }
        
        // 如果存在消费
        if (dateCurrentExpend > 0) {
        	
        	//切换到业务库准备操作 PartnerExpendDailySnapshot 和 Partner
            CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

        	// 马上生成一个消费总和快照，准备插入或覆盖之前的快照
            PartnerExpendDailySnapshot snapshot = new PartnerExpendDailySnapshot();
            snapshot.setPartnerId(partnerId);
            snapshot.setExpendSnapshot(dateCurrentExpend);
            snapshot.setAccountBalance(accBalance);
            snapshot.setDateTime(date);

            // 查询今天最新的消费总和的快照，以计算本次需要扣掉的余额
            Map<String, Object> expendSnapshotMap = expendDailysnapshotDao.findByPartnerAndDate(partnerId, date);
            if (expendSnapshotMap != null
                    && expendSnapshotMap.get("expend_snapshot") != null) {

                // 如果之前快照不为空，则取出快照金额
            	long expendSumSnapshot = (long) expendSnapshotMap
                        .get("expend_snapshot");
                
                // 当前余额 - (今天截止目前消费 - 之前快照金额) = 新余额
                accBalance = accBalance - (dateCurrentExpend - expendSumSnapshot);
                snapshot.setAccountBalanceResult(accBalance);

                expendDailysnapshotDao.update(snapshot);
                
            } else {
            	
            	// 当前余额 - 今天截止目前消费 = 新余额
                accBalance = accBalance - dateCurrentExpend ;
                snapshot.setAccountBalanceResult(accBalance);
            	
                expendDailysnapshotDao.insert(snapshot);
            }
            
            balanceLogger.info("生成快照：" + JSONObject.toJSONString(snapshot));
            
            // 更新partner表的用户余额
            partnerDao.updateAccountBalance(partnerId, accBalance);

        }
    	
        return accBalance ;
    }

    @Override
    public long updateAccountBalance(Partner partner, long amountFen, ActiveUser activeUser, int is_finance_recharge, int fromTradeType,int toTradeType, PartnerPreFlow partnerPreFlow) throws PlatformException {

        CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

        try {

            if (partner == null) {
                throw new NullPointerException("updateAccountBalance，partner查询为空");
            }
            long partnerId = partner.getId();
            long accBalance = partner.getAccBalance();
            if (toTradeType == TradeType.PROXY_RECHARGE.getCode() || toTradeType == TradeType.RECHARGE.getCode()) {
                accBalance = amountFen + accBalance;
            } else {
                accBalance = accBalance - amountFen;
            }

            // 追加acc_balance字段的值
            partnerDao.updateAccountBalance(partnerId, accBalance);

            // 向partner_acc_flow表新增一条流水
            PartnerAccFlow paf = new PartnerAccFlow();
            paf.setFromPartnerId(activeUser.getPartner().getId()); //当前广告主
            paf.setPartnerId(partnerId); //被操作的广告主
            paf.setTradeId(PartnerAccFlow.generateNewTradeId(activeUser.getUserId(), partnerId));
            paf.setTradeType(toTradeType);
            if (toTradeType == TradeType.PROXY_RECHARGE.getCode() || toTradeType == TradeType.RECHARGE.getCode()) {
                paf.setAmount(amountFen);
            }else {
                paf.setAmount(-amountFen);
            }
            paf.setAccBalanceResult(accBalance);
            paf.setTradeTime(new Date());
            paf.setOperatorId(activeUser.getUserId());
            paf.setOperatorName(activeUser.getUserName());

            long flow_id = partnerAccFlowDao.insert(paf);

            //如果是财务充值或退款,则更新预充值流水信息
            if (is_finance_recharge == 1 && partnerPreFlow != null) {
                partnerPreFlow.setOperatorId(activeUser.getUserId());
                partnerPreFlow.setOperatorName(activeUser.getUserName());
                partnerPreFlow.setAccFlowId(flow_id);
                partnerPreFlow.setStatus(1);
                partnerPreFlow.setModifyTime(new Date());
                if(partnerPreFlow.getType() == 1){ //退款金额为负
                    partnerPreFlow.setAmount(-partnerPreFlow.getAmount());
                }
                adminPartnerPreFlowService.update(partnerPreFlow);
            }else {

                Partner currentPartner = activeUser.getPartner();
                long amount = currentPartner.getAccBalance();
                if (toTradeType == TradeType.PROXY_RECHARGE.getCode() || toTradeType == TradeType.RECHARGE.getCode()) {
                    amount = amount - amountFen;
                } else {
                    amount = amount + amountFen;
                }
                currentPartner.setAccBalance(amount); // 更新自己帐户的余额
                //当前登录用户所属广告主
                partnerDao.update(currentPartner);
                activeUser.setPartner(currentPartner); //更新缓存

                //广告主转帐流水
                paf = new PartnerAccFlow();

                // 如果是充值(财务充值或者代理充值),则记录财务或者代理所在广告主,否则记录被操作的广告主
                if (toTradeType == TradeType.PROXY_RECHARGE.getCode() || toTradeType == TradeType.RECHARGE.getCode()) {
                    paf.setFromPartnerId(currentPartner.getId());
                    paf.setPartnerId(currentPartner.getId());
                    paf.setAmount(-amountFen);
                } else {
                    paf.setFromPartnerId(currentPartner.getId());
                    paf.setPartnerId(currentPartner.getId());
                    paf.setAmount(amountFen);
                }

                paf.setTradeId(PartnerAccFlow.generateNewTradeId(activeUser.getUserId(), partner.getId()));
                //代理
                paf.setTradeType(fromTradeType);
                paf.setAccBalanceResult(amount);
                paf.setTradeTime(new Date());
                paf.setOperatorId(activeUser.getUserId());
                paf.setOperatorName(activeUser.getUserName());

                partnerAccFlowDao.insert(paf);
            }

            return accBalance;

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("updateAccountBalance出现错误", e);
            throw new PlatformException(PlatformException.DATABASE_ERROR_MESSAGE);
        }
    }

    @Override
    public PartnerDim loadBlacklist(long partnerId) throws PlatformException {

        CustomerContextHolder
                .setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

        try {
            return partnerDimDao.selectBlacklistByPartnerId(partnerId);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("loadBlacklist出现错误", e);
            throw new PlatformException(
                    PlatformException.DATABASE_ERROR_MESSAGE);
        }

    }

    @Override
    public void saveOrUpdateBlacklist(long partnerId, String dimValue)
            throws PlatformException {

        CustomerContextHolder
                .setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

        PartnerDim dim;
        try {
            dim = partnerDimDao.selectBlacklistByPartnerId(partnerId);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("updateBlacklist出现错误", e);
            throw new PlatformException(
                    PlatformException.DATABASE_ERROR_MESSAGE);
        }

        HashMap<String, Object> params = new HashMap<String, Object>();

        params.put("partnerId", partnerId);
        params.put("dimName", "blacklist");
        params.put("dimValue", dimValue);

        if (dim == null) {

            params.put("createTime", new Date());
            try {
                partnerDimDao.insertByMap(params);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("saveOrUpdateBlacklist添加时出现错误", e);
                throw new PlatformException(
                        PlatformException.DATABASE_ERROR_MESSAGE);
            }

        } else {

            try {
                partnerDimDao.updateByMap(params);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("saveOrUpdateBlacklist修改时出现错误", e);
                throw new PlatformException(
                        PlatformException.DATABASE_ERROR_MESSAGE);
            }

        }

    }

    @Override
    public List<Map<String, Object>> listAllChildren(Map<String, Object> params) {

        CustomerContextHolder
                .setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

        List<Map<String, Object>> listPartner = partnerDao
                .listAllChildrenByMap(params);
        return listPartner;
    }

    @Override
    public List<Partner> listAll() {
    	
    	CustomerContextHolder
		.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
    	
        return partnerDao.listAll();
    }

    @Override
    public List<Map<String, Object>> listTreeByMap(Map<String, Object> params) {

        CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

        List<Map<String, Object>> listPartner = partnerDao.listTreeByMap(params);
        // 查询广告主是否被其它运营人员关联
        if (listPartner.size() > 0) {
            if (!StringUtils.isEmpty(params.get("userId"))) {
                for (Map<String, Object> map : listPartner) {
                    String partnerId = map.get("id").toString();
                    String userId = params.get("userId").toString();
                    List<Map<String, Object>> list = userPartnerDao.queryPartnerRoles(userId, partnerId);
                    boolean flag = false;
                    if (list.size() > 0) {
                        for (Map<String, Object> resultMap : list) {
                            // 如果该广告主关联的用户有运营角色,当前用户除外,【jtd公司】广告除外
                            int sysRoleId = Integer.parseInt(resultMap.get("sys_role_id").toString());
                            if (sysRoleId == RoleType.OPERATE.getCode() && !"1".equals(partnerId)) {
                                flag = true;
                                break;
                            }
                        }
                    }
                    map.put("flag", flag);
                }
            }

            return listPartner;
        } else {
            return null;
        }

    }

    @Override
    public String findChildrenIdsById(Long id) {
        CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
        return partnerDao.getChildrenIdsById(id);
    }

    @Override
    public List<Partner> listChildPartnerByMap(Map<String, Object> partnerMap) {
        CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
        return partnerDao.listChildPartnerByMap(partnerMap);
    }

    @Override
    public List<Map<String, Object>> sortTreeTable(Partner partner, List<Map<String, Object>> partnerList,ActiveUser activeUser) {

        List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> subList = new ArrayList<Map<String, Object>>();

        for (Map<String, Object> map : partnerList) {
            if (map.get("pid").toString().equals("0")
                    || map.get("pid").toString().equals("-1")
                    || map.get("id").toString()
                    .equals(partner.getId().toString())) {
                map.put("level", 1);
                //当前登录用户所属广告是【jtd公司】且操作的广告主是【jtd公司】
                if(activeUser.getPartner().getId() == 1L && "1".equals(partner.getId().toString()) ){
                    // 1. 广告主上级代理是无
                    // 2. 勾选的广告主与遍历的广告主相同,并且【jtd公司】除外
                    if(map.get("pid").toString().equals("0") || (map.get("id").toString().equals(partner.getId().toString()) && !"1".equals(map.get("id").toString()))){
                        map.put("key_id", map.get("id").toString());
                    }
                }

                retList.add(map);
                // partnerList.remove(map);

                subList = returnChildPartnerList(map, partnerList, 1);
                if (subList.size() > 0) {
                    // partnerList.removeAll(subList);
                    retList.addAll(subList);
                }
            }
        }
        return retList;
    }

    /**
     * 返回该广告主下面的子集
     *
     * @param map
     * @param partnerList
     * @return
     */
    private List<Map<String, Object>> returnChildPartnerList(
            Map<String, Object> map, List<Map<String, Object>> partnerList,
            int level) {
    	
        List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> subList = new ArrayList<Map<String, Object>>();
        level = level + 1;

        for (Map<String, Object> partner : partnerList) {
            // 遍历广告主的上级是不是该广告主
            if (map.get("id").toString().equals(partner.get("pid").toString())) {
                partner.put("level", level);
                if(level == 2 && !map.containsKey("key_id")){
                    partner.put("key_id", partner.get("id").toString());
                }
                if(map.containsKey("key_id")){
                    partner.put("key_id", map.get("key_id").toString());
                }
                retList.add(partner);
                // 根据这个广告主查询下面的子集
                subList = returnChildPartnerList(partner, partnerList, level);
                if (subList.size() > 0) {
                    retList.addAll(subList);
                }
            }
        }
        return retList;
    }

    @Override
    public HashMap<String, Object> callbackBoss(long partnerId, String bossPartnerCode) {

        HashMap<String, Object> result = new HashMap<String, Object>();
        
        int code = 0 ;
        String msg = "" ;

        List<DspSalesProductFlow> list = null ;
        String requestJSON = null ;
		
        try {
        	 //查出biz_type
	    	 DspSalesProductFlow param = new DspSalesProductFlow();
	         param.setPartnerId(partnerId);
	         param.setType(BossApiType.ACCOUNT_CREATE.getCode());
        	
	         list = dspSalesProductFlowDao.listBy(param);
			
	        if (list != null && list.size() == 1) {

	            DspSalesProductFlow dsp = list.get(0);
	            String bizType = dsp.getBizType();

	            //组织请求数据
	            HashMap<String, Object> requestData = new HashMap<String, Object>();
	            requestData.put("biz_type", bizType);
	            requestData.put("code", 1);
	            requestJSON = JSONObject.toJSONString(requestData);

	            //调用接口
	            JSONObject responseJSON = HTTPUtil.postJSON(Constants.BOSS_PARTNER_CALLBACK_URL, requestJSON);
	            code = responseJSON.getIntValue("code");
	            msg = responseJSON.getString("message");

	        } else {
	           
	            msg = "未找到广告主的开户记录" ;
	        }
	        
		} catch (Exception e) {
			logger.error("回调boss出现内部错误", e);
			
	        msg = "回调boss出现内部错误" ;
		}
        
        //记录日志
        BossRequestLog log = new BossRequestLog();
        log.setBossPartnerCode(bossPartnerCode);
        log.setRequestType(BossApiType.CALLBACK_BOSS.getCode());
        log.setRequestData(requestJSON);
        log.setResultCode(code + "");
        log.setResultMessage(msg);
        log.setCreateTime(new Date());

        bossRequestLogDao.insert(log);
        
        result.put("code", code);
        result.put("msg", msg);
        
        return result;
    }

    @Override
    public long validAcountRechargeLimit(Long partnerId) {
    	
        if (partnerId == null) {
            throw new PlatformException("拒绝提交：广告主id为空");
        }

        CustomerContextHolder
		.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
        
        Partner params = new Partner();
        params.setId(partnerId);
        List<Partner> list = partnerDao.listBy(params);

        Partner partner = null;
        if (list != null && list.size() > 0) {
            partner = list.get(0);
            long balance = partner.getAccBalance();

            return (Constants.PARTNER_BALANCE_LIMIT - balance);

        } else {
            throw new PlatformException("拒绝提交：找不到广告主[" + partnerId + "]");
        }
    }

    @Override
    public List<Map<String, Object>> findPartnerAndChild(Map<String, Object> params) {

        CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

        List<Map<String, Object>> listPartner = partnerDao.findPartnerAndChild(params);

        return listPartner;
    }

    @Override
    public List<Partner> findPartnerPOAndChild(Map<String, Object> pMap) {
        CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
        List<Partner> list = partnerDao.findPartnerPOAndChild(pMap);
        return list;
    }

    @Override
    public Pagination<Partner> findAdminPageBy(HashMap<String, Object> paraMap, Integer pageNo, Integer pageSize, ActiveUser user) {

        return null;
    }
    
    @Override
	public void updateFirstOnlineTime(long partnerId)
			throws PlatformException {
    	
    	CustomerContextHolder
        .setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
    	
    	Partner partner = new Partner();
    	partner.setFirstOnlineTime(new Date());
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("partnerId", partnerId);
    	params.put("updateFirOnlTime",partner.getFirstOnlineTime());
    	partnerDao.updateFirstOnlineTime(params);
	}
    
	@Override
	public void updateGrossProfit(long partnerId, Partner partner) throws PlatformException {
		
		CustomerContextHolder
        .setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("partnerId", partnerId);
		params.put("grossProfit", partner.getGrossProfit());
		try {
			partnerDao.updateGrossProfit(params);
		} catch (Exception e) {
			e.printStackTrace();
            logger.error("updateGrossProfit出现错误", e);
            throw new PlatformException(
                    PlatformException.DATABASE_ERROR_MESSAGE);
		}
		

        // 发送广告主毛利率变更消息
        try {
            mqConnectorService.sendSetPartnerGrossProfitMessage(
            		partnerId,
                    (int) Math.round(partner.getGrossProfit() * 100));
        } catch (Exception e) {
            logger.error("saveOrUpdate发送毛利设置JMS发生错误", e);
        }
	}
	
	
	
	@Override
	public void partnerOperatorList(Model model, Map<String, Object> paraMap, Integer pageNo, Integer pageSize , ActiveUser user) {

		CustomerContextHolder
				.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

		pageNo = null == pageNo ? 1 : pageNo;
		pageSize = null == pageSize ? 10 : pageSize;
		
		List<Map<String, Object>> firstPartnerList = null ;
		Pagination<Map<String,Object>> page = new Pagination<Map<String,Object>>();
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		model.addAttribute("page", page);

		Map<String, Object> map = new HashMap<String, Object>();
		
		//普通运营
		if(!user.isAdminOrManager() && !user.isOperateDirectorOrManager()) { 
			// 如果不是管理员，也不是运营经理主管，就要受partner访问权限的控制
			map.put("userId", user.getUserId()); 
		}
		
		page.setCondition(map);
		//先查用户权限范围内所有顶级广告主
		firstPartnerList = partnerDao.operatorPageListMapByMap(page);
		
		if (firstPartnerList != null) {
			
			//加载顶级广告主的所有下级
			ArrayList<Map<String, Object>> allPartnerList = new ArrayList<Map<String,Object>>();
			for (Map<String, Object> partner : firstPartnerList) {
				long partnerId = (long)partner.get("id");
				List<Map<String, Object>> partnerHierachyList = listPartnerHierarchy(partnerId, false);
				allPartnerList.add(partner);
				allPartnerList.addAll(partnerHierachyList);
			}
			
			//根据查询条件筛选，决定最后要显示出来的广告主
			ArrayList<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
			for (Map<String, Object> partner : allPartnerList) {
				
				if(filter(partner, allPartnerList, paraMap)){
					resultList.add(partner);
				}
			}
			
			//处理广告主的扩展信息
			processPartnerExtendInfo(paraMap , resultList , true , false);
			
			page.setListMap(resultList);
		}
		
	}
	
	/**
	 * 为广告主列表处理广告主的扩展信息
	 * 
	 * @param paraMap
	 * @param partnerList
	 * @param needLastSumRow
	 * @param pvNeedFormat
	 */
	private void processPartnerExtendInfo(Map<String, Object> paraMap , List<Map<String, Object>> partnerList , boolean needLastSumRow , boolean pvNeedFormat){

		/**
		 * 每个广告主的基本信息
		 */
		for (Map<String, Object> partnerMap : partnerList) {
			// 处理展示信息
			long partnerId = (long) partnerMap.get("id");
			
			// 如果广告主不是【jtd公司】，就查询当前partner的运营人员
			Map<String, Object> operatorMap = userPartnerDao
					.findByPartnerAndRole(partnerId, RoleType.OPERATE);

			if (operatorMap != null) {
				// 把运营人员信息放到map中
				partnerMap.put("operator_name", operatorMap.get("user_name"));
			}
			
			//查询当前广告主是否有下级（以便判断是否显示折叠图标）
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("pid", partnerId);
			List<Map<String, Object>> partnerLevel = partnerDao.listMapByMap(params);
			if (partnerLevel != null && partnerLevel.size() > 0) {
				partnerMap.put("hasChildren","1");
			}
			
			//上线时间
			Date firstOnlineTime = (Date)partnerMap.get("first_online_time");
			if(firstOnlineTime != null){
				partnerMap.put("first_online_time", firstOnlineTime);
			}
			else{
				partnerMap.put("first_online_time", "--");
			}
			
			//读取行业信息 
			//ChannelCategory cc = getPartnerCategory(partnerId);
			//if(cc != null){
			//	partnerMap.put("category_name",cc.getName());
			//}
			
			//获取账户余额
			long accBallanceFen = partnerMap.get("acc_balance")!=null ? (long)partnerMap.get("acc_balance") : 0;
		    String accBalance = AccountAmountUtil.getAmountYuanString(accBallanceFen);
			partnerMap.put("acc_balance", accBalance);
			
			//如果是从boss系统同步过来的，要查询该用户是否已经跟boss系统同步成功
			if(StringUtils.isEmpty(partnerMap.get("boss_partner_code")) == false){
				//查询boss接口交互日志
				BossRequestLog param = new BossRequestLog();
				param.setBossPartnerCode((String) partnerMap.get("boss_partner_code"));
				//类型为回调
				param.setRequestType(BossApiType.CALLBACK_BOSS.getCode());
				//成功的标识=1
				param.setResultCode("1"); 
				List<BossRequestLog> logs = bossRequestLogDao.listBy(param);
				if(logs != null && logs.size() > 0){
					//表示成功
					partnerMap.put("boss_callback_result", 1);
				}
				else{
					partnerMap.put("boss_callback_result", 0);
				}
			}
			
		
		}
	
	}
	
	/**
	 * 
	 * @param partner
	 * @param allPartnerList
	 * @param paraMap
	 * @return
	 */
	private boolean filter(Map<String,Object> partner , List<Map<String,Object>> allPartnerList , Map<String, Object> paraMap){
		
		if(partner == null){
			return false ;
		}
		
		long partnerId = partner.get("id") != null ? (long) partner.get("id") : -1 ;
		String partner_partnerName = partner.get("partner_name") != null ? (String)partner.get("partner_name") : "" ;
		String partner_partnerPName = partner.get("pname") != null ? (String)partner.get("pname") : "" ;
		String partner_region = partner.get("region") != null ? (String)partner.get("region") : "" ;
		String partner_city = partner.get("city") != null ? (String)partner.get("city") : "" ;
		int partner_partnerType = partner.get("partner_type") != null ? (int)partner.get("partner_type") : -1 ;
		int partner_status = partner.get("status") != null ? (int) partner.get("status") : -1 ;
		Date partner_firstOnlineTime = partner.get("first_online_time") != null ? (Date)partner.get("first_online_time") : null ;
		/**
		 * ----------  看partner是否满足条件，如果满足则直接返回 -------------------
		 */
		boolean b = true ;
		if(paraMap.get("partnerName") != null){
			String partnerName = (String) paraMap.get("partnerName");
			if( partner_partnerName.indexOf(partnerName) < 0){
				b = false ;
			}
		}
		if(paraMap.get("partnerType") != null){
			int partnerType = (int)paraMap.get("partnerType");
			if(partner_partnerType != partnerType){
				b = false ;
			}
		}
		if(paraMap.get("status") != null){ 
			int status = (int) paraMap.get("status");
			if(partner_status != status){
				b = false ;
			}
		} 
		if(paraMap.get("partnerPName") != null){
			String partnerPName = (String) paraMap.get("partnerPName");
			if( partner_partnerPName.indexOf(partnerPName) < 0){
				b = false ;
			}
		}
		if(paraMap.get("region") != null){
			String region = (String) paraMap.get("region");
			if( partner_region.indexOf(region) < 0){
				b = false ;
			}
		}
		if(paraMap.get("city") != null){
			String city = (String) paraMap.get("city");  
			if( partner_city.indexOf(city) < 0){
				b = false ;
			}
		}
		if(b && paraMap.get("updateFristOnlineStratTime") != null){
			Date updateFristOnlineStratTime = (Date) paraMap.get("updateFristOnlineStratTime");
			//没有上线时间就不满足条件
			if(partner_firstOnlineTime == null){
				b = false ;
			}
			else{
				int i = partner_firstOnlineTime.compareTo(updateFristOnlineStratTime) ;
				//小于开始时间就不满足条件
				if(i == -1){
					b = false ;
				}
			}
		}
		if(b && paraMap.get("updateFristOnlineEndTime") != null){
			Date updateFristOnlineEndTime = (Date) paraMap.get("updateFristOnlineEndTime");
			//没有上线时间就不满足条件
			if(partner_firstOnlineTime == null){
				b = false ;
			}
			else{
				int i = partner_firstOnlineTime.compareTo(updateFristOnlineEndTime) ;
				//大于结束时间就不满足条件
				if(i == 1){
					b = false ;
				}
			}
		}
		
		if(b){
			partner.put("isTarget", "1");
			return true ;
		}
		/**
		 * ----------  看partner是否满足条件，如果满足则直接返回  end -------------------
		 */
		
		/**
		 * ----------  partner不满足条件，找到partner的下级 或下级的下级有没有符合条件的-------------------
		 */
		ArrayList<Map<String,Object>> sublist = new ArrayList<Map<String,Object>>();
		
		for (Map<String, Object> subPartner : allPartnerList) {
			
			long pid = (long)subPartner.get("pid");
			
			if(pid == partnerId){
				sublist.add(subPartner);
			}
			
		}
		
		for (Map<String, Object> subPartner : sublist) {
			
			if(filter(subPartner , allPartnerList , paraMap)){
				return true ;
			}
			
		}
		/**
		 * ----------  partner不满足条件，找到partner的下级 或下级的下级有没有符合条件的  end -------------------
		 */
		
		return false ;
	}
	
	
    @Override
    public List<Partner> findPartnerListByLoginUser(ActiveUser activeUser) {

        CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

        List<Partner> partners = new ArrayList<Partner>();
        if(activeUser.isAdminOrManager()) { // 管理员
            partners = partnerDao.listAll();
        }else if(activeUser.isOperateDirectorOrManager()) { //运营经理和运营总监
            Partner mPartner = new Partner();
            partners = partnerDao.listBy(mPartner);
            partners.add(activeUser.getPartner());
        }else { // 查询用户与广告主关系表(运营角色,代理广告主角色和广告主角色)
            UserPartner up = new UserPartner();
            up.setUserId(activeUser.getUserId().toString());

            // 查看当前用户下的广告主数据权限
            List<UserPartner> upList = userPartnerDao.listBy(up);
            List<Partner> partnerList = new ArrayList<Partner>();
            Map<String,Object> map = null;
            for(UserPartner userPartner: upList){

                if(activeUser.isOperateUser()) { //运营角色级联查询子集
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

        List<Partner> newList = new ArrayList<Partner>();
        for (Partner all:partners) {
            boolean flag = true;
            for(Partner p:newList){
                if(p.getId().equals(all.getId())){
                    flag = false;
                }
            }
            if(flag){
                newList.add(all);
            }
        }
        return newList;
    }

    @Override
    public double getPartnerRechargeAmountSumYuan(Long partnerId,String startDate,String endDate){
    	
    	CustomerContextHolder
		.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
    	
    	Long amountSum = partnerAccFlowDao.getAmountSum(partnerId, TradeType.RECHARGE, startDate, endDate) ;

    	return ReportUtil.getYuan(amountSum);
    }

	@Override
	public List<Map<String, Object>> findChildren(Map<String, Object> paraMap) {
	
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		
		List<Map<String,Object>> childrenList = partnerDao.listMapByMap(paraMap);
		
		if(childrenList != null && childrenList.size() > 0){
			processPartnerExtendInfo(paraMap , childrenList , false , true);
		}
		
		return childrenList ;
	}

	@Override
	public List<Partner> listByMap(Map<String, Object> map) {
        CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		return partnerDao.listByMap(map);
	}

    @Override
    public List<Partner> listOneLevelPartnerBy(Partner p) {
        CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
        return partnerDao.listOneLevelPartnerBy(p);
    }

	@Override
	public List<Partner>  listById(Long pid) {
		return partnerDao.listById(pid);
	}


}
