package com.jtd.engine.message.v1;

import java.util.List;
import java.util.Map;
import com.jtd.engine.adserver.message.v1.MessageV1;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p></p>
 */
public class HttpRequest extends MessageV1 {

	private static final long serialVersionUID = -6250623332502679758L;
	
	public static final int GET = 1;
	public static final int POST = 2;

	// 方法 1 GET 2 POST
	private int method;

	// 请求行
	private String reqLine;

	// URI
	private String uri;

	// query
	private String query;

	// HTTP请求中的参数
	private Map<String, List<String>> params;

	// HTTP请求中的Headers
	private Map<String, String> headers;

	//HTTP post 请求体
	private byte[] body = null;

	/**
	 * @return the method
	 */
	public int getMethod() {
		return method;
	}

	/**
	 * @param method the method to set
	 */
	public void setMethod(int method) {
		this.method = method;
	}

	public byte[] getBody() {
		return body;
	}

	public void setBody(byte[] body) {
		this.body = body;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.maxit.adserver.message.v1.MessageV1#type()
	 */
	public Type type() {
		return Type.HttpRequest;
	}

	/**
	 * @return the reqLine
	 */
	public String getReqLine() {
		return reqLine;
	}

	/**
	 * @param reqLine the reqLine to set
	 */
	public void setReqLine(String reqLine) {
		this.reqLine = reqLine;
	}

	/**
	 * @return the query
	 */
	public String getQuery() {
		return query;
	}

	/**
	 * @param query the query to set
	 */
	public void setQuery(String query) {
		this.query = query;
	}

	/**
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * @param uri
	 *            the uri to set
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}

	/**
	 * @return the params
	 */
	public Map<String, List<String>> getParams() {
		return params;
	}

	/**
	 * @param params
	 *            the params to set
	 */
	public void setParams(Map<String, List<String>> params) {
		this.params = params;
	}

	/**
	 * @return the headers
	 */
	public Map<String, String> getHeaders() {
		return headers;
	}

	/**
	 * @param headers
	 *            the headers to set
	 */
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
}
