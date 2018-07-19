package com.jtd.statistic;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.WriteCompletionEvent;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月11日
 * @项目名称 dsp-counter
 * @描述 <p></p>
 */
@Component("serverHandler")
public class ServerHandler extends SimpleChannelUpstreamHandler implements ApplicationListener<ApplicationEvent> , ApplicationContextAware {

	private static final Log log = LogFactory.getLog(ServerHandler.class);

	private ApplicationContext applicationContext;
	private Map<String, HttpHandler> httpHanlderMap = new HashMap<String, HttpHandler>();

    /** (non-Javadoc)
     * @see SimpleChannelUpstreamHandler#messageReceived(ChannelHandlerContext, MessageEvent)
     * 接收到消息后处理消息
     */
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        Object msg = e.getMessage();
        if (msg instanceof HttpRequest) {
        	HttpRequest req = (HttpRequest) msg;
        	String uri = req.getUri();
    		if(uri == null) {
    			ctx.getChannel().close();
    			return;
    		}
    		int i = uri.indexOf("?");
    		if(i != -1) uri = uri.substring(0, i);
    		HttpHandler handler = httpHanlderMap.get(uri);
    		if(handler != null) {
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
		if (t != null && ("Connection reset by peer".equalsIgnoreCase(t.getMessage()) || "Broken pipe".equalsIgnoreCase(t.getMessage()))) {
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

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if(event instanceof ContextRefreshedEvent) {
			//取所有，有Handler.class注解的类，放到map中，
			Map<String, Object> httpHandlers = applicationContext.getBeansWithAnnotation(Handler.class);
			for(Iterator<Object> it = httpHandlers.values().iterator(); it.hasNext();) {
				Object handler = it.next();
				String uri = handler.getClass().getAnnotation(Handler.class).uri();
				if(handler instanceof  HttpHandler) {
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
