package com.jtd.effect;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.netty.channel.*;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月12日
 * @项目名称 dsp-tracker
 * @描述 <p>处理请求分发的Handler</p>
 */
@Component("serverHandler")
public class ServerHandler extends SimpleChannelUpstreamHandler implements ApplicationListener<ApplicationEvent> , ApplicationContextAware {

	private static final Log log = LogFactory.getLog(ServerHandler.class);

	private ApplicationContext applicationContext;
	private Map<String, HttpHandler> httpHanlderMap = new HashMap<String, HttpHandler>();

	/**
	 * 截取请求中得uri，根据uri取出handler，并调用handler的process方法，处理具体的业务。
	 */
    @Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		Object msg = e.getMessage();
		if (msg instanceof HttpRequest) {
			HttpRequest req = (HttpRequest) msg;
			String uri = req.getUri();
			if (uri == null) {
				ctx.getChannel().close();
				return;
			}
			int i = uri.indexOf("?");
			if (i != -1) uri = uri.substring(0, i);
			HttpHandler handler = httpHanlderMap.get(uri);
			if (handler != null) {
				handler.process(ctx, req);
			} else {
				ctx.getChannel().close();
			}
		} else {
			e.getChannel().close();
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see SimpleChannelUpstreamHandler#exceptionCaught(ChannelHandlerContext,
	 *      ExceptionEvent)
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
		ctx.getChannel().close();
		Throwable t = e.getCause();
		if (t != null && "Connection reset by peer".equalsIgnoreCase(t.getMessage())) {
			return;
		}
		log.error("exceptionCaught", e.getCause());
	}

	/**
	 * Invoked when a {@link Channel} is open, but not bound nor connected. <br/>
	 * 
	 * <strong>Be aware that this event is fired from within the Boss-Thread so
	 * you should not execute any heavy operation in there as it will block the
	 * dispatching to other workers!</strong>
	 */
	public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
	}

	/**
	 * Invoked when a {@link Channel} is open and bound to a local address, but
	 * not connected. <br/>
	 * 
	 * <strong>Be aware that this event is fired from within the Boss-Thread so
	 * you should not execute any heavy operation in there as it will block the
	 * dispatching to other workers!</strong>
	 */
	public void channelBound(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
	}

	/**
	 * Invoked when a {@link Channel} is open, bound to a local address, and
	 * connected to a remote address. <br/>
	 * 
	 * <strong>Be aware that this event is fired from within the Boss-Thread so
	 * you should not execute any heavy operation in there as it will block the
	 * dispatching to other workers!</strong>
	 */
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
	}

	/**
	 * Invoked when a {@link Channel}'s {@link Channel#getInterestOps()
	 * interestOps} was changed.
	 */
	public void channelInterestChanged(ChannelHandlerContext ctx,
			ChannelStateEvent e) throws Exception {
	}

	/**
	 * Invoked when a {@link Channel} was disconnected from its remote peer.
	 */
	public void channelDisconnected(ChannelHandlerContext ctx,
			ChannelStateEvent e) throws Exception {
	}

	/**
	 * Invoked when a {@link Channel} was unbound from the current local
	 * address.
	 */
	public void channelUnbound(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
	}

	/**
	 * Invoked when a {@link Channel} was closed and all its related resources
	 * were released.
	 */
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
	}

	/**
	 * Invoked when something was written into a {@link Channel}.
	 */
	public void writeComplete(ChannelHandlerContext ctx, WriteCompletionEvent e)
			throws Exception {
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	/**
	 * 该方法会在spring启动完成后调用，目的是将容器中所有标注了Handler注解的类，读取出来，
	 * 存放到httpHanlderMap中，key是注解中uri，value是handler本身。
	 */
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		//当spring容器启动完成后，会调用 applicationContext的publishEvent(event) 方法，发送事件为 ContextRefreshedEvent 事件；
		//这时 容器中实现了 ApplicationListener 接口的对象，都会收到这个消息
		if(event instanceof ContextRefreshedEvent) {
			//读取所有标注了Handler注解的类
			Map<String, Object> httpHandlers = applicationContext.getBeansWithAnnotation(Handler.class);
			for(Iterator<Object> it = httpHandlers.values().iterator(); it.hasNext();) {
				Object handler = it.next();
				String uri = handler.getClass().getAnnotation(Handler.class).uri();
				//判断是否为HttpHandler
				if(handler instanceof  HttpHandler) {
					//如果httpHanlderMap中没有这个uri，则将handle  put 到map中。
					if(!httpHanlderMap.containsKey(uri)) {
						httpHanlderMap.put(uri, (HttpHandler)handler);
						log.info("Found Handler: " + uri + " --> " + handler.getClass().getSimpleName());
					} else {
						String error = "URI[" + uri + "]的HttpHandler经存在, Handler[" + handler.getClass().getName() + "]无效";
						throw new IllegalStateException(error);
					}
				} else {
					String error = "URI[" + uri + "]配置的Handler[" + handler.getClass().getName() + "]不是" + HttpHandler.class.getName() + "的实现, 配置无效";
					throw new IllegalStateException(error);
				}
			}
		}
	}
}
