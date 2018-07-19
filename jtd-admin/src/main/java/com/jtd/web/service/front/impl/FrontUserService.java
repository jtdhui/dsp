package com.jtd.web.service.front.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtd.commons.PropertyConfig;
import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.utils.UserPwd;
import com.jtd.web.dao.ISysUserDao;
import com.jtd.web.po.SysUser;
import com.jtd.web.service.front.IFrontUserService;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置
 * @创建日期 2016年9月29日
 * @项目名称 dsp-admin
 * @描述
 *     <p>
 *     </p>
 */
@Service
public class FrontUserService implements IFrontUserService {

	@Autowired
	private PropertyConfig propertyConfig;

	@Autowired
	private ISysUserDao sysUserDao;

	public SysUser findUserInfo(SysUser user) {
		return null;
	}

	public List<SysUser> findUserAll() {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		List<SysUser> retList = sysUserDao.listAll();
		return retList;
	}

	@Override
	public SysUser getById(long id) {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		SysUser user = sysUserDao.getById(id);
		return user;
	}

	@Override
	public List<SysUser> listBy(SysUser user) {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		return sysUserDao.listBy(user);
	}
	
	

	@Override
	public SysUser findUserByLoginName(String loginName) {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		SysUser tmp=new SysUser();
		tmp.setLoginName(loginName);
		List<SysUser> listUser=listBy(tmp);
		if (listUser!=null&&listUser.size()>0) {
			return listUser.get(0);
		}
		return null;
	}

	@Override
	public long insert(SysUser user) {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		// 读取配置文件中散列的算法和次数
		String hashAlgorithmName = propertyConfig.getProperty("shiro.hashAlgorithmName");
		int hashIterations = Integer.parseInt(propertyConfig.getProperty("shiro.hashIterations").trim());
		// 获取散列时的干扰字符串
		String salt = UserPwd.salt(5);
		// 生成用户密码
		String pwd = UserPwd.password(user.getPwd(), salt, hashAlgorithmName, hashIterations);
		user.setSalt(salt);
		user.setPwd(pwd);
		return sysUserDao.insert(user);
	}

	@Override
	public long insertBatch() {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		List<SysUser> list = new ArrayList<SysUser>();

		// 读取配置文件中散列的算法和次数
		String hashAlgorithmName = propertyConfig.getProperty("shiro.hashAlgorithmName");
		int hashIterations = Integer.parseInt(propertyConfig.getProperty("shiro.hashIterations").trim());
		// 获取散列时的干扰字符串
		String salt = UserPwd.salt(5);
		// 生成用户密码
		String pwd = UserPwd.password("dddddd", salt, hashAlgorithmName, hashIterations);
		for (int i = 0; i < 10; i++) {
			SysUser user = new SysUser();
			user.setPartnerId(new Long(1));
			user.setUserName("aa" + i);
			user.setLoginName("batch" + i);
			user.setPwd(pwd);
			user.setSalt(salt);
			user.setEmail("aaa" + i + "@bbb.com");
			list.add(user);
		}
		return sysUserDao.insert(list);
	}

	@Override
	public long update(SysUser user) {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		return sysUserDao.update(user);
	}

	@Override
	public long update(List<SysUser> list) {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		return sysUserDao.update(list);
	}

	@Override
	public long deleteById(long id) {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		return sysUserDao.deleteById(id);
	}

	@Override
	public long delete(List<SysUser> list) {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		return sysUserDao.delete(list);
	}

}
