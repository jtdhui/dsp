package com.jtd.engine.utils;

import org.apache.commons.logging.Log;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;

import java.net.InetSocketAddress;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-engine
 * @描述
 */
public class SessionUtil {

	// 会话认证通过标志的KEY
	private static final AttributeKey AUTHENTICATION_VALID_KEY = new AttributeKey(SessionUtil.class, "AuthenticationValid");

	// 明文A的KEY
	private static final AttributeKey CLEAR_TEXT_A_KEY = new AttributeKey(SessionUtil.class, "ClearTextA");

	/**
	 * 会话是否已经是认证通过的
	 * @param session
	 * @return
	 * @return boolean
	 */
	public static boolean isAuthenticationValid(IoSession session) {
		return session.containsAttribute(AUTHENTICATION_VALID_KEY);
	}

	/**
	 * 设置会话认证通过标志
	 * @param session
	 * @return void
	 */
	public static void setAuthenticationValid(IoSession session) {
		session.setAttribute(AUTHENTICATION_VALID_KEY);
	}

	/**
	 * 清除会话认证通过标志
	 * @param session
	 * @return void
	 */
	public static void clearAuthentication(IoSession session) {
		session.removeAttribute(AUTHENTICATION_VALID_KEY);
	}

	/**
	 * 保存明文A到session中
	 * @param session
	 * @param cta
	 * @return void
	 */
	public static void setClearTextA(IoSession session, byte[] cta) {
		session.setAttribute(CLEAR_TEXT_A_KEY, cta);
	}

	/**
	 * 获取明文A
	 * @param session
	 * @return
	 * @return byte[]
	 */
	public static byte[] getClearTextA(IoSession session) {
		return (byte[]) session.getAttribute(CLEAR_TEXT_A_KEY);
	}

	/**
	 * 清除明文A
	 * @param session
	 * @return void
	 */
	public static void clearClearTextA(IoSession session) {
		session.removeAttribute(CLEAR_TEXT_A_KEY);
	}
	
	/**
	 * 断开连接
	 * @param session
	 * @param log
	 * @param reasion
	 * @return void
	 */
	public static void blockSession(IoSession session, Log log, String reasion) {
		InetSocketAddress addr = (InetSocketAddress) session.getRemoteAddress();
		log.warn(new StringBuilder("断开连接：[").append(
				addr.getAddress().getHostAddress()).append(":").append(
				addr.getPort()).append("] 原因: ").append(reasion));
		session.close(true);
	}

	/**
	 * 断开连接
	 * @param session
	 * @param log
	 * @param cause
	 * @return void
	 */
	public static void blockSession(IoSession session, Log log, Throwable cause) {
		InetSocketAddress addr = (InetSocketAddress) session.getRemoteAddress();
		log.error(new StringBuilder("断开连接：[").append(
				addr.getAddress().getHostAddress()).append(":").append(
				addr.getPort()).append("]"), cause);
		session.close(true);
	}

	/**
	 * 从会话对象中取出客户端IP地址
	 * @param session
	 * @return
	 * @return String
	 */
	public static String getRemoteHostAddress(IoSession session) {
		return ((InetSocketAddress) session.getRemoteAddress()).getAddress().getHostAddress();
	}
}
