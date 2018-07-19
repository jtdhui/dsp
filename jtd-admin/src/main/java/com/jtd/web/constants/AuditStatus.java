package com.jtd.web.constants;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置
 * @创建日期 2016年8月24日
 * @项目名称 dsp-common-entity
 * @描述 审核状态
 */
public enum AuditStatus {
	STATUS_NOTCOMMIT(0, "未提交"), STATUS_WAIT(1, "待审核"), STATUS_SUCCESS(2, "审核通过"), STATUS_FAIL(
			3, "审核拒绝"),STATUS_COMMIT_FAIL(4, "上传失败");

	private AuditStatus(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	private final int code;
	private final String desc;

	public int getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	// 根据code返回名称
	public static String getName(int code) {
		for (AuditStatus c : AuditStatus.values()) {
			if (c.getCode() == code) {
				return c.desc;
			}
		}
		return null;
	}

	// 根据code返回枚举
	public static AuditStatus getAuditStatus(int code) {
		for (AuditStatus c : AuditStatus.values()) {
			if (c.getCode() == code) {
				return c;
			}
		}
		return null;
	}

	public static AuditStatus getBaiduStatus(int state) {
		switch (state) {
		case 0:
			return STATUS_SUCCESS;
		case 1:
			return STATUS_WAIT;
		case 2:
			return STATUS_FAIL;
		case 3:
			return STATUS_WAIT;
		default:
			break;
		}
		return null;
	}
}
