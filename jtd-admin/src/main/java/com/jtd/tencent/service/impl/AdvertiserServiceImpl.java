package com.jtd.tencent.service.impl;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.jtd.tencent.service.AdvertiserService;

@Service
public class AdvertiserServiceImpl implements AdvertiserService{

	@Override
	public Object addAdvertiser(HashMap<String, Object> params) 
	{
		String corporation_name = params.get("corporation_name").toString();             //企业名称
		String certification_image_id = params.get("certification_image_id").toString(); //营业执照/企业资质证明图片
		String website = params.get("website").toString();                               //推广站点地址
		String icp_image_id = params.get("icp_image_id").toString();                     //推广站点 ICP 备案图片
		Integer system_industry_id = (Integer)params.get("system_industry_id");          //至少需要细化到二级行业（最细有三级）
		String customized_industry = params.get("customized_industry").toString();       //调用方行业名称，请尽量细化
		String[] industry_qualification_image_id_list = (String[])params.get("industry_qualification_image_id_list");//行业资质图片 id 列表
		String[] ad_qualification_image_id_listad_qualification_image_id_list = (String[])params.get(""); //广告资质图片 id 列表
		String corporate_image_name = params.get("corporate_image_name").toString();     //企业形象名称
		String contact_person_telephone = params.get("contact_person_telephone").toString(); //联系人座机电话号码
		String contact_person_mobile = params.get("contact_person_mobile").toString();    //联系人手机号码
		String certification_number = params.get("certification_number").toString();      //企业营业执照注册号
		return null;
	}

	@Override
	public Object updateAdvertiser(HashMap<String, Object> params) {
		return null;
	}

	@Override
	public Object getAdvertiser(HashMap<String, Object> parmas) {
		return null;
	}

	@Override
	public Object transferFund() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getFunds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getFundStatementsDaily() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getFundStatementsDetailed() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getRealtimeCost() {
		// TODO Auto-generated method stub
		return null;
	}

}
