package com.jtd.web.adx.bes;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class AuthRequest implements Serializable{

	private static final long serialVersionUID = -2971824122189596426L;

	int dspId;

	String token;
	
	public int getDspId() {
		return dspId;
	}

	public void setDspId(int dspId) {
		this.dspId = dspId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	public String toString(){
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
		.append("dspId",dspId)
		.append("token",token)
        .toString();
	}

}
