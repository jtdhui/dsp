package com.jtd.web.service.admin;


import com.alibaba.fastjson.JSONArray;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by duber on 2017/6/19.
 */
public interface IAdminFinanceService {

	/**
	 * 查询明细
	 * 
	 * @param paraMap
	 * @return
	 */
	public List<Map<String, Object>> getListFinaceDetail(Map<String, Object> paraMap,HttpServletResponse response);
	
	/**
	 * 代理充值查询明细
	 * 
	 * @param paraMap
	 * @return
	 */
	public List<Map<String, Object>> getProxyFinaceDetailList(Map<String, Object> paraMap,HttpServletResponse response);

    public List<Map<String,Object>> listFinanceBy(Map<String, Object> paraMap);


    public Map<String, Object> getExpendList(Map<String, Object> paraMap);

    public List<Map<String, Object>> getProxyExpendList(Model model, Map<String, Object> paraMap);

    public Set<String> initTableColum(String startDate,String endDate);

    public List<Map<String,Object>> getBrokerageList(Map<String, Object> paraMap);

    public void changeStatus(Long id);

    public List<Map<String, Object>> downloadBrokerageReport(List<Map<String, Object>> resultList, HttpServletResponse response);

    public List<Map<String, Object>> downloadExpendReport(String fileName,JSONArray headerJson, List<Map<String, Object>> mapList, HttpServletResponse response);

    public void downloadFinanceReport(Map<String, Object> paraMap, HttpServletResponse response);

}
