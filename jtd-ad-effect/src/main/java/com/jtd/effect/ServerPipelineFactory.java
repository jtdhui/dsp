package com.jtd.effect;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.handler.codec.http.HttpChunkAggregator;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;
import org.jboss.netty.handler.timeout.IdleState;
import org.jboss.netty.handler.timeout.IdleStateHandler;
import org.jboss.netty.util.HashedWheelTimer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import static org.jboss.netty.channel.Channels.pipeline;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月12日
 * @项目名称 dsp-tracker
 * @描述 <p>在这里组装pipeline,过滤链条中有超时handler，netty自带的http解码器，http分片设置，编码器，最终处理分发的handler</p>
 */
@Component("serverPipelineFactory")
public class ServerPipelineFactory implements ChannelPipelineFactory {

	@Resource
	private ServerHandler serverHandler;
	
	private IdleStateHandler timeoutHandler;
	
	@PostConstruct
	public void init() {
		timeoutHandler = new IdleStateHandler(new HashedWheelTimer(), 5, 5, 0) {
			protected void channelIdle(ChannelHandlerContext ctx,
					IdleState state, long lastActivityTimeMillis)
					throws Exception {
				ctx.getChannel().close();
			}
		};
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ChannelPipelineFactory#getPipeline()
	 */
	@Override
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = pipeline();
		//请求超时处理
		pipeline.addLast("timeout", timeoutHandler);
		//解码处理
		pipeline.addLast("decoder", new HttpRequestDecoder());
		//分片聚集的工具类
		pipeline.addLast("aggregator", new HttpChunkAggregator(65536));
		//相应的时候，http的编码工具
		pipeline.addLast("encoder", new HttpResponseEncoder());
		//具体的业务处理
		pipeline.addLast("handler", serverHandler);
		return pipeline;
	}
}
