package com.jtd.web.po;

import com.jtd.commons.entity.BaseEntity;

/**
 * 
 * @作者 duber
 * @版本 V1.0
 * @创建日期 2016年10月24日
 * @描述 用户广告主关联实体类
 */
public class UserPartner extends BaseEntity {

	 /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private String userId;

	    private String partnerId;

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId == null ? null : userId.trim();
		}

		public String getPartnerId() {
			return partnerId;
		}

		public void setPartnerId(String partnerId) {
			this.partnerId = partnerId == null ? null : partnerId.trim();
		}
	    
}
