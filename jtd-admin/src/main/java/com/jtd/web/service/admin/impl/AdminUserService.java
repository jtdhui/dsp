package com.jtd.web.service.admin.impl;

import com.jtd.commons.PropertyConfig;
import com.jtd.commons.dao.BaseDao;
import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.commons.page.Pagination;
import com.jtd.utils.UserPwd;
import com.jtd.web.dao.ISysUserDao;
import com.jtd.web.dao.IUserPartnerDao;
import com.jtd.web.po.ActiveUser;
import com.jtd.web.po.Partner;
import com.jtd.web.po.SysUser;
import com.jtd.web.po.UserPartner;
import com.jtd.web.service.admin.IAdminPartnerService;
import com.jtd.web.service.admin.IAdminUserService;
import com.jtd.web.service.impl.BaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

import java.util.*;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置
 * @创建日期 2016年9月29日
 * @项目名称 dsp-admin
 * @描述 用户管理
 */
@Service
public class AdminUserService extends BaseService<SysUser> implements
		IAdminUserService {

	@Autowired
	private PropertyConfig propertyConfig;

	@Autowired
	private ISysUserDao sysUserDao;

	@Autowired
	private IUserPartnerDao userPartnerDao;

    @Autowired
    private IAdminPartnerService adminPartnerService;

	@Override
	protected BaseDao<SysUser> getDao() {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		return sysUserDao;
	}

	@Override
	public SysUser findUserByLoginName(String loginName) {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		SysUser tmp = new SysUser();
		tmp.setLoginName(loginName);
		List<SysUser> listUser = listBy(tmp);
		if (listUser != null && listUser.size() > 0) {
			return listUser.get(0);
		}
		return null;
	}

	@Override
	public long insert(SysUser user) {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		// 读取配置文件中散列的算法和次数
		String hashAlgorithmName = propertyConfig
				.getProperty("shiro.hashAlgorithmName");
		int hashIterations = Integer.parseInt(propertyConfig.getProperty(
				"shiro.hashIterations").trim());
		// 获取散列时的干扰字符串
		String salt = UserPwd.salt(5);
		// 生成用户密码
		String pwd = UserPwd.password(user.getPwd(), salt, hashAlgorithmName,
				hashIterations);
		user.setSalt(salt);
		user.setPwd(pwd);
		return sysUserDao.insert(user);
	}
	
	@Override
	public long changePassword(long userId, String passwordOrgin) {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		// 读取配置文件中散列的算法和次数
		String hashAlgorithmName = propertyConfig.getProperty("shiro.hashAlgorithmName");
		int hashIterations = Integer.parseInt(propertyConfig.getProperty("shiro.hashIterations").trim());
		// 获取散列时的干扰字符串
		String salt = UserPwd.salt(5);
		// 生成用户密码
		String pwd = UserPwd.password(passwordOrgin, salt, hashAlgorithmName, hashIterations);

		SysUser user = new SysUser();
		user.setId(userId);
		user.setSalt(salt);
		user.setPwd(pwd);
		
		return sysUserDao.update(user);

	}

	@Override
	public void findUserList(Model model,HttpServletRequest request, Integer pageNo, Integer pageSize, ActiveUser activeUser) {
		
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		
		HashMap<String, Object> paraMap = new HashMap<String, Object>();
		if (StringUtils.isEmpty(request.getParameter("loginName")) == false) {
			paraMap.put("loginName", request.getParameter("loginName"));
		}
		if (StringUtils.isEmpty(request.getParameter("userName")) == false) {
			paraMap.put("userName", request.getParameter("userName"));
		}
		if (StringUtils.isEmpty(request.getParameter("partnerName")) == false) {
			paraMap.put("partnerName", request.getParameter("partnerName"));
		}
		if (StringUtils.isEmpty(request.getParameter("status")) == false) {
			paraMap.put("status", request.getParameter("status"));
		}
		model.addAttribute("queryMap", paraMap);


        //根据当前登录用户ID查询该用户关联的广告主
        String partnerIds = "";
        List<Partner> partnerList = adminPartnerService.findPartnerListByLoginUser(activeUser);

        if (partnerList.size()>0) {

            for(Partner partner:partnerList){
                partnerIds += partner.getId().toString()+",";
            }
            partnerIds = partnerIds.substring(0,partnerIds.length()-1);

            String child_partnerIds = adminPartnerService.findChildrenIdsById(activeUser.getPartner().getId());
            String arr_partnerId[] = child_partnerIds.split(",");
            Collection collection = Arrays.asList(arr_partnerId);

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("partnerIds", partnerIds);
            //根据广告主ID的集合反向查询用户
            List<UserPartner> list = userPartnerDao.listByPartnerIds(map);
            if (list.size() > 0) {
                Set<String> upSet = new HashSet<String>();
                for (UserPartner up : list) {
                    upSet.add(up.getUserId());
                }
                if (activeUser.getUserId() != 1) { //如果用户不是admin,则admin不显示
                    upSet.remove("1");
                }
                Iterator it = upSet.iterator();
                String userIds = "";

                SysUser sysUser = null;
                String userId = "";
                while (it.hasNext()) {
                    userId = it.next().toString();
                    if (activeUser.getPartner().getId() != 1) { //当前登录用户是否是【jtd公司】
                        sysUser = sysUserDao.getById(Long.parseLong(userId));
                        if (sysUser != null) {
                            // 不显示【jtd公司】且不显示上级广告主的用户,只能显示本级及其子集的用户
                            if (sysUser.getPartnerId() != 1 && collection.contains(sysUser.getPartnerId().toString())) {
                                userIds += userId + ",";
                            }
                        }
                    } else {
                        userIds += userId + ",";
                    }
                }

                if (!StringUtils.isEmpty(userIds)) {
                    userIds = userIds.substring(0, userIds.length() - 1);
                    paraMap.put("userIds", userIds);
                }

            }
        }

		Pagination<Map<String, Object>> page = this.findMapPageBy(paraMap, pageNo, pageSize);

		model.addAttribute("page", page);
	}

}
