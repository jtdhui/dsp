package com.jtd.engine.dao;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p>定时接收统计系统的心跳，统计系统一旦有节点死掉即停止竞价</p>
 */
public interface HeartBeatDAO {

	/**
	 * 接收统计系统的心跳
	 */
	public void heartBeat(String nodeName);

	/**
	 * 是否活着
	 */
	public boolean isAlive();
	
	/**
	 * 刷新状态
	 */
	public void refresh();
}
