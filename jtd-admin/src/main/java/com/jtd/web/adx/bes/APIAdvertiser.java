package com.jtd.web.adx.bes;

import java.io.Serializable;


public class APIAdvertiser  implements Serializable{
	
	private static final long serialVersionUID = 9131158642127107715L;

	private long advertiserId;

	private String advertiserName;
	
	private String advertiserLiteName;
	
	private String siteName;
	
	private String siteUrl;
	
	private String telephone;
	
	private String address;
	
	private String isWhiteUser;
	
	

	public String getAdvertiserLiteName() {
		return advertiserLiteName;
	}

	public void setAdvertiserLiteName(String advertiserLiteName) {
		this.advertiserLiteName = advertiserLiteName;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getSiteUrl() {
		return siteUrl;
	}

	public void setSiteUrl(String siteUrl) {
		this.siteUrl = siteUrl;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getIsWhiteUser() {
		return isWhiteUser;
	}

	public void setIsWhiteUser(String isWhiteUser) {
		this.isWhiteUser = isWhiteUser;
	}

	public long getAdvertiserId() {
		return advertiserId;
	}

	public void setAdvertiserId(long advertiserId) {
		this.advertiserId = advertiserId;
	}

	public String getAdvertiserName() {
		return advertiserName;
	}

	public void setAdvertiserName(String advertiserName) {
		this.advertiserName = advertiserName;
	}


}
