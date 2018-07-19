package com.jtd.web.po;

import com.jtd.web.constants.RoleType;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 用户身份信息，存入session 由于tomcat将session会序列化在本地硬盘上，所以使用Serializable接口
 * 
 * @author Thinkpad
 * 
 */
public class ActiveUser implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Long userId;// 用户id（主键）
	private String loginName;// 登陆名称
	private String userName;// 显示用户名称
    private String roleId; //用户角色
	private Partner partner; // 用户所属广告主的id
	private Partner favPartner; //用户关联的广告主
	private List<SysPermission> menus;// 用户关联菜单

    @DateTimeFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private Date currentTime = new Date();

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public List<SysPermission> getMenus() {
		return menus;
	}

	public void setMenus(List<SysPermission> menus) {
		this.menus = menus;
	}

	public Partner getPartner() {
		return partner;
	}

	public void setPartner(Partner partner) {
		this.partner = partner;
	}

	public Partner getFavPartner() {
		return favPartner;
	}

	public void setFavPartner(Partner favPartner) {
		this.favPartner = favPartner;
	}
	
	public boolean isAdminOrManager(){
		if(this.getRoleId() != null){
			return (this.getRoleId().equals(RoleType.ADMIN.getCode()+"") || this.getRoleId().equals(RoleType.MANAGER.getCode()+""));
		}
		return false ;
	}
	
	public boolean isOperateDirectorOrManager(){
		if(this.getRoleId() != null){
			return (this.getRoleId().equals(RoleType.OPERATE_DIRECTOR.getCode()+"") || this.getRoleId().equals(RoleType.OPERATE_MANAGER.getCode()+""));
		}
		return false ;
	}
	
	public boolean isOperateUser(){
		if(this.getRoleId() != null){
			return (this.getRoleId().equals(RoleType.OPERATE.getCode()+""));
		}
		return false ;
	}
	
	public boolean isProxy(){
		if(this.getRoleId() != null){
			return (this.getRoleId().equals(RoleType.PARTNER_PROXY.getCode()+""));
		}
		return false ;
	}
	
	public boolean isFinanceUser(){
		if(this.getRoleId() != null){
			return (this.getRoleId().equals(RoleType.FINANCE.getCode()+""));
		}
		return false ;
	}

    public Date getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Date currentTime) {
        this.currentTime = currentTime;
    }
}
