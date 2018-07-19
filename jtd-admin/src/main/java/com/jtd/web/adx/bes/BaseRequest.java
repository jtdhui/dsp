package com.jtd.web.adx.bes;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class BaseRequest implements Serializable {

	private static final long serialVersionUID = -2971824122189596426L;

	protected AuthRequest authHeader;

	public AuthRequest getAuthHeader() {
		return authHeader;
	}

	public void setAuthHeader(AuthRequest authHeader) {
		this.authHeader = authHeader;
	}

	public String toString(){
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
		.append("authHeader",authHeader)
        .toString();
	}

}