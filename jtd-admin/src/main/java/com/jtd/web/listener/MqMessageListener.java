package com.jtd.web.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jtd.web.constants.CampaignStatus;
import com.jtd.web.dao.ICampaignDao;
import com.jtd.web.jms.ChangeAutoStatusMsg;
import com.jtd.web.jms.RefreshOnlineData;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置
 * @创建日期 2016年8月25日
 * @项目名称 dsp-admin
 * @描述 <p>
 *     MQ监听器
 *     </p>
 */
@Component(value = "mqMessageListener")
public class MqMessageListener implements MessageListener {
	private Log log = LogFactory.getLog(this.getClass());

	@Resource
	private ICampaignDao campaignDao;

	@Override
	public void onMessage(Message message) {
		if (message instanceof TextMessage) {
			TextMessage tm = (TextMessage) message;
			com.jtd.web.jms.Message dspMessage = null;
			try {
				dspMessage = JSON.parseObject(tm.getText(),
						com.jtd.web.jms.Message.class);
			} catch (Exception e) {
				log.error("读取message发生异常", e);
			}
			if (dspMessage == null) {
				return;
			}
			switch (dspMessage.getType()) {
			case CHANGE_AUTO_STATUS: 
				// 修改活动自动状态
				ChangeAutoStatusMsg changeAutoStatusMsg = (ChangeAutoStatusMsg) dspMessage;
				String jsonStr = JSON.toJSONString(changeAutoStatusMsg,
						SerializerFeature.WriteClassName);
				log.info(" MqMessageListener 修改活动自动状态: " + jsonStr);
				com.jtd.web.po.Campaign camp = new com.jtd.web.po.Campaign();
				camp = campaignDao.getById(changeAutoStatusMsg.getCampid());
				camp.setAutoStatus(changeAutoStatusMsg.getStatus());
				CampaignStatus campaignStatus = camp.getCampaignStatus();
                int orderBy = 1;
                switch (campaignStatus){
                    case EDIT:
                        orderBy = 4;
                        break;
                    case TOCOMMIT:
                        orderBy = 5;
                        break;
                    case READY:
                        orderBy = 3;
                        break;
                    case ONLINE:
                        orderBy = 1;
                        break;
                    case PAUSED:
                        orderBy = 6;
                        break;
                    case OFFLINE:
                        orderBy = 2;
                        break;
                    case FINISHED:
                        orderBy = 7;
                        break;
                    case DELETE:
                        orderBy = 8;
                        break;
                }

				campaignDao.updateAutoStatus(changeAutoStatusMsg.getCampid(),
						changeAutoStatusMsg.getStatus().getCode(),
						campaignStatus.getCode(),orderBy);

				break;
			case REFRESH_ONLINE_DATA:
				RefreshOnlineData refreshOnlineData = (RefreshOnlineData) dspMessage;
				if (refreshOnlineData.getCountVo() != null) {
					// adPlaceService.refreshData(refreshOnlineData.getCountVo());
					// appService.refreshData(refreshOnlineData.getCountVo());
					// mediaPacketService.refreshData(refreshOnlineData.getCountVo());
				}
				break;
			default:
				return;
			}
		}
	}

}
