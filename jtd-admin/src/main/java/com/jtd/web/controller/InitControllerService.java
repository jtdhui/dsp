package com.jtd.web.controller;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.jtd.web.constants.AuditStatus;
import com.jtd.web.constants.CatgSerial;
import com.jtd.web.constants.PartnerStatus;
import com.jtd.web.dao.IPartnerDao;
import com.jtd.web.dao.impl.PartnerStatusDao;
import com.jtd.web.dao.impl.QualiDocDao;
import com.jtd.web.po.Partner;
import com.jtd.web.po.QualiDoc;
import com.jtd.web.service.IMQConnectorService;
import com.jtd.web.service.admin.IAdminPartnerService;

@Service
public class InitControllerService {
	
	Logger logger = Logger.getLogger(InitControllerService.class);

	@Autowired
	private IAdminPartnerService partnerService;

	@Autowired
	private QualiDocDao qualiDocDao;

	@Autowired
	private PartnerStatusDao partnerStatusDao;

	@Autowired
	private IMQConnectorService mqConnectorService;
	
	@Autowired
	private IPartnerDao partnerDao;
	

	public void initPartnerMQ(String partnerId , HttpServletResponse response) throws Exception {

		PrintWriter pw = response.getWriter();

		List<Partner> list = null ;
		if(StringUtils.isEmpty(partnerId) == false){
			
			HashMap<String,Object> params = new HashMap<String, Object>();
			params.put("id", partnerId);
			list = partnerDao.listByMap(params);
		}
		else{
			list = partnerDao.listAll();
		}
		
		if (list != null) {

			pw.print("partner总数：" + list.size() + "<br/>");

			for (Partner partner : list) {

				pw.print("检查partner[" + partner.getId() + "]<br/>");

				pw.print("partner[" + partner.getId() + "]的毛利为"
						+ partner.getGrossProfit() + "，开始发送MQ<br/>");

				// 1.发送毛利设置的MQ
				mqConnectorService.sendSetPartnerGrossProfitMessage(
						partner.getId(),
						(int) Math.round(partner.getGrossProfit() * 100));

				// 查看partner的资质的内部审核情况
				List<QualiDoc> docList = qualiDocDao.listByPartnerId(partner
						.getId());
				if (docList != null && docList.size() > 0) {

					QualiDoc doc = docList.get(0);

					if (doc.getStatus() == AuditStatus.STATUS_SUCCESS.getCode()) {

						pw.print("partner[" + partner.getId()
								+ "]已通过内部审核，开始发送MQ<br/>");

						// 2.发送内部审核MQ
						mqConnectorService.sendAuditPartnerMessage(
								partner.getId(), null,
								AuditStatus.STATUS_SUCCESS);

						// 查看partner的渠道审核情况
						List<Map<String, Object>> channelStatusList = partnerStatusDao
								.listAllChannelAuditStatusByPartnerId(partner
										.getId());
						if (channelStatusList != null) {

							for (Map<String, Object> map : channelStatusList) {
								Long channelId = (Long) map.get("channel_id");
								Integer status = (Integer) map.get("status");

								// 如果status不为空则表示在该渠道有审核情况
								if (status != null) {

									CatgSerial channel = CatgSerial
											.fromChannelId(channelId);

									pw.print("partner[" + partner.getId()
											+ "]在渠道" + channel.getName()
											+ "存在审核情况，开始发送MQ<br/>");

									// 3.发送提交渠道审核的MQ
									mqConnectorService
											.sendPartnerCommitChannelMessage(
													partner.getId(), channel);

									if (status == AuditStatus.STATUS_SUCCESS
											.getCode()) {

										pw.print("partner["
												+ partner.getId()
												+ "]在渠道"
												+ channel.getName()
												+ "审核状态为"
												+ AuditStatus.STATUS_SUCCESS
														.getDesc()
												+ "，开始发送MQ<br/>");

										// 4.发送渠道审核结果的MQ
										mqConnectorService
												.sendAuditPartnerMessage(
														partner.getId(),
														channel,
														AuditStatus.STATUS_SUCCESS);
									}
									if (status == AuditStatus.STATUS_FAIL
											.getCode()) {

										pw.print("partner["
												+ partner.getId()
												+ "]在渠道"
												+ channel.getName()
												+ "审核状态为"
												+ AuditStatus.STATUS_FAIL
														.getDesc()
												+ "，开始发送MQ<br/>");

										// 4.发送渠道审核结果的MQ
										mqConnectorService
												.sendAuditPartnerMessage(
														partner.getId(),
														channel,
														AuditStatus.STATUS_FAIL);
									}
								}
							}

						}

					}

					pw.print("partner[" + partner.getId() + "]的状态为"
							+ PartnerStatus.fromCode(partner.getStatus())
							+ "，开始发送MQ<br/>");

					// 5.发送改变账户状态的MQ
					mqConnectorService.sendPartnerStatusChangeMessage(
							partner.getId(),
							PartnerStatus.fromCode(partner.getStatus()));
				}
			}
		}

	}

}
