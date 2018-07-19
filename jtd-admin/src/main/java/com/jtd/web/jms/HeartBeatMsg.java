package com.jtd.web.jms;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-common-entity
 * @描述 <p>统计系统发送的心跳</p>
 */
public class HeartBeatMsg extends Message{
	private static final long serialVersionUID = -2400742645501699836L;
	
	private String nodeName;

	/**
	 * @return the nodeName
	 */
	public String getNodeName() {
		return nodeName;
	}

	/**
	 * @param nodeName the nodeName to set
	 */
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	@Override
	public MsgType getType() {
		return MsgType.HEARTBEAT;
	}
}
