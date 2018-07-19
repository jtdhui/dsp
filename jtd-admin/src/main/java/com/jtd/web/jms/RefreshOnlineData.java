package com.jtd.web.jms;

import com.jtd.web.vo.BidRequestCountVo;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-common-entity
 * @描述 <p>刷新线上数据</p>
 */
public class RefreshOnlineData extends Message {

	private static final long serialVersionUID = 3712110258805724385L;

	private BidRequestCountVo countVo;
	public BidRequestCountVo getCountVo() {
		return countVo;
	}
	public void setCountVo(BidRequestCountVo countVo) {
		this.countVo = countVo;
	}
	@Override
	public MsgType getType() {
		return MsgType.REFRESH_ONLINE_DATA;
	}

}
