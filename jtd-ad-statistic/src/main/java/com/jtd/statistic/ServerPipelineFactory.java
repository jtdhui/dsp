package com.jtd.statistic;

import static org.jboss.netty.channel.Channels.pipeline;

import javax.annotation.Resource;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.handler.codec.http.HttpChunkAggregator;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;
import org.springframework.stereotype.Component;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月11日
 * @项目名称 dsp-counter
 * @描述 <p></p>
 */
@Component("serverPipelineFactory")
public class ServerPipelineFactory implements ChannelPipelineFactory {

	@Resource
	private ServerHandler serverHandler;

	/**
	 * (non-Javadoc)
	 * 
	 * @see ChannelPipelineFactory#getPipeline()
	 */
	@Override
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = pipeline();
		/**
		 请求的时候，http的解码工具
		 int maxInitialLineLength, 
		 请求头中，最大的行数
		 int maxHeaderSize, 
		 请求头最大字节数
		 int maxChunkSize
		 http分片，最大字节数，给post请求体分片，图片文件，或是json
		 */
		pipeline.addLast("decoder", new HttpRequestDecoder(16384, 32768, 32768));
		//分片聚集的工具类
		pipeline.addLast("aggregator", new HttpChunkAggregator(65536));
		//相应的时候，http的编码工具
		pipeline.addLast("encoder", new HttpResponseEncoder());
		//具体的业务处理
		pipeline.addLast("handler", serverHandler);
		return pipeline;
	}
}
