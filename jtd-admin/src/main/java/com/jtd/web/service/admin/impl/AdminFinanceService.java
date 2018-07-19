package com.jtd.web.service.admin.impl;

import com.alibaba.fastjson.JSONArray;
import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.utils.DateUtil;
import com.jtd.utils.ExcelUtil;
import com.jtd.web.dao.IDspSalesProductFlowDao;
import com.jtd.web.dao.IFinanceDao;
import com.jtd.web.dao.IPardCountDao;
import com.jtd.web.dao.IPartnerDao;
import com.jtd.web.po.DspSalesProductFlow;
import com.jtd.web.po.Partner;
import com.jtd.web.po.count.Pard;
import com.jtd.web.service.admin.IAdminFinanceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by duber on 2017/6/19.
 */
@Service
public class AdminFinanceService implements IAdminFinanceService {

	@Autowired
	private IFinanceDao financeDao;

    @Autowired
    private IPardCountDao pardCountDao;

    @Autowired
    private IPartnerDao partnerDao;

    @Autowired
    private IDspSalesProductFlowDao salesProductFlowDao;


    private DecimalFormat currencyFormat = new DecimalFormat("0.00");
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public List<Map<String, Object>> getListFinaceDetail(
			Map<String, Object> paraMap,HttpServletResponse response) {

		CustomerContextHolder
				.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		if (paraMap == null) {
			return resultList;
		}

		resultList = financeDao.listFinaceDetailByMap(paraMap);
		if(response != null){
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			Map<String, Object> lastRow = null;
			for (Map<String, Object> map : resultList) {
				lastRow = new HashMap<String, Object>();
				lastRow.put("partnerId", map.get("partner_id").toString());
				lastRow.put("partnerName", map.get("partner_name"));
				Date tradeTime = (Date)map.get("trade_time");
				lastRow.put("tradeTime", sdf.format(tradeTime));
				double openAmount = (long) map.get("open_amount") / 100f;
				if(openAmount == 0.0){
					lastRow.put("openAmount", currencyFormat.format(0));
				}else{
					lastRow.put("openAmount", currencyFormat.format(openAmount));
				}
				double serviceAmount = (long) map.get("service_amount") / 100f;
				if(serviceAmount == 0.0){
					lastRow.put("serviceAmount", currencyFormat.format(0));
				}else{
					lastRow.put("serviceAmount", currencyFormat.format(serviceAmount));
				}
				double amount = (long) map.get("amount") / 100f;
				if(amount == 0){
					lastRow.put("amount", currencyFormat.format(0));
				}else{
					lastRow.put("amount", currencyFormat.format(amount));
				}
				double pregift = (long) map.get("pre_gift") / 100f;
				lastRow.put("countAmount", currencyFormat.format(openAmount + serviceAmount+amount));
				lastRow.put("pregift", currencyFormat.format(pregift));
				
				list.add(lastRow);
			}
			
			ExcelUtil.downloadExcel("finaceDetailData", list, response);
			return null ;
		} else {
			return resultList;
		}
		
	}

	@Override
	public List<Map<String, Object>> getProxyFinaceDetailList(
			Map<String, Object> paraMap,HttpServletResponse response) {

		CustomerContextHolder
				.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		if (paraMap == null) {
			return resultList;
		}
		List<Map<String, Object>>  result = financeDao.proxyFinaceDetailList(paraMap);

		//代理商的金额信息
		Map<String, Object> oneMap = null;
		for (Map<String, Object> map : result) {
			 oneMap = new HashMap<String, Object>();
			 oneMap.putAll(map);
			 oneMap.put("two_partnerName", "--");
			 oneMap.put("two_amount", 0l);
			 oneMap.put("two_acc_balance_result", 0l);
			 oneMap.put("type", map.get("type"));
			 if (paraMap.containsKey("partnerId2")) {
				 long partnerId2 = Long.parseLong(paraMap.get("partnerId2").toString());
				 if (partnerId2 == 0) {
					 resultList.add(oneMap);
				 } 
			}else {
				resultList.add(oneMap);
			}
		}

		Long partnerId = Long.parseLong((String) paraMap.get("partnerId")) ;
		Partner p = new Partner();
		p.setPid(partnerId);
		if (StringUtils.isEmpty(paraMap.get("partnerId2")) == false) {
			Long pId2 = Long.parseLong(paraMap.get("partnerId2").toString());
			if (pId2 == 0) {
				p.setId(null);
			} else {
				p.setId(pId2);
			}
		}
		//查询下级广告主
		List<Partner> listPartners = partnerDao.listBy(p);
		Partner proxyPartner = partnerDao.getById(partnerId);

		//下级广告主的金额信息
		for (Partner partner : listPartners) {
			Map<String, Object> pMap = new HashMap<String, Object>();
			pMap.put("partnerId", partner.getId());
			pMap.put("fromPartnerId", partner.getPid());
			pMap.put("startDate",paraMap.get("startDate"));
			pMap.put("endDate",paraMap.get("endDate"));
			if (StringUtils.isEmpty(paraMap.get("partnerId2")) == false) {
				Long partnerId1 = Long.parseLong((String) paraMap.get("partnerId2")) ;
				if (partnerId1 == 0 ){ 
					pMap.put("partnerId", partner.getId());
				}else{
					pMap.put("partnerId", paraMap.get("partnerId2"));
				}
			}else {
				pMap.put("partnerId", partner.getId());
			}
			//查询代理商为下级广告主充值流水
			result = financeDao.clawBackAndAmountBack(pMap);

			Map<String, Object> twoMap = null;
			for (Map<String, Object> map : result) {
				twoMap = new HashMap<String, Object>();
				twoMap.putAll(map);
				twoMap.put("two_partnerName",partner.getPartnerName());
				twoMap.put("partner_name",proxyPartner.getPartnerName());
				twoMap.put("trade_time",map.get("create_time"));
				twoMap.put("two_amount", map.get("amount"));
				twoMap.put("two_acc_balance_result", map.get("acc_balance_result"));

				//查询代理商充值退款后的余额acc_balance_result
				 String tradeId = (String) map.get("trade_id");
			     String accBalanceResult = financeDao.getAccBalanceResult(tradeId);
			     if (accBalanceResult != null) {
			    	 twoMap.put("acc_balance_result",Long.parseLong(accBalanceResult));
				}
			     twoMap.put("type", 2);
			     twoMap.put("pre_gift", 0l);
			     resultList.add(twoMap);
			}
		}
		
		if(response != null){
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			Map<String, Object> lastRow = null;
			for (Map<String, Object> map : resultList) {
				lastRow = new HashMap<String, Object>();
				lastRow.put("partnerName", map.get("partner_name").toString());
				Date tradeTime = (Date)map.get("trade_time");
				lastRow.put("tradeTime", sdf.format(tradeTime));
				String type = map.get("type").toString();
				double amount = 0.00;
				lastRow.put("tamount", currencyFormat.format(amount));
				lastRow.put("camount", currencyFormat.format(amount));
				if(type.equals("0")) {
					amount = (long) map.get("amount") / 100f;
					lastRow.put("camount", currencyFormat.format(amount));
				}else if (type.equals("1")) {
					amount = (long) map.get("amount") / 100f;
					lastRow.put("tamount", currencyFormat.format(amount));
				}
				double preGift = (long) map.get("pre_gift") / 100f;
				lastRow.put("pregift", preGift);  
				double accBalanceResult = (long) map.get("acc_balance_result") / 100f;
				lastRow.put("accBalanceResult", accBalanceResult);
				
				lastRow.put("two_partnerName", map.get("two_partnerName").toString());
				double twoAmount = (long) map.get("two_amount") / 100f;
				if (twoAmount>= 0.0) {
					lastRow.put("two_camount", currencyFormat.format(twoAmount));
					lastRow.put("two_tamount", currencyFormat.format(0));
				}else {
					lastRow.put("two_camount", currencyFormat.format(0));
					lastRow.put("two_tamount", currencyFormat.format(-twoAmount));
				}
				String twoPartnerName = map.get("two_partnerName").toString();
				double two_acc_balance_result = (long) map.get("two_acc_balance_result") / 100f;
				if (twoPartnerName == "--") {  
					lastRow.put("two_acc_balance_result", 0.00);
				}else {
					lastRow.put("two_acc_balance_result", currencyFormat.format(two_acc_balance_result));
				}
				lastRow.put("remark", map.get("remark"));
				list.add(lastRow);
			}
			ExcelUtil.downloadExcel("proxyFinaceDetailData", list, response);
			return null ;
		} else{
			return resultList ;
		}
	}

	@Override
	public List<Map<String, Object>> listFinanceBy(Map<String, Object> paraMap) {

		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
        List<Map<String, Object>> list = financeDao.listFinanceBy(paraMap);
        Map<String, Object> countMap = new HashMap<String,Object>();
        for(Map<String,Object> map : list){

            CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
            Long partnerId = Long.parseLong(map.get("partner_id").toString());
            Partner po = partnerDao.getById(partnerId);
            String partnerIdArray = partnerId+"";
            long accBalance = Long.parseLong(map.get("acc_balance").toString());
            if(po.getPartnerType().longValue() == 0) { //代理广告主
                Partner partner = new Partner();
                partner.setPid(partnerId);
                List<Partner> partnerList = partnerDao.listBy(partner);

                for(Partner pa: partnerList) {
                    accBalance += pa.getAccBalance();
                    partnerIdArray += "," + pa.getId();
                }
            }
            if(partnerIdArray.length()>0) {
                paraMap.put("partnerIdArray", partnerIdArray);
            }
            map.put("acc_balance",accBalance);
            CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_COUNT);
            countMap = pardCountDao.getSum(paraMap);
            if(countMap == null ){
                map.put("total_expend","0.00");
            }else {
                map.put("total_expend",countMap.get("expend_sum_yuan").toString());
            }
        }
		return list;
	}

    @Override
    public Map<String, Object> getExpendList(Map<String, Object> paraMap) {

        CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_COUNT);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Map<String,Object> parMap = new HashMap<String, Object>();
        parMap.put("partnerId", paraMap.get("partnerId").toString());
        try {
            parMap.put("startDay", DateUtil.getDateInt(df.parse(paraMap.get("startDay").toString())));
            parMap.put("endDay", DateUtil.getDateInt(df.parse(paraMap.get("endDay").toString())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<Pard> pards = pardCountDao.listByMap(parMap);

        Map<String,Object> monthMap = new HashMap<String, Object>();
        for( Pard pard : pards ){
            String date = pard.getDate().toString();
            String month = date.substring(0,6);

            if(monthMap.containsKey(month)){
                int expend = Integer.parseInt(monthMap.get(month).toString());
                monthMap.put(month,pard.getExpend()+expend);
            }else {
                monthMap.put(month,pard.getExpend());
            }
        }
        return monthMap;
    }

    @Override
    public List<Map<String, Object>> getProxyExpendList(Model model, Map<String, Object> paraMap) {

        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        Map<String, Object> resultMap = getExpendList(paraMap);
        CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
        Long partnerId = Long.parseLong(paraMap.get("partnerId").toString());
        Partner po = partnerDao.getById(partnerId);
        resultMap.put("partnerName",po.getPartnerName());
        resultList.add(resultMap);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pid",0);
        map.put("id",partnerId);
        List<Partner> partnerList = partnerDao.listChildPartnerByMap(map);
        for(Partner partner:partnerList){
            if(!partner.getId().equals(partnerId)){
                paraMap.put("partnerId",partner.getId());
                resultMap = getExpendList(paraMap);
                resultMap.put("partnerName",partner.getPartnerName());
                resultList.add(resultMap);
            }
        }
        return resultList;
    }

    @Override
    public Set<String> initTableColum(String startDate,String endDate) {

        Set<String> retSet = new TreeSet<String>();

        Date d1 = null;//定义起始日期
        Date d2 = null;//定义结束日期
        try {
            d1 = new SimpleDateFormat("yyyy-MM").parse(startDate);
            d2 = new SimpleDateFormat("yyyy-MM").parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar dd = Calendar.getInstance();//定义日期实例
        dd.setTime(d1);//设置日期起始时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        while(dd.getTime().before(d2)){//判断是否到结束日期
            String str = sdf.format(dd.getTime());
            retSet.add(str);
            dd.add(Calendar.MONTH, 1);//进行当前日期月份加1
        }
        retSet.add(sdf.format(dd.getTime()));
        return retSet;
    }

    @Override
    public List<Map<String, Object>> getBrokerageList(Map<String, Object> paraMap) {

        CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
        DspSalesProductFlow flow = new DspSalesProductFlow();
        flow.setPartnerId(Long.parseLong(paraMap.get("partnerId").toString()));
        List<DspSalesProductFlow> list = salesProductFlowDao.listBy(flow);

        Partner partner = partnerDao.getById(Long.parseLong(paraMap.get("partnerId").toString()));

        CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_COUNT);
        Map<String,Object> parMap = new HashMap<String, Object>();
        parMap.put("partnerId", paraMap.get("partnerId").toString());

        Map<String, Object> sumMap = pardCountDao.getSum(parMap);
        int expend = Integer.parseInt(sumMap.get("expend_sum").toString());

        List<Map<String,Object>> returnList = new ArrayList<Map<String,Object>>();
        Map<String,Object> pMap = null;
        DspSalesProductFlow po = null;
        for(int i=list.size()-1;i>=0;i--){
            po = list.get(i);
            pMap = new HashMap<String, Object>();
            pMap.put("Id",po.getId());
            pMap.put("partnerId",po.getPartnerId());
            int pExpend = po.getDeliverAmount();
            pMap.put("amount",pExpend);
            pMap.put("status",po.getStatus());
            pMap.put("partnerName",partner.getPartnerName());
            pMap.put("date",po.getCreateTime());

            if(expend>pExpend){
                pMap.put("expend",pExpend);
                expend = expend -pExpend;
            }else {
                pMap.put("expend",expend);
                expend = 0;
            }

            int status = 0;
            if( po.getStatus().intValue() == 0 ){
                int exp = Integer.parseInt(pMap.get("expend").toString());
                if(exp == 0){
                    status = 0; //待返
                }else {
                    status = 1; //可返
                }
            }else {
                status = 2; //已返
            }

            if(StringUtils.isEmpty(paraMap.get("backStatus")) == false){

                int backStatus = Integer.parseInt(paraMap.get("backStatus").toString());
                if( status == backStatus ){
                    returnList.add(pMap);
                }
            }else {
                returnList.add(pMap);
            }
        }

        return returnList;
    }

    @Override
    public void changeStatus(Long id) {
        DspSalesProductFlow flow = new DspSalesProductFlow();
        flow.setId(id);
        flow.setStatus(1);
        salesProductFlowDao.update(flow);
    }

    @Override
    public List<Map<String, Object>> downloadBrokerageReport(List<Map<String, Object>> resultList, HttpServletResponse response) {

        List<Map<String, Object>>  returnList = new ArrayList<Map<String, Object>>();

        Map<String, Object> lastSumRow = null;
        for(Map<String, Object> map : resultList){
            lastSumRow = new HashMap<String, Object>();
            lastSumRow.put("Id", map.get("Id").toString());
            Date date = (Date)map.get("date");
            lastSumRow.put("date", sdf.format(date));
            lastSumRow.put("partnerName", map.get("partnerName").toString());
            double amount = (int) map.get("amount") / 100f;
            lastSumRow.put("amount", currencyFormat.format(amount));
            String status = map.get("status").toString();
            long expend = 0l;
            if(StringUtils.isEmpty(map.get("expend")) == false){
                expend = Long.parseLong(map.get("expend").toString());
            }

            if("0".equals(status)){
                if(expend > 0L){
                    status = "可返佣金";
                }else {
                    status = "待返佣金";
                }
            }else {
                status = "已返佣金";
            }
            lastSumRow.put("status", status);

            returnList.add(lastSumRow);
        }

        if(response != null){
            ExcelUtil.downloadExcel("brokerageReportData", returnList, response);
            return null ;
        }
        else{
            return returnList ;
        }
    }

    @Override
    public void downloadFinanceReport(Map<String, Object> paraMap, HttpServletResponse response) {

        List<Map<String, Object>>  returnList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> list =  listFinanceBy(paraMap);
        Map<String, Object> lastSumRow = null;

        for(Map<String, Object> map : list){
            lastSumRow = new HashMap<String, Object>();
            lastSumRow.put("id", map.get("id").toString());
            if(map.containsKey("user_name")) {
                lastSumRow.put("user_name", map.get("user_name").toString());
            }

            String partner_type_name= "";
            if(!StringUtils.isEmpty(map.get("partner_type"))) {
                String partner_type = map.get("partner_type").toString();
                if("0".equals(partner_type)){
                    partner_type_name= "新米迪-代理";
                }else {
                    if (StringUtils.isEmpty(map.get("boss_partner_code"))) {
                        partner_type_name = "新米迪-直客";
                    } else {
                        partner_type_name = "【jtd公司】-直客";
                    }
                }
                lastSumRow.put("partner_type_name", partner_type_name);
            }

            if(map.containsKey("region")) {
                lastSumRow.put("region", map.get("region").toString());
            }
            if(map.containsKey("company_name")) {
                lastSumRow.put("company_name", map.get("company_name").toString());
            }
            if(map.containsKey("sales_name")) {
                lastSumRow.put("sales_name", map.get("sales_name").toString());
            }
            if(map.containsKey("partner_name")) {
                lastSumRow.put("partner_name", map.get("partner_name").toString());
            }

            double amount = Integer.parseInt(map.get("amount").toString()) / 100f;
            lastSumRow.put("amount", currencyFormat.format(amount));

            double pre_gift = Integer.parseInt(map.get("pre_gift").toString())  / 100f;
            lastSumRow.put("pre_gift", currencyFormat.format(pre_gift));

            double open_amount = Integer.parseInt(map.get("open_amount").toString())  / 100f;
            lastSumRow.put("open_amount", currencyFormat.format(open_amount));

            double service_amount = Integer.parseInt(map.get("service_amount").toString()) / 100f;
            lastSumRow.put("service_amount", currencyFormat.format(service_amount));

            lastSumRow.put("total_amount", currencyFormat.format(amount + open_amount + service_amount));
            lastSumRow.put("account_amount", currencyFormat.format(amount + pre_gift));

            double acc_balance = Integer.parseInt(map.get("acc_balance").toString()) / 100f;
            lastSumRow.put("acc_balance", currencyFormat.format(acc_balance));

            lastSumRow.put("total_expend", map.get("total_expend").toString());
            if(StringUtils.isEmpty(map.get("remark")) == false){
                lastSumRow.put("remark", map.get("remark").toString());
            }else {
                lastSumRow.put("remark", "");
            }

            returnList.add(lastSumRow);
        }

        ExcelUtil.downloadExcel("financeReportData", returnList, response);
    }

    @Override
    public List<Map<String, Object>> downloadExpendReport(String fileName,JSONArray json, List<Map<String, Object>> mapList, HttpServletResponse response) {

        JSONArray headerJson = new JSONArray();
        headerJson.add("广告主名称");
        headerJson.addAll(json);

        JSONArray dataJson = new JSONArray();
        dataJson.add("partnerName");
        dataJson.addAll(json);

        if (mapList != null) {
            for (Map<String, Object> dataMap : mapList) {
                String[] dataRowCols = new String[headerJson.size()];
                for (int i = 0; i < headerJson.size(); i++) {
                    String dataKey = dataJson.getString(i);
                    String value = dataMap.get(dataKey) != null ? dataMap.get(dataKey).toString() : "0";
                    if(dataKey.startsWith("partnerName")){
                        dataMap.put(dataKey, value);
                    }else {
                        double amount = Integer.parseInt(value) / 100f;
                        dataMap.put(dataKey, currencyFormat.format(amount));
                    }
                }
            }
        }

        if(response != null){
            ExcelUtil.downloadExcel(fileName, headerJson, dataJson, mapList, response);
            return null ;
        }
        else{
            return mapList ;
        }
    }


}
