package test;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.utils.DateUtil;
import com.jtd.utils.FileUtil;
import com.jtd.utils.ReportUtil;
import com.jtd.web.constants.CatgSerial;
import com.jtd.web.constants.CookieType;
import com.jtd.web.controller.DicController;
import com.jtd.web.dao.IPardCountDao;
import com.jtd.web.dao.IPartnerDao;
import com.jtd.web.po.Partner;
import com.jtd.web.po.RetargetPacket;
import com.jtd.web.service.IMQConnectorService;
import com.jtd.web.service.admin.IAdminPartnerService;
import com.jtd.web.service.front.IFrontAccountService;
import com.jtd.web.vo.ChannelCategory;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/applicationContext-dao.xml",
		"classpath*:/applicationContext-service.xml",
		"classpath*:/applicationContext-transation.xml",																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																				
		"classpath*:/applicationContext-shiro.xml",
		"classpath*:/springmvc.xml"})

public class UnitTest {
	
	
	private final Logger logger = LoggerFactory.getLogger(UnitTest.class);

	private final Logger operateLogger = LoggerFactory.getLogger("operateLogger");

	@Autowired
	private IPartnerDao partnerDao;

	@Autowired
	private IPardCountDao pardDao;
	
	@Autowired
	private IAdminPartnerService partnerService ;
	
	@Autowired
	private IFrontAccountService accountService ;

	@Autowired
	private IMQConnectorService mqService ;

	//@Test
	public void testGetSum() {
		CustomerContextHolder
				.setContextType(CustomerContextHolder.SESSION_FACTORY_COUNT);
		try {
			
			Map<String, Object> map = pardDao.getSum(140L, DateUtil.getDate("20161101"),
					DateUtil.getDate("20161102"));
			System.out.println("pv_sum" + map.get("pv_sum"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void testCheckAccBalanceTransaction(){
		
		Partner p = partnerDao.getById(280);
		
		boolean b = partnerService.checkPartnerAccBalance(p);
	
		logger.info("b = {}" , b);
	}
	
	//@Test
	public void testUpdateAccBalanceTransaction() throws Exception{
		
//		partnerService.updateAccountBalance(140, 200, 1, "admin");
		
	}
	
	//@Test
	public void testSavePartnerTransaction() throws Exception{
		
		Partner p = new Partner();
		p.setPid(139L);
		p.setSimpleName("阿迪");
		p.setPartnerName("阿迪达斯");
		p.setCategoryId(80201L);
		
//		partnerService.saveOrUpdate(p, 1);
		
	}
	
	//@Test
	public void testDicController() throws Exception{
		
		ChannelCategory cc = DicController.getDSPChannelCategory(80201L);
		Map<CatgSerial, Long> map = cc.getChannelCategorys();
		Set<CatgSerial> keySet = map.keySet();
		for (CatgSerial key : keySet) {
			System.out.println(key + " " + map.get(key));
		}
	}
	
	//@Test
	public void testBase64() throws Exception{
		File file = new File("d:/track.js");
		String s = FileUtil.readAsBase64String(file);
		
		System.out.println(s);
	}
	
	//@Test
	public void testInsertRetargetPacketTransaction() throws Exception{
		
		RetargetPacket rpGlobal = new RetargetPacket();
		rpGlobal.setOwnerPartnerId(888L);
		rpGlobal.setPacketName("zrtest-全站访客找回");
		rpGlobal.setRemark("global");
		
		accountService.insertRetargetPacketMap(rpGlobal, CookieType.RETARGET);
		
	}

	//@Test
	public void testMQ(){
		mqService.sendPartnerCommitChannelMessage(158, CatgSerial.BES);
	}
	
	//@Test
	public void testListPartnerHierarchy(){
		
		//List<Partner> list = partnerService.listPartnerHierarchy(185L);
//		for (Partner partner : list) {
//			logger.info(partner.getId().toString());
//		}
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id",185);
		map.put("pid", 0);
		List<Map<String, Object>> partnerList = partnerService.listTreeByMap(map);
		for (Map<String, Object> map2 : partnerList) {
			logger.info(map2.get("id").toString());
		}
		
	}
	
	@Test
	public void testReportUtil(){
		System.out.println(ReportUtil.getClickRate(100, 0));
	}
	
}
