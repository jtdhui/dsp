package com.jtd.engine.message.v1;

import com.jtd.engine.adserver.message.v1.MessageV1;

/**
 * Smaato RTB2.4 广告响应
 * @author zl
 *
 */
public class SmaatoBidResponse extends MessageV1{

	private static final long serialVersionUID = 3461695624219871115L;

	@Override
	public Type type() {
		return Type.SmaatoBidResponse;
	}

}
