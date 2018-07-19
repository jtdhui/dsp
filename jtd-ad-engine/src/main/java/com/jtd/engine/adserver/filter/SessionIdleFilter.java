package com.jtd.engine.adserver.filter;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import com.jtd.engine.utils.SessionUtil;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p></p>
 */
public class SessionIdleFilter extends IoFilterAdapter {

	private static final Log log = LogFactory.getLog(SessionIdleFilter.class);

	// 在哪个本地地址上起作用
	private Set<InetSocketAddress> interceptAddresses = new HashSet<InetSocketAddress>();

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.apache.mina.core.filterchain.IoFilterAdapter#sessionIdle(org.apache.mina.core.filterchain.IoFilter.NextFilter,
	 *      org.apache.mina.core.session.IoSession,
	 *      org.apache.mina.core.session.IdleStatus)
	 */
	public void sessionIdle(NextFilter nextFilter, IoSession session,
			IdleStatus status) throws Exception {
		if (interceptAddresses.contains(session.getLocalAddress())) {
			if (status.equals(IdleStatus.READER_IDLE) && !SessionUtil.isAuthenticationValid(session)) {
				SessionUtil.blockSession(session, log, "认证超时");
			} else {
				nextFilter.sessionIdle(session, status);
			}
		} else {
			nextFilter.sessionIdle(session, status);
		}
	}

	/**
	 * @param interceptAddress
	 *            the interceptAddress to set
	 */
	public void setInterceptAddresses(List<InetSocketAddress> interceptAddresses) {
		for(InetSocketAddress addr : interceptAddresses) {
			this.interceptAddresses.add(addr);
			log.info(this.getClass().getSimpleName() + "拦截地址: " + addr);
		}
	}
}
