package com.jtd.engine.message.v1;

import com.jtd.engine.adserver.message.v1.MessageV1;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p></p>
 */
public class HttpResponse extends MessageV1 {

	private static final long serialVersionUID = -4685276655210562657L;
	
	private static final byte[] B0 = new byte[0];

	// 响应的状态,状态文本,响应头以及响应的内容
	private int status;
	private String statusText;
	private String[] headers;
	private byte[] content;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.maxit.adserver.message.v1.MessageV1#type()
	 */
	public Type type() {
		return Type.HttpRequest;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the statusText
	 */
	public String getStatusText() {
		return statusText;
	}

	/**
	 * @param statusText
	 *            the statusText to set
	 */
	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}

	/**
	 * @return the headers
	 */
	public String[] getHeaders() {
		return headers;
	}

	/**
	 * @param headers
	 *            the headers to set
	 */
	public void setHeaders(String[] headers) {
		this.headers = headers;
	}

	/**
	 * @return the content
	 */
	public byte[] getContent() {
		return content == null ? B0 : content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(byte[] content) {
		this.content = content;
	}

	public void setContent(String content) {
		byte[] c = B0;
		if(content != null) try { c = content.getBytes("UTF-8"); } catch (Exception e) {}
		this.content = c;
	}
}
