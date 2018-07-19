package com.jtd.engine.adserver.filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-engine
 * @描述 过滤所有的请求，在这个sessionIdle方法中，如果本地的地址与要监控的地址端口相同，则关闭这个链接。
 */
public class HttpSessionIdleFilter extends IoFilterAdapter {

	private static final Log log = LogFactory.getLog(HttpSessionIdleFilter.class);

	/**
	 * 在哪个本地地址上起作用
	 */
	private Set<InetSocketAddress> interceptAddresses = new HashSet<InetSocketAddress>();

	/**
	 * 当session空闲的时候关闭session，否则继续下一个过滤器
	 */
	public void sessionIdle(NextFilter nextFilter, IoSession session,
			IdleStatus status) throws Exception {
		if (interceptAddresses.contains(session.getLocalAddress())) {
			session.close(true);
		} else {
			nextFilter.sessionIdle(session, status);
		}
	}

	/**
	 * 设置需要被拦截的地址
	 * @param interceptAddresses
	 * @return void
	 */
	public void setInterceptAddresses(List<InetSocketAddress> interceptAddresses) {
		for(InetSocketAddress addr : interceptAddresses) {
			this.interceptAddresses.add(addr);
			log.info(this.getClass().getSimpleName() + "拦截地址: " + addr);
		}
	}

}
