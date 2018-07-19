package com.jtd.engine.message.v1;

import com.jtd.engine.adserver.message.v1.MessageV1;
/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p></p>
 */
public class Message000 extends MessageV1 {

	private static final long serialVersionUID = 4275528047740784419L;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.maxit.datadriver.message.v1.MessageV1#type()
	 */
	public Type type() {
		return Type.MSG000;
	}
}
