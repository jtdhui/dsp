package com.jtd.engine.message.v1;

import com.jtd.engine.adserver.message.v1.MessageV1;

/**
 * 新汇达通广告请求request
 * @author zl
 * 2017-09-26
 */
public class XHDTBidRequest  extends MessageV1{

	private static final long serialVersionUID = -6684420216582287423L;
	
	@Override
	public Type type() {
		return Type.XHDTBidRequest;
	}
	/** 广告类型（1：原生广告，2：banner，3：插屏） */
	private String adtype;
	/** 按说ID */
	private String androidid;
	/** APP key */
	private String addkey;
	/**  */
	private String imei;
	/**  */
	private String imsi;
	/** MAC地址 */
	private String mac;
	/** 网络类型（wifi，4g，3g，2g，other） */
	private String network;
	/** 运营商（00移动，01联通，03电信） */
	private String operator;
	/** 分辨率 1080*1920 */
	private String resolution;
	/** 屏幕密度 */
	private String screen;
	/** 当前版本 */
	private String version;
	/** ICCID */
	private String iccid;
	/** 系统版本 */
	private String sysver;
	/** 设备IP */
	private String ip;
	/** 屏幕方向（1：竖屏，2：反向竖屏，3：横屏，4：反向横屏） */
	private String orientation;
	/** 维度 */
	private String lat;
	/** 经度 */
	private String lon;
	/** 用户年龄 */
	private String age;
	/** 用户性别（m:男性，f：女性） */
	private String gender;

	public String getAdtype() {
		return adtype;
	}
	public void setAdtype(String adtype) {
		this.adtype = adtype;
	}
	public String getAndroidid() {
		return androidid;
	}
	public void setAndroidid(String androidid) {
		this.androidid = androidid;
	}
	public String getAddkey() {
		return addkey;
	}
	public void setAddkey(String addkey) {
		this.addkey = addkey;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getImsi() {
		return imsi;
	}
	public void setImsi(String imsi) {
		this.imsi = imsi;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getNetwork() {
		return network;
	}
	public void setNetwork(String network) {
		this.network = network;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getResolution() {
		return resolution;
	}
	public void setResolution(String resolution) {
		this.resolution = resolution;
	}
	public String getScreen() {
		return screen;
	}
	public void setScreen(String screen) {
		this.screen = screen;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getIccid() {
		return iccid;
	}
	public void setIccid(String iccid) {
		this.iccid = iccid;
	}
	public String getSysver() {
		return sysver;
	}
	public void setSysver(String sysver) {
		this.sysver = sysver;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getOrientation() {
		return orientation;
	}
	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLon() {
		return lon;
	}
	public void setLon(String lon) {
		this.lon = lon;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
}
