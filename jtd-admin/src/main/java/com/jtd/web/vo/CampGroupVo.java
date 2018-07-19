package com.jtd.web.vo;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月25日
 * @项目名称 dsp-admin
 * @描述 <p></p>
 */
public class CampGroupVo {

	/**
	 * 推广组id
	 */
	private Long id;
	/**
	 * 推广组名称
	 */
	private String groupName;
	/**
	 * 在线活动数
	 */
	private Integer onlineCampainCount;
	/**
	 * 总活动数
	 */
	private Integer allCampaignCount;
	/**
	 * 每日预算目标
	 */
	private Double dailyBudgetGoal;
	/**
	 * 当日花费
	 */
	private Double todayExpend;
	/**
	 * 当日预算完成率
	 */
	private Double todayExpendCompletionRate;
	/**
	 * 总预算目标
	 */
	private Double totalBudgetGoal;
	/**
	 * 总花费
	 */
	private Double totalExpend;
	/**
	 * 总花费完成率
	 */
	private Double totalExpendCompletionRate;
	/**
	 * 日展现目标
	 */
	private Long dailyPvGoal;
	/**
	 * 当日展现量
	 */
	private Long todayPv;
	/**
	 * 日展现完成率
	 */
	private Double todayPvCompletionRate;
	/**
	 * 总展现目标
	 */
	private Long totalPvGoal;
	/**
	 * 总展现量
	 */
	private Long totalPv;
	/**
	 * 总展现完成率
	 */
	private Double totalPvCompletionRate;
	/**
	 * 每日点击量目标
	 */
	private Long dailyClickGoal;
	/**
	 * 当日点击量
	 */
	private Long todayClick;
	/**
	 * 当日点击量完成率
	 */
	private Double todayClickCompletionRate;
	/**
	 * 总点击量目标
	 */
	private Long totalClickGoal;
	/**
	 * 总点击量
	 */
	private Long totalClick;
	/**
	 * 总点击量完成率
	 */
	private Double totalClickCompletionRate;
	/**
	 * 当日点击率
	 */
	private Double todayCtr;
	/**
	 * 总点击率
	 */
	private Double totalCtr;
	/**
	 * 当日毛利率
	 */
	private Double todayRatioOfMargin;
	/**
	 * 总毛利率
	 */
	private Double totalRatioOfMargin;
	/**
	 * 推广组描述
	 */
	private String description;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Integer getOnlineCampainCount() {
		return onlineCampainCount;
	}

	public void setOnlineCampainCount(Integer onlineCampainCount) {
		this.onlineCampainCount = onlineCampainCount;
	}

	public Integer getAllCampaignCount() {
		return allCampaignCount;
	}

	public void setAllCampaignCount(Integer allCampaignCount) {
		this.allCampaignCount = allCampaignCount;
	}

	public Double getDailyBudgetGoal() {
		return dailyBudgetGoal;
	}

	public void setDailyBudgetGoal(Double dailyBudgetGoal) {
		this.dailyBudgetGoal = dailyBudgetGoal;
	}

	public Double getTodayExpendCompletionRate() {
		return todayExpendCompletionRate;
	}

	public void setTodayExpendCompletionRate(Double todayExpendCompletionRate) {
		this.todayExpendCompletionRate = todayExpendCompletionRate;
	}

	public Double getTotalBudgetGoal() {
		return totalBudgetGoal;
	}

	public void setTotalBudgetGoal(Double totalBudgetGoal) {
		this.totalBudgetGoal = totalBudgetGoal;
	}

	public Double getTotalExpendCompletionRate() {
		return totalExpendCompletionRate;
	}

	public void setTotalExpendCompletionRate(Double totalExpendCompletionRate) {
		this.totalExpendCompletionRate = totalExpendCompletionRate;
	}

	public Long getDailyPvGoal() {
		return dailyPvGoal;
	}

	public void setDailyPvGoal(Long dailyPvGoal) {
		this.dailyPvGoal = dailyPvGoal;
	}

	public Double getTodayPvCompletionRate() {
		return todayPvCompletionRate;
	}

	public void setTodayPvCompletionRate(Double todayPvCompletionRate) {
		this.todayPvCompletionRate = todayPvCompletionRate;
	}

	public Long getTotalPvGoal() {
		return totalPvGoal;
	}

	public void setTotalPvGoal(Long totalPvGoal) {
		this.totalPvGoal = totalPvGoal;
	}

	public Double getTotalPvCompletionRate() {
		return totalPvCompletionRate;
	}

	public void setTotalPvCompletionRate(Double totalPvCompletionRate) {
		this.totalPvCompletionRate = totalPvCompletionRate;
	}

	public Long getDailyClickGoal() {
		return dailyClickGoal;
	}

	public void setDailyClickGoal(Long dailyClickGoal) {
		this.dailyClickGoal = dailyClickGoal;
	}

	public Double getTodayClickCompletionRate() {
		return todayClickCompletionRate;
	}

	public void setTodayClickCompletionRate(Double todayClickCompletionRate) {
		this.todayClickCompletionRate = todayClickCompletionRate;
	}

	public Long getTotalClickGoal() {
		return totalClickGoal;
	}

	public void setTotalClickGoal(Long totalClickGoal) {
		this.totalClickGoal = totalClickGoal;
	}

	public Double getTotalClickCompletionRate() {
		return totalClickCompletionRate;
	}

	public void setTotalClickCompletionRate(Double totalClickCompletionRate) {
		this.totalClickCompletionRate = totalClickCompletionRate;
	}

	public Double getTodayCtr() {
		return todayCtr;
	}

	public void setTodayCtr(Double todayCtr) {
		this.todayCtr = todayCtr;
	}

	public Double getTotalCtr() {
		return totalCtr;
	}

	public void setTotalCtr(Double totalCtr) {
		this.totalCtr = totalCtr;
	}

	public Double getTodayRatioOfMargin() {
		return todayRatioOfMargin;
	}

	public void setTodayRatioOfMargin(Double todayRatioOfMargin) {
		this.todayRatioOfMargin = todayRatioOfMargin;
	}

	public Double getTotalRatioOfMargin() {
		return totalRatioOfMargin;
	}

	public void setTotalRatioOfMargin(Double totalRatioOfMargin) {
		this.totalRatioOfMargin = totalRatioOfMargin;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getTodayExpend() {
		return todayExpend;
	}

	public void setTodayExpend(Double todayExpend) {
		this.todayExpend = todayExpend;
	}

	public Double getTotalExpend() {
		return totalExpend;
	}

	public void setTotalExpend(Double totalExpend) {
		this.totalExpend = totalExpend;
	}

	public Long getTodayPv() {
		return todayPv;
	}

	public void setTodayPv(Long todayPv) {
		this.todayPv = todayPv;
	}

	public Long getTotalPv() {
		return totalPv;
	}

	public void setTotalPv(Long totalPv) {
		this.totalPv = totalPv;
	}

	public Long getTodayClick() {
		return todayClick;
	}

	public void setTodayClick(Long todayClick) {
		this.todayClick = todayClick;
	}

	public Long getTotalClick() {
		return totalClick;
	}

	public void setTotalClick(Long totalClick) {
		this.totalClick = totalClick;
	}
}
