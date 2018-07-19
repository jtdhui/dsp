package com.jtd.web.vo;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月25日
 * @项目名称 dsp-admin
 * @描述 <p>统计查询结果vo类</p>
 */
public class CountResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2371746067398380344L;
	private static final CountResult EMPTY = new CountResult();

	public CountResult() {
	}

	public CountResult(Object groupObject, Long bid, Long pv, Long uv, Long arrivePv, Long arriveUv, Long click, Long uClick, Long expend, Long cost) {
		super();
		this.groupObject = groupObject;
		this.bid = bid;
		this.pv = pv;
		this.uv = uv;
		this.arrivePv = arrivePv;
		this.arriveUv = arriveUv;
		this.click = click;
		this.uClick = uClick;
		this.expend = expend;
		this.cost = cost;
	}

	private Object groupObject;
	/**
	 * 用于展示的字段 gourpType为时间时，类别为Date groupType为其他是，类别是String
	 */
	private String showValue;

	/* 投放指标 */
	/**
	 * 投放花费<br>
	 * 单位是分
	 */
	private Long expend = 0L;
	/**
	 * 投放花费<br>
	 * 单位是元
	 */
	private Double showExpend = 0D;
	/**
	 * 展现量
	 */
	private Long pv = 0L;
	private Long uv = 0L;
	/**
	 * 每千次展现花费<br>
	 * 单位是分
	 */
	private Double cpm = 0d;
	/**
	 * 每千次展现花费<br>
	 * 单位是元
	 */
	private Double showCpm = 0d;

	/**
	 * 点击量
	 */
	private Long click = 0L;

	/**
	 * 每次点击花费<br>
	 * 单位是分
	 */
	private Double cpc = 0d;
	/**
	 * 每次点击花费<br>
	 * 单位是元
	 */
	private Double showCpc = 0d;
	/**
	 * 用户点击
	 */
	private Long uClick = 0L;
	/**
	 * 点击率
	 */
	private Double ctr = 0d;
	/**
	 * 点击率<br>
	 * 显示用的百分比 放大100倍
	 */
	private Double showCtr = 0d;
	/* 投放指标 */

	/* 成本指标 */
	/**
	 * 竞价次数
	 */
	private Long bid = 0L;
	/**
	 * 竞价成功率
	 */
	private Double ratioOfBid = 0d;
	/**
	 * 竞价成功率<br>
	 * 显示用的百分比 放大100倍
	 */
	private Double showRatioOfBid = 0d;
	/**
	 * 成本<br>
	 * 单位是分
	 */
	private Long cost = 0L;
	/**
	 * 成本<br>
	 * 单位是元
	 */
	private Double showCost = 0D;
	/**
	 * 成本cpm<br>
	 * 单位是分
	 */
	private Double costCpm = 0d;
	/**
	 * 成本cpm<br>
	 * 单位是元
	 */
	private Double showCostCpm = 0d;
	/**
	 * 成本cpc<br>
	 * 单位是分
	 */
	private Double costCpc = 0d;
	/**
	 * 成本cpc<br>
	 * 单位是元
	 */
	private Double showCostCpc = 0d;
	/**
	 * 毛利率
	 */
	private Double ratioOfMargin = 0d;
	/**
	 * 毛利率<br>
	 * 显示用的百分比 放大100倍
	 */
	private Double showRatioOfMargin = 0d;
	/**
	 * 毛利润<br>
	 * 单位是分
	 */
	private Long grossMargin = 0L;
	/**
	 * 毛利润<br>
	 * 单位是元
	 */
	private Double showGrossMargin = 0D;
	/* 成本指标 */

	/* 到达转化 */
	/*
	 * 到达pv
	 */
	private Long arrivePv = 0L;
	/**
	 * pv到达率
	 */
	private Double ratioOfArrivePv = 0d;
	/**
	 * pv到达率<br>
	 * 显示用的百分比 放大100倍
	 */
	private Double showRatioOfArrivePv = 0d;
	/**
	 * 到达uv
	 */
	private Long arriveUv = 0L;
	/**
	 * uv到达率
	 */
	private Double ratioOfArriveUv = 0d;
	/**
	 * uv到达率<br>
	 * 显示用的百分比 放大100倍
	 */
	private Double showRatioOfArriveUv = 0d;
	/* 到达转化 */

	/* 订单转化 */
	/**
	 * 实时订单数
	 */
	private Integer todayOrderCount = 0;
	/**
	 * 实时订单商品数
	 */
	private Integer todayGoodCount = 0;
	/**
	 * 实时订单金额<br>
	 * 单位 (分)
	 */
	private Integer todayAmount = 0;
	/**
	 * 实时订单金额<br>
	 * 单位(元)
	 */
	private Double showTodayAmount = 0D;
	/**
	 * 延时订单数
	 */
	private Integer delayedOrderCount = 0;
	/**
	 * 延时商品件数
	 */
	private Integer delayedGoodCount = 0;
	/**
	 * 延时订单金额<br>
	 * 单位 (分)
	 */
	private Integer delayedAmount = 0;
	/**
	 * 延时订单金额<br>
	 * 单位 (元)
	 */
	private Double showDelayedAmount = 0D;
	/**
	 * 总订单数
	 */
	private Integer totalOrderCount = 0;
	/**
	 * 总商品件数
	 */
	private Integer totalGoodCount = 0;
	/**
	 * 总订单金额<br>
	 * 单位(分)
	 */
	private Integer totalAmount = 0;
	/**
	 * 总订单金额<br>
	 * 单位(元)
	 */
	private Double showTotalAmount = 0D;
	/**
	 * 总订单购买人数
	 */
	private Integer totalOrderUsers = 0;
	/**
	 * 总订单转化率
	 */
	private Double ratioOfTotalOrder = 0D;
	/**
	 * 总订单转化率<br>
	 * 显示用的百分比 放大100倍
	 */
	private Double showRatioOfTotalOrder = 0D;
	/**
	 * 客单价<br>
	 * 单位(分)
	 */
	private Double avgAmount = 0D;
	/**
	 * 客单价<br>
	 * 单位(元)
	 */
	private Double showAvgAmount = 0D;
	/**
	 * 总订单ROI<br>
	 * 单位(分)
	 */
	private Double totalOrderRoi = 0D;
	/**
	 * 总订单ROI<br>
	 * 单位(元)
	 */
	private Double showTotalOrderRoi = 0D;
	/* 订单转化 */

	/* 加购转化 */
	/**
	 * 加购次数
	 */
	private Integer addCartCount = 0;
	/**
	 * 加购商品件数
	 */
	private Integer addCartGoodCount = 0;
	/**
	 * 加购商品金额<br>
	 * 单位(分)
	 */
	private Integer addCartAmount = 0;
	/**
	 * 加购商品金额<br>
	 * 单位(元)
	 */
	private Double showAddCartAmount = 0D;
	/**
	 * 加购人数
	 */
	private Integer addCartUsers = 0;
	/**
	 * 加购转化率
	 */
	private Double ratioOfAddCart = 0D;
	/**
	 * 加购转化率<br>
	 * 显示用的百分比 放大100倍
	 */
	private Double showRatioOfAddCart = 0D;
	/* 加购转化 */

	/* 注册转化 */
	/**
	 * 注册意向数
	 */
	private Integer intentionRegisterCount = 0;
	/**
	 * 注册数
	 */
	private Integer registerCount = 0;
	/**
	 * 注册转化率
	 */
	private Double ratioOfRegister = 0D;
	/**
	 * 注册转化率<br>
	 * 显示用的百分比 放大100倍
	 */
	private Double showRatioOfRegister = 0D;
	/**
	 * 平均注册成本(CPA)<br>
	 * 单位(分)
	 */
	private Double avgRegistExpend = 0D;
	/**
	 * 平均注册成本(CPA)<br>
	 * 单位(元)
	 */
	private Double showAvgRegistExpend = 0D;
	/* 注册转化 */

	/* 咨询转化 */
	/**
	 * 咨询次数
	 */
	private Integer consultCount = 0;
	/**
	 * 咨询人数
	 */
	private Integer consultUsers = 0;
	/**
	 * 咨询率
	 */
	private Double ratioOfConsult = 0D;
	/**
	 * 咨询率<br>
	 * 显示用的百分比 放大100倍
	 */
	private Double showRatioOfConsult = 0D;
	/**
	 * 平均咨询成本<br>
	 * 单位(分)
	 */
	private Double avgConsultExpend = 0D;
	/**
	 * 平均咨询成本<br>
	 * 单位(元)
	 */
	private Double showAvgConsultExpend = 0D;
	/* 咨询转化 */

	/* 登录转化 */
	/**
	 * 登录次数
	 */
	private Integer loginCount = 0;
	/**
	 * 登录人数
	 */
	private Integer loginUsers = 0;
	/* 登录转化 */

	/* 收藏转化 */
	/**
	 * 收藏次数
	 */
	private Integer collectCount = 0;
	/**
	 * 收藏人数
	 */
	private Integer collectUsers = 0;
	/**
	 * 收藏率
	 */
	private Double ratioOfCollect = 0D;
	/**
	 * 收藏率<br>
	 * 显示用的百分比 放大100倍
	 */
	private Double showRatioOfCollect = 0D;
	/* 收藏转化 */

	/* 分享转化 */
	/**
	 * 分享次数
	 */
	private Integer shareCount = 0;
	/**
	 * 分享人数
	 */
	private Integer shareUsers = 0;
	/**
	 * 分享率
	 */
	private Double ratioOfShare = 0D;
	/**
	 * 分享率<br>
	 * 显示用的百分比 放大100倍
	 */
	private Double showRatioOfShare = 0D;
	/* 分享转化 */

	/* 评论/发布转化 */
	/**
	 * 评论次数
	 */
	private Integer commentCount = 0;
	/**
	 * 评论人数
	 */
	private Integer commentUsers = 0;
	/**
	 * 评论率
	 */
	private Double ratioOfComment = 0D;
	/**
	 * 评论率<br>
	 * 显示用的百分比 放大100倍
	 */
	private Double showRatioOfComment = 0D;
	/* 评论/发布转化 */

	/**
	 * 额外的属性<br>
	 * imgurl
	 */
	private Map<String, String> extendParams = new HashMap<String, String>();;

	/**
	 * 计算数据
	 */
	public void compute() {

		if (click != 0) {
			cpc = (expend * 1D) / click;
			showCpc = cpc / 100D;
			costCpc = (cost * 1D) / click;
			showCostCpc = costCpc / 100D;
			ratioOfArrivePv = (arrivePv * 1D) / click;
			showRatioOfArrivePv = ratioOfArrivePv * 100;
		}
		if (pv != 0) {
			Double pm = pv / 1000D;
			cpm = expend / pm;
			showCpm = cpm / 100D;
			costCpm = cost / pm;
			showCostCpm = costCpm / 100D;
			ctr = (click * 1D) / pv;
			showCtr = ctr * 100;
		}

		if (uClick != 0) {
			ratioOfArriveUv = (arriveUv * 1D) / uClick;
			showRatioOfArriveUv = ratioOfArriveUv * 100;
		}
		if (bid != 0) {
			ratioOfBid = (pv * 1D) / bid;
			showRatioOfBid = ratioOfBid * 100;
		}
		grossMargin = expend - cost;
		showGrossMargin = grossMargin / 100D;
		if (expend != 0L) {
			showExpend = expend / 100D;
			showCost = cost / 100D;
			ratioOfMargin = (grossMargin * 1D) / expend;
			showRatioOfMargin = ratioOfMargin * 100;
		}
	}

	public void computeTotalTransfer() {
		// NumberFormat nf = new
		// TODO Auto-generated method stub
		DecimalFormat df = new DecimalFormat("######0.00");
		showTodayAmount = todayAmount / 100D;
		showDelayedAmount = delayedAmount / 100D;
		showTotalAmount = totalAmount / 100D;
		if (arriveUv != 0) {
			ratioOfTotalOrder = (totalOrderUsers * 1D) / arriveUv;
			showRatioOfTotalOrder = ratioOfTotalOrder * 100;

			ratioOfAddCart = (addCartUsers * 1D) / arriveUv;
			showRatioOfAddCart = ratioOfAddCart * 100;

			ratioOfRegister = (registerCount * 1D) / arriveUv;
			showRatioOfRegister = ratioOfRegister * 100;

			ratioOfConsult = (consultUsers * 1D) / arriveUv;
			showRatioOfConsult = ratioOfConsult * 100;

			ratioOfCollect = (collectCount * 1D) / arriveUv;
			showRatioOfCollect = ratioOfCollect * 100;

			ratioOfShare = (shareCount * 1D) / arriveUv;
			showRatioOfShare = ratioOfShare * 100;

			ratioOfComment = (commentCount * 1D) / arriveUv;
			showRatioOfComment = ratioOfComment * 100;

		}
		if (totalOrderUsers != 0) {
			avgAmount = (totalAmount * 1D) / totalOrderUsers;
			showAvgAmount = avgAmount / 100D;
			showAvgAmount = Double.parseDouble(df.format(showAvgAmount));
		}
		if (expend != 0) {
			totalOrderRoi = (totalAmount * 1D) / expend;
			showTotalOrderRoi = totalOrderRoi / 100D;
			showTotalOrderRoi = Double.parseDouble(df.format(showTotalOrderRoi));
		}
		showAddCartAmount = addCartAmount / 100D;

		if (registerCount != 0) {
			avgRegistExpend = (expend * 1D) / registerCount;
			showAvgRegistExpend = avgRegistExpend / 100D;
			showAvgRegistExpend = Double.parseDouble(df.format(showAvgRegistExpend));
		}

		if (consultCount != 0) {
			avgConsultExpend = (expend * 1D) / consultCount;
			showAvgConsultExpend = avgConsultExpend / 100D;
			showAvgConsultExpend = Double.parseDouble(df.format(showAvgConsultExpend));
		}
	}

	/**
	 * 统计分组类别
	 * 
	 * @author mwf
	 *
	 */
	public static enum CountGroupType {
		DATEDAY("D", "date"), DATEHOUR("H", "datehour"),

		CAMPGROUPDAY("D", "campgroupid"),

		CAMPAIGNTYPEDAY("CAMPD", "camptype"),

		CAMPAIGNDAY("CAMPD", "campid"),

		ADTYPEDAY("ADTPD", "adtype"),

		ADPLACEDAY("ADPD", "adpid"),

		TRANSTYPEDAY("TRANSTPD", "transtype"),

		DOMAINDAY("CHANNELD", "host"),

		CHANNELDAY("CHANNELD", "channelid"),

		CREATIVEDAY("CRED", "creativeid"),

		CREATIVESIZEDAY("SIZED", "sizeid"),

		CREATIVEGROUPDAY("CREGRPD", "cregrpid"),

		SEXDAY("CKD", "ckid"),

		AGEDAY("CKD", "ckid"),

		MARRIAGEDAY("CKD", "ckid"),

		CHILDDAY("CKD", "ckid"),

		PROFESSIONDAY("CKD", "ckid"),

		EDUCATIONDAY("CKD", "ckid"),

		EXAMDAY("CKD", "ckid"),

		HOUSEDAY("CKD", "ckid"),

		INTERESTDAY("CKD", "ckid"),

		CALLBACKDAY("CKD", "ckid"),

		CUSTOMDAY("CKD", "ckid"),

		CITYDAY("CITYD", "cityid");

		/**
		 * 数据库表后缀名
		 */
		private final String tableSuffixName;
		/**
		 * 分组字段
		 */
		private final String groupByStr;

		private CountGroupType(String tableSuffixName, String groupByStr) {
			this.tableSuffixName = tableSuffixName;
			this.groupByStr = groupByStr;
		}

		public String getTableSuffixName() {
			return tableSuffixName;
		}

		public String getGroupByStr() {
			return groupByStr;
		}
	}

	public String getShowValue() {
		return showValue;
	}

	public void setShowValue(String showValue) {
		this.showValue = showValue;
	}

	public Long getCost() {
		return cost;
	}

	public void setCost(Long cost) {
		this.cost = cost;
	}

	public Long getPv() {
		return pv;
	}

	public void setPv(Long pv) {
		this.pv = pv;
	}

	public Long getClick() {
		return click;
	}

	public void setClick(Long click) {
		this.click = click;
	}

	public Long getUv() {
		return uv;
	}

	public void setUv(Long uv) {
		this.uv = uv;
	}

	public Long getArrivePv() {
		return arrivePv;
	}

	public void setArrivePv(Long arrivePv) {
		this.arrivePv = arrivePv;
	}

	public Long getArriveUv() {
		return arriveUv;
	}

	public void setArriveUv(Long arriveUv) {
		this.arriveUv = arriveUv;
	}

	public Long getuClick() {
		return uClick;
	}

	public void setuClick(Long uClick) {
		this.uClick = uClick;
	}

	public Long getExpend() {
		return expend;
	}

	public void setExpend(Long expend) {
		this.expend = expend;
	}

	public void setGrossMargin(Long grossMargin) {
		this.grossMargin = grossMargin;
	}

	public Long getBid() {
		return bid;
	}

	public void setBid(Long bid) {
		this.bid = bid;
	}

	public Object getGroupObject() {
		return groupObject;
	}

	public void setGroupObject(Object groupObject) {
		this.groupObject = groupObject;
	}

	public void setCpm(Double cpm) {
		this.cpm = cpm;
	}

	public void setCpc(Double cpc) {
		this.cpc = cpc;
	}

	public void setCtr(Double ctr) {
		this.ctr = ctr;
	}

	public void setRatioOfMargin(Double ratioOfMargin) {
		this.ratioOfMargin = ratioOfMargin;
	}

	public void setRatioOfBid(Double ratioOfBid) {
		this.ratioOfBid = ratioOfBid;
	}

	public void setCostCpm(Double costCpm) {
		this.costCpm = costCpm;
	}

	public void setCostCpc(Double costCpc) {
		this.costCpc = costCpc;
	}

	public void setRatioOfArrivePv(Double ratioOfArrivePv) {
		this.ratioOfArrivePv = ratioOfArrivePv;
	}

	public void setRatioOfArriveUv(Double ratioOfArriveUv) {
		this.ratioOfArriveUv = ratioOfArriveUv;
	}

	public Map<String, String> getExtendParams() {
		return extendParams;
	}

	public void addExtendParams(String key, String value) {
		extendParams.put(key, value);
	}

	public void setExtendParams(Map<String, String> extendParams) {
		this.extendParams = extendParams;
	}

	public Long getGrossMargin() {
		return grossMargin;
	}

	public Double getCpm() {
		return cpm;
	}

	public Double getCpc() {
		return cpc;
	}

	public Double getCtr() {
		return ctr;
	}

	public Double getRatioOfMargin() {
		return ratioOfMargin;
	}

	public Double getRatioOfBid() {
		return ratioOfBid;
	}

	public Double getCostCpm() {
		return costCpm;
	}

	public Double getCostCpc() {
		return costCpc;
	}

	public Double getRatioOfArrivePv() {
		return ratioOfArrivePv;
	}

	public Double getRatioOfArriveUv() {
		return ratioOfArriveUv;
	}

	public Double getShowExpend() {
		return showExpend;
	}

	public void setShowExpend(Double showExpend) {
		this.showExpend = showExpend;
	}

	public Double getShowCost() {
		return showCost;
	}

	public void setShowCost(Double showCost) {
		this.showCost = showCost;
	}

	public Double getShowCpm() {
		return showCpm;
	}

	public void setShowCpm(Double showCpm) {
		this.showCpm = showCpm;
	}

	public Double getShowCpc() {
		return showCpc;
	}

	public void setShowCpc(Double showCpc) {
		this.showCpc = showCpc;
	}

	public Double getShowCostCpm() {
		return showCostCpm;
	}

	public void setShowCostCpm(Double showCostCpm) {
		this.showCostCpm = showCostCpm;
	}

	public Double getShowCostCpc() {
		return showCostCpc;
	}

	public void setShowCostCpc(Double showCostCpc) {
		this.showCostCpc = showCostCpc;
	}

	public Double getShowGrossMargin() {
		return showGrossMargin;
	}

	public void setShowGrossMargin(Double showGrossMargin) {
		this.showGrossMargin = showGrossMargin;
	}

	public Double getShowCtr() {
		return showCtr;
	}

	public void setShowCtr(Double showCtr) {
		this.showCtr = showCtr;
	}

	public Double getShowRatioOfMargin() {
		return showRatioOfMargin;
	}

	public void setShowRatioOfMargin(Double showRatioOfMargin) {
		this.showRatioOfMargin = showRatioOfMargin;
	}

	public Double getShowRatioOfBid() {
		return showRatioOfBid;
	}

	public void setShowRatioOfBid(Double showRatioOfBid) {
		this.showRatioOfBid = showRatioOfBid;
	}

	public Double getShowRatioOfArrivePv() {
		return showRatioOfArrivePv;
	}

	public void setShowRatioOfArrivePv(Double showRatioOfArrivePv) {
		this.showRatioOfArrivePv = showRatioOfArrivePv;
	}

	public Double getShowRatioOfArriveUv() {
		return showRatioOfArriveUv;
	}

	public void setShowRatioOfArriveUv(Double showRatioOfArriveUv) {
		this.showRatioOfArriveUv = showRatioOfArriveUv;
	}

	public void filterColumn(Set<Long> rights) {
		if (!rights.contains(117L)) {
			// 没有成本查看权限
			setBid(null);
			setRatioOfBid(null);
			setShowRatioOfBid(null);
			setCost(null);
			setShowCost(null);
			setCostCpm(null);
			setShowCostCpm(null);
			setCostCpc(null);
			setShowCostCpc(null);
			setRatioOfMargin(null);
			setShowRatioOfMargin(null);
			setGrossMargin(null);
			setShowGrossMargin(null);
		}
		if (!rights.contains(119L)) {
			// 没有转化查看权限
			setArrivePv(null);
			setRatioOfArrivePv(null);
			setArriveUv(null);
			setRatioOfArriveUv(null);
		}
	}

	public static CountResult empty() {
		return EMPTY;
	}

	public Integer getTodayOrderCount() {
		return todayOrderCount;
	}

	public void setTodayOrderCount(Integer todayOrderCount) {
		this.todayOrderCount = todayOrderCount;
	}

	public Integer getTodayGoodCount() {
		return todayGoodCount;
	}

	public void setTodayGoodCount(Integer todayGoodCount) {
		this.todayGoodCount = todayGoodCount;
	}

	public Integer getTodayAmount() {
		return todayAmount;
	}

	public void setTodayAmount(Integer todayAmount) {
		this.todayAmount = todayAmount;
	}

	public Double getShowTodayAmount() {
		return showTodayAmount;
	}

	public void setShowTodayAmount(Double showTodayAmount) {
		this.showTodayAmount = showTodayAmount;
	}

	public Integer getDelayedOrderCount() {
		return delayedOrderCount;
	}

	public void setDelayedOrderCount(Integer delayedOrderCount) {
		this.delayedOrderCount = delayedOrderCount;
	}

	public Integer getDelayedGoodCount() {
		return delayedGoodCount;
	}

	public void setDelayedGoodCount(Integer delayedGoodCount) {
		this.delayedGoodCount = delayedGoodCount;
	}

	public Integer getDelayedAmount() {
		return delayedAmount;
	}

	public void setDelayedAmount(Integer delayedAmount) {
		this.delayedAmount = delayedAmount;
	}

	public Double getShowDelayedAmount() {
		return showDelayedAmount;
	}

	public void setShowDelayedAmount(Double showDelayedAmount) {
		this.showDelayedAmount = showDelayedAmount;
	}

	public Integer getTotalOrderCount() {
		return totalOrderCount;
	}

	public void setTotalOrderCount(Integer totalOrderCount) {
		this.totalOrderCount = totalOrderCount;
	}

	public Integer getTotalGoodCount() {
		return totalGoodCount;
	}

	public void setTotalGoodCount(Integer totalGoodCount) {
		this.totalGoodCount = totalGoodCount;
	}

	public Integer getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Integer totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getShowTotalAmount() {
		return showTotalAmount;
	}

	public void setShowTotalAmount(Double showTotalAmount) {
		this.showTotalAmount = showTotalAmount;
	}

	public Integer getTotalOrderUsers() {
		return totalOrderUsers;
	}

	public void setTotalOrderUsers(Integer totalOrderUsers) {
		this.totalOrderUsers = totalOrderUsers;
	}

	public Double getRatioOfTotalOrder() {
		return ratioOfTotalOrder;
	}

	public void setRatioOfTotalOrder(Double ratioOfTotalOrder) {
		this.ratioOfTotalOrder = ratioOfTotalOrder;
	}

	public Double getShowRatioOfTotalOrder() {
		return showRatioOfTotalOrder;
	}

	public void setShowRatioOfTotalOrder(Double showRatioOfTotalOrder) {
		this.showRatioOfTotalOrder = showRatioOfTotalOrder;
	}

	public Double getAvgAmount() {
		return avgAmount;
	}

	public void setAvgAmount(Double avgAmount) {
		this.avgAmount = avgAmount;
	}

	public Double getShowAvgAmount() {
		return showAvgAmount;
	}

	public void setShowAvgAmount(Double showAvgAmount) {
		this.showAvgAmount = showAvgAmount;
	}

	public Double getTotalOrderRoi() {
		return totalOrderRoi;
	}

	public void setTotalOrderRoi(Double totalOrderRoi) {
		this.totalOrderRoi = totalOrderRoi;
	}

	public Double getShowTotalOrderRoi() {
		return showTotalOrderRoi;
	}

	public void setShowTotalOrderRoi(Double showTotalOrderRoi) {
		this.showTotalOrderRoi = showTotalOrderRoi;
	}

	public Integer getAddCartCount() {
		return addCartCount;
	}

	public void setAddCartCount(Integer addCartCount) {
		this.addCartCount = addCartCount;
	}

	public Integer getAddCartGoodCount() {
		return addCartGoodCount;
	}

	public void setAddCartGoodCount(Integer addCartGoodCount) {
		this.addCartGoodCount = addCartGoodCount;
	}

	public Integer getAddCartAmount() {
		return addCartAmount;
	}

	public void setAddCartAmount(Integer addCartAmount) {
		this.addCartAmount = addCartAmount;
	}

	public Double getShowAddCartAmount() {
		return showAddCartAmount;
	}

	public void setShowAddCartAmount(Double showAddCartAmount) {
		this.showAddCartAmount = showAddCartAmount;
	}

	public Integer getAddCartUsers() {
		return addCartUsers;
	}

	public void setAddCartUsers(Integer addCartUsers) {
		this.addCartUsers = addCartUsers;
	}

	public Double getRatioOfAddCart() {
		return ratioOfAddCart;
	}

	public void setRatioOfAddCart(Double ratioOfAddCart) {
		this.ratioOfAddCart = ratioOfAddCart;
	}

	public Double getShowRatioOfAddCart() {
		return showRatioOfAddCart;
	}

	public void setShowRatioOfAddCart(Double showRatioOfAddCart) {
		this.showRatioOfAddCart = showRatioOfAddCart;
	}

	public Integer getIntentionRegisterCount() {
		return intentionRegisterCount;
	}

	public void setIntentionRegisterCount(Integer intentionRegisterCount) {
		this.intentionRegisterCount = intentionRegisterCount;
	}

	public Integer getRegisterCount() {
		return registerCount;
	}

	public void setRegisterCount(Integer registerCount) {
		this.registerCount = registerCount;
	}

	public Double getRatioOfRegister() {
		return ratioOfRegister;
	}

	public void setRatioOfRegister(Double ratioOfRegister) {
		this.ratioOfRegister = ratioOfRegister;
	}

	public Double getShowRatioOfRegister() {
		return showRatioOfRegister;
	}

	public void setShowRatioOfRegister(Double showRatioOfRegister) {
		this.showRatioOfRegister = showRatioOfRegister;
	}

	public Double getAvgRegistExpend() {
		return avgRegistExpend;
	}

	public void setAvgRegistExpend(Double avgRegistExpend) {
		this.avgRegistExpend = avgRegistExpend;
	}

	public Double getShowAvgRegistExpend() {
		return showAvgRegistExpend;
	}

	public void setShowAvgRegistExpend(Double showAvgRegistExpend) {
		this.showAvgRegistExpend = showAvgRegistExpend;
	}

	public Integer getConsultCount() {
		return consultCount;
	}

	public void setConsultCount(Integer consultCount) {
		this.consultCount = consultCount;
	}

	public Integer getConsultUsers() {
		return consultUsers;
	}

	public void setConsultUsers(Integer consultUsers) {
		this.consultUsers = consultUsers;
	}

	public Double getRatioOfConsult() {
		return ratioOfConsult;
	}

	public void setRatioOfConsult(Double ratioOfConsult) {
		this.ratioOfConsult = ratioOfConsult;
	}

	public Double getShowRatioOfConsult() {
		return showRatioOfConsult;
	}

	public void setShowRatioOfConsult(Double showRatioOfConsult) {
		this.showRatioOfConsult = showRatioOfConsult;
	}

	public Double getAvgConsultExpend() {
		return avgConsultExpend;
	}

	public void setAvgConsultExpend(Double avgConsultExpend) {
		this.avgConsultExpend = avgConsultExpend;
	}

	public Double getShowAvgConsultExpend() {
		return showAvgConsultExpend;
	}

	public void setShowAvgConsultExpend(Double showAvgConsultExpend) {
		this.showAvgConsultExpend = showAvgConsultExpend;
	}

	public Integer getLoginCount() {
		return loginCount;
	}

	public void setLoginCount(Integer loginCount) {
		this.loginCount = loginCount;
	}

	public Integer getLoginUsers() {
		return loginUsers;
	}

	public void setLoginUsers(Integer loginUsers) {
		this.loginUsers = loginUsers;
	}

	public Integer getCollectCount() {
		return collectCount;
	}

	public void setCollectCount(Integer collectCount) {
		this.collectCount = collectCount;
	}

	public Integer getCollectUsers() {
		return collectUsers;
	}

	public void setCollectUsers(Integer collectUsers) {
		this.collectUsers = collectUsers;
	}

	public Double getRatioOfCollect() {
		return ratioOfCollect;
	}

	public void setRatioOfCollect(Double ratioOfCollect) {
		this.ratioOfCollect = ratioOfCollect;
	}

	public Double getShowRatioOfCollect() {
		return showRatioOfCollect;
	}

	public void setShowRatioOfCollect(Double showRatioOfCollect) {
		this.showRatioOfCollect = showRatioOfCollect;
	}

	public Integer getShareCount() {
		return shareCount;
	}

	public void setShareCount(Integer shareCount) {
		this.shareCount = shareCount;
	}

	public Integer getShareUsers() {
		return shareUsers;
	}

	public void setShareUsers(Integer shareUsers) {
		this.shareUsers = shareUsers;
	}

	public Double getRatioOfShare() {
		return ratioOfShare;
	}

	public void setRatioOfShare(Double ratioOfShare) {
		this.ratioOfShare = ratioOfShare;
	}

	public Double getShowRatioOfShare() {
		return showRatioOfShare;
	}

	public void setShowRatioOfShare(Double showRatioOfShare) {
		this.showRatioOfShare = showRatioOfShare;
	}

	public Integer getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(Integer commentCount) {
		this.commentCount = commentCount;
	}

	public Integer getCommentUsers() {
		return commentUsers;
	}

	public void setCommentUsers(Integer commentUsers) {
		this.commentUsers = commentUsers;
	}

	public Double getRatioOfComment() {
		return ratioOfComment;
	}

	public void setRatioOfComment(Double ratioOfComment) {
		this.ratioOfComment = ratioOfComment;
	}

	public Double getShowRatioOfComment() {
		return showRatioOfComment;
	}

	public void setShowRatioOfComment(Double showRatioOfComment) {
		this.showRatioOfComment = showRatioOfComment;
	}

}
