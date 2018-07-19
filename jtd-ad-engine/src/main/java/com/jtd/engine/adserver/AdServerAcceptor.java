package com.jtd.engine.adserver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoServiceListener;
import org.apache.mina.core.session.IoSessionConfig;
import org.apache.mina.transport.socket.SocketAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月22日
 * @项目名称 dsp-engine
 * @描述 
 */
public class AdServerAcceptor {

	private static final Log log = LogFactory.getLog(AdServerAcceptor.class);

	// MINA的acceptor，在spring中将
	private SocketAcceptor acceptor;

	// 过滤器
	private Map<String, IoFilter> filters;

	// 监听器
	private IoServiceListener[] listeners;

	// 处理器
	private IoHandler handler;

	// socket参数
	private IoSessionConfig sessionConfig; 

	// 需要绑定的地址
	private List<InetSocketAddress> addresses;

	/**
	 * 初始化，该方法会在spring实例化类时调用
	 * @return void
	 */
	public void init() {
		
		if (acceptor == null) {
			throw new IllegalStateException("Acceptor没有设置");
		}

		// 配置
		if (sessionConfig != null) {
			acceptor.getSessionConfig().setAll(sessionConfig);
		}

		// 添加监听器
		if (listeners != null) {
			for (IoServiceListener listener : listeners) {
				acceptor.addListener(listener);
				log.info("Acceptor添加监听器: " + listener.getClass().getName());
			}
		}

		// 配置过滤器链
		if (filters == null) {
			throw new IllegalStateException("filters没有设置");
		}
		DefaultIoFilterChainBuilder filterChain = acceptor.getFilterChain();
		
		for(Entry<String,IoFilter>  entry : filters.entrySet()){
			String name = entry.getKey();
			IoFilter filter = entry.getValue();
			filterChain.addLast(name, filter);
			log.info("Acceptor添加过滤器: " + name);
		}

		// 设置处理器
		if (handler == null) {
			throw new IllegalStateException("handler没有设置");
		}
		acceptor.setHandler(handler);
		log.info("Acceptor设置处理器: " + handler.getClass().getSimpleName());
		log.info("Acceptor初始化成功");
	}

	/**
	 * 绑服务地址
	 * 
	 * @throws IOException
	 */
	public void bind() throws IOException {
		acceptor.bind(addresses);
		log.info("Acceptor绑定服务地址成功");
	}

	/**
	 * 销毁acceptor，释放资源
	 * @return void
	 */
	public void destroy() {
		//解除绑定
		acceptor.unbind();
		acceptor.dispose();
		log.info("Acceptor销毁完成");
	}

	/**
	 * 设置SocketAcceptor，在spring中注入
	 * @param acceptor
	 * @return void
	 */
	public void setAcceptor(SocketAcceptor acceptor) {
		this.acceptor = acceptor;
	}

	/**
	 * 设置过滤器集合，在spring中注入
	 * @param filters
	 * @return void
	 */
	public void setFilters(Map<String, IoFilter> filters) {
		this.filters = filters;
	}

	/**
	 * 设置监听，在spring中注入，暂时没有用到
	 * @param listeners
	 * @return void
	 */
	public void setListeners(IoServiceListener[] listeners) {
		this.listeners = listeners;
	}

	/**
	 * 在spring中注入实际的业务处理逻辑
	 * @param handler
	 * @return void
	 */
	public void setHandler(IoHandler handler) {
		this.handler = handler;
	}

	/**
	 * 在spring中注入服务端的监听地址和端口
	 * @param addresses
	 * @return void
	 */
	public void setAddresses(List<InetSocketAddress> addresses) {
		this.addresses = addresses;
	}

	/**
	 * 在spring中注入sessionConfig配置
	 * @param sessionConfig
	 * @return void
	 */
	public void setSessionConfig(IoSessionConfig sessionConfig) {
		this.sessionConfig = sessionConfig;
	}
}