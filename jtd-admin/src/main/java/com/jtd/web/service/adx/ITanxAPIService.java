package com.jtd.web.service.adx;

import com.alibaba.fastjson.JSONObject;
import com.jtd.web.po.Ad;

/**
 * Created by duber on 2017/5/2.
 */
public interface ITanxAPIService {

    /**
     * 新增广告主数据到TanxADX
     * @param advertiserName
     * @param nickName
     * @param usertype
     * @return
     */
    public int addAdvertiser(String advertiserName,String nickName, int usertype);

    /**
     * 发送tanx创意审核请求
     * @param ad
     * @return
     */
    int syncCreative(Ad ad);

    /**
     * 查询创意
     * @param ad
     * @return
     */
    JSONObject queryCreative(Ad ad);


}
