package com.jtd.web.jms;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-common-entity
 * @描述 <p>修改推广组的设置字段</p>
 */
public class ChangeCampGrpMsg extends Message {

	private static final long serialVersionUID = 414964624155231252L;

	private long groupid;

	/**
	 * 日预算
	 */
	private Long dailyBudgetGoal;
	/**
	 * 总预算
	 */
	private Long totalBudgetGoal;
	/**
	 * 日展现量目标
	 */
	private Long dailyPvGoal;
	/**
	 * 日点击量目标
	 */
	private Long dailyClickGoal;
	/**
	 * 总展现量目标
	 */
	private Long totalPvGoal;
	/**
	 * 总点击量目标
	 */
	private Long totalClickGoal;

	/**
	 * @return the groupid
	 */
	public long getGroupid() {
		return groupid;
	}

	/**
	 * @param groupid
	 *            the groupid to set
	 */
	public void setGroupid(long groupid) {
		this.groupid = groupid;
	}

	/**
	 * @return the dailyBudgetGoal
	 */
	public Long getDailyBudgetGoal() {
		return dailyBudgetGoal;
	}

	/**
	 * @param dailyBudgetGoal
	 *            the dailyBudgetGoal to set
	 */
	public void setDailyBudgetGoal(Long dailyBudgetGoal) {
		this.dailyBudgetGoal = dailyBudgetGoal;
	}

	/**
	 * @return the totalBudgetGoal
	 */
	public Long getTotalBudgetGoal() {
		return totalBudgetGoal;
	}

	/**
	 * @param totalBudgetGoal
	 *            the totalBudgetGoal to set
	 */
	public void setTotalBudgetGoal(Long totalBudgetGoal) {
		this.totalBudgetGoal = totalBudgetGoal;
	}

	/**
	 * @return the dailyPvGoal
	 */
	public Long getDailyPvGoal() {
		return dailyPvGoal;
	}

	/**
	 * @param dailyPvGoal
	 *            the dailyPvGoal to set
	 */
	public void setDailyPvGoal(Long dailyPvGoal) {
		this.dailyPvGoal = dailyPvGoal;
	}

	/**
	 * @return the dailyClickGoal
	 */
	public Long getDailyClickGoal() {
		return dailyClickGoal;
	}

	/**
	 * @param dailyClickGoal
	 *            the dailyClickGoal to set
	 */
	public void setDailyClickGoal(Long dailyClickGoal) {
		this.dailyClickGoal = dailyClickGoal;
	}

	/**
	 * @return the totalPvGoal
	 */
	public Long getTotalPvGoal() {
		return totalPvGoal;
	}

	/**
	 * @param totalPvGoal
	 *            the totalPvGoal to set
	 */
	public void setTotalPvGoal(Long totalPvGoal) {
		this.totalPvGoal = totalPvGoal;
	}

	/**
	 * @return the totalClickGoal
	 */
	public Long getTotalClickGoal() {
		return totalClickGoal;
	}

	/**
	 * @param totalClickGoal
	 *            the totalClickGoal to set
	 */
	public void setTotalClickGoal(Long totalClickGoal) {
		this.totalClickGoal = totalClickGoal;
	}

	@Override
	public MsgType getType() {
		return MsgType.CHANGE_CAMP_GROUP;
	}
}
