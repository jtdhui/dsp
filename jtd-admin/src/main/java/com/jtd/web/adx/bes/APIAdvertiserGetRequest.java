package com.jtd.web.adx.bes;

import java.io.Serializable;
import java.util.List;

public class APIAdvertiserGetRequest extends BaseRequest implements Serializable{
	
	private static final long serialVersionUID = -335503382035838570L;

	private List<Long> advertiserIds;

	public List<Long> getAdvertiserIds() {
		return advertiserIds;
	}

	public void setAdvertiserIds(List<Long> advertiserIds) {
		this.advertiserIds = advertiserIds;
	}

}
