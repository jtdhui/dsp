package com.jtd.web.service.adx.tanx;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jtd.utils.MD5Util;
import com.jtd.web.constants.CatgSerial;
import com.jtd.web.constants.Constants;
import com.jtd.web.dao.IAdCategoryDao;
import com.jtd.web.dao.IAdDao;
import com.jtd.web.dao.ICreativeDao;
import com.jtd.web.po.Ad;
import com.jtd.web.po.AdCategory;
import com.jtd.web.po.Creative;
import com.jtd.web.service.adx.ITanxAPIService;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.alibaba.fastjson.JSON.parseObject;
import static com.jtd.utils.MD5Util.md5Signature;
import static com.jtd.web.service.adx.tanx.TanxUtil.APPSEC;

/**
 * Created by duber on 2017/5/4.
 */
@Service
public class TanxAPIService implements ITanxAPIService {

    private Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private IAdDao adDao;

    @Autowired
    private ICreativeDao creativeDao;

    @Autowired
    private IAdCategoryDao adCategoryDao;

    /**
     * 新增广告主数据到TanxADX
     * @param advertiserName
     * @param nickName
     * @param usertype
     * @return
     */
    @Override
    public int addAdvertiser(String advertiserName,String nickName, int usertype) {

        if (TanxUtil.isIgnore) {
            return 1;
        }

        TreeMap<String, String> apiparamsMap = new TreeMap<String, String>();
        apiparamsMap.put("format", "json");
        apiparamsMap.put("method", "taobao.tanx.qualification.advertiser.add");
        apiparamsMap.put("sign_method", "md5");
        apiparamsMap.put("app_key", TanxUtil.APPKEY);
        apiparamsMap.put("v", "2.0");
        apiparamsMap.put("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        Map<String, Object> adverm = new HashMap<String, Object>();
        adverm.put("user_type", usertype); // 1淘系客户 2非淘系公司 3非淘系个人
        adverm.put("nick_name", nickName);
        adverm.put("advertiser_name", advertiserName);
        List<Map<String, Object>> l = new ArrayList<Map<String, Object>>();
        l.add(adverm);

        String advertisers = "";
        try {
            advertisers = URLEncoder.encode(JSON.toJSONString(l),"utf-8");
        }catch (UnsupportedEncodingException e){
            logger.error("advertisers编码转换异常!");
            return 1;
        }
        apiparamsMap.put("advertisers",JSON.toJSONString(l));

        apiparamsMap.put("member_id", String.valueOf(TanxUtil.DSPID));
        long t = System.currentTimeMillis() / 1000;
        apiparamsMap.put("token", MD5Util.MD5(TanxUtil.TOKEN + t));
        apiparamsMap.put("sign_time", String.valueOf(t));

        // 生成签名
        String sign = md5Signature(apiparamsMap, APPSEC);
        apiparamsMap.put("sign", sign);

        StringBuilder param = new StringBuilder();
        for (Iterator<Map.Entry<String, String>> it = apiparamsMap.entrySet().iterator(); it.hasNext();) {
            Map.Entry<String, String> e = it.next();
//            param.append("&").append(e.getKey()).append("=").append(e.getValue());
            try {
                param.append("&").append(e.getKey()).append("=").append(URLEncoder.encode(e.getValue(), "UTF-8"));
            } catch (UnsupportedEncodingException e1) {
                logger.error("advertisers编码转换异常!"+e.getValue());
                return 1;
            }
        }
        String result = null;
        System.out.println(TanxUtil.SERVER_URL+"?"+param.toString().substring(1));
        result = getResult(TanxUtil.SERVER_URL, param.toString().substring(1));
        System.out.println(result);
        logger.info("提交广告主[" + advertiserName + "]TANX响应: " + result);
        JSONObject o = parseObject(result);
        JSONObject rsp = o.getJSONObject("tanx_qualification_advertiser_add_response");
        if (rsp != null && rsp.getBooleanValue("is_success")) {
            return 0;
//            return rsp.getJSONObject("advertiser_list").getJSONArray("advertiser_dto").getJSONObject(0).getString("advertiser_id");
        } else {
            String rspString = (o.getJSONObject("error_response")).get("msg").toString();
            logger.error(rspString);
            return 1;
        }
    }

    @Override
    public int syncCreative(Ad ad) {
        if (TanxUtil.isIgnore) {//如是测试直接跳过不执行提交淘宝审核
            logger.info("广告adId:"+ad.getId()+" 进行了测试,不执行提交tanx...");
            return 1;
        }
        //1.主体
        TreeMap<String, String> mainBody = new TreeMap<String, String>();
        //member_id
        mainBody.put("member_id", String.valueOf(TanxUtil.DSPID));
        //token
        long t = System.currentTimeMillis() / 1000;
        mainBody.put("token", MD5Util.MD5(TanxUtil.TOKEN + t));
        //sign_time
        mainBody.put("sign_time", String.valueOf(t));
        mainBody.put("method", "taobao.tanx.audit.creative.add");
        mainBody.put("sign_method", "md5");
        mainBody.put("app_key", TanxUtil.APPKEY);
        mainBody.put("v", "2.0");
        mainBody.put("format", "json");
        //2.creative 创意内容
        TreeMap<String, String> creativeBody = new TreeMap<String, String>();
        //创意信息
        Creative creative = creativeDao.getById(ad.getCreativeId());
        if(null == creative){
            creative = new Creative();
        }
        //创意 id
        creativeBody.put("creative_id",String.valueOf(ad.getId()));
        String adboard_type = getAdboardType(ad);
        //广告类别列表
        creativeBody.put("adboard_type", adboard_type);

        Map<String,String> adboard_data = creativeadBoardData(ad);
        //创意代码
        creativeBody.put("adboard_data", JSON.toJSONString(adboard_data));
        //目标地址
        creativeBody.put("destination_url", ad.getLandingPage());
        //创意尺寸
        creativeBody.put("adboard_size", creative.getSize());

        mainBody.put("creative", JSON.toJSONString(creativeBody));

        mainBody.put("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        // 生成签名
        String sign = md5Signature(mainBody, APPSEC);
        mainBody.put("sign", sign);
        StringBuilder param = new StringBuilder();
        for (Iterator<Map.Entry<String, String>> it = mainBody.entrySet().iterator(); it.hasNext();) {
            Map.Entry<String, String> e = it.next();
            param.append("&").append(e.getKey()).append("=").append(e.getValue());
        }
        /*******提交审核************/
        String result = getResult(TanxUtil.SERVER_URL, param.toString().substring(1));
        if(StringUtils.isEmpty(result)){
            return 0;
        }
        //	log.info("提交广告主[" + adername + "]TANX响应: " + result);
        JSONObject o = parseObject(result);

        //	JSONObject rsp = o.getJSONObject("tanx_qualification_advertiser_add_response");
        JSONObject rsp = o.getJSONObject("tanx_audit_creative_add_response");
        //获取成功信息
        if(null == rsp){
            rsp = o.getJSONObject("error_response");
            logger.error("tanx返回结果code:"+rsp.getString("sub_code") +",信息:"+rsp.getString("sub_msg")+",  请求 request_id is :"+rsp.getString("request_id"));
            return 0;
        }

        logger.info("tanx返回结果code:"+rsp.getString("tanx_error_code") +",提交tanx成功!, 请求request_id is :"+rsp.getString("request_id"));
        //处理结果
        Boolean is_ok = rsp.getBoolean("is_ok");

        if(is_ok){
            return 1;
        }else {
            return 0;
        }
    }

    private Map<String,String> creativeadBoardData(Ad ad){
        TreeMap<String, String> creativeadBoardData = new TreeMap<String, String>();
        //创意代码数据
        TreeMap<String, String> boardData = new TreeMap<String, String>();
        if(StringUtils.isNotEmpty(ad.getClickUrl())){
            //点击的url
            boardData.put("clickUrl", ad.getClickUrl());
        }

        if(StringUtils.isNotEmpty(ad.getPhoneTitle())){
            //标题
            boardData.put("title", ad.getPhoneTitle());
        }
        if(StringUtils.isNotEmpty(ad.getDiscountPrice())){
            //折后价
            boardData.put("promoprice", ad.getDiscountPrice());
        }

        //销量
        boardData.put("sell", String.valueOf(ad.getSalesVolume()));
        if(StringUtils.isNotEmpty(ad.getPhonePropaganda())){
            //广告语
            boardData.put("adwords", ad.getPhonePropaganda());
        }
        //3. 创意代码
        creativeadBoardData.put("native", JSON.toJSONString(boardData));
        return creativeadBoardData;
    }

    /**
     * 获取类目
     * @param ad
     * @return
     */
    private String getAdboardType(Ad ad){
        String categoryId = Constants.BANK_STR;
        AdCategory ac = new AdCategory();
        ac.setCampaignId(ad.getCampaignId());
        ac.setCatgserial(CatgSerial.TANX.getCode());
        List<AdCategory>  categorys = adCategoryDao.findAdCategoryBy(ac);
        for(int i = 0; i < categorys.size(); i++){
            categoryId += categorys.get(i).getCatgId();
            if(i < categorys.size() -1){
                categoryId += Constants.COMMA_STR;
            }
        }
        return categoryId;
    }

    @Override
    public JSONObject queryCreative(Ad ad) {
        TreeMap<String, String> apiparamsMap = new TreeMap<String, String>();
        apiparamsMap.put("format", "json");
        apiparamsMap.put("method", "taobao.tanx.creative.get");
        apiparamsMap.put("sign_method", "md5");
        apiparamsMap.put("app_key", TanxUtil.APPKEY);
        apiparamsMap.put("v", "2.0");
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        apiparamsMap.put("timestamp", timestamp);

        long t = System.currentTimeMillis() / 1000;
        apiparamsMap.put("member_id", String.valueOf(TanxUtil.DSPID));
        apiparamsMap.put("token", MD5Util.MD5(TanxUtil.TOKEN + t));
        apiparamsMap.put("sign_time", String.valueOf(t));
        apiparamsMap.put("creative_id", ad.getId().toString());

        String sign = md5Signature(apiparamsMap, APPSEC);
        apiparamsMap.put("sign", sign);

        String httpParam = getHttpParam(apiparamsMap);
        String s = getResult(TanxUtil.SERVER_URL, httpParam);
        if (s != null) {
            try {
                JSONObject data = parseObject(s).getJSONObject("tanx_creative_get_response");
                if(data == null ){
                    data = parseObject(s).getJSONObject("error_response");
                    logger.error("Tanx创意查询结果返回:"+data.toJSONString());
                    return null;
                }
                logger.info("Tanx创意查询结果返回:"+data.toJSONString());
                if (data.getBooleanValue("is_ok")) {
                    return data.getJSONObject("result");
                }
            } catch (Exception e) {
                logger.error("Tanx读取创意审核状态失败！method=queryCreative");
            }
        }
        return null;
    }


    public JSONArray queryCreativeByPage(int page) {
        TreeMap<String, String> apiparamsMap = new TreeMap<String, String>();
        apiparamsMap.put("format", "json");
        apiparamsMap.put("method", "taobao.tanx.creatives.get");
        apiparamsMap.put("sign_method", "md5");
        apiparamsMap.put("app_key", TanxUtil.APPKEY);
        apiparamsMap.put("v", "2.0");
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        apiparamsMap.put("timestamp", timestamp);

        long t = System.currentTimeMillis() / 1000;
        apiparamsMap.put("member_id", String.valueOf(TanxUtil.DSPID));
        apiparamsMap.put("token", MD5Util.MD5(TanxUtil.TOKEN + t));
        apiparamsMap.put("sign_time", String.valueOf(t));
        apiparamsMap.put("status", "ALL");
        apiparamsMap.put("page", "" + page);
        String sign = md5Signature(apiparamsMap, APPSEC);
        apiparamsMap.put("sign", sign);

        String httpParam = getHttpParam(apiparamsMap);
        String s = getResult(TanxUtil.SERVER_URL, httpParam);
        if (s != null) {
            try {
                JSONObject data = parseObject(s).getJSONObject("tanx_creatives_get_response");
                if(data == null ){
                    data = parseObject(s).getJSONObject("error_response");
                    logger.info("Tanx创意查询结果返回:"+data.toJSONString());
                    return null;
                }
                logger.info("Tanx创意查询结果返回:"+data.toJSONString());
                if (data.getBooleanValue("is_ok")) {
                    return data.getJSONObject("results").getJSONArray("creative_dto");
                }
            } catch (Exception e) {
                logger.error("Tanx读取创意审核状态失败！method:queryCreativeByPage");
            }
        }
        return null;
    }

    /**
     * 资质图片上传接口
     * @param file_byte
     * @return
     */
    private JSONObject qualificationPictureUpload(byte[] file_byte) {
        TreeMap<String, String> apiparamsMap = new TreeMap<String, String>();
        apiparamsMap.put("format", "json");
        apiparamsMap.put("method", "taobao.tanx.qualification.picture.upload");
        apiparamsMap.put("sign_method", "md5");
        apiparamsMap.put("app_key", TanxUtil.APPKEY);
        apiparamsMap.put("v", "2.0");
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        apiparamsMap.put("timestamp", timestamp);

        long t = System.currentTimeMillis() / 1000;
        apiparamsMap.put("member_id", String.valueOf(TanxUtil.DSPID));
        apiparamsMap.put("token", MD5Util.MD5(TanxUtil.TOKEN + t));
        apiparamsMap.put("sign_time", String.valueOf(t));
        apiparamsMap.put("file_byte", MD5Util.byte2hex(file_byte));

        String sign = md5Signature(apiparamsMap, APPSEC);
        apiparamsMap.put("sign", sign);
        String httpParam = getHttpParam(apiparamsMap);
        String s = getResult(TanxUtil.SERVER_URL, httpParam);
        if (s != null) {
            try {
                JSONObject data = parseObject(s).getJSONObject("tanx_qualification_picture_get_response");
                if(data == null ){
                    data = parseObject(s).getJSONObject("error_response");
                    logger.error("Tanx资质图片上传结果返回:"+data.toJSONString());
                    return null;
                }
                logger.info("Tanx资质图片上传结果返回:"+data.toJSONString());
                if (data.getBooleanValue("is_ok")) {
                    return data.getJSONObject("result");
                }
            } catch (Exception e) {
                logger.error("Tanx读取资质图片上传结果返失败！method=qualificationPictureUpload");
            }
        }
        return null;
    }

    private String getHttpParam(Map<String, String> data) {
        if (data == null || data.isEmpty())
            return "";

        StringBuilder param = new StringBuilder();
        for (Iterator<Map.Entry<String, String>> it = data.entrySet().iterator(); it.hasNext();) {
            Map.Entry<String, String> e = it.next();
            param.append("&").append(e.getKey()).append("=").append(e.getValue());
        }
        String s = param.toString();
        s = s.substring(1);
        return s;
    }

    /** 连接到TOP服务器并获取数据 */
    public static String getResult(String urlStr, String content) {

        if (TanxUtil.isIgnore) {
            return null;
        }

        URL url = null;
        HttpURLConnection connection = null;

        try {
            url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.connect();

            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.write(content.getBytes("utf-8"));
            out.flush();
            out.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            reader.close();
            return buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }

    public static String testUserGet() {
        TreeMap<String, String> apiparamsMap = new TreeMap<String, String>();
        apiparamsMap.put("format", "json");
        apiparamsMap.put("method", "taobao.tanx.creative.get");
        apiparamsMap.put("sign_method", "md5");
        apiparamsMap.put("app_key", TanxUtil.APPKEY);
        apiparamsMap.put("v", "2.0");
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        apiparamsMap.put("timestamp", timestamp);
        long t = System.currentTimeMillis() / 1000;
        apiparamsMap.put("member_id", String.valueOf(TanxUtil.DSPID));
        apiparamsMap.put("token", MD5Util.MD5(TanxUtil.TOKEN + t));
        apiparamsMap.put("sign_time", String.valueOf(t));
        apiparamsMap.put("creative_id", "123456");		//创意ID

        // 生成签名
        String sign = md5Signature(apiparamsMap, TanxUtil.APPSEC);
        apiparamsMap.put("sign", sign);
        StringBuilder param = new StringBuilder();
        try {
            for (Iterator<Map.Entry<String, String>> it = apiparamsMap.entrySet().iterator(); it.hasNext();) {
                Map.Entry<String, String> e = it.next();
                param.append("&").append(e.getKey()).append("=").append(URLEncoder.encode(e.getValue(), "UTF-8"));
            }
        }catch (UnsupportedEncodingException e){
           e.printStackTrace();
        }
        System.out.println(TanxUtil.SERVER_URL+"?"+param.toString().substring(1));
        return param.toString().substring(1);
    }

    public static void main(String[] args) {
        TanxAPIService service = new TanxAPIService();
        service.addAdvertiser("testName","testNickName",1);
//        String result = getResult(TanxUtil.SERVER_URL, testUserGet());
//        System.out.print(result);

    }
}
