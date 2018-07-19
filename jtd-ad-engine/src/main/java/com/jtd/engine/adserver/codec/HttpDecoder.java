package com.jtd.engine.adserver.codec;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.charset.CharacterCodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderAdapter;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.jtd.engine.message.Message;
import com.jtd.engine.message.v1.HttpRequest;
import com.jtd.engine.utils.SessionUtil;



/**
 * 
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-engine
 * @描述 http解码器
 */
public class HttpDecoder extends ProtocolDecoderAdapter implements AddressMappedCodec {

	private static final Log log = LogFactory.getLog(HttpDecoder.class);

	// 解码用的Buffer的key
	private static final AttributeKey DECODE_BUFFER_KEY = new AttributeKey(HttpDecoder.class, "HttpDecodeBufferKey");

	// 解码器服务的地址
	private List<InetSocketAddress> serviceAddresses;

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.apache.mina.filter.codec.CumulativeProtocolDecoder#decode(org.apache.mina.core.session.IoSession,
	 *      org.apache.mina.core.buffer.IoBuffer,
	 *      org.apache.mina.filter.codec.ProtocolDecoderOutput)
	 */
	public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out)
			throws Exception {

		if (in.hasRemaining()) {

			Context context = getContext(session);
			context.read(in);

			// 0是没收全
			for (int i = context.tryParse(); i != 0; i = context.tryParse()) {
				if (i == 1) {
					// 解析出来了请求
					Message msg = context.msg;
					context.msg = null;
					out.write(msg);
				} else if (i == 2) {
					// 解析出错或者没有解析到
					SessionUtil.blockSession(session, log, "解析Http请求出错");
					break;
				}
			}
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.apache.mina.filter.codec.ProtocolDecoderAdapter#dispose(org.apache.mina.core.session.IoSession)
	 */
	public void dispose(IoSession session) throws Exception {
		removeContext(session);
	}

	/**
	 * 获取session中的context
	 * 
	 * @param session
	 * @return
	 */
	private Context getContext(IoSession session) {
		Context context = (Context) session.getAttribute(DECODE_BUFFER_KEY);
		if (context == null) {
			context = new Context();
			session.setAttribute(DECODE_BUFFER_KEY, context);
		}
		return context;
	}

	/**
	 * 清除session中的context
	 * 
	 * @param session
	 */
	private void removeContext(IoSession session) {
		Context context = (Context) session.getAttribute(DECODE_BUFFER_KEY);
		if (context != null) {
			context.msgBuf.free();
		}
		session.removeAttribute(DECODE_BUFFER_KEY);
	}

	/**
	 * @return the serviceAddress
	 */
	public List<InetSocketAddress> getServiceAddresses() {
		return serviceAddresses;
	}

	/**
	 * @param serviceAddress the serviceAddress to set
	 */
	public void setServiceAddresses(List<InetSocketAddress> serviceAddresses) {
		this.serviceAddresses = serviceAddresses;
		for(InetSocketAddress addr : serviceAddresses)
		log.info(this.getClass().getSimpleName() + "服务地址: " + addr);
	}

	/**
	 * @作者 Amos Xu
	 * @版本 V1.0
	 * @配置 
	 * @创建日期 2016年10月10日
	 * @项目名称 dsp-engine
	 * @描述 <p></p>
	 */
	private static class Context {
		
		private static final ThreadLocal<byte[]> Bytes = new ThreadLocal<byte[]>() {
			protected byte[] initialValue() {
				return new byte[1024];
			}
		};

		// HTTP请求头的最大长度只允许8K
		private static final int MAX_HTTP_REQ_LEN = 16384;

		// 单个请求允许的最大的请求体长度 1M
		private static final int MAX_HTTP_CONTENT_LEN = 1048576;

		private int method; // GET 1 POST 2

		// 已接收到的请求的长度
		private int msgLen;
		
		// 请求行
		private String reqLine;

		// 请求头
		private Map<String, String> headers = new HashMap<String, String>();

		// 请求体开始的位置
		private int bodyStartIndex = -1;

		// 当前解析的行
		private int lineStartIndex = 0;

		// 接收buffer
		private IoBuffer msgBuf;

		// 解出来的请求
		private Message msg;

		private Context() {
			msgLen = 0;
			//开辟一个1024个字节大小的IoBuffer缓冲区，并设置这个IoBuffer支持动态扩展
			msgBuf = IoBuffer.allocate(1024).setAutoExpand(true);
		}

		/**
		 * 读数据
		 * 
		 * @param in
		 */
		private void read(IoBuffer in) {
			msgBuf = msgBuf.put(in);
			msgLen = msgBuf.position();
		}

		/**
		 * 解析请求
		 * @return 0:请求头没有接受完整; 1:解析成功; 2:解析错误
		 */
		private int tryParse() {
			
			if (method == 0) {
				if (msgLen < 4) return 0;
				if (msgBuf.get(0) == 'G' && msgBuf.get(1) == 'E' && msgBuf.get(2) == 'T') {
					method = 1;
				} else if (msgBuf.get(0) == 'P' && msgBuf.get(1) == 'O' && msgBuf.get(2) == 'S' && msgBuf.get(3) == 'T') {
					method = 2;
				} else {
					
					// 不是GET和POST的,不管
					return 2;
				}
			}

			switch(method) {
			case 1:
				if (msgLen > MAX_HTTP_REQ_LEN) {
	
					// 请求超长,直接开始解析
					log.warn("请求超长: " + msgLen);
					return parseGet() ? 1 : 2;
				} else {
	
					// 判断请求头是否已经接收全, 判断依据为连续的\r\n\r\n
					if (msgLen > 4 
							&& msgBuf.get(msgLen - 4) == '\r'
							&& msgBuf.get(msgLen - 3) == '\n'
							&& msgBuf.get(msgLen - 2) == '\r'
							&& msgBuf.get(msgLen - 1) == '\n') {
	
						// 已经收完全,开始解析
						return parseGet() ? 1 : 2;
					} else {
	
						// 没有接受完全,继续读
						return 0;
					}
				}

			case 2:
				return parsePost();
			}

			return 2;
		}

		/**
		 * 解析HTTP请求
		 * @return
		 */
		private boolean parseGet() {

			// 把请求头解析出来
			try {
				parseLines(msgBuf);
			} catch (CharacterCodingException e) {

				// 解析行出错
				log.error("解码错误", e);
				return false;
			} catch (UnsupportedEncodingException e) {
				// 绝不可能不支持ascii编码
				return false;
			}

			// 获取请求行
			String req = null;
			if (reqLine != null) {

				req = reqLine;

				// 找到请求串后面的空格
				int i = req.lastIndexOf((char) 32);

				// 我们只处理GET方法
				if (i > 4) {

					// 取出请求串
					req = req.substring(4, i);
				} else {
					log.error(reqLine + "不合法的GET请求");
					return false;
				}
			} else {
				log.error("没有解析出一行数据");
				return false;
			}

			// 解析URI和query
			String[] uriquery = parseURIQuery(req);

			// 解析请求行得到请求参数
			Map<String, List<String>> params = parseParams(uriquery[1]);

			// 封消息
			msg = new HttpRequest();
			((HttpRequest) msg).setMethod(1);
			((HttpRequest) msg).setReqLine(reqLine);
			((HttpRequest) msg).setUri(uriquery[0]);
			((HttpRequest) msg).setQuery(uriquery[1]);
			((HttpRequest) msg).setParams(params);
			Map<String, String> hs = new HashMap<String, String>();
			hs.putAll(headers);
			((HttpRequest) msg).setHeaders(hs);

			// 清理
			method = 0;
			msgLen -= bodyStartIndex;
			reqLine = null;
			headers.clear();
			bodyStartIndex = -1;
			lineStartIndex = 0;
			msgBuf.compact();

			return true;
		}
		
		// 0 未收全  1成功  2失败
		private int parsePost() {
			
			// 把请求头解析出来
			try {
				parseLines(msgBuf);
			} catch (CharacterCodingException e) {

				// 解析行出错
				log.error("解码错误", e);
				return 2;
			} catch (UnsupportedEncodingException e) {
				// 绝不可能不支持ascii编码
				return 2;
			}

			
			// 看看请求头收完了没
			if(bodyStartIndex == -1) return 0;

			String contentLen = headers.get("content-length");
			if(contentLen == null) return 0;

			int clen = 0;
			try {
				clen = Integer.parseInt(contentLen);
			} catch(Exception e) {
				log.error("Content-Length: " + contentLen + "不合法", e);
				return 2;
			}
			
			if(clen > MAX_HTTP_CONTENT_LEN) {
				log.error("Content-Length: " + clen + "超过最大允许长度");
				return 2;
			}

			// 看看请求体收完了没
			if(msgLen >= clen + bodyStartIndex) {

				// 收完了,解析出来
				String req = null;
				if (reqLine != null) {

					req = reqLine;

					// 找到请求串后面的空格
					int i = req.lastIndexOf((char) 32);

					if (i > 5) {

						// 取出请求串
						req = req.substring(5, i);
					} else {
						log.error(reqLine + "不合法的POST请求");
						return 2;
					}
				} else {
					log.error("没有解析出一行数据");
					return 2;
				}

				// 解析URI和query
				String[] uriquery = parseURIQuery(req);

				// 解析请求行得到请求参数
				Map<String, List<String>> params = parseParams(uriquery[1]);

				// 请求头
				Map<String, String> hs = new HashMap<String, String>();
				hs.putAll(headers);

				// 请求体
				byte[] body = new byte[clen];
				msgBuf.position(bodyStartIndex);
				msgBuf.get(body, 0, clen);

				// 封消息
				msg = new HttpRequest();
				((HttpRequest) msg).setMethod(2);
				((HttpRequest) msg).setReqLine(reqLine);
				((HttpRequest) msg).setUri(uriquery[0]);
				((HttpRequest) msg).setQuery(uriquery[1]);
				((HttpRequest) msg).setParams(params);
				((HttpRequest) msg).setHeaders(hs);
				((HttpRequest) msg).setBody(body);

				// 清理
				method = 0;
				msgLen -= clen + bodyStartIndex;
				reqLine = null;
				headers.clear();
				bodyStartIndex = -1;
				lineStartIndex = 0;
				msgBuf.compact();
				return 1;
			}

			return 0;
		}

		/**
		 * 从buffer里解析出所有的行
		 * 
		 * @param buf
		 * @return
		 * @throws CharacterCodingException
		 * @throws UnsupportedEncodingException 
		 */
		private void parseLines(IoBuffer buf) throws CharacterCodingException, UnsupportedEncodingException {

			int position = buf.position();
			buf.flip();
			int limit = buf.limit();
			buf.position(lineStartIndex);

			// 一行一行地解析
			for (int posr = -1, posn = -1; buf.hasRemaining();) {

				switch (buf.get()) {

				case '\r':
					posr = buf.position();
					break;

				case '\n':
					posn = buf.position();
					break;
				}

				if (posr != -1 && posn == posr + 1) {

					// 找到了一行，解析出来
					int lineEndIndex = posr - 1;
					posr = -1;
					posn = -1;
					buf.position(lineStartIndex);
					buf.limit(lineEndIndex);

					int len = lineEndIndex - lineStartIndex;
					if(len == 0) {
						// 请求头的最后一行了
						bodyStartIndex = lineEndIndex + 2;
						lineStartIndex = 0;
						buf.position(position);
						buf.limit(limit);
						return;
					}
					byte[] b = Bytes.get();
					if(b.length < len) b = new byte[len];
					buf.get(b, 0, len);
					String line = new String(b, 0, len, "ASCII");
					if (line.length() > 0) {
						if(reqLine == null) {
							reqLine = line;
						} else {
							parseHeaderLine(line);
						}
					}
					buf.position(lineEndIndex + 2);
					buf.limit(limit);
					lineStartIndex = lineEndIndex + 2;
				}
			}
			
			buf.position(position);
			buf.limit(limit);
		}

		/**
		 * 解析URI
		 * @param reqLine
		 * @return
		 */
		private String[] parseURIQuery(String req) {

			// ? 前面是URI
			int index = req.indexOf("?");
			if (index != -1) {
				return new String[]{req.substring(0, index), req.substring(index + 1)};
			} else {
				return new String[]{req, ""};
			}
		}

		/**
		 * 解析参数
		 * @param reqLine
		 * @return
		 */
		private Map<String, List<String>> parseParams(String query) {
			Map<String, List<String>> ret = new HashMap<String, List<String>>();
			String[] entrys = query.split("&");
			for (String entry : entrys) {
				String[] kv = entry.split("=");
				if (kv.length >= 1 && kv[0].trim().length() == 0) continue;
				if (kv.length == 1) {
					if (!ret.containsKey(kv[0])) {
						ret.put(kv[0], new ArrayList<String>(1));
					}
				} else if (kv.length == 2) {
					if (ret.containsKey(kv[0])) {
						ret.get(kv[0]).add(kv[1].trim());
					} else {
						List<String> list = new ArrayList<String>(1);
						list.add(kv[1].trim());
						ret.put(kv[0], list);
					}
				}
			}
			return ret;
		}

		/**
		 * 解析Headers, 所有的Header Name都是小写格式
		 * @param lines
		 * @return
		 */
		private void parseHeaderLine(String line) {
			int index = line.indexOf(":");
			if (index == -1) return;
			String name = line.substring(0, index).toLowerCase();
			String value = line.substring(index + 1).trim();
			headers.put(name, value);
		}
	}
}
