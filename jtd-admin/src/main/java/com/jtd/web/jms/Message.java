package com.jtd.web.jms;

import java.io.Serializable;
/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-common-entity
 * @描述 <p>消息抽象类，实现序列化接口，提供获取类型的虚函数</p>
 */
public abstract class Message implements Serializable{

	private static final long serialVersionUID = 2694385447105014714L;

	/**
	 * @return the type
	 */
	public abstract MsgType getType() ;
	

}
