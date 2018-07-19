package com.jtd.web.adx.bes;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


	
	public class APICreativeAddRequest extends BaseRequest implements Serializable {

		private static final long serialVersionUID = 6019489411011064951L;
		
		List<APIAdvertiser> request;

		public List<APIAdvertiser> getRequest() {
			return request;
		}

		public void setRequest(List<APIAdvertiser> request) {
			this.request = request;
		}
		
		public String toString(){
			return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
			.append("authHeader",authHeader)
			.append("request",request)
	        .toString();
		}


}
